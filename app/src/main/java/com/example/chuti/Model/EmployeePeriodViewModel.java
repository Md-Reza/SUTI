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
    @SerializedName("periodYear")
    @Expose
    private Integer periodYear;
    @SerializedName("isCurrent")
    @Expose
    private Boolean isCurrent;
    @SerializedName("isClosed")
    @Expose
    private Boolean isClosed;

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

    public Integer getPeriodYear() {
        return periodYear;
    }

    public void setPeriodYear(Integer periodYear) {
        this.periodYear = periodYear;
    }

    public Boolean getCurrent() {
        return isCurrent;
    }

    public void setCurrent(Boolean current) {
        isCurrent = current;
    }

    public Boolean getClosed() {
        return isClosed;
    }

    public void setClosed(Boolean closed) {
        isClosed = closed;
    }

    @Override
    public String toString() {
        return "EmployeePeriodViewModel{" +
                "employeePeriodID=" + employeePeriodID +
                ", fromDate='" + fromDate + '\'' +
                ", toDate='" + toDate + '\'' +
                ", periodYear=" + periodYear +
                ", isCurrent=" + isCurrent +
                ", isClosed=" + isClosed +
                '}';
    }
}
