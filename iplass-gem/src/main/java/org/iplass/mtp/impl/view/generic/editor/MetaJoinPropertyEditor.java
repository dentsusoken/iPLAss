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

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.entity.property.ReferencePropertyHandler;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.view.generic.HasMetaNestProperty;
import org.iplass.mtp.view.generic.editor.JoinPropertyEditor;
import org.iplass.mtp.view.generic.editor.NestProperty;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor;

public class MetaJoinPropertyEditor extends MetaCustomPropertyEditor implements HasMetaNestProperty {

	/** SerialVersionUID */
	private static final long serialVersionUID = 7750500799495855836L;

	public static MetaJoinPropertyEditor createInstance(PropertyEditor editor) {
		return new MetaJoinPropertyEditor();
	}

	/** オブジェクトID */
	private String objectId;

	/** フォーマット */
	private String format;

	/** プロパティエディタ */
	private MetaPropertyEditor editor;

	/** プロパティ */
	private List<MetaNestProperty> properties;

	/** ネストプロパティの検証エラーメッセージをまとめて表示 */
	private boolean showNestPropertyErrors;

	/**
	 * オブジェクトIDを取得します。
	 * @return オブジェクトID
	 */
	public String getObjectId() {
	    return objectId;
	}

	/**
	 * オブジェクトIDを設定します。
	 * @param objectId オブジェクトID
	 */
	public void setObjectId(String objectId) {
	    this.objectId = objectId;
	}

	/**
	 * フォーマットを取得します。
	 * @return フォーマット
	 */
	public String getFormat() {
	    return format;
	}

	/**
	 * フォーマットを設定します。
	 * @param format フォーマット
	 */
	public void setFormat(String format) {
	    this.format = format;
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
	 * プロパティを取得します。
	 * @return プロパティ
	 */
	public List<MetaNestProperty> getProperties() {
		if (properties == null) properties = new ArrayList<MetaNestProperty>();
	    return properties;
	}

	/**
	 * プロパティを設定します。
	 * @param properties プロパティ
	 */
	public void setProperties(List<MetaNestProperty> properties) {
	    this.properties = properties;
	}

	public void addProperty(MetaNestProperty property) {
		getProperties().add(property);
	}

	@Override
	public List<MetaNestProperty> getNestProperties() {
		return getProperties();
	}

	public boolean isShowNestPropertyErrors() {
		return showNestPropertyErrors;
	}

	public void setShowNestPropertyErrors(boolean showNestPropertyErrors) {
		this.showNestPropertyErrors = showNestPropertyErrors;
	}

	@Override
	public void applyConfig(PropertyEditor _editor) {
		super.fillFrom(_editor);

		JoinPropertyEditor e = (JoinPropertyEditor) _editor;

		EntityContext metaContext = EntityContext.getCurrentContext();
		EntityHandler entity = metaContext.getHandlerByName(e.getObjectName());

		objectId = entity.getMetaData().getId();
		format = e.getFormat();
		showNestPropertyErrors = e.isShowNestPropertyErrors();
		editor = MetaPropertyEditor.createInstance(e.getEditor());
		if (e.getEditor() != null) {
			fillCustomPropertyEditor(e.getEditor(), e.getPropertyName(), metaContext, entity);
			editor.applyConfig(e.getEditor());
		}
		for (NestProperty nest : e.getProperties()) {
			MetaNestProperty mnp = new MetaNestProperty();
			mnp.applyConfig(nest, entity, null);
			if (mnp.getPropertyId() != null) addProperty(mnp);
		}
	}

	private void fillCustomPropertyEditor(PropertyEditor editor, String propName, EntityContext context, EntityHandler entity) {
		PropertyHandler ph = entity.getProperty(propName, context);
		if (ph == null) return;

		if (editor instanceof ReferencePropertyEditor) {
			if (ph instanceof ReferencePropertyHandler) {
				String objName = ((ReferencePropertyHandler) ph).getReferenceEntityHandler(context).getMetaData().getName();
				((ReferencePropertyEditor) editor).setObjectName(objName);
			}
		}
	}

	@Override
	public PropertyEditor currentConfig(String propertyName) {
		//対象Entityの存在チェック
		EntityContext metaContext = EntityContext.getCurrentContext();
		EntityHandler entity = metaContext.getHandlerById(objectId);
		if (entity == null) {
			return null;
		}
		JoinPropertyEditor _editor = new JoinPropertyEditor();
		super.fillTo(_editor);

		_editor.setObjectName(entity.getMetaData().getName());
		_editor.setFormat(format);
		_editor.setShowNestPropertyErrors(showNestPropertyErrors);
		if (editor != null) {
			_editor.setEditor(editor.currentConfig(propertyName));
		}
		for (MetaNestProperty nest : getProperties()) {
			NestProperty np = nest.currentConfig(entity, null);
			if (np != null && np.getPropertyName() != null) _editor.addProperty(np);
		}
		return _editor;
	}

	@Override
	public MetaJoinPropertyEditor copy() {
		return ObjectUtil.deepCopy(this);
	}

}
