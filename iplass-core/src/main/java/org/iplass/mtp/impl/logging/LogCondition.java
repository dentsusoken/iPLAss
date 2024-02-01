/*
 * Copyright (C) 2019 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.logging;

public class LogCondition {
	
	public static String BIND_NAME_MDC = "mdc";
	public static String BIND_NAME_REQUEST = "request";
	public static String BIND_NAME_AUTH_CONTEXT = "auth";
	
	private String condition;
	private long expiresAt;
	
	private String level;
	private String loggerNamePattern;

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getLoggerNamePattern() {
		return loggerNamePattern;
	}

	public void setLoggerNamePattern(String loggerNamePattern) {
		this.loggerNamePattern = loggerNamePattern;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public long getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(long expiresAt) {
		this.expiresAt = expiresAt;
	}
	
	public LogCondition copy() {
		LogCondition copy = new LogCondition();
		copy.level = level;
		copy.loggerNamePattern = loggerNamePattern;
		copy.condition = condition;
		copy.expiresAt = expiresAt;
		return copy;
	}
}
