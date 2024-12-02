package com.example.chuti;

import static com.example.chuti.FragmentManager.FragmentManager.replaceFragment;
import static com.example.chuti.Handlers.SMessageHandler.SAlertError;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chuti.Model.EmployeeProfile;
import com.example.chuti.Model.ServiceResponseViewModel;
import com.example.chuti.Security.BaseURL;
import com.example.chuti.Security.Services;
import com.example.chuti.Security.SharedPref;
import com.example.chuti.UI.EmployeeGatepassFragment;
import com.example.chuti.UI.FragmentBalance;
import com.example.chuti.UI.GatePassQRFragment;
import com.example.chuti.UI.RequestLeaveFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentMain extends Fragment {

    Services retrofitApiInterface;
    Gson gson;
    String token, accountID, companyID, userID, appKey;
    SpotsDialog spotsDialog;
    ServiceResponseViewModel serviceResponseViewModel = new ServiceResponseViewModel();
    LinearLayout mnuEmpGatePass,txtGatepassHistory,mnuLeaveRequest;
    TextView txtEmployeeID,txtName;
    EmployeeProfile employeeProfile;
    Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_main, container, false);

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
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.chuti_logo);
        int iconSize = getResources().getDimensionPixelSize(R.dimen.navigation_icon_size); // Define size in dimens.xml
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, iconSize, iconSize, false);
        BitmapDrawable drawable = new BitmapDrawable(getResources(), scaledBitmap);

        toolbar.setNavigationIcon(drawable);
        toolbar.setTitle("");
        if (getActivity() instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            activity.setSupportActionBar(toolbar);

            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
                activity.getSupportActionBar().setDisplayShowTitleEnabled(true);
            }
        }

        txtEmployeeID = root.findViewById(R.id.txtEmployeeID);
        txtName = root.findViewById(R.id.txtName);

        mnuEmpGatePass = root.findViewById(R.id.mnuEmpGatePass);
        mnuEmpGatePass.setOnClickListener(v -> replaceFragment(new EmployeeGatepassFragment(), getContext()));
        txtGatepassHistory = root.findViewById(R.id.txtGatepassHistory);
        txtGatepassHistory.setOnClickListener(v -> replaceFragment(new GatePassQRFragment(), getContext()));
        mnuLeaveRequest = root.findViewById(R.id.mnuLeaveRequest);
        mnuLeaveRequest.setOnClickListener(v -> replaceFragment(new RequestLeaveFragment(), getContext()));

        GetEmployee();

        return root;
    }

    private void GetEmployee() {
        try {
            Call<EmployeeProfile> getContToLocCall = retrofitApiInterface.GetEmployeeServiceAsync("Bearer " + token, appKey, companyID, accountID);

            getContToLocCall.enqueue(new Callback<EmployeeProfile>() {
                @Override
                public void onResponse(Call<EmployeeProfile> call, Response<EmployeeProfile> response) {

                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            // Successfully received response
                            employeeProfile = new EmployeeProfile();
                            employeeProfile = response.body();
                            txtEmployeeID.setText(employeeProfile.getHrEmployeeID());
                            txtName.setText(employeeProfile.getEmployeeName());

                        } else {
                            if (response.errorBody() != null) {
                                gson = new GsonBuilder().create();
                                serviceResponseViewModel = gson.fromJson(response.errorBody().string(), ServiceResponseViewModel.class);
                                //SAlertError(serviceResponseViewModel.getMessage(), getContext());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<EmployeeProfile> call, Throwable t) {
                    // Handle network or other failures
                   // SAlertError(t.getMessage(), getContext());
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