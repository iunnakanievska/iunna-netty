package org.netty.iunna.data;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class ClientsReport {
	private static volatile ClientsReport clientsReport;
	private final ConcurrentHashMap<String, ClientStatistic> clientsInfo;

	private ClientsReport() {
		this.clientsInfo = new ConcurrentHashMap<String, ClientStatistic>();
	}

	public static ConcurrentHashMap<String, ClientStatistic> getInstance() {
		ClientsReport lazyReport = clientsReport;
		if (lazyReport == null) {
			synchronized (ClientsReport.class) {
				lazyReport = clientsReport;
				if (lazyReport == null) {
					clientsReport = lazyReport = new ClientsReport();
				}
			}
		}
		return clientsReport.clientsInfo;
	}

	public static void incrementRequestsQuantity(String ip) {
		ClientStatistic clientStatistic = getInstance().get(ip);
		if (clientStatistic == null) {
			getInstance().putIfAbsent(ip, new ClientStatistic(ip));
		}
		if (clientStatistic != null) {
			clientStatistic.incrementRequestsQuantity();
			clientStatistic.setLastAccess(System.currentTimeMillis());
		}
	}

	public static int getUniqueRequestsQuantity() {
		return getInstance().size();
	}

	public static Long getRequestsQuantity() {
		AtomicLong requestsQuantity = new AtomicLong(0L);
		for (Entry<String, ClientStatistic> clinetStatictic : getInstance()
				.entrySet()) {
			requestsQuantity.getAndAdd(clinetStatictic.getValue()
					.getRequestsQuantity().longValue());
		}
		return requestsQuantity.longValue();
	}

}
