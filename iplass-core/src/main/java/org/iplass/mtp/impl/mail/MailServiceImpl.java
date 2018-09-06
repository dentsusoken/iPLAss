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

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.HeaderTokenizer;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.impl.definition.AbstractTypedMetaDataService;
import org.iplass.mtp.impl.definition.DefinitionMetaDataTypeMap;
import org.iplass.mtp.impl.mail.template.MetaMailTemplate;
import org.iplass.mtp.impl.mail.template.MetaMailTemplate.MailTemplateRuntime;
import org.iplass.mtp.mail.Mail;
import org.iplass.mtp.mail.MailException;
import org.iplass.mtp.mail.SendMailListener;
import org.iplass.mtp.mail.template.definition.MailTemplateDefinition;
import org.iplass.mtp.mail.template.definition.MailTemplateDefinitionManager;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.tenant.TenantMailInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 片野　博之
 *
 */
public class MailServiceImpl extends AbstractTypedMetaDataService<MetaMailTemplate, MailTemplateRuntime> implements MailService {

	public static final String MAIL_TEMPLATE_META_PATH = "/mail/template/";

	//default values
	public static final String ISO_2022_JP = "ISO-2022-JP";
	public static final String ENCODING_7BIT = "7bit";
	public static final String DEFAULT_TIMEOUT_MILLIS = "60000";
	
	//custom properties
	public static final String MAIL_SMTP_POPBEFORESMTP = "mail.smtp.popbeforesmtp";
	public static final String MAIL_POP3_AUTH_ID ="mail.pop3.auth.id";
	public static final String MAIL_POP3_AUTH_PASSWORD ="mail.pop3.auth.password";
	public static final String MAIL_SMTP_AUTH_ID ="mail.smtp.auth.id";
	public static final String MAIL_SMTP_AUTH_PASSWORD ="mail.smtp.auth.password";
	public static final String MAIL_CHARSET ="mail.charset";
	public static final String MAIL_ENCODING ="mail.encoding";
	
	private static Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

	public static class TypeMap extends DefinitionMetaDataTypeMap<MailTemplateDefinition, MetaMailTemplate> {
		public TypeMap() {
			super(getFixedPath(), MetaMailTemplate.class, MailTemplateDefinition.class);
		}
		@Override
		public TypedDefinitionManager<MailTemplateDefinition> typedDefinitionManager() {
			return ManagerLocator.getInstance().getManager(MailTemplateDefinitionManager.class);
		}
	}

	private boolean debug;
	private boolean mailSmtpPopbeforesmtp;
	private boolean mailSmtpAuth;
	private String mailPop3AuthId;
	private String mailPop3AuthPassword;
	private String mailSmtpAuthId;
	private String mailSmtpAuthPassword;
	private String mailCharset;
	private String mailEncoding;

	private Map<String, Object> sendProperties;

	private List<SendMailListener> listener;

	private long retryIntervalMillis;
	private int retryCount;
	

	/**
	 * コンストラクタ
	 */
	public MailServiceImpl() {
	}

	public Map<String, Object> getSendProperties() {
		return sendProperties;
	}

	public static String getFixedPath() {
		return MAIL_TEMPLATE_META_PATH;
	}

	public long getRetryIntervalMillis() {
		return retryIntervalMillis;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public List<SendMailListener> getListener() {
		return listener;
	}

	/**
	 *
	 * @see org.iplass.mtp.spi.Service#init(org.iplass.mtp.spi.Config)
	 */
	@Override
	public void init(Config config) {
		
		listener = config.getValues("listener", SendMailListener.class);
		retryIntervalMillis = config.getValue("retryIntervalMillis", Long.TYPE, 0L);
		retryCount = config.getValue("retryCount", Integer.TYPE, 0);
		debug = config.getValue("debug", Boolean.TYPE, false);
		
		sendProperties = new HashMap<>();
		for (String name: config.getNames()) {
			switch (name) {
			case MAIL_SMTP_POPBEFORESMTP:
				mailSmtpPopbeforesmtp = config.getValue(MAIL_SMTP_POPBEFORESMTP, Boolean.TYPE, false);
				break;
			case MAIL_POP3_AUTH_ID:
				mailPop3AuthId = config.getValue(MAIL_POP3_AUTH_ID);
				break;
			case MAIL_POP3_AUTH_PASSWORD:
				mailPop3AuthPassword = config.getValue(MAIL_POP3_AUTH_PASSWORD);
				break;
			case MAIL_SMTP_AUTH_ID:
				mailSmtpAuthId = config.getValue(MAIL_SMTP_AUTH_ID);
				break;
			case MAIL_SMTP_AUTH_PASSWORD:
				mailSmtpAuthPassword = config.getValue(MAIL_SMTP_AUTH_PASSWORD);
				break;
			case MAIL_CHARSET:
				mailCharset = config.getValue(MAIL_CHARSET);
				break;
			case MAIL_ENCODING:
				mailEncoding = config.getValue(MAIL_ENCODING);
				break;
			default:
				if (name.startsWith("mail.")) {
					sendProperties.put(name, config.getValue(name));
				}
				break;
			}
		}
		
		//set default value
		if (!sendProperties.containsKey("mail.smtp.connectiontimeout")) {
			sendProperties.put("mail.smtp.connectiontimeout", DEFAULT_TIMEOUT_MILLIS);
		}
		if (!sendProperties.containsKey("mail.smtp.timeout")) {
			sendProperties.put("mail.smtp.timeout", DEFAULT_TIMEOUT_MILLIS);
		}
		if (!sendProperties.containsKey("mail.smtps.connectiontimeout")) {
			sendProperties.put("mail.smtps.connectiontimeout", DEFAULT_TIMEOUT_MILLIS);
		}
		if (!sendProperties.containsKey("mail.smtps.timeout")) {
			sendProperties.put("mail.smtps.timeout", DEFAULT_TIMEOUT_MILLIS);
		}
		if (!sendProperties.containsKey("mail.pop3.connectiontimeout")) {
			sendProperties.put("mail.pop3.connectiontimeout", DEFAULT_TIMEOUT_MILLIS);
		}
		if (!sendProperties.containsKey("mail.pop3.timeout")) {
			sendProperties.put("mail.pop3.timeout", DEFAULT_TIMEOUT_MILLIS);
		}
		
		if ("TRUE".equalsIgnoreCase((String) sendProperties.get("mail.smtp.auth"))) {
			mailSmtpAuth = true;
		} else {
			mailSmtpAuth = false;
		}
		
	}

	/**
	 * @see org.iplass.mtp.spi.Service#destroy()
	 */
	@Override
	public void destroy() {

	}

	@Override
	public Mail createMail(Tenant tenant, String charset) {
		String buf = charset;
		if (buf == null) {
			buf = this.mailCharset;
		}
		return new Mail(buf);
	}

	@Override
	public void sendMail(Tenant tenant, Mail mail) {
		try {
			String charset = mail.getCharset();
			if(charset == null || charset.length() == 0) {
				charset = this.mailCharset;
			}
			boolean isDefault = false;
			TenantMailInfo tenantMailInfo = tenant.getTenantConfig(TenantMailInfo.class);
			if(mail.getFromAddress() == null) {
				isDefault = true;
				// デフォルトのFromを設定する
				InternetAddress address = new InternetAddress(tenantMailInfo.getMailFrom(), tenantMailInfo.getMailFromName(), charset);
				address.validate();
				mail.setFromAddress(address);
			}
			if(mail.getReplyToAddress() == null) {
				// デフォルトのReplyToを設定する
				if (isDefault && tenantMailInfo.getMailReply() != null && tenantMailInfo.getMailReply().length() != 0) {
					InternetAddress address = new InternetAddress(tenantMailInfo.getMailReply(), tenantMailInfo.getMailReplyName(), charset);
					address.validate();
					mail.setReplyToAddress(address);
				} else {
					mail.setReplyToAddress(mail.getFromAddress());
				}
			}

			//Listnerに通知
			if (!fireOnSendMail(mail)) {
				return;
			}

			//Tenantのメール送信設定が無効の場合は処理を終了
			if (!tenantMailInfo.isSendMailEnable()) {
				logger.debug("send mail flag of tenat configration is off, so don't send mail.");
				return;
			}

			Exception ex = null;
			boolean isSuccess = false;
			for (int i = 0; i <= retryCount; i++) {
				if (ex != null) {
					if (retryIntervalMillis > 0) {
						Thread.sleep(retryIntervalMillis);
					}
					if (logger.isDebugEnabled()) {
						logger.warn("Exception occured while send mail:" + ex + ", so retry...", ex);
					} else {
						logger.warn("Exception occured while send mail:" + ex + ", so retry...");
					}
				}

				try {
					MimeMessage mimeMessage = createSendMailMessage(mail, charset);
					Transport.send(mimeMessage);
					isSuccess = true;
				} catch (Exception e) {
					ex = e;
				}

				if (isSuccess) {
					break;
				}
			}

			if (isSuccess) {
				fireOnSuccess(mail);
			} else {
				throw ex;
			}

		} catch (Exception e) {
			handleException(mail, e);
		}
	}

	private MimeMessage createSendMailMessage(Mail mail, String charset) throws UnsupportedEncodingException, MessagingException {
		// メール接続情報の生成
		Properties props = createSendMailProperty(mail);

		if (logger.isDebugEnabled()) {
			logger.debug("send mail props:" + props);
		}

		Session session;
		// PopBeforeSmtpかのチェック
		if (mailSmtpPopbeforesmtp) {
			// 必要なのでPop認証を実行する
			session = execPopBeforeSmtp(mail, props);
		} else {
			session = createSendMailSession(mail, props);
		}
		// デバッグを行います。標準出力にトレースが出ます。
		if (debug) {
			session.setDebug(true);
		}

		// メッセージ内容の設定。
		final MimeMessage message = new MimeMessage(session);
		message.setFrom(mail.getFromAddress());
		message.setReplyTo(new Address[]{mail.getReplyToAddress()});
		setRecipients(mail, message);
		// 件名の設定
		message.setSubject(mail.getSubject(), charset);

		// 本文の設定 (添付ファイルがある場合とない場合で処理が異なる。
		setMessage(mail, message, charset);

		// その他の付加情報。
		// message.addHeader("X-Mailer", "blancoMail 0.1");
		Date d = mail.getDate();
		if(d == null) {
			d = new Date();
		}
		message.setSentDate(d);
		return message;
	}




	/**
	 * メール送信アドレスを設定する。
	 *
	 * @param mail
	 *            送信情報
	 * @param message
	 *            送信情報を設定するMessage
	 * @throws MessagingException
	 *             メッセージにエラーがある場合
	 */

	protected final void setRecipients(final Mail mail, final MimeMessage message) throws MessagingException {
		boolean validate = false;

		List<InternetAddress> bufTo = mail.getRecipientTo();
		if (bufTo != null && bufTo.size() > 0) {
			validate = true;
			// TOを追加する
			message.addRecipients(Message.RecipientType.TO, bufTo.toArray(new Address[bufTo.size()]));
		}
		// CCの設定
		List<InternetAddress> bufCc = mail.getRecipientCc();
		if (bufCc != null && bufCc.size() > 0) {
			validate = true;
			// CCを追加する
			message.addRecipients(Message.RecipientType.CC, bufCc.toArray(new Address[bufCc.size()]));
		}
		// BCCの設定
		List<InternetAddress> bufBcc = mail.getRecipientBcc();
		if (bufBcc != null && bufBcc.size() > 0) {
			validate = true;
			// BCCを追加する
			message.addRecipients(Message.RecipientType.BCC, bufBcc.toArray(new Address[bufBcc.size()]));
		}

		if (!validate) {
			throw new MailException("no send to (or cc or bcc) address specified.");
		}
	}

	private String handlePlainText(String text, String charset) {
		if (ISO_2022_JP.equalsIgnoreCase(charset)) {
			//ISO-2022-JPの場合、プレーンテキストの最後が文字化けることがある。
			if (!text.endsWith("\r\n")) {
				return text + "\r\n";
			}
		}
		return text;
	}

	protected final void setMessage(Mail mail, MimeMessage message, String charset) throws MessagingException {

		boolean isAlt =
			mail.getMessage() != null && mail.getMessage().length() != 0 &&
			mail.getHtmlMessage() != null && mail.getHtmlMessage().getContent().length() != 0;
		boolean hasAtt = mail.getAttachments() != null;

		if (hasAtt) {
			Multipart multipartMixed = new MimeMultipart("mixed");
			//multipart/alternativeでない場合
			if (!isAlt) {
				if (mail.getMessage() != null && mail.getMessage().length() != 0) {
					//プレーンテキスト
					BodyPart plainMessageBodyPart = new MimeBodyPart();
					plainMessageBodyPart.setContent(handlePlainText(mail.getMessage(), charset), "text/plain; charset=" + charset);
					multipartMixed.addBodyPart(plainMessageBodyPart);
					// Content-Transfer-Encoding設定
					plainMessageBodyPart.setHeader("Content-Transfer-Encoding", mailEncoding);
				} else {
					//HTMLメッセージ
					BodyPart htmlMessageBodyPart = new MimeBodyPart();
					String htmlCharset = mail.getHtmlMessage().getCharset();
					if (htmlCharset == null) {
						htmlCharset = charset;
					}
					htmlMessageBodyPart.setContent(mail.getHtmlMessage().getContent(), "text/html; charset=" + htmlCharset);
					multipartMixed.addBodyPart(htmlMessageBodyPart);
				}
			} else {
				BodyPart altBodyPart = new MimeBodyPart();
				Multipart multipartAlt = new MimeMultipart("alternative");
				altBodyPart.setContent(multipartAlt);

				//プレーンテキスト
				BodyPart plainMessageBodyPart = new MimeBodyPart();
				plainMessageBodyPart.setContent(handlePlainText(mail.getMessage(), charset), "text/plain; charset=" + charset);
				multipartAlt.addBodyPart(plainMessageBodyPart);
				// Content-Transfer-Encoding設定
				plainMessageBodyPart.setHeader("Content-Transfer-Encoding", mailEncoding);

				//HTMLメッセージ
				BodyPart htmlMessageBodyPart = new MimeBodyPart();
				String htmlCharset = mail.getHtmlMessage().getCharset();
				if (htmlCharset == null) {
					htmlCharset = charset;
				}
				htmlMessageBodyPart.setContent(mail.getHtmlMessage().getContent(), "text/html; charset=" + htmlCharset);
				multipartAlt.addBodyPart(htmlMessageBodyPart);

				multipartMixed.addBodyPart(altBodyPart);
			}
			for (DataHandler at: mail.getAttachments()) {
				MimeBodyPart attBodyPart = new MimeBodyPart();
				attBodyPart.setDataHandler(at);
				try {
					String fileName = at.getName();
					attBodyPart.setFileName(fileName);
					
					//for old outlook
					String contentType = at.getContentType();
					if (contentType != null) {
						String fileNameEnc = MimeUtility.quote(MimeUtility.encodeText(fileName), HeaderTokenizer.MIME);
						
						StringBuilder sb = new StringBuilder();
						sb.append(contentType);
						sb.append(";\r\n");
						sb.append(" name=");
						sb.append(MimeUtility.fold(7, fileNameEnc));
						attBodyPart.setHeader("Content-Type", sb.toString());
					}
					
				} catch (UnsupportedEncodingException e) {
					logger.warn("file name cant encoded... cause " + e.getMessage(), e);
				}
				attBodyPart.setDisposition("attachment");
				multipartMixed.addBodyPart(attBodyPart);
			}
			message.setContent(multipartMixed);
		} else {
			//multipart/alternativeでない場合
			if (!isAlt) {
				if (mail.getMessage() != null && mail.getMessage().length() != 0) {
					//プレーンテキスト
					message.setText(handlePlainText(mail.getMessage(), charset), charset);
					// Content-Transfer-Encoding設定
					message.setHeader("Content-Transfer-Encoding", mailEncoding);
				} else {
					//HTMLメッセージ
					String htmlCharset = mail.getHtmlMessage().getCharset();
					if (htmlCharset == null) {
						htmlCharset = charset;
					}
					message.setContent(mail.getHtmlMessage().getContent(), "text/html; charset=\"" + htmlCharset + "\"");
				}
			} else {
				Multipart multipart = new MimeMultipart("alternative");

				//プレーンテキスト
				BodyPart plainMessageBodyPart = new MimeBodyPart();
				plainMessageBodyPart.setContent(handlePlainText(mail.getMessage(), charset), "text/plain; charset=\"" + charset + "\"");
				multipart.addBodyPart(plainMessageBodyPart);
				plainMessageBodyPart.setHeader("Content-Transfer-Encoding", mailEncoding);

				//HTMLメッセージ
				BodyPart htmlMessageBodyPart = new MimeBodyPart();
				String htmlCharset = mail.getHtmlMessage().getCharset();
				if (htmlCharset == null) {
					htmlCharset = charset;
				}
				htmlMessageBodyPart.setContent(mail.getHtmlMessage().getContent(), "text/html; charset=\"" + htmlCharset + "\"");
				multipart.addBodyPart(htmlMessageBodyPart);

				message.setContent(multipart);
			}
		}
	}

	/**
	 * Pop Before SmtpのためにPop認証を実行します。
	 *
	 * @param mail
	 *            メール送信情報
	 */
	private Session execPopBeforeSmtp(Mail mail, Properties props) {
		final Session session = createPopSession(mail, props);
		// デバッグを行います。標準出力にトレースが出ます。
		if (debug) {
			session.setDebug(true);
		}
		Store store = null;
		try {
			store = session.getStore("pop3");
			store.connect();
		} catch (NoSuchProviderException e) {
			throw new MailException("Pop Before SMTP user auth fail.", e);
		} catch (MessagingException e) {
			throw new MailException("Pop Before SMTP user auth fail.", e);
		} finally {
			if (store != null) {
				try {
					store.close();
				} catch (MessagingException e) {
					logger.error("can't close pop3 store:" + e, e);
				}
			}
		}
		return session;
	}

	private Session createPopSession(Mail mail, Properties props) {
		final Session session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(mailPop3AuthId, mailPop3AuthPassword);
			}
		});
		return session;
	}

	/**
	 * 送信メールのSessionを作成する。
	 *
	 * @param MailServerConfig
	 *            メールサーバコンフィグ
	 * @param mail
	 *            メール情報
	 * @param props
	 *            接続Properties
	 * @return 作成したSession情報
	 */
	private Session createSendMailSession(Mail mail, Properties props) {
		Authenticator authenticator = null;
		if (mailSmtpAuth) {
			authenticator = new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(
							mailSmtpAuthId,
							mailSmtpAuthPassword);
				}
			};
		}
		final Session session;
		if (authenticator == null) {
			session = Session.getInstance(props);
		} else {
			session = Session.getInstance(props, authenticator);
		}
		return session;
	}

	/**
	 * 送信メールプロパティーを作成する。
	 *
	 * @param mail
	 *            送信メール情報
	 * @return 生成したPrpperti情報
	 */
	private Properties createSendMailProperty(Mail mail) {
		Properties props = new Properties();
		props.putAll(sendProperties);
		
		//Message-IDでローカルサーバ名/ローカルユーザが出ないように
		props.setProperty("mail.from", mail.getFromAddress().getAddress());

		//mail.smtp.from
		if (mail.getReturnPath() != null) {
			props.setProperty("mail.smtp.from", mail.getReturnPath());
		}
		
		return props;
	}

	/**
	 * 登録されたSendMailListnerに通知します。
	 *
	 * @param mail メール
	 * @param mimeMessage メールから生成したメッセージ
	 */
	protected boolean fireOnSendMail(final Mail mail) {
		if (listener != null) {
			for (SendMailListener l: listener) {
				if (!l.beforeSend(mail)) {
					logger.info("send mail canceled by Listener:" + l);
					return false;
				}
			}
		}
		return true;
	}

	protected void fireOnSuccess(Mail mail) {
		if (listener != null) {
			for (SendMailListener l: listener) {
				l.onSuccess(mail);
			}
		}
	}

	protected void handleException(Mail mail, Exception e) {
		if (listener != null) {
			for (SendMailListener l: listener) {
				if (!l.onFailure(mail, e)) {
					return;
				}
			}
		}
		if (e instanceof RuntimeException) {
			throw (RuntimeException) e;
		} else {
			throw new MailException(e.getMessage(), e);
		}
	}

	@Override
	public Class<MetaMailTemplate> getMetaDataType() {
		return MetaMailTemplate.class;
	}

	@Override
	public Class<MailTemplateRuntime> getRuntimeType() {
		return MailTemplateRuntime.class;
	}
}
