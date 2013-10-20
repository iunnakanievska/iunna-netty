package org.netty.iunna.handlers;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders.Values;
import io.netty.handler.codec.http.HttpRequest;

/**
 * Handler for unexpected request.
 * User should get help message in this case.
 * @author Iunna
 *
 */
public class DefaultRequestHandler extends ChannelInboundHandlerAdapter {
	
	/**
	 * Help message for user
	 */
	private static final byte[] helpMessage;

	static {
		String message = "Dear user,<br>"
				+ "Seems like you make some mistake during choosing of the request.<br>"
				+ "Please, use one of the allowed requests:<br>"
				+ "<a title=\"hello\" href=\"http://localhost:8080/hello\">hello</a> - to get greeting message<br>"
				+ "redirect?url=<url> - to be redirected onto <url><br>"
				+ "<a title=\"status\" href=\"http://localhost:8080/status\">status</a> - to get statistic";
		helpMessage = message.getBytes();
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext context)
			throws Exception {
		context.flush();
	}

	@Override
	public void channelRead(ChannelHandlerContext context, Object message)
			throws Exception {
		if (message instanceof HttpRequest) {
			FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK,
					Unpooled.wrappedBuffer(helpMessage));
			response.headers().set(CONTENT_TYPE, "text/html");
			response.headers().set(CONTENT_LENGTH,
					response.content().readableBytes());
			response.headers().set(CONNECTION, Values.KEEP_ALIVE);
			context.writeAndFlush(response)
					.addListener(ChannelFutureListener.CLOSE);
		}
	}

}
