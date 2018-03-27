package com.mtesitoo.backend.service;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mtesitoo.backend.model.Unit;
import com.mtesitoo.backend.service.logic.ICallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nan on 1/9/2016.
 */
public class CommonResponse implements Response.Listener<String>, Response.ErrorListener {
    private ICallback<List<Unit>> mCallback;
    private String mType;

    public final static String TYPE_LENGTH = "length";
    public final static String TYPE_WEIGHT = "weight";
    public final static String TYPE_UNIT = "unit";

    public CommonResponse(ICallback<List<Unit>> callback, String type) {
        mType = type;
        mCallback = callback;
    }

    @Override
    public void onResponse(String response) {
        try {
            List<Unit> result = null;

            if (mType == TYPE_LENGTH) {
                result = parseLengthUnits(response);
            }

            if (mType == TYPE_WEIGHT) {
                result = parseWeightUnits(response);
            }

            if (mType == TYPE_UNIT) {
                result = parseUnits(response);
            }

            if (result == null) {
                result = new ArrayList<>();
            }

            if (mCallback != null) {
                mCallback.onResult(result);
            }
        } catch (JSONException e) {
            if (mCallback != null)
                mCallback.onError(e);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mCallback.onError(error);
    }

    private List<Unit> parseLengthUnits(String response) throws JSONException {
        JSONObject jsonResponse = new JSONObject(response);
        JSONArray jsonUnits = jsonResponse.getJSONArray("length_units");
        List<Unit> result = new ArrayList<>(jsonUnits.length());

        for (int i = 0; i < jsonUnits.length(); ++i) {
            JSONObject jsonUnit = jsonUnits.getJSONObject(i);
            Unit unit = new Unit(Integer.parseInt(
                    jsonUnit.getString("length_class_id")),
                    jsonUnit.getString("title"),
                    jsonUnit.getString("unit"));

            result.add(unit);
        }

        return result;
    }

    private List<Unit> parseWeightUnits(String response) throws JSONException {
        JSONObject jsonResponse = new JSONObject(response);
        JSONArray jsonUnits = jsonResponse.getJSONArray("weight_units");
        List<Unit> result = new ArrayList<>(jsonUnits.length());

        for (int i = 0; i < jsonUnits.length(); ++i) {
            JSONObject jsonUnit = jsonUnits.getJSONObject(i);
            Unit unit = new Unit(Integer.parseInt(
                    jsonUnit.getString("weight_class_id")),
                    jsonUnit.getString("title"),
                    jsonUnit.getString("unit"));

            result.add(unit);
        }

        return result;
    }

    private List<Unit> parseUnits(String response) throws JSONException {
        JSONObject jsonResponse = new JSONObject(response);
        JSONArray jsonUnits = jsonResponse.getJSONArray("units");
        List<Unit> result = new ArrayList<>(jsonUnits.length());

        for (int i = 0; i < jsonUnits.length(); ++i) {
            JSONObject jsonUnit = jsonUnits.getJSONObject(i);
            Unit unit = new Unit(Integer.parseInt(
                    jsonUnit.getString("unit_class_id")),
                    jsonUnit.getString("title"),
                    jsonUnit.getString("abbreviation"),
                    Integer.parseInt(jsonUnit.getString("sort_order")));

            result.add(unit);
        }

        return result;
    }
}