package com.example.chuti.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LeaveRequestsViewModel {
    @SerializedName("leaveRequestID")
    @Expose
    private Integer leaveRequestID;
    @SerializedName("policyName")
    @Expose
    private String policyName;
    @SerializedName("leaveTypeName")
    @Expose
    private String leaveTypeName;
    @SerializedName("leaveCycle")
    @Expose
    private Integer leaveCycle;
    @SerializedName("fromDate")
    @Expose
    private String fromDate;
    @SerializedName("toDate")
    @Expose
    private String toDate;
    @SerializedName("noOfDays")
    @Expose
    private Integer noOfDays;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("isApproved")
    @Expose
    private Boolean isApproved;
    @SerializedName("isRejected")
    @Expose
    private Boolean isRejected;
    @SerializedName("lastComment")
    @Expose
    private String lastComment;
    @SerializedName("reason")
    @Expose
    private String reason;
    @SerializedName("requestDate")
    @Expose
    private String requestDate;
    @SerializedName("modifiedDate")
    @Expose
    private String modifiedDate;
    @SerializedName("documentUrl")
    @Expose
    private String documentUrl;
    @SerializedName("employeeCompactViewModel")
    @Expose
    private EmployeeCompactViewModel employeeCompactViewModel;

    public Integer getLeaveRequestID() {
        return leaveRequestID;
    }

    public void setLeaveRequestID(Integer leaveRequestID) {
        this.leaveRequestID = leaveRequestID;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public String getLeaveTypeName() {
        return leaveTypeName;
    }

    public void setLeaveTypeName(String leaveTypeName) {
        this.leaveTypeName = leaveTypeName;
    }

    public Integer getLeaveCycle() {
        return leaveCycle;
    }

    public void setLeaveCycle(Integer leaveCycle) {
        this.leaveCycle = leaveCycle;
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

    public Integer getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(Integer noOfDays) {
        this.noOfDays = noOfDays;
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

    public String getLastComment() {
        return lastComment;
    }

    public void setLastComment(String lastComment) {
        this.lastComment = lastComment;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public String getDocumentUrl() {
        return documentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    public EmployeeCompactViewModel getEmployeeCompactViewModel() {
        return employeeCompactViewModel;
    }

    public void setEmployeeCompactViewModel(EmployeeCompactViewModel employeeCompactViewModel) {
        this.employeeCompactViewModel = employeeCompactViewModel;
    }

    @Override
    public String toString() {
        return "LeaveRequestsViewModel{" +
                "leaveRequestID=" + leaveRequestID +
                ", policyName='" + policyName + '\'' +
                ", leaveTypeName='" + leaveTypeName + '\'' +
                ", leaveCycle=" + leaveCycle +
                ", fromDate='" + fromDate + '\'' +
                ", toDate='" + toDate + '\'' +
                ", noOfDays=" + noOfDays +
                ", status=" + status +
                ", isApproved=" + isApproved +
                ", isRejected=" + isRejected +
                ", lastComment='" + lastComment + '\'' +
                ", reason='" + reason + '\'' +
                ", requestDate='" + requestDate + '\'' +
                ", modifiedDate='" + modifiedDate + '\'' +
                ", documentUrl='" + documentUrl + '\'' +
                ", employeeCompactViewModel=" + employeeCompactViewModel +
                '}';
    }
}
