/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.gem.command.generic.delete;

import java.util.ArrayList;
import java.util.List;

import org.iplass.gem.GemConfigService;
import org.iplass.gem.command.Constants;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.RestJson;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.view.generic.EntityView;
import org.iplass.mtp.view.generic.EntityViewManager;
import org.iplass.mtp.view.generic.SearchFormView;
import org.iplass.mtp.webapi.definition.RequestType;
import org.iplass.mtp.webapi.definition.MethodType;

/**
 * 削除データ取得
 * @author lis3wg
 */
@WebApi(
	name=GetRecycleBinCommand.WEBAPI_NAME,
	displayName="削除データ取得",
	accepts=RequestType.REST_JSON,
	methods=MethodType.POST,
	restJson=@RestJson(parameterName="param"),
	results={Constants.DATA_ENTITY},
	checkXRequestedWithHeader=true
)
@CommandClass(name="gem/generic/delete/GetRecycleBinCommand", displayName="削除データ取得")
public final class GetRecycleBinCommand implements Command{

	public static final String WEBAPI_NAME = "gem/generic/delete/getRecycleBin";

	private EntityManager em = null;
	private EntityViewManager evm = null;
	private GemConfigService gcs = null;

	public GetRecycleBinCommand() {
		em = ManagerLocator.getInstance().getManager(EntityManager.class);
		evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
		gcs = ServiceRegistry.getRegistry().getService(GemConfigService.class);
	}

	@Override
	public String execute(RequestContext request) {
		String defName = request.getParam(Constants.DEF_NAME);
		String viewName = request.getParam(Constants.VIEW_NAME);

		final boolean isAllowTrashOperationToRecycleBy = isAllowTrashOperationToRecycleBy(defName, viewName);
		final String userOid = AuthContext.getCurrentContext().getUser().getOid();

		final List<Entity> list = new ArrayList<Entity>();
		em.getRecycleBin(defName, entity -> {
			if (isAllowTrashOperationToRecycleBy) {
				// ユーザー自身が削除したデータのみ
				if (userOid.equals(entity.getUpdateBy())) {
					list.add(entity);
				}
			} else {
				list.add(entity);
			}
			if (gcs.getRecycleBinMaxCount() > 0 && list.size() == gcs.getRecycleBinMaxCount()) {
				return false;
			} else {
				return true;
			}
		});

		request.setAttribute(Constants.DATA_ENTITY, list);
		return "SUCCESS";
	}

	private boolean isAllowTrashOperationToRecycleBy(String defName, String viewName) {
		EntityView view = evm.get(defName);
		SearchFormView form = null;
		if (viewName == null || viewName.equals("")) {
			//1件でもView定義があればその中からデフォルトレイアウトを探す
			if (view != null && view.getSearchFormViewNames().length > 0) {
				form = view.getDefaultSearchFormView();
			}
		} else {
			//指定レイアウトを利用
			if (view != null) {
				form = view.getSearchFormView(viewName);
			}
		}

		// formがあれば設定値を、無ければ全削除データを許可(false)
		return form != null ? form.isAllowTrashOperationToRecycleBy() : false;
	}

}

