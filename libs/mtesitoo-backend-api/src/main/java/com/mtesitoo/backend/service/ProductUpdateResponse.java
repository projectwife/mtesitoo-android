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
    private ICallback<String> mCallback;

    public ProductUpdateResponse(ICallback<String> callback) {
        mCallback = callback;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mCallback.onError(error);
    }

    @Override
    public void onResponse(Object response) {
        if(response instanceof String){
            mCallback.onResult(null);
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

    private ArrayList<Uri> parseAuxImages(){
        ArrayList<Uri> images = new ArrayList<>();
        return images;
    }
}