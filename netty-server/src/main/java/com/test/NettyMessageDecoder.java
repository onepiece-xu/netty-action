package com.test;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.TooLongFrameException;

import java.util.HashMap;
import java.util.Map;

import com.test.util.MarshallingCodeCFactory;
import com.test.util.NettyMarshallingDecoder;

public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder{

	private NettyMarshallingDecoder decoder;
	
	/**
     * Creates a new instance.
     *
     * @param maxFrameLength
     *        the maximum length of the frame.  If the length of the frame is
     *        greater than this value, {@link TooLongFrameException} will be
     *        thrown.
     * @param lengthFieldOffset
     *        the offset of the length field
     * @param lengthFieldLength
     *        the length of the length field
     * @param lengthAdjustment
     *        the compensation value to add to the value of the length field
     * @param initialBytesToStrip
     *        the number of first bytes to strip out from the decoded frame
     */
	
	public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset,
			int lengthFieldLength,int lengthAdjustment, int initialBytesToStrip) {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
		decoder = MarshallingCodeCFactory.buildMarshallingDecoder();
	}
	

	public Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception{
		ByteBuf frame = (ByteBuf)super.decode(ctx, in);
		if(frame == null){
			return null;
		}
		
		NettyMessage message = new NettyMessage();
		NettyMessageHeader header = new NettyMessageHeader();
		header.setCrcCode(frame.readInt());
		header.setLength(frame.readInt());
		header.setSessionId(frame.readLong());
		header.setType(frame.readByte());
		header.setPriority(frame.readByte());
		
		int size = frame.readInt();
		if(size > 0){
			Map<String, Object> attach = new HashMap<String, Object>(size);
			int keySize = 0;
			byte[] keyArray = null;
			String key = null;
			for(int i=0; i<size; i++){
				keySize = frame.readInt();
				keyArray = new byte[keySize];
				in.readBytes(keyArray);
				key = new String(keyArray, "UTF-8");
				attach.put(key, decoder.decode(ctx, frame));
			}
			key = null;
			keyArray = null;
			header.setAttachment(attach);
		} 
		if(frame.readableBytes() > 0){
			message.setBody(decoder.decode(ctx, frame));
		}
		message.setHeader(header);
		return message;
	}

}
