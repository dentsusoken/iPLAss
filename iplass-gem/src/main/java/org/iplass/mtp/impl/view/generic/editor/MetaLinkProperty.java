/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.view.generic.editor.LinkProperty;

/**
 * 参照型の場合の連動元のプロパティ定義
 */
public class MetaLinkProperty implements MetaData {

	private static final long serialVersionUID = 7220193122853969208L;

	/** 連動元プロパティID */
	private String linkFromPropertyId;

	/** ネストプロパティ同士の連動 */
	private boolean withNestProperty;

	/** 連動対象プロパティID */
	private String linkToPropertyId;

	/**
	 * 連動元プロパティIDを取得します。
	 * @return 連動元プロパティID
	 */
	public String getLinkFromPropertyId() {
		return linkFromPropertyId;
	}

	/**
	 * 連動元プロパティIDを設定します。
	 * @param linkFromPropertyId 連動元プロパティID
	 */
	public void setLinkFromPropertyId(String linkFromPropertyId) {
		this.linkFromPropertyId = linkFromPropertyId;
	}

	/**
	 * ネストプロパティ同士の連動かを取得します。
	 * @return ネストプロパティ同士の連動か
	 */
	public boolean isWithNestProperty() {
		return withNestProperty;
	}

	/**
	 * ネストプロパティ同士の連動かを設定します。
	 * @param withNestProperty ネストプロパティ同士の連動か
	 */
	public void setWithNestProperty(boolean withNestProperty) {
		this.withNestProperty = withNestProperty;
	}

	/**
	 * 連動対象プロパティIDを取得します。
	 * @return 連動対象プロパティID
	 */
	public String getLinkToPropertyId() {
		return linkToPropertyId;
	}

	/**
	 * 連動対象プロパティIDを設定します。
	 * @param linkToPropertyId 連動対象プロパティID
	 */
	public void setLinkToPropertyId(String linkToPropertyId) {
		this.linkToPropertyId = linkToPropertyId;
	}

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

	/**
	 * DefinitionをMetaDataに変換します。
	 *
	 * @param property プロパティ定義
	 * @param referenceEntity 参照先Entity定義
	 * @param fromEntity 参照元Entity定義
	 * @param rootEntity ルートEntity定義
	 */
	public void applyConfig(LinkProperty linkProperty, EntityHandler referenceEntity, EntityHandler fromEntity, EntityHandler rootEntity) {
		EntityContext ctx = EntityContext.getCurrentContext();

		PropertyHandler fromProperty = null;
		if (linkProperty.isWithNestProperty()) {
			if (fromEntity != null) {
				fromProperty = fromEntity.getProperty(linkProperty.getLinkFromPropertyName(), ctx);
			}
		} else {
			if (rootEntity != null) {
				fromProperty = rootEntity.getProperty(linkProperty.getLinkFromPropertyName(), ctx);
			}
		}
		if (fromProperty == null) return;

		PropertyHandler toProperty = referenceEntity.getProperty(linkProperty.getLinkToPropertyName(), ctx);
		if (toProperty == null) return;

		this.linkFromPropertyId = fromProperty.getId();
		this.withNestProperty = linkProperty.isWithNestProperty();
		this.linkToPropertyId = toProperty.getId();
	}

	/**
	 * MetaDataをDefinitionに変換します。
	 *
	 * @param referenceEntity 参照先Entity定義
	 * @param fromEntity 参照元Entity定義
	 * @param rootEntity ルートEntity定義
	 * @return Definition
	 */
	public LinkProperty currentConfig(EntityHandler referenceEntity, EntityHandler fromEntity, EntityHandler rootEntity) {
		EntityContext ctx = EntityContext.getCurrentContext();

		PropertyHandler fromProperty = null;
		if (withNestProperty) {
			fromProperty = fromEntity.getPropertyById(linkFromPropertyId, ctx);
		} else {
			fromProperty = rootEntity.getPropertyById(linkFromPropertyId, ctx);
		}
		if (fromProperty == null) return null;

		PropertyHandler toProperty = referenceEntity.getPropertyById(linkToPropertyId, ctx);
		if (toProperty == null) return null;

		LinkProperty linkProperty = new LinkProperty();
		linkProperty.setLinkFromPropertyName(fromProperty.getName());
		linkProperty.setWithNestProperty(withNestProperty);
		linkProperty.setLinkToPropertyName(toProperty.getName());
		return linkProperty;
	}

}
