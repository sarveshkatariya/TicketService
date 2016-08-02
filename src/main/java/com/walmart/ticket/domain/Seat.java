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

import com.walmart.ticket.constants.SeatLevel;

/**
 * Seat domain object
 * 
 * @author Sarvesh Katariya
 * @version 1.0
 * @since 08/01/2016
 * 
 */
public class Seat {

	private int				row;
	private int				seatNumber;
	private SeatLevel		seatLevel;
	private boolean			hold;
	private SeatReservation	seatReservation;
	
	/**
	 * @param row
	 * @param seatNumber
	 * @param seatLevel
	 */
	public Seat(int row, int seatNumber, SeatLevel seatLevel) {
		super();
		this.row = row;
		this.seatNumber = seatNumber;
		this.seatLevel = seatLevel;
	}

	/**
	 * @return the row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * @param row
	 *            the row to set
	 */
	public void setRow(int row) {
		this.row = row;
	}

	/**
	 * @return the seatNumber
	 */
	public int getSeatNumber() {
		return seatNumber;
	}

	/**
	 * @param seatNumber
	 *            the seatNumber to set
	 */
	public void setSeatNumber(int seatNumber) {
		this.seatNumber = seatNumber;
	}

	/**
	 * @return the seatLevel
	 */
	public SeatLevel getSeatLevel() {
		return seatLevel;
	}

	/**
	 * @param seatLevel
	 *            the seatLevel to set
	 */
	public void setSeatLevel(SeatLevel seatLevel) {
		this.seatLevel = seatLevel;
	}

	/**
	 * @return the hold
	 */
	public boolean isHold() {
		return hold;
	}

	/**
	 * @param hold
	 *            the hold to set
	 */
	public void setHold(boolean hold) {
		this.hold = hold;
	}

	/**
	 * @return the seatReservation
	 */
	public SeatReservation getSeatReservation() {
		return seatReservation;
	}

	/**
	 * @param seatReservation
	 *            the seatReservation to set
	 */
	public void setSeatReservation(SeatReservation seatReservation) {
		this.seatReservation = seatReservation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Seat [row=" + row + ", seatNumber=" + seatNumber + ", seatLevel=" + seatLevel + ", hold=" + hold
				+ ", seatReservation=" + seatReservation + "]";
	}

}
