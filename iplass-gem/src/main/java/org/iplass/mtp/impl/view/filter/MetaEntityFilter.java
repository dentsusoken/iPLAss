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

package org.iplass.mtp.impl.view.filter;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.iplass.mtp.impl.definition.DefinableMetaData;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.metadata.BaseMetaDataRuntime;
import org.iplass.mtp.impl.metadata.BaseRootMetaData;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.metadata.MetaDataRuntime;
import org.iplass.mtp.impl.util.KeyGenerator;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.view.filter.EntityFilter;
import org.iplass.mtp.view.filter.EntityFilterItem;

/**
 * フィルタ定義
 * @author lis3wg
 */
@XmlRootElement
public class MetaEntityFilter extends BaseRootMetaData implements DefinableMetaData<EntityFilter> {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -2468076736719316702L;

	/** Entity定義のID */
	private String definitionId;

	/** Entityのフィルタ設定 */
	private List<MetaEntityFilterItem> items;

	/**
	 * Entity定義のIDを取得します。
	 * @return Entity定義のID
	 */
	public String getDefinitionId() {
		return definitionId;
	}

	/**
	 * Entity定義のIDを設定します。
	 * @param definitionId Entity定義のID
	 */
	public void setDefinitionId(String definitionId) {
		this.definitionId = definitionId;
	}

	/**
	 * Entityのフィルタ設定を取得します。
	 * @return Entityのフィルタ設定
	 */
	public List<MetaEntityFilterItem> getItems() {
		if (items == null) items = new ArrayList<MetaEntityFilterItem>();
		return items;
	}

	/**
	 * Entityのフィルタ設定を設定します。
	 * @param items Entityのフィルタ設定
	 */
	public void setItems(List<MetaEntityFilterItem> items) {
		this.items = items;
	}

	/**
	 * Entityのフィルタ設定を追加します。
	 * @param item Entityのフィルタ設定
	 */
	public void addItem(MetaEntityFilterItem item) {
		getItems().add(item);
	}

	/* (非 Javadoc)
	 * @see prot.spi.metadata.RootMetaData#createRuntime()
	 */
	@Override
	public MetaDataRuntime createRuntime(MetaDataConfig metaDataConfig) {
		return new EntityFilterHandler();
	}

	/* (非 Javadoc)
	 * @see prot.spi.metadata.RootMetaData#copy()
	 */
	@Override
	public MetaEntityFilter copy() {
		return ObjectUtil.deepCopy(this);
	}

	/**
	 * フィルタ定義の内容を自身に反映します。
	 * @param filter フィルタ定義
	 */
	public void applyConfig(EntityFilter filter) {
		if (this.id == null) {
			this.id = new KeyGenerator().generateId();
		}

		EntityContext metaContext = EntityContext.getCurrentContext();
		MetaEntity metaEntity = metaContext.getHandlerByName(filter.getDefinitionName()).getMetaData();

		this.name = filter.getName();
		this.displayName = filter.getDisplayName();
		this.description = filter.getDescription();

		this.definitionId = metaEntity.getId();
		for (EntityFilterItem item : filter.getItems()) {
			MetaEntityFilterItem mItem = new MetaEntityFilterItem();
			mItem.applyConfig(item);
			addItem(mItem);
		}
	}

	/**
	 * 自身の内容をフィルタ定義に反映します。
	 * @return フィルタ定義
	 */
	public EntityFilter currentConfig() {
		EntityContext metaContext = EntityContext.getCurrentContext();
		MetaEntity metaEntity = metaContext.getHandlerById(definitionId).getMetaData();

		EntityFilter filter = new EntityFilter();
		filter.setName(name);
		filter.setDisplayName(displayName);
		filter.setDescription(description);

		filter.setDefinitionName(metaEntity.getName());
		for (MetaEntityFilterItem mItem : getItems()) {
			filter.addItem(mItem.currentConfig());
		}
		return filter;
	}

	/**
	 * ランタイム
	 * @author lis3wg
	 */
	public class EntityFilterHandler extends BaseMetaDataRuntime {
		/* (非 Javadoc)
		 * @see prot.spi.metadata.MetaDataRuntime#getMetaData()
		 */
		@Override
		public MetaEntityFilter getMetaData() {
			return MetaEntityFilter.this;
		}
	}

}
