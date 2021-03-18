package com.op;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.stomp.StompCommand;
import io.netty.handler.codec.stomp.StompFrame;
import io.netty.handler.codec.stomp.StompHeaders;
import io.netty.handler.codec.stomp.StompSubframeAggregator;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/3/1811:26
 */
public class StompHandler extends SimpleChannelInboundHandler<StompFrame> {

    protected void channelRead0(ChannelHandlerContext ctx, StompFrame msg) throws Exception {
        StompCommand command = msg.command();
        System.out.println("command: "+command.toString());
        StompHeaders headers = msg.headers();
        System.out.println("headers: "+ headers.toString());
        ByteBuf content = msg.content();
        System.out.println("content: "+content.toString());
    }
}
