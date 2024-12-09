package com.example.chuti.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmployeeAccount {
    @SerializedName("accountID")
    @Expose
    private Integer accountID;
    @SerializedName("accountType")
    @Expose
    private Integer accountType;
    @SerializedName("displayName")
    @Expose
    private String displayName;
    @SerializedName("profileImgUrl")
    @Expose
    private String profileImgUrl;
    @SerializedName("enabled")
    @Expose
    private Boolean enabled;

    public Integer getAccountID() {
        return accountID;
    }

    public void setAccountID(Integer accountID) {
        this.accountID = accountID;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProfileImgUrl() {
        return profileImgUrl;
    }

    public void setProfileImgUrl(String profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "EmployeeAccount{" +
                "accountID=" + accountID +
                ", accountType=" + accountType +
                ", displayName='" + displayName + '\'' +
                ", profileImgUrl='" + profileImgUrl + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}
