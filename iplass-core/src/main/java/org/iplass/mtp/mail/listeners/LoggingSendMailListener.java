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

package org.iplass.mtp.mail.listeners;

import java.util.List;

import jakarta.activation.DataHandler;
import jakarta.mail.internet.InternetAddress;

import org.iplass.mtp.mail.InlineContent;
import org.iplass.mtp.mail.Mail;
import org.iplass.mtp.mail.SendMailListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 送信メールをLogに出力します。
 *
 * ※標準のLog設定では改行をブランクに置き換えているため、改行されずに出力されます。
 *
 * @author lis70i
 */
public class LoggingSendMailListener implements SendMailListener {

	private static final Logger log = LoggerFactory.getLogger(LoggingSendMailListener.class);

	@Override
	public boolean beforeSend(Mail mail) {
		debugMail(mail);
		return true;
	}

	/**
	 * Logを出力します。
	 *
	 * @param mail   メール
	 */
	private void debugMail(final Mail mail) {
		if (mail != null) {
			StringBuilder builder = new StringBuilder();

			builder.append("\r\n");
			builder.append(debugRecipientAddress("From", mail.getFromAddress()) + "\n");
			builder.append(debugRecipientAddress("ReplyTo", mail.getReplyToAddress()) + "\n");
			builder.append(debugRecipientAddress("To", mail.getRecipientTo()) + "\n");
			builder.append(debugRecipientAddress("Cc", mail.getRecipientCc()) + "\n");
			builder.append(debugRecipientAddress("Bcc", mail.getRecipientBcc()) + "\n");

			builder.append("Subject:" + mail.getSubject() + "\n");

			String fileName = "";
			if (mail.getAttachments() != null) {
				for (DataHandler handler : mail.getAttachments()) {
					fileName = fileName + handler.getName() + " ";
				}
			}
			builder.append("FileName:" + fileName + "\n");

			builder.append("PlainMessage:" + mail.getMessage() + "\n");
			if (mail.getHtmlMessage() != null) {
				builder.append("HtmlMessage:" + mail.getHtmlMessage().getContent() + "\n");
				if (mail.getHtmlMessage().getInlineContents() != null) {
					builder.append("InlineContents:");
					for (InlineContent ic: mail.getHtmlMessage().getInlineContents()) {
						if (ic != null) {
							builder.append(ic.getContentId()).append(", ");
						}
					}
					builder.append("\n");
				}
			}

			log.debug(builder.toString());
		}
	}

	private String debugRecipientAddress(String title, InternetAddress address) {
		return (title + ":" + getAddressString(address));
	}

	private String debugRecipientAddress(String title, List<InternetAddress> addresses) {
		if (addresses != null) {
			StringBuilder builder = new StringBuilder();
			for (InternetAddress address : addresses) {
				builder.append(getAddressString(address) + ";");
			}
			return (title + ":" + "(" + addresses.size() + ")" + builder.toString());
		} else {
			return (title + ":" + "(0)");
		}
	}

	private String getAddressString(InternetAddress address) {
		if (address == null) {
			return "";
		}
		if (address.getPersonal() != null && !address.getPersonal().isEmpty()) {
			return address.getPersonal() + "[" + address.getAddress() + "]";
		} else {
			return address.getAddress();
		}
	}

}
