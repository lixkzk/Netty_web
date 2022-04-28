package com.inetty.web.payload;

public abstract class BasePayLoad {
    public int payload;

    public BasePayLoad(int payload){
        this.payload = payload;
    }

    abstract boolean incr();

    abstract void decr();

    abstract int get();
}
