package com.mtesitoo.backend.model.url;

import com.mtesitoo.backend.model.URL;
import android.content.Context;


/**
 * Created by User on 27-04-2016.
 */
public class VendorOrdersURL extends URL {
    public VendorOrdersURL(Context context, int resId, int sellerId) {
        super(context, resId);
        mPath.append("/" + Integer.toString(sellerId));
        mPath.append("/orders");
    }
}
