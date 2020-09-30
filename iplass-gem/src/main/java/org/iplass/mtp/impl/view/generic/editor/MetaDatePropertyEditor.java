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

import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.entity.definition.properties.DateProperty;
import org.iplass.mtp.entity.definition.properties.ExpressionProperty;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.metadata.MetaDataRuntime;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.view.generic.EntityViewRuntime;
import org.iplass.mtp.impl.view.generic.FormViewRuntime;
import org.iplass.mtp.impl.view.generic.element.property.MetaPropertyLayout;
import org.iplass.mtp.view.generic.editor.DatePropertyEditor;
import org.iplass.mtp.view.generic.editor.PropertyEditor;

/**
 * 日付型プロパティエディタのメタデータ
 * @author lis3wg
 */
public class MetaDatePropertyEditor extends MetaDateTimePropertyEditor {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 8195076567521958652L;

	public static MetaDatePropertyEditor createInstance(PropertyEditor editor) {
		return new MetaDatePropertyEditor();
	}

	/** 現在日付設定ボタン表示可否 */
	private boolean hideButtonPanel;

	/** 曜日を表示 */
	private boolean showWeekday;

	/**
	 * 現在日付設定ボタン表示可否を取得します。
	 * @return 現在日付設定ボタン表示可否
	 */
	public boolean isHideButtonPanel() {
	    return hideButtonPanel;
	}

	/**
	 * @return showWeekday
	 */
	public boolean isShowWeekday() {
		return showWeekday;
	}

	/**
	 * @param showWeekday セットする showWeekday
	 */
	public void setShowWeekday(boolean showWeekday) {
		this.showWeekday = showWeekday;
	}

	/**
	 * 現在日付設定ボタン表示可否を設定します。
	 * @param hideButtonPanel 現在日付設定ボタン表示可否
	 */
	public void setHideButtonPanel(boolean hideButtonPanel) {
	    this.hideButtonPanel = hideButtonPanel;
	}

	@Override
	public void applyConfig(PropertyEditor editor) {
		super.fillFrom(editor);

		DatePropertyEditor de = (DatePropertyEditor) editor;
		this.hideButtonPanel = de.isHideButtonPanel();
		this.showWeekday = de.isShowWeekday();
	}

	@Override
	public PropertyEditor currentConfig(String propertyName) {
		DatePropertyEditor editor = new DatePropertyEditor();
		super.fillTo(editor);

		editor.setHideButtonPanel(hideButtonPanel);
		editor.setShowWeekday(showWeekday);
		return editor;
	}

	@Override
	public MetaDatePropertyEditor copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public MetaDataRuntime createRuntime(EntityViewRuntime entityView, FormViewRuntime formView,
			MetaPropertyLayout propertyLayout, EntityContext context, EntityHandler eh) {
		return new PropertyEditorRuntime(entityView, formView, propertyLayout, context, eh) {
			@Override
			protected boolean checkPropertyType(PropertyDefinition pd) {
				if (pd == null) {
					return true;
				}
				if (pd instanceof DateProperty) {
					return true;
				}
				if (pd instanceof ExpressionProperty) {
					ExpressionProperty ep = (ExpressionProperty)pd;
					if (ep.getResultType() == PropertyDefinitionType.DATE) {
						return true;
					}
				}
				return false;
			}

		};
	}
}
