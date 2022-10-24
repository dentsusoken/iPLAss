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

package org.iplass.mtp.impl.tools.auth.builtin;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import org.apache.commons.lang3.time.DateUtils;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.SystemException;
import org.iplass.mtp.auth.User;
import org.iplass.mtp.auth.policy.definition.AuthenticationPolicyDefinition;
import org.iplass.mtp.auth.policy.definition.AuthenticationPolicyDefinitionManager;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.LoadOption;
import org.iplass.mtp.entity.query.PreparedQuery;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.QueryException;
import org.iplass.mtp.entity.query.SortSpec;
import org.iplass.mtp.entity.query.SortSpec.SortType;
import org.iplass.mtp.entity.query.Where;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.expr.And;
import org.iplass.mtp.entity.query.condition.expr.Or;
import org.iplass.mtp.entity.query.condition.predicate.GreaterEqual;
import org.iplass.mtp.entity.query.condition.predicate.IsNotNull;
import org.iplass.mtp.entity.query.condition.predicate.Lesser;
import org.iplass.mtp.entity.query.condition.predicate.LesserEqual;
import org.iplass.mtp.entity.query.condition.predicate.Like;
import org.iplass.mtp.entity.query.condition.predicate.Like.MatchPattern;
import org.iplass.mtp.impl.auth.authenticate.builtin.BuiltinAccount;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapterService;
import org.iplass.mtp.impl.tools.auth.builtin.cond.SearchOperator;
import org.iplass.mtp.impl.tools.auth.builtin.cond.UserAttributeCondition;
import org.iplass.mtp.impl.tools.auth.builtin.cond.UserSpecificCondition;
import org.iplass.mtp.impl.tools.auth.builtin.cond.UserSpecificCondition.SpecificType;
import org.iplass.mtp.impl.util.InternalDateUtil;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.util.DateUtil;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BuiltinAuthToolService implements Service {

	private static final Logger logger = LoggerFactory.getLogger(BuiltinAuthToolService.class);

	private RdbAdapter rdb;
	private AccountToolDao accountToolDao;
	private EntityManager em;
	private AuthenticationPolicyDefinitionManager apdm;

	@Override
	public void init(Config config) {
		rdb = config.getDependentService(RdbAdapterService.class).getRdbAdapter();
		accountToolDao = new AccountToolDao(rdb);
		em = ManagerLocator.getInstance().getManager(EntityManager.class);
		apdm = ManagerLocator.getInstance().getManager(AuthenticationPolicyDefinitionManager.class);
	}

	@Override
	public void destroy() {
	}

	/**
	 * ユーザ情報を検索します。
	 *
	 * @param param 検索条件
	 * @return ユーザ情報
	 */
	public BuiltinAuthUserSearchResult search(final BuiltinAuthUserSearchParameter param) {
		BuiltinAuthUserSearchResult result = new BuiltinAuthUserSearchResult();

		if (param.getCondition() instanceof UserSpecificCondition) {
			UserSpecificCondition cond = (UserSpecificCondition)param.getCondition();

			AuthenticationPolicyDefinition authPolicy = null;
			if (cond.getType() == SpecificType.LOCKED) {
				authPolicy = apdm.getOrDefault(cond.getLockedUserPolicyName());
				if (authPolicy.getAccountLockoutPolicy().getLockoutFailureCount() == 0) {
					//無期限
					result.setTotalCount(0);
					result.setExecuteOffset(0);
					result.setUsers(Collections.emptyList());
					return result;
				}
			} else if (cond.getType() == SpecificType.EXPIRED_PASSWORD) {
				authPolicy = apdm.getOrDefault(cond.getPasswordRemainDaysPolicyName());
				if (authPolicy.getPasswordPolicy().getMaximumPasswordAge() == 0) {
					//無期限
					result.setTotalCount(0);
					result.setExecuteOffset(0);
					result.setUsers(Collections.emptyList());
					return result;
				}
			}

			//件数の取得
			int totalCount = countAccount(cond, authPolicy);
			result.setTotalCount(totalCount);
			if (param.getOffset() > 0) {
				if (totalCount <= param.getOffset()) {
					//offsetの方が大きい場合はoffsetを0にする
					result.addMessages("該当件数(" + totalCount + ")よりoffset(" + param.getOffset() + ")が大きいため、"
							+ "offsetを0にして検索します。");
					param.setOffset(0);
				}
			}
			result.setExecuteOffset(param.getOffset());

			//Account検索
			final List<BuiltinAuthUser> userList = new ArrayList<>();
			searchAccount(cond, authPolicy, param.getLimit(), param.getOffset(), new Predicate<BuiltinAuthUser>() {

				@Override
				public boolean test(BuiltinAuthUser user) {
					userList.add(user);
					return true;
				}
			});
			result.setUsers(userList);

		} else if (param.getCondition() instanceof UserAttributeCondition) {
			UserAttributeCondition cond = (UserAttributeCondition)param.getCondition();

			try {
				//検索条件の生成
				Where where = getUserWhere(cond);

				//件数の取得
				int totalCount = countUser(where);
				result.setTotalCount(totalCount);
				if (param.getOffset() > 0) {
					if (totalCount <= param.getOffset()) {
						//offsetの方が大きい場合はoffsetを0にする
						result.addMessages("該当件数(" + totalCount + ")よりoffset(" + param.getOffset() + ")が大きいため、"
								+ "offsetを0にして検索します。");
						param.setOffset(0);
					}
				}
				result.setExecuteOffset(param.getOffset());

				//User検索
				final List<BuiltinAuthUser> userList = new ArrayList<>();
				searchUser(where, param.getLimit(), param.getOffset() , new Predicate<BuiltinAuthUser>() {

					@Override
					public boolean test(BuiltinAuthUser user) {
						userList.add(user);
						return true;
					}
				});
				result.setUsers(userList);

			} catch (QueryException e) {
				//DirectWhere時のParseエラー発生時の対応
				result.setError(true);
				result.addMessages(e.getMessage());
				result.addMessages("Where:" + cond.getDirectWhere());
			}

		} else {
			result.setError(true);
			result.addMessages("unsupport condition type. condition is "
						+ (param.getCondition() != null ? param.getCondition().getClass().getName() : "null"));
			return result;
		}

		return result;
	}

	/**
	 * ユーザ情報をCSV出力します。
	 *
	 * @param os OutputStream
	 * @param param 検索条件
	 * @throws IOException
	 */
	public void exportCsv(final OutputStream os, final BuiltinAuthUserSearchParameter param) throws IOException {

		try (final BuiltinAuthUserCsvWriter writer = new BuiltinAuthUserCsvWriter(os)){

			//ヘッダ出力
			writer.writeHeader();

			//検索処理
			search(param, new Predicate<BuiltinAuthUser>() {

				@Override
				public boolean test(BuiltinAuthUser user) {
					try {
						writer.writeUser(user);
					} catch (IOException e) {
						logger.error("An error occurred in the CSV output of Auth User.", e);
						throw new SystemException(e);
					}

					return true;
				}
			});
		}
	}

	/**
	 * Accountベースの件数取得
	 *
	 * @param cond   検索条件
	 */
	private int countAccount(UserSpecificCondition cond, AuthenticationPolicyDefinition authPolicy) {
		return accountToolDao.countAccount(cond, authPolicy);
	}

	/**
	 * Accountベースの検索
	 *
	 * @param result 結果
	 * @param cond   検索条件
	 * @param limit  limit
	 * @param offset offset
	 */
	private void searchAccount(final UserSpecificCondition cond, final AuthenticationPolicyDefinition authPolicy,
			final int limit, final int offset, final Predicate<BuiltinAuthUser> callback) {

		//Accountの検索
		accountToolDao.searchAccount(cond, authPolicy, limit, offset, new Predicate<BuiltinAccount>() {

			@Override
			public boolean test(BuiltinAccount account) {
				return callback.test(toAuthUserByAccount(account, authPolicy));
			}
		});
	}

	/**
	 * Account情報からUser情報を生成します。
	 *
	 * @param accountList Account情報
	 * @return User情報
	 */
	private BuiltinAuthUser toAuthUserByAccount(BuiltinAccount account, AuthenticationPolicyDefinition authPolicy) {

		BuiltinAuthUser authUser = new BuiltinAuthUser();
		authUser.setAccountExist(true);
		authUser.setAccountId(account.getAccountId());
		authUser.setOid(account.getOid());
		authUser.setPolicyName(account.getPolicyName());

		authUser.setLoginErrorCnt(account.getLoginErrorCnt());
		authUser.setLoginErrorDate(account.getLoginErrorDate());
		authUser.setLastLoginOn(account.getLastLoginOn());
		authUser.setLastPasswordChange(account.getLastPasswordChange());

		//最終ログイン取得時などAuthPolicyが未取得の場合
		if (authPolicy == null) {
			authPolicy = apdm.getOrDefault(account.getPolicyName());
		}
		authUser.setPasswordRemainDays(getPasswordRemainDays(account.getLastPasswordChange(), authPolicy));

		//UserEntity情報の取得
		applyUser(authUser);

		return authUser;
	}

	/**
	 * Account情報からUserEntityを検索して値をセットします。
	 *
	 * @param user Account情報が設定されたUser
	 */
	private void applyUser(BuiltinAuthUser authUser) {
		User user = (User)em.load(authUser.getOid(), User.DEFINITION_NAME, new LoadOption(false, false));

		if (user != null) {
			authUser.setUserExist(true);
			authUser.setName(user.getName());
			authUser.setMail(user.getMail());
			authUser.setStartDate(user.getStartDate());
			authUser.setEndDate(user.getEndDate());
			authUser.setAdmin(user.isAdmin());
		}
	}

	/**
	 * UserEntityベースの検索条件の生成
	 *
	 * @param cond
	 * @return
	 */
	private Where getUserWhere(UserAttributeCondition cond) {

		//条件生成
		Where where = new Where();
		List<Condition> conditions = new ArrayList<Condition>();

		if (StringUtil.isNotBlank(cond.getAccountId())) {
			conditions.add(new Like(User.ACCOUNT_ID, cond.getAccountId(), MatchPattern.PARTIAL));
		}
		if (StringUtil.isNotBlank(cond.getName())) {
			String name = cond.getName();

			String names[] = null;
			if (name.contains(" ")) {
				names = name.split(" ", 2);	//最大で2つのみ
			} else if (name.contains("　")) {
				names = name.split("　", 2);	//最大で2つのみ
			} else {
				names = new String[]{name};
			}
			if (names.length == 2) {
				conditions.add(new Or(
						new And(
							new Like(User.FIRST_NAME, names[0], MatchPattern.PARTIAL),
							new Like(User.LAST_NAME, names[1], MatchPattern.PARTIAL)),
						new And(
								new Like(User.LAST_NAME, names[0], MatchPattern.PARTIAL),
								new Like(User.FIRST_NAME, names[1], MatchPattern.PARTIAL)),
						new And(
								new Like(User.FIRST_NAME_KANA, names[0], MatchPattern.PARTIAL),
								new Like(User.LAST_NAME_KANA, names[1], MatchPattern.PARTIAL)),
						new And(
								new Like(User.LAST_NAME_KANA, names[0], MatchPattern.PARTIAL),
								new Like(User.FIRST_NAME_KANA, names[1], MatchPattern.PARTIAL))
						));
			} else if (names.length == 1) {
				conditions.add(new Or(
						new Like(User.FIRST_NAME, names[0], MatchPattern.PARTIAL),
						new Like(User.LAST_NAME, names[0], MatchPattern.PARTIAL),
						new Like(User.FIRST_NAME_KANA, names[0], MatchPattern.PARTIAL),
						new Like(User.LAST_NAME_KANA, names[0], MatchPattern.PARTIAL)
						));
			}
		}
		if (StringUtil.isNotBlank(cond.getMail())) {
			conditions.add(new Like(User.MAIL, cond.getMail(), MatchPattern.PARTIAL));
		}
		if (cond.getValidTermRemainDaysOparator() != null) {
			conditions.add(new IsNotNull(Entity.END_DATE));

			java.util.Date sysDate = new Date(System.currentTimeMillis());
			if (cond.getValidTermRemainDays() <= 0) {
				//0日の場合は無効なデータをすべて検索
				conditions.add(new Lesser(Entity.END_DATE, adjustTimes(sysDate, 0, 0, 0, 0)));
			} else {
				//1日以上の場合はシステム日時に対して条件設定
				java.util.Date calcDate = DateUtils.addDays(sysDate, cond.getValidTermRemainDays());
				if (SearchOperator.EQUAL == cond.getValidTermRemainDaysOparator()) {
					//時間レベルを0とMaxで挟んで検索
					conditions.add(new And(
							new GreaterEqual(Entity.END_DATE, adjustTimes(calcDate, 0, 0, 0, 0)),
							new LesserEqual(Entity.END_DATE, adjustTimes(calcDate, 23, 59, 59, 999))
							));
				} else if (SearchOperator.LESSEQUAL == cond.getValidTermRemainDaysOparator()) {
					//時間レベルをMaxにして検索
					conditions.add(new LesserEqual(Entity.END_DATE, adjustTimes(calcDate, 23, 59, 59, 999)));
				}
			}
		}
		//Where条件が直接指定されていた場合はPreparedQueryで変換
		if (cond.getDirectWhere() != null && !cond.getDirectWhere().isEmpty()) {
			conditions.add(new PreparedQuery(cond.getDirectWhere()).condition(null));
		}

		if (!conditions.isEmpty()) {
			where.setCondition(new And(conditions));
		}

		return where;
	}

	/**
	 * UserEntityベースの件数取得
	 *
	 * @param where 検索条件
	 */
	private int countUser(Where where) {

		//件数検索
		Query countQuery = new Query();
		countQuery.select(User.ACCOUNT_ID)
				.from(User.DEFINITION_NAME)
				.where(where.getCondition())
				.setVersioned(false);

		return em.count(countQuery);
	}

	/**
	 * UserEntityベースの検索
	 *
	 * @param result 結果
	 * @param cond   検索条件
	 * @param limit  limit
	 * @param offset offset
	 */
	private void searchUser(Where where, int limit, int offset, final Predicate<BuiltinAuthUser> callback) {

		//検索
		Query query = new Query();
		query.selectAll(User.DEFINITION_NAME, true, false, false)
				.where(where.getCondition())
				.order(new SortSpec(User.ACCOUNT_ID, SortType.ASC))
				.limit(limit, offset)
				.setVersioned(false);

		em.searchEntity(query, new Predicate<User>() {

			@Override
			public boolean test(User user) {
				return callback.test(toAuthUserByUser(user));
			}
		});
	}

	private java.sql.Date adjustTimes(java.util.Date date, int hour, int minute, int second, int millisecond) {
//		Calendar cal = Calendar.getInstance();
		Calendar cal = DateUtil.getCalendar(true);
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, second);
		cal.set(Calendar.MILLISECOND, millisecond);
		return new java.sql.Date(cal.getTimeInMillis());
	}

	/**
	 * Account情報からUser情報を生成します。
	 *
	 * @param accountList Account情報
	 * @return User情報
	 */
	private BuiltinAuthUser toAuthUserByUser(User user) {

		BuiltinAuthUser authUser = new BuiltinAuthUser();
		authUser.setUserExist(true);
		authUser.setAccountId(user.getAccountId());
		authUser.setOid(user.getOid());
		authUser.setPolicyName(user.getAccountPolicy());

		authUser.setName(user.getName());
		authUser.setMail(user.getMail());
		authUser.setStartDate(user.getStartDate());
		authUser.setEndDate(user.getEndDate());
		authUser.setAdmin(user.isAdmin());

		//Account情報の取得
		applyAccount(authUser);

		return authUser;
	}

	/**
	 * User情報からAccountを検索して値をセットします。
	 *
	 * @param user User情報が設定されたUser
	 */
	private void applyAccount(BuiltinAuthUser authUser) {
		BuiltinAccount account = accountToolDao.getAccount(authUser.getAccountId());

		if (account != null) {
			authUser.setAccountExist(true);

			AuthenticationPolicyDefinition authPolicy = apdm.getOrDefault(authUser.getPolicyName());

			authUser.setLoginErrorCnt(account.getLoginErrorCnt());
			authUser.setLoginErrorDate(account.getLoginErrorDate());
			authUser.setLastLoginOn(account.getLastLoginOn());
			authUser.setLastPasswordChange(account.getLastPasswordChange());
			authUser.setPasswordRemainDays(getPasswordRemainDays(account.getLastPasswordChange(), authPolicy));
		}
	}

	/**
	 * ユーザ情報を検索します。
	 * 該当ユーザ１件ごとにCallbackに返します。
	 *
	 * @param param 検索条件
	 * @param callback 該当ユーザ情報を処理するCallback
	 */
	private void search(final BuiltinAuthUserSearchParameter param, final Predicate<BuiltinAuthUser> callback) {

		if (param.getCondition() instanceof UserSpecificCondition) {
			UserSpecificCondition cond = (UserSpecificCondition)param.getCondition();

			AuthenticationPolicyDefinition authPolicy = null;
			if (cond.getType() == SpecificType.LOCKED) {
				authPolicy = apdm.getOrDefault(cond.getLockedUserPolicyName());
				if (authPolicy.getAccountLockoutPolicy().getLockoutFailureCount() == 0) {
					//無期限
					return;
				}
			} else if (cond.getType() == SpecificType.EXPIRED_PASSWORD) {
				authPolicy = apdm.getOrDefault(cond.getPasswordRemainDaysPolicyName());
				if (authPolicy.getPasswordPolicy().getMaximumPasswordAge() == 0) {
					//無期限
					return;
				}
			}

			//Account検索
			searchAccount(cond, authPolicy, param.getLimit(), param.getOffset(), new Predicate<BuiltinAuthUser>() {

				@Override
				public boolean test(BuiltinAuthUser user) {
					return callback.test(user);
				}
			});

		} else if (param.getCondition() instanceof UserAttributeCondition) {
			UserAttributeCondition cond = (UserAttributeCondition)param.getCondition();

			//検索条件の生成
			Where where = getUserWhere(cond);

			//User検索
			searchUser(where, param.getLimit(), param.getOffset() , new Predicate<BuiltinAuthUser>() {

				@Override
				public boolean test(BuiltinAuthUser user) {
					return callback.test(user);
				}
			});
		}
	}

	/**
	 * ユーザのログインエラー回数をリセットします。
	 *
	 * @param accountIds 対象アカウントID
	 * @return 更新結果
	 */
	public BuiltinAuthUserResetErrorCountResult resetErrorCount(List<String> accountIds) {

		BuiltinAuthUserResetErrorCountResult result = new BuiltinAuthUserResetErrorCountResult();

		try {
			int allCount = 0;
			for (String accountId : accountIds) {
				//アカウントの検索
				BuiltinAccount account = accountToolDao.getAccount(accountId);
				if (account == null) {
					result.addMessages("not found " + accountId + ".");
					continue;
				}

				//更新
				accountToolDao.resetLoginErrorCnt(account);
				allCount ++;
				result.addMessages("reset login error count for " + account.getAccountId() + ".");
			}
			result.addMessages("Result : SUCCESS");
			result.addMessages("Reset Count : " + allCount);

		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
			result.setError(true);
			result.addMessages("Result : FAILURE");
			result.addMessages("Cause : " + (e.getMessage() != null ? e.getMessage() : e.getClass().getName()));
		}
		return result;
	}

	private Integer getPasswordRemainDays(Date lastPasswordChange, AuthenticationPolicyDefinition authPolicy) {
		if (lastPasswordChange == null) {
			//パスワード変更日が取得できない場合はnull（未ログイン時など）
			return null;
		}

		//最大日数を取得してLimitの日付を算出
		int maxAge = authPolicy.getPasswordPolicy().getMaximumPasswordAge();
		if (maxAge > 0) {
			//時分秒をクリア
			Date lastPasswordChangeDate = InternalDateUtil.truncateTime(lastPasswordChange);
			Date limitDate = InternalDateUtil.addDays(lastPasswordChangeDate, maxAge);

			//残日数を計算
			Date now = ExecuteContext.getCurrentContext().getCurrentLocalDate();
			return InternalDateUtil.diffDays(limitDate, now);
		} else {
			return null;
		}
	}

}
