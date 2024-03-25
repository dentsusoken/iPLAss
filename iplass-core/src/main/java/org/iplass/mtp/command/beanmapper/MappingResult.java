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

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlTransient;

import org.iplass.mtp.impl.command.beanmapper.jackson.MappingResultSerializer;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Beanへのマッピング結果を表現するクラスです。
 * 
 * @author K.Higuchi
 *
 */
@XmlSeeAlso({
	MappingError.class
})
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonSerialize(using = MappingResultSerializer.class)
public class MappingResult {
	@XmlTransient
	private Object bean;
	@XmlTransient
	private Map<String, MappingError> errorMap;
	@XmlTransient
	private List<MappingError> errorList;
	
	public MappingResult() {
	}

	public MappingResult(Object bean) {
		this.bean = bean;
	}
	
	public Object getBean() {
		return bean;
	}

	@XmlElement
	public List<MappingError> getErrors() {
		if (errorList == null) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableList(errorList);
	}
	
	public MappingError getError(String propertyPath) {
		if (errorMap == null) {
			return null;
		}
		return errorMap.get(propertyPath);
	}
	
	public void addError(MappingError error) {
		if (errorMap == null) {
			errorMap = new HashMap<>();
			errorList = new LinkedList<>();
		}
		MappingError old = errorMap.put(error.getPropertyPath(), error);
		if (old != null) {
			errorList.remove(old);
		}
		errorList.add(error);
	}
	
	public MappingError removeError(String propertyPath) {
		if (errorMap == null) {
			return null;
		}
		MappingError err = errorMap.remove(propertyPath);
		if (err != null) {
			errorList.remove(err);
		}
		return err;
	}
	
	public boolean hasError() {
		return errorMap != null && errorMap.size() != 0;
	}
	
	/**
	 * 指定のerrorsをすべて追加します。
	 * propertyPathが等しいMappingErrorが既に存在した場合は、マージされます。
	 * 
	 * @param errors
	 */
	@SuppressWarnings("unchecked")
	public void addErrors(List<MappingError> errors) {
		for (MappingError me: errors) {
			MappingError pre = getError(me.getPropertyPath());
			if (pre == null) {
				addError(me);
			} else {
				for (String msg: me.getErrorMessages()) {
					pre.addMessage(msg);
				}
				if (me.getCause() != null) {
					LinkedList<Object> merged = new LinkedList<>();
					Object preCause = pre.getCause();
					if (preCause instanceof List) {
						for (Object o: ((List<Object>) preCause)) {
							merged.add(o);
						}
					} else if (preCause != null) {
						merged.add(preCause);
					}
					Object addCause = me.getCause();
					if (addCause instanceof List) {
						for (Object o: ((List<Object>) addCause)) {
							merged.add(o);
						}
					} else if (addCause != null) {
						merged.add(addCause);
					}
					if (merged.size() == 1) {
						pre.setCause(merged.get(0));
					} else {
						pre.setCause(merged);
					}
				}
			}
		}
	}
	
}
