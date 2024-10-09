package com.example.chuti.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDetailsViewModel {
    @SerializedName("userID")
    @Expose
    private Integer userID;
    @SerializedName("role")
    @Expose
    private Integer role;
    @SerializedName("companyViewModel")
    @Expose
    private CompanyViewModel companyViewModel;
    @SerializedName("displayName")
    @Expose
    private String displayName;
    @SerializedName("emailConfirmed")
    @Expose
    private Boolean emailConfirmed;
    @SerializedName("profileImgUrl")
    @Expose
    private Object profileImgUrl;
    @SerializedName("enabled")
    @Expose
    private Boolean enabled;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("modifiedDate")
    @Expose
    private String modifiedDate;
    @SerializedName("lastLoginDate")
    @Expose
    private String lastLoginDate;

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public CompanyViewModel getCompanyViewModel() {
        return companyViewModel;
    }

    public void setCompanyViewModel(CompanyViewModel companyViewModel) {
        this.companyViewModel = companyViewModel;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Boolean getEmailConfirmed() {
        return emailConfirmed;
    }

    public void setEmailConfirmed(Boolean emailConfirmed) {
        this.emailConfirmed = emailConfirmed;
    }

    public Object getProfileImgUrl() {
        return profileImgUrl;
    }

    public void setProfileImgUrl(Object profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
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

    public String getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(String lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    @Override
    public String toString() {
        return "UserDetailsViewModel{" +
                "userID=" + userID +
                ", role=" + role +
                ", companyViewModel=" + companyViewModel +
                ", displayName='" + displayName + '\'' +
                ", emailConfirmed=" + emailConfirmed +
                ", profileImgUrl=" + profileImgUrl +
                ", enabled=" + enabled +
                ", createdDate='" + createdDate + '\'' +
                ", modifiedDate='" + modifiedDate + '\'' +
                ", lastLoginDate='" + lastLoginDate + '\'' +
                '}';
    }
}
