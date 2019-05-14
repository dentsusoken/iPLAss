/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.server.metadata.rpc.refrect;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlSeeAlso;

import org.iplass.adminconsole.server.base.rpc.util.AuthUtil;
import org.iplass.adminconsole.shared.metadata.dto.Name;
import org.iplass.adminconsole.shared.metadata.dto.refrect.AnalysisListDataResult;
import org.iplass.adminconsole.shared.metadata.dto.refrect.AnalysisResult;
import org.iplass.adminconsole.shared.metadata.dto.refrect.FieldInfo;
import org.iplass.adminconsole.shared.metadata.dto.refrect.RefrectableInfo;
import org.iplass.adminconsole.shared.metadata.rpc.refrect.RefrectionService;
import org.iplass.adminconsole.view.annotation.IgnoreField;
import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.adminconsole.view.annotation.Refrectable;
import org.iplass.adminconsole.view.annotation.generic.EntityViewField;
import org.iplass.mtp.impl.view.generic.EntityViewService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.server.rpc.XsrfProtectedServiceServlet;

public class RefrectionServiceImpl extends XsrfProtectedServiceServlet implements RefrectionService {

	private static final long serialVersionUID = 5813243665867941856L;
	private static final Logger logger = LoggerFactory.getLogger(RefrectionServiceImpl.class);

	private EntityViewService evs = ServiceRegistry.getRegistry().getService(EntityViewService.class);

	/**
	 * インターフェースクラスを解析して、フィールド情報を各フィールドの値を取得する。
	 *
	 * @param className インターフェースクラスのクラス名
	 * @param value     インターフェースクラス
	 * @return
	 */
	@Override
	public AnalysisResult analysis(int tenantId, String className, Refrectable value) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(),
				this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<AnalysisResult>() {

					@Override
					public AnalysisResult call() {
						AnalysisResult ret = new AnalysisResult();
						ret.setFields(getFieldInfo(className));
						ret.setValueMap(getFieldValues(className, value, ret.getFields()));
						return ret;
					}
				});

	}

	/**
	 * インターフェースクラスの配列を解析して、フィールド情報と各フィールドの値を取得する。
	 *
	 * @param className
	 * @param valueList
	 * @return
	 */
	@Override
	public AnalysisListDataResult analysisListData(int tenantId, String className, List<Refrectable> valueList) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(),
				this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<AnalysisListDataResult>() {

					@Override
					public AnalysisListDataResult call() {
						AnalysisListDataResult ret = new AnalysisListDataResult();
						ret.setFields(getFieldInfo(className));
						ret.setRefrectables(getListFieldValues(valueList));
						return ret;
					}
				});
	}

	/**
	 * インターフェースクラスのインスタンスを生成する。
	 *
	 * @param className
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Refrectable create(int tenantId, String className) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(),
				this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<Refrectable>() {

					@Override
					public Refrectable call() {
						Refrectable instance = null;

						try {
							Class<? extends Refrectable> cls = (Class<? extends Refrectable>) Class.forName(className);
							instance = cls.newInstance();

							FieldInfo[] infos = getFieldInfo(cls);
							for (FieldInfo info : infos) {
								if (info.getInputType() == InputType.ENUM) {
									// enumの場合は初期値(最初の値)を入れとく
									setDefaultEnumValue(info, cls, instance);
								}
							}

						} catch (ClassNotFoundException e) {
							logger.error(e.getMessage(), e);
						} catch (InstantiationException e) {
							logger.error(e.getMessage(), e);
						} catch (IllegalAccessException e) {
							logger.error(e.getMessage(), e);
						}

						return instance;
					}
				});
	}

	/**
	 * インターフェースクラスのフィールドを更新する。
	 *
	 * @param value    インターフェースクラス
	 * @param valueMap インターフェースクラスの更新データ
	 * @return
	 */
	@Override
	public Refrectable update(int tenantId, Refrectable value, Map<String, Serializable> valueMap) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(),
				this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<Refrectable>() {

					@Override
					public Refrectable call() {
						Class<?> cls = value.getClass();

						update(cls, value, valueMap);

						return value;
					}
				});

	}

	@Override
	public Name[] getSubClass(int tenantId, final String rootClassName) {
		return AuthUtil.authCheckAndInvoke(getServletContext(), this.getThreadLocalRequest(),
				this.getThreadLocalResponse(), tenantId, new AuthUtil.Callable<Name[]>() {

					@Override
					public Name[] call() {
						List<Name> nameList = new ArrayList<Name>();
						try {
							Class<?> cls = Class.forName(rootClassName);
							int mod = cls.getModifiers();
							if (!Modifier.isAbstract(mod) && !Modifier.isInterface(mod)) {
								nameList.add(new Name(cls.getName(), cls.getSimpleName()));
							}
							nameList.addAll(getXmlSeeAlsoClass(cls));
						} catch (ClassNotFoundException e) {
							// throw e;
							logger.error(e.getMessage(), e);
						}

						return nameList.toArray(new Name[nameList.size()]);
					}
				});

	}

	/**
	 * instanceのEnumフィールドに初期値を設定する。
	 *
	 * @param info
	 * @param cls
	 * @param instance
	 */
	private void setDefaultEnumValue(FieldInfo info, Class<?> cls, Refrectable instance) {
		if (cls.getSuperclass() != null && cls.getSuperclass() instanceof Object) {
			setDefaultEnumValue(info, cls.getSuperclass(), instance);
		}

		try {
			Field field = cls.getDeclaredField(info.getName());
			field.setAccessible(true);
//			field.set(instance, info.getEnumValues()[0]);
			field.set(instance, getFirstEnumValue(info));
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(), e);
		} catch (SecurityException e) {
			logger.error(e.getMessage(), e);
		} catch (NoSuchFieldException e) {
			// logger.error(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
		}
	}

	private Object getFirstEnumValue(FieldInfo info) throws ClassNotFoundException {
		Class<?> cls = Class.forName(info.getEnumClassName());
		for (Object constants : cls.getEnumConstants()) {
			// Deprecatedされてない最初の要素を返す
			try {
				if (!cls.getField(constants.toString()).isAnnotationPresent(Deprecated.class)) {
					return constants;
				}
			} catch (SecurityException e) {
				logger.error(e.getMessage(), e);
			} catch (NoSuchFieldException e) {
				// logger.error(e.getMessage(), e);
			}
		}
		return null;
	}

	/**
	 * 型名から各フィールドのメタデータを取得。
	 *
	 * @param className
	 * @return
	 */
	private FieldInfo[] getFieldInfo(String className) {
		try {
			Class<?> cls = Class.forName(className);
			return getFieldInfo(cls);
		} catch (ClassNotFoundException e) {
			// throw e;
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 型から各フィールドのメタデータを取得。
	 *
	 * @param cls
	 * @return
	 */
	private FieldInfo[] getFieldInfo(Class<?> cls) {
		List<String> ignoreFields = new ArrayList<String>();
		IgnoreField ignoreField = cls.getAnnotation(IgnoreField.class);
		if (ignoreField != null) {
			ignoreFields.addAll(Arrays.asList(ignoreField.value()));
		}

		List<FieldInfo> list = new ArrayList<FieldInfo>();
		getFieldInfo(list, cls, ignoreFields);

		//必須を先頭に移動
		List<FieldInfo> result = new ArrayList<FieldInfo>();
		List<FieldInfo> required = new ArrayList<FieldInfo>();
		for (FieldInfo info : list) {
			if (info.isRequired()) {
				required.add(info);
			} else {
				result.add(info);
			}
		}
		result.addAll(0, required);

		return result.toArray(new FieldInfo[result.size()]);
	}

	/**
	 * 型から各フィールドのメタデータを取得。
	 *
	 * @param list
	 * @param cls
	 */
	private void getFieldInfo(List<FieldInfo> list, Class<?> cls, List<String> ignoreFields) {
		if (cls.getSuperclass() != null && cls.getSuperclass() instanceof Object) {
			getFieldInfo(list, cls.getSuperclass(), ignoreFields);
		}

		for (Field field : cls.getDeclaredFields()) {
			MetaFieldInfo annotation = field.getAnnotation(MetaFieldInfo.class);
			if (annotation != null && !ignoreFields.contains(field.getName())) {
				FieldInfo info = new FieldInfo();
				info.setName(field.getName());
				info.setDisplayName(annotation.displayName());
//				info.setDisplayName(getDisplayName(annotation));
				info.setDisplayNameKey(annotation.displayNameKey());
				info.setInputType(annotation.inputType());
				info.setRequired(annotation.required());
				info.setRangeCheck(annotation.rangeCheck());
				info.setMaxRange(annotation.maxRange());
				info.setMinRange(annotation.minRange());
				info.setMultiple(annotation.multiple());
				info.setReferenceClassName(annotation.referenceClass().getName());
				if (annotation.fixedReferenceClass().length > 0) {
					List<Name> nameList = new ArrayList<Name>();
					for (Class<?> fixedReferenceClass : annotation.fixedReferenceClass()) {
						int mod = fixedReferenceClass.getModifiers();
						if (!Modifier.isAbstract(mod) && !Modifier.isInterface(mod)) {
							nameList.add(new Name(fixedReferenceClass.getName(), fixedReferenceClass.getSimpleName()));
						}
					}
					info.setFixedReferenceClass(nameList.toArray(new Name[] {}));
				}
				if (annotation.inputType() == InputType.ENUM && annotation.enumClass() != null
						&& annotation.enumClass().isEnum()) {
//					info.setEnumValues((Serializable[]) annotation.enumClass().getEnumConstants());
					info.setEnumClassName(annotation.enumClass().getName());
					List<Serializable> enumValues = new ArrayList<Serializable>();
					for (Object constants : annotation.enumClass().getEnumConstants()) {
						// Deprecatedなメソッドは選択させない
						try {
							if (!annotation.enumClass().getField(constants.toString())
									.isAnnotationPresent(Deprecated.class)) {
								enumValues.add((Serializable) constants);
							}
						} catch (SecurityException e) {
							logger.error(e.getMessage(), e);
						} catch (NoSuchFieldException e) {
							// logger.error(e.getMessage(), e);
						}
					}
					Object[] array = (Object[]) Array.newInstance(annotation.enumClass(), enumValues.size());
					info.setEnumValues((Serializable[]) enumValues.toArray(array));
				}
				info.setMode(annotation.mode());
				info.setDescription(annotation.description());
//				info.setDescription(getDescription(annotation));
				info.setDescriptionKey(annotation.descriptionKey());
				info.setMultiLangFieldName(annotation.multiLangField());
				info.setUseReferenceType(annotation.useReferenceType());
				info.setEntityDefinitionName(annotation.entityDefinitionName());
				info.setDeprecated(annotation.deprecated());

				if (evs.isFilterSettingProperty()) {
					// EntityViewに特化した情報の取得
					EntityViewField viewAnnotation = field.getAnnotation(EntityViewField.class);
					if (viewAnnotation != null) {
						info.setEntityViewReferenceType(viewAnnotation.referenceTypes());
						info.setOverrideTriggerType(viewAnnotation.overrideTriggerType());
					}
				}

				list.add(info);
			}
		}
	}

//	private String getDisplayName(MetaFieldInfo annotation) {
//		if (annotation.displayNameKey() == null || annotation.displayNameKey().isEmpty()) {
//			return annotation.displayName();
//		}
//		String displayName = TemplateUtil.getString(annotation.displayNameKey());
//		if (displayName != null && !displayName.isEmpty()) {
//			return displayName;
//		}
//		return annotation.displayName();
//	}
//
//	private String getDescription(MetaFieldInfo annotation) {
//		if (annotation.descriptionKey() == null || annotation.descriptionKey().isEmpty()) {
//			return annotation.description();
//		}
//		String description = TemplateUtil.getString(annotation.descriptionKey());
//		if (description != null && !description.isEmpty()) {
//			return description;
//		}
//		return annotation.description();
//	}

	/**
	 *
	 * @param valueList
	 * @return
	 */
	private List<RefrectableInfo> getListFieldValues(List<Refrectable> valueList) {
		List<RefrectableInfo> ret = new ArrayList<RefrectableInfo>();
		for (Refrectable value : valueList) {
			RefrectableInfo info = new RefrectableInfo();
			info.setValue(value);
			info.setValueMap(getFieldValues(value.getClass(), value));
			ret.add(info);
		}
		return ret;
	}

	/**
	 * インターフェースクラスのフィールドの値を取得。
	 *
	 * @param className
	 * @param object
	 * @param fields
	 * @return
	 */
	private Map<String, Serializable> getFieldValues(Class<?> cls, Refrectable object) {
		FieldInfo[] fields = getFieldInfo(cls);
		return getFieldValues(cls, object, fields);
	}

	/**
	 * インターフェースクラスのフィールドの値を取得。
	 *
	 * @param className
	 * @param object
	 * @param fields
	 * @return
	 */
	private Map<String, Serializable> getFieldValues(String className, Refrectable object, FieldInfo[] fields) {
		try {
			Class<?> cls = Class.forName(className);
			return getFieldValues(cls, object, fields);
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * インターフェースクラスのフィールドの値を取得。
	 *
	 * @param cls
	 * @param object
	 * @param fields
	 * @return
	 */
	private Map<String, Serializable> getFieldValues(Class<?> cls, Refrectable object, FieldInfo[] fields) {
		Map<String, Serializable> valueMap = new HashMap<String, Serializable>();
		if (object == null || cls.getName().equals(Object.class.getName()))
			return valueMap;

		if (cls.getSuperclass() != null && cls.getSuperclass() instanceof Object) {
			valueMap.putAll(getFieldValues(cls.getSuperclass(), object, fields));
		}

		for (FieldInfo info : fields) {
			Serializable value = getFieldValue(cls, object, info.getName());
			if (value != null) {
				valueMap.put(info.getName(), value);
			}
		}
		return valueMap;
	}

	/**
	 * 指定フィールドの値を取得。
	 *
	 * @param cls
	 * @param object
	 * @param name
	 * @return
	 */
	private Serializable getFieldValue(Class<?> cls, Refrectable object, String name) {
		try {
			Field field = cls.getDeclaredField(name);
			field.setAccessible(true);
			return (Serializable) field.get(object);
		} catch (SecurityException e) {
			logger.error(e.getMessage(), e);
		} catch (NoSuchFieldException e) {
			// 継承しているフィールドは見つからない
			// →親のフィールドを見に行くのでエラー処理は不要
			// logger.error(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * インターフェースクラスのフィールドを更新する。
	 *
	 * @param cls
	 * @param value
	 * @param valueMap
	 */
	private void update(Class<?> cls, Refrectable value, Map<String, Serializable> valueMap) {
		if (cls.getSuperclass() != null && cls.getSuperclass() instanceof Object) {
			update(cls.getSuperclass(), value, valueMap);
		}

		FieldInfo[] fields = getFieldInfo(cls);
		for (FieldInfo info : fields) {
			if (valueMap.containsKey(info.getName())) {
				try {
					Field field = cls.getDeclaredField(info.getName());
					field.setAccessible(true);
					if (info.getInputType() == InputType.ENUM) {
						Method valueOf = field.getType().getMethod("valueOf", String.class);
						if (valueMap.get(info.getName()) != null) {
							field.set(value, valueOf.invoke(value, valueMap.get(info.getName())));
						} else {
							field.set(value, null);
						}
					} else {
						field.set(value, valueMap.get(info.getName()));
					}
				} catch (SecurityException e) {
					logger.error(e.getMessage(), e);
				} catch (NoSuchFieldException e) {
					// 親クラスのフィールドなどが見つからない場合にここにくる
				} catch (IllegalArgumentException e) {
					logger.error(e.getMessage(), e);
				} catch (IllegalAccessException e) {
					logger.error(e.getMessage(), e);
				} catch (NoSuchMethodException e) {
					logger.error(e.getMessage(), e);
				} catch (InvocationTargetException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}

	private List<Name> getXmlSeeAlsoClass(Class<?> cls) {
		List<Name> nameList = new ArrayList<Name>();
		XmlSeeAlso annotation = cls.getAnnotation(XmlSeeAlso.class);
		if (annotation != null) {
			for (Class<?> seeClass : annotation.value()) {
				int mod = seeClass.getModifiers();
				if (!Modifier.isAbstract(mod) && !Modifier.isInterface(mod)) {
					nameList.add(new Name(seeClass.getName(), seeClass.getSimpleName()));
				}
				nameList.addAll(getXmlSeeAlsoClass(seeClass));
			}
		}
		return nameList;
	}

}
