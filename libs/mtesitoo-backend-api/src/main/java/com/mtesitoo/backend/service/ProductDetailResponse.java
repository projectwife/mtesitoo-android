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
 * Created by Carl on 9/11/2016.
 */
public class ProductDetailResponse implements Response.Listener<String>, Response.ErrorListener {
    private ICallback<Product> mCallback;

    public ProductDetailResponse(ICallback<Product> callback) {
        mCallback = callback;
    }

    @Override
    public void onResponse(String response) {
        try {
            Product product = parseResponse(response);

            if (mCallback != null)
                mCallback.onResult(product);
        } catch (JSONException e) {
            if (mCallback != null)
                mCallback.onError(e);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mCallback.onError(error);
    }

    public Product parseResponse(String response) throws JSONException {
        JSONObject jsonProduct = new JSONObject(response);
        jsonProduct = (JSONObject) jsonProduct.get("product");

        Product result = new Product(
                Integer.parseInt(jsonProduct.getString("product_id")),
                jsonProduct.getString("title"),
                jsonProduct.getString("description"),
                "Location",
                "Category",
                "SI Unit",
                jsonProduct.getString("price"), 100,
                new Date(),
                Uri.parse(jsonProduct.getString("thumb_image")),
                parseAuxImages(jsonProduct.getJSONArray("images"))
        );

        return result;
    }

    private ArrayList<Uri> parseAuxImages(JSONArray imageArray){
        ArrayList<Uri> images = new ArrayList<>();

        for (int i = 0; i < imageArray.length(); i++) {
            try {
                images.add(Uri.parse(((JSONObject)imageArray.get(i)).getString("image")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return images;
    }
}