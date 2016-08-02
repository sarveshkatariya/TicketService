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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 
 * SeatLevel is an enum type that represents the different Seat Levels for the Venue.
 * 
 * @author Sarvesh Katariya
 * @version 1.0
 * @since 08/01/2016
 * 
 */
public enum SeatLevel {
	
	ORCHESTRA(1, "Orchestra", new Double(100), 25, 50),
	MAIN(2, "Main", new Double(75), 20, 100),
	BALCONY1(3, "Balcony 1", new Double(50), 15, 100),
	BALCONY2(4, "Balcony 2", new Double(40), 15, 100);

	private final Integer	levelId;
	private final String	levelName;
	private final Double	price;
	private final Integer	rows;
	private final Integer	numberofSeats;

	/**
	 * @param levelId
	 * @param levelName
	 * @param price
	 * @param rows
	 * @param numberofSeats
	 */
	private SeatLevel(Integer levelId, String levelName, Double price, Integer rows, Integer numberofSeats) {
		this.levelId = levelId;
		this.levelName = levelName;
		this.price = price;
		this.rows = rows;
		this.numberofSeats = numberofSeats;
	}

	/**
	 * @return the levelId
	 */
	public Integer getLevelId() {
		return levelId;
	}

	/**
	 * @return the levelName
	 */
	public String getLevelName() {
		return levelName;
	}

	/**
	 * @return the price
	 */
	public Double getPrice() {
		return price;
	}

	/**
	 * @return the rows
	 */
	public Integer getRows() {
		return rows;
	}

	/**
	 * @return the numberofSeats
	 */
	public Integer getNumberofSeats() {
		return numberofSeats;
	}
	
	/**
	 * 
	 * Find all levels between minLevel and maxLevel
	 * 
	 * @param minLevel the minimum venue level
	 * @param maxLevel the maximum venue level
	 * @return list of SeatLevel
	 */
	public static List<SeatLevel> getSeatLevels(Optional<Integer> minLevel, Optional<Integer> maxLevel) {
		List<SeatLevel> seatLevels = new ArrayList<SeatLevel>();
		Integer min = (minLevel != null && minLevel.isPresent()) ? minLevel.get() : 1;
		Integer max = (maxLevel != null && maxLevel.isPresent()) ? maxLevel.get() : 4;
		
		for(SeatLevel seatLevel : SeatLevel.values()) {
			if(seatLevel.getLevelId() >= min && seatLevel.getLevelId() <= max) {
				seatLevels.add(seatLevel);
			}
		}
		return seatLevels;
	}

}
