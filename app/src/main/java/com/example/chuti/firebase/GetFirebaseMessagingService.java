package com.example.chuti.firebase;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.chuti.MainActivity;
import com.example.chuti.Model.RemoteMessageViewModel;
import com.example.chuti.R;

import java.util.Map;


import com.example.chuti.UI.GatepassNotificationMessageActivity;
import com.example.chuti.UI.LeaveNotificationMessageActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class GetFirebaseMessagingService extends FirebaseMessagingService {
    RemoteMessageViewModel remoteMessageViewModel;
    Intent intent;

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
        intent = new Intent();
        switch (remoteMessageViewModel.getRequestType()) {
            case "LEAVE":
                intent = new Intent(this, LeaveNotificationMessageActivity.class);
                intent = new Intent(this, MainActivity.class);
                break;
            case "OUTPASS":
                intent = new Intent(this, GatepassNotificationMessageActivity.class);
                intent = new Intent(this, MainActivity.class);
                break;
            default:
                break;
        }
        System.out.println("remoteMessageViewModel :1 " + remoteMessageViewModel);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("remoteMessageViewModel", remoteMessageViewModel);

        // Create a PendingIntent for the notification
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_ONE_SHOT
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
            channel.enableLights(true);
            channel.enableVibration(true);
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
        // Show notification
        showNotification(title, body, data);
    }

    private void showNotification(String title, String body, Map<String, String> data) {

        intent = new Intent();
        PendingIntent pendingIntent;

        remoteMessageViewModel = new RemoteMessageViewModel(
                data.get("Status"),
                data.get("RequestID"),
                data.get("RequestType")
        );
        switch (remoteMessageViewModel.getRequestType()) {
            case "LEAVE":
                intent = new Intent(this, LeaveNotificationMessageActivity.class);
                intent = new Intent(this, MainActivity.class);
                intent.putExtra("remoteMessageViewModel", remoteMessageViewModel);
                break;
            case "OUTPASS":
                intent = new Intent(this, GatepassNotificationMessageActivity.class);
                intent = new Intent(this, MainActivity.class);
                intent.putExtra("remoteMessageViewModel", remoteMessageViewModel);
                break;
            default:
                break;
        }
        System.out.println("remoteMessageViewModel :2 " + remoteMessageViewModel);
        intent.putExtra("remoteMessageViewModel", remoteMessageViewModel);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Create a PendingIntent for the notification
        pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );     // Create a PendingIntent for the notification

        // Notification channel setup for Android O and above
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "default_channel";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Default Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.enableLights(true);
            channel.enableVibration(true);
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

