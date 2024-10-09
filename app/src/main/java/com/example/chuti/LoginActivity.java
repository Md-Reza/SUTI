package com.example.chuti;

import static com.example.chuti.FragmentManager.FragmentManager.intentActivity;
import static com.example.chuti.Handlers.SMessageHandler.SAlertDialog;
import static com.example.chuti.Handlers.SMessageHandler.SAlertError;
import static com.example.chuti.Handlers.SMessageHandler.SConnectionFail;
import static com.example.chuti.Security.SDeviceInfo.getDeviceName;
import static com.example.chuti.Security.SDeviceInfo.getIPAddress;
import static com.example.chuti.Security.SDeviceInfo.getMacAddress;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chuti.Dto.LoginDto;
import com.example.chuti.Model.LoginSuccessViewModel;
import com.example.chuti.Model.ServiceResponseViewModel;
import com.example.chuti.Security.BaseURL;
import com.example.chuti.Security.Services;
import com.example.chuti.Security.SharedPref;
import com.example.chuti.Security.SharedPrefServer;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.Executor;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    Services retrofitApiInterface;
    Gson gson;
    ToneGenerator toneGen1;
    TextInputEditText userIdName, userPassword;
    String token, userName;
    Integer userNameId;
    SpotsDialog spotsDialog;
    Call<LoginSuccessViewModel> loginResponseCall;
    ServiceResponseViewModel clientResponseModel;
    LoginSuccessViewModel loginSuccessViewModel;
    Button btnLogin;
    TextView txtRegister;
    ImageView ivFingerPrint;
    CheckBox chkRemember;
    SharedPreferences preferences;
    static String appKey;

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        retrofitApiInterface = BaseURL.getRetrofit().create(Services.class);
        SharedPref.init(this);
        gson = new Gson();
        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        SharedPrefServer.init(this);
        userIdName = findViewById(R.id.userIdName);
        userPassword = findViewById(R.id.userPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtRegister = findViewById(R.id.txtRegister);
        ivFingerPrint = findViewById(R.id.ivFingerPrint);
        chkRemember = findViewById(R.id.chkRemember);

        spotsDialog = new SpotsDialog(this, R.style.Custom);

        appKey = "CF637E5D60454148826F8DDDDB628380";
        SharedPref.write("appKey", appKey);

        token = SharedPref.read("token", "");
        appKey = SharedPref.read("appKey", "");


        ivFingerPrint.setOnClickListener(view -> {
            BIOMetric();
        });

        btnLogin.setOnClickListener(v -> UserLogin());
        //txtRegister.setOnClickListener(v -> replaceFragment(new UserRegisterFragment(),getContext()));

        preferences = getSharedPreferences("chkRemember", Context.MODE_PRIVATE);

        String checkBox = preferences.getString("chkRemember", "");
        if (checkBox.equals("true")) {
            String userName = SharedPref.read("uId", "");
            String pass = SharedPref.read("uPass", "");

            userIdName.setText(userName);
            userPassword.setText(pass);
        }


        chkRemember.setOnCheckedChangeListener((compoundButton, b) -> {
            if (compoundButton.isChecked()) {
                preferences = getSharedPreferences("chkRemember", Context.MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = preferences.edit();
                prefsEditor.putString("chkRemember", "true");
                prefsEditor.apply();
            } else if (!compoundButton.isChecked()) {
                preferences = getSharedPreferences("chkRemember", Context.MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = preferences.edit();
                prefsEditor.putString("chkRemember", "false");
                prefsEditor.apply();
            }
        });

        userPassword.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                UserLogin();
            }
            if (keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
                UserLogin();
            }
            if (keyEvent.getAction() == KeyEvent.KEYCODE_DPAD_CENTER) {
                UserLogin();
            }
            return false;
        });
        btnLogin = findViewById(R.id.btnLogin);

    }

    private void UserLogin() {
        String uId = userIdName.getText().toString();
        SharedPref.write("uId", uId);
        String uPass = userPassword.getText().toString();
        SharedPref.write("uPass", uPass);
        String userId = userIdName.getText().toString();
        String userPass = userPassword.getText().toString();

        if (userId.isEmpty()) {
            userIdName.requestFocus();
            userIdName.setError("Enter valid user id");
        } else if (userPass.isEmpty()) {
            userPassword.requestFocus();
            userPassword.setError("Enter valid password");
        } else {
            LoginDto loginDto = new LoginDto();
            loginDto.setLoginID(userId);
            loginDto.setPassword(userPass);

            spotsDialog.show();
            loginResponseCall = retrofitApiInterface.LoginAsync(appKey, loginDto);
            loginResponseCall.enqueue(new Callback<LoginSuccessViewModel>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(@NonNull Call<LoginSuccessViewModel> call, Response<LoginSuccessViewModel> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                spotsDialog.dismiss();
                                loginSuccessViewModel = response.body();
                                Log.i("info", "Login" + response.body());
                                token = loginSuccessViewModel.getAccessToken();
                                SharedPref.write("token", token);

                                intentActivity(new MainActivity(), LoginActivity.this);
                            }
                        } else if (!response.isSuccessful()) {
                            if (response.errorBody() != null) {
                                spotsDialog.dismiss();
                                if (response.code() == 400) {
                                    gson = new GsonBuilder().create();
                                    clientResponseModel = new ServiceResponseViewModel();
                                    try {
                                        clientResponseModel = gson.fromJson(response.errorBody().string(), ServiceResponseViewModel.class);
                                        SAlertError(clientResponseModel.getMessage(), LoginActivity.this);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    } catch (NullPointerException e) {
                        spotsDialog.dismiss();
                        loginSuccessViewModel = response.body();
                        Log.i("info", "Login" + response.body());
                        SAlertDialog("Error: ", e.getMessage(), R.drawable.error_read_64, LoginActivity.this, false);
                    }
                }

                @Override
                public void onFailure(Call<LoginSuccessViewModel> call, Throwable t) {
                    spotsDialog.dismiss();
                    SConnectionFail(t.getMessage(), LoginActivity.this);
                    userIdName.requestFocus();
                }
            });
        }
    }

    private void BIOMetric() {
        Executor executor = ContextCompat.getMainExecutor(this);
        final BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor,
                new androidx.biometric.BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                    }

                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        LoginDto loginDto = new LoginDto();
                        String uId = SharedPref.read("uId", "");
                        loginDto.setLoginID(uId);
                        String uPass = SharedPref.read("uPass", "");
                        loginDto.setPassword(uPass);

                        spotsDialog.show();
                        loginResponseCall = retrofitApiInterface.LoginAsync(appKey, loginDto);
                        loginResponseCall.enqueue(new Callback<LoginSuccessViewModel>() {
                            @Override
                            public void onResponse(@NonNull Call<LoginSuccessViewModel> call, Response<LoginSuccessViewModel> response) {
                                try {
                                    if (response.isSuccessful()) {
                                        if (response.body() != null) {
                                            spotsDialog.dismiss();
                                            loginSuccessViewModel = new LoginSuccessViewModel();
                                            token = loginSuccessViewModel.getAccessToken();
                                            SharedPref.write("token", token);

                                            intentActivity(new MainActivity(), LoginActivity.this);
                                        }
                                    } else if (!response.isSuccessful()) {
                                        if (response.errorBody() != null) {
                                            spotsDialog.dismiss();
                                            if (response.code() == 400) {
                                                gson = new GsonBuilder().create();
                                                clientResponseModel = new ServiceResponseViewModel();
                                                try {
                                                    clientResponseModel = gson.fromJson(response.errorBody().string(), ServiceResponseViewModel.class);
                                                    SAlertError(clientResponseModel.getMessage(), LoginActivity.this);
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    }
                                } catch (NullPointerException e) {
                                    spotsDialog.dismiss();
                                    SAlertDialog("Error: ", clientResponseModel.getMessage()
                                            + response.errorBody(), R.drawable.error_read_64, LoginActivity.this, false);
                                }

                            }

                            @Override
                            public void onFailure(Call<LoginSuccessViewModel> call, Throwable t) {
                                spotsDialog.dismiss();
                                SConnectionFail(t.getMessage(), LoginActivity.this);
                                userIdName.requestFocus();
                            }
                        });
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                    }
                });
        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Smart Procurement")
                .setDescription("Use your fingerprint to login. ")
                .setNegativeButtonText("Cancel").build();

        BiometricManager biometricManager = BiometricManager.from(getApplicationContext());
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                biometricPrompt.authenticate(promptInfo);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(getApplicationContext(), "This device does not have a fingerprint sensor", Toast.LENGTH_LONG).show();
                UserLogin();
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(getApplicationContext(), "The biometric sensor is currently unavailable.", Toast.LENGTH_LONG).show();
                UserLogin();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(getApplicationContext(), "Your device doesn't have fingerprint saved,please check your security settings.", Toast.LENGTH_LONG).show();
                UserLogin();
                break;
        }
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