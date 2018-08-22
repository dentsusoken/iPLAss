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

package org.iplass.mtp.auth;

import org.iplass.mtp.entity.GenericEntity;

/**
 * ユーザのランク（職位、会員種別など）を表す。
 * Entity定義上、mtp.auth.Rankで定義される。
 * 
 * 
 * @author K.Higuchi
 *
 */
public class Rank extends GenericEntity {
	private static final long serialVersionUID = -5867931536450600419L;
	
	public static final String ENTITY_DEFINITION_NAME = "mtp.auth.Rank";
	public static final String CODE = "code";
	public static final String LEVEL = "level";
	
	public Rank() {
		setDefinitionName(ENTITY_DEFINITION_NAME);
	}

	/**
	 * ランクコードを取得します。
	 *
	 * @return ランクコード
	 */
	public String getCode() {
		return getValue(CODE);
	}

	/**
	 * ランクコードを設定します。
	 *
	 * @param code
	 *            ランクコード
	 */
	public void setCode(String code) {
		setValue(CODE, code);
	}

	/**
	 * ランクの順位を取得する。
	 * @return ランクの順位
	 */
	public Long getLevel() {
		return getValue(LEVEL);
	}

	/**
	 * ランクの順位を設定する。
	 * @param level ランクの順位
	 */
	public void setLevel(Long level) {
		setValue(LEVEL, level);
	}

}
