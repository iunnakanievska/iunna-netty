package org.netty.iunna.handlers;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.ImmediateEventExecutor;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicLong;

import org.netty.iunna.data.ClientStatistic;
import org.netty.iunna.data.ClientsReport;
import org.netty.iunna.data.ServerReport;
import org.netty.iunna.data.TrafficReport;
import org.netty.iunna.data.TrafficStatistic;

/**
 * Handler to display status information
 * @author Iunna
 *
 */
public class ServerStatusHandler extends ChannelInboundHandlerAdapter {

	private static DefaultChannelGroup allChannels = new DefaultChannelGroup(
			"netty-receiver", ImmediateEventExecutor.INSTANCE);

	public static synchronized void addConnection(Channel connection) {
		allChannels.add(connection);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		allChannels.add(ctx.channel());
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
				&& ((HttpRequest) message).getUri().equals("/status")) {
			FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
					OK, Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(
							getStatusReport(), CharsetUtil.UTF_8)));
			response.headers().set(CONTENT_TYPE, "text/html");
			response.headers().set(CONTENT_LENGTH,
					response.content().readableBytes());
			context.writeAndFlush(response).addListener(
					ChannelFutureListener.CLOSE);
		} else {
			context.fireChannelRead(message);
		}
	}

	private String getStatusReport() {
		StringBuilder reportBuilder = new StringBuilder(
				"<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\"><head>");
		reportBuilder
				.append("<title>Status</title> <style type=\"text/css\"> TABLE {width: 300px; border-collapse: collapse; } TD, TH { padding: 3px; border: 1px solid black; } TH { background: #b0e0e6; }  </style>");
		reportBuilder.append("Total connections: ")
				.append(ClientsReport.getRequestsQuantity()).append("<br>");

		reportBuilder.append("<br>Unique connections: ")
				.append(ClientsReport.getUniqueRequestsQuantity())
				.append("<br><br><br>");

		reportBuilder
				.append("<table> <tr> <th>IP</th> <th>Connections</th> <th>Last connection</th> </tr> ");
		for (Entry<String, ClientStatistic> clientEntry : ClientsReport
				.getInstance().entrySet()) {
			ClientStatistic clientStatistic = clientEntry.getValue();
			reportBuilder.append("<tr><td>").append(clientStatistic.getIp())
					.append("</td><td>")
					.append(clientStatistic.getRequestsQuantity())
					.append("</td><td>");
			Calendar calendar = GregorianCalendar.getInstance();
			calendar.setTimeInMillis(clientStatistic.getLastAccess()
					.longValue());
			reportBuilder.append(calendar.getTime().toGMTString()).append(
					"</td></tr>");
		}

		reportBuilder.append("<table/><br><br>");
		reportBuilder
				.append("<table style=\"border:2px solid pink;\"><tr><th>URI</th><th>Total redirects</th></tr>");
		for (Entry<String, AtomicLong> redirectEntry : ServerReport
				.getInstance().entrySet()) {
			reportBuilder.append("<tr>").append("<td>")
					.append(redirectEntry.getKey()).append("</td><td>")
					.append(redirectEntry.getValue()).append("</td></tr>");
		}
		reportBuilder.append("<table/><br><br>");

		reportBuilder.append("Active connections: ").append(allChannels.size())
				.append("<br><br><br>");

		reportBuilder
				.append("<table style=\"border:2px solid pink;\"> <tr>")
				.append("<th>IP</th><th>URI</th><th>Timestamp</th><th>Sent Bytes</th><th>Received Bytes</th><th>Speed</th>")
				.append("</tr>");
		for (TrafficStatistic trafficStatistic : TrafficReport.getInstance()) {
			reportBuilder.append("<tr><td>")
					.append(trafficStatistic.getSourceIp()).append("</td><td>")
					.append(trafficStatistic.getRequestUri())
					.append("</td><td>");
			Calendar calendar = GregorianCalendar.getInstance();
			calendar.setTimeInMillis(trafficStatistic.getAccessTime()
					.longValue());
			reportBuilder.append(calendar.getTime().toGMTString())
					.append("</td><td>")
					.append(trafficStatistic.getSentBytes())
					.append("</td><td>")
					.append(trafficStatistic.getReceivedBytes())
					.append("</td><td>").append(trafficStatistic.getSpeed())
					.append("</td></tr>");
		}
		reportBuilder.append("</table><br><br>");

		return reportBuilder.toString();
	}
}
