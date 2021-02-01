package com.xul.self.protocol.privated;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.charset.Charset;
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
        Header header = new Header();
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
            key = null;
            keyArray = null;
            header.setAttachment(attach);
        }
        if (buf.readableBytes() > 4){
            message.setBody(nettyMarshallingDecoder.decode(ctx, buf));
        }
        message.setHeader(header);
        return message;
    }
}
