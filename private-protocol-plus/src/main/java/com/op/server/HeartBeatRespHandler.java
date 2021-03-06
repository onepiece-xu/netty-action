package com.op.server;

import com.op.model.NettyHeader;
import com.op.model.NettyMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/2/114:13
 */
public class HeartBeatRespHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;
        if (message.getHeader() != null &&
                message.getHeader().getType() == NettyHeader.MessageType.HEARTBEAT_REQ.code) {
            System.out.println("receive client heart beat message : --> " + message);
            NettyMessage heartBeat = buildHeartBeat();
            System.out.println("send heart beat response message to client : --> " + heartBeat);
            ctx.writeAndFlush(heartBeat);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    private NettyMessage buildHeartBeat() {
        NettyMessage message = new NettyMessage();
        NettyHeader header = new NettyHeader();
        header.setType(NettyHeader.MessageType.HEARTBEAT_RESP.code);
        message.setHeader(header);
        return message;
    }
}
