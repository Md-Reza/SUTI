package com.example.suti;

import static com.example.suti.Handlers.SMessageHandler.SAlertDialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.example.suti.Security.SharedPrefServer;

import dmax.dialog.SpotsDialog;

public class ServerSetupActivity extends AppCompatActivity {
    LottieAnimationView lottieLinkServer;
    EditText txtProductionServer;
    Button btnScannerSettingOk;
    String serverLink;
    SpotsDialog spotsDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_server_setup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        spotsDialog = new SpotsDialog(this, R.style.Custom);

        SharedPrefServer.init(getApplicationContext());

        lottieLinkServer = findViewById(R.id.lottieLinkServer);
        txtProductionServer = findViewById(R.id.txtProductionServer);
        btnScannerSettingOk = findViewById(R.id.btnScannerSettingOk);

        serverLink = SharedPrefServer.read("serverURL", "");

        txtProductionServer.setText(serverLink);

        btnScannerSettingOk.setOnClickListener(view -> {

            String prodS = txtProductionServer.getText().toString();

            if (prodS.isEmpty()) {
                SAlertDialog("Error: ", "Invalid Server URl, Please enter correct URL (IP Sample: 192.168.01.01:81) ", R.drawable.error_read_64, this, false);
                return;
            }
            if (prodS.contains("http://")) {
                SAlertDialog("Error: ", "Invalid Server URl, Please enter correct URL (IP Sample: 192.168.01.01:81) ", R.drawable.error_read_64, this, false);
            } else if (!prodS.contains(":")) {
                SAlertDialog("Error: ", "Invalid Server URl, Port are missing (IP Sample: 192.168.01.01:81) ", R.drawable.error_read_64, this, false);
            } else {
                String serverURL = "http://" + prodS + "/";
                SharedPrefServer.write("serverURL", serverURL);

                spotsDialog.show();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                spotsDialog.dismiss();
            }
        });
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }
}