package com.inetty.web.start;

import com.inetty.web.annotation.ControllerScanner;
import com.inetty.web.handler.NelProcessorFactory;
import com.inetty.web.manager.NelResourceManager;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NelServer {
    private Integer port = 80;
    private Integer bossGroupThreadNums = 0;//为0表示使用默认cpu*2
    private Integer executorThreadNums = 60;
    private Integer refuseConns = 400;
    private String serverName = "NEL-01";
    private Integer interval = 10;
    private String scanPackage = "c.h";
    private NelResourceManager resourceManager;

    public NelServer init(NelResourceManager resourceManager) {
        if (resourceManager.getPort() != null && resourceManager.getPort() > 0) {
            this.port = resourceManager.getPort();
        }
        bossGroupThreadNums = resourceManager.getBossGroupThreadNums();
        executorThreadNums = resourceManager.getExecutorThreadNums();
        refuseConns = resourceManager.getRefuseConns();
        serverName = resourceManager.getServerName();
        interval = resourceManager.getInterval();
        scanPackage = resourceManager.getScanPackage();
        this.resourceManager = resourceManager;
        return this;
    }

    public void start() {
        log.info("Nel-Common service start !");
        registerRequestHandler(scanPackage);
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(bossGroupThreadNums);
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(
                    new PipelineInitializer()
                            .setExecutionHandler(initializeExecutorGroup())
                            .setResourceManager(resourceManager)
                            .setRefuseConnections(resourceManager))
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.TCP_NODELAY, true);
            log.info("PORT =>" + port + ",refuseConns ==>" + refuseConns);
            Channel ch = b.bind(port).sync().channel();
            ch.closeFuture().sync();
        } catch (Exception e) {
            log.error(serverName + " Exception =>", e);
        } finally {
            log.info("[Nel-Common] server finally stop!");
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private EventExecutorGroup initializeExecutorGroup() {
        if (executorThreadNums > 0) {
            return new DefaultEventExecutorGroup(executorThreadNums);
        } else {
            return new DefaultEventExecutorGroup(10);
        }
    }

    private void registerRequestHandler(String packageName) {
        ControllerScanner cs = new ControllerScanner();
        cs.scan(packageName);
        for (String key : NelProcessorFactory.queryRegistry().keySet()) {
            log.info("registerRequestHandler key = " + key);
        }
    }
}
