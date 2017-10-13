package com.mtesitoo.backend.service;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mtesitoo.backend.service.logic.ICallback;


/**
 * Created by Priyanka on 1/24/2017
 */
class ProfileThumbnailResponse implements Response.Listener<String>, Response.ErrorListener {
    private ICallback<String> mCallback;

    ProfileThumbnailResponse(ICallback<String> callback) {
        mCallback = callback;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mCallback.onError(error);
    }

    @Override
    public void onResponse(String response) {
        if (response != null && !response.isEmpty()) {
            if (mCallback != null)
                mCallback.onResult(response);
        } else {
            mCallback.onError(new Exception("Empty or null response"));
        }
    }
}