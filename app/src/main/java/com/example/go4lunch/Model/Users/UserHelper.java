package com.example.go4lunch.Model.Users;





import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;


public class UserHelper {

    private static final String COLLECTION_NAME = "users";

    public static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    public static Task<Void> createUser(String uid, String username, String urlPicture) {
        User userToCreate = new User(uid, username, urlPicture);
        return UserHelper.getUsersCollection().document(uid).set(userToCreate);
    }

    public static Task<DocumentSnapshot> getUser(String uid){
        return UserHelper.getUsersCollection().document(uid).get();
    }

    public static String getCurrentUserId() {
        return Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    }

    public static Task<Void> updateRestoName(String restoName, String uid) {
        return UserHelper.getUsersCollection().document(uid).update("restoName", restoName);
    }

    public static Task<Void> updateLikedResto(List<String> restoLike, String uid) {
        return UserHelper.getUsersCollection().document(uid).update("restoLike", restoLike);
    }

    public static Task<Void> updateRestoId(String restoId, String uid) {
        return UserHelper.getUsersCollection().document(uid).update("restoId", restoId);
    }






}
