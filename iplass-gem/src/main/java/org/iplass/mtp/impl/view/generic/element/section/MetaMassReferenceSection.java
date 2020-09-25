/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.query.PreparedQuery;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.entity.property.ReferencePropertyHandler;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.i18n.MetaLocalizedString;
import org.iplass.mtp.impl.script.GroovyScriptEngine;
import org.iplass.mtp.impl.script.template.GroovyTemplate;
import org.iplass.mtp.impl.script.template.GroovyTemplateCompiler;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.view.generic.EntityViewRuntime;
import org.iplass.mtp.impl.view.generic.editor.MetaNestProperty;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.PagingPosition;
import org.iplass.mtp.view.generic.editor.NestProperty;
import org.iplass.mtp.view.generic.element.Element;
import org.iplass.mtp.view.generic.element.section.MassReferenceSection;
import org.iplass.mtp.view.generic.element.section.MassReferenceSection.MassReferenceEditType;
import org.iplass.mtp.view.generic.element.section.SortSetting;

public class MetaMassReferenceSection extends MetaSection {

	private static final long serialVersionUID = -2758207829852210618L;

	public static MetaMassReferenceSection createInstance(Element element) {
		return new MetaMassReferenceSection();
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

	/** リンクを表示するか */
	private boolean showLink;

	/** 上部のコンテンツ */
	private String upperContents;

	/** 下部のコンテンツ */
	private String lowerContents;

	/** 上限値 */
	private int limit;

	/** ダイアログ表示アクション名 */
	private String viewActionName;

	/** ダイアログ編集アクション名 */
	private String detailActionName;

	/** ビュー定義名 */
	private String viewName;

	/** 編集リンクを詳細リンクに変更 */
	private boolean changeEditLinkToViewLink;

	/** 削除ボタン非表示設定 */
	private boolean hideDeleteButton;

	/** 追加ボタン非表示設定 */
	private boolean hideAddButton;

	/** ページング非表示設定 */
	private boolean hidePaging;

	/** 件数非表示設定 */
	private boolean hideCount;

	/** ページジャンプ非表示設定 */
	private boolean hidePageJump;

	/** ページリンク非表示設定 */
	private boolean hidePageLink;

	/** 検索アイコンを常に表示 */
	private boolean showSearchBtn;

	/** ページング表示位置 */
	private PagingPosition pagingPosition;

	/** 詳細編集非表示設定 */
	private boolean hideDetail;

	/** 詳細表示非表示設定 */
	private boolean hideView;

	/** 編集タイプ */
	private MassReferenceEditType editType;

	/** 参照型の表示プロパティ */
	private List<MetaNestProperty> nestProperties;

	/** ソート設定 */
	private List<MetaSortSetting> sortSetting;

	/** 絞り込み条件設定スクリプト */
	private String filterConditionScript;

	/** 絞り込み条件設定スクリプトを特定するkey(内部用) */
	private String filterScriptKey;

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
	 * 上限値を取得します。
	 * @return 上限値
	 */
	public int getLimit() {
	    return limit;
	}

	/**
	 * 上限値を設定します。
	 * @param limit 上限値
	 */
	public void setLimit(int limit) {
	    this.limit = limit;
	}

	/**
	 * ダイアログ表示アクション名を取得します。
	 * @return ダイアログ表示アクション名
	 */
	public String getViewActionName() {
	    return viewActionName;
	}

	/**
	 * ダイアログ表示アクション名を設定します。
	 * @param viewActionName ダイアログ表示アクション名
	 */
	public void setViewActionName(String viewActionName) {
	    this.viewActionName = viewActionName;
	}

	/**
	 * ダイアログ編集アクション名を取得します。
	 * @return ダイアログ編集アクション名
	 */
	public String getDetailActionName() {
	    return detailActionName;
	}

	/**
	 * ダイアログ編集アクション名を設定します。
	 * @param detailActionName ダイアログ編集アクション名
	 */
	public void setDetailActionName(String detailActionName) {
	    this.detailActionName = detailActionName;
	}

	/**
	 * ビュー定義名を取得します。
	 * @return ビュー定義名
	 */
	public String getViewName() {
	    return viewName;
	}

	/**
	 * ビュー定義名を設定します。
	 * @param viewName ビュー定義名
	 */
	public void setViewName(String viewName) {
	    this.viewName = viewName;
	}

	/**
	 * 編集リンクを詳細リンクに変更するかを取得します。
	 * @return 編集リンクを詳細リンクに変更するか
	 */
	public boolean isChangeEditLinkToViewLink() {
		return changeEditLinkToViewLink;
	}

	/**
	 * 編集リンクを詳細リンクに変更するかを設定します。
	 * @param changeEditLinkToViewLink 編集リンクを詳細リンクに変更するか
	 */
	public void setChangeEditLinkToViewLink(boolean changeEditLinkToViewLink) {
		this.changeEditLinkToViewLink = changeEditLinkToViewLink;
	}

	/**
	 * 削除ボタン非表示設定を取得します。
	 * @return 削除ボタン非表示設定
	 */
	public boolean isHideDeleteButton() {
	    return hideDeleteButton;
	}

	/**
	 * 削除ボタン非表示設定を設定します。
	 * @param hideDeleteButton 削除ボタン非表示設定
	 */
	public void setHideDeleteButton(boolean hideDeleteButton) {
	    this.hideDeleteButton = hideDeleteButton;
	}

	/**
	 * 追加ボタン非表示設定を取得します。
	 * @return 追加ボタン非表示設定
	 */
	public boolean isHideAddButton() {
	    return hideAddButton;
	}

	/**
	 * 追加ボタン非表示設定を設定します。
	 * @param hideAddButton 追加ボタン非表示設定
	 */
	public void setHideAddButton(boolean hideAddButton) {
	    this.hideAddButton = hideAddButton;
	}

	/**
	 * ページング非表示設定を取得します。
	 * @return ページング非表示設定
	 */
	public boolean isHidePaging() {
	    return hidePaging;
	}

	/**
	 * ページング非表示設定を設定します。
	 * @param hidePaging ページング非表示設定
	 */
	public void setHidePaging(boolean hidePaging) {
	    this.hidePaging = hidePaging;
	}

	/**
	 * 件数非表示設定を取得します。
	 * @return 件数非表示設定
	 */
	public boolean isHideCount() {
	    return hideCount;
	}

	/**
	 * 件数非表示設定を設定します。
	 * @param hideCount 件数非表示設定
	 */
	public void setHideCount(boolean hideCount) {
	    this.hideCount = hideCount;
	}

	/**
	 * ページジャンプ非表示設定を取得します。
	 * @return ページジャンプ非表示設定
	 */
	public boolean isHidePageJump() {
	    return hidePageJump;
	}

	/**
	 * ページジャンプ非表示設定を設定します。
	 * @param hidePageJump ページジャンプ非表示設定
	 */
	public void setHidePageJump(boolean hidePageJump) {
	    this.hidePageJump = hidePageJump;
	}

	/**
	 * ページリンク非表示設定を取得します。
	 * @return ページリンク非表示設定
	 */
	public boolean isHidePageLink() {
	    return hidePageLink;
	}

	/**
	 * ページリンク非表示設定を設定します。
	 * @param hidePageLink ページリンク非表示設定
	 */
	public void setHidePageLink(boolean hidePageLink) {
	    this.hidePageLink = hidePageLink;
	}

	/**
	 * 検索アイコンを常に表示設定を取得します。
	 * @return 検索アイコンを常に表示設定
	 */
	public boolean isShowSearchBtn() {
		return showSearchBtn;
	}

	/**
	 * 検索アイコンを常に表示設定を設定します。
	 * @param showSearchBtn 検索アイコンを常に表示設定
	 */
	public void setShowSearchBtn(boolean showSearchBtn) {
		this.showSearchBtn = showSearchBtn;
	}

	/**
	 * ページング表示位置を取得します。
	 * @return ページング表示位置
	 */
	public PagingPosition getPagingPosition() {
	    return pagingPosition;
	}

	/**
	 * ページング表示位置を設定します。
	 * @param pagingPosition ページング表示位置
	 */
	public void setPagingPosition(PagingPosition pagingPosition) {
	    this.pagingPosition = pagingPosition;
	}

	/**
	 * 編集タイプを取得します。
	 * @return 編集タイプ
	 */
	public MassReferenceEditType getEditType() {
	    return editType;
	}

	/**
	 * 編集タイプを設定します。
	 * @param editType 編集タイプ
	 */
	public void setEditType(MassReferenceEditType editType) {
	    this.editType = editType;
	}

	/**
	 * 参照型の表示プロパティを取得します。
	 * @return 参照型の表示プロパティ
	 */
	public List<MetaNestProperty> getNestProperties() {
		if (nestProperties == null) nestProperties = new ArrayList<MetaNestProperty>();
		return nestProperties;
	}

	/**
	 * 参照型の表示プロパティを設定します。
	 * @param nestProperties 参照型の表示プロパティ
	 */
	public void setNestProperties(List<MetaNestProperty> nestProperties) {
		this.nestProperties = nestProperties;
	}

	public List<MetaSortSetting> getSortSetting() {
		if (sortSetting == null) sortSetting = new ArrayList<>();
		return sortSetting;
	}

	public void setSortSetting(List<MetaSortSetting> sortSetting) {
		this.sortSetting = sortSetting;
	}

	public void addSortSetting(MetaSortSetting setting) {
		getSortSetting().add(setting);
	}

	/**
	 * 絞り込み条件設定スクリプトを取得します。
	 * @return 絞り込み条件設定スクリプト
	 */
	public String getFilterConditionScript() {
	    return filterConditionScript;
	}

	/**
	 * 絞り込み条件設定スクリプトを設定します。
	 * @param filterConditionScript 絞り込み条件設定スクリプト
	 */
	public void setFilterConditionScript(String filterConditionScript) {
	    this.filterConditionScript = filterConditionScript;
	}

	/**
	 * 多言語設定情報を取得します。
	 * @return 多言語設定情報
	 */
	public List<MetaLocalizedString> getLocalizedTitleList() {
	    return localizedTitleList;
	}

	/**
	 * 多言語設定情報を設定します。
	 * @param localizedTitleList 多言語設定情報
	 */
	public void setLocalizedTitleList(List<MetaLocalizedString> localizedTitleList) {
	    this.localizedTitleList = localizedTitleList;
	}

	public void addNestProperty(MetaNestProperty property) {
		getNestProperties().add(property);
	}

	@Override
	public MetaMassReferenceSection copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public void applyConfig(Element element, String definitionId) {
		super.fillFrom(element, definitionId);

		MassReferenceSection section = (MassReferenceSection) element;

		EntityContext ctx = EntityContext.getCurrentContext();
		EntityHandler entity = ctx.getHandlerById(definitionId);
		if (entity == null) return;

		//被参照プロパティかチェックする
		PropertyHandler property = entity.getProperty(section.getPropertyName(), ctx);
		if (!(property instanceof ReferencePropertyHandler)) {
			throw new EntityRuntimeException(section.getPropertyName() + " is not ReferenceProperty.");
		}
		ReferencePropertyHandler rp = (ReferencePropertyHandler) property;

		ReferencePropertyHandler mappedBy = rp.getMappedByPropertyHandler(ctx);
		if (mappedBy == null) {
			throw new EntityRuntimeException(section.getPropertyName() + " is not MappedByProperty. MassReferencePropertyEditor is supports only MappedByProperty.");
		}
		EntityHandler refEntity = mappedBy.getParent();

		propertyId = property.getId();
		title = section.getTitle();
		expandable = section.isExpandable();
		id = section.getId();
		style = section.getStyle();
		showLink = section.isShowLink();
		upperContents = section.getUpperContents();
		lowerContents = section.getLowerContents();
		limit = section.getLimit();
		viewActionName = section.getViewActionName();
		detailActionName = section.getDetailActionName();
		viewName = section.getViewName();
		changeEditLinkToViewLink = section.isChangeEditLinkToViewLink();
		hideDeleteButton = section.isHideDeleteButton();
		hideAddButton = section.isHideAddButton();
		hidePaging = section.isHidePaging();
		hidePageJump = section.isHidePageJump();
		hidePageLink = section.isHidePageLink();
		showSearchBtn = section.isShowSearchBtn();
		pagingPosition = section.getPagingPosition();
		hideCount = section.isHideCount();
		hideDetail = section.isHideDetail();
		hideView = section.isHideView();
		editType = section.getEditType();
		filterConditionScript = section.getFilterConditionScript();
		for (NestProperty np : section.getProperties()) {
			MetaNestProperty mnp = new MetaNestProperty();
			mnp.applyConfig(np, refEntity, entity);
			if (mnp.getPropertyId() != null) addNestProperty(mnp);
		}

		if (!section.getSortSetting().isEmpty()) {
			for (SortSetting setting : section.getSortSetting()) {
				MetaSortSetting meta = new MetaSortSetting();
				meta.applyConfig(setting, ctx, refEntity);
				addSortSetting(meta);
			}
		}

		// 言語毎の文字情報設定
		localizedTitleList = I18nUtil.toMeta(section.getLocalizedTitleList());
	}

	@Override
	public Element currentConfig(String definitionId) {
		MassReferenceSection section = new MassReferenceSection();
		super.fillTo(section, definitionId);

		EntityContext ctx = EntityContext.getCurrentContext();
		EntityHandler entity = ctx.getHandlerById(definitionId);
		if (entity == null) return null;

		PropertyHandler property = entity.getPropertyById(propertyId, ctx);
		if (property == null || !(property instanceof ReferencePropertyHandler)) return null;
		ReferencePropertyHandler rp = (ReferencePropertyHandler) property;

		EntityHandler refEntity = null;
		if (rp != null) {
			ReferencePropertyHandler mappedBy = rp.getMappedByPropertyHandler(ctx);
			if (mappedBy != null) {
				refEntity = mappedBy.getParent();
			}
		}
		if (refEntity == null) {
			//参照Entityが存在しない場合はnull
			return null;
		}

		section.setDefintionName(refEntity.getMetaData().getName());
		section.setPropertyName(rp.getName());
		section.setTitle(title);
		section.setExpandable(expandable);
		section.setId(id);
		section.setStyle(style);
		section.setShowLink(showLink);
		section.setUpperContents(upperContents);
		section.setLowerContents(lowerContents);
		section.setLimit(limit);
		section.setViewActionName(viewActionName);
		section.setDetailActionName(detailActionName);
		section.setViewName(viewName);
		section.setChangeEditLinkToViewLink(changeEditLinkToViewLink);
		section.setHideDeleteButton(hideDeleteButton);
		section.setHideAddButton(hideAddButton);
		section.setHidePaging(hidePaging);
		section.setHidePageJump(hidePageJump);
		section.setHidePageLink(hidePageLink);
		section.setShowSearchBtn(showSearchBtn);
		section.setPagingPosition(pagingPosition);
		section.setHideCount(hideCount);
		section.setHideDetail(hideDetail);
		section.setHideView(hideView);
		section.setEditType(editType);
		section.setFilterConditionScript(filterConditionScript);
		section.setFilterScriptKey(filterScriptKey);
		section.setContentScriptKey(contentScriptKey);

		for (MetaNestProperty mnp : getNestProperties()) {
			if (refEntity != null) {
				NestProperty np = mnp.currentConfig(refEntity, entity);
				if (np != null) section.addProperty(np);
			}
		}

		if (!getSortSetting().isEmpty()) {
			for (MetaSortSetting meta : getSortSetting()) {
				SortSetting ss = meta.currentConfig(ctx, refEntity);
				if (ss != null) section.addSortSetting(ss);
			}
		}

		section.setLocalizedTitleList(I18nUtil.toDef(localizedTitleList));
		return section;
	}

	@Override
	public SectionHandler createRuntime(EntityViewRuntime entityView) {
		return new MassReferenceSectionHandler(this, entityView);
	}

	/**
	 * ランタイム
	 * @author lis3wg
	 *
	 */
	public class MassReferenceSectionHandler extends SectionHandler {

		public MassReferenceSectionHandler(MetaMassReferenceSection metadata, EntityViewRuntime entityView) {
			super(metadata, entityView);
			if (StringUtil.isNotBlank(metadata.filterConditionScript)) {
				if (metadata.filterScriptKey == null) {
					//テンプレートコンパイル
					metadata.filterScriptKey = "mrs_filterCondition_" + GroovyTemplateCompiler.randomName().replace("-", "_");

					PreparedQuery query = new PreparedQuery(metadata.filterConditionScript);
					entityView.addQuery(filterScriptKey, query);
				}
			} else {
				metadata.filterScriptKey = null;
			}
			if (nestProperties != null && !nestProperties.isEmpty()) {
				for (MetaNestProperty meta : nestProperties) {
					if (meta.getAutocompletionSetting()  != null) {
						entityView.addAutocompletionSettingHandler(meta.getAutocompletionSetting().getHandler(entityView));
					}
				}
			}
			//上下コンテンツのコンパイル
			if (StringUtil.isNotEmpty(metadata.upperContents) || StringUtil.isNotEmpty(metadata.lowerContents)) {
				if (metadata.contentScriptKey == null) {
					//ContentsScript用のKEYを設定
					metadata.contentScriptKey = "MassReferenceSection_content_" + GroovyTemplateCompiler.randomName().replace("-", "_");
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
		public MetaMassReferenceSection getMetaData() {
			return (MetaMassReferenceSection) super.getMetaData();
		}

		private GroovyTemplate compile(String script, String key) {
			TenantContext tenant = ExecuteContext.getCurrentContext().getTenantContext();
			return GroovyTemplateCompiler.compile(
					script, key, (GroovyScriptEngine) tenant.getScriptEngine());
		}
	}
}
