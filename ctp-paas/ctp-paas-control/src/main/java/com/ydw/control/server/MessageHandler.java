package com.ydw.control.server;

import com.ydw.control.service.IClientService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/4/1311:12
 */
@Component
@ChannelHandler.Sharable
public class MessageHandler extends ChannelInboundHandlerAdapter {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MessageHelper messageHelper;

    @Autowired
    private IClientService clientService;

    /**
     * 消息到达
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("{} channelRead", ctx.channel().remoteAddress());
        ByteBuf byteBuf = (ByteBuf)msg;
        messageHelper.dispatchMessage(ctx.channel(), byteBuf);
    }

    /**
     * 掉线处理
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("{} channelInactive", ctx.channel().remoteAddress());
        clientService.removeClient(ctx.channel());
    }

    /**
     * 异常处理
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.info("{} exceptionCaught", ctx.channel().remoteAddress());
        cause.printStackTrace();
        ctx.close();
    }

}
