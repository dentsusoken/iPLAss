/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.auth.authorize.builtin.entity;

import org.iplass.mtp.entity.query.AsOf;
import org.iplass.mtp.entity.query.Having;
import org.iplass.mtp.entity.query.QueryVisitorSupport;
import org.iplass.mtp.entity.query.SubQuery;
import org.iplass.mtp.entity.query.value.primary.EntityField;

class HasReferencePropertyChecker extends QueryVisitorSupport {
	
	private boolean hasReferenceProperty = false;

	@Override
	public boolean visit(EntityField entityField) {
		if (!hasReferenceProperty && entityField.getPropertyName().contains(".")) {
			hasReferenceProperty = true;
		}
		return true;
	}
	
	@Override
	public boolean visit(SubQuery scalarSubQuery) {
		//ScalarSubQuery内のEntityFieldは未評価でOK
		return false;
	}
	
	public boolean hasReferenceProperty() {
		return hasReferenceProperty;
	}

	@Override
	public boolean visit(Having having) {
		//Having内のEntityFieldは未評価
		return false;
	}

	@Override
	public boolean visit(AsOf asOf) {
		//AsOf内のEntityFieldは未評価
		return false;
	}
	

}
