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

import java.util.concurrent.Exchanger;

public class MyClient {
    private EventLoopGroup group;
    private Channel channel;
    private MyClientHandler myClientHandler = new MyClientHandler();

    public void start() throws InterruptedException {
        group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(myClientHandler);
                    }
                });

        channel = bootstrap.connect("localhost", 2302).sync().channel();
    }

    public void stop() {
        channel.close();
        group.shutdownGracefully();
    }

    public int addNumbers(int x, int y) throws InterruptedException {
        Exchanger<Integer> resultExchanger = new Exchanger<Integer>();
        myClientHandler.setResultExchanger(resultExchanger);

        ByteBuf byteBuf = Unpooled.buffer(8);
        byteBuf.writeInt(x);
        byteBuf.writeInt(y);
        channel.writeAndFlush(byteBuf).sync();

        return resultExchanger.exchange(0);
    }
}
