package com.example.chuti.Dto;

public class LoginDto {
    public String LoginID ;
    public String Password ;

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
                '}';
    }
}
