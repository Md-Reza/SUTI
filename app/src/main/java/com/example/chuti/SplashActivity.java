package com.example.chuti;

import static com.example.chuti.FragmentManager.FragmentManager.intentActivity;
import static com.example.chuti.Handlers.DateFormatterHandlers.CurrentOffsetDateTimeParser;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.chuti.Security.SharedPref;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class SplashActivity extends AppCompatActivity {

    String token, expireDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPref.init(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        token = SharedPref.read("token", "");

        if (token.isEmpty()) {
            new Handler().postDelayed(() -> {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                ActivityOptions options = ActivityOptions.makeCustomAnimation(
                        getApplicationContext(),
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                );
                startActivity(i, options.toBundle());
                finish();

            }, 5000);
        } else {
            try {
                String[] parts = token.split("\\.", 0);

                for (String part : parts) {
                    byte[] decodedBytes = Base64.decode(part, Base64.URL_SAFE);
                    String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);
                    System.out.println("Decoded 1: " + decodedString);
                    try {
                        expireDate = (new JSONObject(decodedString)).getString("exp");
                        System.out.println("Decoded value: " + expireDate);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                String currentOffsetDateTime;
                long currentTimeInMilliseconds;
                LocalDateTime localDate;
                try {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        currentOffsetDateTime = CurrentOffsetDateTimeParser(OffsetDateTime.now().toString());
                        localDate = LocalDateTime.parse(currentOffsetDateTime);
                        currentTimeInMilliseconds = localDate.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli() / 1000;

                        if (Long.parseLong(expireDate) > currentTimeInMilliseconds) {
                            intentActivity(new MainActivity(), this);

                        } else {
                            new Handler().postDelayed(() -> {
                                System.out.println("Decoded value 2: " + currentTimeInMilliseconds);
                                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                ActivityOptions options = ActivityOptions.makeCustomAnimation(
                                        getApplicationContext(),
                                        R.anim.slide_in_left,
                                        R.anim.slide_out_right
                                );
                                startActivity(i, options.toBundle());
                                finish();

                            }, 5000);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}