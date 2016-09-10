package com.mtesitoo.backend.service;

import android.net.Uri;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mtesitoo.backend.model.Order;
import com.mtesitoo.backend.service.logic.ICallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 26-04-2016
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
        //Log.d("ARRAY response string", response);

        List<Order> result = new ArrayList<>(jsonOrders.length());
        for (int i = 0; i < jsonOrders.length(); ++i) {
            JSONObject jsonOrder = jsonOrders.getJSONObject(i);
            //TODO NAILY COMMENT OUT BEFORE SUBMISSION
            //Log.d("JSON definition file",jsonOrder.toString());
            Order order =
                    new Order(
                            jsonOrder.getInt("order_id"),
                            jsonOrder.getString("customer"),
                            "Delivery Address",
                            jsonOrder.getString("status"),
                            jsonOrder.getDouble("total"),
                            FormatJsonDate(jsonOrder.getString("date_added")),
                            "Payment Method"
                    );
            result.add(order);
        }

        return result;
    }

    private static Date FormatJsonDate(String dateString)
    {
        // Represents the current date format as specified by the string in the json definition file
        // e.g. "date_added":"2016-05-21 22:32:25"
        String dateStringFormat = "yyyy-MM-dd hh:mm:sss";

        Date date;

        try
        {
            date = new SimpleDateFormat(dateStringFormat).parse(dateString);
        }
        catch (java.text.ParseException e)
        {
            // If exception fires, make sure that the format specified by dateStringFormat matches
            // the format in the json definition file.
            // At time of writing (Jul-2016), this is what we get back: "date_added":"2016-05-08 22:41:05"
            Log.e("FormatHelper", "Error - " + e.getMessage());
            date = new Date();
        }

        return date;
    }
}
