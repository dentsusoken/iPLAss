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

import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.i18n.MetaLocalizedString;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.view.generic.element.Element;
import org.iplass.mtp.view.generic.element.section.TemplateSection;

/**
 * テンプレートセクションのメタデータ
 * @author lis3wg
 */
public class MetaTemplateSection extends MetaSection {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 3587801590097006196L;

	public static MetaTemplateSection createInstance(Element element) {
		return new MetaTemplateSection();
	}

	/** テンプレート名 */
	private String templateName;

	/** タイトル */
	private String title;

	/** id */
	private String id;

	/** リンクを表示するか */
	private boolean showLink;

	/** 詳細編集非表示設定 */
	private boolean hideDetail;

	/** 詳細表示非表示設定 */
	private boolean hideView;

	/** 多言語設定情報 */
	private List<MetaLocalizedString> localizedTitleList = new ArrayList<MetaLocalizedString>();

	/** セクション内に配置した場合に枠線を表示 */
	private boolean dispBorderInSection;

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

	@Override
	public MetaSection copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public void applyConfig(Element element, String definitionId) {
		super.fillFrom(element, definitionId);

		TemplateSection section = (TemplateSection) element;
		this.templateName = section.getTemplateName();
		title = section.getTitle();
		id = section.getId();
		this.showLink = section.isShowLink();
		this.hideDetail = section.isHideDetail();
		this.hideView = section.isHideView();
		this.dispBorderInSection = section.isDispBorderInSection();

		// 言語毎の文字情報設定
		localizedTitleList = I18nUtil.toMeta(section.getLocalizedTitleList());
	}

	@Override
	public Element currentConfig(String definitionId) {
		TemplateSection section = new TemplateSection();
		super.fillTo(section, definitionId);

		section.setTemplateName(templateName);
		section.setTitle(title);
		section.setId(id);
		section.setShowLink(showLink);
		section.setHideDetail(hideDetail);
		section.setHideView(hideView);
		section.setDispBorderInSection(dispBorderInSection);
		section.setLocalizedTitleList(I18nUtil.toDef(localizedTitleList));

		return section;
	}
}
