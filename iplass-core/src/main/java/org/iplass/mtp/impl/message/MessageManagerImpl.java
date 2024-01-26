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

package org.iplass.mtp.impl.message;

import java.util.List;

import org.iplass.mtp.definition.DefinitionModifyResult;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.definition.AbstractTypedDefinitionManager;
import org.iplass.mtp.impl.definition.TypedMetaDataService;
import org.iplass.mtp.impl.message.MetaMessageCategory.MetaMessageCategoryHandler;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.message.MessageCategory;
import org.iplass.mtp.message.MessageItem;
import org.iplass.mtp.message.MessageManager;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * メッセージ実装マネージャ
 *
 * @author 藤田　義弘
 *
 */
public class MessageManagerImpl extends AbstractTypedDefinitionManager<MessageCategory> implements MessageManager {

	private static final Logger logger = LoggerFactory.getLogger(MessageManagerImpl.class);

	private MessageService service = ServiceRegistry.getRegistry().getService(MessageService.class);

	/**
	 * @deprecated {@link #create(MessageCategory)} を使用してください。
	 */
	@Deprecated
	@Override
	public DefinitionModifyResult createMessage(MessageCategory messageCategory) {
		return create(messageCategory);
	}

	/**
	 * @deprecated {@link #update(MessageCategory)} を使用してください。
	 */
	@Deprecated
	@Override
	public DefinitionModifyResult updateMessage(MessageCategory messageCategory) {
		return update(messageCategory);
	}

	/**
	 * @deprecated {@link #remove(String)} を使用してください。
	 */
	@Deprecated
	@Override
	public DefinitionModifyResult deleteMessage(String name) {
		return remove(name);
	}

	// FIXME Item用のメソッドは必要か？
	@Override
	public DefinitionModifyResult createMessageItem(String category, MessageItem messageItem) {
		MetaMessageCategory metaCategoryMessage;
		MetaMessageCategoryHandler handler = service.getRuntimeByName(category);
		MetaMessageItem metaMessageItem = new MetaMessageItem();
		metaMessageItem.setValue(messageItem);
		if(handler == null) {
			metaCategoryMessage = new MetaMessageCategory();
			metaCategoryMessage.setName(category);
			metaCategoryMessage.addMessage(metaMessageItem);
			try {
				service.createMetaData(metaCategoryMessage);
			} catch (Exception e) {
				setRollbackOnly();
				if (e.getCause() != null) {
					logger.error("exception occured during message category definition create:" + e.getCause().getMessage(), e.getCause());
					return new DefinitionModifyResult(false, "exception occured during message category definition create:" + e.getCause().getMessage());
				} else {
					logger.error("exception occured during message category definition create:" + e.getMessage(), e);
					return new DefinitionModifyResult(false, "exception occured during message category definition create:" + e.getMessage());
				}
			}
		}else{
			metaCategoryMessage = handler.getMetaData();
			metaCategoryMessage.addMessage(metaMessageItem);
			try {
				service.updateMetaData(metaCategoryMessage);
			} catch (Exception e) {
				setRollbackOnly();
				if (e.getCause() != null) {
					logger.error("exception occured during message category definition update:" + e.getCause().getMessage(), e.getCause());
					return new DefinitionModifyResult(false, "exception occured during message category definition update:" + e.getCause().getMessage());
				} else {
					logger.error("exception occured during message category definition update:" + e.getMessage(), e);
					return new DefinitionModifyResult(false, "exception occured during message category definition update:" + e.getMessage());
				}
			}
		}
		return new DefinitionModifyResult(true);
	}

	@Override
	public DefinitionModifyResult updateMessageItem(String category, MessageItem messageItem) {
		MetaMessageCategoryHandler handler = service.getRuntimeByName(category);
		if(handler == null) {
			logger.error("exception occured during message category definition update:"
					+ "指定のメッセージカテゴリは存在しません。メッセージカテゴリ名=" + category);
			return new DefinitionModifyResult(false, "exception occured during message category definition update:"
					+ "指定のメッセージカテゴリは存在しません。メッセージカテゴリ名=" + category);
		}
		MetaMessageItem metaMessageItem = new MetaMessageItem();
		metaMessageItem.setValue(messageItem);
		MetaMessageCategory metaCategoryMessage = handler.getMetaData();
		metaCategoryMessage.addMessage(metaMessageItem);
		try {
			service.updateMetaData(metaCategoryMessage);
		} catch (Exception e) {
			setRollbackOnly();
			if (e.getCause() != null) {
				logger.error("exception occured during message category definition update:" + e.getCause().getMessage(), e.getCause());
				return new DefinitionModifyResult(false, "exception occured during message category definition update:" + e.getCause().getMessage());
			} else {
				logger.error("exception occured during message category definition update:" + e.getMessage(), e);
				return new DefinitionModifyResult(false, "exception occured during message category definition update:" + e.getMessage());
			}
		}
		return new DefinitionModifyResult(true);
	}

	@Override
	public DefinitionModifyResult deleteMessageItem(String category, String messageId) {
		MetaMessageCategoryHandler handler =  service.getRuntimeByName(category);
		if(handler == null) {
			//該当がない場合は正常とみなす
			return new DefinitionModifyResult(true);
		}
		MetaMessageCategory metaCategoryMessage = handler.getMetaData();
		metaCategoryMessage.getMessages().remove(messageId);
		try {
			service.updateMetaData(metaCategoryMessage);
		} catch (Exception e) {
			setRollbackOnly();
			if (e.getCause() != null) {
				logger.error("exception occured during message category definition update:" + e.getCause().getMessage(), e.getCause());
				return new DefinitionModifyResult(false, "exception occured during message category definition update:" + e.getCause().getMessage());
			} else {
				logger.error("exception occured during message category definition update:" + e.getMessage(), e);
				return new DefinitionModifyResult(false, "exception occured during message category definition update:" + e.getMessage());
			}
		}
		return new DefinitionModifyResult(true);
	}

	/**
	 * @see org.iplass.mtp.message.MessageManager#getMessageCategoryByName(java.lang.String)
	 * @deprecated {@link #get(String)} を使用してください。
	 */
	@Deprecated
	@Override
	public MessageCategory getMessageCategoryByName(String name) {
		return get(name);
	}

	/**
	 * @see org.iplass.mtp.message.MessageManager#getMessageIdList(java.lang.String)
	 */
	@Override
	public List<String> getMessageIdList(String category) {
		int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
		return service.getMessageIdList(tenantId, category);
	}

	/**
	 * @see org.iplass.mtp.message.MessageManager#getMessageItem(java.lang.String, java.lang.String)
	 */
	@Override
	public MessageItem getMessageItem(String category , String messageId) {
		MetaMessageCategoryHandler handler =  service.getRuntimeByName(category);
		if(handler == null) {
			return null;
		}
		MessageItem ret = handler.createMessageItem(messageId);
		return ret;
	}

	@Override
	public Class<MessageCategory> getDefinitionType() {
		return MessageCategory.class;
	}

	@Override
	protected RootMetaData newInstance(MessageCategory definition) {
		return new MetaMessageCategory();
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected TypedMetaDataService getService() {
		return service;
	}
}
