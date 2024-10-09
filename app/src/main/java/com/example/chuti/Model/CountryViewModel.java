package com.example.chuti.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CountryViewModel {
    @SerializedName("countryID")
    @Expose
    private String countryID;
    @SerializedName("countryName")
    @Expose
    private String countryName;
    @SerializedName("langCode")
    @Expose
    private String langCode;

    public String getCountryID() {
        return countryID;
    }

    public void setCountryID(String countryID) {
        this.countryID = countryID;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    @Override
    public String toString() {
        return "CountryViewModel{" +
                "countryID='" + countryID + '\'' +
                ", countryName='" + countryName + '\'' +
                ", langCode='" + langCode + '\'' +
                '}';
    }
}
