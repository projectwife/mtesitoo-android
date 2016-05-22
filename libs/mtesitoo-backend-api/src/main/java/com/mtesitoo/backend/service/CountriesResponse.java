package com.mtesitoo.backend.service;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.mtesitoo.backend.model.Countries;
import com.mtesitoo.backend.service.logic.ICallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/11 0011.
 */
public class CountriesResponse implements Response.Listener<String>, Response.ErrorListener {

    private ICallback<List<Countries>> mCallback;

    public CountriesResponse(ICallback<List<Countries>> callback) {
        mCallback = callback;
    }
    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(String response) {
        try {
            List<Countries> countries = parseResponse(response);

            if (mCallback != null)
                mCallback.onResult(countries);
        } catch (JSONException e) {
            if (mCallback != null)
                mCallback.onError(e);
        }
    }

    private List<Countries> parseResponse(String response) throws JSONException {
        JSONObject jsonResponse = new JSONObject(response);
        JSONArray jsonCountries = jsonResponse.getJSONArray("countries");
        List<Countries> result = new ArrayList<>(jsonCountries.length());

        for (int i = 0; i < jsonCountries.length(); ++i) {
            JSONObject jsonCategory = jsonCountries.getJSONObject(i);
            Countries countries = new Countries(Integer.parseInt(
                    jsonCategory.getString("country_id")),
                    jsonCategory.getString("name"));

            result.add(countries);
        }

        return result;
    }
}
