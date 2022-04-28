package com.inetty.web.common.enums;

import io.netty.handler.codec.http.HttpResponseStatus;

public enum ResponseEnum{

    BAD_REQUEST(HttpResponseStatus.BAD_REQUEST,"bad request"),
    NOT_FOUND(HttpResponseStatus.NOT_FOUND,"not found");

    ResponseEnum(HttpResponseStatus status,String desc){
        this.status = status;
        this.desc = desc;
    }

    HttpResponseStatus status;
    String desc;

    public HttpResponseStatus getStatus(){
        return this.status;
    }

    public String getDesc(){
        return this.desc;
    }
}