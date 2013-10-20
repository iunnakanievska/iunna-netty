package org.netty.iunna.data;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 
 * @author Iunna
 * 
 *         Storage for the redirections statistic.
 * 
 */
public class ServerReport {

	private static volatile ServerReport serverReport;
	private final ConcurrentHashMap<String, AtomicLong> serverInfo;

	private ServerReport() {
		this.serverInfo = new ConcurrentHashMap<String, AtomicLong>();
	}

	/**
	 * 
	 * @return storage with redirections statistic
	 */
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

	/**
	 * 
	 * @param url URL which redirections quantity should be increased
	 */
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
