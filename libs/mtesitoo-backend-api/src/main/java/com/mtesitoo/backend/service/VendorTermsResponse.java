package com.mtesitoo.backend.service;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mtesitoo.backend.service.logic.ICallback;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ptyagi on 6/4/17.
 */

public class VendorTermsResponse implements Response.Listener<String>, Response.ErrorListener {

    private ICallback<String> mCallback;

    public VendorTermsResponse(ICallback<String> callback) {
        mCallback = callback;
    }
    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(String response) {
        try {
            JSONObject vendorTerms = parseResponse(response);

            if (mCallback != null) {
                mCallback.onResult(vendorTerms.getString("vendor_terms"));
            }
        } catch (JSONException e) {
            if (mCallback != null)
                mCallback.onError(e);
        }
    }

    private JSONObject parseResponse(String response) throws JSONException {
        JSONObject jsonResponse = new JSONObject(response);


        return jsonResponse;
    }
}

