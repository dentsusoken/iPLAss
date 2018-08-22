/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.server.base.rpc.i18n;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.adminconsole.server.base.i18n.AdminResourceBundleUtil;
import org.iplass.adminconsole.shared.base.dto.i18n.MultiLangFieldInfo;
import org.iplass.mtp.definition.LocalizedStringDefinition;
import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.entity.definition.LocalizedSelectValueDefinition;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * 多言語データの生成ロジック。
 * </p>
 */
public class LangDataLogic implements Service {

	private static final Logger logger = LoggerFactory.getLogger(LangDataLogic.class);

	@Override
	public void init(Config config) {
	}

	@Override
	public void destroy() {
	}

	private static final String DEFAULT_KEY_NAME = "defaultLang";

	public void createMultiLangInfo(Map<String, List<LocalizedStringDefinition>> listGridFieldsMap, Class<?> cls, Object definition, String parentItemKeyName) {
		Class<?> superCls = cls.getSuperclass();
		if (superCls != null) {
			createMultiLangInfo(listGridFieldsMap, superCls, definition, parentItemKeyName);
		}
		createMultiLangItemInfo(listGridFieldsMap, cls, definition, parentItemKeyName);
	}

	private void createMultiLangItemInfo(Map<String, List<LocalizedStringDefinition>> listGridFieldsMap, Class<?> cls, Object definition, String parentItemKeyName) {

		try {
			for (Field field : cls.getDeclaredFields()) {
				MultiLang annotation = field.getAnnotation(MultiLang.class);
				if (annotation != null) {
					MultiLangInfo info = new MultiLangInfo(field, annotation);
					createMultiLangItemInfo(listGridFieldsMap, cls, definition, parentItemKeyName, info);
				}
			}

			// TopViewParts系がgetter, setterしかないため、メソッドについてもアノテーションを有効にする対応
			for (Method method : cls.getDeclaredMethods()) {
				MultiLang annotation = method.getAnnotation(MultiLang.class);
				if (annotation != null) {
					MultiLangInfo info = new MultiLangInfo(null, annotation);
					createMultiLangItemInfo(listGridFieldsMap, cls, definition, parentItemKeyName, info);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void createMultiLangItemInfo(Map<String, List<LocalizedStringDefinition>> listGridFieldsMap, Class<?> cls, Object definition, String parentItemKeyName, MultiLangInfo annotation) throws Exception {

		if (annotation == null) return;

		String itemKey = createItemKeyName(annotation, cls, definition, parentItemKeyName);

		if (annotation.isMultiLangValue()) {

			// defaultの値を取得してlocalizeStringへ変換
			String defaultLang = (String) cls.getMethod(annotation.itemGetter()).invoke(definition, new Object[]{});

			@SuppressWarnings("unchecked")
			List<LocalizedStringDefinition> localizedDisplayNameList = (List<LocalizedStringDefinition>) cls.getMethod(annotation.multiLangGetter()).invoke(definition, new Object[]{});

			LocalizedStringDefinition defaultDefinition = new LocalizedStringDefinition();
			defaultDefinition.setLocaleName(DEFAULT_KEY_NAME);
			defaultDefinition.setStringValue(defaultLang);

			// localizedDisplayNameListはnullの可能性あり
			if (localizedDisplayNameList == null) {
				localizedDisplayNameList = new ArrayList<LocalizedStringDefinition>();
			}
			localizedDisplayNameList.add(defaultDefinition);

			// 取得した多言語情報をgird表示する為にMapへput
			listGridFieldsMap.put(itemKey, localizedDisplayNameList);

		} else {

			Object child = cls.getMethod(annotation.itemGetter()).invoke(definition, new Object[]{});

			// topsection1のようにnullの可能性あり、その場合は次の処理へ
			if (child == null) {
				return;
			}

			if (annotation.isSelectValue()) {

				@SuppressWarnings("unchecked")
				List<SelectValue> selectValueList = (List<SelectValue>) child;
				for (SelectValue selectValue : selectValueList) {
					List<LocalizedStringDefinition> localizedDisplayNameList = new ArrayList<LocalizedStringDefinition>();
					LocalizedStringDefinition defaultDefinition = new LocalizedStringDefinition();
					defaultDefinition.setLocaleName(DEFAULT_KEY_NAME);
					defaultDefinition.setStringValue(selectValue.getDisplayName());
					localizedDisplayNameList.add(defaultDefinition);

					@SuppressWarnings("unchecked")
					List<LocalizedSelectValueDefinition> localizedSelectValueDefinitionList = (List<LocalizedSelectValueDefinition>) cls.getMethod(annotation.multiLangGetter()).invoke(definition, new Object[]{});
					for (LocalizedSelectValueDefinition localizedSelectValueDefinition : localizedSelectValueDefinitionList) {
						// localizedSelectValueDefinitionはnullの可能性あり
						if (localizedSelectValueDefinition != null && localizedSelectValueDefinition.getSelectValueList() != null) {
							for (SelectValue localizedSelectValue : localizedSelectValueDefinition.getSelectValueList()) {
								if (localizedSelectValue.getValue().equals(selectValue.getValue())) {
									LocalizedStringDefinition localizedDefinition = new LocalizedStringDefinition();
									localizedDefinition.setLocaleName(localizedSelectValueDefinition.getLocaleName());
									localizedDefinition.setStringValue(localizedSelectValue.getDisplayName());
									localizedDisplayNameList.add(localizedDefinition);
								}
							}
						}
					}
					listGridFieldsMap.put(itemKey + "." + selectValue.getValue(), localizedDisplayNameList);
				}

			} else {
				if (child instanceof List) {

					int cnt = 0;
					for (Object o : (List<?>) child) {

						if (o.getClass().getSuperclass() != null) {
							createMultiLangInfo(listGridFieldsMap, o.getClass().getSuperclass(), o, itemKey + cnt);
						}
						createMultiLangItemInfo(listGridFieldsMap, o.getClass(), o, itemKey + cnt);
						cnt ++;
					}
				} else if (child instanceof Map<?,?>) {

					int cnt = 0;

					Map<?,?> temp = (Map<?,?>) child;
					for(Map.Entry<?,?> e : temp.entrySet()) {

						if (e.getValue().getClass().getSuperclass() != null) {
							createMultiLangInfo(listGridFieldsMap, e.getValue().getClass().getSuperclass(), e.getValue(), itemKey + cnt);
						}
						createMultiLangItemInfo(listGridFieldsMap, e.getValue().getClass(), e.getValue(), itemKey + cnt);
						cnt ++;
					}
				} else {
					// List以外の処理。
					if (child.getClass().getSuperclass() != null) {
						createMultiLangInfo(listGridFieldsMap, child.getClass().getSuperclass(), child, itemKey);
					}
					createMultiLangItemInfo(listGridFieldsMap, child.getClass(), child, itemKey);
				}
			}
		}
	}

	// 更新処理用
	public void createDefinitionInfo(Class<?> cls, Object definition, Map<String, MultiLangFieldInfo> updateDefinitionInfo, String parentItemKeyName) {
		Class<?> superCls = cls.getSuperclass();
		if (superCls != null) {
			createDefinitionInfo(superCls, definition, updateDefinitionInfo, parentItemKeyName);
		}
		createDefinition(cls, definition, updateDefinitionInfo, parentItemKeyName);
	}

	private void createDefinition(Class<?> cls, Object definition, Map<String, MultiLangFieldInfo> updateDefinitionInfo, String parentItemKeyName) {
		try {
			for (Field field : cls.getDeclaredFields()) {
				MultiLang annotation = field.getAnnotation(MultiLang.class);
				if (annotation != null) {
					MultiLangInfo info = new MultiLangInfo(field, annotation);
					createDefinition(cls, definition, updateDefinitionInfo, parentItemKeyName, info);
				}
			}

			// TopViewParts系がgetter, setterしかないため、メソッドについてもアノテーションを有効にする対応
			for (Method method : cls.getDeclaredMethods()) {
				MultiLang annotation = method.getAnnotation(MultiLang.class);
				if (annotation != null) {
					MultiLangInfo info = new MultiLangInfo(null, annotation);
					createDefinition(cls, definition, updateDefinitionInfo, parentItemKeyName, info);
				}
			}
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			logger.error(e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			logger.error(e.getMessage(), e);
		} catch (SecurityException e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void createDefinition(Class<?> cls, Object definition, Map<String, MultiLangFieldInfo> updateDefinitionInfo, String parentItemKeyName, MultiLangInfo annotation) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		if (annotation == null) return;

		String itemKey = createItemKeyName(annotation, cls, definition, parentItemKeyName);

		if (annotation.isMultiLangValue()) {
			// このitemKeyにマッチするupdateDefinitionInfoがあればその値で上書き
			for (Map.Entry<String, MultiLangFieldInfo> entry : updateDefinitionInfo.entrySet()) {

				String editItemKey = entry.getKey();

				if (editItemKey.equals(itemKey)) {

					MultiLangFieldInfo multiLangFieldInfo = entry.getValue();

					// default
					if (annotation.isRequired() && (multiLangFieldInfo.getDefaultString() == null || multiLangFieldInfo.getDefaultString().isEmpty())) {
						throw new RuntimeException(resourceString("inputError", editItemKey));
					}
					String itemSetter = annotation.itemSetter();
					cls.getMethod(itemSetter, new Class[]{String.class}).invoke(definition, new Object[]{multiLangFieldInfo.getDefaultString()});

					// multilang
					String multiLangSetter = annotation.multiLangSetter();
					cls.getMethod(multiLangSetter, new Class[]{List.class}).invoke(definition, new Object[]{multiLangFieldInfo.getLocalizedStringList()});

					continue;
				}
			}

		} else {

			Object child = cls.getMethod(annotation.itemGetter()).invoke(definition, new Object[]{});

			// topsection1のようにnullの可能性あり、その場合は次の処理へ
			if (child == null) {
				return;
			}

			if (annotation.isSelectValue()) {

				@SuppressWarnings("unchecked")
				List<SelectValue> selectValueList = (List<SelectValue>) child;
				for (SelectValue selectValue : selectValueList) {

					String selectValueItemKey = itemKey + "." + selectValue.getValue();

					for (Map.Entry<String, MultiLangFieldInfo> entry : updateDefinitionInfo.entrySet()) {
						String editItemKey = entry.getKey();

						if (editItemKey.equals(selectValueItemKey)) {
							MultiLangFieldInfo multiLangFieldInfo = entry.getValue();

							// default
							if (multiLangFieldInfo.getDefaultString() == null || multiLangFieldInfo.getDefaultString().isEmpty()) {
								throw new RuntimeException(resourceString("inputError", editItemKey));
							}
							selectValue.setDisplayName(multiLangFieldInfo.getDefaultString());

							// multi
							@SuppressWarnings("unchecked")
							List<LocalizedSelectValueDefinition> localizedSelectValueDefinitionList = (List<LocalizedSelectValueDefinition>) cls.getMethod(annotation.multiLangGetter()).invoke(definition, new Object[]{});
							for (LocalizedStringDefinition editLocalizedString : multiLangFieldInfo.getLocalizedStringList()) {
								String editlocale = editLocalizedString.getLocaleName();
								for (LocalizedSelectValueDefinition lsvd : localizedSelectValueDefinitionList) {
									if (editlocale.equals(lsvd.getLocaleName())) {

										if (lsvd.getSelectValueList() != null) {
											boolean isExistSelectValue = false;
											for (SelectValue sv : lsvd.getSelectValueList()) {
												if (sv.getValue().equals(selectValue.getValue())) {
													sv.setDisplayName(editLocalizedString.getStringValue());
													isExistSelectValue = true;
												}
											}

											if (!isExistSelectValue) {
												SelectValue sv = new SelectValue();
												sv.setDisplayName(editLocalizedString.getStringValue());
												sv.setValue(selectValue.getValue());
												lsvd.getSelectValueList().add(sv);
											}
										} else {
											if (editLocalizedString.getStringValue() != null) {
												SelectValue sv = new SelectValue();
												sv.setDisplayName(editLocalizedString.getStringValue());
												sv.setValue(selectValue.getValue());

												List<SelectValue> localizedSelectValueListSelectValue = new ArrayList<SelectValue>();
												localizedSelectValueListSelectValue.add(sv);
												lsvd.setSelectValueList(localizedSelectValueListSelectValue);
											}
										}
									}
								}
							}
						}
					}
				}
			} else {
				if (child instanceof List) {

					int cnt = 0;
					for (Object o : (List<?>) child) {

						if (o.getClass().getSuperclass() != null) {
							createDefinitionInfo(o.getClass().getSuperclass(), o, updateDefinitionInfo, itemKey + cnt);
						}
						createDefinition(o.getClass(), o, updateDefinitionInfo, itemKey + cnt);
						cnt ++;
					}
				} else if (child instanceof Map<?,?>) {

					int cnt = 0;

					Map<?,?> temp = (Map<?,?>) child;
					for(Map.Entry<?,?> e : temp.entrySet()) {

						if (e.getValue().getClass().getSuperclass() != null) {
							createDefinitionInfo(e.getValue().getClass().getSuperclass(), e.getValue(), updateDefinitionInfo, itemKey + cnt);
						}
						createDefinition(e.getValue().getClass(), e.getValue(), updateDefinitionInfo, itemKey + cnt);
						cnt ++;
					}
				} else {
					// List以外の処理。
					if (child.getClass().getSuperclass() != null) {
						createDefinitionInfo(child.getClass().getSuperclass(), child, updateDefinitionInfo, itemKey);
					}
					createDefinition(child.getClass(), child, updateDefinitionInfo, itemKey);
				}
			}
		}
	}

	private String createItemKeyName(MultiLangInfo annotation, Class<?> cls, Object definition, String parentItemKeyName) {

		String itemKey = annotation.itemKey();
		try {
			String nameGetter = annotation.itemNameGetter();
			if (!"".equals(nameGetter)) {
				if (annotation.isUseSuperForItemName()) {
					String name = (String) cls.getSuperclass().getMethod(nameGetter).invoke(definition, new Object[] {});
					itemKey = name + "." + itemKey;
				} else {
					String name = (String) cls.getMethod(nameGetter).invoke(definition, new Object[] {});
					itemKey = name + "." + itemKey;
				}
			}

			// rootnodeの場合のみnull
			if (parentItemKeyName != null) {
				if (itemKey.equals("")) {
					itemKey = parentItemKeyName;
				} else {
					itemKey = parentItemKeyName + "." + itemKey;
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return itemKey;
	}

	/**
	 * MultiLangアノテーションのラッパー
	 * <p>
	 * 省略されたitemKeyやgetter、setterをFieldの情報から補完する。
	 * メソッドに指定されたアノテーションには利用できないので注意すること。
	 * </p>
	 */
	private static class MultiLangInfo {

		private static final String DEFAULT_ANNOTATION_VALUE = "";
		private static final String PREFIX_GET = "get";
		private static final String PREFIX_SET = "set";
		private static final String PREFIX_MULTI_GET = "getLocalized";
		private static final String PREFIX_MULTI_SET = "setLocalized";
		private static final String SUFFIX_MULTI_GET = "List";
		private static final String SUFFIX_MULTI_SET = "List";

		private Field field;

		private MultiLang annotation;

		public MultiLangInfo(Field field, MultiLang annotation) {
			this.field = field;
			this.annotation = annotation;
		}

		/** 項目のnameを取得するGetterMethod名 */
		public String itemNameGetter() {
			return annotation.itemNameGetter();
		}

		/** 親クラスの名前をキー名に使うかどうか */
		public boolean isUseSuperForItemName() {
			return annotation.isUseSuperForItemName();
		}

		/** ListやMap等ではなく、この項目自体が多言語項目かどうか */
		public boolean isMultiLangValue() {
			return annotation.isMultiLangValue();
		}

		/** 多言語項目のキー名 */
		public String itemKey() {
			if (DEFAULT_ANNOTATION_VALUE.equals(annotation.itemKey()) && field != null) {
				//省略時はフィールド名を利用
				return field.getName();
			} else {
				return annotation.itemKey();
			}
		}

		/** 多言語項目のGetterMethod名 */
		public String itemGetter() {
			if (DEFAULT_ANNOTATION_VALUE.equals(annotation.itemGetter())) {
				//省略時はgetフィールド名を返す
				return PREFIX_GET + StringUtil.capitalize(itemKey());
			} else {
				return annotation.itemGetter();
			}
		}

		/** 多言語項目のSetterMethod名 */
		public String itemSetter() {
			if (DEFAULT_ANNOTATION_VALUE.equals(annotation.itemSetter())) {
				//省略時はsetフィールド名を返す
				return PREFIX_SET + StringUtil.capitalize(itemKey());
			} else {
				return annotation.itemSetter();
			}
		}

		/** 多言語項目のLocalizedListのGetterMethod名 */
		public String multiLangGetter() {
			if (DEFAULT_ANNOTATION_VALUE.equals(annotation.multiLangGetter())) {
				//省略時はgetLocalizedフィールド名Listを返す
				return PREFIX_MULTI_GET + StringUtil.capitalize(itemKey()) + SUFFIX_MULTI_GET;
			} else {
				return annotation.multiLangGetter();
			}
		}

		/** 多言語項目のLocalizedListのSetterMethod名 */
		public String multiLangSetter() {
			if (DEFAULT_ANNOTATION_VALUE.equals(annotation.multiLangSetter())) {
				//省略時はsetLocalizedフィールド名Listを返す
				return PREFIX_MULTI_SET + StringUtil.capitalize(itemKey()) + SUFFIX_MULTI_SET;
			} else {
				return annotation.multiLangSetter();
			}
		}

		/** 多言語項目がSelectValueかどうか */
		public boolean isSelectValue() {
			return annotation.isSelectValue();
		}

		/** 多言語項目のデフォルト言語値が入力必須項目かどうか */
		public boolean isRequired() {
			return annotation.isRequired();
		}

	}

	private static String resourceString(String suffix, Object... arguments) {
		return AdminResourceBundleUtil.resourceString("i18n.LangDataLogic." + suffix, arguments);
	}

}
