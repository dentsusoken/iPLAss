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

import java.util.Locale;

import javax.validation.MessageInterpolator;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;

/**
 * Tenant単位にMessageInterpolatorのインスタンスを保持し、呼び分けるMessageInterpolator。
 * Hibernateの場合、MessageInterpolatorでResourceBundleのメッセージのキャッシュを行っている。
 * ResourceBundleをMessage定義から取得する場合、テナント横断でキャッシュされてしまうとまずいので、Tenant単位でキャッシュする。
 * <br>
 * ※テナント単位であってもHibernateでキャッシュ（SoftReference）されてしまっているので、
 * メッセージ定義を変更しただけでは反映されない可能性あること注意。
 * TenantContextをリロードする必要あり。
 * 
 * @author K.Higuchi
 *
 */
public class TenantContextMessageInterpolator implements MessageInterpolator {
	
	private MessageInterpolatorFactory messageInterpolatorFactory;

	public MessageInterpolatorFactory getMessageInterpolatorFactory() {
		return messageInterpolatorFactory;
	}

	public void setMessageInterpolatorFactory(MessageInterpolatorFactory messageInterpolatorFactory) {
		this.messageInterpolatorFactory = messageInterpolatorFactory;
	}

	@Override
	public String interpolate(String messageTemplate, Context context) {
		TenantContext tc = ExecuteContext.getCurrentContext().getTenantContext();
		BeanValidationResource bvr = tc.getResource(BeanValidationResource.class);
		return bvr.getMessageInterpolator().interpolate(messageTemplate, context);
	}

	@Override
	public String interpolate(String messageTemplate, Context context, Locale locale) {
		TenantContext tc = ExecuteContext.getCurrentContext().getTenantContext();
		BeanValidationResource bvr = tc.getResource(BeanValidationResource.class);
		return bvr.getMessageInterpolator().interpolate(messageTemplate, context, locale);
	}
	
	public MessageInterpolator createMessageInterpolatorForTenant(int tenantId) {
		return messageInterpolatorFactory.newMessageInterpolator(tenantId);
	}

}
