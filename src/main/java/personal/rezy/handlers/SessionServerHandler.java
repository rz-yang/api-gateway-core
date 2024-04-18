package personal.rezy.handlers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


// 具体的Handler实现类，进行处理
public class SessionServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final Logger logger = LoggerFactory.getLogger(SessionServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) {
        logger.info("网关收到的请求为 url: {} method: {}", fullHttpRequest.uri(), fullHttpRequest.method());

        // 返回的HTTP响应结构体
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        // 响应结构体的content内容
        response.content().writeBytes(JSON.toJSONBytes("我接受到你的请求了，然后我回你这个。另外你的url是" + fullHttpRequest.uri(), SerializerFeature.PrettyFormat));
        // 返回的HTTP头信息
        HttpHeaders headers = response.headers();
        // 设置头信息：返回类别
        headers.add(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON + "; charset=UTF-8");
        // 设置头信息：响应体长度
        headers.add(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        // 设置头信息：长连接
        headers.add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        // 设置头信息：配置跨域访问
        headers.add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        headers.add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS, "*");
        headers.add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, DELETE");
        headers.add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        // channelHandlerContext.writeAndFlush(response);
        channelHandlerContext.channel().writeAndFlush(response);
    }


}
