package com.mtesitoo.backend.service;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mtesitoo.backend.model.Seller;
import com.mtesitoo.backend.service.logic.ICallback;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nan on 9/13/2015.
 */
public class SellerResponse implements Response.Listener<String>, Response.ErrorListener {
    private ICallback<Seller> mCallback;

    public SellerResponse(ICallback<Seller> callback) {
        mCallback = callback;
    }

    @Override
    public void onResponse(String response) {
        try {
            Seller seller = parseResponse(response);
            if (mCallback != null)
                mCallback.onResult(seller);
        } catch (JSONException e) {
            if (mCallback != null){
                Log.e("Seller Parsing", response);
                mCallback.onError(e);
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mCallback.onError(error);
    }

    private Seller parseResponse(String response) throws JSONException {
        JSONObject jsonResponse = new JSONObject(response);
        JSONObject jsonSellerObject = jsonResponse.getJSONObject("vendor");
        JSONObject jsonSellerAddressObject = jsonSellerObject.getJSONObject("address");
        JSONObject jsonSellerAddressCountryObject = jsonSellerAddressObject.getJSONObject("country");

        return new Seller(Integer.parseInt(
                jsonSellerObject.getString("vendor_id")),
                jsonSellerObject.getString("username"),
                jsonSellerObject.getString("firstname"),
                jsonSellerObject.getString("lastname"),
                jsonSellerObject.getString("telephone"),
                jsonSellerObject.getString("email"),
                jsonSellerObject.getString("company"),
                jsonSellerAddressObject.getString("address_1"),
                jsonSellerAddressObject.getString("city"),
                jsonSellerAddressObject.getString("postcode"),
                jsonSellerAddressCountryObject.getString("name"),
                "URI");
    }
}