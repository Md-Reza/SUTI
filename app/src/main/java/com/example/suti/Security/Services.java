package com.example.suti.Security;

import com.example.suti.Dto.LoginDto;
import com.example.suti.Model.LoginSuccessViewModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface Services {
    @POST("SAPI/AuthService/Login")
    Call<LoginSuccessViewModel> Login(
            @Header("ClientID") String ClientID,
            @Header("ClientVersion") String ClientVersion,
            @Header("DeviceName") String DeviceName,
            @Body LoginDto loginDto
    );
}
