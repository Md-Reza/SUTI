package com.example.chuti.firebase;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.chuti.Model.RemoteMessageViewModel;
import com.example.chuti.R;
import com.example.chuti.UI.PushNotificationMessageActivity;

import java.util.Map;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class GetFirebaseMessagingService extends FirebaseMessagingService {
    RemoteMessageViewModel remoteMessageViewModel;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Extract the notification title and body
        String title = remoteMessage.getNotification() != null ? remoteMessage.getNotification().getTitle() : "Notification";
        String body = remoteMessage.getNotification() != null ? remoteMessage.getNotification().getBody() : "";

        // Extract data payload if available
        Map<String, String> data = remoteMessage.getData();

        remoteMessageViewModel = new RemoteMessageViewModel(
                data.get("Status"),
                data.get("RequestID"),
                data.get("RequestType")
        );

        Log.i("info", "remoteMessage " + remoteMessage.getData());

        // Show notification
        showNotification(title, body, data);
    }

    private void showNotification(String title, String body, Map<String, String> data) {
        // Create an intent to open PushNotificationMessageActivity
        Intent intent = new Intent(this, PushNotificationMessageActivity.class);
        Log.i("info", "remoteMessage 4" + body);
        Log.i("info", "remoteMessage 5" + data);
        remoteMessageViewModel = new RemoteMessageViewModel(
                data.get("Status"),
                data.get("RequestID"),
                data.get("RequestType")
        );
        intent.putExtra("remoteMessageViewModel",remoteMessageViewModel);


        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Create a PendingIntent for the notification
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Notification channel setup for Android O and above
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "default_channel";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Default Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            manager.createNotificationChannel(channel);
        }

        // Build and display the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.chuti_logo) // Replace with your icon
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        manager.notify(1, builder.build());
    }
}

