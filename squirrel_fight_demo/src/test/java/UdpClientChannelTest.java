import com.lemeng.common.util.ConvertUtil;
import com.lemeng.server.command.GameCommand;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

/**
 * Description:
 * User: zhumeilu
 * Date: 2017/9/25
 * Time: 16:22
 */
public class UdpClientChannelTest extends SimpleChannelInboundHandler<DatagramPacket>{


    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {
        ByteBuf content = datagramPacket.content();
        byte[] cmd = new byte[2];
        content.readBytes(cmd);
        short aShort = ConvertUtil.getShort(cmd);
        System.out.println("cmd"+aShort);
        byte[] body = new byte[content.readableBytes()];
        content.readBytes(body);
        GameCommand.PlayerInfoCommand playerInfoCommand = GameCommand.PlayerInfoCommand.parseFrom(body);
        int id = playerInfoCommand.getId();
        float positionY = playerInfoCommand.getPositionY();
        float positionX = playerInfoCommand.getPositionX();
        float positionZ = playerInfoCommand.getPositionZ();
        System.out.println("-------id:"+id);
        System.out.println("-------positionY:"+positionY);
        System.out.println("-------positionX:"+positionX);
        System.out.println("-------positionZ:"+positionZ);

    }
}
