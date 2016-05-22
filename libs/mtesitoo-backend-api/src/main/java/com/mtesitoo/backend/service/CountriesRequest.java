package com.mtesitoo.backend.service;

import android.content.Context;

import com.mtesitoo.backend.R;
import com.mtesitoo.backend.model.AuthorizedStringRequest;

import com.mtesitoo.backend.model.Countries;
import com.mtesitoo.backend.model.URL;
import com.mtesitoo.backend.model.header.Authorization;
import com.mtesitoo.backend.service.logic.ICallback;
import com.mtesitoo.backend.service.logic.ICountriesRequest;

import java.util.List;

/**
 * Created by Administrator on 2016/5/11 0011.
 */
public class CountriesRequest  extends Request implements ICountriesRequest {


    public CountriesRequest(Context context) {
        super(context);
        mILoginRequest = new LoginRequest(mContext);
    }


    @Override
    public void getCountries(final ICallback<List<Countries>> callback) {
        URL url = new URL(mContext, R.string.path_common_country);
        CountriesResponse response = new CountriesResponse(callback);
        AuthorizedStringRequest stringRequest = new AuthorizedStringRequest(mContext, com.android.volley.Request.Method.GET, url.toString(), response, response);

        stringRequest.setAuthorization(new Authorization(mContext, mAuthorizationCache.getAuthorization()).toString());
        mRequestQueue.add(stringRequest);
    }
}
