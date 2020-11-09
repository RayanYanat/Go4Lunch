package com.example.go4lunch.ViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.go4lunch.R;
import com.example.go4lunch.utils.MyAlarm;

public class NotificationActivity extends AppCompatActivity {

    Switch mSwitch;
    TextView tittle;
    public static final String NOTIFICATIONS_STATE = "NOTIFICATIONS";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TAG", "activity = notificationActivity" );
        setContentView(R.layout.activity_notification);
        mSwitch = findViewById(R.id.notification_activity_switch);
        tittle = findViewById(R.id.notification_tittle);
    }

    @Override
    protected void onPause(){
        super.onPause();
        if (mSwitch.isChecked()){
            Log.e("Notifications", "onPause: True" );
            Toast.makeText(NotificationActivity.this, "Notifications preferences saved", Toast.LENGTH_SHORT).show();
        }else{
            Log.e("Notifications", "onPause: False" );
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSwitch.isChecked()){
            Intent intent = new Intent(NotificationActivity.this, MyAlarm.class);
            intent.putExtra("notificationChecked", NOTIFICATIONS_STATE );
        }
    }
}
