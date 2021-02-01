package com.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;
import java.util.Map;

import com.test.util.MarshallingCodeCFactory;
import com.test.util.NettyMarshallingEncoder;

public class NettyMessageEncoder extends MessageToMessageEncoder<NettyMessage>{

	private NettyMarshallingEncoder encoder;
	public NettyMessageEncoder(){
		encoder = MarshallingCodeCFactory.buildMarshallingEncoder();
	}
	@Override
	protected void encode(ChannelHandlerContext ctx, NettyMessage msg,
			List<Object> out) throws Exception {
		if(msg == null || msg.getHeader() == null){
			throw new Exception("The encode message is null");
		}
		
		ByteBuf sendBuf = Unpooled.buffer();
		sendBuf.writeInt(msg.getHeader().getCrcCode());
		sendBuf.writeInt(msg.getHeader().getLength());
		sendBuf.writeLong(msg.getHeader().getSessionId());
		sendBuf.writeByte(msg.getHeader().getType());
		sendBuf.writeByte(msg.getHeader().getPriority());
		sendBuf.writeInt(msg.getHeader().getAttachment().size());
		
		String key = null;
		byte[] keyArray = null;
		Object value = null;
		for(Map.Entry<String, Object> param: msg.getHeader().getAttachment().entrySet()){
			key = param.getKey();
			keyArray = key.getBytes("UTF-8");
			sendBuf.writeInt(keyArray.length);
			sendBuf.writeBytes(keyArray);
			value = param.getValue();
			encoder.encode(ctx, value, sendBuf);
		}
		key = null;
		keyArray = null;
		value = null;
		if(msg.getBody() != null){
			encoder.encode(ctx, msg.getBody(), sendBuf);
		}
		
//		sendBuf.writeInt(0);
		// �ڵ�4���ֽڳ�д��Buffer�ĳ���
		int readableBytes = sendBuf.readableBytes();
		sendBuf.setInt(4, readableBytes);
		
		// ��Message��ӵ�List���ݵ���һ��Handler 
		out.add(sendBuf);
	}
}
