package org.netty.iunna.handlers;

import io.netty.util.HashedWheelTimer;

/**
 * Timer for scheduled tasks. 
 * @author Iunna
 *
 */
public class TaskTimer {
	
	private static volatile TaskTimer taskTimer;
	private final HashedWheelTimer timer;

	private TaskTimer() {
		this.timer = new HashedWheelTimer();
	}

	/**
	 * 
	 * @return timer instance for scheduled tasks
	 */
	public static HashedWheelTimer getInstance() {
		TaskTimer lazyTaskTimer = taskTimer;
		if (lazyTaskTimer == null) {
			synchronized (TaskTimer.class) {
				lazyTaskTimer = taskTimer;
				if (lazyTaskTimer == null) {
					taskTimer = lazyTaskTimer = new TaskTimer();
				}
			}
		}
		return taskTimer.timer;
	}



}
