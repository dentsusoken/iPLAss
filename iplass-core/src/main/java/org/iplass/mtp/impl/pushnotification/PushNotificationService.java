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

import java.util.HashMap;
import java.util.List;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.impl.definition.AbstractTypedMetaDataService;
import org.iplass.mtp.impl.definition.DefinitionMetaDataTypeMap;
import org.iplass.mtp.impl.definition.DefinitionNameChecker;
import org.iplass.mtp.impl.pushnotification.template.MetaPushNotificationTemplate;
import org.iplass.mtp.impl.pushnotification.template.MetaPushNotificationTemplate.PushNotificationTemplateRuntime;
import org.iplass.mtp.pushnotification.PushNotification;
import org.iplass.mtp.pushnotification.PushNotificationException;
import org.iplass.mtp.pushnotification.PushNotificationListener;
import org.iplass.mtp.pushnotification.PushNotificationResult;
import org.iplass.mtp.pushnotification.template.definition.PushNotificationTemplateDefinition;
import org.iplass.mtp.pushnotification.template.definition.PushNotificationTemplateDefinitionManager;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.tenant.Tenant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PushNotificationService extends AbstractTypedMetaDataService<MetaPushNotificationTemplate, PushNotificationTemplateRuntime> implements Service {
	public static final String PUSH_NOTIFICATION_TEMPLATE_META_PATH = "/pushNotification/template/";

	private static Logger logger = LoggerFactory.getLogger(PushNotificationService.class);

	public static class TypeMap extends DefinitionMetaDataTypeMap<PushNotificationTemplateDefinition, MetaPushNotificationTemplate> {
		public TypeMap() {
			super(getFixedPath(), MetaPushNotificationTemplate.class, PushNotificationTemplateDefinition.class);
		}
		@Override
		public TypedDefinitionManager<PushNotificationTemplateDefinition> typedDefinitionManager() {
			return ManagerLocator.getInstance().getManager(PushNotificationTemplateDefinitionManager.class);
		}

		@Override
		protected DefinitionNameChecker createDefinitionNameChecker() {
			return DefinitionNameChecker.getPathSlashDefinitionNameChecker();
		}
	}

	private List<PushNotificationListener> listener;

	public static String getFixedPath() {
		return PUSH_NOTIFICATION_TEMPLATE_META_PATH;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void init(Config config) {
		if (config.getBeans("listener") != null) {
			this.listener = (List<PushNotificationListener>) config.getBeans("listener");
		}
	}

	@Override
	public void destroy() {
	}

	public final PushNotificationResult push(Tenant tenant, PushNotification notification) {
		try {
			if (listener != null) {
				for (PushNotificationListener l: listener) {
					if (!l.beforePush(notification)) {
						logger.info("Push notification canceled by Listener:" + l);
						HashMap<String, Object> details = new HashMap<>();
						details.put("listener", l.getClass());
						details.put("canceled", true);
						return new PushNotificationResult(false, details);
					}
				}
			}

			PushNotificationResult res = pushImpl(tenant, notification);

			if (listener != null) {
				for (PushNotificationListener l: listener) {
					l.onSuccess(notification, res);
				}
			}

			return res;

		} catch (Exception e) {
			if (listener != null) {
				for (PushNotificationListener l: listener) {
					if (!l.onFailure(notification, e)) {
						HashMap<String, Object> details = new HashMap<>();
						details.put("listener", l.getClass());
						details.put("exceptionConsumed", true);
						details.put("exception", e);
						return new PushNotificationResult(true, details);
					}
				}
			}
			if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			} else {
				throw new PushNotificationException(e.getMessage(), e);
			}
		}
	}

	protected PushNotificationResult pushImpl(Tenant tenant, PushNotification notification) {
		//noop
		logger.warn("can't push notification, use FCMPushNotificationService or own implementation of PushNotificationService. toList:" + notification.getToList() + ", notification:" + notification.getNotification() + ", data:" + notification.getData());
		HashMap<String, Object> details = new HashMap<>();
		details.put("noop", true);
		return new PushNotificationResult(true, details);
	}

	@Override
	public Class<MetaPushNotificationTemplate> getMetaDataType() {
		return MetaPushNotificationTemplate.class;
	}

	@Override
	public Class<PushNotificationTemplateRuntime> getRuntimeType() {
		return PushNotificationTemplateRuntime.class;
	}

}
