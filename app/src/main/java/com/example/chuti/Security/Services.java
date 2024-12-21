package com.example.chuti.Security;

import com.example.chuti.Dto.ApproveLeaveRequestDto;
import com.example.chuti.Dto.LoginDto;
import com.example.chuti.Dto.OutpassDto;
import com.example.chuti.Dto.ResetPasswordDto;
import com.example.chuti.Dto.SaveLeaveRequestDto;
import com.example.chuti.Dto.UpdateEmployeeSelfDto;
import com.example.chuti.Model.Announcement;
import com.example.chuti.Model.EmployeeAccount;
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

    @GET("LeaveService/{companyID}/LeaveRequestsForApproval/{accountID}")
    Call<List<LeaveRequestsViewModel>> GetLeaveRequestsForApprovalAsync(
            @Header("Authorization") String authHeader,
            @Header("AppKey") String AppKey,
            @Path("companyID") String companyID,
            @Path("accountID") String accountID
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

    @DELETE("GateService/{companyID}/DeleteOutpass/{accountID}/{outpassID}")
    Call<String> DeleteOutpassRequestAsync(
            @Header("Authorization") String authHeader,
            @Header("AppKey") String AppKey,
            @Path("companyID") String companyID,
            @Path("accountID") String accountID,
            @Path("outpassID") String outpassID
    );

    @GET("EmployeeService/{companyID}/User/{accountID}")
    Call<EmployeeAccount> GetUserAsync(
            @Header("Authorization") String authHeader,
            @Header("AppKey") String AppKey,
            @Path("companyID") String companyID,
            @Path("accountID") String accountID
    );

    @GET("GateService/{companyID}/OutpassRequest/{accountID}")
    Call<OutPassViewModel> GetOutpassRequestAsync(
            @Header("Authorization") String authHeader,
            @Header("AppKey") String AppKey,
            @Path("companyID") String companyID,
            @Path("accountID") String accountID
    );

    @GET("GateService/{companyID}/OutpassRequestsForApproval/{accountID}")
    Call<List<OutPassViewModel>> GetOutpassRequestsForApprovalAsync(
            @Header("Authorization") String authHeader,
            @Header("AppKey") String AppKey,
            @Path("companyID") String companyID,
            @Path("accountID") String accountID
    );

    @GET("GateService/{companyID}/ApproveOutpassRequest/{accountID}/{outpassID}")
    Call<String> ApproveOutpassRequestAsync(
            @Header("Authorization") String authHeader,
            @Header("AppKey") String AppKey,
            @Path("companyID") String companyID,
            @Path("accountID") String accountID,
            @Path("outpassID") String outpassID
    );

    @GET("GateService/{companyID}/RejectOutpassRequest/{accountID}/{outpassID}")
    Call<String> RejectOutpassRequestAsync(
            @Header("Authorization") String authHeader,
            @Header("AppKey") String AppKey,
            @Path("companyID") String companyID,
            @Path("accountID") String accountID,
            @Path("outpassID") String outpassID
    );

    @POST("LeaveService/{companyID}/ApproveLeaveRequest")
    Call<String> ApproveLeaveRequestAsync(
            @Header("Authorization") String authHeader,
            @Header("AppKey") String AppKey,
            @Path("companyID") String companyID,
            @Body ApproveLeaveRequestDto approveLeaveRequestDto
    );

    @POST("LeaveService/{companyID}/RejectLeaveRequest")
    Call<String> RejectLeaveRequestAsync(
            @Header("Authorization") String authHeader,
            @Header("AppKey") String AppKey,
            @Path("companyID") String companyID,
            @Body ApproveLeaveRequestDto approveLeaveRequestDto
    );

    @POST("EmployeeService/{companyID}/ChangePassword/{accountID}")
    Call<String> ChangePasswordAsync(
            @Header("Authorization") String authHeader,
            @Header("AppKey") String AppKey,
            @Path("companyID") String companyID,
            @Path("accountID") String accountID,
            @Body ResetPasswordDto resetPasswordDto
    );

    @GET("LeaveService/{companyID}/LeaveRequest/{leaveRequestID}")
    Call<LeaveRequestsViewModel> GetLeaveRequestsForApprovalByIDAsync(
            @Header("Authorization") String authHeader,
            @Header("AppKey") String AppKey,
            @Path("companyID") String companyID,
            @Path("leaveRequestID") String leaveRequestID
    );

    @GET("GateService/{companyID}/ExecuteOutpassInOut/{accountID}/{outpassID}")
    Call<String> ExecuteOutpassInOutAsync(
            @Header("Authorization") String authHeader,
            @Header("AppKey") String AppKey,
            @Path("companyID") String companyID,
            @Path("accountID") String accountID,
            @Path("outpassID") String outpassID
    );

    @GET("AnnouncementService/{companyID}/Announcements/{year}")
    Call<List<Announcement>> GetAnnouncementsAsync(
            @Header("Authorization") String authHeader,
            @Header("AppKey") String AppKey,
            @Path("companyID") String companyID,
            @Path("year") int year
    );

    @GET("AnnouncementService/{companyID}/AnnouncementDetail/{announcementID}")
    Call<Announcement> GetAnnouncementAsync(
            @Header("Authorization") String authHeader,
            @Header("AppKey") String AppKey,
            @Path("companyID") String companyID,
            @Path("announcementID") int announcementID
    );

    @POST("EmployeeService/{companyID}/UpdateEmployeeSelf/{accountID}")
    Call<String> UpdateEmployeeSelfAsync(
            @Header("Authorization") String authHeader,
            @Header("AppKey") String AppKey,
            @Path("companyID") String companyID,
            @Path("accountID") String accountID,
            @Body UpdateEmployeeSelfDto updateEmployeeSelfDto
    );

}
