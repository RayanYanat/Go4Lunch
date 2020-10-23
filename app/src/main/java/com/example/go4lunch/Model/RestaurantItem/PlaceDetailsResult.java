
package com.example.go4lunch.Model.RestaurantItem;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class PlaceDetailsResult {

    @SerializedName("html_attributions")
    private List<Object> mHtmlAttributions;
    @SerializedName("result")
    private Result mResult;
    @SerializedName("status")
    private String mStatus;


    public List<Object> getHtmlAttributions() {
        return mHtmlAttributions;
    }

    public Result getResult() {
        return mResult;
    }

    public String getStatus() {
        return mStatus;
    }

}
