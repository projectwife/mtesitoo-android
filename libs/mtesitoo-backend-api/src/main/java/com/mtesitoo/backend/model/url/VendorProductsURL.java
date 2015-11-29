package com.mtesitoo.backend.model.url;

import android.content.Context;

import com.mtesitoo.backend.model.URL;

/**
 * Created by Nan on 9/19/2015.
 */
public class VendorProductsURL extends URL {
    public VendorProductsURL(Context context, int resId, int sellerId) {
        super(context, resId);
        mPath.append("/" + Integer.toString(sellerId));
        mPath.append("/products");
    }
}
