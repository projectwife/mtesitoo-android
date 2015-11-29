package com.mtesitoo.backend.model;

import android.content.Context;

import com.mtesitoo.backend.R;

/**
 * Created by Nan on 9/13/2015.
 */
public class URL {
    protected Context mContext;
    protected int mResId;
    protected StringBuilder mPath;

    public URL(Context context, int resId) {
        mContext = context;
        mResId = resId;
        mPath = new StringBuilder();
        mPath.append(context.getString(resId));
    }

    final public String getHost() {
        return mContext.getString(R.string.server);
    }

    public void append(String s) {
        mPath.append(s);
    }

    @Override
    public String toString() {
        return getHost() + mPath.toString();
    }
}