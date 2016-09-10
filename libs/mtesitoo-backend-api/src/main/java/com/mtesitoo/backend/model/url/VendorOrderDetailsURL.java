package com.mtesitoo.backend.model.url;

import android.content.Context;

import com.mtesitoo.backend.R;
import com.mtesitoo.backend.model.URL;

/**
 * Get details of a specific order, specified by id.
 * This includes details about the buyer as well as the products bought.
 *
 * Server request: GET /api/v1/vendor/order/{id}
 *
 * Created by naily on 10/08/16.
 */
public class VendorOrderDetailsURL extends URL {
    public VendorOrderDetailsURL(Context context, int orderId) {
        super(context, R.string.path_order_vendor);

        mPath.append("/" + orderId);
    }
}
