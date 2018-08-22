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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.InsertOption;
import org.iplass.mtp.entity.UpdateOption;
import org.iplass.mtp.entity.definition.DataLocalizationStrategy;
import org.iplass.mtp.entity.definition.l10n.EachInstanceDataLocalizationStrategy;
import org.iplass.mtp.entity.definition.l10n.EachPropertyDataLocalizationStrategy;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.metadata.MetaData;

@XmlSeeAlso({MetaEachInstanceDataLocalizationStrategy.class, MetaEachPropertyDataLocalizationStrategy.class})
public abstract class MetaDataLocalizationStrategy implements MetaData {
	private static final long serialVersionUID = 4197648632656222700L;
	
	public static MetaDataLocalizationStrategy newInstance(DataLocalizationStrategy def) {
		if (def instanceof EachInstanceDataLocalizationStrategy) {
			return new MetaEachInstanceDataLocalizationStrategy();
		}
		if (def instanceof EachPropertyDataLocalizationStrategy) {
			return new MetaEachPropertyDataLocalizationStrategy();
		}
		return null;
	}

	public static abstract class DataLocalizationStrategyRuntime {
		public abstract void handleEntityForInsert(Entity e, InsertOption option);
		public abstract void handleEntityForUpdate(Entity e, UpdateOption option);
		public abstract Entity handleEntityForLoad(Entity e);
	}
	
	private List<String> languages;
	
	public MetaDataLocalizationStrategy() {
	}
	
	public MetaDataLocalizationStrategy(List<String> languages) {
		this.languages = languages;
	}

	/**
	 * 対応する言語を取得。
	 * 
	 * @return
	 */
	public List<String> getLanguages() {
		return languages;
	}

	/**
	 * 対応する言語をセット。
	 * セット可能な言語は、I18nServiceに定義されるenableLanguagesのlanguageKey。
	 * 
	 * @param languages
	 */
	public void setLanguages(List<String> languages) {
		this.languages = languages;
	}
	
	

	public abstract void applyConfig(DataLocalizationStrategy def);
	
	public abstract DataLocalizationStrategy currentConfig();
	
	@Override
	public abstract MetaDataLocalizationStrategy copy();
	
	public abstract DataLocalizationStrategyRuntime createDataLocalizationStrategyRuntime(EntityHandler eh);
	
	protected void copyTo(MetaDataLocalizationStrategy c) {
		if (languages != null) {
			c.languages = new ArrayList<>(languages);
		}
	}
	
	protected void fillFrom(DataLocalizationStrategy def) {
		if (def.getLanguages() != null) {
			languages = new ArrayList<>(def.getLanguages());
		} else {
			languages = null;
		}
	}
	
	protected void fillTo(DataLocalizationStrategy def) {
		if (languages != null) {
			def.setLanguages(new ArrayList<>(languages));
		} else {
			def.setLanguages(null);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((languages == null) ? 0 : languages.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MetaDataLocalizationStrategy other = (MetaDataLocalizationStrategy) obj;
		if (languages == null) {
			if (other.languages != null)
				return false;
		} else if (!languages.equals(other.languages))
			return false;
		return true;
	}

}
