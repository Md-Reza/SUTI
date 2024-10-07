package com.example.chuti.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EmployeeLeaveRequestViewModel {
    @SerializedName("employeeLeaveRequestID")
    @Expose
    private String employeeLeaveRequestID;
    @SerializedName("userID")
    @Expose
    private Integer userID;
    @SerializedName("employeePeriodViewModel")
    @Expose
    private EmployeePeriodViewModel employeePeriodViewModel;
    @SerializedName("policyID")
    @Expose
    private Integer policyID;
    @SerializedName("policyName")
    @Expose
    private String policyName;
    @SerializedName("leaveTypeID")
    @Expose
    private Integer leaveTypeID;
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
    @SerializedName("reason")
    @Expose
    private String reason;
    @SerializedName("documentUrl")
    @Expose
    private String documentUrl;
    @SerializedName("approverUserID")
    @Expose
    private Integer approverUserID;
    @SerializedName("approverName")
    @Expose
    private String approverName;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("isApproved")
    @Expose
    private Boolean isApproved;
    @SerializedName("isRejected")
    @Expose
    private Boolean isRejected;
    @SerializedName("isDeleted")
    @Expose
    private Boolean isDeleted;
    @SerializedName("comment")
    @Expose
    private Object comment;
    @SerializedName("actionDate")
    @Expose
    private String actionDate;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("modifiedDate")
    @Expose
    private String modifiedDate;
    @SerializedName("leaveDates")
    @Expose
    private List<Object> leaveDates;
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("message")
    @Expose
    private String message;

    public String getEmployeeLeaveRequestID() {
        return employeeLeaveRequestID;
    }

    public void setEmployeeLeaveRequestID(String employeeLeaveRequestID) {
        this.employeeLeaveRequestID = employeeLeaveRequestID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public EmployeePeriodViewModel getEmployeePeriodViewModel() {
        return employeePeriodViewModel;
    }

    public void setEmployeePeriodViewModel(EmployeePeriodViewModel employeePeriodViewModel) {
        this.employeePeriodViewModel = employeePeriodViewModel;
    }

    public Integer getPolicyID() {
        return policyID;
    }

    public void setPolicyID(Integer policyID) {
        this.policyID = policyID;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public Integer getLeaveTypeID() {
        return leaveTypeID;
    }

    public void setLeaveTypeID(Integer leaveTypeID) {
        this.leaveTypeID = leaveTypeID;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDocumentUrl() {
        return documentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    public Integer getApproverUserID() {
        return approverUserID;
    }

    public void setApproverUserID(Integer approverUserID) {
        this.approverUserID = approverUserID;
    }

    public String getApproverName() {
        return approverName;
    }

    public void setApproverName(String approverName) {
        this.approverName = approverName;
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

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Object getComment() {
        return comment;
    }

    public void setComment(Object comment) {
        this.comment = comment;
    }

    public String getActionDate() {
        return actionDate;
    }

    public void setActionDate(String actionDate) {
        this.actionDate = actionDate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public List<Object> getLeaveDates() {
        return leaveDates;
    }

    public void setLeaveDates(List<Object> leaveDates) {
        this.leaveDates = leaveDates;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "EmployeeLeaveRequestViewModel{" +
                "employeeLeaveRequestID='" + employeeLeaveRequestID + '\'' +
                ", userID=" + userID +
                ", employeePeriodViewModel=" + employeePeriodViewModel +
                ", policyID=" + policyID +
                ", policyName='" + policyName + '\'' +
                ", leaveTypeID=" + leaveTypeID +
                ", leaveTypeName='" + leaveTypeName + '\'' +
                ", leaveCycle=" + leaveCycle +
                ", fromDate='" + fromDate + '\'' +
                ", toDate='" + toDate + '\'' +
                ", noOfDays=" + noOfDays +
                ", reason='" + reason + '\'' +
                ", documentUrl='" + documentUrl + '\'' +
                ", approverUserID=" + approverUserID +
                ", approverName='" + approverName + '\'' +
                ", status=" + status +
                ", isApproved=" + isApproved +
                ", isRejected=" + isRejected +
                ", isDeleted=" + isDeleted +
                ", comment=" + comment +
                ", actionDate='" + actionDate + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", modifiedDate='" + modifiedDate + '\'' +
                ", leaveDates=" + leaveDates +
                ", statusCode=" + statusCode +
                ", message='" + message + '\'' +
                '}';
    }
}
