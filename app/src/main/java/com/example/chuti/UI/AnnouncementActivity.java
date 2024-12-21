package com.example.chuti.UI;

import static com.example.chuti.FragmentManager.FragmentManager.intentActivity;
import static com.example.chuti.Handlers.DateFormatterHandlers.CurrentOffsetTimeParser;
import static com.example.chuti.Handlers.SMessageHandler.SAlertError;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.example.chuti.MainActivity;
import com.example.chuti.Model.Announcement;
import com.example.chuti.Model.OutPassViewModel;
import com.example.chuti.Model.RemoteMessageViewModel;
import com.example.chuti.Model.ServiceResponseViewModel;
import com.example.chuti.R;
import com.example.chuti.Security.BaseURL;
import com.example.chuti.Security.Services;
import com.example.chuti.Security.SharedPref;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnnouncementActivity extends AppCompatActivity {

    Services retrofitApiInterface;
    Gson gson;
    String token, companyID;
    SpotsDialog spotsDialog;
    ServiceResponseViewModel serviceResponseViewModel = new ServiceResponseViewModel();
    String userID, appKey;

    public TextView
            txtAnnouncementText,
            txtAnnouncementTitle,
            txtPublishedDate;

    String reqID, reqType;
    Toolbar toolbar;
    ImageView arrow_button;
    LinearLayout itemHiddenView,itemLayout;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_announcement);

        retrofitApiInterface = BaseURL.getRetrofit().create(Services.class);
        SharedPref.init(this);
        gson = new Gson();
        spotsDialog = new SpotsDialog(this, R.style.Custom);
        appKey = SharedPref.read("appKey", "");
        token = SharedPref.read("token", "");
        companyID = SharedPref.read("companyID", "");
        userID = SharedPref.read("uId", "");
        reqType = getIntent().getStringExtra("RequestType");
        reqID = getIntent().getStringExtra("RequestID");

        txtAnnouncementText=findViewById(R.id.txtAnnouncementText);
        txtAnnouncementTitle=findViewById(R.id.txtAnnouncementTitle);
        txtPublishedDate=findViewById(R.id.txtPublishedDate);
        arrow_button=findViewById(R.id.arrow_button);
        itemLayout=findViewById(R.id.itemLayout);
        itemHiddenView=findViewById(R.id.itemHiddenView);

        toolbar = findViewById(R.id.annToolbar);
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setTitle("Announcement");
        toolbar.setSubtitle("");
        toolbar.setNavigationOnClickListener(v -> intentActivity(new MainActivity(), this));

        arrow_button.setOnClickListener(v -> {
            if (itemHiddenView.getVisibility() == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(itemLayout, new AutoTransition());
                itemHiddenView.setVisibility(View.GONE);
                arrow_button.setImageResource(R.drawable.icon_expand_more_24);
            } else {
                TransitionManager.beginDelayedTransition(itemLayout, new AutoTransition());
                itemHiddenView.setVisibility(View.VISIBLE);
                arrow_button.setImageResource(R.drawable.icon_expand_less_24);
            }
        });

        Announcement();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void Announcement() {
        try {
            Call<Announcement> getContToLocCall = retrofitApiInterface.GetAnnouncementAsync("Bearer" + " " + token, appKey, companyID, Integer.parseInt(reqID));
            getContToLocCall.enqueue(new Callback<Announcement>() {
                @Override
                public void onResponse(Call<Announcement> call, Response<Announcement> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {

                                Log.i("info", "anna " + response.body());
                                spotsDialog.dismiss();
                                try {
                                    txtAnnouncementText.setText(response.body().getAnnouncementText());
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    txtAnnouncementTitle.setText(response.body().getAnnouncementTitle());
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    txtPublishedDate.setText(CurrentOffsetTimeParser(response.body().getPublishedDate()));
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
                                    SAlertError(serviceResponseViewModel.getMessage(), AnnouncementActivity.this);
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
                public void onFailure(Call<Announcement> call, Throwable t) {
                    SAlertError(t.getMessage(), AnnouncementActivity.this);
                    spotsDialog.dismiss();
                }
            });
        } catch (Exception e) {
            spotsDialog.dismiss();
            e.printStackTrace();
        }
    }
}