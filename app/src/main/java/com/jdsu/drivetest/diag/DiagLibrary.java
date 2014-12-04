package com.jdsu.drivetest.diag;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.ShortByReference;

/**
 * The JNA mapping of the libdiag.so
 * Created by wen55527 on 3/12/14.
 */
public interface DiagLibrary extends Library {

    DiagLibrary INSTANCE = (DiagLibrary) Native.loadLibrary("diag", DiagLibrary.class);

    int Diag_LSM_Init(byte[] pIEnv);

    int Diag_LSM_DeInit();

    int diag_register_dci_client(IntByReference client_id, short[] list, int channel, int[] os_params);

    int diag_release_dci_client(IntByReference client_id);

    int diag_get_dci_support_list(ShortByReference list);
}
