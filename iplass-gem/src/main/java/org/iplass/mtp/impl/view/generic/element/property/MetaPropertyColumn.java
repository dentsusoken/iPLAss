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

package org.iplass.mtp.impl.view.generic.element.property;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.entity.property.ReferencePropertyHandler;
import org.iplass.mtp.impl.view.generic.editor.MetaPropertyEditor;
import org.iplass.mtp.view.generic.NullOrderType;
import org.iplass.mtp.view.generic.RequiredDisplayType;
import org.iplass.mtp.view.generic.TextAlign;
import org.iplass.mtp.view.generic.editor.DateRangePropertyEditor;
import org.iplass.mtp.view.generic.editor.JoinPropertyEditor;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor;
import org.iplass.mtp.view.generic.element.Element;
import org.iplass.mtp.view.generic.element.property.PropertyColumn;

/**
 * プロパティレイアウト情報のメタデータ
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class MetaPropertyColumn extends MetaPropertyLayout {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1940139489715422445L;

	public static MetaPropertyColumn createInstance(Element element) {
		return new MetaPropertyColumn();
	}

	/** 列幅 */
	private int width;

	/** null項目のソート順 */
	private NullOrderType nullOrderType;

	/** テキストの配置 */
	private TextAlign textAlign;

	/** 一括更新プロパティエディタ  */
	private MetaPropertyEditor bulkUpdateEditor;

	/** 一括更新必須属性表示タイプ */
	private RequiredDisplayType bulkUpdateRequiredDisplayType;

	/**
	 * 列幅を取得します。
	 * @return 列幅
	 */
	public int getWidth() {
	    return width;
	}

	/**
	 * 列幅を設定します。
	 * @param width 列幅
	 */
	public void setWidth(int width) {
	    this.width = width;
	}

	/**
	 * null項目のソート順を取得します。
	 * @return null項目のソート順
	 */
	public NullOrderType getNullOrderType() {
		return nullOrderType;
	}

	/**
	 * null項目のソート順を設定します。
	 * @param nullOrderType null項目のソート順
	 */
	public void setNullOrderType(NullOrderType nullOrderType) {
		this.nullOrderType = nullOrderType;
	}

	/**
	 * テキストの配置を取得します。
	 * @return テキストの配置
	 */
	public TextAlign getTextAlign() {
	    return textAlign;
	}

	/**
	 * テキストの配置を設定します。
	 * @param textAlign テキストの配置
	 */
	public void setTextAlign(TextAlign textAlign) {
	    this.textAlign = textAlign;
	}

	/**
	 * 一括更新プロパティエディタを取得します。
	 * @return 一括更新プロパティエディタ
	 */
	public MetaPropertyEditor getBulkUpdateEditor() {
		return bulkUpdateEditor;
	}

	/**
	 * 一括更新プロパティエディタを設定します。
	 * @param bulkUpdateEditor 一括更新プロパティエディタ
	 */
	public void setBulkUpdateEditor(MetaPropertyEditor bulkUpdateEditor) {
		this.bulkUpdateEditor = bulkUpdateEditor;
	}

	/**
	 * 一括更新必須属性表示タイプを取得します。
	 * @return 一括更新必須属性表示タイプ
	 */
	public RequiredDisplayType getBulkUpdateRequiredDisplayType() {
		return bulkUpdateRequiredDisplayType;
	}

	/**
	 * 一括更新必須属性表示タイプを設定します。
	 * @param requiredDisplayType 一括更新必須属性表示タイプ
	 */
	public void setBulkUpdateRequiredDisplayType(RequiredDisplayType bulkUpdateRequiredDisplayType) {
		this.bulkUpdateRequiredDisplayType = bulkUpdateRequiredDisplayType;
	}

	@Override
	public void applyConfig(Element element, String definitionId) {
		super.fillFrom(element, definitionId);
		PropertyColumn p = (PropertyColumn) element;
		width = p.getWidth();
		nullOrderType =  p.getNullOrderType();
		textAlign = p.getTextAlign();

		if(p.getBulkUpdateEditor() != null) {
			MetaPropertyEditor editor = MetaPropertyEditor.createInstance(p.getBulkUpdateEditor());
			EntityContext context = EntityContext.getCurrentContext();
			EntityHandler entity = context.getHandlerById(definitionId);

			fillCustomPropertyEditor(p.getBulkUpdateEditor(),p.getPropertyName(), context, entity);

			if (editor != null) {
				p.getBulkUpdateEditor().setPropertyName(p.getPropertyName());
				editor.applyConfig(p.getBulkUpdateEditor());
				this.bulkUpdateEditor = editor;
			}
		}

		bulkUpdateRequiredDisplayType = p.getBulkUpdateRequiredDisplayType();
	}

	@Override
	public Element currentConfig(String definitionId) {
		PropertyColumn p = new PropertyColumn();
		super.fillTo(p, definitionId);

		//ReferencePropertyEditorは参照先Entityがない場合null。
		//その場合はElementもnullで返す。
		if (p.getEditor() == null) {
			return null;
		}

		p.setWidth(width);
		p.setNullOrderType(nullOrderType);
		p.setTextAlign(textAlign);
		if (bulkUpdateEditor != null) {
			p.setBulkUpdateEditor(bulkUpdateEditor.currentConfig(p.getPropertyName()));
		}
		p.setBulkUpdateRequiredDisplayType(bulkUpdateRequiredDisplayType);
		return p;
	}

}
