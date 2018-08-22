/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.iplass.mtp.impl.webapi.jackson.WebApiResponseSerializer;
import org.iplass.mtp.impl.webapi.jaxb.Result;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;


/**
 * WebAPI呼び出し結果。
 *
 * @author K.Higuchi
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"status", "resultList", "exceptionType", "exceptionMessage"})
@JsonSerialize(using = WebApiResponseSerializer.class)
public class WebApiResponse {
	private String status;

	@XmlTransient
	private Throwable throwable;

	@XmlTransient
	private Map<String, Object> results;
	
	public WebApiResponse() {
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public void addResult(String key, Object value) {
		if (results == null) {
			results = new LinkedHashMap<>();
		}
		results.put(key, value);
	}
	
	public Map<String, Object> getResults() {
		return results;
	}

	public void setResults(Map<String, Object> results) {
		this.results = results;
	}
	
	public Throwable getException() {
		return throwable;
	}

	public void setException(Throwable throwable) {
		this.throwable = throwable;
	}

	@XmlElement
	public String getExceptionType() {
		if (this.throwable == null) {
			return null;
		} else {
			return this.throwable.getClass().getName();
		}
	}

	@XmlElement
	public String getExceptionMessage() {
		if (this.throwable == null) {
			return null;
		} else {
			return this.throwable.getMessage();
		}
	}
	
	@XmlElement(name="result")
	public List<Result> getResultList() {
		if (results == null) {
			return null;
		}
		
		List<Result> res = new ArrayList<>(results.size());
		for (Map.Entry<String, Object> e: results.entrySet()) {
			res.add(new Result(e.getKey(), e.getValue()));
		}
		return res;
	}
}
