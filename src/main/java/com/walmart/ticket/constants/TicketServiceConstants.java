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
package com.walmart.ticket.constants;

/**
 * 
 * Ticket service constants
 * 
 * @author Sarvesh Katariya
 * @version 1.0
 * @since 08/01/2016
 * 
 */
public class TicketServiceConstants {

	private TicketServiceConstants() {

	}

	public static final long	TWO_MINUTES			= 2 * 60 * 1000;
	public static final long	DEFAULT_LIFETIME	= TWO_MINUTES;
	public static final long	TWO_SECONDS			= 2 * 1000;
	public static final long	THREAD_SLEEP_TIME	= TWO_SECONDS;

}
