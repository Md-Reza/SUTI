package com.example.chuti.UI;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chuti.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Calendar;


public class RequestLeaveFragment extends Fragment {
    private static final int GALLERY_REQ_CODE = 1000;
    private DatePickerDialog picker;
    ImageView txtFromDateSelect, txtToDateSelect;
    TextView txtFromDate, txtToDate, txtFileName;
    String fromDate, toDate;
    Button btnBrowse;
    Bitmap bitmap;
    ByteArrayOutputStream stream;
    byte[] byteImageArray;
    ImageView takePhoto;
    String encodeImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_request_leave, container, false);

        txtFromDateSelect = root.findViewById(R.id.txtFromDateSelect);
        txtToDateSelect = root.findViewById(R.id.txtToDateSelect);
        txtFromDate = root.findViewById(R.id.txtFromDate);
        txtToDate = root.findViewById(R.id.txtToDate);
        btnBrowse = root.findViewById(R.id.btnBrowse);
        takePhoto = root.findViewById(R.id.takePhoto);
        txtFileName = root.findViewById(R.id.txtFileName);

        btnBrowse.setOnClickListener(v -> {

            Intent iGallery = new Intent(Intent.ACTION_PICK);
            iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(iGallery, GALLERY_REQ_CODE);
        });

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