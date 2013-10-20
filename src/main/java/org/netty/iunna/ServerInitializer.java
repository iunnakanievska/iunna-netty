package org.netty.iunna;

import org.netty.iunna.handlers.DefaultRequestHandler;
import org.netty.iunna.handlers.HelloHandler;
import org.netty.iunna.handlers.RedirectHandler;
import org.netty.iunna.handlers.ServerStatusHandler;
import org.netty.iunna.handlers.TrafficHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.traffic.AbstractTrafficShapingHandler;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {
	/**
	 * Initializing pipeline by adding handlers in order
	 */
	@Override
	public void initChannel(SocketChannel channel) throws Exception {
		ChannelPipeline pipeline = channel.pipeline();

        pipeline.addLast("codec", new HttpServerCodec());
        pipeline.addLast("traffic", new TrafficHandler(
                        AbstractTrafficShapingHandler.DEFAULT_CHECK_INTERVAL));
        pipeline.addLast("hello", new HelloHandler());
        pipeline.addLast("redirect", new RedirectHandler());
        pipeline.addLast("status", new ServerStatusHandler());
        pipeline.addLast("default", new DefaultRequestHandler());
	}
}