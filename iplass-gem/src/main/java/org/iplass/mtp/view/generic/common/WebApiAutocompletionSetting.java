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

package org.iplass.mtp.view.generic.common;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.adminconsole.view.annotation.generic.EntityViewField;

/**
 * WebApiを利用した自動補完設定
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class WebApiAutocompletionSetting extends AutocompletionSetting {

	private static final long serialVersionUID = 5470093908850810111L;

	/**
	 * 自動補完タイプ
	 */
	public enum AutocompletionType {
		EQL,
		GROOVYSCRIPT
	}

	/** 自動補完タイプ */
	@MetaFieldInfo(
		displayName="自動補完タイプ",
		displayNameKey="generic_common_WebApiAutocompletionSetting_autocompletionTypeDisplaNameKey",
		inputType=InputType.ENUM,
		enumClass=AutocompletionType.class,
		description="自動補完する値を取得する方法を指定します。<BR />" +
				"EQL          : 設定されたEQLを実行し、その結果で自動補完します。<BR />" +
				"GROOVYSCRIPT : Groovyscriptの実行結果を自動補完します。",
		descriptionKey="generic_common_WebApiAutocompletionSetting_autocompletionTypeDescriptionKey"
	)
	@EntityViewField()
	private AutocompletionType autocompletionType;

	/** EQL */
	@MetaFieldInfo(
		displayName="EQL",
		displayNameKey="generic_common_WebApiAutocompletionSetting_eqlDisplaNameKey",
		inputType=InputType.SCRIPT,
		mode="groovytemplate",
		description="自動補完タイプでEQLを選択した際に実行する処理です。<br>"
				+ "実行結果の1行目の最初のSelect項目を補完の値として利用します。<br>"
				+ "連動元のプロパティの値は以下の名前でバインドされています。<br>"
				+ "xxx",
		descriptionKey="generic_common_WebApiAutocompletionSetting_eqlDescriptionKey"
	)
	@EntityViewField()
	private String eql;

	/** Groovyscript */
	@MetaFieldInfo(
		displayName="Groovyscript",
		displayNameKey="generic_common_WebApiAutocompletionSetting_groovyscriptDisplaNameKey",
		inputType=InputType.SCRIPT,
		mode="groovy_script",
		description="自動補完タイプでGROOVYSCRIPTを選択した際に実行する処理です。<br>"
				+ "連動元のプロパティの値は以下の名前でバインドされています。<br>"
				+ "xxx",
		descriptionKey="generic_common_WebApiAutocompletionSetting_groovyscriptDescriptionKey"
	)
	@EntityViewField()
	private String groovyscript;

	/**
	 * @return autocompletionType
	 */
	public AutocompletionType getAutocompletionType() {
		return autocompletionType;
	}

	/**
	 * @param autocompletionType セットする autocompletionType
	 */
	public void setAutocompletionType(AutocompletionType autocompletionType) {
		this.autocompletionType = autocompletionType;
	}

	/**
	 * @return eql
	 */
	public String getEql() {
		return eql;
	}

	/**
	 * @param eql セットする eql
	 */
	public void setEql(String eql) {
		this.eql = eql;
	}

	/**
	 * @return groovyscript
	 */
	public String getGroovyscript() {
		return groovyscript;
	}

	/**
	 * @param groovyscript セットする groovyscript
	 */
	public void setGroovyscript(String groovyscript) {
		this.groovyscript = groovyscript;
	}
}
