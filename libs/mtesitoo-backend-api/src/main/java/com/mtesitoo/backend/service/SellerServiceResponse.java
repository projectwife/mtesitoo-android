package com.mtesitoo.backend.service;

import com.mtesitoo.backend.model.Seller;
import com.mtesitoo.backend.service.logic.ISellerServiceResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nan on 9/13/2015.
 */
public class SellerServiceResponse implements ISellerServiceResponse {
    public Seller parseResponse(String response) throws JSONException {
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
