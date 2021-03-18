package com.op;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/3/415:43
 */
public class YdwNioClientTest {

    public static void main(String[] args) throws Exception{
        Selector selector = Selector.open();

        SocketChannel c1 = SocketChannel.open();
        // 设置为非阻塞方式
        c1.configureBlocking(false);
        c1.connect(new InetSocketAddress("116.163.30.22", 8556));

//        SocketChannel c2 = SocketChannel.open();
//        c2.configureBlocking(false);
//        c2.connect(new InetSocketAddress("101.205.173.77",8556));
//
//        SocketChannel c3 = SocketChannel.open();
//        c3.configureBlocking(false);
//        c3.connect(new InetSocketAddress("1.180.74.220",8556));

        c1.register(selector, SelectionKey.OP_CONNECT);
//        c2.register(selector, SelectionKey.OP_CONNECT);
//        c3.register(selector, SelectionKey.OP_CONNECT);

        ByteBuffer b = ByteBuffer.allocate(512);

        while (true){
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey next = iterator.next();
                SocketChannel channel = (SocketChannel)next.channel();
                System.out.println("isReadable          " + next.isReadable());
                System.out.println("isWritable          " + next.isWritable());
                System.out.println("isConnectable       " + next.isConnectable());
                System.out.println("isAcceptable        " + next.isAcceptable());
                System.out.println("isValid             " + next.isValid());
                if (next.isConnectable()){
                    if (channel.isConnectionPending()) {
                        channel.finishConnect();
                        System.out.println("on connect "+ channel.getRemoteAddress().toString());
                        b.clear();
                        b.putInt(2);
                        b.putShort((short)1);
                        b.putInt(0x0301);
                        b.putInt(new Random().nextInt());
                        Long l = System.currentTimeMillis() / 1000;
                        b.putInt(l.intValue());
                        StringBuilder sb = new StringBuilder();
                        sb.append("deviceid:"+"123");
                        byte[] s = sb.toString().getBytes();
                        b.putInt(s.length);
                        b.put(s);

                        b.flip();

                        channel.write(b);

                        channel.register(selector,SelectionKey.OP_READ);
                    }
                }else if (next.isReadable()){
                    b.clear();
                    int read = channel.read(b);
                    if (read == -1){
                        next.cancel();
                    }
                    System.out.println("read "+ read);
                    b.flip();
                    if (read > 0)
                    System.out.println("on read "+ new String(b.array(), 0, read,"GBK"));
                }
                iterator.remove();
            }

        }
    }
}
