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
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.metadata.MetaDataRuntime;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.view.generic.EntityViewRuntime;
import org.iplass.mtp.impl.view.generic.FormViewRuntime;
import org.iplass.mtp.impl.view.generic.element.property.MetaPropertyLayout;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.TemplatePropertyEditor;
import org.iplass.mtp.view.generic.editor.TemplatePropertyEditor.TemplateDisplayType;

/**
 * テンプレートを表示するプロパティエディタのメタデータ
 * @author lis3wg
 */
public class MetaTemplatePropertyEditor extends MetaCustomPropertyEditor {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -5453413698796458443L;

	public static MetaTemplatePropertyEditor createInstance(PropertyEditor editor) {
		return new MetaTemplatePropertyEditor();
	}

	/** 表示タイプ */
	private TemplateDisplayType displayType;

	/** テンプレート名 */
	private String templateName;

	/**
	 * 表示タイプを取得します。
	 * @return 表示タイプ
	 */
	public TemplateDisplayType getDisplayType() {
	    return displayType;
	}

	/**
	 * 表示タイプを設定します。
	 * @param displayType 表示タイプ
	 */
	public void setDisplayType(TemplateDisplayType displayType) {
	    this.displayType = displayType;
	}

	/**
	 * テンプレート名を取得します。
	 * @return テンプレート名
	 */
	public String getTemplateName() {
		return templateName;
	}

	/**
	 * テンプレート名を設定します。
	 * @param templateName テンプレート名
	 */
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	@Override
	public void applyConfig(PropertyEditor editor) {
		super.fillFrom(editor);

		TemplatePropertyEditor e = (TemplatePropertyEditor) editor;
		displayType = e.getDisplayType();
		templateName = e.getTemplateName();
	}

	@Override
	public PropertyEditor currentConfig(String propertyName) {
		TemplatePropertyEditor editor = new TemplatePropertyEditor();
		super.fillTo(editor);

		if (displayType == null) {
			editor.setDisplayType(TemplateDisplayType.TEMPLATE);
		} else {
			editor.setDisplayType(displayType);
		}
		editor.setTemplateName(templateName);
		return editor;
	}

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public MetaDataRuntime createRuntime(EntityViewRuntime entityView, FormViewRuntime formView,
			MetaPropertyLayout propertyLayout, EntityContext context, EntityHandler eh) {
		return new PropertyEditorRuntime(entityView, formView, propertyLayout, context, eh) {
			@Override
			protected boolean checkPropertyType(PropertyDefinition pd) {
				return true;
			}
		};
	}

}
