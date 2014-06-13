package me.loki2302;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.Exchanger;

public class MyClientHandler extends ChannelHandlerAdapter {
    private final Exchanger<Integer> resultExchanger;

    public MyClientHandler(Exchanger<Integer> resultExchanger) {
        this.resultExchanger = resultExchanger;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf)msg;
        // System.out.printf("read %d\n", byteBuf.readableBytes());
        int result = byteBuf.readInt();
        resultExchanger.exchange(result);
        //System.out.printf("CLIENT GOT: %d\n", byteBuf.readInt());
    }
}
