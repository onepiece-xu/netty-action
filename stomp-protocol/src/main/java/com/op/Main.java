package com.op;

import com.op.handler.*;
import com.op.server.ServerRuntime;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.stomp.StompSubframeAggregator;
import io.netty.handler.codec.stomp.StompSubframeDecoder;
import io.netty.handler.codec.stomp.StompSubframeEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.extern.slf4j.Slf4j;
import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.security.*;
import java.security.cert.CertificateException;

@Slf4j
public class Main {
    private final int port;
    private final ServerRuntime serverRuntime = new ServerRuntime();

    private Main(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
        new Main(8899).start();
    }

    private void start() throws InterruptedException {
        String keyStorePath = System.getProperty("javax.net.ssl.keyStore");
        String keyStorePassword = System.getProperty("javax.net.ssl.keyStorePassword");
        String keyStoreType = System.getProperty("javax.net.ssl.keyStoreType");
        String trustStorePath = System.getProperty("javax.net.ssl.trustStore");
        String trustStorePassword = System.getProperty("javax.net.ssl.trustStorePassword");
        String trustStoreType = System.getProperty("javax.net.ssl.trustStoreType");

        SSLContext serverSSLContext = null;
        if (keyStorePath != null) {
            try (InputStream is = new FileInputStream(keyStorePath)) {
                String algorithm = KeyManagerFactory.getDefaultAlgorithm();
                KeyStore ks = KeyStore.getInstance(keyStoreType);
                if (keyStorePassword == null)
                    throw new IllegalStateException("Empty keystore password");
                ks.load(is, keyStorePassword.toCharArray());
                KeyManagerFactory kmf = KeyManagerFactory.getInstance(algorithm);
                kmf.init(ks, keyStorePassword.toCharArray());
                KeyManager[] keyManagers = kmf.getKeyManagers();
                //Don't need mutual ssl
                TrustManager[] trustManagers = null;
                serverSSLContext = SSLContext.getInstance("TLS");
                serverSSLContext.init(keyManagers, trustManagers, null);

            } catch (IOException | KeyStoreException | CertificateException | NoSuchAlgorithmException | UnrecoverableKeyException | KeyManagementException e) {
//                log.error("Error on ssl configuration",e);
                System.out.println("Error on ssl configuration");
            }
        }
        final EventLoopGroup acceptLoopGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("acceptThread"));
        final EventExecutorGroup stompGroup = new DefaultEventExecutorGroup(Runtime.getRuntime().availableProcessors(), new DefaultThreadFactory("stompHandlerThread"));
        final EventLoopGroup rwLoopGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors(),new DefaultThreadFactory("rwWorkerThread"));
        try {
            final ServerBootstrap b = new ServerBootstrap();
            SSLContext finalServerSSLContext = serverSSLContext;
            b.group(acceptLoopGroup, rwLoopGroup)
                    .localAddress(new InetSocketAddress(port))
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            if (finalServerSSLContext != null) {
                                SSLEngine sslEngine = finalServerSSLContext.createSSLEngine();
                                sslEngine.setUseClientMode(false);
                                sslEngine.setNeedClientAuth(false);
                                ch.pipeline().addLast(new SslHandler(sslEngine));
                            }
                            ch.pipeline().addLast(new HttpServerCodec());
                            ch.pipeline().addLast(new ChunkedWriteHandler());
                            ch.pipeline().addLast(new HttpObjectAggregator(64 * 1024));
//                            ch.pipeline().addLast(new HttpRequestHandler("/ws", serverRuntime));
                            ch.pipeline().addLast(new WebSocketServerProtocolHandler("/ws","v11.stomp"));
//                            ch.pipeline().addLast(new SockJsDecoder());
//                            ch.pipeline().addLast(new SockJsEncoder());
                            ch.pipeline().addLast(new StompSubframeDecoder());
                            ch.pipeline().addLast(new StompSubframeAggregator(10 * 1024 * 1024));
                            ch.pipeline().addLast(new StompSubframeEncoder());
                            ch.pipeline().addLast(stompGroup,"stompHandler", new StompMessageHandler(serverRuntime, 0, 30000));
                            ch.pipeline().addLast(new AfterSlowHandler(serverRuntime));
                        }
                    });
            ChannelFuture sync = b.bind().sync();
            sync.channel().closeFuture().sync();
        } finally {
            acceptLoopGroup.shutdownGracefully().sync();
            rwLoopGroup.shutdownGracefully().sync();
        }
    }
}