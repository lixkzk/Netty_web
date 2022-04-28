package com.inetty.web.handler;

import com.inetty.web.log.NelLog;
import com.inetty.web.manager.NelResourceManager;
import com.inetty.web.url.UrlMatch;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import static io.netty.handler.codec.http.HttpHeaderNames.*;

@Slf4j
abstract public class NelBaseController {

    public UrlMatch match;
    protected String resData;
    protected NelLog nelLog;
    protected NelResourceManager nelResourceManager;
    protected HttpResponseStatus resStatus = HttpResponseStatus.OK;

    abstract public void handleHttpMsg(ChannelHandlerContext ctx, FullHttpRequest request);

    public void sendResponse(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        if (this.nelLog.getInfo() == null) {
            this.nelLog.setInfo(this.resData);
        }
        log.info(this.nelLog.info);
        ctx.writeAndFlush(response(request)).addListener(ChannelFutureListener.CLOSE);
    }

    protected FullHttpResponse response(FullHttpRequest request) throws Exception {
        ByteBuf content = null;
        if (resData != null) {
            content = Unpooled.wrappedBuffer(resData.getBytes(CharsetUtil.UTF_8));
        } else {
            content = Unpooled.EMPTY_BUFFER;
        }
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, resStatus, content);
        response.headers().set(CONTENT_TYPE, "application/json;charset=UTF-8");
        if (resData == null) {
            response.headers().set(CONTENT_LENGTH, 0);
        } else {
            response.headers().set(CONTENT_LENGTH, content.array().length);
        }
        if (response != null) {
            response.headers().set(CONNECTION, HttpHeaders.Values.CLOSE);
        }
        return response;
    }

    public void setMatch(UrlMatch match) {
        this.match = match;
    }

    public String getParm(String p) {
        if (match != null) {
            return match.get(p);
        } else {
            return null;
        }
    }

    public void initResourceManager(NelResourceManager nelResourceManager) {
        this.nelResourceManager = nelResourceManager;
    }

    public void initLog(NelLog logBean) {
        this.nelLog = logBean;
    }
}
