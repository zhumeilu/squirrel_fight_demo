import com.lemeng.common.SystemManager;
import com.lemeng.server.bootstrap.ShutdownHook;
import com.lemeng.server.channel.SquirrelFightTcpChannelInitializer;
import com.lemeng.server.channel.SquirrelFightUdpChannelInitializer;
import com.lemeng.server.decoder.TcpMessageDecoder;
import com.lemeng.server.encoder.TcpMessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Description:
 * User: zhumeilu
 * Date: 2017/9/18
 * Time: 19:02
 */
public class TcpClientTest {


    void startTcpService(String hostname,int port){

        //配置服务端的NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        try{
            Bootstrap b = new Bootstrap();
            b.group(bossGroup)
                    .channel(NioSocketChannel.class)
                    //BACKLOG用于构造服务端套接字ServerSocket对象，标识当服务器请求处理线程全满时，用于临时存放已完成三次握手的请求的队列的最大长度。如果未设置或所设置的值小于1，Java将使用默认值50。
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            int maxLength = Integer.MAX_VALUE;
                            pipeline.addLast("frame", new LengthFieldBasedFrameDecoder(maxLength, 2, 4, 3, 0));
                            pipeline.addLast(new TcpMessageDecoder());
                            pipeline.addLast(new TcpMessageEncoder());
                            pipeline.addLast(new TcpClientChannelTest());
                        }
                    });
            //绑定端口，同步等待成功
            ChannelFuture f= b.connect(hostname,port).sync();
            //等待服务器监听端口关闭
            f.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
        }

    }


    public static void main(String[] args) throws Exception {
        int tcpPort = 7777;
        String hostname = "127.0.0.1";
        TcpClientTest squirrelFightServer = new TcpClientTest();
        squirrelFightServer.startTcpService(hostname,tcpPort);
    }
}
