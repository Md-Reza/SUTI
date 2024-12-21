package com.example.chuti.UI;


import static com.example.chuti.FragmentManager.FragmentManager.intentActivity;
import static com.example.chuti.Handlers.DateFormatterHandlers.DateTimeParseFormatter;
import static com.example.chuti.Handlers.DateFormatterHandlers.DateTimeParseMonthYearFormatter;
import static com.example.chuti.Handlers.SMessageHandler.SAlertError;
import static com.example.chuti.Handlers.SMessageHandler.SAlertSuccess;
import static com.example.chuti.Handlers.SMessageHandler.SConnectionFail;

import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.chuti.Dto.ApproveLeaveRequestDto;
import com.example.chuti.MainActivity;
import com.example.chuti.Model.LeaveRequestsViewModel;
import com.example.chuti.Model.ServiceResponseViewModel;
import com.example.chuti.R;
import com.example.chuti.Security.BaseURL;
import com.example.chuti.Security.Services;
import com.example.chuti.Security.SharedPref;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaveNotificationMessageActivity extends AppCompatActivity {

    Services retrofitApiInterface;
    Gson gson;
    String token, accountID, companyID;
    SpotsDialog spotsDialog;
    ServiceResponseViewModel serviceResponseViewModel = new ServiceResponseViewModel();
    String userID, appKey;

    public TextView
            txtLeaveName,
            txtEmployeeName,
            txtReqDate,
            txtToDate,
            txtFromDate,
            txtNoOfDays,
            txtReason,
            txtLeaveType;
    LinearLayout btnReject, btnApprove;

    LeaveRequestsViewModel leaveRequestsViewModel;

    AlertDialog.Builder builder;
    AlertDialog alert;

    MaterialButton btnSave, btnClose;
    Dialog rejectLeaveDialog;
    View rejectLeaveView;
    TextInputEditText txtRejectReason;
    DisplayMetrics displayMetrics;
    WindowManager.LayoutParams layoutParams;
    ApproveLeaveRequestDto approveLeaveRequestDto = new ApproveLeaveRequestDto();
    Toolbar toolbar;
    String reqID, reqType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_notification_message);

        retrofitApiInterface = BaseURL.getRetrofit().create(Services.class);
        SharedPref.init(this);
        builder = new AlertDialog.Builder(this);
        gson = new Gson();
        spotsDialog = new SpotsDialog(this, R.style.Custom);
        appKey = SharedPref.read("appKey", "");
        token = SharedPref.read("token", "");
        companyID = SharedPref.read("companyID", "");
        accountID = SharedPref.read("accountID", "");
        userID = SharedPref.read("uId", "");
        reqType = getIntent().getStringExtra("RequestType");
        reqID = getIntent().getStringExtra("RequestID");

        toolbar = findViewById(R.id.leaveToolbar);
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setTitle("Pending Leave Request");
        toolbar.setSubtitle("");
        toolbar.setNavigationOnClickListener(v -> intentActivity(new MainActivity(), this));

        txtLeaveName = findViewById(R.id.txtLeaveName);
        txtToDate = findViewById(R.id.txtToDate);
        txtFromDate = findViewById(R.id.txtFromDate);
        txtNoOfDays = findViewById(R.id.txtNoOfDays);
        txtLeaveType = findViewById(R.id.txtLeaveType);
        txtReason = findViewById(R.id.txtReason);
        txtEmployeeName = findViewById(R.id.txtEmployeeName);
        txtReqDate = findViewById(R.id.txtReqDate);
        btnReject = findViewById(R.id.btnReject);
        btnApprove = findViewById(R.id.btnApprove);

        System.out.println("push requestData: " + reqID);
        GetEmployeePendingLeaveApproval();

        //Reject Reason Dialog
        rejectLeaveDialog = new Dialog(this);
        rejectLeaveView = getLayoutInflater().inflate(R.layout.reject_leave_dialog, null);
        rejectLeaveDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        btnClose = rejectLeaveView.findViewById(R.id.btnClose);
        btnSave = rejectLeaveView.findViewById(R.id.btnSave);
        txtRejectReason = rejectLeaveView.findViewById(R.id.txtRejectReason);
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(rejectLeaveDialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        rejectLeaveDialog.setContentView(rejectLeaveView);
        rejectLeaveDialog.setCancelable(false);

        btnApprove.setOnClickListener(v -> {
            approveLeaveRequestDto.setLeaveRequestID(reqID);
            approveLeaveRequestDto.setApproverAccountID(accountID);
            builder.setTitle(R.string.chuti_aleart);
            builder.setMessage("Are You Sure to Approve Leave Request ID: " + reqID + "?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> {
                        ApproveLeave(approveLeaveRequestDto);
                    })
                    .setNegativeButton("No", (dialog, id) -> {
                        dialog.cancel();
                    });
            alert = builder.create();
            alert.show();
        });

        btnReject.setOnClickListener(v -> {
            rejectLeaveDialog.setContentView(rejectLeaveView);
            rejectLeaveDialog.setCancelable(true);
            rejectLeaveDialog.show();
            txtRejectReason.setText("");
            txtRejectReason.requestFocus();

            btnSave.setOnClickListener(v1 -> {
                spotsDialog.show();
                approveLeaveRequestDto.setLeaveRequestID(reqID);
                approveLeaveRequestDto.setApproverAccountID(accountID);
                approveLeaveRequestDto.setRejectComment(txtRejectReason.getText().toString());
                Log.i("info", "approveLeaveRequestDto " + approveLeaveRequestDto);
                Call<String> saveMachineCall = retrofitApiInterface.RejectLeaveRequestAsync("Bearer " + token, appKey, companyID, approveLeaveRequestDto);
                saveMachineCall.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        try {
                            if (response.isSuccessful()) {
                                if (response.body() != null) {
                                    spotsDialog.dismiss();
                                    if (response.code() == 200) {
                                        serviceResponseViewModel = gson.fromJson(response.body(), ServiceResponseViewModel.class);
                                        SAlertSuccess(serviceResponseViewModel.getMessage(), LeaveNotificationMessageActivity.this);
                                        rejectLeaveDialog.dismiss();
                                        intentActivity(new MainActivity(), LeaveNotificationMessageActivity.this);
                                    }
                                }
                            } else {
                                if (response.errorBody() != null) {
                                    spotsDialog.dismiss();
                                    serviceResponseViewModel = new ServiceResponseViewModel();
                                    gson = new GsonBuilder().create();
                                    try {
                                        serviceResponseViewModel = gson.fromJson(response.errorBody().string(), ServiceResponseViewModel.class);
                                        SAlertError(serviceResponseViewModel.getMessage(), LeaveNotificationMessageActivity.this);
                                    } catch (Exception e) {
                                        SAlertError(e.getMessage(), LeaveNotificationMessageActivity.this);
                                        e.printStackTrace();
                                    }
                                }
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        spotsDialog.dismiss();
                        SConnectionFail(t.getMessage(), LeaveNotificationMessageActivity.this);
                    }
                });
            });
            btnClose.setOnClickListener(v1 -> rejectLeaveDialog.dismiss());
        });
        // Get the data from the intent
    }

    private void GetEmployeePendingLeaveApproval() {
        try {
            Call<LeaveRequestsViewModel> getContToLocCall = retrofitApiInterface.GetLeaveRequestsForApprovalByIDAsync("Bearer" + " " + token, appKey, companyID, reqID);
            getContToLocCall.enqueue(new Callback<LeaveRequestsViewModel>() {
                @Override
                public void onResponse(Call<LeaveRequestsViewModel> call, Response<LeaveRequestsViewModel> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                spotsDialog.dismiss();
                                leaveRequestsViewModel = response.body();
                                try {
                                    txtLeaveType.setText(String.format("%d %s", leaveRequestsViewModel.getNoOfDays(), leaveRequestsViewModel.getLeaveTypeName()));
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                                try {

                                    if (!reqType.equals("LEAVE")) {
                                        btnApprove.setVisibility(View.GONE);
                                        btnReject.setVisibility(View.GONE);
                                        txtReason.setText(leaveRequestsViewModel.getLastComment());
                                    } else {
                                        btnApprove.setVisibility(View.VISIBLE);
                                        btnReject.setVisibility(View.VISIBLE);
                                        txtReason.setText(leaveRequestsViewModel.getReason());
                                    }

                                    txtLeaveName.setText(leaveRequestsViewModel.getLeaveTypeName());
                                    if (leaveRequestsViewModel.getLeaveTypeName().equals("Casual Leave"))
                                        txtLeaveName.setBackgroundResource(R.drawable.casual_leave_button);
                                    else if (leaveRequestsViewModel.getLeaveTypeName().equals("Earn Leave")) {
                                        txtLeaveName.setBackgroundResource(R.drawable.earn_leave_button);
                                    } else
                                        txtLeaveName.setBackgroundResource(R.drawable.sick_leave_button);
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    txtFromDate.setText(String.valueOf(DateTimeParseMonthYearFormatter(leaveRequestsViewModel.getFromDate())));
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    txtToDate.setText(String.valueOf(DateTimeParseMonthYearFormatter(leaveRequestsViewModel.getToDate())));
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    txtNoOfDays.setText(leaveRequestsViewModel.getNoOfDays() + " Days");
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    txtEmployeeName.setText(leaveRequestsViewModel.getEmployeeCompactViewModel().getEmployeeName() + "[" + leaveRequestsViewModel.getEmployeeCompactViewModel().getHrEmployeeID() + "]");
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    txtReqDate.setText(String.valueOf(DateTimeParseFormatter(leaveRequestsViewModel.getRequestDate())));
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            if (response.errorBody() != null) {
                                spotsDialog.dismiss();
                                gson = new GsonBuilder().create();
                                try {
                                    serviceResponseViewModel = gson.fromJson(response.errorBody().string(), ServiceResponseViewModel.class);
                                    SAlertError(serviceResponseViewModel.getMessage(), LeaveNotificationMessageActivity.this);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } catch (Exception e) {
                        spotsDialog.dismiss();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<LeaveRequestsViewModel> call, Throwable t) {
                    SAlertError(t.getMessage(), LeaveNotificationMessageActivity.this);
                    spotsDialog.dismiss();
                }
            });
        } catch (Exception e) {
            spotsDialog.dismiss();
            e.printStackTrace();
        }
    }

    public void ApproveLeave(ApproveLeaveRequestDto approveLeaveRequestDto) {
        spotsDialog.show();
        Call<String> saveMachineCall = retrofitApiInterface.ApproveLeaveRequestAsync("Bearer " + token, appKey, companyID, approveLeaveRequestDto);
        saveMachineCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            spotsDialog.dismiss();
                            serviceResponseViewModel = new ServiceResponseViewModel();
                            if (response.code() == 200) {
                                serviceResponseViewModel = gson.fromJson(response.body(), ServiceResponseViewModel.class);
                                SAlertSuccess(serviceResponseViewModel.getMessage(), LeaveNotificationMessageActivity.this);
                                intentActivity(new MainActivity(), LeaveNotificationMessageActivity.this);
                            }
                        }
                    } else if (!response.isSuccessful()) {
                        if (response.errorBody() != null) {
                            spotsDialog.dismiss();
                            serviceResponseViewModel = new ServiceResponseViewModel();
                            gson = new GsonBuilder().create();
                            try {
                                serviceResponseViewModel = gson.fromJson(response.errorBody().string(), ServiceResponseViewModel.class);
                                SAlertError(serviceResponseViewModel.getMessage(), LeaveNotificationMessageActivity.this);
                            } catch (Exception e) {
                                SAlertError(e.getMessage(), LeaveNotificationMessageActivity.this);
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                spotsDialog.dismiss();
                SConnectionFail(t.getMessage(), LeaveNotificationMessageActivity.this);
            }
        });
    }

    public void RejectLeave(ApproveLeaveRequestDto approveLeaveRequestDto) {
        spotsDialog.show();
        Call<String> saveMachineCall = retrofitApiInterface.RejectLeaveRequestAsync("Bearer " + token, appKey, companyID, approveLeaveRequestDto);
        saveMachineCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            spotsDialog.dismiss();
                            if (response.code() == 200) {
                                serviceResponseViewModel = gson.fromJson(response.body(), ServiceResponseViewModel.class);
                                SAlertSuccess(serviceResponseViewModel.getMessage(), LeaveNotificationMessageActivity.this);
                                intentActivity(new MainActivity(), LeaveNotificationMessageActivity.this);
                            }
                        }
                    } else {
                        if (response.errorBody() != null) {
                            spotsDialog.dismiss();
                            serviceResponseViewModel = new ServiceResponseViewModel();
                            gson = new GsonBuilder().create();
                            try {
                                serviceResponseViewModel = gson.fromJson(response.errorBody().string(), ServiceResponseViewModel.class);
                                SAlertError(serviceResponseViewModel.getMessage(), LeaveNotificationMessageActivity.this);
                            } catch (Exception e) {
                                SAlertError(e.getMessage(), LeaveNotificationMessageActivity.this);
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                spotsDialog.dismiss();
                SConnectionFail(t.getMessage(), LeaveNotificationMessageActivity.this);
            }
        });
    }

}