package com.example.chuti.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Announcement {
    @SerializedName("announcementID")
    @Expose
    private Integer announcementID;
    @SerializedName("announcementPeriod")
    @Expose
    private Integer announcementPeriod;
    @SerializedName("announcementTitle")
    @Expose
    private String announcementTitle;
    @SerializedName("announcementText")
    @Expose
    private String announcementText;
    @SerializedName("isPublished")
    @Expose
    private Boolean isPublished;
    @SerializedName("publishedDate")
    @Expose
    private String publishedDate;
    @SerializedName("isForAll")
    @Expose
    private Boolean isForAll;
    @SerializedName("modifiedDate")
    @Expose
    private String modifiedDate;

    public Integer getAnnouncementID() {
        return announcementID;
    }

    public void setAnnouncementID(Integer announcementID) {
        this.announcementID = announcementID;
    }

    public Integer getAnnouncementPeriod() {
        return announcementPeriod;
    }

    public void setAnnouncementPeriod(Integer announcementPeriod) {
        this.announcementPeriod = announcementPeriod;
    }

    public String getAnnouncementTitle() {
        return announcementTitle;
    }

    public void setAnnouncementTitle(String announcementTitle) {
        this.announcementTitle = announcementTitle;
    }

    public String getAnnouncementText() {
        return announcementText;
    }

    public void setAnnouncementText(String announcementText) {
        this.announcementText = announcementText;
    }

    public Boolean getPublished() {
        return isPublished;
    }

    public void setPublished(Boolean published) {
        isPublished = published;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public Boolean getForAll() {
        return isForAll;
    }

    public void setForAll(Boolean forAll) {
        isForAll = forAll;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @Override
    public String toString() {
        return "Announcement{" +
                "announcementID=" + announcementID +
                ", announcementPeriod=" + announcementPeriod +
                ", announcementTitle='" + announcementTitle + '\'' +
                ", announcementText='" + announcementText + '\'' +
                ", isPublished=" + isPublished +
                ", publishedDate='" + publishedDate + '\'' +
                ", isForAll=" + isForAll +
                ", modifiedDate='" + modifiedDate + '\'' +
                '}';
    }
}
