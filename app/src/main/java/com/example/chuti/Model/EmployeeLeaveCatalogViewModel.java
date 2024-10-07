package com.example.chuti.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmployeeLeaveCatalogViewModel {
    @SerializedName("employeeLeaveCatalogID")
    @Expose
    private Integer employeeLeaveCatalogID;
    @SerializedName("policyID")
    @Expose
    private Integer policyID;
    @SerializedName("policyName")
    @Expose
    private Object policyName;
    @SerializedName("policyLeaveID")
    @Expose
    private Integer policyLeaveID;
    @SerializedName("employeePeriodID")
    @Expose
    private Integer employeePeriodID;
    @SerializedName("leaveTypeID")
    @Expose
    private Integer leaveTypeID;
    @SerializedName("leaveTypeName")
    @Expose
    private String leaveTypeName;
    @SerializedName("leaveCycle")
    @Expose
    private Integer leaveCycle;
    @SerializedName("allowOverRequest")
    @Expose
    private Boolean allowOverRequest;
    @SerializedName("requiredDocument")
    @Expose
    private Boolean requiredDocument;
    @SerializedName("allowAutoApprove")
    @Expose
    private Boolean allowAutoApprove;
    @SerializedName("totalHandDays")
    @Expose
    private Integer totalHandDays;
    @SerializedName("availableDays")
    @Expose
    private Integer availableDays;
    @SerializedName("onHeldDays")
    @Expose
    private Integer onHeldDays;
    @SerializedName("usedDays")
    @Expose
    private Integer usedDays;

    public Integer getEmployeeLeaveCatalogID() {
        return employeeLeaveCatalogID;
    }

    public void setEmployeeLeaveCatalogID(Integer employeeLeaveCatalogID) {
        this.employeeLeaveCatalogID = employeeLeaveCatalogID;
    }

    public Integer getPolicyID() {
        return policyID;
    }

    public void setPolicyID(Integer policyID) {
        this.policyID = policyID;
    }

    public Object getPolicyName() {
        return policyName;
    }

    public void setPolicyName(Object policyName) {
        this.policyName = policyName;
    }

    public Integer getPolicyLeaveID() {
        return policyLeaveID;
    }

    public void setPolicyLeaveID(Integer policyLeaveID) {
        this.policyLeaveID = policyLeaveID;
    }

    public Integer getEmployeePeriodID() {
        return employeePeriodID;
    }

    public void setEmployeePeriodID(Integer employeePeriodID) {
        this.employeePeriodID = employeePeriodID;
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

    public Boolean getAllowOverRequest() {
        return allowOverRequest;
    }

    public void setAllowOverRequest(Boolean allowOverRequest) {
        this.allowOverRequest = allowOverRequest;
    }

    public Boolean getRequiredDocument() {
        return requiredDocument;
    }

    public void setRequiredDocument(Boolean requiredDocument) {
        this.requiredDocument = requiredDocument;
    }

    public Boolean getAllowAutoApprove() {
        return allowAutoApprove;
    }

    public void setAllowAutoApprove(Boolean allowAutoApprove) {
        this.allowAutoApprove = allowAutoApprove;
    }

    public Integer getTotalHandDays() {
        return totalHandDays;
    }

    public void setTotalHandDays(Integer totalHandDays) {
        this.totalHandDays = totalHandDays;
    }

    public Integer getAvailableDays() {
        return availableDays;
    }

    public void setAvailableDays(Integer availableDays) {
        this.availableDays = availableDays;
    }

    public Integer getOnHeldDays() {
        return onHeldDays;
    }

    public void setOnHeldDays(Integer onHeldDays) {
        this.onHeldDays = onHeldDays;
    }

    public Integer getUsedDays() {
        return usedDays;
    }

    public void setUsedDays(Integer usedDays) {
        this.usedDays = usedDays;
    }

    @Override
    public String toString() {
        return "EmployeeLeaveCatalogViewModel{" +
                "employeeLeaveCatalogID=" + employeeLeaveCatalogID +
                ", policyID=" + policyID +
                ", policyName='" + policyName + '\'' +
                ", policyLeaveID=" + policyLeaveID +
                ", employeePeriodID=" + employeePeriodID +
                ", leaveTypeID=" + leaveTypeID +
                ", leaveTypeName='" + leaveTypeName + '\'' +
                ", leaveCycle='" + leaveCycle + '\'' +
                ", allowOverRequest=" + allowOverRequest +
                ", requiredDocument=" + requiredDocument +
                ", allowAutoApprove=" + allowAutoApprove +
                ", totalHandDays=" + totalHandDays +
                ", availableDays=" + availableDays +
                ", onHeldDays=" + onHeldDays +
                ", usedDays=" + usedDays +
                '}';
    }
}
