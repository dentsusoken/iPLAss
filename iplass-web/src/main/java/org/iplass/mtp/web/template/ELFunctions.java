/*
 * Copyright (C) 2016 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.web.template;

import java.sql.Time;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.ResourceBundle;

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.prefs.MetaPreference.PreferenceRuntime;
import org.iplass.mtp.impl.prefs.MetaPreferenceSet.PreferenceSetRuntime;
import org.iplass.mtp.impl.prefs.PreferenceService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.DateUtil;
import org.iplass.mtp.util.ResourceBundleUtil;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.web.template.TemplateUtil.TokenOutputType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * EL式にて提供するカスタム関数が定義されるクラスです。
 * iPLAss上でTemplateとして管理されるJSPからは、 RequestContext, SessionContextそれぞれにsetAttributeした値を透過的に取得可能です。
 * </p>
 *
 * <p>
 * JSPでの利用例を以下に示します。
 * <pre>
 * &lt;%{@literal @}page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%&gt;
 * &lt;%{@literal @}taglib prefix="m" uri="http://iplass.org/tags/mtp"%&gt;
 * &lt;html&gt;
 * :
 * :
 * &lt;body&gt;
 * :
 * :
 * &lt;%-- testBean.user.nameの値をXMLエスケープして出力 --%&gt;
 * userName:${m:esc(testBean.user.name)}&lt;br&gt;
 * :
 * :
 * &lt;%-- m:tcPath()にてtenantContextPathを出力 --%&gt;
 * &lt;a href="${m:tcPath()}/user/update"&gt;更新する&lt;/a&gt;
 * :
 * :
 * &lt;/body&gt;
 * &lt;/html&gt;
 * </pre>
 * </p>
 *
 * @author K.Higuchi
 *
 */
public class ELFunctions {
	private static Logger logger = LoggerFactory.getLogger(ELFunctions.class);

	private static PreferenceService ps = ServiceRegistry.getRegistry().getService(PreferenceService.class);

	/**
	 * <p>
	 * RequestContextのインスタンスを取得します。
	 * </p>
	 * <p>
	 * 利用例<br>
	 * <pre>
	 * ${m:rc().session.getAttribute('cart').count}
	 * </pre>
	 * このEL関数呼び出しは、
	 * {@link TemplateUtil#getRequestContext()}呼び出しと等価です。
	 * </p>
	 * <p>
	 * <b>note:</b><Br>
	 * iPLAss3.0からは、JSPの暗黙変数requestから透過的にRequestContextの値を取得可能です。
	 * 当該関数は、明示的にRequestContextのインスタンスを取得したい場合に利用可能です。
	 * </p>
	 *
	 * @return　RequestContextのインスタンス
	 */
	public static RequestContext rc() {
		return TemplateUtil.getRequestContext();
	}

	/**
	 * tenant名まで含むコンテキストパスを取得します。<br>
	 * <br>
	 * 利用例<br>
	 * <pre>
	 * &lt;a href="${m:tcPath()}/path/to/action"&gt;link&lt;/a&gt;
	 * </pre>
	 * このEL関数呼び出しは、
	 * {@link TemplateUtil#getTenantContextPath()}呼び出しと等価です。
	 *
	 * @return テナントコンテキストパス
	 */
	public static String tcPath() {
		return TemplateUtil.getTenantContextPath();
	}

	/**
	 * Message定義（メタデータ）として登録されているメッセージを出力します。
	 * <br>
	 * 利用例<br>
	 * <pre>
	 * 多言語化されたメッセージ： ${m:msg('path/to/MessageCategory', 'M101')}
	 * </pre>
	 * このEL関数呼び出しは、
	 * {@link TemplateUtil#getMessageString(String, String)}呼び出しと等価です。
	 *
	 *
	 * @param categoryName メッセージ定義のカテゴリ名
	 * @param messageId メッセージID
	 * @return 整形されたメッセージ
	 */
	public static String msg(String categoryName, String messageId) {
		return TemplateUtil.getMessageString(categoryName, messageId);
	}

	/**
	 * Message定義（メタデータ）として登録されているメッセージを出力します。
	 * paramsには、メッセージに埋め込むパラメータ（単一のオブジェクト、もしくは配列、もしくはCollectionのインスタンス）を指定可能です。
	 * パラメータがない場合は、nullを指定します。<br>
	 * <br>
	 * 利用例<br>
	 * <pre>
	 * 多言語化されたメッセージ： ${m:msgp('path/to/MessageCategory', 'M101', bean.messageParams)}
	 * </pre>
	 * このEL関数呼び出しは、
	 * {@link TemplateUtil#getMessageString(String, String, Object...)}呼び出しと等価です。
	 *
	 *
	 * @param categoryName メッセージ定義のカテゴリ名
	 * @param messageId メッセージID
	 * @param params メッセージへ埋め込むパラメータ
	 * @return 整形されたメッセージ
	 */
	public static String msgp(String categoryName, String messageId, Object params) {
		if (params == null) {
			return TemplateUtil.getMessageString(categoryName, messageId);
		}
		if (params instanceof Object[]) {
			return TemplateUtil.getMessageString(categoryName, messageId, (Object[]) params);
		}
		if (params instanceof Collection<?>) {
			return TemplateUtil.getMessageString(categoryName, messageId, (Object[]) ((Collection<?>) params).toArray(new Object[((Collection<?>) params).size()]));
		}
		return TemplateUtil.getMessageString(categoryName, messageId, params);
	}

	/**
	 * strをJavaScript出力用にエスケープします。<br>
	 * <br>
	 * 利用例<br>
	 * <pre>
	 * ${m:escJs(bean.user.name)}
	 * </pre>
	 * このEL関数呼び出しは、
	 * {@link StringUtil#escapeJavaScript(String)}呼び出しと等価です。
	 *
	 * @param str エスケープする文字列
	 * @return エスケープされた文字列
	 */
	public static String escJs(String str) {
		return StringUtil.escapeJavaScript(str);
	}

	/**
	 * strをXML(XHTML)出力用にエスケープします。<br>
	 * <br>
	 * 利用例<br>
	 * <pre>
	 * ${m:escXml(bean.user.name)}
	 * </pre>
	 * このEL関数呼び出しは、
	 * {@link StringUtil#escapeXml10(String)}呼び出しと等価です。
	 *
	 * @param str エスケープする文字列
	 * @return エスケープされた文字列
	 */
	public static String escXml(String str) {
		return StringUtil.escapeXml10(str);
	}

	/**
	 * strをHTML出力用にエスケープします。<br>
	 * <br>
	 * 利用例<br>
	 * <pre>
	 * ${m:esc(bean.user.name)}
	 * </pre>
	 * このEL関数呼び出しは、
	 * {@link StringUtil#escapeHtml(String)}呼び出しと等価です。
	 *
	 * @param str エスケープする文字列
	 * @return エスケープされた文字列
	 */
	public static String esc(String str) {
		return StringUtil.escapeHtml(str);
	}

	/**
	 * トランザクショントークンを出力します。
	 * tokenOutputTypeにて、出力形式を指定します。tokenOutputTypeは{@link TokenOutputType}に定義されるEnum値もしくは、そのString表現を指定可能です。
	 * tokenOutputType、createNewについては、{@link TemplateUtil#outputToken(TokenOutputType, boolean)}を参照下さい。<br>
	 * <br>
	 * 利用例<br>
	 * <pre>
	 * ${m:outputToken('FORM_HTML', true)}
	 * </pre>
	 * このEL関数呼び出しは、
	 * {@link TemplateUtil#outputToken(TokenOutputType, boolean)}呼び出しと等価です。
	 *
	 * @param tokenOutputType 出力形式
	 * @param createNew 新規のトークンを発行する場合はtrue。すでに発行済みの（セッション単位に固定の）トークンを取得する場合はfalse。
	 * @return トランザクショントークン
	 */
	public static String outputToken(Object tokenOutputType, boolean createNew) {
		if (tokenOutputType instanceof TokenOutputType) {
			return TemplateUtil.outputToken((TokenOutputType) tokenOutputType, createNew);
		}
		return TemplateUtil.outputToken(TokenOutputType.valueOf(tokenOutputType.toString()), createNew);
	}

	/**
	 * トランザクショントークンの値を出力します。
	 * <br>
	 * 利用例<br>
	 * <pre>
	 * &lt;input type="hidden" name="_t" value="${m:token()}"&gt;
	 * </pre>
	 * このEL関数呼び出しは、outputStyle=VALUE、createNew=trueにて
	 * {@link TemplateUtil#outputToken(TokenOutputType, boolean)}呼び出しと等価です。
	 *
	 * @return トランザクショントークン
	 */
	public static String token() {
		return TemplateUtil.outputToken(TokenOutputType.VALUE, true);
	}

	/**
	 * トランザクショントークン（セッション単位の固定値）の値を出力します。
	 * <br>
	 * 利用例<br>
	 * <pre>
	 * &lt;input type="hidden" name="_t" value="${m:fixToken()}"&gt;
	 * </pre>
	 * このEL関数呼び出しは、outputStyle=VALUE、createNew=falseにて
	 * {@link TemplateUtil#outputToken(TokenOutputType, boolean)}呼び出しと等価です。
	 *
	 * @return トランザクショントークン（セッション単位の固定値）
	 */
	public static String fixToken() {
		return TemplateUtil.outputToken(TokenOutputType.VALUE, false);
	}

	/**
	 * nameで指定されるPreferenceの値を取得します。<br>
	 * PreferenceにruntimeClassが指定されている場合は、そのクラスのインスタンスが取得されます。<br>
	 * runtimeClass指定がない場合、かつPreferenceSetの場合は、Mapが取得されます。
	 * Preferenceの場合は、valueに定義されているStringが取得されます。
	 * <br>
	 * 利用例<br>
	 * <pre>
	 * ${m:prefs('preferenceName')}
	 * </pre>
	 *
	 * @param name Preferenceのname
	 * @return
	 */
	public static Object prefs(String name) {
		PreferenceRuntime pr = ps.getRuntimeByName(name);
		if (pr == null) {
			return null;
		}
		if (pr.getRuntime() != null) {
			return pr.getRuntime();
		}
		if (pr instanceof PreferenceSetRuntime) {
			return pr.getMap();
		} else {
			return pr.getMetaData().getValue();
		}
	}

	/**
	 * 指定のvalueをpatternでフォーマット出力します。
	 * <br>
	 * 利用例<br>
	 * <pre>
	 * &lt;input type="text" name="endDate" value="${m:fmt(dateVal, 'yyyy/MM/dd')}"&gt;
	 * </pre>
	 * valueには、Dateのインスタンスもしくは、Numberのインスタンスを指定可能です。
	 * Dateの場合は、SimpleDateFormatにて定義されるpattern、
	 * Numberの場合は、DecimalFormatにて定義されるpatternを指定可能です。
	 *
	 * @param value フォーマット対象のオブジェクト、Date or Number
	 * @param pattern フォーマットパターン
	 * @return フォーマットされた値。フォーマットできなかった場合null
	 */
	public static String fmt(Object value, String pattern) {
		try {
			if (value instanceof java.sql.Date) {
				SimpleDateFormat fmt = DateUtil.getSimpleDateFormat(pattern, false);
				return fmt.format((java.sql.Date) value);
			}
			if (value instanceof Time) {
				SimpleDateFormat fmt = DateUtil.getSimpleDateFormat(pattern, false);
				return fmt.format((java.sql.Time) value);
			}
			if (value instanceof Date) {
				SimpleDateFormat fmt = DateUtil.getSimpleDateFormat(pattern, true);
				return fmt.format((Date) value);
			}
			if (value instanceof Number) {
				DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance(ExecuteContext.getCurrentContext().getLocale());
				DecimalFormat fmt = new DecimalFormat(pattern, dfs);
				return fmt.format(value);
			}
		} catch (RuntimeException e) {
			logger.debug("cant format:" + value + ",pattern:" + pattern + ", " + e);
		}

		return null;
	}

	/**
	 * valがnullであった場合、defaultValを返却します。
	 * それ以外はvalを返却します。
	 * <br>
	 * 利用例<br>
	 * <pre>
	 * &lt;input type="text" name="test" value="${m:nvl(testVal, 'testVal is null')}"&gt;
	 * </pre>
	 *
	 * @param val null検証する値
	 * @param defaultVal valがnullの場合返却する値
	 * @return valもしくはdefaultVal
	 */
	public static Object nvl(Object val, Object defaultVal) {
		if (val != null) {
			return val;
		} else {
			return defaultVal;
		}
	}

	/**
	 * 指定された基底名、キーからResourceBundleに定義された文字列を返します。
	 * <br>
	 * 利用例<br>
	 * <pre>
	 * ${m:rs('resource-bundle-name', 'key')}
	 * </pre>
	 * このEL関数呼び出しは、
	 * {@link TemplateUtil#getResourceString(ResourceBundle, String, String)}呼び出しと等価です。
	 *
	 * @param baseName ResourceBundleのbaseName
	 * @param key キー
	 * @return 文字列
	 */
	public static String rs(String baseName, String key) {
		return TemplateUtil.getResourceString(ResourceBundleUtil.getResourceBundle(baseName), key);
	}

	/**
	 * 指定された基底名、キーからResourceBundleに定義された文字列を返します。
	 * paramsには、文字列に埋め込むパラメータ（単一のオブジェクト、もしくは配列、もしくはCollectionのインスタンス）を指定可能です。
	 * <br>
	 * 利用例<br>
	 * <pre>
	 * ${m:rsp('resource-bundle-name', 'key', params)}
	 * </pre>
	 * このEL関数呼び出しは、
	 * {@link TemplateUtil#getResourceString(ResourceBundle, String, String, Object...)}呼び出しと等価です。
	 *
	 * @param baseName ResourceBundleのbaseName
	 * @param key キー
	 * @param params 文字列へ埋め込むパラメータ
	 * @return 文字列
	 */
	public static String rsp(String baseName, String key, Object params) {
		if (params == null) {
			return TemplateUtil.getResourceString(ResourceBundleUtil.getResourceBundle(baseName), key);
		}
		if (params instanceof Object[]) {
			return TemplateUtil.getResourceString(ResourceBundleUtil.getResourceBundle(baseName), key, (Object[])params);
		}
		if (params instanceof Collection<?>) {
			return TemplateUtil.getResourceString(ResourceBundleUtil.getResourceBundle(baseName), key,
					(Object[]) ((Collection<?>) params).toArray(new Object[((Collection<?>) params).size()]));
		}
		return TemplateUtil.getResourceString(ResourceBundleUtil.getResourceBundle(baseName), key, params);
	}

}
