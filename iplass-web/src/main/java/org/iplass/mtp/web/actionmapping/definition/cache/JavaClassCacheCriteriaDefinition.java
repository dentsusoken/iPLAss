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

package org.iplass.mtp.web.actionmapping.definition.cache;

import org.iplass.mtp.web.actionmapping.ActionCacheCriteria;

/**
 * 
 * {@link ActionCacheCriteria}の実装クラス名を指定する。
 * 
 * @author K.Higuchi
 *
 */
public class JavaClassCacheCriteriaDefinition extends CacheCriteriaDefinition {

	private static final long serialVersionUID = 7102926695133618308L;

	/** 判定ロジックを実装するjavaクラス。ActionCacheCriteriaを実装する。*/
	private String className;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public String summaryInfo() {
		return "Java ClassName = " + className;
	}
}
