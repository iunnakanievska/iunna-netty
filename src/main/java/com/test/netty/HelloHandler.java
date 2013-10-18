/*
 * Copyright 2013 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.test.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timer;

import java.util.concurrent.TimeUnit;

public class HelloHandler extends ChannelInboundHandlerAdapter {
	private Timer timer = new HashedWheelTimer();

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
			System.out.println("jkjkljlk" + ((HttpRequest) message).getUri());
			HelloTask helloTask = new HelloTask(context);
			timer.newTimeout(helloTask, 10, TimeUnit.SECONDS);
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