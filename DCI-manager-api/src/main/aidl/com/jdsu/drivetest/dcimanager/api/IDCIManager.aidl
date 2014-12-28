// IDCIManager.aidl
package com.jdsu.drivetest.dcimanager.api;

// Declare any non-default types here with import statements
import com.jdsu.drivetest.dcimanager.api.ILogRecordListener;
import com.jdsu.drivetest.dcimanager.api.IEventListener;
import com.jdsu.drivetest.dcimanager.api.RemoteResponse;
import com.jdsu.drivetest.dcimanager.api.IPacketResponseListener;

interface IDCIManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    RemoteResponse init();

    RemoteResponse listenLogRecord(in ILogRecordListener listener, in char[] log_types, int flag);

    RemoteResponse listenEvent(in IEventListener listener, in int[] event_ids, int flag);

    void registerPacketResponseListener(IPacketResponseListener listener);

    RemoteResponse sendRequestAsync(in byte[] request);

    void unregisterPacketResponseListener(IPacketResponseListener listener);

}
