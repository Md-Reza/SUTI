package com.example.chuti.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmployeePeriodViewModel {
    @SerializedName("employeePeriodID")
    @Expose
    private Integer employeePeriodID;
    @SerializedName("fromDate")
    @Expose
    private String fromDate;
    @SerializedName("toDate")
    @Expose
    private String toDate;
    @SerializedName("year")
    @Expose
    private Integer year;
    @SerializedName("isCurrent")
    @Expose
    private Boolean isCurrent;

    public Integer getEmployeePeriodID() {
        return employeePeriodID;
    }

    public void setEmployeePeriodID(Integer employeePeriodID) {
        this.employeePeriodID = employeePeriodID;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Boolean getCurrent() {
        return isCurrent;
    }

    public void setCurrent(Boolean current) {
        isCurrent = current;
    }

    @Override
    public String toString() {
        return "EmployeePeriodViewModel{" +
                "employeePeriodID=" + employeePeriodID +
                ", fromDate='" + fromDate + '\'' +
                ", toDate='" + toDate + '\'' +
                ", year=" + year +
                ", isCurrent=" + isCurrent +
                '}';
    }
}
