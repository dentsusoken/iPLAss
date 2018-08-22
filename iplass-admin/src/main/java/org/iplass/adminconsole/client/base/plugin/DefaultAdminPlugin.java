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

package org.iplass.adminconsole.client.base.plugin;

import org.iplass.adminconsole.client.base.ui.layout.MainWorkspaceTab;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;


public abstract class DefaultAdminPlugin implements AdminPlugin {

	/** Owner  */
	protected Canvas owner;

	/** Tree  */
	protected Tree tree;

	/** Tree  */
	protected TreeGrid treeGrid;

	/** ワークスペース  */
	protected MainWorkspaceTab workspace;

	@Override
	public void setOwner(Canvas owner) {
		this.owner = owner;
	}

	@Override
	public void setTree(Tree tree) {
		this.tree = tree;
	}

	@Override
	public void setTreeGrid(TreeGrid treeGrid) {
		this.treeGrid = treeGrid;
	}

	@Override
	public void setWorkSpace(MainWorkspaceTab workspace) {
		this.workspace = workspace;
	}

	@Override
	public String getCategoryName() {
		return "";
	}

	//------------------------------------------------
	//WorkspaceContentsStateChangeHandlerの実装部分
	//------------------------------------------------

	/**
	 * <p>Workspace上のコンテンツがCloseされた際に呼び出されます。</p>
	 *
	 * <p>
	 * 注意！：どのコンテンツがCloseされた場合にも呼び出されます。<br/>
	 * プラグインの対象コンテンツかを判断してください。
	 * </p>
	 *
	 * @param event {@link ContentClosedEvent}
	 */
	@Override
	public void onContentClosed(ContentClosedEvent event){};

	/**
	 * <p>Workspace上のコンテンツがSelectされた際に呼び出されます。</p>
	 *
	 * <p>
	 * 注意！：どのコンテンツがSelectされた場合にも呼び出されます。<br/>
	 * プラグインの対象コンテンツかを判断してください。
	 * </p>
	 *
	 * @param event {@link ContentSelectedEvent}
	 */
	@Override
	public void onContentSelected(ContentSelectedEvent event){};

}
