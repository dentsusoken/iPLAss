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

package org.iplass.mtp.impl.view.generic.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.view.generic.EntityViewRuntime;
import org.iplass.mtp.view.generic.common.AutocompletionProperty;
import org.iplass.mtp.view.generic.common.AutocompletionSetting;
import org.iplass.mtp.view.generic.common.JavascriptAutocompletionSetting;
import org.iplass.mtp.view.generic.common.WebApiAutocompletionSetting;

@XmlSeeAlso({MetaJavascriptAutocompletionSetting.class, MetaWebApiAutocompletionSetting.class})
public abstract class MetaAutocompletionSetting implements MetaData {

	private static final long serialVersionUID = 6255668944455905313L;

	public static MetaAutocompletionSetting createInstance(AutocompletionSetting setting) {
		if (setting instanceof JavascriptAutocompletionSetting) {
			return MetaJavascriptAutocompletionSetting.createInstance(setting);
		} else if (setting instanceof WebApiAutocompletionSetting) {
			return MetaWebApiAutocompletionSetting.createInstance(setting);
		}
		return null;
	}

	/** 連動元のプロパティ */
	private List<MetaAutocompletionProperty> properties;

	/** runtime id */
	private String runtimeKey;

	/**
	 * @return properties
	 */
	public List<MetaAutocompletionProperty> getProperties() {
		if (properties == null) properties = new ArrayList<>();
		return properties;
	}

	/**
	 * @param properties セットする properties
	 */
	public void setProperties(List<MetaAutocompletionProperty> properties) {
		this.properties = properties;
	}

	public void addProperty(MetaAutocompletionProperty property) {
		getProperties().add(property);
	}

	@XmlTransient
	public String getRuntimeKey() {
		return runtimeKey;
	}

	public void setRuntimeKey(String runtimeKey) {
		this.runtimeKey = runtimeKey;
	}

	public abstract void applyConfig(AutocompletionSetting setting, EntityHandler entity, EntityHandler rootEntity);

	protected void fillFrom(AutocompletionSetting setting, EntityHandler entity, EntityHandler rootEntity) {
		if (setting.getProperties() != null && !setting.getProperties().isEmpty()) {
			for (AutocompletionProperty property : setting.getProperties()) {
				MetaAutocompletionProperty meta = new MetaAutocompletionProperty();
				meta.applyConfig(property, entity, rootEntity);
				if (meta.getPropertyId() != null) {
					addProperty(meta);
				}
			}
		}
	}

	public abstract AutocompletionSetting currentConfig(EntityHandler entity, EntityHandler rootEntity);

	protected void fillTo(AutocompletionSetting setting, EntityHandler entity, EntityHandler rootEntity) {
		if (getProperties() != null && !getProperties().isEmpty()) {
			for (MetaAutocompletionProperty meta : getProperties()) {
				AutocompletionProperty property = meta.currentConfig(entity, rootEntity);
				if (property != null) {
					setting.addProperty(property);
				}
			}
		}
		setting.setRuntimeKey(runtimeKey);
	}

	public abstract AutocompletionSettingHandler getHandler(EntityViewRuntime entityView);

	public abstract class AutocompletionSettingHandler {

		/** メタデータ */
		private MetaAutocompletionSetting metadata;

		/**
		 * コンストラクタ
		 * @param metadata メタデータ
		 * @param entityView 画面定義
		 */
		public AutocompletionSettingHandler(MetaAutocompletionSetting metadata, EntityViewRuntime entityView) {
			this.metadata = metadata;
		}

		/**
		 * メタデータを取得します。
		 * @return メタデータ
		 */
		public MetaAutocompletionSetting getMetaData() {
			return metadata;
		}

		public abstract Object handle(Map<String, String[]> param, Object currentValue, boolean isReference);
	}
}
