package com.mtesitoo.backend.model.header;

import android.content.Context;
import android.util.Base64;

import com.mtesitoo.backend.R;

/**
 * Created by Nan on 9/13/2015.
 */
public class Authorization {
    private Context mContext;
    private String mAuthorization;

    public Authorization(Context context, String authorization) {
        mContext = context;
        mAuthorization = authorization;
    }

    @Override
    public String toString() {
        if (mAuthorization == null) {
            return getDefaultAuthorization();
        } else {
            return mContext.getString(R.string.bearer) + " " + mAuthorization;
        }
    }

    private String getDefaultAuthorization() {
        return mContext.getString(R.string.basic_auth) + " " + Base64.encodeToString(
                (mContext.getString(R.string.client_id) + ":" + mContext.getString(R.string.client_secret)).getBytes(),
                Base64.NO_WRAP
        );
    }
}