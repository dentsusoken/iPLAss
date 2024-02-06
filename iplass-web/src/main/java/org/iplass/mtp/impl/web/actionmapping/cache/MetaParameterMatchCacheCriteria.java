/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.web.actionmapping.MetaActionMapping;
import org.iplass.mtp.web.actionmapping.definition.cache.CacheCriteriaDefinition;
import org.iplass.mtp.web.actionmapping.definition.cache.ParameterMatchCacheCriteriaDefinition;

/**
 * 指定されたパラメータの値を比較し、一致したキャッシュを返すCacheCriteria。
 *
 * @author K.Higuchi
 *
 */
public class MetaParameterMatchCacheCriteria extends MetaCacheCriteria {
	private static final long serialVersionUID = -620353212482061976L;

	/** キャッシュの一致を確認する際、比較するパラメータの名前 */
	private List<String> matchingParameterName;

	public List<String> getMatchingParameterName() {
		return matchingParameterName;
	}

	public void setMatchingParameterName(List<String> matchingParameterName) {
		this.matchingParameterName = matchingParameterName;
	}

	@Override
	public void applyConfig(CacheCriteriaDefinition definition) {
		fillFrom(definition);
		ParameterMatchCacheCriteriaDefinition def = (ParameterMatchCacheCriteriaDefinition)definition;
		if (def.getMatchingParameterName() != null) {
			matchingParameterName = new ArrayList<String>();
			matchingParameterName.addAll(def.getMatchingParameterName());
		} else {
			matchingParameterName = null;
		}
	}

	@Override
	public CacheCriteriaDefinition currentConfig() {
		ParameterMatchCacheCriteriaDefinition definition = new ParameterMatchCacheCriteriaDefinition();
		fillTo(definition);
		if (matchingParameterName != null) {
			List<String> names = new ArrayList<String>();
			names.addAll(matchingParameterName);
			definition.setMatchingParameterName(names);
		}
		return definition;
	}

	@Override
	public ParameterMatchCacheCriteriaRuntime createRuntime(MetaActionMapping actionMapping) {
		return new ParameterMatchCacheCriteriaRuntime();
	}

	public class ParameterMatchCacheCriteriaRuntime extends CacheCriteriaRuntime {

		private List<String> sortedParamList;

		public ParameterMatchCacheCriteriaRuntime() {
			if (matchingParameterName != null) {
				sortedParamList = new ArrayList<String>(matchingParameterName);
				Collections.sort(sortedParamList);
			} else {
				sortedParamList = Collections.emptyList();
			}
		}

		@Override
		public String createContentCacheKey(RequestContext request) {
			StringBuilder sb = new StringBuilder();
			for (String param: sortedParamList) {
				sb.append(param);
				sb.append("=");
				String[] value = request.getParams(param);
				if (value != null && value.length > 0) {
					if (value.length == 1) {
						sb.append(value[0]);
					} else {
						//sort
						String[] copy = new String[value.length];
						System.arraycopy(value, 0, copy, 0, copy.length);
						Arrays.sort(copy);
						for (String v: copy) {
							sb.append(v);
							sb.append(":");
						}
					}
				}
				sb.append(",");
			}
			return sb.toString();
		}


	}

}
