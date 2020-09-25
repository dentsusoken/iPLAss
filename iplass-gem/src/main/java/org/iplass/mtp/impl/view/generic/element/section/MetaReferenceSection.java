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

package org.iplass.mtp.impl.view.generic.element.section;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.entity.property.ReferencePropertyHandler;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.i18n.MetaLocalizedString;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.script.GroovyScriptEngine;
import org.iplass.mtp.impl.script.template.GroovyTemplate;
import org.iplass.mtp.impl.script.template.GroovyTemplateCompiler;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.view.generic.EntityViewRuntime;
import org.iplass.mtp.impl.view.generic.editor.MetaNestProperty;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.editor.NestProperty;
import org.iplass.mtp.view.generic.element.Element;
import org.iplass.mtp.view.generic.element.section.ReferenceSection;

/**
 *
 * @author lis3wg
 */
public class MetaReferenceSection extends MetaSection {

	/** SerialVersionUID */
	private static final long serialVersionUID = -3690001980373138857L;

	public static MetaReferenceSection createInstance(Element element) {
		return new MetaReferenceSection();
	}

	/** プロパティID */
	private String propertyId;

	/** タイトル */
	private String title;

	/** 多言語設定情報 */
	private List<MetaLocalizedString> localizedTitleList = new ArrayList<MetaLocalizedString>();

	/** セクションの展開可否 */
	private boolean expandable;

	/** id */
	private String id;

	/** クラス名 */
	private String style;

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
	 * タイトルを取得します。
	 * @return タイトル
	 */
	public String getTitle() {
	    return title;
	}

	/**
	 * タイトルを設定します。
	 * @param title タイトル
	 */
	public void setTitle(String title) {
	    this.title = title;
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
	 * idを取得します。
	 * @return id
	 */
	public String getId() {
	    return id;
	}

	/**
	 * idを設定します。
	 * @param id id
	 */
	public void setId(String id) {
	    this.id = id;
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
		if (properties == null) properties = new ArrayList<MetaNestProperty>();
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
	 * 表示プロパティを追加します。
	 * @param property 表示プロパティ
	 */
	public void addProperty(MetaNestProperty property) {
		getProperties().add(property);
	}

	/**
	 * 多言語設定情報を取得します。
	 * @return リスト
	 */
	public List<MetaLocalizedString> getLocalizedTitleList() {
		return localizedTitleList;
	}

	/**
	 * 多言語設定情報を設定します。
	 * @param リスト
	 */
	public void setLocalizedTitleList(List<MetaLocalizedString> localizedTitleList) {
		this.localizedTitleList = localizedTitleList;
	}

	@Override
	public void applyConfig(Element element, String definitionId) {
		super.fillFrom(element, definitionId);

		ReferenceSection section = (ReferenceSection) element;

		EntityContext ctx = EntityContext.getCurrentContext();
		EntityHandler entity = ctx.getHandlerById(definitionId);
		if (entity == null) return;

		PropertyHandler property = entity.getProperty(section.getPropertyName(), ctx);
		if (!(property instanceof ReferencePropertyHandler)) return;

		ReferencePropertyHandler refHandler = (ReferencePropertyHandler) property;
		EntityHandler refEntity = refHandler.getReferenceEntityHandler(ctx);

		propertyId = refHandler.getId();
		title = section.getTitle();
		expandable = section.isExpandable();
		id = section.getId();
		style = section.getStyle();
		colNum = section.getColNum();
		showLink = section.isShowLink();
		hideDetail = section.isHideDetail();
		hideView = section.isHideView();
		forceUpadte = section.isForceUpadte();
		upperContents = section.getUpperContents();
		lowerContents = section.getLowerContents();
		index = section.getIndex();
		if (StringUtil.isNotBlank(section.getOrderPropName())) {
			PropertyHandler orderProp = refEntity.getProperty(section.getOrderPropName(), ctx);
			if (orderProp != null) orderPropId = orderProp.getId();
		}
		for (NestProperty nest : section.getProperties()) {
			MetaNestProperty meta = new MetaNestProperty();
			meta.applyConfig(nest, refEntity, entity);
			if (meta.getPropertyId() != null) addProperty(meta);
		}

		// 言語毎の文字情報設定
		localizedTitleList = I18nUtil.toMeta(section.getLocalizedTitleList());
	}

	@Override
	public Element currentConfig(String definitionId) {
		ReferenceSection section = new ReferenceSection();

		super.fillTo(section, definitionId);
		EntityContext ctx = EntityContext.getCurrentContext();
		EntityHandler entity = ctx.getHandlerById(definitionId);
		if (entity == null) return null;

		PropertyHandler property = entity.getPropertyById(propertyId, ctx);
		if (property == null || !(property instanceof ReferencePropertyHandler)) return null;

		ReferencePropertyHandler refHandler = (ReferencePropertyHandler) property;
		EntityHandler refEntity = refHandler.getReferenceEntityHandler(ctx);
		if (refEntity == null) {
			//参照Entityが存在しない場合はnull
			return null;
		}

		section.setDefintionName(refEntity.getMetaData().getName());
		section.setPropertyName(refHandler.getName());
		section.setTitle(title);
		section.setExpandable(expandable);
		section.setId(id);
		section.setStyle(style);
		section.setColNum(this.colNum);
		section.setShowLink(showLink);
		section.setHideDetail(hideDetail);
		section.setHideView(hideView);
		section.setForceUpadte(forceUpadte);
		section.setUpperContents(upperContents);
		section.setLowerContents(lowerContents);
		section.setIndex(index);
		if (StringUtil.isNotBlank(orderPropId)) {
			PropertyHandler orderProp = refEntity.getPropertyById(orderPropId, ctx);
			if (orderProp != null) section.setOrderPropName(orderProp.getName());
		}
		for (MetaNestProperty meta : getProperties()) {
			NestProperty nest = meta.currentConfig(refEntity, entity);
			if (nest != null) section.addProperty(nest);
		}
		section.setContentScriptKey(contentScriptKey);

		section.setLocalizedTitleList(I18nUtil.toDef(localizedTitleList));

		return section;
	}

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public SectionRuntime createRuntime(EntityViewRuntime entityView) {
		return new ReferenceSectionRuntime(this, entityView);
	}

	/**
	 * ランタイム
	 * @author lis3wg
	 *
	 */
	public class ReferenceSectionRuntime extends SectionRuntime {

		public ReferenceSectionRuntime(MetaReferenceSection metadata, EntityViewRuntime entityView) {
			super(metadata, entityView);
			if (properties != null && !properties.isEmpty()) {
				for (MetaNestProperty meta : properties) {
					if (meta.getAutocompletionSetting()  != null) {
						entityView.addAutocompletionSetting(meta.getAutocompletionSetting().createRuntime(entityView));
					}
				}
			}
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

		private GroovyTemplate compile(String script, String key) {
			TenantContext tenant = ExecuteContext.getCurrentContext().getTenantContext();
			return GroovyTemplateCompiler.compile(
					script, key, (GroovyScriptEngine) tenant.getScriptEngine());
		}
	}

}
