package com.example.go4lunch.ViewModel.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.go4lunch.Model.Restaurant.Result;
import com.example.go4lunch.Model.Restaurant.Results;
import com.example.go4lunch.R;
import com.example.go4lunch.ViewModel.adapter.RestaurantRecyclerAdapter;
import com.example.go4lunch.utils.RestaurantCall;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class listViewFragment extends Fragment implements RestaurantCall.Callbacks {

    private RecyclerView recyclerView;
    private RestaurantRecyclerAdapter adapter;
    String API_KEY = "key=AIzaSyCCR-afR0LoWYb1wYm4q8loXKuJIvCl7OM";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_list_view, container, false);
        recyclerView = view.findViewById(R.id.fragment_restaurants_recyclerview);
        executeHttpRequestWithRetrofit();
        setUpRecyclerView();
        return view;

    }

    private void executeHttpRequestWithRetrofit() {
        LatLng paris = new LatLng(48.806860, 2.272980);
        String location = paris.latitude+"," + paris.longitude;
        Log.d("TAG", "Response = responseRequest" + location);
        RestaurantCall.fetchNearbyRestaurant(this, location,"restaurant",5000,API_KEY);
    }

    @Override
    public void onResponse(Results restaurantResults) {
        Log.d("TAG", "Response = response");
        adapter.setResults(restaurantResults.getResults());

    }

    @Override
    public void onFailure() {
        Log.d("TAG", "Response = failed");
    }

    private void setUpRecyclerView() {
        Log.d("TAG", "Response = recycler ");
        List<Result> results = new ArrayList<>();
        adapter = new RestaurantRecyclerAdapter(results);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }
}

