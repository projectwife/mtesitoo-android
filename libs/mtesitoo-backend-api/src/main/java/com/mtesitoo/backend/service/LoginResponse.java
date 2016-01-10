package com.mtesitoo.backend.service;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mtesitoo.backend.service.logic.ICallback;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nan on 9/7/2015.
 */
public class LoginResponse implements Response.Listener<String>, Response.ErrorListener {
    private ICallback<String> mCallback;
    private String mType;

    public final static String TYPE_TOKEN = "token";
    public final static String TYPE_AUTHENTICATE = "authenticate";

    public LoginResponse(ICallback<String> callback, String type) {
        mType = type;
        mCallback = callback;
    }

    @Override
    public void onResponse(String response) {
        try {
            String result = "";

            if (mType == TYPE_AUTHENTICATE) {
                result = parseResponse(response);
            }

            if (mType == TYPE_TOKEN) {
                result = parseToken(response);
            }

            if (mCallback != null)
                mCallback.onResult(result);
        } catch (JSONException e) {
            if (mCallback != null)
                mCallback.onError(e);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mCallback.onError(error);
    }

    private String parseToken(String response) throws JSONException {
        JSONObject jsonResponse = new JSONObject(response);
        return jsonResponse.getString("access_token");
    }

    private String parseResponse(String response) throws JSONException {
        JSONObject jsonResponse = new JSONObject(response);
        JSONObject jsonUserObject = jsonResponse.getJSONObject("user");
        return jsonUserObject.getString("vendor_id");
    }
}