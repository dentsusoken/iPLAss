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

package org.iplass.gem.command.generic.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.iplass.gem.command.Constants;
import org.iplass.gem.command.GemResourceBundleUtil;
import org.iplass.gem.command.generic.search.condition.PropertySearchCondition;
import org.iplass.gem.command.generic.search.condition.ReferencePropertySearchCondition;
import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.LongTextProperty;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.entity.query.Where;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.expr.And;
import org.iplass.mtp.entity.query.condition.expr.Not;
import org.iplass.mtp.entity.query.condition.expr.Or;
import org.iplass.mtp.impl.entity.property.PropertyService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.filter.expression.FilterExpressionParser;
import org.iplass.mtp.view.filter.expression.FilterExpressionParser.FilterItemHandler;
import org.iplass.mtp.view.filter.expression.FilterValueExpressionChecker;
import org.iplass.mtp.view.filter.expression.UnsupportedFilterOperationException;
import org.iplass.mtp.view.generic.EntityViewUtil;
import org.iplass.mtp.view.generic.OutputType;
import org.iplass.mtp.view.generic.editor.NestProperty;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.ReferenceDisplayType;
import org.iplass.mtp.view.generic.element.property.PropertyItem;
import org.iplass.mtp.web.template.TemplateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DetailSearchContext extends SearchContextBase {

	private static Logger logger = LoggerFactory.getLogger(DetailSearchContext.class);

	private List<SearchConditionDetail> details;

	private Map<Integer, PropertySearchCondition> searchConditions;

	@Override
	public Where getWhere() {
		try {
			initParam();
		} catch (ApplicationException e) {
			//パラメータの作成でこける→定義がないのでException
			return null;
		}

		And a = new And();
		Condition defaultCondition = getDefaultCondition();
		if (defaultCondition != null) {
			a.addExpression(defaultCondition);
		}
		List<Condition> conditions = getConditions();

		String expr = getDetailConditionExpr();
		if (Constants.EXPRESSION.equals(expr)) {
			a.addExpression(getFilterExpressionCondition());
		} else if (Constants.AND.equals(expr)) {
			if (!conditions.isEmpty()) a.addExpression(new And(conditions));
		} else if (Constants.OR.equals(expr)) {
			if (!conditions.isEmpty()) a.addExpression(new Or(conditions));
		} else if (Constants.NOT.equals(expr)) {
			if (!conditions.isEmpty()) {
				And and = new And();
				for (Condition con : conditions) {
					Not not = new Not(con);
					and.addExpression(not);
				}
				a.addExpression(and);
			}
		}

		Where w = new Where();
		if (a.getChildExpressions() != null && !a.getChildExpressions().isEmpty()) {
			w.setCondition(a);
		}

		return w;
	}

	@Override
	public boolean checkParameter() {
		boolean isValid = true;
		try {
			initParam();
		} catch (ApplicationException e) {
			//パラメータの作成でこける→定義がないのでException
			if (logger.isDebugEnabled()) {
				logger.debug(e.getMessage(), e);
			}
			isValid = false;
		}

		if (isValid) {
			for (int i = 0; i < details.size(); i++) {
				SearchConditionDetail detail = details.get(i);
				if (detail != null) {
					PropertyItem property = null;
					if (detail.getPropertyName().indexOf(".") == -1) {
						property = getLayoutProperty(detail.getPropertyName());
					} else {
						property = getLayoutProperty(detail.getPropertyName());//直接参照のプロパティをD&Dかも
						if (property == null) {//取れなければネスト項目かも
							String[] tmp = detail.getPropertyName().split("\\.");
							property = getLayoutProperty(tmp[0]);
						}
					}

					if (property != null) {
						//非表示設定されてるか、詳細検索条件にない項目であればエラー
						if (!EntityViewUtil.isDisplayElement(getDefName(), property.getElementRuntimeId(), OutputType.SEARCHCONDITION)
								|| property.isHideDetailCondition()) {
							isValid = false;
						}
					} else {
						//画面定義に設定されてなければエラー
						isValid = false;
					}
					if (!isValid) {
						break;
					}

					//型毎の固有チェックとか
					PropertySearchCondition condition = searchConditions.get(i);
					if (condition != null && !condition.checkDetailParameter(property)) {
						isValid = false;
						break;
					}
				}
			}
		}


		if (isValid) {
			try {
				//条件式のチェック
				validateFilterExpression();
			} catch (UnsupportedFilterOperationException e) {
				if (logger.isDebugEnabled()) {
					logger.debug(e.getMessage(), e);
				}
				getRequest().setAttribute(Constants.MESSAGE, e.getMessage());
				isValid = false;
			}
		} else {
			getRequest().setAttribute(Constants.MESSAGE, resourceString("command.generic.search.SearchCommandBase.searchCondErr"));
		}

		return isValid;
	}

	@Override
	public boolean validation() {
		//必須チェック
		HashMap<String, String> propMap = getRequiredPropertyMap();
		for (Entry<String, String> entry : propMap.entrySet()) {
			SearchConditionDetail detail = null;
			for (SearchConditionDetail _detail : details) {
				if (_detail != null && entry.getKey().equals(_detail.getPropertyName())) {
					detail = _detail;
					break;
				}
			}

			//条件がなければエラー
			if (detail == null) {
				getRequest().setAttribute(Constants.MESSAGE, resourceString("command.generic.search.DetailSearchContext.pleaseInput", entry.getValue()));
				return false;
			}
		}
		return true;
	}

	/**
	 * 必須項目のマップを取得
	 * @return
	 */
	private HashMap<String, String> getRequiredPropertyMap() {
		HashMap<String, String> map = new HashMap<>();
		List<PropertyItem> properties = getLayoutProperties();
		for (PropertyItem property : properties) {
			//ブランクor非表示なので対象外
			if (property.isBlank()
					|| !EntityViewUtil.isDisplayElement(getDefName(), property.getElementRuntimeId(), OutputType.SEARCHCONDITION)
					|| property.isHideDetailCondition()) {
				continue;
			}

			String propName = property.getPropertyName();
			PropertyDefinition pd = getPropertyDefinition(propName);

			//非表示なので対象外
			if (!isDisplayProperty(pd)) continue;

			if (pd instanceof ReferenceProperty && property.getEditor() instanceof ReferencePropertyEditor) {
				//参照の場合はネストのチェック
				String defName = ((ReferenceProperty) pd).getObjectDefinitionName();
				String displayLabel = TemplateUtil.getMultilingualString(property.getDisplayLabel(), property.getLocalizedDisplayLabelList(),
						pd.getDisplayName(), pd.getLocalizedDisplayNameList());
				getRequiredNestPropertyMap(map, propName, displayLabel, defName, (ReferencePropertyEditor) property.getEditor(), property.isRequiredDetail());
			} else {
				//その他は必須ならマップに保持
				if (property.isRequiredDetail()) {
					String displayLabel = TemplateUtil.getMultilingualString(property.getDisplayLabel(), property.getLocalizedDisplayLabelList(),
							pd.getDisplayName(), pd.getLocalizedDisplayNameList());
					map.put(propName, displayLabel);
				}
			}
		}
		return map;
	}

	/**
	 * 必須項目のネストのマップを取得
	 * @param map
	 * @param propName
	 * @param displayLabel
	 * @param defName
	 * @param editor
	 * @param required
	 */
	private void getRequiredNestPropertyMap(HashMap<String, String> map, String propName, String displayLabel, String defName, ReferencePropertyEditor editor, boolean required) {

		boolean showNest = false;
		if (editor.getDisplayType() == ReferenceDisplayType.SELECT
				|| editor.getDisplayType() == ReferenceDisplayType.CHECKBOX
				|| editor.getDisplayType() == ReferenceDisplayType.REFCOMBO
				|| (editor.getDisplayType() == ReferenceDisplayType.LINK && editor.isUseSearchDialog())) {
			if (required) map.put(propName, displayLabel);
		} else {
			if (editor.getNestProperties().isEmpty()) {
				//ネストがなければ名前
				if (required) map.put(propName, displayLabel);
			} else {
				showNest = true;
				if (editor.isUseNestConditionWithProperty() && required) {
					//ネストと同時に名前も
					map.put(propName, displayLabel);
				}
			}
		}

		if (showNest || editor.isUseNestConditionWithProperty()) {
			EntityDefinition ed = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class).get(defName);

			for (NestProperty np : editor.getNestProperties()) {
				String npName = propName + "." + np.getPropertyName();
				PropertyDefinition pd = ed.getProperty(np.getPropertyName());
				if (pd instanceof ReferenceProperty && np.getEditor() instanceof ReferencePropertyEditor) {
					//参照の場合はネストのチェック
					String refDefName = ((ReferenceProperty) pd).getObjectDefinitionName();
					String refDisplayLabel = TemplateUtil.getMultilingualString(np.getDisplayLabel(), np.getLocalizedDisplayLabelList(),
							pd.getDisplayName(), pd.getLocalizedDisplayNameList());
					getRequiredNestPropertyMap(map, npName, refDisplayLabel, refDefName, (ReferencePropertyEditor) np.getEditor(), np.isRequiredDetail());
				} else {
					//その他は必須ならマップに保持
					if (np.isRequiredDetail()) {
						String refDisplayLabel = TemplateUtil.getMultilingualString(np.getDisplayLabel(), np.getLocalizedDisplayLabelList(),
								pd.getDisplayName(), pd.getLocalizedDisplayNameList());
						map.put(npName, refDisplayLabel);
					}
				}
			}
		}
	}

	/**
	 * 画面に表示されてるプロパティか確認
	 * @param property
	 * @return
	 */
	private boolean isDisplayProperty(PropertyDefinition property) {
		if (property instanceof LongTextProperty) {
			//ロングテキストを表示するかは設定次第
			PropertyService service = ServiceRegistry.getRegistry().getService(PropertyService.class);
			return service.isRemainInlineText();
		}
		return true;
	}

	@Override
	public boolean isVersioned() {
		String allVer = getRequest().getParam("allVersionDtl");
		return "1".equals(allVer);
	}

	private void initParam() {
		//リクエストからパラメータ取得
		getSearchConditionDetails();

		//パラメータを変換
		convertCondition();
	}

	private List<Condition> getConditions() {
		List<Condition> ret = new ArrayList<Condition>();
		for (PropertySearchCondition condition : searchConditions.values()) {
			if (condition != null) {
				List<Condition> _cond = condition.convertDetailCondition();//複数になることはないはず
				if (!_cond.isEmpty()) ret.add(_cond.get(0));//addAllでもいいけど条件式の場合の順序も考えて
			}
		}
		return ret;
	}

	private Condition getFilterExpressionCondition() {
		String filterExpression = getFilterExpression();

		FilterExpressionParser parser = new FilterExpressionParser(filterExpression, new FilterItemHandler() {

			@Override
			public boolean isIndexOutOfBounds(int index) {
				return index >= searchConditions.size();
			}

			@Override
			public Condition getCondition(int index) {
				PropertySearchCondition cond = searchConditions.get(index);
				Condition c = null;
				if (cond != null) {
					List<Condition> _cond = cond.convertDetailCondition();
					if (!_cond.isEmpty()) c = _cond.get(0);
				}
				return c;
			}
		});
		return parser.parse();
	}

	/**
	 * 詳細検索の条件を取得します。
	 */
	private void getSearchConditionDetails() {
		if (details == null) {
			details = new ArrayList<SearchConditionDetail>();
			int count = getCondtionCount();
			for (int i = 0; i < count; i++) {
				SearchConditionDetail detail = getDetailCondition(i);
				details.add(detail);
			}
		}
	}

	/**
	 * リクエストから詳細検索の条件数を取得します。
	 * @return 詳細検索の条件数
	 */
	private int getCondtionCount() {
		Integer count = getRequest().getParamAsInt(Constants.DETAIL_COND_COUNT);
		return count == null ? 0 : count;
	}

	/**
	 * リクエストから詳細検索の条件を取得します。
	 * @param index 詳細検索の条件の位置
	 * @return 詳細検索の条件
	 */
	private SearchConditionDetail getDetailCondition(int index) {
		String propName = getRequest().getParam(Constants.DETAIL_COND_PROP_NM + index);
		if (StringUtil.isBlank(propName)) return null;

		String predicate = getRequest().getParam(Constants.DETAIL_COND_PREDICATE + index);
		if (StringUtil.isBlank(predicate)) return null;

		String value = getRequest().getParam(Constants.DETAIL_COND_VALUE + index);
		if (StringUtil.isBlank(value)) {
			if (!Constants.NOTNULL.equals(predicate) && !Constants.NULL.equals(predicate)) return null;
		}

		SearchConditionDetail detail = new SearchConditionDetail();
		detail.setPropertyName(propName);
		detail.setPredicate(predicate);
		detail.setValue(value);

		return detail;
	}

	/**
	 * 条件を変換します。
	 */
	private void convertCondition() {
		if (searchConditions == null) {
			searchConditions = new HashMap<Integer, PropertySearchCondition>();
			for (int i = 0; i < details.size(); i++) {
				SearchConditionDetail detail = details.get(i);
				if (detail == null) {
					searchConditions.put(i, null);
				} else {
					searchConditions.put(i, detail2Condition(detail));
				}
			}
		}
	}

	/**
	 * SearchDetailConditionをPropertySearchConditionに変換します。
	 * @param detail 詳細検索条件
	 * @return PropertySearchCondition
	 */
	private PropertySearchCondition detail2Condition(SearchConditionDetail detail) {
		PropertySearchCondition condition = null;
		int firstDotIndex = detail.getPropertyName().indexOf('.');
		if (firstDotIndex == -1) {
			PropertyDefinition pd = getPropertyDefinition(detail.getPropertyName());
			if (pd == null) throw new ApplicationException();//定義が取れない場合不正な項目扱い

			PropertyItem property = getLayoutProperty(pd.getName());
			if (property == null) throw new ApplicationException();//定義が取れない場合不正な項目扱い

			condition = PropertySearchCondition.newInstance(pd, property.getEditor(), detail);
		} else {
			PropertyItem property = getLayoutProperty(detail.getPropertyName());
			if (property != null) {
				//ツリーから直接D&Dした参照のプロパティ
				PropertyDefinition pd = EntityViewUtil.getPropertyDefinition(detail.getPropertyName(), getEntityDefinition());
				if (pd == null) throw new ApplicationException();//定義が取れない場合不正な項目扱い

				condition = PropertySearchCondition.newInstance(pd, property.getEditor(), detail);
			} else {
				//ネストプロパティ
				String parent = detail.getPropertyName().substring(0, firstDotIndex);

				PropertyDefinition pd = getEntityDefinition().getProperty(parent);
				if (pd == null) throw new ApplicationException();//定義が取れない場合不正な項目扱い

				property = getLayoutProperty(parent);
				if (property == null) throw new ApplicationException();//定義が取れない場合不正な項目扱い

				if (pd instanceof ReferenceProperty && property.getEditor() instanceof ReferencePropertyEditor) {
					ReferencePropertyEditor editor = (ReferencePropertyEditor) property.getEditor();
					PropertyDefinition _pd = getPropertyDefinition(detail.getPropertyName());
					if (_pd != null) {
						ReferencePropertySearchCondition _condition = (ReferencePropertySearchCondition) PropertySearchCondition.newInstance(pd, editor, detail);
						_condition.setNestProperty(_pd);
						condition = _condition;
					} else {
						throw new ApplicationException();//定義が取れない場合不正な項目扱い
					}
				}
			}
		}
		return condition;
	}

	/**
	 * 条件式を評価します。
	 */
	private void validateFilterExpression() {
		String expr = getDetailConditionExpr();
		if (StringUtil.isEmpty(expr) || !Constants.EXPRESSION.equals(expr)) {
			return;
		}

		String filterExpression = getFilterExpression();

		//条件式が未指定はエラー
		if (StringUtil.isEmpty(filterExpression)) {
			throw new UnsupportedFilterOperationException(resourceString("command.generic.search.DetailSearchContext.requiredExpressionErr"));
		}

		//サポートしている条件のみ利用しているかをチェック
		FilterValueExpressionChecker checker = new FilterValueExpressionChecker();
		checker.execute(filterExpression);
	}

	/**
	 * リクエストから詳細検索の述語を取得します。
	 * @return 詳細検索の述語
	 */
	private String getDetailConditionExpr() {
		return getRequest().getParam(Constants.DETAIL_COND_EXPR);
	}

	/**
	 * リクエストから条件式を取得します。
	 * @return 条件式
	 */
	private String getFilterExpression() {
		return getRequest().getParam(Constants.DETAIL_COND_FILTER_EXPRESSION);
	}

	private static String resourceString(String key, Object... arguments) {
		return GemResourceBundleUtil.resourceString(key, arguments);
	}
}
