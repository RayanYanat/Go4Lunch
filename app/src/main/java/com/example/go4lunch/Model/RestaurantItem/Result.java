
package com.example.go4lunch.Model.RestaurantItem;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Result {

    @SerializedName("formatted_phone_number")
    private String mFormattedPhoneNumber;
    @SerializedName("name")
    private String mName;
    @SerializedName("rating")
    private Double mRating;
    @SerializedName("website")
    @Expose
    private String website;

    public String getFormattedPhoneNumber() {
        return mFormattedPhoneNumber;
    }

    public String getName() {
        return mName;
    }

    public Double getRating() {
        return mRating;
    }

    public String getWebsite() {
        return website;
    }

}


