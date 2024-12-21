package com.example.chuti.UI;

import static com.example.chuti.FragmentManager.FragmentManager.replaceFragment;
import static com.example.chuti.Handlers.SMessageHandler.SAlertError;
import static com.example.chuti.Handlers.SMessageHandler.SAlertSuccess;
import static com.example.chuti.Handlers.SMessageHandler.SConnectionFail;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chuti.Dto.OutpassDto;
import com.example.chuti.Dto.SaveLeaveRequestDto;
import com.example.chuti.FragmentMain;
import com.example.chuti.Model.ServiceResponseViewModel;
import com.example.chuti.R;
import com.example.chuti.Security.BaseURL;
import com.example.chuti.Security.Services;
import com.example.chuti.Security.SharedPref;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeGatepassFragment extends Fragment {
    Services retrofitApiInterface;
    Gson gson;
    String token, accountID, companyID, userID, appKey;
    SpotsDialog spotsDialog;
    ServiceResponseViewModel serviceResponseViewModel = new ServiceResponseViewModel();
    ImageView txtFromTimeSelect, txtToTimeSelect;
    TextView txtFromTime, txtToTime, txtRequestedTime, txtToDateTime, txtFromDateTime;
    private TimePickerDialog timePicker;
    Toolbar toolbar;
    SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    Date date1, date2;
    Calendar cldr;
    CheckBox txtCheckIsFullDay;
    EditText txtReason;
    Button btnSubmit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_employee_gatepass, container, false);

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
        toolbar.setTitle("Employee Gate Pass Request");
        toolbar.setSubtitle("");
        toolbar.setNavigationOnClickListener(v -> {
            toolbar.setTitle(R.string.chuti);
            replaceFragment(new FragmentMain(), getContext());
        });


        txtFromTimeSelect = root.findViewById(R.id.txtFromTimeSelect);
        txtToTimeSelect = root.findViewById(R.id.txtToTimeSelect);
        txtFromTime = root.findViewById(R.id.txtFromTime);
        txtToTime = root.findViewById(R.id.txtToTime);
        txtRequestedTime = root.findViewById(R.id.txtRequestedTime);
        txtCheckIsFullDay = root.findViewById(R.id.txtCheckIsFullDay);
        btnSubmit = root.findViewById(R.id.btnSubmit);
        txtReason = root.findViewById(R.id.txtReason);
        txtToDateTime = root.findViewById(R.id.txtToDateTime);
        txtFromDateTime = root.findViewById(R.id.txtFromDateTime);

        GetFromTime();
        GetToTime();

        txtFromTime.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            try {
                String fromTime = txtFromTime.getText().toString();
                String toTime = txtToTime.getText().toString();

                if (fromTime.isEmpty() || toTime.isEmpty()) {
                    txtRequestedTime.setText("Please enter both times");
                    return;
                }

                date1 = format.parse(fromTime);
                date2 = format.parse(toTime);

                long difference = date2.getTime() - date1.getTime();

                long hours = (difference / (1000 * 60 * 60)) % 24;
                long minutes1 = (difference / (1000 * 60)) % 60;

                txtRequestedTime.setText("Duration: " + hours + " hours and " + minutes1 + " minutes");

            } catch (ParseException e) {
                txtRequestedTime.setText("Invalid time format");
                e.printStackTrace();
            }
        });

        txtToTime.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            try {
                String fromTime = txtFromTime.getText().toString();
                String toTime = txtToTime.getText().toString();

                if (fromTime.isEmpty() || toTime.isEmpty()) {
                    txtRequestedTime.setText("Please enter both times");
                    return;
                }

                date1 = format.parse(fromTime);
                date2 = format.parse(toTime);

                long difference = date2.getTime() - date1.getTime();

                long hours = (difference / (1000 * 60 * 60)) % 24;
                long minutes1 = (difference / (1000 * 60)) % 60;

                txtRequestedTime.setText("Duration: " + hours + " hours and " + minutes1 + " minutes");

            } catch (ParseException e) {
                txtRequestedTime.setText("Invalid time format");
                e.printStackTrace();
            }
        });


        btnSubmit.setOnClickListener(v -> SaveGatePass());

        return root;
    }

    private void GetFromTime() {
        txtFromTimeSelect.setOnClickListener(v -> {
            Drawable iconDrawable = txtFromTimeSelect.getDrawable();
            iconDrawable.setColorFilter(getResources().getColor(R.color.redOrange), PorterDuff.Mode.SRC_IN);

            final Calendar cldr = Calendar.getInstance();
            int hour = cldr.get(Calendar.HOUR_OF_DAY);
            int minutes = cldr.get(Calendar.MINUTE);
            sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            format = new SimpleDateFormat("hh:mm a"); // Use 12-hour format with AM/PM

            timePicker = new TimePickerDialog(getContext(), (tp, sHour, sMinute) -> {
                // Convert selected 24-hour time to 12-hour format
                Calendar selectedTime = Calendar.getInstance();
                selectedTime.set(Calendar.HOUR_OF_DAY, sHour);
                selectedTime.set(Calendar.MINUTE, sMinute);

                format = new SimpleDateFormat("hh:mm a"); // 12-hour format with AM/PM
                txtFromTime.setText(format.format(selectedTime.getTime()));
                txtFromDateTime.setText(sdf.format(selectedTime.getTime()));

            }, hour, minutes, false);

            timePicker.show();
            timePicker.setOnDismissListener(dialog -> {
                try {
                    date1 = format.parse(txtFromTime.getText().toString());
                    date2 = format.parse(txtToTime.getText().toString());

                    long difference = date2.getTime() - date1.getTime();

                    long hours = (difference / (1000 * 60 * 60)) % 24;
                    long minutes1 = (difference / (1000 * 60)) % 60;

                    txtRequestedTime.setText("Duration: " + hours + " hours and " + minutes1 + " minutes");

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            });
        });
        txtFromTime.setOnClickListener(v -> {
            Drawable iconDrawable = txtFromTimeSelect.getDrawable();
            iconDrawable.setColorFilter(getResources().getColor(R.color.redOrange), PorterDuff.Mode.SRC_IN);

            final Calendar cldr = Calendar.getInstance();
            int hour = cldr.get(Calendar.HOUR_OF_DAY);
            int minutes = cldr.get(Calendar.MINUTE);
            sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            format = new SimpleDateFormat("hh:mm a"); // Use 12-hour format with AM/PM

            timePicker = new TimePickerDialog(getContext(), (tp, sHour, sMinute) -> {
                // Convert selected 24-hour time to 12-hour format
                Calendar selectedTime = Calendar.getInstance();
                selectedTime.set(Calendar.HOUR_OF_DAY, sHour);
                selectedTime.set(Calendar.MINUTE, sMinute);

                format = new SimpleDateFormat("hh:mm a"); // 12-hour format with AM/PM
                txtFromTime.setText(format.format(selectedTime.getTime()));
                txtFromDateTime.setText(sdf.format(selectedTime.getTime()));

            }, hour, minutes, false);
            timePicker.show();

            try {
                date1 = format.parse(txtFromTime.getText().toString());
                date2 = format.parse(txtToTime.getText().toString());

                long difference = date2.getTime() - date1.getTime();

                long hours = (difference / (1000 * 60 * 60)) % 24;
                long minutes1 = (difference / (1000 * 60)) % 60;

                txtRequestedTime.setText("Duration: " + hours + " hours and " + minutes1 + " minutes");

            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
    }

    private void GetToTime() {
        txtToTimeSelect.setOnClickListener(v -> {
            Drawable iconDrawable = txtToTimeSelect.getDrawable();
            iconDrawable.setColorFilter(getResources().getColor(R.color.redOrange), PorterDuff.Mode.SRC_IN);

            cldr = Calendar.getInstance();
            int hour = cldr.get(Calendar.HOUR_OF_DAY);
            int minutes = cldr.get(Calendar.MINUTE);
            sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            timePicker = new TimePickerDialog(getContext(), (tp, sHour, sMinute) -> {
                // Convert selected time to 12-hour format with AM/PM
                Calendar selectedTime = Calendar.getInstance();
                selectedTime.set(Calendar.HOUR_OF_DAY, sHour);
                selectedTime.set(Calendar.MINUTE, sMinute);

                format = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                txtToTime.setText(format.format(selectedTime.getTime()));
                txtToDateTime.setText(sdf.format(selectedTime.getTime()));
            }, hour, minutes, false);

            try {
                date1 = format.parse(txtFromTime.getText().toString());
                date2 = format.parse(txtToTime.getText().toString());

                long difference = date2.getTime() - date1.getTime();

                long hours = (difference / (1000 * 60 * 60)) % 24;
                long minutes1 = (difference / (1000 * 60)) % 60;

                txtRequestedTime.setText("Duration: " + hours + " hours and " + minutes1 + " minutes");

            } catch (ParseException e) {
                e.printStackTrace();
            }
            timePicker.show();
        });
        txtToTime.setOnClickListener(v -> {
            Drawable iconDrawable = txtToTimeSelect.getDrawable();
            iconDrawable.setColorFilter(getResources().getColor(R.color.redOrange), PorterDuff.Mode.SRC_IN);

            cldr = Calendar.getInstance();
            int hour = cldr.get(Calendar.HOUR_OF_DAY);
            int minutes = cldr.get(Calendar.MINUTE);
            sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            timePicker = new TimePickerDialog(getContext(), (tp, sHour, sMinute) -> {
                // Convert selected time to 12-hour format with AM/PM
                Calendar selectedTime = Calendar.getInstance();
                selectedTime.set(Calendar.HOUR_OF_DAY, sHour);
                selectedTime.set(Calendar.MINUTE, sMinute);

                format = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                txtToTime.setText(format.format(selectedTime.getTime()));
                txtToDateTime.setText(sdf.format(selectedTime.getTime()));
            }, hour, minutes, false);


            timePicker.show();
            timePicker.setOnDismissListener(dialog -> {
                try {
                    date1 = format.parse(txtFromTime.getText().toString());
                    date2 = format.parse(txtToTime.getText().toString());

                    long difference = date2.getTime() - date1.getTime();

                    long hours = (difference / (1000 * 60 * 60)) % 24;
                    long minutes1 = (difference / (1000 * 60)) % 60;

                    txtRequestedTime.setText("Duration: " + hours + " hours and " + minutes1 + " minutes");

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            });

        });

    }

    private void SaveGatePass() {
        if (txtReason.getText().toString().isEmpty()) {
            SAlertError("OutPass reason is required", getContext());
            return;
        }

        OutpassDto outpassDto = new OutpassDto();
        outpassDto.setFromTime(txtFromDateTime.getText().toString());
        outpassDto.setToTime(txtToDateTime.getText().toString());
        outpassDto.setReason(txtReason.getText().toString());

        if (txtCheckIsFullDay.isChecked())
            outpassDto.setFullDay(true);
        else
            outpassDto.setFullDay(false);

        spotsDialog.show();
        Call<String> saveMachineCall = retrofitApiInterface.SaveOutpassAsync("Bearer " + token, appKey, companyID, accountID, outpassDto);
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
                                txtReason.setText("");
                                txtFromTime.setText("");
                                txtToTime.setText("");
                                txtRequestedTime.setText(R.string.total_duration_00_minutes);
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