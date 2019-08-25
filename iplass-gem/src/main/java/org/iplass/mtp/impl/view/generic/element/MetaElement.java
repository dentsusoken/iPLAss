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

package org.iplass.mtp.impl.view.generic.element;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.view.generic.EntityViewHandler;
import org.iplass.mtp.impl.view.generic.element.property.MetaPropertyLayout;
import org.iplass.mtp.impl.view.generic.element.section.MetaSection;
import org.iplass.mtp.view.generic.element.BlankSpace;
import org.iplass.mtp.view.generic.element.Button;
import org.iplass.mtp.view.generic.element.Element;
import org.iplass.mtp.view.generic.element.Element.EditDisplayType;
import org.iplass.mtp.view.generic.element.Link;
import org.iplass.mtp.view.generic.element.ScriptingElement;
import org.iplass.mtp.view.generic.element.TemplateElement;
import org.iplass.mtp.view.generic.element.VirtualPropertyItem;
import org.iplass.mtp.view.generic.element.property.PropertyBase;
import org.iplass.mtp.view.generic.element.section.Section;

/**
 * 画面を構成する要素のメタデータ
 * @author lis3wg
 */
@XmlSeeAlso({MetaSection.class, MetaPropertyLayout.class, MetaButton.class, MetaTemplateElement.class,
	MetaScriptingElement.class, MetaLink.class, MetaBlankSpace.class, MetaVirtualProperty.class})
public abstract class MetaElement implements MetaData {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 3853311110953755541L;

	public static MetaElement createInstance(Element element) {
		if (element instanceof PropertyBase) {
			return MetaPropertyLayout.createInstance(element);
		} else if (element instanceof Section) {
			return MetaSection.createInstance(element);
		} else if (element instanceof BlankSpace) {
			return MetaBlankSpace.createInstance(element);
		} else if (element instanceof Button) {
			return MetaButton.createInstance(element);
		} else if (element instanceof Link) {
			return MetaLink.createInstance(element);
		} else if (element instanceof ScriptingElement) {
			return MetaScriptingElement.createInstance(element);
		} else if (element instanceof TemplateElement) {
			return MetaTemplateElement.createInstance(element);
		} else if (element instanceof VirtualPropertyItem) {
			return MetaVirtualProperty.createInstance(element);
		}
		return null;
	}

	/** 表示フラグ */
	private boolean dispFlag;

	/** 表示スクリプト  */
	private String dispScript;

	/** 編集時の表示タイプ */
	private EditDisplayType editDisplayType;
	
	/** ElementのRuntimeId */
	private String elementRuntimeId;

	/**
	 * 表示フラグを取得します。
	 * @return 表示フラグ
	 */
	public boolean isDispFlag() {
		return dispFlag;
	}

	/**
	 * 表示フラグを設定します。
	 * @param dispFlag 表示フラグ
	 */
	public void setDispFlag(boolean dispFlag) {
		this.dispFlag = dispFlag;
	}

	/**
	 * 表示スクリプトを取得します。
	 * 
	 * @return the 表示スクリプト
	 */
	public String getDispScript() {
		return dispScript;
	}

	/**
	 * 表示スクリプトを設定します。
	 * @param dispScript 表示スクリプト
	 */
	public void setDispScript(String dispScript) {
		this.dispScript = dispScript;
	}

	/**
	 * 編集時の表示タイプを取得します。
	 * @return 編集時の表示タイプ
	 */
	public EditDisplayType getEditDisplayType() {
		return editDisplayType;
	}

	/**
	 * 編集時の表示タイプを設定します。
	 * @param editDisplayType 編集時の表示タイプ
	 */
	public void setEditDisplayType(EditDisplayType editDisplayType) {
		this.editDisplayType = editDisplayType;
	}

	/**
	 * ElementのRuntimeIdを取得します。
	 * @return ElementのRuntimeId
	 */
	@XmlTransient
	public String getElementRuntimeId() {
		return elementRuntimeId;
	}

	/**
	 * ElementのRuntimeIdを設定します。
	 * @param elementRuntimeId ElementのRuntimeId
	 */
	public void setElementRuntimeId(String elementRuntimeId) {
		this.elementRuntimeId = elementRuntimeId;
	}

	/**
	 * エレメントの設定をメタデータに反映します。
	 * @param element エレメント定義
	 * @param definitionId Entity定義のID
	 */
	public abstract void applyConfig(Element element, String definitionId);

	/**
	 * エレメント共通の設定をメタデータに反映します。
	 * @param element エレメント定義
	 * @param definitionId Entity定義のID
	 */
	protected void fillFrom(Element element, String definitionId) {
		this.dispFlag = element.isDispFlag();
		this.dispScript = element.getDispScript();
		this.editDisplayType = element.getEditDisplayType();
	}

	/**
	 * メタデータからエレメントを生成します。
	 * @param definitionId Entity定義のID
	 * @return エレメント
	 */
	public abstract Element currentConfig(String definitionId);

	/**
	 * メタデータ共通の設定をエレメントに反映します。
	 * @param element エレメント定義
	 * @param definitionId Entity定義のID
	 */
	protected void fillTo(Element element, String definitionId) {
		element.setDispFlag(dispFlag);
		element.setDispScript(dispScript);
		element.setEditDisplayType(editDisplayType);
		element.setElementRuntimeId(elementRuntimeId);
	}

	/**
	 * ランタイムを生成します。
	 * @param entityView 画面定義
	 * @return ランタイム
	 */
	public ElementHandler createRuntime(EntityViewHandler entityView) {
		return new ElementHandler(this, entityView);
	}

}
