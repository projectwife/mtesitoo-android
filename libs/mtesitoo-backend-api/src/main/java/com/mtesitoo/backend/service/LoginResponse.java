package com.mtesitoo.backend.service;

import android.util.Log;

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
    public final static String TYPE_REGISTER_FIREBASE_TOKEN = "register_firebase_token";

    public LoginResponse(ICallback<String> callback, String type) {
        mType = type;
        mCallback = callback;
    }

    @Override
    public void onResponse(String response) {
        try {
            String result = "";
            if (mType.equals(TYPE_AUTHENTICATE)) {
                result = parseResponse(response);
            }

            if (mType.equals(TYPE_TOKEN)) {
                result = parseToken(response);
            }

            if (mType.equals(TYPE_REGISTER_FIREBASE_TOKEN)) {
                result = response;
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
        Log.e("NETWORK ERROR", "Empty response from server: " + error.getMessage());
        if (mCallback != null) {
            mCallback.onError(error);
        }
    }

    private String parseToken(String response) throws JSONException {
        JSONObject jsonResponse = new JSONObject(response);
        Log.d("NETWORK - ACCESS TOKEN", jsonResponse.getString("access_token"));
        return jsonResponse.getString("access_token");
    }

    private String parseResponse(String response) throws JSONException {
        JSONObject jsonResponse = new JSONObject(response);
        JSONObject jsonUserObject;
        try{
            jsonUserObject = jsonResponse.getJSONObject("user");
            Log.d("NETWORK - VENDOR ID", jsonUserObject.getString("vendor_id"));
        }catch(Exception e){
            String errorMessage = jsonResponse.getJSONObject("errors").getString("message");
            throw new JSONException(errorMessage);
        }

        return jsonUserObject.getString("vendor_id");
    }
}