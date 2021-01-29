//package com.xul.self.protocol.privated;
//
//import io.netty.buffer.ByteBuf;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.handler.codec.marshalling.MarshallerProvider;
//import org.jboss.marshalling.Marshaller;
//
///**
// * @author xulh
// * @description: TODO
// * @date 2021/1/2915:48
// */
//public class MarshallingEncoder {
//
//    private static final byte[] LENGTH_PLACEHOLDER = new byte[4];
//    Marshaller marshaller;
//
//    /**
//     * Creates a new encoder.
//     *
//     * @param provider the {@link MarshallerProvider} to use
//     */
//    public MarshallingEncoder() {
//        this.marshaller = MarshallingCodeCFactory.buildMarshalling();
//    }
//
//    protected void encode(Object msg, ByteBuf out) throws Exception {
//        try {
//            int lengthPos = out.writerIndex();
//            out.writeBytes(LENGTH_PLACEHOLDER);
//            ChannelBufferByteOutput output = new ChannelBufferByteOutput(out);
//            marshaller.start(output);
//            marshaller.writeObject(msg);
//            marshaller.finish();
//            marshaller.close();
//            out.setInt(lengthPos, out.writerIndex() - lengthPos - 4);
//        }finally {
//            marshaller.close();
//        }
//
//    }
//}
