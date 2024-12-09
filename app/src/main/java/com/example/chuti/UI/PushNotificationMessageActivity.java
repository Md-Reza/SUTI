package com.example.chuti.UI;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chuti.Model.RemoteMessageViewModel;
import com.example.chuti.R;

public class PushNotificationMessageActivity extends AppCompatActivity {
    TextView txtMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_notification_message);
        txtMessage=findViewById(R.id.txtMessage);
        RemoteMessageViewModel requestData = getIntent().getParcelableExtra("remoteMessageViewModel");
        // Get the data from the intent

        txtMessage.setText(requestData.getRequestId()+", "+requestData.getRequestType()+", "+requestData.getStatus());
    }
}