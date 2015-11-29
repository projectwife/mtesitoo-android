package com.mtesitoo.backend.service;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.mtesitoo.backend.R;
import com.mtesitoo.backend.model.header.Authorization;
import com.mtesitoo.backend.model.AuthorizedStringRequest;
import com.mtesitoo.backend.model.URL;
import com.mtesitoo.backend.service.logic.ILoginServiceResponse;
import com.mtesitoo.backend.service.logic.IResponse;
import com.mtesitoo.backend.service.logic.ILoginService;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * Api-based implementation of {@link ILoginService}
 */
public class LoginService extends Service implements ILoginService {
    /**
     * Caching oauth tokens for the lifetime of the app, since we set OpenCart to never expire tokens.
     */
    private String mOauthToken;
    private IResponse<String> mCallback;

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            mCallback.onError(error);
        }
    };

    private Response.Listener tokenListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                ILoginServiceResponse loginServiceResponse = new LoginServiceResponse();
                String oauthToken = loginServiceResponse.parseToken(response);
                LoginService.this.mOauthToken = oauthToken;

                if (mCallback != null)
                    mCallback.onResult(oauthToken);
            } catch (JSONException e) {
                if (mCallback != null)
                    mCallback.onError(e);
            }
        }
    };

    private Response.Listener authenticationListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                ILoginServiceResponse loginServiceResponse = new LoginServiceResponse();
                String vendorId = loginServiceResponse.parseResponse(response);

                if (mCallback != null)
                    mCallback.onResult(vendorId);
            } catch (JSONException e) {
                if (mCallback != null)
                    mCallback.onError(e);
            }
        }
    };

    public LoginService(Context context) {
        super(context);
    }

    public void authenticateUser(final String username, final String password, final IResponse<String> callback) {
        mCallback = callback;
        URL url = new URL(mContext, R.string.path_admin_login);

        AuthorizedStringRequest stringRequest = new AuthorizedStringRequest(mContext, Request.Method.POST, url.toString(), authenticationListener, errorListener) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(mContext.getString(R.string.params_admin_username), username);
                params.put(mContext.getString(R.string.params_admin_password), password);
                return params;
            }
        };

        stringRequest.setAuthorization(new Authorization(mContext, mAuthorizationCache.getAuthorization()).toString());
        mRequestQueue.add(stringRequest);
    }

    public void getAuthToken(final IResponse<String> callback) {
        mCallback = callback;

        if (mOauthToken != null) {
            mCallback.onResult(mOauthToken);
            return;
        }

        //TODO: Add support for HTTPS requests
        URL url = new URL(mContext, R.string.path_oauth2_token);
        AuthorizedStringRequest stringRequest = new AuthorizedStringRequest(mContext, Request.Method.POST, url.toString(), tokenListener, errorListener);
        stringRequest.setAuthorization(new Authorization(mContext, null).toString());
        mRequestQueue.add(stringRequest);
    }
}