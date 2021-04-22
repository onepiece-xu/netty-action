package com.ydw.monitor.server;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.ssl.ClientAuth;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStream;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/4/229:35
 */
@Component
public class CtpChannelInitializer extends ChannelInitializer<SocketChannel> {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${netty.readTimeoutSeconds}")
    private int readTimeoutSeconds;

    @Value("${ssl.open}")
    private boolean sslOpen;

    @Value("${ssl.keyCertChainFile}")
    private String keyCertChainFile;

    @Value("${ssl.keyFile}")
    private String keyFile;

    private SslContext sslCtx;

    @Autowired
    private MessageHandler messageHandler;

    @PostConstruct
    public void init() throws Exception{
        if (sslOpen && sslCtx == null){
            logger.info("ssl的open状态为true，开始初始化ssl");
            InputStream keyCertChainInputStream = new ClassPathResource(keyCertChainFile).getInputStream();
            InputStream keyInputStream = new ClassPathResource(keyFile).getInputStream();
            sslCtx = SslContextBuilder.forServer(keyCertChainInputStream, keyInputStream).clientAuth(ClientAuth.NONE)
                    .sslProvider(SslProvider.OPENSSL).build();
            logger.info("ssl容器初始化完毕！");
        }
    }

    @Override
    protected void initChannel(SocketChannel ch){
        ChannelPipeline pipeline = ch.pipeline();
        if (sslOpen && sslCtx != null){
            pipeline.addLast("sslHandler",sslCtx.newHandler(ByteBufAllocator.DEFAULT));
        }
        pipeline.addLast("readTimeoutHandler", new ReadTimeoutHandler(readTimeoutSeconds));
        pipeline.addLast("lengthFieldBasedFrameDecoder",
                new LengthFieldBasedFrameDecoder(1<<16,16,4,0,0));
        pipeline.addLast("messageHandler", messageHandler);
    }
}
