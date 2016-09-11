package com.mtesitoo.backend.service;

import android.net.Uri;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mtesitoo.backend.model.Product;
import com.mtesitoo.backend.service.logic.ICallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Nan on 9/7/2015.
 */
public class ProductUpdateResponse implements Response.Listener, Response.ErrorListener {
    private ICallback<Product> mCallback;

    public ProductUpdateResponse(ICallback<Product> callback) {
        mCallback = callback;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mCallback.onError(error);
    }

    public Product parseResponse(String response) throws JSONException {
        JSONObject jsonProduct = new JSONObject(response);

        Product result = new Product(
                Integer.parseInt(jsonProduct.getString("product_id")),
                jsonProduct.getString("name"),
                jsonProduct.getString("description"),
                "Location",
                "Category",
                "SI Unit",
                jsonProduct.getString("price"), 100,
                new Date(),
                Uri.parse(jsonProduct.getString("thumb_image")),
                null
        );

        return result;
    }

    @Override
    public void onResponse(Object response) {
        if(response instanceof String){
            try {
                Product product = parseResponse((String)response);

                if (mCallback != null)
                    mCallback.onResult(product);
            } catch (JSONException e) {
                if (mCallback != null)
                    mCallback.onError(e);
            }
        }else if(response instanceof NetworkResponse){
            NetworkResponse networkResponse = (NetworkResponse)response;
            if(networkResponse.statusCode==200)
                mCallback.onResult(null);
            else
                mCallback.onError(new Exception(networkResponse.toString()));
        }else{
            Log.d("PRODUCT_UPDATE",response.toString());
        }
    }
}