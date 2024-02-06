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

package org.iplass.mtp.impl.view.generic.editor;

import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.entity.property.ReferencePropertyHandler;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.view.generic.editor.ReferenceRecursiveTreeSetting;

public class MetaReferenceRecursiveTreeSetting implements MetaData {

	private static final long serialVersionUID = 7607433679491145837L;

	/** ツリーのルートに表示するデータの検索条件 */
	private String rootCondition;

	/** 子階層のプロパティID */
	private String childPropertyId;

	/**
	 * ツリーのルートに表示するデータの検索条件を取得します。
	 * @return ツリーのルートに表示するデータの検索条件
	 */
	public String getRootCondition() {
	    return rootCondition;
	}

	/**
	 * ツリーのルートに表示するデータの検索条件を設定します。
	 * @param rootCondition ツリーのルートに表示するデータの検索条件
	 */
	public void setRootCondition(String rootCondition) {
	    this.rootCondition = rootCondition;
	}

	/**
	 * 子階層のプロパティIDを取得します。
	 * @return 子階層のプロパティID
	 */
	public String getChildPropertyId() {
	    return childPropertyId;
	}

	/**
	 * 子階層のプロパティIDを設定します。
	 * @param childPropertyId 子階層のプロパティID
	 */
	public void setChildPropertyId(String childPropertyId) {
	    this.childPropertyId = childPropertyId;
	}

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

	public void applyConfig(ReferenceRecursiveTreeSetting setting, EntityHandler entity) {
		EntityContext ctx = EntityContext.getCurrentContext();

		PropertyHandler property = entity.getProperty(setting.getChildPropertyName(), ctx);
		if (property == null && !(property instanceof ReferencePropertyHandler)) return;

		//プロパティのIDを取得
		ReferencePropertyHandler referenceProperty = (ReferencePropertyHandler) property;

		rootCondition = setting.getRootCondition();
		childPropertyId = referenceProperty.getId();
	}

	public ReferenceRecursiveTreeSetting currentConfig(EntityHandler entity) {
		EntityContext ctx = EntityContext.getCurrentContext();

		PropertyHandler property = entity.getPropertyById(childPropertyId, ctx);
		if (property == null && !(property instanceof ReferencePropertyHandler)) return null;

		//プロパティ名を取得
		ReferencePropertyHandler referenceProperty = (ReferencePropertyHandler) property;

		ReferenceRecursiveTreeSetting setting = new ReferenceRecursiveTreeSetting();
		setting.setRootCondition(rootCondition);
		setting.setChildPropertyName(referenceProperty.getName());
		return setting;
	}

}
