package com.mtesitoo.backend.service;

import android.net.Uri;

import com.mtesitoo.backend.model.Product;
import com.mtesitoo.backend.model.Seller;
import com.mtesitoo.backend.service.logic.IProductServiceResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Nan on 9/7/2015.
 */
public class ProductServiceResponse implements IProductServiceResponse {
    public List<Product> parseResponse(String response) throws JSONException {
        JSONObject jsonResponse = null;
        jsonResponse = new JSONObject(response);
        JSONArray jsonProducts = jsonResponse.getJSONArray("products");

        List<Product> result = new ArrayList<>(jsonProducts.length());
        for (int i = 0; i < jsonProducts.length(); ++i) {
            JSONObject jsonProduct = jsonProducts.getJSONObject(i);
            Product product =
                    new Product(
                            jsonProduct.getString("name"),
                            jsonProduct.getString("description"),
                            "Location",
                            "Category",
                            "SI Unit",
                            jsonProduct.getString("price"), 100,
                            new Date(),
                            Uri.parse(jsonProduct.getString("thumb_image")),
                            null,
                            Seller.DUMMY
                    );
            result.add(product);
        }

        return result;
    }
}
