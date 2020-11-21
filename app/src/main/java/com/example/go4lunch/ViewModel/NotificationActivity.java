package com.example.go4lunch.ViewModel;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.go4lunch.R;

public class NotificationActivity extends AppCompatActivity {

    Switch mSwitch;
    TextView tittle;
    public static final String PREFS = "PREFS";
    public static final String NOTIFICATIONS_STATE = "NOTIFICATIONS_STATE";
    public static final String NOTIFICATIONS_ENABLED = "NOTIFICATIONS_STATE";
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TAG", "activity = notificationActivity");
        setContentView(R.layout.activity_notification);
        mSwitch = findViewById(R.id.notification_activity_switch);
        tittle = findViewById(R.id.notification_tittle);

        sharedPreferences = getBaseContext().getSharedPreferences(PREFS, MODE_PRIVATE);

        this.configureSwitchChangeListener();


    }


    private void configureSwitchChangeListener() {
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sharedPreferences.edit().putString(NOTIFICATIONS_STATE,NOTIFICATIONS_ENABLED).apply();
                    Toast.makeText(NotificationActivity.this, "notification enabled", Toast.LENGTH_SHORT).show();
                }else {
                    sharedPreferences.edit().putString(NOTIFICATIONS_STATE,"").apply();
                    Toast.makeText(NotificationActivity .this,"notification disabled",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
