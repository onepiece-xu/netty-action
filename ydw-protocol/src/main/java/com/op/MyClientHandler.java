package com.op;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.nio.charset.Charset;
import java.util.Random;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/3/211:48
 */
public class MyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    int a = 0;

    MyClientHandler(){
        System.out.println("MyClientHandler init");
    }

    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        System.out.println(++a);
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

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        System.out.println(ctx.channel().remoteAddress()+ " channelActive");
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeShort(2);
        byteBuf.writeShort(1);
        byteBuf.writeInt(0x0105);
        byteBuf.writeInt(new Random().nextInt());
        Long l = System.currentTimeMillis() / 1000;
        byteBuf.writeInt(l.intValue());
        StringBuilder sb = new StringBuilder();
        sb.append("deviceid:"+a);
        byte[] s = sb.toString().getBytes();
        byteBuf.writeInt(s.length);
        byteBuf.writeBytes(s);
        ctx.writeAndFlush(byteBuf);
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
