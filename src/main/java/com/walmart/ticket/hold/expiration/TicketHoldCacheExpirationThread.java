/*
 * Copyright 2014-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.walmart.ticket.hold.expiration;

import com.walmart.ticket.cache.TicketHoldCache;
import com.walmart.ticket.constants.TicketServiceConstants;

/**
 * 
 * Thread responsible to find and remove the expired object from cache.
 * 
 * @author Sarvesh Katariya
 * @version 1.0
 * @since 08/01/2016
 * 
 */
public class TicketHoldCacheExpirationThread extends Thread {

	private boolean				shouldKeepRunning			= true;

	public TicketHoldCacheExpirationThread() {
		this(TicketServiceConstants.THREAD_SLEEP_TIME);
	}

	public TicketHoldCacheExpirationThread(long timeToSleep) {
		super("TicketHoldCacheExpirationThread");
		setPriority(Thread.MIN_PRIORITY);
	}

	public synchronized void halt() {
		shouldKeepRunning = false;
	}

	public void run() {
		while (shouldKeepRunning()) {
			try {
				Thread.sleep(TicketServiceConstants.THREAD_SLEEP_TIME);
			} catch (InterruptedException interruptedException) {
			}
			TicketHoldCache.expireObjects();
		}
	}

	private synchronized boolean shouldKeepRunning() {
		return shouldKeepRunning;
	}
}
