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

package org.iplass.mtp.impl.view.top.parts;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.i18n.MetaLocalizedString;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.web.WebUtil;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.EntityView;
import org.iplass.mtp.view.generic.EntityViewManager;
import org.iplass.mtp.view.generic.FormViewUtil;
import org.iplass.mtp.view.generic.SearchFormView;
import org.iplass.mtp.view.top.parts.EntityListParts;
import org.iplass.mtp.view.top.parts.TopViewParts;
import org.iplass.mtp.web.template.TemplateUtil;

/**
 * お知らせ一覧パーツ
 * @author lis3wg
 */
public class MetaEntityListParts extends MetaTemplateParts {

	/** SerialVersionUID */
	private static final long serialVersionUID = 9029097706504538709L;

	/** ID */
	private String definitionId;

	/** View名 */
	private String viewName;

	/** 検索リンク用のView名 */
	private String viewNameForLink;

	/** 詳細リンク用のView名 */
	private String viewNameForDetail;

	/** フィルタ名*/
	private String filterName;

	/** タイトル */
	private String title;

	/** 高さ */
	private Integer height;

	/** 多言語設定情報 */
	private List<MetaLocalizedString> localizedTitleList = new ArrayList<>();

	/** アイコンタグ */
	private String iconTag;

	/**
	 * インスタンスを生成します。
	 * @param parts TOP画面パーツ
	 * @return インスタンス
	 */
	public static MetaEntityListParts createInstance(TopViewParts parts) {
		return new MetaEntityListParts();
	}

	/**
	 * IDを取得します。
	 * @return ID
	 */
	public String getDefinitionId() {
	    return definitionId;
	}

	/**
	 * IDを設定します。
	 * @param definitionId ID
	 */
	public void setDefinitionId(String definitionId) {
	    this.definitionId = definitionId;
	}

	/**
	 * View名を取得します。
	 * @return View名
	 */
	public String getViewName() {
	    return viewName;
	}

	/**
	 * View名を設定します。
	 * @param viewName View名
	 */
	public void setViewName(String viewName) {
	    this.viewName = viewName;
	}

	/**
	 * 検索リンク用のView名を取得します。
	 * @return 検索リンク用のView名
	 */
	public String getViewNameForLink() {
	    return viewNameForLink;
	}

	/**
	 * 検索リンク用のView名を設定します。
	 * @param viewNameForLink 検索リンク用のView名
	 */
	public void setViewNameForLink(String viewNameForLink) {
	    this.viewNameForLink = viewNameForLink;
	}

	/**
	 * 詳細リンク用のView名を取得します。
	 * @return 詳細リンク用のView名
	 */
	public String getViewNameForDetail() {
	    return viewNameForDetail;
	}

	/**
	 * 詳細リンク用のView名を設定します。
	 * @param viewNameForDetail 詳細リンク用のView名
	 */
	public void setViewNameForDetail(String viewNameForDetail) {
	    this.viewNameForDetail = viewNameForDetail;
	}

	/**
	 * フィルタ名を取得します。
	 * @return フィルタ名
	 */
	public String getFilterName() {
	    return filterName;
	}

	/**
	 * フィルタ名を設定します。
	 * @param filterName フィルタ名
	 */
	public void setFilterName(String filterName) {
	    this.filterName = filterName;
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
	 * 高さを取得します。
	 * @return 高さ
	 */
	public Integer getHeight() {
	    return height;
	}

	/**
	 * 高さを設定します。
	 * @param height 高さ
	 */
	public void setHeight(Integer height) {
	    this.height = height;
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

	/**
	 * アイコンタグを取得します。
	 * @return アイコンタグ
	 */
	public String getIconTag() {
	    return iconTag;
	}

	/**
	 * アイコンタグを設定します。
	 * @param iconTag アイコンタグ
	 */
	public void setIconTag(String iconTag) {
	    this.iconTag = iconTag;
	}

	@Override
	public void applyConfig(TopViewParts parts) {
		EntityListParts e = (EntityListParts) parts;
		EntityContext context = EntityContext.getCurrentContext();
		EntityHandler entity = context.getHandlerByName(e.getDefName());
		if (entity == null) return;

		definitionId = entity.getMetaData().getId();
		viewName = e.getViewName();
		viewNameForLink = e.getViewNameForLink();
		viewNameForDetail = e.getViewNameForDetail();
		filterName = e.getFilterName();
		title = e.getTitle();
		height = e.getHeight();

		// 言語毎の文字情報設定
		localizedTitleList = I18nUtil.toMeta(e.getLocalizedTitleList());
		iconTag = e.getIconTag();
	}

	@Override
	public TopViewParts currentConfig() {
		EntityListParts parts = new EntityListParts();
		EntityContext context = EntityContext.getCurrentContext();
		EntityHandler entity = null;
		if (definitionId != null) {
			entity = context.getHandlerById(definitionId);
			if (entity != null) {
				parts.setDefName(entity.getMetaData().getName());
			}
		}

		parts.setViewName(viewName);
		parts.setViewNameForLink(viewNameForLink);
		parts.setViewNameForDetail(viewNameForDetail);
		parts.setFilterName(filterName);
		parts.setTitle(title);
		parts.setHeight(height);
		parts.setLocalizedTitleList(I18nUtil.toDef(localizedTitleList));
		parts.setIconTag(iconTag);
		return parts;
	}

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public TemplatePartsHandler createRuntime() {
		return new TemplatePartsHandler(this) {
			private static final String TEMPLATE_PATH = "gem/generic/search/list";
			private static final String TEMPLATE_PATH_WIDGET = "gem/generic/search/listWidget";

			@Override
			public boolean isParts() {
				return checkEntity();
			}

			@Override
			public boolean isWidget() {
				return checkEntity();
			}

			@Override
			public String getTemplatePathForParts(HttpServletRequest req) {
				return TEMPLATE_PATH;
			}

			@Override
			public String getTemplatePathForWidget(HttpServletRequest req) {
				return TEMPLATE_PATH_WIDGET;
			}

			@Override
			public void setAttribute(HttpServletRequest req) {
				EntityContext context = EntityContext.getCurrentContext();
				if (definitionId == null) return;

				RequestContext request = WebUtil.getRequestContext();

				request.setAttribute("partsCnt", req.getAttribute("partsCnt"));

				SearchFormView form = null;
				String title = I18nUtil.stringMeta(MetaEntityListParts.this.title, MetaEntityListParts.this.localizedTitleList);
				EntityHandler handler = context.getHandlerById(definitionId);
				if (handler != null) {
					EntityDefinition ed = handler.getMetaData().currentConfig();
					EntityViewManager evm = ManagerLocator.manager(EntityViewManager.class);
					EntityView ev = evm.get(ed.getName());
					if (ev != null) {
						if (StringUtil.isEmpty(viewName)) {
							//デフォルトレイアウトを利用
							if (ev.getSearchFormViewNames().length > 0) {
								//1件でもView定義があればその中からデフォルトレイアウトを探す
								form = ev.getDefaultSearchFormView();
							} else {
								//何もなければ自動生成
								form = FormViewUtil.createDefaultSearchFormView(ed);
							}
						} else {
							//指定レイアウトを利用
							form = ev.getSearchFormView(viewName);
						}
					}
					if (form == null) {
						//View定義がないか、指定されたViewがない場合は自動生成
						form = FormViewUtil.createDefaultSearchFormView(ed);
					}

					if(StringUtil.isEmpty(title)) {
						title = TemplateUtil.getMultilingualString(form.getTitle(), form.getLocalizedTitleList());
					}
					if(StringUtil.isEmpty(title)) {
						title = TemplateUtil.getMultilingualString(
								ed.getDisplayName(), ed.getLocalizedDisplayNameList());
					}
				}

				request.setAttribute("entityListParts", currentConfig());
				request.setAttribute("searchFormView", form);
				request.setAttribute("title", title);
			}

			@Override
			public void clearAttribute(HttpServletRequest req) {
			}

			private boolean checkEntity() {
				if (definitionId == null) return false;

				EntityContext context = EntityContext.getCurrentContext();
				EntityHandler entity = context.getHandlerById(definitionId);

				//Entity定義が取れなければ表示しない
				if (entity == null) return false;

				if (viewName != null && !viewName.isEmpty()) {
					EntityViewManager em = ManagerLocator.getInstance().getManager(EntityViewManager.class);
					EntityView ev = em.get(entity.getMetaData().getName());

					//画面定義が取れなければ表示しない
					if (ev == null || ev.getSearchFormView(viewName)  == null) return false;
				}
				return true;
			}
		};
	}
}
