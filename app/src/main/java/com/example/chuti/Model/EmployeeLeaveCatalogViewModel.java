package com.example.chuti.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmployeeLeaveCatalogViewModel {
    @SerializedName("employeeCatalogID")
    @Expose
    private Integer employeeCatalogID;
    @SerializedName("employeePeriodViewModel")
    @Expose
    private EmployeePeriodViewModel employeePeriodViewModel;
    @SerializedName("policyViewModel")
    @Expose
    private PolicyViewModel policyViewModel;
    @SerializedName("policyLeaveViewModel")
    @Expose
    private PolicyLeaveViewModel policyLeaveViewModel;
    @SerializedName("policyInitialDays")
    @Expose
    private Integer policyInitialDays;
    @SerializedName("carryFromLastYear")
    @Expose
    private Integer carryFromLastYear;
    @SerializedName("availableDays")
    @Expose
    private Integer availableDays;
    @SerializedName("adjustDays")
    @Expose
    private Integer adjustDays;
    @SerializedName("onHeldDays")
    @Expose
    private Integer onHeldDays;
    @SerializedName("usedDays")
    @Expose
    private Integer usedDays;

    public Integer getEmployeeCatalogID() {
        return employeeCatalogID;
    }

    public void setEmployeeCatalogID(Integer employeeCatalogID) {
        this.employeeCatalogID = employeeCatalogID;
    }

    public EmployeePeriodViewModel getEmployeePeriodViewModel() {
        return employeePeriodViewModel;
    }

    public void setEmployeePeriodViewModel(EmployeePeriodViewModel employeePeriodViewModel) {
        this.employeePeriodViewModel = employeePeriodViewModel;
    }

    public PolicyViewModel getPolicyViewModel() {
        return policyViewModel;
    }

    public void setPolicyViewModel(PolicyViewModel policyViewModel) {
        this.policyViewModel = policyViewModel;
    }

    public PolicyLeaveViewModel getPolicyLeaveViewModel() {
        return policyLeaveViewModel;
    }

    public void setPolicyLeaveViewModel(PolicyLeaveViewModel policyLeaveViewModel) {
        this.policyLeaveViewModel = policyLeaveViewModel;
    }

    public Integer getPolicyInitialDays() {
        return policyInitialDays;
    }

    public void setPolicyInitialDays(Integer policyInitialDays) {
        this.policyInitialDays = policyInitialDays;
    }

    public Integer getCarryFromLastYear() {
        return carryFromLastYear;
    }

    public void setCarryFromLastYear(Integer carryFromLastYear) {
        this.carryFromLastYear = carryFromLastYear;
    }

    public Integer getAvailableDays() {
        return availableDays;
    }

    public void setAvailableDays(Integer availableDays) {
        this.availableDays = availableDays;
    }

    public Integer getAdjustDays() {
        return adjustDays;
    }

    public void setAdjustDays(Integer adjustDays) {
        this.adjustDays = adjustDays;
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
                "employeeCatalogID=" + employeeCatalogID +
                ", employeePeriodViewModel=" + employeePeriodViewModel +
                ", policyViewModel=" + policyViewModel +
                ", policyLeaveViewModel=" + policyLeaveViewModel +
                ", policyInitialDays=" + policyInitialDays +
                ", carryFromLastYear=" + carryFromLastYear +
                ", availableDays=" + availableDays +
                ", adjustDays=" + adjustDays +
                ", onHeldDays=" + onHeldDays +
                ", usedDays=" + usedDays +
                '}';
    }
}
