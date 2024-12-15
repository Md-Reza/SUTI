package com.example.chuti;

import static com.example.chuti.FragmentManager.FragmentManager.replaceFragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;

import com.example.chuti.Model.RemoteMessageViewModel;
import com.example.chuti.Security.SharedPref;
import com.example.chuti.UI.AnnouncementActivity;
import com.example.chuti.UI.EmployeeGatepassFragment;
import com.example.chuti.UI.FragmentAnnouncement;
import com.example.chuti.UI.FragmentBalance;
import com.example.chuti.UI.FragmentOutpassApproval;
import com.example.chuti.UI.GatepassNotificationMessageActivity;
import com.example.chuti.UI.LeaveNotificationMessageActivity;
import com.example.chuti.UI.RequestLeaveFragment;
import com.example.chuti.UI.SettingFragment;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    ChipNavigationBar bottomNavigationView;
    FloatingActionButton btnLeave, btnGatePass;
    String token, accountType;
    Dialog dialogClose;
    View logoutView;
    Intent intent = getIntent();
    String reqID, reqType = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.redOrange));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        intent = getIntent();
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        SharedPref.init(this);

        token = SharedPref.read("token", "");
        reqType = getIntent().getStringExtra("RequestType");
        reqID = getIntent().getStringExtra("RequestID");
        btnGatePass = findViewById(R.id.btnGatePass);
        btnLeave = findViewById(R.id.btnLeave);

        try {
            String[] parts = token.split("\\.", 0);

            for (String part : parts) {
                byte[] decodedBytes = Base64.decode(part, Base64.URL_SAFE);
                String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);
                System.out.println("Decoded 1: " + decodedString);
                try {
                    accountType = (new JSONObject(decodedString)).getString("http://schemas.microsoft.com/ws/2008/06/identity/claims/role");
                    SharedPref.write("accountType", accountType);

                    System.out.println("Decoded value: " + accountType);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Toolbar toolbar = findViewById(R.id.toolbar); //Ignore red line errors
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);


        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        if (accountType.equals("GATE")) {
            bottomNavigationView.setVisibility(View.GONE);
            btnLeave.setVisibility(View.GONE);
            btnGatePass.setVisibility(View.GONE);
            replaceFragment(new FragmentOutpassApproval(), this);
        } else {

            bottomNavigationView.setItemSelected(R.id.bottomNavigationView, true);
            // replaceFragment(new FragmentBalance(), this);
            try {
                if (!reqType.isEmpty()) {
                    if (reqType.equals("LEAVE")) {
                        intent = new Intent(this, LeaveNotificationMessageActivity.class);
                        intent.putExtra("RequestID", reqID);
                        intent.putExtra("RequestType", reqType);
                        startActivity(intent);
                    } else if (reqType.equals("OUTPASS")) {
                        intent = new Intent(this, LeaveNotificationMessageActivity.class);
                        intent.putExtra("RequestID", reqID);
                        intent.putExtra("RequestType", reqType);
                        startActivity(intent);
                    }
                    else if (reqType.equals("ANNOUNCEMENT")) {
                        intent = new Intent(this, AnnouncementActivity.class);
                        intent.putExtra("RequestID", reqID);
                        intent.putExtra("RequestType", reqType);
                        startActivity(intent);
                    }else {
                        replaceFragment(new FragmentBalance(), this);
                    }
                } else {
                    replaceFragment(new FragmentBalance(), this);
                }
            } catch (NullPointerException e) {
                replaceFragment(new FragmentBalance(), this);
            }
            bottomNavigationView.setFocusable(false);
            bottomNavigationView.setBackground(null);
            bottomNavigationView.setOnItemSelectedListener(i -> {
                switch (i) {
                    case R.id.home:
                        replaceFragment(new FragmentMain(), this);
                        break;
                    case R.id.settings:
                        replaceFragment(new SettingFragment(), this);
                        break;
                    case R.id.employee:
                        replaceFragment(new FragmentBalance(), this);
                        break;
                    case R.id.announcement:
                        replaceFragment(new FragmentAnnouncement(), this);
                        break;
                }
            });
        }
        btnLeave.setOnClickListener(v -> replaceFragment(new RequestLeaveFragment(), this));
        btnGatePass.setOnClickListener(v -> replaceFragment(new EmployeeGatepassFragment(), this));
        bottomNavigationView.setItemSelected(R.id.bottomNavigationView, false);
        dialogClose = new Dialog(this);
        logoutView = getLayoutInflater().inflate(R.layout.logout_message, null);
        dialogClose.setCancelable(false);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                dialogClose.setContentView(logoutView);
                TextView txtMessage = logoutView.findViewById(R.id.txtMessage);
                txtMessage.setText(R.string.are_you_sure_you_want_to_exit);
                Button btnOk = logoutView.findViewById(R.id.btnOk);
                btnOk.setOnClickListener(v1 -> {
                    dialogClose.dismiss();
                    finish();
                    SharedPref.remove("Token", "");
                });
                Button btnClose = logoutView.findViewById(R.id.btnClose);
                btnClose.setOnClickListener(v1 -> {
                    dialogClose.dismiss();
                });
                dialogClose.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialogClose.show();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 200) {
                Toast.makeText(getApplicationContext(), "Take Photo", Toast.LENGTH_LONG).show();
            }
            Toast.makeText(getApplicationContext(), "Take Photo", Toast.LENGTH_LONG).show();
        }
    }
}