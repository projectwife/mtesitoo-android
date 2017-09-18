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
        } catch (ParseException e) {
            if (mCallback != null)
                mCallback.onError(e);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mCallback.onError(error);
    }

    public List<Product> parseResponse(String response) throws JSONException, ParseException {
        //handling empty response from server
        if (response.isEmpty()) {
            return new ArrayList<>();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        JSONArray jsonProducts = new JSONArray(response);

        List<Product> result = new ArrayList<>(jsonProducts.length());
        for (int i = 0; i < jsonProducts.length(); ++i) {
            JSONObject jsonProduct = jsonProducts.getJSONObject(i);

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
            Product product =
                    new Product(
                            Integer.parseInt(jsonProduct.getString("product_id")),
                            jsonProduct.getString("name"),
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
                            parseAuxImages(),
                            jsonProduct.getInt("status")
                    );
            result.add(product);
        }

        return result;
    }

    private ArrayList<Uri> parseAuxImages(){
        ArrayList<Uri> images = new ArrayList<>();
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