package me.loki2302;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class App {
    public static void main(String[] args) throws InterruptedException {
        MyServer myServer = new MyServer();
        myServer.start();
        try {
            EventLoopGroup group = new NioEventLoopGroup();
            try {
                Bootstrap bootstrap = new Bootstrap();
                bootstrap
                        .group(group)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                ch.pipeline().addLast(new MyClientHandler());
                            }
                        });

                Channel channel = bootstrap.connect("localhost", 2302).await().channel();

                Thread.sleep(1000);

                channel.close();
            } finally {
                group.shutdownGracefully();
            }

        } finally {
            myServer.stop();
        }
    }

    public static class MyClientHandler extends ChannelHandlerAdapter {
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("ACTIVE");

            ByteBuf byteBuf = Unpooled.buffer(8);
            byteBuf.writeInt(123);
            byteBuf.writeInt(1);
            ctx.writeAndFlush(byteBuf);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf byteBuf = (ByteBuf)msg;
            System.out.printf("CLIENT GOT: %d", byteBuf.readInt());
        }
    }

    public static class MyServer {
        private EventLoopGroup bossGroup;
        private EventLoopGroup workerGroup;
        private Channel channel;

        public void start() throws InterruptedException {
            bossGroup = new NioEventLoopGroup();
            workerGroup = new NioEventLoopGroup();

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new MyMessageDecoder(), new MyServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            channel = serverBootstrap.bind(2302).await().channel();
        }

        public void stop() {
            channel.close();
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static class MyMessageDecoder extends ByteToMessageDecoder {
        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
            int readableBytes = in.readableBytes();
            while(readableBytes / 8 > 0) {
                out.add(in.readBytes(8));
                readableBytes -= 8;
            }
        }
    }

    public static class MyServerHandler extends ChannelHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf inByteBuf = (ByteBuf)msg;

            int a = inByteBuf.readInt();
            int b = inByteBuf.readInt();

            System.out.printf("SERVER GOT: %d %d\n", a, b);

            inByteBuf.release();

            ByteBuf outByteBuf = Unpooled.buffer(4);
            outByteBuf.writeInt(a + b);
            ctx.writeAndFlush(outByteBuf);

            System.out.println("SERVER SENT RESPONSE");
        }
    }
}
