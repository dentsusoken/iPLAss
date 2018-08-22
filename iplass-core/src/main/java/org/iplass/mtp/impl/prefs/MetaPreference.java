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

package org.iplass.mtp.impl.prefs;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.definition.DefinableMetaData;
import org.iplass.mtp.impl.metadata.BaseMetaDataRuntime;
import org.iplass.mtp.impl.metadata.BaseRootMetaData;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.script.GroovyScriptEngine;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.impl.util.ConvertUtil;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.prefs.Preference;
import org.iplass.mtp.prefs.PreferenceAware;
import org.iplass.mtp.prefs.PreferenceSet;

@XmlSeeAlso(MetaPreferenceSet.class)
public class MetaPreference extends BaseRootMetaData implements DefinableMetaData<Preference> {
	private static final long serialVersionUID = -897325395196986612L;

	static final String VALUE_KEY = "value";

	private String value;
	private String runtimeClassName;

	public static MetaPreference newMeta(Preference def) {
		if (def instanceof PreferenceSet) {
			return new MetaPreferenceSet();
		} else {
			return new MetaPreference();
		}
	}

	public MetaPreference() {
	}

	public MetaPreference(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getRuntimeClassName() {
		return runtimeClassName;
	}

	public void setRuntimeClassName(String runtimeClassName) {
		this.runtimeClassName = runtimeClassName;
	}

	@Override
	public PreferenceRuntime createRuntime(MetaDataConfig metaDataConfig) {
		return new PreferenceRuntime();
	}

	@Override
	public MetaPreference copy() {
		return ObjectUtil.deepCopy(this);
	}

	public void applyConfig(Preference def) {
		name = def.getName();
		description = def.getDescription();
		displayName = def.getDisplayName();
		value = def.getValue();
		runtimeClassName = def.getRuntimeClassName();
	}

	protected void fillTo(Preference def) {
		def.setName(name);
		def.setDescription(description);
		def.setDisplayName(displayName);
		def.setValue(value);
		def.setRuntimeClassName(runtimeClassName);
	}

	public Preference currentConfig() {
		Preference def = new Preference();
		fillTo(def);
		return def;
	}


	public class PreferenceRuntime extends BaseMetaDataRuntime {

		protected Object runtime;

		PreferenceRuntime() {
			if (runtimeClassName != null) {
				try {
					ScriptEngine se = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();
					GroovyScriptEngine gse = (GroovyScriptEngine) se;
					ClassLoader cl = gse.getSharedClassLoader();
					runtime = Class.forName(runtimeClassName, true, cl).newInstance();
					if (runtime instanceof PreferenceAware) {
						((PreferenceAware) runtime).initialize(MetaPreference.this.currentConfig());
					} else {
						if (value != null) {
							setProperty(runtime, new MetaPreference(VALUE_KEY, value), cl);
						}
					}
				} catch (RuntimeException e) {
					setIllegalStateException(e);
				} catch (Exception e) {
					setIllegalStateException(new IllegalStateException(e));
				}
			}
		}

		public Map<String, Object> getMap() {
			return Collections.singletonMap(VALUE_KEY, (Object) value);
		}

		public Object getRuntime() {
			return runtime;
		}

		protected void applyToBean(Object bean, List<MetaPreference> nameValues, ClassLoader cl) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IntrospectionException {
			if (nameValues != null) {
				for (MetaPreference nv: nameValues) {
					setProperty(bean, nv, cl);
				}
			}
		}

		private Object toBean(PropertyDescriptor prop, MetaPreference pref, ClassLoader cl) {
			try {
				String className = pref.getRuntimeClassName();
				Class<?> type = null;
				if (className != null) {
					type = Class.forName(pref.getRuntimeClassName(), true, cl);
				} else {
					type = prop.getPropertyType();
					if (type != null) {
						if (type.isArray()) {
							type = type.getComponentType();
						}
						if (Collection.class.isAssignableFrom(type)) {
							boolean resolve = false;
							Method getter = prop.getReadMethod();
							Type gt = getter.getGenericReturnType();
							if (gt instanceof ParameterizedType) {
								ParameterizedType pt = (ParameterizedType) gt;
								Type t = pt.getActualTypeArguments()[0];
								if (t instanceof Class && !Object.class.equals(t)) {
									type = (Class<?>) t;
									resolve = true;
								} else if (t instanceof ParameterizedType) {
									type = ((ParameterizedType) t).getRawType().getClass();
									resolve = true;
								}
							}
							if (!resolve) {
								type = null;
							}
						}
					}
				}

				if (type == null) {
					return pref.getValue();
				}
				if (isSupport(type)) {
					return conv(pref.getValue(), type);
				}

				Object bean = type.newInstance();
				if (bean instanceof PreferenceAware) {
					((PreferenceAware) bean).initialize(pref.currentConfig());
				} else {
					if (pref.getValue() != null) {
						setProperty(bean, new MetaPreference(VALUE_KEY, pref.getValue()), cl);
					}
					if (pref instanceof MetaPreferenceSet) {
						applyToBean(bean, ((MetaPreferenceSet) pref).getSubSet(), cl);
					}
				}
				return bean;
			} catch (NoSuchMethodException e) {
				throw new IllegalStateException(e);
			} catch (InvocationTargetException e) {
				throw new IllegalStateException(e);
			} catch (IntrospectionException e) {
				throw new IllegalStateException(e);
			} catch (InstantiationException e) {
				throw new IllegalStateException(e);
			} catch (IllegalAccessException e) {
				throw new IllegalStateException(e);
			} catch (ClassNotFoundException e) {
				throw new IllegalStateException(e);
			}
		}

		private boolean isSupport(Class<?> type) {
			if (type.isPrimitive()) {
				return true;
			}
			if (type == String.class
					|| type == Boolean.class
					|| type == Integer.class
					|| type == Long.class
					|| type == Float.class
					|| type == Double.class
					|| type == Date.class
					|| type == Timestamp.class
					|| type == Time.class
					|| type == BigDecimal.class
					|| type == Class.class
					) {
				return true;
			}
			if (type.isEnum()) {
				return true;
			}
			return false;
		}

		private PropertyDescriptor getPropertyDescriptor(Class<?> clazz, String name) throws IntrospectionException {
			BeanInfo bi = Introspector.getBeanInfo(clazz);
			PropertyDescriptor[] pds = bi.getPropertyDescriptors();
			if (pds != null) {
				for (PropertyDescriptor pd: pds) {
					if (pd.getName().equals(name)) {
						return pd;
					}
				}
			}
			return null;
		}

		@SuppressWarnings("unchecked")
		private void setProperty(Object bean, MetaPreference nv, ClassLoader cl) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IntrospectionException {
			try {
				PropertyDescriptor pd = getPropertyDescriptor(bean.getClass(), nv.getName());
				if (pd != null) {
					Object value = toBean(pd, nv, cl);
					if (pd.getPropertyType().isArray()) {
						Object array = pd.getReadMethod().invoke(bean, new Object[]{});
						if (array == null) {
							array = Array.newInstance(pd.getPropertyType().getComponentType(), 1);
							Array.set(array, 0, value);
						} else {
							int arrayLength = arrayLength(array);
							Object newArray = Array.newInstance(pd.getPropertyType().getComponentType(), arrayLength + 1);
							System.arraycopy(array, 0, newArray, 0, arrayLength);
							Array.set(newArray, arrayLength, value);
							array = newArray;
						}
						if (pd.getWriteMethod() != null) {
							pd.getWriteMethod().invoke(bean, new Object[]{array});
						}
					} else if (pd.getPropertyType() == List.class) {
//					List list = (List) pd.getReadMethod().invoke(bean, (Object)null);
						List list = (List) pd.getReadMethod().invoke(bean);
						if (list == null) {
							list = new ArrayList();
						}
						list.add(value);//Listは実体型がわからない
						if (pd.getWriteMethod() != null) {
							pd.getWriteMethod().invoke(bean, list);
						}
					} else {
						if (pd.getWriteMethod() != null) {
							pd.getWriteMethod().invoke(bean, value);
						}
					}

				}
			} catch (RuntimeException e) {
				throw new IllegalStateException("cant set property value:" + nv.getName() + " to " + bean,  e);
			}
		}

		private int arrayLength(Object array) {
			if (array instanceof Object[]) {
				return ((Object[]) array).length;
			}
			if (array instanceof long[]) {
				return ((long[]) array).length;
			}
			if (array instanceof int[]) {
				return ((int[]) array).length;
			}
			if (array instanceof double[]) {
				return ((double[]) array).length;
			}
			if (array instanceof float[]) {
				return ((float[]) array).length;
			}
			if (array instanceof char[]) {
				return ((char[]) array).length;
			}
			if (array instanceof byte[]) {
				return ((byte[]) array).length;
			}
			if (array instanceof short[]) {
				return ((short[]) array).length;
			}
			if (array instanceof boolean[]) {
				return ((boolean[]) array).length;
			}
			return 0;
		}

		@SuppressWarnings("unchecked")
		public Object conv(Object value, Class<?> type) {
			if (value instanceof String) {
				String str = (String) value;
				if (type == String.class) {
					return value;
				}
				if (type == Boolean.class || type == Boolean.TYPE) {
					if ("TRUE".equalsIgnoreCase(str)) {
						return Boolean.TRUE;
					} else if ("FALSE".equalsIgnoreCase(str)) {
						return Boolean.FALSE;
					} else {
						return null;
					}
				}
				if (type == Integer.class || type == Integer.TYPE) {
					return Integer.parseInt(str);
				}
				if (type == Long.class || type == Long.TYPE) {
					return Long.parseLong(str);
				}
				if (type == Float.class || type == Float.TYPE) {
					return Float.parseFloat(str);
				}
				if (type == Double.class || type == Double.TYPE) {
					return Double.parseDouble(str);
				}
				if (type == Class.class) {
					try {
						return Class.forName(str);
					} catch (ClassNotFoundException e) {
						throw new IllegalArgumentException(e);
					}
				}
				if (type.isEnum()) {
					return Enum.valueOf((Class<Enum>) type, str);
				}
				return ConvertUtil.convertFromString(type, str);
			} else {
				return value;
			}
		}

		@Override
		public MetaPreference getMetaData() {
			return MetaPreference.this;
		}

	}


}
