package personal.rezy;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public abstract class BaseHandler<T> extends SimpleChannelInboundHandler<T> {

    // 接收信息和ctx业务数据，并处理
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, T msg) {
        session(ctx, msg, ctx.channel());

    }

    // 处理的输出结果到ctx的channel里边
    protected abstract void session(ChannelHandlerContext ctx, T requset, final Channel chan);
}
