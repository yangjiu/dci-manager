package com.jdsu.drivetest.dcimanager;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.jdsu.drivetest.dcimanager.api.EventConstants;
import com.jdsu.drivetest.dcimanager.api.IDCIManager;
import com.jdsu.drivetest.dcimanager.api.IEventListener;
import com.jdsu.drivetest.dcimanager.api.ILogRecordListener;
import com.jdsu.drivetest.dcimanager.api.IPacketResponseListener;
import com.jdsu.drivetest.dcimanager.api.LogRecordConstants;
import com.jdsu.drivetest.dcimanager.api.RemoteResponse;
import com.jdsu.drivetest.diag.CLibrary;
import com.jdsu.drivetest.diag.DiagLibrary;
import com.jdsu.drivetest.diag.sigaction;
import com.jdsu.drivetest.diag.siginfo;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

public class DiagService extends Service {

    private static final String TAG = DiagService.class.getName();
    private static long GPS_ZERO_TIME_IN_MILLIS;
    private DiagLibrary.diag_register_dci_stream_func_ptr_logs_callback logHandler;
    private DiagLibrary.diag_register_dci_stream_func_ptr_events_callback eventHandler;
    private DiagLibrary.diag_send_dci_async_req_func_ptr_callback responseHandler;
    //private sigaction connectionNotifyAction;
    private RemoteCallbackList<ILogRecordListener> logRecordListenerRemoteCallbackList;
    private RemoteCallbackList<IEventListener> eventListenerRemoteCallbackList;
    private RemoteCallbackList<IPacketResponseListener> packetResponseListenerRemoteCallbackList;
    private IBinder diagBinder;
    private Set<Character> activeLogTypes;
    private Set<Integer> activeEventIds;

    static {
        Calendar gpsZeroCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        gpsZeroCalendar.set(1980, Calendar.JANUARY, 6, 0, 0, 0);
        GPS_ZERO_TIME_IN_MILLIS = gpsZeroCalendar.getTimeInMillis();
    }

    private IntBuffer client_id;

    public DiagService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return diagBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        diagBinder = new DiagBinder();
        activeLogTypes = new HashSet<>();
        activeEventIds = new HashSet<>();
        logRecordListenerRemoteCallbackList = new DiagStreamRemoteCallbackList<>();
        eventListenerRemoteCallbackList = new DiagStreamRemoteCallbackList<>();
        packetResponseListenerRemoteCallbackList = new RemoteCallbackList<>();
        //connectionNotifyAction = new sigaction(new sigaction._u_union(new SigActionHandler()), new NativeLong(0), new NativeLong(CLibrary.SA_SIGINFO), null);
        //CLibrary.INSTANCE.sigaction(CLibrary.SIGCONT, connectionNotifyAction, connectionNotifyAction);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DiagLibrary.INSTANCE.diag_disable_all_logs(client_id.get(0));
        int result = DiagLibrary.INSTANCE.diag_release_dci_client(client_id);
        Log.i(TAG, "release DCI client result code " + result);
        result = DiagLibrary.INSTANCE.Diag_LSM_DeInit();
        Log.i(TAG, "LSM deinitialization result code " + result);
    }

    private synchronized RemoteResponse initialize() {
        int result = DiagLibrary.INSTANCE.Diag_LSM_Init(ByteBuffer.allocate(0));
        Log.i(TAG, "LSM initialization result code " + result);
        RemoteResponse response = new RemoteResponse();

        ShortBuffer list = ShortBuffer.allocate(1);
        result = DiagLibrary.INSTANCE.diag_get_dci_support_list(list);
        if (result != DiagLibrary.diag_dci_error_type_enum.DIAG_DCI_NO_ERROR) {
            Log.e(TAG, "failed to get supported list, result code " + result);
            response.setResponse(RemoteResponse.Response.valueOf(result));
            return response;
        }
        if ((list.get(0) & DiagLibrary.DIAG_CON_MPSS) != DiagLibrary.DIAG_CON_MPSS) {
            Log.e(TAG, "MPSS is down");
            response.setResponse(RemoteResponse.Response.MPSS_DOWN);
            return response;
        }
        client_id = IntBuffer.allocate(1);
        ShortBuffer peripherals = ShortBuffer.wrap(new short[]{(short) DiagLibrary.DIAG_CON_MPSS});
        result = DiagLibrary.INSTANCE.diag_register_dci_client(client_id, peripherals, 0, new IntByReference(CLibrary.SIGCONT).getPointer());
        if (result != DiagLibrary.diag_dci_error_type_enum.DIAG_DCI_NO_ERROR) {
            Log.e(TAG, "Failed to register DCI client result code " + result);
            response.setResponse(RemoteResponse.Response.valueOf(result));
            return response;
        }
        Log.i(TAG, "Register DCI client successfully, client id " + client_id.get(0));

        logHandler = new LogRecordHanlder();
        eventHandler = new EventHandler();
        responseHandler = new PacketResponseHandler();

        result = DiagLibrary.INSTANCE.diag_register_dci_stream(logHandler, eventHandler);
        if (result != DiagLibrary.diag_dci_error_type_enum.DIAG_DCI_NO_ERROR) {
            Log.e(TAG, "failed to register dci streams result code " + result);
            response.setResponse(RemoteResponse.Response.valueOf(result));
            return response;
        }

        response.setResponse(RemoteResponse.Response.DIAG_DCI_SUCCESS);
        return response;
    }

    private long convertModemTimestampToEpoch(long modemTimestamp) {
        long counter = modemTimestamp >>> 16;
        double millisFromGPSZero = counter * 1.25d;
        double logTimestampInMillis = GPS_ZERO_TIME_IN_MILLIS + millisFromGPSZero;
        return (long) logTimestampInMillis;
    }

    private synchronized RemoteResponse adjustActiveLogTypes() {
        Set<Character> currentLogTypes = getCurrentLogTypes();
        Set<Character> newLogTypes = new HashSet<>(currentLogTypes);
        newLogTypes.removeAll(activeLogTypes);
        int code = DiagLibrary.diag_dci_error_type_enum.DIAG_DCI_NO_ERROR;
        RemoteResponse response = new RemoteResponse();
        if (newLogTypes.size() != 0) {
            ShortBuffer logTypesParam = ShortBuffer.allocate(newLogTypes.size());
            for (char c : newLogTypes) {
                logTypesParam.put((short) c);
            }
            logTypesParam.rewind();
            code = DiagLibrary.INSTANCE.diag_log_stream_config(client_id.get(0), DiagLibrary.ENABLE, logTypesParam, logTypesParam.capacity());
            if (code != DiagLibrary.diag_dci_error_type_enum.DIAG_DCI_NO_ERROR) {
                response.setResponse(RemoteResponse.Response.valueOf(code));
                return response;
            }
            activeLogTypes.addAll(newLogTypes);
        }

        Set<Character> deprecatedLogTypes = new HashSet<>(activeLogTypes);
        deprecatedLogTypes.removeAll(currentLogTypes);
        if (deprecatedLogTypes.size() != 0) {
            ShortBuffer logTypesParam = ShortBuffer.allocate(deprecatedLogTypes.size());
            for (char c : deprecatedLogTypes) {
                logTypesParam.put((short) c);
            }
            logTypesParam.rewind();
            code = DiagLibrary.INSTANCE.diag_log_stream_config(client_id.get(0), DiagLibrary.DISABLE, logTypesParam, logTypesParam.capacity());
            if (code == DiagLibrary.diag_dci_error_type_enum.DIAG_DCI_NO_ERROR) {
                activeLogTypes.removeAll(deprecatedLogTypes);
            }
        }
        response.setResponse(RemoteResponse.Response.valueOf(code));
        return response;
    }

    private Set<Character> getCurrentLogTypes() {
        Set<Character> logTypes = new HashSet<>();
        int i = logRecordListenerRemoteCallbackList.beginBroadcast();
        while (i > 0) {
            i--;
            char[] types = (char[]) logRecordListenerRemoteCallbackList.getBroadcastCookie(i);
            if (types != null) {
                for (char c : types) {
                    logTypes.add(c);
                }
            }
        }
        logRecordListenerRemoteCallbackList.finishBroadcast();
        return logTypes;
    }

    private synchronized RemoteResponse adjustActiveEventIds() {
        Set<Integer> currentEventIds = getCurrentEventIds();
        Set<Integer> newEventIds = new HashSet<>(currentEventIds);
        newEventIds.removeAll(activeEventIds);
        int code = DiagLibrary.diag_dci_error_type_enum.DIAG_DCI_NO_ERROR;
        RemoteResponse response = new RemoteResponse();
        if (newEventIds.size() != 0) {
            IntBuffer eventIdsParam = IntBuffer.allocate(newEventIds.size());
            for (int i : newEventIds) {
                eventIdsParam.put(i);
            }
            eventIdsParam.rewind();
            code = DiagLibrary.INSTANCE.diag_event_stream_config(client_id.get(0), DiagLibrary.ENABLE, eventIdsParam, eventIdsParam.capacity());
            if (code != DiagLibrary.diag_dci_error_type_enum.DIAG_DCI_NO_ERROR) {
                response.setResponse(RemoteResponse.Response.valueOf(code));
                return response;
            }
            activeEventIds.addAll(newEventIds);
        }

        Set<Integer> deprecatedEventIds = new HashSet<>(activeEventIds);
        deprecatedEventIds.removeAll(currentEventIds);
        if (deprecatedEventIds.size() != 0) {
            IntBuffer eventIdsParam = IntBuffer.allocate(deprecatedEventIds.size());
            for (int i : deprecatedEventIds) {
                eventIdsParam.put(i);
            }
            eventIdsParam.rewind();
            code = DiagLibrary.INSTANCE.diag_event_stream_config(client_id.get(0), DiagLibrary.DISABLE, eventIdsParam, eventIdsParam.capacity());
            if (code == DiagLibrary.diag_dci_error_type_enum.DIAG_DCI_NO_ERROR) {
                activeEventIds.removeAll(deprecatedEventIds);
            }
        }
        response.setResponse(RemoteResponse.Response.valueOf(code));
        return response;
    }

    private Set<Integer> getCurrentEventIds() {
        Set<Integer> eventIds = new HashSet<>();
        int i = eventListenerRemoteCallbackList.beginBroadcast();
        while (i > 0) {
            i--;
            int[] ids = (int[]) eventListenerRemoteCallbackList.getBroadcastCookie(i);
            for (int id : ids) {
                eventIds.add(id);
            }
        }
        eventListenerRemoteCallbackList.finishBroadcast();
        return eventIds;
    }

    private synchronized RemoteResponse onSendRequestAsync(byte[] request, int uid) {
        //check the version of the modem software
        ByteBuffer req = ByteBuffer.wrap(request);
        ByteBuffer rsp = ByteBuffer.allocate(100);
        int code = DiagLibrary.INSTANCE.diag_send_dci_async_req(client_id.get(0), req, req.capacity(), rsp, 100, responseHandler, new IntByReference(uid).getPointer());
        RemoteResponse response = new RemoteResponse();
        response.setResponse(RemoteResponse.Response.valueOf(code));
        return response;
    }

    private class DiagBinder extends IDCIManager.Stub {

        @Override
        public RemoteResponse init() throws RemoteException {
            return initialize();
        }

        @Override
        public RemoteResponse listenLogRecord(ILogRecordListener listener, char[] log_types, int flag) throws RemoteException {
            logRecordListenerRemoteCallbackList.unregister(listener);
            if (flag != LogRecordConstants.LOG_RECORD_NONE) {
                Arrays.sort(log_types);
                logRecordListenerRemoteCallbackList.register(listener, log_types);
            }
            return adjustActiveLogTypes();
        }

        @Override
        public RemoteResponse listenEvent(IEventListener listener, int[] event_ids, int flag) throws RemoteException {
            eventListenerRemoteCallbackList.unregister(listener);
            if (flag != EventConstants.EVENT_NONE) {
                Arrays.sort(event_ids);
                eventListenerRemoteCallbackList.register(listener, event_ids);
            }
            return adjustActiveEventIds();
        }

        @Override
        public void registerPacketResponseListener(IPacketResponseListener listener) throws RemoteException {
            packetResponseListenerRemoteCallbackList.register(listener, Binder.getCallingUid());
        }

        @Override
        public RemoteResponse sendRequestAsync(byte[] request) throws RemoteException {
            return onSendRequestAsync(request, Binder.getCallingUid());
        }

        @Override
        public void unregisterPacketResponseListener(IPacketResponseListener listener) throws RemoteException {
            packetResponseListenerRemoteCallbackList.unregister(listener);
        }
    }

    private class LogRecordHanlder implements DiagLibrary.diag_register_dci_stream_func_ptr_logs_callback {

        @Override
        public void apply(Pointer ptr, int len) {
            short logcodeType = ptr.getShort(2);
            long dmssTimestamp = ptr.getLong(4);
            long unixTimestamp = convertModemTimestampToEpoch(dmssTimestamp);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
            String birthday = dateFormat.format(new Date(unixTimestamp));
            Log.i(TAG, "received log type 0x" + Integer.toHexString(logcodeType & 0xFFFF) + " born " + birthday + " " + Arrays.toString(ptr.getByteArray(0, len)));
            int i = logRecordListenerRemoteCallbackList.beginBroadcast();
            while (i > 0) {
                i--;
                char[] logTypes = (char[]) logRecordListenerRemoteCallbackList.getBroadcastCookie(i);
                if (Arrays.binarySearch(logTypes, (char) logcodeType) >= 0) {
                    try {
                        logRecordListenerRemoteCallbackList.getBroadcastItem(i).onReceived((char) logcodeType, unixTimestamp, ptr.getByteArray(12, len - 12));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
            logRecordListenerRemoteCallbackList.finishBroadcast();
        }

    }

    private class EventHandler implements DiagLibrary.diag_register_dci_stream_func_ptr_events_callback {

        @Override
        public void apply(Pointer ptr, int len) {
            int id = ptr.getInt(0);
            int i = eventListenerRemoteCallbackList.beginBroadcast();
            while (i > 0) {
                i--;
                int[] eventIds = (int[]) eventListenerRemoteCallbackList.getBroadcastCookie(i);
                if (Arrays.binarySearch(eventIds, id) >= 0) {
                    try {
                        eventListenerRemoteCallbackList.getBroadcastItem(i).onReceived(id, ptr.getByteArray(4, len - 4));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
            eventListenerRemoteCallbackList.finishBroadcast();
        }
    }

    private class PacketResponseHandler implements DiagLibrary.diag_send_dci_async_req_func_ptr_callback {

        @Override
        public void apply(Pointer ptr, int len, Pointer data_ptr) {
            int uid = data_ptr.getInt(0);
            Log.i(TAG, "received response packet " + Arrays.toString(ptr.getByteArray(0, len)));
            Log.i(TAG, "dispatch to client app uid = " + uid);
            int i = packetResponseListenerRemoteCallbackList.beginBroadcast();
            while (i > 0) {
                i--;
                if ((int) packetResponseListenerRemoteCallbackList.getBroadcastCookie(i) == uid) {
                    try {
                        packetResponseListenerRemoteCallbackList.getBroadcastItem(i).onReceived(ptr.getByte(0), ptr.getByteArray(1, len - 1));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
            packetResponseListenerRemoteCallbackList.finishBroadcast();
        }

    }

    private class SigActionHandler implements sigaction._u_union._sa_sigaction_callback {

        @Override
        public void apply(int int1, siginfo siginfoPtr1, Pointer voidPtr1) {
            siginfoPtr1._sifields.setType(siginfo._sifields_union._rt_struct.class);
            siginfoPtr1.read();
            int data = siginfoPtr1._sifields._rt._sigval.sival_int;
            Log.i(TAG, "Status change of DCI channel is received with data " + Integer.toHexString(data));

            if ((data & DiagLibrary.DIAG_CON_MPSS) == DiagLibrary.DIAG_CON_MPSS) {
                if ((data & DiagLibrary.DIAG_STATUS_CLOSED) == DiagLibrary.DIAG_STATUS_CLOSED) {
                    Log.i(TAG, "DCI channel to MPSS has just closed");
                } else if ((data & DiagLibrary.DIAG_STATUS_OPEN) == DiagLibrary.DIAG_STATUS_OPEN) {
                    Log.i(TAG, "DCI channel to MPSS has just been opened");
                }
            }
        }

    }

    private class DiagStreamRemoteCallbackList<E extends IInterface> extends RemoteCallbackList<E> {
        @Override
        public void onCallbackDied(E callback) {
            super.onCallbackDied(callback);
            if (callback instanceof ILogRecordListener) {
                adjustActiveLogTypes();
            } else if (callback instanceof IEventListener) {
                adjustActiveEventIds();
            }
        }
    }

}
