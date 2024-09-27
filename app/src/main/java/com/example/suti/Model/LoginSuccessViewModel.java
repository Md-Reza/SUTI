package com.example.suti.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginSuccessViewModel {
    @SerializedName("userID")
    @Expose
    private Integer userID;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("employeeID")
    @Expose
    private String employeeID;
    @SerializedName("displayName")
    @Expose
    private String displayName;
    @SerializedName("plantID")
    @Expose
    private Integer plantID;
    @SerializedName("hasOrgPlantAccess")
    @Expose
    private Boolean hasOrgPlantAccess;
    @SerializedName("accessToken")
    @Expose
    private String accessToken;

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getPlantID() {
        return plantID;
    }

    public void setPlantID(Integer plantID) {
        this.plantID = plantID;
    }

    public Boolean getHasOrgPlantAccess() {
        return hasOrgPlantAccess;
    }

    public void setHasOrgPlantAccess(Boolean hasOrgPlantAccess) {
        this.hasOrgPlantAccess = hasOrgPlantAccess;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }


    @Override
    public String toString() {
        return "LoginSuccessViewModel{" +
                "userID=" + userID +
                ", userName='" + userName + '\'' +
                ", employeeID='" + employeeID + '\'' +
                ", displayName='" + displayName + '\'' +
                ", plantID=" + plantID +
                ", hasOrgPlantAccess=" + hasOrgPlantAccess +
                ", accessToken='" + accessToken + '\'' +
                '}';
    }
}