/*
 * Copyright (C) 2023 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.tools.batch.storagespace.tableallocators;

import org.iplass.mtp.impl.datastore.grdb.StorageSpaceMap;
import org.iplass.mtp.impl.datastore.grdb.TableAllocator;

/**
 * 指定した疑似パーティション位置を割り当てる機能
 *
 * <p>
 * ストレージスペース移行のみ利用する TableAllocator です。
 * </p>
 *
 * @author SEKIGUCHI Naoya
 */
public class LocationSpecificationTableAllocator implements TableAllocator {
	/** 疑似パーティション位置の割り当て先 */
	private int allocateTo;

	/**
	 * コンストラクタ
	 * @param allocateTo 疑似パーティション位置の割り当て先
	 */
	public LocationSpecificationTableAllocator(int allocateTo) {
		this.allocateTo = allocateTo;
	}

	@Override
	public int allocate(int tenantId, String metaId, StorageSpaceMap storage) {
		return allocateTo;
	}
}
