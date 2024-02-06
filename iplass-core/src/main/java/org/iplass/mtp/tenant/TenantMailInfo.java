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

package org.iplass.mtp.tenant;

/**
 * テナントのメール情報
 *
 * @author 藤田 義弘
 *
 */
public class TenantMailInfo extends TenantConfig {

	/** Serial Version UID	 */
	private static final long serialVersionUID = 5185141304991556527L;

	/** メール送信をするか否かの設定 */
	private boolean sendMailEnable;

	/** From アドレス */
	private String mailFrom;
	/** Reply アドレス */
	private String mailReply;
	/** From アドレス名 */
	private String mailFromName;
	/** Reply アドレス名 */
	private String mailReplyName;

	/**
	 * Constractor
	 */
	public TenantMailInfo() {
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((mailFrom == null) ? 0 : mailFrom.hashCode());
		result = prime * result
				+ ((mailFromName == null) ? 0 : mailFromName.hashCode());
		result = prime * result
				+ ((mailReply == null) ? 0 : mailReply.hashCode());
		result = prime * result
				+ ((mailReplyName == null) ? 0 : mailReplyName.hashCode());
		result = prime * result + (sendMailEnable ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TenantMailInfo other = (TenantMailInfo) obj;
		if (mailFrom == null) {
			if (other.mailFrom != null)
				return false;
		} else if (!mailFrom.equals(other.mailFrom))
			return false;
		if (mailFromName == null) {
			if (other.mailFromName != null)
				return false;
		} else if (!mailFromName.equals(other.mailFromName))
			return false;
		if (mailReply == null) {
			if (other.mailReply != null)
				return false;
		} else if (!mailReply.equals(other.mailReply))
			return false;
		if (mailReplyName == null) {
			if (other.mailReplyName != null)
				return false;
		} else if (!mailReplyName.equals(other.mailReplyName))
			return false;
		if (sendMailEnable != other.sendMailEnable)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TenantMailInfo [sendMailEnable=" + sendMailEnable
				+ ", mailFrom=" + mailFrom + ", mailReply=" + mailReply
				+ ", mailFromName=" + mailFromName + ", mailReplyName="
				+ mailReplyName + "]";
	}

}
