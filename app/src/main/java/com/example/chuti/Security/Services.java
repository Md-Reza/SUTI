package com.example.chuti.Security;

import com.example.chuti.Dto.LoginDto;
import com.example.chuti.Dto.OutpassDto;
import com.example.chuti.Dto.SaveLeaveRequestDto;
import com.example.chuti.Model.EmployeeLeaveCatalogViewModel;
import com.example.chuti.Model.EmployeeLeaveRequestViewModel;
import com.example.chuti.Model.EmployeeProfile;
import com.example.chuti.Model.LeaveRequestsViewModel;
import com.example.chuti.Model.LoginSuccessViewModel;
import com.example.chuti.Model.OutPassViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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
    @GET("LeaveService/{companyID}/LeaveRequests/{accountID}/{year}")
    Call<List<LeaveRequestsViewModel>> GetEmployeeLeaveRequestAsync(
            @Header("Authorization") String authHeader,
            @Header("AppKey") String AppKey,
            @Path("companyID") String companyID,
            @Path("accountID") String accountID,
            @Path("year") String year
    );
    @POST("LeaveService/{companyID}/SaveLeaveRequest/{accountID}")
    Call<String> SaveLeaveRequestAsync(
            @Header("Authorization") String authHeader,
            @Header("AppKey") String AppKey,
            @Path("companyID") String companyID,
            @Path("accountID") String accountID,
            @Body SaveLeaveRequestDto saveLeaveRequestDto
    );

    @DELETE("LeaveService/{companyID}/DeleteLeaveRequest/{accountID}/{leaveRequestID}")
    Call<String> DeleteLeaveRequestAsync(
            @Header("Authorization") String authHeader,
            @Header("AppKey") String AppKey,
            @Path("companyID") String companyID,
            @Path("accountID") String accountID,
            @Path("leaveRequestID") String leaveRequestID
    );

    @GET("EmployeeService/{companyID}/Employee/{accountID}")
    Call<EmployeeProfile> GetEmployeeServiceAsync(
            @Header("Authorization") String authHeader,
            @Header("AppKey") String AppKey,
            @Path("companyID") String companyID,
            @Path("accountID") String accountID
    );

    @POST("GateService/{companyID}/SaveOutpass/{accountID}")
    Call<String> SaveOutpassAsync(
            @Header("Authorization") String authHeader,
            @Header("AppKey") String AppKey,
            @Path("companyID") String companyID,
            @Path("accountID") String accountID,
            @Body OutpassDto outpassDto
    );

    @GET("GateService/{companyID}/OutpassRequests/{accountID}/{year}")
    Call<List<OutPassViewModel>> GetOutpassRequestsAsync(
            @Header("Authorization") String authHeader,
            @Header("AppKey") String AppKey,
            @Path("companyID") String companyID,
            @Path("accountID") String accountID,
            @Path("year") int year
    );
}
