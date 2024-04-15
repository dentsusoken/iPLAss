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

package org.iplass.mtp.impl.view.generic.element.property;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.entity.property.ReferencePropertyHandler;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.i18n.MetaLocalizedString;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.view.generic.EntityViewRuntime;
import org.iplass.mtp.impl.view.generic.FormViewRuntime;
import org.iplass.mtp.impl.view.generic.HasEntityProperty;
import org.iplass.mtp.impl.view.generic.common.MetaAutocompletionSetting;
import org.iplass.mtp.impl.view.generic.common.MetaAutocompletionSetting.AutocompletionSettingRuntime;
import org.iplass.mtp.impl.view.generic.editor.MetaPropertyEditor;
import org.iplass.mtp.impl.view.generic.element.ElementRuntime;
import org.iplass.mtp.impl.view.generic.element.MetaElement;
import org.iplass.mtp.view.generic.editor.DateRangePropertyEditor;
import org.iplass.mtp.view.generic.editor.JoinPropertyEditor;
import org.iplass.mtp.view.generic.editor.NumericRangePropertyEditor;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor;
import org.iplass.mtp.view.generic.element.Element;
import org.iplass.mtp.view.generic.element.property.PropertyBase;
import org.iplass.mtp.view.generic.element.property.PropertyColumn;
import org.iplass.mtp.view.generic.element.property.PropertyItem;

/**
 * プロパティレイアウト情報のメタデータ
 * @author lis3wg
 */
@XmlSeeAlso({ MetaPropertyItem.class, MetaPropertyColumn.class })
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class MetaPropertyLayout extends MetaElement implements HasEntityProperty {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1940139489715422445L;

	public static MetaPropertyLayout createInstance(Element element) {
		if (element instanceof PropertyItem) {
			return new MetaPropertyItem();
		} else if (element instanceof PropertyColumn) {
			return new MetaPropertyColumn();
		}
		return null;
	}

	/** プロパティID */
	private String propertyId;

	/** 画面表示時のラベル */
	private String displayLabel;

	/** 多言語設定情報 */
	private List<MetaLocalizedString> localizedDisplayLabelList = new ArrayList<>();

	/** クラス名 */
	private String style;

	/** プロパティエディタ */
	private MetaPropertyEditor editor;

	/** 自動補完設定 */
	private MetaAutocompletionSetting autocompletionSetting;

	/**
	 * プロパティIDを取得します。
	 * @return プロパティID
	 */
	public String getPropertyId() {
		return propertyId;
	}

	/**
	 * プロパティIDを設定します。
	 * @param propertyId プロパティID
	 */
	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	/**
	 * 画面表示時のラベルを取得します。
	 * @return 画面表示時のラベル
	 */
	public String getDisplayLabel() {
		return displayLabel;
	}

	/**
	 * 画面表示時のラベルを設定します。
	 * @param displayLabel 画面表示時のラベル
	 */
	public void setDisplayLabel(String displayLabel) {
		this.displayLabel = displayLabel;
	}

	/**
	 * クラス名を取得します。
	 * @return クラス名
	 */
	public String getStyle() {
		return style;
	}

	/**
	 * クラス名を設定します。
	 * @param style クラス名
	 */
	public void setStyle(String style) {
		this.style = style;
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
	 * 自動補完設定を取得します。
	 * @return setting 自動補完設定
	 */
	public MetaAutocompletionSetting getAutocompletionSetting() {
		return autocompletionSetting;
	}

	/**
	 * 自動補完設定を設定します。
	 * @param autocompletionSetting 自動補完設定
	 */
	public void setAutocompletionSetting(MetaAutocompletionSetting autocompletionSetting) {
		this.autocompletionSetting = autocompletionSetting;
	}

	/**
	 * 多言語設定情報を取得します。
	 * @return リスト
	 */
	public List<MetaLocalizedString> getLocalizedDisplayLabelList() {
		return localizedDisplayLabelList;
	}

	/**
	 * 多言語設定情報を設定します。
	 * @param リスト
	 */
	public void setLocalizedDisplayLabelList(List<MetaLocalizedString> localizedDisplayLabelList) {
		this.localizedDisplayLabelList = localizedDisplayLabelList;
	}


	@Override
	public MetaPropertyLayout copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	protected void fillFrom(Element element, String definitionId) {
		super.fillFrom(element, definitionId);

		PropertyBase p = (PropertyBase) element;

		//名前からEntityの定義を取得してIDをセット
		EntityContext context = EntityContext.getCurrentContext();
		EntityHandler entity = context.getHandlerById(definitionId);
//		PropertyHandler handler = entity.getProperty(p.getPropertyName(), metaContext);
//		if (handler != null) {
//			this.propertyId = handler.getId();
//		}
		//.区切りのネストプロパティに対応
		String id = convertId(p.getPropertyName(), context, entity);
		if (id != null) {
			this.propertyId = id;
		}

		this.style = p.getStyle();
		this.displayLabel = p.getDisplayLabel();

		MetaPropertyEditor editor = MetaPropertyEditor.createInstance(p.getEditor());

		fillCustomPropertyEditor(p.getEditor(), p.getPropertyName(), context, entity);

		if (editor != null) {
			p.getEditor().setPropertyName(p.getPropertyName());
			editor.applyConfig(p.getEditor());
			this.editor = editor;
		}

		if (p.getAutocompletionSetting() != null) {
			autocompletionSetting = MetaAutocompletionSetting.createInstance(p.getAutocompletionSetting());
			autocompletionSetting.applyConfig(p.getAutocompletionSetting(), entity, null);
		}

		// 言語毎の文字情報設定
		localizedDisplayLabelList = I18nUtil.toMeta(p.getLocalizedDisplayLabelList());

	}

	protected void fillCustomPropertyEditor(PropertyEditor pe, String propName, EntityContext context, EntityHandler entity) {
		if (pe instanceof JoinPropertyEditor) {
			((JoinPropertyEditor) pe).setObjectName(entity.getMetaData().getName());
		} else if (pe instanceof DateRangePropertyEditor) {
				((DateRangePropertyEditor) pe).setObjectName(entity.getMetaData().getName());
		} else if (pe instanceof NumericRangePropertyEditor) {
			((NumericRangePropertyEditor) pe).setObjectName(entity.getMetaData().getName());
		} else if (pe instanceof ReferencePropertyEditor) {
			ReferencePropertyEditor rpe = (ReferencePropertyEditor)pe;
			//参照Entity情報を取得してEditorにEntity名をセット
			PropertyHandler handler = getHandler(propName, context, entity);
			if (handler != null && handler instanceof ReferencePropertyHandler) {
				String objName = ((ReferencePropertyHandler) handler).getReferenceEntityHandler(context).getMetaData().getName();
				rpe.setObjectName(objName);
			}
			//Editorに参照元Entity名をセット
			rpe.setReferenceFromObjectName(entity.getMetaData().getName());
		}
	}

	private PropertyHandler getHandler(String propName, EntityContext context, EntityHandler entity) {
		if (propName == null || propName.isEmpty()) {
			return null;
		}
		if (propName.contains(".")) {
			int indexOfDot = propName.indexOf('.');
			String objPropName = propName.substring(0, indexOfDot);
			String subPropPath = propName.substring(indexOfDot + 1, propName.length());

			PropertyHandler property = entity.getProperty(objPropName, context);
			if (!(property instanceof ReferencePropertyHandler)) {
				throw new IllegalArgumentException("path is invalid:" + objPropName + " is not ObjectReferenceProperty of " + entity.getMetaData().getName());
			}
			ReferencePropertyHandler refProp = (ReferencePropertyHandler) property;
			EntityHandler refEntity = refProp.getReferenceEntityHandler(context);
			if (refEntity == null) {
				throw new IllegalArgumentException(objPropName + "'s Entity is not defined.");
			}
			PropertyHandler refProperty = getHandler(subPropPath, context, refEntity);
			if (refProperty == null) {
				throw new IllegalArgumentException(subPropPath + "'s Property is not defined.");
			}
			return refProperty;

		} else {
			PropertyHandler property = entity.getProperty(propName, context);
			if (property == null) {
				throw new IllegalArgumentException(propName + "'s Property is not defined.");
			}
			return property;
		}
	}

	@Override
	protected void fillTo(Element element, String definitionId) {
		//IDからEntityの定義を取得して名前をセット
		EntityContext metaContext = EntityContext.getCurrentContext();
		EntityHandler entity = metaContext.getHandlerById(definitionId);
//		PropertyHandler handler = entity.getPropertyById(this.propertyId, metaContext);
//		if (handler == null) {
//			//既に存在しないプロパティの場合は中断
//			return;
//		}
		//.区切りのネストプロパティに対応
		String name = convertName(this.propertyId, metaContext, entity);
		if (name == null) {
			return;
		}

		PropertyBase p = (PropertyBase) element;
		super.fillTo(p, definitionId);

		p.setStyle(this.style);
//		p.setPropertyName(handler.getName());
		p.setPropertyName(name);
		p.setDisplayLabel(this.displayLabel);
		if (this.editor != null) {
			//ReferencePropertyEditorの場合、参照先Entityが存在しないとnullが設定される。
//			p.setEditor(this.editor.currentConfig(handler.getName()));
			p.setEditor(this.editor.currentConfig(name));
		}

		if (autocompletionSetting != null) {
			p.setAutocompletionSetting(autocompletionSetting.currentConfig(entity, null));
		}

		p.setLocalizedDisplayLabelList(I18nUtil.toDef(localizedDisplayLabelList));
	}

	@Override
	public ElementRuntime createRuntime(EntityViewRuntime entityView, FormViewRuntime formView) {
		if (autocompletionSetting != null) {
			AutocompletionSettingRuntime runtime = autocompletionSetting.createRuntime(entityView);
			if (runtime != null) {
				entityView.addAutocompletionSetting(runtime);
			}
		}
		return super.createRuntime(entityView, formView);
	}
}
