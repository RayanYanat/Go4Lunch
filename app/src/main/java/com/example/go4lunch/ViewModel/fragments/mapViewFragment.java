package com.example.go4lunch.ViewModel.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.go4lunch.Model.Restaurant.Result;
import com.example.go4lunch.Model.Restaurant.Results;
import com.example.go4lunch.R;
import com.example.go4lunch.ViewModel.RestaurantDetails;
import com.example.go4lunch.utils.RestaurantCall;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import static com.example.go4lunch.utils.GoogleService.API_KEY;

public class mapViewFragment extends Fragment implements OnMapReadyCallback, RestaurantCall.Callbacks{

    private Marker marker;
    private GoogleMap map;
    private SupportMapFragment mapFragment;

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
        map.moveCamera(CameraUpdateFactory.zoomBy(13));
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


            LatLng restaurantPosition = new LatLng(restaurantLat,restaurantLng);
            marker = map.addMarker(new MarkerOptions().position(restaurantPosition).title(restaurantName));
            marker.setTag(restaurantItem.getPlaceId());

        }
    }

    private void executeHttpRequestWithRetrofit() {
        LatLng paris = new LatLng(48.806860, 2.272980);
        String location = paris.latitude+"," + paris.longitude;
        Log.d("TAG", "Response = MapResponse" + location);
        RestaurantCall.fetchNearbyRestaurant(this, location,"restaurant",2000,API_KEY);
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
