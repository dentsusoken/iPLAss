/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.action.ActionMapping;
import org.iplass.mtp.command.annotation.action.ActionMapping.ClientCacheType;
import org.iplass.mtp.command.annotation.action.ActionMappings;
import org.iplass.mtp.command.annotation.action.Result;
import org.iplass.mtp.command.annotation.action.Result.Type;
import org.iplass.mtp.command.annotation.template.Template;
import org.iplass.mtp.command.annotation.template.Templates;
import org.iplass.mtp.definition.annotation.LocalizedString;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.condition.predicate.In;
import org.iplass.mtp.entity.query.hint.CacheHint;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.web.i18n.LangSelector;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.menu.EntityMenuItem;
import org.iplass.mtp.view.menu.MenuItem;
import org.iplass.mtp.view.menu.MenuTree;
import org.iplass.mtp.view.menu.MenuTreeManager;
import org.iplass.mtp.view.top.TopViewDefinitionManager;

@ActionMappings({
	@ActionMapping(
		name=Constants.LAYOUT_NORMAL_ACTION,
		displayName="標準レイアウト",
		localizedDisplayName={
			@LocalizedString(localeName="ja", stringValue="標準レイアウト"),
			@LocalizedString(localeName="en", stringValue="Default Layout")
		},
		result=@Result(type=Type.JSP, value=Constants.CMD_RSLT_JSP_DEFAULT, templateName="gem/layout/layout")
	),
	@ActionMapping(
			name=Constants.LAYOUT_POPOUT_ACTION,
			displayName="ポップアップレイアウト",
			localizedDisplayName={
				@LocalizedString(localeName="ja", stringValue="ポップアップレイアウト"),
				@LocalizedString(localeName="en", stringValue="Popup Layout")
			},
			result=@Result(type=Type.JSP, value=Constants.CMD_RSLT_JSP_DIALOG, templateName="gem/layout/dialog")
		),
	@ActionMapping(
		name=MenuCommand.ACTION_NAME,
		displayName="TOP画面",
		localizedDisplayName={
			@LocalizedString(localeName="ja", stringValue="TOP画面"),
			@LocalizedString(localeName="en", stringValue="Top View")
		},
		clientCacheType=ClientCacheType.CACHE,
		clientCacheMaxAge=0,
		command={},//遷移先で標準レイアウト呼び出すので不要
		result=@Result(type=Type.JSP, value=Constants.CMD_RSLT_JSP_INDEX, templateName="gem/layout/index", layoutActionName=Constants.LAYOUT_NORMAL_ACTION)
	),
	@ActionMapping(
		name="gem/logout",
		displayName="ログアウト",
		localizedDisplayName={
			@LocalizedString(localeName="ja", stringValue="ログアウト"),
			@LocalizedString(localeName="en", stringValue="Logout")
		},
		command={},
		result=@Result(type=Type.REDIRECT, value=".")
	)
})
@Templates({
	@Template(name="gem/layout/header", displayName="ヘッダー", path="/jsp/gem/layout/header.jsp"),
	@Template(name="gem/layout/footer", displayName="フッター", path="/jsp/gem/layout/footer.jsp"),
	@Template(name="gem/layout/navi", displayName="ナビ", path="/jsp/gem/layout/navi.jsp"),
	@Template(name="gem/menu/menu", displayName="メニュー", path="/jsp/gem/menu/menu.jsp"),

	//GemErrorUrlSelector用設定
	@Template(
			name=Constants.TEMPLATE_ERROR,
			displayName="エラー画面（通常）",
			path=Constants.CMD_RSLT_JSP_ERROR,
			layoutActionName=Constants.LAYOUT_NORMAL_ACTION),
	@Template(name="gem/error/system", displayName="エラー画面（システムエラー）", path="/jsp/gem/error/Error.jsp", contentType="text/html; charset=utf-8"),
	@Template(name="gem/auth/PermissionError", displayName="権限エラー", path="/jsp/gem/error/Error.jsp", contentType="text/html; charset=utf-8")
})
@CommandClass(name="gem/MenuCommand", displayName="メニュー",
	localizedDisplayName={
		@LocalizedString(localeName="ja", stringValue="メニュー"),
		@LocalizedString(localeName="en", stringValue="Menu")
	},
	readOnly=true
)
public final class MenuCommand implements Command {

	public static final String ACTION_NAME = "gem/index";

	/** デフォルトのメニュー定義名 */
	public static final String DEFAULT = "DEFAULT";

	private EntityDefinitionManager edm;
	private EntityManager em;
	private MenuTreeManager mtm;
	private TopViewDefinitionManager tm;

	/**
	 * コンストラクタ
	 */
	public MenuCommand() {
		edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
		em = ManagerLocator.getInstance().getManager(EntityManager.class);
		mtm = ManagerLocator.getInstance().getManager(MenuTreeManager.class);
		tm = ManagerLocator.getInstance().getManager(TopViewDefinitionManager.class);
	}

	@Override
	public String execute(RequestContext request) {

		AuthContext authContext = AuthContext.getCurrentContext();

		// 有効なメニュー定義を取得
		List<String> allMenuNames = mtm.definitionList();
		List<String> permitMenuNames = checkRole(authContext, allMenuNames);

		//有効なトップ画面定義を取得
		List<String> allTopNames = tm.definitionList();
		List<String> permitTopNames = checkRole(authContext, allTopNames);

		String selectRoleName = (String) request.getSession().getAttribute(Constants.ROLE_NAME);
		MenuTree selectTree = null;
		LinkedHashMap<String, String> sortedRoleMap = null;
		if (permitMenuNames.isEmpty() && permitTopNames.isEmpty()) {
			//メニュー、トップどっちのロールにも紐づかない

			if (allMenuNames.contains(DEFAULT)) {
				//デフォルトメニューあればそれを利用
				selectTree = mtm.get(DEFAULT);

				sortedRoleMap = new LinkedHashMap<String, String>();
				selectRoleName = null;
			}
		} else {
			//メニュー、トップどちらかのロールに紐づく

			//ロール名称取得（ロール優先度で並び変え）
			sortedRoleMap = getRoleNames(permitMenuNames, permitTopNames);

			if (authContext.getUser().isAdmin()) {
				//Adminの場合だけデフォルトメニューも追加
				if (allMenuNames.contains(DEFAULT)) {
					sortedRoleMap.put(DEFAULT, mtm.get(DEFAULT).getName());
				}
			}

			//ロールが未指定の場合は先頭を選択
			if (StringUtil.isEmpty(selectRoleName)) {
				selectRoleName = sortedRoleMap.keySet().iterator().next();
				request.getSession().setAttribute(Constants.ROLE_NAME, selectRoleName);
			}
			selectTree = mtm.get(selectRoleName);
		}

		if (selectTree == null) {
			//DEFAULTのメニューもない場合はEntity定義の一覧をメニュー化
			List<MenuItem> items = createEntityMenuItem();
			selectTree = new MenuTree();
			selectTree.setMenuItems(items);
		}

		//メニューの権限チェック
		MenuAuthVisitor visitor = new MenuAuthVisitor(authContext);
		selectTree.accept(visitor);

		request.setAttribute(Constants.ROLE, sortedRoleMap);
		request.setAttribute(Constants.ROLE_NAME, selectRoleName);
		request.setAttribute(Constants.MENU_TREE, selectTree);
		request.setAttribute(LangSelector.LANG_ATTRIBUTE_NAME, ExecuteContext.getCurrentContext().getLanguage());

		return Constants.CMD_EXEC_SUCCESS;
	}

	/**
	 * 有効なロールかチェック
	 * @param authContext
	 * @param defNames チェック対象の定義名
	 */
	private List<String> checkRole(final AuthContext authContext, final List<String> defNames) {

		if(defNames != null && !defNames.isEmpty()) {
			//DEFAULTは一旦無視
			return defNames.stream()
				.filter(name -> !DEFAULT.equals(name))
				.filter(name -> authContext.userInRole(name))
				.collect(Collectors.toList());
		} else {
			return Collections.emptyList();
		}
	}

	private LinkedHashMap<String, String> getRoleNames(List<String> menuNames, List<String> topNames) {

		Set<String> roleNames = new HashSet<>(menuNames);
		roleNames.addAll(topNames);

		if (roleNames.isEmpty()) {
			return new LinkedHashMap<String, String>();
		}

		Query query = (new Query())
			.hint(new CacheHint())
			.select("code", Entity.NAME, "priority").from("mtp.auth.Role")
			.where(new In("code", roleNames.toArray()));

		List<Entity> roleList = em.searchEntity(query).getList();

		LinkedHashMap<String, String> ret = roleList.stream()
			.map(entity -> {
				//Menu定義の表示順を取得
				MenuTree mt = mtm.get(entity.getValue("code"));
				if (mt != null) {
					return new RoleInfo(entity, mt.getDisplayOrder());
				} else {
					return new RoleInfo(entity, Integer.MAX_VALUE);
				}
			})
			.sorted(
					//Tree定義の表示順(昇順) -> ロール優先度(降順) -> ロール名(昇順)
					Comparator.comparing(RoleInfo::getDisplayOrder)
					.thenComparing(Comparator.comparing(RoleInfo::getPriority).reversed())
					.thenComparing(Comparator.comparing(RoleInfo::getName))
			)
			.collect(Collectors.toMap(RoleInfo::getCode, RoleInfo::getName, (a, b) -> a, LinkedHashMap::new));

		return ret;
	}

	private List<MenuItem> createEntityMenuItem() {

		List<String> defList = edm.definitionList();

		List<MenuItem> items = defList.stream()
			.sorted((name1, name2) -> name1.compareTo(name2))
			.map(name -> {
				EntityMenuItem item = new EntityMenuItem();
				item.setName(name);
				item.setEntityDefinitionName(name);
				return item;
			})
			.collect(Collectors.toList());

		return items;
	}

	private static class RoleInfo {

		private final Entity role;
		private final Integer displayOrder;

		public RoleInfo(Entity role, Integer dispOrder) {
			this.role = role;
			this.displayOrder = dispOrder != null ? dispOrder : Integer.MAX_VALUE;
		}
		public Integer getDisplayOrder() {
			return displayOrder;
		}
		public Long getPriority() {
			return role.getValue("priority") != null ? role.getValue("priority") : Long.MIN_VALUE;
		}
		public String getName() {
			return role.getName();
		}
		public String getCode() {
			return role.getValue("code");
		}
	}

}
