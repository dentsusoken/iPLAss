/*
 * Copyright (C) 2016 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.definition.binary;

import java.io.InputStream;
import java.io.Serializable;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.iplass.mtp.impl.metadata.binary.jaxb.BinaryDefinitionXmlAdapter;


/**
 * バイナリ形式の定義（メタデータ）を表すインタフェース。
 * 
 * @author K.Higuchi
 *
 */
@XmlJavaTypeAdapter(BinaryDefinitionXmlAdapter.class)
public interface BinaryDefinition extends Serializable {

	/**
	 * バイナリ形式の定義の名称。
	 * @return
	 */
	public String getName();
	
	/**
	 * バイナリのサイズ。
	 * 
	 * @return
	 */
	public long getSize();
	
	/**
	 * バイナリデータのInputStream。
	 * @return
	 */
	public InputStream getInputStream();
	
}
