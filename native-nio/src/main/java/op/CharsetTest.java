package op;

import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/3/510:49
 */
public class CharsetTest {

    public static void main(String[] args) throws Exception{
        RandomAccessFile fis = new RandomAccessFile("test1.txt","r");
        RandomAccessFile fos = new RandomAccessFile("test2.txt","rw");

        FileChannel inchannel = fis.getChannel();
        FileChannel outchannel = fos.getChannel();

        ByteBuffer b = ByteBuffer.allocate(512);
        inchannel.read(b);

        Charset charset = Charset.forName("ASCII");
        CharsetDecoder decoder = charset.newDecoder();
        CharsetEncoder encoder = charset.newEncoder();

        CharBuffer decode = decoder.decode(b);
        encoder.encode(decode);

        b.flip();
        outchannel.write(b);

        inchannel.close();
        outchannel.close();
    }
}
