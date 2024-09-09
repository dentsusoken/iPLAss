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
package org.iplass.mtp.impl.core.config;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import org.apache.commons.beanutils.ConvertUtils;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ObjectBuilder;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.spi.ServiceConfigrationException;
import org.iplass.mtp.spi.ServiceInitListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigImpl implements Config {
	private static final Logger logger = LoggerFactory.getLogger(ConfigImpl.class);

	private static final Map<String, Class<?>> primitiveMap = new HashMap<String, Class<?>>();
	private static final Map<Class<?>, Class<?>> primitiveWrapperMap = new HashMap<Class<?>, Class<?>>();
	static {
		primitiveMap.put(Boolean.TYPE.getName(), Boolean.TYPE);
		primitiveMap.put(Byte.TYPE.getName(), Byte.TYPE);
		primitiveMap.put(Character.TYPE.getName(), Character.TYPE);
		primitiveMap.put(Short.TYPE.getName(), Short.TYPE);
		primitiveMap.put(Integer.TYPE.getName(), Integer.TYPE);
		primitiveMap.put(Long.TYPE.getName(), Long.TYPE);
		primitiveMap.put(Double.TYPE.getName(), Double.TYPE);
		primitiveMap.put(Float.TYPE.getName(), Float.TYPE);
		
		primitiveWrapperMap.put(Boolean.TYPE, Boolean.class);
		primitiveWrapperMap.put(Byte.TYPE, Byte.class);
		primitiveWrapperMap.put(Character.TYPE, Character.class);
		primitiveWrapperMap.put(Short.TYPE, Short.class);
		primitiveWrapperMap.put(Integer.TYPE, Integer.class);
		primitiveWrapperMap.put(Long.TYPE, Long.class);
		primitiveWrapperMap.put(Double.TYPE, Double.class);
		primitiveWrapperMap.put(Float.TYPE, Float.class);
		
	}
	
	private static class Instance {
		List<NameValue> nvl;
		Class<?> type;
		Object instance;
		boolean inited;
		
		Map<String, Instance> beanMap;
		List<ServiceInitListener<Service>> internalCreatedListeners;
		
		Instance(Map<String, Instance> beanMap, List<ServiceInitListener<Service>> internalCreatedListeners) {
			this.beanMap = beanMap;
			this.internalCreatedListeners = internalCreatedListeners;
		}
		
		void add(NameValue nv) {
			if (nvl == null) {
				nvl = new LinkedList<>();
			}
			nvl.add(nv);
		}
		
		@SuppressWarnings("unchecked")
		private Object toInstance(NameValue nv) {
			if (nv.isNull()) {
				return null;
			}
			
			if (nv.getRef() != null) {
				Instance bi = beanMap.get(nv.getRef());
				if (bi == null) {
					logger.warn("No bean defined on property:" + nv.getName() + "'s ref:" + nv.getRef());
					return null;
				}
				bi.init(null);
				return bi.instance;
			}
			
			ObjectBuilder<?> builder = nv.builder();
			if (builder != null) {
				builder.setName(nv.getName());
				if (nv.value() != null) {
					builder.setValue(nv.value());
				}
				if (nv.getClassName() != null) {
					builder.setClassName(nv.getClassName());
				}
				if (nv.getArg() != null) {
					HashMap<String, Object> argMap = (HashMap<String, Object>) toBean(HashMap.class, nv.getArg(), null);
					builder.setArgs(argMap);
				}
				if (nv.getProperty() != null) {
					HashMap<String, Object> propMap = (HashMap<String, Object>) toBean(HashMap.class, nv.getProperty(), null);
					builder.setProperties(propMap);
				}
				Object ret = builder.build();
				if (ret instanceof ServiceInitListener) {
					internalCreatedListeners.add((ServiceInitListener<Service>) ret);
				}
				return ret;
			}
			
			if (nv.getClassName() == null) {
				if (type == null) {
					return nv.value();
				} else if (isSupport(type)) {
					return conv(nv.value(), type);
				} else {
					if (type == Map.class) {
						return toBean(HashMap.class, nv.getProperty(), nv.getArg());
					}
					return toBean(type, nv.getProperty(), nv.getArg());
				}
			} else {
				Class<?> cn = toClass(nv.getClassName());
				if (type != null) {
					if (!type.isAssignableFrom(cn)) {
						Class<?> c1 = toWrapperType(type);
						Class<?> c2 = toWrapperType(cn);
						
						if (c1 != c2) {
							throw new ClassCastException(cn.getName() + " can not assignable to " + type.getName());
						}
					}
				}
				if (isSupport(cn)) {
					return conv(nv.value(), cn);
				} else {
					return toBean(cn, nv.getProperty(), nv.getArg());
				}
			}
		}
		
		private Class<?> toWrapperType(Class<?> cls) {
			if (cls != null && cls.isPrimitive()) {
				return primitiveWrapperMap.get(type);
			}
			return cls;
		}
		
		void init(Class<?> type) {
			if (inited) {
				return;
			}
			
			this.type = type;
			
			if (nvl == null || nvl.size() == 0) {
				instance = null;
			} else {
				if (nvl.size() == 1) {
					NameValue nv = nvl.get(0);
					instance = toInstance(nv);
				} else {
					List<Object> valList = new ArrayList<>(nvl.size());
					for (NameValue nv: nvl) {
						valList.add(toInstance(nv));
					}
					instance = valList;
				}
			}

			inited = true;
		}
		
		Object toArray(Class<?> toType) {
			if (type != null) {
				if (!toType.isAssignableFrom(type)) {
					throw new ClassCastException(type.getName() + " can not assignable to " + toType.getName());
				}
			}
			
			if (instance == null) {
				return null;
			}
			
			Object array = Array.newInstance(toType, nvl.size());
			if (nvl.size() == 1) {
				Array.set(array, 0, instance);
			} else {
				@SuppressWarnings("rawtypes")
				List l = (List) instance;
				for (int i = 0; i < nvl.size(); i++) {
					Array.set(array, i, l.get(i));
				}
			}
			return array;
		}
		
		@SuppressWarnings("unchecked")
		<T> List<T> toList(Class<T> toType) {
			if (type != null) {
				if (!toType.isAssignableFrom(type)) {
					throw new ClassCastException(type.getName() + " can not assignable to " + toType.getName());
				}
			}
			
			if (instance == null) {
				return null;
			}
			
			if (nvl.size() == 1) {
				ArrayList<T> l = new ArrayList<>(nvl.size());
				l.add((T) instance);
				return l;
			} else {
				return (List<T>) instance;
			}
		}
		
		@SuppressWarnings("unchecked")
		private Object toBean(Class<?> type, NameValue[] props, NameValue[] args) {
			try {
				Object bean = newIns(type, args);
				if (props != null) {
					LinkedHashMap<String, Instance> nvMap = toNameValueMap(props, beanMap, internalCreatedListeners);
					for (Map.Entry<String, Instance> e: nvMap.entrySet()) {
						setProperty(bean, e.getKey(), e.getValue());
					}
				}
				if (bean instanceof ServiceInitListener) {
					internalCreatedListeners.add((ServiceInitListener<Service>) bean);
				}
				return bean;
			} catch (NoSuchMethodException e) {
				throw new ServiceConfigrationException(e);
			} catch (InvocationTargetException e) {
				throw new ServiceConfigrationException(e);
			} catch (IntrospectionException e) {
				throw new ServiceConfigrationException(e);
			} catch (InstantiationException e) {
				throw new ServiceConfigrationException(e);
			} catch (IllegalAccessException e) {
				throw new ServiceConfigrationException(e);
			}
		}
		
		private Object invokeConstructor(Constructor<?> c, LinkedHashMap<String, Instance> argMap) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			
			Parameter[] params = c.getParameters();
			Object[] paramValues = new Object[params.length];
			for (int i = 0; i < params.length; i++) {
				Parameter p = params[i];
				Instance ins = argMap.get(p.getName());
				if (ins == null) {
					return null;
				}
				
				Class<?> type = p.getType();
				if (type != null) {
					if (type.isArray()) {
						type = type.getComponentType();
					} else if (Collection.class.isAssignableFrom(type)
							&& !Map.class.isAssignableFrom(type)) {
						boolean resolve = false;
						Type gt = p.getParameterizedType();
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
				
				if (type != null && ins.inited && ins.type != null) {
					if (!type.isAssignableFrom(ins.type)) {
						ins.inited = false;
					}
				}
				ins.init(type);
				
				Object value;
				if (ins.instance == null) {
					value = null;
				} else if (p.getType().isArray()) {
					value = ins.toArray(type);
				} else if (p.getType() == List.class || p.getType() == Collection.class) {
					value = ins.toList(type);
				} else {
					value = ins.instance;
				}
				
				paramValues[i] = value;
			}
			
			return c.newInstance(paramValues);
			
		}
		
		private Object newIns(final Class<?> type, NameValue[] args) throws InstantiationException, IllegalAccessException {
			if (args == null) {
				return type.newInstance();
			} else {
				LinkedHashMap<String, Instance> argMap = toNameValueMap(args, beanMap, internalCreatedListeners);
				ArrayList<Constructor<?>> targets = new ArrayList<>();
				for (Constructor<?> c: type.getConstructors()) {
					if (argMap.size() == c.getParameterCount()) {
						targets.add(c);
					}
				}
				
				Exception ee = null;
				for (Constructor<?> c: targets) {
					try {
						Object ins = invokeConstructor(c, argMap);
						if (ins != null) {
							return ins;
						}
					} catch (Exception e) {
						if (ee == null) {
							ee = e;
						} else {
							ee.addSuppressed(e);
						}
					}
				}
				if (ee != null) {
					throw new ServiceConfigrationException("no suitable constructor " + type.getName() + " with args:" + Arrays.toString(args), ee);
				} else {
					throw new ServiceConfigrationException("no suitable constructor " + type.getName() + " with args:" + Arrays.toString(args));
				}
			}
		}
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		private void setProperty(Object bean, String propName, Instance val) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IntrospectionException {
			try {
				PropertyDescriptor pd = getPropertyDescriptor(bean.getClass(), propName);
				if (pd == null) {
					if (bean instanceof Map) {
						val.init(null);
						((Map) bean).put(propName, val.instance);
					} else {
						logger.warn("No property defined: " + propName + " on " + bean.getClass().getName());
					}
					return;
				}
				
				Class<?> type = pd.getPropertyType();
				if (type != null) {
					if (type.isArray()) {
						type = type.getComponentType();
					} else if (Map.class == type) {
						type = HashMap.class;
					} else if (Collection.class.isAssignableFrom(type)) {
						boolean resolve = false;
						Method getter = pd.getReadMethod();
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
				
				val.init(type);
				Object value = val.instance;
				
				if (value != null) {
					if (pd.getPropertyType().isArray()) {
						Object array = val.toArray(type);
						if (pd.getWriteMethod() != null) {
							pd.getWriteMethod().invoke(bean, new Object[]{array});
						} else {
							logger.warn("writeMethod not defined, so can't set array value to " + propName + " on " + bean.getClass().getName());
						}
					} else if (pd.getPropertyType() == List.class || pd.getPropertyType() == Collection.class) {
						Collection list = (Collection) pd.getReadMethod().invoke(bean);
						if (list == null) {
							list = val.toList(type);
							if (pd.getWriteMethod() != null) {
								pd.getWriteMethod().invoke(bean, list);
							} else {
								logger.warn("writeMethod not defined, so can't set list value to " + propName + " on " + bean.getClass().getName());
							}
						} else {
							list.clear();
							list.addAll(val.toList(type));
						}
					} else {
						if (pd.getWriteMethod() != null) {
							pd.getWriteMethod().invoke(bean, value);
						} else {
							logger.warn("writeMethod not defined, so can't set value to " + propName + " on " + bean.getClass().getName());
						}
					}
				}
			} catch (RuntimeException e) {
				throw new ServiceConfigrationException("cant set property value:" + propName + "(" + val.type + ") to " + bean,  e);
				
			}
		}
	}

	private String serviceName;
	private List<ServiceInitListener<Service>> internalCreatedListeners;
	private LinkedHashMap<String, Instance> beanMap;
	private LinkedHashMap<String, Instance> propMap;
	private Map<String, Service> dependentServices;
	private List<String> dependentServiceNames;

	private boolean initedPhase;
	private List<ServiceInitListener<Service>> additionalListeners;

	public ConfigImpl(String serviceName, NameValue[] nameValues, NameValue[] beanNameValues) {
		this.serviceName = serviceName;
		internalCreatedListeners = new ArrayList<>();
		beanMap = toNameValueMap(beanNameValues, null, internalCreatedListeners);
		propMap = toNameValueMap(nameValues, beanMap, internalCreatedListeners);
	}
	
	public String getServiceName() {
		return serviceName;
	}

	public void addDependentService(String type, Service instance) {
		if (dependentServices == null) {
			dependentServices = new HashMap<String, Service>();
			dependentServiceNames = new ArrayList<String>();
		}
		dependentServices.put(type, instance);
		dependentServiceNames.add(type);
	}

	public List<String> getDependentServiceNames() {
		return dependentServiceNames;
	}

	@SuppressWarnings("unchecked")
	public <T extends Service> T getDependentService(Class<T> type) {
		if (dependentServices == null) {
			return null;
		}
		return (T) dependentServices.get(type.getName());
	}

	@SuppressWarnings("unchecked")
	public <T extends Service> T getDependentService(String serviceName) {
		if (dependentServices == null) {
			return null;
		}
		return (T) dependentServices.get(serviceName);
	}
	
	private static LinkedHashMap<String, Instance> toNameValueMap(NameValue[] nameValues, Map<String, Instance> sharedMap, List<ServiceInitListener<Service>> internalCreatedListeners) {
		LinkedHashMap<String, Instance> nvm = new LinkedHashMap<>();
		if (sharedMap == null) {
			sharedMap = nvm;
		}
		if (nameValues != null) {
			for (NameValue nv: nameValues) {
				Instance val = nvm.get(nv.getName());
				if (val == null) {
					val = new Instance(sharedMap, internalCreatedListeners);
					nvm.put(nv.getName(), val);
				}
				val.add(nv);
			}
		}
		return nvm;
	}
	
	private Object valueInit(String name, Class<?> type) {
		Instance i = propMap.get(name);
		if (i != null) {
			i.init(type);
			return i.instance;
		} else {
			return null;
		}
	}
	
	private static Class<?> toClass(String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			Class<?> primType = primitiveMap.get(className);
			if (primType != null) {
				return primType;
			}
			
			throw new ServiceConfigrationException(e);
		}
	}
	
	private static PropertyDescriptor getPropertyDescriptor(Class<?> clazz, String name) throws IntrospectionException {
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
	
	private static boolean isSupport(Class<?> type) {
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Object conv(Object value, Class<?> type) {
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
			return ConvertUtils.convert(str, type);
		} else {
			return value;
		}
	}

	public Set<String> getNames() {
		return propMap.keySet();
	}

	@SuppressWarnings("unchecked")
	public String getValue(String name) {
		Object value = valueInit(name, null);
		
		if (value instanceof String) {
			return (String) value;
		}
		if (value instanceof List) {
			value = ((List<Object>) value).get(0);
			if (value instanceof String) {
				return (String) value;
			}
		}
		
		//from NameValue
		Instance i = propMap.get(name);
		if (i != null && i.nvl != null && i.nvl.size() > 0) {
			return i.nvl.get(0).value();
		}
		
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<String> getValues(String name) {
		Object value = valueInit(name, null);
		if (value instanceof String) {
			List<String> res = new ArrayList<String>();
			res.add((String) value);
			return res;
		}
		if (value instanceof List) {
			//check instance type
			boolean isStr = true;
			for (Object o: (List<Object>) value) {
				if (o != null && !(o instanceof String)) {
					isStr = false;
					break;
				}
			}
			if (isStr) {
				return (List<String>) value;
			}
		}
		
		//from NameValue
		Instance i = propMap.get(name);
		if (i != null && i.nvl != null && i.nvl.size() > 0) {
			ArrayList<String> ret = new ArrayList<>(i.nvl.size());
			for (NameValue nv: i.nvl) {
				ret.add(nv.value());
			}
			return ret;
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getValue(String name, Class<T> type) {
		if (type == Object.class) {
			type = null;
		}
		
		Object value = valueInit(name, type);
		if (value instanceof List) {
			return ((List<T>) value).get(0);
		}
		return (T) value;
		
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> getValues(String name, Class<T> type) {
		if (type == Object.class) {
			type = null;
		}
		
		Object value = valueInit(name, type);
		if (value == null) {
			return null;
		}
		if (value instanceof List) {
			return (List<T>) value;
		}
		return Collections.singletonList((T) value);

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getValue(String name, Class<T> type, T defaultValue) {
		T ret = getValue(name, type);
		if (ret == null) {
			if (logger.isDebugEnabled()) {
				logger.debug(name + "(type:" + type.getName() + ") undefined. so use defaultValue:" + defaultValue);
			}
			if (defaultValue instanceof ServiceInitListener) {
				internalCreatedListeners.add((ServiceInitListener<Service>) defaultValue);
			}
			return defaultValue;
		} else {
			return ret;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getValueWithSupplier(String name, Class<T> type, Supplier<T> defaultValueSupplier) {
		T ret = getValue(name, type);
		if (ret == null) {
			ret = defaultValueSupplier.get();
			if (logger.isDebugEnabled()) {
				logger.debug(name + "(type:" + type.getName() + ") undefined. so use defaultValue:" + ret);
			}
			if (ret instanceof ServiceInitListener) {
				internalCreatedListeners.add((ServiceInitListener<Service>) ret);
			}

		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getValues(String name, Class<T> type, List<T> defaultValues) {
		List<T> ret = getValues(name, type);
		if (ret == null) {
			if (logger.isDebugEnabled()) {
				logger.debug(name + "(type:" + type.getName() + ") undefined. so use defaultValue:" + defaultValues);
			}
			if (defaultValues != null) {
				for (T dv : defaultValues) {
					if (dv instanceof ServiceInitListener) {
						internalCreatedListeners.add((ServiceInitListener<Service>) dv);
					}
				}
			}
			return defaultValues;
		} else {
			return ret;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getValuesWithSupplier(String name, Class<T> type, Supplier<List<T>> defaultValueSupplier) {
		List<T> ret = getValues(name, type);
		if (ret == null) {
			ret = defaultValueSupplier.get();
			if (logger.isDebugEnabled()) {
				logger.debug(name + "(type:" + type.getName() + ") undefined. so use defaultValue:" + ret);
			}
			if (ret != null) {
				for (T dv : ret) {
					if (dv instanceof ServiceInitListener) {
						internalCreatedListeners.add((ServiceInitListener<Service>) dv);
					}
				}
			}
		}
		return ret;
	}

	public Object getBean(String name) {
		return getValue(name, null);
	}

	public List<?> getBeans(String name) {
		return getValues(name, null);
	}
	
	public void notifyInited(Service service) {
		for (Instance i: beanMap.values()) {
			i.init(null);
		}
		
		for (Instance i: propMap.values()) {
			i.init(null);
		}
		
		initedPhase = true;
		additionalListeners = new ArrayList<>();
		
		for (ServiceInitListener<Service> l : internalCreatedListeners) {
			((ServiceInitListener<Service>) l).inited(service, this);
		}
		
		while (additionalListeners.size() > 0) {
			List<ServiceInitListener<Service>> forInited = additionalListeners;
			additionalListeners = new ArrayList<>();
			for (ServiceInitListener<Service> l : forInited) {
				l.inited(service, this);
			}
			internalCreatedListeners.addAll(forInited);
		}
		
	}

	public void notifyDestroyed() {
		for (ServiceInitListener<Service> l : internalCreatedListeners) {
			((ServiceInitListener<Service>) l).destroyed();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Service> void addServiceInitListener(ServiceInitListener<T> listener) {
		if (initedPhase) {
			additionalListeners.add((ServiceInitListener<Service>) listener);
		} else {
			internalCreatedListeners.add((ServiceInitListener<Service>) listener);
		}
	}

	@Override
	public <T extends Service> void removeServiceInitListener(ServiceInitListener<T> listener) {
		internalCreatedListeners.remove(listener);
		if (additionalListeners != null) {
			additionalListeners.remove(listener);
		}
	}

}
