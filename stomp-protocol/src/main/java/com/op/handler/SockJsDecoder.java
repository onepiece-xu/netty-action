package com.op.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public class SockJsDecoder extends SimpleChannelInboundHandler<TextWebSocketFrame>  {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String text = msg.text();
        String[] content = objectMapper.readValue(text, String[].class);
        log.info("c2s " + content[0]);
        ByteBuf buf = Unpooled.wrappedBuffer(content[0].getBytes(StandardCharsets.UTF_8));
        ctx.fireChannelRead(buf);
    }
}