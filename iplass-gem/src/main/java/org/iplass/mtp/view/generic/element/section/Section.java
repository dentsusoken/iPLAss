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

package org.iplass.mtp.view.generic.element.section;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.iplass.adminconsole.view.annotation.FieldOrder;
import org.iplass.mtp.view.generic.element.Element;

/**
 * フォーム内の要素を複数保持できるセクション
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({TemplateSection.class, DefaultSection.class, ScriptingSection.class, VersionSection.class,
	SearchConditionSection.class, SearchResultSection.class, ReferenceSection.class, MassReferenceSection.class})
@FieldOrder(manual=true)
public abstract class Section extends Element {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -8864732096685759293L;

	public abstract boolean isShowLink();
}
