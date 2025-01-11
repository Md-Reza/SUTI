package com.example.chuti.UI;

import static androidx.core.app.ActivityCompat.recreate;
import static com.example.chuti.FragmentManager.FragmentManager.intentActivity;
import static com.example.chuti.FragmentManager.FragmentManager.replaceFragment;
import static com.example.chuti.Handlers.DateFormatterHandlers.DateTimeParseMonthYearFormatter;
import static com.example.chuti.Handlers.SMessageHandler.SAlertError;
import static com.example.chuti.Handlers.SMessageHandler.SAlertSuccess;
import static com.example.chuti.Handlers.SMessageHandler.SConnectionFail;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;


import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chuti.Dto.UpdateEmployeeSelfDto;
import com.example.chuti.FragmentMain;
import com.example.chuti.LoginActivity;
import com.example.chuti.MainActivity;
import com.example.chuti.Model.EmployeeProfile;
import com.example.chuti.Model.ServiceResponseViewModel;
import com.example.chuti.R;
import com.example.chuti.Security.BaseURL;
import com.example.chuti.Security.Services;
import com.example.chuti.Security.SharedPref;
import com.example.chuti.Utility.LanguageManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

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
    TextView txtJoiningDate, txtLogOut, txtEmployeeName, txtResetPassword, txtStatus,
            txtEmployeeID, txtFileName,txtLangEnglish,txtLangBangla;
    EmployeeProfile employeeProfile;
    Toolbar toolbar;
    String employeeName, hrmsID;

    TextInputEditText txtEditEmployeeName, txtEditEmail, txtEditPhoneNumber;
    ImageView takePhoto, arrow_button;
    Button btnBrowse, btnSave;
    LinearLayout itemHiddenView;
    private static final int GALLERY_REQ_CODE = 1000;

    Bitmap bitmap;
    ByteArrayOutputStream stream;
    byte[] byteImageArray;
    String encodeImage;

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

        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setTitle("Setting");
        toolbar.setSubtitle("");
        toolbar.setNavigationOnClickListener(v -> {
            toolbar.setTitle(R.string.chuti);
            replaceFragment(new FragmentMain(), getContext());
        });

        LanguageManager languageManager=new LanguageManager(getContext());

        txtJoiningDate = root.findViewById(R.id.txtJoiningDate);
        txtLogOut = root.findViewById(R.id.txtLogOut);
        txtEmployeeName = root.findViewById(R.id.txtEmployeeName);
        txtResetPassword = root.findViewById(R.id.txtResetPassword);
        txtStatus = root.findViewById(R.id.txtStatus);
        txtEmployeeID = root.findViewById(R.id.txtEmployeeID);
        txtEditEmployeeName = root.findViewById(R.id.txtEditEmployeeName);
        txtEditEmail = root.findViewById(R.id.txtEditEmail);
        txtEditPhoneNumber = root.findViewById(R.id.txtEditPhoneNumber);
        takePhoto = root.findViewById(R.id.takePhoto);
        btnBrowse = root.findViewById(R.id.btnBrowse);
        btnSave = root.findViewById(R.id.btnSave);
        arrow_button = root.findViewById(R.id.arrow_button);
        itemHiddenView = root.findViewById(R.id.itemHiddenView);
        txtFileName = root.findViewById(R.id.txtFileName);
        txtLangBangla = root.findViewById(R.id.txtLangBangla);
        txtLangEnglish = root.findViewById(R.id.txtLangEnglish);

        txtLogOut.setOnClickListener(v -> {
            SharedPref.remove("token", "");
            Intent i1 = new Intent(getContext(),
                    LoginActivity.class);
            startActivity(i1);
        });

        txtResetPassword.setOnClickListener(v -> replaceFragment(new FragmentResetPassword(), getContext()));

        GetEmployee();

        arrow_button.setOnClickListener(v -> {
            if (itemHiddenView.getVisibility() == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(itemHiddenView, new AutoTransition());
                itemHiddenView.setVisibility(View.GONE);
                arrow_button.setImageResource(R.drawable.icon_expand_more_24);
            } else {
                TransitionManager.beginDelayedTransition(itemHiddenView, new AutoTransition());
                itemHiddenView.setVisibility(View.VISIBLE);
                arrow_button.setImageResource(R.drawable.icon_expand_less_24);
            }
        });

        btnBrowse.setOnClickListener(v -> {
            Intent iGallery = new Intent(Intent.ACTION_PICK);
            iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(iGallery, GALLERY_REQ_CODE);
        });
        takePhoto.setOnClickListener(v -> {
            Intent iGallery = new Intent(Intent.ACTION_PICK);
            iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(iGallery, GALLERY_REQ_CODE);
        });

        btnSave.setOnClickListener(v -> UpdateEmployeeSelf());

        txtLangBangla.setOnClickListener(v -> {
            languageManager.updateResource("bn");
            getActivity().recreate();
        });
        txtLangEnglish.setOnClickListener(v -> {
            languageManager.updateResource("en");
            getActivity().recreate();
        });

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
                            txtEditEmployeeName.setText(employeeProfile.getEmployeeName());
                            txtEditEmail.setText(employeeProfile.getEmailAddress());
                            txtEditPhoneNumber.setText(employeeProfile.getPhoneNo());

                            txtJoiningDate.setText(DateTimeParseMonthYearFormatter(employeeProfile.getJoiningDate()));
                            employeeName = employeeProfile.getEmployeeName();
                            hrmsID = employeeProfile.getHrEmployeeID();
                            SharedPref.write("employeeName", employeeName);
                            SharedPref.write("hrmsID", hrmsID);
                            Boolean enable = employeeProfile.getEnabled();
                            if (enable)
                                txtStatus.setText(R.string.active);
                            else
                                txtStatus.setText(R.string.inactive);

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == GALLERY_REQ_CODE) {

            if (resultCode == Activity.RESULT_OK) {

                if (data != null) {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);

                byteImageArray = stream.toByteArray();

                Bitmap bitmap = BitmapFactory.decodeByteArray(byteImageArray, 0,
                        byteImageArray.length);
                takePhoto.setImageBitmap(bitmap);
                takePhoto.setScaleType(ImageView.ScaleType.FIT_XY);
                Uri selectedImageUri = data.getData();
                String fileName = getFileName(selectedImageUri);
                txtFileName.setText(fileName);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    encodeImage = Base64.getEncoder().encodeToString(byteImageArray);
                }
            }
        }
    }

    public String getFileName(Uri uri) {
        String fileName = null;
        long size = 0;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                    if (nameIndex != -1) {
                        fileName = cursor.getString(nameIndex);
                    }
                    if (sizeIndex != -1) {
                        size = cursor.getLong(sizeIndex);
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }

        if (fileName == null) {
            fileName = uri.getLastPathSegment();
        }
        return fileName;
    }
    private void UpdateEmployeeSelf() {
        spotsDialog.show();
        UpdateEmployeeSelfDto updateEmployeeSelfDto = new UpdateEmployeeSelfDto();
        updateEmployeeSelfDto.setEmployeeName(txtEditEmployeeName.getText().toString());
        updateEmployeeSelfDto.setEmailAddress(txtEditEmail.getText().toString());
        updateEmployeeSelfDto.setPhoneNo(txtEditPhoneNumber.getText().toString());
        updateEmployeeSelfDto.setProfileImageString(encodeImage);
        updateEmployeeSelfDto.setProfileImageName(txtFileName.getText().toString());


        Call<String> saveMachineCall = retrofitApiInterface.UpdateEmployeeSelfAsync("Bearer " + token, appKey, companyID, accountID, updateEmployeeSelfDto);
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
                                if (itemHiddenView.getVisibility() == View.VISIBLE) {
                                    TransitionManager.beginDelayedTransition(itemHiddenView, new AutoTransition());
                                    itemHiddenView.setVisibility(View.GONE);
                                    arrow_button.setImageResource(R.drawable.icon_expand_more_24);
                                } else {
                                    TransitionManager.beginDelayedTransition(itemHiddenView, new AutoTransition());
                                    itemHiddenView.setVisibility(View.VISIBLE);
                                    arrow_button.setImageResource(R.drawable.icon_expand_less_24);
                                }
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

    @Override
    public void onResume() {
        super.onResume();

        // Hide the FAB
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setFabVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        // Show the FAB again when the fragment is not visible
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setFabVisibility(View.VISIBLE);
        }
    }
}