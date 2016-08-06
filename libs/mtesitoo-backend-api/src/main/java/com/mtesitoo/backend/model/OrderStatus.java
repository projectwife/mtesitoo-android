package com.mtesitoo.backend.model;

/**
 * Created by naily on 04/08/16
 */
public enum OrderStatus {
    ALL(0), PENDING(1), PROCESSING(2), SHIPPED(3), COMPLETE(5), CANCELED(7), DENIED(8),
    CANCELED_REVERSAL(9), FAILED(10), REFUNDED(11), REVERSED(12), CHARGEBACK(13),
    EXPIRED(14), PROCESSED(15), VOIDED(16);

    private int statusId;

    OrderStatus(int id)
    {
        statusId = id;
    }

    public int getStatusId()
    {
        return statusId;
    }
}
