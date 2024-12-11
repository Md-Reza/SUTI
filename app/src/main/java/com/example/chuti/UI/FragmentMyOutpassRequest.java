package com.example.chuti.UI;

import static com.example.chuti.FragmentManager.FragmentManager.replaceFragment;
import static com.example.chuti.FragmentManager.FragmentManager.replaceFragmentWithMultipleBundle;
import static com.example.chuti.Handlers.DateFormatterHandlers.ConvertDateToTime;
import static com.example.chuti.Handlers.DateFormatterHandlers.DateTimeParseFormatter;
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
import com.example.chuti.Model.OutPassViewModel;
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

public class FragmentMyOutpassRequest extends Fragment {

    Services retrofitApiInterface;
    Gson gson;
    String token, accountID, companyID;
    SpotsDialog spotsDialog;
    ServiceResponseViewModel serviceResponseViewModel = new ServiceResponseViewModel();
    ArrayList<OutPassViewModel> leaveRequestsViewModelList = new ArrayList<>();
    Spinner catalogSpinner;
    String userID, appKey, periodYear, outPassID;
    Toolbar toolbar;
    RecyclerView outPassRecycalerView;
    EmployeeOutPassRequestAdapter employeeOutPassRequestAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_my_outpass_request, container, false);

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
        toolbar.setTitle("My Outpass Requests");
        toolbar.setSubtitle("");
        toolbar.setNavigationOnClickListener(v -> {
            replaceFragment(new FragmentMain(), getContext());
        });

        catalogSpinner = root.findViewById(R.id.catalogSpinner);
        outPassRecycalerView = root.findViewById(R.id.outPassRecycalerView);

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
                    GetEmployeeOutPass();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Do nothing if no selection is made
                }
            });
        }

        return root;
    }

    private void GetEmployeeOutPass() {
        try {
            Call<List<OutPassViewModel>> getContToLocCall = retrofitApiInterface.GetOutpassRequestsAsync("Bearer" + " " + token, appKey, companyID, accountID, Integer.parseInt(periodYear));
            getContToLocCall.enqueue(new Callback<List<OutPassViewModel>>() {
                @Override
                public void onResponse(Call<List<OutPassViewModel>> call, Response<List<OutPassViewModel>> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                spotsDialog.dismiss();
                                leaveRequestsViewModelList = new ArrayList<>(response.body());
                                employeeOutPassRequestAdapter = new EmployeeOutPassRequestAdapter(getContext(), leaveRequestsViewModelList);
                                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                outPassRecycalerView.setLayoutManager(mLayoutManager);
                                outPassRecycalerView.setAdapter(employeeOutPassRequestAdapter);
                            }
                        } else {
                            if (response.errorBody() != null) {
                                spotsDialog.dismiss();
                                gson = new GsonBuilder().create();
                                try {
                                    serviceResponseViewModel = gson.fromJson(response.errorBody().string(), ServiceResponseViewModel.class);
                                    SAlertError(serviceResponseViewModel.getMessage(), getContext());
                                    outPassRecycalerView.setAdapter(null);
                                    employeeOutPassRequestAdapter.equals("");
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
        ArrayList<OutPassViewModel> leaveRequestsViewModelList;

        public EmployeeOutPassRequestAdapter(Context context, ArrayList<OutPassViewModel> leaveRequestsViewModelList) {
            this.context = context;
            this.leaveRequestsViewModelList = leaveRequestsViewModelList;
        }

        @NonNull
        @Override
        public EmployeeOutPassRequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.my_outpass_request_recycalerview, parent, false);
            return new EmployeeOutPassRequestAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull EmployeeOutPassRequestAdapter.ViewHolder holder, int position) {
            final OutPassViewModel leaveRequestsViewModel = leaveRequestsViewModelList.get(position);

            if (position == holder.getAdapterPosition()) {
                holder.btnDelete.setOnClickListener(v -> {
                    String leaveReqID = leaveRequestsViewModel.getOutPassID().toString();
                    DeleteOutPassRequest(leaveReqID);
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
                        break;
                    case 3:
                        holder.txtStatusCode.setText(R.string.rejected);
                        holder.txtStatusCode.setBackgroundResource(R.drawable.reject_button);
                        break;
                    default:
                        break;
                }

                try {
                    holder.txtFromTime.setText(String.valueOf(ConvertDateToTime(leaveRequestsViewModel.getFromTime())));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                try {
                    holder.txtToTime.setText(String.valueOf(ConvertDateToTime(leaveRequestsViewModel.getToTime())));
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
                    holder.txtReqDate.setText(DateTimeParseFormatter(leaveRequestsViewModel.getRequestDate()));
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
}