// IEventListener.aidl
package com.jdsu.drivetest.dcimanager.api;

// Declare any non-default types here with import statements

interface IEventListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void onReceived(int id, in byte[] data);
}
