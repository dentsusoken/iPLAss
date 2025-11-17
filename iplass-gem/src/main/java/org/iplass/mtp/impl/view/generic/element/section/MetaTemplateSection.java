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

	/** リンクを表示するか */
	private boolean showLink;

	/** 詳細編集非表示設定 */
	private boolean hideDetail;

	/** 詳細表示非表示設定 */
	private boolean hideView;

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
		this.showLink = section.isShowLink();
		this.hideDetail = section.isHideDetail();
		this.hideView = section.isHideView();
		this.dispBorderInSection = section.isDispBorderInSection();
	}

	@Override
	public Element currentConfig(String definitionId) {
		TemplateSection section = new TemplateSection();
		super.fillTo(section, definitionId);

		section.setTemplateName(templateName);
		section.setShowLink(showLink);
		section.setHideDetail(hideDetail);
		section.setHideView(hideView);
		section.setDispBorderInSection(dispBorderInSection);

		return section;
	}
}
