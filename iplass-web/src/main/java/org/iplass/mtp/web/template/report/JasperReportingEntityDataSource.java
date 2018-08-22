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

package org.iplass.mtp.web.template.report;

import java.util.Collection;

import org.iplass.mtp.entity.Entity;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class JasperReportingEntityDataSource extends JRBeanCollectionDataSource {

	public JasperReportingEntityDataSource(Collection<?> beanCollection) {
		super(beanCollection);
	}

	public JasperReportingEntityDataSource(Collection<?> beanCollection, boolean isUseFieldDescription) {
		super(beanCollection, isUseFieldDescription);
	}

	@Override
	protected Object getFieldValue(Object bean, JRField field) throws JRException {
		if (bean instanceof Entity) {
			return getEntityFieldValue((Entity)bean, getPropertyName(field));
		} else {
			return getBeanProperty(bean, getPropertyName(field));
		}
	}

	protected Object getEntityFieldValue(Entity entity, String propertyName) throws JRException {
		return entity.getValue(propertyName);
	}

}
