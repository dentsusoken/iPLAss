/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.mail;

import java.util.Map;

import org.iplass.mtp.SystemException;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.mail.template.MetaMailTemplate.MailTemplateRuntime;
import org.iplass.mtp.mail.Mail;
import org.iplass.mtp.mail.MailManager;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.Tenant;

/**
 * @author 片野　博之
 *
 */
public class MailManagerImpl implements MailManager {

	private MailService mailService = ServiceRegistry.getRegistry().getService(MailService.class);


	/**
	 *
	 */
	public MailManagerImpl() {
	}

	/**
	 * @see org.iplass.mtp.mail.MailManager#createMail(java.lang.String)
	 */
	@Override
	public Mail createMail() {
		return mailService.createMail(ExecuteContext.getCurrentContext().getCurrentTenant(), null);
	}

	/**
	 * @see org.iplass.mtp.mail.MailManager#sendMail(org.iplass.mtp.mail.Mail)
	 */
	@Override
	public void sendMail(Mail mail) {
		Tenant tenant = ExecuteContext.getCurrentContext().getCurrentTenant();
		mailService.sendMail(tenant, mail);

	}

	@Override
	public Mail createMail(String tmplDefName, Map<String, Object> bindings) {
		MailTemplateRuntime tmpl = mailService.getRuntimeByName(tmplDefName);
		if (tmpl == null) {
			throw new SystemException("mailTemplate:" + tmplDefName + " not found");
		}
		return tmpl.createMail(bindings);
	}

}
