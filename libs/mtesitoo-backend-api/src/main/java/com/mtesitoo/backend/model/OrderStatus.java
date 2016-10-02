package com.mtesitoo.backend.model;

import android.content.Context;

import com.mtesitoo.backend.R;

/**
 * Created by naily on 04/08/16
 *
 * The following statuses are not supported (used) in the app:
 * PROCESSING(2), DENIED(8), CANCELED_REVERSAL(9), FAILED(10), REFUNDED(11), REVERSED(12),
 * CHARGEBACK(13), EXPIRED(14), PROCESSED(15), VOIDED(16)
 */
public enum OrderStatus {
    ALL(0, R.string.order_status_all_orders), PENDING(1, R.string.order_status_pending),
    SHIPPED(3, R.string.order_status_shipped), COMPLETE(5, R.string.order_status_complete),
    CANCELED(7, R.string.order_status_canceled);

    private final int     statusId;
    private final int statusStringResId;

    OrderStatus(int id, int resString)
    {
        statusId = id;
        statusStringResId = resString;
    }

    public int getStatusId()
    {
        return statusId;
    }

    public String getStatus(Context context) {
        return context.getString(statusStringResId);
    }
}
