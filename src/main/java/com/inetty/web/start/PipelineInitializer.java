package com.inetty.web.start;

import com.inetty.web.handler.DefaultNelRequestHandler;
import com.inetty.web.handler.RelaseRequestHandler;
import com.inetty.web.manager.NelResourceManager;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.EventExecutorGroup;

public class PipelineInitializer extends ChannelInitializer<SocketChannel> {
    private EventExecutorGroup eventExecutorGroup = null;
    private NelResourceManager nelResourceManager;
    private RelaseRequestHandler relaseRequestHandler;
    /**
     * 设置请求处理线程数
     */
    public PipelineInitializer setExecutionHandler(EventExecutorGroup eventExecutorGroup){
        this.eventExecutorGroup = eventExecutorGroup;
        return this;
    }

    /**
     * 设置拒绝服务连接数
     * @param nelResourceManager
     * @return
     */
    public PipelineInitializer setRefuseConnections(NelResourceManager nelResourceManager){
        this.relaseRequestHandler = new RelaseRequestHandler(nelResourceManager);
        return this;
    }

    /**
     * 设置manager
     * @param nelResourceManager
     * @return
     */
    public PipelineInitializer setResourceManager(NelResourceManager nelResourceManager){
        this.nelResourceManager= nelResourceManager;
        return this;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception{
        ChannelPipeline pipeline = ch.pipeline();
        //http服务器端对request解码
        pipeline.addLast("decoder",new HttpRequestDecoder());
        pipeline.addLast("readCheck",new IdleStateHandler(30,0,0));
        //http服务器端对response编码
        pipeline.addLast("encoder",new HttpResponseEncoder());
        //将多个请求整合成一个请求
        pipeline.addLast("aggregator",new HttpObjectAggregator(5*1024*1024));
        pipeline.addLast("controller",relaseRequestHandler);
        //业务处理
        pipeline.addLast(eventExecutorGroup,new DefaultNelRequestHandler(nelResourceManager));

    }
}
