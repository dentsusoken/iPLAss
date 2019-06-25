/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

import org.iplass.mtp.spi.ServiceConfigrationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerEnv {
	
	private static Logger logger = LoggerFactory.getLogger(ServerEnv.class);
	
	public static final String SERVER_ROLE_DEF_SYSTEM_PROP_NAME = "mtp.server.myserverroles";
	public static final String SERVER_ID_DEF_SYSTEM_PROP_NAME = "mtp.server.myserverid";
	public static final String SERVER_NAME_DEF_SYSTEM_PROP_NAME = "mtp.server.myservername";
	public static final String INTERFACE_NAME_DEF_SYSTEM_PROP_NAME = "mtp.server.myinterfacename";
	
	private static ServerEnv instance = new ServerEnv();
	
	private final Properties props;
	private String[] serverRoles;
	private String serverId;
	
	public static ServerEnv getInstance() {
		return instance;
	}
	
	private ServerEnv() {
		String fileName = ServiceRegistryInitializer.getServerEnvFileName();
		if (fileName != null) {
			try (InputStream is = getClass().getResourceAsStream(fileName)) {
				if (is == null) {
					logger.debug("mtp.server.env:" + fileName + " not found.use SystemProperty as server env config.");
					props = System.getProperties();
				} else {
					Properties p = new Properties();
					p.load(new InputStreamReader(is, "UTF-8"));
					props = p;
				}
			} catch (IOException e) {
				throw new ServiceConfigrationException("cant load server env file:" + fileName, e);
			}
		} else {
			logger.debug("mtp.server.env not specified.use SystemProperty as server env config.");
			props = System.getProperties();
		}
		
		String myserverroles = getProperty(SERVER_ROLE_DEF_SYSTEM_PROP_NAME);
		if (myserverroles != null) {
			serverRoles = myserverroles.trim().split("\\s*,\\s*");
		}
		
		String id = getProperty(SERVER_ID_DEF_SYSTEM_PROP_NAME);
		if (id == null) {
			try {
				id = getServerNameAndAddress()[0];
			} catch (SocketException e) {
				throw new IllegalStateException(e);
			}
		}
		serverId = id;
	}
	
	public String getProperty(String key) {
		return props.getProperty(key);
	}
	
	public String getProperty(String key, String def) {
		return props.getProperty(key, def);
	}
	
	public String[] getServerRoles() {
		return serverRoles;
	}
	
	public String getServerId() {
		return serverId;
	}
	
	public String[] getServerNameAndAddress() throws SocketException {
		String defHostName = getProperty(SERVER_NAME_DEF_SYSTEM_PROP_NAME);
		if (defHostName != null) {
			return new String[]{defHostName};
		} else {
			Set<String> list = new LinkedHashSet<>();
			String networkInterfaceName = getProperty(INTERFACE_NAME_DEF_SYSTEM_PROP_NAME);
			NetworkInterface ni = null;
			NetworkInterface loopBack = null;
			if (networkInterfaceName != null) {
				ni = NetworkInterface.getByName(networkInterfaceName);
				if (ni == null) {
					throw new ServiceConfigrationException("networkInterfaceName:" + networkInterfaceName + " not found...");
				}
			} else {
				//Loopbackでない、先頭に定義されているNetworkInterfaceの定義を採用
				Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
				while (e.hasMoreElements()) {
					NetworkInterface forCheck = e.nextElement();
					if (forCheck.isLoopback()) {
						loopBack = forCheck;
					} else if (forCheck.isUp()) {
						ni = forCheck;
						break;
					}
				}
			}
			
			if (ni != null) {
				Enumeration<InetAddress> addresses = ni.getInetAddresses();
				while (addresses.hasMoreElements()) {
					addHostNameAndAddress(list, addresses.nextElement());
				}
			}
			if (loopBack != null) {
				Enumeration<InetAddress> addresses = loopBack.getInetAddresses();
				while (addresses.hasMoreElements()) {
					addHostNameAndAddress(list, addresses.nextElement());
				}
			}
			return list.toArray(new String[list.size()]);
		}

	}
	
	private void addHostNameAndAddress(Set<String> list, InetAddress ia) {
		String hostName = ia.getHostName();
		if (ia.isLoopbackAddress()) {
			list.add("localhost");//hostNameがIPになってしまう
			list.add(ia.getHostAddress());
		} else {
			list.add(hostName);
			String address = ia.getHostAddress();
			if (hostName.contains(".")
					&& !address.equals(hostName)) {
				list.add(hostName.substring(0, hostName.indexOf('.')));
			}
			list.add(address);
		}
	}

}
