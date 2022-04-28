package com.inetty.demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.DefaultEventExecutorGroup;

import static io.netty.handler.codec.http.HttpHeaderNames.*;

public class NettyDemo {
    public void start(int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(5);
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new PipelineInitializer())
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.TCP_NODELAY, true);
            Channel ch = b.bind(port).sync().channel();
            ch.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public class PipelineInitializer extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            //http服务器端对request解码
            pipeline.addLast("decoder", new HttpRequestDecoder());
            pipeline.addLast("readCheck", new IdleStateHandler(30, 0, 0));
            //http服务器端对response编码
            pipeline.addLast("encoder", new HttpResponseEncoder());
            //将多个请求整合成一个请求
            pipeline.addLast("aggregator", new HttpObjectAggregator(5 * 1024 * 1024));
            //业务处理
            pipeline.addLast(new DefaultEventExecutorGroup(10), new DefaultRequestHandler());
        }
    }


    public class DefaultRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
            ByteBuf bf =  msg.content();
            byte[] byteArray = new byte[bf.capacity()];
            bf.readBytes(byteArray);
            String result = new String(byteArray);
            System.out.println("Req < " + result);
            ctx.writeAndFlush(makeResponse("hello word!")).addListener(ChannelFutureListener.CLOSE);
        }

        public FullHttpResponse makeResponse(String body){
            ByteBuf content = Unpooled.copiedBuffer(body, CharsetUtil.UTF_8);
            HttpResponseStatus status = HttpResponseStatus.OK;
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,status,content);
            response.headers().set(CONTENT_TYPE,"application/json;charset=UTF-8");
            response.headers().set(CONTENT_LENGTH,response.content().readableBytes());
            response.headers().set(CONNECTION,HttpHeaders.Values.CLOSE);
            return response;
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx){
            System.out.println("channelInactive ...");
        }

        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            System.out.println("channelRegistered ...");
        }

        @Override
        public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
            System.out.println("channelUnregistered ...");
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("channelActive ...");
        }
    }

    public static void main(String[] args){
        NettyDemo nd = new NettyDemo();
        nd.start(8089);
    }
}
