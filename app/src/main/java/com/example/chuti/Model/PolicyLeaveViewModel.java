package com.example.chuti.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PolicyLeaveViewModel {
    @SerializedName("policyLeaveID")
    @Expose
    private Integer policyLeaveID;
    @SerializedName("leaveTypeViewModel")
    @Expose
    private LeaveTypeViewModel leaveTypeViewModel;
    @SerializedName("noOfDay")
    @Expose
    private Integer noOfDay;
    @SerializedName("allowOverRequest")
    @Expose
    private Boolean allowOverRequest;
    @SerializedName("requiredDocument")
    @Expose
    private Boolean requiredDocument;
    @SerializedName("carryOverNextYear")
    @Expose
    private Boolean carryOverNextYear;
    @SerializedName("allowAutoApprove")
    @Expose
    private Boolean allowAutoApprove;
    @SerializedName("disabled")
    @Expose
    private Boolean disabled;

    public Integer getPolicyLeaveID() {
        return policyLeaveID;
    }

    public void setPolicyLeaveID(Integer policyLeaveID) {
        this.policyLeaveID = policyLeaveID;
    }

    public LeaveTypeViewModel getLeaveTypeViewModel() {
        return leaveTypeViewModel;
    }

    public void setLeaveTypeViewModel(LeaveTypeViewModel leaveTypeViewModel) {
        this.leaveTypeViewModel = leaveTypeViewModel;
    }

    public Integer getNoOfDay() {
        return noOfDay;
    }

    public void setNoOfDay(Integer noOfDay) {
        this.noOfDay = noOfDay;
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

    public Boolean getCarryOverNextYear() {
        return carryOverNextYear;
    }

    public void setCarryOverNextYear(Boolean carryOverNextYear) {
        this.carryOverNextYear = carryOverNextYear;
    }

    public Boolean getAllowAutoApprove() {
        return allowAutoApprove;
    }

    public void setAllowAutoApprove(Boolean allowAutoApprove) {
        this.allowAutoApprove = allowAutoApprove;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public String toString() {
        return "PolicyLeaveViewModel{" +
                "policyLeaveID=" + policyLeaveID +
                ", leaveTypeViewModel=" + leaveTypeViewModel +
                ", noOfDay=" + noOfDay +
                ", allowOverRequest=" + allowOverRequest +
                ", requiredDocument=" + requiredDocument +
                ", carryOverNextYear=" + carryOverNextYear +
                ", allowAutoApprove=" + allowAutoApprove +
                ", disabled=" + disabled +
                '}';
    }
}
