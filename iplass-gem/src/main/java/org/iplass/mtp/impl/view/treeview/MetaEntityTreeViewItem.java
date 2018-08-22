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

import javax.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.view.treeview.EntityTreeViewItem;
import org.iplass.mtp.view.treeview.ReferenceTreeViewItem;
import org.iplass.mtp.view.treeview.TreeViewItem;

/**
 * ツリービューにEntityを表示するための定義情報
 * @author lis3wg
 *
 */
@XmlSeeAlso({ MetaReferenceTreeViewItem.class })
public class MetaEntityTreeViewItem extends MetaTreeViewItem {

	public static MetaEntityTreeViewItem getInstance(TreeViewItem item) {
		if (item instanceof ReferenceTreeViewItem) {
			return MetaReferenceTreeViewItem.getInstance(item);
		}
		return new MetaEntityTreeViewItem();
	}

	/** SerialVersionUID */
	private static final long serialVersionUID = 7796408876104481655L;

	/**
	 * コンストラクタ
	 */
	public MetaEntityTreeViewItem() {
	}

	/**
	 * ツリービューのアイテムの情報をメタデータに設定します。
	 * @param item ツリービューのアイテム
	 */
	public void applyConfig(TreeViewItem item) {
		if (!(item instanceof EntityTreeViewItem)) {
			throw new IllegalArgumentException(
					"type is not EntityTreeViewItem. value is"+ item.getClass().getName());
		}
		this.fillFrom(item);
	}

	@Override
	protected void fillFrom(TreeViewItem item) {
		super.fillFrom(item);
	}

	/**
	 * ツリービューのアイテム情報を取得します。
	 * @return ツリービューのアイテム
	 */
	public EntityTreeViewItem currentConfig() {
		EntityTreeViewItem item = new EntityTreeViewItem();
		this.fillTo(item);
		return item;
	}

	@Override
	protected void fillTo(TreeViewItem item) {
		super.fillTo(item);
	}
}
