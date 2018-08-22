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

package org.iplass.mtp.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.mtp.definition.LocalizedStringDefinition;

/**
 * メッセージ情報格納クラス
 *
 * @author 藤田　義弘
 *
 */
public class MessageItem implements Serializable {

	private static final long serialVersionUID = 6941829508291170486L;

	@MultiLang(itemNameGetter = "getMessageId", itemKey = "message", itemGetter = "getMessage", itemSetter = "setMessage", multiLangGetter = "getLocalizedMessageList", multiLangSetter = "setLocalizedMessageList")
	private String message;
	private String messageId;

	/** 多言語設定情報 */
	private List<LocalizedStringDefinition> localizedMessageList;

	/**
	 * メッセージIDを取得します。
	 *
	 * @return メッセージID
	 */
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	/**
	 * メッセージIDを設定します。
	 *
	 * @param messageId
	 *            メッセージID
	 */
	public String getMessageId() {
		return messageId;
	}

	/**
	 * メッセージを取得します。
	 *
	 * @return メッセージ
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * メッセージを設定します。
	 *
	 * @param message
	 *            メッセージ
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 多言語設定情報を取得します。
	 * @return リスト
	 */
	public List<LocalizedStringDefinition> getLocalizedMessageList() {
		return localizedMessageList;
	}

	/**
	 * 多言語設定情報を設定します。
	 * @param リスト
	 */
	public void setLocalizedMessageList(List<LocalizedStringDefinition> localizedMessageList) {
		this.localizedMessageList = localizedMessageList;
	}

	/**
	 * 多言語設定情報を追加します。
	 * @param 多言語設定情報
	 */
	public void addLocalizedMessage(LocalizedStringDefinition localizedMessage) {
		if (localizedMessageList == null) {
			localizedMessageList = new ArrayList<LocalizedStringDefinition>();
		}

		localizedMessageList.add(localizedMessage);
	}
}
