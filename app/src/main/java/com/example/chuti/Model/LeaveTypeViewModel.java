package com.example.chuti.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LeaveTypeViewModel {
    @SerializedName("leaveTypeID")
    @Expose
    private Integer leaveTypeID;
    @SerializedName("leaveTypeName")
    @Expose
    private String leaveTypeName;
    @SerializedName("leaveCycle")
    @Expose
    private Integer leaveCycle;
    @SerializedName("inUsed")
    @Expose
    private Boolean inUsed;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("modifiedDate")
    @Expose
    private String modifiedDate;

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

    @Override
    public String toString() {
        return "LeaveTypeViewModel{" +
                "leaveTypeID=" + leaveTypeID +
                ", leaveTypeName='" + leaveTypeName + '\'' +
                ", leaveCycle=" + leaveCycle +
                ", inUsed=" + inUsed +
                ", createdDate='" + createdDate + '\'' +
                ", modifiedDate='" + modifiedDate + '\'' +
                '}';
    }
}
