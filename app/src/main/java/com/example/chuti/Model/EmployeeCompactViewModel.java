package com.example.chuti.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmployeeCompactViewModel {
    @SerializedName("accountID")
    @Expose
    private Integer accountID;
    @SerializedName("hrEmployeeID")
    @Expose
    private String hrEmployeeID;
    @SerializedName("employeeName")
    @Expose
    private String employeeName;
    @SerializedName("joiningDate")
    @Expose
    private String joiningDate;

    public Integer getAccountID() {
        return accountID;
    }

    public void setAccountID(Integer accountID) {
        this.accountID = accountID;
    }

    public String getHrEmployeeID() {
        return hrEmployeeID;
    }

    public void setHrEmployeeID(String hrEmployeeID) {
        this.hrEmployeeID = hrEmployeeID;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(String joiningDate) {
        this.joiningDate = joiningDate;
    }

    @Override
    public String toString() {
        return "EmployeeCompactViewModel{" +
                "accountID=" + accountID +
                ", hrEmployeeID='" + hrEmployeeID + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", joiningDate='" + joiningDate + '\'' +
                '}';
    }
}
