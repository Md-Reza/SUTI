package com.example.chuti.UI;

import static com.example.chuti.Handlers.DateFormatterHandlers.ConvertDateToTime;
import static com.example.chuti.Handlers.SMessageHandler.SAlertError;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;


import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chuti.Model.OutPassViewModel;
import com.example.chuti.Model.ServiceResponseViewModel;
import com.example.chuti.R;
import com.example.chuti.Security.BaseURL;
import com.example.chuti.Security.Services;
import com.example.chuti.Security.SharedPref;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentOutpassApproval extends Fragment {
    Services retrofitApiInterface;
    Gson gson;
    String token, accountID, companyID, userID, appKey, scanContData;
    SpotsDialog spotsDialog;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    static final int REQUEST_CAMERA_PERMISSION = 201;
    ToneGenerator toneGen1;
    ServiceResponseViewModel serviceResponseViewModel = new ServiceResponseViewModel();
    Toolbar toolbar;
    TextInputEditText txtOutPassID;
    TextView txtEmpName, txtDuration, txtFromTime, txtToTime, txtReason, txtHRMSID, txtStatusCode;
    LinearLayout  outPassViewLayout;
    CardView viewOutpass;
    ImageView ivScanner;
    Button  btnScan, GoBack;
    MaterialButton btnSubmit;

    OutPassViewModel outPassViewModel = new OutPassViewModel();

    SurfaceView surface_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_outpass_approval, container, false);

        retrofitApiInterface = BaseURL.getRetrofit().create(Services.class);
        SharedPref.init(getContext());
        gson = new Gson();
        spotsDialog = new SpotsDialog(getContext(), R.style.Custom);
        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        appKey = SharedPref.read("appKey", "");
        token = SharedPref.read("token", "");
        companyID = SharedPref.read("companyID", "");
        accountID = SharedPref.read("accountID", "");
        userID = SharedPref.read("uId", "");

        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Outpass Approval");
        toolbar.setSubtitle("");

        toolbar = getActivity().findViewById(R.id.toolbar);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.chuti_logo);
        int iconSize = getResources().getDimensionPixelSize(R.dimen.navigation_icon_size); // Define size in dimens.xml
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, iconSize, iconSize, false);
        BitmapDrawable drawable = new BitmapDrawable(getResources(), scaledBitmap);

        toolbar.setNavigationIcon(drawable);


        viewOutpass = root.findViewById(R.id.viewOutpass);
        txtOutPassID = root.findViewById(R.id.txtOutPassID);
        txtEmpName = root.findViewById(R.id.txtEmpName);
        txtDuration = root.findViewById(R.id.txtDuration);
        txtFromTime = root.findViewById(R.id.txtFromTime);
        txtToTime = root.findViewById(R.id.txtToTime);
        txtReason = root.findViewById(R.id.txtReason);
        txtHRMSID = root.findViewById(R.id.txtHRMSID);
        txtStatusCode = root.findViewById(R.id.txtStatusCode);
        ivScanner = root.findViewById(R.id.ivScanner);
        btnSubmit = root.findViewById(R.id.btnSubmit);
        btnScan = root.findViewById(R.id.btnScan);

        surface_view = root.findViewById(R.id.surface_view);
        outPassViewLayout = root.findViewById(R.id.outPassViewLayout);
        GoBack = root.findViewById(R.id.GoBack);

        GoBack = root.findViewById(R.id.GoBack);
        GoBack.setOnClickListener(v -> {
            outPassViewLayout.setVisibility(View.VISIBLE);
            surface_view.setVisibility(View.GONE);
            GoBack.setVisibility(View.GONE);
        });

        ivScanner.setOnClickListener(v -> {
            GatePassIDScanner();
            outPassViewLayout.setVisibility(View.GONE);
            surface_view.setVisibility(View.VISIBLE);
            GoBack.setVisibility(View.VISIBLE);
        });
        txtOutPassID.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                    case KeyEvent.KEYCODE_ENTER:

                        if (txtOutPassID.getText().toString().isEmpty()) {
                            txtOutPassID.setError("Please scan or enter outpass id.");
                        } else {
                            GetOutpass();
                        }
                        return true;
                    default:
                        break;
                }
            }
            return false;
        });
        btnScan.setOnClickListener(v -> {
            if (txtOutPassID.getText().toString().isEmpty()) {
                txtOutPassID.setError("Please scan or enter outpass id.");
            } else {
                GetOutpass();
            }
        });


        return root;
    }

    private void GetOutpass() {
        try {
            Call<OutPassViewModel> getContToLocCall = retrofitApiInterface.GetOutpassRequestAsync("Bearer" + " " + token, appKey, companyID, txtOutPassID.getText().toString());
            getContToLocCall.enqueue(new Callback<OutPassViewModel>() {
                @Override
                public void onResponse(Call<OutPassViewModel> call, Response<OutPassViewModel> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                spotsDialog.dismiss();
                                viewOutpass.setVisibility(View.VISIBLE);
                                outPassViewModel = new OutPassViewModel();
                                outPassViewModel = response.body();
                                txtEmpName.setText(outPassViewModel.getEmployeeCompactViewModel().getEmployeeName());
                                txtHRMSID.setText("ID: " + outPassViewModel.getEmployeeCompactViewModel().getHrEmployeeID());
                                txtReason.setText("Reason: " + outPassViewModel.getReason());

                                try {
                                    int hours = outPassViewModel.getDurationMin() / 60;  // Divide total minutes by 60 to get hours
                                    int minutes = outPassViewModel.getDurationMin() % 60;
                                    txtDuration.setText("Duration: "+String.format("%dh,%dm", hours, minutes));
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                                switch (outPassViewModel.getStatus()) {
                                    case 0:
                                        txtStatusCode.setText(R.string.created);
                                        txtStatusCode.setBackgroundResource(R.drawable.created_button);
                                        break;
                                    case 2:
                                        txtStatusCode.setText(R.string.approved);
                                        txtStatusCode.setBackgroundResource(R.drawable.approved_button);
                                        break;

                                    default:
                                        break;
                                }
                                try {
                                    txtFromTime.setText("From Time: "+ConvertDateToTime(outPassViewModel.getFromTime()));
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    txtToTime.setText("To Time :"+ConvertDateToTime(outPassViewModel.getToTime()));
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            if (response.errorBody() != null) {
                                spotsDialog.dismiss();
                                gson = new GsonBuilder().create();
                                try {
                                    serviceResponseViewModel = gson.fromJson(response.errorBody().string(), ServiceResponseViewModel.class);
                                    SAlertError(serviceResponseViewModel.getMessage(), getContext());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } catch (Exception e) {
                        spotsDialog.dismiss();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<OutPassViewModel> call, Throwable t) {
                    SAlertError(t.getMessage(), getContext());
                    spotsDialog.dismiss();
                }
            });
        } catch (Exception e) {
            spotsDialog.dismiss();
            e.printStackTrace();
        }
    }

    private void GatePassIDScanner() {
        barcodeDetector = new BarcodeDetector.Builder(getContext())
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(getContext(), barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surface_view.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surface_view.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                // Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    txtOutPassID.post(() -> {
                        if (barcodes.valueAt(0).email != null) {
                            txtOutPassID.removeCallbacks(null);
                            scanContData = barcodes.valueAt(0).email.address;
                            txtOutPassID.setText(scanContData);
                            toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);

                            outPassViewLayout.setVisibility(View.VISIBLE);
                            surface_view.setVisibility(View.GONE);
                            GoBack.setVisibility(View.GONE);
                        } else {

                            scanContData = barcodes.valueAt(0).displayValue;
                            txtOutPassID.setText(scanContData);
                            toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);

                            outPassViewLayout.setVisibility(View.VISIBLE);
                            surface_view.setVisibility(View.GONE);
                            GoBack.setVisibility(View.GONE);

                        }
                    });
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    public void onResume() {
        super.onResume();
        GatePassIDScanner();
    }
}