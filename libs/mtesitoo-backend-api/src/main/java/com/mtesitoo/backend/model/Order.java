package com.mtesitoo.backend.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Nan on 9/13/2015.
 */
public class Order implements Parcelable {



    private final int mId;
    private final String mCustomerName;
    private final String mDeliveryAddress;
    private final String mProductName;
    private final String mOrderStatus;
    private final String mTotalPrice;
    private final String mProductPrice;
    private final Integer mProductQuantity;
    private final Date mDateOrderPlaced;
    private final String mPaymentMethod;


    public Order(Parcel in) {

        this.mId = in.readInt();;
        this.mCustomerName = in.readString();
        this.mDeliveryAddress =in.readString();
        this.mProductName = in.readString();
        this.mOrderStatus = in.readString();
        this.mTotalPrice = in.readString();
        this.mProductPrice = in.readString();
        this.mProductQuantity = in.readInt();
        this.mDateOrderPlaced =new Date(in.readLong());
        this.mPaymentMethod = in.readString();

    }

    public Order(int mId, String mCustomerName, String mDeliveryAddress, String mProductName, String mOrderStatus, String mTotalPrice, String mProductPrice, Integer mProductQuantity, Date mDateOrderPlaced, String mPaymentMethod) {
        this.mId = mId;
        this.mCustomerName = mCustomerName;
        this.mDeliveryAddress = mDeliveryAddress;
        this.mProductName = mProductName;
        this.mOrderStatus = mOrderStatus;
        this.mTotalPrice = mTotalPrice;
        this.mProductPrice = mProductPrice;
        this.mProductQuantity = mProductQuantity;
        this.mDateOrderPlaced = mDateOrderPlaced;
        this.mPaymentMethod = mPaymentMethod;
    }


    public String getmCustomerName() {
        return mCustomerName;
    }

    public int getmId() {
        return mId;
    }

    public String getmDeliveryAddress() {
        return mDeliveryAddress;
    }

    public String getmProductName() {
        return mProductName;
    }

    public String getmOrderStatus() {
        return mOrderStatus;
    }

    public String getmTotalPrice() {
        return mTotalPrice;
    }

    public String getmProductPrice() {
        return mProductPrice;
    }

    public Integer getmProductQuantity() {
        return mProductQuantity;
    }

    public Date getmDateOrderPlaced() {
        return mDateOrderPlaced;
    }

    public String getmPaymentMethod() {
        return mPaymentMethod;
    }

//    public static Creator<Order> getCREATOR() {
//        return CREATOR;
//    }


    @Override
    public String toString() {
        return "Order{" +
                "mId=" + mId +
                ", mCustomerName='" + mCustomerName + '\'' +
                ", mDeliveryAddress='" + mDeliveryAddress + '\'' +
                ", mProductName='" + mProductName + '\'' +
                ", mOrderStatus='" + mOrderStatus + '\'' +
                ", mTotalPrice='" + mTotalPrice + '\'' +
                ", mProductPrice='" + mProductPrice + '\'' +
                ", mProductQuantity=" + mProductQuantity +
                ", mDateOrderPlaced=" + mDateOrderPlaced +
                ", mPaymentMethod='" + mPaymentMethod + '\'' +
                '}';
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
        dest.writeInt(mId);
        dest.writeString(mCustomerName);
        dest.writeString(mDeliveryAddress);
        dest.writeString(mProductName);
        dest.writeString(mOrderStatus);
        dest.writeString(mTotalPrice);
        dest.writeString(mProductPrice);
        dest.writeInt(mProductQuantity);
        dest.writeLong(mDateOrderPlaced.getTime());
        dest.writeString(mPaymentMethod);
    }
}