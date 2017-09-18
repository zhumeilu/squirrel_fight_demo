import com.google.protobuf.InvalidProtocolBufferException;
import com.lemeng.proto.BaseCommand;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import java.util.List;

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

    @Test
    public void test2() throws InvalidProtocolBufferException {

        BaseCommand.GameStartCommand.Builder builder = BaseCommand.GameStartCommand.newBuilder();
        BaseCommand.PositionCommand.Builder builder1 = BaseCommand.PositionCommand.newBuilder();
        builder1.setPositionZ(1f);
        builder1.setPositionY(1f);
        builder1.setPositionX(1f);
        builder1.setId(1);
        builder.addPositionList(builder1.build());

        BaseCommand.GameStartCommand build = builder.build();
        ByteBuf byteBuf = Unpooled.copiedBuffer(build.toByteArray());

        byte[] commandByte = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(commandByte);
        BaseCommand.GameStartCommand gameStartCommand = BaseCommand.GameStartCommand.parseFrom(commandByte);
        List<BaseCommand.PositionCommand> positionListList = gameStartCommand.getPositionListList();
        for (BaseCommand.PositionCommand positionCommand:positionListList) {
            System.out.println(positionCommand.getId());
            System.out.println(positionCommand.getPositionX());

        }
    }
}
