package com.op;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.stomp.StompSubframeAggregator;
import io.netty.handler.codec.stomp.StompSubframeDecoder;
import io.netty.handler.codec.stomp.StompSubframeEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/3/1811:15
 */
public class StompServer {

    NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
    NioEventLoopGroup wokerGroup = new NioEventLoopGroup();

    public void bind(int port) throws Exception{
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup,wokerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new HttpServerCodec());
                        pipeline.addLast(new HttpObjectAggregator(64 * 1024));
                        pipeline.addLast(new WebSocketServerProtocolHandler("/ws", null, false, 10 * 1024 * 1024, false, true, 10000L));
                        pipeline.addLast(new StompSubframeEncoder());
                        pipeline.addLast(new StompSubframeDecoder());
                        pipeline.addLast(new StompSubframeAggregator(65536));
                        pipeline.addLast(new StompHandler());
                    }
                });
        ChannelFuture channelFuture = bootstrap.bind(port).sync();
        channelFuture.channel().closeFuture().sync();
    }

    public static void main(String[] args) throws Exception {
        new StompServer().bind(8899);
    }
}
