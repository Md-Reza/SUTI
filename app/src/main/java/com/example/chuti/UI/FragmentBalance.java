package com.example.chuti.UI;

import static com.example.chuti.FragmentManager.FragmentManager.replaceFragment;
import static com.example.chuti.FragmentManager.FragmentManager.replaceFragmentWithMultipleBundle;
import static com.example.chuti.Handlers.DateFormatterHandlers.ConvertDateToTime;
import static com.example.chuti.Handlers.DateFormatterHandlers.CurrentOffsetTimeParser;
import static com.example.chuti.Handlers.DateFormatterHandlers.DateTimeParseFormatter;
import static com.example.chuti.Handlers.DateFormatterHandlers.DateTimeParseMonthYearFormatter;
import static com.example.chuti.Handlers.DateFormatterHandlers.OffsetTimeParser;
import static com.example.chuti.Handlers.SMessageHandler.SAlertError;
import static com.example.chuti.Handlers.SMessageHandler.SAlertSuccess;
import static com.example.chuti.Handlers.SMessageHandler.SConnectionFail;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.chuti.Dto.ApproveLeaveRequestDto;
import com.example.chuti.FragmentMain;
import com.example.chuti.Model.EmployeeLeaveCatalogViewModel;
import com.example.chuti.Model.LeaveRequestsViewModel;
import com.example.chuti.Model.OutPassViewModel;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentBalance extends Fragment {
    Services retrofitApiInterface;
    Gson gson;
    String token, accountID, companyID;
    SpotsDialog spotsDialog;
    ServiceResponseViewModel serviceResponseViewModel = new ServiceResponseViewModel();
    List<EmployeeLeaveCatalogViewModel> employeeLeaveCatalogViewModel = new ArrayList<>();
    ProgressBar stats_progressbarCasualLeave, stats_progressbarSick, stats_progressbarEarnLeave;
    String userID, appKey, periodYear, outPassID;
    TextView txtCasualLeaveName, txtCasualLeaveBalanceStatus, txtNofSickLeave,
            txtSickLeaveName, txtEarnLeaveName, txtNoOfEarnLeave,
            txtLeaveCounter, txtGatePass, txtLeaveApproval, txtGatePassCount, txtPendingApproval, txtOutPassApproval, txtOutPassApprovalCounter,
            txtPendingApprovalCounter;
    EmployeeLeaveRequestAdapter employeeLeaveRequestAdapter;
    EmployeeOutPassRequestAdapter employeeOutPassRequestAdapter;
    EmployeeOutPassApprovalAdapter employeeOutPassApprovalAdapter;
    EmployeePendingLeaveApprovalAdapter employeePendingLeaveApprovalAdapter;
    RecyclerView employeeLeaveRequestRecyclerView, outPassRecycalerView, outPassApprovalRecycalerView, pendingLeaveApprovalRecycalerView;
    Button btnRequestLeave;
    LinearLayout layoutLeaveApproval, layoutGatePass, layoutOutPassApproval, layoutPendingLeaveApproval;
    Toolbar toolbar;
    int currentYear;
    List<OutPassViewModel> leaveRequestsViewModelList = new ArrayList<>();

    AlertDialog.Builder builder;
    AlertDialog alert;

    MaterialButton btnSave, btnClose;
    Dialog rejectLeaveDialog;
    View rejectLeaveView;
    TextInputEditText txtRejectReason;
    DisplayMetrics displayMetrics;
    WindowManager.LayoutParams layoutParams;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_balance, container, false);
        retrofitApiInterface = BaseURL.getRetrofit().create(Services.class);
        SharedPref.init(getContext());
        builder = new AlertDialog.Builder(getContext());
        gson = new Gson();
        spotsDialog = new SpotsDialog(getContext(), R.style.Custom);
        appKey = SharedPref.read("appKey", "");
        token = SharedPref.read("token", "");
        companyID = SharedPref.read("companyID", "");
        accountID = SharedPref.read("accountID", "");
        userID = SharedPref.read("uId", "");

        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setTitle("Dashboard");
        toolbar.setSubtitle("");
        toolbar.setNavigationOnClickListener(v -> {
            toolbar.setTitle(R.string.chuti);
            replaceFragment(new FragmentMain(), getContext());
        });

        stats_progressbarCasualLeave = root.findViewById(R.id.stats_progressbarCasualLeave);
        stats_progressbarSick = root.findViewById(R.id.stats_progressbarSick);
        stats_progressbarEarnLeave = root.findViewById(R.id.stats_progressbarEarnLeave);
        txtCasualLeaveName = root.findViewById(R.id.txtCasualLeaveName);
        txtCasualLeaveBalanceStatus = root.findViewById(R.id.txtCasualLeaveBalanceStatus);
        txtNofSickLeave = root.findViewById(R.id.txtNofSickLeave);
        txtSickLeaveName = root.findViewById(R.id.txtSickLeaveName);
        txtEarnLeaveName = root.findViewById(R.id.txtEarnLeaveName);
        txtNoOfEarnLeave = root.findViewById(R.id.txtNoOfEarnLeave);
        employeeLeaveRequestRecyclerView = root.findViewById(R.id.employeeLeaveRequestRecyclerView);
        btnRequestLeave = root.findViewById(R.id.btnRequestLeave);
        txtLeaveCounter = root.findViewById(R.id.txtLeaveCounter);
        txtGatePass = root.findViewById(R.id.txtGatePass);
        layoutLeaveApproval = root.findViewById(R.id.layoutLeaveApproval);
        outPassRecycalerView = root.findViewById(R.id.outPassRecycalerView);
        txtLeaveApproval = root.findViewById(R.id.txtLeaveApproval);
        layoutGatePass = root.findViewById(R.id.layoutGatePass);
        txtGatePassCount = root.findViewById(R.id.txtGatePassCount);
        txtPendingApproval = root.findViewById(R.id.txtPendingApproval);
        txtPendingApprovalCounter = root.findViewById(R.id.txtPendingApprovalCounter);
        txtOutPassApproval = root.findViewById(R.id.txtOutPassApproval);
        txtOutPassApprovalCounter = root.findViewById(R.id.txtOutPassApprovalCounter);
        layoutOutPassApproval = root.findViewById(R.id.layoutOutPassApproval);
        outPassApprovalRecycalerView = root.findViewById(R.id.outPassApprovalRecycalerView);
        layoutPendingLeaveApproval = root.findViewById(R.id.layoutPendingLeaveApproval);
        pendingLeaveApprovalRecycalerView = root.findViewById(R.id.pendingLeaveApprovalRecycalerView);

        txtGatePass.setOnClickListener(v -> {
            layoutLeaveApproval.setVisibility(View.GONE);
            layoutGatePass.setVisibility(View.VISIBLE);
            txtGatePass.setBackgroundResource(R.drawable.button_background_n);
            txtLeaveApproval.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
            txtPendingApproval.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
            txtOutPassApproval.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
            GetEmployeeOutPass();
        });
        txtLeaveApproval.setOnClickListener(v -> {
            layoutLeaveApproval.setVisibility(View.VISIBLE);
            layoutGatePass.setVisibility(View.GONE);
            layoutPendingLeaveApproval.setVisibility(View.GONE);
            layoutOutPassApproval.setVisibility(View.GONE);
            txtLeaveApproval.setBackgroundResource(R.drawable.button_background_n);
            txtGatePass.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
            txtPendingApproval.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
            txtOutPassApproval.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
            GetEmployeeLeaveRequest();
        });
        txtOutPassApproval.setOnClickListener(v -> {
            layoutOutPassApproval.setVisibility(View.VISIBLE);
            layoutLeaveApproval.setVisibility(View.GONE);
            layoutGatePass.setVisibility(View.GONE);
            layoutPendingLeaveApproval.setVisibility(View.GONE);
            txtOutPassApproval.setBackgroundResource(R.drawable.button_background_n);
            txtGatePass.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
            txtPendingApproval.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
            txtLeaveApproval.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
            GetEmployeeOutPassApproval();
        });
        txtPendingApproval.setOnClickListener(v -> {
            layoutOutPassApproval.setVisibility(View.GONE);
            layoutLeaveApproval.setVisibility(View.GONE);
            layoutGatePass.setVisibility(View.GONE);
            layoutPendingLeaveApproval.setVisibility(View.VISIBLE);
            txtPendingApproval.setBackgroundResource(R.drawable.button_background_n);
            txtGatePass.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
            txtOutPassApproval.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
            txtLeaveApproval.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
            GetEmployeePendingLeaveApproval();
        });

        btnRequestLeave.setOnClickListener(v -> replaceFragment(new RequestLeaveFragment(), getContext()));

        EmployeeCurrentLeaveStatistics();

        Calendar calendar = Calendar.getInstance();

        currentYear = calendar.get(Calendar.YEAR);


        //Reject Reason Dialog
        rejectLeaveDialog = new Dialog(getContext());
        rejectLeaveView = getActivity().getLayoutInflater().inflate(R.layout.reject_leave_dialog, null);
        rejectLeaveDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        btnClose = rejectLeaveView.findViewById(R.id.btnClose);
        btnSave = rejectLeaveView.findViewById(R.id.btnSave);
        txtRejectReason = rejectLeaveView.findViewById(R.id.txtRejectReason);
        displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(rejectLeaveDialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        rejectLeaveDialog.setContentView(rejectLeaveView);
        rejectLeaveDialog.setCancelable(false);

        return root;
    }

    private void EmployeeCurrentLeaveStatistics() {
        try {
            spotsDialog.show();
            Call<List<EmployeeLeaveCatalogViewModel>> getContToLocCall = retrofitApiInterface.GetEmployeeLeaveCatalogAsync("Bearer" + " " + token, appKey, companyID, accountID);
            getContToLocCall.enqueue(new Callback<List<EmployeeLeaveCatalogViewModel>>() {
                @Override
                public void onResponse(Call<List<EmployeeLeaveCatalogViewModel>> call, Response<List<EmployeeLeaveCatalogViewModel>> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                spotsDialog.dismiss();
                                employeeLeaveCatalogViewModel = response.body();

                                for (int i = 0; i < employeeLeaveCatalogViewModel.size(); i++) {
                                    //Calculate the slice size and update the pie chart:
                                    if (employeeLeaveCatalogViewModel.get(i).getPolicyLeaveViewModel().getLeaveTypeViewModel().getLeaveTypeName().equals("Casual Leave")) {
                                        int calsBurned = employeeLeaveCatalogViewModel.get(i).getUsedDays() + employeeLeaveCatalogViewModel.get(i).getOnHeldDays();
                                        int totalLeave = employeeLeaveCatalogViewModel.get(i).getAvailableDays() + employeeLeaveCatalogViewModel.get(i).getUsedDays() + employeeLeaveCatalogViewModel.get(i).getOnHeldDays();
                                        periodYear = employeeLeaveCatalogViewModel.get(i).getEmployeePeriodViewModel().getPeriodYear().toString();
                                        double d = (double) calsBurned / (double) totalLeave;
                                        int progress = (int) (d * 100);
                                        stats_progressbarCasualLeave.setProgress(progress);
                                        txtCasualLeaveName.setText(employeeLeaveCatalogViewModel.get(i).getPolicyLeaveViewModel().getLeaveTypeViewModel().getLeaveTypeName());
                                        txtCasualLeaveBalanceStatus.setText(String.format("%d/%d", employeeLeaveCatalogViewModel.get(i).getUsedDays() + employeeLeaveCatalogViewModel.get(i).getOnHeldDays(), employeeLeaveCatalogViewModel.get(i).getAvailableDays()));
                                    }
                                    //Calculate the slice size and update the pie chart:
                                    if (employeeLeaveCatalogViewModel.get(i).getPolicyLeaveViewModel().getLeaveTypeViewModel().getLeaveTypeName().equals("Sick Leave")) {
                                        int calsBurned = employeeLeaveCatalogViewModel.get(i).getUsedDays() + employeeLeaveCatalogViewModel.get(i).getOnHeldDays();
                                        periodYear = employeeLeaveCatalogViewModel.get(i).getEmployeePeriodViewModel().getPeriodYear().toString();
                                        int totalLeave = employeeLeaveCatalogViewModel.get(i).getAvailableDays() + employeeLeaveCatalogViewModel.get(i).getUsedDays() + employeeLeaveCatalogViewModel.get(i).getOnHeldDays();
                                        double d = (double) calsBurned / (double) totalLeave;
                                        int progress = (int) (d * 100);
                                        stats_progressbarSick.setProgress(progress);
                                        txtSickLeaveName.setText(employeeLeaveCatalogViewModel.get(i).getPolicyLeaveViewModel().getLeaveTypeViewModel().getLeaveTypeName());
                                        txtNofSickLeave.setText(String.format("%d/%d", employeeLeaveCatalogViewModel.get(i).getUsedDays() + employeeLeaveCatalogViewModel.get(i).getOnHeldDays(), employeeLeaveCatalogViewModel.get(i).getAvailableDays()));
                                    }
                                    //Calculate the slice size and update the pie chart:
                                    if (employeeLeaveCatalogViewModel.get(i).getPolicyLeaveViewModel().getLeaveTypeViewModel().getLeaveTypeName().equals("Earn Leave")) {
                                        int calsBurned = employeeLeaveCatalogViewModel.get(i).getUsedDays() + employeeLeaveCatalogViewModel.get(i).getOnHeldDays();
                                        int totalLeave = employeeLeaveCatalogViewModel.get(i).getAvailableDays();
                                        double d = (double) calsBurned / (double) totalLeave;
                                        int progress = (int) (d * 100);
                                        periodYear = employeeLeaveCatalogViewModel.get(i).getEmployeePeriodViewModel().getPeriodYear().toString();
                                        stats_progressbarEarnLeave.setProgress(progress);
                                        txtEarnLeaveName.setText(employeeLeaveCatalogViewModel.get(i).getPolicyLeaveViewModel().getLeaveTypeViewModel().getLeaveTypeName());
                                        txtNoOfEarnLeave.setText(String.format("%d/%d", employeeLeaveCatalogViewModel.get(i).getUsedDays() + employeeLeaveCatalogViewModel.get(i).getOnHeldDays(), employeeLeaveCatalogViewModel.get(i).getAvailableDays()));
                                    }
                                }

                                GetEmployeeLeaveRequest();
                            }
                        } else {
                            if (response.errorBody() != null) {
                                spotsDialog.dismiss();
                                gson = new GsonBuilder().create();
                                try {
                                    serviceResponseViewModel = gson.fromJson(response.errorBody().string(), ServiceResponseViewModel.class);
                                    SAlertError(serviceResponseViewModel.getMessage(), getContext());
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
                public void onFailure(Call<List<EmployeeLeaveCatalogViewModel>> call, Throwable t) {
                    SAlertError(t.getMessage(), getContext());
                    spotsDialog.dismiss();
                }
            });
        } catch (Exception e) {
            spotsDialog.dismiss();
            e.printStackTrace();
        }
    }

    private void GetEmployeeLeaveRequest() {
        try {
            Call<List<LeaveRequestsViewModel>> getContToLocCall = retrofitApiInterface.GetEmployeeLeaveRequestAsync("Bearer" + " " + token, appKey, companyID, accountID, periodYear);
            getContToLocCall.enqueue(new Callback<List<LeaveRequestsViewModel>>() {
                @Override
                public void onResponse(Call<List<LeaveRequestsViewModel>> call, Response<List<LeaveRequestsViewModel>> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                spotsDialog.dismiss();
                                employeeLeaveRequestAdapter = new EmployeeLeaveRequestAdapter(getContext(), response.body());
                                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                employeeLeaveRequestRecyclerView.setLayoutManager(mLayoutManager);
                                employeeLeaveRequestRecyclerView.setAdapter(employeeLeaveRequestAdapter);
                                txtLeaveCounter.setText(String.valueOf(response.body().size()));
                            }
                        } else {
                            if (response.errorBody() != null) {
                                spotsDialog.dismiss();
                                gson = new GsonBuilder().create();
                                try {
                                    txtLeaveCounter.setText("0");
                                    employeeLeaveRequestRecyclerView.setAdapter(null);
                                    employeeLeaveRequestAdapter = null;
                                    serviceResponseViewModel = gson.fromJson(response.errorBody().string(), ServiceResponseViewModel.class);
                                    SAlertError(serviceResponseViewModel.getMessage(), getContext());
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
                public void onFailure(Call<List<LeaveRequestsViewModel>> call, Throwable t) {
                    SAlertError(t.getMessage(), getContext());
                    spotsDialog.dismiss();
                }
            });
        } catch (Exception e) {
            spotsDialog.dismiss();
            e.printStackTrace();
        }
    }

    public class EmployeeLeaveRequestAdapter extends RecyclerView.Adapter<EmployeeLeaveRequestAdapter.ViewHolder> {

        Context context;
        List<LeaveRequestsViewModel> leaveRequestsViewModelList;

        public EmployeeLeaveRequestAdapter(Context context, List<LeaveRequestsViewModel> leaveRequestsViewModelList) {
            this.context = context;
            this.leaveRequestsViewModelList = leaveRequestsViewModelList;
        }

        @NonNull
        @Override
        public EmployeeLeaveRequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.balance_recycalerview, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull EmployeeLeaveRequestAdapter.ViewHolder holder, int position) {
            final LeaveRequestsViewModel leaveRequestsViewModel = leaveRequestsViewModelList.get(position);

            if (position == holder.getAdapterPosition()) {
                holder.btnDelete.setOnClickListener(v -> {
                    String leaveReqID = leaveRequestsViewModel.getLeaveRequestID().toString();
                    DeleteLeaveRequest(leaveReqID);
                });
            }

            try {

                try {
                    holder.txtLeaveType.setText(leaveRequestsViewModel.getNoOfDays()+" "+leaveRequestsViewModel.getLeaveTypeName());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                try {

                    holder.txtLeaveName.setText(leaveRequestsViewModel.getLeaveTypeName());
                    if (leaveRequestsViewModel.getLeaveTypeName().equals(getString(R.string.casual_leave)))
                        holder.txtLeaveName.setBackgroundResource(R.drawable.casual_leave_button);
                    else if (leaveRequestsViewModel.getLeaveTypeName().equals(getString(R.string.earn_leave))) {
                        holder.txtLeaveName.setBackgroundResource(R.drawable.earn_leave_button);
                    } else
                        holder.txtLeaveName.setBackgroundResource(R.drawable.sick_leave_button);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                switch (leaveRequestsViewModel.getStatus()) {
                    case 0:
                        holder.txtStatusCode.setText(R.string.created);
                        holder.txtStatusCode.setBackgroundResource(R.drawable.created_button);
                        break;
                    case 2:
                        holder.txtStatusCode.setText(R.string.approved);
                        holder.txtStatusCode.setBackgroundResource(R.drawable.approved_button);
                        holder.btnDelete.setVisibility(View.INVISIBLE);
                        break;

                    case 3:
                        holder.txtStatusCode.setText(R.string.rejected);
                        holder.txtStatusCode.setBackgroundResource(R.drawable.reject_button);
                        break;

                    default:
                        break;
                }

                try {

                    holder.txtFromDate.setText(String.valueOf(DateTimeParseMonthYearFormatter(leaveRequestsViewModel.getFromDate())));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                try {
                    holder.txtToDate.setText(String.valueOf(DateTimeParseMonthYearFormatter(leaveRequestsViewModel.getToDate())));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                try {
                    holder.txtLastComment.setText(leaveRequestsViewModel.getReason());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return leaveRequestsViewModelList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView
                    txtLeaveName,
                    txtToDate,
                    txtFromDate,
                    txtStatusCode,
                    txtLastComment,
                    txtLeaveType;
            LinearLayout btnDelete;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                txtLeaveName = itemView.findViewById(R.id.txtLeaveName);
                txtToDate = itemView.findViewById(R.id.txtToDate);
                txtFromDate = itemView.findViewById(R.id.txtFromDate);
                txtStatusCode = itemView.findViewById(R.id.txtStatusCode);
                txtLeaveType = itemView.findViewById(R.id.txtLeaveType);
                txtLastComment = itemView.findViewById(R.id.txtLastComment);
                btnDelete = itemView.findViewById(R.id.btnDelete);
            }
        }

    }

    private void DeleteLeaveRequest(String leaveReqID) {
        spotsDialog.show();
        Call<String> saveMachineCall = retrofitApiInterface.DeleteLeaveRequestAsync("Bearer " + token, appKey, companyID, accountID, leaveReqID);
        saveMachineCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            spotsDialog.dismiss();
                            if (response.code() == 200) {
                                EmployeeCurrentLeaveStatistics();
                            }
                        }
                    } else if (!response.isSuccessful()) {
                        if (response.errorBody() != null) {
                            spotsDialog.dismiss();
                            serviceResponseViewModel = new ServiceResponseViewModel();
                            gson = new GsonBuilder().create();
                            try {
                                serviceResponseViewModel = gson.fromJson(response.errorBody().string(), ServiceResponseViewModel.class);
                                SAlertError(serviceResponseViewModel.getMessage(), getContext());
                            } catch (Exception e) {
                                SAlertError(e.getMessage(), getContext());
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
                SConnectionFail(t.getMessage(), getContext());
            }
        });
    }

    private void GetEmployeeOutPass() {
        try {
            Call<List<OutPassViewModel>> getContToLocCall = retrofitApiInterface.GetOutpassRequestsAsync("Bearer" + " " + token, appKey, companyID, accountID, currentYear);
            getContToLocCall.enqueue(new Callback<List<OutPassViewModel>>() {
                @Override
                public void onResponse(Call<List<OutPassViewModel>> call, Response<List<OutPassViewModel>> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                spotsDialog.dismiss();
                                leaveRequestsViewModelList = new ArrayList<>(response.body());
                                employeeOutPassRequestAdapter = new EmployeeOutPassRequestAdapter(getContext(), response.body());
                                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                outPassRecycalerView.setLayoutManager(mLayoutManager);
                                outPassRecycalerView.setAdapter(employeeOutPassRequestAdapter);
                                txtGatePassCount.setText(String.valueOf(response.body().size()));
                            }
                        } else {
                            if (response.errorBody() != null) {
                                spotsDialog.dismiss();
                                gson = new GsonBuilder().create();
                                try {
                                    txtGatePassCount.setText("0");
                                    outPassRecycalerView.setAdapter(null);
                                    employeeOutPassRequestAdapter = null;
                                    serviceResponseViewModel = gson.fromJson(response.errorBody().string(), ServiceResponseViewModel.class);
                                    SAlertError(serviceResponseViewModel.getMessage(), getContext());
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
                public void onFailure(Call<List<OutPassViewModel>> call, Throwable t) {
                    SAlertError(t.getMessage(), getContext());
                    spotsDialog.dismiss();
                }
            });
        } catch (Exception e) {
            spotsDialog.dismiss();
            e.printStackTrace();
        }
    }

    public class EmployeeOutPassRequestAdapter extends RecyclerView.Adapter<EmployeeOutPassRequestAdapter.ViewHolder> {
        Context context;
        List<OutPassViewModel> leaveRequestsViewModelList;

        public EmployeeOutPassRequestAdapter(Context context, List<OutPassViewModel> leaveRequestsViewModelList) {
            this.context = context;
            this.leaveRequestsViewModelList = leaveRequestsViewModelList;
        }

        @NonNull
        @Override
        public EmployeeOutPassRequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.my_outpass_request_recycalerview, parent, false);
            return new ViewHolder(view);
        }

        @Override
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void onBindViewHolder(@NonNull EmployeeOutPassRequestAdapter.ViewHolder holder, int position) {
            final OutPassViewModel leaveRequestsViewModel = leaveRequestsViewModelList.get(position);

            if (position == holder.getAdapterPosition()) {
                holder.btnDelete.setOnClickListener(v -> {
                    String outPassID = leaveRequestsViewModel.getOutPassID().toString();
                    DeleteOutPassRequest(outPassID);
                });
            }
            try {

                try {
                    int hours = leaveRequestsViewModel.getDurationMin() / 60;  // Divide total minutes by 60 to get hours
                    int minutes = leaveRequestsViewModel.getDurationMin() % 60;
                    holder.txtDuration.setText(String.format("%dh,%dm", hours, minutes));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                switch (leaveRequestsViewModel.getStatus()) {
                    case 0:
                        holder.txtStatusCode.setText(R.string.created);
                        holder.txtStatusCode.setBackgroundResource(R.drawable.created_button);
                        break;
                    case 2:
                        holder.txtStatusCode.setText(R.string.approved);
                        holder.txtStatusCode.setBackgroundResource(R.drawable.approved_button);
                        holder.btnDelete.setVisibility(View.INVISIBLE);
                        break;
                    case 3:
                        holder.txtStatusCode.setText(R.string.rejected);
                        holder.txtStatusCode.setBackgroundResource(R.drawable.reject_button);
                        break;

                    default:
                        break;
                }

                try {
                    holder.txtFromTime.setText(String.valueOf(OffsetTimeParser(leaveRequestsViewModel.getFromTime())));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                try {
                    holder.txtToTime.setText(String.valueOf(OffsetTimeParser(leaveRequestsViewModel.getToTime())));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                try {
                    holder.txtOutGate.setText(leaveRequestsViewModel.getOutGateCode());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                try {
                    holder.txtInGate.setText(leaveRequestsViewModel.getInGateCode());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                try {
                    holder.txtReason.setText(leaveRequestsViewModel.getReason());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                try {
                    holder.txtReqDate.setText(CurrentOffsetTimeParser(leaveRequestsViewModel.getRequestDate()));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (position == holder.getAdapterPosition()) {
                outPassID = leaveRequestsViewModel.getOutPassID().toString();
            }
            holder.btnView.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString("key", leaveRequestsViewModel.getEmployeeCompactViewModel().getHrEmployeeID());
                bundle.putString("key1", outPassID);
                bundle.putString("key2", leaveRequestsViewModel.getEmployeeCompactViewModel().getEmployeeName());
                bundle.putString("key3", leaveRequestsViewModel.getRequestDate());
                replaceFragmentWithMultipleBundle(new GatePassQRFragment(), getContext(), bundle);
            });
        }

        @Override
        public int getItemCount() {
            return leaveRequestsViewModelList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView
                    txtDuration,
                    txtFromTime,
                    txtToTime,
                    txtOutGate,
                    txtReason,
                    txtReqDate,
                    txtStatusCode,
                    txtInGate;
            LinearLayout btnDelete, btnView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                txtDuration = itemView.findViewById(R.id.txtDuration);
                txtFromTime = itemView.findViewById(R.id.txtFromTime);
                txtToTime = itemView.findViewById(R.id.txtToTime);
                txtStatusCode = itemView.findViewById(R.id.txtStatusCode);
                txtOutGate = itemView.findViewById(R.id.txtOutGate);
                txtInGate = itemView.findViewById(R.id.txtInGate);
                txtReason = itemView.findViewById(R.id.txtReason);
                txtReqDate = itemView.findViewById(R.id.txtReqDate);
                btnDelete = itemView.findViewById(R.id.btnDelete);
                btnView = itemView.findViewById(R.id.btnView);
            }
        }
    }

    private void DeleteOutPassRequest(String outPassID) {
        spotsDialog.show();
        Call<String> saveMachineCall = retrofitApiInterface.DeleteOutpassRequestAsync("Bearer " + token, appKey, companyID, accountID, outPassID);
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
                                SAlertSuccess(serviceResponseViewModel.getMessage(), getContext());
                                GetEmployeeOutPass();
                            }
                        }
                    } else if (!response.isSuccessful()) {
                        if (response.errorBody() != null) {
                            spotsDialog.dismiss();
                            serviceResponseViewModel = new ServiceResponseViewModel();
                            gson = new GsonBuilder().create();
                            try {
                                serviceResponseViewModel = gson.fromJson(response.errorBody().string(), ServiceResponseViewModel.class);
                                SAlertError(serviceResponseViewModel.getMessage(), getContext());
                            } catch (Exception e) {
                                SAlertError(e.getMessage(), getContext());
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
                SConnectionFail(t.getMessage(), getContext());
            }
        });
    }

    // Out Pass Approval
    private void GetEmployeeOutPassApproval() {
        try {
            Call<List<OutPassViewModel>> getContToLocCall = retrofitApiInterface.GetOutpassRequestsForApprovalAsync("Bearer" + " " + token, appKey, companyID, accountID);
            getContToLocCall.enqueue(new Callback<List<OutPassViewModel>>() {
                @Override
                public void onResponse(Call<List<OutPassViewModel>> call, Response<List<OutPassViewModel>> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                spotsDialog.dismiss();
                                employeeOutPassApprovalAdapter = new EmployeeOutPassApprovalAdapter(getContext(), response.body());
                                Log.i("info","res out app "+ response.body());
                                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                outPassApprovalRecycalerView.setLayoutManager(mLayoutManager);
                                outPassApprovalRecycalerView.setAdapter(employeeOutPassApprovalAdapter);
                                txtOutPassApprovalCounter.setText(String.valueOf(response.body().size()));
                            }
                        } else {
                            if (response.errorBody() != null) {
                                spotsDialog.dismiss();
                                gson = new GsonBuilder().create();
                                try {
                                    outPassApprovalRecycalerView.setAdapter(null);
                                    employeeOutPassApprovalAdapter = null;
                                    txtOutPassApprovalCounter.setText("0");
                                    serviceResponseViewModel = gson.fromJson(response.errorBody().string(), ServiceResponseViewModel.class);
                                    SAlertError(serviceResponseViewModel.getMessage(), getContext());
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
                public void onFailure(Call<List<OutPassViewModel>> call, Throwable t) {
                    SAlertError(t.getMessage(), getContext());
                    spotsDialog.dismiss();
                }
            });
        } catch (Exception e) {
            spotsDialog.dismiss();
            e.printStackTrace();
        }
    }

    public class EmployeeOutPassApprovalAdapter extends RecyclerView.Adapter<EmployeeOutPassApprovalAdapter.ViewHolder> {
        Context context;
        List<OutPassViewModel> leaveRequestsViewModelList;

        public EmployeeOutPassApprovalAdapter(Context context, List<OutPassViewModel> leaveRequestsViewModelList) {
            this.context = context;
            this.leaveRequestsViewModelList = leaveRequestsViewModelList;
        }

        @NonNull
        @Override
        public EmployeeOutPassApprovalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.outpass_approval_recycalerview, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull EmployeeOutPassApprovalAdapter.ViewHolder holder, int position) {
            final OutPassViewModel outPassViewModel = leaveRequestsViewModelList.get(position);

            if (position == holder.getAdapterPosition()) {
                holder.btnApprove.setOnClickListener(v -> {
                    String outPassID = outPassViewModel.getOutPassID().toString();
                    ApproveOutPassRequest(outPassID);
                });
            }
            try {

                try {
                    int hours = outPassViewModel.getDurationMin() / 60;  // Divide total minutes by 60 to get hours
                    int minutes = outPassViewModel.getDurationMin() % 60;
                    holder.txtDuration.setText(String.format("%dh,%dm", hours, minutes));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                try {
                    holder.txtEmployeeName.setText(outPassViewModel.getEmployeeCompactViewModel().getEmployeeName() + "(" + outPassViewModel.getEmployeeCompactViewModel().getHrEmployeeID() + ")");
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                try {
                    holder.txtJoinedDate.setText("Joined Date: " + DateTimeParseFormatter(outPassViewModel.getEmployeeCompactViewModel().getJoiningDate()));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                try {
                    holder.txtFromTime.setText(String.valueOf(ConvertDateToTime(outPassViewModel.getFromTime())));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                try {
                    holder.txtToTime.setText(String.valueOf(ConvertDateToTime(outPassViewModel.getToTime())));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                try {
                    holder.txtOutGate.setText(outPassViewModel.getOutGateCode());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                try {
                    holder.txtInGate.setText(outPassViewModel.getInGateCode());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                try {
                    holder.txtReason.setText(outPassViewModel.getReason());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                try {
                    holder.txtReqDate.setText("Request Date: " + DateTimeParseFormatter(outPassViewModel.getRequestDate()));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (position == holder.getAdapterPosition()) {
                outPassID = outPassViewModel.getOutPassID().toString();
            }
            holder.btnReject.setOnClickListener(v -> {
                String outPassID = outPassViewModel.getOutPassID().toString();
                RejectOutPassRequest(outPassID);
            });
        }

        @Override
        public int getItemCount() {
            return leaveRequestsViewModelList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView
                    txtDuration,
                    txtFromTime,
                    txtToTime,
                    txtOutGate,
                    txtReason,
                    txtReqDate,
                    txtEmployeeName, txtJoinedDate,
                    txtInGate;
            LinearLayout btnReject, btnApprove;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                txtDuration = itemView.findViewById(R.id.txtDuration);
                txtFromTime = itemView.findViewById(R.id.txtFromTime);
                txtToTime = itemView.findViewById(R.id.txtToTime);
                txtOutGate = itemView.findViewById(R.id.txtOutGate);
                txtInGate = itemView.findViewById(R.id.txtInGate);
                txtReason = itemView.findViewById(R.id.txtReason);
                txtReqDate = itemView.findViewById(R.id.txtReqDate);
                txtEmployeeName = itemView.findViewById(R.id.txtEmployeeName);
                txtJoinedDate = itemView.findViewById(R.id.txtJoinedDate);
                btnApprove = itemView.findViewById(R.id.btnApprove);
                btnReject = itemView.findViewById(R.id.btnReject);
            }
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
                                SAlertSuccess(serviceResponseViewModel.getMessage(), getContext());
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
                                SAlertError(serviceResponseViewModel.getMessage(), getContext());
                            } catch (Exception e) {
                                SAlertError(e.getMessage(), getContext());
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
                SConnectionFail(t.getMessage(), getContext());
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
                                SAlertSuccess(serviceResponseViewModel.getMessage(), getContext());
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
                                SAlertError(serviceResponseViewModel.getMessage(), getContext());
                            } catch (Exception e) {
                                SAlertError(e.getMessage(), getContext());
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
                SConnectionFail(t.getMessage(), getContext());
            }
        });
    }

    // Leave Approval
    private void GetEmployeePendingLeaveApproval() {
        try {
            Call<List<LeaveRequestsViewModel>> getContToLocCall = retrofitApiInterface.GetLeaveRequestsForApprovalAsync("Bearer" + " " + token, appKey, companyID, accountID);
            getContToLocCall.enqueue(new Callback<List<LeaveRequestsViewModel>>() {
                @Override
                public void onResponse(Call<List<LeaveRequestsViewModel>> call, Response<List<LeaveRequestsViewModel>> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                spotsDialog.dismiss();
                                employeePendingLeaveApprovalAdapter = new EmployeePendingLeaveApprovalAdapter(getContext(), response.body());
                                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                pendingLeaveApprovalRecycalerView.setLayoutManager(mLayoutManager);
                                pendingLeaveApprovalRecycalerView.setAdapter(employeePendingLeaveApprovalAdapter);
                                txtPendingApprovalCounter.setText(String.valueOf(response.body().size()));
                            }
                        } else {
                            if (response.errorBody() != null) {
                                spotsDialog.dismiss();
                                gson = new GsonBuilder().create();
                                try {
                                    pendingLeaveApprovalRecycalerView.setAdapter(null);
                                    employeePendingLeaveApprovalAdapter = null;
                                    txtPendingApprovalCounter.setText("0");
                                    serviceResponseViewModel = gson.fromJson(response.errorBody().string(), ServiceResponseViewModel.class);
                                    SAlertError(serviceResponseViewModel.getMessage(), getContext());
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
                public void onFailure(Call<List<LeaveRequestsViewModel>> call, Throwable t) {
                    SAlertError(t.getMessage(), getContext());
                    spotsDialog.dismiss();
                }
            });
        } catch (Exception e) {
            spotsDialog.dismiss();
            e.printStackTrace();
        }
    }

    public class EmployeePendingLeaveApprovalAdapter extends RecyclerView.Adapter<EmployeePendingLeaveApprovalAdapter.ViewHolder> {
        Context context;
        List<LeaveRequestsViewModel> leaveRequestsViewModelList;
        ApproveLeaveRequestDto approveLeaveRequestDto;

        public EmployeePendingLeaveApprovalAdapter(Context context, List<LeaveRequestsViewModel> leaveRequestsViewModelList) {
            this.context = context;
            this.leaveRequestsViewModelList = leaveRequestsViewModelList;
        }

        @NonNull
        @Override
        public EmployeePendingLeaveApprovalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.pending_leave_approval_recycalerview, parent, false);
            return new EmployeePendingLeaveApprovalAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull EmployeePendingLeaveApprovalAdapter.ViewHolder holder, int position) {
            final LeaveRequestsViewModel leaveRequestsViewModel = leaveRequestsViewModelList.get(position);

            if (position == holder.getAdapterPosition()) {
                approveLeaveRequestDto = new ApproveLeaveRequestDto();
                holder.btnApprove.setOnClickListener(v -> {
                    approveLeaveRequestDto.setLeaveRequestID(leaveRequestsViewModel.getLeaveRequestID().toString());
                    approveLeaveRequestDto.setApproverAccountID(accountID);
                    builder.setTitle("Chuti Aleart");
                    builder.setMessage("Are You Sure to Approve Leave Request ID: " + leaveRequestsViewModel.getLeaveRequestID() + "?")
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

            }
            holder.btnReject.setOnClickListener(v -> {
                rejectLeaveDialog.setContentView(rejectLeaveView);
                rejectLeaveDialog.setCancelable(true);
                rejectLeaveDialog.show();
                txtRejectReason.setText("");
                txtRejectReason.requestFocus();

                btnSave.setOnClickListener(v1 -> {
                    spotsDialog.show();
                    approveLeaveRequestDto.setLeaveRequestID(leaveRequestsViewModel.getLeaveRequestID().toString());
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
                                            SAlertSuccess(serviceResponseViewModel.getMessage(), getContext());
                                            rejectLeaveDialog.dismiss();
                                        }
                                    }
                                } else {
                                    if (response.errorBody() != null) {
                                        spotsDialog.dismiss();
                                        serviceResponseViewModel = new ServiceResponseViewModel();
                                        gson = new GsonBuilder().create();
                                        try {
                                            serviceResponseViewModel = gson.fromJson(response.errorBody().string(), ServiceResponseViewModel.class);
                                            SAlertError(serviceResponseViewModel.getMessage(), getContext());
                                        } catch (Exception e) {
                                            SAlertError(e.getMessage(), getContext());
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
                            SConnectionFail(t.getMessage(), getContext());
                        }
                    });
                });
                btnClose.setOnClickListener(v1 -> rejectLeaveDialog.dismiss());
            });

            try {

                try {
                    holder.txtLeaveType.setText(String.format("%d %s", leaveRequestsViewModel.getNoOfDays(), leaveRequestsViewModel.getLeaveTypeName()));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                try {

                    holder.txtLeaveName.setText(leaveRequestsViewModel.getLeaveTypeName());
                    if (leaveRequestsViewModel.getLeaveTypeName().equals("Casual Leave"))
                        holder.txtLeaveName.setBackgroundResource(R.drawable.casual_leave_button);
                    else if (leaveRequestsViewModel.getLeaveTypeName().equals("Earn Leave")) {
                        holder.txtLeaveName.setBackgroundResource(R.drawable.earn_leave_button);
                    } else
                        holder.txtLeaveName.setBackgroundResource(R.drawable.sick_leave_button);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                try {
                    holder.txtFromDate.setText(String.valueOf(DateTimeParseMonthYearFormatter(leaveRequestsViewModel.getFromDate())));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                try {
                    holder.txtToDate.setText(String.valueOf(DateTimeParseMonthYearFormatter(leaveRequestsViewModel.getToDate())));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                try {
                    holder.txtReason.setText(leaveRequestsViewModel.getReason());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                try {
                    holder.txtNoOfDays.setText(leaveRequestsViewModel.getNoOfDays() + " Days");
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                try {
                    holder.txtEmployeeName.setText(leaveRequestsViewModel.getEmployeeCompactViewModel().getEmployeeName() + "[" + leaveRequestsViewModel.getEmployeeCompactViewModel().getHrEmployeeID() + "]");
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                try {
                    holder.txtReqDate.setText(String.valueOf(DateTimeParseFormatter(leaveRequestsViewModel.getRequestDate())));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return leaveRequestsViewModelList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
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

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                txtLeaveName = itemView.findViewById(R.id.txtLeaveName);
                txtToDate = itemView.findViewById(R.id.txtToDate);
                txtFromDate = itemView.findViewById(R.id.txtFromDate);
                txtNoOfDays = itemView.findViewById(R.id.txtNoOfDays);
                txtLeaveType = itemView.findViewById(R.id.txtLeaveType);
                txtReason = itemView.findViewById(R.id.txtReason);
                txtEmployeeName = itemView.findViewById(R.id.txtEmployeeName);
                txtReqDate = itemView.findViewById(R.id.txtReqDate);
                btnReject = itemView.findViewById(R.id.btnReject);
                btnApprove = itemView.findViewById(R.id.btnApprove);
            }
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
                                SAlertSuccess(serviceResponseViewModel.getMessage(), getContext());
                                GetEmployeePendingLeaveApproval();
                            }
                        }
                    } else if (!response.isSuccessful()) {
                        if (response.errorBody() != null) {
                            spotsDialog.dismiss();
                            serviceResponseViewModel = new ServiceResponseViewModel();
                            gson = new GsonBuilder().create();
                            try {
                                serviceResponseViewModel = gson.fromJson(response.errorBody().string(), ServiceResponseViewModel.class);
                                SAlertError(serviceResponseViewModel.getMessage(), getContext());
                            } catch (Exception e) {
                                SAlertError(e.getMessage(), getContext());
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
                SConnectionFail(t.getMessage(), getContext());
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
                            serviceResponseViewModel = new ServiceResponseViewModel();
                            if (response.code() == 200) {
                                serviceResponseViewModel = gson.fromJson(response.body(), ServiceResponseViewModel.class);
                                SAlertSuccess(serviceResponseViewModel.getMessage(), getContext());
                                GetEmployeePendingLeaveApproval();
                            }
                        }
                    } else if (!response.isSuccessful()) {
                        if (response.errorBody() != null) {
                            spotsDialog.dismiss();
                            serviceResponseViewModel = new ServiceResponseViewModel();
                            gson = new GsonBuilder().create();
                            try {
                                serviceResponseViewModel = gson.fromJson(response.errorBody().string(), ServiceResponseViewModel.class);
                                SAlertError(serviceResponseViewModel.getMessage(), getContext());
                            } catch (Exception e) {
                                SAlertError(e.getMessage(), getContext());
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
                SConnectionFail(t.getMessage(), getContext());
            }
        });
    }
}