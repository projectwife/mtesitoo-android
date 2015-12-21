package com.mtesitoo.backend.model;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.mtesitoo.backend.R;
import com.mtesitoo.backend.cache.SessionCache;
import com.mtesitoo.backend.cache.logic.ISessionCache;

import java.util.HashMap;

/**
 * Created by Nan on 9/13/2015.
 */
public class AuthorizedStringRequest extends StringRequest {
    private Context mContext;
    private String mAuthorization;

    public AuthorizedStringRequest(Context context, int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        mContext = context;
    }

    public void setAuthorization(String authorization) {
        mAuthorization = authorization;
    }

    @Override
    public HashMap<String, String> getHeaders() {
        HashMap<String, String> params = new HashMap<String, String>();

        ISessionCache cache = new SessionCache(mContext);
        if (cache.getSession() != null) {
            params.put(mContext.getString(R.string.header_cookie), cache.getSession());
        }

        params.put(mContext.getString(R.string.header_authorization), mAuthorization);
        params.put(mContext.getString(R.string.header_accept), mContext.getString(R.string.application_json));
        return params;
    }
}