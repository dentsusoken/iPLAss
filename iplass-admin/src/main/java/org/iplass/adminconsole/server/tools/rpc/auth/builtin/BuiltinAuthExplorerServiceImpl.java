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

package org.iplass.adminconsole.server.tools.rpc.auth.builtin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.iplass.adminconsole.server.base.rpc.util.AuthUtil;
import org.iplass.adminconsole.shared.tools.dto.auth.builtin.BuiltinAuthUserDto;
import org.iplass.adminconsole.shared.tools.dto.auth.builtin.BuiltinAuthUserListResultDto;
import org.iplass.adminconsole.shared.tools.dto.auth.builtin.BuiltinAuthUserResetErrorCountResultInfo;
import org.iplass.adminconsole.shared.tools.dto.auth.builtin.BuiltinAuthUserSearchConditionDto;
import org.iplass.adminconsole.shared.tools.dto.auth.builtin.BuiltinAuthUserSearchOperator;
import org.iplass.adminconsole.shared.tools.dto.auth.builtin.BuiltinAuthUserSearchType;
import org.iplass.adminconsole.shared.tools.dto.auth.builtin.BuiltinAuthUserSpecificType;
import org.iplass.adminconsole.shared.tools.rpc.auth.builtin.BuiltinAuthExplorerService;
import org.iplass.mtp.impl.tools.auth.builtin.BuiltinAuthToolService;
import org.iplass.mtp.impl.tools.auth.builtin.BuiltinAuthUser;
import org.iplass.mtp.impl.tools.auth.builtin.BuiltinAuthUserResetErrorCountResult;
import org.iplass.mtp.impl.tools.auth.builtin.BuiltinAuthUserSearchParameter;
import org.iplass.mtp.impl.tools.auth.builtin.BuiltinAuthUserSearchResult;
import org.iplass.mtp.impl.tools.auth.builtin.cond.SearchOperator;
import org.iplass.mtp.impl.tools.auth.builtin.cond.UserAttributeCondition;
import org.iplass.mtp.impl.tools.auth.builtin.cond.UserSpecificCondition;
import org.iplass.mtp.impl.tools.auth.builtin.cond.UserSpecificCondition.SpecificType;
import org.iplass.mtp.spi.ServiceRegistry;

import com.google.gwt.user.server.rpc.XsrfProtectedServiceServlet;

public class BuiltinAuthExplorerServiceImpl extends XsrfProtectedServiceServlet implements BuiltinAuthExplorerService {

	private static final long serialVersionUID = -7886389786183377885L;

	private BuiltinAuthToolService service = ServiceRegistry.getRegistry().getService(BuiltinAuthToolService.class);

	@Override
	public BuiltinAuthUserListResultDto search(final int tenantId, final BuiltinAuthUserSearchConditionDto condDto) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<BuiltinAuthUserListResultDto>() {

			@Override
			public BuiltinAuthUserListResultDto call() {

				BuiltinAuthUserListResultDto resultDto = new BuiltinAuthUserListResultDto();

				//dto -> tools.cond
				BuiltinAuthUserSearchParameter param = convertCond(condDto);

				long start = System.nanoTime();

				//検索実行
				BuiltinAuthUserSearchResult result = service.search(param);

				//tools.result -> dto
				resultDto.setError(result.isError());
				resultDto.setLogMessages(result.getMessages());
				resultDto.setTotalCount(result.getTotalCount());
				resultDto.setExecuteOffset(result.getExecuteOffset());

				if (!result.isError()) {
					long total = System.nanoTime() - start;
					if (total > 1000000000) {
						resultDto.addLogMessage("search exec time："
								+ ((double)(System.nanoTime() - start)) / 1000000000 + "sec");
					} else {
						resultDto.addLogMessage("search exec time："
								+ ((double)(System.nanoTime() - start)) / 1000000 + "ms");
					}

					if (result.getUsers() == null || result.getUsers().isEmpty()) {
						resultDto.addLogMessage("Count ：" + "not found data.");
					} else {
						resultDto.addLogMessage("Count ：" + result.getUsers().size()
								+ " / " + result.getTotalCount());
					}

					//tools.user -> dto
					if (result.getUsers() != null) {
						List<BuiltinAuthUserDto> users = new ArrayList<>();
						for (BuiltinAuthUser user : result.getUsers()) {
							users.add(convertUser(user));
						}

						resultDto.setUsers(users);
					}

				}

				return resultDto;
			}

		});
	}

	private BuiltinAuthUserSearchParameter convertCond(BuiltinAuthUserSearchConditionDto condDto) {
		BuiltinAuthUserSearchParameter param = new BuiltinAuthUserSearchParameter();

		if (BuiltinAuthUserSearchType.SPECIFIC == condDto.getSearchType()) {
			UserSpecificCondition searchCond = new UserSpecificCondition();
			if (BuiltinAuthUserSpecificType.LOCKED == condDto.getSpecificType()) {
				searchCond.setType(SpecificType.LOCKED);
				searchCond.setLockedUserPolicyName(condDto.getLockedUserPolicyName());
			} else if (BuiltinAuthUserSpecificType.PASSWORDDAYS == condDto.getSpecificType()) {
				searchCond.setType(SpecificType.EXPIRED_PASSWORD);
				searchCond.setPasswordRemainDaysPolicyName(condDto.getPasswordRemainDaysPolicyName());
				if (BuiltinAuthUserSearchOperator.LESSTHAN == condDto.getPasswordRemainDaysOparator()) {
					searchCond.setPasswordRemainDaysOparator(SearchOperator.LESSEQUAL);
				} else if (BuiltinAuthUserSearchOperator.EQUAL == condDto.getPasswordRemainDaysOparator()) {
					searchCond.setPasswordRemainDaysOparator(SearchOperator.EQUAL);
				}
				searchCond.setPasswordRemainDays(condDto.getPasswordRemainDays());
			} else if (BuiltinAuthUserSpecificType.LASTLOGIN == condDto.getSpecificType()) {
				searchCond.setType(SpecificType.LAST_LOGIN);
				if (condDto.getLastLoginFrom() != null) {
					searchCond.setLastLoginFromDate(convertDate(condDto.getLastLoginFrom()));
				}
				if (condDto.getLastLoginTo() != null) {
					searchCond.setLastLoginToDate(convertDate(condDto.getLastLoginTo()));
				}
			}

			param.setCondition(searchCond);
		} else if (BuiltinAuthUserSearchType.ATTRIBUTE == condDto.getSearchType()) {
			UserAttributeCondition searchCond = new UserAttributeCondition();
			searchCond.setAccountId(condDto.getAccountId());
			searchCond.setName(condDto.getName());
			searchCond.setMail(condDto.getMail());
			if (condDto.getValidTermRemainDaysOparator() != null) {
				if (BuiltinAuthUserSearchOperator.LESSTHAN == condDto.getValidTermRemainDaysOparator()) {
					searchCond.setValidTermRemainDaysOparator(SearchOperator.LESSEQUAL);
				} else if (BuiltinAuthUserSearchOperator.EQUAL == condDto.getValidTermRemainDaysOparator()) {
					searchCond.setValidTermRemainDaysOparator(SearchOperator.EQUAL);
				}
				searchCond.setValidTermRemainDays(condDto.getValidTermRemainDays());
			}
			searchCond.setDirectWhere(condDto.getDirectWhere());

			param.setCondition(searchCond);
		}
		param.setLimit(condDto.getLimit());
		param.setOffset(condDto.getOffset());

		return param;
	}

	private java.sql.Date convertDate(java.util.Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return new java.sql.Date(cal.getTimeInMillis());
	}

	private BuiltinAuthUserDto convertUser(BuiltinAuthUser user) {
		BuiltinAuthUserDto userDto = new BuiltinAuthUserDto();
		userDto.setAccountExist(user.isAccountExist());
		userDto.setUserExist(user.isUserExist());

		userDto.setOid(user.getOid());
		userDto.setAccountId(user.getAccountId());
		userDto.setPolicyName(user.getPolicyName());
		userDto.setName(user.getName());
		userDto.setMail(user.getMail());
		userDto.setAdmin(user.isAdmin());
		userDto.setStartDate(user.getStartDate());
		userDto.setEndDate(user.getEndDate());
		userDto.setLastLoginOn(user.getLastLoginOn());
		userDto.setLoginErrorCnt(user.getLoginErrorCnt());
		userDto.setLoginErrorDate(user.getLoginErrorDate());
		userDto.setLastPasswordChange(user.getLastPasswordChange());
		userDto.setPasswordRemainDays(user.getPasswordRemainDays());

		return userDto;
	}

	@Override
	public BuiltinAuthUserResetErrorCountResultInfo resetErrorCount(final int tenantId, final List<BuiltinAuthUserDto> users) {

		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(), this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<BuiltinAuthUserResetErrorCountResultInfo>() {

			@Override
			public BuiltinAuthUserResetErrorCountResultInfo call() {

				BuiltinAuthUserResetErrorCountResultInfo ret = new BuiltinAuthUserResetErrorCountResultInfo();

				//対象アカウントIDの取得
				List<String> accountIds = new ArrayList<>(users.size());
				for (BuiltinAuthUserDto user : users) {
					accountIds.add(user.getAccountId());
				}

				//更新
				BuiltinAuthUserResetErrorCountResult result = service.resetErrorCount(accountIds);

				ret.setError(result.isError());
				ret.setMessages(result.getMessages());

				return ret;
			}

		});
	}
}
