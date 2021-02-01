package com.xul.self.protocol.privated;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/2/114:13
 */
public class HeartBeatRespHandler extends ChannelHandlerAdapter {

    /**
     * Calls {@link ChannelHandlerContext#fireExceptionCaught(Throwable)} to forward
     * to the next {@link ChannelHandler} in the {@link ChannelPipeline}.
     * <p>
     * Sub-classes may override this method to change behavior.
     *
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("123");
    }

    /**
     * Calls {@link ChannelHandlerContext#fireChannelRead(Object)} to forward
     * to the next {@link ChannelHandler} in the {@link ChannelPipeline}.
     * <p>
     * Sub-classes may override this method to change behavior.
     *
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage)msg;
        if (message.getHeader() != null &&
            message.getHeader().getType() == Header.MessageType.HEARTBEAT_REQ.code){
            System.out.println("receive client heart beat message : --> " +message);
            NettyMessage heartBeat = buildHeartBeat();
            System.out.println("send heart beat response message to client : --> " + heartBeat);
            ctx.writeAndFlush(message);
        }else {
            ctx.fireChannelRead(msg);
        }
    }

    private NettyMessage buildHeartBeat(){
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(Header.MessageType.HEARTBEAT_RESP.code);
        message.setHeader(header);
        return message;
    }
}
