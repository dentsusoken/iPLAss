/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.gem.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.iplass.gem.command.generic.search.ResponseUtil;
import org.iplass.gem.command.generic.search.ResponseUtil.Func;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.EntityViewUtil;
import org.iplass.mtp.view.generic.OutputType;
import org.iplass.mtp.view.generic.ViewConst;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.element.Element;
import org.iplass.mtp.view.generic.element.VirtualPropertyItem;
import org.iplass.mtp.view.generic.element.property.PropertyColumn;
import org.iplass.mtp.view.generic.element.section.SearchResultSection;

public class CreateSearchResultUtil {
	public static List<Map<String, String>> getHtmlData(List<Entity> entityList, final EntityDefinition ed, SearchResultSection section, String viewName) throws IOException, ServletException {
		if (entityList == null) return null;

		List<Map<String, String>> ret = new ArrayList<Map<String,String>>();
		List<Element> elements = section.getElements();
		for (final Entity entity : entityList) {
			final Map<String, String> eval = new LinkedHashMap<String, String>();
			eval.put("orgOid", entity.getOid());
			eval.put("orgVersion", entity.getVersion().toString());

			if (entity.getValue("score") != null) {
				eval.put("score", entity.getValue("score").toString());
			}
			for (Element element : elements) {
				if (element instanceof PropertyColumn) {
					PropertyColumn property = (PropertyColumn) element;
					outputPropertyColumn(ed, section, entity, eval, property, viewName);
				} else if (element instanceof VirtualPropertyItem) {
					VirtualPropertyItem property = (VirtualPropertyItem) element;
					outputVirtualProperty(ed, section, entity, eval, property, viewName);
				}
			}
			ret.add(eval);
		}

		return ret;
	}

	private static void outputVirtualProperty(EntityDefinition ed, SearchResultSection section,
			Entity entity, Map<String, String> eval, VirtualPropertyItem property, String viewName)
					throws ServletException, IOException {
		if (isDispProperty(property)) {
			PropertyEditor editor = property.getEditor();
			String path = EntityViewUtil.getJspPath(editor, ViewConst.DESIGN_TYPE_GEM);
			if (path != null) {
				String propName = property.getPropertyName();
				PropertyDefinition pd = EntityViewUtil.getPropertyDefinition(property);

				String html = outputHtml(ed, section, entity, eval, propName, pd, editor, path, viewName);

				eval.put(propName, html);
			}
		}
	}

	private static void outputPropertyColumn(EntityDefinition ed, SearchResultSection section,
			Entity entity, Map<String, String> eval, PropertyColumn property, String viewName)
					throws ServletException, IOException {
		if (isDispProperty(property)) {
			PropertyEditor editor = property.getEditor();
			String path = EntityViewUtil.getJspPath(editor, ViewConst.DESIGN_TYPE_GEM);
			if (path != null) {
				String propName = property.getPropertyName();
				PropertyDefinition pd = EntityViewUtil.getPropertyDefinition(propName, ed);

				String html = outputHtml(ed, section, entity, eval, propName, pd, editor, path, viewName);

				WebRequestStack stack = WebRequestStack.getCurrent();
				HttpServletRequest req = stack.getRequest();
				Boolean isNest = (Boolean) req.getAttribute(Constants.EDITOR_REF_NEST_PROPERTY_PREFIX + propName);
				if (isNest != null && isNest) {
					eval.put(propName + ".name", html);
				} else {
					eval.put(propName, html);
				}
				req.setAttribute(Constants.EDITOR_REF_NEST_PROPERTY_PREFIX + propName, null);
			}
		}
	}

	private static String outputHtml(final EntityDefinition ed, SearchResultSection section, final Entity entity,
			final Map<String, String> eval, String propName, final PropertyDefinition pd, final PropertyEditor editor,
			String path, String viewName) throws ServletException, IOException {
		final Object propValue = entity.getValue(propName);
		Func beforeFunc = new Func() {

			@Override
			public void execute(HttpServletRequest req, HttpServletResponse res) {
				//リクエストに必要なパラメータを詰める
				req.setAttribute(Constants.OUTPUT_TYPE, OutputType.SEARCHRESULT);
				req.setAttribute(Constants.EDITOR_EDITOR, editor);
				req.setAttribute(Constants.ENTITY_DATA, entity);
				req.setAttribute(Constants.EDITOR_PROP_VALUE, propValue);
				req.setAttribute(Constants.ENTITY_DEFINITION, ed);
				req.setAttribute(Constants.EDITOR_PROPERTY_DEFINITION, pd);
				req.setAttribute(Constants.EDITOR_REF_ENTITY_VALUE_MAP, eval); // Reference型用
				req.setAttribute(Constants.VIEW_NAME, viewName);
				req.setAttribute(Constants.ROOT_DEF_NAME, ed.getName()); //Reference型ネストテーブル用
			}
		};
		Func afterFunc = new Func() {

			@Override
			public void execute(HttpServletRequest req, HttpServletResponse res) {
				//パラメータ削除
				req.removeAttribute(Constants.OUTPUT_TYPE);
				req.removeAttribute(Constants.EDITOR_EDITOR);
				req.removeAttribute(Constants.ENTITY_DATA);
				req.removeAttribute(Constants.EDITOR_PROP_VALUE);
				req.removeAttribute(Constants.ENTITY_DEFINITION);
				req.removeAttribute(Constants.EDITOR_REF_ENTITY_VALUE_MAP);
				req.removeAttribute(Constants.VIEW_NAME);
				req.removeAttribute(Constants.ROOT_DEF_NAME);
			}
		};

		//HTML取得
		editor.setPropertyName(propName);
		String html = ResponseUtil.getIncludeJspContents(path, beforeFunc, afterFunc).replace("\r\n", "").replace("\n", "").replace("\r", "");

		// カスタムスタイルの反映
		if (StringUtil.isNotEmpty(editor.getCustomStyle())) {
			String customStyle = EntityViewUtil.getCustomStyle(entity.getDefinitionName(), section.getScriptKey(), editor.getOutputCustomStyleScriptKey(), entity, propValue);
			html = "<span style=\"" + customStyle + "\">" + html + "</span>";
		}
		return html;
	}

	private static boolean isDispProperty(Element element) {
		if (!element.isDispFlag()) return false;
		if (element instanceof PropertyColumn) {
			if (((PropertyColumn) element).getEditor() != null) return true;
		}
		if (element instanceof VirtualPropertyItem) {
			if (((VirtualPropertyItem) element).getEditor() != null) return true;
		}
		return false;
	}
}
