/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.entity.l10n;

import java.util.List;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.InsertOption;
import org.iplass.mtp.entity.UpdateOption;
import org.iplass.mtp.entity.definition.DataLocalizationStrategy;
import org.iplass.mtp.entity.definition.l10n.EachInstanceDataLocalizationStrategy;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.entity.EntityHandler;


public class MetaEachInstanceDataLocalizationStrategy extends MetaDataLocalizationStrategy {
	private static final long serialVersionUID = -6948763544125469746L;

	private String languagePropertyName;
	
	public MetaEachInstanceDataLocalizationStrategy() {
	}

	public MetaEachInstanceDataLocalizationStrategy(List<String> languages, String languagePropertyName) {
		super(languages);
		this.languagePropertyName = languagePropertyName;
	}

	public String getLanguagePropertyName() {
		return languagePropertyName;
	}

	/**
	 * Entityに定義される"言語判別用プロパティ"（文字列型）のプロパティ名を設定。　
	 * @param languagePropertyName
	 */
	public void setLanguagePropertyName(String languagePropertyName) {
		this.languagePropertyName = languagePropertyName;
	}

	@Override
	public MetaDataLocalizationStrategy copy() {
		MetaEachInstanceDataLocalizationStrategy copy = new MetaEachInstanceDataLocalizationStrategy();
		copyTo(copy);
		copy.languagePropertyName = languagePropertyName;
		return copy;
	}

	@Override
	public void applyConfig(DataLocalizationStrategy d) {
		EachInstanceDataLocalizationStrategy def = (EachInstanceDataLocalizationStrategy) d;
		fillFrom(def);
		languagePropertyName = def.getLanguagePropertyName();
	}

	@Override
	public DataLocalizationStrategy currentConfig() {
		EachInstanceDataLocalizationStrategy def = new EachInstanceDataLocalizationStrategy();
		fillTo(def);
		def.setLanguagePropertyName(languagePropertyName);
		return def;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ((languagePropertyName == null) ? 0 : languagePropertyName
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		MetaEachInstanceDataLocalizationStrategy other = (MetaEachInstanceDataLocalizationStrategy) obj;
		if (languagePropertyName == null) {
			if (other.languagePropertyName != null)
				return false;
		} else if (!languagePropertyName.equals(other.languagePropertyName))
			return false;
		return true;
	}

	@Override
	public DataLocalizationStrategyRuntime createDataLocalizationStrategyRuntime(EntityHandler eh) {
		return new EachInstanceDataLocalizationStrategyRuntime();
	}
	
	public class EachInstanceDataLocalizationStrategyRuntime extends DataLocalizationStrategyRuntime {

		@Override
		public void handleEntityForInsert(Entity e, InsertOption option) {
			String userLang = ExecuteContext.getCurrentContext().getLanguage();
			if (getLanguages() != null && getLanguages().contains(userLang)) {
				e.setValue(languagePropertyName, userLang);
			} else {
				e.setValue(languagePropertyName, null);
			}
		}

		@Override
		public void handleEntityForUpdate(Entity e, UpdateOption option) {
			String userLang = ExecuteContext.getCurrentContext().getLanguage();
			if (getLanguages() != null && getLanguages().contains(userLang)) {
				e.setValue(languagePropertyName, userLang);
			} else {
				e.setValue(languagePropertyName, null);
			}
			if (option.getUpdateProperties() == null || !option.getUpdateProperties().contains(languagePropertyName)) {
				option.add(languagePropertyName);
			}
		}

		@Override
		public Entity handleEntityForLoad(Entity e) {
			return e;
		}
	}

}
