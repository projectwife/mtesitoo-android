package com.mtesitoo.backend.model;

/**
 * Created by naily on 04/08/16
 *
 * The following statuses are not supported (used) in the app:
 * PROCESSING(2), DENIED(8), CANCELED_REVERSAL(9), FAILED(10), REFUNDED(11), REVERSED(12),
 * CHARGEBACK(13), EXPIRED(14), PROCESSED(15), VOIDED(16)
 */
public enum OrderStatus {
    ALL(0), PENDING(1), SHIPPED(3), COMPLETE(5), CANCELED(7);

    private int     statusId;
    private String  status;

    OrderStatus(int id)
    {
        statusId = id;
    }

    public int getStatusId()
    {
        return statusId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatusString(String aStatus) {
        status = aStatus;
    }
}
