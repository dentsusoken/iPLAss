/*
 * Copyright (C) 2021 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.gem.command.generic.detail;

import java.util.ArrayList;
import java.util.List;

/**
 * カスタム登録処理によるNestEntityの更新制御オプション
 * @author Y.Ishida
 *
 */
public class ReferenceRegistOption {
	/** 更新対象の範囲 */
	private boolean isSpecifyAllProperties;

	/** Reference項目として更新対象に指定されたか */
	private boolean specifiedAsReference;

	/** 更新対象として個別指定のあったネストプロパティ */
	private List<String> specifiedUpdateNestProperties = new ArrayList<String>();

	public boolean isSpecifyAllProperties() {
		return isSpecifyAllProperties;
	}

	public void setSpecifyAllProperties(boolean isSpecifyAllProperties) {
		this.isSpecifyAllProperties = isSpecifyAllProperties;
	}

	public boolean isSpecifiedAsReference() {
		return specifiedAsReference;
	}

	public void setSpecifiedAsReference(boolean specifiedAsReference) {
		this.specifiedAsReference = specifiedAsReference;
	}

	public List<String> getSpecifiedUpdateNestProperties() {
		return specifiedUpdateNestProperties;
	}

	public void setSpecifiedUpdateNestProperties(List<String> specifiedUpdateNestProperties) {
		this.specifiedUpdateNestProperties = specifiedUpdateNestProperties;
	}

}
