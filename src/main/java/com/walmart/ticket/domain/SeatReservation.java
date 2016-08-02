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

import java.util.Date;

/**
 * Seat Reservation domain object
 * 
 * @author Sarvesh Katariya
 * @version 1.0
 * @since 08/01/2016
 * 
 */
public class SeatReservation {

	private String	reservationId;
	private String	customerEmail;
	private Date	reservationDate;
	
	/**
	 * @param reservationId
	 * @param customerEmail
	 * @param reservationDate
	 */
	public SeatReservation(String reservationId, String customerEmail, Date reservationDate) {
		super();
		this.reservationId = reservationId;
		this.customerEmail = customerEmail;
		this.reservationDate = reservationDate;
	}

	/**
	 * @return the reservationId
	 */
	public String getReservationId() {
		return reservationId;
	}

	/**
	 * @param reservationId
	 *            the reservationId to set
	 */
	public void setReservationId(String reservationId) {
		this.reservationId = reservationId;
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
	 * @return the reservationDate
	 */
	public Date getReservationDate() {
		return reservationDate;
	}

	/**
	 * @param reservationDate
	 *            the reservationDate to set
	 */
	public void setReservationDate(Date reservationDate) {
		this.reservationDate = reservationDate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SeatReservation [reservationId=" + reservationId + ", customerEmail=" + customerEmail
				+ ", reservationDate=" + reservationDate + "]";
	}

}
