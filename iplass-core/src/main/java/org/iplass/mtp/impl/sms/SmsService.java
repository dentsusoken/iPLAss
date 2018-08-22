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

import org.iplass.mtp.impl.definition.TypedMetaDataService;
import org.iplass.mtp.impl.sms.template.MetaSmsMailTemplate;
import org.iplass.mtp.impl.sms.template.MetaSmsMailTemplate.SmsMailTemplateRuntime;
import org.iplass.mtp.sms.SmsMail;
import org.iplass.mtp.tenant.Tenant;

/***
 * SMS利用のインターフェース.
 * <p>実装はTwilioなど別モジュールのjarで提供されます.</p>
 * @author T.Nishida
 *
 */
public interface SmsService extends TypedMetaDataService<MetaSmsMailTemplate, SmsMailTemplateRuntime> {

	/**
	 * テナント情報に設定されているメール情報を利用してSMSを送信する。
	 * @param tenant テナント情報
	 * @param mail 送信するメール情報
	 */
	void sendSmsMail(Tenant tenant, SmsMail sms);
	SmsMail createMail(Tenant tenant);
}
