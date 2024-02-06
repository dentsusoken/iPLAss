/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.RequestType;

/**
 * 上位階層選択値取得処理
 */
@WebApi(
		name=SearchParentCommand.WEBAPI_NAME,
		accepts=RequestType.REST_JSON,
		methods=MethodType.POST,
		restJson=@RestJson(parameterName="param"),
		results={"parent"},
		checkXRequestedWithHeader=true
	)
@CommandClass(name="gem/generic/refcombo/SearchParentCommand", displayName="上位階層選択値取得")
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

		//参照コンボ設定取得
		Command cmd = ci.getCommandInstance(GetReferenceComboSettingCommand.CMD_NAME);
		cmd.execute(request);
		ReferenceComboSetting setting = (ReferenceComboSetting) request.getAttribute(GetReferenceComboSettingCommand.RESULT_DATA_NAME);
		if (setting == null) {
			return "NOT_DEFINED_EDITOR";
		}

		String defName = request.getParam(Constants.DEF_NAME);
		String propName = request.getParam(Constants.PROP_NAME);

		//検索対象の階層の名前
		String targetPath = request.getParam("targetPath");
		//1個下の階層で選択されたEntityのOID
		String childOid = request.getParam("childOid");

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
		ReferenceProperty rp = (ReferenceProperty) pd;

		Entity parent = searchParent(setting, targetPath, childOid, rp, rp.getName());
		request.setAttribute("parent", parent);

		return Constants.CMD_EXEC_SUCCESS;
	}

	/**
	 * 親階層選択データ取得
	 *
	 * @param setting 参照コンボ設定
	 * @param targetPath 検索対象のパス
	 * @param childOid 子階層の選択データOID
	 * @param currentProperty 現在(子階層)のプロパティ定義
	 * @param currentPath 現在(子階層)のパス
	 * @return 親階層選択データ
	 */
	private Entity searchParent(ReferenceComboSetting setting,
			String targetPath, String childOid,
			ReferenceProperty currentProperty, String currentPath) {

		if (childOid == null) {
			return null;
		}

		//子階層のEntity定義取得
		EntityDefinition childEntityDefinition = edm.get(currentProperty.getObjectDefinitionName());
		if (childEntityDefinition == null) {
			return null;
		}

		//親階層プロパティ定義取得
		ReferenceProperty parentProperty = (ReferenceProperty) childEntityDefinition.getProperty(setting.getPropertyName());

		if (parentProperty != null) {
			String parentPath = currentPath + "." + parentProperty.getName();
			if (targetPath.equals(parentPath)) {
				//子階層のEntityデータを検索し、その親階層のデータを取得
				Query query = new Query()
						.select(parentProperty.getName() + "." + Entity.OID)
						.from(currentProperty.getObjectDefinitionName())
						.where(new Equals(Entity.OID, childOid));
				Entity ret = em.searchEntity(query).getFirst();
				if (ret != null && ret.getValue(parentProperty.getName()) != null) {
					//最初の項目をデフォルト選択させる
					Entity ref = ret.getValue(parentProperty.getName());
					return ref;
				}
			} else {
				if (setting.getParent() != null && StringUtil.isNotBlank(setting.getParent().getPropertyName())) {
					return searchParent(setting.getParent(), targetPath, childOid, parentProperty, parentPath);
				}
			}
		}

		return null;
	}

}
