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

import org.iplass.mtp.entity.PropertyNormalizer;
import org.iplass.mtp.entity.ValidationContext;
import org.iplass.mtp.entity.definition.NormalizerDefinition;
import org.iplass.mtp.entity.definition.normalizers.JavaClassNormalizer;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.entity.property.MetaProperty;
import org.iplass.mtp.impl.util.ObjectUtil;

public class MetaJavaClassNormalizer extends MetaNormalizer {
	private static final long serialVersionUID = 625875739932037108L;

	private String className;
	private boolean asArray = false;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public boolean isAsArray() {
		return asArray;
	}

	public void setAsArray(boolean asArray) {
		this.asArray = asArray;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (asArray ? 1231 : 1237);
		result = prime * result + ((className == null) ? 0 : className.hashCode());
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
		MetaJavaClassNormalizer other = (MetaJavaClassNormalizer) obj;
		if (asArray != other.asArray)
			return false;
		if (className == null) {
			if (other.className != null)
				return false;
		} else if (!className.equals(other.className))
			return false;
		return true;
	}

	@Override
	public MetaJavaClassNormalizer copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public void applyConfig(NormalizerDefinition definition) {
		JavaClassNormalizer d = (JavaClassNormalizer) definition;
		this.className = d.getClassName();
		this.asArray = d.isAsArray();
	}

	@Override
	public JavaClassNormalizer currentConfig(EntityContext context) {
		JavaClassNormalizer d = new JavaClassNormalizer();
		d.setClassName(className);
		d.setAsArray(asArray);
		return d;
	}

	@Override
	public NormalizerRuntime createRuntime(MetaEntity entity, MetaProperty property) {
		return new JavaClassNormalizerRuntime(property);
	}
	
	public class JavaClassNormalizerRuntime extends NormalizerRuntime {
		
		PropertyNormalizer<?> propertyNormalizer;
		
		public JavaClassNormalizerRuntime(MetaProperty property) {
			if (className == null) {
				throw new NullPointerException(property.getName() + "'s JavaClassNormalizer className must specified");
			}
			
			try {
				propertyNormalizer = (PropertyNormalizer<?>) Class.forName(className).newInstance();
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				throw new IllegalStateException("can not instantiate " + className + " of " + property.getName() + "'s JavaClassNormalizer", e);
			}
		}

		@Override
		public Object normalize(Object value, ValidationContext context) {
			return propertyNormalizer.normalize(value, context);
		}

		@Override
		public Object normalizeArray(Object[] values, ValidationContext context) {
			if (asArray) {
				return normalize(values, context);
			} else {
				return super.normalizeArray(values, context);
			}
		}
	}

}
