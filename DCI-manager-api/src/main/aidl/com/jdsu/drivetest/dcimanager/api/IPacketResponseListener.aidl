// IPacketResponseListener.aidl
package com.jdsu.drivetest.dcimanager.api;

// Declare any non-default types here with import statements

interface IPacketResponseListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void onReceived(byte cmd_code, in byte[] data);
}
