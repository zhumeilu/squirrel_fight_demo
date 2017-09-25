import com.lemeng.common.Const;
import com.lemeng.server.command.UserCommand;
import com.lemeng.server.message.SquirrelFightTcpMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Description:
 * User: zhumeilu
 * Date: 2017/9/22
 * Time: 10:56
 */
public class TcpClientChannelTest extends SimpleChannelInboundHandler<SquirrelFightTcpMessage> {
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, SquirrelFightTcpMessage squirrelFightTcpMessage) throws Exception {
        short cmd = squirrelFightTcpMessage.getCmd();
        int length = squirrelFightTcpMessage.getLength();
        System.out.println("----------读取到消息：cmd:"+cmd+"-----length:"+length);

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        System.out.println("-------channel激活--------");
//        UserCommand.LoginRequestCommand.Builder builder = UserCommand.LoginRequestCommand.newBuilder();
//        builder.setMobile("zml");
//        builder.setPassword("123");
//        builder.setType(3);
//        byte[] body = builder.build().toByteArray();
//        SquirrelFightTcpMessage tcpMessage = new SquirrelFightTcpMessage();
//        tcpMessage.setLength(body.length);
//        tcpMessage.setCmd(Const.LoginCommand);
//        tcpMessage.setBody(body);
//        ctx.channel().writeAndFlush(tcpMessage);
    }
}
