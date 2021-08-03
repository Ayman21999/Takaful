package com.graduatio.project.takaful.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.graduatio.project.takaful.R;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MyFirebaseMessaging extends FirebaseMessagingService {

    private String currentUID;
    private NotificationManager notificationManager;
    private SharedPreferences sharedPreferences;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            FirebaseFirestore.getInstance().collection("Users").document(
                    FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .update("cloudMessagingToken",s);
        }

    }

    

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("ttt","Firebase Messaging serivice created");

    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d("ttt","message reciceived");
        if(currentUID == null){
            currentUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        if (notificationManager == null) {
            notificationManager = (NotificationManager)
                    getSystemService(Context.NOTIFICATION_SERVICE);
        }

        if (sharedPreferences == null) {
            sharedPreferences = getSharedPreferences("Takafull", Context.MODE_PRIVATE);
        }

        try {

            final CloudMessagingNotificationsSender.Data data = new CloudMessagingNotificationsSender.Data(remoteMessage.getData());

//            if (sharedPreferences.contains("currentMessagingUserId")) {
//
//                if (data.getSenderID()
//                        .equals(sharedPreferences.getString("currentMessagingUserId", "")) &&
//                        data.getDestinationID().equals(sharedPreferences.getString("currentMessagingDeliveryID",""))) {
//
//                    if (sharedPreferences.contains("isPaused") &&
//                            sharedPreferences.getBoolean("isPaused", false)) {
//                        sendNotification(data);
//                    }
//                } else {
//                    sendNotification(data);
//                }
//            } else {
//            }
            sendNotification(data);

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

    }


    public void sendNotification(CloudMessagingNotificationsSender.Data data) throws ExecutionException, InterruptedException {

        String type = String.valueOf(data.getType());
        createChannel(type);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,type)
                .setSmallIcon(R.drawable.ic_hand)
                .setContentTitle(data.getTitle())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentText(data.getBody())
                .setAutoCancel(true);


        builder.setPriority(NotificationCompat.PRIORITY_HIGH);

//        if (data.getImageUrl()!=null) {
//            builder.setLargeIcon(
//                    Glide.with(this)
//                            .asBitmap()
//                            .apply(new RequestOptions().override(100, 100))
//                            .centerCrop()
//                            .load(data.getImageUrl())
//                            .submit()
//                            .get());
//        } else {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_hand);
            builder.setLargeIcon(bitmap);
//        }


        builder.setGroup(type);

//        if (GlobalVariables.getMessagesNotificationMap() == null)
//            GlobalVariables.setMessagesNotificationMap(new HashMap<>());
//
//        final String identifierTitle = data.getSenderID() + type + data.getDestinationID();
//
//        builder.setDeleteIntent(
//                PendingIntent.getBroadcast(this, notificationNum,
//                        new Intent(this, NotificationDeleteListener.class)
//                                .putExtra("notificationIdentifierTitle", identifierTitle)
//                        , PendingIntent.FLAG_UPDATE_CURRENT));
//
        notificationManager.notify(3,builder.build());

    }

    void updateNotificationSent(String user, String destinationId, int type) {

        if (FirebaseAuth.getInstance().getCurrentUser() == null)
            return;

        String currentUd = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Log.d("ttt", "current user id: " + currentUd + " user: " + user + " promoid: " + destinationId + " type: " + type);

        FirebaseFirestore.getInstance().collection("Notifications")
                .whereEqualTo("receiverId", currentUd).whereEqualTo("senderId", user)
                .whereEqualTo("destinationId", destinationId).whereEqualTo("type", type).get()
                .addOnSuccessListener(snapshots -> {
                    if (!snapshots.isEmpty()) {
                        Log.d("ttt", "found this notificaiton and updating it to sent");
                        snapshots.getDocuments().get(0).getReference().update("sent", true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("ttt", "failed because: " + e.getMessage());
            }
        });
    }


    public void createChannel(String channelId) {
        if (Build.VERSION.SDK_INT >= 26) {
            if (notificationManager.getNotificationChannel(channelId) == null) {
                Log.d("ttt", "didn't find: " + channelId);
                Log.d("ttt", "creating notificaiton channel");
                NotificationChannel channel = new NotificationChannel(channelId, channelId + " channel", NotificationManager.IMPORTANCE_HIGH);
                channel.setShowBadge(true);
                channel.setDescription("notifications");
                channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), null);
                channel.enableVibration(true);
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

}
