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
package org.iplass.mtp.impl.datastore.grdb.sql.queryconvert;

import org.iplass.mtp.entity.query.QueryVisitorSupport;
import org.iplass.mtp.entity.query.SubQuery;
import org.iplass.mtp.entity.query.value.primary.EntityField;
import org.iplass.mtp.impl.datastore.grdb.GRdbPropertyStoreRuntime;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbPropertyStore.GRdbPropertyStoreHandler;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.property.PrimitivePropertyHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;

class MultiPageChecker extends QueryVisitorSupport {
	
	private EntityHandler eh;
	private EntityContext ec;
	private String targetRef;

	private boolean multiPage = false;
	private boolean allPropertyUnderRef = true;
	
	MultiPageChecker(EntityHandler eh, EntityContext ec, String targetRef) {
		this.eh = eh;
		this.ec = ec;
		this.targetRef = targetRef + ".";
	}
	
	boolean isMultiPage() {
		return multiPage;
	}
	
	public boolean isAllPropertyUnderRef() {
		return allPropertyUnderRef;
	}

	@Override
	public boolean visit(EntityField entityField) {
		if (!multiPage) {
			if (entityField.getPropertyName().startsWith(targetRef)) {
				PropertyHandler ph = eh.getPropertyCascade(entityField.getPropertyName(), ec);
				if (ph != null && ph instanceof PrimitivePropertyHandler) {
					PrimitivePropertyHandler pph = (PrimitivePropertyHandler) ph;
					GRdbPropertyStoreRuntime psr = (GRdbPropertyStoreRuntime) pph.getStoreSpecProperty();
					for (GRdbPropertyStoreHandler gpsh: psr.asList()) {
						if (gpsh.getMetaData().getPageNo() > 0) {
							multiPage = true;
						}
						if (!psr.isNative() && !psr.isMulti()) {
							//native、multipleは値カラムのみのはずなのでチェックしない
							if (!gpsh.isExternalIndex() && gpsh.getIndexColName() != null) {
								if (gpsh.getMetaData().getIndexPageNo() > 0) {
									multiPage = true;
								}
							}
						}
						if (multiPage) {
							break;
						}
					}
				}
			}
		}
		
		if (!entityField.getPropertyName().startsWith(targetRef)) {
			allPropertyUnderRef = false;
		}
		
		return true;
	}
	
	@Override
	public boolean visit(SubQuery scalarSubQuery) {
		//ScalarSubQuery内のEntityFieldは未評価でOK
		return false;
	}

}
