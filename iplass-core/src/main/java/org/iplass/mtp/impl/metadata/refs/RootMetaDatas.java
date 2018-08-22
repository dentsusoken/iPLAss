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
package org.iplass.mtp.impl.metadata.refs;

import javax.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.impl.auth.authenticate.builtin.policy.MetaAuthenticationPolicy;
import org.iplass.mtp.impl.command.MetaInterceptorClass;
import org.iplass.mtp.impl.command.MetaMetaCommand;
import org.iplass.mtp.impl.command.async.MetaAsyncCommand;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.mail.template.MetaMailTemplate;
import org.iplass.mtp.impl.message.MetaMessageCategory;
import org.iplass.mtp.impl.metadata.BaseRootMetaData;
import org.iplass.mtp.impl.prefs.MetaPreference;
import org.iplass.mtp.impl.properties.extend.select.MetaSelectValue;
import org.iplass.mtp.impl.pushnotification.template.MetaPushNotificationTemplate;
import org.iplass.mtp.impl.script.MetaUtilityClass;
import org.iplass.mtp.impl.sms.template.MetaSmsMailTemplate;
import org.iplass.mtp.impl.tenant.MetaTenant;
import org.iplass.mtp.impl.tenant.MetaTenantAuthInfo;
import org.iplass.mtp.impl.tenant.MetaTenantI18nInfo;
import org.iplass.mtp.impl.tenant.MetaTenantMailInfo;

@XmlSeeAlso({
	BaseRootMetaData.class
	,MetaInterceptorClass.class
	,MetaMetaCommand.class
	,MetaEntity.class
	,MetaMailTemplate.class
	,MetaSmsMailTemplate.class
	,MetaMessageCategory.class
	,MetaSelectValue.class
	,MetaUtilityClass.class
	,MetaTenant.class
	,MetaAsyncCommand.class
	,MetaPreference.class
	,MetaAuthenticationPolicy.class
	,MetaPushNotificationTemplate.class

	,MetaTenantAuthInfo.class
	,MetaTenantMailInfo.class
	,MetaTenantI18nInfo.class
})
class RootMetaDatas {
	//MetaDataへのXmlSeeAlsoを管理するためだけのクラス
}
