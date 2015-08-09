package com.mtesitoo.backend.api;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mtesitoo.backend.C;
import com.mtesitoo.backend.logic.LoginService;
import com.mtesitoo.backend.logic.ProductService;
import com.mtesitoo.backend.model.Product;
import com.mtesitoo.backend.model.Seller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Opencart API-based implementation of {@link ProductService}.
 *
 * @author danieldanciu
 */
public class ApiProductService implements ProductService {
    private static final String TAG = ApiProductService.class.getSimpleName();
    private final RequestQueue queue;
    private final LoginService loginService;

    public ApiProductService(Context context) {
        queue = Volley.newRequestQueue(context);
        loginService = new ApiLoginService(context);
    }

    @Override
    public void getSpecials(final Callback<List<Product>> callback) {
        loginService.getAuthToken(new Callback<String>() {
            @Override
            public void onResult(final String result) {
                getSpecialsWithToken(result, callback);
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);
            }
        });
    }

    private void getSpecialsWithToken(final String oauthToken, final Callback<List<Product>> callback) {
        String url = C.api.server + C.api.special_path;

        // Request a string response from the provided URL.
        StringRequest stringRequest =
                new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response is: " + response);
                        JSONObject jsonResponse = null;
                        try {
                            jsonResponse = new JSONObject(response);
                            JSONArray jsonProducts = jsonResponse.getJSONArray("products");
                            List<Product> result = new ArrayList<>(jsonProducts.length());
                            for (int i = 0; i < jsonProducts.length(); ++i) {
                                JSONObject jsonProduct = jsonProducts.getJSONObject(i);

                                Product product =
                                        new Product(jsonProduct.getString("name"), jsonProduct.getString("description"),
                                                "Location", "Category", "SI Unit",
                                                jsonProduct.getString("price"), 100,
                                                new Date(), Uri.parse(jsonProduct.getString("thumb_image")),
                                                null /* special price */, Seller.DUMMY);
                                result.add(product);
                            }
                            callback.onResult(result);
                        } catch (JSONException e) {
                            callback.onError(e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error retrieving special offers: " + error);
                        callback.onError(error);
                    }
                }) {
                    @Override
                    public HashMap<String, String> getHeaders() {
                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("Authorization", "Bearer " + oauthToken);
                        params.put("Accept", "application/json; charset=utf-8");
                        Log.d(TAG, "Headers are: " + params);
                        return params;
                    }

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("grant_type", "client_credentials");
                        return params;
                    }

                };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}