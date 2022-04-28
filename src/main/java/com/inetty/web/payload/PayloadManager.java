package com.inetty.web.payload;

public class PayloadManager {
    private static BasePayLoad payLoad;

    public static void init(BasePayLoad payLoad) {
        payLoad = payLoad;
    }

    public static boolean increasePayload() {
        return payLoad.incr();
    }

    public static void decreasePayload() {
        payLoad.decr();
    }

    public static int getPayload() {
        return payLoad.get();
    }
}