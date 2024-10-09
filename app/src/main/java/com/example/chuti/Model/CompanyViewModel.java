package com.example.chuti.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompanyViewModel {
    @SerializedName("companyID")
    @Expose
    private Integer companyID;
    @SerializedName("companyShortText")
    @Expose
    private String companyShortText;
    @SerializedName("countryViewModel")
    @Expose
    private CountryViewModel countryViewModel;
    @SerializedName("companyName")
    @Expose
    private String companyName;
    @SerializedName("emailAdd")
    @Expose
    private Object emailAdd;
    @SerializedName("hasProAccess")
    @Expose
    private Boolean hasProAccess;
    @SerializedName("enabled")
    @Expose
    private Boolean enabled;
    @SerializedName("logoUrl")
    @Expose
    private Object logoUrl;
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

    public String getCompanyShortText() {
        return companyShortText;
    }

    public void setCompanyShortText(String companyShortText) {
        this.companyShortText = companyShortText;
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

    public Object getEmailAdd() {
        return emailAdd;
    }

    public void setEmailAdd(Object emailAdd) {
        this.emailAdd = emailAdd;
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

    public Object getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(Object logoUrl) {
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

}
