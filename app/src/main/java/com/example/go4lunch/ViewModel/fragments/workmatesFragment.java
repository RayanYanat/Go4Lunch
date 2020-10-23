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


import com.example.go4lunch.Model.Users.User;


import com.example.go4lunch.Model.Users.UserHelper;
import com.example.go4lunch.R;
import com.example.go4lunch.ViewModel.adapter.WorkmateRecyclerAdapter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;


import java.util.ArrayList;
import java.util.List;


public class workmatesFragment extends Fragment {

    private RecyclerView recyclerView;
    private WorkmateRecyclerAdapter adapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workmates, container, false);
        recyclerView = view.findViewById(R.id.MyRecyclerView);
        setUpRecyclerView();
        return view;
    }


    private void setUpRecyclerView() {
         List<User> results = new ArrayList<>();
        Log.d("TAG", "Response = workmateRecycler ");
        CollectionReference collectionReference = UserHelper.getUsersCollection();
        collectionReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                   // User user = document.toObject(User.class);
                        String uid = document.getData().get("uid").toString();
                        String username = document.getData().get("username").toString();
                        Log.d("TAG", "Response = workmateRecycler " + username);
                        String urlPicture = document.getData().get("urlPicture").toString();
                        User userToAdd = new User(uid,username,urlPicture);
                        results.add(userToAdd);
                        Log.d("TAG", "Response = workmateRecycler " + results.get(0).toString());
                        adapter = new WorkmateRecyclerAdapter (results);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(adapter);
                    }
                }
        });


    }

}
