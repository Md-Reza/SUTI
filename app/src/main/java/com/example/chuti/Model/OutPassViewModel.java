package com.example.chuti.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OutPassViewModel {
    @SerializedName("outPassID")
    @Expose
    private Integer outPassID;
    @SerializedName("employeeCompactViewModel")
    @Expose
    private EmployeeCompactViewModel employeeCompactViewModel;
    @SerializedName("fromTime")
    @Expose
    private String fromTime;
    @SerializedName("toTime")
    @Expose
    private String toTime;
    @SerializedName("reason")
    @Expose
    private String reason;
    @SerializedName("isFullDay")
    @Expose
    private Boolean isFullDay;
    @SerializedName("durationMin")
    @Expose
    private Integer durationMin;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("isApproved")
    @Expose
    private Boolean isApproved;
    @SerializedName("isRejected")
    @Expose
    private Boolean isRejected;
    @SerializedName("requestDate")
    @Expose
    private String requestDate;
    @SerializedName("modifiedDate")
    @Expose
    private String modifiedDate;
    @SerializedName("outGateCode")
    @Expose
    private String outGateCode;
    @SerializedName("outDatetime")
    @Expose
    private String outDatetime;
    @SerializedName("isGateOut")
    @Expose
    private Boolean isGateOut;
    @SerializedName("inGateCode")
    @Expose
    private String inGateCode;
    @SerializedName("inDatetime")
    @Expose
    private String inDatetime;
    @SerializedName("isGateIn")
    @Expose
    private Boolean isGateIn;

    public Integer getOutPassID() {
        return outPassID;
    }

    public void setOutPassID(Integer outPassID) {
        this.outPassID = outPassID;
    }

    public EmployeeCompactViewModel getEmployeeCompactViewModel() {
        return employeeCompactViewModel;
    }

    public void setEmployeeCompactViewModel(EmployeeCompactViewModel employeeCompactViewModel) {
        this.employeeCompactViewModel = employeeCompactViewModel;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Boolean getFullDay() {
        return isFullDay;
    }

    public void setFullDay(Boolean fullDay) {
        isFullDay = fullDay;
    }

    public Integer getDurationMin() {
        return durationMin;
    }

    public void setDurationMin(Integer durationMin) {
        this.durationMin = durationMin;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getApproved() {
        return isApproved;
    }

    public void setApproved(Boolean approved) {
        isApproved = approved;
    }

    public Boolean getRejected() {
        return isRejected;
    }

    public void setRejected(Boolean rejected) {
        isRejected = rejected;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getOutGateCode() {
        return outGateCode;
    }

    public void setOutGateCode(String outGateCode) {
        this.outGateCode = outGateCode;
    }

    public String getOutDatetime() {
        return outDatetime;
    }

    public void setOutDatetime(String outDatetime) {
        this.outDatetime = outDatetime;
    }

    public Boolean getGateOut() {
        return isGateOut;
    }

    public void setGateOut(Boolean gateOut) {
        isGateOut = gateOut;
    }

    public String getInGateCode() {
        return inGateCode;
    }

    public void setInGateCode(String inGateCode) {
        this.inGateCode = inGateCode;
    }

    public String getInDatetime() {
        return inDatetime;
    }

    public void setInDatetime(String inDatetime) {
        this.inDatetime = inDatetime;
    }

    public Boolean getGateIn() {
        return isGateIn;
    }

    public void setGateIn(Boolean gateIn) {
        isGateIn = gateIn;
    }

    @Override
    public String toString() {
        return "OutPassViewModel{" +
                "outPassID=" + outPassID +
                ", employeeCompactViewModel=" + employeeCompactViewModel +
                ", fromTime='" + fromTime + '\'' +
                ", toTime='" + toTime + '\'' +
                ", reason='" + reason + '\'' +
                ", isFullDay=" + isFullDay +
                ", durationMin=" + durationMin +
                ", status=" + status +
                ", isApproved=" + isApproved +
                ", isRejected=" + isRejected +
                ", requestDate='" + requestDate + '\'' +
                ", modifiedDate='" + modifiedDate + '\'' +
                ", outGateCode='" + outGateCode + '\'' +
                ", outDatetime='" + outDatetime + '\'' +
                ", isGateOut=" + isGateOut +
                ", inGateCode='" + inGateCode + '\'' +
                ", inDatetime='" + inDatetime + '\'' +
                ", isGateIn=" + isGateIn +
                '}';
    }
}
