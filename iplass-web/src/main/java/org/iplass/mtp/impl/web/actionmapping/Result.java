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

package org.iplass.mtp.impl.web.actionmapping;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.web.actionmapping.definition.result.DynamicTemplateResultDefinition;
import org.iplass.mtp.web.actionmapping.definition.result.RedirectResultDefinition;
import org.iplass.mtp.web.actionmapping.definition.result.ResultDefinition;
import org.iplass.mtp.web.actionmapping.definition.result.StaticResourceResultDefinition;
import org.iplass.mtp.web.actionmapping.definition.result.StreamResultDefinition;
import org.iplass.mtp.web.actionmapping.definition.result.TemplateResultDefinition;


//TODO Result -> MetaResult
@XmlSeeAlso({RedirectResult.class, DynamicTemplateResult.class, StreamResult.class, TemplateResult.class, StaticResourceResult.class})
public abstract class Result implements MetaData {

	//Commandの実行結果と、それに対する後続処理のマッピング

	private static final long serialVersionUID = 6043623303625555566L;

	/** マッピング対象となるCommandの実行結果ステータス。*指定の場合は全てのステータスの意。 */
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

	public String getCommandResultStatus() {
		return commandResultStatus;
	}

	public void setCommandResultStatus(String commandResultStatus) {
		this.commandResultStatus = commandResultStatus;
	}

	//Definition → Meta インスタンス
	public static Result createInstance(ResultDefinition definition) {
		if (definition instanceof DynamicTemplateResultDefinition) {
			return new DynamicTemplateResult();
		} else if (definition instanceof RedirectResultDefinition) {
			return new RedirectResult();
		} else if (definition instanceof StreamResultDefinition) {
			return new StreamResult();
		} else if (definition instanceof TemplateResultDefinition) {
			return new TemplateResult();
		} else if (definition instanceof StaticResourceResultDefinition) {
			return new StaticResourceResult();
		}
		return null;
	}

	//Definition → Meta
	public abstract void applyConfig(ResultDefinition definition);

	//Meta → Definition
	public abstract ResultDefinition currentConfig();

	//Definition → Meta共通項目
	protected void fillFrom(ResultDefinition definition) {
		commandResultStatus = definition.getCommandResultStatus();
		exceptionClassName = definition.getExceptionClassName();
	}

	//Meta共通項目 → Definition
	protected void fillTo(ResultDefinition definition) {
		definition.setCommandResultStatus(commandResultStatus);
		definition.setExceptionClassName(exceptionClassName);
	}

	public abstract ResultRuntime createRuntime();

	public Result copy() {
		return ObjectUtil.deepCopy(this);
	}

	public abstract class ResultRuntime {

		public abstract void handle(WebRequestStack request) throws ServletException, IOException;

		public abstract Result getMetaData();

		public abstract void finallyProcess(WebRequestStack request);

	}



}
