package com.test.netty;

import java.util.concurrent.atomic.AtomicLong;

public class ClientStatistic {
	private String ip;
	private AtomicLong requestsQuantity;
	private AtomicLong lastAccess;

	public ClientStatistic(String ip) {
		this(ip, new AtomicLong(1L), new AtomicLong(System.currentTimeMillis()));
	}

	public ClientStatistic(String ip, AtomicLong requestsQuantity, AtomicLong lastAccess) {
		this.ip = ip;
		this.requestsQuantity = requestsQuantity;
		this.lastAccess = lastAccess;
	}

	public String getIp() {
		return ip;
	}

	public AtomicLong getRequestsQuantity() {
		return requestsQuantity;
	}

	public void setRequestsQuantity(Long requestsQuantity) {
		this.requestsQuantity.getAndSet(requestsQuantity);
	}

	public AtomicLong getLastAccess() {
		return lastAccess;
	}

	public void setLastAccess(Long lastAccess) {
		this.lastAccess.getAndSet(lastAccess);
	}
	
	public void incrementRequestsQuantity(){
		this.requestsQuantity.getAndIncrement();
	}

	@Override
	public int hashCode() {
		return ip.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClientStatistic other = (ClientStatistic) obj;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		return true;
	}

}
