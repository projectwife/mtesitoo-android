package com.mtesitoo.backend.model.url;

import android.content.Context;

import com.mtesitoo.backend.model.URL;

/**
 * Created by Nan on 9/19/2015.
 */
public class VendorURL extends URL {
    public VendorURL(Context context, int resId, int vendorId) {
        super(context, resId);
        mPath.append("/" + Integer.toString(vendorId));
    }
}
