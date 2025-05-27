package com.chikacow.kohimana.util.enums;

public enum OrderStatus {
    PENDING, CONFIRMED, PREPARING, READY, SERVED, CANCELLED, PAID;

    public OrderStatus next()  {

        switch (this) {
            case PENDING:
                return OrderStatus.CONFIRMED;

            case CONFIRMED:
                return OrderStatus.PREPARING;

            case PREPARING:
                return OrderStatus.READY;

            case READY:
                return OrderStatus.SERVED;

            case SERVED:
                return OrderStatus.CANCELLED;

            case CANCELLED:
                return OrderStatus.PAID;

            case PAID:
                return OrderStatus.PAID;
        }

        return OrderStatus.PENDING;
    }
}

