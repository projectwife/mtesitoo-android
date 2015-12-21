package com.mtesitoo.backend.cache;

import android.content.Context;

import com.mtesitoo.backend.R;
import com.mtesitoo.backend.cache.logic.ISessionCache;

/**
 * Created by Nan on 12/19/2015.
 */
public class SessionCache extends Cache implements ISessionCache {
    public SessionCache(Context context) {
        super(context);
    }

    public void storeSession(String sessionKey) {
        mEditor.putString(mContext.getString(R.string.shared_preference_key_session), sessionKey);
        mEditor.apply();

    }

    public String getSession() {
        return mPrefs.getString(mContext.getString(R.string.shared_preference_key_session), null);
    }
}
