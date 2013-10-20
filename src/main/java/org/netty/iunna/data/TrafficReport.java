package org.netty.iunna.data;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * 
 * @author Iunna
 * 
 *         Storage for {@link TrafficStatistic traffic statistic}.
 * 
 */
public class TrafficReport {

	/**
	 * Number of records in report
	 */
	private static final int TRAFFIC_REPORT_ENTRIES_NUMBER = 16;

	private LinkedBlockingDeque<TrafficStatistic> trafficInfo;

	private static volatile TrafficReport trafficReport;

	private TrafficReport() {
		this.trafficInfo = new LinkedBlockingDeque<TrafficStatistic>(
				TRAFFIC_REPORT_ENTRIES_NUMBER);
	}

	/**
	 * 
	 * @return storage with traffic statistic
	 */
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
