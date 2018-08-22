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

package org.iplass.mtp.view.generic.editor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlEnumValue;

import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.adminconsole.view.annotation.generic.EntityViewField;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.view.generic.Jsp;
import org.iplass.mtp.view.generic.Jsps;
import org.iplass.mtp.view.generic.ViewConst;

/**
 * テンプレートを表示するプロパティエディタ
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Jsps({
	@Jsp(path="/jsp/gem/generic/editor/TemplatePropertyEditor.jsp", key=ViewConst.DESIGN_TYPE_GEM)
})
public class TemplatePropertyEditor extends CustomPropertyEditor {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 5338674344581643775L;

	/** 表示タイプ */
	public enum TemplateDisplayType {
		@XmlEnumValue("Template")TEMPLATE
	}

	/** テンプレート名 */
	@MetaFieldInfo(
			displayName="テンプレート名",
			displayNameKey="generic_editor_TemplatePropertyEditor_templateNameDisplaNameKey",
			inputType=InputType.TEMPLATE,
			required=true,
			description="表示時に読み込むテンプレートの名前を設定します。",
			descriptionKey="generic_editor_TemplatePropertyEditor_templateNameDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.ALL}
	)
	private String templateName;

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

	@Override
	public Enum<?> getDisplayType() {
		return TemplateDisplayType.TEMPLATE;
	}
}
