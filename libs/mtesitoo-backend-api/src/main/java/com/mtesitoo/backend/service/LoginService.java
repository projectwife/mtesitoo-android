package com.mtesitoo.backend.service;

import android.content.Context;
import android.util.Base64;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;

import com.mtesitoo.backend.R;
import com.mtesitoo.backend.service.logic.ILoginServiceResponse;
import com.mtesitoo.backend.service.logic.IResponse;
import com.mtesitoo.backend.service.logic.ILoginService;

import org.json.JSONException;

/**
 * Api-based implementation of {@link ILoginService}
 */
public class LoginService implements ILoginService {
    private final RequestQueue mRequestQueue;

    /**
     * Caching oauth tokens for the lifetime of the app, since we set OpenCart to never expire tokens.
     */
    private String mOauthToken;
    private Context mContext;
    private IResponse<String> mCallback;

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
                ILoginServiceResponse loginServiceResponse = new LoginServiceResponse();
                String oauthToken = loginServiceResponse.parseResponse(response);
                LoginService.this.mOauthToken = oauthToken;

                if (mCallback != null)
                    mCallback.onResult(oauthToken);
            } catch (JSONException e) {
                if (mCallback != null)
                    mCallback.onError(e);
            }
        }
    };

    public LoginService(Context context) {
        mContext = context;
        mRequestQueue = Volley.newRequestQueue(mContext);
    }

    public void getAuthToken(final IResponse<String> callback) {
        mCallback = callback;

        if (mOauthToken != null) {
            mCallback.onResult(mOauthToken);
            return;
        }

        //TODO: Add support for HTTPS requests
        String url = mContext.getString(R.string.server) + mContext.getString(R.string.path_oauth2_token);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, listener, errorListener) {
            @Override
            public HashMap<String, String> getHeaders() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(mContext.getString(R.string.header_authorization), getAuthorization());
                params.put(mContext.getString(R.string.header_accept), mContext.getString(R.string.application_json));
                return params;
            }
        };

        mRequestQueue.add(stringRequest);
    }

    private String getAuthorization() {
        return mContext.getString(R.string.basic_auth) + " " + Base64.encodeToString(
                (mContext.getString(R.string.client_id) + ":" + mContext.getString(R.string.client_secret)).getBytes(),
                Base64.NO_WRAP
        );
    }
}