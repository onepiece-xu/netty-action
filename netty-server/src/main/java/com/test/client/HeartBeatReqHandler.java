package com.test.client;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.TimeUnit;

import com.test.MessageType;
import com.test.NettyMessage;
import com.test.NettyMessageHeader;


public class HeartBeatReqHandler extends ChannelHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		NettyMessage nettyMsg = (NettyMessage) msg;
		if(nettyMsg!=null&&nettyMsg.getHeader()!=null){
			if(nettyMsg.getHeader().getType()==MessageType.LOGIN_RESP_SUCCESS){
				ctx.executor().scheduleWithFixedDelay(new HeartBeatTask(ctx), 0, 5, TimeUnit.SECONDS);
			}else if(nettyMsg.getHeader().getType()==MessageType.HEARBEAR_RESP){
				System.out.println("client recived heart beat msg :"+nettyMsg);
			}else{
				ctx.fireChannelRead(msg);
			}
		}
	}
	
	private class HeartBeatTask implements Runnable{
		private final ChannelHandlerContext ctx;
		private NettyMessage hearBeatMsg;
		public HeartBeatTask(ChannelHandlerContext ctx){
			this.ctx = ctx;
			this.hearBeatMsg = buildHearBeatMsg();
		}
		public void run() {
			System.out.println("client send heart beat msg    :"+hearBeatMsg);
			ctx.writeAndFlush(hearBeatMsg);
		}
		
		private NettyMessage buildHearBeatMsg(){
			NettyMessage hearBeatMsg = new NettyMessage();
			hearBeatMsg.setHeader(new NettyMessageHeader());
			hearBeatMsg.getHeader().setType(MessageType.HEARTBEAT_REQ);
			return hearBeatMsg;
		}
	}
}
