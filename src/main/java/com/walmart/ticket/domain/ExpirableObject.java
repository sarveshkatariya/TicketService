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
package com.walmart.ticket.domain;

import com.walmart.ticket.constants.TicketServiceConstants;

/**
 * 
 * Abstract class for object which will expire.
 * 
 * @author Sarvesh Katariya
 * @version 1.0
 * @since 08/01/2016
 * 
 */
public abstract class ExpirableObject implements Comparable {

	private long				expirationTime;

	public abstract void expire();

	public abstract Object getKey();

	public ExpirableObject() {
		this(TicketServiceConstants.DEFAULT_LIFETIME);
	}

	public ExpirableObject(long timeToLive) {
		expirationTime = System.currentTimeMillis() + timeToLive;
	}

	public void setExpiration(long timeToLive) {
		expirationTime = System.currentTimeMillis() + timeToLive;
	}

	/**
	 * 
	 * Returns if the object is eligible to expiry.
	 * 
	 * @return whether the object is eligible to be expired
	 */
	public final boolean shouldExpire() {
		return System.currentTimeMillis() > expirationTime;
	}

	public int compareTo(Object object) {
		if (!(object instanceof ExpirableObject)) {
			return compareToBasedOnHashcodes(object);
		}
		ExpirableObject otherExpirableObject = (ExpirableObject) object;
		int returnValue = compare(expirationTime, otherExpirableObject.expirationTime);
		if (0 != returnValue) {
			return returnValue;
		}
		return compareToBasedOnHashcodes(object);
	}

	private int compareToBasedOnHashcodes(Object object) {
		return compare(hashCode(), object.hashCode());
	}

	private int compare(long firstValue, long secondValue) {
		if (firstValue < secondValue) {
			return 1;
		}
		if (secondValue < firstValue) {
			return -1;
		}
		return 0;
	}

}
