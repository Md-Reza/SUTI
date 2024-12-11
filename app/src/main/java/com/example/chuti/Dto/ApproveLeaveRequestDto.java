package com.example.chuti.Dto;

public class ApproveLeaveRequestDto {
    private String ApproverAccountID;
    public String LeaveRequestID;
    public String RejectComment;

    public String getApproverAccountID() {
        return ApproverAccountID;
    }

    public void setApproverAccountID(String approverAccountID) {
        ApproverAccountID = approverAccountID;
    }

    public String getLeaveRequestID() {
        return LeaveRequestID;
    }

    public void setLeaveRequestID(String leaveRequestID) {
        LeaveRequestID = leaveRequestID;
    }

    public String getRejectComment() {
        return RejectComment;
    }

    public void setRejectComment(String rejectComment) {
        RejectComment = rejectComment;
    }

    @Override
    public String toString() {
        return "ApproveLeaveRequestDto{" +
                "ApproverAccountID='" + ApproverAccountID + '\'' +
                ", LeaveRequestID='" + LeaveRequestID + '\'' +
                ", RejectComment='" + RejectComment + '\'' +
                '}';
    }
}
