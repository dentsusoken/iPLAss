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

package org.iplass.mtp.impl.view.generic.element.section;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.entity.property.ReferencePropertyHandler;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.script.GroovyScriptEngine;
import org.iplass.mtp.impl.script.template.GroovyTemplate;
import org.iplass.mtp.impl.script.template.GroovyTemplateCompiler;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.view.generic.EntityViewRuntime;
import org.iplass.mtp.impl.view.generic.FormViewRuntime;
import org.iplass.mtp.impl.view.generic.editor.MetaNestProperty;
import org.iplass.mtp.impl.view.generic.editor.MetaPropertyEditor;
import org.iplass.mtp.impl.view.generic.editor.MetaPropertyEditor.PropertyEditorRuntime;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.editor.NestProperty;
import org.iplass.mtp.view.generic.element.Element;
import org.iplass.mtp.view.generic.element.section.ReferenceSection;

/**
 *
 * @author lis3wg
 */
public class MetaReferenceSection extends MetaAdjustableHeightSection {

	/** SerialVersionUID */
	private static final long serialVersionUID = -3690001980373138857L;
	/** dispBorderInSectionのデフォルト設定 */
	@Deprecated
	private static boolean defaultDispBorderInSection;

	static {
		// システムプロパティorデフォルトtrueで初期化
		String value = System.getProperty("mtp.generic.dispBorderInSection", "true");
		defaultDispBorderInSection = Boolean.parseBoolean(value);
	}


	public static MetaReferenceSection createInstance(Element element) {
		return new MetaReferenceSection();
	}

	/** プロパティID */
	private String propertyId;

	/** セクションの展開可否 */
	private boolean expandable;

	/** 列数 */
	private int colNum;

	/** リンクを表示するか */
	private boolean showLink;

	/** 詳細編集非表示設定 */
	private boolean hideDetail;

	/** 詳細表示非表示設定 */
	private boolean hideView;

	/** 更新時に強制的に更新処理を行う */
	private boolean forceUpadte;

	/** 上部のコンテンツ */
	private String upperContents;

	/** 下部のコンテンツ */
	private String lowerContents;

	/** 表示プロパティ */
	private List<MetaNestProperty> properties;

	/** 表示順プロパティ */
	private String orderPropId;

	/** データのインデックス */
	private int index;

	/** セクション内に配置した場合に枠線を表示 */
	private boolean dispBorderInSection = defaultDispBorderInSection;

	/** 上下コンテンツスクリプトのキー(内部用) */
	private String contentScriptKey;

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
	 * セクションの展開可否を取得します。
	 * @return セクションの展開可否
	 */
	public boolean isExpandable() {
		return expandable;
	}

	/**
	 * セクションの展開可否を設定します。
	 * @param expandable セクションの展開可否
	 */
	public void setExpandable(boolean expandable) {
		this.expandable = expandable;
	}

	/**
	 * 列数を取得します。
	 * @return 列数
	 */
	public int getColNum() {
		return colNum;
	}

	/**
	 * 列数を設定します。
	 * @param colNum 列数
	 */
	public void setColNum(int colNum) {
		this.colNum = colNum;
	}

	/**
	 * リンクを表示するかを取得します。
	 * @return リンクを表示するか
	 */
	public boolean isShowLink() {
		return showLink;
	}

	/**
	 * リンクを表示するかを設定します。
	 * @param showLink リンクを表示するか
	 */
	public void setShowLink(boolean showLink) {
		this.showLink = showLink;
	}

	/**
	 * 詳細編集非表示設定を取得します。
	 * @return 詳細編集非表示設定
	 */
	public boolean isHideDetail() {
		return hideDetail;
	}

	/**
	 * 詳細編集非表示設定を設定します。
	 * @param hideDetail 詳細編集非表示設定
	 */
	public void setHideDetail(boolean hideDetail) {
		this.hideDetail = hideDetail;
	}

	/**
	 * 詳細表示非表示設定を取得します。
	 * @return 詳細表示非表示設定
	 */
	public boolean isHideView() {
		return hideView;
	}

	/**
	 * 詳細表示非表示設定を設定します。
	 * @param hideView 詳細表示非表示設定
	 */
	public void setHideView(boolean hideView) {
		this.hideView = hideView;
	}

	/**
	 * 更新時に強制的に更新処理を行うかを取得します。
	 * @return forceUpdate 更新時に強制的に更新処理を行うか
	 */
	public boolean isForceUpadte() {
		return forceUpadte;
	}

	/**
	 * 更新時に強制的に更新処理を行うかを設定します。
	 * @param forceUpadte 更新時に強制的に更新処理を行うか
	 */
	public void setForceUpadte(boolean forceUpadte) {
		this.forceUpadte = forceUpadte;
	}
	/**
	 * 上部のコンテンツを取得します。
	 * @return 上部のコンテンツ
	 */
	public String getUpperContents() {
		return upperContents;
	}

	/**
	 * 上部のコンテンツを設定します。
	 * @param upperContents 上部のコンテンツ
	 */
	public void setUpperContents(String upperContents) {
		this.upperContents = upperContents;
	}

	/**
	 * 下部のコンテンツを取得します。
	 * @return 下部のコンテンツ
	 */
	public String getLowerContents() {
		return lowerContents;
	}

	/**
	 * 下部のコンテンツを設定します。
	 * @param lowerContents 下部のコンテンツ
	 */
	public void setLowerContents(String lowerContents) {
		this.lowerContents = lowerContents;
	}

	/**
	 * 表示プロパティを取得します。
	 * @return 表示プロパティ
	 */
	public List<MetaNestProperty> getProperties() {
		if (properties == null) properties = new ArrayList<>();
		return properties;
	}

	/**
	 * 表示プロパティを設定します。
	 * @param properties 表示プロパティ
	 */
	public void setProperties(List<MetaNestProperty> properties) {
		this.properties = properties;
	}

	/**
	 * 表示順プロパティを取得します。
	 * @return 表示順プロパティ
	 */
	public String getOrderPropName() {
		return orderPropId;
	}

	/**
	 * 表示順プロパティを設定します。
	 * @param orderPropName 表示順プロパティ
	 */
	public void setOrderPropName(String orderPropName) {
		this.orderPropId = orderPropName;
	}

	/**
	 * データのインデックスを取得します。
	 * @return データのインデックス
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * データのインデックスを設定します。
	 * @param index データのインデックス
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * セクション内に配置した場合に枠線を表示を取得します。
	 * @return セクション内に配置した場合に枠線を表示
	 */
	public boolean isDispBorderInSection() {
		return dispBorderInSection;
	}

	/**
	 * セクション内に配置した場合に枠線を表示を設定します。
	 * @param dispBorderInSection セクション内に配置した場合に枠線を表示
	 */
	public void setDispBorderInSection(boolean dispBorderInSection) {
		this.dispBorderInSection = dispBorderInSection;
	}

	/**
	 * 表示プロパティを追加します。
	 * @param property 表示プロパティ
	 */
	public void addProperty(MetaNestProperty property) {
		getProperties().add(property);
	}

	@Override
	public void applyConfig(Element element, String definitionId) {
		super.fillFrom(element, definitionId);

		ReferenceSection section = (ReferenceSection) element;

		EntityContext ctx = EntityContext.getCurrentContext();
		EntityHandler fromEntity = ctx.getHandlerById(definitionId);
		if (fromEntity == null) return;

		PropertyHandler property = fromEntity.getProperty(section.getPropertyName(), ctx);
		if (!(property instanceof ReferencePropertyHandler)) return;

		ReferencePropertyHandler refHandler = (ReferencePropertyHandler) property;
		EntityHandler referenceEntity = refHandler.getReferenceEntityHandler(ctx);

		propertyId = refHandler.getId();
		expandable = section.isExpandable();
		colNum = section.getColNum();
		showLink = section.isShowLink();
		hideDetail = section.isHideDetail();
		hideView = section.isHideView();
		forceUpadte = section.isForceUpadte();
		upperContents = section.getUpperContents();
		lowerContents = section.getLowerContents();
		index = section.getIndex();
		dispBorderInSection = section.isDispBorderInSection();
		if (StringUtil.isNotBlank(section.getOrderPropName())) {
			PropertyHandler orderProp = referenceEntity.getProperty(section.getOrderPropName(), ctx);
			if (orderProp != null) orderPropId = orderProp.getId();
		}
		for (NestProperty nest : section.getProperties()) {
			MetaNestProperty meta = new MetaNestProperty();
			meta.applyConfig(nest, referenceEntity, fromEntity, fromEntity);
			if (meta.getPropertyId() != null) addProperty(meta);
		}
	}

	@Override
	public Element currentConfig(String definitionId) {
		ReferenceSection section = new ReferenceSection();

		super.fillTo(section, definitionId);
		EntityContext ctx = EntityContext.getCurrentContext();
		EntityHandler fromEntity = ctx.getHandlerById(definitionId);
		if (fromEntity == null) return null;

		PropertyHandler property = fromEntity.getPropertyById(propertyId, ctx);
		if (property == null || !(property instanceof ReferencePropertyHandler)) return null;

		ReferencePropertyHandler refHandler = (ReferencePropertyHandler) property;
		EntityHandler referenceEntity = refHandler.getReferenceEntityHandler(ctx);
		if (referenceEntity == null) {
			//参照Entityが存在しない場合はnull
			return null;
		}

		section.setDefintionName(referenceEntity.getMetaData().getName());
		section.setPropertyName(refHandler.getName());
		section.setExpandable(expandable);
		section.setColNum(this.colNum);
		section.setShowLink(showLink);
		section.setHideDetail(hideDetail);
		section.setHideView(hideView);
		section.setForceUpadte(forceUpadte);
		section.setUpperContents(upperContents);
		section.setLowerContents(lowerContents);
		section.setIndex(index);
		section.setDispBorderInSection(dispBorderInSection);
		if (StringUtil.isNotBlank(orderPropId)) {
			PropertyHandler orderProp = referenceEntity.getPropertyById(orderPropId, ctx);
			if (orderProp != null) section.setOrderPropName(orderProp.getName());
		}
		for (MetaNestProperty meta : getProperties()) {
			NestProperty nest = meta.currentConfig(referenceEntity, fromEntity, fromEntity);
			if (nest != null) section.addProperty(nest);
		}
		section.setContentScriptKey(contentScriptKey);

		return section;
	}

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public SectionRuntime createRuntime(EntityViewRuntime entityView, FormViewRuntime formView) {
		return new ReferenceSectionRuntime(this, entityView, formView);
	}

	/**
	 * ランタイム
	 * @author lis3wg
	 *
	 */
	public class ReferenceSectionRuntime extends SectionRuntime implements HasSectionPropertyRuntimeMap {
		/**
		 * Referenceセクションで保持しているプロパティランタイム情報マップ。
		 * 本プロパティで取得できるプロパティは、ReferenceProperty で、ネストプロパティとして、参照先のプロパティを保持している。
		 */
		private Map<String, SectionPropertyRuntime> sectionPropertyRuntimeMap = new HashMap<>();

		public ReferenceSectionRuntime(MetaReferenceSection metadata, EntityViewRuntime entityView, FormViewRuntime formView) {
			super(metadata, entityView);

			EntityContext ctx = EntityContext.getCurrentContext();
			EntityHandler parentEntityHandler = ctx.getHandlerById(entityView.getMetaData().getDefinitionId());
			ReferencePropertyHandler referencePropertyHandler = (ReferencePropertyHandler) parentEntityHandler
					.getPropertyById(metadata.getPropertyId(), ctx);
			EntityHandler referenceEntityHandler = referencePropertyHandler.getReferenceEntityHandler(ctx);

			SectionPropertyRuntimeBuilder referencePropertyRuntimeBuilder = new SectionPropertyRuntimeBuilder(
					referencePropertyHandler.getName());

			if (properties != null && !properties.isEmpty()) {
				Map<String, SectionPropertyRuntime> nestPropertyRuntimeMap = new HashMap<>();
				for (MetaNestProperty meta : properties) {
					if (meta.getAutocompletionSetting()  != null) {
						entityView.addAutocompletionSetting(meta.getAutocompletionSetting().createRuntime(entityView));
					}

					// Referenceプロパティの参照先プロパティの情報を取得
					String nestPropertyName = meta.convertName(meta.getPropertyId(), ctx, referenceEntityHandler);
					SectionPropertyRuntimeBuilder nestPropertyRuntimeBuilder = new SectionPropertyRuntimeBuilder(nestPropertyName);

					MetaPropertyEditor nestPropertyEditor = meta.getEditor();
					if (null != nestPropertyEditor) {
						// nestPropertyEditor が存在すれば、ランタイムを生成する
						PropertyEditorRuntime runtime = (PropertyEditorRuntime) nestPropertyEditor.createRuntime(entityView, formView, null,
								ctx, referenceEntityHandler);
						nestPropertyRuntimeBuilder.editor(runtime);
					}
					// ネストプロパティを設定する
					nestPropertyRuntimeMap.put(nestPropertyName, nestPropertyRuntimeBuilder.build());
				}
				// リファレンスプロパティにネストプロパティを設定
				referencePropertyRuntimeBuilder.nest(nestPropertyRuntimeMap);
			}
			// プロパティを設定する
			sectionPropertyRuntimeMap.put(referencePropertyHandler.getName(), referencePropertyRuntimeBuilder.build());

			//上下コンテンツのコンパイル
			if (StringUtil.isNotEmpty(metadata.upperContents) || StringUtil.isNotEmpty(metadata.lowerContents)) {
				if (metadata.contentScriptKey == null) {
					//ContentsScript用のKEYを設定
					metadata.contentScriptKey = "ReferenceSection_content_" + GroovyTemplateCompiler.randomName().replace("-", "_");
				}
				if (StringUtil.isNotEmpty(metadata.upperContents)) {
					//EntityViewにセット
					String upperKey = metadata.contentScriptKey + "_UpperContent";
					entityView.addTemplate(upperKey, compile(metadata.upperContents, upperKey));
				}
				if (StringUtil.isNotEmpty(metadata.lowerContents)) {
					//EntityViewにセット
					String lowerKey = metadata.contentScriptKey + "_LowerContent";
					entityView.addTemplate(lowerKey, compile(metadata.lowerContents, lowerKey));
				}
			}

		}

		@Override
		public MetaReferenceSection getMetaData() {
			return (MetaReferenceSection) super.getMetaData();
		}

		@Override
		public Map<String, SectionPropertyRuntime> getSectionPropertyRuntimeMap() {
			return sectionPropertyRuntimeMap;
		}

		private GroovyTemplate compile(String script, String key) {
			TenantContext tenant = ExecuteContext.getCurrentContext().getTenantContext();
			return GroovyTemplateCompiler.compile(
					script, key, (GroovyScriptEngine) tenant.getScriptEngine());
		}
	}
}
