package com.jdsu.drivetest.dcimanager;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.jdsu.drivetest.diag.CLibrary;
import com.jdsu.drivetest.diag.DiagLibrary;
import com.jdsu.drivetest.diag.sigaction;
import com.jdsu.drivetest.diag.siginfo;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;

public class DiagService extends Service {

    private static final String TAG = DiagService.class.getName();
    private DiagLibrary.diag_register_dci_stream_func_ptr_logs_callback logHandler = new DiagLibrary.diag_register_dci_stream_func_ptr_logs_callback() {

        @Override
        public void apply(Pointer ptr, int len) {
            ByteBuffer logRecord = ptr.getByteBuffer(0, len);
            short recordLength = logRecord.getShort(0);
            short logcodeType = logRecord.getShort(2);
            Log.i(TAG, "received log type 0x" + Integer.toHexString(logcodeType & 0xFFFF) + " length " + recordLength + " " + Arrays.toString(ptr.getByteArray(0, len)));
        }
    };
    private DiagLibrary.diag_send_dci_async_req_func_ptr_callback responseHandler = new DiagLibrary.diag_send_dci_async_req_func_ptr_callback() {

        @Override
        public void apply(Pointer ptr, int len, Pointer data_ptr) {
            Log.i(TAG, "received response " + Arrays.toString(ptr.getByteArray(0, len)));
        }
    };
    private IntBuffer client_id;
    private DiagLibrary.diag_register_dci_stream_func_ptr_events_callback eventHandler = new DiagLibrary.diag_register_dci_stream_func_ptr_events_callback() {
        @Override
        public void apply(Pointer buffer, int length) {

        }
    };
    private sigaction conn_notify_action;

    public DiagService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        conn_notify_action = new sigaction(new sigaction._u_union(new sigaction._u_union._sa_sigaction_callback() {
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
        }), new NativeLong(0), new NativeLong(CLibrary.SA_SIGINFO), null);
        CLibrary.INSTANCE.sigaction(CLibrary.SIGCONT, conn_notify_action, conn_notify_action);

        int result = DiagLibrary.INSTANCE.Diag_LSM_Init(ByteBuffer.allocate(0));
        Log.i(TAG, "LSM initialization result code " + result);
        client_id = IntBuffer.allocate(1);
        ShortBuffer peripherals = ShortBuffer.allocate(1);
        peripherals.put((short) DiagLibrary.DIAG_CON_MPSS);
        result = DiagLibrary.INSTANCE.diag_register_dci_client(client_id, peripherals, 0, new IntByReference(CLibrary.SIGCONT).getPointer());
        if (result != DiagLibrary.diag_dci_error_type_enum.DIAG_DCI_NO_ERROR) {
            Log.e(TAG, "Failed to register DCI client result code " + result);
            return;
        }
        Log.i(TAG, "Register DCI client successfully, client id " + client_id.get(0));


        ShortBuffer list = ShortBuffer.allocate(1);
        result = DiagLibrary.INSTANCE.diag_get_dci_support_list(list);
        if (result != DiagLibrary.diag_dci_error_type_enum.DIAG_DCI_NO_ERROR) {
            Log.e(TAG, "failed to get supported list, result code " + result);
            return;
        }
        Log.i(TAG, "APSS Status: " + ((list.get(0) & 0x0001) == 1 ? "Up" : "Down"));
        Log.i(TAG, "MPSS Status: " + ((list.get(0) & 0x0002) == 2 ? "Up" : "Down"));
        Log.i(TAG, "LPASS Status: " + ((list.get(0) & 0x0004) == 4 ? "Up" : "Down"));
        Log.i(TAG, "WCNSS Status: " + ((list.get(0) & 0x0008) == 8 ? "Up" : "Down"));

        result = DiagLibrary.INSTANCE.diag_register_dci_stream(logHandler, eventHandler);
        if (result != DiagLibrary.diag_dci_error_type_enum.DIAG_DCI_NO_ERROR) {
            Log.e(TAG, "failed to register dci streams result code " + result);
            return;
        }

        ByteBuffer request = ByteBuffer.wrap(new byte[]{75, 18, 0, 0, 1, 0, 0, 0, 16, 1, 1, 0, 0, 1, 0, 0, (byte) 232, 3, 0, 0, 1, 0, 0, 0});
        ByteBuffer response = ByteBuffer.allocate(100);
        result = DiagLibrary.INSTANCE.diag_send_dci_async_req(client_id.get(0), request, 24, response, 100, responseHandler, Pointer.NULL);
        if (result != DiagLibrary.diag_dci_error_type_enum.DIAG_DCI_NO_ERROR) {
            Log.e(TAG, "failed to send async request result code " + result);
            return;
        }

        ShortBuffer log_codes_array = ShortBuffer.wrap(new short[]{0x5072, 0x115F, 0x12E8, 0x119B, 0x11AF, 0x14C8, 0x1375});
        result = DiagLibrary.INSTANCE.diag_log_stream_config(client_id.get(0), DiagLibrary.ENABLE, log_codes_array, 7);
        if (result != DiagLibrary.diag_dci_error_type_enum.DIAG_DCI_NO_ERROR) {
            Log.e(TAG, "failed to config log streams result code " + result);
            return;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        int result = DiagLibrary.INSTANCE.diag_release_dci_client(client_id);
        Log.i(TAG, "release DCI client result code " + result);
        result = DiagLibrary.INSTANCE.Diag_LSM_DeInit();
        Log.i(TAG, "LSM deinitialization result code " + result);
    }
}
