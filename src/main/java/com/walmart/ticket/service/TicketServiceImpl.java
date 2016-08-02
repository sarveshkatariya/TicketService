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
package com.walmart.ticket.service;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.lang3.StringUtils;

import com.walmart.ticket.cache.TicketHoldCache;
import com.walmart.ticket.constants.SeatLevel;
import com.walmart.ticket.domain.ExpirableObject;
import com.walmart.ticket.domain.Seat;
import com.walmart.ticket.domain.SeatHold;
import com.walmart.ticket.domain.SeatReservation;
import com.walmart.ticket.exception.InvalidVenueLevelException;
import com.walmart.ticket.exception.TicketHoldNotFoundException;
import com.walmart.ticket.util.TicketUtils;

/**
 * 
 * TicketService is a service class to find number of seats available, hold seats and reserve seats.
 * 
 * @author Sarvesh Katariya
 * @version 1.0
 * @since 08/01/2016
 *
 */
public class TicketServiceImpl implements TicketService {

	private Map<Integer, Seat[][]>	venueArrangement	= new HashMap<Integer, Seat[][]>();
	ReadWriteLock					lock				= new ReentrantReadWriteLock();
	
	public TicketServiceImpl() { 
		init();
	}

	/**
	 * Initialize the venue arrangement. In real world implementation this method won't be required.
	 * 
	 * In-memory seat held and reservation information, ideally this information will be stored in some kind of data storage e.g., Relational Database
	 * 
	 */
	public void init() {
		lock.writeLock().lock();
		try {
			SeatLevel[] seatLevels = SeatLevel.values();
			if (seatLevels != null && seatLevels.length > 0) {
				for (SeatLevel seatLevel : seatLevels) {
					Seat[][] seats = new Seat[seatLevel.getRows()][seatLevel.getNumberofSeats()];
					this.venueArrangement.put(seatLevel.getLevelId(), seats);
				}
			}
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	* The number of seats in the requested level that are neither held nor reserved
	*
	* @param venueLevel a numeric venue level identifier to limit the search
	* @return the number of tickets available on the provided level
	*/
	public int numSeatsAvailable(Optional<Integer> venueLevel) {
		int numSeatsAvailable = 0;
		lock.readLock().lock();
		try {
			if (venueLevel != null && venueLevel.isPresent()) {
				Integer levelId = venueLevel.get();
				if (levelId != null) {
					Seat[][] seats = this.venueArrangement.get(levelId);
					if (seats != null) {
						numSeatsAvailable = numSeatsAvailable + countNumberofSeatsAvailable(seats);
					} else {
						throw new InvalidVenueLevelException(levelId);
					}
				}
			} else {
				Collection<Seat[][]> levelSeats = this.venueArrangement.values();
				if (levelSeats != null && !levelSeats.isEmpty()) {
					for (Seat[][] seats : levelSeats) {
						numSeatsAvailable = numSeatsAvailable + countNumberofSeatsAvailable(seats);
					}
				}
			}
		} finally {
			lock.readLock().unlock();
		}
		return numSeatsAvailable;
	}

	/**
	 * Find and hold the best available seats for a customer. Seats are not allocated across different seat levels.
	 *
	 * @param numSeats
	 *            the number of seats to find and hold
	 * @param minLevel
	 *            the minimum venue level
	 * @param maxLevel
	 *            the maximum venue level
	 * @param customerEmail
	 *            unique identifier for the customer
	 * @return a SeatHold object identifying the specific seats and related
	 *         information
	 */
	public synchronized SeatHold findAndHoldSeats(int numSeats, Optional<Integer> minLevel, Optional<Integer> maxLevel, String customerEmail) {
		if (numSeats <= 0 || StringUtils.isEmpty(customerEmail))
			return null;

		SeatHold seatHold = null;
		boolean holdCompleted = false;
		List<SeatLevel> seatLevels = SeatLevel.getSeatLevels(minLevel, maxLevel);
		if (seatLevels != null && !seatLevels.isEmpty()) {
			lock.writeLock().lock();
			try {
				for (SeatLevel seatLevel : seatLevels) {
					if (!holdCompleted) {
						int numberofSeatsAvailable = numSeatsAvailable(Optional.of(seatLevel.getLevelId()));
						if (numberofSeatsAvailable >= numSeats) {
							seatHold = new SeatHold(TicketUtils.generateTicketHoldId(), customerEmail, new Date());
							Seat[][] seats = this.venueArrangement.get(seatLevel.getLevelId());
							if (seats != null && seats.length > 0) {
								for (int row = 0; row < seats.length; row++) {
									for (int seatNumber = 0; seatNumber < seats[row].length; seatNumber++) {
										Seat seat = seats[row][seatNumber];
										if (seat == null) {
											seat = new Seat(row, seatNumber, seatLevel);
											seats[row][seatNumber] = seat;
										}
										if (!seat.isHold()) {
											seat.setHold(true);
											seatHold.getSeats().add(seat);
										}
										if (seatHold.getSeats().size() == numSeats) {
											holdCompleted = true;
											break;
										}
									}
	
									if (holdCompleted) {
										break;
									}
								}
							}
						}
					}
				}
				if (seatHold != null) {
					TicketHoldCache.add(seatHold);
				}
			} finally {
				lock.writeLock().unlock();
			}
		}
		return seatHold;
	}

	/**
	* Commit seats held for a specific customer
	*
	* @param seatHoldId the seat hold identifier
	* @param customerEmail the email address of the customer to which the seat hold is assigned
	* @return a reservation confirmation code
	*/
	public synchronized String reserveSeats(int seatHoldId, String customerEmail) {
		String reservationId = null;
		if (seatHoldId <=0 || StringUtils.isEmpty(customerEmail))
			return null;
		
		Object object = TicketHoldCache.get(seatHoldId);
		SeatHold seatHold = (object != null && object instanceof SeatHold) ? (SeatHold) object : null;
		if (seatHold != null && customerEmail.equalsIgnoreCase(seatHold.getCustomerEmail()) && !seatHold.shouldExpire()) {
			lock.writeLock().lock();
			try {
				List<Seat> holdSeats = seatHold.getSeats();
				if (holdSeats != null && !holdSeats.isEmpty()) {
					reservationId = UUID.randomUUID().toString();
					SeatReservation seatReservation = new SeatReservation(reservationId, customerEmail, new Date());
					for (Seat holdSeat : holdSeats) {
						if (holdSeat != null) {
							Seat[][] seats = this.venueArrangement.get(holdSeat.getSeatLevel().getLevelId());
							if (seats != null && seats.length > 0) {
								Seat seat = seats[holdSeat.getRow()][holdSeat.getSeatNumber()];
								if (seat != null) {
									seat.setHold(false);
									seat.setSeatReservation(seatReservation);
								}
							}
						}
					}
				}
				TicketHoldCache.remove((ExpirableObject) object);
			} finally {
				lock.writeLock().unlock();
			}
		} else {
			throw new TicketHoldNotFoundException(seatHoldId, customerEmail);
		}
		return reservationId;
	}

	/**
	 * 
	 * Count the number os seats which are neither held nor reserved.
	 * 
	 * @param seats
	 * @return number of seat available - one which are neither held nor reserved
	 */
	private int countNumberofSeatsAvailable(Seat[][] seats) {
		int numSeatsAvailable = 0;
		if (seats != null && seats.length > 0) {
			for (int row = 0; row < seats.length; row++) {
				for (int seatNumber = 0; seatNumber < seats[row].length; seatNumber++) {
					Seat seat = seats[row][seatNumber];
					if (seat != null) {
						if (!seat.isHold() && seat.getSeatReservation() == null) {
							numSeatsAvailable = numSeatsAvailable + 1;
						}
					} else {
						numSeatsAvailable = numSeatsAvailable + 1;
					}
				}
			}
		}
		return numSeatsAvailable;
	}

}
