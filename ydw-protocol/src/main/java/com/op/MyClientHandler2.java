package com.op;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Random;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/3/214:20
 */
public class MyClientHandler2 extends ChannelInboundHandlerAdapter {

    private String deviceId;

    public MyClientHandler2(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+ " handlerAdded");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+ " handlerRemoved");
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+ " channelRegistered");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+ " channelUnregistered");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+ " channelInactive");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+ " exceptionCaught");
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
        sb.append("deviceid:"+deviceId);
        byte[] s = sb.toString().getBytes();
        byteBuf.writeInt(s.length);
        byteBuf.writeBytes(s);
        ctx.writeAndFlush(byteBuf);
    }

}
