package com.jdsu.drivetest.diag;

import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * Created by wen55527 on 3/12/14.
 */
public interface DiagLibrary extends Library {

    DiagLibrary INSTANCE = (DiagLibrary) Native.loadLibrary("diag", DiagLibrary.class);

    int Diag_LSM_Init(byte[] pIEnv);

    int Diag_LSM_DeInit();
}
