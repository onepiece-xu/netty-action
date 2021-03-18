package com.op;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.nio.charset.Charset;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/3/211:48
 */
public class MyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        int i = msg.readableBytes();
        byte[] bytes = new byte[i];
        msg.readBytes(bytes);
        System.out.println(ctx.channel().remoteAddress()+ " "+new String(bytes, "GBK"));
        Channel channel = ctx.channel();
        ChannelFuture future = channel.close();
        System.out.println("开始关");
        future.addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                Thread.sleep(5000);
                System.out.println("休息一下");
            }
        });
        System.out.println("关了");
    }

    /**
     * Sub-classes may override this method to change behavior.
     *
     * @param ctx
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+ " channelReadComplete");
    }
}
