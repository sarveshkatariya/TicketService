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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * SeatHold represents the seat hold.
 * 
 * @author Sarvesh Katariya
 * @version 1.0
 * @since 08/01/2016
 * 
 */
public class SeatHold extends ExpirableObject {

	private int			seatHoldId;
	private String		customerEmail;
	private Date		holdTime;
	private List<Seat>	seats;

	/**
	 * @param seatHoldId
	 */
	public SeatHold(int seatHoldId) {
		super();
		this.seatHoldId = seatHoldId;
	}

	/**
	 * @param seatHoldId
	 * @param customerEmail
	 */
	public SeatHold(int seatHoldId, String customerEmail, Date holdTime) {
		super();
		this.seatHoldId = seatHoldId;
		this.customerEmail = customerEmail;
		this.holdTime = holdTime;
	}

	/**
	 * @return the seatHoldId
	 */
	public int getSeatHoldId() {
		return seatHoldId;
	}

	/**
	 * @param seatHoldId
	 *            the seatHoldId to set
	 */
	public void setSeatHoldId(int seatHoldId) {
		this.seatHoldId = seatHoldId;
	}

	/**
	 * @return the customerEmail
	 */
	public String getCustomerEmail() {
		return customerEmail;
	}

	/**
	 * @param customerEmail
	 *            the customerEmail to set
	 */
	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	/**
	 * @return the seats
	 */
	public List<Seat> getSeats() {
		if (this.seats == null) {
			this.seats = new ArrayList<Seat>();
		}
		return this.seats;
	}

	/**
	 * @return the holdTime
	 */
	public Date getHoldTime() {
		return holdTime;
	}

	/**
	 * @param holdTime
	 *            the holdTime to set
	 */
	public void setHoldTime(Date holdTime) {
		this.holdTime = holdTime;
	}

	@Override
	public void expire() {
		if (this.seats != null && !this.seats.isEmpty()) {
			for (Seat seat : this.seats) {
				seat.setHold(false);
			}
		}
	}

	@Override
	public Object getKey() {
		return this.getSeatHoldId();
	}

	@Override
	public int hashCode() {
		return this.seatHoldId;
	}

	@Override
	public boolean equals(Object object) {
		// If the object is compared with itself then return true
		if (object == this) {
			return true;
		}

		/*
		 * Check if object is an instance of Complex or not
		 * "null instanceof [type]" also returns false
		 */
		if (!(object instanceof SeatHold)) {
			return false;
		}

		// typecast object to SeatHold so that we can compare data members
		SeatHold seatHold = (SeatHold) object;

		// Compare the data members and return accordingly
		return this.getSeatHoldId() == seatHold.getSeatHoldId();
	}

	@Override
	public String toString() {
		return "SeatHold [seatHoldId=" + seatHoldId + ", customerEmail=" + customerEmail + ", holdTime=" + holdTime
				+ ", seats=" + seats + "]";
	}

}
