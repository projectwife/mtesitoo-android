package com.mtesitoo.backend.service;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mtesitoo.backend.R;
import com.mtesitoo.backend.model.header.Authorization;
import com.mtesitoo.backend.model.AuthorizedStringRequest;
import com.mtesitoo.backend.model.URL;
import com.mtesitoo.backend.model.OrderStatus;
import com.mtesitoo.backend.model.url.VendorOrderDetailsURL;
import com.mtesitoo.backend.model.url.VendorOrdersURL;
import com.mtesitoo.backend.service.logic.ICallback;

import com.mtesitoo.backend.model.Order;
import com.mtesitoo.backend.service.logic.IOrderRequest;


/**
 * Created by User on 26-04-2016
 */
public class OrderRequest  extends Request implements IOrderRequest {
    public OrderRequest(Context context) {
        super(context);
        mILoginRequest = new LoginRequest(mContext);
    }

    @Override
    public void submitOrder(final Order order, final ICallback<Order> callback) {

        /**/
        URL url = new URL(mContext, R.string.path_product_product);
        OrderResponse response = new OrderResponse(null);

        AuthorizedStringRequest stringRequest = new AuthorizedStringRequest(mContext, com.android.volley.Request.Method.POST, url.toString(), response, response) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(mContext.getString(R.string.params_customer_name), order.getCustomerName());
                params.put(mContext.getString(R.string.params_product_name), order.getmProductName());
                params.put(mContext.getString(R.string.params_product_price), order.getmProductPrice());
                params.put(mContext.getString(R.string.params_product_quantity), Integer.toString(order.getmProductQuantity()));
                params.put(mContext.getString(R.string.params_order_delivery_address), order.getDeliveryAddress());
                params.put(mContext.getString(R.string.params_order_total_price), Double.toString(order.getmTotalPrice()));
                //params.put(mContext.getString(R.string.params_order_placed_date), order.getmDateOrderPlaced().toString());
                params.put(mContext.getString(R.string.params_order_payment_method), order.getPaymentMethod());
                params.put(mContext.getString(R.string.params_order_status), order.getmOrderStatus());
                //params.put(mContext.getString(R.string.params_product_meta_title), "meta_title");
               // params.put(mContext.getString(R.string.params_product_status), mContext.getString(R.string.params_product_status_enabled));

                return params;
            }
        };

        stringRequest.setAuthorization(new Authorization(mContext, mAuthorizationCache.getAuthorization()).toString());
        mRequestQueue.add(stringRequest);

    }

    @Override
    public void getOrders(final int sellerId, OrderStatus orderStatus, ICallback<List<Order>> callback) {
        Log.d("getOrders - orderStatus", orderStatus.toString());
        URL url = new VendorOrdersURL(mContext, orderStatus);
        Log.d("Vendor Orders URL",url.toString());

        OrderResponse response = new OrderResponse(callback);

        AuthorizedStringRequest stringRequest = new AuthorizedStringRequest(mContext, com.android.volley.Request.Method.GET, url.toString(), response, response);

        stringRequest.setAuthorization(new Authorization(mContext, mAuthorizationCache.getAuthorization()).toString());
        mRequestQueue.add(stringRequest);
    }

    //TODO move to another class
    @Override
    public void getDetailedOrders(final Order order, ICallback callback)
    {
        //TO DELETE
        /*OrderStatus orderStatus = OrderStatus.ALL;
        Log.d("getOrders - orderStatus", orderStatus.toString());
        URL url = new VendorOrdersURL(mContext, orderStatus);
        Log.d("Vendor Orders URL",url.toString());*/

        //GET /api/v1/vendor/order/{id}
        Log.d("getOrders - orderId", Integer.toString(order.getmId()));
        URL url = new VendorOrderDetailsURL(mContext, order.getmId());
        Log.d("Detailed Orders URL",url.toString());

        OrderDetailsResponse response = new OrderDetailsResponse(order, callback);

        AuthorizedStringRequest stringRequest = new AuthorizedStringRequest(mContext, com.android.volley.Request.Method.GET, url.toString(), response, response);

        stringRequest.setAuthorization(new Authorization(mContext, mAuthorizationCache.getAuthorization()).toString());
        mRequestQueue.add(stringRequest);
    }
}
