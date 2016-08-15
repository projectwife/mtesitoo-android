package com.mtesitoo.backend.model.url;

import com.mtesitoo.backend.R;
import com.mtesitoo.backend.model.OrderStatus;
import com.mtesitoo.backend.model.URL;
import android.content.Context;


//TODO NAILY FIX COMMENT
/**
 * Get a list of all the open orders belonging to the current logged in vendor.
 * GET /api/v1/vendor/order
 *
 * Request parameters

 filter_order_status: a comma separated list of integers matching one or more of the order_status_id values as returned by the GET /common/order_status request. If the parameter is omitted, orders of any status will be returned.

 *
 * Created by User on 27-04-2016
 */
public class VendorOrdersURL extends URL {
    public VendorOrdersURL(Context context, OrderStatus orderStatus) {
        super(context, R.string.path_order_vendor);

        if (orderStatus != OrderStatus.ALL) {
            mPath.append("/?");
            mPath.append(context.getString(R.string.path_order_vendor_filterBy));
            mPath.append("=");
            mPath.append(orderStatus.getStatusId());
        }
    }
}
