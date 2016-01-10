package com.mtesitoo.backend.service;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.mtesitoo.backend.cache.AuthorizationCache;
import com.mtesitoo.backend.cache.logic.IAuthorizationCache;
import com.mtesitoo.backend.service.logic.ILoginRequest;

/**
 * Created by Nan on 9/13/2015.
 */
public abstract class Request {
    protected Context mContext;
    protected RequestQueue mRequestQueue;
    protected ILoginRequest mILoginRequest;
    protected IAuthorizationCache mAuthorizationCache;

    public Request(Context context) {
        mContext = context;
        mRequestQueue = Volley.newRequestQueue(mContext);
        mAuthorizationCache = new AuthorizationCache(mContext);
    }
}