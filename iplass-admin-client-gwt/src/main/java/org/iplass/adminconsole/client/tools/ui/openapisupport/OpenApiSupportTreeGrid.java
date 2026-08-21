/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.adminconsole.client.tools.ui.openapisupport;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.ui.widget.MtpTreeGrid;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.widgets.tree.TreeGridField;

/**
 * OpenAPI(Swagger)Support 用の TreeGridコンポーネント
 *
 * @author SEKIGUCHI Naoya
 */
public class OpenApiSupportTreeGrid extends MtpTreeGrid {
	/** 追加したフィールド */
	private List<TreeGridField> fieldList = new ArrayList<>();

	/**
	 * コンストラクタ
	 */
	public OpenApiSupportTreeGrid() {
		super();
	}

	/**
	 * 選択ツリーとして利用するように設定します。
	 */
	public void enableSelectionTree() {
		setLeaveScrollbarGap(false);
		setCanSort(false);
		setCanFreezeFields(false);
		setSelectionAppearance(SelectionAppearance.CHECKBOX);
		setShowSelectedStyle(false);
		setShowPartialSelection(true);
		setCascadeSelection(true);
		setCanGroupBy(false);
		setCanPickFields(false);
	}

	/**
	 * RecordComponentを表示するように設定します。
	 */
	public void enableShowRecordComponents() {
		// この２つを指定することでcreateRecordComponentが有効
		setShowRecordComponents(true);
		setShowRecordComponentsByCell(true);
	}

	/**
	 * 表示中データをリフレッシュします。
	 * <p>
	 * DataSource を利用した TreeGrid の場合は、以下の順序で初期化する必要がある。
	 * </p>
	 * <ol>
	 * <li>{@link #setDataSource(DataSource)}</li>
	 * <li>{@link #setFields(com.smartgwt.client.widgets.grid.ListGridField...)}</li>
	 * <li>{@link #fetchData()}</li>
	 * </ol>
	 * @param dataSource データソース
	 */
	public void refresh(DataSource dataSource) {
		setDataSource(dataSource);
		setFields(fieldList.toArray(new TreeGridField[fieldList.size()]));
		fetchData();
	}

	/**
	 * TreeGridField を追加します。
	 * <p>
	 * フィールドの幅は、引数で指定した値を設定します。
	 * 追加したフィールドはコンポーネント内に保持されます。
	 * </p>
	 *
	 * @param name フィールド名
	 * @param title フィールドタイトル
	 * @param width フィールド幅
	 * @return 追加された TreeGridField インスタンス
	 */
	public TreeGridField addField(String name, String title, int width) {
		TreeGridField field = new TreeGridField(name, title, width);
		field.setWidth(width);
		field.setCanSort(Boolean.FALSE);

		fieldList.add(field);

		return field;
	}

	/**
	 * TreeGrid のツリーノードを全て展開します。
	 */
	public void expandTree() {
		getTree().openAll();
	}

	/**
	 * TreeGrid のツリーノードを全て折りたたみ、ルートノードのみ展開します。
	 */
	public void collapseTree() {
		getTree().closeAll();
		getTree().openFolders(getTree().getChildren(getTree().getRoot()));
	}

	@Override
	public void destroy() {
		fieldList.clear();
		fieldList = null;

		super.destroy();
	}
}
