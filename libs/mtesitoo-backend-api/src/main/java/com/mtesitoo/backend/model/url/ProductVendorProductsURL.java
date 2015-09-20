package com.mtesitoo.backend.model.url;

import android.content.Context;

import com.mtesitoo.backend.model.URL;

/**
 * Created by Nan on 9/19/2015.
 */
public class ProductVendorProductsURL extends URL {
    public ProductVendorProductsURL(Context context, int resId, int sellerId) {
        super(context, resId);
        mPath.append("/" + Integer.toString(sellerId));
        mPath.append("/products");
    }
}
