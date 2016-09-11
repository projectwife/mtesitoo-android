package com.mtesitoo.backend.model.url;

import android.content.Context;

import com.mtesitoo.backend.model.URL;

/**
 * Created by carl on 9/11/16.
 */

public class ProductURL extends URL {
    public ProductURL(Context context, int resId, int productId) {
        super(context, resId);
        mPath.append("/" + Integer.toString(productId));
    }
}