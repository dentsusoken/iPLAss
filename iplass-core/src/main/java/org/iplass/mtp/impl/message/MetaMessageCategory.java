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
package org.iplass.mtp.impl.message;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.iplass.mtp.impl.definition.DefinableMetaData;
import org.iplass.mtp.impl.i18n.MetaLocalizedString;
import org.iplass.mtp.impl.metadata.BaseMetaDataRuntime;
import org.iplass.mtp.impl.metadata.BaseRootMetaData;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.metadata.MetaDataRuntime;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.message.MessageCategory;
import org.iplass.mtp.message.MessageItem;


/**
 * メッセージカテゴリのメタ情報
 * @author 藤田　義弘
 *
 */
@XmlRootElement
public class MetaMessageCategory extends BaseRootMetaData implements DefinableMetaData<MessageCategory> {

	/** SerialVersionUID */
	private static final long serialVersionUID = 3080437844175871382L;

	/** メッセージメタ情報 */
	@XmlElement
	private LinkedHashMap<String,MetaMessageItem> messages;

	/**
	 * コンストラクタ
	 */
	public MetaMessageCategory() {
	}

	/**
	 *
	 * @see org.iplass.mtp.impl.metadata.MetaData#copy()
	 */
	@Override
	public MetaMessageCategory copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public MetaDataRuntime createRuntime(MetaDataConfig metaDataConfig) {
		return new MetaMessageCategoryHandler();
	}

	/**
	 * メタ情報を元にメッセージ情報マップを作成
	 *
	 * @return Map<String, MessageItem> メッセージ情報マップ
	 */
	public Map<String, MessageItem> createMessageItems() {
		if(messages == null || messages.size() == 0) {
			return Collections.emptyMap();
		}
		Map<String, MessageItem> ret = new LinkedHashMap<String, MessageItem>(messages.size());
		for (Map.Entry<String, MetaMessageItem> metaItem: messages.entrySet()) {
			ret.put(metaItem.getKey(),metaItem.getValue().createMessageItem());
		}
		return ret.size() == 0 ? Collections.<String, MessageItem>emptyMap() : ret;
	}

	/**
	 * メタメッセージカテゴリハンドラ
	 *
	 * @author 藤田　義弘
	 *
	 */
	public class MetaMessageCategoryHandler extends BaseMetaDataRuntime {

		/**
		 * コンストラクタ
		 */
		public MetaMessageCategoryHandler() {
		}

		/**
		 * メタデータ取得
		 *
		 * @return MetaMessageCategory メタメッセージカテゴリ
		 */
		@Override
		public MetaMessageCategory getMetaData() {
			return MetaMessageCategory.this;
		}
//
//		/**
//		 * メタ情報を元にメッセージ情報マップを作成
//		 *
//		 * @return Map<String, MessageItem> メッセージ情報マップ
//		 */
//		public Map<String, MessageItem> createMessageItems() {
//			return createMessageItems();
//		}

		/**
		 * パラメータを元にメッセージ情報を作成
		 *
		 * @param String messageId メッセージＩＤ
		 * @return MessageItem メタメッセージカテゴリ
		 */
		public MessageItem createMessageItem(String messageId) {
			if(messages == null || messages.size() == 0) {
				return null;
			}
			MetaMessageItem meta = messages.get(messageId);
			if (meta == null) {
				return null;
			}
			return meta.createMessageItem();
		}

		public String getMessageString(String messageId, String lang) {
			if (messages == null) {
				return null;
			}
			MetaMessageItem item = messages.get(messageId);
			if (item == null) {
				return null;
			}
			if (lang != null) {
				if (item.getLocalizedMessageList() != null) {
					for (MetaLocalizedString mls: item.getLocalizedMessageList()) {
						if (lang.equals(mls.getLocaleName())) {
							return mls.getStringValue();
						}
					}
				}
			}
			return item.getMessage();
		}
	}

	public void applyConfig(MessageCategory definition) {
		setName(definition.getName());
		setDisplayName(definition.getDisplayName());
		setDescription(definition.getDescription());
		setMetaMessages(definition.getMessageItems());
	}

	public MessageCategory currentConfig() {
		MessageCategory definition = new MessageCategory();
		definition.setName(name);
		definition.setDisplayName(displayName);
		definition.setDescription(description);
		definition.setMessageItems(createMessageItems());

		return definition;
	}

	/**
	 * メッセージメタ情報を取得します。
	 * @return メッセージメタ情報
	 */
	@XmlTransient
	public Map<String,MetaMessageItem> getMessages() {
	    return messages;
	}

	/**
	 * メッセージメタ情報を設定します。
	 * @param messages メッセージメタ情報
	 */
	public void setMessages(Map<String,MetaMessageItem> messages) {
		if (messages == null) {
			this.messages = null;
		} else {
			if (messages instanceof LinkedHashMap) {
			    this.messages = (LinkedHashMap<String,MetaMessageItem>)messages;
			} else {
				this.messages = new LinkedHashMap<>(messages);
			}
		}
	}

	/**
	 * メッセージメタ情報を追加します。
	 * @param messages メッセージメタ情報
	 */
	public void addMessage(MetaMessageItem metaMessageItem) {
		if(messages == null) {
			messages = new LinkedHashMap<String,MetaMessageItem>();
		}
		messages.put(metaMessageItem.getMessageId(), metaMessageItem);
	}

	/**
	 * メッセージ情報からメッセージメタ情報を設定します。
	 * @param messages メッセージ情報
	 */
	public void setMetaMessages(Map<String, MessageItem> messages) {
		if (messages == null) {
			return;
		}
		for (Map.Entry<String, MessageItem> messageItem: messages.entrySet()) {
			MetaMessageItem item = new MetaMessageItem();
			item.setValue(messageItem.getValue());
			addMessage(item);
		}
	}

}
