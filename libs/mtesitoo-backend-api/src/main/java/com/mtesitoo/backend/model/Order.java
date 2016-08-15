package com.mtesitoo.backend.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Nan on 9/13/2015.
 */
public class Order implements Parcelable {

    //TODO Naily Regenerate toString + rename
    private final int mId;
    private final String mOrderStatus;
    private final double mTotalPrice;
    private final Date mDateOrderPlaced;
    private String paymentMethod;

    private int customerId;
    private final String customerName;
    //TODO Naily Convert to Address object
    private String deliveryAddress;
    private String emailAddress;
    private String customerTelephone;

    private ArrayList<OrderProduct> products;

    //TO REMOVE
    private final String mProductName;
    private final String mProductPrice;
    private final Integer mProductQuantity;




    public Order(Parcel in) {

        this.mId = in.readInt();;
        this.customerName = in.readString();
        this.deliveryAddress =in.readString();
        this.mProductName = in.readString();
        this.mOrderStatus = in.readString();
        this.mTotalPrice = in.readDouble();
        this.mProductPrice = in.readString();
        this.mProductQuantity = in.readInt();
        this.mDateOrderPlaced =new Date(in.readLong());
        this.paymentMethod = in.readString();

    }

    public Order(int mId, String customerName, String deliveryAddress, String mProductName, String mOrderStatus, double mTotalPrice, String mProductPrice, Integer mProductQuantity, Date mDateOrderPlaced, String paymentMethod) {
        this.mId = mId;
        this.customerName = customerName;
        this.deliveryAddress = deliveryAddress;
        this.mProductName = mProductName;
        this.mOrderStatus = mOrderStatus;
        this.mTotalPrice = mTotalPrice;
        this.mProductPrice = mProductPrice;
        this.mProductQuantity = mProductQuantity;
        this.mDateOrderPlaced = mDateOrderPlaced;
        this.paymentMethod = paymentMethod;
    }


    public String getCustomerName() {
        return customerName;
    }

    public int getmId() {
        return mId;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getmProductName() {
        return mProductName;
    }

    public String getmOrderStatus() {
        return mOrderStatus;
    }

    public double getmTotalPrice() {
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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerTelephone() {
        return customerTelephone;
    }

    public ArrayList<OrderProduct> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<OrderProduct> products) {
        this.products = products;
    }

    public void setCustomerTelephone(String customerTelephone) {
        this.customerTelephone = customerTelephone;
    }

    //    public static Creator<Order> getCREATOR() {
//        return CREATOR;
//    }


    @Override
    public String toString() {
        return "Order{" +
                "mId=" + mId +
                ", mOrderStatus='" + mOrderStatus + '\'' +
                ", mTotalPrice=" + mTotalPrice +
                ", mDateOrderPlaced=" + mDateOrderPlaced +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", customerId=" + customerId +
                ", customerName='" + customerName + '\'' +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", customerTelephone='" + customerTelephone + '\'' +
                ", products=" + products +
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
        dest.writeString(customerName);
        dest.writeString(deliveryAddress);
        dest.writeString(mProductName);
        dest.writeString(mOrderStatus);
        dest.writeDouble(mTotalPrice);
        dest.writeString(mProductPrice);
        dest.writeInt(mProductQuantity);
        dest.writeLong(mDateOrderPlaced.getTime());
        dest.writeString(paymentMethod);
    }


}