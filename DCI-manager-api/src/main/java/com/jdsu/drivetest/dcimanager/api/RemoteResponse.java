package com.jdsu.drivetest.dcimanager.api;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by simingweng on 26/12/14.
 */
public class RemoteResponse implements Parcelable {

    public static final Parcelable.Creator<RemoteResponse> CREATOR = new Parcelable.Creator<RemoteResponse>() {
        public RemoteResponse createFromParcel(Parcel source) {
            return new RemoteResponse(source);
        }

        public RemoteResponse[] newArray(int size) {
            return new RemoteResponse[size];
        }
    };
    private Response response;

    public RemoteResponse() {
    }

    private RemoteResponse(Parcel in) {
        int tmpResponse = in.readInt();
        this.response = tmpResponse == -1 ? null : Response.values()[tmpResponse];
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.response == null ? -1 : this.response.ordinal());
    }

    public enum Response {

        MPSS_DOWN(1, "MPSS is down"),

        DIAG_DCI_SUCCESS(1001, "Success"),
        DIAG_DCI_NO_REG(1002, "Could not register"),
        DIAG_DCI_NO_MEM(1003, "Failed memory allocation"),
        DIAG_DCI_NOT_SUPPORTED(1004, "Client is not supported"),
        DIAG_DCI_HUGE_PACKET(1005, "Request or response packet is too big"),
        DIAG_DCI_SEND_DATA_FAIL(1006, "Writing to the kernel or peripheral failed"),
        DIAG_DCI_ERR_DEREG(1007, "Error while unregister"),
        DIAG_DCI_PARAM_FAIL(1008, "Incorrect parameter");

        private int code;
        private String description;

        Response(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public static Response valueOf(int code) {
            for (Response rsp : values()) {
                if (rsp.code == code) {
                    return rsp;
                }
            }
            return null;
        }

        public int getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }
}
