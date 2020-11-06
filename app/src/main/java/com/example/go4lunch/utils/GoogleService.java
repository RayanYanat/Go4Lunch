package com.example.go4lunch.utils;


import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.Model.Restaurant.Results;
import com.example.go4lunch.Model.RestaurantItem.PlaceDetailsResult;


import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleService {

    String API_KEY = BuildConfig.google_maps_api_key;

    @GET("nearbysearch/json?" + API_KEY)
    Call<Results> getPlaces(@Query("location") String location, @Query("type") String type, @Query("radius") int radius,@Query("key") String key);

    @GET("details/json?" + API_KEY)
    Call<PlaceDetailsResult> getDetailPlaces(@Query("placeid") String placeId, @Query("key") String key);



    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/place/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();
}
