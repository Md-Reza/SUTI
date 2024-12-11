package com.example.chuti.Dto;

public class ResetPasswordDto {
    public String ConfirmPassword;
    public String NewPassword;
    public String OldPassword;

    public String getConfirmPassword() {
        return ConfirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        ConfirmPassword = confirmPassword;
    }

    public String getNewPassword() {
        return NewPassword;
    }

    public void setNewPassword(String newPassword) {
        NewPassword = newPassword;
    }

    public String getOldPassword() {
        return OldPassword;
    }

    public void setOldPassword(String oldPassword) {
        OldPassword = oldPassword;
    }

    @Override
    public String toString() {
        return "ResetPasswordDto{" +
                "ConfirmPassword='" + ConfirmPassword + '\'' +
                ", NewPassword='" + NewPassword + '\'' +
                ", OldPassword='" + OldPassword + '\'' +
                '}';
    }
}
