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
import java.util.List;
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
	public static final String ISO_2022_JP = "ISO-2022-JP";

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

	private boolean debug = false;

	private MailServerConfig config;

	private List<SendMailListener> listener;

	private long retryIntervalMillis;
	private int retryCount;

	/**
	 * コンストラクタ
	 */
	public MailServiceImpl() {
	}

	public static String getFixedPath() {
		return MAIL_TEMPLATE_META_PATH;
	}

	private boolean convBoolean(Config config, String key) {
		String value = config.getValue(key);
		return Boolean.valueOf(value);
	}

	private int getInt(Config config, String key, int defaultValue) {
		String value = config.getValue(key);
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
		}
		return defaultValue;
	}

	private int chkInt(Config config, String key) {
		String value = chkNan(config, key);
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			throw new IllegalStateException("Mail configration invalid.Key=" + key + ":value=" + value, e);
		}
	}

	private String chkNan(Config config, String key) {
		String ret = config.getValue(key);
		if(ret == null || ret.length() == 0) {
			throw new IllegalStateException("Mail configration invalid.Key=" + key + ":value=" + ret);
		}
		return ret;
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
	@SuppressWarnings("unchecked")
	@Override
	public void init(Config config) {
		String isDebug = config.getValue("debug");
		if (isDebug != null && isDebug.equalsIgnoreCase("true")) {
			debug = true;
		}

		//FIXME javamailのプロパティの設定の汎用化。他にも設定可能なプロパティがたくさんある。

		MailServerConfig server = new MailServerConfig();
		// メールサーバアドレス
		server.setMailSmtpHost(chkNan(config, "mail.smtp.host"));
		// メールサーバsmtpポート 通常設定: 25 / サブミッションポート:587 / SSL:465
		server.setMailSmtpPort(chkInt(config, "mail.smtp.port"));
		// SMTPConnectionタイムアウト設定(ms)
		server.setMailSmtpConnectiontimeout(getInt(config, "mail.smtp.connectiontimeout", 60000));
		// SMTPタイムアウト設定(ms)
		server.setMailSmtpTimeout(getInt(config, "mail.smtp.timeout", 60000));
		//mail.smtp.from
		if (config.getValue("mail.smtp.from") != null) {
			server.setMailSmtpFrom(config.getValue("mail.smtp.from"));
		}

		// SMTP 認証方式
		// SMTP認証を利用するか否か
		server.setMailSmtpAuth(convBoolean(config, "mail.smtp.auth"));
		if(server.isMailSmtpAuth()) {
			// SMTP認証ユーザID
			server.setMailSmtpAuthId(chkNan(config, "mail.smtp.auth.id"));
			// SMTP認証パスワード
			server.setMailSmtpAuthPassword(chkNan(config, "mail.smtp.auth.password"));
			// SMTP認証方式 (LOGIN PLAIN DIGEST-MD5 NTLM(対応していない)
			server.setMailSmtpAuthMechanisms(config.getValue("mail.smtp.auth.mechanisms"));
		}

		// SMTP SSL
		if (config.getValue("mail.smtp.ssl") != null) {
			server.setMailSmtpSsl(convBoolean(config, "mail.smtp.ssl"));
		} else {
			server.setMailSmtpSsl(false);
		}

		// PopBeforeSmtpの設定 mailSmtpAuthがfalseの場合に有効
		server.setMailSmtpPopbeforesmtp(convBoolean(config, "mail.smtp.popbeforesmtp"));
		// POP設定
		if(!server.isMailSmtpAuth() && server.isMailSmtpPopbeforesmtp()) {
			// SMTP認証でなく、Pop Before SMTPの場合にPOP3設定を取得する。
			// popサーバ
			server.setMailPop3Host(chkNan(config, "mail.pop3.host"));
			// pop3ポート 通常：110 / ssl 995
			server.setMailPop3Port(chkInt(config, "mail.pop3.port"));
			// pop認証設定 */
			server.setMailPop3AuthId(chkNan(config, "mail.pop3.auth.id"));
			server.setMailPop3AuthPassword(chkNan(config, "mail.pop3.auth.password"));
			// APOP を利用するか否か true:利用する/true文字以外:利用しない */
			server.setMailPop3ApopEnable(convBoolean(config, "mail.pop3.apop.enable"));
			// Pop3接続タイムアウト設定(ms)
			server.setMailPop3Connectiontimeout(getInt(config, "mail.pop3.connectiontimeout", 60000));
			// Pop3タイムアウト設定
			server.setMailPop3Timeout(getInt(config, "mail.pop3.timeout", 60000));

			// Pop3 SSL
			if (config.getValue("mail.pop3.ssl") != null) {
				server.setMailSmtpSsl(convBoolean(config, "mail.pop3.ssl"));
			} else {
				server.setMailSmtpSsl(false);
			}
		}

		// MailHost設定
		server.setMailHost(chkNan(config, "mail.host"));
		// Charset
		server.setMailCharset(chkNan(config, "mail.charset"));
		// Content-Transfer-Encoding
		// デフォルト "7bit"
		// 仮対策: 参照元 http://www.igapyon.jp/igapyon/diary/2007/ig070904.html
		// これは、一部のケータイメールが quoted-printable を処理できないことへの対策となります。
		String contentTransferEncoding = config.getValue("mail.encoding");
		if(contentTransferEncoding == null || contentTransferEncoding.length() == 0) {
			contentTransferEncoding = "7bit";
		}
		server.setContentTransferEncoding(contentTransferEncoding);

		if (config.getBeans("listener") != null) {
			this.listener = (List<SendMailListener>) config.getBeans("listener");
		}

		this.config = server;

		if (config.getValue("retryIntervalMillis") != null) {
			retryIntervalMillis = Long.parseLong(config.getValue("retryIntervalMillis"));
		}
		if (config.getValue("retryCount") != null) {
			retryCount = Integer.parseInt(config.getValue("retryCount"));
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
		if(buf == null) {
			buf = this.config.getMailCharset();
		}
		return new Mail(buf);
	}

	@Override
	public void sendMail(Tenant tenant, Mail mail) {
		try {
			String charset = mail.getCharset();
			if(charset == null || charset.length() == 0) {
				charset = this.config.getMailCharset();
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
		if (isPopBeforeSmtp(mail)) {
			// 必要なのでPop認証を実行する
			session = execPopBeforeSmtp(mail, props);
		} else {
			session = createSendMailSession(this.config, mail, props);
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
					plainMessageBodyPart.setContent(handlePlainText(mail.getMessage(), charset), "text/plain; charset=\"" + charset + "\"");
					multipartMixed.addBodyPart(plainMessageBodyPart);
					// Content-Transfer-Encoding設定
					plainMessageBodyPart.setHeader("Content-Transfer-Encoding", config.getContentTransferEncoding());
				} else {
					//HTMLメッセージ
					BodyPart htmlMessageBodyPart = new MimeBodyPart();
					String htmlCharset = mail.getHtmlMessage().getCharset();
					if (htmlCharset == null) {
						htmlCharset = charset;
					}
					htmlMessageBodyPart.setContent(mail.getHtmlMessage().getContent(), "text/html; charset=\"" + htmlCharset + "\"");
					multipartMixed.addBodyPart(htmlMessageBodyPart);
				}
			} else {
				BodyPart altBodyPart = new MimeBodyPart();
				Multipart multipartAlt = new MimeMultipart("alternative");
				altBodyPart.setContent(multipartAlt);

				//プレーンテキスト
				BodyPart plainMessageBodyPart = new MimeBodyPart();
				plainMessageBodyPart.setContent(handlePlainText(mail.getMessage(), charset), "text/plain; charset=\"" + charset + "\"");
				multipartAlt.addBodyPart(plainMessageBodyPart);
				// Content-Transfer-Encoding設定
				plainMessageBodyPart.setHeader("Content-Transfer-Encoding", config.getContentTransferEncoding());

				//HTMLメッセージ
				BodyPart htmlMessageBodyPart = new MimeBodyPart();
				String htmlCharset = mail.getHtmlMessage().getCharset();
				if (htmlCharset == null) {
					htmlCharset = charset;
				}
				htmlMessageBodyPart.setContent(mail.getHtmlMessage().getContent(), "text/html; charset=\"" + htmlCharset + "\"");
				multipartAlt.addBodyPart(htmlMessageBodyPart);

				multipartMixed.addBodyPart(altBodyPart);
			}
			for (DataHandler at: mail.getAttachments()) {
				MimeBodyPart attBodyPart = new MimeBodyPart();
				attBodyPart.setDataHandler(at);
				try {
					attBodyPart.setFileName(MimeUtility.encodeWord(at.getName()));
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
					message.setHeader("Content-Transfer-Encoding", config.getContentTransferEncoding());
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
				plainMessageBodyPart.setHeader("Content-Transfer-Encoding", config.getContentTransferEncoding());

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
		final Session session = createPopSession(this.config, mail, props);
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
					e.printStackTrace();
				}
			}
		}
		return session;
	}

	private Session createPopSession(final MailServerConfig mailSeverConfig, Mail mail, Properties props) {
//		Properties props = new Properties();
		// 基本情報。
		props.setProperty("mail.pop3.host", mailSeverConfig.getMailPop3Host());
		props.setProperty("mail.pop3.port", String.valueOf(mailSeverConfig.getMailPop3Port()));
		// Apop
		props.setProperty("mail.pop3.apop.enable", String.valueOf(mailSeverConfig.isMailPop3ApopEnable()));
		// タイムアウト設定
		props.setProperty("mail.pop3.connectiontimeout", String.valueOf(mailSeverConfig.getMailPop3Connectiontimeout()));
		props.setProperty("mail.pop3.timeout", String.valueOf(mailSeverConfig.getMailPop3Timeout()));
		//SSL設定
		if(mailSeverConfig.isMailPop3Ssl()) {
			props.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.setProperty("mail.pop3.socketFactory.fallback", "false");
			props.setProperty("mail.pop3.socketFactory.port", String.valueOf(mailSeverConfig.getMailPop3Port()));
		}

		final Session session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(mailSeverConfig.getMailPop3AuthId(),
												mailSeverConfig.getMailPop3AuthPassword());
			}
		});
		return session;
	}

	private boolean isPopBeforeSmtp(Mail mail) {
		if (this.config.isMailSmtpAuth()) {
			return false;
		}
		return this.config.isMailSmtpPopbeforesmtp();
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
	private Session createSendMailSession(final MailServerConfig mailServerConfig, Mail mail, Properties props) {
		Authenticator authenticator = null;
		if (mailServerConfig.isMailSmtpAuth()) {
			authenticator = new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(
							mailServerConfig.getMailSmtpAuthId(),
							mailServerConfig.getMailSmtpAuthPassword());
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
		// 基本情報。
		//Message-IDでローカルサーバ名/ローカルユーザが出ないように
		props.setProperty("mail.from", mail.getFromAddress().getAddress());

		if (this.config.getMailHost() != null && this.config.getMailHost().length() != 0) {
			props.setProperty("mail.host", this.config.getMailHost());
		}
		props.setProperty("mail.smtp.host", this.config.getMailSmtpHost());
		props.setProperty("mail.smtp.port", String.valueOf(this.config.getMailSmtpPort()));
		// タイムアウト設定
		props.setProperty("mail.smtp.connectiontimeout", String.valueOf(this.config.getMailSmtpConnectiontimeout()));
		props.setProperty("mail.smtp.timeout", String.valueOf(this.config.getMailSmtpTimeout()));
		//mail.smtp.from
		if (mail.getReturnPath() != null) {
			props.setProperty("mail.smtp.from", mail.getReturnPath());
		} else if (this.config.getMailSmtpFrom() != null) {
			props.setProperty("mail.smtp.from", this.config.getMailSmtpFrom());
		}
		// SMTP認証設定
		if(this.config.isMailSmtpAuth()) {
			props.setProperty("mail.smtp.auth", "true");
			if(this.config.getMailSmtpAuthMechanisms() != null) {
				props.setProperty("mail.smtp.auth.mechanisms", this.config.getMailSmtpAuthMechanisms());
			}
		}
		//SSL設定
		if(this.config.isMailSmtpSsl()) {
			props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.setProperty("mail.smtp.socketFactory.fallback", "false");
			props.setProperty("mail.smtp.socketFactory.port", String.valueOf(this.config.getMailSmtpPort()));
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
