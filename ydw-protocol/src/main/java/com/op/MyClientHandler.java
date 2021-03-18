package com.op;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

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

}
