/*
 * Copyright (C) 2018 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.entity.definition.listeners;

import org.iplass.mtp.definition.Definition;
import org.iplass.mtp.mail.template.definition.MailTemplateDefinition;
import org.iplass.mtp.pushnotification.template.definition.PushNotificationTemplateDefinition;
import org.iplass.mtp.sms.template.definition.SmsMailTemplateDefinition;
import org.iplass.mtp.webhook.template.definition.WebhookTemplateDefinition;

/**
 * 送信種別を表すenum。
 */
public enum SendNotificationType {

	MAIL("Mail", MailTemplateDefinition.class),
	SMS("SMS", SmsMailTemplateDefinition.class),
	PUSH("PushNotification", PushNotificationTemplateDefinition.class),
	WEBHOOK("Webhook", WebhookTemplateDefinition.class);

	private String displayName;
	private Class<? extends Definition> definitionClass;

	private SendNotificationType(String displayName, Class<? extends Definition> definitionClass) {
		this.displayName = displayName;
		this.definitionClass = definitionClass;
	}

	public String displayName() {
		return displayName;
	}

	public Class<? extends Definition> definitionClass() {
		return definitionClass;
	}
}
