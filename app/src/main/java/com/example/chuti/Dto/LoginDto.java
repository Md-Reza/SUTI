package com.example.chuti.Dto;

public class LoginDto {
    public String LoginID ;
    public String Password ;
    public String DeviceUID ;
    public String DeviceName ;
    public String DeviceIP ;
    public String DeviceMAC ;
    public String GoogleFCMID;

    public String getGoogleFCMID() {
        return GoogleFCMID;
    }

    public void setGoogleFCMID(String googleFCMID) {
        GoogleFCMID = googleFCMID;
    }

    public String getDeviceUID() {
        return DeviceUID;
    }

    public void setDeviceUID(String deviceUID) {
        DeviceUID = deviceUID;
    }

    public String getDeviceName() {
        return DeviceName;
    }

    public void setDeviceName(String deviceName) {
        DeviceName = deviceName;
    }

    public String getDeviceIP() {
        return DeviceIP;
    }

    public void setDeviceIP(String deviceIP) {
        DeviceIP = deviceIP;
    }

    public String getDeviceMAC() {
        return DeviceMAC;
    }

    public void setDeviceMAC(String deviceMAC) {
        DeviceMAC = deviceMAC;
    }

    public String getLoginID() {
        return LoginID;
    }

    public void setLoginID(String loginID) {
        LoginID = loginID;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    @Override
    public String toString() {
        return "LoginDto{" +
                "LoginID='" + LoginID + '\'' +
                ", Password='" + Password + '\'' +
                ", DeviceUID='" + DeviceUID + '\'' +
                ", DeviceName='" + DeviceName + '\'' +
                ", DeviceIP='" + DeviceIP + '\'' +
                ", DeviceMAC='" + DeviceMAC + '\'' +
                ", GoogleFCMID='" + GoogleFCMID + '\'' +
                '}';
    }
}
