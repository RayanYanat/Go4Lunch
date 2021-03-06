package com.example.go4lunch.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.core.app.NotificationCompat;


import com.example.go4lunch.Model.Users.UserHelper;
import com.example.go4lunch.R;
import com.example.go4lunch.ViewModel.HomeActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static com.example.go4lunch.ViewModel.NotificationActivity.NOTIFICATIONS_ENABLED;
import static com.example.go4lunch.ViewModel.NotificationActivity.NOTIFICATIONS_STATE;
import static com.example.go4lunch.ViewModel.NotificationActivity.PREFS;

public class MyAlarm extends BroadcastReceiver {

    private Context mContext;
    private List<String> usersList;
    private String restoName ;
    private String IdResto ;

    String currentUserId = UserHelper.getCurrentUserId();





    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("alarm", "alarm onReceived" );
        this.mContext=context;
        usersList = new ArrayList<>();
        SharedPreferences preferences = mContext.getSharedPreferences(PREFS, MODE_PRIVATE);
        String notifState = preferences.getString(NOTIFICATIONS_STATE,"");
        Log.e("alarm", "alarm onReceivedNotifState" + notifState );



        UserHelper.getUser(currentUserId).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                 restoName = document.get("restoName").toString();
                 IdResto = document.get("restoId").toString();
                Log.e("alarm", "restiID " + IdResto);
            }
        });


             Log.e("alarm", "notifState " + notifState);
            CollectionReference collectionReference = UserHelper.getUsersCollection();
            collectionReference.get().addOnCompleteListener(task -> {
                if (FirebaseAuth.getInstance().getCurrentUser() != null && IdResto != null && notifState.equals(NOTIFICATIONS_ENABLED)) {
                    Log.e("alarm", "restoID " + restoName);
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String username = Objects.requireNonNull(document.getData().get("username")).toString();
                            String restoId = Objects.requireNonNull(document.getData().get("restoId")).toString();

                            if ((IdResto.equals(restoId) && (!(IdResto.equals(""))))) {
                                usersList.add(username);

                                Log.e("alarm", "triggered");
                                NotificationManager notificationManager = (NotificationManager) mContext
                                        .getSystemService(Context.NOTIFICATION_SERVICE);
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    NotificationChannel channel = new NotificationChannel("CHANNEL_ID",
                                            "NOTIFICATION_CHANNEL",
                                            NotificationManager.IMPORTANCE_DEFAULT);
                                    channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DESCRIPTION");
                                    if (notificationManager != null) {
                                        notificationManager.createNotificationChannel(channel);
                                    }
                                }


                                Intent notificationIntent = new Intent(mContext, HomeActivity.class);
                                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0,
                                        notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
                                        mContext, "CHANNEL_ID")
                                        .setContentTitle("Go4Lunch")
                                        .setSmallIcon(R.drawable.ic_view_list_black_24dp)
                                        .setContentIntent(pendingIntent)
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                        .setStyle(new NotificationCompat.BigTextStyle()
                                                .bigText("vous aves décidé de déjeuner chez " + restoName + "vous y passerez un agréable moment avec " + usersList.toString()));
                                Log.e("alarm", "liste utilisateurs" + usersList.toString());
                                if (notificationManager != null) {
                                    Log.e("alarm", "notificationManager");
                                    notificationManager.notify(1, mNotifyBuilder.build());
                                }

                            }
                        }
                    }
                }
            });

        }
    }

