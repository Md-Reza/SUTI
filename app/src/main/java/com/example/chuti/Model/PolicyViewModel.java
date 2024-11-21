package com.example.chuti.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PolicyViewModel {
    @SerializedName("policyID")
    @Expose
    private Integer policyID;
    @SerializedName("policyName")
    @Expose
    private String policyName;
    @SerializedName("isDefault")
    @Expose
    private Boolean isDefault;
    @SerializedName("inUsed")
    @Expose
    private Boolean inUsed;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("modifiedDate")
    @Expose
    private String modifiedDate;
    @SerializedName("leaveTypesAsString")
    @Expose
    private List<Object> leaveTypesAsString;
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("message")
    @Expose
    private String message;

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

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public Boolean getInUsed() {
        return inUsed;
    }

    public void setInUsed(Boolean inUsed) {
        this.inUsed = inUsed;
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

    public List<Object> getLeaveTypesAsString() {
        return leaveTypesAsString;
    }

    public void setLeaveTypesAsString(List<Object> leaveTypesAsString) {
        this.leaveTypesAsString = leaveTypesAsString;
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
        return "PolicyViewModel{" +
                "policyID=" + policyID +
                ", policyName='" + policyName + '\'' +
                ", isDefault=" + isDefault +
                ", inUsed=" + inUsed +
                ", createdDate='" + createdDate + '\'' +
                ", modifiedDate='" + modifiedDate + '\'' +
                ", leaveTypesAsString=" + leaveTypesAsString +
                ", statusCode=" + statusCode +
                ", message='" + message + '\'' +
                '}';
    }
}
