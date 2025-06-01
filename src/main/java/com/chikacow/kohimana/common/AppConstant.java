package com.chikacow.kohimana.common;

import java.util.concurrent.atomic.AtomicInteger;

import java.util.concurrent.atomic.AtomicInteger;

public class AppConstant {
    private static final AtomicInteger REQUEST_COUNT = new AtomicInteger(0);

    public static int increase() {
        return REQUEST_COUNT.incrementAndGet();
    }

    public static int getRequestCount() {
        return REQUEST_COUNT.get();
    }

    public static void reset() {
        REQUEST_COUNT.set(0);
    }

    public static int decrease() {
        return REQUEST_COUNT.decrementAndGet();
    }
}

