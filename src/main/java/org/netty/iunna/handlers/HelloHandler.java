package org.netty.iunna.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;

import java.util.concurrent.TimeUnit;

/**
 * Handler for /hello URI.
 * Greetings message will be shown after 10 seconds.
 * @author Iunna
 *
 */
public class HelloHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ServerStatusHandler.addConnection(ctx.channel());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext context)
                    throws Exception {
            context.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object message)
                    throws Exception {
            if (message instanceof HttpRequest
                            && ((HttpRequest) message).getUri().toLowerCase()
                                            .equals("/hello")) {
                    HelloTask helloTask = new HelloTask(context);
                    TaskTimer.timer.newTimeout(helloTask, 10, TimeUnit.SECONDS);
            } else {
                    context.fireChannelRead(message);
            }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
                    throws Exception {
            cause.printStackTrace();
            ctx.close();
    }
}