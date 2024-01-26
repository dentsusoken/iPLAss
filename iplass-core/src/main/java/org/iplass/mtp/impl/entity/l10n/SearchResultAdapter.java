/*
 * Copyright (C) 2015 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.entity.l10n;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.value.primary.EntityField;

class SearchResultAdapter implements Predicate<Entity> {
	private Query orign;
	private Query trans;
	private List<Integer> index;
	private Predicate<Entity> real;

	SearchResultAdapter(Query orign, Query trans, Predicate<Entity> real) {
		this.orign = orign;
		this.trans = trans;
		this.real = real;
		index = new ArrayList<>();
		for (int i = 0; i < orign.getSelect().getSelectValues().size(); i++) {
			if (orign.getSelect().getSelectValues().get(i) instanceof EntityField) {
				EntityField oe = (EntityField) orign.getSelect().getSelectValues().get(i);
				EntityField te = (EntityField) trans.getSelect().getSelectValues().get(i);
				if (!oe.equals(te)) {
					index.add(i);
				}
			}
		}
	}

	@Override
	public boolean test(Entity dataModel) {
		if (index.size() > 0) {
			for (Integer i: index) {
				EntityField oe = (EntityField) orign.getSelect().getSelectValues().get(i);
				EntityField te = (EntityField) trans.getSelect().getSelectValues().get(i);
				Object val = dataModel.getValue(te.getPropertyName());
				if (val != null) {
					dataModel.setValue(te.getPropertyName(), null);
					dataModel.setValue(oe.getPropertyName(), val);
				}
			}
		}

		return real.test(dataModel);
	}

}
