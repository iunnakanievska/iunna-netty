package org.netty.iunna.handlers;

/**
 * Handler for redirection.
 * User will be redirected onto URL specified via request property "url"
 */
import static io.netty.handler.codec.http.HttpHeaders.Names.LOCATION;
import static io.netty.handler.codec.http.HttpResponseStatus.FOUND;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.List;
import java.util.Map;

import org.netty.iunna.data.ServerReport;

public class RedirectHandler extends ChannelInboundHandlerAdapter {

	private static final String uri = "url";

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
						.startsWith("/redirect?")) {
			HttpRequest request = (HttpRequest) message;

			QueryStringDecoder decoder = new QueryStringDecoder(
					request.getUri());
			Map<String, List<String>> requestParameters = decoder.parameters();
			String destinationUrl = "";
			if (requestParameters != null
					&& requestParameters.containsKey(uri)) {
				if (requestParameters.get(uri) != null
						&& !requestParameters.get(uri).isEmpty()) {
					destinationUrl = requestParameters.get(uri).get(0);
					if (!destinationUrl.startsWith("http:")
							&& !destinationUrl.startsWith("https:")) {
						destinationUrl = "//".concat(destinationUrl);
						System.out.println("URI:" + destinationUrl);
					}
					ServerReport.incrementRedirectQuantity(destinationUrl);
				}
			}
			FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
					FOUND);
			response.headers().set(LOCATION, destinationUrl);
			context.writeAndFlush(response).addListener(
					ChannelFutureListener.CLOSE);
		} else {
			context.fireChannelRead(message);
		}
	}

}
