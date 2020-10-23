package com.example.go4lunch.ViewModel;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.go4lunch.Model.Users.User;
import com.example.go4lunch.Model.Users.UserHelper;
import com.example.go4lunch.R;
import com.example.go4lunch.ViewModel.fragments.listViewFragment;
import com.example.go4lunch.ViewModel.fragments.mapViewFragment;
import com.example.go4lunch.ViewModel.fragments.workmatesFragment;

import com.example.go4lunch.utils.MyAlarm;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Calendar;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment,
                new mapViewFragment()).commit();
        configureDrawerLayout();
        configureNavigationView();
        setUpAlarm();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener
            navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_map:
                     selectedFragment = new mapViewFragment();
                    break;
                case R.id.navigation_list:
                    selectedFragment = new listViewFragment();
                    break;
                case R.id.navigation_workmates:
                    selectedFragment = new workmatesFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment,
                    selectedFragment).commit();
            return true ;
        }
    };

    //  - Configure Drawer Layout
    private void configureDrawerLayout() {
        this.drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // - Configure NavigationView
    private void configureNavigationView() {
        NavigationView navigationView = findViewById(R.id.activity_main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // 4 - Handle Navigation Item Click
        int id = item.getItemId();

        switch (id) {
            case R.id.lunch:
                String  currentUserId;
                currentUserId = UserHelper.getCurrentUserId();
                UserHelper.getUser(currentUserId).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        String restoId = document.get("restoId").toString();
                        if (!(restoId.equals(""))) {
                            Intent intentDetails = new Intent(this, RestaurantDetails.class);
                            intentDetails.putExtra("PlaceDetailResult", restoId);
                            startActivity(intentDetails);
                        }
                    }
                });

            case R.id.settings:
              //  break;
            case R.id.logout:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            default:
                break;
        }
        return true;
    }

    private void setUpAlarm() {
        // The alarm is set to be launch at midi
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // Recover a PendingIntent that will perform a broadcast
        Intent alarmIntent = new Intent(this, MyAlarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        //setting of our alarm
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }


    }
}
