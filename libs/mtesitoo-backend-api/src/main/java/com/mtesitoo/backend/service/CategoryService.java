package com.mtesitoo.backend.service;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mtesitoo.backend.R;
import com.mtesitoo.backend.model.AuthorizedStringRequest;
import com.mtesitoo.backend.model.Category;
import com.mtesitoo.backend.model.URL;
import com.mtesitoo.backend.model.header.Authorization;
import com.mtesitoo.backend.model.url.ProductCategoryURL;
import com.mtesitoo.backend.service.logic.ICategoryService;
import com.mtesitoo.backend.service.logic.ICategoryServiceResponse;
import com.mtesitoo.backend.service.logic.IResponse;

import org.json.JSONException;

import java.util.List;

/**
 * Created by Nan on 9/19/2015.
 */
public class CategoryService extends Service implements ICategoryService {
    private static final String TAG = ProductService.class.getSimpleName();
    private IResponse<List<Category>> mCallback;

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
                ICategoryServiceResponse categoryServiceResponse = new CategoryServiceResponse();
                List<Category> products = categoryServiceResponse.parseResponse(response);

                if (mCallback != null)
                    mCallback.onResult(products);
            } catch (JSONException e) {
                if (mCallback != null)
                    mCallback.onError(e);
            }
        }
    };

    public CategoryService(Context context) {
        super(context);
        mILoginService = new LoginService(mContext);
    }

    @Override
    public void getCategories(final IResponse<List<Category>> callback) {
        mCallback = callback;
        mILoginService.getAuthToken(new IResponse<String>() {
            @Override
            public void onResult(final String result) {
                URL url = new ProductCategoryURL(mContext, R.string.path_product_category);
                AuthorizedStringRequest stringRequest = new AuthorizedStringRequest(mContext, Request.Method.GET, url.toString(), listener, errorListener);
                stringRequest.setAuthorization(new Authorization(mContext, result).toString());
                mRequestQueue.add(stringRequest);
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);
            }
        });
    }
}