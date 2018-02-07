package me.loki2302;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.Exchanger;

public class MyClientHandler extends ChannelHandlerAdapter {
    private Exchanger<Integer> resultExchanger;

    public void setResultExchanger(Exchanger<Integer> resultExchanger) {
        this.resultExchanger = resultExchanger;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf)msg;
        int result = byteBuf.readInt();
        resultExchanger.exchange(result);
    }
}
