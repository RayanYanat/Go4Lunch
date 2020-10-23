package com.example.go4lunch.ViewModel;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.go4lunch.Model.RestaurantItem.PlaceDetailsResult;
import com.example.go4lunch.Model.RestaurantItem.Result;
import com.example.go4lunch.Model.Users.User;
import com.example.go4lunch.Model.Users.UserHelper;
import com.example.go4lunch.R;
import com.example.go4lunch.ViewModel.adapter.WorkmateRecyclerAdapter;
import com.example.go4lunch.utils.RestaurantDetailCall;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RestaurantDetails extends AppCompatActivity implements RestaurantDetailCall.Callbacks {


    String API_KEY = "key=AIzaSyCCR-afR0LoWYb1wYm4q8loXKuJIvCl7OM";
    public static final int MAX_WIDTH = 408;
    public static final int MAX_HEIGHT = 250;

    private String restoTel;
    private static final int REQUEST_CALL = 1;

    TextView mRestaurantName;
    TextView mRestaurantAddress;

    WorkmateRecyclerAdapter adapter;

    RecyclerView mUserRecyclerView;
    FloatingActionButton mFloatingActionButton;
    List<String> listRestoLike = new ArrayList<>();
    private String userId;
    ImageView mMainPhoto;
    ImageView mImageCall;
    ImageView mImageLike;
    ImageView mImageWebsite;
    ImageView mStar;
    ImageView mStar2;
    ImageView mStar3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);
        mRestaurantName = findViewById(R.id.name_detail);
        mRestaurantAddress = findViewById(R.id.address_detail);
        mUserRecyclerView = findViewById(R.id.fragment_workmates_detail_resto_recyclerview);
        mImageCall = findViewById(R.id.phone_detail_button);
        mMainPhoto = findViewById(R.id.photo_detail);
        mImageLike = findViewById(R.id.like_detail_button);
        mImageWebsite = findViewById(R.id.website_detail_button);
        mStar = findViewById(R.id.star1_detail);
        mStar2 = findViewById(R.id.star2_detail);
        mStar3 = findViewById(R.id.star3_detail);
        mFloatingActionButton = findViewById(R.id.restoToday_FloatingButton);
        userId = UserHelper.getCurrentUserId();

        mRestaurantName.setText(getIntent().getStringExtra("placeDetailName"));

        mRestaurantAddress.setText(getIntent().getStringExtra("PlaceDetailAdresse"));

        String photoUrl = getIntent().getStringExtra("placeDetailPhoto");
        String urlImage = "https://maps.googleapis.com/maps/api/place/photo" + "?maxwidth=" + MAX_WIDTH + "&maxheight=" + MAX_HEIGHT + "&photoreference=" + photoUrl + "&" + API_KEY;
        Glide.with(this).load(urlImage).into(mMainPhoto);

        executeHttpRequestWithRetrofit();
        setUpRecyclerView();


        mImageLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String placeidResto = getIntent().getStringExtra("PlaceDetailResult");
                updateLikeInFirebase(placeidResto);
            }
        });


        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //update Firestore
                String placeidResto = getIntent().getStringExtra("PlaceDetailResult");
                String restoName = getIntent().getStringExtra("placeDetailName");
                updateRestoNameInFirebase(placeidResto, restoName);
            }
        });
    }

    private void executeHttpRequestWithRetrofit() {
        String result = getIntent().getStringExtra("PlaceDetailResult");
        Log.d("TAG", "Response = responseDetailRequest");
        RestaurantDetailCall.fetchPlaceDetails(this, result, API_KEY);
    }

    @Override
    public void onResponse(final PlaceDetailsResult restaurantResults) {

        final String detailUrl = restaurantResults.getResult().getWebsite();
        mImageWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detailUrl.equals("")) {
                    Toast.makeText(RestaurantDetails.this, "le site web n'est pas disponible", Toast.LENGTH_LONG).show();
                } else {
                    Intent webViewIntent = new Intent(RestaurantDetails.this, WebViewActivity.class);
                    webViewIntent.putExtra("DetailRestauUrl", detailUrl);
                    startActivity(webViewIntent);
                }

            }
        });

        // display rating
            if(restaurantResults.getResult().getRating() != null){
                double rating = restaurantResults.getResult().getRating();
                if (rating > 1) {
                   mStar.setVisibility(View.VISIBLE);
                    if (rating > 2.5) {
                        mStar2.setVisibility(View.VISIBLE);
                        if (rating > 4) {
                            mStar3.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }



        restoTel = restaurantResults.getResult().getFormattedPhoneNumber();
        mImageCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });
    }

    private void setUpRecyclerView() {
        @Nullable
        List<User> results = new ArrayList<>();

        String result = getIntent().getStringExtra("PlaceDetailResult");
        Log.d("TAG", "Response = workmateRecycler ");
        CollectionReference collectionReference = UserHelper.getUsersCollection();
        collectionReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String uid = document.getData().get("uid").toString();
                    String username = document.getData().get("username").toString();
                    Log.d("TAG", "Response = workmateRecycler " + username);
                    String urlPicture = document.getData().get("urlPicture").toString();
                    String restoId = document.getData().get("restoId").toString();
                    Log.d("TAG", " workmateRecycler restoId = " + restoId);
                    User userToAdd = new User(uid,username,urlPicture);
                   if(restoId.equals(result)){
                        Log.d("TAG", " workmateRecycler if statement = " + restoId);
                        results.add(userToAdd);
                   }
                    adapter = new WorkmateRecyclerAdapter (results);
                    mUserRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                    mUserRecyclerView.setAdapter(adapter);
               //     Log.d("TAG", "Response = workmateRecycler " + results.get(0).toString());

                }
            }
        });


    }


    @Override
    public void onFailure() {

    }

    private void makePhoneCall() {
        if (restoTel != "") {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);

            } else {
                String dial = "tel:" + restoTel;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }

        } else {
            Toast.makeText(this, "numéro de téléphone indisponible", Toast.LENGTH_LONG).show();
        }
    }

    private void updateLikeInFirebase(final String idResto) {

        UserHelper.getUser(userId).addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Log.d("TAG", "onSuccess: documentSnapshot exists");
                listRestoLike = Objects.requireNonNull(documentSnapshot.toObject(User.class)).getRestoLike();
                if (listRestoLike != null) {
                    if (listRestoLike.contains(idResto)) {
                        listRestoLike.remove(idResto);
                    } else {
                        listRestoLike.add(idResto);
                    }
                }
                UserHelper.updateLikedResto(listRestoLike, userId);
            }
        });

    }

    private void updateRestoNameInFirebase ( final String restoID, final String restoName){

        UserHelper.getUser(userId).addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Log.d("TAG", "onSuccess: documentSnapshot exists 2 ");
                User restoOfTheDay = documentSnapshot.toObject(User.class);
                if(restoOfTheDay.getRestoId().equals(restoID) ) {
                    Log.d("TAG", "if updateResto ");
                    UserHelper.updateRestoName( "",userId);
                    UserHelper.updateRestoId("",userId);
                }else {
                    Log.d("TAG", "else updateResto " + restoOfTheDay.getRestoId() + restoID);
                    UserHelper.updateRestoName(restoName, userId);
                    UserHelper.updateRestoId(restoID, userId);
                }
            }
        });
    }
}


