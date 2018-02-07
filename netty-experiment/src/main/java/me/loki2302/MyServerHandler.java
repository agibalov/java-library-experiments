package me.loki2302;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class MyServerHandler extends ChannelHandlerAdapter {
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
