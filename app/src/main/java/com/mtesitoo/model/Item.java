package com.mtesitoo.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by jackwu on 2015-07-11.
 */

public class Item implements Parcelable {
    private String mName;
    private Double mPrice;
    private Integer mQuantity;
    private Date mExpiration;
    private String mTransportation;

    private Item(Parcel in) {

        this.mName = in.readString();
        this.mPrice = in.readDouble();
        this.mQuantity = in.readInt();
        this.mExpiration = new Date(in.readLong());
        this.mTransportation = in.readString();
    }

    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {

        @Override
        public Item createFromParcel(Parcel source) {
            return new Item(source);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public Item(String newName, double newPrice, int newQuantity, String newTransportation) {
        mName = newName;
        mPrice = newPrice;
        mQuantity = newQuantity;
        mExpiration = new Date();
        mTransportation = newTransportation;
    }

    public String getName() {
        return mName;
    }

    public Double getPrice() {
        return mPrice;
    }

    public Integer getQuantity() {
        return mQuantity;
    }

    public Date getExpiration() {
        return mExpiration;
    }

    public String getTransportation() {
        return mTransportation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeDouble(mPrice);
        dest.writeInt(mQuantity);
        dest.writeLong(mExpiration.getTime());
        dest.writeString(mTransportation);
    }
}
