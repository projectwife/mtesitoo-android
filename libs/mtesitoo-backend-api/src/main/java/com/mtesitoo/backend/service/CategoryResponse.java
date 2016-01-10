package com.mtesitoo.backend.service;

import com.android.volley.VolleyError;
import com.mtesitoo.backend.model.Category;
import com.android.volley.Response;
import com.mtesitoo.backend.service.logic.ICallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nan on 9/19/2015.
 */
public class CategoryResponse implements Response.Listener<String>, Response.ErrorListener {
    private ICallback<List<Category>> mCallback;

    public CategoryResponse(ICallback<List<Category>> callback) {
        mCallback = callback;
    }

    @Override
    public void onResponse(String response) {
        try {
            List<Category> products = parseResponse(response);

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

    private List<Category> parseResponse(String response) throws JSONException {
        JSONObject jsonResponse = new JSONObject(response);
        JSONArray jsonCategories = jsonResponse.getJSONArray("categories");
        List<Category> result = new ArrayList<>(jsonCategories.length());

        for (int i = 0; i < jsonCategories.length(); ++i) {
            JSONObject jsonCategory = jsonCategories.getJSONObject(i);
            Category category = new Category(Integer.parseInt(
                    jsonCategory.getString("category_id")),
                    jsonCategory.getString("name"));

            result.add(category);
        }

        return result;
    }
}