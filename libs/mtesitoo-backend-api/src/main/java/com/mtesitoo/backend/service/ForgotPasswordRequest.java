package com.mtesitoo.backend.service;

import android.content.Context;

import com.mtesitoo.backend.R;
import com.mtesitoo.backend.model.AuthorizedStringRequest;
import com.mtesitoo.backend.model.URL;
import com.mtesitoo.backend.model.header.Authorization;
import com.mtesitoo.backend.service.logic.ICallback;
import com.mtesitoo.backend.service.logic.IForgotPasswordRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pritya on 2/21/2017
 */
public class ForgotPasswordRequest extends Request implements IForgotPasswordRequest {
    public ForgotPasswordRequest(Context context) {
        super(context);
        //mILoginRequest = new LoginRequest(mContext);
    }

    @Override
    public void forgotPassword(final String username, final ICallback<String> callback) {
        URL url = new URL(mContext, R.string.path_admin_forgot_password);
        ForgotPasswordResponse response = new ForgotPasswordResponse(callback);
        AuthorizedStringRequest stringRequest = new AuthorizedStringRequest(mContext, com.android.volley.Request.Method.POST, url.toString(), response, response) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(mContext.getString(R.string.params_admin_username), username);
                return params;
            }
        };

        stringRequest.setAuthorization(new Authorization(mContext, mAuthorizationCache.getAuthorization()).toString());

        mRequestQueue.add(stringRequest);
    }
}