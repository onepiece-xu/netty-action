package com.xul.self.protocol.privated;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/2/114:54
 */
public class NettyServer {

    public void bind(int port) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new NettyMessageDecoder(1024 * 1024, 4,4,-8,0));
                        pipeline.addLast(new NettyMessageEncoder());
                        pipeline.addLast("readTimeoutHandler", new ReadTimeoutHandler(50));
                        pipeline.addLast(new LoginAuthRespHandler());
                        pipeline.addLast("HeartBeatHandler",new HeartBeatRespHandler());
                    }
                });
        b.bind(port).sync();
        System.out.println("netty server start ok : " + port);
    }

    public static void main(String[] args) throws InterruptedException {
        new NettyServer().bind(8000);
    }
}
