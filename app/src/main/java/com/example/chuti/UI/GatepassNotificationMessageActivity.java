package com.example.chuti.UI;

import static com.example.chuti.FragmentManager.FragmentManager.intentActivity;
import static com.example.chuti.FragmentManager.FragmentManager.replaceFragment;
import static com.example.chuti.Handlers.DateFormatterHandlers.ConvertDateToTime;
import static com.example.chuti.Handlers.DateFormatterHandlers.DateTimeParseFormatter;
import static com.example.chuti.Handlers.SMessageHandler.SAlertError;
import static com.example.chuti.Handlers.SMessageHandler.SAlertSuccess;
import static com.example.chuti.Handlers.SMessageHandler.SConnectionFail;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.chuti.FragmentMain;
import com.example.chuti.MainActivity;
import com.example.chuti.Model.OutPassViewModel;
import com.example.chuti.Model.RemoteMessageViewModel;
import com.example.chuti.Model.ServiceResponseViewModel;
import com.example.chuti.R;
import com.example.chuti.Security.BaseURL;
import com.example.chuti.Security.Services;
import com.example.chuti.Security.SharedPref;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GatepassNotificationMessageActivity extends AppCompatActivity {

    Services retrofitApiInterface;
    Gson gson;
    String token, accountID, companyID;
    SpotsDialog spotsDialog;
    ServiceResponseViewModel serviceResponseViewModel = new ServiceResponseViewModel();
    String userID, appKey;
    Intent intent;
    RemoteMessageViewModel requestData;

    public TextView
            txtDuration,
            txtFromTime,
            txtToTime,
            txtOutGate,
            txtReason,
            txtReqDate,
            txtStatusCode, txtEmployeeName, txtJoinedDate,
            txtInGate;
    LinearLayout btnReject, btnApprove;
    OutPassViewModel leaveRequestsViewModel = new OutPassViewModel();

    AlertDialog.Builder builder;
    AlertDialog alert;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gate_notification_message);

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

        toolbar = findViewById(R.id.gateToolbar);
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setTitle("Pending Outpass Request");
        toolbar.setSubtitle("");
        toolbar.setNavigationOnClickListener(v -> intentActivity(new MainActivity(), this));

        txtDuration = findViewById(R.id.txtDuration);
        txtFromTime = findViewById(R.id.txtFromTime);
        txtToTime = findViewById(R.id.txtToTime);
        txtOutGate = findViewById(R.id.txtOutGate);
        txtInGate = findViewById(R.id.txtInGate);
        txtReason = findViewById(R.id.txtReason);
        txtReqDate = findViewById(R.id.txtReqDate);
        txtEmployeeName = findViewById(R.id.txtEmployeeName);
        txtJoinedDate = findViewById(R.id.txtJoinedDate);
        btnApprove = findViewById(R.id.btnApprove);
        btnReject = findViewById(R.id.btnReject);

        intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            requestData = getIntent().getParcelableExtra("remoteMessageViewModel");// Retrieve the data passed from the notification
            if (requestData.getRequestType() != null) {
                System.out.println("push requestData: " + requestData);
                GetEmployeeOutPassApproval();
            }
        }


        btnApprove.setOnClickListener(v -> {
            String outPassID = leaveRequestsViewModel.getOutPassID().toString();
            builder.setTitle("Chuti Aleart");
            builder.setMessage("Are You Sure to Approve Outpass Request ID: " + outPassID + "?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> {
                        ApproveOutPassRequest(outPassID);
                    })
                    .setNegativeButton("No", (dialog, id) -> {
                        dialog.cancel();
                    });
            alert = builder.create();
            alert.show();

        });

        btnReject.setOnClickListener(v -> {
            String outPassID = leaveRequestsViewModel.getOutPassID().toString();
            builder.setTitle("Chuti Aleart");
            builder.setMessage("Are You Sure to Approve Outpass Request ID: " + outPassID + "?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> {
                        RejectOutPassRequest(outPassID);
                    })
                    .setNegativeButton("No", (dialog, id) -> {
                        dialog.cancel();
                    });
            alert = builder.create();
            alert.show();

        });

    }

    private void GetEmployeeOutPassApproval() {
        try {
            Call<OutPassViewModel> getContToLocCall = retrofitApiInterface.GetOutpassRequestAsync("Bearer" + " " + token, appKey, companyID, requestData.getRequestId().toString());
            getContToLocCall.enqueue(new Callback<OutPassViewModel>() {
                @Override
                public void onResponse(Call<OutPassViewModel> call, Response<OutPassViewModel> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                spotsDialog.dismiss();
                                try {
                                    int hours = leaveRequestsViewModel.getDurationMin() / 60;  // Divide total minutes by 60 to get hours
                                    int minutes = leaveRequestsViewModel.getDurationMin() % 60;
                                    txtDuration.setText(String.format("%dh,%dm", hours, minutes));
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }

                                switch (leaveRequestsViewModel.getStatus()) {
                                    case 0:
                                        txtStatusCode.setText(R.string.created);
                                        txtStatusCode.setBackgroundResource(R.drawable.created_button);
                                        break;
                                    case 2:
                                        txtStatusCode.setText(R.string.approved);
                                        txtStatusCode.setBackgroundResource(R.drawable.approved_button);
                                        break;
                                    case 3:
                                        txtStatusCode.setText(R.string.rejected);
                                        txtStatusCode.setBackgroundResource(R.drawable.reject_button);
                                        break;

                                    default:
                                        break;
                                }

                                try {
                                    txtEmployeeName.setText(leaveRequestsViewModel.getEmployeeCompactViewModel().getEmployeeName() + "(" + leaveRequestsViewModel.getEmployeeCompactViewModel().getHrEmployeeID() + ")");
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    txtJoinedDate.setText("Joined Date: " + DateTimeParseFormatter(leaveRequestsViewModel.getEmployeeCompactViewModel().getJoiningDate()));
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    txtFromTime.setText(String.valueOf(ConvertDateToTime(leaveRequestsViewModel.getFromTime())));
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    txtToTime.setText(String.valueOf(ConvertDateToTime(leaveRequestsViewModel.getToTime())));
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    txtOutGate.setText(leaveRequestsViewModel.getOutGateCode());
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    txtInGate.setText(leaveRequestsViewModel.getInGateCode());
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    txtReason.setText(leaveRequestsViewModel.getReason());
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    txtReqDate.setText("Request Date: " + DateTimeParseFormatter(leaveRequestsViewModel.getRequestDate()));
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
                                    SAlertError(serviceResponseViewModel.getMessage(), GatepassNotificationMessageActivity.this);
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
                public void onFailure(Call<OutPassViewModel> call, Throwable t) {
                    SAlertError(t.getMessage(), GatepassNotificationMessageActivity.this);
                    spotsDialog.dismiss();
                }
            });
        } catch (Exception e) {
            spotsDialog.dismiss();
            e.printStackTrace();
        }
    }

    private void ApproveOutPassRequest(String outPassID) {
        spotsDialog.show();
        Call<String> saveMachineCall = retrofitApiInterface.ApproveOutpassRequestAsync("Bearer " + token, appKey, companyID, accountID, outPassID);
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
                                SAlertSuccess(serviceResponseViewModel.getMessage(), GatepassNotificationMessageActivity.this);
                                GetEmployeeOutPassApproval();
                            }
                        }
                    } else if (!response.isSuccessful()) {
                        if (response.errorBody() != null) {
                            spotsDialog.dismiss();
                            serviceResponseViewModel = new ServiceResponseViewModel();
                            gson = new GsonBuilder().create();
                            try {
                                serviceResponseViewModel = gson.fromJson(response.errorBody().string(), ServiceResponseViewModel.class);
                                SAlertError(serviceResponseViewModel.getMessage(), GatepassNotificationMessageActivity.this);
                            } catch (Exception e) {
                                SAlertError(e.getMessage(), GatepassNotificationMessageActivity.this);
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
                SConnectionFail(t.getMessage(), GatepassNotificationMessageActivity.this);
            }
        });
    }

    private void RejectOutPassRequest(String outPassID) {
        spotsDialog.show();
        Call<String> saveMachineCall = retrofitApiInterface.RejectOutpassRequestAsync("Bearer " + token, appKey, companyID, accountID, outPassID);
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
                                SAlertSuccess(serviceResponseViewModel.getMessage(), GatepassNotificationMessageActivity.this);
                                GetEmployeeOutPassApproval();
                            }
                        }
                    } else if (!response.isSuccessful()) {
                        if (response.errorBody() != null) {
                            spotsDialog.dismiss();
                            serviceResponseViewModel = new ServiceResponseViewModel();
                            gson = new GsonBuilder().create();
                            try {
                                serviceResponseViewModel = gson.fromJson(response.errorBody().string(), ServiceResponseViewModel.class);
                                SAlertError(serviceResponseViewModel.getMessage(), GatepassNotificationMessageActivity.this);
                            } catch (Exception e) {
                                SAlertError(e.getMessage(), GatepassNotificationMessageActivity.this);
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
                SConnectionFail(t.getMessage(), GatepassNotificationMessageActivity.this);
            }
        });
    }
}