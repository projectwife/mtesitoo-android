package com.mtesitoo.backend.service;

import android.net.Uri;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mtesitoo.backend.model.Order;
import com.mtesitoo.backend.model.Product;
import com.mtesitoo.backend.service.logic.ICallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 26-04-2016.
 */
public class OrderResponse implements Response.Listener<String>, Response.ErrorListener {

    private ICallback<List<Order>> mCallback;

    public OrderResponse(ICallback<List<Order>> callback) {
        mCallback = callback;
    }

    @Override
    public void onResponse(String response) {
        try {
            List<Order> orders = parseResponse(response);

            if (mCallback != null)
                mCallback.onResult(orders);
        } catch (JSONException e) {
            if (mCallback != null)
                mCallback.onError(e);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mCallback.onError(error);
    }

    public List<Order> parseResponse(String response) throws JSONException {
        JSONArray jsonOrders = new JSONArray(response);

        List<Order> result = new ArrayList<>(jsonOrders.length());
        for (int i = 0; i < jsonOrders.length(); ++i) {
            JSONObject jsonOrder = jsonOrders.getJSONObject(i);
            //TODO NAILY REMOVE BEFORE SUBMISSION
            Log.d("JSON definition file",jsonOrder.toString());
            //TODO NAILY replace new Date() by properly formatted date
            Order order =
                    new Order(
                            jsonOrder.getInt("order_id"),//Integer.parseInt(jsonOrder.getString("order_id")),
                            jsonOrder.getString("customer"),
                            "Delivery Address",
                            "Product Name",
                            jsonOrder.getString("status"),
                            jsonOrder.getDouble("total"),
                            "Product Price",
                            2,
                            new Date(),
                            "Payment Method"

                    );
            result.add(order);
        }

        return result;
    }
}
