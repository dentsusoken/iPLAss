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

/**
 *
 */
package org.iplass.mtp.message;

import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.xml.bind.annotation.XmlRootElement;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.mtp.definition.Definition;

/**
 * メッセージカテゴリ情報格納クラス
 *
 * @author 藤田　義弘
 *
 */
@XmlRootElement
public class MessageCategory implements Definition {

	private static final long serialVersionUID = -3083536347753776159L;

	/** 名前 */
	private String name;

	/** 表示名 */
	private String displayName;

	/** 説明 */
	private String description;

	/** メッセージ情報 */
	@MultiLang(isMultiLangValue = false, itemKey = "messageItems", itemGetter = "getMessageItems", itemSetter = "setMessageItems")
	Map<String,MessageItem> messageItems;

	/**
	 * コンストラクタ
	 */
	public MessageCategory() {
	}

	/**
	 * 名前を取得します。
	 *
	 * @return 名前
	 */
	public String getName() {
		return name;
	}

	/**
	 * 名前を設定します。
	 *
	 * @param name 名前
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 表示名を取得します。
	 *
	 * @return 表示名
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * 表示名を設定します。
	 *
	 * @param displayName 表示名
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * 説明を取得します。
	 *
	 * @return 説明
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 説明を設定します。
	 *
	 * @param description 説明
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * メッセージ情報を取得します。
	 *
	 * @return メッセージ情報
	 */
	public Map<String,MessageItem> getMessageItems() {
	    return messageItems;
	}

	/**
	 * メッセージ情報を設定します。
	 *
	 * @param messageItems
	 *            メッセージ情報
	 */
	public void setMessageItems(Map<String,MessageItem> messageItems) {
	    this.messageItems = messageItems;
	}

	/**
	 * メッセージ情報を追加する。
	 *
	 * @param messageItem
	 *            追加するメッセージ情報
	 */
	public void addMessageItem(MessageItem messageItem) {
		if (messageItems == null) {
			messageItems = new LinkedHashMap<String,MessageItem>();
		}
		messageItems.put(messageItem.getMessageId(), messageItem);
	}

}
