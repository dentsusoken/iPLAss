/*
 * Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.gem.auth;

import java.util.List;

import org.iplass.gem.GemConfigService;
import org.iplass.gem.command.Constants;
import org.iplass.gem.command.GemWebApiParameter;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.impl.auth.authorize.builtin.action.ActionParameterBinding;
import org.iplass.mtp.impl.auth.authorize.builtin.webapi.WebApiParameterBinding;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.view.generic.EntityViewManager;

/**
 * gemのAction、WebApiに対する実行権限を制御します。
 *
 * <h3>権限制御について</h3>
 * 設定される権限は以下のように動作します。
 *
 * <p>
 * <ul>
 * <li>gem/* : 実行できるのは、GemConfigService#permitRolesToGem指定ロールのみ</li>
 * <li>gem/generic/* (gemの汎用画面)
 * <ul>
 * <li>view定義がない場合、自動生成される画面は、GemConfigService#permitRolesToNoView指定ロールのみ許可</li>
 * <li>view定義がある場合、管理設定がない場合、全て許可</li>
 * <li>view定義がある場合、対象Viewの管理設定に許可ロールが設定されていない場合、全て許可</li>
 * <li>view定義がある場合、対象Viewの管理設定に許可ロールが設定されている場合、指定ロールのみ許可</li>
 * </ul>
 * </li>
 * </ul>
 * </p>
 *
 * <h3>権限の設定方法</h3>
 * Action権限、WebApi権限の設定方法を説明します。
 *
 * <h4>Action権限設定</h4>
 *
 * <p>Action権限の許可条件に以下のScriptを設定します。
 * 対象Actionは"gem/*"とします。</p>
 *
 * <h5>Action権限許可条件</h5>
 * <pre>
 * org.iplass.gem.auth.GemAuth.isPermitAction(action, parameter)
 *
 * ※action、parameterは許可条件Scriptにバインドされる変数です。
 * </pre>
 *
 * <h4>WebApi権限設定</h4>
 *
 * <p>WebApi権限の許可条件に以下のScriptを設定します。
 * 対象WebApiは"gem/*"とします。</p>
 *
 * <h5>WebApi権限許可条件</h5>
 * <pre>
 * org.iplass.gem.auth.GemAuth.isPermitWebApi(webApi, parameter)
 *
 * ※webApi、parameterは許可条件Scriptにバインドされる変数です。
 * </pre>
 *
 */
public class GemAuth {

	/**
	 * Actionに対して許可されているかを返します。
	 *
	 * @param actionName アクション名
	 * @param param パラメータ
	 * @return true：許可
	 */
	public static boolean isPermitAction(String actionName, ActionParameterBinding param) {

		if (actionName.startsWith("gem/generic/")) {
			//汎用画面
			String definitionName = (String)param.getValue(Constants.DEF_NAME);
			String viewName = (String)param.getValue(Constants.VIEW_NAME);
			return isPermitEntityView(definitionName, viewName);
		} else if (actionName.startsWith("gem/")) {
			//その他のgem
			return isPermitGem();
		}

		return true;
	}

	/**
	 * WebApiに対して許可されているかを返します。
	 *
	 * @param webApiName WebApi名
	 * @param param パラメータ
	 * @return true：許可
	 */
	public static boolean isPermitWebApi(String webApiName, WebApiParameterBinding param) {

		if (webApiName.startsWith("gem/generic/")) {
			//汎用画面
			String definitionName = null;
			String viewName = null;

			Object paramValue = param.getValue("params");
			if (paramValue == null) {
				definitionName = (String)param.getValue(Constants.DEF_NAME);
				viewName = (String)param.getValue(Constants.VIEW_NAME);
			} else if (paramValue instanceof GemWebApiParameter) {
				GemWebApiParameter gemParam = (GemWebApiParameter) paramValue;
				definitionName = gemParam.getDefName();
				viewName = gemParam.getViewName();
			}

			// null等が文字列で渡される場合はnullにする
			if (viewName != null && ("null".equals(viewName) || "undefined".equals(viewName))) {
				viewName = null;
			}

			return isPermitEntityView(definitionName, viewName);
		} else if (webApiName.startsWith("gem/")) {
			//その他のgem
			return isPermitGem();
		}

		return true;
	}

	/**
	 * EntityViewに対する操作が許可されているかを返します。
	 *
	 * @param definitionName Entity名
	 * @param viewName EntityView名
	 * @return true：許可
	 */
	public static boolean isPermitEntityView(String definitionName, String viewName) {

		EntityViewManager evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);

		//許可ロールを取得
		List<String> permitRoles = evm.getPermitRoles(definitionName, viewName);

		if (permitRoles == null) {
			//許可ロールがない場合（自動生成）は、指定されているロールのみ許可
			GemConfigService service = ServiceRegistry.getRegistry().getService(GemConfigService.class);
			permitRoles = service.getPermitRolesToNoView();
		}

		if (permitRoles == null || permitRoles.isEmpty()) {
			//許可ロールが未指定の場合は、全て許可
			return true;
		} else {
			final AuthContext authContext = AuthContext.getCurrentContext();
			return permitRoles.stream().anyMatch(role -> authContext.userInRole(role));
		}
	}

	/**
	 * gemに対する操作が許可されているかを返します。
	 *
	 * @return true：許可
	 */
	public static boolean isPermitGem() {
		final AuthContext authContext = AuthContext.getCurrentContext();

		//指定されているロールのみ許可
		GemConfigService service = ServiceRegistry.getRegistry().getService(GemConfigService.class);
		if (service.getPermitRolesToGem() == null) {
			return true;
		}
		return service.getPermitRolesToGem().stream().anyMatch(role -> authContext.userInRole(role));
	}

}
