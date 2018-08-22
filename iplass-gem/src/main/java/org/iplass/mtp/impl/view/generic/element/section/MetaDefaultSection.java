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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.i18n.MetaLocalizedString;
import org.iplass.mtp.impl.script.GroovyScriptEngine;
import org.iplass.mtp.impl.script.template.GroovyTemplate;
import org.iplass.mtp.impl.script.template.GroovyTemplateCompiler;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.view.generic.EntityViewHandler;
import org.iplass.mtp.impl.view.generic.editor.HasNestProperty;
import org.iplass.mtp.impl.view.generic.editor.MetaNestProperty;
import org.iplass.mtp.impl.view.generic.editor.MetaPropertyEditor;
import org.iplass.mtp.impl.view.generic.editor.MetaPropertyEditor.PropertyEditorHandler;
import org.iplass.mtp.impl.view.generic.element.ElementHandler;
import org.iplass.mtp.impl.view.generic.element.MetaButton;
import org.iplass.mtp.impl.view.generic.element.MetaButton.ButtonHandler;
import org.iplass.mtp.impl.view.generic.element.MetaElement;
import org.iplass.mtp.impl.view.generic.element.MetaLink;
import org.iplass.mtp.impl.view.generic.element.MetaLink.LinkHandler;
import org.iplass.mtp.impl.view.generic.element.property.MetaPropertyItem;
import org.iplass.mtp.impl.view.generic.element.property.MetaPropertyLayout;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.element.Element;
import org.iplass.mtp.view.generic.element.property.PropertyBase;
import org.iplass.mtp.view.generic.element.property.PropertyItem;
import org.iplass.mtp.view.generic.element.section.DefaultSection;
import org.iplass.mtp.view.generic.element.section.MassReferenceSection;
import org.iplass.mtp.view.generic.element.section.ReferenceSection;
import org.iplass.mtp.view.generic.element.section.Section;

/**
 * 標準セクションのメタデータ
 * @author lis3wg
 */
public class MetaDefaultSection extends MetaSection {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 6368546528151431462L;

	public static MetaDefaultSection createInstance(Element element) {
		return new MetaDefaultSection();
	}

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

	/** 上部のコンテンツ */
	private String upperContents;

	/** 下部のコンテンツ */
	private String lowerContents;

	/** 要素情報 */
	private List<MetaElement> elements;

	/** カスタムスタイルスクリプトのキー(内部用) */
	private String styleScriptKey;

	/** 上下コンテンツスクリプトのキー(内部用) */
	private String contentScriptKey;

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
	 * 要素情報を取得します。
	 * @return 要素情報
	 */
	public List<MetaElement> getElements() {
		if (this.elements == null) this.elements = new ArrayList<MetaElement>();
		return elements;
	}

	/**
	 * 要素情報を設定します。
	 * @param elements 要素情報
	 */
	public void setElements(List<MetaElement> elements) {
		this.elements = elements;
	}

	/**
	 * 要素情報を追加。
	 * @param val 要素情報
	 */
	public void addElement(MetaElement element) {
		if (this.elements == null) this.elements = new ArrayList<MetaElement>();
		this.elements.add(element);
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
	public MetaDefaultSection copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public void applyConfig(Element element, String definitionId) {
		super.fillFrom(element, definitionId);

		DefaultSection section = (DefaultSection) element;
		this.title = section.getTitle();
		this.expandable = section.isExpandable();
		this.id = section.getId();
		this.style = section.getStyle();
		this.colNum = section.getColNum();
		this.showLink = section.isShowLink();
		this.hideDetail = section.isHideDetail();
		this.hideView = section.isHideView();
		this.upperContents = section.getUpperContents();
		this.lowerContents = section.getLowerContents();
		if (section.getElements().size() > 0) {
			for (Element elem : section.getElements()) {
				MetaElement mElem = null;
				if (elem instanceof Section) {
					mElem = fillFromSection(elem, definitionId);
				} else if (elem instanceof PropertyItem) {
					mElem = fillFromProperty(elem, definitionId);
				} else {
					mElem = fillFromElement(elem, definitionId);
				}
				if (mElem != null) this.addElement(mElem);
			}
		}

		// 言語毎の文字情報設定
		localizedTitleList = I18nUtil.toMeta(section.getLocalizedTitleList());
	}

	/**
	 * 要素情報を要素情報のメタデータに適用。
	 * @param element 要素情報
	 * @param definitionId Entity定義のID
	 * @return メタデータ
	 */
	private MetaElement fillFromElement(Element element, String definitionId) {
		MetaElement mElem = MetaElement.createInstance(element);
		mElem.applyConfig(element, definitionId);
		return mElem;
	}

	/**
	 * 要素情報をプロパティレイアウトのメタデータに適用。
	 * @param element 要素情報
	 * @param definitionId Entity定義のID
	 * @return メタデータ
	 */
	private MetaPropertyLayout fillFromProperty(Element element, String definitionId) {
		MetaPropertyItem mProp = new MetaPropertyItem();
		mProp.applyConfig(element, definitionId);
		if (mProp.getPropertyId() == null) mProp = null;
		return mProp;
	}

	/**
	 * 要素情報をセクションのメタデータに適用。
	 * @param element 要素情報
	 * @param definitionId Entity定義のID
	 * @return メタデータ
	 */
	private MetaSection fillFromSection(Element element, String definitionId) {
		MetaSection meta = MetaSection.createInstance(element);
		meta.applyConfig(element, definitionId);
		if (element instanceof ReferenceSection) {
			MetaReferenceSection rs = (MetaReferenceSection) meta;
			if (rs.getPropertyId() == null) return null;
		} else if (element instanceof MassReferenceSection) {
			MetaMassReferenceSection ms = (MetaMassReferenceSection) meta;
			if (ms.getPropertyId() == null) return null;
		}
		return meta;
	}

	@Override
	public Element currentConfig(String definitionId) {
		DefaultSection section = new DefaultSection();
		super.fillTo(section, definitionId);

		section.setStyleScriptKey(styleScriptKey);
		section.setContentScriptKey(contentScriptKey);

		section.setExpandable(this.expandable);
		section.setTitle(this.title);
		section.setId(this.id);
		section.setStyle(this.style);
		section.setColNum(this.colNum);
		section.setShowLink(this.showLink);
		section.setHideDetail(hideDetail);
		section.setHideView(hideView);
		section.setUpperContents(this.upperContents);
		section.setLowerContents(this.lowerContents);
		if (this.getElements().size() > 0) {
			for (MetaElement elem : this.getElements()) {
				if (elem instanceof MetaSection) {
					Section subSection = fillToSection(elem, definitionId);
					if (subSection != null) section.addElement(subSection);
				} else if (elem instanceof MetaPropertyLayout) {
					PropertyBase p = fillToProperty(elem, definitionId);
					if (p != null) section.addElement(p);
				} else {
					Element e = fillToElement(elem, definitionId);
					section.addElement(e);
				}
			}
		}

		section.setLocalizedTitleList(I18nUtil.toDef(localizedTitleList));

		return section;
	}

	/**
	 * 要素情報のメタデータを要素情報に適用。
	 * @param element 要素情報
	 * @param definitionId Entity定義のID
	 * @return 要素情報
	 */
	private Element fillToElement(MetaElement element, String definitionId) {
		Element elem = element.currentConfig(definitionId);
		return elem;
	}

	/**
	 * 要素情報のメタデータをプロパティレイアウトに適用。
	 * @param element 要素情報
	 * @param definitionId Entity定義のID
	 * @return プロパティレイアウト
	 */
	private PropertyBase fillToProperty(MetaElement element, String definitionId) {
		MetaPropertyLayout mp = (MetaPropertyLayout) element;
		PropertyItem property = (PropertyItem) mp.currentConfig(definitionId);
		if (property == null || property.getPropertyName() == null) property = null;
		return property;
	}

	/**
	 * 要素情報のメタデータをセクションに適用。
	 * @param element 要素情報
	 * @param definitionId Entity定義のID
	 * @return セクション
	 */
	private Section fillToSection(MetaElement element, String definitionId) {
		MetaSection ms = (MetaSection) element;
		Section section = (Section) ms.currentConfig(definitionId);
		return section;
	}

	@Override
	public DefaultSectionRuntime createRuntime(EntityViewHandler entityView) {
		return new DefaultSectionRuntime(this, entityView);
	}

	/**
	 * ランタイム
	 * @author lis3wg
	 */
	public class DefaultSectionRuntime extends SectionHandler {

		/** 要素情報*/
		private List<ElementHandler> elements;

		/**
		 * コンストラクタ
		 * @param metadata メタデータ
		 * @param entityView 画面定義
		 */
		public DefaultSectionRuntime(MetaDefaultSection metadata, EntityViewHandler entityView) {
			super(metadata, entityView);

			elements = new ArrayList<ElementHandler>();
			Map<String, GroovyTemplate> customStyleMap = new HashMap<>();
			for (MetaElement element : metadata.getElements()) {
				elements.add(element.createRuntime(entityView));

				if (element instanceof MetaPropertyLayout) {
					MetaPropertyEditor editor = ((MetaPropertyLayout)element).getEditor();
					if (editor != null) {
						PropertyEditorHandler handler = (PropertyEditorHandler)editor.createRuntime(entityView);
						customStyleMap.put(editor.getOutputCustomStyleScriptKey(), handler.getOutputCustomStyleScript());
						customStyleMap.put(editor.getInputCustomStyleScriptKey(), handler.getInputCustomStyleScript());

						if (editor instanceof HasNestProperty) {
							for (MetaNestProperty nest : ((HasNestProperty)editor).getNestProperties()) {
								MetaPropertyEditor nestEditor = nest.getEditor();
								if (nestEditor != null) {
									PropertyEditorHandler nestHandler = (PropertyEditorHandler)nestEditor.createRuntime(entityView);
									customStyleMap.put(nestEditor.getOutputCustomStyleScriptKey(), nestHandler.getOutputCustomStyleScript());
									customStyleMap.put(nestEditor.getInputCustomStyleScriptKey(), nestHandler.getInputCustomStyleScript());
								}
							}
						}
					}
				}
				if (element instanceof MetaButton) {
					MetaButton button = (MetaButton)element;
					ButtonHandler handler = button.createRuntime(entityView);
					customStyleMap.put(button.getInputCustomStyleScriptKey(), handler.getInputCustomStyleScript());
				}
				if (element instanceof MetaLink) {
					MetaLink link = (MetaLink)element;
					LinkHandler handler = link.createRuntime(entityView);
					customStyleMap.put(link.getInputCustomStyleScriptKey(), handler.getInputCustomStyleScript());
				}
			}
			//StyleScript用のKEYを設定
			metadata.styleScriptKey = "DefaultSection_Style_" + GroovyTemplateCompiler.randomName().replace("-", "_");
			//EntityViewに登録
			entityView.addCustomStyle(styleScriptKey , customStyleMap);

			//上下コンテンツのコンパイル
			if (StringUtil.isNotEmpty(metadata.upperContents) || StringUtil.isNotEmpty(metadata.lowerContents)) {
				if (metadata.contentScriptKey == null) {
					//ContentsScript用のKEYを設定
					metadata.contentScriptKey = "DefaultSection_content_" + GroovyTemplateCompiler.randomName().replace("-", "_");
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

		private GroovyTemplate compile(String script, String key) {
			TenantContext tenant = ExecuteContext.getCurrentContext().getTenantContext();
			return GroovyTemplateCompiler.compile(
					script, key, (GroovyScriptEngine) tenant.getScriptEngine());
		}
	}
}
