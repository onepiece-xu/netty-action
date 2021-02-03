本项目是根据《Netty权威指南》中的私有协议修订而来，进行改造，支持多服务端和多客户端
--------------------------------------------------------
登录流程部分流程讲解
nettyclient:
    客户端连接，连接成功后回调管道有效方法，准备发送登录消息
    connect--->LoginAuthReqHandler.channelActive---->ctx.writeAndFlush(buildLoginReq())

    在pipeline的出口方向有NettyMessageEncoder进行消息编码,编码后才发送出去
    NettyMessageEncoder.encode(ChannelHandlerContext ctx, NettyMessage msg, List<Object> out)

nettyserver:
    绑定端口，等待客户端连接，客户端发送消息进入管道，先进行解码
    bind----->NettyMessageDecoder.decode---->LengthFieldBasedFrameDecoder.decode

    在pipeline的出口方向有NettyMessageEncoder进行消息编码,编码后才发送出去
    NettyMessageEncoder.encode(ChannelHandlerContext ctx, NettyMessage msg, List<Object> out)