package com.example.chuti.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompanyViewModel {
    @SerializedName("companyID")
    @Expose
    private Integer companyID;
    @SerializedName("countryViewModel")
    @Expose
    private CountryViewModel countryViewModel;
    @SerializedName("companyCode")
    @Expose
    private String companyCode;
    @SerializedName("companyName")
    @Expose
    private String companyName;
    @SerializedName("emailAddress")
    @Expose
    private String emailAddress;
    @SerializedName("hasProAccess")
    @Expose
    private Boolean hasProAccess;
    @SerializedName("enabled")
    @Expose
    private Boolean enabled;
    @SerializedName("logoUrl")
    @Expose
    private String logoUrl;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("modifiedDate")
    @Expose
    private String modifiedDate;

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public CountryViewModel getCountryViewModel() {
        return countryViewModel;
    }

    public void setCountryViewModel(CountryViewModel countryViewModel) {
        this.countryViewModel = countryViewModel;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }


    public Boolean getHasProAccess() {
        return hasProAccess;
    }

    public void setHasProAccess(Boolean hasProAccess) {
        this.hasProAccess = hasProAccess;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
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

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public String toString() {
        return "CompanyViewModel{" +
                "companyID=" + companyID +
                ", countryViewModel=" + countryViewModel +
                ", companyCode='" + companyCode + '\'' +
                ", companyName='" + companyName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", hasProAccess=" + hasProAccess +
                ", enabled=" + enabled +
                ", logoUrl='" + logoUrl + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", modifiedDate='" + modifiedDate + '\'' +
                '}';
    }
}
