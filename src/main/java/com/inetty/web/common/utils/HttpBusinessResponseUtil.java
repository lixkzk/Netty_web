package com.inetty.web.common.utils;

import com.inetty.web.common.WSConstants;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import static io.netty.handler.codec.http.HttpHeaderNames.*;

@Slf4j
public class HttpBusinessResponseUtil{
    public static void sendResponse(final ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response){
        final String msgId = ctx.channel().attr(WSConstants.MSGID).get();
        try{
            if(response != null){
                if(ctx.channel().isActive()){
                    ChannelFuture future = ctx.writeAndFlush(response);
                    if(future != null){
                        future.addListener(new ChannelFutureListener(){
                            @Override
                            public void operationComplete(ChannelFuture future) throws Exception{
                                if(future.isSuccess()){

                                }else{log.error("send response failed , msgId ="+msgId);
                                    ctx.channel().close();
                                }
                            }
                        });
                    }else{
                        if(response.refCnt()>0){
                            log.error("msgId ="+msgId+", future is null,http response ref ="+response.refCnt());
                            response.release();
                        }
                        ctx.channel().close();
                    }
                }else{
                    log.info("msgId="+msgId+", inactive");
                    response.release();
                    ctx.channel().close();
                }
            }else{
                log.error("send response error,response is null,msgId = "+msgId);
                ctx.channel().close();
            }
        }catch(Exception e){
            log.error("send response error,response is null,msgId = "+msgId);
            ctx.channel().close();
        }
    }

    public static FullHttpResponse makeResponse(FullHttpRequest request, String body , HttpResponseStatus status) throws Exception{
        ByteBuf content;
        FullHttpResponse response;
        if(body != null){
            content = Unpooled.copiedBuffer(body, CharsetUtil.UTF_8);
            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,status,content);
            response.headers().set(CONTENT_TYPE,"application/json;charset=UTF-8");
            response.headers().set(CONTENT_LENGTH,response.content().readableBytes());
        }else{
            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,status);
            response.headers().set(CONTENT_LENGTH,0);
        }
        response.headers().set(CONNECTION,HttpHeaders.Values.CLOSE);
        return response;

    }
}
