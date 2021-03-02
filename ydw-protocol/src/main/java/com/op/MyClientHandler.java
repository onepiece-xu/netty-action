package com.op;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

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
    }

}
