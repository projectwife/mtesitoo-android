package com.mtesitoo.backend.service;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mtesitoo.backend.model.Zone;
import com.mtesitoo.backend.service.logic.ICallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/11 0011.
 */
public class ZoneResponse implements Response.Listener<String>, Response.ErrorListener {

    private ICallback<List<Zone>> mCallback;

    public ZoneResponse(ICallback<List<Zone>> callback) {
        mCallback = callback;
    }
    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(String response) {
        try {
           // System.out.println("I am testing Zone id here1 countries id2");
            //System.out.println("I am testing Zone id here1 countries id2 response1: "+response.toString());
            List<Zone> zones = parseResponse(response);

            if (mCallback != null)
                mCallback.onResult(zones);
        } catch (JSONException e) {
            if (mCallback != null)
                mCallback.onError(e);
        }
    }

    private List<Zone> parseResponse(String response) throws JSONException {
       // System.out.println("I am testing Zone id here1 countries id2 response: "+response.toString());
        JSONObject jsonResponse = new JSONObject(response);
        //System.out.println("I am testing Zone id here1 countries id2 response: "+response.toString());
        JSONObject jsonCountries=jsonResponse.getJSONObject("country");
        JSONArray jsonZones = jsonCountries.getJSONArray("zones");
       // System.out.println("I am testing Zone id here1 countries id2 JsonCountries: "+jsonZones+toString());
       // System.out.println("I am testing Zone id here1 countries id2 Zones: "+jsonZones+toString());
        List<Zone> result = new ArrayList<>(jsonZones.length());

        for (int i = 0; i < jsonZones.length(); ++i) {
            JSONObject jsonZone = jsonZones.getJSONObject(i);
            Zone zone = new Zone(Integer.parseInt(
                    jsonZone.getString("zone_id")),
                    jsonZone.getString("name"),"BU");
            //System.out.println("I am testing Zone id here1 countries id2 name: "+zone.getName()+"id"+zone.getName());

            result.add(zone);
        }
       // System.out.println("I am testing Zone id here1 countries id2 name: Size "+result.size());
        return result;
    }
}
