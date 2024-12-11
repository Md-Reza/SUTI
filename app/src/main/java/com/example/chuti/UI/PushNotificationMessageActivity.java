package com.example.chuti.UI;



import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chuti.MainActivity;
import com.example.chuti.Model.RemoteMessageViewModel;
import com.example.chuti.R;

public class PushNotificationMessageActivity extends AppCompatActivity {
    TextView txtMessage;
    Intent intent;
    RemoteMessageViewModel requestData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_notification_message);
        txtMessage = findViewById(R.id.txtMessage);
        intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            requestData = getIntent().getParcelableExtra("remoteMessageViewModel");// Retrieve the data passed from the notification
            if (requestData.getRequestType() != null) {
                System.out.println("push requestData: " + requestData);
                Toast.makeText(PushNotificationMessageActivity.this, "Push", Toast.LENGTH_LONG).show();
                txtMessage.setText("Rezwan" + requestData.getRequestId() + ", " + requestData.getRequestType() + ", " + requestData.getStatus());
            }
        }
        // Get the data from the intent
    }
}