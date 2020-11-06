package com.example.go4lunch.ViewModel.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;



import com.example.go4lunch.Model.Restaurant.Result;
import com.example.go4lunch.Model.Restaurant.Results;
import com.example.go4lunch.Model.Users.UserHelper;
import com.example.go4lunch.R;
import com.example.go4lunch.ViewModel.RestaurantDetails;
import com.example.go4lunch.utils.RestaurantCall;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

import static com.example.go4lunch.utils.GoogleService.API_KEY;

public class mapViewFragment extends Fragment implements OnMapReadyCallback, RestaurantCall.Callbacks{


    private GoogleMap map;
    private SupportMapFragment mapFragment;
    FusedLocationProviderClient fusedLocationProviderClient;
    private String currentLocation;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_view, container, false);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        return view;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        LatLng paris = new LatLng(48.806860, 2.272980);
        map.addMarker(new MarkerOptions().position(paris).title("Paris"));
        map.moveCamera(CameraUpdateFactory.zoomBy(5000));
        map.moveCamera(CameraUpdateFactory.newLatLng(paris));
        executeHttpRequestWithRetrofit();

    }

    private void displayNearbyPlaces(final List<Result> restaurantResult) {
        Log.d("TAG", "Response = displayNearbyPlace" + restaurantResult.size());


        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                for(int i = 0; i < restaurantResult.size(); i++){
                    final Result restaurantItem = restaurantResult.get(i);
                    final String restaurantId = restaurantItem.getPlaceId();
                    if(restaurantId.equals(marker.getTag().toString())) {
                        Intent detailIntent = new Intent(getContext(), RestaurantDetails.class);
                        detailIntent.putExtra("PlaceDetailResult",marker.getTag().toString());
                        detailIntent.putExtra("PlaceDetailAdresse",restaurantItem.getVicinity());
                        detailIntent.putExtra("placeDetailName",restaurantItem.getName());
                        Log.d("TAG", "markerTag" + marker.getTag().toString());
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

        for (int i = 0; i < restaurantResult.size(); i++) {
            final Result restaurantItem = restaurantResult.get(i);
            final String restaurantName = restaurantItem.getName();
            double restaurantLat = restaurantItem.getGeometry().getLocation().getLat();
            double restaurantLng = restaurantItem.getGeometry().getLocation().getLng();
            Log.d("TAG", "Marker = workmateRecycler location" );

            LatLng restaurantPosition = new LatLng(restaurantLat,restaurantLng);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(restaurantPosition);
            markerOptions.title(restaurantName);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker((BitmapDescriptorFactory.HUE_RED)));
            CollectionReference collectionReference = UserHelper.getUsersCollection();
            collectionReference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String uid = document.getData().get("uid").toString();
                        String username = document.getData().get("username").toString();
                        Log.d("TAG", "Marker = workmateRecycler " + username);
                        String urlPicture = document.getData().get("urlPicture").toString();
                        String selectedResto = document.getData().get("restoName").toString();
                        String restoId = document.getData().get("restoId").toString();
                        if (restoId.equals(restaurantItem.getPlaceId())){
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker((BitmapDescriptorFactory.HUE_GREEN)));
                        }
                    }
                    Log.d("TAG", "Marker = workmateRecycler addMarker" );
                    Marker marker = map.addMarker(markerOptions);
                    marker.setTag(restaurantItem.getPlaceId());
                }
            });
        }
    }

    private void executeHttpRequestWithRetrofit() {
        LatLng paris = new LatLng(48.806860, 2.272980);
        String location = paris.latitude+"," + paris.longitude;

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
             getLocation();
       }else{
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
       }



    }

   private void getLocation(){
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
               Location location = task.getResult();
                if(location != null){
                  LatLng currentPosition = new LatLng(location.getLatitude(),location.getLongitude());
                    currentLocation = currentPosition.latitude+","+currentPosition.longitude;
                   Log.d("TAG", "Response = MapResponse" + currentLocation);
                   launchRequest();
                }
           }
        });
   }

   private void launchRequest(){
       RestaurantCall.fetchNearbyRestaurant(this, currentLocation,"restaurant",2000,API_KEY);
   }


    @Override
    public void onResume() {
        super.onResume();
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onResponse(Results restaurantResults) {
        displayNearbyPlaces(restaurantResults.getResults());
    }

    @Override
    public void onFailure() {

    }
}
