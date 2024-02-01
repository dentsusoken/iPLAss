/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.shared.tools.dto.metaexplorer;

import java.io.Serializable;

import org.iplass.adminconsole.shared.metadata.dto.MetaTreeNode;

public class ImportFileInfo implements Serializable {

	private static final long serialVersionUID = -2097236157993692397L;

	private String tagOid;
	private String fileName;

	private int count;

	private MetaTreeNode rootNode;

	/**
	 * コンストラクタ
	 */
	public ImportFileInfo() {
	}

	public String getTagOid() {
		return tagOid;
	}

	public void setTagOid(String tagOid) {
		this.tagOid = tagOid;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public MetaTreeNode getRootNode() {
		return rootNode;
	}

	public void setRootNode(MetaTreeNode rootNode) {
		this.rootNode = rootNode;
	}

}
