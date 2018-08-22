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

package org.iplass.mtp.impl.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.i18n.MetaLocalizedString;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.message.MessageItem;


/**
 * メッセージのメタ情報
 * @author 藤田　義弘
 *
 */
public class MetaMessageItem implements Serializable {

	/** SerialVersionUID */
	private static final long serialVersionUID = -464261717351843618L;

	/** メッセージ定義ID */
	private String messageId;

	/** メッセージ定義ID */
	private String message;

	/** 多言語設定情報 */
	private List<MetaLocalizedString> localizedMessageList = new ArrayList<MetaLocalizedString>();

	/**
	 * コンストラクタ
	 */
	public MetaMessageItem() {
	}

	/**
	 * @see org.iplass.mtp.impl.metadata.MetaData#copy()
	 */
	public MetaMessageItem copy() {
		return ObjectUtil.deepCopy(this);
	}

	/**
	 * MessageItemの情報を自身に設定する。
	 * @param messageItem
	 */
	public void setValue(MessageItem messageItem) {
		messageId = messageItem.getMessageId();
		message =messageItem.getMessage();
		localizedMessageList = I18nUtil.toMeta(messageItem.getLocalizedMessageList());
	}

	/**
	 * メッセージ定義IDを取得します。
	 * @return メッセージ定義ID
	 */
	public String getMessageId() {
	    return messageId;
	}

	/**
	 * メッセージ定義IDを設定します。
	 * @param messageId メッセージ定義ID
	 */
	public void setMessageId(String messageId) {
	    this.messageId = messageId;
	}

	/**
	 * メッセージを取得します。
	 * @return メッセージ
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * メッセージを設定します。
	 * @param message メッセージ
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 自身のメタ情報からメッセージ情報を作成する
	 * @return ret メッセージ情報
	 */
	public MessageItem createMessageItem(){
		MessageItem ret = new MessageItem();
		setMessageItem(ret);
		return ret;
	}

	/**
	 * 引数のMessageItemに自身の情報を設定する
	 * @param messageItem
	 */
	protected void setMessageItem(MessageItem messageItem) {
		messageItem.setMessage(message);
		messageItem.setMessageId(messageId);
		messageItem.setLocalizedMessageList(I18nUtil.toDef(localizedMessageList));
	}

	/**
	 * 多言語設定情報を取得します。
	 * @return リスト
	 */
	public List<MetaLocalizedString> getLocalizedMessageList() {
		return localizedMessageList;
	}

	/**
	 * 多言語設定情報を設定します。
	 * @param リスト
	 */
	public void setLocalizedMessageList(List<MetaLocalizedString> localizedMessageList) {
		this.localizedMessageList = localizedMessageList;
	}

}
