package com.example.chuti.UI;

import static com.example.chuti.FragmentManager.FragmentManager.replaceFragment;
import static com.example.chuti.Handlers.DateFormatterHandlers.DateTimeParseFormatter;
import static com.example.chuti.Handlers.DateFormatterHandlers.DateTimeParseMonthYearFormatter;
import static com.example.chuti.Handlers.SMessageHandler.SAlertError;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.chuti.Model.EmployeeLeaveCatalogViewModel;
import com.example.chuti.Model.EmployeeLeaveRequestViewModel;
import com.example.chuti.Model.ServiceResponseViewModel;
import com.example.chuti.R;
import com.example.chuti.Security.BaseURL;
import com.example.chuti.Security.Services;
import com.example.chuti.Security.SharedPref;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentBalance extends Fragment {
    Services retrofitApiInterface;
    Gson gson;
    String token, userName;
    SpotsDialog spotsDialog;
    ServiceResponseViewModel serviceResponseViewModel = new ServiceResponseViewModel();
    List<EmployeeLeaveCatalogViewModel> employeeLeaveCatalogViewModel = new ArrayList<>();
    ProgressBar stats_progressbarCasualLeave, stats_progressbarSick, stats_progressbarEarnLeave;
    String userID, appKey;
    TextView txtCasualLeaveName, txtCasualLeaveBalanceStatus, txtNofSickLeave,
            txtSickLeaveName, txtEarnLeaveName, txtNoOfEarnLeave;

    EmployeeLeaveRequestAdapter employeeLeaveRequestAdapter;
    RecyclerView employeeLeaveRequestRecyclerView;
    Button btnRequestLeave;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_balance, container, false);
        retrofitApiInterface = BaseURL.getRetrofit().create(Services.class);
        SharedPref.init(getContext());
        gson = new Gson();
        spotsDialog = new SpotsDialog(getContext(), R.style.Custom);
        appKey = SharedPref.read("appKey", "");
        token = SharedPref.read("token", "");
        userID = SharedPref.read("uId", "");

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

        btnRequestLeave.setOnClickListener(v -> replaceFragment(new RequestLeaveFragment(), getContext()));

        EmployeeCurrentLeaveStatistics();
        GetEmployeeLeaveRequest();
        return root;
    }

    private void EmployeeCurrentLeaveStatistics() {
        try {
            spotsDialog.show();
            Call<List<EmployeeLeaveCatalogViewModel>> getContToLocCall = retrofitApiInterface.GetEmployeeLeaveCatalogAsync("Bearer" + " " + token, appKey, 5021, 5013);
            getContToLocCall.enqueue(new Callback<List<EmployeeLeaveCatalogViewModel>>() {
                @Override
                public void onResponse(Call<List<EmployeeLeaveCatalogViewModel>> call, Response<List<EmployeeLeaveCatalogViewModel>> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                spotsDialog.dismiss();
                                employeeLeaveCatalogViewModel = response.body();
                                Log.i("info", "employeeLeaveCatalogViewModel" + response.body());

                                for (int i = 0; i < employeeLeaveCatalogViewModel.size(); i++) {
                                    //Calculate the slice size and update the pie chart:
                                    if (employeeLeaveCatalogViewModel.get(i).getLeaveTypeName().equals("Casual Leave")) {
                                        int calsBurned = employeeLeaveCatalogViewModel.get(i).getUsedDays() + employeeLeaveCatalogViewModel.get(i).getOnHeldDays();
                                        int totalLeave = employeeLeaveCatalogViewModel.get(i).getTotalHandDays();
                                        double d = (double) calsBurned / (double) totalLeave;
                                        int progress = (int) (d * 100);
                                        stats_progressbarCasualLeave.setProgress(progress);
                                        txtCasualLeaveName.setText(employeeLeaveCatalogViewModel.get(i).getLeaveTypeName());
                                        txtCasualLeaveBalanceStatus.setText(String.format("%d/%d", employeeLeaveCatalogViewModel.get(i).getUsedDays() + employeeLeaveCatalogViewModel.get(i).getOnHeldDays(), employeeLeaveCatalogViewModel.get(i).getTotalHandDays()));
                                    }
                                    //Calculate the slice size and update the pie chart:
                                    if (employeeLeaveCatalogViewModel.get(i).getLeaveTypeName().equals("Sick Leave")) {
                                        int calsBurned = employeeLeaveCatalogViewModel.get(i).getUsedDays() + employeeLeaveCatalogViewModel.get(i).getOnHeldDays();
                                        int totalLeave = employeeLeaveCatalogViewModel.get(i).getTotalHandDays();
                                        double d = (double) calsBurned / (double) totalLeave;
                                        int progress = (int) (d * 100);
                                        stats_progressbarSick.setProgress(progress);
                                        txtSickLeaveName.setText(employeeLeaveCatalogViewModel.get(i).getLeaveTypeName());
                                        txtNofSickLeave.setText(String.format("%d/%d", employeeLeaveCatalogViewModel.get(i).getUsedDays() + employeeLeaveCatalogViewModel.get(i).getOnHeldDays(), employeeLeaveCatalogViewModel.get(i).getTotalHandDays()));
                                    }
                                    //Calculate the slice size and update the pie chart:
                                    if (employeeLeaveCatalogViewModel.get(i).getLeaveTypeName().equals("Earn Leave")) {
                                        int calsBurned = employeeLeaveCatalogViewModel.get(i).getUsedDays() + employeeLeaveCatalogViewModel.get(i).getOnHeldDays();
                                        int totalLeave = employeeLeaveCatalogViewModel.get(i).getTotalHandDays();
                                        double d = (double) calsBurned / (double) totalLeave;
                                        int progress = (int) (d * 100);
                                        stats_progressbarEarnLeave.setProgress(progress);
                                        txtEarnLeaveName.setText(employeeLeaveCatalogViewModel.get(i).getLeaveTypeName());
                                        txtNoOfEarnLeave.setText(String.format("%d/%d", employeeLeaveCatalogViewModel.get(i).getUsedDays() + employeeLeaveCatalogViewModel.get(i).getOnHeldDays(), employeeLeaveCatalogViewModel.get(i).getTotalHandDays()));
                                    }
                                }


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
            spotsDialog.show();
            Call<List<EmployeeLeaveRequestViewModel>> getContToLocCall = retrofitApiInterface.GetEmployeeLeaveRequestAsync("Bearer" + " " + token, appKey, 5021, 5013, 2024);
            getContToLocCall.enqueue(new Callback<List<EmployeeLeaveRequestViewModel>>() {
                @Override
                public void onResponse(Call<List<EmployeeLeaveRequestViewModel>> call, Response<List<EmployeeLeaveRequestViewModel>> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                spotsDialog.dismiss();
                                Log.i("info", "employeeLeaveCatalogViewModel" + response.body());

                                employeeLeaveRequestAdapter = new EmployeeLeaveRequestAdapter(getContext(), response.body());
                                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                employeeLeaveRequestRecyclerView.setLayoutManager(mLayoutManager);
                                employeeLeaveRequestRecyclerView.setAdapter(employeeLeaveRequestAdapter);
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
                public void onFailure(Call<List<EmployeeLeaveRequestViewModel>> call, Throwable t) {
                    SAlertError(t.getMessage(), getContext());
                    spotsDialog.dismiss();
                }
            });
        } catch (Exception e) {
            spotsDialog.dismiss();
            e.printStackTrace();
        }
    }

    public static class EmployeeLeaveRequestAdapter extends RecyclerView.Adapter<EmployeeLeaveRequestAdapter.ViewHolder> {

        Context context;
        List<EmployeeLeaveRequestViewModel> agvTaskViewModelList;

        public EmployeeLeaveRequestAdapter(Context context, List<EmployeeLeaveRequestViewModel> agvTaskViewModelList) {
            this.context = context;
            this.agvTaskViewModelList = agvTaskViewModelList;
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
            final EmployeeLeaveRequestViewModel agvTaskViewModel = agvTaskViewModelList.get(position);

            try {

                try {
                    holder.txtLeaveType.setText(String.format("%d %s", agvTaskViewModel.getNoOfDays(), agvTaskViewModel.getLeaveTypeName()));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                try {

                    holder.txtLeaveName.setText(agvTaskViewModel.getLeaveTypeName());
                    if (agvTaskViewModel.getLeaveTypeName().equals("Casual Leave"))
                        holder.txtLeaveName.setBackgroundResource(R.drawable.casual_leave_button);
                    else if (agvTaskViewModel.getLeaveTypeName().equals("Earn Leave")) {
                        holder.txtLeaveName.setBackgroundResource(R.drawable.earn_leave_button);
                    } else
                        holder.txtLeaveName.setBackgroundResource(R.drawable.sick_leave_button);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                switch (agvTaskViewModel.getStatus()) {
                    case 0:
                        holder.txtStatusCode.setText(R.string.created);
                        break;
                    case 1:
                        holder.txtStatusCode.setText(R.string.pending);
                        break;
                    case 2:
                        holder.txtStatusCode.setText(R.string.inprocess);
                        break;
                    case 3:
                        holder.txtStatusCode.setText(R.string.rejected);
                        break;
                    case 4:
                        holder.txtStatusCode.setText(R.string.rework);
                        break;
                    case 5:
                        holder.txtStatusCode.setText(R.string.approved);
                        break;
                    case 6:
                        holder.txtStatusCode.setText(R.string.completed);
                        break;
                    case 7:
                        holder.txtStatusCode.setText(R.string.deleted);
                        break;

                    default:
                        break;
                }

                try {
                    holder.txtFromDate.setText(String.valueOf(DateTimeParseMonthYearFormatter(agvTaskViewModel.getFromDate())));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                try {
                    holder.txtToDate.setText(String.valueOf(DateTimeParseMonthYearFormatter(agvTaskViewModel.getToDate())));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return agvTaskViewModelList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView
                    txtLeaveName,
                    txtToDate,
                    txtFromDate,
                    txtStatusCode,
                    txtLeaveType;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                txtLeaveName = itemView.findViewById(R.id.txtLeaveName);
                txtToDate = itemView.findViewById(R.id.txtToDate);
                txtFromDate = itemView.findViewById(R.id.txtFromDate);
                txtStatusCode = itemView.findViewById(R.id.txtStatusCode);
                txtLeaveType = itemView.findViewById(R.id.txtLeaveType);
            }
        }

    }
}