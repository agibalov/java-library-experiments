package me.loki2302;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.Test;

import java.util.concurrent.Exchanger;

import static org.junit.Assert.assertEquals;

public class DummyTest {
    @Test
    public void dummy() throws InterruptedException {
        MyServer myServer = new MyServer();
        myServer.start();
        try {
            EventLoopGroup group = new NioEventLoopGroup();
            try {
                final Exchanger<Integer> resultExchanger = new Exchanger<Integer>();

                Bootstrap bootstrap = new Bootstrap();
                bootstrap
                        .group(group)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                ch.pipeline().addLast(new MyClientHandler(resultExchanger));
                            }
                        });

                Channel channel = bootstrap.connect("localhost", 2302).sync().channel();

                ByteBuf byteBuf = Unpooled.buffer(8);
                byteBuf.writeInt(123);
                byteBuf.writeInt(1);
                channel.writeAndFlush(byteBuf).sync();

                System.out.println("sent");

                int result = resultExchanger.exchange(0);
                System.out.printf("Got result: %d\n", result);

                assertEquals(124, result);

                channel.close();
            } finally {
                group.shutdownGracefully();
            }

        } finally {
            myServer.stop();
        }
    }
}
