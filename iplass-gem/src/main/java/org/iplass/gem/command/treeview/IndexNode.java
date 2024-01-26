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

package org.iplass.gem.command.treeview;


/**
 * Entityのデータを件数ごとに区切るノード
 * @author lis3wg
 */
public class IndexNode extends TreeViewNode {

	public static final String NODE_TYPE = "index";

	/** オフセット */
	private int offset;

	/** 親ノードのOID */
	private String oid;

	/**
	 * コンストラクタ
	 * @param parent 親ノード
	 */
	public IndexNode(TreeViewNode parent) {
		this(parent != null ? parent.getPath() : null);
	}

	/**
	 * コンストラクタ
	 * @param path ノードのパス
	 */
	public IndexNode(String path) {
		if (path != null) {
			this.path = path;
		}
		this.type = NODE_TYPE;
	}

	/**
	 * オフセットを取得します。
	 * @return オフセット
	 */
	public int getOffset() {
	    return offset;
	}

	/**
	 * オフセットを設定します。
	 * @param offset オフセット
	 */
	public void setOffset(int offset) {
	    this.offset = offset;
	}

	/**
	 * 親ノードのOIDを取得します。
	 * @return 親ノードのOID
	 */
	public String getOid() {
	    return oid;
	}

	/**
	 * 親ノードのOIDを設定します。
	 * @param oid 親ノードのOID
	 */
	public void setOid(String oid) {
	    this.oid = oid;
	}
}
