package com.test.netty;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class ServerReport {

	private static volatile ServerReport serverReport;
	private final ConcurrentHashMap<String, AtomicLong> serverInfo;

	private ServerReport() {
		this.serverInfo = new ConcurrentHashMap<String, AtomicLong>();
	}

	public static ConcurrentHashMap<String, AtomicLong> getInstance() {
		ServerReport lazyReport = serverReport;
		if (lazyReport == null) {
			synchronized (ServerReport.class) {
				lazyReport = serverReport;
				if (lazyReport == null) {
					serverReport = lazyReport = new ServerReport();
				}
			}
		}
		return serverReport.serverInfo;
	}

	public static void incrementRedirectQuantity(String url) {
		AtomicLong value = getInstance().get(url);
		if (value == null) {
			value = getInstance().putIfAbsent(url, new AtomicLong(1));
		}
		if (value != null) {
			value.incrementAndGet();
		}
	}

}
