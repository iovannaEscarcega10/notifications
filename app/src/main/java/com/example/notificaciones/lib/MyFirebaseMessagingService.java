package com.example.notificaciones.lib;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.notificaciones.MainActivity;
import com.example.notificaciones.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "My firebase message";
    public MyFirebaseMessagingService() {
    }
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "onNewToken: " + token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "onMessageReceived: ");
        Map<String, String> data = remoteMessage.getData();
        if (data.size() > 0){
            String title = data.get("title");
            String msg = data.get("message");
            sendNotification(title, msg);
        }else{
            RemoteMessage.Notification notification = remoteMessage.getNotification();
            String title = notification.getTitle();
            String msg = notification.getBody();
            sendNotification(title, msg);
        }
    }

    private void sendNotification(String title, String msg) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, MyNotification.NOTIFICATION_ID, intent, PendingIntent.FLAG_ONE_SHOT);
        MyNotification notification = new MyNotification(this, MyNotification.CHANNEL_ID_NOTIFICATIONS);
        notification.build(R.drawable.ic_launcher_foreground, title, msg, pendingIntent);
        notification.addChannel("Notificaciones", NotificationManager.IMPORTANCE_DEFAULT);
        notification.createChannelGroup(MyNotification.CHANNEL_GROUP_GENERAL, R.string.fcm_fallback_notification_channel_label);
        notification.show(MyNotification.NOTIFICATION_ID);
        startActivity(intent);
    }
}