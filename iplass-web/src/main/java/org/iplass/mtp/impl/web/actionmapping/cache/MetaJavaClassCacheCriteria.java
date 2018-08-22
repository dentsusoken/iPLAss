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

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.metadata.MetaDataRuntimeException;
import org.iplass.mtp.impl.web.actionmapping.MetaActionMapping;
import org.iplass.mtp.web.actionmapping.ActionCacheCriteria;
import org.iplass.mtp.web.actionmapping.definition.cache.CacheCriteriaDefinition;
import org.iplass.mtp.web.actionmapping.definition.cache.JavaClassCacheCriteriaDefinition;

/**
 * キャッシュの一致判定ロジックをjavaのクラスで実装するCacheCriteria。
 *
 * @author K.Higuchi
 *
 */
public class MetaJavaClassCacheCriteria extends MetaCacheCriteria {
	private static final long serialVersionUID = -5025406247037005563L;

	/** 判定ロジックを実装するjavaクラス。ActionCacheCriteriaを実装する。*/
	private String className;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public void applyConfig(CacheCriteriaDefinition definition) {
		fillFrom(definition);
		JavaClassCacheCriteriaDefinition def = (JavaClassCacheCriteriaDefinition)definition;
		className = def.getClassName();
	}

	@Override
	public CacheCriteriaDefinition currentConfig() {
		JavaClassCacheCriteriaDefinition definition = new JavaClassCacheCriteriaDefinition();
		fillTo(definition);
		definition.setClassName(className);
		return definition;
	}

	@Override
	public JavaClassCacheCriteriaRuntime createRuntime(MetaActionMapping actionMapping) {
		return new JavaClassCacheCriteriaRuntime();
	}
	
	public class JavaClassCacheCriteriaRuntime extends CacheCriteriaRuntime {

		private ActionCacheCriteria actionCacheCriteria;

		public JavaClassCacheCriteriaRuntime() {
			try {
				actionCacheCriteria = (ActionCacheCriteria) Class.forName(className).newInstance();
			} catch (InstantiationException e) {
				throw new MetaDataRuntimeException("can not instantiate " + className, e);
			} catch (IllegalAccessException e) {
				throw new MetaDataRuntimeException("can not instantiate " + className, e);
			} catch (ClassNotFoundException e) {
				throw new MetaDataRuntimeException("class not found:" + className, e);
			}
		}

		@Override
		public String createContentCacheKey(RequestContext request) {
			return actionCacheCriteria.createCacheKey(request);
		}

	}

}
