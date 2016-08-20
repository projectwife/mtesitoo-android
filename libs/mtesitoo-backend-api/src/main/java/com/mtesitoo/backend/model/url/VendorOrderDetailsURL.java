package com.mtesitoo.backend.model.url;

//TODO NAILY FIX COMMENT

import android.content.Context;

import com.mtesitoo.backend.R;
import com.mtesitoo.backend.model.URL;

/**
 * Created by naily on 10/08/16.
 * Method GET /api/v1/vendor/order/{id}

 Get details of a specific order, specified by id.

 All products will be listed

 Only the ordered products in the catalog of the logged-in vendor will be returned.
 */
public class VendorOrderDetailsURL extends URL {
    public VendorOrderDetailsURL(Context context, int orderId) {
        super(context, R.string.path_order_vendor);

        mPath.append("/" + orderId);
    }
}
