package com.example.chuti.Dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateEmployeeSelfDto {
    @SerializedName("employeeName")
    @Expose
    private String employeeName;
    @SerializedName("emailAddress")
    @Expose
    private String emailAddress;
    @SerializedName("phoneNo")
    @Expose
    private String phoneNo;
    @SerializedName("profileImageName")
    @Expose
    private String profileImageName;
    @SerializedName("profileImageString")
    @Expose
    private String profileImageString;
    @SerializedName("profileImageSize")
    @Expose
    private String profileImageSize;

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getProfileImageName() {
        return profileImageName;
    }

    public void setProfileImageName(String profileImageName) {
        this.profileImageName = profileImageName;
    }

    public String getProfileImageString() {
        return profileImageString;
    }

    public void setProfileImageString(String profileImageString) {
        this.profileImageString = profileImageString;
    }

    public String getProfileImageSize() {
        return profileImageSize;
    }

    public void setProfileImageSize(String profileImageSize) {
        this.profileImageSize = profileImageSize;
    }

    @Override
    public String toString() {
        return "UpdateEmployeeSelfDto{" +
                "employeeName='" + employeeName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", profileImageName='" + profileImageName + '\'' +
                ", profileImageString='" + profileImageString + '\'' +
                ", profileImageSize='" + profileImageSize + '\'' +
                '}';
    }
}
