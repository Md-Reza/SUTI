package com.example.chuti.Dto;

public class SaveLeaveRequestDto {
    public String LeaveCatalogID;
    public String FromDate;
    public String ToDate;
    public long NoOfDay;
    public String Reason;
    public String DocumentName;
    public String DocumentString;

    public String getLeaveCatalogID() {
        return LeaveCatalogID;
    }

    public void setLeaveCatalogID(String leaveCatalogID) {
        LeaveCatalogID = leaveCatalogID;
    }

    public String getFromDate() {
        return FromDate;
    }

    public void setFromDate(String fromDate) {
        FromDate = fromDate;
    }

    public String getToDate() {
        return ToDate;
    }

    public void setToDate(String toDate) {
        ToDate = toDate;
    }

    public long getNoOfDay() {
        return NoOfDay;
    }

    public void setNoOfDay(long noOfDay) {
        NoOfDay = noOfDay;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public String getDocumentName() {
        return DocumentName;
    }

    public void setDocumentName(String documentName) {
        DocumentName = documentName;
    }

    public String getDocumentString() {
        return DocumentString;
    }

    public void setDocumentString(String documentString) {
        DocumentString = documentString;
    }

    @Override
    public String toString() {
        return "SaveLeaveRequestDto{" +
                "LeaveCatalogID='" + LeaveCatalogID + '\'' +
                ", FromDate='" + FromDate + '\'' +
                ", ToDate='" + ToDate + '\'' +
                ", NoOfDay=" + NoOfDay +
                ", Reason='" + Reason + '\'' +
                ", DocumentName='" + DocumentName + '\'' +
                ", DocumentString='" + DocumentString + '\'' +
                '}';
    }
}
