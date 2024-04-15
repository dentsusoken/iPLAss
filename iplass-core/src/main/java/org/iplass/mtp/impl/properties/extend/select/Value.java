/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.properties.extend.select;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.entity.definition.LocalizedSelectValueDefinition;
import org.iplass.mtp.entity.definition.properties.selectvalue.SelectValueDefinition;
import org.iplass.mtp.impl.i18n.EnableLanguages;
import org.iplass.mtp.impl.i18n.I18nService;
import org.iplass.mtp.impl.i18n.MetaLocalizedString;
import org.iplass.mtp.spi.ServiceRegistry;

@XmlAccessorType(XmlAccessType.FIELD)
public class Value implements Serializable {
	private static final long serialVersionUID = 2187443429118008336L;
	
	private static I18nService i18nService = ServiceRegistry.getRegistry().getService(I18nService.class);

	public static SelectValueDefinition toSelectValueDefinition(List<Value> values) {
		SelectValueDefinition def = new SelectValueDefinition();
		if (values != null) {
			List<EnableLanguages> els = i18nService.getEnableLanguages();
			@SuppressWarnings("unchecked")
			List<SelectValue>[] lsvl = new List[els.size()];
			for (int i = 0; i < values.size(); i++) {
				Value val = values.get(i);
				def.addSelectValue(new SelectValue(val.getValue(), val.getDisplayName()));
				if (val.getLocalizedDisplayNameList() != null) {
					for (int j = 0; j < els.size(); j++) {
						for (MetaLocalizedString mls: val.getLocalizedDisplayNameList()) {
							if (els.get(j).getLanguageKey().equals(mls.getLocaleName())) {
								if (lsvl[j] == null) {
									lsvl[j] = new ArrayList<>(values.size());
								}
								lsvl[j].add(new SelectValue(val.getValue(), mls.getStringValue()));
								break;
							}
						}
					}
				}
			}
			
			for (int i = 0; i < els.size(); i++) {
				def.addLocalizedSelectValue(new LocalizedSelectValueDefinition(els.get(i).getLanguageKey(), lsvl[i]));
			}
		}
		return def;
	}
	
	public static List<Value> toValues(SelectValueDefinition def) {
		ArrayList<Value> values = new ArrayList<>();
		if (def.getSelectValueList() != null) {
			for (SelectValue v: def.getSelectValueList()) {
				Value value = new Value(v.getValue(), v.getDisplayName());

				List<MetaLocalizedString> mlsList = new ArrayList<MetaLocalizedString>();
				if (def.getLocalizedSelectValueList() != null) {
					for (LocalizedSelectValueDefinition lsvd: def.getLocalizedSelectValueList()) {
						if (lsvd.getSelectValueList() != null) {
							for (SelectValue lsv :lsvd.getSelectValueList()) {
								if (v.getValue().equals(lsv.getValue())) {
									MetaLocalizedString mls = new MetaLocalizedString();
									mls.setLocaleName(lsvd.getLocaleName());
									mls.setStringValue(lsv.getDisplayName());
									mlsList.add(mls);
									break;
								}
							}
						}
					}
				}

				if (mlsList.size() > 0) {
					value.setLocalizedDisplayNameList(mlsList);
				}
				values.add(value);
			}
		}
		return values;
	}
	
	
	String value;
	String displayName;
	private List<MetaLocalizedString> localizedDisplayNameList;

	public List<MetaLocalizedString> getLocalizedDisplayNameList() {
		return localizedDisplayNameList;
	}

	public void setLocalizedDisplayNameList(
			List<MetaLocalizedString> localizedDisplayNameList) {
		this.localizedDisplayNameList = localizedDisplayNameList;
	}

	public Value() {
	}

	public Value(String value, String displayName) {
		this.value = value;
		this.displayName = displayName;
	}

	public String getValue() {
		return value;
	}
	public String getDisplayName() {
		return displayName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((displayName == null) ? 0 : displayName.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		Value other = (Value) obj;
		if (displayName == null) {
			if (other.displayName != null)
				return false;
		} else if (!displayName.equals(other.displayName))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	
}
