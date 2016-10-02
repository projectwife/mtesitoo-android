package com.mtesitoo.backend.service;

import android.net.Uri;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mtesitoo.backend.model.Product;
import com.mtesitoo.backend.service.logic.ICallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Carl on 9/11/2016.
 */
public class ProductImageDeleteResponse implements Response.Listener<String>, Response.ErrorListener {
    private ICallback<Product> mCallback;

    public ProductImageDeleteResponse(ICallback<Product> callback) {
        mCallback = callback;
    }

    @Override
    public void onResponse(String response) {
        mCallback.onResult(null);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mCallback.onError(error);
    }

}