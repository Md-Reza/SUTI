package com.example.chuti.UI;

import static com.example.chuti.FragmentManager.FragmentManager.intentActivity;
import static com.example.chuti.FragmentManager.FragmentManager.replaceFragment;

import android.app.TimePickerDialog;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.chuti.FragmentMain;
import com.example.chuti.MainActivity;
import com.example.chuti.Model.ServiceResponseViewModel;
import com.example.chuti.R;
import com.example.chuti.Security.BaseURL;
import com.example.chuti.Security.Services;
import com.example.chuti.Security.SharedPref;
import com.google.gson.Gson;

import java.util.Calendar;

import dmax.dialog.SpotsDialog;

public class EmployeeGatepassFragment extends Fragment {
    Services retrofitApiInterface;
    Gson gson;
    String token, accountID, companyID, userID, appKey;
    SpotsDialog spotsDialog;
    ServiceResponseViewModel serviceResponseViewModel = new ServiceResponseViewModel();
    ImageView txtFromTimeSelect,txtToTimeSelect;
    TextView txtFromTime,txtToTime;
    private TimePickerDialog timePicker;
    Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_employee_gatepass, container, false);

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


        txtFromTimeSelect=root.findViewById(R.id.txtFromTimeSelect);
        txtToTimeSelect=root.findViewById(R.id.txtToTimeSelect);
        txtFromTime=root.findViewById(R.id.txtFromTime);
        txtToTime=root.findViewById(R.id.txtToTime);

        GetFromTime();
        GetToTime();

        return root;
    }

    private void GetFromTime() {
        txtFromTimeSelect.setOnClickListener(v -> {
            Drawable iconDrawable = txtFromTimeSelect.getDrawable();
            iconDrawable.setColorFilter(getResources().getColor(R.color.redOrange), PorterDuff.Mode.SRC_IN);
            final Calendar cldr = Calendar.getInstance();
            int hour = cldr.get(Calendar.HOUR_OF_DAY);
            int minutes = cldr.get(Calendar.MINUTE);
            // time picker dialog
            timePicker = new TimePickerDialog(getContext(),
                    (tp, sHour, sMinute) -> txtFromTime.setText(sHour + ":" + sMinute), hour, minutes, true);
            timePicker.show();
        });
    }
    private void GetToTime() {
        txtToTimeSelect.setOnClickListener(v -> {
            Drawable iconDrawable = txtToTimeSelect.getDrawable();
            iconDrawable.setColorFilter(getResources().getColor(R.color.redOrange), PorterDuff.Mode.SRC_IN);
            final Calendar cldr = Calendar.getInstance();
            int hour = cldr.get(Calendar.HOUR_OF_DAY);
            int minutes = cldr.get(Calendar.MINUTE);
            // time picker dialog
            timePicker = new TimePickerDialog(getContext(),
                    (tp, sHour, sMinute) -> txtToTime.setText(sHour + ":" + sMinute), hour, minutes, true);
            timePicker.show();
        });
    }
}