package com.mtesitoo.backend.service;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.mtesitoo.backend.model.Seller;
import com.mtesitoo.backend.service.logic.ICallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RegistrationResponse implements Response.Listener<String>, Response.ErrorListener {
    private ICallback<Seller> mCallback;

    public RegistrationResponse(ICallback<Seller> callback) {
        mCallback = callback;
    }

    @Override
    public void onResponse(String response) {

        try {
            // We dont need to parse the response, just report back that it worked
            mCallback.onResult(null);
        } catch (Exception e) {
            Log.e("RegisteredUser", e.toString());
            mCallback.onError(e);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mCallback.onError(error);
    }
}