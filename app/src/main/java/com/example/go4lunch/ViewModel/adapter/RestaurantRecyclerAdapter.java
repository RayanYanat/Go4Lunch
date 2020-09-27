package com.example.go4lunch.ViewModel.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.go4lunch.Model.Restaurant.Result;
import com.example.go4lunch.R;

import java.util.List;

public class RestaurantRecyclerAdapter extends RecyclerView.Adapter<RestaurantRecyclerAdapter.ImageViewHolder> {

    private List<Result> mData;

    public RestaurantRecyclerAdapter(List<Result> data) {
        mData = data;
    }


    @NonNull
    @Override
    public RestaurantRecyclerAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_item_recyclerview, parent, false);
        return new ImageViewHolder(view);
    }

    public void setResults(List<Result> restauResults) {
        this.mData=restauResults;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantRecyclerAdapter.ImageViewHolder holder, int position) {
        Log.d("TAG", "Response = onBindViewHolder ");
        Result restauItem =mData.get(position);
        holder.restaurantName.setText(restauItem.getName());
     //   holder.restaurantOpeningHour.setText((CharSequence) restauItem.getOpeningHours());
        Glide.with(holder.itemView.getContext()).load(restauItem.getPhotos().get(0).getPhotoReference()).into(holder.restaurantImg);
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

        ImageViewHolder(View itemView) {
            super(itemView);

            restaurantName = itemView.findViewById(R.id.restau_recycler_view_item_name);
            restaurantAdresse = itemView.findViewById(R.id.restau_recycler_view_item_address);
            restaurantDistance = itemView.findViewById(R.id.restau_recycler_view_item_distance);
            restaurantOpeningHour = itemView.findViewById(R.id.restau_recycler_view_item_opening_hours);
            restaurantWorkmates = itemView.findViewById(R.id.restau_recycler_view_item_nb_workmates);
            restaurantImg = itemView.findViewById(R.id.restau_recycler_view_item_img);


        }

    }
}
