/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.command;

import jakarta.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.definition.CommandDefinition;
import org.iplass.mtp.command.definition.JavaClassCommandDefinition;
import org.iplass.mtp.command.definition.ScriptingCommandDefinition;
import org.iplass.mtp.impl.definition.DefinableMetaData;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.metadata.BaseMetaDataRuntime;
import org.iplass.mtp.impl.metadata.BaseRootMetaData;
import org.iplass.mtp.impl.util.ObjectUtil;


@XmlSeeAlso({MetaMetaJavaCommand.class, MetaMetaScriptingCommand.class})
public abstract class MetaMetaCommand extends BaseRootMetaData implements DefinableMetaData<CommandDefinition> {

	private static final long serialVersionUID = -5145049329757303334L;

	//TODO imageアイコン
//	private String iconUrl;

//	private String[] resultStatus;

	//TODO この情報はCommandクラスにアノテーションで定義できるように
//	private ParameterName[] configParameterName;

	//TODO キャッシュ機能の実装。その他キャッシュ設定。キャッシュのキーとなるパラメータの定義
//	private boolean cacheable;

	private boolean readOnly;
	private boolean newInstancePerRequest;

//	public String[] getResultStatus() {
//		return resultStatus;
//	}
//
//	public void setResultStatus(String[] resultStatus) {
//		this.resultStatus = resultStatus;
//	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public boolean isNewInstancePerRequest() {
		return newInstancePerRequest;
	}

	public void setNewInstancePerRequest(boolean newInstancePerRequest) {
		this.newInstancePerRequest = newInstancePerRequest;
	}

//	public ParameterName[] getConfigParameterName() {
//		return configParameterName;
//	}
//
//	public void setConfigParameterName(ParameterName[] configParameterName) {
//		this.configParameterName = configParameterName;
//	}

//	public boolean isCacheable() {
//		return cacheable;
//	}
//
//	public void setCacheable(boolean cacheable) {
//		this.cacheable = cacheable;
//	}

	public MetaMetaCommand copy() {
		return ObjectUtil.deepCopy(this);
	}

	public abstract class MetaCommandRuntime extends BaseMetaDataRuntime {

		//FIXME ここのタイミングでどこから使われているか把握して、メタデータ更新時に参照先のMetaをリロード
		public abstract Command newCommand();
		public abstract MetaMetaCommand getMetaData();

	}

	//Definition → Meta インスタンス
	public static MetaMetaCommand createInstance(CommandDefinition definition) {
		if (definition instanceof JavaClassCommandDefinition) {
			return new MetaMetaJavaCommand();
		} else if (definition instanceof ScriptingCommandDefinition) {
			return new MetaMetaScriptingCommand();
		}
		return null;
	}

	//Definition → Meta
	public abstract void applyConfig(CommandDefinition definition);

	//Meta → Definition
	public abstract CommandDefinition currentConfig();

	//Definition → Meta共通項目
	protected void fillFrom(CommandDefinition definition) {
		name = definition.getName();
		displayName = definition.getDisplayName();
		localizedDisplayNameList = I18nUtil.toMeta(definition.getLocalizedDisplayNameList());
		description = definition.getDescription();
//		resultStatus = definition.getResultStatus();
		readOnly = definition.isReadOnly();
		newInstancePerRequest = definition.isNewInstancePerRequest();
	}

	//Meta共通項目 → Definition
	protected void fillTo(CommandDefinition definition) {
		definition.setName(name);
		definition.setDisplayName(displayName);
		definition.setLocalizedDisplayNameList(I18nUtil.toDef(localizedDisplayNameList));
		definition.setDescription(description);
//		definition.setResultStatus(resultStatus);
		definition.setReadOnly(readOnly);
		definition.setNewInstancePerRequest(newInstancePerRequest);
	}
}
