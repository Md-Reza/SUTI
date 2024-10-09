package com.example.chuti.Security;

import com.example.chuti.Dto.LoginDto;
import com.example.chuti.Model.EmployeeLeaveCatalogViewModel;
import com.example.chuti.Model.EmployeeLeaveRequestViewModel;
import com.example.chuti.Model.LoginSuccessViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Services {
    @POST("AuthService/Login")
    Call<LoginSuccessViewModel> LoginAsync(
            @Header("AppKey") String AppKey,
            @Body LoginDto loginDto
    );

    @GET("LeaveCatalog/{companyID}/EmployeeCurrentLeaveStatistics/{userID}")
    Call<List<EmployeeLeaveCatalogViewModel>> GetEmployeeLeaveCatalogAsync(
            @Header("Authorization") String authHeader,
            @Header("AppKey") String AppKey,
            @Path("companyID") long companyID,
            @Path("userID") long userID
    );
    @GET("LeaveCatalog/{companyID}/LeaveRequests/{userID}/{year}")
    Call<List<EmployeeLeaveRequestViewModel>> GetEmployeeLeaveRequestAsync(
            @Header("Authorization") String authHeader,
            @Header("AppKey") String AppKey,
            @Path("companyID") long companyID,
            @Path("userID") long userID,
            @Path("year") long year
    );
}
