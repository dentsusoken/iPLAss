/*
 * Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
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

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import org.iplass.mtp.impl.definition.DefinableMetaData;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.metadata.BaseMetaDataRuntime;
import org.iplass.mtp.impl.metadata.BaseRootMetaData;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.metadata.MetaDataRuntime;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.view.treeview.TreeView;
import org.iplass.mtp.view.treeview.TreeViewGridColModel;
import org.iplass.mtp.view.treeview.TreeViewItem;

/**
 * ツリービュー定義のメタデータ
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class MetaTreeView extends BaseRootMetaData implements DefinableMetaData<TreeView> {

	/** SerialVersionUID */
	private static final long serialVersionUID = -3133231241912699539L;

	/** ツリービューを構成するアイテム */
	private List<MetaTreeViewItem> items;

	/** ColModel */
	private List<MetaTreeViewGridColModel> colModel;

	@Override
	public MetaTreeView copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public MetaDataRuntime createRuntime(MetaDataConfig metaDataConfig) {
		return new TreeViewHandler();
	}

	/**
	 * ツリービューを構成するアイテムを取得します。
	 * @return ツリービューを構成するアイテム
	 */
	public List<MetaTreeViewItem> getItems() {
		if (items == null) items = new ArrayList<MetaTreeViewItem>();
		return items;
	}

	/**
	 * ツリービューを構成するアイテムを設定します。
	 * @param items ツリービューを構成するアイテム
	 */
	public void setItems(List<MetaTreeViewItem> items) {
		this.items = items;
	}

	/**
	 * ツリービューを構成するアイテムを追加します。
	 * @param item ツリービューを構成するアイテム
	 */
	public void addItem(MetaTreeViewItem item) {
		getItems().add(item);
	}

	/**
	 * ColModelを取得します。
	 * @return ColModel
	 */
	public List<MetaTreeViewGridColModel> getColModel() {
		if (colModel == null) colModel = new ArrayList<>();
	    return colModel;
	}

	/**
	 * ColModelを設定します。
	 * @param colModel ColModel
	 */
	public void setColModel(List<MetaTreeViewGridColModel> colModel) {
	    this.colModel = colModel;
	}

	/**
	 * ColModelを追加します。
	 * @param colModel ColModel
	 */
	public void addColModel(MetaTreeViewGridColModel colModel) {
	    getColModel().add(colModel);
	}

	/**
	 * ツリービュー定義の情報をメタデータに設定します。
	 * @param treeView ツリービュー定義
	 */
	public void applyConfig(TreeView treeView) {
		this.name = treeView.getName();
		this.displayName = treeView.getDisplayName();
		this.description = treeView.getDescription();
		for (TreeViewItem item : treeView.getItems()) {
			MetaTreeViewItem meta = MetaTreeViewItem.getInstance(item);
			meta.applyConfig(item);
			addItem(meta);
		}
		// 言語毎の文字情報設定
		localizedDisplayNameList = I18nUtil.toMeta(treeView.getLocalizedDisplayNameList());

		for (TreeViewGridColModel model : treeView.getColModel()) {
			MetaTreeViewGridColModel meta = new MetaTreeViewGridColModel();
			meta.applyConfig(model);
			addColModel(meta);
		}
	}

	/**
	 * ツリービュー定義の情報を取得します。
	 * @return ツリービュー定義
	 */
	public TreeView currentConfig() {
		TreeView treeView = new TreeView();
		treeView.setName(name);
		treeView.setDisplayName(displayName);
		treeView.setDescription(description);
		for (MetaTreeViewItem item : getItems()) {
			treeView.addItem(item.currentConfig());
		}

		treeView.setLocalizedDisplayNameList(I18nUtil.toDef(localizedDisplayNameList));

		for (MetaTreeViewGridColModel model : getColModel()) {
			treeView.addColModel(model.currentConfig());
		}

		return treeView;
	}

	/**
	 * ランタイム
	 * @author lis3wg
	 */
	public class TreeViewHandler extends BaseMetaDataRuntime {

		@Override
		public MetaTreeView getMetaData() {
			return MetaTreeView.this;
		}
	}
}
