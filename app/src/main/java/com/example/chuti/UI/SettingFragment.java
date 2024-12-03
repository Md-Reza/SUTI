package com.example.chuti.UI;

import static com.example.chuti.FragmentManager.FragmentManager.replaceFragment;
import static com.example.chuti.Handlers.DateFormatterHandlers.DateTimeParseMonthYearFormatter;
import static com.example.chuti.Handlers.SMessageHandler.SAlertError;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.chuti.FragmentMain;
import com.example.chuti.LoginActivity;
import com.example.chuti.Model.EmployeeLeaveCatalogViewModel;
import com.example.chuti.Model.EmployeeProfile;
import com.example.chuti.Model.ServiceResponseViewModel;
import com.example.chuti.R;
import com.example.chuti.Security.BaseURL;
import com.example.chuti.Security.Services;
import com.example.chuti.Security.SharedPref;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingFragment extends Fragment {
    Services retrofitApiInterface;
    Gson gson;
    String token, accountID, companyID, userID, appKey;
    SpotsDialog spotsDialog;
    ServiceResponseViewModel serviceResponseViewModel = new ServiceResponseViewModel();
    TextView txtJoiningDate, txtLogOut, txtEmployeeName, txtResetPassword, txtStatus, txtEmployeeID;
    EmployeeProfile employeeProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_setting, container, false);

        retrofitApiInterface = BaseURL.getRetrofit().create(Services.class);
        SharedPref.init(getContext());
        gson = new Gson();
        spotsDialog = new SpotsDialog(getContext(), R.style.Custom);
        appKey = SharedPref.read("appKey", "");
        token = SharedPref.read("token", "");
        companyID = SharedPref.read("companyID", "");
        accountID = SharedPref.read("accountID", "");
        userID = SharedPref.read("uId", "");

        txtJoiningDate = root.findViewById(R.id.txtJoiningDate);
        txtLogOut = root.findViewById(R.id.txtLogOut);
        txtEmployeeName = root.findViewById(R.id.txtEmployeeName);
        txtResetPassword = root.findViewById(R.id.txtResetPassword);
        txtStatus = root.findViewById(R.id.txtStatus);
        txtEmployeeID = root.findViewById(R.id.txtEmployeeID);

        txtLogOut.setOnClickListener(v -> {
            SharedPref.remove("token", "");
            Intent i1 = new Intent(getContext(),
                    LoginActivity.class);
            startActivity(i1);
        });

        txtResetPassword.setOnClickListener(v -> replaceFragment(new FragmentResetPassword(), getContext()));

        GetEmployee();
        return root;
    }

    private void GetEmployee() {
        try {
            spotsDialog.show(); // Show loading dialog

            // Call the API to get the employee leave catalog
            Call<EmployeeProfile> getContToLocCall = retrofitApiInterface.GetEmployeeServiceAsync("Bearer " + token, appKey, companyID, accountID);

            getContToLocCall.enqueue(new Callback<EmployeeProfile>() {
                @Override
                public void onResponse(Call<EmployeeProfile> call, Response<EmployeeProfile> response) {
                    spotsDialog.dismiss(); // Dismiss the loading dialog

                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            // Successfully received response
                            employeeProfile = new EmployeeProfile();
                            employeeProfile = response.body();
                            txtEmployeeName.setText(employeeProfile.getEmployeeName());
                            txtEmployeeID.setText(employeeProfile.getHrEmployeeID());
                            txtJoiningDate.setText(DateTimeParseMonthYearFormatter(employeeProfile.getJoiningDate()));
                            Boolean enable = employeeProfile.getEnabled();
                            if (enable)
                                txtStatus.setText("ACTIVE");
                            else
                                txtStatus.setText("INACTIVE");

                        } else {
                            // Handle API error response
                            if (response.errorBody() != null) {
                                gson = new GsonBuilder().create();
                                serviceResponseViewModel = gson.fromJson(response.errorBody().string(), ServiceResponseViewModel.class);
                                SAlertError(serviceResponseViewModel.getMessage(), getContext());
                            }
                        }
                    } catch (Exception e) {
                        // Handle unexpected exceptions
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<EmployeeProfile> call, Throwable t) {
                    // Handle network or other failures
                    SAlertError(t.getMessage(), getContext());
                    spotsDialog.dismiss();
                }
            });
        } catch (Exception e) {
            // Handle general exceptions
            spotsDialog.dismiss();
            e.printStackTrace();
        }

    }
}