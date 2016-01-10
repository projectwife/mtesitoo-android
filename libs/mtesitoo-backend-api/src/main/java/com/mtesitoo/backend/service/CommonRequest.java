package com.mtesitoo.backend.service;

import android.content.Context;

import com.mtesitoo.backend.R;
import com.mtesitoo.backend.model.AuthorizedStringRequest;
import com.mtesitoo.backend.model.Unit;
import com.mtesitoo.backend.model.URL;
import com.mtesitoo.backend.model.header.Authorization;
import com.mtesitoo.backend.service.logic.ICallback;
import com.mtesitoo.backend.service.logic.ICommonRequest;

import java.util.List;

/**
 * Created by Nan on 1/9/2016.
 */
public class CommonRequest extends Request implements ICommonRequest {
    public CommonRequest(Context context) {
        super(context);
        mILoginRequest = new LoginRequest(mContext);
    }

    public void getLengthUnits(ICallback<List<Unit>> callback) {
        URL url = new URL(mContext, R.string.path_common_length);
        CommonResponse response = new CommonResponse(callback, CommonResponse.TYPE_LENGTH);
        AuthorizedStringRequest stringRequest = new AuthorizedStringRequest(mContext, com.android.volley.Request.Method.GET, url.toString(), response, response);

        stringRequest.setAuthorization(new Authorization(mContext, mAuthorizationCache.getAuthorization()).toString());
        mRequestQueue.add(stringRequest);
    }

    public void getWeightUnits(ICallback<List<Unit>> callback) {
        URL url = new URL(mContext, R.string.path_common_weight);
        CommonResponse response = new CommonResponse(callback, CommonResponse.TYPE_WEIGHT);
        AuthorizedStringRequest stringRequest = new AuthorizedStringRequest(mContext, com.android.volley.Request.Method.GET, url.toString(), response, response);

        stringRequest.setAuthorization(new Authorization(mContext, mAuthorizationCache.getAuthorization()).toString());
        mRequestQueue.add(stringRequest);
    }
}