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

package org.iplass.gem.command.information;

import java.sql.Date;
import java.sql.Timestamp;

import org.iplass.gem.command.Constants;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.auth.AuthManager;
import org.iplass.mtp.auth.User;
import org.iplass.mtp.auth.policy.definition.AuthenticationPolicyDefinition;
import org.iplass.mtp.auth.policy.definition.AuthenticationPolicyDefinitionManager;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.action.ActionMapping;
import org.iplass.mtp.command.annotation.action.Result;
import org.iplass.mtp.command.annotation.action.Result.Type;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.SortSpec;
import org.iplass.mtp.entity.query.SortSpec.SortType;
import org.iplass.mtp.entity.query.condition.expr.And;
import org.iplass.mtp.entity.query.condition.predicate.GreaterEqual;
import org.iplass.mtp.entity.query.condition.predicate.LesserEqual;
import org.iplass.mtp.impl.auth.authenticate.AccountHandle;
import org.iplass.mtp.impl.util.InternalDateUtil;
import org.iplass.mtp.view.top.parts.InformationParts;

/**
 * お知らせ管理用のコマンドです
 * @author H.Yoshida
 *
 */
@ActionMapping(
	name=InformationListCommand.ACTION_NAME,
	displayName="お知らせ一覧",
	result=@Result(type=Type.JSP, value=Constants.CMD_RSLT_JSP_INFO_LIST, templateName="gem/information/list")
)
@CommandClass(name="gem/information/InformationCommand", displayName="お知らせ一覧")
public final class InformationListCommand implements Command {

	public static final String ACTION_NAME = "gem/information/list";

	/** お知らせ情報のEntity定義名 */
	public static final String INFORMATION_ENTITY = "mtp.Information";

	/** EntityManager */
	private EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);

	/** AuthManager */
	private AuthManager am = ManagerLocator.getInstance().getManager(AuthManager.class);

	/** AuthenticationPolicyDefinitionManager */
	private AuthenticationPolicyDefinitionManager apdm = ManagerLocator.getInstance().getManager(AuthenticationPolicyDefinitionManager.class);

	/**
	 * コンストラクタ
	 */
	public InformationListCommand() {
	}

	@Override
	public String execute(RequestContext request) {
		//お知らせ情報Entity検索
		searchInfo(request);


		//パスワード期限チェック
		checkPasswordAge(request);

		return Constants.CMD_EXEC_SUCCESS;
	}

	private void searchInfo(RequestContext request) {
		Timestamp value = em.getCurrentTimestamp();

		And a = new And();
		a.addExpression(new LesserEqual(Entity.START_DATE, value));
		a.addExpression(new GreaterEqual(Entity.END_DATE, value));
		Query q = new Query();
		q.select(Entity.OID, Entity.NAME, Entity.VERSION, Entity.START_DATE)
		 .from(INFORMATION_ENTITY)
		 .where(a)
		 .order(new SortSpec(Entity.START_DATE, SortType.DESC),
				 new SortSpec(Entity.OID, SortType.DESC));

		request.setAttribute(Constants.DATA, em.searchEntity(q).getList());
	}

	private void checkPasswordAge(RequestContext request) {
		//対象のInformationParts取得
		InformationParts infoParts = (InformationParts)request.getAttribute(Constants.INFO_SETTING);

		if (infoParts == null || !infoParts.isShowWarningPasswordAge()) {
			return;
		}

		AuthContext auth = AuthContext.getCurrentContext();
		User user = auth.getUser();

		//未ログイン時は終了
		if (user == null || user.isAnonymous()) {
			return;
		}

		//パスワード変更日時の取得
		Date lastPasswordChange = (Date)auth.getAttribute(AccountHandle.LAST_PASSWORD_CHANGE);
		if (lastPasswordChange == null) {
			//パスワード変更日が取得できない場合（AccountHandler側でサポートしていない）は終了
			return;
		}
		//時分秒をクリア
		lastPasswordChange = InternalDateUtil.truncateTime(lastPasswordChange);

		//パスワード変更不可時は終了
		if (!am.canUpdateCredential(auth.getPolicyName())) {
			return;
		}

		//認証ポリシーから最大日数を取得
		AuthenticationPolicyDefinition policy = apdm.getOrDefault(auth.getPolicyName());
		int maxAge = policy.getPasswordPolicy().getMaximumPasswordAge();

		//期限が無期限の場合は終了
		if (maxAge <= 0) {
			return;
		}

		//Limitの日付を算出
		Date limitDate = InternalDateUtil.addDays(lastPasswordChange, maxAge);

		//残日数を計算
		Date now = new Date(System.currentTimeMillis());	//TODO プレビュー日付で？
		now = InternalDateUtil.truncateTime(now);
		int days = InternalDateUtil.diffDays(limitDate, now);

		if (days <= infoParts.getPasswordWarningAge()) {
			request.setAttribute(Constants.INFO_PASSWORD_WARNING, Boolean.TRUE);
			request.setAttribute(Constants.INFO_PASSWORD_REMAINING_DAYS, days);
		}
	}
}
