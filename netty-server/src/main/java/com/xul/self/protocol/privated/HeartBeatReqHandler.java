package com.xul.self.protocol.privated;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/2/113:55
 */
public class HeartBeatReqHandler extends ChannelHandlerAdapter {

    private volatile ScheduledFuture<?> heartBeat;


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (heartBeat != null){
            heartBeat.cancel(true);
            heartBeat = null;
        }
        ctx.fireExceptionCaught(cause);
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;
        if (message.getHeader() != null && message.getHeader().getType() == Header.MessageType.HEARTBEAT_REQ.code){
            ctx.executor().scheduleAtFixedRate(new HeartBeatTask(ctx), 0, 5000, TimeUnit.MILLISECONDS);
        }else if (message.getHeader() != null && message.getHeader().getType() == Header.MessageType.HEARTBEAT_RESP.code){
            System.out.println("Client receive server heart beat message : --> " + message);
        }else {
            ctx.fireChannelRead(msg);
        }
    }

    private class HeartBeatTask implements Runnable{
        private final ChannelHandlerContext ctx;

        public HeartBeatTask(final ChannelHandlerContext ctx){
            this.ctx = ctx;
        }

        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        public void run() {
            NettyMessage heartBeat = buildHeartBeat();
            System.out.println("client send heart beat message to server : -->" + heartBeat);
            ctx.writeAndFlush(heartBeat);
        }

        private NettyMessage buildHeartBeat(){
            NettyMessage message = new NettyMessage();
            Header header = new Header();
            header.setType(Header.MessageType.HEARTBEAT_RESP.code);
            message.setHeader(header);
            return message;
        }
    }
}
