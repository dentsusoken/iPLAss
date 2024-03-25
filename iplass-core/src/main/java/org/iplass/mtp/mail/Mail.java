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

package org.iplass.mtp.mail;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.activation.DataHandler;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;

import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.impl.mail.BinaryReferenceDataSource;

/**
 * メールを表すクラスです。
 * 
 * @author 片野　博之
 *
 */
public class Mail {

	/** TO 送信アドレス */
	private List<InternetAddress> recipientTo = new ArrayList<InternetAddress>();
	/** CC 送信アドレス */
	private List<InternetAddress> recipientCc = new ArrayList<InternetAddress>();
	/** BCC 送信アドレス */
	private List<InternetAddress> recipientBcc = new ArrayList<InternetAddress>();
	/** FROM 送信アドレス */
	private InternetAddress fromAddress;
	/** replyToアドレス */
	private InternetAddress replyToAddress;
	/** Return-Pathアドレス */
	private String returnPath;

	/** 文字コード */
	final private String charset;

	/** メール件名 */
	private String subject;
	/** メール本文 */
	private String message;
	/** htmlメール本文 */
	private HtmlMessage htmlMessage;
	
	/** メールメッセージ日付 */
	private Date date;
	
	/** 添付ファイル */
	private List<DataHandler> attachments;

	private boolean smimeSign;
	private String smimeSignPassword;
	private boolean smimeEncript;

	public Mail(String charset) {
		this.charset = charset;
	}
	
	/**
	 * TO送信アドレスを追加します。
	 *
	 * @param address 送信アドレス
	 */
	public void addRecipientTo(String address) throws MailException {
		add(recipientTo, address, null);
	}

	/**
	 * TO送信アドレスを追加します。
	 *
	 * @param address 送信アドレス
	 * @param personal 個人名
	 */
	public void addRecipientTo(String address, String personal) throws MailException{
		add(recipientTo, address, personal);
	}

	/**
	 * CC送信アドレスを追加します。
	 *
	 * @param address 送信アドレス
	 */
	public void addRecipientCc(String address) throws MailException {
		add(recipientCc, address, null);
	}

	/**
	 * CC送信アドレスを追加します。
	 *
	 * @param address 送信アドレス
	 * @param personal 個人名
	 */
	public void addRecipientCc(String address, String personal) throws MailException{
		add(recipientCc, address, personal);
	}

	/**
	 * BCC送信アドレスを追加します。
	 *
	 * @param address 送信アドレス
	 */
	public void addRecipientBcc(String address) throws MailException {
		add(recipientBcc, address, null);
	}

	/**
	 * BCC送信アドレスを追加します。
	 *
	 * @param address 送信アドレス
	 * @param personal 個人名
	 */
	public void addRecipientBcc(String address, String personal) throws MailException{
		add(recipientBcc, address, personal);
	}

	/**
	 * 送信元アドレスをセットします。
	 *
	 * @param address 送信元アドレス
	 */
	public void setFrom(String address) throws MailException {
		setFrom(address, null);
	}

	/**
	 * 送信元アドレスをセットします。
	 *
	 * @param address 送信元アドレス
	 * @param personal 個人名
	 */
	public void setFrom(String address, String personal) throws MailException{
		fromAddress = createAddress(address, personal, charset);
	}

	/**
	 * 送信元アドレスをセットします。
	 * @param address
	 */
	public void setFromAddress(InternetAddress address) {
		this.fromAddress = address;
	}
	
	private InternetAddress createAddress(String address, String personal, String charset) {
		try {
			InternetAddress iAddress;
			if (personal == null) {
				iAddress = new InternetAddress(address);
			} else {
				iAddress = new InternetAddress(address, personal, charset);
			}
			iAddress.validate();
			return iAddress;
		} catch (AddressException e) {
			throw new MailException("invalid address:" + address, e);
		} catch (UnsupportedEncodingException e) {
			throw new MailException("invalid charset:" + charset, e);
		}

	}

	private void add(List<InternetAddress> list, String address, String personal) throws MailException {
		list.add(createAddress(address, personal, charset));
	}
	
	/**
	 * セットされている送信元アドレスを取得します。
	 * @return
	 */
	public InternetAddress getFromAddress() {
		return fromAddress;
	}

	/**
	 * 返信先アドレスを設定します。
	 *
	 * @param address 返信先アドレス
	 */
	public void setReplyTo(String address) throws MailException {
		setReplyTo(address, null);
	}

	/**
	 * 返信先アドレスを設定します。
	 * 
	 * @param address 返信先アドレス
	 * @param personal 個人名
	 */
	public void setReplyTo(String address, String personal) throws MailException {
		replyToAddress = createAddress(address, personal, charset);
	}

	/**
	 * 返信先アドレスを設定します。
	 * @param address
	 */
	public void setReplyToAddress(InternetAddress address) {
		this.replyToAddress = address;
	}
	
	/**
	 * セットされている返信先アドレスを取得します。
	 * @return
	 */
	public InternetAddress getReplyToAddress() {
		return replyToAddress;
	}

	/**
	 * Return-Pathを設定します。
	 *
	 * @param address Return-Pathアドレス
	 */
	public void setReturnPath(String returnPath) {
		this.returnPath = returnPath;
	}
	
	/**
	 * 設定されているReturn-Pathを取得します。
	 * @return
	 */
	public String getReturnPath() {
		return returnPath;
	}
	
	/**
	 * 文字コードを取得します。
	 * @return 文字コード
	 */
	public String getCharset() {
	    return charset;
	}

	/**
	 * メール件名を取得します。
	 * @return メール件名
	 */
	public String getSubject() {
	    return subject;
	}

	/**
	 * メール件名を設定します。
	 * @param subject メール件名
	 */
	public void setSubject(String subject) {
	    this.subject = subject;
	}

	/**
	 * メール本文（プレーンテキスト）を取得します。
	 * @return メール本文
	 */
	public String getMessage() {
	    return message;
	}

	/**
	 * メール本文（プレーンテキスト）を設定します。
	 * @param message メール本文
	 */
	public void setMessage(String message) {
	    this.message = message;
	}

	/**
	 * メール本文（html）を取得します。
	 *
	 * @return
	 */
	public HtmlMessage getHtmlMessage() {
		return htmlMessage;
	}

	/**
	 * メール本文（html）を設定します。
	 * htmlメール送信時に、messageにプレーンテキストをセットし、
	 * htmlMessageにはhtmlをセットすることにより、
	 * 両対応のメールとして送信可能です。
	 *
	 * @param htmlMessage
	 */
	public void setHtmlMessage(HtmlMessage htmlMessage) {
		this.htmlMessage = htmlMessage;
	}

	/**
	 * メールメッセージ日付を取得します。
	 * @return メールメッセージ日付
	 */
	public Date getDate() {
	    return date;
	}
	
	/**
	 * メールメッセージ日付を設定します。
	 * @param date メールメッセージ日付
	 */
	public void setDate(Date date) {
	    this.date = date;
	}
	
	/**
	 * 添付ファイルを追加します。
	 * 添付ファイル名は、BinaryReferenceのname、contentTypeはBinaryReferenceのtypeが設定されます。
	 * 
	 * @param bin 添付するデータを指し示すBinaryReference
	 */
	public void addAttachment(BinaryReference bin) {
		addAttachment(new DataHandler(new BinaryReferenceDataSource(bin)));
	}
	
	/**
	 * 添付ファイルを追加します。
	 * 
	 * @param dataHandler 添付ファイルを指し示すDataHandler
	 */
	public void addAttachment(DataHandler dataHandler) {
		if (attachments == null) {
			attachments = new ArrayList<>();
		}
		attachments.add(dataHandler);
	}
	
	public List<DataHandler> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<DataHandler> attachments) {
		this.attachments = attachments;
	}
	
	public List<InternetAddress> getRecipientTo() {
	    return recipientTo;
	}

	public void setRecipientTo(List<InternetAddress> recipientTo) {
	    this.recipientTo = recipientTo;
	}

	public List<InternetAddress> getRecipientCc() {
	    return recipientCc;
	}

	public void setRecipientCc(List<InternetAddress> recipientCc) {
	    this.recipientCc = recipientCc;
	}

	public List<InternetAddress> getRecipientBcc() {
	    return recipientBcc;
	}

	public void setRecipientBcc(List<InternetAddress> recipientBcc) {
	    this.recipientBcc = recipientBcc;
	}
	
	/**
	 * S/MIMEによる署名を行う場合、trueを設定します。
	 * 事前に、S/MIME用の証明書（および秘密鍵）ストアに
	 * 送信者のメールアドレスに対する証明書、秘密鍵が格納されている必要があります。
	 * 
	 * @param smimeSign
	 */
	public void setSmimeSign(boolean smimeSign) {
		this.smimeSign = smimeSign;
	}
	public boolean isSmimeSign() {
		return smimeSign;
	}

	/**
	 * S/MIMEによる署名を行う場合、
	 * 且つ明示的にパスワード指定を行う場合に指定します。
	 * 
	 * @param smimeSignPassword
	 */
	public void setSmimeSignPassword(String smimeSignPassword) {
		this.smimeSignPassword = smimeSignPassword;
	}
	public String getSmimeSignPassword() {
		return smimeSignPassword;
	}
	
	/**
	 * S/MIMEによる暗号化を行う場合、trueを設定します。
	 * 事前に、S/MIME用の証明書ストアに
	 * 受信者のメールアドレスに対する証明書が格納されている必要があります。
	 * 
	 * @param smimeEncript
	 */
	public void setSmimeEncript(boolean smimeEncript) {
		this.smimeEncript = smimeEncript;
	}
	public boolean isSmimeEncript() {
		return smimeEncript;
	}

}
