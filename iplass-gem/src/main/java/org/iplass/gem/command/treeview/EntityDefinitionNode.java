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
 * Entity定義を表すノード
 * @author lis3wg
 */
public class EntityDefinitionNode extends TreeViewNode {

	public static final String NODE_TYPE = "entitydefinition";

	/**
	 * コンストラクタ
	 * @param parent 親ノード
	 * @param name ノードの名前
	 */
	public EntityDefinitionNode(TreeViewNode parent, String name) {
		this(parent != null ? parent.getPath() : null, name);
	}

	/**
	 * コンストラクタ
	 * @param path ノードのパス
	 * @param name ノードの名前
	 */
	public EntityDefinitionNode(String path, String name) {
		if (path != null) {
			this.path = path + "/" + name;
		} else {
			this.path = name;
		}
		this.displayName = name;
		this.type = NODE_TYPE;
	}
}
