/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.command.beanmapper;

import java.util.LinkedList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;

import org.iplass.mtp.impl.command.beanmapper.jackson.MappingErrorSerializer;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Beanへのマッピングエラーの詳細を表現するクラスです。
 * 
 * @author K.Higuchi
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mappingError", propOrder = { "propertyPath", "errorMessages"})
@JsonSerialize(using = MappingErrorSerializer.class)
public class MappingError {
	@XmlElement
	private String propertyPath;
	@XmlElementWrapper(name = "errorMessages")
	@XmlElement(name = "message")
	private List<String> errorMessages;
	@XmlTransient
	private Object errorValue;
	@XmlTransient
	private Object cause;
	
	public MappingError(String propertyPath, Object errorValue) {
		this.propertyPath = propertyPath;
		this.errorValue = errorValue;
	}
	
	public MappingError(String propertyPath, List<String> errorMessages, Object errorValue, Object cause) {
		this.propertyPath = propertyPath;
		this.errorMessages = errorMessages;
		this.errorValue = errorValue;
		this.cause = cause;
	}

	public MappingError(String propertyPath, String errorMessage, Object errorValue, Object cause) {
		this.propertyPath = propertyPath;
		this.errorMessages = new LinkedList<>();
		this.errorMessages.add(errorMessage);
		this.errorValue = errorValue;
		this.cause = cause;
	}

	/**
	 * マッピングに失敗したプロパティへのパスを取得します。
	 * 
	 * @return
	 */
	public String getPropertyPath() {
		return propertyPath;
	}

	public void setPropertyPath(String propertyPath) {
		this.propertyPath = propertyPath;
	}

	/**
	 * マッピングに失敗した理由を示すメッセージ（複数の場合もある）です。
	 * 
	 * @return
	 */
	public List<String> getErrorMessages() {
		return errorMessages;
	}

	public void setErrorMessages(List<String> errorMessages) {
		this.errorMessages = errorMessages;
	}

	/**
	 * マッピングに失敗した値を取得します。
	 * 
	 * @return
	 */
	public Object getErrorValue() {
		return errorValue;
	}

	public void setErrorValue(Object errorValue) {
		this.errorValue = errorValue;
	}

	/**
	 * マッピングに失敗した原因をあらわすオブジェクトを取得します。
	 * 複数の原因がある場合は、List&lt;Object&gt;が取得されます。
	 * 
	 * @return
	 */
	public Object getCause() {
		return cause;
	}

	public void setCause(Object cause) {
		this.cause = cause;
	}
	
	public void addMessage(String message) {
		addMessage(message, null);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addMessage(String message, Object cause) {
		if (errorMessages == null) {
			errorMessages = new LinkedList<>();
		}
		errorMessages.add(message);
		
		if (cause != null) {
			if (this.cause == null) {
				this.cause = cause;
			} else if (this.cause instanceof List) {
				((List) this.cause).add(cause);
			} else {
				List<Object> causes = new LinkedList<>();
				causes.add(this.cause);
				causes.add(cause);
				this.cause = causes;
			}
		}
	}
}
