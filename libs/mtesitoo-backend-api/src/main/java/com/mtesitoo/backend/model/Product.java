package com.mtesitoo.backend.model;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.mtesitoo.backend.cache.CategoryCache;
import com.mtesitoo.backend.cache.logic.ICategoryCache;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Model object for a Mtesitoo product.
 */
public class Product implements Parcelable {
    public static final int MAX_AUX_IMAGES = 3;

    private final int mId;
    private final String mName;
    private final String mDescription;
    private final String mLocation;
    private ArrayList<String> mCategories = new ArrayList<>();
    private final String mSIUnit;
    private final String mPricePerUnit;
    private final Integer mQuantity;
    private final Date mExpiration;
    private final Uri mThumbnail;
    private final ArrayList<Uri> mAuxImages;
    private Uri lastImage;

    private Product(Parcel in) {
        this.mId = in.readInt();
        this.mName = in.readString();
        this.mDescription = in.readString();
        this.mLocation = in.readString();
        in.readStringList(mCategories);
        this.mSIUnit = in.readString();
        this.mPricePerUnit = in.readString();
        this.mQuantity = in.readInt();
        this.mExpiration = new Date(in.readLong());
        this.mThumbnail = null;
        this.mAuxImages = new ArrayList<>();
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {

        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };



    /**
     * @param name         product name (e.g. apples)
     * @param description  product descriptions (e.g. freshly picked, sweet-sour)
     * @param location     product location (e.g. 123 fake street)
     * @param categories     product category (e.g. fruits & nuts)
     * @param siUnit       product si unit measurement (e.g. liter, gram)
     * @param pricePerUnit product price per quantity (e.g. $2.5)
     * @param quantity     product quantity (e.g. 100)
     * @param expiration   product post expiration (e.g. YYYY-MM-DD)
     * @param thumbnail    url of the product's thumbnail
     */
    public Product(int id, String name, String description, String location, ArrayList<String> categories, String siUnit,
                   String pricePerUnit, Integer quantity, Date expiration, Uri thumbnail, ArrayList<Uri> auxImages) {
        mId = id;
        mName = name;
        mDescription = description;
        mLocation = location;
        mCategories = categories;
        mSIUnit = siUnit;
        mPricePerUnit = pricePerUnit;
        mQuantity = quantity;
        mExpiration = expiration;
        mThumbnail = thumbnail;
        mAuxImages = auxImages;
    }

    /**
     * @param name         product name (e.g. apples)
     * @param description  product descriptions (e.g. freshly picked, sweet-sour)
     * @param location     product location (e.g. 123 fake street)
     * @param category     product category (e.g. fruits & nuts)
     * @param siUnit       product si unit measurement (e.g. liter, gram)
     * @param pricePerUnit product price per quantity (e.g. $2.5)
     * @param quantity     product quantity (e.g. 100)
     * @param expiration   product post expiration (e.g. YYYY-MM-DD)
     * @param thumbnail    url of the product's thumbnail
     */
    public Product(int id, String name, String description, String location, String category, String siUnit,
                   String pricePerUnit, Integer quantity, Date expiration, Uri thumbnail, ArrayList<Uri> auxImages) {

        ArrayList<String> categoryList = new ArrayList<>();
        categoryList.add(category);

        mId = id;
        mName = name;
        mDescription = description;
        mLocation = location;
        mCategories = categoryList;
        mSIUnit = siUnit;
        mPricePerUnit = pricePerUnit;
        mQuantity = quantity;
        mExpiration = expiration;
        mThumbnail = thumbnail;
        mAuxImages = auxImages;

    }

    /**
     * Returns true if image is added successfully, otherwise false
     * @param imageUri
     * @return
     */
    public boolean addImage(Uri imageUri){
        boolean success = false;
        if (mAuxImages.size() < MAX_AUX_IMAGES) {
            mAuxImages.add(imageUri);
            lastImage = imageUri;
            success = true;
        } else {
            Log.e("PRODUCT", "No more than " + MAX_AUX_IMAGES + " allowed per product");
        }

        return success;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getLocation() {
        return mLocation;
    }

    public ArrayList<String> getCategories() {
        return mCategories;
    }

    public String getCategoriesJSON() {

        JSONArray jsonCategories = new JSONArray();

        for(String s : mCategories){
            jsonCategories.put(s);
        }

        return jsonCategories.toString();
    }

    public ArrayList<String> getResolvedCategories(Context context){
        ICategoryCache cache = new CategoryCache(context);
        List<Category> categories = cache.getCategories();
        ArrayList<String> resolvedList = new ArrayList<>();

        for (String mCatId : mCategories) {
            for (Category c : categories) {
                if(c.getId() == Integer.valueOf(mCatId)){
                    resolvedList.add(c.getName());
                    break;
                }
            }
        }

        return resolvedList;

    }

    public String getCategoriesIDStringList(){

        String categoryIDStringList = "";
        for(String cat : mCategories){
            categoryIDStringList = categoryIDStringList.concat(cat).concat(",");
        }

        if(categoryIDStringList.length() > 0){
            return categoryIDStringList.substring(0,categoryIDStringList.length()-1);
        }else{
            return "None";
        }
    }

    public String getCategoriesStringList(Context context){

        ICategoryCache cache = new CategoryCache(context);
        List<Category> categories = cache.getCategories();
        String categoryStringList = "";

        for (String mCatId : mCategories) {
            for (Category c : categories) {
                if(c.getId() == Integer.valueOf(mCatId)){
                    categoryStringList = categoryStringList.concat(c.getName()).concat(", ");
                    break;
                }
            }
        }

        if(categoryStringList.length() > 0){
            return categoryStringList.substring(0,categoryStringList.length()-2);
        }else{
            return "None";
        }
    }

    public String getSIUnit() {
        return mSIUnit;
    }

    public String getPricePerUnit() {
        return mPricePerUnit;
    }

    public Integer getQuantity() {
        return mQuantity;
    }

    public Date getExpiration() {
        return mExpiration;
    }

    public String getExpirationFormattedForApp() {

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = "";
        if (mExpiration != null) {
            (dateFormatter).format(mExpiration);
        }

        return formattedDate;
    }

    public String getExpirationFormattedForAPI() {

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String formattedDate = "0000-00-00 00:00:00";

        if (mExpiration != null) {
            (dateFormatter).format(mExpiration);
        }

        return formattedDate;
    }

    public Uri getmThumbnail() {
        return mThumbnail;
    }

    public ArrayList<Uri> getAuxImages() {return mAuxImages; }

    public Uri getLastImage() { return lastImage; }

    @Override
    public String toString() {
        return "Product{" + "id+'" + mId + '\'' + "name='" + mName + '\'' + ", description='" + mDescription + '\''
                + ", price='" + mPricePerUnit + '\'' + ", thumbnail=" + mThumbnail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Product product = (Product) o;

        if (!mName.equals(product.mName))
            return false;
        return (mDescription != null ? !mDescription.equals(product.mDescription)
                : product.mDescription != null);
    }

    @Override
    public int hashCode() {
        int result = mName.hashCode();
        result = 31 * result + (mDescription != null ? mDescription.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeString(mDescription);
        dest.writeString(mLocation);
        dest.writeStringList(mCategories);
        dest.writeString(mSIUnit);
        dest.writeString(mPricePerUnit);
        dest.writeInt(mQuantity);
        if (mExpiration != null) {
            dest.writeLong(mExpiration.getTime());
        } else {
            dest.writeLong(0);
        }
    }
}