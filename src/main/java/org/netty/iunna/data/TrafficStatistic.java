package org.netty.iunna.data;

import java.util.concurrent.atomic.AtomicLong;

public class TrafficStatistic {

	private static AtomicLong idCounter = new AtomicLong(0L);

	private final Long id;
	private String sourceIp;
	private String requestUri;
	private AtomicLong accessTime;
	private AtomicLong sentBytes;
	private AtomicLong receivedBytes;
	private AtomicLong speed;

	public TrafficStatistic() {
		this(null, null, System.currentTimeMillis(), 0L, 0L, 0L);
	}

	public TrafficStatistic(String sourceIp, String requestUri,
			Long accessTime, Long sentBytes, Long receivedBytes, Long speed) {
		this.id = getNextId();
		this.sourceIp = sourceIp;
		this.requestUri = requestUri;
		this.accessTime = new AtomicLong(accessTime);
		this.sentBytes = new AtomicLong(sentBytes);
		this.receivedBytes = new AtomicLong(receivedBytes);
		this.speed = new AtomicLong(speed);
	}

	private static Long getNextId() {
		return idCounter.getAndIncrement();
	}

	public Long getId() {
		return id;
	}

	public String getSourceIp() {
		return sourceIp;
	}

	public void setSourceIp(String sourceIp) {
		this.sourceIp = sourceIp;
	}

	public String getRequestUri() {
		return requestUri;
	}

	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}

	public AtomicLong getAccessTime() {
		return accessTime;
	}

	public void setAccessTime(Long accessTime) {
		this.accessTime.getAndSet(accessTime);
	}

	public AtomicLong getSentBytes() {
		return sentBytes;
	}

	public void setSentBytes(Long sentBytes) {
		this.sentBytes.getAndSet(sentBytes);
	}

	public AtomicLong getReceivedBytes() {
		return receivedBytes;
	}

	public void setReceivedBytes(Long receivedBytes) {
		this.receivedBytes.getAndSet(receivedBytes);
	}

	public AtomicLong getSpeed() {
		return speed;
	}

	public void setSpeed(Long speed) {
		this.speed.getAndSet(speed);
	}

	@Override
	public int hashCode() {
		return id.intValue();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TrafficStatistic other = (TrafficStatistic) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
