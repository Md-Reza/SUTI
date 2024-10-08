package com.example.chuti;

import static com.example.chuti.FragmentManager.FragmentManager.replaceFragment;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;

import com.example.chuti.UI.FragmentBalance;
import com.example.chuti.UI.SettingFragment;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {

    LinearLayout id01;

    ChipNavigationBar bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.redOrange));

        WindowCompat.setDecorFitsSystemWindows(getWindow(),true);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Toolbar toolbar = findViewById(R.id.toolbar); //Ignore red line errors
        toolbar.setTitle("CHUTI");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setItemSelected(R.id.bottomNavigationView,true);
        replaceFragment(new FragmentBalance(),this);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(i -> {
            switch (i){
                case R.id.home:
                    replaceFragment(new FragmentBalance(),this);
                    break;
                case R.id.settings:
                    replaceFragment(new SettingFragment(),this);
                    break;
                case R.id.employee:
                    replaceFragment(new FragmentBalance(),this);
                    break;
            }
        });

    }
}