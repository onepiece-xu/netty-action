package com.ydw.monitor.server;

import com.ydw.monitor.utils.ThreadPool;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/4/1218:04
 */
@Component
public class DeviceConnectServer {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${netty.port}")
    private int nettyPort;

    @Value("${netty.readTimeoutSeconds}")
    private int readTimeoutSeconds;

    @Autowired
    ThreadPool threadPool;

    @Autowired
    private MessageHandler messageHandler;

    /**
     * 连接事件循环组
     */
    private EventLoopGroup bossGroup = new NioEventLoopGroup(1);

    /**
     * io事件循环组
     */
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    @PostConstruct
    public void init(){
        threadPool.submit(() -> start());
    }

    /**
     * 开启服务
     */
    public void start(){
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5 * 1000 * 60);
        serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("readTimeoutHandler", new ReadTimeoutHandler(readTimeoutSeconds));
                pipeline.addLast("lengthFieldBasedFrameDecoder",
                        new LengthFieldBasedFrameDecoder(1<<16,16,4,0,0));
                pipeline.addLast("messageHandler", messageHandler);
            }
        });
        logger.info("检测服务启动，开始绑定端口{}", nettyPort);
        try {
            ChannelFuture future = serverBootstrap.bind(nettyPort).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("检测服务绑定端口{}失败！", nettyPort);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
