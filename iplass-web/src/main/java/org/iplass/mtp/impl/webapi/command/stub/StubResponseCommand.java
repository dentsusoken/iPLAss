/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.webapi.command.stub;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Variant;

import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.net.WWWFormCodec;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.impl.util.random.SecureRandomService;
import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.impl.webapi.MetaWebApi.WebApiRuntime;
import org.iplass.mtp.impl.webapi.MetaWebApiStubContent.WebApiStubContentRuntime;
import org.iplass.mtp.impl.webapi.WebApiService;
import org.iplass.mtp.impl.webapi.rest.RestRequestContext;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.webapi.WebApiRequestConstants;
import org.iplass.mtp.webapi.WebApiRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * WebAPI スタブレスポンスを返却するコマンド
 * <p>
 * WebAPI 設定で {@link org.iplass.mtp.webapi.definition.WebApiDefinition#isReturnStubResponse()} が true の場合に設定されるコマンドです。<br>
 * リクエストの Accepts ヘッダー、{@link org.iplass.mtp.webapi.definition.WebApiDefinition#getResponseType()} によってコンテンツタイプを決定します。<br>
 * 決定されたコンテンツタイプに対応するスタブコンテンツ（ {@link org.iplass.mtp.webapi.definition.WebApiDefinition#getStubContents()} ）から決定します。<br>
 * コンテンツタイプに対応するスタブコンテンツが複数ある場合は、リクエストのクエリー文字列 label に一致するものを、無ければランダムに選択されます。<br>
 * コンテンツタイプに対応するスタブコンテンツが存在しない場合は、{@link org.iplass.mtp.webapi.definition.WebApiDefinition#getStubDefaultContent()} を返却します。<br>
 * この場合、コンテンツタイプと一致するレスポンスが返却されない可能性があるのでご注意ください。<br>
 * （例えば、WebAPIに複数のレスポンスタイプが設定されている状態で、デフォルトコンテンツに JSON 文字列を設定しており、accepts ヘッダーに application/xml を指定している場合）
 * </p>
 * <p>
 * WebAPI 定義でスタブレスポンスを返却する {@link org.iplass.mtp.webapi.definition.WebApiDefinition#isReturnStubResponse()} が true の場合に実行されます。<br>
 * 本クラスでは、ResponseBuilder を利用して、レスポンスを返却します。status の値が必要な場合はスタブコンテンツに入力してください。
 * </p>
 * @author SEKIGUCHI Naoya
 */
@CommandClass(name = StubResponseCommand.NAME, displayName = "Return stub response for Web API.", overwritable = false)
public class StubResponseCommand implements Command {
	/** コマンド名 */
	public static final String NAME = "mtp/webapi/StubResponseCommand";
	/** ロガー */
	private Logger logger = LoggerFactory.getLogger(StubResponseCommand.class);

	@Override
	public String execute(RequestContext request) {
		var isWebApi = (Boolean) request.getAttribute(WebApiRequestConstants.WEB_API);
		if (Boolean.TRUE != isWebApi) {
			// WebAPI 経由の実行ではない場合は SUCCESS を返却し終了。
			logger.warn("This command should be set as a WebAPI command.");
			return "SUCCESS";
		}

		var name = (String) request.getAttribute(WebApiRequestConstants.API_NAME);
		// スタブであることのログを出力する
		logger.debug("Stub command executed. This command is used for testing purposes only. " +
				"Do not use this command in production environment. WebAPI name: {}", name);

		var service = ServiceRegistry.getRegistry().getService(WebApiService.class);
		var runtime = service.getRuntimeByName(name);
		var variant = selectVariant(runtime);

		// クエリ文字列から label を取得
		var label = getQueryString("label");
		// コンテンツタイプに対応するスタブコンテンツリスト
		var stubContentList = runtime.getStubContentList(variant.getMediaType().toString());
		// 返却するコンテンツを決定
		var contentValue = getContent(stubContentList, label, runtime.getMetaData().getStubDefaultContent());

		if (logger.isDebugEnabled()) {
			logger.debug("Stub Response. content-type: {}, content: {}", variant.getMediaType().toString(), contentValue);
		}
		var responseBuilder = Response.status(200).variant(variant).entity(contentValue);
		// スタブ動作の場合は、ResponseResults のキー名が {@link org.iplass.mtp.webapi.WebApiRequestConstants#DEFAULT_RESULT} になる
		// org.iplass.mtp.impl.webapi.MetaWebApi$WebApiRuntime のコンストラクタを参照
		request.setAttribute(WebApiRequestConstants.DEFAULT_RESULT, responseBuilder);

		return "SUCCESS";
	}

	/**
	 * スタブコンテンツを取得する
	 * <p>
	 * スタブコンテンツランタイムリストから、指定されたラベルに一致するスタブコンテンツを取得します。<br>
	 * ラベルに一致するスタブコンテンツが見つからない場合は、ランダムに選択されたスタブコンテンツを返却します。<br>
	 * 一致するスタブコンテンツが存在しない場合は、デフォルトコンテンツを返却します。
	 * </p>
	 * @param stubContentList スタブコンテンツランタイムリスト
	 * @param label ラベル
	 * @param defaultContent 一致するスタブが見つからない場合のデフォルトコンテンツ
	 * @return 選択されたコンテンツ
	 */
	private String getContent(List<WebApiStubContentRuntime> stubContentList, String label, String defaultContent) {
		if (null == stubContentList || stubContentList.isEmpty()) {
			// stubContentList が null または空の場合は、デフォルトのコンテンツを返す
			// WebApi に設定されている Response Type と Accept ヘッダを元にレスポンスの Variant を選択する。
			// デフォルト値が選択された場合、Variant(content-type) と返却値（メタデータの stubDefaultContent）の形式は必ずしも一致しない
			return defaultContent;
		}

		if (1 == stubContentList.size()) {
			// スタブコンテンツが1つしかない場合は、そのスタブコンテンツを返却
			return stubContentList.get(0).getMetaData().getContent();
		}

		if (StringUtil.isNotEmpty(label)) {
			for (WebApiStubContentRuntime stubContent : stubContentList) {
				if (StringUtil.equals(stubContent.getMetaData().getLabel(), label)) {
					logger.debug("Stub content selected. label: {}", label);
					// ラベルに一致する場合、そのスタブコンテンツを返却
					return stubContent.getMetaData().getContent();
				}
			}
		}

		// ラベル設定が無いもしくはラベルに該当するスタブコンテンツが見つからない場合は、ランダム選択した値を返却
		var service = ServiceRegistry.getRegistry().getService(SecureRandomService.class);
		// TODO secureRuntime をリクエスト毎に作成している。。。
		var rnd = service.createGenerator().randomInt(stubContentList.size());
		return stubContentList.get(rnd).getMetaData().getContent();
	}

	/**
	 * リクエストのクエリ文字列から指定されたキーの値を取得する。
	 * @param key クエリ文字列のキー
	 * @return 指定されたキーの値。存在しない場合は null を返す。
	 */
	private String getQueryString(String key) {
		var queryStringMap = convertQueryStringMap(WebRequestStack.getCurrent().getRequest().getQueryString());
		var valueList = queryStringMap.get(key);

		return valueList != null && !valueList.isEmpty() ? valueList.get(0) : null;
	}

	/**
	 * WebAPI のレスポンスの Variant を選択する。
	 * @param runtime WebAPI ランタイム
	 * @return 選択された Variant
	 */
	private Variant selectVariant(WebApiRuntime runtime) {
		List<Variant> variants = runtime.getVariants();
		if (variants.size() == 1) {
			return variants.get(0);
		}

		WebRequestStack stack = WebRequestStack.getCurrent();
		RestRequestContext context = (RestRequestContext) stack.getRequestContext();
		Variant selected = context.rsRequest().selectVariant(variants);
		if (null == selected) {
			throw new WebApiRuntimeException(
					"Response Type cannot determined. Specify correct Accept header:" + stack.getRequest().getHeader("Accept"));
		}
		return selected;
	}

	/**
	 * クエリ文字列を Map 変換する
	 * @param queryString リクエストのクエリ文字列
	 * @return クエリ文字列の Map 変換結果
	 */
	private Map<String, List<String>> convertQueryStringMap(String queryString) {
		if (null == queryString || queryString.isEmpty()) {
			return Collections.emptyMap();
		}

		List<NameValuePair> parsed = WWWFormCodec.parse(queryString, StandardCharsets.UTF_8);

		Map<String, List<String>> result = new HashMap<>();
		for (NameValuePair pair : parsed) {
			List<String> values = result.get(pair.getName());
			if (values == null) {
				values = new ArrayList<>();
				result.put(pair.getName(), values);
			}
			values.add(pair.getValue());
		}

		// 変更不可
		Map<String, List<String>> unmodifyResult = new HashMap<>();
		for (Map.Entry<String, List<String>> entry : result.entrySet()) {
			unmodifyResult.put(entry.getKey(), Collections.unmodifiableList(entry.getValue()));
		}
		return Collections.unmodifiableMap(unmodifyResult);
	}

}
