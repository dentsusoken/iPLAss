/*
 * Copyright (C) 2023 DENTSU SOKEN INC. All Rights Reserved.
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

import org.iplass.mtp.impl.view.generic.editor.MetaPropertyEditor.PropertyEditorRuntime;
import org.iplass.mtp.impl.view.generic.element.ElementRuntime;

/**
 * セクションプロパティランタイム
 *
 * <p>
 * 本クラスでは以下の情報を管理する。
 * <ul>
 * <li>プロパティ名</li>
 * <li>FormView要素ランタイム</li>
 * <li>プロパティエディタランタイム</li>
 * </p>
 *
 * <p>package private</p>
 *
 * @author SEKIGUCHI Naoya
 */
class SectionPropertyRuntimeImpl implements SectionPropertyRuntime {
	/** プロパティ名*/
	private String propertyName;
	/** FormView要素ランタイム */
	private ElementRuntime element;
	/** プロパティエディタランタイム */
	private PropertyEditorRuntime editor;

	/**
	 * コンストラクタ
	 * @param propertyName プロパティ名
	 */
	public SectionPropertyRuntimeImpl(String propertyName) {
		this.propertyName = propertyName;
	}

	@Override
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * FormView要素ランタイムを取得する
	 * @return FormView要素ランタイム
	 */
	public ElementRuntime getElement() {
		return element;
	}

	/**
	 * FormView要素ランタイムを設定する
	 * @param element FormView要素ランタイム
	 */
	void setElement(ElementRuntime element) {
		this.element = element;
	}

	@Override
	public PropertyEditorRuntime getEditor() {
		return editor;
	}

	/**
	 * プロパティエディタランタイムを設定する
	 * @param editor プロパティエディタランタイム
	 */
	void setEditor(PropertyEditorRuntime editor) {
		this.editor = editor;
	}
}
