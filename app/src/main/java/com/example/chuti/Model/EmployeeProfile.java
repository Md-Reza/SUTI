package com.example.chuti.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EmployeeProfile {
    @SerializedName("accountID")
    @Expose
    private Integer accountID;
    @SerializedName("hrEmployeeID")
    @Expose
    private String hrEmployeeID;
    @SerializedName("employeeName")
    @Expose
    private String employeeName;
    @SerializedName("phoneNo")
    @Expose
    private String phoneNo;
    @SerializedName("emailAddress")
    @Expose
    private String emailAddress;
    @SerializedName("joiningDate")
    @Expose
    private String joiningDate;
    @SerializedName("isCompanyHead")
    @Expose
    private Boolean isCompanyHead;
    @SerializedName("reportToAccountID")
    @Expose
    private Integer reportToAccountID;
    @SerializedName("reportToName")
    @Expose
    private String reportToName;
    @SerializedName("policyID")
    @Expose
    private Integer policyID;
    @SerializedName("profileImgUrl")
    @Expose
    private String profileImgUrl;
    @SerializedName("enabled")
    @Expose
    private Boolean enabled;
    @SerializedName("loginID")
    @Expose
    private String loginID;
    @SerializedName("accountType")
    @Expose
    private Integer accountType;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("modifiedDate")
    @Expose
    private String modifiedDate;
    @SerializedName("employeeCatalogViewModel")
    @Expose
    private List<EmployeeCatalogViewModel> employeeCatalogViewModel;

    public Integer getAccountID() {
        return accountID;
    }

    public void setAccountID(Integer accountID) {
        this.accountID = accountID;
    }

    public String getHrEmployeeID() {
        return hrEmployeeID;
    }

    public void setHrEmployeeID(String hrEmployeeID) {
        this.hrEmployeeID = hrEmployeeID;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(String joiningDate) {
        this.joiningDate = joiningDate;
    }

    public Boolean getCompanyHead() {
        return isCompanyHead;
    }

    public void setCompanyHead(Boolean companyHead) {
        isCompanyHead = companyHead;
    }

    public Integer getReportToAccountID() {
        return reportToAccountID;
    }

    public void setReportToAccountID(Integer reportToAccountID) {
        this.reportToAccountID = reportToAccountID;
    }

    public String getReportToName() {
        return reportToName;
    }

    public void setReportToName(String reportToName) {
        this.reportToName = reportToName;
    }

    public Integer getPolicyID() {
        return policyID;
    }

    public void setPolicyID(Integer policyID) {
        this.policyID = policyID;
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

    public String getLoginID() {
        return loginID;
    }

    public void setLoginID(String loginID) {
        this.loginID = loginID;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
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

    public List<EmployeeCatalogViewModel> getEmployeeCatalogViewModel() {
        return employeeCatalogViewModel;
    }

    public void setEmployeeCatalogViewModel(List<EmployeeCatalogViewModel> employeeCatalogViewModel) {
        this.employeeCatalogViewModel = employeeCatalogViewModel;
    }

    @Override
    public String toString() {
        return "EmployeeProfile{" +
                "accountID=" + accountID +
                ", hrEmployeeID='" + hrEmployeeID + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", joiningDate='" + joiningDate + '\'' +
                ", isCompanyHead=" + isCompanyHead +
                ", reportToAccountID=" + reportToAccountID +
                ", reportToName='" + reportToName + '\'' +
                ", policyID=" + policyID +
                ", profileImgUrl='" + profileImgUrl + '\'' +
                ", enabled=" + enabled +
                ", loginID='" + loginID + '\'' +
                ", accountType=" + accountType +
                ", createdDate='" + createdDate + '\'' +
                ", modifiedDate='" + modifiedDate + '\'' +
                ", employeeCatalogViewModel=" + employeeCatalogViewModel +
                '}';
    }
}
