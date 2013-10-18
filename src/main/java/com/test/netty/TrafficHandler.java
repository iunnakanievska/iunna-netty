package com.test.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;

import java.net.InetSocketAddress;

public class TrafficHandler extends ChannelTrafficShapingHandler {

	private TrafficStatistic trafficStatistic;

	public TrafficHandler(long checkInterval) {
		super(checkInterval);
		this.trafficStatistic = new TrafficStatistic();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ServerStatusHandler.addConnection(ctx.channel());
		InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel()
				.remoteAddress();
		this.trafficStatistic.setSourceIp(socketAddress.getHostName());
		this.trafficStatistic.setAccessTime(System.currentTimeMillis());
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		this.trafficStatistic.setReceivedBytes(Math.abs(this.trafficCounter
				.cumulativeReadBytes()));
		this.trafficStatistic.setSentBytes(this.trafficCounter
				.cumulativeWrittenBytes());
		long readSpeed = this.trafficCounter.cumulativeReadBytes();
		long writeSpeed = this.trafficCounter.cumulativeWrittenBytes();
		long averageSpeed = (readSpeed + writeSpeed) / 2;
		this.trafficStatistic.setSpeed(averageSpeed);
		while (!TrafficReport.getInstance().offerFirst(this.trafficStatistic)) {
			TrafficReport.getInstance().pollLast();
		}
		super.channelActive(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext context, Object message)
			throws Exception {
		if (message instanceof HttpRequest) {
			HttpRequest request = (HttpRequest) message;
			String uri = request.getUri();
			trafficStatistic.setRequestUri(uri);
			ClientsReport.incrementRequestsQuantity(this.trafficStatistic
					.getSourceIp());
		}
		super.channelRead(context, message);
	}

}
