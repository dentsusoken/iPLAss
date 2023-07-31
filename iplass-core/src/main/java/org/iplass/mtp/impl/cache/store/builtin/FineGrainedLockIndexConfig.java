/*
 * Copyright (C) 2023 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.cache.store.builtin;

public class FineGrainedLockIndexConfig {
	private int shardSize = 1;
	private boolean fair;

	public int getShardSize() {
		return shardSize;
	}
	public void setShardSize(int shardSize) {
		this.shardSize = shardSize;
	}
	public boolean isFair() {
		return fair;
	}
	public void setFair(boolean fair) {
		this.fair = fair;
	}
	
	public FineGrainedLockIndex build() {
		return new FineGrainedLockIndex(shardSize, fair);
	}

}
