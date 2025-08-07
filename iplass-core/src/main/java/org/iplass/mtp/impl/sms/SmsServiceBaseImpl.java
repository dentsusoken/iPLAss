/*
 * Copyright (C) 2015 DENTSU SOKEN INC. All Rights Reserved.
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

import java.util.List;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.impl.definition.AbstractTypedMetaDataService;
import org.iplass.mtp.impl.definition.DefinitionMetaDataTypeMap;
import org.iplass.mtp.impl.definition.DefinitionNameChecker;
import org.iplass.mtp.impl.sms.template.MetaSmsMailTemplate;
import org.iplass.mtp.impl.sms.template.MetaSmsMailTemplate.SmsMailTemplateRuntime;
import org.iplass.mtp.sms.SendSmsMailListener;
import org.iplass.mtp.sms.SmsException;
import org.iplass.mtp.sms.SmsMail;
import org.iplass.mtp.sms.template.definition.SmsMailTemplateDefinition;
import org.iplass.mtp.sms.template.definition.SmsMailTemplateDefinitionManager;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.tenant.Tenant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SmeServiceのベース実装.
 * <p>テンプレート操作など最低限の操作のみ.</p>
 */
public class SmsServiceBaseImpl extends AbstractTypedMetaDataService<MetaSmsMailTemplate, SmsMailTemplateRuntime> implements SmsService {
	public static final String SMS_TEMPLATE_META_PATH = "/sms/template/";

	private static Logger logger = LoggerFactory.getLogger(SmsServiceBaseImpl.class);

	public static class TypeMap extends DefinitionMetaDataTypeMap<SmsMailTemplateDefinition, MetaSmsMailTemplate> {
		public TypeMap() {
			super(getFixedPath(), MetaSmsMailTemplate.class, SmsMailTemplateDefinition.class);
		}
		@Override
		public TypedDefinitionManager<SmsMailTemplateDefinition> typedDefinitionManager() {
			return ManagerLocator.getInstance().getManager(SmsMailTemplateDefinitionManager.class);
		}

		@Override
		protected DefinitionNameChecker createDefinitionNameChecker() {
			return DefinitionNameChecker.getPathSlashDefinitionNameChecker();
		}
	}

	private String defaultFrom;
	private List<SendSmsMailListener> listener;

	@SuppressWarnings("unchecked")
	@Override
	public void init(Config config) {
		this.defaultFrom = config.getValue("defaultFrom");
		if (config.getBeans("listener") != null) {
			this.listener = (List<SendSmsMailListener>) config.getBeans("listener");
		}

	}

	@Override
	public void destroy() {
	}

	public static String getFixedPath() {
		return SMS_TEMPLATE_META_PATH;
	}

	@Override
	public final void sendSmsMail(Tenant tenant, SmsMail sms) {
		try {
			if (!fireOnSendMail(sms)) {
				return;
			}

			sendSmsMailImpl(tenant, sms);

			fireOnSuccess(sms);

		} catch (Exception e) {
			handleException(sms, e);
		}
	}

	protected boolean fireOnSendMail(final SmsMail mail) {
		if (listener != null) {
			for (SendSmsMailListener l: listener) {
				if (!l.beforeSend(mail)) {
					logger.info("send mail canceled by Listener:" + l);
					return false;
				}
			}
		}
		return true;
	}

	protected void fireOnSuccess(SmsMail mail) {
		if (listener != null) {
			for (SendSmsMailListener l: listener) {
				l.onSuccess(mail);
			}
		}
	}

	protected void handleException(SmsMail mail, Exception e) {
		if (listener != null) {
			for (SendSmsMailListener l: listener) {
				if (!l.onFailure(mail, e)) {
					return;
				}
			}
		}
		if (e instanceof RuntimeException) {
			throw (RuntimeException) e;
		} else {
			throw new SmsException(e.getMessage(), e);
		}
	}

	protected void sendSmsMailImpl(Tenant tenant, SmsMail sms) {
		//noope
		logger.warn("can't send sms, use TwilioSmsService or own implementation of SmsService.To:" + sms.getTo() + ", From:" + sms.getFrom() + ", Message:" + sms.getMessage());
	}

	@Override
	public SmsMail createMail(Tenant tenant) {
		SmsMail smsMail = new SmsMail();
		smsMail.setFrom(defaultFrom);
		return smsMail;
	}

	public String getDefaultFrom() {
		return defaultFrom;
	}

	public void setDefaultFrom(String defaultFrom) {
		this.defaultFrom = defaultFrom;
	}

	@Override
	public Class<MetaSmsMailTemplate> getMetaDataType() {
		return MetaSmsMailTemplate.class;
	}

	@Override
	public Class<SmsMailTemplateRuntime> getRuntimeType() {
		return SmsMailTemplateRuntime.class;
	}
}
