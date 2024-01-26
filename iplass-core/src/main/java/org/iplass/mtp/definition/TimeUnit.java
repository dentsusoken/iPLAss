/*
 * Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
 * 
 * Unless you have purchased a commercial license,
 * the following license terms apply:
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.iplass.mtp.definition;

public enum TimeUnit {
	NANOSECONDS,
	MICROSECONDS,
	MILLISECONDS,
	SECONDS,
	MINUTES,
	HOURS,
	DAYS;

	public static java.util.concurrent.TimeUnit applyTimeUnit(TimeUnit timeunit) {

		if (timeunit == null) {
			return null;
		} else {
			switch (timeunit) {
			case NANOSECONDS:
				return java.util.concurrent.TimeUnit.NANOSECONDS;
			case MICROSECONDS:
				return java.util.concurrent.TimeUnit.MICROSECONDS;
			case MILLISECONDS:
				return java.util.concurrent.TimeUnit.MILLISECONDS;
			case SECONDS:
				return java.util.concurrent.TimeUnit.SECONDS;
			case MINUTES:
				return java.util.concurrent.TimeUnit.MINUTES;
			case HOURS:
				return java.util.concurrent.TimeUnit.HOURS;
			case DAYS:
				return java.util.concurrent.TimeUnit.DAYS;
			default:
				return null;
			}
		}
	};

	public static TimeUnit currentTimeUnit(java.util.concurrent.TimeUnit timeunit) {

		if (timeunit == null) {
			return null;
		} else {
			switch (timeunit) {
			case NANOSECONDS:
				return NANOSECONDS;
			case MICROSECONDS:
				return MICROSECONDS;
			case MILLISECONDS:
				return MILLISECONDS;
			case SECONDS:
				return SECONDS;
			case MINUTES:
				return MINUTES;
			case HOURS:
				return HOURS;
			case DAYS:
				return DAYS;
			default:
				return null;
			}
		}
	}
}
