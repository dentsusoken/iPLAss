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

package org.iplass.mtp.view.generic.common;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.adminconsole.view.annotation.Refrectable;
import org.iplass.adminconsole.view.annotation.generic.EntityViewField;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * 画面項目間の連動、自動補完設定
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({JavascriptAutocompletionSetting.class, WebApiAutocompletionSetting.class})
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS)
public abstract class AutocompletionSetting implements Refrectable {

	private static final long serialVersionUID = 559723210737582551L;

	/** 連動元のプロパティ */
	@MetaFieldInfo(displayName="連動元のプロパティ",
			displayNameKey="generic_common_AutocompletionSetting_propertiesDisplaNameKey",
			inputType=InputType.REFERENCE,
			referenceClass=AutocompletionProperty.class,
			multiple=true,
			description="連動元のプロパティを指定します。<br>"
					+ "複数指定した場合、いずれかのプロパティが変更されると、"
					+ "全ての項目の値をパラメータとして補完処理を呼び出します。",
			descriptionKey="generic_common_AutocompletionSetting_propertiesDescriptionKey"
	)
	@EntityViewField()
	private List<AutocompletionProperty> properties;

	/** runtime key */
	private String runtimeKey;

	/**
	 * @return properties
	 */
	public List<AutocompletionProperty> getProperties() {
		if (properties == null) properties = new ArrayList<>();
		return properties;
	}

	/**
	 * @param properties セットする properties
	 */
	public void setProperties(List<AutocompletionProperty> properties) {
		this.properties = properties;
	}

	public void addProperty(AutocompletionProperty property) {
		getProperties().add(property);
	}

	public String getRuntimeKey() {
		return runtimeKey;
	}

	public void setRuntimeKey(String runtimeKey) {
		this.runtimeKey = runtimeKey;
	}
}
