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

package org.iplass.mtp.web.actionmapping.definition.cache;

import java.util.List;

/**
 * クライアントからのリクエストのRequestContextのパラメータ名を指定（複数指定可）する。
 * 
 * @author K.Higuchi
 *
 */
public class ParameterMatchCacheCriteriaDefinition extends CacheCriteriaDefinition {

	private static final long serialVersionUID = 5145085472178880346L;

	/** キャッシュの一致を確認する際、比較するパラメータの名前 */
	private List<String> matchingParameterName;

	public List<String> getMatchingParameterName() {
		return matchingParameterName;
	}

	public void setMatchingParameterName(List<String> matchingParameterName) {
		this.matchingParameterName = matchingParameterName;
	}

	@Override
	public String summaryInfo() {
		StringBuilder builder = new StringBuilder();
		if (matchingParameterName != null && !matchingParameterName.isEmpty()) {
			for (String name : matchingParameterName) {
				builder.append(name + ",");
			}
			builder.deleteCharAt(builder.length() - 1);
		} else {
			builder.append("empty");
		}
		return "Parameter Name = " + builder.toString();
	}
}
