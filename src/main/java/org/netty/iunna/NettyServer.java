package org.netty.iunna;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Class with configured server.
 * 
 * @author Iunna
 *
 */
public class NettyServer {

	private final int port;

	public NettyServer(int port) {
		this.port = port;
	}

	public void run() throws Exception {
		// Configure the server.
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024);
			serverBootstrap.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new ServerInitializer());

			Channel channel = serverBootstrap.bind(port).sync().channel();
			System.out.println("Web socket server started at port " + port
					+ '.');
			channel.closeFuture().sync();

		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		int port;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		} else {
			port = 8080;
		}
		NettyServer nettyServer = new NettyServer(port);
		nettyServer.run();
	}
}
