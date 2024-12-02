package com.example.chuti.Dto;

public class OutpassDto {
    public String FromTime;
    public String ToTime;
    public String Reason;
    public boolean IsFullDay;
    public String getFromTime() {
        return FromTime;
    }

    public void setFromTime(String fromTime) {
        FromTime = fromTime;
    }

    public String getToTime() {
        return ToTime;
    }

    public void setToTime(String toTime) {
        ToTime = toTime;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public boolean isFullDay() {
        return IsFullDay;
    }

    public void setFullDay(boolean fullDay) {
        IsFullDay = fullDay;
    }

    @Override
    public String toString() {
        return "OutpassDto{" +
                "FromTime='" + FromTime + '\'' +
                ", ToTime='" + ToTime + '\'' +
                ", Reason='" + Reason + '\'' +
                ", IsFullDay=" + IsFullDay +
                '}';
    }
}
