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

import javax.xml.bind.annotation.XmlSeeAlso;

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
import org.iplass.mtp.view.generic.element.section.TemplateSection;
import org.iplass.mtp.view.generic.element.section.VersionSection;

/**
 * セクションのメタデータ
 * @author lis3wg
 */
@XmlSeeAlso({MetaTemplateSection.class, MetaDefaultSection.class, MetaScriptingSection.class,
	MetaVersionSection.class, MetaSearchConditionSection.class, MetaSearchResultSection.class,
	MetaReferenceSection.class, MetaMassReferenceSection.class})
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

	@Override
	protected void fillFrom(Element element, String definitionId) {
		super.fillFrom(element, definitionId);
	}

	@Override
	protected void fillTo(Element element, String definitionId) {
		super.fillTo(element, definitionId);
	}

	@Override
	public SectionRuntime createRuntime(EntityViewRuntime entityView, FormViewRuntime formView) {
		return new SectionRuntime(this, entityView);
	}
}
