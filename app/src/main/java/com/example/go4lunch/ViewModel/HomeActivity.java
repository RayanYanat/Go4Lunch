package com.example.go4lunch.ViewModel;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.Model.Restaurant.Result;
import com.example.go4lunch.Model.Restaurant.Results;
import com.example.go4lunch.Model.Users.UserHelper;
import com.example.go4lunch.R;
import com.example.go4lunch.ViewModel.fragments.listViewFragment;
import com.example.go4lunch.ViewModel.fragments.mapViewFragment;
import com.example.go4lunch.ViewModel.fragments.workmatesFragment;

import com.example.go4lunch.utils.MyAlarm;
import com.example.go4lunch.utils.RestaurantCall;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;


import java.util.Arrays;
import java.util.Calendar;
import java.util.List;



public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, RestaurantCall.Callbacks {


    private Toolbar toolbar;
    private NavigationView mNavigationView;
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;
    private String API_KEY = BuildConfig.google_maps_api_key;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Places.initialize(getApplicationContext(),API_KEY);
        navView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment,
                new mapViewFragment()).commit();
        updateUIWhenCreating();
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

    @Nullable
    private FirebaseUser getCurrentUser(){ return FirebaseAuth.getInstance().getCurrentUser(); }

    //  - Configure Drawer Layout
    private void configureDrawerLayout() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
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
                executeHttpRequestWithRetrofit();
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


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.menu_search) {
            LatLng paris = new LatLng(48.806860, 2.272980);
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,
                    Arrays.asList(Place.Field.ID,Place.Field.NAME))
                    .setTypeFilter(TypeFilter.ESTABLISHMENT)
                    .setLocationBias(RectangularBounds.newInstance(
                            new LatLng(paris.latitude - 0.01, paris.longitude - 0.01),
                            new LatLng(paris.latitude + 0.01, paris.latitude + 0.01)))
                    .build(this);
            startActivityForResult(intent,AUTOCOMPLETE_REQUEST_CODE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateUIWhenCreating(){

        mNavigationView = findViewById(R.id.activity_main_nav_view);

        if (this.getCurrentUser() != null){
            View headerContainer = mNavigationView.getHeaderView(0); // This returns the container layout in nav_drawer_header.xml (e.g., your RelativeLayout or LinearLayout)
            ImageView mImageView = headerContainer.findViewById(R.id.drawer_imageview_profile);
            TextView mNameText = headerContainer.findViewById(R.id.drawer_username);


            //Get picture URL from Firebase
            if (this.getCurrentUser().getPhotoUrl() != null) {
                Glide.with(this)
                        .load(this.getCurrentUser().getPhotoUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(mImageView);
            }

            //Get email from Firebase

            String name = TextUtils.isEmpty(this.getCurrentUser().getDisplayName()) ? "email not found" : this.getCurrentUser().getDisplayName();

            //Update views with data
            mNameText.setText(name);
        }
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

    private void executeHttpRequestWithRetrofit() {
        LatLng paris = new LatLng(48.806860, 2.272980);
        String location = paris.latitude+"," + paris.longitude;
        Log.d("TAG", "Response = responseRequest" + location);
        RestaurantCall.fetchNearbyRestaurant(this, location,"restaurant",2000,API_KEY);
    }

    @Override
    public void onResponse(Results restaurantResults) {
        List<Result> restoList = restaurantResults.getResults();

        String currentUserId = UserHelper.getCurrentUserId();
        UserHelper.getUser(currentUserId).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                String restoId = document.get("restoId").toString();
                for(int i = 0; i < restoList.size(); i++){
                    Result restaurantItem = restoList.get(i);
                    if(restoId.equals(restaurantItem.getPlaceId()) && (!restoId.equals(""))){
                        Intent detailIntent = new Intent(this, RestaurantDetails.class);
                        detailIntent.putExtra("PlaceDetailAdresse",restaurantItem.getVicinity());
                        detailIntent.putExtra("placeDetailName",restaurantItem.getName());
                        detailIntent.putExtra("PlaceDetailResult", restoId);
                        if (!(restaurantItem.getPhotos() == null)) {
                            if (!(restaurantItem.getPhotos().isEmpty())) {
                                detailIntent.putExtra("placeDetailPhoto", restaurantItem.getPhotos().get(0).getPhotoReference());
                            }
                        }
                        startActivity(detailIntent);
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public void onFailure() {

    }

    public void startAutocompleteActivity(View view){
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,
                Arrays.asList(Place.Field.ID,Place.Field.NAME))
                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .setLocationBias(RectangularBounds.newInstance(
                        new LatLng(-33.880490, 151.184363),
                        new LatLng(-33.858754, 151.229596)))
                .setCountries(Arrays.asList("FR","EN"))
                .build(this);
        startActivityForResult(intent,AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i("TAG", "Place: " + place.getName() + ", " + place.getId());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                //
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("TAG", status.getStatusMessage());
            }  // The user canceled the operation.
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
