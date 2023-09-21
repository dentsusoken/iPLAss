/*
 * Copyright (C) 2023 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.view.generic.element.section;

import java.util.Map;

import org.iplass.mtp.impl.view.generic.editor.MetaPropertyEditor.PropertyEditorRuntime;
import org.iplass.mtp.impl.view.generic.element.ElementRuntime;

/**
 * セクションプロパティランタイムビルダー
 *
 * <p>package private</p>
 *
 * @author SEKIGUCHI Naoya
 */
class SectionPropertyRuntimeBuilder {
	/** プロパティ名 */
	private String propertyName;
	/** プロパティエディタランタイム */
	private PropertyEditorRuntime editor;
	/** FormView要素ランタイム */
	private ElementRuntime element;
	/** ネストプロパティ*/
	private Map<String, SectionPropertyRuntime> nest;

	/**
	 * コンストラクタ
	 * @param propertyName プロパティ名
	 */
	public SectionPropertyRuntimeBuilder(String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * プロパティエディタランタイムを設定する
	 * @param editor プロパティエディタランタイム
	 * @return 自インスタンス
	 */
	public SectionPropertyRuntimeBuilder editor(PropertyEditorRuntime editor) {
		this.editor = editor;
		return this;
	}

	/**
	 * FormView要素ランタイムを設定する
	 * @param element FormView要素ランタイム
	 * @return 自インスタンス
	 */
	public SectionPropertyRuntimeBuilder element(ElementRuntime element) {
		this.element = element;
		return this;
	}

	/**
	 * ネストプロパティランタイムを設定する
	 * @param nest ネストプロパティランタイム
	 * @return 自インスタンス
	 */
	public SectionPropertyRuntimeBuilder nest(Map<String, SectionPropertyRuntime> nest) {
		this.nest = nest;
		return this;
	}

	/**
	 * インスタンスを生成する
	 * @return InnerPropertyRuntime インスタンス
	 */
	public SectionPropertyRuntime build() {
		// ネストプロパティランタイムの有無で生成インスタンスを分ける
		SectionPropertyRuntimeImpl property = nest != null
				? new SectionNestPropertyRuntime(propertyName, nest)
				: new SectionPropertyRuntimeImpl(propertyName);
		property.setEditor(editor);
		property.setElement(element);
		return property;
	}
}
