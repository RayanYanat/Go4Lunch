package com.example.go4lunch.ViewModel;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;

import com.example.go4lunch.R;
import com.example.go4lunch.ViewModel.fragments.listViewFragment;
import com.example.go4lunch.ViewModel.fragments.mapViewFragment;
import com.example.go4lunch.ViewModel.fragments.workmatesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(navListener);

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
}
