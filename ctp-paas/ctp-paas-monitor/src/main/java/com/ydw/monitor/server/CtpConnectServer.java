package com.ydw.monitor.server;

import com.ydw.monitor.utils.ThreadPool;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


/**
 * @author xulh
 * @description: TODO
 * @date 2021/4/1218:04
 */
@Component
public class CtpConnectServer {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${netty.port}")
    private int nettyPort;

    @Autowired
    ThreadPool threadPool;

    @Autowired
    private CtpChannelInitializer channelInitializer;

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
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5 * 1000 * 60);
            serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));
            serverBootstrap.childHandler(channelInitializer);
            logger.info("检测服务启动，开始绑定端口{}", nettyPort);
            ChannelFuture future = serverBootstrap.bind(nettyPort).sync();
            future.addListener(f -> logger.info("检测服务启动完成，绑定端口{}", nettyPort));
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
