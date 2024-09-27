package com.example.suti;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.example.suti.Security.SharedPrefServer;

public class SplashActivity extends AppCompatActivity {
    LottieAnimationView lottieErp;
    ImageView img2;
    String serverURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SharedPrefServer.init(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        lottieErp = findViewById(R.id.lottieErp);

        img2 = findViewById(R.id.splashScreenImage);

        Animation lottieAnim = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
        lottieErp.startAnimation(lottieAnim);

        Animation img2Anim = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);
        img2.startAnimation(img2Anim);

        new Handler().postDelayed(() -> {
            serverURL = SharedPrefServer.read("serverURL", "");

            if(serverURL.isEmpty()){
                Intent i = new Intent(this, ServerSetupActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
            }else {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                ActivityOptions options = ActivityOptions.makeCustomAnimation(
                        getApplicationContext(),
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                );
                startActivity(i, options.toBundle());
                finish();
            }
        }, 5000);
    }
}