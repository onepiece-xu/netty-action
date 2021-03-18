/*
 * Copyright 2013-2018 Lilinfeng.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.op;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.net.URI;

/**
 * @author lilinfeng
 * @date 2014年2月14日
 * @version 1.0
 */
public class HttpFileClient {

    public void run(final URI uri) throws Exception {
        System.out.println();
        final String ip = uri.getHost();
        System.out.println(ip);
        final int port = uri.getPort();
        System.out.println(port);
        final String url = uri.getPath();
        System.out.println(url);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch)
                                throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("http-codec", new HttpClientCodec());
                            pipeline.addLast("contentDecompressor",new HttpContentDecompressor());
                            pipeline.addLast("http-aggregator", new HttpObjectAggregator(1<<30));
                            pipeline.addLast("fileClientHandler",new HttpFileClientHandler(uri));
                        }
                    });
            ChannelFuture future = b.connect(ip,port).sync();
            System.out.println("HTTP连接开始启动，网址是 : " + ip+":"
                    + port + url);
            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        String ip = "http://www.baidu.com";//"yzz.qk62.com";
        int port = 80;
        String url = "/";//"/remote_control.php?time=1615705812&cv=26a8ef0f222956ae4e042fec1ebb59a5&lr=0&cv2=c0919a163f5f17be4b80f543559e473b&file=%2Fvideos%2F95000%2F95741%2F95741.mp4&cv3=07fe2a842d44022e3661fe3f32f1a1f0&cv4=0e52f518c723fb1ab3e40588193cde3d";
        URI uri = new URI(ip + ":"+port+ url);
        new HttpFileClient().run(uri);
    }
}
