/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.iplass.gem.command.Constants;
import org.iplass.gem.command.GemResourceBundleUtil;
import org.iplass.gem.command.generic.search.condition.PropertySearchCondition;
import org.iplass.gem.command.generic.search.condition.ReferencePropertySearchCondition;
import org.iplass.gem.command.generic.search.condition.ReferencePropertySearchCondition.RefComboCondition;
import org.iplass.gem.command.generic.search.condition.ReferencePropertySearchCondition.ReferenceNormalConditionValue;
import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.GenericEntity;
import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.entity.definition.properties.BooleanProperty;
import org.iplass.mtp.entity.definition.properties.DateProperty;
import org.iplass.mtp.entity.definition.properties.DateTimeProperty;
import org.iplass.mtp.entity.definition.properties.DecimalProperty;
import org.iplass.mtp.entity.definition.properties.ExpressionProperty;
import org.iplass.mtp.entity.definition.properties.FloatProperty;
import org.iplass.mtp.entity.definition.properties.IntegerProperty;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.entity.definition.properties.SelectProperty;
import org.iplass.mtp.entity.definition.properties.TimeProperty;
import org.iplass.mtp.entity.query.Where;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.expr.And;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.EntityViewUtil;
import org.iplass.mtp.view.generic.OutputType;
import org.iplass.mtp.view.generic.editor.NestProperty;
import org.iplass.mtp.view.generic.editor.RangePropertyEditor;
import org.iplass.mtp.view.generic.editor.ReferenceComboSetting;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.RefComboSearchType;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.ReferenceDisplayType;
import org.iplass.mtp.view.generic.editor.SelectPropertyEditor.SelectDisplayType;
import org.iplass.mtp.view.generic.element.property.PropertyItem;
import org.iplass.mtp.web.template.TemplateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NormalSearchContext extends SearchContextBase {

	private static Logger logger = LoggerFactory.getLogger(NormalSearchContext.class);

	private NormalConditionValue condition;
	private Map<String, PropertySearchCondition> propertyConditions;
	private Map<String, PropertySearchCondition> nestPropertyConditions;

	@Override
	public Where getWhere() {
		Where w = new Where();
		ArrayList<Condition> conditions = new ArrayList<>();
		getPropertyConditions();
		if (propertyConditions != null && !propertyConditions.isEmpty()) {
			for (PropertySearchCondition condition : propertyConditions.values()) {
				List<Condition> list = condition.convertNormalCondition();
				if (list != null && !list.isEmpty()) conditions.addAll(list);
			}
		}
		if (nestPropertyConditions != null && !nestPropertyConditions.isEmpty()) {
			for (PropertySearchCondition condition : nestPropertyConditions.values()) {
				List<Condition> list = condition.convertNormalCondition();
				if (list != null && !list.isEmpty()) conditions.addAll(list);
			}
		}

		Condition defaultCondition = getDefaultCondition();
		if (defaultCondition != null) conditions.add(defaultCondition);

		if (conditions.size() > 0) {
			w.setCondition(new And(conditions));
		}
		return w;
	}

	@Override
	public boolean checkParameter() {
		boolean isValid = true;
		try {
			if (propertyConditions == null) getPropertyConditions();
		} catch (ApplicationException e) {
			//パラメータの作成でこける→定義がないのでException
			isValid = false;
		}

		if (isValid) {
			for (PropertyDefinition pd : getPropertyList()) {
				PropertyItem property = getLayoutPropertyForCheck(pd.getName());
				PropertySearchCondition propertyCondition = null;
				if (property != null) {
					//非表示設定されてるか、通常検索条件にない項目で値があればエラー
					if (!EntityViewUtil.isDisplayElement(getDefName(), property.getElementRuntimeId(), OutputType.SEARCHCONDITION, null)
							|| property.isHideNormalCondition()) {
						Object value = condition.getValue(property.getPropertyName());
						if (value != null) {
							if (value.getClass().isArray()) {
								if (((Object[])value).length != 0) {
									isValid = false;
								}
							} else {
								isValid = false;
							}
							if (!isValid) {
								break;
							}
						}
					}
					propertyCondition = propertyConditions.get(property.getPropertyName());
				} else {
					//画面定義に設定されてないのに値があればエラー
					Object value = condition.getValue(pd.getName());
					if (value != null) {
						if (value.getClass().isArray()) {
							if (((Object[])value).length != 0) {
								isValid = false;
							}
						} else {
							isValid = false;
						}
						if (!isValid) {
							break;
						}
					}
					propertyCondition = propertyConditions.get(pd.getName());
				}

				//各条件に対して型別のチェック等
				if (propertyCondition != null) {
					if (!propertyCondition.checkNormalParameter(property)) {
						isValid = false;
						break;
					}
				}
			}
		}

		//ツリーから直接D&Dした参照のプロパティ項目、PropertyDefinitionからでは拾えない条件
		if (isValid) {
			List<PropertyItem> properties = getLayoutProperties();
			for (PropertyItem property : properties) {
				//直接拾えるものは上で処理済みなのでスルー
				if (property.isBlank() || getEntityDefinition().getProperty(property.getPropertyName()) != null) continue;

				//非表示設定されてるか、通常検索条件にない項目で値があればエラー
				if (!EntityViewUtil.isDisplayElement(getDefName(), property.getElementRuntimeId(), OutputType.SEARCHCONDITION, null)
						|| property.isHideNormalCondition()) {
					Object value = condition.getValue(property.getPropertyName());
					if (value != null) {
						if (value.getClass().isArray()) {
							if (((Object[])value).length != 0) {
								isValid = false;
							}
						} else {
							isValid = false;
						}
						if (!isValid) {
							break;
						}
					}
				}

				//各条件に対して型別のチェック等
				PropertySearchCondition propertyCondition = nestPropertyConditions.get(property.getPropertyName());
				if (propertyCondition != null) {
					if (!propertyCondition.checkNormalParameter(property)) {
						isValid = false;
						break;
					}
				}
			}
		}
		if (!isValid) {
			getRequest().setAttribute(Constants.MESSAGE, resourceString("command.generic.search.SearchCommandBase.searchCondErr"));
		}

		return isValid;
	}

	@Override
	public boolean validation() {
		//必須チェック
		List<PropertyItem> properties = getLayoutProperties();
		for (PropertyItem property : properties) {
			if (!property.isBlank()
					&& EntityViewUtil.isDisplayElement(getDefName(), property.getElementRuntimeId(), OutputType.SEARCHCONDITION, null)
					&& !property.isHideNormalCondition()) {
				PropertyDefinition pd = getPropertyDefinition(property.getPropertyName());

				PropertySearchCondition propertyCondition = null;
				if (property.getPropertyName().indexOf(".") == -1) {
					propertyCondition = propertyConditions.get(property.getPropertyName());
				} else {
					propertyCondition = nestPropertyConditions.get(property.getPropertyName());
				}

				if (property.isRequiredNormal() && (propertyCondition == null || propertyCondition.getValue() == null)) {
					String displayLabel = TemplateUtil.getMultilingualString(property.getDisplayLabel(),
							property.getLocalizedDisplayLabelList(), pd.getDisplayName(), pd.getLocalizedDisplayNameList());
					getRequest().setAttribute(Constants.MESSAGE, resourceString("command.generic.search.NormalSearchContext.pleaseInput", displayLabel));
					return false;
				}

				if (pd instanceof ReferenceProperty && property.getEditor() instanceof ReferencePropertyEditor) {
					try {
						if (propertyCondition == null) {
							//参照の本体がないとネストがチェックできないので、本体の条件を作成
							propertyCondition = new ReferencePropertySearchCondition(pd, property.getEditor(), null);
						}
						((ReferencePropertySearchCondition) propertyCondition).validateNormalParameter(property.isRequiredNormal());
					} catch (SearchConditionValidationException e) {
						if (logger.isDebugEnabled()) {
							logger.debug(e.getMessage(), e);
						}
						if (e.getMessage() == null) {
							String displayLabel = TemplateUtil.getMultilingualString(property.getDisplayLabel(),
									property.getLocalizedDisplayLabelList(), pd.getDisplayName(), pd.getLocalizedDisplayNameList());
							getRequest().setAttribute(Constants.MESSAGE, resourceString("command.generic.search.NormalSearchContext.pleaseInput", displayLabel));
						} else {
							getRequest().setAttribute(Constants.MESSAGE, e.getMessage());
						}
						return false;
					}
				}
			}
		}

		return true;
	}

	private Map<String, PropertySearchCondition> getPropertyConditions() {
		if (propertyConditions == null) {
			condition = new NormalConditionValue();
			propertyConditions = new LinkedHashMap<>();
			for (PropertyDefinition p : getPropertyList()) {
				Object value = getConditionValue(p, p.getName());
				if (value != null && !(value.getClass().isArray() && ((Object[])value).length == 0)) {
					PropertyItem property = getLayoutProperty(p.getName());
					if (property == null || property.isHideNormalCondition()) throw new ApplicationException();

					condition.setValue(property.getPropertyName(), value);
					propertyConditions.put(property.getPropertyName(),
							PropertySearchCondition.newInstance(p, property.getEditor(), value));
				}
			}

			//ツリーから直接D&Dした参照のプロパティ項目、PropertyDefinitionからでは拾えない条件
			nestPropertyConditions = new LinkedHashMap<>();
			List<PropertyItem> properties = getLayoutProperties();
			for (PropertyItem property : properties) {
				//直接拾えるものは上で処理済みなのでスルー
				if (property.isBlank() || getEntityDefinition().getProperty(property.getPropertyName()) != null) continue;

				PropertyDefinition pd = EntityViewUtil.getPropertyDefinition(property.getPropertyName(), getEntityDefinition());
				Object value = getConditionValue(pd, property.getPropertyName());
				if (value != null && !(value.getClass().isArray() && ((Object[])value).length == 0)) {
					int lastIndex = property.getPropertyName().lastIndexOf(".");
					String parentName = property.getPropertyName().substring(0, lastIndex);
					if (property == null || property.isHideNormalCondition()) throw new ApplicationException();

					condition.setValue(property.getPropertyName(), value);
					nestPropertyConditions.put(property.getPropertyName(),
							PropertySearchCondition.newInstance(pd, property.getEditor(), value, parentName));
				}
			}
		}
		return propertyConditions;
	}

	/**
	 * 通常検索条件の値をセットします。
	 * @param propertyConditions 検索条件
	 * @param p プロパティ定義
	 * @param propName
	 */
	private Object getConditionValue(PropertyDefinition p, String propName) {
		final String conditionPrefix = Constants.SEARCH_COND_PREFIX;

		Object ret = null;
		if (p instanceof DateProperty) {
			// 単一検索、範囲検索が判断できないため2つ取得する
			Date[] date = new Date[2];
			date[0] = getRequest().getParamAsDate(conditionPrefix + propName + "From", TemplateUtil.getLocaleFormat().getServerDateFormat());
			date[1] = getRequest().getParamAsDate(conditionPrefix + propName + "To", TemplateUtil.getLocaleFormat().getServerDateFormat());
			ret = (date[0] == null && date[1] == null) ? null : date;
		} else if (p instanceof TimeProperty) {
			// 単一検索、範囲検索が判断できないため2つ取得する
			Time[] time = new Time[2];
			time[0] = getRequest().getParamAsTime(conditionPrefix + propName + "From", TemplateUtil.getLocaleFormat().getServerTimeFormat());
			time[1] = getRequest().getParamAsTime(conditionPrefix + propName + "To", TemplateUtil.getLocaleFormat().getServerTimeFormat());
			ret = (time[0] == null && time[1] == null) ? null : time;
		} else if (p instanceof DateTimeProperty) {
			// 単一検索、範囲検索が判断できないため2つ取得する
			Timestamp[] timestamp = new Timestamp[2];
			timestamp[0] = getRequest().getParamAsTimestamp(conditionPrefix + propName + "From", TemplateUtil.getLocaleFormat().getServerDateTimeFormat());
			timestamp[1] = getRequest().getParamAsTimestamp(conditionPrefix + propName + "To", TemplateUtil.getLocaleFormat().getServerDateTimeFormat());
			ret = (timestamp[0] == null && timestamp[1] == null) ? null : timestamp;
		} else if (p instanceof SelectProperty) {
			ret = getSelectValue(conditionPrefix + propName);
		} else if (p instanceof ReferenceProperty) {
			if (p.getName().equals(propName)) {
				ret = getReferenceValue((ReferenceProperty) p);
			} else {
				PropertyItem property = getLayoutProperty(propName);
				if (property != null) {
					//参照のプロパティを直接D&Dした場合
					ReferencePropertyEditor re = (ReferencePropertyEditor) property.getEditor();
					re.setPropertyName(propName);
					ret = getReferenceValue((ReferenceProperty) p, re);
				} else {
					//ネストプロパティ
					ret = getNestReferenceValue((ReferenceProperty) p, propName);
				}
			}
		} else if (p instanceof ExpressionProperty) {
			ret = getExpressionValue((ExpressionProperty) p, conditionPrefix + propName);
		} else if (p instanceof DecimalProperty) {
			// 単一検索、範囲検索の判断ができないため3つ取得する
			// DecimalPropertySearchConditionで判定
			BigDecimal[] decimal = new BigDecimal[3];
			decimal[0] = getRequest().getParamAsBigDecimal(conditionPrefix + propName);
			decimal[1] = getRequest().getParamAsBigDecimal(conditionPrefix + propName + "From");
			decimal[2] = getRequest().getParamAsBigDecimal(conditionPrefix + propName + "To");
			ret = (decimal[0] == null && decimal[1] == null && decimal[2] == null) ? null : decimal;
		} else if (p instanceof FloatProperty) {
			// 単一検索、範囲検索の判断ができないため3つ取得する
			// FloatPropertySearchConditionで判定
			Double[] dbl = new Double[3];
			dbl[0] =  getRequest().getParamAsDouble(conditionPrefix + propName);
			dbl[1] =  getRequest().getParamAsDouble(conditionPrefix + propName + "From");
			dbl[2] =  getRequest().getParamAsDouble(conditionPrefix + propName + "To");
			ret = (dbl[0] == null && dbl[1] == null && dbl[2] == null) ? null : dbl;
		} else if (p instanceof IntegerProperty) {
			// 単一検索、範囲検索の判断ができないため3つ取得する
			// IntegerPropertySearchConditionで判定
			Long[] lng = new Long[3];
			lng[0] = getRequest().getParamAsLong(conditionPrefix + propName);
			lng[1] = getRequest().getParamAsLong(conditionPrefix + propName + "From");
			lng[2] = getRequest().getParamAsLong(conditionPrefix + propName + "To");
			ret = (lng[0] == null && lng[1] == null && lng[2] == null) ? null : lng;
		} else {
			String value = getRequest().getParam(conditionPrefix + propName);
			if (value == null || value.trim().length() == 0) return null;
			
			//文字とかそのまま検索できるプロパティは変換しない
			ret = value;
		}
		return ret;
	}

	private Object getExpressionValue(ExpressionProperty ep, String propName) {
		//この時点でeditorにあわせた値作るの難しいので、あり得そうな値全て拾っておき、
		//ExpressionSearchConditionで型にあわせた条件作成時に変換
		if (ep.getResultType() == PropertyDefinitionType.DATE) {
			String[] ret = new String[3];
			ret[0] = getRequest().getParam(propName);//Editor未指定時
			ret[1] = getRequest().getParam(propName + "From");//DatePropertyEditor指定時
			ret[2] = getRequest().getParam(propName + "To");//DatePropertyEditor指定時
			//何も入ってなければ条件作らない
			if (StringUtil.isBlank(ret[0]) && StringUtil.isBlank(ret[1]) && StringUtil.isBlank(ret[2])) {
				return null;
			}
			return ret;
		} else if (ep.getResultType() == PropertyDefinitionType.DATETIME) {
			String[] ret = new String[3];
			ret[0] = getRequest().getParam(propName);//Editor未指定時
			ret[1] = getRequest().getParam(propName + "From");//DatePropertyEditor指定時
			ret[2] = getRequest().getParam(propName + "To");//DatePropertyEditor指定時
			//何も入ってなければ条件作らない
			if (StringUtil.isBlank(ret[0]) && StringUtil.isBlank(ret[1]) && StringUtil.isBlank(ret[2])) {
				return null;
			}
			return ret;
		} else if (ep.getResultType() == PropertyDefinitionType.SELECT) {
			String[] ret = getRequest().getParams(propName);
			if (ret != null && ret.length > 0) {
				//何も入ってなければ条件作らない
				boolean isNull = true;
				for (int i = 0; i < ret.length; i++) {
					if (StringUtil.isNotBlank(ret[i])) {
						isNull = false;
						break;
					}
				}
				if (isNull) return null;
			}
			return ret;
		} else if (ep.getResultType() == PropertyDefinitionType.TIME) {
			String[] ret = new String[3];
			ret[0] = getRequest().getParam(propName);//Editor未指定時
			ret[1] = getRequest().getParam(propName + "From");//DatePropertyEditor指定時
			ret[2] = getRequest().getParam(propName + "To");//DatePropertyEditor指定時
			//何も入ってなければ条件作らない
			if (StringUtil.isBlank(ret[0]) && StringUtil.isBlank(ret[1]) && StringUtil.isBlank(ret[2])) {
				return null;
			}
			return ret;
		} else if (ep.getResultType() == PropertyDefinitionType.DECIMAL) {
			String[] ret = new String[3];
			// Editor未指定時または単一検索
			ret[0] = getRequest().getParam(propName);
			// 範囲検索
			ret[1] = getRequest().getParam(propName + "From");
			ret[2] = getRequest().getParam(propName + "To");
			if (StringUtil.isBlank(ret[0]) && StringUtil.isBlank(ret[1]) && StringUtil.isBlank(ret[2])) {
				return null;
			}
			return ret;
		} else if (ep.getResultType() == PropertyDefinitionType.FLOAT) {
			String[] ret = new String[3];
			// Editor未指定時または単一検索
			ret[0] = getRequest().getParam(propName); // Editor未指定時または単一検索
			// 範囲検索
			ret[1] = getRequest().getParam(propName + "From");
			ret[2] = getRequest().getParam(propName + "To");
			if (StringUtil.isBlank(ret[0]) && StringUtil.isBlank(ret[1]) && StringUtil.isBlank(ret[2])) {
				return null;
			}
			return ret;
		} else if (ep.getResultType() == PropertyDefinitionType.INTEGER) {
			String[] ret = new String[3];
			// Editor未指定時または単一検索
			ret[0] = getRequest().getParam(propName);
			// 範囲検索
			ret[1] = getRequest().getParam(propName + "From");
			ret[2] = getRequest().getParam(propName + "To");
			if (StringUtil.isBlank(ret[0]) && StringUtil.isBlank(ret[1]) && StringUtil.isBlank(ret[2])) {
				return null;
			}
			return ret;
		} else {
			String ret = getRequest().getParam(propName);
			if (StringUtil.isBlank(ret)) return null;
			return ret;
		}
	}

	/**
	 * リクエストから参照型に関する検索条件を取得します。
	 * @param rp 参照プロパティ定義
	 * @return 検索条件
	 */
	private Object getReferenceValue(ReferenceProperty rp) {
		Object ret = null;
		List<PropertyItem> properties = getLayoutProperties();
		for (PropertyItem property : properties) {
			if (property.isBlank()) continue;

			if (property.getPropertyName().equals(rp.getName())) {
				if (property.getEditor() instanceof ReferencePropertyEditor) {
					ReferencePropertyEditor editor = (ReferencePropertyEditor) property.getEditor();
					editor.setPropertyName(rp.getName());
					ret = getReferenceValue(rp, editor);
				}
			}
		}
		return ret;
	}

	private Object getNestReferenceValue(ReferenceProperty rp, String propName) {
		ReferencePropertyEditor editor = getReferencePropertyEditor(propName);
		return getReferenceValue(rp, editor);
	}

	private Object getReferenceValue(ReferenceProperty rp, ReferencePropertyEditor editor) {
		ReferenceNormalConditionValue ret = null;
		boolean showNest = false;
		String propName = null;
		if (rp.getName().equals(editor.getPropertyName())) {
			propName = rp.getName();
		} else {
			propName = editor.getPropertyName();
		}
		if (editor.getDisplayType() == ReferenceDisplayType.SELECT) {
			//プルダウンならoid
			String oid = getRequest().getParam(Constants.SEARCH_COND_PREFIX + propName);
			if (StringUtil.isNotBlank(oid)) {
				ret = new ReferenceNormalConditionValue(new GenericEntity(rp.getObjectDefinitionName(), oid, null));
			}
		} else if (editor.getDisplayType() == ReferenceDisplayType.CHECKBOX) {
			//チェックボックスならoid
			List<Entity> list = new ArrayList<>();
			String[] oid = getRequest().getParams(Constants.SEARCH_COND_PREFIX + propName, String.class);
			if (oid != null && oid.length > 0) {
				for (String tmp : oid) {
					list.add(new GenericEntity(rp.getObjectDefinitionName(), tmp, null));
				}
				ret = new ReferenceNormalConditionValue(list.toArray(new Entity[list.size()]));
			}
		} else if (editor.getDisplayType() == ReferenceDisplayType.REFCOMBO) {
			//連動コンボならoid
			String oid = getRequest().getParam(Constants.SEARCH_COND_PREFIX + propName);
			if (StringUtil.isNotBlank(oid)) {
				ret = new ReferenceNormalConditionValue(new GenericEntity(rp.getObjectDefinitionName(), oid, null));
			} else {
				//上位階層でも検索
				if (editor.getSearchType() == RefComboSearchType.UPPER) {
					RefComboCondition rcc = getRefComboCondition(propName, editor.getReferenceComboSetting());
					if (rcc != null) {
						ret = new ReferenceNormalConditionValue(rcc);
					}
				}
			}
		} else if ((editor.getDisplayType() == ReferenceDisplayType.LINK && editor.isUseSearchDialog())
				 || (editor.getDisplayType() == ReferenceDisplayType.LABEL)){
			//リンクOrラベルならoid
			List<Entity> list = new ArrayList<>();
			String[] oid_ver = getRequest().getParams(Constants.SEARCH_COND_PREFIX + propName, String.class);
			if (oid_ver != null && oid_ver.length > 0) {
				for (String tmp : oid_ver) {
					if (tmp == null) continue;
					int lastIndex = tmp.lastIndexOf("_");
					String oid = tmp.substring(0, lastIndex);
					list.add(new GenericEntity(rp.getObjectDefinitionName(), oid, null));
				}
				ret = new ReferenceNormalConditionValue(list.toArray(new Entity[list.size()]));
			}
		} else if (editor.getDisplayType() == ReferenceDisplayType.TREE) {
			//ツリーならoid
			List<Entity> list = new ArrayList<>();
			String[] oid_ver = getRequest().getParams(Constants.SEARCH_COND_PREFIX + propName, String.class);
			if (oid_ver != null && oid_ver.length > 0) {
				for (String tmp : oid_ver) {
					if (tmp == null) continue;
					int lastIndex = tmp.lastIndexOf("_");
					String oid = tmp.substring(0, lastIndex);
					list.add(new GenericEntity(rp.getObjectDefinitionName(), oid, null));
				}
				ret = new ReferenceNormalConditionValue(list.toArray(new Entity[list.size()]));
			}
		} else if (editor.getDisplayType() == ReferenceDisplayType.UNIQUE && editor.isUseSearchDialog()) {
			//ユニークならoid
			List<Entity> list = new ArrayList<>();
			String[] oid_ver = getRequest().getParams(Constants.SEARCH_COND_PREFIX + propName, String.class);
			if (oid_ver != null && oid_ver.length > 0) {
				for (String tmp : oid_ver) {
					if (tmp == null || tmp.length() == 0) continue;
					int lastIndex = tmp.lastIndexOf("_");
					String oid = tmp.substring(0, lastIndex);
					list.add(new GenericEntity(rp.getObjectDefinitionName(), oid, null));
				}
				ret = new ReferenceNormalConditionValue(list.toArray(new Entity[list.size()]));
			}
		} else if (editor.getDisplayType() == ReferenceDisplayType.HIDDEN) {
			//HIDDENならoid
			List<Entity> list = new ArrayList<>();
			String[] oid_ver = getRequest().getParams(Constants.SEARCH_COND_PREFIX + propName, String.class);
			if (oid_ver != null && oid_ver.length > 0) {
				for (String tmp : oid_ver) {
					if (tmp == null) continue;
					int lastIndex = tmp.lastIndexOf("_");
					String oid = tmp.substring(0, lastIndex);
					String version = tmp.substring(lastIndex + 1);
					Entity cond = new GenericEntity(rp.getObjectDefinitionName(), oid, null);
					cond.setVersion(Long.valueOf(version));
					list.add(cond);
				}
				ret = new ReferenceNormalConditionValue(list.toArray(new Entity[list.size()]));
			}
		} else {
			boolean searchName = false;
			if (editor.getNestProperties().isEmpty()) {
				//ネストがないので名前で検索
				searchName = true;
			} else {
				//ネストと一緒に名前で検索
				if (editor.isUseNestConditionWithProperty()) searchName = true;
				showNest = true;
			}
			if (searchName) {
				Entity refEntity = null;
				String name = getRequest().getParam(Constants.SEARCH_COND_PREFIX + propName);
				if (StringUtil.isNotBlank(name)) {
					refEntity = new GenericEntity(rp.getObjectDefinitionName());
					refEntity.setName(name);
					ret = new ReferenceNormalConditionValue(refEntity);
				}
			}
		}

		if (showNest || editor.isUseNestConditionWithProperty()) {
			//ネスト項目あれば参照先の各プロパティ
			EntityDefinition ed = getReferenceEntityDefinition(rp);
			Entity nest = null;
			for (NestProperty np : editor.getNestProperties()) {
				PropertyDefinition pd = ed.getProperty(np.getPropertyName());
				Object value = getConditionValue(pd, propName + "." + pd.getName());
				if (value != null) {
					if (nest == null) nest = new GenericEntity(rp.getObjectDefinitionName());
					nest.setValue(pd.getName(), value);
				}
				if (np.getEditor() instanceof RangePropertyEditor) {
					RangePropertyEditor rangep = (RangePropertyEditor) np.getEditor();
					pd = ed.getProperty(rangep.getToPropertyName());
					value = getConditionValue(pd, propName + "." + pd.getName());
					if (value != null) {
						if (nest == null) nest = new GenericEntity(rp.getObjectDefinitionName());
						nest.setValue(pd.getName(), value);
					}
				}
			}
			if (nest != null) {
				if (ret == null) ret = new ReferenceNormalConditionValue(null);
				ret.setNest(nest);
			}
		}
		return ret;
	}

	private RefComboCondition getRefComboCondition(String propName, ReferenceComboSetting setting) {
		String name = propName + "." + setting.getPropertyName();
		String oid = getRequest().getParam(name);
		if (StringUtil.isNotBlank(oid)) {
			return new RefComboCondition(oid, name);
		} else if (setting.getParent() != null) {
			return getRefComboCondition(name, setting.getParent());
		}
		return null;
	}

	private ReferencePropertyEditor getReferencePropertyEditor(String propName) {
		String[] _propName = propName.split("\\.");
		List<PropertyItem> properties = getLayoutProperties();
		for (PropertyItem property : properties) {
			if (!property.isBlank() && property.getPropertyName().equals(_propName[0])) {
				if (property.getEditor() instanceof ReferencePropertyEditor) {
					ReferencePropertyEditor editor = (ReferencePropertyEditor) property.getEditor();
					ReferencePropertyEditor ret = getNestReferencePropertyEditor(editor, _propName, 1);
					ret.setPropertyName(propName);
					return ret;
				}
			}
		}
		return null;
	}

	private ReferencePropertyEditor getNestReferencePropertyEditor(ReferencePropertyEditor editor, String[] propName, int index) {
		if (editor.getNestProperties().size() == 0 && propName.length < index) return null;

		if (propName.length == index) {
			//最後のプロパティ
			return editor;
		} else {
			for (NestProperty property : editor.getNestProperties()) {
				if (property.getPropertyName().equals(propName[index])) {
					if (property.getEditor() instanceof ReferencePropertyEditor) {
						return getNestReferencePropertyEditor((ReferencePropertyEditor) property.getEditor(), propName, ++index);
					} else {
						return null;
					}
				}
			}
		}

		return null;
	}


	private Object getSelectValue(String propName) {
		Object ret = null;
		String strDispType = getRequest().getParam(propName + "_dispType");
		SelectDisplayType dispType = StringUtil.isNotBlank(strDispType) ? SelectDisplayType.valueOf(strDispType) : null;
		if (dispType == SelectDisplayType.CHECKBOX || dispType == SelectDisplayType.HIDDEN) {
			//CHECKBOXまたはHIDDENの場合は、複数可
			String[] values = getRequest().getParams(propName);
			List<SelectValue> list = new ArrayList<>();
			if (values != null && values.length > 0) {
				for (String value : values) {
					if (value != null && !value.isEmpty()) {
						list.add(new SelectValue(value));
					}
				}
			}
			ret = list.toArray(new SelectValue[list.size()]);
		} else {
			String value = getRequest().getParam(propName);
			if (value != null && !value.isEmpty()) {
				ret = new SelectValue(value);
			}
		}

		return ret;
	}

	public class NormalConditionValue {

		private Map<String, Object> properties;

		public NormalConditionValue() {
			properties = new HashMap<>();
		}

		public Object getValue(String propName) {
			return properties.get(propName);
		}

		public void setValue(String propName, Object value) {
			properties.put(propName, value);
		}
	}

	private static String resourceString(String key, Object... arguments) {
		return GemResourceBundleUtil.resourceString(key, arguments);
	}
}
