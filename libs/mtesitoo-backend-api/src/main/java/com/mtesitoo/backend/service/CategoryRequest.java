package com.mtesitoo.backend.service;

import android.content.Context;

import com.mtesitoo.backend.R;
import com.mtesitoo.backend.model.AuthorizedStringRequest;
import com.mtesitoo.backend.model.Category;
import com.mtesitoo.backend.model.URL;
import com.mtesitoo.backend.model.header.Authorization;
import com.mtesitoo.backend.service.logic.ICategoryRequest;
import com.mtesitoo.backend.service.logic.ICallback;

import java.util.List;

/**
 * Created by Nan on 9/19/2015.
 */
public class CategoryRequest extends Request implements ICategoryRequest {
    public CategoryRequest(Context context) {
        super(context);
        mILoginRequest = new LoginRequest(mContext);
    }

    @Override
    public void getCategories(final ICallback<List<Category>> callback) {
        URL url = new URL(mContext, R.string.path_product_category);
        CategoryResponse response = new CategoryResponse(callback);
        AuthorizedStringRequest stringRequest = new AuthorizedStringRequest(mContext, com.android.volley.Request.Method.GET, url.toString(), response, response);

        stringRequest.setAuthorization(new Authorization(mContext, mAuthorizationCache.getAuthorization()).toString());
        mRequestQueue.add(stringRequest);
    }
}