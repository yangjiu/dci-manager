package com.jdsu.drivetest.dcimanager;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.jdsu.drivetest.diag.DiagLibrary;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.ShortByReference;

public class DiagService extends Service {

    private static final String TAG = DiagService.class.getName();
    private IntByReference client_id;

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
        int result = DiagLibrary.INSTANCE.Diag_LSM_Init(null);
        Log.i(TAG, "LSM initialization result code" + result);
        client_id = new IntByReference();
        short[] peripherals = {0x0003};
        int[] os_params = {18};
        result = DiagLibrary.INSTANCE.diag_register_dci_client(client_id, peripherals, 0, os_params);
        if (result != 1001) {
            Log.e(TAG, "Failed to register DCI client result code " + result);
            return;
        }
        Log.i(TAG, "Register DCI client successfully, client id " + client_id.getValue());

        ShortByReference list = new ShortByReference();
        result = DiagLibrary.INSTANCE.diag_get_dci_support_list(list);
        if (result != 1001) {
            Log.e(TAG, "failed to get supported list, result code " + result);
            return;
        }
        Log.i(TAG, "APSS Status: " + ((list.getValue() & 0x0001) == 1 ? "Up" : "Down"));
        Log.i(TAG, "MPSS Status: " + ((list.getValue() & 0x0002) == 2 ? "Up" : "Down"));
        Log.i(TAG, "LPASS Status: " + ((list.getValue() & 0x0004) == 4 ? "Up" : "Down"));
        Log.i(TAG, "WCNSS Status: " + ((list.getValue() & 0x0008) == 8 ? "Up" : "Down"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        int result = DiagLibrary.INSTANCE.Diag_LSM_DeInit();
        Log.i(TAG, "LSM deinitialization result code" + result);
        result = DiagLibrary.INSTANCE.diag_release_dci_client(client_id);
        Log.i(TAG, "release DCI client result code " + result);
    }
}
