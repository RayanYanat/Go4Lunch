package com.example.go4lunch.utils;

import android.util.Log;


import com.example.go4lunch.Model.RestaurantItem.PlaceDetailsResult;
import com.example.go4lunch.Model.RestaurantItem.Result;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantDetailCall {
    public interface Callbacks {
        void onResponse(PlaceDetailsResult restaurantResults);

        void onFailure();
    }

    public static void fetchPlaceDetails(Callbacks callbacks, String placeID, String key) {

        final WeakReference<RestaurantDetailCall.Callbacks> callbacksWeakReference = new WeakReference<>(callbacks);

        GoogleService googlePlaceDetailService = GoogleService.retrofit.create(GoogleService.class);

        Call<PlaceDetailsResult> call = googlePlaceDetailService.getDetailPlaces(placeID,key);

        call.enqueue(new Callback<PlaceDetailsResult>() {

            @Override
            public void onResponse(Call<PlaceDetailsResult> call, Response<PlaceDetailsResult> response) {
                Log.d("TAG","Response = ");
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onResponse(response.body());
            }



            @Override
            public void onFailure(Call<PlaceDetailsResult> call, Throwable t) {
                Log.d("TAG","Response = "+t.toString());
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onFailure();
            }
        });
    }
}
