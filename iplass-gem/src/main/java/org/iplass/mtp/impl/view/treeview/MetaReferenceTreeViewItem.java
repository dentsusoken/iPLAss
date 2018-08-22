/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.view.treeview;

import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.view.treeview.ReferenceTreeViewItem;
import org.iplass.mtp.view.treeview.TreeViewItem;

/**
 * ツリービューにEntityの参照を表示するための定義情報
 * @author lis3wg
 *
 */
public class MetaReferenceTreeViewItem extends MetaEntityTreeViewItem {

	public static MetaReferenceTreeViewItem getInstance(TreeViewItem item) {
		return new MetaReferenceTreeViewItem();
	}

	/** SerialVersionUID */
	private static final long serialVersionUID = 4352394071681424899L;

	/**
	 * コンストラクタ
	 */
	public MetaReferenceTreeViewItem() {
	}

	/** 親Entityからの参照プロパティID */
	private String propertyId;

	/** Entity定義階層の表示名 */
	private String displayName;

	/**プロパティIDプロパティIDを取得します。
	 * @return プロパティID
	 */
	public String getPropertyId() {
		return propertyId;
	}

	/**
	 * プロパティIDを設定します。
	 * @param propertyName プロパティID
	 */
	public void setPropertyId(String propertyName) {
		this.propertyId = propertyName;
	}

	/**
	 * 表示名を取得します。
	 * @return 表示名
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * 表示名を設定します。
	 * @param displayName 表示名
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * ツリービューのアイテムの情報をメタデータに設定します。
	 * @param item ツリービューのアイテム
	 */
	public void applyConfig(TreeViewItem item, EntityHandler parent) {
		if (!(item instanceof ReferenceTreeViewItem)) {
			throw new IllegalArgumentException(
					"type is not ReferenceTreeViewItem. value is" + item.getClass().getName());
		}
		super.fillFrom(item);
		ReferenceTreeViewItem rItem = (ReferenceTreeViewItem) item;

		EntityContext ctx = EntityContext.getCurrentContext();
		PropertyHandler pHandler = parent.getProperty(rItem.getPropertyName(), ctx);

		this.propertyId = pHandler.getId();
		this.displayName = rItem.getDisplayName();
	}

	/**
	 * ツリービューのアイテム情報を取得します。
	 * @return ツリービューのアイテム
	 */
	public ReferenceTreeViewItem currentConfig(EntityHandler parent) {
		ReferenceTreeViewItem item = new ReferenceTreeViewItem();
		super.fillTo(item);

		EntityContext ctx = EntityContext.getCurrentContext();
		PropertyHandler pHandler = parent.getPropertyById(propertyId, ctx);

		item.setPropertyName(pHandler.getName());
		item.setDisplayName(displayName);
		return item;
	}

}
