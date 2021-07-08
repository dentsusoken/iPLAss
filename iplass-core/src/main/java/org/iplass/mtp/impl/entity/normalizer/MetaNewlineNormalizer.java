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

import java.util.regex.Pattern;

import org.iplass.mtp.entity.ValidationContext;
import org.iplass.mtp.entity.definition.NormalizerDefinition;
import org.iplass.mtp.entity.definition.normalizers.NewlineNormalizer;
import org.iplass.mtp.entity.definition.normalizers.NewlineType;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.entity.property.MetaProperty;
import org.iplass.mtp.impl.util.ObjectUtil;

public class MetaNewlineNormalizer extends MetaNormalizer {
	private static final long serialVersionUID = -10976021022360335L;

	private static Pattern NEWLINE_PATTERN = Pattern.compile("\\r\\n|\\r|\\n");
	private static String CRLF_REPLACE = "\r\n";
	private static String LF_REPLACE = "\n";

	private NewlineType type;

	public NewlineType getType() {
		return type;
	}

	public void setType(NewlineType type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		MetaNewlineNormalizer other = (MetaNewlineNormalizer) obj;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public MetaNewlineNormalizer copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public void applyConfig(NormalizerDefinition definition) {
		this.type = ((NewlineNormalizer) definition).getType();
	}

	@Override
	public NewlineNormalizer currentConfig(EntityContext context) {
		return new NewlineNormalizer(type);
	}

	@Override
	public NormalizerRuntime createRuntime(MetaEntity entity, MetaProperty property) {
		return new NewlineNormalizerRuntime(property);
	}
	
	public class NewlineNormalizerRuntime extends NormalizerRuntime {
		private String replacement;
		
		NewlineNormalizerRuntime(MetaProperty property) {
			if (type == null) {
				throw new NullPointerException(property.getName() + "'s NewlineNormalizer type must specified");
			}
			switch (type) {
			case CRLF:
				replacement = CRLF_REPLACE;
				break;
			case LF:
				replacement = LF_REPLACE;
				break;
			default:
				break;
			}
		}

		@Override
		public Object normalize(Object value, ValidationContext context) {
			if (value == null) {
				return null;
			}
			
			return NEWLINE_PATTERN.matcher(value.toString()).replaceAll(replacement);
		}
		
	}

}
