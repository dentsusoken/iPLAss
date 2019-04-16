/*
 * Copyright (C) 2016 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.http;

import java.util.function.BooleanSupplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExponentialBackoff {
	
	private static final Logger logger = LoggerFactory.getLogger(ExponentialBackoff.class);
	
	public static ExponentialBackoff NO_RETRY = new ExponentialBackoff() {
		@Override
		public void execute(BooleanSupplier func) throws InterruptedException {
			func.getAsBoolean();
		}
	};
	
	private long retryIntervalMillis = 500;
	private double randomizationFactor = 0.5;
	private double multiplier = 1.5;
	private long maxIntervalMillis = 1000 * 60;//1 min.
	private long maxElapsedTimeMillis = 1000 * 60 * 5;//5 min.
	
	public long getRetryIntervalMillis() {
		return retryIntervalMillis;
	}

	public void setRetryIntervalMillis(long retryIntervalMillis) {
		this.retryIntervalMillis = retryIntervalMillis;
	}

	public double getRandomizationFactor() {
		return randomizationFactor;
	}

	public void setRandomizationFactor(double randomizationFactor) {
		this.randomizationFactor = randomizationFactor;
	}

	public double getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(double multiplier) {
		this.multiplier = multiplier;
	}

	public long getMaxIntervalMillis() {
		return maxIntervalMillis;
	}

	public void setMaxIntervalMillis(long maxIntervalMillis) {
		this.maxIntervalMillis = maxIntervalMillis;
	}

	public long getMaxElapsedTimeMillis() {
		return maxElapsedTimeMillis;
	}

	public void setMaxElapsedTimeMillis(long maxElapsedTimeMillis) {
		this.maxElapsedTimeMillis = maxElapsedTimeMillis;
	}

	/**
	 * 同期（呼び出しスレッドをブロックして）でExponentialBackoffでfuncを実行する。
	 * 処理成功した場合、funcでtrueを返却するようにする。
	 * 
	 * @param func
	 * @throws InterruptedException
	 */
	public void execute(BooleanSupplier func) throws InterruptedException {
		
		long endTime = System.currentTimeMillis() + maxElapsedTimeMillis;
		long intervalTime = retryIntervalMillis;
		while (System.currentTimeMillis() < endTime) {
			if (func.getAsBoolean()) {
				return;
			}
			
			//retry
			long sleepTime = randomized(intervalTime);
			if (logger.isDebugEnabled()) {
				logger.debug("execution failed. retry after " + sleepTime + "ms.");
			}
			Thread.sleep(sleepTime);
			intervalTime = (long) (intervalTime * multiplier);
			if (intervalTime > maxIntervalMillis) {
				intervalTime = maxIntervalMillis;
			}
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("execution failed and retry over.");
		}
	}
	
	private long randomized(long l) {
		double rv = randomizationFactor * (Math.random() * 2 - 1);
		return (long) (l * (1 + rv));
	}

}
