package com.mtesitoo.backend.service.logic;

import org.json.JSONException;

/**
 * Created by Nan on 9/7/2015.
 */
public interface ILoginServiceResponse {
    String parseResponse(String response) throws JSONException;
}