/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.utilityclass.definition;

import javax.xml.bind.annotation.XmlRootElement;

import org.iplass.mtp.definition.Definition;

/**
 * テナント内のロジック（Command、Templateなど）で共通的に利用したい
 * ユーティリティクラス等を登録する際に利用する定義。
 * UtilityClassとして定義されたclassは、
 * テナント内の各Command、Templateなどのスクリプトから参照可能。
 *
 * @author K.Higuchi
 *
 */
@XmlRootElement
public class UtilityClassDefinition implements Definition {
	private static final long serialVersionUID = 6071126872985670959L;

	private String name;
	private String displayName;
	private String description;

	private String script;

	/**
	 * UtilityClassの定義名。
	 * .区切りでパッケージを指定可能。
	 * scriptにて記述されるクラスのクラス名はこの定義名と一致している必要がある。
	 *
	 * @return
	 */
	public String getName() {
		return convertName(name);
	}

	public void setName(String name) {
		this.name = convertName(name);
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	/**
	 * UtilityClassのname区切りは「/」ではなく「.」とする。
	 * MetaUtilityClass、UtilityClassDefinitionのnameセット時にチェックし変換する。（念のため）
	 *
	 * @param name
	 * @return
	 */
	private String convertName(String name) {
		return name.replace("/", ".");
	}
}
