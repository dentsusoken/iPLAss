/*
 * Copyright (C) 2021 DENTSU SOKEN INC. All Rights Reserved.
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

import org.iplass.mtp.entity.ValidationContext;
import org.iplass.mtp.entity.definition.NormalizerDefinition;
import org.iplass.mtp.entity.definition.normalizers.ICUTransliterator;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.entity.property.MetaProperty;
import org.iplass.mtp.impl.util.ObjectUtil;

import com.ibm.icu.text.Transliterator;

public class MetaICUTransliterator extends MetaNormalizer {
	private static final long serialVersionUID = 8075490131535862272L;

	private String transliteratorId;

	public String getTransliteratorId() {
		return transliteratorId;
	}

	public void setTransliteratorId(String transliteratorId) {
		this.transliteratorId = transliteratorId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((transliteratorId == null) ? 0 : transliteratorId.hashCode());
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
		MetaICUTransliterator other = (MetaICUTransliterator) obj;
		if (transliteratorId == null) {
			if (other.transliteratorId != null)
				return false;
		} else if (!transliteratorId.equals(other.transliteratorId))
			return false;
		return true;
	}

	@Override
	public MetaICUTransliterator copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public void applyConfig(NormalizerDefinition definition) {
		ICUTransliterator d = (ICUTransliterator) definition;
		this.transliteratorId = d.getTransliteratorId();
	}

	@Override
	public ICUTransliterator currentConfig(EntityContext context) {
		return new ICUTransliterator(transliteratorId);
	}

	@Override
	public NormalizerRuntime createRuntime(MetaEntity entity, MetaProperty property) {
		return new ICUTransliteratorRuntime(property);
	}
	
	public class ICUTransliteratorRuntime extends NormalizerRuntime {
		
		private Transliterator trans;
		
		ICUTransliteratorRuntime(MetaProperty property) {
			if (transliteratorId == null) {
				throw new NullPointerException(property.getName() + "'s ICUTransliterator transliteratorId must specified");
			}
			trans = Transliterator.getInstance(transliteratorId);
		}

		@Override
		public Object normalize(Object value, ValidationContext context) {
			if (value == null) {
				return null;
			}
			
			return trans.transliterate(value.toString());
		}
		
	}

}
