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

package org.iplass.mtp.spi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.iplass.mtp.SystemException;
import org.iplass.mtp.impl.core.config.ConfigImpl;
import org.iplass.mtp.impl.core.config.ConfigPreprocessor;
import org.iplass.mtp.impl.core.config.NameValue;
import org.iplass.mtp.impl.core.config.ServiceConfig;
import org.iplass.mtp.impl.core.config.ServiceDefinition;
import org.iplass.mtp.impl.core.config.ServiceRegistryInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Serviceのレジストリです。
 * iPLAssが管理するServiceのインスタンスを取得可能です。
 * Serviceは設定ファイルにてコンフィグレーション可能です。
 * 
 * @author K.Higuchi
 *
 */
public class ServiceRegistry {

	private static final Logger logger = LoggerFactory.getLogger(ServiceRegistry.class);

	private static ServiceRegistry registry = new ServiceRegistry();

	private final ConcurrentHashMap<String, ServiceEntry> services;

	private volatile ServiceDefinition serviceDefinition;

	private volatile boolean destroyed = false;

	/**
	 * SingletonなServiceRegistryを取得します。
	 * 
	 * @return
	 */
	public static ServiceRegistry getRegistry() {
		return registry;
	}

	private ServiceRegistry() {
		services = new ConcurrentHashMap<>(32, 0.75f, 1);
		serviceDefinition = loadServiceDefinition();
	}

	private ServiceDefinition loadServiceDefinition() {
		String configFileName = ServiceRegistryInitializer.getConfigFileName();
		
		ConfigPreprocessor[] prepros = newConfigPreprocessor();
		
		try {
			JAXBContext context = JAXBContext.newInstance(NameValue.class, ServiceConfig.class, ServiceDefinition.class);
			ServiceDefinition serviceDefinition = getServiceDefinision(configFileName, context, prepros);
			return serviceDefinition;
		} catch (JAXBException e) {
			logger.error("JAXBContext can not initialize.", e);
			throw new ServiceConfigrationException(e);
		}
	}
	
	private ConfigPreprocessor[] newConfigPreprocessor() {
		List<String> cnames = ServiceRegistryInitializer.getConfigPreprocessorClassNames();
		ConfigPreprocessor[] cps = null;
		if (cnames != null) {
			cps = new ConfigPreprocessor[cnames.size()];
			for (int i = 0; i < cps.length; i++) {
				try {
					cps[i] = (ConfigPreprocessor) Class.forName(cnames.get(i)).newInstance();
				} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
					throw new ServiceConfigrationException("Can not instanceate ConfigPreprocessor:" + cnames.get(i), e);
				}
			}
		}
		return cps;
	}
	
	private String readContent(String fileName) {
		InputStream is = null;
		try {
			is = getClass().getResourceAsStream(fileName);
			if (is == null) {
				File file = new File(fileName);
				if (file.exists()) {
					try {
						is = new FileInputStream(file);
					} catch (FileNotFoundException e) {
						if (logger.isDebugEnabled()) {
							logger.debug("ConfigFile:" + fileName + " not found.", e);
						}
					}
				}
			}
			if (is == null) {
				logger.error("ConfigFile:" + fileName + " not found.Can not initialize ServiceRegistry.");
				throw new ServiceConfigrationException("Config File:" + fileName + " Not Found.");
			}
			
			InputStreamReader r = new InputStreamReader(is, "utf-8");
			StringBuffer str = new StringBuffer();
			char[] buf = new char[1024];
			int length = 0;
			while ((length = r.read(buf)) != -1) {
				str.append(buf, 0, length);
			}
			return str.toString();
		} catch (IOException e) {
			logger.error("Cant read ConfigFile:" + fileName + ".Can not initialize ServiceRegistry.", e);
			throw new ServiceConfigrationException(e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					logger.warn("resource close failed. Maybe Resource Leak.", e);
				}
			}
		}
	}


	private ServiceDefinition getServiceDefinision(String fileName, JAXBContext context, ConfigPreprocessor[] prepros) {
		
		String content = readContent(fileName);
		if (prepros != null) {
			for (ConfigPreprocessor p: prepros) {
				content = p.preprocess(content, fileName);
			}
		}

		try {
			Unmarshaller um = context.createUnmarshaller();
			ServiceDefinition sd = (ServiceDefinition) um.unmarshal(new StringReader(content));
			if (prepros != null) {
				for (ConfigPreprocessor p: prepros) {
					sd = p.preprocess(sd);
				}
			}
			
			if (sd.getInherits() != null) {
				//merge multi inherits
				if (logger.isDebugEnabled()) {
					logger.debug(fileName + " inherited " + sd.getInherits()[0]);
				}
				ServiceDefinition inhSd = getServiceDefinision(sd.getInherits()[0], context, prepros);
				for (int i = 1; i < sd.getInherits().length; i++) {
					if (logger.isDebugEnabled()) {
						logger.debug(fileName + " inherited " + sd.getInherits()[i]);
					}
					inhSd.include(getServiceDefinision(sd.getInherits()[i], context, prepros));
				}
				
				sd.inherit(inhSd);
			}

			if (sd.getIncludes() != null) {
				for (String inc: sd.getIncludes()) {
					ServiceDefinition incSd = getServiceDefinision(inc, context, prepros);
					sd.include(incSd);
					if (logger.isDebugEnabled()) {
						logger.debug(fileName + " included " + inc);
					}
				}
			}

			if (logger.isTraceEnabled()) {
				Marshaller m = context.createMarshaller();
				m.setProperty("jaxb.formatted.output", true);
				StringWriter w = new StringWriter();
				m.marshal(sd, w);
				logger.trace("configration of " + fileName + "\n=============\n" + w.toString() + "\n=============");
			}

			return sd;
		} catch (JAXBException e) {
			logger.error("Parse failed ConfigFile:" + fileName + ".Can not initialize ServiceRegistry.", e);
			throw new ServiceConfigrationException(e);
		}
	}

	private ServiceEntry createService(String serviceName, List<String> dependStack) {

		synchronized (this) {

			if (destroyed) {
				throw new SystemException("service allready destroyed");
			}

			ServiceEntry service = services.get(serviceName);
			if (service != null) {
				return service;
			}

			long start = 0;
			if (logger.isDebugEnabled()) {
				logger.debug("Service: " + serviceName + " create");
				start = System.currentTimeMillis();
			}
			
			if (dependStack.contains(serviceName)) {
				throw new ServiceConfigrationException("depend loop occured." + dependStack + " " + serviceName);
			}

			dependStack.add(serviceName);

			ServiceConfig sc = serviceDefinition.search(serviceName);
			if (sc == null) {
				throw new ServiceConfigrationException(serviceName + " not defined.");
			}
			ConfigImpl config = new ConfigImpl(serviceName, sc.getProperty());

			if (sc.getDepend() != null) {
				for (String depend: sc.getDepend()) {
					ServiceEntry dependService = services.get(depend);
					if (dependService == null) {
						dependService = createService(depend, dependStack);
					}
					config.addDependentService(depend, dependService.service);
				}
			}
			try {
				
				Class<?> interfaceType = Class.forName(sc.getInterfaceName());
				if (!Service.class.isAssignableFrom(interfaceType)) {
					logger.error("Can not regist Service." + sc.getInterfaceName() + "must implements Service interface.");
					throw new ServiceConfigrationException("Can not regist Service." + sc.getInterfaceName() + "must implements Service interface.");
				}

				String className = sc.getClassName();
				if (className == null) {
					className = sc.getInterfaceName();
				}
				Class<?> implClassType = Class.forName(className);
				if (!interfaceType.isAssignableFrom(implClassType)) {
					logger.error("Can not regist Service." + sc.getClassName() + "must implements " + sc.getInterfaceName() + " interface.");
					throw new ServiceConfigrationException("Can not regist Service." + sc.getClassName() + " must implements " + sc.getInterfaceName() + " interface.");
				}

				Service s = (Service) implClassType.newInstance();
				s.init(config);

				config.notifyInited(s);

				ServiceEntry se = new ServiceEntry(serviceName, s, config);

				services.put(serviceName, se);

				dependStack.remove(serviceName);

				if (logger.isDebugEnabled()) {
					logger.debug("Service: " + serviceName + " created in " + (System.currentTimeMillis() - start) + "ms.");
				}

				return se;
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | RuntimeException e) {
				throw new ServiceConfigrationException("failed to create Service:" + serviceName, e);
			}
		}
	}

	/**
	 * 指定のクラス・インタフェースを実装するServiceのインスタンスを取得します。
	 * 
	 * @param serviceClass
	 * @return
	 */
	public <T extends Service> T getService(Class<T> serviceClass) {
		return this.<T> getService(serviceClass.getName());
	}

	/**
	 * 指定の名前で登録されるServiceのインスタンスを取得します。
	 * 
	 * @param serviceName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends Service> T getService(String serviceName) {
		ServiceEntry se = services.get(serviceName);
		if (se == null) {
			try {
				se = createService(serviceName, new ArrayList<String>());
			} catch(ServiceConfigrationException e) {
				throw e;
			} catch(RuntimeException e) {
				throw new ServiceConfigrationException("can not initialize service:" + serviceName, e);
			}
		}
		return (T) se.service;
	}

	/**
	 * 指定ののクラス・インタフェースを実装するServiceが登録されているかを返します。
	 * 
	 * @param serviceClass
	 * @return
	 */
	public <T extends Service> boolean exists(Class<T> serviceClass) {
		return exists(serviceClass.getName());
	}

	/**
	 * 指定の名前で登録されるServiceが登録されているかを返します。
	 * 
	 * @param serviceName
	 * @return
	 */
	public boolean exists(String serviceName) {
		return serviceDefinition.search(serviceName) != null;
	}

	/**
	 * プログラムから明示的にサービスを登録します。
	 * 
	 * @param serviceName サービスの名前
	 * @param service Serviceを実装するインスタンス
	 */
	public void setService(String serviceName, Service service) {
		synchronized (this) {
			if (destroyed) {
				throw new SystemException("service already destroyed");
			}
			ServiceEntry previous = services.put(serviceName, new ServiceEntry(serviceName, service, null));
			if (previous != null) {
				previous.destroy();
			}
		}
	}

	/**
	 * プログラムから明示的にサービスを登録します。
	 * serviceの実装クラス名がサービス名として登録されます。
	 * 
	 * @param service
	 */
	public void setService(Service service) {
		setService(service.getClass().getName(), service);
	}

	/**
	 * すべてのサービスを破棄します。
	 * 
	 */
	public void destroyAllService() {
		destroyed = true;
		for (Map.Entry<String, ServiceEntry> e: services.entrySet()) {
			e.getValue().destroy();
		}
		services.clear();
	}

	/**
	 * Serviceを再初期化します。
	 * 
	 */
	public void reInit() {
		synchronized (this) {
			if (destroyed) {
				throw new SystemException("service already destroyed");
			}

			List<ServiceEntry> forDest = new ArrayList<>(services.size());
			for (Map.Entry<String, ServiceEntry> e: services.entrySet()) {
				forDest.add(e.getValue());
			}

			//reload
			services.clear();
			serviceDefinition = loadServiceDefinition();

			for (ServiceEntry se: forDest) {
				se.destroy();
			}

		}

	}

	private static class ServiceEntry {
		private final String name;
		private final Service service;
		private final ConfigImpl config;

		ServiceEntry(String name, Service service, ConfigImpl config) {
			this.name = name;
			this.service = service;
			this.config = config;
		}

		void destroy() {
			try {
				service.destroy();
				if (config != null) {
					config.notifyDestroyed();
				}
			} catch (Exception e) {
				logger.error("service:" + name + " destroy process faild. so mybe memory leak. cause:" + e, e);
			}
		}
	}


}
