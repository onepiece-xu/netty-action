package com.op;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/3/2 11:07
 */
public class Client {

    EventLoopGroup group = new NioEventLoopGroup();
    AtomicInteger atomicInteger = new AtomicInteger(0);
    Bootstrap b = new Bootstrap();

    {
        b.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 15000)
                .handler(new ChannelInitializer<SocketChannel>() {

                    /**
                     * This method will be called once the {@link Channel} was registered. After the method returns this instance
                     * will be removed from the {@link ChannelPipeline} of the {@link Channel}.
                     *
                     * @param ch the {@link Channel} which was registered.
                     * @throws Exception is thrown if an error occurs. In that case the {@link Channel} will be closed.
                     */
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        //这里实际上用到了LengthFieldBasedFrameDecoder
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 16,
                                4,0,20));
//                            pipeline.addLast("ReadTimeoutHandler", new IdleStateHandler()Handler(50));
//                            pipeline.addLast("LoginAuthHandler", new LoginAuthReqHandler());
//                            pipeline.addLast(new MyClientHandler2(deviceId));
                            pipeline.addLast(new MyClientHandler());
                    }
                });
    }

    public void connect(Bootstrap b, final String host, final int port, String deviceId){
        try {
            String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy MM dd HH:mm:ss SSS"));
            atomicInteger.addAndGet(1);
            System.out.println("host:" + host + " port:" + port + " 开始连接时间 "+format);
            ChannelFuture connect = b.connect(host, port);
            connect.awaitUninterruptibly();
            if (connect.isSuccess()){
                System.out.println("host:" + host + " port:" + port +" 连接成功！");
            }else {
                format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy MM dd HH:mm:ss SSS"));
                System.out.println("host:" + host + " port:" + port +" 连接失败！" + format);
                connect.cause().printStackTrace();
            }
            connect.channel().closeFuture().sync();
            System.out.println("client close");
        } catch (Exception e){
            String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy MM dd HH:mm:ss SSS"));
            System.out.println(format);
            e.printStackTrace();
        } finally {

        }
    }

    static class ConnectVO{
        public String ip;
        public int port = 8556;
        public String id;

        public ConnectVO(String ip, int port, String id) {
            this.ip = ip;
            this.port = port;
            this.id = id;
        }
    }

    public static void main(String[] args) throws Exception {
        Client client = new Client();
        List<ConnectVO> list = new ArrayList<>();
//        list.add(new ConnectVO("192.168.103.41", 8556,"6723432240988411815"));
//        list.add(new ConnectVO("192.168.103.42", 8556,"6723432241105852328"));
//        list.add(new ConnectVO("192.168.104.43", 8556,"6723432241206515625"));
//        list.add(new ConnectVO("192.168.104.44", 8556,"6723432241311373226"));
//        list.add(new ConnectVO("192.168.104.45", 8556,"6723432241407842219"));
//        list.add(new ConnectVO("192.168.104.46", 8556,"6723432241504311212"));

        list.add(new ConnectVO("116.163.30.22", 8556,"6770281333287352650"));
        list.add(new ConnectVO("101.205.173.77", 8556,"6770280404274182472"));
        list.add(new ConnectVO("1.180.74.220", 8556,"6770279011484236102"));
        for (int i = 0 ; i < 3 ; i ++){
            ConnectVO connectVO = list.get(i % 3);
            client.connect(client.b, connectVO.ip, connectVO.port,connectVO.id);
        }
//        client.connect("192.168.103.41", 8556,"6723432240988411815");
//        client.connect("192.168.103.42", 8556,"6723432241105852328");
//        client.connect("192.168.104.43", 8556,"6723432241206515625");
//        client.connect("192.168.104.44", 8556,"6723432241311373226");
//        client.connect("192.168.104.45", 8556,"6723432241407842219");
//        client.connect("192.168.104.46", 8556,"6723432241504311212");
//        client.connect("192.168.105.47", 8556,"6723432240573175715");
//        client.connect("192.168.105.48", 8556,"6723432240673839012");
//        client.connect("192.168.105.49", 8556,"6723432240778696613");
//        client.connect("192.168.105.50", 8556,"6723432240875165606");
        int i = client.atomicInteger.get();
        System.out.println(i);
        Thread.sleep(10000);
        client.group.shutdownGracefully();
        System.out.println("end");
    }
}
