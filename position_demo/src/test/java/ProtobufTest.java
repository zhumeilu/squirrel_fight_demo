import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

/**
 * Description:
 * User: zhumeilu
 * Date: 2017/9/13
 * Time: 17:37
 */
public class ProtobufTest {


    @Test
    public void test1(){
        int i = 123;
        ByteBuf byteBuf = Unpooled.copiedBuffer("abcdef".getBytes());

//        byte b = byteBuf.readByte();
//        byte b2 = byteBuf.readByte();
        byte[] temp = new byte[4];
        byte[] temp2 = new byte[2];
        byteBuf.readBytes(temp);
        byteBuf.readBytes(temp2);

        System.out.println(new String(temp));
        System.out.println(new String(temp2));
//        System.out.println((char) temp[0]);
//        System.out.println((char)temp[1]);
//        System.out.println(b2);
    }

}
