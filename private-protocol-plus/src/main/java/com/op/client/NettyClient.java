package com.op.client;

import com.op.decoder.NettyMessageDecoder;
import com.op.encoder.NettyMessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/2/114:29
 */
public class NettyClient {

    String host;

    int port;

    Bootstrap bootstrap;

    Channel channel;

    public NettyClient(EventLoopGroup group, String host, int port) {
        this.host = host;
        this.port = port;
        this.bootstrap = init(group);
    }

    public Bootstrap init(EventLoopGroup group){
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
            .channel(NioSocketChannel.class)
            .option(ChannelOption.TCP_NODELAY, true)
            .handler(new LoggingHandler(LogLevel.INFO))
            .handler(new ChannelInitializer<SocketChannel>() {

                /**
                 * This method will be called once the {@link Channel} was registered. After the method returns this instance
                 * will be removed from the {@link ChannelPipeline} of the {@link Channel}.
                 *
                 * @param ch the {@link Channel} which was registered.
                 * @throws Exception is thrown if an error occurs. In that case the {@link Channel} will be closed.
                 */
                protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                //这里实际上用到了LengthFieldBasedFrameDecoder
                pipeline.addLast(new NettyMessageDecoder(1024 * 1024, 4,
                        4, -8, 0));
                pipeline.addLast("MessageEncoder", new NettyMessageEncoder());
                pipeline.addLast("ReadTimeoutHandler", new ReadTimeoutHandler(50));
                pipeline.addLast("LoginAuthHandler", new LoginAuthReqHandler());
                pipeline.addLast("HeartBeatHandler", new HeartBeatReqHandler());
                }
            });
        return bootstrap;
    }

    public boolean connect(){
        boolean connected = false;
        try{
            bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 20000);
            channel = bootstrap.connect(host, port).sync().channel();
            connected = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return connected;
    }

    public boolean disconnect(){
        boolean disconnected = false;
        try{
            channel.disconnect();
            disconnected = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return disconnected;
    }
}
