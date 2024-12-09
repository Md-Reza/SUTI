package com.example.chuti;

import static com.example.chuti.FragmentManager.FragmentManager.replaceFragment;
import static com.example.chuti.Handlers.SMessageHandler.SAlertError;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chuti.Model.EmployeeAccount;
import com.example.chuti.Model.EmployeeProfile;
import com.example.chuti.Model.ServiceResponseViewModel;
import com.example.chuti.Security.BaseURL;
import com.example.chuti.Security.Services;
import com.example.chuti.Security.SharedPref;
import com.example.chuti.UI.EmployeeGatepassFragment;
import com.example.chuti.UI.FragmentBalance;
import com.example.chuti.UI.FragmentMyLeaveRequest;
import com.example.chuti.UI.FragmentMyOutpassRequest;
import com.example.chuti.UI.GatePassQRFragment;
import com.example.chuti.UI.RequestLeaveFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

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
    LinearLayout mnuEmpGatePass, txtGatepassHistory, mnuLeaveRequest, mnuMyLeaveRequest, idMnu01;
    TextView txtEmployeeID, txtName;
    EmployeeProfile employeeProfile;
    EmployeeAccount employeeAccount;
    Toolbar toolbar;
    String accountType;

    Dialog dialogClose;
    View logoutView;

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
        idMnu01 = root.findViewById(R.id.idMnu01);


        txtGatepassHistory = root.findViewById(R.id.txtGatepassHistory);
        txtGatepassHistory.setOnClickListener(v -> replaceFragment(new FragmentMyOutpassRequest(), getContext()));
        mnuMyLeaveRequest = root.findViewById(R.id.mnuMyLeaveRequest);
        mnuMyLeaveRequest.setOnClickListener(v -> replaceFragment(new FragmentMyLeaveRequest(), getContext()));

        try {
            String[] parts = token.split("\\.", 0);

            for (String part : parts) {
                byte[] decodedBytes = Base64.decode(part, Base64.URL_SAFE);
                String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);
                System.out.println("Decoded 1: " + decodedString);
                try {
                    accountType = (new JSONObject(decodedString)).getString("http://schemas.microsoft.com/ws/2008/06/identity/claims/role");
                    SharedPref.write("accountType", accountType);

                    System.out.println("Decoded value: " + accountType);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (accountType.equals("EMPLOYEE")) {
            GetEmployee();
        } else {
            GetEmployeeAccount();
        }

        dialogClose = new Dialog(getActivity());
        logoutView = getActivity().getLayoutInflater().inflate(R.layout.logout_message, null);
        dialogClose.setCancelable(false);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                dialogClose.setContentView(logoutView);
                TextView txtMessage = logoutView.findViewById(R.id.txtMessage);
                txtMessage.setText(R.string.are_you_sure_you_want_to_exit);
                Button btnOk = logoutView.findViewById(R.id.btnOk);
                btnOk.setOnClickListener(v1 -> {
                    dialogClose.dismiss();
                    getActivity().finish();
                    SharedPref.remove("Token", "");
                });
                Button btnClose = logoutView.findViewById(R.id.btnClose);
                btnClose.setOnClickListener(v1 -> {
                    dialogClose.dismiss();
                });
                dialogClose.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialogClose.show();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);

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
                            Log.i("info", "employeeProfile " + employeeProfile);
                            txtEmployeeID.setText(employeeProfile.getHrEmployeeID());
                            txtName.setText(employeeProfile.getEmployeeName());

                            if (employeeProfile.getAccountType().equals(3)) {
                                idMnu01.setEnabled(false);
                            }

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

    private void GetEmployeeAccount() {
        try {
            Call<EmployeeAccount> getContToLocCall = retrofitApiInterface.GetUserAsync("Bearer " + token, appKey, companyID, accountID);

            getContToLocCall.enqueue(new Callback<EmployeeAccount>() {
                @Override
                public void onResponse(Call<EmployeeAccount> call, Response<EmployeeAccount> response) {

                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            // Successfully received response
                            employeeAccount = new EmployeeAccount();
                            employeeAccount = response.body();
                            txtEmployeeID.setText(accountType);
                            txtName.setText(employeeAccount.getDisplayName());

                            if (employeeAccount.getAccountType().equals(3)) {
                                Log.i("info", "employeeAccount " + employeeAccount);
                                boolean isEnabled = idMnu01.isEnabled();
                                for (int i = 0; i < idMnu01.getChildCount(); i++) {
                                    View child = idMnu01.getChildAt(i);
                                    child.setEnabled(false); // Enable individual child views
                                }
                            }

                        } else {
                            if (response.errorBody() != null) {
                                gson = new GsonBuilder().create();
                                serviceResponseViewModel = gson.fromJson(response.errorBody().string(), ServiceResponseViewModel.class);
                                SAlertError(serviceResponseViewModel.getMessage(), getContext());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<EmployeeAccount> call, Throwable t) {
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