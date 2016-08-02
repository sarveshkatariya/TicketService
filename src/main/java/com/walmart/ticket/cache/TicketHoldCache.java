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
package com.walmart.ticket.cache;

import java.util.Hashtable;
import java.util.SortedSet;
import java.util.TreeSet;

import com.walmart.ticket.domain.ExpirableObject;
import com.walmart.ticket.hold.expiration.TicketHoldCacheExpirationThread;

/**
 *
 * Implementation to cache ticket hold for certain period of time. A minimum priority thread is responsible to expire the object from cache.
 * 
 * It uses a Treeset, to avoid iterating through everything.
 * 
 * @author Sarvesh Katariya
 * @version 1.0
 * @since 08/01/2016
 * 
 */
public class TicketHoldCache {
	
	private static Hashtable<Object, ExpirableObject>	cachedObjects		= new Hashtable<Object, ExpirableObject>();
	private static SortedSet<ExpirableObject>			timeStampedStore	= new TreeSet<ExpirableObject>();
	private static TicketHoldCacheExpirationThread		ticketHoldCacheExpirationThread;

	// No instances allowed.
	private TicketHoldCache() {

	}

	static {
		ticketHoldCacheExpirationThread = new TicketHoldCacheExpirationThread();
		ticketHoldCacheExpirationThread.start();
	}

	/**
	 * Adds the ExpirableObject to cache.
	 * 
	 * @param expirableObject
	 */
	public synchronized static void add(ExpirableObject expirableObject) {
		Object key = expirableObject.getKey();
		if (cachedObjects.containsKey(key)) {
			return;
		}
		timeStampedStore.add(expirableObject);
		cachedObjects.put(key, expirableObject);
	}

	/**
	 * Removes the ExpirableObject from cache.
	 * 
	 * @param expirableObject
	 */
	public static synchronized void remove(ExpirableObject expirableObject) {
		timeStampedStore.remove(expirableObject);
		cachedObjects.remove(expirableObject.getKey());
	}

	/**
	 * Returns the ExpirableObject to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
	 * 
	 * @param key
	 * @return the value to which the specified key is mapped, or
     *         {@code null} if this map contains no mapping for the key
	 */
	public synchronized static ExpirableObject get(Object key) {
		return cachedObjects.get(key);
	}

	/**
	 * 
	 * Clears the cache and halts the thread which is responsible to remove the expired object from cache.
	 * 
	 */
	public synchronized static void halt() {
		cachedObjects.clear();
		timeStampedStore.clear();
		ticketHoldCacheExpirationThread.halt();
	}

	/**
	 * Expire oldest eligible object from cache.
	 * 
	 */
	public static void expireObjects() {
		while (removeOldestObject()) {}
	}

	/**
	 * 
	 * Remove the expired object from cache.
	 * 
	 * @return whether the oldest object was expired
	 */
	private static boolean removeOldestObject() {
		ExpirableObject oldestObject = getOldestObject();
		if (oldestObject != null && oldestObject.shouldExpire()) {
			remove(oldestObject);
			expire(oldestObject);
			return true;
		}
		return false;
	}

	/**
	 * 
	 * Returns the oldest object which was added to the cache
	 * 
	 * @return oldest object which was added to the cache
	 */
	private static synchronized ExpirableObject getOldestObject() {
		if (timeStampedStore != null && !timeStampedStore.isEmpty()) {
			return timeStampedStore.last();
		}
		return null;
	}

	/**
	 * 
	 * Invoke lifecycle method expiry of ExpirableObject.
	 * 
	 * @param expirableObject
	 */
	private static void expire(ExpirableObject expirableObject) {
		try {
			expirableObject.expire();
		} catch (Throwable throwable) {
		}
	}
}
