package com.op;

import com.op.client.NettyClient;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/2/311:48
 */
public class Application {

    public static void main(String[] args) {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup(10);
        try {
            NettyClient nettyClient = new NettyClient(eventLoopGroup,"127.0.0.1",8000);
            Boolean connect = nettyClient.connect();

        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}
