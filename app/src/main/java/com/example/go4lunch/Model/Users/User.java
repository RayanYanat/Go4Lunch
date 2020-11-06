package com.example.go4lunch.Model.Users;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String uid;
    private String username;
    private String restoName;
    private String restoId;
    @Nullable
    private String urlPicture;
    private List<String> restoLike;




    public User(String uid, String username, String urlPicture) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.restoName = "";
        this.restoId="";
        this.restoLike = new ArrayList<>();

    }

    public User() {}

    public String getUid() { return uid; }
    public String getUsername() { return username; }
    public String getUrlPicture() { return urlPicture; }
    public String getRestoName() {return restoName;}
    public String getRestoId(){return restoId; }
    public List<String> getRestoLike() { return restoLike; }


    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRestoName(String restoName) {
        this.restoName = restoName;
    }

    public void setRestoId(String restoId) {
        this.restoId = restoId;
    }

    public void setUrlPicture(@Nullable String urlPicture) {
        this.urlPicture = urlPicture;
    }

    public void setRestoLike(List<String> restoLike) {
        this.restoLike = restoLike;
    }
}
