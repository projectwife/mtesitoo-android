package com.mtesitoo.backend.model.url;

import android.content.Context;

import com.mtesitoo.backend.model.URL;

/**
 * Created by Nan on 10/31/2015.
 */
public class ProductImageURL extends URL {
    public ProductImageURL(Context context, int resId, int productId) {
        super(context, resId);
        mPath.append("/" + Integer.toString(productId));
        mPath.append("/image");
    }
}