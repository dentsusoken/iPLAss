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

package org.iplass.mtp.view.top;

import org.iplass.mtp.definition.DefinitionModifyResult;

/**
 * 更新結果
 * @author lis3wg
 */
@Deprecated
public class TopViewDefinitionModifyResult extends DefinitionModifyResult {

	private static final long serialVersionUID = -5602096746705902L;

	/**
	 * コンストラクタ
	 *
	 * @param isSuccess 結果
	 */
	public TopViewDefinitionModifyResult(boolean isSuccess) {
		this(isSuccess, null);
	}

	/**
	 * コンストラクタ
	 *
	 * @param isSuccess 結果
	 * @param message メッセージ
	 */
	public TopViewDefinitionModifyResult(boolean isSuccess, String message) {
		super(isSuccess, message);
	}
}
