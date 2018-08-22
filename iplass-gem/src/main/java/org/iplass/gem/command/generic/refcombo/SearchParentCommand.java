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

package org.iplass.gem.command.generic.refcombo;

import org.iplass.gem.command.Constants;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.CommandInvoker;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.RestJson;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.editor.ReferenceComboSetting;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor;
import org.iplass.mtp.webapi.definition.RequestType;
import org.iplass.mtp.webapi.definition.MethodType;

@WebApi(
		name=SearchParentCommand.WEBAPI_NAME,
		accepts=RequestType.REST_JSON,
		methods=MethodType.POST,
		restJson=@RestJson(parameterName="param"),
		results={"parent"},
		checkXRequestedWithHeader=true
	)
@CommandClass(name="gem/generic/refcombo/SearchParentCommand", displayName="プロパティエディタ取得マンド")
public final class SearchParentCommand implements Command {

	public static final String WEBAPI_NAME = "gem/generic/refcombo/searchParent";

	private EntityDefinitionManager edm;
	private EntityManager em;
	private CommandInvoker ci;

	public SearchParentCommand() {
		edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
		em = ManagerLocator.getInstance().getManager(EntityManager.class);
		ci = ManagerLocator.getInstance().getManager(CommandInvoker.class);
	}

	@Override
	public String execute(RequestContext request) {
		//Editor取得
		Command cmd = ci.getCommandInstance(GetEditorCommand.CMD_NAME);
		cmd.execute(request);
		ReferencePropertyEditor editor = (ReferencePropertyEditor) request.getAttribute("editor");
		if (editor == null) {
			return "NOT_DEFINED_EDITOR";
		}

		String defName = request.getParam(Constants.DEF_NAME);
		String propName = request.getParam(Constants.PROP_NAME);
		String currentName = request.getParam("currentName");//検索対象の階層の名前
		String oid = request.getParam("oid");//1個下の階層で選択されたEntityのOID

		//Entity本体の定義取得
		EntityDefinition ed = edm.get(defName);
		if (ed == null) {
			return "NOT_DEFINED_ENTITY";
		}

		//参照プロパティの定義取得
		PropertyDefinition pd = ed.getProperty(propName);
		if (pd == null) {
			return "NOT_DEFINED_PROPERTY";
		}
		if (!(pd instanceof ReferenceProperty)) {
			return "NOT_REFERENCE_PROPERTY";
		}

		//参照先の定義取得
		ReferenceProperty rp = (ReferenceProperty) pd;
		EntityDefinition red = edm.get(rp.getObjectDefinitionName());
		if (red == null) {
			return "NOT_DEFINED_REFERENCE_ENITY";
		}
		ReferenceComboSetting setting = editor.getReferenceComboSetting();
		searchParent(request, rp, setting, rp.getName(), currentName, oid);

		return null;
	}

	private void searchParent(RequestContext request, ReferenceProperty crp, ReferenceComboSetting setting,
			String childName, String currentName, String childOid) {
		//子の親プロパティ
		EntityDefinition ed = edm.get(crp.getObjectDefinitionName());
		ReferenceProperty rp = (ReferenceProperty) ed.getProperty(setting.getPropertyName());
		if (rp != null) {
			String propName = childName + "." + rp.getName();
			if (currentName.equals(propName)) {
				if (childOid != null) {
					//子階層が指定されてたら、指定の子を持つ親階層を検索
					Query q = new Query().select(rp.getName() + "." + Entity.OID).from(crp.getObjectDefinitionName()).where(new Equals(Entity.OID, childOid));
					Entity ret = em.searchEntity(q).getFirst();
					if (ret != null && ret.getValue(rp.getName()) != null) {
						//最初の項目をデフォルト選択させる
						Entity ref = ret.getValue(rp.getName());
						request.setAttribute("parent", ref);
					}
				}
			} else {
				if (setting.getParent() != null && StringUtil.isNotBlank(setting.getParent().getPropertyName())) {
					searchParent(request, rp, setting.getParent(), propName, currentName, childOid);
				}
			}
		}

	}

}
