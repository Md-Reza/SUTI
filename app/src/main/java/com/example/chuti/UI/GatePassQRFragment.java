package com.example.chuti.UI;

import static com.example.chuti.FragmentManager.FragmentManager.replaceFragment;
import static com.example.chuti.Handlers.DateFormatterHandlers.DateTimeParseFormatter;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chuti.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;


public class GatePassQRFragment extends Fragment {

    ImageView txtBarcode;
    Toolbar toolbar;
    Bundle bundle;
    String outPassID, reqDate, hrmsID, name;
    TextView txtEmployeeID, txtName, txtReqDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_gate_pass_q_r, container, false);

        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setTitle("Out Pass QR Code");
        toolbar.setSubtitle("");
        toolbar.setNavigationOnClickListener(v -> {
            toolbar.setTitle(R.string.chuti);
            replaceFragment(new FragmentBalance(), getContext());
        });

        bundle = getArguments();
        if (bundle != null) {
            outPassID = (bundle.getString("key1"));
            hrmsID = (bundle.getString("key"));
            name = (bundle.getString("key2"));
            reqDate = (bundle.getString("key3"));
        }

        txtBarcode = root.findViewById(R.id.txtBarcode);
        txtEmployeeID = root.findViewById(R.id.txtEmployeeID);
        txtName = root.findViewById(R.id.txtName);
        txtReqDate = root.findViewById(R.id.txtReqDate);

        try {
            txtReqDate.setText(String.format("Request Date: %s", DateTimeParseFormatter(reqDate)));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        try {
            txtEmployeeID.setText(String.format("HRMS ID: %s", hrmsID));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        try {
            txtName.setText(String.format("Name: %s", name));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(outPassID, BarcodeFormat.QR_CODE, 400, 400);
            txtBarcode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return root;
    }
}