package org.netty.iunna.handlers;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders.Values;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;

public class HelloTask implements TimerTask {
	private ChannelHandlerContext context;
	private static final byte[] greetings = "Hello, World!".getBytes();

	public HelloTask(ChannelHandlerContext context) {
		this.context = context;
	}

	@Override
	public void run(Timeout timeout) throws Exception {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK,
				Unpooled.wrappedBuffer(greetings));
		response.headers().set(CONTENT_TYPE, "text/plain");
		response.headers().set(CONTENT_LENGTH,
				response.content().readableBytes());
		response.headers().set(CONNECTION, Values.KEEP_ALIVE);
		context.writeAndFlush(response)
				.addListener(ChannelFutureListener.CLOSE);
	}

}
