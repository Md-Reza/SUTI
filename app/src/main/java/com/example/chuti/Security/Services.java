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

    @GET("EmployeeCatalog/{companyID}/CurrentEmployeeCatalog/{accountID}")
    Call<List<EmployeeLeaveCatalogViewModel>> GetEmployeeLeaveCatalogAsync(
            @Header("Authorization") String authHeader,
            @Header("AppKey") String AppKey,
            @Path("companyID") String companyID,
            @Path("accountID") String userID
    );
    @GET("LeaveCatalog/{companyID}/LeaveRequests/{accountID}/{year}")
    Call<List<EmployeeLeaveRequestViewModel>> GetEmployeeLeaveRequestAsync(
            @Header("Authorization") String authHeader,
            @Header("AppKey") String AppKey,
            @Path("companyID") String companyID,
            @Path("accountID") String accountID,
            @Path("year") long year
    );
}
