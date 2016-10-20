package com.mtesitoo.backend.service.logic;

import com.mtesitoo.backend.model.Order;
import com.mtesitoo.backend.model.OrderProduct;
import com.mtesitoo.backend.model.OrderStatus;


import java.util.List;

/**
 * Created by User on 26-04-2016
 */
public interface IOrderRequest {

    void getOrders(int sellerId, OrderStatus orderStatus, ICallback<List<Order>> callback);

    void getDetailedOrders(Order order, ICallback<Order> callback);

    void submitEditStatusSingleProduct(OrderProduct orderProduct, OrderStatus orderStatus, ICallback<OrderProduct> callback);
}
