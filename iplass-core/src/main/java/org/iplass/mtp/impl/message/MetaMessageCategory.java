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

/**
 *
 */
package org.iplass.mtp.impl.message;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlRootElement;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.definition.DefinableMetaData;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.i18n.MetaLocalizedString;
import org.iplass.mtp.impl.metadata.BaseMetaDataRuntime;
import org.iplass.mtp.impl.metadata.BaseRootMetaData;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.metadata.MetaDataRuntime;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.validation.ValidationContext;
import org.iplass.mtp.message.MessageCategory;
import org.iplass.mtp.message.MessageItem;
import org.iplass.mtp.util.StringUtil;


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
	private LinkedHashMap<String,MetaMessageItem> messages;

	private static final Pattern namePattern = Pattern.compile("${name}", Pattern.LITERAL);
	private static final Pattern entityNamePattern = Pattern.compile("${entityName}", Pattern.LITERAL);

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

		public String createMessage(MetaMessageItem item, ValidationContext context, String propertyDisplayName, String entityDisplayName) {

			String msg = null;
			if (item != null) {
				msg = item.getMessage();

				Map<String, String> localizedStringMap = new HashMap<String, String>();
				if (item.getLocalizedMessageList() != null) {
					for (MetaLocalizedString mls : item.getLocalizedMessageList()) {
						localizedStringMap.put(mls.getLocaleName(), mls.getStringValue());
					}
				}

				String lang = ExecuteContext.getCurrentContext().getLanguage();

				if (StringUtil.isNotEmpty(localizedStringMap.get(lang))) {
					msg = localizedStringMap.get(lang);
				}

				//${name},${entityName}限定で置換
				Entity entity = context.getValidatingDataModel();
				// listnerクラスが設定されている場合落ちるのでプロパティ定義を取得
				MetaDataEntry entry = MetaDataContext.getContext().getMetaDataEntry(("/entity/" + entity.getDefinitionName()).replace(".","/"));
				EntityHandler eHandl = (EntityHandler) entry.getRuntime();
				Map<String, String> entityLangMap = eHandl.getLocalizedStringMap();

				PropertyHandler pHandl = eHandl.getProperty(context.getValidatePropertyName(), EntityContext.getCurrentContext());
				Map<String, String> propLangMap = pHandl.getLocalizedStringMap();

				if (msg != null) {
					if (msg.contains("${name}")) {
						String replaceName = propertyDisplayName;
						if (propLangMap.get(lang) != null) {
							replaceName = propLangMap.get(lang);
						}
						msg = namePattern.matcher(msg).replaceAll(replaceName);
					}
					if (msg.contains("${entityName}")) {
						String replaceName = entityDisplayName;
						if (entityLangMap.get(lang) != null) {
							replaceName = entityLangMap.get(lang);
						}
						msg = entityNamePattern.matcher(msg).replaceAll(replaceName);
					}
				}
			}

			return msg;
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
	public LinkedHashMap<String,MetaMessageItem> getMessages() {
	    return messages;
	}

	/**
	 * メッセージメタ情報を設定します。
	 * @param messages メッセージメタ情報
	 */
	public void setMessages(LinkedHashMap<String,MetaMessageItem> messages) {
	    this.messages = messages;
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
			MetaMessageItem buf = new MetaMessageItem();
			buf.setValue(messageItem.getValue());
			if(buf != null) {
				addMessage(buf);
			}
		}
	}

}
