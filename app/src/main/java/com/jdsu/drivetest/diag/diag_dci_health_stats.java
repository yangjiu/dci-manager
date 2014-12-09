package com.jdsu.drivetest.diag;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

/**
 * This is used for health stats<br>
 * <i>native declaration : diag/include/diag_lsm_dci.h:82</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class diag_dci_health_stats extends Structure {
    public int dropped_logs;
    public int dropped_events;
    public int received_logs;
    public int received_events;
    public int reset_status;

    public diag_dci_health_stats() {
        super();
    }

    public diag_dci_health_stats(int dropped_logs, int dropped_events, int received_logs, int received_events, int reset_status) {
        super();
        this.dropped_logs = dropped_logs;
        this.dropped_events = dropped_events;
        this.received_logs = received_logs;
        this.received_events = received_events;
        this.reset_status = reset_status;
    }

    public diag_dci_health_stats(Pointer peer) {
        super(peer);
    }

    protected List<?> getFieldOrder() {
        return Arrays.asList("dropped_logs", "dropped_events", "received_logs", "received_events", "reset_status");
    }

    public static class ByReference extends diag_dci_health_stats implements Structure.ByReference {

    }

    ;

    public static class ByValue extends diag_dci_health_stats implements Structure.ByValue {

    }

    ;
}