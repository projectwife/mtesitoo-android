package com.mtesitoo.backend.service;

import com.mtesitoo.backend.service.logic.ILoginServiceResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nan on 9/7/2015.
 */
public class LoginResponse implements ILoginServiceResponse {
    public String parseToken(String response) throws JSONException {
        JSONObject jsonResponse = new JSONObject(response);
        return jsonResponse.getString("access_token");
    }

    public String parseResponse(String response) throws JSONException {
        JSONObject jsonResponse = new JSONObject(response);
        JSONObject jsonUserObject = jsonResponse.getJSONObject("user");
        return jsonUserObject.getString("vendor_id");
    }
}
