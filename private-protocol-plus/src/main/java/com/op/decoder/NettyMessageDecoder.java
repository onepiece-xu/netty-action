package com.op.decoder;

import com.op.model.NettyHeader;
import com.op.model.NettyMessage;
import com.op.util.MarshallingCodeCFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.util.HashMap;

public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {

    private NettyMarshallingDecoder nettyMarshallingDecoder;

    public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
        nettyMarshallingDecoder = MarshallingCodeCFactory.buildMarshallingDecoder();
    }

    public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
        nettyMarshallingDecoder = MarshallingCodeCFactory.buildMarshallingDecoder();
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf buf = (ByteBuf)super.decode(ctx, in);
        if (buf == null){
            return null;
        }
        NettyMessage message = new NettyMessage();
        NettyHeader header = new NettyHeader();
        header.setCrcCode(buf.readInt());
        header.setLength(buf.readInt());
        header.setSessionID(buf.readLong());
        header.setType(buf.readByte());
        header.setPriority(buf.readByte());
        int size = buf.readInt();
        if (size > 0){
            HashMap<String ,Object> attach = new HashMap<String, Object>(size);
            int keySize = 0;
            byte[] keyArray = null;
            String key = null;
            for (int i = 0 ; i < size ; i++){
                keySize = buf.readInt();
                keyArray = new byte[keySize];
                buf.readBytes(keyArray);
                key = new String(keyArray, "UTF-8");
                attach.put(key, nettyMarshallingDecoder.decode(ctx, in));
            }
            header.setAttachment(attach);
        }
        //这里判断的是body是否是空
        if (buf.readableBytes() > 4){
            message.setBody(nettyMarshallingDecoder.decode(ctx, buf));
        }
        message.setHeader(header);
        return message;
    }


}
