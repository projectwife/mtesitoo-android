package com.mtesitoo.backend.service;

import com.mtesitoo.backend.service.logic.ILoginServiceResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nan on 9/7/2015.
 */
public class LoginServiceResponse implements ILoginServiceResponse {
    public String parseResponse(String response) throws JSONException {
        JSONObject jsonResponse = null;
        jsonResponse = new JSONObject(response);
        return jsonResponse.getString("access_token");
    }
}
