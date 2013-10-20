package org.netty.iunna.data;

import java.util.concurrent.LinkedBlockingDeque;

public class TrafficReport {

	private static final int TRAFFIC_REPORT_ENTRIES_NUMBER = 16;

	private LinkedBlockingDeque<TrafficStatistic> trafficInfo;

	private static volatile TrafficReport trafficReport;

	private TrafficReport() {
		this.trafficInfo = new LinkedBlockingDeque<TrafficStatistic>(
				TRAFFIC_REPORT_ENTRIES_NUMBER);
	}

	public static LinkedBlockingDeque<TrafficStatistic> getInstance() {
		TrafficReport lazyReport = trafficReport;
		if (lazyReport == null) {
			synchronized (TrafficReport.class) {
				lazyReport = trafficReport;
				if (lazyReport == null) {
					trafficReport = lazyReport = new TrafficReport();
				}
			}
		}
		return trafficReport.trafficInfo;
	}
}
