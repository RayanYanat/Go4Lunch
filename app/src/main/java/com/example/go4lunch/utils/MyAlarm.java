package com.example.go4lunch.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;


import com.example.go4lunch.Model.Users.User;
import com.example.go4lunch.Model.Users.UserHelper;
import com.example.go4lunch.R;
import com.example.go4lunch.ViewModel.HomeActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyAlarm extends BroadcastReceiver {

    private Context mContext;
    private List<String> usersList;
    private String currentUserId;
    private User currentUser;





    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("alarm", "alarm onReceived" );
        this.mContext=context;
        usersList = new ArrayList<>();
        currentUserId = UserHelper.getCurrentUserId();
        UserHelper.getUser(currentUserId).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                String restoName = document.get("restoName").toString();
                String restoId = document.get("restoId").toString();
                String urlPicture = document.getData().get("urlPicture").toString();
                 currentUser = new User(restoName,restoId,urlPicture);
            }
        });

        if (FirebaseAuth.getInstance().getCurrentUser() != null && currentUser.getRestoId() != null){
            CollectionReference collectionReference = UserHelper.getUsersCollection();
            collectionReference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String uid = document.getData().get("uid").toString();
                        String username = document.getData().get("username").toString();
                        String restoId = document.getData().get("restoId").toString();

                        if (currentUser.getRestoId().equals(restoId)){
                            usersList.add(username);

                            Log.e("alarm", "triggered" );
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
                                    mContext,"CHANNEL_ID")
                                    .setContentTitle("Go4Lunch")
                                    .setContentText("vous aves décidé de déjeuner chez " + currentUser.getRestoName() + "vous y passerez un agréable moment avec " + usersList.toString() )
                                    .setSmallIcon(R.drawable.ic_view_list_black_24dp)
                                    .setContentIntent(pendingIntent)
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                                    Log.e("alarm", "liste utilisateurs" + usersList.toString() );
                            if (notificationManager != null) {
                                notificationManager.notify(1, mNotifyBuilder.build());
                            }

                        }
                    }
                }
            });
        }
    }
}
