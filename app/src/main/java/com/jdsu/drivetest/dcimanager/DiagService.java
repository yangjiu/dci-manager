package com.jdsu.drivetest.dcimanager;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.jdsu.drivetest.diag.DiagLibrary;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public class DiagService extends Service {

    private static final String TAG = DiagService.class.getName();
    private DiagLibrary.process_dci_log_stream logHandler = new DiagLibrary.process_dci_log_stream() {
        @Override
        public void apply(Pointer buffer, int length) {
            ByteBuffer logRecord = buffer.getByteBuffer(0, length);
            short recordLength = logRecord.getShort(0);
            short logcodeType = logRecord.getShort(2);
            Log.i(TAG, "received log type 0x" + Integer.toHexString(logcodeType & 0xFFFF) + " length " + recordLength);
        }
    };
    private IntBuffer client_id;
    private DiagLibrary.process_dci_event_stream eventHandler = new DiagLibrary.process_dci_event_stream() {
        @Override
        public void apply(Pointer buffer, int length) {

        }
    };

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
        int result = DiagLibrary.INSTANCE.Diag_LSM_Init(ByteBuffer.allocate(0));
        Log.i(TAG, "LSM initialization result code " + result);
        client_id = IntBuffer.allocate(1);
        ShortBuffer peripherals = ShortBuffer.allocate(1);
        peripherals.put((short) 0x0002);
        Memory os_param = new Memory(4);
        os_param.setInt(0, 18);
        result = DiagLibrary.INSTANCE.diag_register_dci_client(client_id, peripherals, 0, os_param);
        if (result != 1001) {
            Log.e(TAG, "Failed to register DCI client result code " + result);
            return;
        }
        Log.i(TAG, "Register DCI client successfully, client id " + client_id.get(0));

        ShortBuffer list = ShortBuffer.allocate(1);
        result = DiagLibrary.INSTANCE.diag_get_dci_support_list(list);
        if (result != 1001) {
            Log.e(TAG, "failed to get supported list, result code " + result);
            return;
        }
        Log.i(TAG, "APSS Status: " + ((list.get(0) & 0x0001) == 1 ? "Up" : "Down"));
        Log.i(TAG, "MPSS Status: " + ((list.get(0) & 0x0002) == 2 ? "Up" : "Down"));
        Log.i(TAG, "LPASS Status: " + ((list.get(0) & 0x0004) == 4 ? "Up" : "Down"));
        Log.i(TAG, "WCNSS Status: " + ((list.get(0) & 0x0008) == 8 ? "Up" : "Down"));

        result = DiagLibrary.INSTANCE.diag_register_dci_stream(logHandler, eventHandler);
        if (result != 1001) {
            Log.e(TAG, "failed to register dci streams result code " + result);
            return;
        }
        short[] log_codes_array = {0x5072, 0x115F, 0x12E8, 0x119B, 0x11AF, 0x14C8, 0x1375};
        result = DiagLibrary.INSTANCE.diag_log_stream_config(client_id.get(0), 1, log_codes_array, 7);
        if (result != 1001) {
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
