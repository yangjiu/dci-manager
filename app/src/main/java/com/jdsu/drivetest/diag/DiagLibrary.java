package com.jdsu.drivetest.diag;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

/**
 * The JNA mapping of the libdiag.so
 * Created by wen55527 on 3/12/14.
 */
public interface DiagLibrary extends Library {

    DiagLibrary INSTANCE = (DiagLibrary) Native.loadLibrary("diag", DiagLibrary.class);

    int Diag_LSM_Init(ByteBuffer pIEnv);

    int Diag_LSM_DeInit();

    int diag_register_dci_client(IntBuffer intPtr1, ShortBuffer diag_dci_peripheralsPtr1, int int1, Pointer voidPtr1);

    int diag_release_dci_client(IntBuffer intPtr1);

    int diag_get_dci_support_list(ShortBuffer diag_dci_peripheralsPtr1);

    int diag_register_dci_stream(process_dci_log_stream log_handler, process_dci_event_stream event_handler);

    int diag_log_stream_config(int client_id, int set_mask, short[] log_codes_array, int num_codes);

    int diag_send_dci_async_req(int client_id, byte[] request, int request_len, ByteBuffer response, int response_len, process_response response_handler, Pointer voidPtr1);

    public interface process_dci_log_stream extends Callback {
        void apply(Pointer buffer, int length);
    }

    public interface process_dci_event_stream extends Callback {
        void apply(Pointer buffer, int length);
    }

    public interface process_response extends Callback {
        void apply(Pointer buffer, int len, Pointer data);
    }
}
