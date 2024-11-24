package com.example.chuti.UI;

import static com.example.chuti.Handlers.SMessageHandler.SAlertError;
import static com.example.chuti.Handlers.SMessageHandler.SAlertSuccess;
import static com.example.chuti.Handlers.SMessageHandler.SConnectionFail;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.chuti.Dto.SaveLeaveRequestDto;
import com.example.chuti.Model.EmployeeLeaveCatalogViewModel;
import com.example.chuti.Model.ServiceResponseViewModel;
import com.example.chuti.R;
import com.example.chuti.Security.BaseURL;
import com.example.chuti.Security.Services;
import com.example.chuti.Security.SharedPref;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RequestLeaveFragment extends Fragment {
    Services retrofitApiInterface;
    Gson gson;
    String token, accountID, companyID, userID, appKey;
    SpotsDialog spotsDialog;
    ServiceResponseViewModel serviceResponseViewModel = new ServiceResponseViewModel();
    static List<EmployeeLeaveCatalogViewModel> employeeLeaveCatalogViewModel = new ArrayList<>();
    private static final int GALLERY_REQ_CODE = 1000;
    private DatePickerDialog picker;
    ImageView txtFromDateSelect, txtToDateSelect;
    TextView txtFromDate, txtToDate, txtFileName, txtHeldDays, txtUsedDays, txtAvailableDays,txtRequestedDays;
    String fromDate, toDate;
    Button btnBrowse, btnSubmit;
    Bitmap bitmap;
    ByteArrayOutputStream stream;
    byte[] byteImageArray;
    ImageView takePhoto;
    String encodeImage;
    List<String> catalog;
    Spinner catalogSpinner;
    String employeeCatalogID, selectedItem, leaveTypeID;

    int fromYY, fromMM, fromDD, toYY, toMM, toDD;
    long daysDifference;
    EditText txtReason;
    LocalDate localFromDate = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_request_leave, container, false);

        retrofitApiInterface = BaseURL.getRetrofit().create(Services.class);
        SharedPref.init(getContext());
        gson = new Gson();
        spotsDialog = new SpotsDialog(getContext(), R.style.Custom);
        appKey = SharedPref.read("appKey", "");
        token = SharedPref.read("token", "");
        companyID = SharedPref.read("companyID", "");
        accountID = SharedPref.read("accountID", "");
        userID = SharedPref.read("uId", "");

        txtFromDateSelect = root.findViewById(R.id.txtFromDateSelect);
        txtToDateSelect = root.findViewById(R.id.txtToDateSelect);
        txtFromDate = root.findViewById(R.id.txtFromDate);
        txtToDate = root.findViewById(R.id.txtToDate);
        btnBrowse = root.findViewById(R.id.btnBrowse);
        takePhoto = root.findViewById(R.id.takePhoto);
        txtFileName = root.findViewById(R.id.txtFileName);
        catalogSpinner = root.findViewById(R.id.catalogSpinner);
        txtAvailableDays = root.findViewById(R.id.txtAvailableDays);
        txtUsedDays = root.findViewById(R.id.txtUsedDays);
        txtHeldDays = root.findViewById(R.id.txtHeldDays);
        txtReason = root.findViewById(R.id.txtReason);
        btnSubmit = root.findViewById(R.id.btnSubmit);
        txtRequestedDays = root.findViewById(R.id.txtRequestedDays);

        btnBrowse.setOnClickListener(v -> {

            Intent iGallery = new Intent(Intent.ACTION_PICK);
            iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(iGallery, GALLERY_REQ_CODE);
        });
        EmployeeCurrentLeaveStatistics();
        GetFromDateTime();
        GetToDateTime();

        btnSubmit.setOnClickListener(v -> SaveLeaveRequest());

        return root;
    }

    private void GetFromDateTime() {
        final Calendar c = Calendar.getInstance();
        fromYY = c.get(Calendar.YEAR);
        fromMM = c.get(Calendar.MONTH);
        fromDD = c.get(Calendar.DAY_OF_MONTH);
        txtFromDate.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(fromYY).append("-").append(fromMM + 1).append("-")
                .append(fromDD));
        txtFromDateSelect.setOnClickListener(v -> {
            Drawable iconDrawable = txtFromDateSelect.getDrawable();
            iconDrawable.setColorFilter(getResources().getColor(R.color.redOrange), PorterDuff.Mode.SRC_IN);
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            // date picker dialog
            picker = new DatePickerDialog(getContext(),
                    (view, year1, monthOfYear, dayOfMonth) -> txtFromDate.setText(year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth), year, month, day);
            picker.show();
        });

        fromDate = txtFromDate.getText().toString();
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

    private void GetToDateTime() {
        final Calendar c = Calendar.getInstance();
        toYY = c.get(Calendar.YEAR);
        toMM = c.get(Calendar.MONTH);
        toDD = c.get(Calendar.DAY_OF_MONTH);
        txtToDate.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(toYY).append("-").append(toMM + 1).append("-")
                .append(toDD));
        txtToDateSelect.setOnClickListener(v -> {
            Drawable iconDrawable = txtToDateSelect.getDrawable();
            iconDrawable.setColorFilter(getResources().getColor(R.color.redOrange), PorterDuff.Mode.SRC_IN);

            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            // date picker dialog
            picker = new DatePickerDialog(getContext(),
                    (view, year1, monthOfYear, dayOfMonth) -> txtToDate.setText(year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth), year, month, day);
            picker.show();
        });

        toDate = txtToDate.getText().toString();
    }

    private void EmployeeCurrentLeaveStatistics() {
        try {
            spotsDialog.show(); // Show loading dialog

            // Call the API to get the employee leave catalog
            Call<List<EmployeeLeaveCatalogViewModel>> getContToLocCall = retrofitApiInterface.GetEmployeeLeaveCatalogAsync(
                    "Bearer " + token, appKey, companyID, accountID
            );

            getContToLocCall.enqueue(new Callback<List<EmployeeLeaveCatalogViewModel>>() {
                @Override
                public void onResponse(Call<List<EmployeeLeaveCatalogViewModel>> call, Response<List<EmployeeLeaveCatalogViewModel>> response) {
                    spotsDialog.dismiss(); // Dismiss the loading dialog

                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            // Successfully received response
                            employeeLeaveCatalogViewModel = response.body();
                            Log.i("info", "employeeLeaveCatalogViewModel: " + response.body());

                            // Populate the catalog list
                            List<String> catalog = new ArrayList<>();
                            for (EmployeeLeaveCatalogViewModel item : employeeLeaveCatalogViewModel) {
                                String leaveTypeName = item.getPolicyLeaveViewModel().getLeaveTypeViewModel().getLeaveTypeName();
                                catalog.add(leaveTypeName); // Add leave type names to catalog
                            }

                            // Set up the spinner only if the catalog is not empty
                            if (!catalog.isEmpty()) {
                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(
                                        getContext(),
                                        android.R.layout.simple_spinner_item,
                                        catalog
                                );
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                catalogSpinner.setAdapter(dataAdapter);

                                // Set item selection listener
                                catalogSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        String selectedItem = catalogSpinner.getItemAtPosition(position).toString();

                                        // Find the matching catalog item and update UI
                                        for (EmployeeLeaveCatalogViewModel item : employeeLeaveCatalogViewModel) {
                                            if (selectedItem.equals(item.getPolicyLeaveViewModel().getLeaveTypeViewModel().getLeaveTypeName())) {
                                                leaveTypeID = item.getPolicyLeaveViewModel().getLeaveTypeViewModel().getLeaveTypeID().toString();
                                                employeeCatalogID = item.getEmployeeCatalogID().toString();
                                                txtAvailableDays.setText(String.valueOf(item.getAvailableDays()));
                                                txtHeldDays.setText(String.valueOf(item.getOnHeldDays()));
                                                txtUsedDays.setText(String.valueOf(item.getUsedDays()));
                                                break;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                        // Do nothing if no selection is made
                                    }
                                });
                            }
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
                public void onFailure(Call<List<EmployeeLeaveCatalogViewModel>> call, Throwable t) {
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

    private void SaveLeaveRequest() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            localFromDate = LocalDate.of(fromYY, fromMM, fromDD);
            LocalDate toDate = LocalDate.of(toYY, toMM, toDD);
            LocalDate adjustedFromDate = localFromDate.minusDays(1);

            // Calculate the difference in days
            daysDifference = ChronoUnit.DAYS.between(adjustedFromDate, toDate);
        }

        if (txtReason.getText().toString().isEmpty()) {
            SAlertError("Leave reason is required.", getContext());
            return;
        }
        if (daysDifference < 0) {
            SAlertError("To date is required.", getContext());
            return;
        }

        SaveLeaveRequestDto saveLeaveRequestDto = new SaveLeaveRequestDto();
        saveLeaveRequestDto.setLeaveCatalogID(employeeCatalogID);
        saveLeaveRequestDto.setFromDate(txtFromDate.getText().toString());
        saveLeaveRequestDto.setToDate(txtToDate.getText().toString());
        saveLeaveRequestDto.setNoOfDay(daysDifference);
        saveLeaveRequestDto.setReason(txtReason.getText().toString());
        saveLeaveRequestDto.setDocumentName(txtFileName.getText().toString());
        saveLeaveRequestDto.setDocumentString(encodeImage);
        spotsDialog.show();
        Call<String> saveMachineCall = retrofitApiInterface.SaveLeaveRequestAsync("Bearer " + token, appKey, companyID, accountID, saveLeaveRequestDto);
        saveMachineCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            spotsDialog.dismiss();
                            if (response.code() == 200) {
                                serviceResponseViewModel = new ServiceResponseViewModel();
                                gson = new GsonBuilder().create();
                                try {
                                    serviceResponseViewModel = gson.fromJson(response.body(), ServiceResponseViewModel.class);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                SAlertSuccess(serviceResponseViewModel.getMessage(), getContext());
                                Clear();
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

    private void Clear() {
        txtReason.setText("");
        takePhoto.setImageResource(R.drawable.icon_upload);
        txtFileName.setText("");
    }
}