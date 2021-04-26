package com.ydw.monitor.client;

import com.ydw.monitor.model.constant.Constants;
import com.ydw.monitor.server.CtpChannelInitializer;
import com.ydw.monitor.server.MessageHeader;
import com.ydw.monitor.utils.ThreadPool;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.ClientAuth;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetAddress;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/4/1218:04
 */
public class CtpConnectClient {

    Logger logger = LoggerFactory.getLogger(getClass());

    EventLoopGroup workerGroup = new NioEventLoopGroup();


    /**
     * 开启服务
     */
    public ChannelFuture start(){
        ChannelFuture future = null;
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5 * 1000 * 60);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    SslContext build = SslContextBuilder.forClient().sslProvider(SslProvider.OPENSSL).build();
                    pipeline.addLast(build.newHandler(ByteBufAllocator.DEFAULT));
                }
            });
            String ip = "yigougou.cn";
            int port = 8899;
            logger.info("客户端服务启动，开始连接{}：{}", ip, port);
            future = bootstrap.connect(ip, port).sync();
            future.addListener(f -> logger.info("客户端服务启动完成 {}：{}", ip, port));
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("客户端服务启动失败！");
        }
        return future;
    }

    public static void main(String[] args) throws Exception{
        CtpConnectClient client = new CtpConnectClient();
        Channel channel = client.start().channel();
//        while (true){
            Thread.sleep(5000);
            if (channel.isActive()){
                ByteBuf byteBuf = Unpooled.buffer();
                MessageHeader messageHeader = MessageHeader.SimpleMessageHeader(Constants.COMMAND_SYSTEM_GETID);
                byteBuf.writeShort(messageHeader.getCmdType());
                byteBuf.writeShort(messageHeader.getCsType());
                byteBuf.writeInt(messageHeader.getCommandId());
                byteBuf.writeInt(messageHeader.getPacketId());
                byteBuf.writeInt(messageHeader.getTimeStamp());
                byteBuf.writeInt(0);
                channel.writeAndFlush(byteBuf);
            }
            channel.close();
            client.workerGroup.shutdownGracefully();
//        }
    }
}
