package com.jdsu.drivetest.dcimanager;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.jdsu.drivetest.diag.DiagLibrary;

public class DiagService extends Service {

    private static final String TAG = DiagService.class.getName();

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
        Log.i(TAG, "LSM initialization result " + result);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        int result = DiagLibrary.INSTANCE.Diag_LSM_DeInit();
        Log.i(TAG, "LSM deinitialization result " + result);
    }
}
