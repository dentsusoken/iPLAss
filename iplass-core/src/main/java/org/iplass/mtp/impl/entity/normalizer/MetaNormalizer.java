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

import javax.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.entity.ValidationContext;
import org.iplass.mtp.entity.definition.NormalizerDefinition;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.entity.property.MetaProperty;
import org.iplass.mtp.impl.metadata.MetaData;

@XmlSeeAlso({
	MetaICUTransliterator.class,
	MetaJavaClassNormalizer.class,
	MetaNewlineNormalizer.class,
	MetaRegexReplace.class,
	MetaScriptingNormalizer.class,
	MetaUnicodeNormalizer.class,
	MetaWhiteSpaceTrimmer.class})
public abstract class MetaNormalizer implements MetaData {
	private static final long serialVersionUID = 5895313087859012114L;

	@Override
	public abstract MetaNormalizer copy();
	
	public abstract void applyConfig(NormalizerDefinition definition);

	public abstract NormalizerDefinition currentConfig(EntityContext context);

	public abstract NormalizerRuntime createRuntime(MetaEntity entity, MetaProperty property);
	
	public abstract class NormalizerRuntime {
		
		public abstract Object normalize(Object value, ValidationContext context);

		public Object normalizeArray(Object[] values, ValidationContext context) {
			if (values != null) {
				for (int i = 0; i < values.length; i++) {
					values[i] = normalize(values[i], context);
				}
			}
			return values;
		}
	}

}
