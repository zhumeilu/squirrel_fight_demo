import com.lemeng.common.Const;
import com.lemeng.common.util.ConvertUtil;
import com.lemeng.server.command.GameCommand;
import com.lemeng.server.command.UserCommand;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;

/**
 * Description:
 * User: zhumeilu
 * Date: 2017/9/20
 * Time: 14:21
 */
public class UdpClientTest {

    public void bind(String hostname,int port) throws Exception{

        //配置服务端的NIO线程组
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            Bootstrap b = new Bootstrap();
            b.group(workerGroup)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.SO_BROADCAST, true)   //支持广播
                    .option(ChannelOption.SO_RCVBUF, 1024 * 1024)// 设置UDP读缓冲区为1M
                    .option(ChannelOption.SO_SNDBUF, 1024 * 1024)// 设置UDP写缓冲区为1M
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .handler(new UdpClientChannelTest());
            //绑定端口，同步等待成功
            ChannelFuture f= b.bind(0).sync();
            Channel channel = f.channel();
//
            UserCommand.RegistRequestCommand.Builder builder = UserCommand.RegistRequestCommand.newBuilder();
            builder.setMobile("18679654496");
            builder.setPassword("123456");
            builder.setVerifyCode("1234");
            //登录
            byte[] loginCommand = ConvertUtil.getBytes((Const.RegistRequestCommand));
            channel.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(loginCommand,builder.build().toByteArray()),new InetSocketAddress(hostname,port)));


            Thread.sleep(1000);

            GameCommand.FullPlayerInfoCommand.Builder builder1 = GameCommand.FullPlayerInfoCommand.newBuilder();
            builder1.setId(1);
            builder1.setPositionX(10f);
            builder1.setPositionY(11f);
            builder1.setPositionZ(12f);
            byte[] playInfoCommand = ConvertUtil.getBytes((Const.PlayerInfoCommand));
            channel.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(playInfoCommand,builder1.build().toByteArray()),new InetSocketAddress(hostname,port)));


            //等待服务器监听端口关闭
            channel.closeFuture().await();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //优雅退出，释放线程池资源
            workerGroup.shutdownGracefully();
        }
    }


    public static void main(String[] args) throws Exception {
        int port = 6666;
        if(args!=null&&args.length>0){
            try{
                port = Integer.valueOf(args[0]);
            }catch (NumberFormatException e){

            }
        }
//        String hostname ="127.0.0.1";
        String hostname = "47.92.114.52";
        new UdpClientTest().bind(hostname,port);

    }
}
