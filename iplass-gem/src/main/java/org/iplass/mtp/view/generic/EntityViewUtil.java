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

package org.iplass.mtp.view.generic;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.iplass.gem.command.GemResourceBundleUtil;
import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.entity.ValidateError;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.definition.LocalizedSelectValueDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.BooleanProperty;
import org.iplass.mtp.entity.definition.properties.DateProperty;
import org.iplass.mtp.entity.definition.properties.DateTimeProperty;
import org.iplass.mtp.entity.definition.properties.DecimalProperty;
import org.iplass.mtp.entity.definition.properties.FloatProperty;
import org.iplass.mtp.entity.definition.properties.IntegerProperty;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.entity.definition.properties.SelectProperty;
import org.iplass.mtp.entity.definition.properties.StringProperty;
import org.iplass.mtp.entity.definition.properties.TimeProperty;
import org.iplass.mtp.entity.definition.properties.selectvalue.SelectValueDefinition;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.condition.predicate.In;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.view.generic.parser.Parser;
import org.iplass.mtp.tenant.TenantI18nInfo;
import org.iplass.mtp.util.ResourceBundleUtil;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.editor.BooleanPropertyEditor;
import org.iplass.mtp.view.generic.editor.DatePropertyEditor;
import org.iplass.mtp.view.generic.editor.DecimalPropertyEditor;
import org.iplass.mtp.view.generic.editor.EditorValue;
import org.iplass.mtp.view.generic.editor.FloatPropertyEditor;
import org.iplass.mtp.view.generic.editor.IntegerPropertyEditor;
import org.iplass.mtp.view.generic.editor.JoinPropertyEditor;
import org.iplass.mtp.view.generic.editor.NestProperty;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.SelectPropertyEditor;
import org.iplass.mtp.view.generic.editor.StringPropertyEditor;
import org.iplass.mtp.view.generic.editor.TemplatePropertyEditor;
import org.iplass.mtp.view.generic.editor.TimePropertyEditor;
import org.iplass.mtp.view.generic.editor.TimestampPropertyEditor;
import org.iplass.mtp.view.generic.editor.UserPropertyEditor;
import org.iplass.mtp.view.generic.element.Element;
import org.iplass.mtp.view.generic.element.VirtualPropertyItem;
import org.iplass.mtp.view.generic.element.property.PropertyElement;
import org.iplass.mtp.view.generic.element.section.DefaultSection;
import org.iplass.mtp.view.generic.element.section.ReferenceSection;
import org.iplass.mtp.view.generic.parser.Token;
import org.iplass.mtp.web.template.TemplateUtil;

/**
 * 画面定義用ユーティリティクラス
 *
 * @author lis3wg
 */
public class EntityViewUtil {

	/**
	 * エレメントの表示可否を判定します。
	 *
	 * @param definitionName Entity定義名
	 * @param elementRuntimeId エレメントのランタイムID
	 * @param outputType 表示タイプ
	 * @param entity 表示対象のエンティティ
	 * @return 表示可否
	 */
	public static boolean isDisplayElement(String definitionName, String elementRuntimeId, OutputType outputType, Entity entity) {
		EntityViewManager evm = ManagerLocator.manager(EntityViewManager.class);
		return evm.isDisplayElement(definitionName, elementRuntimeId, outputType, entity);
	}

	/**
	 * エレメントのJSPファイルパスを取得します。
	 * @param element エレメント
	 * @return JSPファイルパス
	 */
	public static String getJspPath(Element element) {
//		Jsp jsp = element.getClass().getAnnotation(Jsp.class);
//		if (jsp != null) {
//			return jsp.path();
//		}
//		return null;
		return getJspPath(element, "");
	}
	/**
	 * エレメントのJSPファイルパスを取得します。
	 * @param element エレメント
	 * @return JSPファイルパス
	 */
	public static String getJspPath(Element element, String key) {
		Jsps jsps = element.getClass().getAnnotation(Jsps.class);
		if (jsps != null) {
			for (Jsp jsp : jsps.value()) {
				if (key.equals(jsp.key())) {
					return jsp.path();
				}
			}
		}
		Jsp jsp = element.getClass().getAnnotation(Jsp.class);
		if (jsp != null) {
			return jsp.path();
		}
		return null;
	}

	/**
	 * プロパティエディタのJSPファイルパスを取得します。
	 * @param editor プロパティエディタ
	 * @return JSPファイルパス
	 */
	public static String getJspPath(PropertyEditor editor) {
//		Jsp jsp = editor.getClass().getAnnotation(Jsp.class);
//		if (jsp != null) {
//			return jsp.path();
//		}
//		return null;
		return getJspPath(editor, "");
	}

	/**
	 * プロパティエディタのJSPファイルパスを取得します。
	 * @param editor プロパティエディタ
	 * @return JSPファイルパス
	 */
	public static String getJspPath(PropertyEditor editor, String key) {
		Jsps jsps = editor.getClass().getAnnotation(Jsps.class);
		if (jsps != null) {
			for (Jsp jsp : jsps.value()) {
				if (key.equals(jsp.key())) {
					return jsp.path();
				}
			}
		}
		Jsp jsp = editor.getClass().getAnnotation(Jsp.class);
		if (jsp != null) {
			return jsp.path();
		}
		return null;
	}

	public static List<Token> perse(JoinPropertyEditor editor) throws IOException {
		Parser parser = new Parser(editor.getJoinProperties());
		return parser.parse(editor.getFormat());
	}

	public static ValidateError[] collectNestPropertyErrors(JoinPropertyEditor editor, String prefix, final ValidateError[] errors) {
		ValidateError error = new ValidateError();
		error.setErrorCodes(new ArrayList<String>());
		error.setErrorMessages(new ArrayList<String>());
		if (StringUtil.isNotBlank(prefix)) {
			error.setPropertyName("join_" + prefix + "." + editor.getPropertyName());
		} else {
			error.setPropertyName("join_" + editor.getPropertyName());
		}

		// ネストプロパティ名を取得します。
		List<String> nestPropNames = collectNestPropertyNames(editor, prefix);
		nestPropNames.stream()
			.map(propName -> {
				// ネストプロパティの検証エラーを取得します。
				return Arrays.stream(errors)
						.filter(err -> {
							String name = err.getPropertyName();
							if (name.equals(propName) || name.startsWith(propName + ".")) {
								return true;
							}
							// 多重度が複数の場合、検証エラーメッセージにindex値が含む可能性があります。
							// メターデータの定義のみからindex値が判断きないので、プロパティ名の文字列で始まるかで判断します。
							int firstIndex = -1;
							if (name.startsWith(propName + "[") && (firstIndex = name.indexOf("]", propName.length() + 1)) > -1) {
								return firstIndex + 1 >= name.length() || name.charAt(firstIndex + 1) == '.';
							}
							return false;
						})
						.collect(Collectors.toList());
			})
			.flatMap(Collection::stream)
			.forEach(err -> {
				for (int i = 0; i < err.getErrorMessages().size(); i++) {
					String errorMessage = err.getErrorMessages().get(i);
					String errorCode = (i < err.getErrorCodes().size()) ? err.getErrorCodes().get(i) : "";
					// 重複の検証エラーメッセージを除外します。
					if ((errorCode.length() > 0 && error.getErrorCodes().contains(errorCode))
							|| error.getErrorMessages().contains(errorMessage)) {
						continue;
					}
					error.addErrorMessage(errorMessage, errorCode);
				}
			});

		return error.getErrorMessages().size() > 0 ? new ValidateError[] { error } : null;
	}

	/**
	 * エンティティの定義でJoinPropertyEditorの一番目プロパティと同じ階層のネストプロパティを取得します。
	 */
	private static List<String> collectNestPropertyNames(JoinPropertyEditor editor, String prefix) {
		List<String> nestPropNames = new ArrayList<String>();
		for (NestProperty np : editor.getJoinProperties()) {
			if (np.getEditor() instanceof JoinPropertyEditor) {
				JoinPropertyEditor jpe = (JoinPropertyEditor) np.getEditor();
				// JoinPropertyにさらにJoinPropertyが定義された場合。。。
				collectNestPropertyNames(jpe, prefix);
			} else {
				if (StringUtil.isNotBlank(prefix)) {
					nestPropNames.add(prefix + "." + np.getPropertyName());
				} else {
					nestPropNames.add(np.getPropertyName());
				}
			}
		}
		return nestPropNames;
	}

	public static String getSelectPropertyLabel(
			List<LocalizedSelectValueDefinition> localizedList, EditorValue editorValue, List<SelectValue> valueList) {

		String value = editorValue.getValue();

		String editorLabel = editorValue.getLabel();

		if (ExecuteContext.getCurrentContext().getCurrentTenant().getTenantConfig(TenantI18nInfo.class).isUseMultilingual()) {
			// テナントの多言語設定がONの場合

			String lang = ExecuteContext.getCurrentContext().getLanguage();

			if (StringUtil.isBlank(editorLabel)) {
				// viewのdefaultがブランクの場合はentityのラベルを返却
				if (valueList != null) {
					for (SelectValue selectValue : valueList) {
						if (selectValue.getValue().equals(value)) {
							return getSelectPropertyLabel(localizedList, value, selectValue.getDisplayName());
						}
					}
				}
			} else {
				// viewのdefaultがブランクでない場合は多言語をチェックしあれば多言語を返却、なければviewのデフォルトを返却
				if (editorValue.getLocalizedLabelList() != null) {
					for (LocalizedStringDefinition localizedStringDefinition : editorValue.getLocalizedLabelList()) {
						if (lang.equals(localizedStringDefinition.getLocaleName())) {
							return localizedStringDefinition.getStringValue();
						}
					}
				}
				return editorLabel;
			}
		} else {
			// テナントの多言語設定がOFFの場合

			if (StringUtil.isBlank(editorLabel)) {
				if (valueList != null) {
					for (SelectValue selectValue: valueList) {
						if (selectValue.getValue().equals(value)) {
							return selectValue.getDisplayName();
						}
					}
				}
			} else {
				return editorLabel;
			}
		}

		return null;
	}

	public static String getSelectPropertyLabel(List<LocalizedSelectValueDefinition> localizedList, String value, String defaultLabel){
		if (value == null) {
			return null;
		}

		if (ExecuteContext.getCurrentContext().getCurrentTenant().getTenantConfig(TenantI18nInfo.class).isUseMultilingual()) {
			// テナントの多言語設定がONの場合
			if (localizedList != null) {
				String lang = ExecuteContext.getCurrentContext().getLanguage();

				for (LocalizedSelectValueDefinition tmpLsvd : localizedList) {
					if (tmpLsvd.getLocaleName().equals(lang)) {
						if (tmpLsvd.getSelectValueList() != null) {
							for (SelectValue lsv : tmpLsvd.getSelectValueList()) {
								if (value.equals(lsv.getValue())) {
									return lsv.getDisplayName();
								}
							}
						}

						break;
					}
				}
			}

			return defaultLabel;
		} else {
			// テナントの多言語設定がOFFの場合
			return defaultLabel;
		}
	}

	/**
	 * EditorValueのリストを生成します。
	 *
	 * @param sp 対象SelectProperty
	 * @param lang 言語(多言語利用時に利用されます。nullの場合は、利用可能言語すべてが取得されます)
	 * @return EditorValueのリスト
	 */
	public static List<EditorValue> createEditorValueList(SelectProperty sp, String lang) {
		List<EditorValue> valueList = null;
		if (sp != null) {
			if (sp.getSelectValueList() != null) {
				valueList = new ArrayList<EditorValue>();
				for (SelectValue v : sp.getSelectValueList()) {
					EditorValue ev = new EditorValue(v.getDisplayName(), v.getValue());
					//多言語設定
					if (ExecuteContext.getCurrentContext().getCurrentTenant().getTenantConfig(TenantI18nInfo.class).isUseMultilingual()) {
						if (StringUtil.isEmpty(lang)) {
							//lang未指定の場合は全部
							if (TemplateUtil.getEnableLanguages() != null) {
								List<LocalizedStringDefinition> evlList = new ArrayList<LocalizedStringDefinition>();
								for (String enableLang : TemplateUtil.getEnableLanguages().keySet()) {
									SelectValue lsv = sp.getLocalizedSelectValue(v.getValue(), enableLang);
									if (lsv != null) {
										LocalizedStringDefinition lsd = new LocalizedStringDefinition();
										lsd.setLocaleName(enableLang);
										lsd.setStringValue(lsv.getDisplayName());
										evlList.add(lsd);
									}
								}
								ev.setLocalizedLabelList(evlList);
							}
						} else {
							List<LocalizedStringDefinition> evlList = null;
							SelectValue lsv = sp.getLocalizedSelectValue(v.getValue(), lang);
							if (lsv != null) {
								LocalizedStringDefinition lsd = new LocalizedStringDefinition();
								lsd.setLocaleName(lang);
								lsd.setStringValue(lsv.getDisplayName());
								evlList = new ArrayList<LocalizedStringDefinition>();
								evlList.add(lsd);
							}
							ev.setLocalizedLabelList(evlList);
						}
					}
					valueList.add(ev);
				}
			}
		}
		return valueList;
	}

	/**
	 * EditorValueのリストを生成します。
	 *
	 * @param svd 対象SelectValueDefinition
	 * @param lang 言語(多言語利用時に利用されます。nullの場合は、利用可能言語すべてが取得されます)
	 * @return EditorValueのリスト
	 */
	public static List<EditorValue> createEditorValueList(SelectValueDefinition svd, String lang) {
		List<EditorValue> valueList = null;
		if (svd != null) {
			if (svd.getSelectValueList() != null) {
				valueList = new ArrayList<EditorValue>();
				for (SelectValue v : svd.getSelectValueList()) {
					EditorValue ev = new EditorValue(v.getDisplayName(), v.getValue());
					//多言語設定
					if (ExecuteContext.getCurrentContext().getCurrentTenant().getTenantConfig(TenantI18nInfo.class).isUseMultilingual()) {
						if (StringUtil.isEmpty(lang)) {
							//lang未指定の場合は全部
							if (TemplateUtil.getEnableLanguages() != null) {
								List<LocalizedStringDefinition> evlList = new ArrayList<LocalizedStringDefinition>();
								for (String enableLang : TemplateUtil.getEnableLanguages().keySet()) {
									SelectValue lsv = svd.getLocalizedSelectValue(v.getValue(), enableLang);
									if (lsv != null) {
										LocalizedStringDefinition lsd = new LocalizedStringDefinition();
										lsd.setLocaleName(enableLang);
										lsd.setStringValue(lsv.getDisplayName());
										evlList.add(lsd);
									}
								}
								ev.setLocalizedLabelList(evlList);
							}
						} else {
							List<LocalizedStringDefinition> evlList = null;
							SelectValue lsv = svd.getLocalizedSelectValue(v.getValue(), lang);
							if (lsv != null) {
								LocalizedStringDefinition lsd = new LocalizedStringDefinition();
								lsd.setLocaleName(lang);
								lsd.setStringValue(lsv.getDisplayName());
								evlList = new ArrayList<LocalizedStringDefinition>();
								evlList.add(lsd);
							}
							ev.setLocalizedLabelList(evlList);
						}
					}
					valueList.add(ev);
				}
			}
		}
		return valueList;
	}



	public static String getStringPropertySelectTypeLabel(EditorValue editorValue) {
		// StringPropertyEditor内でTypeをSelectにしているケース
		// 単純にedtitorValue内の多言語が存在する場合だけ多言語情報を返却する

		if (ExecuteContext.getCurrentContext().getCurrentTenant().getTenantConfig(TenantI18nInfo.class).isUseMultilingual()) {
			// テナントの多言語設定がONの場合
			String lang = ExecuteContext.getCurrentContext().getLanguage();

			if (editorValue.getLocalizedLabelList() != null) {
				for (LocalizedStringDefinition localizedStringDefinition : editorValue.getLocalizedLabelList()) {
					if (lang.equals(localizedStringDefinition.getLocaleName())) {
						return localizedStringDefinition.getStringValue();
					}
				}
			}
			return editorValue.getLabel();
		} else {
			// テナントの多言語設定がOFFの場合
			return editorValue.getLabel();
		}
	}

	/**
	 * Resourceファイルからデフォルト言語に対応した対象KEYの値を返します。
	 *
	 * @param key Resource Key
	 * @return デフォルト言語に対応するリソース値
	 */
	public static String getDefautlResourceString(String key) {
		//defaultは、テナントのLocaleを基本とする
		return ResourceBundleUtil.resourceString(key);
	}

	public static PropertyDefinition getPropertyDefinition(String propName, EntityDefinition entity) {
		if (propName == null || propName.isEmpty()) {
			return null;
		}
		if (propName.contains(".")) {
			int indexOfDot = propName.indexOf('.');
			String objPropName = propName.substring(0, indexOfDot);
			String subPropPath = propName.substring(indexOfDot + 1, propName.length());

			PropertyDefinition property = entity.getProperty(objPropName);
			if (!(property instanceof ReferenceProperty)) {
				throw new IllegalArgumentException("path is invalid:" + objPropName + " is not ObjectReferenceProperty of " + entity.getName());
			}
			ReferenceProperty refProp = (ReferenceProperty) property;
			EntityDefinition refEntity = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class).get(refProp.getObjectDefinitionName());
			if (refEntity == null) {
				throw new IllegalArgumentException(objPropName + "'s Entity is not defined.");
			}
			PropertyDefinition refProperty = getPropertyDefinition(subPropPath, refEntity);
			if (refProperty == null) {
				throw new IllegalArgumentException(subPropPath + "'s Property is not defined.");
			}
			return refProperty;

		} else {
			PropertyDefinition property = entity.getProperty(propName);
			if (property == null) {
				throw new IllegalArgumentException(propName + "'s Property is not defined.");
			}
			return property;
		}
	}

	/**
	 * 画面表示用に仮想プロパティ用のプロパティ定義を作成
	 * @param property
	 * @return
	 */
	public static PropertyDefinition getPropertyDefinition(VirtualPropertyItem property) {
		return getVirtualPropertyDefinition(property);
	}

	/**
	 * ネストテーブルプロパティのプロパティ定義を取得
	 * 
	 * @param nestProperty ネストプロパティ
	 * @param entityDefinition ネストテーブルのEntity定義
	 * @return プロパティ定義
	 */
	public static PropertyDefinition getNestTablePropertyDefinition(NestProperty nestProperty, EntityDefinition entityDefinition) {
		// プロパティ定義があればそれを使う
		PropertyDefinition propertyDefinition = entityDefinition.getProperty(nestProperty.getPropertyName());
		if (propertyDefinition != null) {
			return propertyDefinition;
		}

		// プロパティ定義がない場合、仮想プロパティならプロパティ定義に変換する
		if (nestProperty.isVirtual()) {
			propertyDefinition = getVirtualPropertyDefinition(nestProperty);
			if (propertyDefinition == null) {
				// 変換できない場合は仮想プロパティに利用できないエディタを設定してるのでエラーにする
				throw new ApplicationException(
						GemResourceBundleUtil.resourceString("view.generic.EntityViewUtil.nestTableEditorErr", nestProperty.getPropertyName()));
			}
		}

		return propertyDefinition;
	}

	/**
	 * ネストテーブルのプロパティが仮想プロパティかどうか
	 * 
	 * @param nestProperty ネストプロパティ
	 * @param entityDefinition Entity定義
	 * @return ネストテーブルのプロパティが仮想プロパティかどうか
	 */
	public static boolean isVirtualNestProperty(NestProperty nestProperty, EntityDefinition entityDefinition) {
		return isVirtualNestProperty(nestProperty, entityDefinition.getProperty(nestProperty.getPropertyName()));
	}

	/**
	 * ネストテーブルのプロパティが仮想プロパティかどうか
	 * 
	 * @param nestProperty ネストプロパティ
	 * @param propertyDefinition プロパティ定義
	 * @return ネストテーブルのプロパティが仮想プロパティかどうか
	 */
	public static boolean isVirtualNestProperty(NestProperty nestProperty, PropertyDefinition propertyDefinition) {
		return nestProperty.isVirtual() && (propertyDefinition == null);
	}

	/**
	 * Editorに定義されたカスタムスタイル(表示、入力)を取得
	 *
	 * @param definitionName  Entity定義名
	 * @param scriptKey       Form/Sectionに設定されたScriptKey(EntityViewにてこのKEYでTemplateをキャッシュ)
	 * @param editorScriptKey Editorに設定されたScriptKey(Sectio単位のキャッシュにこのKEYでEditorのTemplateをキャッシュ)
	 * @param entity          対象Entityデータ(検索条件、新規登録などの場合はnullあり)
	 * @param propValue       対象Propertyデータ
	 * @return
	 */
	public static String getCustomStyle(String definitionName, String scriptKey, String editorScriptKey, Entity entity, Object propValue) {
		EntityViewManager evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
		return evm.getCustomStyle(definitionName, scriptKey, editorScriptKey, entity, propValue);
	}

	/**
	 * 指定のプロパティ名を持つ参照セクションの件数を取得します。
	 * @param view 詳細画面定義
	 * @param propName プロパティ名
	 * @return 参照セクションの件数
	 */
	public static long referenceSectionCount(DetailFormView view, String propName) {
		return getReferenceSectionList(new ArrayList<ReferenceSection>(), view.getSections()).stream()
				.filter(e -> propName.equals(e.getPropertyName()))
				.count();
	}

	public static List<ReferenceSection> getReferenceSectionList(List<ReferenceSection> sections, List<?> elem) {
		for (Object e : elem) {
			if (e instanceof DefaultSection) {
				getReferenceSectionList(sections, ((DefaultSection) e).getElements());
			} else if (e instanceof ReferenceSection) {
				sections.add((ReferenceSection) e);
			}
		}
		return sections;
	}

	public static List<Entity> sortByOrderProperty(List<Entity> entities, final String propertyName, boolean research) {
		if (entities == null || entities.isEmpty()) return entities;

		//表示順を検索
		List<Entity> target = null;
		if (research) {
			List<String> oidList = entities.stream().filter(e -> e.getOid() != null).map(e -> e.getOid()).collect(Collectors.toList());
			if (oidList.isEmpty()) {
				target = entities;
			} else {
				List<Entity> newEntities = entities.stream().filter(e -> e.getOid() == null).collect(Collectors.toList());
				Query query = new Query().select(Entity.OID, Entity.VERSION, propertyName)
						.from(entities.get(0).getDefinitionName())
						.where(new In(Entity.OID, oidList.toArray()));
				target = ManagerLocator.getInstance().getManager(EntityManager.class).searchEntity(query).getList();
				target.addAll(newEntities);
			}
		} else {
			target = entities;
		}

		return sortByOrderProperty(target, propertyName);
	}

	public static List<Entity> sortByOrderProperty(List<Entity> entities, final String propertyName) {
		if (entities == null || entities.isEmpty()) return entities;

		//表示順でソート、表示順がnullの場合は後ろに
		List<Entity> ret = entities.stream().map(e -> new SortInfo(e, propertyName))
				.sorted(Comparator.comparing(SortInfo::getSortValue, Comparator.nullsLast(Comparator.naturalOrder())))
				.map(s -> s.getEntity()).collect(Collectors.toList());
		return ret;
	}

	private static PropertyDefinition getVirtualPropertyDefinition(PropertyElement property) {
		PropertyDefinition definition = null;
		if (property.getEditor() instanceof BooleanPropertyEditor) {
			BooleanProperty prop = new BooleanProperty(property.getPropertyName());
			definition = prop;
		} else if (property.getEditor() instanceof DatePropertyEditor) {
			DateProperty prop = new DateProperty(property.getPropertyName());
			definition = prop;
		} else if (property.getEditor() instanceof TimePropertyEditor) {
			TimeProperty prop = new TimeProperty(property.getPropertyName());
			definition = prop;
		} else if (property.getEditor() instanceof TimestampPropertyEditor) {
			DateTimeProperty prop = new DateTimeProperty(property.getPropertyName());
			definition = prop;
		} else if (property.getEditor() instanceof DecimalPropertyEditor) {
			DecimalProperty prop = new DecimalProperty(property.getPropertyName());
			definition = prop;
		} else if (property.getEditor() instanceof IntegerPropertyEditor) {
			IntegerProperty prop = new IntegerProperty(property.getPropertyName());
			definition = prop;
		} else if (property.getEditor() instanceof FloatPropertyEditor) {
			FloatProperty prop = new FloatProperty(property.getPropertyName());
			definition = prop;
		} else if (property.getEditor() instanceof SelectPropertyEditor) {
			SelectProperty prop = new SelectProperty(property.getPropertyName());
			definition = prop;
		} else if (property.getEditor() instanceof StringPropertyEditor) {
			StringProperty prop = new StringProperty(property.getPropertyName());
			definition = prop;
		} else if (property.getEditor() instanceof TemplatePropertyEditor) {
			//Template利用の場合はPropertyDefinitionの型を想定できないのでStringで生成
			StringProperty prop = new StringProperty(property.getPropertyName());
			definition = prop;
		} else if (property.getEditor() instanceof UserPropertyEditor) {
			StringProperty prop = new StringProperty(property.getPropertyName());
			definition = prop;
		}

		// 仮想プロパティで利用できないエディタが設定されてる場合は変換対象外
		if (definition == null) {
			return null;
		}

		definition.setDisplayName(property.getDisplayLabel());
		definition.setLocalizedDisplayNameList(property.getLocalizedDisplayLabelList());
		definition.setMultiplicity(1);
		definition.setUpdatable(true);
		return definition;
	}

	private static class SortInfo {
		private Entity entity;
		private Integer sortValue;
		public SortInfo(Entity entity, String propertyName) {
			this.entity = entity;
			this.sortValue = toInteger(entity.getValue(propertyName));
		}
		public Entity getEntity() {
			return entity;
		}
		public Integer getSortValue() {
			return sortValue;
		}
		private Integer toInteger(Object val) {
			if (val == null) return null;
			if (val instanceof Integer) {
				return (Integer) val;
			} else if (val instanceof Long) {
				return ((Long) val).intValue();
			} else if (val instanceof Float) {
				return ((Float) val).intValue();
			} else if (val instanceof Double) {
				return ((Double) val).intValue();
			} else if (val instanceof BigDecimal) {
				return ((BigDecimal) val).intValue();
			}
			return -1; // 数値以外
		}
	}

}
