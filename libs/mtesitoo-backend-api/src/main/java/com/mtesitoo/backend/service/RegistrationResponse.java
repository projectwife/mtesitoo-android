package com.mtesitoo.backend.service;

import android.net.Uri;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.mtesitoo.backend.model.Product;
import com.mtesitoo.backend.model.Seller;
import com.mtesitoo.backend.service.logic.ICallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by Administrator on 2016/5/6 0006.
 */
public class RegistrationResponse implements Response.Listener<String>, Response.ErrorListener{
    private ICallback<List<Seller>> mCallback;

    public RegistrationResponse(ICallback<List<Seller>> callback) {
        mCallback = callback;
    }
    @Override
    public void onResponse(String response) {


    }
    @Override
    public void onErrorResponse(VolleyError error) {
        String error1 =  error.toString();
        System.out.println("i am the error here:"+error1);
        //mCallback.onError(error);
     //   mCallback.onError(error);
    }



    public List<Seller> parseResponse(String response) throws JSONException {
        JSONArray jsonProducts = new JSONArray(response);

        List<Seller> result = new ArrayList<>(jsonProducts.length());
        for (int i = 0; i < jsonProducts.length(); ++i) {
            JSONObject jsonProduct = jsonProducts.getJSONObject(i);
            Seller seller =
                    new Seller(Integer.parseInt(jsonProduct.getString("product_id")),
                            jsonProduct.getString("name"),
                            "1", "2",
                            "3", "4","3", "1", "1", "2",
                            "3", "4");
            result.add(seller);
        }

        return result;
    }
}
