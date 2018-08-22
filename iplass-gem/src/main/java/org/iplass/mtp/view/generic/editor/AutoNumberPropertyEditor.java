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

package org.iplass.mtp.view.generic.editor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlEnumValue;

import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.adminconsole.view.annotation.generic.EntityViewField;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.view.generic.Jsp;
import org.iplass.mtp.view.generic.Jsps;
import org.iplass.mtp.view.generic.ViewConst;

/**
 * AutoNumber型プロパティエディタ
 * @author Y.Kazama
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Jsps({
	@Jsp(path="/jsp/gem/generic/editor/AutoNumberPropertyEditor.jsp", key=ViewConst.DESIGN_TYPE_GEM),
	@Jsp(path="/jsp/gem/aggregation/unit/editor/AutoNumberPropertyEditor.jsp", key=ViewConst.DESIGN_TYPE_GEM_AGGREGATION)
})
public class AutoNumberPropertyEditor extends PrimitivePropertyEditor {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 6809027984079078798L;

	/** 表示タイプ */
	public enum AutoNumberDisplayType {
		@XmlEnumValue("Label")LABEL
	}

	/** 検索条件完全一致設定 */
	@MetaFieldInfo(
			displayName="検索完全一致設定",
			displayNameKey="generic_editor_AutoNumberPropertyEditor_searchExactMatchConditionDisplaNameKey",
			description="チェック時は完全一致検索します。<br>未チェック時はLike検索します。",
			inputType=InputType.CHECKBOX,
			descriptionKey="generic_editor_AutoNumberPropertyEditor_searchExactMatchConditionDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION}
	)
	private boolean searchExactMatchCondition;

	/**
	 * デフォルトコンストラクタ
	 */
	public AutoNumberPropertyEditor() {
	}

	@Override
	public AutoNumberDisplayType getDisplayType() {
		return AutoNumberDisplayType.LABEL;
	}

	@Override
	public String getDefaultValue() {
		// デフォルト値なし、空実装
		return null;
	}

	@Override
	public void setDefaultValue(String defaultValue) {
		// デフォルト値なし、空実装
	}

	/**
	 * 検索条件完全一致を設定します。
	 *
	 * @return searchExactMatchCondition 検索条件完全一致設定
	 */
	public boolean isSearchExactMatchCondition() {
		return searchExactMatchCondition;
	}

	/**
	 * 検索条件完全一致設定を取得します。
	 *
	 * @param 検索条件完全一致設定
	 */
	public void setSearchExactMatchCondition(boolean searchExactMatchCondition) {
		this.searchExactMatchCondition = searchExactMatchCondition;
	}
}
