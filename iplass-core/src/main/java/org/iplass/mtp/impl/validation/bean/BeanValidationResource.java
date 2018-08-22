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
package org.iplass.mtp.impl.validation.bean;

import javax.validation.MessageInterpolator;

import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.core.TenantResource;
import org.iplass.mtp.impl.validation.ValidationService;
import org.iplass.mtp.spi.ServiceRegistry;

public class BeanValidationResource implements TenantResource {
	
	private MessageInterpolator messageInterpolator;

	public MessageInterpolator getMessageInterpolator() {
		return messageInterpolator;
	}

	@Override
	public void init(TenantContext tenantContext) {
		ValidationService vs = ServiceRegistry.getRegistry().getService(ValidationService.class);
		BeanValidationConfig bvConfig = vs.getBeanValidation();
		if (bvConfig != null) {
			MessageInterpolator mi = bvConfig.getMessageInterpolator();
			if (mi instanceof TenantContextMessageInterpolator) {
				messageInterpolator = ((TenantContextMessageInterpolator) mi).createMessageInterpolatorForTenant(tenantContext.getTenantId());
			}
		}
	}

	@Override
	public void destory() {
	}

}
