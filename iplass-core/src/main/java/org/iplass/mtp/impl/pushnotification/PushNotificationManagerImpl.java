/*
 * Copyright (C) 2016 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.pushnotification;

import java.util.Map;

import org.iplass.mtp.SystemException;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.pushnotification.template.MetaPushNotificationTemplate.PushNotificationTemplateRuntime;
import org.iplass.mtp.pushnotification.PushNotification;
import org.iplass.mtp.pushnotification.PushNotificationManager;
import org.iplass.mtp.pushnotification.PushNotificationResult;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.Tenant;

public class PushNotificationManagerImpl implements PushNotificationManager {

	private PushNotificationService pnService = ServiceRegistry.getRegistry().getService(PushNotificationService.class);

	@Override
	public PushNotification createNotification(String tmplDefName,
			Map<String, Object> bindings) {

		PushNotificationTemplateRuntime tr = pnService.getRuntimeByName(tmplDefName);
		if (tr == null) {
			throw new SystemException("Can not find template:" + tmplDefName);
		}

		return tr.createPushNotification(bindings);
	}

	@Override
	public PushNotificationResult push(PushNotification notification) {
		Tenant tenant = ExecuteContext.getCurrentContext().getCurrentTenant();
		return pnService.push(tenant, notification);
	}

}
