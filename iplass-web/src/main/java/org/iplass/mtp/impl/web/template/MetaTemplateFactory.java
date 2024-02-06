/*
 * Copyright (C) 2018 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.web.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.command.annotation.template.Template;
import org.iplass.mtp.definition.annotation.LocalizedString;
import org.iplass.mtp.impl.i18n.MetaLocalizedString;
import org.iplass.mtp.impl.metadata.annotation.AnnotatableMetaDataFactory;
import org.iplass.mtp.impl.metadata.annotation.AnnotateMetaDataEntry;
import org.iplass.mtp.util.StringUtil;


public class MetaTemplateFactory implements
		AnnotatableMetaDataFactory<Template, Object> {

	public MetaTemplateFactory() {
	}

	@Override
	public Class<Object> getAnnotatedClass() {
		return Object.class;
	}

	@Override
	public Class<Template> getAnnotationClass() {
		return Template.class;
	}

	@Override
	public Map<String, AnnotateMetaDataEntry> toMetaData(Class<Object> annotatedClass) {
		Template template = annotatedClass.getAnnotation(Template.class);
		return toMetaData(template, annotatedClass);
	}

	Map<String, AnnotateMetaDataEntry> toMetaData(Template template, Class<Object> annotatedClass) {
		Map<String, AnnotateMetaDataEntry> map = new HashMap<String, AnnotateMetaDataEntry>();
		//アノテーションはJSPテンプレートのみ定義可能
		MetaJspTemplate metaJspTemplate = new MetaJspTemplate();

		String path = TemplateService.TEMPLATE_META_PATH + template.name();

		metaJspTemplate.setName(template.name());
		if (!DEFAULT.equals(template.id())) {
			metaJspTemplate.setId(template.id());
		} else {
			metaJspTemplate.setId(path);
		}
		if (!DEFAULT.equals(template.displayName())) {
			metaJspTemplate.setDisplayName(template.displayName());
		}
		if (template.localizedDisplayName().length > 0) {
			List<MetaLocalizedString> localizedDisplayNameList = new ArrayList<>();
			for (LocalizedString localeValue : template.localizedDisplayName()) {
				MetaLocalizedString metaLocaleValue = new MetaLocalizedString();
				metaLocaleValue.setLocaleName(localeValue.localeName());
				metaLocaleValue.setStringValue(localeValue.stringValue());
				localizedDisplayNameList.add(metaLocaleValue);
			}
			metaJspTemplate.setLocalizedDisplayNameList(localizedDisplayNameList);
		}
		if (!DEFAULT.equals(template.description())) {
			metaJspTemplate.setDescription(template.description());
		}

		if (StringUtil.isNotEmpty(template.contentType())) {
			metaJspTemplate.setContentType(template.contentType());
		}

		if (StringUtil.isNotEmpty(template.path())) {
			metaJspTemplate.setPath(template.path());
		}

		if (StringUtil.isNotEmpty(template.layoutActionName())) {
			metaJspTemplate.setLayoutName(template.layoutActionName());
			metaJspTemplate.setLayoutResolveByName(true);
		}

		map.put(path, new AnnotateMetaDataEntry(metaJspTemplate, template.overwritable(), false));
		return map;
	}
}
