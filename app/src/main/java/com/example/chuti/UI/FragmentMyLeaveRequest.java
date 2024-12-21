package com.example.chuti.UI;

import static com.example.chuti.FragmentManager.FragmentManager.replaceFragment;
import static com.example.chuti.Handlers.DateFormatterHandlers.DateTimeParseMonthYearFormatter;
import static com.example.chuti.Handlers.SMessageHandler.SAlertError;
import static com.example.chuti.Handlers.SMessageHandler.SAlertSuccess;
import static com.example.chuti.Handlers.SMessageHandler.SConnectionFail;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.chuti.FragmentMain;
import com.example.chuti.Model.EmployeeLeaveCatalogViewModel;
import com.example.chuti.Model.LeaveRequestsViewModel;
import com.example.chuti.Model.ServiceResponseViewModel;
import com.example.chuti.R;
import com.example.chuti.Security.BaseURL;
import com.example.chuti.Security.Services;
import com.example.chuti.Security.SharedPref;
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

public class FragmentMyLeaveRequest extends Fragment {
    Services retrofitApiInterface;
    Gson gson;
    String token, accountID, companyID;
    SpotsDialog spotsDialog;
    ServiceResponseViewModel serviceResponseViewModel = new ServiceResponseViewModel();
    Spinner catalogSpinner;
    String userID, appKey, periodYear;
    Toolbar toolbar;
    RecyclerView employeeLeaveRequestRecyclerView;
    EmployeeLeaveRequestAdapter employeeLeaveRequestAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_my_leave_request, container, false);

        retrofitApiInterface = BaseURL.getRetrofit().create(Services.class);
        SharedPref.init(getContext());
        gson = new Gson();
        spotsDialog = new SpotsDialog(getContext(), R.style.Custom);
        appKey = SharedPref.read("appKey", "");
        token = SharedPref.read("token", "");
        companyID = SharedPref.read("companyID", "");
        accountID = SharedPref.read("accountID", "");
        userID = SharedPref.read("uId", "");

        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setTitle("My Leave Requests");
        toolbar.setSubtitle("");
        toolbar.setNavigationOnClickListener(v -> {
            toolbar.setTitle(R.string.chuti);
            replaceFragment(new FragmentMain(), getContext());
        });

        catalogSpinner = root.findViewById(R.id.catalogSpinner);
        employeeLeaveRequestRecyclerView = root.findViewById(R.id.employeeLeaveRequestRecyclerView);

        List<Integer> yearList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        int currentYear = calendar.get(Calendar.YEAR);

        // Add the current year and the last three years to the list
        for (int i = 0; i < 3; i++) {
            yearList.add(currentYear - i);
        }

        if (!yearList.isEmpty()) {
            ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<>(
                    getContext(),
                    android.R.layout.simple_spinner_item,
                    yearList
            );
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            catalogSpinner.setAdapter(dataAdapter);

            // Set item selection listener
            catalogSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    periodYear = catalogSpinner.getItemAtPosition(position).toString();
                    GetEmployeeLeaveRequest();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Do nothing if no selection is made
                }
            });
        }

        return root;
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
                            }
                        } else {
                            if (response.errorBody() != null) {
                                spotsDialog.dismiss();
                                gson = new GsonBuilder().create();
                                try {
                                    serviceResponseViewModel = gson.fromJson(response.errorBody().string(), ServiceResponseViewModel.class);
                                    SAlertError(serviceResponseViewModel.getMessage(), getContext());
                                    employeeLeaveRequestRecyclerView.setAdapter(null);
                                    employeeLeaveRequestAdapter.equals("");
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
            return new EmployeeLeaveRequestAdapter.ViewHolder(view);
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

                switch (leaveRequestsViewModel.getStatus()) {
                    case 0:
                        holder.txtStatusCode.setText(R.string.created);
                        holder.txtStatusCode.setBackgroundResource(R.drawable.created_button);
                        break;
                    case 2:
                        holder.txtStatusCode.setText(R.string.approved);
                        holder.btnDelete.setVisibility(View.INVISIBLE);
                        holder.txtStatusCode.setBackgroundResource(R.drawable.approved_button);
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
                    holder.txtLastComment.setText(leaveRequestsViewModel.getLastComment());
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
                                serviceResponseViewModel = new ServiceResponseViewModel();
                                serviceResponseViewModel = gson.fromJson(response.body(), ServiceResponseViewModel.class);
                                SAlertSuccess(serviceResponseViewModel.getMessage(), getContext());
                                GetEmployeeLeaveRequest();
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