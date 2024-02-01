/*
 * Copyright (C) 2022 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.fulltextsearch;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.fulltextsearch.FulltextSearchCondition;
import org.iplass.mtp.impl.entity.EntityHandler;

public class TempEntityList {
	EntityHandler eh;
	List<IndexedEntity> oids = new ArrayList<>();
	FulltextSearchCondition cond;
	
	public TempEntityList(EntityHandler eh, FulltextSearchCondition cond) {
		this.eh = eh;
		this.cond = cond;
	}

	public EntityHandler getEh() {
		return eh;
	}

	public List<IndexedEntity> getOids() {
		return oids;
	}

	public FulltextSearchCondition getCond() {
		return cond;
	}

	public void addOids(IndexedEntity indexedEntity) {
		oids.add(indexedEntity);
	}
}
