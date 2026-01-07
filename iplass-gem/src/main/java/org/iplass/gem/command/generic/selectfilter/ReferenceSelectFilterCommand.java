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

package org.iplass.gem.command.generic.selectfilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.iplass.gem.GemConfigService;
import org.iplass.gem.command.Constants;
import org.iplass.gem.command.generic.HasDisplayScriptBindings;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.RestJson;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.entity.query.PreparedQuery;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.SortSpec;
import org.iplass.mtp.entity.query.SortSpec.SortType;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.expr.And;
import org.iplass.mtp.entity.query.condition.expr.Not;
import org.iplass.mtp.entity.query.condition.predicate.In;
import org.iplass.mtp.entity.query.condition.predicate.Like;
import org.iplass.mtp.impl.util.ConvertUtil;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.view.generic.EntityViewManager;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.RefSortType;
import org.iplass.mtp.view.generic.editor.ReferenceSelectFilterSetting;
import org.iplass.mtp.view.generic.editor.ReferenceSelectFilterSetting.SelectFilterMatchPattern;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.RequestType;

/**
 * 参照選択フィルターリスト取得処理
 *
 * @author lish0p
 */
@WebApi(
		name=ReferenceSelectFilterCommand.WEBAPI_NAME,
		accepts=RequestType.REST_JSON,
		methods=MethodType.POST,
		restJson=@RestJson(parameterName="param"),
		results = { ReferenceSelectFilterCommand.RESULT_TOTAL, ReferenceSelectFilterCommand.RESULT_DATA },
		checkXRequestedWithHeader=true
	)
@CommandClass(name = "gem/generic/selectfilter/ReferenceSelectFilterCommand", displayName = "参照選択フィルターリスト取得処理")
public final class ReferenceSelectFilterCommand implements Command, HasDisplayScriptBindings {
	public static final String WEBAPI_NAME = "gem/generic/selectfilter/referenceSelectfilter";

	/** parameter名 */
	public static final String KEYWORD = "keyword";
	public static final String OFFSET = "offset";
	public static final String EXCLUDE_OID = "excludeOid";

	/** 返却値のkeyword名 */
	public static final String RESULT_TOTAL = "count";
	public static final String RESULT_DATA = "data";
    public static final String RESULT_CODE = "code";

	private EntityManager em = null;
	private EntityViewManager evm = null;
	private EntityDefinitionManager edm = null;

	/**
	 * likeのパターン変換
	 * @param p パターン
	 * @return 変換したパターン
	 */
    private static Like.MatchPattern toLikePattern(SelectFilterMatchPattern p) {
        if (p == null) return Like.MatchPattern.PREFIX;
        switch (p) {
            case PREFIX: return Like.MatchPattern.PREFIX;
            case POSTFIX: return Like.MatchPattern.POSTFIX;
            case PARTIAL: return Like.MatchPattern.PARTIAL;
            default: return Like.MatchPattern.PREFIX;
        }
    }

	/**
	 * プロパティ名を EntityDefinition に応じて調整する
	 * @param ed EntityDefinition（null の場合は元名をそのまま返す）
	 * @param propName 元のプロパティ名
	 * @return 調整後のプロパティ名
	 */
    private static String adjustPropertyName(EntityDefinition ed, String propName) {
        if (ed == null || propName == null) {
            return propName;
        }
        PropertyDefinition pd = ed.getProperty(propName);
        if (pd == null) {
            return propName;
        }
        if (pd.getType() == PropertyDefinitionType.REFERENCE) {
            return propName + ".name";
        }
        return propName;
    }

	/**
	 * 参照選択用の空結果データを生成して返す。
	 * @return 空の検索結果データ（一覧と総件数0）
	 */
	private ReferenceSelectFilterData emptyReferenceSelectFilterData() {
		return new ReferenceSelectFilterData(0, new ArrayList<Entity>());
	}

	/**
	 * コンストラクタ
	 */
	public ReferenceSelectFilterCommand() {
		em = ManagerLocator.getInstance().getManager(EntityManager.class);
		evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
		edm = ManagerLocator.getInstance()
				.getManager(EntityDefinitionManager.class);
	}


    /**
     * コマンド実行エントリ。
     * @param request リクエストコンテキスト
     * @return 実行結果コード
     */
	@Override
	public String execute(RequestContext request) {
		//パラメータ取得
		String defName = request.getParam(Constants.DEF_NAME);
		String viewName = request.getParam(Constants.VIEW_NAME);
		String propName = request.getParam(Constants.PROP_NAME);
		String viewType = request.getParam(Constants.VIEW_TYPE);

		if (defName == null || viewName == null || propName == null || viewType == null) {
			return Constants.CMD_EXEC_ERROR;
        }

        // バインディングエンティティを取得
		final Entity entity = getBindingEntity(request);
        final PropertyEditor editor = evm.getPropertyEditor(defName, viewType, viewName, propName, entity);

        // Reference 用のエディタでない場合は処理対象外
        if (!(editor instanceof ReferencePropertyEditor)) {
			return Constants.CMD_EXEC_ERROR;
        }
        final ReferencePropertyEditor rpe = (ReferencePropertyEditor) editor;

		ReferenceSelectFilterData result = buildReferenceSelectFilterData(rpe, request);
		request.setAttribute(RESULT_DATA, result.getOptionValues());
		request.setAttribute(RESULT_TOTAL, result.getTotalCount());
        return Constants.CMD_EXEC_SUCCESS;
    }




	/**
	 * 参照選択用データを構築して返す。
     * @param editor ReferencePropertyEditor
     * @param request リクエストコンテキスト
     * @return 検索結果データ（一覧と総件数）
     */
	private ReferenceSelectFilterData buildReferenceSelectFilterData(ReferencePropertyEditor editor, RequestContext request) {
		if (editor == null) {
			return emptyReferenceSelectFilterData();
        }
		ReferenceSelectFilterSetting setting = editor.getReferenceSelectFilterSetting();
		if (setting == null) {
			return emptyReferenceSelectFilterData();
        }

		String propName = setting.getPropertyName();
        // プロパティ指定がなければ処理不能なので空結果を返す
		if (propName == null || propName.trim().isEmpty()) {
			return emptyReferenceSelectFilterData();
        }

		// 表示ラベル項目
		String labelItem = (editor.getDisplayLabelItem() != null) ? editor.getDisplayLabelItem() : Entity.NAME;

		// エンティティ定義に応じてプロパティ名を調整
		EntityDefinition ed = edm.get(editor.getObjectName());
		propName = adjustPropertyName(ed, propName);
		return executeKeywordSearch(editor, setting, propName, labelItem, request);
    }

	/**
     * キーワード検索を実行して結果を返す。
     * @param editor 参照プロパティエディター
     * @param setting 参照選択フィルタ設定
     * @param propName 抽出するプロパティ名
     * @param labelItem 表示ラベル用プロパティ名
     * @param request リクエストコンテキスト
     * @return 検索結果データ
     */
	private ReferenceSelectFilterData executeKeywordSearch(ReferencePropertyEditor editor, ReferenceSelectFilterSetting setting, String propName,
			String labelItem, RequestContext request) {
		List<Condition> conditions = new ArrayList<Condition>();

		final List<Entity> optionValues = new ArrayList<>();

		// 設定から検索件数のデフォルト値を取得
		int searchCount = ServiceRegistry.getRegistry()
				.getService(GemConfigService.class)
				.getSelectFilterSearchPageSizeDefault();

 		final String keywordRaw = request.getParam(KEYWORD);
		final String excludeOid = request.getParam(EXCLUDE_OID);

        // offset の正規化（null/負値を 0 に）
		final Integer offset = request.getParam(OFFSET, Integer.class);
		final int safeOffset = (offset != null && offset > 0) ? offset : 0;

        // limit の決定（setting が 0 => グローバル設定を使用、負数は無制限）
		int limit = setting.getSelectFilterSearchPageSize() == 0 ? searchCount : setting.getSelectFilterSearchPageSize();
		final Like.MatchPattern likePattern = toLikePattern(setting.getSelectFilterSearchPattern());

		// クエリ構築
		final Query q = new Query();
		q.select(Entity.OID, propName, labelItem);
		q.from(editor.getObjectName());

		if (keywordRaw != null && !keywordRaw.trim()
				.isEmpty()) {
			final String keyword = keywordRaw.trim();
			conditions.add(new Like(propName, keyword, likePattern));
		}
		if (excludeOid != null && !excludeOid.trim()
				.isEmpty()) {
			Object[] oidArr = Arrays.stream(excludeOid.split(","))
					.map(String::trim)
					.filter(s -> !s.isEmpty())
					.distinct()
					.toArray();
			if (oidArr.length > 0) {
				conditions.add(new Not(new In(Entity.OID, oidArr)));
			}
		}

		if (setting.getCondition() != null && !setting.getCondition().isEmpty()) {
			conditions.add(new PreparedQuery(setting.getCondition()).condition(null));
		}
		if (!conditions.isEmpty()) {
			final And where = new And(conditions);
			q.where(where);
		}

		if (limit > 0) {
			q.limit(limit, safeOffset);
		}

		String sortItem = setting.getSortItem();
		if (sortItem == null || sortItem.isEmpty()) {
			sortItem = Entity.OID;
		}
		SortType sortType = null;
		if (setting.getSortType() == null || setting.getSortType() == RefSortType.ASC) {
			sortType = SortType.ASC;
		} else {
			sortType = SortType.DESC;
		}
		q.order(new SortSpec(sortItem, sortType));

        // 検索実行
		em.searchEntity(q, (entity) -> {
			entity.setName(entity.getValue(labelItem));
			entity.setValue(RESULT_CODE, ConvertUtil.convertToString(entity.getValue(propName)));
			optionValues.add(entity);
			return true;
		});

		// 総件数を取得する
		final Query qCount = new Query();
		qCount.select(Entity.OID);
		qCount.from(editor.getObjectName());
		if (!conditions.isEmpty()) {
			final And where = new And(conditions);
			qCount.where(where);
		}
		int total = em.count(qCount);
		return new ReferenceSelectFilterData(total, optionValues);
	}
}
