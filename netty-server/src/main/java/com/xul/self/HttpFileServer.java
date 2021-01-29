package com.xul.self;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.util.logging.Logger;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/1/1510:10
 */
public class HttpFileServer {

    Logger logger = Logger.getLogger(HttpFileServer.class.getName());

    public void start(int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap sb = new ServerBootstrap();
        sb.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast ( "http-decoder",new HttpRequestDecoder());
                        pipeline.addLast ( "http-aggregator",new HttpObjectAggregator(65536)) ;
                        pipeline.addLast ( "http-encoder",new HttpResponseEncoder()) ;
                        pipeline.addLast ( "http-chunked",new ChunkedWriteHandler());
                        pipeline.addLast(new HttpFileServerHandler());
                    }
                });
        try {
            ChannelFuture sync = sb.bind(port).sync();
            logger.info("服务已启动，端口为：" + port);
            sync.channel().closeFuture().sync();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new HttpFileServer().start(5000);
    }
}
