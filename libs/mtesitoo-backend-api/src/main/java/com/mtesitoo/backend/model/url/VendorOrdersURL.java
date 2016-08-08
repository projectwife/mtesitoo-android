package com.mtesitoo.backend.model.url;

import com.mtesitoo.backend.R;
import com.mtesitoo.backend.model.OrderStatus;
import com.mtesitoo.backend.model.URL;
import android.content.Context;


/**
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
