package com.inetty.web.payload;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicPayLoad extends BasePayLoad{

    protected static AtomicInteger atomicInteger = new  AtomicInteger(0);

    public AtomicPayLoad(int payload){
        super(payload);
    }

    @Override
    public boolean incr() {
        int result = atomicInteger.incrementAndGet();
        if(result > payload){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void decr() {
        atomicInteger.decrementAndGet();
    }

    @Override
    public int get() {
        return atomicInteger.get();
    }
}
