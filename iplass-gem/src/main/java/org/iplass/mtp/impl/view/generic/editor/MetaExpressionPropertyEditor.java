/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.ExpressionProperty;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.metadata.MetaDataRuntime;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.view.generic.EntityViewRuntime;
import org.iplass.mtp.impl.view.generic.FormViewRuntime;
import org.iplass.mtp.impl.view.generic.element.property.MetaPropertyLayout;
import org.iplass.mtp.view.generic.editor.ExpressionPropertyEditor;
import org.iplass.mtp.view.generic.editor.ExpressionPropertyEditor.ExpressionDisplayType;
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

	/** 表示タイプ */
	private ExpressionDisplayType displayType;

	/** 数値のフォーマット */
	//TODO なぜprivateじゃない？
	protected String numberFormat;

	private MetaPropertyEditor editor;

	/** Label形式の場合の登録制御 */
	private boolean insertWithLabelValue = true;

	/** Label形式の場合の更新制御 */
	private boolean updateWithLabelValue = false;

	/**
	 * 表示タイプを取得します。
	 * @return 表示タイプ
	 */
	public ExpressionDisplayType getDisplayType() {
	    return displayType;
	}

	/**
	 * 表示タイプを設定します。
	 * @param displayType 表示タイプ
	 */
	public void setDisplayType(ExpressionDisplayType displayType) {
	    this.displayType = displayType;
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

	/**
	 * 表示タイプがLabel形式の場合に、登録時に登録対象にするかを返します。
	 *
	 * @return true：登録対象
	 */
	public boolean isInsertWithLabelValue() {
		return insertWithLabelValue;
	}

	/**
	 * Label形式の場合の登録制御を設定します。
	 *
	 * @param insertWithLabelValue Label形式の場合の登録制御
	 */
	public void setInsertWithLabelValue(boolean insertWithLabelValue) {
		this.insertWithLabelValue = insertWithLabelValue;
	}

	/**
	 * 表示タイプがLabel形式の場合に、更新時に更新対象にするかを返します。
	 *
	 * @return true：更新対象
	 */
	public boolean isUpdateWithLabelValue() {
		return updateWithLabelValue;
	}

	/**
	 * Label形式の場合の更新制御を設定します。
	 *
	 * @param updateWithLabelValue Label形式の場合の更新制御
	 */
	public void setUpdateWithLabelValue(boolean updateWithLabelValue) {
		this.updateWithLabelValue = updateWithLabelValue;
	}

	@Override
	public MetaExpressionPropertyEditor copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public void applyConfig(PropertyEditor editor) {
		super.fillFrom(editor);

		ExpressionPropertyEditor pe = (ExpressionPropertyEditor) editor;
		displayType = pe.getDisplayType();
		numberFormat = pe.getNumberFormat();
		insertWithLabelValue = pe.isInsertWithLabelValue();
		updateWithLabelValue = pe.isUpdateWithLabelValue();

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

		if (displayType == null) {
			editor.setDisplayType(ExpressionDisplayType.TEXT);
		} else {
			editor.setDisplayType(displayType);
		}

		editor.setNumberFormat(numberFormat);

		if (this.editor != null) {
			//ReferencePropertyEditorになることはないのでnullを渡す
			editor.setEditor(this.editor.currentConfig(null));
		}
		editor.setInsertWithLabelValue(insertWithLabelValue);
		editor.setUpdateWithLabelValue(updateWithLabelValue);
		return editor;
	}

	@Override
	public MetaDataRuntime createRuntime(EntityViewRuntime entityView, FormViewRuntime formView,
			MetaPropertyLayout propertyLayout, EntityContext context, EntityHandler eh) {
		return new PropertyEditorRuntime(entityView, formView, propertyLayout, context, eh) {
			@Override
			protected boolean checkPropertyType(PropertyDefinition pd) {
				return pd == null || pd instanceof ExpressionProperty;
			}

		};
	}
}
