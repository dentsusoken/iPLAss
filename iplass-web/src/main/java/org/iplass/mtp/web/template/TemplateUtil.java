/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

import java.io.IOException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.jsp.PageContext;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.RequestContextWrapper;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.i18n.I18nService;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.i18n.LanguageFonts;
import org.iplass.mtp.impl.i18n.LocaleFormat;
import org.iplass.mtp.impl.util.PlatformUtil;
import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.impl.web.WebUtil;
import org.iplass.mtp.impl.web.token.TokenStore;
import org.iplass.mtp.message.MessageCategory;
import org.iplass.mtp.message.MessageItem;
import org.iplass.mtp.message.MessageManager;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.util.CollectionUtil;
import org.iplass.mtp.util.ResourceBundleUtil;
import org.iplass.mtp.util.StringUtil;

/**
 * テンプレートを作成する際利用可能なユーティリティメソッド。
 *
 * @author K.Higuchi
 *
 */
public class TemplateUtil {
	//TODO implのWebUtilから公開するメソッドを移動

	//TODO HttpServletRequestとかは生で扱わせない
	//TODO 階層的なRequestContextの保持も可能に。ネストされた呼び出しの場合、親のRequestContextを引き継ぐ、引き継がないを指定可能

	private static MessageManager mm = ManagerLocator.getInstance().getManager(MessageManager.class);

	/**
	 * <p>静的コンテンツパスを返します。</p>
	 *
	 * <p>
	 * service-configに設定されたstaticContentPathを返します。
	 * </p>
	 *
	 * @return 静的コンテンツパス
	 */
	public static String getStaticContentPath() {
		return WebUtil.getStaticContentPath();
	}

	/**
	 * <p>テナントコンテキストパスを返します。</p>
	 *
	 * <ul>
	 * <li>通常は「{@link javax.servlet.http.HttpServletRequest#getContextPath() HttpServletRequest#getContextPath()} + テナントメタ#テナントURL」を返します。
	 * <li>テナントURLが「/」の場合は、最後に「/」は付与しません。
	 * <li>テナントメタに「リクエストパス構築用テナントURL」を指定している場合はその値を返します。
	 * <li>「リクエストパス構築用テナントURL」が「/」の場合は空を返します。
	 * <p>
	 * 例えば通常の「http://host:port/tenant/」を「http://aaaaaa/」にマッピングしている場合、「リクエストパス構築用テナントURL」には「/」を指定します。
	 * この場合このメソッドの戻り値は空です。
	 * </p>
	 * </li>
	 * </ul>
	 */
	public static String getTenantContextPath() {
		return WebUtil.getTenantContextPath(WebRequestStack.getCurrent().getRequest());
	}

	/**
	 * <p>リソースパスからcssやjs、imgなどに指定するパスを返します。</p>
	 *
	 * <p>
	 * 以下のルールによりパスを判断します。
	 * <ul>
	 * <li>「/」で始まる場合は静的コンテキストパスからの指定と判断します。
	 * <li>「http」で始まる場合は外部リソースと判断します。
	 * <li>上記以外(actionなどの指定含む)の場合は、テナントコンテキストパスからの指定と判断します。
	 * </ul>
	 * </p>
	 *
	 * @param resourcePath
	 * @return
	 */
	public static String getResourceContentPath(String resourcePath) {
		if (resourcePath == null || resourcePath.isEmpty()) {
			return "";
		}

		if (resourcePath.startsWith("/")) {
			//静的コンテキストパスからと判断
			return getStaticContentPath() + resourcePath;
		} else if (resourcePath.startsWith("http")) {
			//外部リソースなのでそのまま返す
			return resourcePath;
		} else {
			//テナント配下の指定と判断
			return getTenantContextPath() + "/" + resourcePath;
		}
	}

	/**
	 * RequestContextのインスタンスを取得します。
	 *
	 * @return
	 */
	public static RequestContext getRequestContext() {
		return WebUtil.getRequestContext();
	}

	public static Tenant getTenant() {
		return ExecuteContext.getCurrentContext().getCurrentTenant();
	}

	public static int getClientTenantId() {
		return getTenant().getId();
	}

	/**
	 * <p>テンプレートを直接includeします。</p>
	 *
	 * <p>直接テンプレートをincludeするためCommandは実行されません。</p>
	 *
	 * @param templateName テンプレート名
	 * @param pageContext 呼び出し元がjspの場合、jspのPageContextを引数に渡す
	 * @throws IOException
	 * @throws ServletException
	 */
	public static void includeTemplate(String templateName, PageContext pageContext)
			throws IOException, ServletException {

		WebRequestStack current = WebRequestStack.getCurrent();
		if (pageContext == null) {
			if (current.getPageContext() != null) {
				pageContext = current.getPageContext();
			}
		}

		WebUtil.includeTemplate(templateName,
				current.getRequest(), current.getResponse(),
				current.getServletContext(), pageContext);
	}

	/**
	 * <p>テンプレートを直接includeします。</p>
	 *
	 * <p>
	 * 直接テンプレートをincludeするためCommandは実行されません。
	 * requestContextを指定した場合は、呼び出し先でその（上書きされた）requestContextで処理が実行されます。
	 * </p>
	 *
	 * @param templateName テンプレート名
	 * @param pageContext 呼び出し元がjspの場合、jspのPageContextを引数に渡す
	 * @param requestContext include先で利用するRequestContextを指定
	 * @throws IOException
	 * @throws ServletException
	 */
	public static void includeTemplate(String templateName, PageContext pageContext, RequestContextWrapper requestContext)
			throws IOException, ServletException {

		WebRequestStack current = WebRequestStack.getCurrent();
		if (pageContext == null) {
			if (current.getPageContext() != null) {
				pageContext = current.getPageContext();
			}
		}

		WebUtil.includeTemplate(templateName,
				current.getRequest(), current.getResponse(),
				current.getServletContext(), pageContext, requestContext);
	}

	/**
	 * 別Actionをincludeします。
	 *
	 * @param actionName アクション名
	 * @param pageContext 呼び出し元がjspの場合、jspのPageContextを引数に渡す
	 * @throws IOException
	 * @throws ServletException
	 */
	public static void include(String actionName, PageContext pageContext) throws IOException, ServletException {
		WebRequestStack current = WebRequestStack.getCurrent();
		if (pageContext == null) {
			if (current.getPageContext() != null) {
				pageContext = current.getPageContext();
			}
		}

		WebUtil.include(actionName, current.getRequest(), current.getResponse(), current.getServletContext(), pageContext);
	}

	/**
	 * <p>別Actionをincludeします。</p>
	 * <p>
	 * requestContextを指定した場合は、呼び出し先でその（上書きされた）requestContextで処理が実行されます。
	 * </p>
	 *
	 * @param actionName アクション名
	 * @param pageContext 呼び出し元がjspの場合、jspのPageContextを引数に渡す
	 * @param requestContext include先で利用するRequestContextを指定
	 * @throws IOException
	 * @throws ServletException
	 */
	public static void include(String actionName, PageContext pageContext, RequestContextWrapper requestContext) throws IOException, ServletException {
		WebRequestStack current = WebRequestStack.getCurrent();
		if (pageContext == null) {
			if (current.getPageContext() != null) {
				pageContext = current.getPageContext();
			}
		}

		WebUtil.include(actionName, current.getRequest(), current.getResponse(), current.getServletContext(), pageContext, requestContext);
	}

	/**
	 * レイアウトテンプレートで、コンテンツをレンダリングする場所で呼び出す。
	 *
	 * @param pageContext 呼び出し元がjspの場合、jspのPageContextを引数に渡す
	 */
	public static void renderContent(PageContext pageContext)
		throws IOException, ServletException{

		WebRequestStack current = WebRequestStack.getCurrent();
		if (pageContext == null) {
			if (current.getPageContext() != null) {
				pageContext = current.getPageContext();
			}
		}
		WebUtil.renderContent(current.getRequest(), current.getResponse(), current.getServletContext(), pageContext);
	}

	/**
	 * トランザクショントークン出力形式
	 */
	public static enum TokenOutputType {
		URL,FORM_HTML,FORM_XHTML,VALUE
	}

	/**
	 * <p>新規に生成されたトランザクショントークンを返します。</p>
	 * <p>
	 * createNew=trueで{@link #outputToken(TokenOutputType, boolean)}を呼び出します。
	 * </p>
	 *
	 * @param outputStyle 出力形式
	 * @return トランザクショントークン
	 */
	public static String outputToken(TokenOutputType outputStyle) {
		return outputToken(outputStyle, true);
	}

	/**
	 * <p>トランザクショントークンを返します。</p>
	 *
	 * <p>
	 * URLの場合、<code>_t=254a-fa...</code>形式で出力。<br>
	 * FORM_HTMLの場合、<code>&lt;input type="hidden" name="_t" value="254a-fa..." &gt;</code>形式で出力。<br>
	 * FORM_XHTMLの場合、<code>&lt;input type="hidden" name="_t" value="254a-fa..." /&gt</code>形式で出力。<br>
	 * VALUEの場合、単純にトランザクショントークンの値のみを出力（WebApi（javaScript）でHTTPヘッダーやリクエストにセットする際に利用することを想定）。
	 * </p>
	 * <p>
	 * createNewがtrueの場合は、ワンタイムのトランザクショントークンとして新規にトークンを生成します。<br>
	 * createNewがfalseの場合は、すでに発行済みの（セッション単位に固定の）トークンを返します。<br>
	 * 固定のトークンは、単純にCSRFを防ぎたい場合に利用可能です。
	 * ワンタイムのトークンはCSRF対策に加えて、そのトークンに関連するトランザクションの重複起動を防御することが可能です。
	 * </p>
	 *
	 * @param outputStyle 出力形式
	 * @param createNew 新規のトークンを発行する場合はtrue。すでに発行済みの（セッション単位に固定の）トークンを取得する場合はfalse。
	 * @return トランザクショントークン
	 */
	public static String outputToken(TokenOutputType outputStyle, boolean createNew) {
		String token;
		if (createNew) {
			token = TokenStore.createNewToken(getRequestContext().getSession());
		} else {
			token = TokenStore.getFixedToken(getRequestContext().getSession());
		}
		switch (outputStyle) {
		case URL:
			return TokenStore.TOKEN_PARAM_NAME + "=" + token;
		case FORM_HTML:
			return "<input type=\"hidden\" name=\"" + TokenStore.TOKEN_PARAM_NAME + "\" value=\"" + token + "\" >";
		case FORM_XHTML:
			return "<input type=\"hidden\" name=\"" + TokenStore.TOKEN_PARAM_NAME + "\" value=\"" + token + "\" />";
		case VALUE:
			return token;
		default:
			return "";
		}
	}

	public static Timestamp getCurrentTimestamp() {
		return ExecuteContext.getCurrentContext().getCurrentTimestamp();
	}

	/**
	 * リソースファイルに定義した文字列を取得します。
	 *
	 * @param key KEY
	 * @return リソース文字列
	 * @deprecated {@link TemplateUtil#getResourceString(String, Object...)} を利用してください。
	 */
	@Deprecated
	public static String getString(String key) {
		return getString(key, (Object[])null);
	}

	/**
	 * リソースファイルに定義した文字列を取得します。
	 *
	 * @param key KEY
	 * @param arguments 引数
	 * @return リソース文字列
	 * @deprecated {@link TemplateUtil#getResourceString(String, Object...)} を利用してください。
	 */
	@Deprecated
	public static String getString(String key, Object... arguments) {
		return getResourceString(key, arguments);
	}

	/**
	 * リソースファイルに定義した文字列を取得します。
	 *
	 * @param key KEY
	 * @param arguments 引数
	 * @return リソース文字列
	 */
	public static String getResourceString(String key, Object... arguments) {
		return ResourceBundleUtil.resourceString(key, arguments);
	}

	/**
	 * リソースファイルに定義した文字列を取得します。
	 *
	 * @param resource リソースバンドル
	 * @param key KEY
	 * @param arguments 引数
	 * @return リソース文字列
	 */
	public static String getResourceString(ResourceBundle resource, String key, Object... arguments) {
		return ResourceBundleUtil.resourceString(resource, key, arguments);
	}

	/**
	 * APIのバージョン番号を返します。
	 *
	 * @return バージョン番号
	 */
	public static String getAPIVersion() {
		return PlatformUtil.getAPIVersion();
	}

	public static Map<String, String> getEnableLanguages() {
		I18nService i18n = ServiceRegistry.getRegistry().getService(I18nService.class);
		return i18n.getEnableLanguagesMap();
	}

	/**
	 * 指定のカテゴリ名のMessageCategoryを取得。
	 *
	 * @param catName
	 * @return
	 */
	public static MessageCategory getMessageCategory(String catName) {
		return mm.get(catName);
	}

	/**
	 * 指定のカテゴリ名、メッセージIDでメッセージ文字列を取得。
	 * 取得されたメッセージ文字列は、多言語を考慮した形で、フォーマット済み。
	 *
	 * @param catName
	 * @param msgId
	 * @param args メッセージをフォーマットする場合の引数
	 * @return
	 */
	public static String getMessageString(String catName, String msgId, Object... args) {
		return getMessageString(mm.getMessageItem(catName, msgId), args);
	}

	/**
	 * 指定のMessageItemでメッセージ文字列を取得。
	 * 取得されたメッセージ文字列は、多言語を考慮した形で、フォーマット済み。
	 *
	 * @param message
	 * @param args
	 * @return
	 */
	public static String getMessageString(MessageItem message, Object... args) {
		if (message == null) {
			return null;
		}
		String str = I18nUtil.stringDef(message.getMessage(), message.getLocalizedMessageList());
		if (args != null) {
			return MessageFormat.format(str, args);
		} else {
			return str;
		}
	}

	public static String getMultilingualString(String defaultString, List<LocalizedStringDefinition> localizedStringList) {
		return I18nUtil.stringDef(defaultString, localizedStringList);
	}

	public static String getMultilingualString(
			String viewString, List<LocalizedStringDefinition> viewLocalizedStringList,
			String propDefString, List<LocalizedStringDefinition> propDefLocalizedStringList) {

		String propDefLabel = I18nUtil.stringDef(propDefString, propDefLocalizedStringList);

		if (StringUtil.isBlank(viewString)) {
			return propDefLabel;
		}

		String viewLabel = I18nUtil.stringDef(viewString, viewLocalizedStringList);

		if (StringUtil.isBlank(viewLabel)) {
			return propDefLabel;
		} else {
			return viewLabel;
		}
	}

	public static String getLanguage() {
		return ExecuteContext.getCurrentContext().getLanguage();
	}

	public static Locale getLocale() {
		return ExecuteContext.getCurrentContext().getLocale();
	}

	public static TimeZone getTimeZone() {
		return ExecuteContext.getCurrentContext().getTimeZone();
	}

	public static LocaleFormat getLocaleFormat() {
		return ExecuteContext.getCurrentContext().getLocaleFormat();
	}

	public static String getLanguageFonts(String langage) {
		I18nService i18n = ServiceRegistry.getRegistry().getService(I18nService.class);
		for (LanguageFonts fonts : i18n.getLanguageFonts()) {
			if (fonts.getLanguage().equals(langage)) {
				return getLangFont(fonts);
			}
		}
		return null;
	}

	private static String getLangFont(LanguageFonts fonts) {
		List<String> genericFontFamilyList = ServiceRegistry.getRegistry().getService(I18nService.class)
				.getGenericFontFamilyList();
		boolean isNotEmptyGenericFontFamilyList = CollectionUtil.isNotEmpty(genericFontFamilyList);

		String genericFontFamily = null;

		StringBuilder sb = new StringBuilder();
		for (String font : fonts.getFonts()) {
			if (isNotEmptyGenericFontFamilyList && genericFontFamilyList.contains(font)) {
				genericFontFamily = font;
				continue;
			}

			if (sb.length() != 0) {
				sb.append(",");
			}
			sb.append("\"").append(font).append("\"");
		}

		if (genericFontFamily != null) {
			sb.append(",").append(genericFontFamily);
		}

		return sb.toString();
	}
}
