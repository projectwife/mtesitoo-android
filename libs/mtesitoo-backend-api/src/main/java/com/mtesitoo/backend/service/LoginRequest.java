package com.mtesitoo.backend.service;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;

import com.mtesitoo.backend.R;
import com.mtesitoo.backend.cache.SessionCache;
import com.mtesitoo.backend.cache.logic.ISessionCache;
import com.mtesitoo.backend.model.header.Authorization;
import com.mtesitoo.backend.model.AuthorizedStringRequest;
import com.mtesitoo.backend.model.URL;
import com.mtesitoo.backend.service.logic.ICallback;
import com.mtesitoo.backend.service.logic.ILoginRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Api-based implementation of {@link ILoginRequest}
 */
public class LoginRequest extends Request implements ILoginRequest {
    public LoginRequest(Context context) {
        super(context);
    }

    public void authenticateUser(final String username, final String password, final ICallback<String> callback) {
        URL url = new URL(mContext, R.string.path_admin_login);
        LoginResponse response = new LoginResponse(callback, LoginResponse.TYPE_AUTHENTICATE);

        AuthorizedStringRequest stringRequest = new AuthorizedStringRequest(mContext, com.android.volley.Request.Method.POST, url.toString(), response, response) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(mContext.getString(R.string.params_admin_username), username);
                params.put(mContext.getString(R.string.params_admin_password), password);
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                if (response.headers.containsKey(mContext.getString(R.string.header_set_cookie))) {
                    ISessionCache cache = new SessionCache(mContext);
                    cache.storeSession(response.headers.get(mContext.getString(R.string.header_set_cookie)));
                }

                return super.parseNetworkResponse(response);
            }
        };

        stringRequest.setAuthorization(new Authorization(mContext, mAuthorizationCache.getAuthorization()).toString());
        mRequestQueue.add(stringRequest);
    }

    public void getAuthToken(final ICallback<String> callback) {
        //TODO: Add support for HTTPS requests
        URL url = new URL(mContext, R.string.path_oauth2_token);
        LoginResponse response = new LoginResponse(callback, LoginResponse.TYPE_TOKEN);
        AuthorizedStringRequest stringRequest = new AuthorizedStringRequest(mContext, com.android.volley.Request.Method.POST, url.toString(), response, response);
        stringRequest.setAuthorization(new Authorization(mContext, null).toString());
        mRequestQueue.add(stringRequest);
    }
}