
package com.example.go4lunch.Model.Restaurant;

import java.util.List;

import com.example.go4lunch.Model.Restaurant.Geometry;
import com.example.go4lunch.Model.Restaurant.OpeningHours;
import com.example.go4lunch.Model.Restaurant.Photo;
import com.example.go4lunch.Model.Restaurant.PlusCode;
import com.google.gson.annotations.SerializedName;


public class Result {

    @SerializedName("business_status")
    private String mBusinessStatus;
    @SerializedName("geometry")
    private Geometry mGeometry;
    @SerializedName("icon")
    private String mIcon;
    @SerializedName("name")
    private String mName;
    @SerializedName("opening_hours")
    private OpeningHours mOpeningHours;
    @SerializedName("photos")
    private List<Photo> mPhotos;
    @SerializedName("place_id")
    private String mPlaceId;
    @SerializedName("plus_code")
    private PlusCode mPlusCode;
    @SerializedName("price_level")
    private Long mPriceLevel;
    @SerializedName("rating")
    private Long mRating;
    @SerializedName("reference")
    private String mReference;
    @SerializedName("scope")
    private String mScope;
    @SerializedName("types")
    private List<String> mTypes;
    @SerializedName("user_ratings_total")
    private Long mUserRatingsTotal;
    @SerializedName("vicinity")
    private String mVicinity;

    public String getBusinessStatus() {
        return mBusinessStatus;
    }

    public void setBusinessStatus(String businessStatus) {
        mBusinessStatus = businessStatus;
    }

    public Geometry getGeometry() {
        return mGeometry;
    }

    public void setGeometry(Geometry geometry) {
        mGeometry = geometry;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public OpeningHours getOpeningHours() {
        return mOpeningHours;
    }

    public void setOpeningHours(OpeningHours openingHours) {
        mOpeningHours = openingHours;
    }

    public List<Photo> getPhotos() {
        return mPhotos;
    }

    public void setPhotos(List<Photo> photos) {
        mPhotos = photos;
    }

    public String getPlaceId() {
        return mPlaceId;
    }

    public void setPlaceId(String placeId) {
        mPlaceId = placeId;
    }

    public PlusCode getPlusCode() {
        return mPlusCode;
    }

    public void setPlusCode(PlusCode plusCode) {
        mPlusCode = plusCode;
    }

    public Long getPriceLevel() {
        return mPriceLevel;
    }

    public void setPriceLevel(Long priceLevel) {
        mPriceLevel = priceLevel;
    }

    public Long getRating() {
        return mRating;
   }

    public void setRating(Long rating) {
        mRating = rating;
     }

    public String getReference() {
        return mReference;
    }

    public void setReference(String reference) {
        mReference = reference;
    }

    public String getScope() {
        return mScope;
    }

    public void setScope(String scope) {
        mScope = scope;
    }

    public List<String> getTypes() {
        return mTypes;
    }

    public void setTypes(List<String> types) {
        mTypes = types;
    }

    public Long getUserRatingsTotal() {
        return mUserRatingsTotal;
    }

    public void setUserRatingsTotal(Long userRatingsTotal) {
        mUserRatingsTotal = userRatingsTotal;
    }

    public String getVicinity() {
        return mVicinity;
    }

    public void setVicinity(String vicinity) {
        mVicinity = vicinity;
    }

}
