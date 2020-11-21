package com.example.go4lunch.ViewModel.adapter;



import android.annotation.SuppressLint;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.Model.Restaurant.Result;
import com.example.go4lunch.Model.Users.User;
import com.example.go4lunch.Model.Users.UserHelper;
import com.example.go4lunch.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class RestaurantRecyclerAdapter extends RecyclerView.Adapter<RestaurantRecyclerAdapter.ImageViewHolder> {

    private List<Result> mData;
    private static final int MAX_WIDTH = 75;
    private static final int MAX_HEIGHT = 75;
    private float[] distanceResults = new float[1];
    private Context context;
    private String currentLocation;
    private LatLng currentPosition;

    private List<User> usersList;

    private String API_KEY = BuildConfig.google_maps_api_key;

    public RestaurantRecyclerAdapter(List<Result> data) {
        mData = data;
    }


    @NonNull
    @Override
    public RestaurantRecyclerAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_item_recyclerview, parent, false);
        context=view.getContext();
        return new ImageViewHolder(view);
    }

    public void setResults(List<Result> restauResults) {
        this.mData=restauResults;
        notifyDataSetChanged();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RestaurantRecyclerAdapter.ImageViewHolder holder, int position) {

        LatLng paris = new LatLng(48.806860, 2.272980);
        String Defaultlocation = paris.latitude+"," + paris.longitude;

        //get the last position known of the user

        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
                Result restauItem = mData.get(position);
                Location location = task.getResult();
                Log.d("TAG", "Response = Adapterlocation " + location);
                if(location != null){
                    currentPosition = new LatLng(location.getLatitude(),location.getLongitude());
                    currentLocation = currentPosition.latitude+"," + currentPosition.longitude;

                    //display distance between user and restaurant

                    Log.d("TAG", "Response = AdapterLocationResponse" + currentLocation);
                    getDistance(currentLocation, restauItem.getGeometry().getLocation());
                    String distance = Integer.toString(Math.round(distanceResults[0]));
                    holder.restaurantDistance.setText(distance + "m");
                }else{
                    getDistance(Defaultlocation, restauItem.getGeometry().getLocation());
                    String distance = Integer.toString(Math.round(distanceResults[0]));
                    holder.restaurantDistance.setText(distance + "m");
                }
            });


        usersList = new ArrayList<>();


        Log.d("TAG", "Response = onBindViewHolder ");
        Result restauItem = mData.get(position);

        holder.restaurantWorkmates.setText("");

        //display name
        holder.restaurantName.setText(restauItem.getName());

        //display adresse
        holder.restaurantAdresse.setText(restauItem.getVicinity());

        //display user icon
        holder.nbUserImg.setVisibility(View.VISIBLE);

        //display photo
        if (!(restauItem.getPhotos() == null)) {
            if (!(restauItem.getPhotos().isEmpty())) {
                Glide.with(holder.itemView.getContext()).load("https://maps.googleapis.com/maps/api/place/photo" + "?maxwidth=" + MAX_WIDTH + "&maxheight=" + MAX_HEIGHT + "&photoreference=" + restauItem.getPhotos().get(0).getPhotoReference() + "&key=" + API_KEY).into(holder.restaurantImg);
            }
        }

        //display distance between user and restaurant
        Log.d("TAG", "Response = AdapterlocationDistance " + currentLocation);
        if (currentLocation != null) {
            getDistance(currentLocation, restauItem.getGeometry().getLocation());
            String distance = Integer.toString(Math.round(distanceResults[0]));
            holder.restaurantDistance.setText(distance + "m");
        }else{
            getDistance(Defaultlocation, restauItem.getGeometry().getLocation());
            String distance = Integer.toString(Math.round(distanceResults[0]));
            holder.restaurantDistance.setText(distance + "m");
        }

        //display opening hours
        if(restauItem.getOpeningHours() != null) {
            if ( restauItem.getOpeningHours().getOpenNow()) {
                holder.restaurantOpeningHour.setText("OPEN");
            } else {
                holder.restaurantOpeningHour.setText("CLOSE");
            }
        }

        //display the stars according to the rating
        if(restauItem.getRating() != null){
            double rating = restauItem.getRating();
            if (rating > 1) {
                holder.ratingStars1.setVisibility(View.VISIBLE);
                if (rating > 2.5) {
                    holder.ratingStars2.setVisibility(View.VISIBLE);
                    if (rating > 4) {
                        holder.ratingStars3.setVisibility(View.VISIBLE);
                    }
                }
            }
        }


        //display the number of users who have selected a restaurant
        CollectionReference collectionReference = UserHelper.getUsersCollection();
        collectionReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                usersList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String uid = document.getData().get("uid").toString();
                    String username = document.getData().get("username").toString();
                    Log.d("TAG", "Response = workmateRecycler " + username);
                    String urlPicture = document.getData().get("urlPicture").toString();
                    String restoId = document.getData().get("restoId").toString();
                    User userToAdd = new User(uid,username,urlPicture);
                    if (restoId.equals(restauItem.getPlaceId()))  {
                        usersList.add(userToAdd);
                        holder.restaurantWorkmates.setText("(" + usersList.size() + ")");
                    }
                }
            }

        });



    }

    public Result getRestaurant(int position){
        return this.mData.get(position);
    }

    @Override
    public int getItemCount() {
        int itemCount=0;
        if (mData != null) itemCount = mData.size();
        return itemCount;
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        TextView restaurantName;
        TextView restaurantAdresse;
        TextView restaurantDistance;
        TextView restaurantOpeningHour;
        TextView restaurantWorkmates;

        ImageView restaurantImg;
        ImageView nbUserImg;
        ImageView ratingStars1;
        ImageView ratingStars2;
        ImageView ratingStars3;

        ImageViewHolder(View itemView) {
            super(itemView);

            restaurantName = itemView.findViewById(R.id.restau_recycler_view_item_name);
            restaurantAdresse = itemView.findViewById(R.id.restau_recycler_view_item_address);
            restaurantDistance = itemView.findViewById(R.id.restau_recycler_view_item_distance);
            restaurantOpeningHour = itemView.findViewById(R.id.restau_recycler_view_item_opening_hours);
            restaurantWorkmates = itemView.findViewById(R.id.restau_recycler_view_item_nb_workmates);
            restaurantImg = itemView.findViewById(R.id.restau_recycler_view_item_img);
            ratingStars1 = itemView.findViewById(R.id.res_list_star_1);
            ratingStars2 = itemView.findViewById(R.id.res_list_star_2);
            ratingStars3 = itemView.findViewById(R.id.res_list_star_3);
            nbUserImg = itemView.findViewById(R.id.res_list_workmates_ic);




        }

    }

    private void getDistance(String startLocation, com.example.go4lunch.Model.Restaurant.Location endLocation){
        String[] separatedStart = startLocation.split(",");
        double startLatitude = Double.parseDouble(separatedStart[0]);
        double startLongitude = Double.parseDouble(separatedStart[1]);
        double endLatitude = endLocation.getLat();
        double endLongitude = endLocation.getLng();
        android.location.Location.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude,distanceResults);
    }

}
