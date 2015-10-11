package com.mtesitoo.backend.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Nan on 10/3/2015.
 */

//TODO: Redesign the local cache, this is only a temporary implementation for storing the categories
public abstract class Cache {
    protected Context mContext;
    protected SharedPreferences.Editor mEditor;
    protected SharedPreferences mPrefs;

    public Cache(Context context) {
        mContext = context;
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mPrefs.edit();
    }
}
