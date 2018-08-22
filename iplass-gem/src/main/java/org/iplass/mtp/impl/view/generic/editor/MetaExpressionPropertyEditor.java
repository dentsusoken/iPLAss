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

package org.iplass.mtp.impl.view.generic.editor;

import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.view.generic.editor.ExpressionPropertyEditor;
import org.iplass.mtp.view.generic.editor.PropertyEditor;

/**
 * 数式型プロパティエディタのメタデータ
 * @author lis3wg
 */
public class MetaExpressionPropertyEditor extends MetaPrimitivePropertyEditor {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -8772842045559089141L;

	public static MetaExpressionPropertyEditor createInstance(PropertyEditor editor) {
		return new MetaExpressionPropertyEditor();
	}

	/** 数値のフォーマット */
	protected String numberFormat;

	private MetaPropertyEditor editor;

	@Override
	public MetaExpressionPropertyEditor copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public void applyConfig(PropertyEditor editor) {
		super.fillFrom(editor);

		ExpressionPropertyEditor pe = (ExpressionPropertyEditor) editor;
		numberFormat = pe.getNumberFormat();

		MetaPropertyEditor me = MetaPropertyEditor.createInstance(pe.getEditor());
		if (me != null) {
			pe.getEditor().setPropertyName(pe.getPropertyName());
			me.applyConfig(pe.getEditor());
			this.editor = me;
		}
	}

	@Override
	public PropertyEditor currentConfig(String propertyName) {
		ExpressionPropertyEditor editor = new ExpressionPropertyEditor();
		super.fillTo(editor);

		editor.setNumberFormat(numberFormat);

		if (this.editor != null) {
			//ReferencePropertyEditorになることはないのでnullを渡す
			editor.setEditor(this.editor.currentConfig(null));
		}
		return editor;
	}

	/**
	 * 数値のフォーマットを取得します。
	 * @return 数値のフォーマット
	 */
	public String getNumberFormat() {
		return numberFormat;
	}

	/**
	 * 数値のフォーマットを設定します。
	 * @param numberFormat 数値のフォーマット
	 */
	public void setNumberFormat(String numberFormat) {
		this.numberFormat = numberFormat;
	}

	/**
	 * プロパティエディタを取得します。
	 * @return プロパティエディタ
	 */
	public MetaPropertyEditor getEditor() {
		return editor;
	}

	/**
	 * プロパティエディタを設定します。
	 * @param editor プロパティエディタ
	 */
	public void setEditor(MetaPropertyEditor editor) {
		this.editor = editor;
	}

}
