package com.mtesitoo.backend.cache;

import android.content.Context;

import com.mtesitoo.backend.R;
import com.mtesitoo.backend.cache.logic.IAuthorizationCache;

/**
 * Created by Nan on 10/11/2015.
 */
public class AuthorizationCache extends Cache implements IAuthorizationCache {
    public AuthorizationCache(Context context) {
        super(context);
    }

    public void storeAuthorization(String authoriation) {
        mEditor.putString(mContext.getString(R.string.shared_preference_key_authorization), authoriation);
        mEditor.apply();
    }

    public String getAuthorization() {
        return mPrefs.getString(mContext.getString(R.string.shared_preference_key_authorization), null);
    }
}