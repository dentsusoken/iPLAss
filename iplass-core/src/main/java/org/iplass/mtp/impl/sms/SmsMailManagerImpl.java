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

package org.iplass.mtp.impl.sms;

import java.util.Map;

import org.iplass.mtp.SystemException;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.sms.template.MetaSmsMailTemplate.SmsMailTemplateRuntime;
import org.iplass.mtp.sms.SmsMail;
import org.iplass.mtp.sms.SmsMailManager;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.Tenant;

/**
 * SmsMailManager実装.
 */
public class SmsMailManagerImpl implements SmsMailManager {

	SmsService smsService = ServiceRegistry.getRegistry().getService(SmsService.class);

	@Override
	public SmsMail createMail() {
		return smsService.createMail(ExecuteContext.getCurrentContext().getCurrentTenant());
	}

	@Override
	public SmsMail createMail(String tmplDefName, Map<String, Object> bindings) {
		SmsMailTemplateRuntime tr = smsService.getRuntimeByName(tmplDefName);
		if (tr == null) {
			throw new SystemException("smsMailTemplate:" + tmplDefName + " not found");
		}
		return tr.createMail(bindings);
	}

	@Override
	public void sendMail(SmsMail smsMail) {
		Tenant tenant = ExecuteContext.getCurrentContext().getCurrentTenant();
		smsService.sendSmsMail(tenant, smsMail);
	}
}
