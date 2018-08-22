/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.web.actionmapping.cache;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.web.actionmapping.MetaActionMapping;
import org.iplass.mtp.impl.web.actionmapping.WebInvocationImpl;
import org.iplass.mtp.web.actionmapping.definition.cache.CacheCriteriaDefinition;
import org.iplass.mtp.web.actionmapping.definition.cache.CacheRelatedEntityDefinition;
import org.iplass.mtp.web.actionmapping.definition.cache.JavaClassCacheCriteriaDefinition;
import org.iplass.mtp.web.actionmapping.definition.cache.ParameterMatchCacheCriteriaDefinition;
import org.iplass.mtp.web.actionmapping.definition.cache.RelatedEntityType;
import org.iplass.mtp.web.actionmapping.definition.cache.ScriptingCacheCriteriaDefinition;

/**
 * キャッシュ基準の定義。
 *
 * @author K.Higuchi
 *
 */
@XmlSeeAlso({MetaJavaClassCacheCriteria.class, MetaParameterMatchCacheCriteria.class, MetaScriptingCacheCriteria.class})
public abstract class MetaCacheCriteria implements MetaData {
	private static final long serialVersionUID = -4394764286765456178L;

	/** キャッシュしてよいCommandのステータス。未指定、*指定の場合は、なんでもキャッシュ。 */
	private List<String> cachableCommandResultStatus;//*指定可能。未指定の場合は*と同義。
	/** このキャッシュに関連するEntityの定義。当該Entityが更新されたら、キャッシュが無効化される。 */
	private List<MetaCacheRelatedEntity> relatedEntity;
	/** キャッシュの有効期間（ms） */
	private Integer timeToLive;

	//TODO キャッシュの最大サイズ（text,bin）をServiceConfigで管理。

	public List<String> getCachableCommandResultStatus() {
		return cachableCommandResultStatus;
	}

	public void setCachableCommandResultStatus(List<String> cachableCommandResultStatus) {
		this.cachableCommandResultStatus = cachableCommandResultStatus;
	}

	public List<MetaCacheRelatedEntity> getRelatedEntity() {
		return relatedEntity;
	}

	public void setRelatedEntity(List<MetaCacheRelatedEntity> relatedEntity) {
		this.relatedEntity = relatedEntity;
	}

	public Integer getTimeToLive() {
		return timeToLive;
	}

	public void setTimeToLive(Integer timeToLive) {
		this.timeToLive = timeToLive;
	}

	//Definition → Meta インスタンス
	public static MetaCacheCriteria createInstance(CacheCriteriaDefinition definition) {
		if (definition instanceof JavaClassCacheCriteriaDefinition) {
			return new MetaJavaClassCacheCriteria();
		} else if (definition instanceof ParameterMatchCacheCriteriaDefinition) {
			return new MetaParameterMatchCacheCriteria();
		} else if (definition instanceof ScriptingCacheCriteriaDefinition) {
			return new MetaScriptingCacheCriteria();
		}
		return null;
	}

	/**
	 * Definition → Meta
	 * @param definition
	 */
	public abstract void applyConfig(CacheCriteriaDefinition definition);

	/**
	 * Meta → Definition
	 * @return Definition
	 */
	public abstract CacheCriteriaDefinition currentConfig();

	//Definition → Meta共通項目
	protected void fillFrom(CacheCriteriaDefinition definition) {
		if (definition.getCachableCommandResultStatus() != null) {
			cachableCommandResultStatus = new ArrayList<String>();
			cachableCommandResultStatus.addAll(definition.getCachableCommandResultStatus());
		} else {
			cachableCommandResultStatus = null;
		}
		if (definition.getRelatedEntity() != null) {
			relatedEntity = new ArrayList<MetaCacheRelatedEntity>();
			for (CacheRelatedEntityDefinition def : definition.getRelatedEntity()) {
				MetaCacheRelatedEntity meta = new MetaCacheRelatedEntity();
				meta.applyConfig(def);
				relatedEntity.add(meta);
			}
		} else {
			relatedEntity = null;
		}
		timeToLive = definition.getTimeToLive();
	}

	//Meta共通項目 → Definition
	protected void fillTo(CacheCriteriaDefinition definition) {
		if (cachableCommandResultStatus != null) {
			List<String> statuses = new ArrayList<String>();
			statuses.addAll(cachableCommandResultStatus);
			definition.setCachableCommandResultStatus(statuses);
		}
		if (relatedEntity != null) {
			List<CacheRelatedEntityDefinition> entities = new ArrayList<CacheRelatedEntityDefinition>();
			for (MetaCacheRelatedEntity value : relatedEntity) {
				entities.add(value.currentConfig());
			}
			definition.setRelatedEntity(entities);
		}
		definition.setTimeToLive(timeToLive);
	}

	@Override
	public MetaCacheCriteria copy() {
		return ObjectUtil.deepCopy(this);
	}

	public abstract CacheCriteriaRuntime createRuntime(MetaActionMapping actionMapping);

	public abstract class CacheCriteriaRuntime {

		private boolean isAllOkCommandStatus = false;
		private boolean isTargetAllEntity = false;
		private RelatedEntityType targetAllEntityType;

		public CacheCriteriaRuntime() {
			if (cachableCommandResultStatus == null) {
				isAllOkCommandStatus = true;
			} else {
				for (String s: cachableCommandResultStatus) {
					if ("*".equals(s)) {
						isAllOkCommandStatus = true;
						break;
					}
				}
			}
			if (relatedEntity != null) {
				for (MetaCacheRelatedEntity cre: relatedEntity) {
					if ("*".equals(cre.getDefinitionName())) {
						isTargetAllEntity = true;
						targetAllEntityType = cre.getType();
						break;
					}
				}
			}
		}
		
		public MetaCacheCriteria getMetaData() {
			return MetaCacheCriteria.this;
		}

		public abstract String createContentCacheKey(RequestContext request);
		
		public RelatedEntityType checkRelatedEntity(String entityName) {
			if (isTargetAllEntity) {
				return targetAllEntityType;
			}
			if (relatedEntity != null) {
				for (MetaCacheRelatedEntity re: relatedEntity) {
					if (entityName.equals(re.getDefinitionName())) {
						return re.getType();
					}
				}
			}
			return null;
		}

		public boolean canCache(WebInvocationImpl webInvocation) {

			if (isAllOkCommandStatus) {
				return true;
			}

			for (String s: cachableCommandResultStatus) {
				if (s.equals(webInvocation.getStatus())) {
					return true;
				}
			}

			return false;
		}


	}


}
