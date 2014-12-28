package com.jdsu.drivetest.dcimanager.sample;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.jdsu.drivetest.dcimanager.DiagService;
import com.jdsu.drivetest.dcimanager.R;
import com.jdsu.drivetest.dcimanager.api.IDCIManager;
import com.jdsu.drivetest.dcimanager.api.ILogRecordListener;
import com.jdsu.drivetest.dcimanager.api.LogRecordConstants;
import com.jdsu.drivetest.dcimanager.api.RemoteResponse;

import java.util.Arrays;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getName();
    private ILogRecordListener logRecordListener = new ILogRecordListener.Stub() {

        @Override
        public void onReceived(char type, long timestamp, byte[] data) throws RemoteException {
            Log.i(TAG, String.format("log type %02x %s", (short) type, Arrays.toString(data)));
        }
    };
    private IDCIManager dciManager;
    private ServiceConnection serviceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                dciManager = IDCIManager.Stub.asInterface(service);
                char[] logTypes = new char[]{0x5072, 0x115F, 0x12E8, 0x119B, 0x11AF, 0x14C8, 0x1375};
                try {
                    RemoteResponse response = dciManager.listenLogRecord(logRecordListener, logTypes, LogRecordConstants.LOG_RECORD_SET);
                    Log.i(TAG, "code " + response.getResponse().getDescription());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                dciManager = null;
            }
        };
        bindService(new Intent(this, DiagService.class), serviceConnection, BIND_AUTO_CREATE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dciManager != null) {
            try {
                dciManager.listenLogRecord(logRecordListener, null, LogRecordConstants.LOG_RECORD_NONE);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            unbindService(serviceConnection);
        }
    }
}
