package com.example.go4lunch.utils;

import android.util.Log;

import com.example.go4lunch.Model.Restaurant.Results;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantCall {

    public interface Callbacks {
        void onResponse(Results restaurantResults);

        void onFailure();
    }

    public static void fetchNearbyRestaurant(Callbacks callbacks, String location, String type, Integer radius,String key) {

        final WeakReference<Callbacks> callbacksWeakReference = new WeakReference<>(callbacks);

        GoogleService nytimesService = GoogleService.retrofit.create(GoogleService.class);

        Call<Results> call = nytimesService.getPlaces(location,type,radius,key);

        call.enqueue(new Callback<Results>() {

            @Override
            public void onResponse(Call<Results> call, Response<Results> response) {
                Log.d("TAG","Response = ");
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onResponse(response.body());
            }



            @Override
            public void onFailure(Call<Results> call, Throwable t) {
                Log.d("TAG","Response = "+t.toString());
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onFailure();
            }
        });
    }
}

