package com.inetty.web.payload;

import java.util.concurrent.Semaphore;

/**
 * 信号量实现服务频率控制
 */
public class SemaphorePayLoad extends BasePayLoad{

    private Semaphore semaphore;

    public SemaphorePayLoad(int payload){
        super(payload);
        semaphore = new Semaphore(payload);
    }

    @Override
    public boolean incr(){
        boolean result = false;
        if(semaphore != null){
            result = this.semaphore.tryAcquire();
        }
        return result;
    }

    @Override
    public void decr(){
        if(semaphore != null)
        {
            this.semaphore.release();
        }
    }

    @Override
    public int get(){
        return this.semaphore.availablePermits();
    }

}
