package com.mtesitoo.backend.service;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mtesitoo.backend.service.logic.ICallback;


/**
 * Created by Priyanka on 1/24/2017
 */
public class ProfileThumbnailResponse implements Response.Listener, Response.ErrorListener {
    private ICallback<String> mCallback;

    public ProfileThumbnailResponse(ICallback<String> callback) {
        mCallback = callback;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mCallback.onError(error);
    }

    @Override
    public void onResponse(Object response) {

        if(response instanceof String){
            if (mCallback != null)
                mCallback.onResult(response.toString());
        } else if(response instanceof NetworkResponse){
            NetworkResponse networkResponse = (NetworkResponse)response;
            if(networkResponse.statusCode==200)
                mCallback.onResult(null);
            else
                mCallback.onError(new Exception(networkResponse.toString()));
        } else{
            Log.d("PROFILE_IMAGE_UPDATE",response.toString());
        }
    }
}