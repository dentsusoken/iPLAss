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

package org.iplass.gem.command.generic.search.condition;

import java.util.ArrayList;
import java.util.List;

import org.iplass.gem.GemConfigService;
import org.iplass.gem.command.Constants;
import org.iplass.gem.command.GemResourceBundleUtil;
import org.iplass.gem.command.generic.search.SearchConditionDetail;
import org.iplass.gem.command.generic.search.SearchConditionValidationException;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.expr.Not;
import org.iplass.mtp.entity.query.condition.expr.Paren;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.entity.query.condition.predicate.Greater;
import org.iplass.mtp.entity.query.condition.predicate.GreaterEqual;
import org.iplass.mtp.entity.query.condition.predicate.In;
import org.iplass.mtp.entity.query.condition.predicate.IsNotNull;
import org.iplass.mtp.entity.query.condition.predicate.IsNull;
import org.iplass.mtp.entity.query.condition.predicate.Lesser;
import org.iplass.mtp.entity.query.condition.predicate.LesserEqual;
import org.iplass.mtp.entity.query.condition.predicate.Like;
import org.iplass.mtp.entity.query.condition.predicate.NotEquals;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.editor.NestProperty;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.RangePropertyEditor;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.ReferenceDisplayType;
import org.iplass.mtp.view.generic.element.property.PropertyItem;
import org.iplass.mtp.web.template.TemplateUtil;

public class ReferencePropertySearchCondition extends PropertySearchCondition {

	private PropertyDefinition nestProperty;

	public ReferencePropertySearchCondition(PropertyDefinition definition,
			PropertyEditor editor, Object value) {
		super(definition, editor, value);
	}

	public ReferencePropertySearchCondition(PropertyDefinition definition,
			PropertyEditor editor, Object value, String parent) {
		super(definition, editor, value, parent);
	}

	public void setNestProperty(PropertyDefinition nestProperty) {
		this.nestProperty = nestProperty;
	}

	@Override
	public List<Condition> convertNormalCondition() {
		List<Condition> conditions = new ArrayList<>();
		ReferencePropertyEditor editor = getReferencePropertyEditor();
		Object value = null;
		Entity nest = null;
		if (getValue() != null) {
			ReferenceNormalConditionValue nValue = (ReferenceNormalConditionValue) getValue();
			value = nValue.getValue();
			nest = nValue.getNest();
		}
		if (value != null && editor != null) {
			if (editor.getDisplayType() == ReferenceDisplayType.SELECT) {
				//選択型はOIDで一致検索
				Entity entity = (Entity) value;
				if (entity.getOid() != null && !entity.getOid().isEmpty()) {
					conditions.add(new Equals(getPropertyName() + "." + Entity.OID, entity.getOid()));
				}
			} else if (editor.getDisplayType() == ReferenceDisplayType.CHECKBOX) {
				Entity[] list = (Entity[]) value;
				if (list != null) {
					if (list.length == 1) {
						conditions.add(new Equals(getPropertyName() + "." + Entity.OID, list[0].getOid()));
					} else if (list.length > 1) {
						List<String> oidList = new ArrayList<>();
						for (Entity tmp : list) {
							oidList.add(tmp.getOid());
						}
						conditions.add(new In(getPropertyName() + "." + Entity.OID, oidList.toArray()));
					}
				}
			} else if (editor.getDisplayType() == ReferenceDisplayType.REFCOMBO) {
				if (value instanceof Entity) {
					Entity entity = (Entity) value;
					if (entity.getOid() != null && !entity.getOid().isEmpty()) {
						conditions.add(new Equals(getPropertyName() + "." + Entity.OID, entity.getOid()));
					}
				} else if (value instanceof RefComboCondition) {
					RefComboCondition cond = (RefComboCondition) value;
					if (StringUtil.isNotBlank(cond.getName()) && StringUtil.isNotBlank(cond.getOid())) {
						conditions.add(new Equals(cond.getName() + "." + Entity.OID, cond.getOid()));
					}
				}
			} else if ((editor.getDisplayType() == ReferenceDisplayType.LINK && editor.isUseSearchDialog())
					 || (editor.getDisplayType() == ReferenceDisplayType.LABEL)){
				Entity[] list = (Entity[]) value;
				if (list != null) {
					if (list.length == 1) {
						conditions.add(new Equals(getPropertyName() + "." + Entity.OID, list[0].getOid()));
					} else if (list.length > 1) {
						List<String> oidList = new ArrayList<>();
						for (Entity tmp : list) {
							oidList.add(tmp.getOid());
						}
						conditions.add(new In(getPropertyName() + "." + Entity.OID, oidList.toArray()));
					}
				}
			} else if (editor.getDisplayType() == ReferenceDisplayType.TREE) {
				Entity[] list = (Entity[]) value;
				if (list != null) {
					if (list.length == 1) {
						conditions.add(new Equals(getPropertyName() + "." + Entity.OID, list[0].getOid()));
					} else if (list.length > 1) {
						List<String> oidList = new ArrayList<>();
						for (Entity tmp : list) {
							oidList.add(tmp.getOid());
						}
						conditions.add(new In(getPropertyName() + "." + Entity.OID, oidList.toArray()));
					}
				}
			} else if (editor.getDisplayType() == ReferenceDisplayType.UNIQUE && editor.isUseSearchDialog()) {
				Entity[] list = (Entity[]) value;
				if (list != null) {
					if (list.length == 1) {
						conditions.add(new Equals(getPropertyName() + "." + Entity.OID, list[0].getOid()));
					} else if (list.length > 1) {
						List<String> oidList = new ArrayList<>();
						for (Entity tmp : list) {
							oidList.add(tmp.getOid());
						}
						conditions.add(new In(getPropertyName() + "." + Entity.OID, oidList.toArray()));
					}
				}
			} else if (editor.getDisplayType() == ReferenceDisplayType.HIDDEN) {
				Entity[] list = (Entity[]) value;
				if (list != null) {
					if (list.length == 1) {
						conditions.add(new Equals(getPropertyName() + "." + Entity.OID, list[0].getOid()));
					} else if (list.length > 1) {
						List<String> oidList = new ArrayList<>();
						for (Entity tmp : list) {
							oidList.add(tmp.getOid());
						}
						conditions.add(new In(getPropertyName() + "." + Entity.OID, oidList.toArray()));
					}
				}
			} else {
				Entity entity = (Entity) value;
				if (entity.getName() != null && !entity.getName().isEmpty()) {
					// 検索処理で表示ラベルとして扱うプロパティを検索条件に利用する
					GemConfigService service = ServiceRegistry.getRegistry().getService(GemConfigService.class);
					String displayLabelItem = getReferencePropertyEditor().getDisplayLabelItem();
					if (service.isUseDisplayLabelItemInSearch() && displayLabelItem != null && !displayLabelItem.isBlank()) {
						//表示ラベルの部分一致
						conditions.add(new Like(getPropertyName() + "." + displayLabelItem, entity.getName(), Like.MatchPattern.PARTIAL));
					} else {
						//名前の部分一致
						conditions.add(new Like(getPropertyName() + "." + Entity.NAME, entity.getName(), Like.MatchPattern.PARTIAL));
					}
				}
			}
		}
		if (nest != null) {
			//ネストの各項目を型にあわせて条件化
			ReferenceProperty rp = getReferenceProperty();
			if (rp != null) {
				EntityDefinition ed = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class).get(rp.getObjectDefinitionName());
				for (NestProperty np : editor.getNestProperties()) {
					Object _value = nest.getValue(np.getPropertyName());
					if (_value != null) {
						PropertyDefinition definition = ed.getProperty(np.getPropertyName());
						PropertySearchCondition nestPropertyCondition =
							PropertySearchCondition.newInstance(definition, np.getEditor(), _value, getPropertyName());
						conditions.addAll(nestPropertyCondition.convertNormalCondition());
					}
				}
			}
		}
		return conditions;
	}

	@Override
	public List<Condition> convertDetailCondition() {
		List<Condition> conditions = new ArrayList<>();
		if (getValue() instanceof SearchConditionDetail) {
			SearchConditionDetail detail = (SearchConditionDetail) getValue();
			String propName = detail.getPropertyName();
			if (nestProperty == null || nestProperty instanceof ReferenceProperty) {
				//nestPropertyがない(参照プロパティ自体)か参照のnestPropertyならnameで検索
				Object conditionValue = convertDetailValue(detail);
				
				GemConfigService service = ServiceRegistry.getRegistry().getService(GemConfigService.class);
				String displayLabelItem = getReferencePropertyEditor().getDisplayLabelItem();
				if (service.isUseDisplayLabelItemInSearch() && displayLabelItem != null && !displayLabelItem.isBlank()) {
					//表示ラベルとして扱うプロパティが設定されたら検索条件に利用する
					propName = propName + "." + displayLabelItem;
				} else {
					propName = propName + "." + Entity.NAME;
				}

				if (Constants.EQUALS.equals(detail.getPredicate())) {
					conditions.add(new Equals(propName, conditionValue));
				} else if (Constants.NOTEQUALS.equals(detail.getPredicate())) {
					conditions.add(new NotEquals(propName, conditionValue));
				} else if (Constants.GREATER.equals(detail.getPredicate())) {
					conditions.add(new Greater(propName, conditionValue));
				} else if (Constants.GREATEREQUALS.equals(detail.getPredicate())) {
					conditions.add(new GreaterEqual(propName, conditionValue));
				} else if (Constants.LESSER.equals(detail.getPredicate())) {
					conditions.add(new Lesser(propName, conditionValue));
				} else if (Constants.LESSEREQUALS.equals(detail.getPredicate())) {
					conditions.add(new LesserEqual(propName, conditionValue));
				} else if (Constants.FRONTMATCH.equals(detail.getPredicate())) {
					//conditions.add(new Like(propName, conditionValue + "%"));
					conditions.add(new Like(propName, (String) conditionValue, Like.MatchPattern.PREFIX));
				} else if (Constants.BACKWARDMATCH.equals(detail.getPredicate())) {
					//conditions.add(new Like(propName, "%" + conditionValue));
					conditions.add(new Like(propName, (String) conditionValue, Like.MatchPattern.POSTFIX));
				} else if (Constants.INCLUDE.equals(detail.getPredicate())) {
					//conditions.add(new Like(propName, "%" + conditionValue + "%"));
					conditions.add(new Like(propName, (String) conditionValue, Like.MatchPattern.PARTIAL));
				} else if (Constants.NOTINCLUDE.equals(detail.getPredicate())) {
					//not not xxx like zzzとならないようにparenで囲む
					//conditions.add(new Paren(new Not(new Like(propName, "%" + conditionValue + "%"))));
					conditions.add(new Paren(new Not(new Like(propName, (String) conditionValue, Like.MatchPattern.PARTIAL))));
				} else if (Constants.NOTNULL.equals(detail.getPredicate())) {
					conditions.add(new IsNotNull(propName));
				} else if (Constants.NULL.equals(detail.getPredicate())) {
					conditions.add(new IsNull(propName));
				} else if (Constants.IN.equals(detail.getPredicate())) {
					Object[] array = null;
					if (conditionValue.getClass().isArray()) {
						array = (Object[]) conditionValue;
					} else {
						array = new Object[]{ conditionValue };
					}
					conditions.add(new In(propName, array));
				}
			} else {
				//ネストの項目で参照以外は、型に合わせて値を変換する必要あるのでネストの条件を作成
				int firstDotIndex = propName.indexOf('.');
				if (firstDotIndex > -1) {
					String topPropName = propName.substring(0, firstDotIndex);
					String subPropName = propName.substring(firstDotIndex + 1);
					if (topPropName.equals(getDefinition().getName())) {
						ReferencePropertyEditor re = getReferencePropertyEditor();
						for (NestProperty nest : re.getNestProperties()) {
							PropertyEditor nestEditor = getNestPropertyEditor(nest, subPropName);
							if (nestEditor != null) {
								PropertySearchCondition nestPropertyCondition = PropertySearchCondition.newInstance(nestProperty, nestEditor, detail);
								conditions.addAll(nestPropertyCondition.convertDetailCondition());
							}
						}
					}

				}

			}
		}
		return conditions;

	}

	@Override
	public boolean checkNormalParameter(PropertyItem property) {
		//nestの項目をチェック
		ReferenceNormalConditionValue nValue = (ReferenceNormalConditionValue) getValue();
		if (nValue.getNest() == null) return true;
		Entity nest = nValue.getNest();

		ReferenceProperty rp = getReferenceProperty();
		EntityDefinition ed = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class).get(rp.getObjectDefinitionName());
		for (PropertyDefinition pd : ed.getPropertyList()) {
			NestProperty np = getNestProperty(pd.getName());
			Object value = nest.getValue(pd.getName());
			if (np == null) {
				//画面定義に設定されてないのに値があればエラー
				//(NestPropertyがなければそもそもリクエストから値を取らないはず)
				if (value != null) {
					if (value.getClass().isArray()) {
						if (((Object[])value).length != 0) {
							return false;
						}
					} else {
						return false;
					}
				}
			} else {
				if (pd instanceof ReferenceProperty) {
					//参照の場合は直下のネスト項目をチェック
					if (value != null) {
						PropertySearchCondition nestPropertyCondition =
								PropertySearchCondition.newInstance(pd, np.getEditor(), value, getPropertyName());
						if (!nestPropertyCondition.checkNormalParameter(null)) return false;
					}
				}
			}
		}

		return true;
	}

	private SearchConditionValidationException createException(String displayName) {
		if (displayName == null) {
			return new SearchConditionValidationException();
		}
		return new SearchConditionValidationException(resourceString("command.generic.search.condition.ReferencePropertySearchCondition.pleaseInput", displayName));
	}

	public void validateNormalParameter(boolean validateEntity) {
		//必須チェック
		ReferencePropertyEditor editor = getReferencePropertyEditor();
		Object value = null;
		Entity nest = null;
		boolean validateNest = false;
		if (getValue() != null) {
			ReferenceNormalConditionValue nValue = (ReferenceNormalConditionValue) getValue();
			value = nValue.getValue();
			nest = nValue.getNest();
		}
		//自身の中に表示用のラベルを持ってないので、Exception受け側でメッセージ作成
		if (editor != null) {
			if (editor.getDisplayType() == ReferenceDisplayType.SELECT) {
				Entity entity = (Entity) value;
				if (validateEntity && (value == null || StringUtil.isBlank(entity.getOid()))) {
					throw createException(null);
				}
			} else if (editor.getDisplayType() == ReferenceDisplayType.CHECKBOX) {
				Entity[] list = (Entity[]) value;
				if (validateEntity && (value == null || list.length == 0)) {
					throw createException(null);
				}
			} else if (editor.getDisplayType() == ReferenceDisplayType.REFCOMBO) {
				if (validateEntity && value == null) throw createException(null);

				if (value instanceof Entity) {
					Entity entity = (Entity) value;
					if (validateEntity && StringUtil.isBlank(entity.getOid())) {
						throw createException(null);
					}
				} else if (value instanceof RefComboCondition) {
					RefComboCondition cond = (RefComboCondition) value;
					if (validateEntity && StringUtil.isBlank(cond.getName()) || StringUtil.isBlank(cond.getOid())) {
						throw createException(null);
					}
				}
			} else if ((editor.getDisplayType() == ReferenceDisplayType.LINK && editor.isUseSearchDialog())
					 || (editor.getDisplayType() == ReferenceDisplayType.LABEL)){
				Entity[] list = (Entity[]) value;
				if (validateEntity && (value == null || list.length == 0)) {
					createException(null);
				}
			} else if (editor.getDisplayType() == ReferenceDisplayType.TREE) {
				Entity[] list = (Entity[]) value;
				if (validateEntity && (value == null || list.length == 0)) {
					createException(null);
				}
			} else if (editor.getDisplayType() == ReferenceDisplayType.UNIQUE && editor.isUseSearchDialog()) {
				Entity[] list = (Entity[]) value;
				if (validateEntity && (value == null || list.length == 0)) {
					createException(null);
				}
			} else if (editor.getDisplayType() == ReferenceDisplayType.HIDDEN) {
				Entity[] list = (Entity[]) value;
				if (validateEntity && (value == null || list.length == 0)) {
					createException(null);
				}
			} else {
				//名前の部分一致
				int rowNum = 0;
				for (NestProperty np : editor.getNestProperties()) {
					if (np.getEditor() != null) rowNum++;
				}
				validateNest = rowNum != 0;

				//ネストがないか、ネストと一緒にプロパティを条件にする場合
				if (rowNum == 0 || editor.isUseNestConditionWithProperty()) {
					Entity entity = (Entity) value;
					if (validateEntity && (value == null || StringUtil.isBlank(entity.getName()))) {
						throw createException(null);
					}
				}
			}
		}

		if (validateNest || (editor != null && editor.isUseNestConditionWithProperty())) {
			//ネストの各項目を型にあわせて条件化
			ReferenceProperty rp = getReferenceProperty();
			if (rp != null) {
				//子要素のエラーメッセージはここで作成する
				EntityDefinition ed = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class).get(rp.getObjectDefinitionName());
				for (NestProperty np : editor.getNestProperties()) {
					PropertyDefinition definition = ed.getProperty(np.getPropertyName());
					String displayLabel = TemplateUtil.getMultilingualString(np.getDisplayLabel(),
							np.getLocalizedDisplayLabelList(), definition.getDisplayName(), definition.getLocalizedDisplayNameList());
					Object _value = nest != null ? nest.getValue(np.getPropertyName()) : null;
					if (definition instanceof ReferenceProperty) {
						try {
							PropertySearchCondition nestPropertyCondition =
								PropertySearchCondition.newInstance(definition, np.getEditor(), _value, getPropertyName());
							((ReferencePropertySearchCondition) nestPropertyCondition).validateNormalParameter(np.isRequiredNormal());
						} catch (SearchConditionValidationException e) {
							if (e.getMessage() == null) {
								throw createException(displayLabel);
							} else {
								throw e;
							}
						}
					}
					if (np.isRequiredNormal()) {
						if (_value == null) {
							throw createException(displayLabel);
						}
					}
				}
			}
		}
	}

	@Override
	public boolean checkDetailParameter(PropertyItem property) {
		if (!(getValue() instanceof SearchConditionDetail)) return false;

		//ネストじゃなければチェック済み
		if (nestProperty == null) return true;

		//ネストのチェック
		ReferencePropertyEditor editor = getReferencePropertyEditor();
		SearchConditionDetail detail = (SearchConditionDetail) getValue();
		int firstDotIndex = detail.getPropertyName().indexOf('.');
		if (firstDotIndex > 0) {
			String topPropName = detail.getPropertyName().substring(0, firstDotIndex);
			String subPropName = detail.getPropertyName().substring(firstDotIndex + 1);
			if (topPropName.equals(property.getPropertyName())) {
				for (NestProperty nest : editor.getNestProperties()) {
					if (checkNestProperty(nest, subPropName)) return true;
				}
			}
		}
		return false;
	}

	private boolean checkNestProperty(NestProperty nest, String propName) {
		int firstDotIndex = propName.indexOf('.');
		if (firstDotIndex > 0) {
			if (nest.getEditor() instanceof ReferencePropertyEditor
					&& !((ReferencePropertyEditor) nest.getEditor()).getNestProperties().isEmpty()) {
				ReferencePropertyEditor editor = (ReferencePropertyEditor) nest.getEditor();
				String topPropName = propName.substring(0, firstDotIndex);
				String subPropName = propName.substring(firstDotIndex + 1);
				if (topPropName.equals(nest.getPropertyName())) {
					for (NestProperty child : editor.getNestProperties()) {
						if (checkNestProperty(child, subPropName)) return true;
					}
				}
			}
		} else if (nest.getEditor() instanceof RangePropertyEditor) {
			return (((RangePropertyEditor) nest.getEditor()).getToPropertyName()).equals(propName);
		} else {
			return nest.getPropertyName().equals(propName);
		}
		return false;
	}

	private PropertyEditor getNestPropertyEditor(NestProperty nest, String propName) {
		int firstDotIndex = propName.indexOf('.');
		if (firstDotIndex > 0) {
			if (nest.getEditor() instanceof ReferencePropertyEditor
					&& !((ReferencePropertyEditor) nest.getEditor()).getNestProperties().isEmpty()) {
				ReferencePropertyEditor editor = (ReferencePropertyEditor) nest.getEditor();
				String topPropName = propName.substring(0, firstDotIndex);
				String subPropName = propName.substring(firstDotIndex + 1);
				if (topPropName.equals(nest.getPropertyName())) {
					for (NestProperty child : editor.getNestProperties()) {
						if (checkNestProperty(child, subPropName)) return child.getEditor();
					}
				}
			}
		} else {
			return nest.getEditor();
		}
		return null;
	}

	private NestProperty getNestProperty(String name) {
		ReferencePropertyEditor editor = getReferencePropertyEditor();
		if (editor == null || editor.getNestProperties().isEmpty()) return null;
		for (NestProperty np : editor.getNestProperties()) {
			if (np.getPropertyName().equals(name)) {
				return np;
			} else if (np.getEditor() instanceof RangePropertyEditor) {
				return np;
			}
		}
		return null;
	}

	private ReferenceProperty getReferenceProperty() {
		if (getDefinition() instanceof ReferenceProperty) {
			return (ReferenceProperty) getDefinition();
		}
		return null;
	}

	private ReferencePropertyEditor getReferencePropertyEditor() {
		if (getEditor() instanceof ReferencePropertyEditor) {
			return (ReferencePropertyEditor) getEditor();
		}
		return null;
	}

	public static class ReferenceNormalConditionValue {
		private Object value;
		private Entity nest;
		public ReferenceNormalConditionValue(Object value) {
			this.value = value;
		}
		public Object getValue() {
			return value;
		}
		public void setValue(Object value) {
			this.value = value;
		}
		public Entity getNest() {
			return nest;
		}
		public void setNest(Entity nest) {
			this.nest = nest;
		}
	}

	public static class RefComboCondition {
		private String oid;
		private String name;
		public RefComboCondition(String oid, String name) {
			this.oid = oid;
			this.name = name;
		}
		public String getOid() {
			return oid;
		}
		public void setOid(String oid) {
			this.oid = oid;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}

	private static String resourceString(String key, Object... arguments) {
		return GemResourceBundleUtil.resourceString(key, arguments);
	}
}
