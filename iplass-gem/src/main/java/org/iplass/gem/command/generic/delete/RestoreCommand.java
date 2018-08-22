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

package org.iplass.gem.command.generic.delete;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.iplass.gem.command.CommandUtil;
import org.iplass.gem.command.Constants;
import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.RestJson;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.command.annotation.webapi.WebApiTokenCheck;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.view.generic.EntityView;
import org.iplass.mtp.view.generic.SearchFormView;
import org.iplass.mtp.webapi.definition.RequestType;
import org.iplass.mtp.webapi.definition.MethodType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 復元コマンド
 * @author lis3wg
 */
@WebApi(
	name=RestoreCommand.WEBAPI_NAME,
	displayName="復元",
	accepts=RequestType.REST_JSON,
	methods=MethodType.POST,
	restJson=@RestJson(parameterName="param"),
	tokenCheck=@WebApiTokenCheck(consume=false, useFixedToken=true),
	results={"message","errorRbid"},
	checkXRequestedWithHeader=true
)
@CommandClass(name="gem/generic/delete/RestoreCommand", displayName="復元")
public final class RestoreCommand extends DeleteCommandBase {

	public static final String WEBAPI_NAME = "gem/generic/delete/restore";
	private static final Logger logger = LoggerFactory.getLogger(RestoreCommand.class);

	@Override
	public String execute(final RequestContext request) {
		final String defName = request.getParam(Constants.DEF_NAME);
		String viewName = request.getParam(Constants.VIEW_NAME);
		Long[] rbid = null;
		Object val = request.getParamMap().get(Constants.RBID);
		if (val instanceof String) {
			rbid = new Long[]{CommandUtil.getLong((String)val)};
		} else if (val instanceof ArrayList<?>) {
			ArrayList<?> list = (ArrayList<?>) val;
			rbid = new Long[list.size()];
			for (int i = 0; i < list.size(); i++) {
				rbid[i] = CommandUtil.getLong(list.get(i).toString());
			}
		}

		if (rbid != null && rbid.length > 0) {
			final boolean isAllowTrashOperationToRecycleBy = isAllowTrashOperationToRecycleBy(defName, viewName);
			final String userOid = AuthContext.getCurrentContext().getUser().getOid();

			int count = rbid.length;
			int countPerHundret = count / 100;
			if (count % 100 > 0) countPerHundret++;
			int current = 0;
			for (int i = 0; i < countPerHundret; i++) {
				current = i * 100;
				int last = current + 100;
				if (last > rbid.length) last = rbid.length;
				List<Long> list = Arrays.asList(rbid);
				final List<Long> subList = list.subList(current, last);
				Boolean ret = Transaction.requiresNew(transaction -> {
					for (Long id : subList) {
						try {
							if (isAllowTrashOperationToRecycleBy) {
								// ユーザ自身が削除したデータのみ
								Entity rb = em.getRecycleBin(id, defName);
								if (userOid.equals(rb.getUpdateBy())) {
									em.restore(id, defName);
								}
							} else {
								em.restore(id, defName);
							}
						} catch (ApplicationException e) {
							transaction.rollback();
							logger.error("restore RecycleBin[rbid=" + id + "] is failed.", e);
							request.setAttribute("message", e.getMessage());
							request.setAttribute("errorRbid", id);
							return false;
						}
					}
					return true;
				});
				if (!ret) {
					break;
				}
			}
		}

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
