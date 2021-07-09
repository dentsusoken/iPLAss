/*
 * Copyright (C) 2021 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.entity.normalizer;

import java.text.Normalizer;

import org.iplass.mtp.entity.ValidationContext;
import org.iplass.mtp.entity.definition.NormalizerDefinition;
import org.iplass.mtp.entity.definition.normalizers.UnicodeNormalizer;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.entity.property.MetaProperty;
import org.iplass.mtp.impl.util.ObjectUtil;

public class MetaUnicodeNormalizer extends MetaNormalizer {
	private static final long serialVersionUID = 7571032436814456562L;

	private String form;

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((form == null) ? 0 : form.hashCode());
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
		MetaUnicodeNormalizer other = (MetaUnicodeNormalizer) obj;
		if (form == null) {
			if (other.form != null)
				return false;
		} else if (!form.equals(other.form))
			return false;
		return true;
	}

	@Override
	public MetaUnicodeNormalizer copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public void applyConfig(NormalizerDefinition definition) {
		this.form = ((UnicodeNormalizer) definition).getForm();
	}

	@Override
	public UnicodeNormalizer currentConfig(EntityContext context) {
		UnicodeNormalizer def = new UnicodeNormalizer();
		def.setForm(form);
		return def;
	}

	@Override
	public NormalizerRuntime createRuntime(MetaEntity entity, MetaProperty property) {
		return new UnicodeNormalizerRuntime(property);
	}
	
	public class UnicodeNormalizerRuntime extends NormalizerRuntime {
		private Normalizer.Form formEnum;
		
		UnicodeNormalizerRuntime(MetaProperty property) {
			if (form == null) {
				throw new NullPointerException(property.getName() + "'s UnicodeNormalizer form must specified");
			}
			formEnum = Normalizer.Form.valueOf(form);
		}

		@Override
		public Object normalize(Object value, ValidationContext context) {
			if (value == null) {
				return null;
			}
			
			return Normalizer.normalize(value.toString(), formEnum);
		}
		
	}

}
