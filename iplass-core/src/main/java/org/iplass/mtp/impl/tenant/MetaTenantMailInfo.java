/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.tenant;

import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.tenant.MetaTenant.MetaTenantHandler;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.tenant.TenantMailInfo;
import org.iplass.mtp.util.StringUtil;

/**
 * テナントメール情報のメタデータ
 *
 * @author 藤田 義弘
 *
 */
public class MetaTenantMailInfo extends MetaTenantConfig<TenantMailInfo> {

	/** Serial Version UID	 */
	private static final long serialVersionUID = -6973970284964999243L;

	/** メール送信をするか否かの設定 */
	private boolean sendMailEnable;

	/** From アドレス */
	private String mailFrom;
	/** From アドレス名 */
	private String mailFromName;
	/** Reply アドレス */
	private String mailReply;
	/** Reply アドレス名 */
	private String mailReplyName;

	/**
	 * Constractor
	 */
	public MetaTenantMailInfo() {
	}

	/**
	 * メール送信可否を取得します。
	 * @return sendMailEnable
	 */
	public boolean isSendMailEnable() {
		return sendMailEnable;
	}

	/**
	 * メール送信可否を設定します。
	 * @param sendMailEnable
	 */
	public void setSendMailEnable(boolean sendMailEnable) {
		this.sendMailEnable = sendMailEnable;
	}


	/**
	 * From アドレスを取得します。
	 *
	 * @return From アドレス
	 */
	public String getMailFrom() {
		return mailFrom;
	}

	/**
	 * From アドレスを設定します。
	 *
	 * @param mailFrom
	 *            From アドレス
	 */
	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}

	/**
	 * From アドレス名を取得します。
	 *
	 * @return From アドレス名
	 */
	public String getMailFromName() {
		return mailFromName;
	}

	/**
	 * From アドレス名を設定します。
	 *
	 * @param mailFromName
	 *            From アドレス名
	 */
	public void setMailFromName(String mailFromName) {
		this.mailFromName = mailFromName;
	}

	/**
	 * Reply アドレスを取得します。
	 *
	 * @return Reply アドレス
	 */
	public String getMailReply() {
		return mailReply;
	}

	/**
	 * Reply アドレスを設定します。
	 *
	 * @param mailReply
	 *            Reply アドレス
	 */
	public void setMailReply(String mailReply) {
		this.mailReply = mailReply;
	}

	/**
	 * Reply アドレス名を取得します。
	 *
	 * @return Reply アドレス名
	 */
	public String getMailReplyName() {
		return mailReplyName;
	}

	/**
	 * Reply アドレス名を設定します。
	 *
	 * @param mailReplyName
	 *            Reply アドレス名
	 */
	public void setMailReplyName(String mailReplyName) {
		this.mailReplyName = mailReplyName;
	}

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public void applyConfig(TenantMailInfo definition) {

		setSendMailEnable(definition.isSendMailEnable());
		setMailFrom(definition.getMailFrom());
		setMailFromName(definition.getMailFromName());
		setMailReply(definition.getMailReply());
		setMailReplyName(definition.getMailReplyName());
	}

	@Override
	public TenantMailInfo currentConfig() {

		TenantMailInfo definition = new TenantMailInfo();
		definition.setSendMailEnable(isSendMailEnable());
		definition.setMailFrom(getMailFrom());
		definition.setMailFromName(getMailFromName());
		definition.setMailReply(getMailReply());
		definition.setMailReplyName(getMailReplyName());

		return definition;
	}

	@Override
	public MetaTenantMailInfoRuntime createRuntime(MetaTenantHandler tenantRuntime) {
		return new MetaTenantMailInfoRuntime();
	}

	public class MetaTenantMailInfoRuntime extends MetaTenantConfigRuntime {

		private String defaultMailFrom;
		private String defaultMailFromName;

		public MetaTenantMailInfoRuntime() {
			TenantContextService tcService = ServiceRegistry.getRegistry().getService(TenantContextService.class);
			defaultMailFrom = tcService.getDefaultMailFrom();
			defaultMailFromName = tcService.getDefaultMailFromName();
		}

		@Override
		public MetaData getMetaData() {
			return MetaTenantMailInfo.this;
		}

		@Override
		public void applyMetaDataToTenant(Tenant tenant) {

			//デフォルトもしくは、メタデータの値の設定
			TenantMailInfo mail = tenant.getTenantConfig(TenantMailInfo.class);
			if (mail != null && StringUtil.isEmpty(mail.getMailFrom())) {
				mail.setMailFrom(defaultMailFrom);
				mail.setMailFromName(defaultMailFromName);
			}
		}

	}

}
