package com.mtesitoo.backend.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Nan on 9/13/2015.
 */
public class Order implements Parcelable {

    private Order(Parcel in) {
    }

    public static final Parcelable.Creator<Order> CREATOR = new Parcelable.Creator<Order>() {

        @Override
        public Order createFromParcel(Parcel source) {
            return new Order(source);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}