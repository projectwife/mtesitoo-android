package com.mtesitoo.backend.service;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mtesitoo.backend.service.logic.ICallback;

public class ForgotPasswordResponse implements Response.Listener<String>, Response.ErrorListener {
    private ICallback<String> mCallback;

    public ForgotPasswordResponse(ICallback<String> callback) {
        mCallback = callback;
    }

    @Override
    public void onResponse(String response) {

        try {
            // We dont need to parse the response, just report back that it worked
            mCallback.onResult(null);
        } catch (Exception e) {
            mCallback.onError(e);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mCallback.onError(error);
    }
}