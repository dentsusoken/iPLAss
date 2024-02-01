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

package org.iplass.mtp.impl.webapi;

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
import org.iplass.mtp.webapi.definition.EntityWebApiDefinition;

/**
 * EntityWebApi定義
 * @author Y.Fukuda
 */
@XmlRootElement
public class MetaEntityWebApi extends BaseRootMetaData implements DefinableMetaData<EntityWebApiDefinition> {
	private static final long serialVersionUID = -5517435548233442201L;

	/** Entity定義のID */
	private String definitionId;

	/** エンティティデータの作成可否 */
	private boolean isInsert;

	/** エンティティデータの読込可否（Load） */
	private boolean isLoad;

	/** エンティティデータの読込可否（Query） */
	private boolean isQuery;

	/** エンティティデータの更新可否 */
	private boolean isUpdate;

	/** エンティティデータの削除可否 */
	private boolean isDelete;

	public boolean isInsert() {
		return isInsert;
	}

	public void setInsert(boolean isInsert) {
		this.isInsert = isInsert;
	}

	public boolean isLoad() {
		return isLoad;
	}

	public void setLoad(boolean isLoad) {
		this.isLoad = isLoad;
	}

	public boolean isQuery() {
		return isQuery;
	}

	public void setQuery(boolean isQuery) {
		this.isQuery = isQuery;
	}

	public boolean isUpdate() {
		return isUpdate;
	}

	public void setUpdate(boolean isUpdate) {
		this.isUpdate = isUpdate;
	}

	public boolean isDelete() {
		return isDelete;
	}

	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

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

	/* (非 Javadoc)
	 * @see prot.spi.metadata.RootMetaData#createRuntime()
	 */
	@Override
	public MetaDataRuntime createRuntime(MetaDataConfig metaDataConfig) {
		return new EntityWebApiHandler();
	}

	/* (非 Javadoc)
	 * @see prot.spi.metadata.RootMetaData#copy()
	 */
	@Override
	public MetaEntityWebApi copy() {
		return ObjectUtil.deepCopy(this);
	}

	/**
	 * EntityWebApi定義の内容を自身に反映します。
	 * @param definition EntityWebApi定義
	 */
	public void applyConfig(EntityWebApiDefinition definition) {
		if (this.id == null) {
			this.id = new KeyGenerator().generateId();
		}

		EntityContext metaContext = EntityContext.getCurrentContext();
		MetaEntity metaEntity = metaContext.getHandlerByName(definition.getName()).getMetaData();

		this.name = metaEntity.getName();
		this.definitionId = metaEntity.getId();
		this.isInsert = definition.isInsert();
		this.isLoad = definition.isLoad();
		this.isQuery = definition.isQuery();
		this.isUpdate = definition.isUpdate();
		this.isDelete = definition.isDelete();
	}

	/**
	 * 自身の内容をEntityWebApi定義に反映します。
	 * @return EntityWebApi定義
	 */
	public EntityWebApiDefinition currentConfig() {
		EntityContext metaContext = EntityContext.getCurrentContext();
		MetaEntity metaEntity = metaContext.getHandlerById(definitionId).getMetaData();

		EntityWebApiDefinition definition = new EntityWebApiDefinition();
		definition.setName(metaEntity.getName());
		definition.setInsert(isInsert);
		definition.setLoad(isLoad);
		definition.setQuery(isQuery);
		definition.setUpdate(isUpdate);
		definition.setDelete(isDelete);
		return definition;
	}

	/**
	 * ランタイム
	 * @author lis7gv
	 */
	public class EntityWebApiHandler extends BaseMetaDataRuntime {

		EntityWebApiHandler() {
		}

		/* (非 Javadoc)
		 * @see prot.spi.metadata.MetaDataRuntime#getMetaData()
		 */
		@Override
		public MetaEntityWebApi getMetaData() {
			return MetaEntityWebApi.this;
		}
	}

}
