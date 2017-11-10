package com.mtesitoo.backend.service;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mtesitoo.backend.service.logic.ICallback;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Carl on 9/11/2016.
 */
public class ProductThumbnailResponse implements Response.Listener, Response.ErrorListener {
    private ICallback<String> mCallback;

    public ProductThumbnailResponse(ICallback<String> callback) {
        mCallback = callback;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mCallback.onError(error);
    }

    public String parseResponse(String response) throws JSONException {
        JSONObject jsonProduct = new JSONObject(response);
        String result = "";
        if (jsonProduct.has("product_id")) {
            result = jsonProduct.getString("product_id");
        } else if (jsonProduct.has("filename")) {
            result = jsonProduct.getString("filename");
        }
        return result;
    }

    @Override
    public void onResponse(Object response) {

        if (response instanceof String) {
            try {
                String productId = parseResponse((String) response);

                if (mCallback != null)
                    mCallback.onResult(productId);
            } catch (JSONException e) {
                if (mCallback != null)
                    mCallback.onError(e);
            }
        } else if (response instanceof NetworkResponse) {
            NetworkResponse networkResponse = (NetworkResponse) response;
            if (networkResponse.statusCode == 200)
                mCallback.onResult(null);
            else
                mCallback.onError(new Exception(networkResponse.toString()));
        } else {
            Log.d("PRODUCT_UPDATE", response.toString());
        }
    }
}