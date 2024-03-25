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

package org.iplass.mtp.impl.webapi;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlTransient;

import org.iplass.mtp.entity.GenericEntity;
import org.iplass.mtp.impl.webapi.jackson.WebApiParameterDeserializer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@XmlSeeAlso({GenericEntity.class, GenericEntity[].class})
@JsonPropertyOrder({"name", "valueType", "value"})
@JsonDeserialize(using=WebApiParameterDeserializer.class)
public class WebApiParameter {


	private String name;

	private Object value;

	/**
	 * valueに格納されているObjectの型を指定（Jsonの場合のみ必要）。
	 * value単体(のtype属性)で解決しようとするとパース処理時に余計なオーバーヘッドの発生の懸念がある。
	 * WebApiParameterでvalueTypeで型を指定する形に。
	 * 未指定の場合は、Jacksonの標準のマッピングが適用される。
	 *
	 */
	private String valueType;

	public WebApiParameter() {
	}

	@XmlTransient
	@JsonInclude(Include.NON_NULL)
	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}


	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
