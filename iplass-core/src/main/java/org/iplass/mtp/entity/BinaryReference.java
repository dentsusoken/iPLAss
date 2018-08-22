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

package org.iplass.mtp.entity;

import java.io.Serializable;

/**
 * バイナリデータへの参照を表すEntityの属性値です。
 * EntityにBinaryPropertyが宣言されている場合は、
 * このインスタンスが返却されます。
 * 
 * 
 * @author K.Higuchi
 *
 */
public class BinaryReference implements Serializable {
	private static final long serialVersionUID = -8654202685558160763L;
	
	private long lobId = -1;
	private String name;
	private String type;
	
	//TODO 以下のプロパティ必要か？
	private String definitionName;
	private String propertyName;
	private String oid;
	private long size;
	
	public BinaryReference() {
	}
	
	public BinaryReference(long lobId, String name, String type, long size) {
		this.lobId = lobId;
		this.name = name;
		this.type = type;
		this.size = size;
	}
	
	public BinaryReference(long lobId, String name, String type, long size, String definitionName, String propertyName, String oid) {
		this.lobId = lobId;
		this.name = name;
		this.type = type;
		this.size = size;
		this.definitionName = definitionName;
		this.propertyName = propertyName;
		this.oid = oid;
	}
	
	public String toString() {
		return "BinaryReference(lobId=" + lobId + ",name=" + name + ",type=" + type + ")";
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setDefinitionName(String definitionName) {
		this.definitionName = definitionName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	/**
	 * このバイナリが格納されているEntityの定義名
	 * 
	 * @return
	 */
	public String getDefinitionName() {
		return definitionName;
	}

	/**
	 * このバイナリが格納されているEntityの属性名
	 * 
	 * @return
	 */
	public String getPropertyName() {
		return propertyName;
	}
	
	/**
	 * バイナリファイルを一意に特定するID
	 * 
	 * @return
	 */
	public long getLobId() {
		return lobId;
	}

	public void setLobId(long lobId) {
		this.lobId = lobId;
	}
	
	/**
	 * バイナリファイルのファイル名
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * バイナリのタイプ（mineタイプ名）
	 * 
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * このバイナリが格納されているEntityのOID
	 * 
	 * @return
	 */
	public String getOid() {
		return oid;
	}
	
	/**
	 * バイナリのサイズ（byte）
	 * 
	 * @return
	 */
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}

	public BinaryReference copy() {
		return new BinaryReference(lobId, name, type, size, definitionName, propertyName, oid);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (lobId ^ (lobId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BinaryReference other = (BinaryReference) obj;
		if (lobId != other.lobId)
			return false;
		return true;
	}
}
