package com.chikacow.kohimana.common;

import java.util.concurrent.atomic.AtomicInteger;

public class AppConstant {
    private static int REQUEST_COUNT = 0;

    public static int increase() {
        return ++REQUEST_COUNT;
    }
    public static int getRequestCount() {
        return REQUEST_COUNT;
    }
}
