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
import com.mtesitoo.backend.model.url.VendorOrdersURL;
import com.mtesitoo.backend.service.logic.ICallback;

import com.mtesitoo.backend.model.Order;
import com.mtesitoo.backend.service.logic.IOrderRequest;


/**
 * Created by User on 26-04-2016.
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
                params.put(mContext.getString(R.string.params_customer_name), order.getmCustomerName());
                params.put(mContext.getString(R.string.params_product_name), order.getmProductName());
                params.put(mContext.getString(R.string.params_product_price), order.getmProductPrice());
                params.put(mContext.getString(R.string.params_product_quantity), Integer.toString(order.getmProductQuantity()));
                params.put(mContext.getString(R.string.params_order_delivery_address), order.getmDeliveryAddress());
                params.put(mContext.getString(R.string.params_order_total_price), order.getmTotalPrice());
                //params.put(mContext.getString(R.string.params_order_placed_date), order.getmDateOrderPlaced().toString());
                params.put(mContext.getString(R.string.params_order_payment_method), order.getmPaymentMethod());
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
    public void getOrders(final int sellerId, ICallback<List<Order>> callback) {
        Log.d("getOrders - SellerId",String.valueOf(sellerId));
        URL url = new VendorOrdersURL(mContext, R.string.path_order_vendor);
        Log.d("Vendor Orders URL",url.toString());
        OrderResponse response = new OrderResponse(callback);

        AuthorizedStringRequest stringRequest = new AuthorizedStringRequest(mContext, com.android.volley.Request.Method.GET, url.toString(), response, response);

        stringRequest.setAuthorization(new Authorization(mContext, mAuthorizationCache.getAuthorization()).toString());
        mRequestQueue.add(stringRequest);
    }
}
