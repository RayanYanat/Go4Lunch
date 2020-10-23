package com.example.go4lunch.ViewModel.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.go4lunch.Model.Users.User;
import com.example.go4lunch.Model.Users.UserHelper;
import com.example.go4lunch.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

import bolts.Task;

public class WorkmateRecyclerAdapter extends RecyclerView.Adapter<WorkmateRecyclerAdapter.UserViewHolder> {

    private List<User> mData;


    public WorkmateRecyclerAdapter(List<User> result) {
        this.mData = result;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workmate_item_recyclerview, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User userItem = mData.get(position);
        Log.d("TAG", "Response = onBindViewHolderWorkmate ");
        UserHelper.getUser(userItem.getUid()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                String restoName = document.get("restoName").toString();
                if (!restoName.equals("")){
                    Log.d("TAG", "restoname = " + restoName);
                    holder.textView.setText(userItem.getUsername() + " is eating at " + restoName);
                }else {
                    holder.textView.setText(userItem.getUsername() + " haven't decided yet" );
                }
            }

        });

        if (!(userItem.getUrlPicture() == null)) {
            Glide.with(holder.itemView.getContext()).load(userItem.getUrlPicture()).into(holder.imageView);
        }


    }

    @Override
    public int getItemCount() {
        int itemCount = 0;
        if (mData != null) itemCount = mData.size();
        return itemCount;
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.workmates_TextView);
            imageView = itemView.findViewById(R.id.workmates_ImageView);
        }
    }
}
