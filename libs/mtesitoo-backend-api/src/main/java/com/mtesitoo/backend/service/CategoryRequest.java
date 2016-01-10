package com.mtesitoo.backend.service;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mtesitoo.backend.R;
import com.mtesitoo.backend.model.AuthorizedStringRequest;
import com.mtesitoo.backend.model.Category;
import com.mtesitoo.backend.model.URL;
import com.mtesitoo.backend.model.header.Authorization;
import com.mtesitoo.backend.service.logic.ICategoryRequest;
import com.mtesitoo.backend.service.logic.ICategoryServiceResponse;
import com.mtesitoo.backend.service.logic.ICallback;

import org.json.JSONException;

import java.util.List;

/**
 * Created by Nan on 9/19/2015.
 */
public class CategoryRequest extends Request implements ICategoryRequest {
    private ICallback<List<Category>> mCallback;
    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            mCallback.onError(error);
        }
    };

    private Response.Listener listener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                ICategoryServiceResponse categoryServiceResponse = new CategoryResponse();
                List<Category> products = categoryServiceResponse.parseResponse(response);

                if (mCallback != null)
                    mCallback.onResult(products);
            } catch (JSONException e) {
                if (mCallback != null)
                    mCallback.onError(e);
            }
        }
    };

    public CategoryRequest(Context context) {
        super(context);
        mILoginRequest = new LoginRequest(mContext);
    }

    @Override
    public void getCategories(final ICallback<List<Category>> callback) {
        mCallback = callback;
        URL url = new URL(mContext, R.string.path_product_category);
        AuthorizedStringRequest stringRequest = new AuthorizedStringRequest(mContext, com.android.volley.Request.Method.GET, url.toString(), listener, errorListener);

        stringRequest.setAuthorization(new Authorization(mContext, mAuthorizationCache.getAuthorization()).toString());
        mRequestQueue.add(stringRequest);
    }
}