/*
 * Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.webapi.jaxb;

import javax.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.auth.policy.definition.AuthenticationPolicyDefinition;
import org.iplass.mtp.command.async.definition.AsyncCommandDefinition;
import org.iplass.mtp.command.definition.CommandDefinition;
import org.iplass.mtp.definition.DefinitionSummary;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.properties.selectvalue.SelectValueDefinition;
import org.iplass.mtp.mail.template.definition.MailTemplateDefinition;
import org.iplass.mtp.message.MessageCategory;
import org.iplass.mtp.prefs.Preference;
import org.iplass.mtp.pushnotification.template.definition.PushNotificationTemplateDefinition;
import org.iplass.mtp.sms.template.definition.SmsMailTemplateDefinition;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.tenant.TenantAuthInfo;
import org.iplass.mtp.tenant.TenantI18nInfo;
import org.iplass.mtp.tenant.TenantMailInfo;
import org.iplass.mtp.tenant.web.TenantWebInfo;
import org.iplass.mtp.utilityclass.definition.UtilityClassDefinition;
import org.iplass.mtp.web.actionmapping.definition.ActionMappingDefinition;
import org.iplass.mtp.web.staticresource.definition.StaticResourceDefinition;
import org.iplass.mtp.web.template.definition.TemplateDefinition;
import org.iplass.mtp.webapi.definition.EntityWebApiDefinition;
import org.iplass.mtp.webapi.definition.WebApiDefinition;
import org.iplass.mtp.webhook.template.definition.WebHookTemplateDefinition;

@XmlSeeAlso({
	DefinitionSummary.class,
	ActionMappingDefinition.class
	,CommandDefinition.class
	,EntityDefinition.class
	,EntityWebApiDefinition.class
	,MailTemplateDefinition.class
	,MessageCategory.class
	,TemplateDefinition.class
	,Tenant.class
	,WebApiDefinition.class
	,UtilityClassDefinition.class
	,SelectValueDefinition.class
	,AsyncCommandDefinition.class
	,Preference.class
	,AuthenticationPolicyDefinition.class
	,StaticResourceDefinition.class
	,PushNotificationTemplateDefinition.class
	,SmsMailTemplateDefinition.class
	,WebHookTemplateDefinition.class

	//FIXME 今はTenantで参照しているが、参照しなくなるので追加
	,TenantAuthInfo.class
	,TenantMailInfo.class
	,TenantI18nInfo.class
	,TenantWebInfo.class
	})
public class BuiltinDefinitions {

}
