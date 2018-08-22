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

package org.iplass.mtp.impl.view.generic.editor;

import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.entity.property.ReferencePropertyHandler;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.view.generic.editor.ReferenceComboSetting;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.RefSortType;

/**
 *
 * @author lis3wg
 */
public class MetaReferenceComboSetting implements MetaData {

	/** SerialVersionUID */
	private static final long serialVersionUID = 2605708983437732896L;

	/** プロパティID */
	private String propertyId;

	/** 検索条件 */
	private String condition;

	/** 上位参照コンボ設定 */
	private MetaReferenceComboSetting parent;

	private String sortItem;

	private RefSortType sortType;

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

	/**
	 * プロパティIDを取得します。
	 * @return プロパティID
	 */
	public String getPropertyId() {
	    return propertyId;
	}

	/**
	 * プロパティIDを設定します。
	 * @param propertyId プロパティID
	 */
	public void setPropertyId(String propertyId) {
	    this.propertyId = propertyId;
	}

	/**
	 * 検索条件を取得します。
	 * @return 検索条件
	 */
	public String getCondition() {
	    return condition;
	}

	/**
	 * 検索条件を設定します。
	 * @param condition 検索条件
	 */
	public void setCondition(String condition) {
	    this.condition = condition;
	}

	/**
	 * 上位参照コンボ設定を取得します。
	 * @return 上位参照コンボ設定
	 */
	public MetaReferenceComboSetting getParent() {
	    return parent;
	}

	/**
	 * 上位参照コンボ設定を設定します。
	 * @param parent 上位参照コンボ設定
	 */
	public void setParent(MetaReferenceComboSetting parent) {
	    this.parent = parent;
	}

	public String getSortItem() {
		return sortItem;
	}

	public void setSortItem(String sortItem) {
		this.sortItem = sortItem;
	}

	public RefSortType getSortType() {
		return sortType;
	}

	public void setSortType(RefSortType sortType) {
		this.sortType = sortType;
	}

	public void applyConfig(ReferenceComboSetting setting, EntityHandler entity) {
		EntityContext ctx = EntityContext.getCurrentContext();

		PropertyHandler property = entity.getProperty(setting.getPropertyName(), ctx);
		if (property == null && !(property instanceof ReferencePropertyHandler)) return;

		//プロパティのIDを取得
		ReferencePropertyHandler referenceProperty = (ReferencePropertyHandler) property;

		this.propertyId = referenceProperty.getId();
		this.condition = setting.getCondition();

		if (setting.getSortItem() != null && !setting.getSortItem().isEmpty()) {
			PropertyHandler sortProperty = referenceProperty.getReferenceEntityHandler(ctx)
					.getProperty(setting.getSortItem(), ctx);
			if (sortProperty != null) {
				sortItem = sortProperty.getId();
			}
		}
		sortType = setting.getSortType();

		if (setting.getParent() != null && setting.getParent().getPropertyName() != null) {
			MetaReferenceComboSetting meta = new MetaReferenceComboSetting();
			meta.applyConfig(setting.getParent(), referenceProperty.getReferenceEntityHandler(ctx));

			//プロパティIDが設定されてない場合は保存しない(不正なプロパティ名や参照でない場合等)
			if (meta.propertyId != null) this.parent = meta;
		}
	}

	public ReferenceComboSetting currentConfig(EntityHandler entity) {
		EntityContext ctx = EntityContext.getCurrentContext();

		PropertyHandler property = entity.getPropertyById(propertyId, ctx);
		if (property == null && !(property instanceof ReferencePropertyHandler)) return null;

		//プロパティ名を取得
		ReferencePropertyHandler referenceProperty = (ReferencePropertyHandler) property;
		ReferenceComboSetting setting = new ReferenceComboSetting();
		setting.setPropertyName(referenceProperty.getName());
		setting.setCondition(this.condition);

		if (sortItem != null) {
			PropertyHandler sortProperty = referenceProperty.getReferenceEntityHandler(ctx)
					.getPropertyById(sortItem, ctx);
			if (sortProperty != null) {
				setting.setSortItem(sortProperty.getName());
			}
		}
		setting.setSortType(sortType);

		if (parent != null && parent.propertyId != null) {
			setting.setParent(parent.currentConfig(referenceProperty.getReferenceEntityHandler(ctx)));
		}
		return setting;
	}

}
