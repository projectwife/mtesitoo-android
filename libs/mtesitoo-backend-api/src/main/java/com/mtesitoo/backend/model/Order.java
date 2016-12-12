package com.mtesitoo.backend.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Nan on 9/13/2015
 */
public class Order implements Parcelable {

    private int id;
    private OrderStatus orderStatus;
    private double totalPrice;
    private Date dateOrderPlaced;
    private String paymentMethod;

    private int customerId;
    private String customerName;
    private String deliveryAddress;
    private String emailAddress;
    private String customerTelephone;

    private ArrayList<OrderProduct> products;

    private Order()
    {
        // Initialise ArrayList to avoid reading or writing null objects in the Parcel.
        products = new ArrayList<>();
    }

    public Order(Parcel in) {
        this();
        this.id = in.readInt();
        this.orderStatus = (OrderStatus)in.readValue(OrderStatus.class.getClassLoader());
        this.totalPrice = in.readDouble();
        this.dateOrderPlaced =new Date(in.readLong());
        this.paymentMethod = in.readString();
        this.customerId = in.readInt();
        this.customerName = in.readString();
        this.deliveryAddress =in.readString();
        this.emailAddress = in.readString();
        this.customerTelephone = in.readString();
        in.readTypedList(products, OrderProduct.CREATOR);
    }

    public Order(int id, String customerName, String deliveryAddress, OrderStatus orderStatus, double totalPrice, Date dateOrderPlaced, String paymentMethod) {
        this();
        this.id = id;
        this.customerName = customerName;
        this.deliveryAddress = deliveryAddress;
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
        this.dateOrderPlaced = dateOrderPlaced;
        this.paymentMethod = paymentMethod;
    }


    public String getCustomerName() {
        return customerName;
    }

    public int getId() {
        return id;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public Date getDateOrderPlaced() {
        return dateOrderPlaced;
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


    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderStatus=" + orderStatus +
                ", totalPrice=" + totalPrice +
                ", dateOrderPlaced=" + dateOrderPlaced +
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
        dest.writeInt(id);
        dest.writeValue(orderStatus);
        dest.writeDouble(totalPrice);
        dest.writeLong(dateOrderPlaced.getTime());
        dest.writeString(paymentMethod);
        dest.writeInt(customerId);
        dest.writeString(customerName);
        dest.writeString(deliveryAddress);
        dest.writeString(emailAddress);
        dest.writeString(customerTelephone);
        dest.writeTypedList(products);
    }
}