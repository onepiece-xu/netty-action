package com.op.server;

import com.op.model.NettyHeader;
import com.op.model.NettyMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/2/111:54
 */
public class LoginAuthRespHandler extends ChannelInboundHandlerAdapter {

    private Map<String, Boolean> nodeCheck = new ConcurrentHashMap<String, Boolean>();
    private String[] whitekList = {"127.0.0.1"};

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;
        if (message == null || message.getHeader() == null){
            ctx.close();
        }
        String nodeIndex = ctx.channel().remoteAddress().toString();
        NettyMessage loginResp = null;
        if (message.getHeader().getType() == NettyHeader.MessageType.LOGIN_REQ.code) {
            if (nodeCheck.containsKey(nodeIndex)) {
                loginResp = buildResponse((byte) -1);
            } else {
                InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
                String ip = address.getAddress().getHostAddress();
                boolean isOK = false;
                for (String WIP : whitekList) {
                    if (WIP.equals(ip)) {
                        isOK = true;
                        break;
                    }
                }
                loginResp = isOK ? buildResponse((byte) 0) :
                        buildResponse((byte) -1);
                if (isOK) {
                    nodeCheck.put(nodeIndex, true);
                }
            }
            System.out.println("The login response is " + loginResp + " body [" +
                    loginResp.getBody() + "]");
            ctx.writeAndFlush(loginResp);
        } else if (!nodeCheck.containsKey(nodeIndex)) {
            loginResp = buildResponse((byte) -1);
            ctx.writeAndFlush(loginResp);
            ctx.close();
        } else{
            ctx.fireChannelRead(msg);
        }
    }

    private NettyMessage buildResponse(byte result) {
        NettyMessage message = new NettyMessage();
        NettyHeader header = new NettyHeader();
        header.setType(NettyHeader.MessageType.LOGIN_RESP.code);
        message.setHeader(header);
        message.setBody(result);
        return message;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        nodeCheck.remove(ctx.channel().remoteAddress().toString());
        ctx.close();
        ctx.fireExceptionCaught(cause);
    }
}
