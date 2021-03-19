package com.op;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.stomp.StompFrame;
import io.netty.handler.codec.stomp.StompSubframeAggregator;
import io.netty.handler.codec.stomp.StompSubframeDecoder;
import io.netty.handler.codec.stomp.StompSubframeEncoder;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author Andrey Mizurov
 * @since 19.07.2019
 */
public class WebSocketStompServer {

    public static void main(String[] args) throws Exception {

        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class).handler(new LoggingHandler("info"))
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel channel) {
                        channel.pipeline()
                                .addLast(new HttpServerCodec())
                                .addLast(new HttpObjectAggregator(64 * 1024))
                                .addLast(new WebSocketServerProtocolHandler("/ws", "v11.stomp"))
                                .addLast(new StompSubProtocolInitializer());
                    }
                });

        ChannelFuture chf = bootstrap.bind(8899);
        chf.addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("Server started !!!");
            } else {
                System.out.println("Cannot start server, follows exception " + future.cause());
            }
        }).channel().closeFuture().sync();

    }

    static class StompSubProtocolInitializer extends SimpleChannelInboundHandler<WebSocketFrame> {

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
                String ctxName = ctx.name();
                ctx.pipeline()
                        .addAfter(ctxName, "stompHandler", new StompHandler())
                        .addAfter(ctxName, "stompAggregator", new StompSubframeAggregator(65536))
                        .addAfter(ctxName, "stompEncoder", new StompSubframeEncoder())
                        .addAfter(ctxName, "stompDecoder", new StompSubframeDecoder());
            } else {
                super.userEventTriggered(ctx, evt);
            }
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame webSocketFrame) {
            if (webSocketFrame instanceof TextWebSocketFrame || webSocketFrame instanceof BinaryWebSocketFrame) {
                ctx.fireChannelRead(webSocketFrame.content().retain());
            }
        }

    }

    static class StompHandler extends SimpleChannelInboundHandler<StompFrame> {

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, StompFrame stompFrame) {
            // Stomp handling
            System.out.println("Received frame: " + stompFrame.toString());
        }
    }

}