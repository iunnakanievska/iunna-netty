package org.netty.iunna.handlers;

import io.netty.util.HashedWheelTimer;

/**
 * Timer for scheduled tasks. 
 * @author Iunna
 *
 */
public class TaskTimer {
	
	public static final HashedWheelTimer timer = new HashedWheelTimer();

}
