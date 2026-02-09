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
import java.util.List;

import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.i18n.MetaLocalizedString;
import org.iplass.mtp.impl.view.generic.EntityViewRuntime;
import org.iplass.mtp.impl.view.generic.FormViewRuntime;
import org.iplass.mtp.impl.view.generic.element.MetaElement;
import org.iplass.mtp.view.generic.element.Element;
import org.iplass.mtp.view.generic.element.section.DefaultSection;
import org.iplass.mtp.view.generic.element.section.MassReferenceSection;
import org.iplass.mtp.view.generic.element.section.ReferenceSection;
import org.iplass.mtp.view.generic.element.section.ScriptingSection;
import org.iplass.mtp.view.generic.element.section.SearchConditionSection;
import org.iplass.mtp.view.generic.element.section.SearchResultSection;
import org.iplass.mtp.view.generic.element.section.Section;
import org.iplass.mtp.view.generic.element.section.TemplateSection;
import org.iplass.mtp.view.generic.element.section.VersionSection;

import jakarta.xml.bind.annotation.XmlSeeAlso;

/**
 * セクションのメタデータ
 * @author lis3wg
 */
@XmlSeeAlso({ MetaTemplateSection.class, MetaDefaultSection.class, MetaScriptingSection.class,
		MetaVersionSection.class, MetaSearchConditionSection.class, MetaReferenceSection.class,
		MetaMassReferenceSection.class, MetaSearchResultSection.class })
public abstract class MetaSection extends MetaElement {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -4605181972367145646L;

	public static MetaSection createInstance(Element element) {
		if (element instanceof DefaultSection) {
			return MetaDefaultSection.createInstance(element);
		} else if (element instanceof ReferenceSection) {
			return MetaReferenceSection.createInstance(element);
		} else if (element instanceof MassReferenceSection) {
			return MetaMassReferenceSection.createInstance(element);
		} else if (element instanceof ScriptingSection) {
			return MetaScriptingSection.createInstance(element);
		} else if (element instanceof SearchConditionSection) {
			return MetaSearchConditionSection.createInstance(element);
		} else if (element instanceof SearchResultSection) {
			return MetaSearchResultSection.createInstance(element);
		} else if (element instanceof TemplateSection) {
			return MetaTemplateSection.createInstance(element);
		} else if (element instanceof VersionSection) {
			return MetaVersionSection.createInstance(element);
		}
		return null;
	}

	/** タイトル */
	private String title;

	/** 多言語設定情報 */
	private List<MetaLocalizedString> localizedTitleList = new ArrayList<>();

	/** id */
	private String id;

	/** クラス名 */
	private String style;

	/** セクション高さ設定（px） */
	private Integer sectionHeight;

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
	 * セクション高さ設定（px）を取得します。
	 * @return セクション高さ設定（px）
	 */
	public Integer getSectionHeight() {
		return sectionHeight;
	}

	/**
	 * セクション高さ設定（px）を設定します。
	 * @param sectionHeight セクション高さ設定（px）
	 */
	public void setSectionHeight(Integer sectionHeight) {
		this.sectionHeight = sectionHeight;
	}

	@Override
	protected void fillFrom(Element element, String definitionId) {
		super.fillFrom(element, definitionId);

		Section section = (Section) element;
		this.title = section.getTitle();
		this.id = section.getId();
		this.style = section.getStyle();
		this.sectionHeight = section.getSectionHeight();

		// 言語毎の文字情報設定
		localizedTitleList = I18nUtil.toMeta(section.getLocalizedTitleList());
	}

	@Override
	protected void fillTo(Element element, String definitionId) {
		super.fillTo(element, definitionId);

		Section section = (Section) element;
		section.setTitle(this.title);
		section.setId(this.id);
		section.setStyle(this.style);
		section.setSectionHeight(this.sectionHeight);

		section.setLocalizedTitleList(I18nUtil.toDef(localizedTitleList));
	}

	@Override
	public SectionRuntime createRuntime(EntityViewRuntime entityView, FormViewRuntime formView) {
		return new SectionRuntime(this, entityView);
	}
}
