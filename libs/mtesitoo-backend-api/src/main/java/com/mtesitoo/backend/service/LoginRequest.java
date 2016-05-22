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
        System.out.println("username---"+username);
        URL url = new URL(mContext, R.string.path_admin_login);
        System.out.println("url---"+url);
        LoginResponse response = new LoginResponse(callback, LoginResponse.TYPE_AUTHENTICATE);
        System.out.println("response11---"+response);
        AuthorizedStringRequest stringRequest = new AuthorizedStringRequest(mContext, com.android.volley.Request.Method.POST, url.toString(), response, response) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(mContext.getString(R.string.params_admin_username), username);
                params.put(mContext.getString(R.string.params_admin_password), password);
                System.out.println("params---" + params);
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                if (response.headers.containsKey(mContext.getString(R.string.header_set_cookie))) {
                    System.out.println("pppp"+response);
                    ISessionCache cache = new SessionCache(mContext);
                    cache.storeSession(response.headers.get(mContext.getString(R.string.header_set_cookie)));
                }
                return super.parseNetworkResponse(response);
            }
        };

        stringRequest.setAuthorization(new Authorization(mContext, mAuthorizationCache.getAuthorization()).toString());

        mRequestQueue.add(stringRequest);
    }

    public void getAuthToken(final ICallback<String> callback) {System.out.println(" in authToken---");
        //TODO: Add support for HTTPS requests
        URL url = new URL(mContext, R.string.path_oauth2_token);
        System.out.println("authtoken url---"+url);
        LoginResponse response = new LoginResponse(callback, LoginResponse.TYPE_TOKEN);
        AuthorizedStringRequest stringRequest = new AuthorizedStringRequest(mContext, com.android.volley.Request.Method.POST, url.toString(), response, response);
        stringRequest.setAuthorization(new Authorization(mContext, null).toString());
        mRequestQueue.add(stringRequest);
        System.out.println("stringRequest---" + stringRequest);
    }
}