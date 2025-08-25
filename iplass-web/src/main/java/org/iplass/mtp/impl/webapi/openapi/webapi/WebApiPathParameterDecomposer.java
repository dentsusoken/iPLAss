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
package org.iplass.mtp.impl.webapi.openapi.webapi;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.WebApiDefinition;
import org.iplass.mtp.webapi.definition.WebApiParamMapDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.PathParameter;

/**
 * WebAPIパスパラメーター分解クラス
 * <p>
 * WebAPI定義からOpenAPIへ変換する際に、パラメーターの設定によって１つのWebAPI定義から複数のOpenAPIパスがマッピングされることがある。
 * このようなケースを想定し、WebAPI定義を先に解析して、OpenAPIに登録する単位のパスに変換しておく。
 * </p>
 * <h3>分解方針</h3>
 * <ul>
 * <li>OpenAPIのパスを調査し、WebAPIのパスを決定します。</li>
 * <li>WebAPIパスでMetaDataを検索し、すでに存在する場合は、PathItem 全体をメタデータにマッピングします。</li>
 * <li>WebAPIパスでMetaDataが存在しない場合は、PathItemのメソッドごとにWebAPIの定義を作成します。WebAPIパスは末尾にメソッド名を付与します。</li>
 * <li>PathItemに対して１つのメソッドしか存在せず、既存のメソッドが存在しない場合は、末尾のメソッドを除去します。</li>
 * </ul>
 * @author SEKIGUCHI Naoya
 */
public class WebApiPathParameterDecomposer {
	/** MapFromパラメーターパターン（旧パターン） */
	private static final Pattern MAP_FROM_LEGACY_PATTERN = Pattern.compile("^\\{([0-9]+)\\}");
	/** MapFromパラメーターパターン */
	private static final Pattern MAP_FROM_PATTERN = Pattern.compile("^\\$\\{([0-9]+)\\}");
	/** メソッドパスパターン */
	private static final Pattern METHOD_PATH_PATTERN = createMethodPathPattern();

	/** ロガー */
	private Logger logger = LoggerFactory.getLogger(WebApiPathParameterDecomposer.class);

	/**
	 * WebAPI定義のパラメーターを解析しOpenAPIパスを解決します。
	 * @param webApiDef WebAPI定義
	 * @param openApi OpenAPIオブジェクト
	 * @return 解決されたOpenAPIパスのリスト
	 */
	public List<String> decompose(WebApiDefinition webApiDef, OpenAPI openApi) {
		var accessor = parseParameter(webApiDef);

		if (0 == accessor.getParamCount()) {
			// パラメーターマップが存在しない場合は、デフォルトのパスを設定
			var defaultPath = getOpenApiPath(webApiDef);
			openApi.path(defaultPath, new PathItem());
			return List.of(defaultPath);
		}

		var resolvePathList = resolvePath(accessor, webApiDef, openApi);

		logger.debug("WebAPI Definition [{}] resolved path list: {}", webApiDef.getName(), resolvePathList);
		return resolvePathList;
	}

	/**
	 * WebAPI定義のパラメーターを解析して、ParameterManager を取得します。
	 * @param webApiDef WebAPI定義
	 * @return ParseParameterManager インスタンス
	 */
	private ParseParameterAccessor parseParameter(WebApiDefinition webApiDef) {
		var removeMethodNameDefinitionName = removeMethodName(webApiDef.getName());
		var splittedNameLength = removeMethodNameDefinitionName.split("/").length;
		var accessor = new ParseParameterAccessor();
		if (null != webApiDef.getWebApiParamMap()) {
			for (var param : webApiDef.getWebApiParamMap()) {
				var mapFrom = param.getMapFrom();
				var isLegacy = mapFrom.startsWith("{");

				var mapFromMatcher = isLegacy ? MAP_FROM_LEGACY_PATTERN.matcher(mapFrom) : MAP_FROM_PATTERN.matcher(mapFrom);

				// NOTE mapFromMatcher.find() が false の場合は、数値以外のパターン（${paths} など）なので無視する。
				if (mapFromMatcher.find()) {
					var num = Integer.parseInt(mapFromMatcher.group(1));
					// パラメータの位置を決定する
					// name = path/to/name の前提で、
					// 通常の場合は num をそのまま利用
					// mapFrom = ${0} の場合、 num を 0 として利用
					// legacy の場合は num の位置を調整。WebAPI定義名を "/" で分割した要素数を引く。
					// mapFrom = {4} の場合、4(mapFrom の値) - 3(WebAPI定義名を "/" で分割した要素数) で 1 になる。
					num = isLegacy ? num - splittedNameLength : num;

					accessor.add(new ParseParameter(num, param));
				}
			}
		}

		return accessor;
	}

	/**
	 * パラメーター解析後に、OpenAPIパスを解決します。
	 * @param accessor パラメーター解析マネージャー
	 * @param webApiDef WebAPI定義
	 * @param openApi OpenAPIオブジェクト
	 * @return 解決されたOpenAPIパスのリスト
	 */
	private List<String> resolvePath(ParseParameterAccessor accessor, WebApiDefinition webApiDef, OpenAPI openApi) {
		var resultPath = new ArrayList<String>();
		var defaultPath = getOpenApiPath(webApiDef);
		// パラメータ解析完了後
		for (var conditionPattern : accessor.getConditionList()) {
			// 条件パターン数がパスパターンとなる
			var conditionParams = accessor.getParamListByCondition(conditionPattern);

			var path = new StringBuilder(defaultPath);
			var pathItem = new PathItem();
			for (var param : conditionParams) {
				path.append("/{").append(param.name).append("}");
				pathItem.addParametersItem(param.parameter);
			}

			var resolvedPath = path.toString();
			// 返却パスを追加
			resultPath.add(resolvedPath);

			if (openApi.getPaths() == null || !openApi.getPaths().containsKey(resolvedPath)) {
				// パスがすでに存在しない場合に PathItem を追加する
				// パスが存在するケースとして、WebAPI の末尾にメソッド名が付与されているパターン（例： scim/v2/DELETE, scim/v2/POST など）がある。
				openApi.path(resolvedPath, pathItem);
			}
		}

		return resultPath;
	}

	private String getOpenApiPath(WebApiDefinition def) {
		return "/" + removeMethodName(def.getName());
	}

	/**
	 * WebAPI定義から末尾のメソッド名を除去します。
	 * <p>
	 * 末尾にHTTPメソッド名が付与されている場合は、HTTPメソッド名を除去し返却します。
	 * </p>
	 * @param def WebAPI定義
	 * @return OpenAPIパス
	 */
	private String removeMethodName(String definitionName) {
		var matcher = METHOD_PATH_PATTERN.matcher(definitionName);
		if (matcher.find()) {
			// 末尾がメソッド名の場合は、メソッド名を除外してパスを取得する
			return matcher.group(1);
		}
		// その他の場合は定義名をそのまま利用する
		return definitionName;
	}

	private static Pattern createMethodPathPattern() {
		// GET|POST|... を作成する
		String methodPattern = String.join("|", Stream.of(MethodType.values()).map(m -> m.name()).toArray(String[]::new));
		return Pattern.compile("^(.*)/(" + methodPattern + ")$");
	}

	/**
	 * パラメーター解析結果を保持し意味のあるデーターとしてアクセスするクラス
	 */
	private static class ParseParameterAccessor {
		/** パラメーター解析結果クラスリスト */
		private List<ParseParameter> paramList = new ArrayList<>();
		/** パラメーターのパス位置の最大 */
		private int maxIdx = -1;

		/**
		 * パラメータ数を取得する
		 * @return パラメータ数
		 */
		public int getParamCount() {
			return paramList.size();
		}

		/**
		 * パラメーター解析結果を追加します。
		 * @param param パラメーター解析結果
		 */
		public void add(ParseParameter param) {
			paramList.add(param);
			maxIdx = Math.max(maxIdx, param.idx);
		}

		/**
		 * パラメーター解析結果の一意な条件リストを取得します。
		 * @return 条件リスト
		 */
		public List<String> getConditionList() {
			return paramList.stream().map(p -> p.condition)
					.distinct()
					.toList();
		}

		/**
		 * 指定された条件に一致するパラメーター解析結果を取得します。
		 * @param condition 条件文字列
		 * @return パラメーター解析結果リスト
		 */
		public List<ParseParameter> getParamListByCondition(String condition) {
			List<ParseParameter> paramList = new ArrayList<>();

			for (int i = 0; i <= maxIdx; i++) {
				var param = getParamByConditionIdx(condition, i);

				if (param == null) {
					// パラメーターが存在しない場合は、以降をスキップ
					break;
				}

				paramList.add(param);
			}

			return paramList;
		}

		/**
		 * 指定された条件とパス位置に一致するパラメーター解析結果を取得します。
		 * <ul>
		 * <li>条件に一致するパラメーターのみ存在 ⇒ 条件に一致するパラメーターを返却</li>
		 * <li>条件が存在しないパラメーターのみ存在 ⇒ 条件が存在しないパラメーターを返却</li>
		 * <li>条件に一致するパラメーター、条件が存在しないパラメーター両方存在 ⇒ 条件に一致するパラメーターを返却</li>
		 * </ul>
		 * @param condition 条件
		 * @param idx パス位置
		 * @return パラメーター解析結果
		 */
		private ParseParameter getParamByConditionIdx(String condition, int idx) {
			ParseParameter noConditionParam = null;
			ParseParameter matchConditionParam = null;

			for (ParseParameter p : paramList) {
				if (p.idx == idx && p.condition != null && p.condition.equals(condition)) {
					matchConditionParam = p;
				}
				if (p.idx == idx && p.condition == null) {
					noConditionParam = p;
				}
			}

			return matchConditionParam != null ? matchConditionParam : noConditionParam;
		}
	}

	/**
	 * パラメーター解析結果クラス
	 */
	private static class ParseParameter {
		/** パラメーターのパス位置 */
		private int idx;
		/** パラメーター名 */
		private String name;
		/** パラメーター条件 */
		private String condition;

		/** OpenAPI 用パラメーターインスタンス */
		private Parameter parameter;

		/**
		 * コンストラクタ
		 * @param idx パラメーターのパス位置
		 * @param def WebAPIパラメーター定義
		 */
		public ParseParameter(int idx, WebApiParamMapDefinition def) {
			this.idx = idx;
			this.name = def.getName();
			this.condition = def.getCondition();

			this.parameter = new PathParameter().name(def.getName()).schema(new StringSchema());
		}
	}
}
