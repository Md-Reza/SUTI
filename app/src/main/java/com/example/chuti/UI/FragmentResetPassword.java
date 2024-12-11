package com.example.chuti.UI;

import static com.example.chuti.FragmentManager.FragmentManager.intentActivity;
import static com.example.chuti.Handlers.SMessageHandler.SAlertError;
import static com.example.chuti.Handlers.SMessageHandler.SAlertSuccess;
import static com.example.chuti.Handlers.SMessageHandler.SConnectionFail;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chuti.Dto.ApproveLeaveRequestDto;
import com.example.chuti.Dto.ResetPasswordDto;
import com.example.chuti.LoginActivity;
import com.example.chuti.MainActivity;
import com.example.chuti.Model.ServiceResponseViewModel;
import com.example.chuti.R;
import com.example.chuti.Security.BaseURL;
import com.example.chuti.Security.Services;
import com.example.chuti.Security.SharedPref;
import com.example.chuti.Utility.OnSwipeTouchListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentResetPassword extends Fragment {
    Services retrofitApiInterface;
    Gson gson;
    String token, accountID, companyID, userID, appKey;
    SpotsDialog spotsDialog;
    ServiceResponseViewModel serviceResponseViewModel = new ServiceResponseViewModel();
    Toolbar toolbar;
    TextView txtUserID, txtUserName, txtSwipeRight;
    ImageView ivBack;
    Button btnReset;
    TextInputEditText txtConfirmPassword, txtPassword, txtOldPassword;

    String employeeName, hrmsID;
    TextInputLayout layoutUserPassword, layoutConfirmPassword;

    ResetPasswordDto resetPasswordDto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_reset_password, container, false);

        SharedPref.init(getContext());


        retrofitApiInterface = BaseURL.getRetrofit().create(Services.class);
        SharedPref.init(getContext());
        gson = new Gson();
        spotsDialog = new SpotsDialog(getContext(), R.style.Custom);
        appKey = SharedPref.read("appKey", "");
        token = SharedPref.read("token", "");
        companyID = SharedPref.read("companyID", "");
        accountID = SharedPref.read("accountID", "");
        userID = SharedPref.read("uId", "");
        employeeName = SharedPref.read("employeeName", "");
        hrmsID = SharedPref.read("hrmsID", "");

        txtUserID = root.findViewById(R.id.txtUserID);
        txtUserName = root.findViewById(R.id.txtUserName);
        txtSwipeRight = root.findViewById(R.id.txtSwipeRight);
        ivBack = root.findViewById(R.id.ivBack);
        btnReset = root.findViewById(R.id.btnReset);
        txtConfirmPassword = root.findViewById(R.id.txtConfirmPassword);
        txtPassword = root.findViewById(R.id.txtPassword);
        txtOldPassword = root.findViewById(R.id.txtOldPassword);
        layoutConfirmPassword = root.findViewById(R.id.layoutConfirmPassword);
        layoutUserPassword = root.findViewById(R.id.layoutUserPassword);

        txtUserName.setText(employeeName);
        txtUserID.setText(hrmsID);


        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setTitle("Change Password");
        toolbar.setSubtitle("");
        toolbar.setNavigationOnClickListener(v -> intentActivity(new MainActivity(), getContext()));


        ivBack.setOnClickListener(v ->
                intentActivity(new MainActivity(), getContext()));
        txtSwipeRight.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            public void onSwipeRight() {
                intentActivity(new MainActivity(), getContext());
            }
            public void onSwipeLeft() {
                intentActivity(new MainActivity(), getContext());
            }
        });

        txtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String password = charSequence.toString();
                if (password.length() >= 4) {

                    Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
                    Matcher matcher = pattern.matcher(password);
                    boolean isPwdContainsSpeChar = matcher.find();
                    if (isPwdContainsSpeChar) {
                        layoutUserPassword.setHelperText("Strong Password");
                        layoutUserPassword.setError("");
                    } else {
                        layoutUserPassword.setHelperText("");
                        layoutUserPassword.setError("Weak Password. Include minimum 1 special char.");
                    }

                } else {
                    layoutUserPassword.setHelperText("The field New Password must be a string with a minimum length of 4 and a maximum length of 20.");
                    layoutUserPassword.setError("");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        txtConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String password = charSequence.toString();
                if (password.length() >= 4) {

                    Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
                    Matcher matcher = pattern.matcher(password);
                    boolean isPwdContainsSpeChar = matcher.find();
                    if (isPwdContainsSpeChar) {
                        layoutConfirmPassword.setHelperText("Strong Password.");
                        layoutConfirmPassword.setError("");
                    } else {
                        layoutConfirmPassword.setHelperText("");
                        layoutConfirmPassword.setError("Weak Password. Include minimum 1 special char.");
                    }
                    if (!txtPassword.getText().toString().equals(txtConfirmPassword.getText().toString())) {
                        layoutConfirmPassword.setHelperText("The field Confirm Password must be a string with a minimum length of 4 and a maximum length of 20. and New password and confirm password is not matched.");
                        layoutConfirmPassword.setError("");
                    }

                } else {
                    layoutConfirmPassword.setHelperText("The field Confirm Password must be a string with a minimum length of 4 and a maximum length of 20. and New password and confirm password is not matched.");
                    layoutConfirmPassword.setError("");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnReset.setOnClickListener(v -> ChangePassword());

        return root;
    }

    public void ChangePassword() {
        spotsDialog.show();
        resetPasswordDto = new ResetPasswordDto();
        resetPasswordDto.setOldPassword(txtOldPassword.getText().toString());
        resetPasswordDto.setNewPassword(txtPassword.getText().toString());
        resetPasswordDto.setConfirmPassword(txtConfirmPassword.getText().toString());

        Call<String> saveMachineCall = retrofitApiInterface.ChangePasswordAsync("Bearer " + token, appKey, companyID, accountID, resetPasswordDto);
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
                                SharedPref.remove("token", "");
                                Intent i1 = new Intent(getContext(),
                                        LoginActivity.class);
                                startActivity(i1);
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
    }
}