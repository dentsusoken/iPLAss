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
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.i18n.MetaLocalizedString;
import org.iplass.mtp.impl.script.GroovyScriptEngine;
import org.iplass.mtp.impl.script.template.GroovyTemplate;
import org.iplass.mtp.impl.script.template.GroovyTemplateCompiler;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.view.generic.EntityViewRuntime;
import org.iplass.mtp.impl.view.generic.FormViewRuntime;
import org.iplass.mtp.impl.view.generic.HasMetaNestProperty;
import org.iplass.mtp.impl.view.generic.editor.MetaNestProperty;
import org.iplass.mtp.impl.view.generic.editor.MetaPropertyEditor;
import org.iplass.mtp.impl.view.generic.editor.MetaPropertyEditor.PropertyEditorRuntime;
import org.iplass.mtp.impl.view.generic.element.ElementRuntime;
import org.iplass.mtp.impl.view.generic.element.MetaButton;
import org.iplass.mtp.impl.view.generic.element.MetaButton.ButtonRuntime;
import org.iplass.mtp.impl.view.generic.element.MetaElement;
import org.iplass.mtp.impl.view.generic.element.MetaLink;
import org.iplass.mtp.impl.view.generic.element.MetaLink.LinkRuntime;
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
	/** dispBorderInSectionのデフォルト設定 */
	@Deprecated
	private static boolean defaultDispBorderInSection;

	static {
		// システムプロパティorデフォルトtrueで初期化
		String value = System.getProperty("mtp.generic.dispBorderInSection", "true");
		defaultDispBorderInSection = Boolean.parseBoolean(value);
	}


	public static MetaDefaultSection createInstance(Element element) {
		return new MetaDefaultSection();
	}

	/** タイトル */
	private String title;

	/** 多言語設定情報 */
	private List<MetaLocalizedString> localizedTitleList = new ArrayList<>();

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

	/** セクション内に配置した場合に枠線を表示 */
	private boolean dispBorderInSection = defaultDispBorderInSection;

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
	 * 要素情報を取得します。
	 * @return 要素情報
	 */
	public List<MetaElement> getElements() {
		if (this.elements == null) this.elements = new ArrayList<>();
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
		if (this.elements == null) this.elements = new ArrayList<>();
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
		this.dispBorderInSection = section.isDispBorderInSection();
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
		section.setDispBorderInSection(this.dispBorderInSection);
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
					if (e != null) section.addElement(e);
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
	public DefaultSectionRuntime createRuntime(EntityViewRuntime entityView, FormViewRuntime formView) {
		return new DefaultSectionRuntime(this, entityView, formView);
	}

	/**
	 * ランタイム
	 * @author lis3wg
	 */
	public class DefaultSectionRuntime extends SectionRuntime implements HasSectionPropertyRuntimeMap {
		// TODO 要素情報の取得意味が不明
		/** 要素情報*/
		private List<ElementRuntime> elements;

		/** セクションに設定されているプロパティ情報。キーはプロパティ名。 */
		private Map<String, SectionPropertyRuntime> sectionPropertyRuntimeMap = new HashMap<>();

		/**
		 * コンストラクタ
		 * @param metadata メタデータ
		 * @param entityView 画面定義
		 */
		public DefaultSectionRuntime(MetaDefaultSection metadata, EntityViewRuntime entityView, FormViewRuntime formView) {
			super(metadata, entityView);

			EntityContext context = EntityContext.getCurrentContext();
			EntityHandler eh = context.getHandlerById(entityView.getMetaData().getDefinitionId());

			elements = new ArrayList<>();
			Map<String, GroovyTemplate> customStyleMap = new HashMap<>();
			for (MetaElement element : metadata.getElements()) {
				ElementRuntime elementRuntime = element.createRuntime(entityView, formView);
				elements.add(elementRuntime);

				if (element instanceof MetaPropertyLayout) {
					MetaPropertyLayout propertyLayout = (MetaPropertyLayout)element;
					MetaPropertyEditor editor = propertyLayout.getEditor();

					// element が MetaPropertyLaytout であれば、propertyRuntime として管理する。
					// プロパティIDからプロパティ名に変換
					String propertyName = propertyLayout.convertName(propertyLayout.getPropertyId(), context, eh);
					// プロパティランタイム管理用インスタンスのビルダーを作成
					SectionPropertyRuntimeBuilder builder = new SectionPropertyRuntimeBuilder(propertyName);
					builder.element(elementRuntime);

					if (editor != null) {
						PropertyEditorRuntime runtime = (PropertyEditorRuntime)editor.createRuntime(entityView, formView, propertyLayout, context, eh);
						customStyleMap.put(editor.getOutputCustomStyleScriptKey(), runtime.getOutputCustomStyleScript());
						customStyleMap.put(editor.getInputCustomStyleScriptKey(), runtime.getInputCustomStyleScript());

						// PropertyEditorRuntime を設定
						builder.editor(runtime);

						if (editor instanceof HasMetaNestProperty) {
							// ネストプロパティの場合は、ネスト先プロパティの情報を管理する。
							// ネストエンティティのエンティティハンドラ
							EntityHandler nestEh = context.getHandlerById(((HasMetaNestProperty) editor).getObjectId());
							// プロパティ情報に格納するプロパティインスタンス
							Map<String, SectionPropertyRuntime> nestProperties = new HashMap<>();
							for (MetaNestProperty nest : ((HasMetaNestProperty)editor).getNestProperties()) {
								MetaPropertyEditor nestEditor = nest.getEditor();

								// ネストプロパティランタイム管理用インスタンスのビルダーを作成
								// ネストプロパティの場合は、通常プロパティの element に該当する情報が無いので、設定されない。
								String nestPropertyName = nest.convertName(nest.getPropertyId(), context, nestEh);
								SectionPropertyRuntimeBuilder nestBuilder = new SectionPropertyRuntimeBuilder(nestPropertyName);

								if (nestEditor != null) {
									//TODO nest type check
									PropertyEditorRuntime nestRuntime = (PropertyEditorRuntime)nestEditor.createRuntime(entityView, formView, null, context, eh);
									customStyleMap.put(nestEditor.getOutputCustomStyleScriptKey(), nestRuntime.getOutputCustomStyleScript());
									customStyleMap.put(nestEditor.getInputCustomStyleScriptKey(), nestRuntime.getInputCustomStyleScript());

									// PropertyEditorRuntime を設定
									nestBuilder.editor(nestRuntime);
								}
								nestProperties.put(nestPropertyName, nestBuilder.build());
							}

							// ネストプロパティランタイム情報を、ビルダーに設定
							builder.nest(nestProperties);
						}
					}

					// プロパティランタイムを生成
					sectionPropertyRuntimeMap.put(propertyName, builder.build());
				}

				if (element instanceof MetaButton) {
					MetaButton button = (MetaButton)element;
					ButtonRuntime runtime = button.createRuntime(entityView, formView);
					customStyleMap.put(button.getInputCustomStyleScriptKey(), runtime.getInputCustomStyleScript());
				}
				if (element instanceof MetaLink) {
					MetaLink link = (MetaLink)element;
					LinkRuntime runtime = link.createRuntime(entityView, formView);
					customStyleMap.put(link.getInputCustomStyleScriptKey(), runtime.getInputCustomStyleScript());
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

		@Override
		public Map<String, SectionPropertyRuntime> getSectionPropertyRuntimeMap() {
			return sectionPropertyRuntimeMap;
		}
	}
}
