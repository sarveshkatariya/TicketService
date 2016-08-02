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

import java.util.Optional;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.walmart.ticket.constants.TicketServiceConstants;
import com.walmart.ticket.domain.SeatHold;
import com.walmart.ticket.exception.InvalidVenueLevelException;
import com.walmart.ticket.exception.TicketHoldNotFoundException;

/**
 * 
 * Test class to test the TicketService.
 * 
 * @author Sarvesh Katariya
 * @version 1.0
 * @since 08/01/2016
 *
 */
public class TicketServiceTest extends TestCase {
	
	TicketService ticketService;
	
	private static final String TEST_EMAIL = "sarvesh.katariya@gmail.com";
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		ticketService = new TicketServiceImpl();
	}
	
	public void testNumberofSeatsAvailable() {
		int numberOfSeatsAvailable = ticketService.numSeatsAvailable(null);
		System.out.println("Number of seats available - " + numberOfSeatsAvailable);
		Assert.assertEquals(6250, numberOfSeatsAvailable);
	}
	
	public void testNumberOfSeatsAvailablePerLevel() {
		int numberOfSeatsAvailableAtLevel1 = ticketService.numSeatsAvailable(Optional.of(1));
		int numberOfSeatsAvailableAtLevel2 = ticketService.numSeatsAvailable(Optional.of(2));
		int numberOfSeatsAvailableAtLevel3 = ticketService.numSeatsAvailable(Optional.of(3));
		int numberOfSeatsAvailableAtLevel4 = ticketService.numSeatsAvailable(Optional.of(4));
		Assert.assertEquals(1250, numberOfSeatsAvailableAtLevel1);
		Assert.assertEquals(2000, numberOfSeatsAvailableAtLevel2);
		Assert.assertEquals(1500, numberOfSeatsAvailableAtLevel3);
		Assert.assertEquals(1500, numberOfSeatsAvailableAtLevel4);
	}
	
	public void testNumberOfSeatsAvailableInvalidLevel() {
		try {
			int numberOfSeatsAvailable = ticketService.numSeatsAvailable(Optional.of(5));
			fail();
		} catch (InvalidVenueLevelException invalidVenueLevelException) {
			Assert.assertNotNull(invalidVenueLevelException);
		}
	}
	
	public void testNumberOfSeatsAvailableNegativeLevelValue() {
		try {
			int numberOfSeatsAvailable = ticketService.numSeatsAvailable(Optional.of(-1));
			fail();
		} catch (InvalidVenueLevelException invalidVenueLevelException) {
			Assert.assertNotNull(invalidVenueLevelException);
		}
	}
	
	public void testfindAndHoldSeatsWithoutLevel() {
		SeatHold seatHold = ticketService.findAndHoldSeats(10, null, null, TEST_EMAIL);
		Assert.assertNotNull(seatHold);
		Assert.assertEquals(10, seatHold.getSeats().size());
		Assert.assertNotNull(seatHold.getSeatHoldId());
		Assert.assertEquals(6240, ticketService.numSeatsAvailable(null));
	}
	
	public void testfindAndHoldSeatsWithMinLevel() {
		SeatHold seatHold = ticketService.findAndHoldSeats(20, Optional.of(2), null, TEST_EMAIL);
		Assert.assertNotNull(seatHold);
		Assert.assertEquals(20, seatHold.getSeats().size());
		Assert.assertNotNull(seatHold.getSeatHoldId());
		Assert.assertEquals(1980, ticketService.numSeatsAvailable(Optional.of(2)));
	}
	
	public void testfindAndHoldSeatsWithMinAndMaxLevel() {
		SeatHold seatHold = ticketService.findAndHoldSeats(30, Optional.of(3), Optional.of(4), TEST_EMAIL);
		Assert.assertNotNull(seatHold);
		Assert.assertEquals(30, seatHold.getSeats().size());
		Assert.assertNotNull(seatHold.getSeatHoldId());
		Assert.assertEquals(1470, ticketService.numSeatsAvailable(Optional.of(3)));
	}
	
	public void testReserveSeats() {
		SeatHold seatHold = ticketService.findAndHoldSeats(30, Optional.of(3), Optional.of(4), TEST_EMAIL);
		Assert.assertNotNull(seatHold);
		Assert.assertEquals(30, seatHold.getSeats().size());
		Assert.assertNotNull(seatHold.getSeatHoldId());
		Assert.assertEquals(1470, ticketService.numSeatsAvailable(Optional.of(3)));
		String reservationId = ticketService.reserveSeats(seatHold.getSeatHoldId(), seatHold.getCustomerEmail());
		Assert.assertNotNull(reservationId);
		Assert.assertEquals(6220, ticketService.numSeatsAvailable(null));
	}
	
	public void testReserveSeatsWithInvalidHoldId() {
		SeatHold seatHold = ticketService.findAndHoldSeats(30, Optional.of(2), Optional.of(4), TEST_EMAIL);
		Assert.assertNotNull(seatHold);
		Assert.assertEquals(30, seatHold.getSeats().size());
		Assert.assertNotNull(seatHold.getSeatHoldId());
		Assert.assertEquals(1970, ticketService.numSeatsAvailable(Optional.of(2)));
		try {
			String reservationId = ticketService.reserveSeats(1234, seatHold.getCustomerEmail());
			fail();
		} catch (TicketHoldNotFoundException ticketHoldNotFoundException) {
			Assert.assertNotNull(ticketHoldNotFoundException);
		}
		Assert.assertEquals(6220, ticketService.numSeatsAvailable(null));
	}
	
	public void testReserveSeatsWithInvalidHoldIdAndEmail() {
		SeatHold seatHold = ticketService.findAndHoldSeats(30, Optional.of(2), Optional.of(4), TEST_EMAIL);
		Assert.assertNotNull(seatHold);
		Assert.assertEquals(30, seatHold.getSeats().size());
		Assert.assertNotNull(seatHold.getSeatHoldId());
		Assert.assertEquals(1970, ticketService.numSeatsAvailable(Optional.of(2)));
		try {
			String reservationId = ticketService.reserveSeats(1234, "sarveshkatariya@gmail.com");
			fail();
		} catch (TicketHoldNotFoundException ticketHoldNotFoundException) {
			Assert.assertNotNull(ticketHoldNotFoundException);
		}
		Assert.assertEquals(6220, ticketService.numSeatsAvailable(null));
	}
	
	public void testHoldExpiration() throws InterruptedException {
		SeatHold seatHold = ticketService.findAndHoldSeats(30, null, null, TEST_EMAIL);
		Assert.assertNotNull(seatHold);
		Assert.assertEquals(6220, ticketService.numSeatsAvailable(null));
		Thread.sleep(TicketServiceConstants.DEFAULT_LIFETIME + TicketServiceConstants.TWO_SECONDS);
		Assert.assertEquals(6250, ticketService.numSeatsAvailable(null));
	}
	
	public void testReserveSeatsAfterHoldExpiration() throws InterruptedException {
		SeatHold seatHold = ticketService.findAndHoldSeats(30, null, null, TEST_EMAIL);
		Assert.assertNotNull(seatHold);
		Assert.assertEquals(30, seatHold.getSeats().size());
		Assert.assertNotNull(seatHold.getSeatHoldId());
		Assert.assertEquals(6220, ticketService.numSeatsAvailable(null));
		try {
			Thread.sleep(TicketServiceConstants.DEFAULT_LIFETIME + TicketServiceConstants.TWO_SECONDS);
			String reservationId = ticketService.reserveSeats(seatHold.getSeatHoldId(), seatHold.getCustomerEmail());
			fail();
		} catch(TicketHoldNotFoundException ticketHoldNotFoundException) {
			Assert.assertNotNull(ticketHoldNotFoundException);
		}
		Assert.assertEquals(6250, ticketService.numSeatsAvailable(null));
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
}
