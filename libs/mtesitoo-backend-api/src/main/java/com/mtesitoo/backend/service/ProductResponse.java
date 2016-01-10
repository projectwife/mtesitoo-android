package com.mtesitoo.backend.service;

import android.net.Uri;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mtesitoo.backend.model.Product;
import com.mtesitoo.backend.service.logic.ICallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Nan on 9/7/2015.
 */
public class ProductResponse implements Response.Listener<String>, Response.ErrorListener {
    private ICallback<List<Product>> mCallback;

    public ProductResponse(ICallback<List<Product>> callback) {
        mCallback = callback;
    }

    @Override
    public void onResponse(String response) {
        try {
            List<Product> products = parseResponse(response);

            if (mCallback != null)
                mCallback.onResult(products);
        } catch (JSONException e) {
            if (mCallback != null)
                mCallback.onError(e);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mCallback.onError(error);
    }

    public List<Product> parseResponse(String response) throws JSONException {
        JSONArray jsonProducts = new JSONArray(response);

        List<Product> result = new ArrayList<>(jsonProducts.length());
        for (int i = 0; i < jsonProducts.length(); ++i) {
            JSONObject jsonProduct = jsonProducts.getJSONObject(i);
            Product product =
                    new Product(
                            Integer.parseInt(jsonProduct.getString("product_id")),
                            jsonProduct.getString("name"),
                            jsonProduct.getString("description"),
                            "Location",
                            "Category",
                            "SI Unit",
                            jsonProduct.getString("price"), 100,
                            new Date(),
                            Uri.parse(jsonProduct.getString("thumb_image"))
                    );
            result.add(product);
        }

        return result;
    }
}