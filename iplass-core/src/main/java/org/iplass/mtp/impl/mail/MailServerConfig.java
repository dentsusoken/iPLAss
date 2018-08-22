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

import java.io.Serializable;

import org.iplass.mtp.impl.util.ObjectUtil;

/**
 * メールサーバの接続情報
 *
 * @author 片野　博之
 *
 */
public class MailServerConfig implements Serializable {

	private static final long serialVersionUID = 3590095189653478145L;

	// SMTP設定
	/** メールサーバアドレス */
	private String mailSmtpHost;
	/** メールサーバsmtpポート 通常設定: 25 / サブミッションポート:587 / SSL:465 */
	private int mailSmtpPort;
	/** SMTPConnectionタイムアウト設定(ms) */
	private int mailSmtpConnectiontimeout;
	/** SMTPタイムアウト設定(ms) */
	private int mailSmtpTimeout;
	/** mail.smtp.from */
	private String mailSmtpFrom;

	// SMTP 認証方式
	/** PopBeforeSmtpの設定 mailSmtpAuthがfalseの場合に有効 */
	private boolean mailSmtpPopbeforesmtp;
	/** SMTP認証を利用するか否か */
	private boolean mailSmtpAuth;
	/** SMTP認証ユーザID */
	private String mailSmtpAuthId;
	/** SMTP認証パスワード */
	private String mailSmtpAuthPassword;
	/** SMTP認証方式 (LOGIN PLAIN DIGEST-MD5 NTLM(対応していない) */
	private String mailSmtpAuthMechanisms;
	/** SMTP SSLを利用するか否か(ポートの変更が必要) */
	private boolean mailSmtpSsl;

	// POP設定
	/** Pop3サーバアドレス */
	private String mailPop3Host;
	/** Pop3ポート 通常：110 / ssl 995 */
	private int mailPop3Port;
	/** Pop3認証ユーザID */
	private String mailPop3AuthId;
	/** Pop3認証パスワード */
	private String mailPop3AuthPassword;
	/** APOP を利用するか否か true:利用する/true文字以外:利用しない */
	private boolean mailPop3ApopEnable;
	/** Pop3接続タイムアウト設定(ms) */
	private int mailPop3Connectiontimeout;
	/** Pop3タイムアウト設定 */
	private int mailPop3Timeout;
	/** Pop3SSLを利用するか否か(ポートの変更が必要) */
	private boolean mailPop3Ssl;

	/**
	 * mailSmtp.hostプロパティはmail.hostプロパティに優先して認識されるため、2つの値が同一ならば、
	 * mailSmtp.hostプロパティを設定するだけでもメール送信は可能です。 しかし、mail.hostプロパティは内部的にMessage-ID
	 * ヘッダを生成するのに利用されます。
	 * mail.hostプロパティを明示的に指定していない場合、Message-IDヘッダが正しく生成できない可能性があります。 元情報
	 * http://wwwAtmarkit.co.jp/fjava/javatips/123java022.html
	 */
	/** MailHost設定 */
	private String mailHost;
	/** Charset */
	private String mailCharset;
	/** Content-Transfer-Encoding */
	private String contentTransferEncoding;

	/**
	 * コンストラクタ
	 */
	public MailServerConfig() {
	}

	/**
	 * @see org.iplass.mtp.impl.metadata.MetaData#copy()
	 */
	public MailServerConfig copy() {
		return ObjectUtil.deepCopy(this);
	}

	/**
	 * SMTPメールサーバアドレスを取得します。
	 *
	 * @return SMTPメールサーバアドレス
	 */
	public String getMailSmtpHost() {
		return mailSmtpHost;
	}

	/**
	 * SMTPメールサーバアドレスを設定します。
	 *
	 * @param mailSmtpHost
	 *            SMTPメールサーバアドレス
	 */
	public void setMailSmtpHost(String mailSmtpHost) {
		this.mailSmtpHost = mailSmtpHost;
	}

	/**
	 * SMTPポート 通常設定: 25 / サブミッションポート:587 / SSL:465を取得します。
	 *
	 * @return SMTPポート 通常設定: 25 / サブミッションポート:587 / SSL:465
	 */
	public int getMailSmtpPort() {
		return mailSmtpPort;
	}

	/**
	 * SMTPポート 通常設定: 25 / サブミッションポート:587 / SSL:465を設定します。
	 *
	 * @param mailSmtpPort
	 *            SMTPポート 通常設定: 25 / サブミッションポート:587 / SSL:465
	 */
	public void setMailSmtpPort(int mailSmtpPort) {
		this.mailSmtpPort = mailSmtpPort;
	}

	/**
	 * SMTPConnectionタイムアウト設定(ms)を取得します。
	 *
	 * @return SMTPConnectionタイムアウト設定(ms)
	 */
	public int getMailSmtpConnectiontimeout() {
		return mailSmtpConnectiontimeout;
	}

	/**
	 * SMTPConnectionタイムアウト設定(ms)を設定します。
	 *
	 * @param mailSmtpConnectiontimeout
	 *            SMTPConnectionタイムアウト設定(ms)
	 */
	public void setMailSmtpConnectiontimeout(int mailSmtpConnectiontimeout) {
		this.mailSmtpConnectiontimeout = mailSmtpConnectiontimeout;
	}

	/**
	 * SMTPタイムアウト設定(ms)を取得します。
	 *
	 * @return SMTPタイムアウト設定(ms)
	 */
	public int getMailSmtpTimeout() {
		return mailSmtpTimeout;
	}

	/**
	 * SMTPタイムアウト設定(ms)を設定します。
	 *
	 * @param mailSmtpTimeout
	 *            SMTPタイムアウト設定(ms)
	 */
	public void setMailSmtpTimeout(int mailSmtpTimeout) {
		this.mailSmtpTimeout = mailSmtpTimeout;
	}

	/**
	 * PopBeforeSmtpの設定 mailSmtpAuthがfalseの場合に有効を取得します。
	 *
	 * @return PopBeforeSmtpの設定 mailSmtpAuthがfalseの場合に有効
	 */
	public boolean isMailSmtpPopbeforesmtp() {
		return mailSmtpPopbeforesmtp;
	}

	/**
	 * PopBeforeSmtpの設定 mailSmtpAuthがfalseの場合に有効を設定します。
	 *
	 * @param mailSmtpPopbeforesmtp
	 *            PopBeforeSmtpの設定 mailSmtpAuthがfalseの場合に有効
	 */
	public void setMailSmtpPopbeforesmtp(boolean mailSmtpPopbeforesmtp) {
		this.mailSmtpPopbeforesmtp = mailSmtpPopbeforesmtp;
	}

	/**
	 * SMTP認証を利用するか否かを取得します。
	 *
	 * @return SMTP認証を利用するか否か
	 */
	public boolean isMailSmtpAuth() {
		return mailSmtpAuth;
	}

	/**
	 * SMTP認証を利用するか否かを設定します。
	 *
	 * @param mailSmtpAuth
	 *            SMTP認証を利用するか否か
	 */
	public void setMailSmtpAuth(boolean mailSmtpAuth) {
		this.mailSmtpAuth = mailSmtpAuth;
	}

	/**
	 * SMTP認証ユーザIDを取得します。
	 *
	 * @return SMTP認証ユーザID
	 */
	public String getMailSmtpAuthId() {
		return mailSmtpAuthId;
	}

	/**
	 * SMTP認証ユーザIDを設定します。
	 *
	 * @param mailSmtpAuthId
	 *            SMTP認証ユーザID
	 */
	public void setMailSmtpAuthId(String mailSmtpAuthId) {
		this.mailSmtpAuthId = mailSmtpAuthId;
	}

	/**
	 * SMTP認証パスワードを取得します。
	 *
	 * @return SMTP認証パスワード
	 */
	public String getMailSmtpAuthPassword() {
		return mailSmtpAuthPassword;
	}

	/**
	 * SMTP認証パスワードを設定します。
	 *
	 * @param mailSmtpAuthPassword
	 *            SMTP認証パスワード
	 */
	public void setMailSmtpAuthPassword(String mailSmtpAuthPassword) {
		this.mailSmtpAuthPassword = mailSmtpAuthPassword;
	}

	/**
	 * SMTP認証方式 (LOGIN PLAIN DIGEST-MD5 NTLM(対応していない)を取得します。
	 *
	 * @return SMTP認証方式 (LOGIN PLAIN DIGEST-MD5 NTLM(対応していない)
	 */
	public String getMailSmtpAuthMechanisms() {
		return mailSmtpAuthMechanisms;
	}

	/**
	 * SMTP認証方式 (LOGIN PLAIN DIGEST-MD5 NTLM(対応していない)を設定します。
	 *
	 * @param mailSmtpAuthMechanisms
	 *            SMTP認証方式 (LOGIN PLAIN DIGEST-MD5 NTLM(対応していない)
	 * @throws IllegalArgumentException
	 *             LOGIN PLAIN DIGEST-MD5以外が設定された場合
	 */
	public void setMailSmtpAuthMechanisms(String mailSmtpAuthMechanisms) {
		if (mailSmtpAuthMechanisms != null && !"LOGIN".equals(mailSmtpAuthMechanisms) && !"PLAIN".equals(mailSmtpAuthMechanisms) && !"DIGEST-MD5".equals(mailSmtpAuthMechanisms)) {
			throw new IllegalArgumentException("LOGIN PLAIN DIGEST-MD5以外は設定できません。Value=" + mailSmtpAuthMechanisms);
		}
		this.mailSmtpAuthMechanisms = mailSmtpAuthMechanisms;
	}

	/**
	 * SMTP SSL接続を利用するか否かを取得します。
	 * （SSLを利用する場合、ポートの変更が必要です。）
	 *
	 * @return SMTP SSL接続を利用するか否か
	 */
	public boolean isMailSmtpSsl() {
		return mailSmtpSsl;
	}

	/**
	 * SMTP SSL接続を利用するか否かを設定します。
	 *
	 * @param mailSmtpSsl SMTP SSL接続を利用するか否か
	 */
	public void setMailSmtpSsl(boolean mailSmtpSsl) {
		this.mailSmtpSsl = mailSmtpSsl;
	}

	/**
	 * Pop3メールサーバアドレスを取得します。
	 *
	 * @return Pop3メールサーバアドレス
	 */
	public String getMailPop3Host() {
		return mailPop3Host;
	}

	/**
	 * Pop3メールサーバアドレスを設定します。
	 *
	 * @param mailPop3Host Pop3メールサーバアドレス
	 */
	public void setMailPop3Host(String mailPop3Host) {
		this.mailPop3Host = mailPop3Host;
	}

	/**
	 * Pop3ポート 通常：110 / ssl 995を取得します。
	 *
	 * @return Pop3ポート 通常：110 / ssl 995
	 */
	public int getMailPop3Port() {
		return mailPop3Port;
	}

	/**
	 * Pop3ポート 通常：110 / ssl 995を設定します。
	 *
	 * @param mailPop3Port
	 *            Pop3ポート 通常：110 / ssl 995
	 */
	public void setMailPop3Port(int mailPop3Port) {
		this.mailPop3Port = mailPop3Port;
	}

	/**
	 * Pop3認証ユーザIDを取得します。
	 *
	 * @return Pop3認証ユーザID
	 */
	public String getMailPop3AuthId() {
		return mailPop3AuthId;
	}

	/**
	 * Pop3認証ユーザIDを設定します。
	 *
	 * @param mailPop3AuthId Pop3認証ユーザID
	 */
	public void setMailPop3AuthId(String mailPop3AuthId) {
		this.mailPop3AuthId = mailPop3AuthId;
	}

	/**
	 * Pop3認証パスワードを取得します。
	 *
	 * @return Pop3認証パスワード
	 */
	public String getMailPop3AuthPassword() {
		return mailPop3AuthPassword;
	}

	/**
	 * Pop3認証パスワードを設定します
	 *
	 * @param mailPop3AuthPassword Pop3認証パスワード
	 */
	public void setMailPop3AuthPassword(String mailPop3AuthPassword) {
		this.mailPop3AuthPassword = mailPop3AuthPassword;
	}

	/**
	 * APOP を利用するか否か true:利用する/true文字以外:利用しないを取得します。
	 *
	 * @return APOP を利用するか否か true:利用する/true文字以外:利用しない
	 */
	public boolean isMailPop3ApopEnable() {
		return mailPop3ApopEnable;
	}

	/**
	 * APOP を利用するか否か true:利用する/true文字以外:利用しないを設定します。
	 *
	 * @param mailPop3ApopEnable
	 *            APOP を利用するか否か true:利用する/true文字以外:利用しない
	 */
	public void setMailPop3ApopEnable(boolean mailPop3ApopEnable) {
		this.mailPop3ApopEnable = mailPop3ApopEnable;
	}

	/**
	 * Pop3接続タイムアウト設定(ms)を取得します。
	 *
	 * @return Pop3接続タイムアウト設定(ms)
	 */
	public int getMailPop3Connectiontimeout() {
		return mailPop3Connectiontimeout;
	}

	/**
	 * Pop3接続タイムアウト設定(ms)を設定します。
	 *
	 * @param mailPop3Connectiontimeout
	 *            Pop3接続タイムアウト設定(ms)
	 */
	public void setMailPop3Connectiontimeout(int mailPop3Connectiontimeout) {
		this.mailPop3Connectiontimeout = mailPop3Connectiontimeout;
	}

	/**
	 * Pop3タイムアウト設定を取得します。
	 *
	 * @return Pop3タイムアウト設定
	 */
	public int getMailPop3Timeout() {
		return mailPop3Timeout;
	}

	/**
	 * Pop3タイムアウト設定を設定します。
	 *
	 * @param mailPop3Timeout
	 *            Pop3タイムアウト設定
	 */
	public void setMailPop3Timeout(int mailPop3Timeout) {
		this.mailPop3Timeout = mailPop3Timeout;
	}

	/**
	 * Pop3SSL接続を利用するか否かを取得します。
	 * （SSLを利用する場合、ポートの変更が必要です。）
	 *
	 * @return Pop3SSL接続を利用するか否か
	 */
	public boolean isMailPop3Ssl() {
		return mailPop3Ssl;
	}

	/**
	 * Pop3SSL接続を利用するか否かを設定します。
	 *
	 * @param mailPop3Ssl Pop3SSL接続を利用するか否か
	 */
	public void setMailPop3Ssl(boolean mailPop3Ssl) {
		this.mailPop3Ssl = mailPop3Ssl;
	}

	/**
	 * mailSmtp.hostプロパティはmail.hostプロパティに優先して認識されるため、2つの値が同一ならば、
	 * mailSmtp.hostプロパティを設定するだけでもメール送信は可能です。 しかし、mail.hostプロパティは内部的にMessage-ID
	 * ヘッダを生成するのに利用されます。
	 * mail.hostプロパティを明示的に指定していない場合、Message-IDヘッダが正しく生成できない可能性があります。 元情報
	 * http://wwwAtmarkit.co.jp/fjava/javatips/123java022.html
	 *
	 * @return MailHost設定
	 */
	public String getMailHost() {
		return mailHost;
	}

	/**
	 * mailSmtp.hostプロパティはmail.hostプロパティに優先して認識されるため、2つの値が同一ならば、
	 * mailSmtp.hostプロパティを設定するだけでもメール送信は可能です。 しかし、mail.hostプロパティは内部的にMessage-ID
	 * ヘッダを生成するのに利用されます。
	 * mail.hostプロパティを明示的に指定していない場合、Message-IDヘッダが正しく生成できない可能性があります。 元情報
	 * http://wwwAtmarkit.co.jp/fjava/javatips/123java022.html
	 *
	 * @param MailHost設定
	 */
	public void setMailHost(String mailHost) {
		this.mailHost = mailHost;
	}

	/**
	 * Charsetを取得します。
	 *
	 * @return Charset
	 */
	public String getMailCharset() {
		return mailCharset;
	}

	/**
	 * Charsetを設定します。
	 *
	 * @param mailCharset
	 *            Charset
	 */
	public void setMailCharset(String mailCharset) {
		this.mailCharset = mailCharset;
	}

	public void setContentTransferEncoding(String contentTransferEncoding) {
		this.contentTransferEncoding = contentTransferEncoding;
	}

	public String getContentTransferEncoding() {
		return contentTransferEncoding;
	}

	public void setMailSmtpFrom(String mailSmtpFrom) {
		this.mailSmtpFrom = mailSmtpFrom;
	}
	
	public String getMailSmtpFrom() {
		return mailSmtpFrom;
	}

}
