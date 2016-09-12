// TODO NAILY Rework this so that you use the Product class rather than this OrderProduct
// Better wait until Carl submits its changes.
// At the moment, there are quite a few changes that need to be done to the Product class
// - Add model
// - Convert unitPrice to double, quantity to int

package com.mtesitoo.backend.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by naily on 11/08/16
 */

public class OrderProduct implements Parcelable {

    private final int id;
    private final OrderStatus orderStatus;
    private final String name;
    private final String model;
    private final int quantity;
    private final double unitPrice;

    public OrderProduct(Parcel in)
    {
        id = in.readInt();
        orderStatus = (OrderStatus)in.readValue(OrderStatus.class.getClassLoader());
        name = in.readString();
        model = in.readString();
        quantity = in.readInt();
        unitPrice = in.readDouble();
    }

    public OrderProduct(int id, OrderStatus status, String name, String model, int quantity, double unitPrice) {
        this.id = id;
        this.orderStatus = status;
        this.name = name;
        this.model = model;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public int getId() {
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public String getName() {
        return name;
    }

    public String getModel() {
        return model;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    @Override
    public String toString() {
        return "OrderProduct{" +
                "id=" + id +
                ", orderStatus=" + orderStatus +
                ", name='" + name + '\'' +
                ", model='" + model + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                '}';
    }

    public static final Parcelable.Creator<OrderProduct> CREATOR = new Parcelable.Creator<OrderProduct>() {

        @Override
        public OrderProduct createFromParcel(Parcel source) {
            return new OrderProduct(source);
        }

        @Override
        public OrderProduct[] newArray(int size) {
            return new OrderProduct[size];
        }
    };

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(id);
        dest.writeValue(orderStatus);
        dest.writeString(name);
        dest.writeString(model);
        dest.writeInt(quantity);
        dest.writeDouble(unitPrice);
    }
}
