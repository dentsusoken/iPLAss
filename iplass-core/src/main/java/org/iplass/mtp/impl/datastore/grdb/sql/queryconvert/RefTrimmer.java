/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.datastore.grdb.sql.queryconvert;

import org.iplass.mtp.entity.query.ASTNode;
import org.iplass.mtp.entity.query.ASTTransformerSupport;
import org.iplass.mtp.entity.query.SubQuery;
import org.iplass.mtp.entity.query.value.primary.EntityField;

class RefTrimmer extends ASTTransformerSupport {
	
	String ref;
	
	RefTrimmer(String refName) {
		this.ref = refName + ".";
	}

	@Override
	public ASTNode visit(EntityField entityField) {
		if (entityField.getPropertyName().startsWith(ref)) {
			return new EntityField(entityField.getPropertyName().substring(ref.length()));
		} else {
			return entityField;
		}
	}

	@Override
	public ASTNode visit(SubQuery subQuery) {
		return subQuery;
	}

}
