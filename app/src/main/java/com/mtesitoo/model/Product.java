package com.mtesitoo.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by jackwu on 2015-07-11.
 */

public class Product implements Parcelable {
    private String mName;
    private String mDescription;
    private String mLocation;
    private String mCategory;
    private String mSIUnit;
    private String mPricePerUnit;
    private Integer mQuantity;
    private Date mExpiration;

    private Product(Parcel in) {

        this.mName = in.readString();
        this.mDescription = in.readString();
        this.mLocation = in.readString();
        this.mCategory = in.readString();
        this.mSIUnit = in.readString();
        this.mPricePerUnit = in.readString();
        this.mQuantity = in.readInt();
        this.mExpiration = new Date(in.readLong());
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

    public Product(String name, String description, String location, String category, String siUnit, String pricePerUnit, int quantity) {
        mName = name;
        mDescription = description;
        mLocation = location;
        mCategory = category;
        mSIUnit = siUnit;
        mPricePerUnit = pricePerUnit;
        mQuantity = quantity;
        mExpiration = new Date();
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

    public String getCategory() {
        return mCategory;
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

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("name: " + mName + ", ");
        sb.append("description: " + mDescription + ", ");
        sb.append("location: " + mLocation + ", ");
        sb.append("category: " + mCategory + ", ");
        sb.append("unit: " + mSIUnit + ", ");
        sb.append("price: " + mPricePerUnit + ", ");
        sb.append("quantity: " + mQuantity + ", ");

        return sb.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mDescription);
        dest.writeString(mLocation);
        dest.writeString(mCategory);
        dest.writeString(mSIUnit);
        dest.writeString(mPricePerUnit);
        dest.writeInt(mQuantity);
        dest.writeLong(mExpiration.getTime());
    }
}