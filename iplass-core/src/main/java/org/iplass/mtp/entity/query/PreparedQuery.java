/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.entity.query;

import java.sql.Timestamp;
import java.util.Map;

import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.query.prepared.PreparedQueryBinding;
import org.iplass.mtp.impl.query.prepared.PreparedQueryTemplate;
import org.iplass.mtp.impl.query.prepared.PreparedQueryTemplateFactory;


/**
 * <p>
 * バインド変数を定義可能なQuery。
 * </P>
 * <code>
 * select oid, name, mail from MtpUser where oid='${user.oid}' and startDate <= '${sysdate}' and customProp='${session.customProp}'
 * </code>
 * <p>
 * といった形で定義されたquery実行時に変数にバインド可能。
 * </p>
 * <p>
 * デフォルトでバインド可能な変数、関数
 * <ul>
 * <li>user : 実行するユーザーの情報。※１参照</li>
 * <li>date : 現在日時のjava.util.Dateのインスタンス。</li>
 * <li>sysdate : 現在日付（時間含まず）の文字列。テナントローカルなタイムゾーンの日付。例：2011-01-20</li>
 * <li>sysdatetime : 現在日時の文字列。テナントローカルなタイムゾーンの日時。例：2011-01-18 02:05:03.348</li>
 * <li>systime : 現在時間の文字列。テナントローカルなタイムゾーンの時間。例:23:19:00</li>
 * <li>toIn(Collection/Array): 引数のCollectionもしくは配列を、inの文字列表現に変換。利用例："oid in (${toIn(user.groupOid)})"</li>
 * <li>toDateString(Date): 引数のjava.util.Dateのインスタンスを日付の文字列表現に変換。利用例"startDate > '${toDateString(date)}'"</li>
 * <li>toLocalDateString(Date): 引数のjava.util.Dateのインスタンスをローカル日付の文字列表現に変換。利用例"startDate > '${toLocalDateString(date)}'"</li>
 * <li>toDateTimeString(Date): 引数のjava.util.Dateのインスタンスを日時の文字列表現に変換。利用例"startDateTime > '${toDateTimeString(date)}'"</li>
 * <li>toTimeString(Date): 引数のjava.util.Dateのインスタンスを時間の文字列表現に変換。利用例"startTime > '${toTimeString(date)}'"</li>
 * <li>toLocalTimeString(Date): 引数のjava.util.Dateのインスタンスをローカル時間の文字列表現に変換。利用例"startTime > '${toLocalTimeString(date)}'"</li>
 * <li>addYear(Date, int): 引数のjava.util.Dateのインスタンスに指定の年を加えたjava.util.Dateインスタンスを取得する。利用例"startDate > '${toDateString(addYear(date, -1))}'"</li>
 * <li>addMonth(Date, int): 引数のjava.util.Dateのインスタンスに指定の月を加えたjava.util.Dateインスタンスを取得する。利用例"startDate > '${toDateString(addMonth(date, 3))}'"</li>
 * <li>addWeek(Date, int): 引数のjava.util.Dateのインスタンスに指定の週を加えたjava.util.Dateインスタンスを取得する。利用例"startDate > '${toDateString(addWeek(date, -2))}'"</li>
 * <li>addDay(Date, int): 引数のjava.util.Dateのインスタンスに指定の日を加えたjava.util.Dateインスタンスを取得する。利用例"startDate > '${toDateString(addDay(date, 10))}'"</li>
 * <li>addHour(Date, int): 引数のjava.util.Dateのインスタンスに指定の時間を加えたjava.util.Dateインスタンスを取得する。利用例"startDateTime > '${toDateTimeString(addHour(date, -12))}'"</li>
 * <li>addMinute(Date, int): 引数のjava.util.Dateのインスタンスに指定の分を加えたjava.util.Dateインスタンスを取得する。利用例"startTime > '${toTimeString(addMinute(date, 30))}'"</li>
 * <li>addSecond(Date, int): 引数のjava.util.Dateのインスタンスに指定の秒を加えたjava.util.Dateインスタンスを取得する。利用例"startTime > '${toTimeString(addSecond(date, -10))}'"</li>
 * <li>addMillisecond(Date, int): 引数のjava.util.Dateのインスタンスに指定のミリ秒を加えたjava.util.Dateインスタンスを取得する。利用例"startDateTime > '${toDateTimeString(addMillisecond(date, -500))}'"</li>
 * </ul>
 * 他、bind()メソッド呼び出し時に独自のバインド変数を設定可能。
 * また、GroovyTemplateとして実装しているので、javaメソッド呼び出しも可能。
 * PreparedQueryのインスタンス自体は再利用可能。再利用することにより、以降のbind()時は高速に動作可能。
 * </p>
 * <h5>userについて※１</h5>
 * <p>
 * userにバインドされているインスタンスから、Userエンティティに登録されている情報を取得可能。
 * バインドされているインスタンス自体はorg.iplass.mtp.auth.Userではない点注意。
 * Userエンティティに登録されている属性に加えて、以下の変数/関数を取得/実行可能。
 * <ul>
 * <li>groupCode : ユーザーの所属するグループのグループコードのString[]。（※セキュリティのEntityの限定条件に利用する場合、groupOidの利用の方が高速です。）</li>
 * <li>groupCodeWithChildren : ユーザーの所属するグループ、およびそのサブグループ含めたすべてのグループコードのString[]。</li>
 * <li>groupCodeWithParents : ユーザーの所属するグループ、およびその親グループ（ルートまで）含めたすべてのグループコードのString[]。</li>
 * <li>groupOid : ユーザーの所属するグループのoidのString[]。</li>
 * <li>groupOidWithChildren : ユーザーの所属するグループ、およびそのサブグループ含めたすべてのグループのoidのString[]。</li>
 * <li>groupOidWithParents : ユーザーの所属するグループ、およびその親グループ（ルートまで）含めたすべてのグループのoidのString[]。</li>
 * <li>memberOf(String): ユーザーが、引数のグループコードのメンバ（サブグループに所属していても）の場合trueを返す。</li>
 * </ul>
 * </p>
 *
 * @author K.Higuchi
 *
 */
public class PreparedQuery {
	private PreparedQueryTemplate tmpl;

	public PreparedQuery(String queryString) {
		tmpl = PreparedQueryTemplateFactory.createPreparedQueryTemplate(queryString);
	}

	public String getQueryString() {
		return tmpl.getQueryString();
	}

	private PreparedQueryBinding newBinding(Map<String, Object> binding) {
		ExecuteContext ex = ExecuteContext.getCurrentContext();
		Timestamp date = ex.getCurrentTimestamp();//同一トランザクション内の時間を一緒にするため
		return new PreparedQueryBinding(
				date,
				AuthContextHolder.getAuthContext().newUserBinding(),
				binding);
	}

	/**
	 * queryExpressionをQueryとして取得する。
	 *
	 * @param binding
	 * @return
	 */
	public Query query(Map<String, Object> binding) {
		return tmpl.query(newBinding(binding));
	}

	/**
	 * queryExpressionをConditionとして取得する。
	 *
	 * @param binding
	 * @return
	 */
	public Condition condition(Map<String, Object> binding) {
		return tmpl.condition(newBinding(binding));
	}

	/**
	 * queryExpressionをValueExpressionとして取得する。
	 *
	 * @param binding
	 * @return
	 */
	public ValueExpression value(Map<String, Object> binding) {
		return tmpl.value(newBinding(binding));
	}

}
