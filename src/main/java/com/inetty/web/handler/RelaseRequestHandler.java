package com.inetty.web.handler;

import com.inetty.web.common.WSConstants;
import com.inetty.web.common.utils.HttpBusinessResponseUtil;
import com.inetty.web.manager.NelResourceManager;
import com.inetty.web.payload.AtomicPayLoad;
import com.inetty.web.payload.PayloadManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

@ChannelHandler.Sharable
@Slf4j
public class RelaseRequestHandler extends SimpleChannelInboundHandler<Object> {
    public static AtomicLong MSGID = new AtomicLong(0);
    private int REFUSE_CONNECTIONS = 400;
    private String serverName;
    private String serverIp = "127.0.0.1";
    private String serverRegion = "DEF-01";

    public RelaseRequestHandler(NelResourceManager nelResource) {
        if (nelResource != null) {
            if (nelResource.getRefuseConns() != null) {
                this.REFUSE_CONNECTIONS = nelResource.getRefuseConns().intValue();
                PayloadManager.init(new AtomicPayLoad(this.REFUSE_CONNECTIONS));
            }
            this.serverName = nelResource.getServerName();
            if (Objects.nonNull(nelResource.getServerRegion())) {
                this.serverRegion = nelResource.getServerRegion();
            }
            if (Objects.nonNull(nelResource.getServerIp())){
                this.serverIp = nelResource.getServerIp();
            }
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg instanceof FullHttpRequest) {
                Channel ch = ctx.channel();
                FullHttpRequest request = (FullHttpRequest) msg;
                //拒绝服务
                int payLoad = PayloadManager.getPayload();
                if (payLoad > 150) {
                    log.info(" ... initChannel ... payLoad = " + payLoad);
                }
                long begin = System.currentTimeMillis();
                if (payLoad < REFUSE_CONNECTIONS || REFUSE_CONNECTIONS == 0) {
                    //连接数+1
                    PayloadManager.increasePayload();
                    try {
                        StringBuffer sb = new StringBuffer();
                        sb.append(this.serverRegion).append("_").append(this.serverName).append("_").append(this.serverIp).append("_").append(begin).append("_").append(MSGID.getAndIncrement());
                        String msgId = sb.toString();
                        ch.attr(WSConstants.MSGID).set(msgId);
                        ch.attr(WSConstants.BEGINTIME).set(begin);
                    } catch (Exception e) {
                        log.error("[Nel-Common] Exception : incr msgId error", e);
                        MSGID.set(0);
                    }
                    request.retain();
                    ctx.fireChannelRead(request);
                } else {
                    log.error("[Nel-Common] payload = " + payLoad + ", too many requests,refuse");
                    FullHttpResponse response = HttpBusinessResponseUtil.makeResponse(request, null, HttpResponseStatus.SERVICE_UNAVAILABLE);
                    HttpBusinessResponseUtil.sendResponse(ctx, request, response);
                    return;
                }
            } else {
                log.error("[Nel-Common] Receive error msg :" + msg.getClass().getName());
                ctx.channel().close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            ctx.channel().close();
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        log.info("userEventTriggered ...");
        String msgId = ctx.channel().attr(WSConstants.MSGID).get();
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (msgId != null && e.state() == IdleState.READER_IDLE) {
                log.error("[Nel-Common] Read idle timeout,msgId = " + msgId);
            }
            ctx.channel().close();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("channelInactive ...");
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("exceptionCaught ...");
        String msgId = ctx.channel().attr(WSConstants.MSGID).get();
        if (cause instanceof IOException) {

        } else {
            log.error("[Nel-Common] ExceptionCaught, msgId = " + msgId, cause);
        }
        ctx.channel().close();
    }
}
