package com.inetty.web.controller;

import com.inetty.web.annotation.Controller;
import com.inetty.web.handler.NelBaseController;
import com.inetty.web.manager.TestResourceManagerImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.extern.slf4j.Slf4j;

@Controller(value="/x/test/{method}",method="POST")
@Slf4j
public class TestController extends NelBaseController {
    HttpResponseStatus status = HttpResponseStatus.OK;
    private TestResourceManagerImpl resourceManager;
    @Override
    public void handleHttpMsg(ChannelHandlerContext ctx, FullHttpRequest request){
        resourceManager = (TestResourceManagerImpl)nelResourceManager;
        String method = this.getParm("method");
        this.resData = "{'a':123}";
        log.info("port:"+resourceManager.getPort()+",method:"+method);
    }
}
