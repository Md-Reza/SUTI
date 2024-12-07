package com.example.chuti;

import static com.example.chuti.FragmentManager.FragmentManager.replaceFragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;

import com.example.chuti.UI.EmployeeGatepassFragment;
import com.example.chuti.UI.FragmentBalance;
import com.example.chuti.UI.RequestLeaveFragment;
import com.example.chuti.UI.SettingFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {

    ChipNavigationBar bottomNavigationView;
    FloatingActionButton btnLeave, btnGatePass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.redOrange));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Toolbar toolbar = findViewById(R.id.toolbar); //Ignore red line errors
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);


        btnLeave = findViewById(R.id.btnLeave);
        btnLeave.setOnClickListener(v -> replaceFragment(new RequestLeaveFragment(), this));

        btnGatePass = findViewById(R.id.btnGatePass);
        btnGatePass.setOnClickListener(v -> replaceFragment(new EmployeeGatepassFragment(), this));

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setItemSelected(R.id.bottomNavigationView, true);
        replaceFragment(new FragmentBalance(), this);
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
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
            // Bitmap bitmap=(Bitmap) data.getExtras().get("data");
            if (requestCode == 200) {
                Toast.makeText(getApplicationContext(), "Take Photo", Toast.LENGTH_LONG).show();
            }
            Toast.makeText(getApplicationContext(), "Take Photo", Toast.LENGTH_LONG).show();
        }
    }
}