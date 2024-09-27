package com.example.suti.Dto;

public class LoginDto {
    public String userNameIDEmpID ;
    public String password ;
    public String iPAddress ;
    public String mACAddress ;

    public String getUserNameIDEmpID() {
        return userNameIDEmpID;
    }

    public void setUserNameIDEmpID(String userNameIDEmpID) {
        this.userNameIDEmpID = userNameIDEmpID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getiPAddress() {
        return iPAddress;
    }

    public void setiPAddress(String iPAddress) {
        this.iPAddress = iPAddress;
    }

    public String getmACAddress() {
        return mACAddress;
    }

    public void setmACAddress(String mACAddress) {
        this.mACAddress = mACAddress;
    }

    @Override
    public String toString() {
        return "LoginDto{" +
                "userNameIDEmpID='" + userNameIDEmpID + '\'' +
                ", password='" + password + '\'' +
                ", iPAddress='" + iPAddress + '\'' +
                ", mACAddress='" + mACAddress + '\'' +
                '}';
    }
}
