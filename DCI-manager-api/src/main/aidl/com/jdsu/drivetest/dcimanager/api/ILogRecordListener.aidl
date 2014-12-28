// ILogRecordListener.aidl
package com.jdsu.drivetest.dcimanager.api;

// Declare any non-default types here with import statements

interface ILogRecordListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void onReceived(char type, long timestamp, in byte[] data);
}
