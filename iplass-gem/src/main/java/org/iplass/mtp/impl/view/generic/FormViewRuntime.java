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

package org.iplass.mtp.impl.view.generic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.impl.script.template.GroovyTemplate;
import org.iplass.mtp.impl.script.template.GroovyTemplateCompiler;
import org.iplass.mtp.impl.view.generic.element.MetaButton;
import org.iplass.mtp.impl.view.generic.element.MetaButton.ButtonRuntime;
import org.iplass.mtp.impl.view.generic.element.section.MetaSection;
import org.iplass.mtp.impl.view.generic.element.section.SectionRuntime;

/**
 * レイアウト情報のランタイム
 * @author lis3wg
 */
public class FormViewRuntime {

	/** メタデータ */
	private MetaFormView metaData;

	/** セクション */
	private List<SectionRuntime> sections;

	/**
	 * コンストラクタ
	 * @param metaData レイアウト情報
	 * @param entityView 画面定義のランタイム
	 */
	public FormViewRuntime(MetaFormView metaData, EntityViewRuntime entityView) {
		this.metaData = metaData;
		sections = new ArrayList<>();
		for (MetaSection section : metaData.getSections()) {
			sections.add(section.createRuntime(entityView));
		}

		Map<String, GroovyTemplate> customStyleMap = new HashMap<>();
		for (MetaButton button : metaData.getButtons()) {
			ButtonRuntime runtime = button.createRuntime(entityView);
			customStyleMap.put(button.getInputCustomStyleScriptKey(), runtime.getInputCustomStyleScript());
		}

		//Script用のKEYを設定
		metaData.scriptKey = "FormView_ButtonStyle_" + GroovyTemplateCompiler.randomName().replace("-", "_");

		//EntityViewに登録
		entityView.addCustomStyle(metaData.scriptKey , customStyleMap);
	}

	/**
	 * メタデータを取得します。
	 * @return メタデータ
	 */
	public MetaFormView getMetaData() {
		return metaData;
	}

}
