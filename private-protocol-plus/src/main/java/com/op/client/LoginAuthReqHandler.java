package com.op.client;

import com.op.model.NettyHeader;
import com.op.model.NettyMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class LoginAuthReqHandler extends ChannelHandlerAdapter {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        ctx.fireExceptionCaught(cause);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        ctx.writeAndFlush(buildLoginReq());
    }

    private NettyMessage buildLoginReq() {
        NettyMessage message = new NettyMessage();
        NettyHeader header = new NettyHeader();
        header.setType(NettyHeader.MessageType.LOGIN_REQ.code);
        message.setHeader(header);
        return message;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        NettyMessage message = (NettyMessage) msg;
        if (message.getHeader() != null &&
                message.getHeader().getType() == NettyHeader.MessageType.LOGIN_RESP.code) {
            byte body = (Byte) message.getBody();
            if (body == -1) {
                ctx.close();
            } else {
                System.out.println("login is ok : " + message);
                ctx.fireChannelRead(msg);
            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }
}
