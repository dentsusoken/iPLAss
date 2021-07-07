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

import org.apache.commons.lang3.StringUtils;
import org.iplass.mtp.entity.ValidationContext;
import org.iplass.mtp.entity.definition.NormalizerDefinition;
import org.iplass.mtp.entity.definition.normalizers.WhiteSpaceTrimmer;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.entity.property.MetaProperty;
import org.iplass.mtp.impl.util.ObjectUtil;

public class MetaWhiteSpaceTrimmer extends MetaNormalizer {
	private static final long serialVersionUID = -5009269860554610788L;
	private static final int hash = -41241865;

	public static void main(String[] args) {
		System.out.println(MetaWhiteSpaceTrimmer.class.getName().hashCode());
	}

	@Override
	public int hashCode() {
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		return true;
	}

	
	@Override
	public MetaWhiteSpaceTrimmer copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public void applyConfig(NormalizerDefinition definition) {
	}

	@Override
	public WhiteSpaceTrimmer currentConfig(EntityContext context) {
		return new WhiteSpaceTrimmer();
	}

	@Override
	public NormalizerRuntime createRuntime(MetaEntity entity, MetaProperty property) {
		return new WhiteSpaceTrimmerRuntime();
	}
	
	public class WhiteSpaceTrimmerRuntime extends NormalizerRuntime {

		@Override
		public Object normalize(Object value, ValidationContext context) {
			if (value == null) {
				return null;
			}
			
			return StringUtils.strip(value.toString());
		}
		
	}

}
