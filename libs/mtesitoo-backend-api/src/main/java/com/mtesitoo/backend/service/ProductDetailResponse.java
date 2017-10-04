package com.mtesitoo.backend.service;

import android.net.Uri;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mtesitoo.backend.model.Product;
import com.mtesitoo.backend.service.logic.ICallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        jsonProduct = (JSONObject) jsonProduct.get("product");

        Product result = null;
        try {
            String expirationStr = null;
            Date expirationDate = null;

            if (jsonProduct.has("expiration_date")) {
                expirationStr = jsonProduct.getString("expiration_date");
            }

            if (expirationStr == null || expirationStr.equals("null")
                    || expirationStr.equals("0000-00-00 00:00:00")) {
                expirationDate = null;
            } else {
                expirationDate = formatter.parse(expirationStr);
            }

            String displayPrice = "";
            String currencyCode = "";
            if (jsonProduct.has("display_price")) {
                displayPrice = jsonProduct.getString("display_price");
            }

            if (jsonProduct.has("currency_code")) {
                currencyCode = jsonProduct.getString("currency_code");
            }

            int numPendingOrders = 0, numProcessingOrders = 0;
            if (jsonProduct.get("order_counts") instanceof JSONObject) {
                JSONObject jsonObject = jsonProduct.getJSONObject("order_counts");
                if (jsonObject.has("Pending")) {
                    numPendingOrders = jsonObject.getInt("Pending");
                }

                if (jsonObject.has("Processing")) {
                    numProcessingOrders = jsonObject.getInt("Processing");
                }
            }

            result = new Product(
                    Integer.parseInt(jsonProduct.getString("product_id")),
                    jsonProduct.getString("title"),
                    jsonProduct.getString("description"),
                    jsonProduct.getString("location"),
                    resolveCategories(jsonProduct.getJSONArray("categories")),
                    "SI Unit",
                    jsonProduct.getString("price"),
                    displayPrice,
                    currencyCode,
                    jsonProduct.getInt("quantity"),
                    expirationDate,
                    Uri.parse(jsonProduct.getString("thumb_image")),
                    parseAuxImages(jsonProduct.getJSONArray("images")),
                    jsonProduct.getInt("status"), numPendingOrders, numProcessingOrders
            );
        } catch (ParseException e) {
            e.printStackTrace();
        }

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

    private ArrayList<String> resolveCategories(JSONArray categoryList) {

        JSONArray arr = categoryList;
        ArrayList<String> list = new ArrayList<>();
        for(int i = 0; i < arr.length(); i++){
            try {
                list.add(arr.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return list;

    }
}