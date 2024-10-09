package com.example.chuti.UI;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chuti.R;

import java.util.Calendar;


public class RequestLeaveFragment extends Fragment {

    private DatePickerDialog picker;
    ImageView txtFromDateSelect, txtToDateSelect;
    TextView txtFromDate, txtToDate;
    String fromDate, toDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_request_leave, container, false);

        txtFromDateSelect = root.findViewById(R.id.txtFromDateSelect);
        txtToDateSelect = root.findViewById(R.id.txtToDateSelect);
        txtFromDate = root.findViewById(R.id.txtFromDate);
        txtToDate = root.findViewById(R.id.txtToDate);

        GetFromDateTime();
        GetToDateTime();
        return root;
    }

    private void GetFromDateTime() {
        final Calendar c = Calendar.getInstance();
        int yy = c.get(Calendar.YEAR);
        int mm = c.get(Calendar.MONTH);
        int dd = c.get(Calendar.DAY_OF_MONTH);
        txtFromDate.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(yy).append("-").append(mm + 1).append("-")
                .append(dd));
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

    private void GetToDateTime() {
        final Calendar c = Calendar.getInstance();
        int yy = c.get(Calendar.YEAR);
        int mm = c.get(Calendar.MONTH);
        int dd = c.get(Calendar.DAY_OF_MONTH);
        txtToDate.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(yy).append("-").append(mm + 1).append("-")
                .append(dd));
        txtToDateSelect.setOnClickListener(v -> {
            Drawable iconDrawable = txtToDateSelect.getDrawable();
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

        toDate = txtToDate.getText().toString();
    }
}