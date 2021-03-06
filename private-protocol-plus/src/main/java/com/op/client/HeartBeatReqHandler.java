package com.op.client;

import com.op.model.NettyHeader;
import com.op.model.NettyMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/2/113:55
 */
public class HeartBeatReqHandler extends ChannelInboundHandlerAdapter {

    private volatile ScheduledFuture<?> heartBeat;


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (heartBeat != null) {
            heartBeat.cancel(true);
            heartBeat = null;
        }
        ctx.fireExceptionCaught(cause);
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;
        if (message.getHeader() != null && message.getHeader().getType() == NettyHeader.MessageType.LOGIN_RESP.code) {
            ctx.executor().scheduleAtFixedRate(new HeartBeatTask(ctx), 0, 5000, TimeUnit.MILLISECONDS);
        } else if (message.getHeader() != null && message.getHeader().getType() == NettyHeader.MessageType.HEARTBEAT_RESP.code) {
            System.out.println("Client receive server heart beat message : --> " + message);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    private class HeartBeatTask implements Runnable {
        private final ChannelHandlerContext ctx;

        public HeartBeatTask(final ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        public void run() {
            NettyMessage heartBeat = buildHeartBeat();
            System.out.println("client send heart beat message to server : -->" + heartBeat);
            ctx.writeAndFlush(heartBeat);
        }

        private NettyMessage buildHeartBeat() {
            NettyMessage message = new NettyMessage();
            NettyHeader header = new NettyHeader();
            header.setType(NettyHeader.MessageType.HEARTBEAT_REQ.code);
            message.setHeader(header);
            return message;
        }
    }
}
