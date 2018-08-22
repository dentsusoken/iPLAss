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
import java.util.stream.Collectors;

import org.iplass.mtp.impl.script.template.GroovyTemplate;
import org.iplass.mtp.impl.script.template.GroovyTemplateCompiler;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.view.generic.EntityViewHandler;
import org.iplass.mtp.impl.view.generic.editor.MetaPropertyEditor;
import org.iplass.mtp.impl.view.generic.editor.MetaPropertyEditor.PropertyEditorHandler;
import org.iplass.mtp.impl.view.generic.element.MetaElement;
import org.iplass.mtp.impl.view.generic.element.property.MetaPropertyColumn;
import org.iplass.mtp.view.generic.PagingPosition;
import org.iplass.mtp.view.generic.element.Element;
import org.iplass.mtp.view.generic.element.section.SearchResultSection;

/**
 * 検索結果セクションのメタデータ
 * @author lis3wg
 */
public class MetaSearchResultSection extends MetaSection {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -4519634456498868653L;

	public static MetaSearchResultSection createInstance(Element element) {
		return new MetaSearchResultSection();
	}

	/** 表示行数 */
	private int dispRowCount;

	/** 編集リンク非表示設定 */
	private boolean hideDetailLink;

	/** 削除ボタン非表示設定 */
	private boolean hideDelete;

	/** ページング非表示設定 */
	private boolean hidePaging;

	/** ページジャンプ非表示設定 */
	private boolean hidePageJump;

	/** ページリンク非表示設定 */
	private boolean hidePageLink;

	/** 件数非表示設定 */
	private boolean hideCount;

	/** ページング表示位置 */
	private PagingPosition pagingPosition;

	/** 要素 */
	private List<MetaElement> elements;

	/** スクリプトのキー(内部用) */
	private String scriptKey;

	/**
	 * 表示行数を取得します。
	 * @return 表示行数
	 */
	public int getDispRowCount() {
		return dispRowCount;
	}

	/**
	 * 表示行数を設定します。
	 * @param dispRowCount 表示行数
	 */
	public void setDispRowCount(int dispRowCount) {
		this.dispRowCount = dispRowCount;
	}

	/**
	 * 編集リンク非表示設定を取得します。
	 * @return 編集リンク非表示設定
	 */
	public boolean isHideDetailLink() {
	    return hideDetailLink;
	}

	/**
	 * 編集リンク非表示設定を設定します。
	 * @param hideDetailLink 編集リンク非表示設定
	 */
	public void setHideDetailLink(boolean hideDetailLink) {
	    this.hideDetailLink = hideDetailLink;
	}

	/**
	 * 削除ボタン非表示設定を取得します。
	 * @return 削除ボタン非表示設定
	 */
	public boolean isHideDelete() {
	    return hideDelete;
	}

	/**
	 * 削除ボタン非表示設定を設定します。
	 * @param hideDelete 削除ボタン非表示設定
	 */
	public void setHideDelete(boolean hideDelete) {
	    this.hideDelete = hideDelete;
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
	 * 要素を取得します。
	 * @return 要素
	 */
	public List<MetaElement> getElements() {
		if (elements == null) elements = new ArrayList<MetaElement>();
	    return elements;
	}

	/**
	 * 要素を設定します。
	 * @param elements 要素
	 */
	public void setElements(List<MetaElement> elements) {
	    this.elements = elements;
	}

	/**
	 * 要素を追加。
	 * @param element 要素
	 */
	public void addElement(MetaElement element) {
		getElements().add(element);
	}

	@Override
	public MetaSearchResultSection copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public void applyConfig(Element element, String definitionId) {
		super.fillFrom(element, definitionId);

		SearchResultSection section = (SearchResultSection) element;
		this.dispRowCount = section.getDispRowCount();
		this.hideDetailLink = section.isHideDetailLink();
		this.hideDelete = section.isHideDelete();
		this.hidePaging = section.isHidePaging();
		this.hidePageJump = section.isHidePageJump();
		this.hidePageLink = section.isHidePageLink();
		this.hideCount = section.isHideCount();
		this.pagingPosition = section.getPagingPosition();
		// 仮想プロパティ追加によりMetaPropertyからMetaElementへフィールドを変更
//		if (section.getProperties().size() > 0) {
//			for (PropertyColumn col : section.getProperties()) {
//				MetaPropertyColumn p = new MetaPropertyColumn();
//				p.applyConfig(col, definitionId);
//				if (p.getPropertyId() != null) this.addProperty(p);
//			}
//		}
		if (section.getElements().size() > 0) {
			for (Element elem : section.getElements()) {
				MetaElement e = MetaElement.createInstance(elem);
				e.applyConfig(elem, definitionId);
				this.addElement(e);
			}
		}
	}

	@Override
	public Element currentConfig(String definitionId) {
		SearchResultSection section = new SearchResultSection();
		super.fillTo(section, definitionId);

		section.setScriptKey(scriptKey);
		section.setDispRowCount(this.dispRowCount);
		section.setHideDetailLink(hideDetailLink);
		section.setHideDelete(hideDelete);
		section.setHidePaging(hidePaging);
		section.setHidePageJump(this.hidePageJump);
		section.setHidePageLink(this.hidePageLink);
		section.setHideCount(this.hideCount);
		section.setPagingPosition(pagingPosition);

		if (this.getElements().size() > 0) {
			for (MetaElement elem : this.getElements()) {
				Element e = elem.currentConfig(definitionId);
				section.addElement(e);
			}
		}
		return section;
	}
	@Override
	public SearchResultSectionHandler createRuntime(EntityViewHandler entityView) {
		return new SearchResultSectionHandler(this, entityView);
	}

	/**
	 * ランタイム
	 */
	public class SearchResultSectionHandler extends SectionHandler {

		/**
		 * コンストラクタ
		 * @param metadata メタデータ
		 * @param entityView 画面定義
		 */
		public SearchResultSectionHandler(MetaSearchResultSection metadata, EntityViewHandler entityView) {
			super(metadata, entityView);

			Map<String, GroovyTemplate> customStyleMap = new HashMap<>();
			List<MetaPropertyColumn> properties = metadata.getElements().stream()
					.filter(e -> e instanceof MetaPropertyColumn)
					.map(e -> (MetaPropertyColumn) e)
					.collect(Collectors.toList());

			for (MetaPropertyColumn metaPropertyColumn : properties) {
				MetaPropertyEditor editor = metaPropertyColumn.getEditor();
				PropertyEditorHandler handler = (PropertyEditorHandler)editor.createRuntime(entityView);
				customStyleMap.put(editor.getOutputCustomStyleScriptKey(), handler.getOutputCustomStyleScript());
			}
			//Script用のKEYを設定
			metadata.scriptKey = "SearchResultSection_Style_" + GroovyTemplateCompiler.randomName().replace("-", "_");

			//EntityViewに登録
			entityView.addCustomStyle(scriptKey , customStyleMap);
		}

		@Override
		public MetaSearchResultSection getMetaData() {
			return (MetaSearchResultSection) super.getMetaData();
		}
	}

}
