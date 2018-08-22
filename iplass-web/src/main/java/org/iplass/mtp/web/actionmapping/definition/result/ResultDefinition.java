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

package org.iplass.mtp.web.actionmapping.definition.result;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * ActionでのCommandの実行結果によるプレゼンテーション処理の定義を表します。
 * commandResultStatus、exceptionClassNameの指定は、いずれかの指定が必須です。
 * 
 * @author K.Higuchi
 *
 */
@XmlSeeAlso (value = {
		DynamicTemplateResultDefinition.class,
		RedirectResultDefinition.class,
		StreamResultDefinition.class,
		TemplateResultDefinition.class,
		StaticResourceResultDefinition.class
})
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS)
public abstract class ResultDefinition implements Serializable {

	private static final long serialVersionUID = 2185607355522422387L;

	/**
	 * マッピング対象となるCommandの実行結果ステータス。
	 * *指定の場合は全てのステータスの意。
	 */
	private String commandResultStatus;
	
	private String exceptionClassName;

	public String getExceptionClassName() {
		return exceptionClassName;
	}

	/**
	 * マッピング対象となるExceptionのクラス名を指定。
	 * Exceptionの継承関係は考慮され、java.lang.Exceptionで定義した場合、
	 * すべてのException継承の例外は、このマッピング定義にしたがった結果を返す。
	 * 
	 * @param exceptionClassName
	 */
	public void setExceptionClassName(String exceptionClassName) {
		this.exceptionClassName = exceptionClassName;
	}

	/**
	 * @return commandResultStatus
	 */
	public String getCommandResultStatus() {
		return commandResultStatus;
	}

	/**
	 * マッピング対象となるCommandの実行結果ステータスを指定。
	 * *指定の場合は全てのステータスの意（但し、例外発生時は含まない）。
	 * 
	 * @param commandResultStatus セットする commandResultStatus
	 */
	public void setCommandResultStatus(String commandResultStatus) {
		this.commandResultStatus = commandResultStatus;
	}

	/**
	 * 各Resultのサマリー情報を返します。
	 *
	 * @return Resultのサマリー情報
	 */
	public abstract String summaryInfo();
}
