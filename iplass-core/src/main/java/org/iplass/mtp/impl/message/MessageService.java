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

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.impl.definition.AbstractTypedMetaDataService;
import org.iplass.mtp.impl.definition.DefinitionMetaDataTypeMap;
import org.iplass.mtp.impl.definition.DefinitionNameChecker;
import org.iplass.mtp.impl.message.MetaMessageCategory.MetaMessageCategoryHandler;
import org.iplass.mtp.message.MessageCategory;
import org.iplass.mtp.message.MessageManager;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;


/**
 * メッセージサービス
 *
 * @author 藤田　義弘
 *
 */
public class MessageService extends AbstractTypedMetaDataService<MetaMessageCategory, MetaMessageCategoryHandler> implements Service {

	/** メタデータのパス */
	public static final String MESSAGE_META_PATH = "/message/";

	public static class TypeMap extends DefinitionMetaDataTypeMap<MessageCategory, MetaMessageCategory> {
		public TypeMap() {
			super(getFixedPath(), MetaMessageCategory.class, MessageCategory.class);
		}
		@Override
		public TypedDefinitionManager<MessageCategory> typedDefinitionManager() {
			return ManagerLocator.getInstance().getManager(MessageManager.class);
		}

		@Override
		protected DefinitionNameChecker createDefinitionNameChecker() {
			return DefinitionNameChecker.getPathSlashDefinitionNameChecker();
		}
	}

	private List<ResourceBundleConfig> resourceBundle;
	private ConcurrentHashMap<String, ResourceBundle.Control> resourceBundleControlMap;
	
	public List<ResourceBundleConfig> getResourceBundle() {
		return resourceBundle;
	}

	/**
	 * @see org.iplass.mtp.spi.Service#init(org.iplass.mtp.spi.Config)
	 */
	@Override
	public void init(Config config) {
		resourceBundle = config.getValues("resourceBundle", ResourceBundleConfig.class);
		resourceBundleControlMap = new ConcurrentHashMap<>();
	}

	/**
	 * @see org.iplass.mtp.spi.Service#destroy()
	 */
	@Override
	public void destroy() {
	}

	public static String getFixedPath() {
		return MESSAGE_META_PATH;
	}

	/**
	 * メッセージカテゴリの一覧を取得する。
	 * @param tenantId テナントID
	 * @param category メッセージカテゴリ名
	 * @return List<String> メッセージカテゴリの一覧
	 */
	public List<String> getMessageIdList(int tenantId, String category) {
		MetaMessageCategoryHandler handler = getRuntimeByName(category);
		MetaMessageCategory metaData = handler.getMetaData();
		return new ArrayList<String>(metaData.getMessages().keySet());
	}

	@Override
	public Class<MetaMessageCategory> getMetaDataType() {
		return MetaMessageCategory.class;
	}

	@Override
	public Class<MetaMessageCategoryHandler> getRuntimeType() {
		return MetaMessageCategoryHandler.class;
	}
	
	public ResourceBundle.Control getResourceBundleControl(String baseBundleName) {
		ResourceBundle.Control ctrl = resourceBundleControlMap.get(baseBundleName);
		if (ctrl == null) {
			if (resourceBundle != null) {
				for (ResourceBundleConfig rbc: resourceBundle) {
					if (rbc.isMatch(baseBundleName)) {
						ctrl = rbc.getResourceBundleControl();
						resourceBundleControlMap.put(baseBundleName, ctrl);
						break;
					}
				}
			}
		}
		
		return ctrl;
	}

}
