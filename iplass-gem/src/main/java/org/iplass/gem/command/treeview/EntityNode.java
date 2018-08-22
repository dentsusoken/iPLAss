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

package org.iplass.gem.command.treeview;


/**
 * Entityを表すノード
 * @author lis3wg
 */
public class EntityNode extends TreeViewNode {

	public static final String NODE_TYPE = "entity";

	/** oid */
	private String oid;

	/** Entity定義の名前 */
	private String defName;

	/** 詳細表示用アクション */
	private String action;

	/** ビュー名 */
	private String viewName;

	/** 参照設定 */
	private boolean hasReference;

	/**
	 * コンストラクタ
	 * @param parent 親ノード
	 */
	public EntityNode(TreeViewNode parent, String defName) {
		this(parent != null ? parent.getPath() : null, defName);
	}

	/**
	 * コンストラクタ
	 * @param path ノードのパス
	 */
	public EntityNode(String path, String defName) {
		if (path != null) {
			this.path = path;
		}
		this.defName = defName;
		this.type = NODE_TYPE;
	}

	/**
	 * oidを取得します。
	 * @return oid
	 */
	public String getOid() {
		return oid;
	}

	/**
	 * oidを設定します。
	 * @param oid oid
	 */
	public void setOid(String oid) {
		this.oid = oid;
	}

	/**
	 * Entity定義の名前を取得します。
	 * @return Entity定義の名前
	 */
	public String getDefName() {
		return defName;
	}

	/**
	 * Entity定義の名前を設定します。
	 * @param defName Entity定義の名前
	 */
	public void setDefName(String defName) {
		this.defName = defName;
	}

	/**
	 * 詳細表示用アクションを取得します。
	 * @return 詳細表示用アクション
	 */
	public String getAction() {
		return action;
	}

	/**
	 * 詳細表示用アクションを設定します。
	 * @param action 詳細表示用アクション
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * ビュー名を取得します。
	 * @return ビュー名
	 */
	public String getViewName() {
	    return viewName;
	}

	/**
	 * ビュー名を設定します。
	 * @param viewName ビュー名
	 */
	public void setViewName(String viewName) {
	    this.viewName = viewName;
	}

	/**
	 * 参照設定を取得します。
	 * @return 参照設定
	 */
	public boolean isHasReference() {
	    return hasReference;
	}

	/**
	 * 参照設定を設定します。
	 * @param hasReference 参照設定
	 */
	public void setHasReference(boolean hasReference) {
	    this.hasReference = hasReference;
	}

}
