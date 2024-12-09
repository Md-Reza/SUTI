package com.example.chuti.Model;

import android.os.Parcel;
import android.os.Parcelable;


public class RemoteMessageViewModel implements Parcelable {
    private String status;
    private String requestId;
    private String requestType;

    // Constructor
    public RemoteMessageViewModel(String status, String requestId, String requestType) {
        this.status = status;
        this.requestId = requestId;
        this.requestType = requestType;
    }

    // Parcelable Constructor
    protected RemoteMessageViewModel(Parcel in) {
        status = in.readString();
        requestId = in.readString();
        requestType = in.readString();
    }

    // Parcelable Creator
    public static final Creator<RemoteMessageViewModel> CREATOR = new Creator<RemoteMessageViewModel>() {
        @Override
        public RemoteMessageViewModel createFromParcel(Parcel in) {
            return new RemoteMessageViewModel(in);
        }

        @Override
        public RemoteMessageViewModel[] newArray(int size) {
            return new RemoteMessageViewModel[size];
        }
    };

    // Getters
    public String getStatus() {
        return status;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getRequestType() {
        return requestType;
    }

    // Setters
    public void setStatus(String status) {
        this.status = status;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeString(requestId);
        dest.writeString(requestType);
    }

    @Override
    public String toString() {
        return "RequestData{" +
                "status='" + status + '\'' +
                ", requestId='" + requestId + '\'' +
                ", requestType='" + requestType + '\'' +
                '}';
    }
}
