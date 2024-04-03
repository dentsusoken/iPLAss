/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

import org.iplass.mtp.spi.ServiceConfigrationException;

public class ServerEnv {

	@Deprecated
	public static final String SERVER_ROLE_DEF_SYSTEM_PROP_NAME = BootstrapProps.SERVER_ROLES;
	@Deprecated
	public static final String SERVER_ID_DEF_SYSTEM_PROP_NAME =  BootstrapProps.SERVER_ID;
	@Deprecated
	public static final String SERVER_NAME_DEF_SYSTEM_PROP_NAME =  BootstrapProps.SERVER_NAME;
	@Deprecated
	public static final String INTERFACE_NAME_DEF_SYSTEM_PROP_NAME =  BootstrapProps.NETWORK_INTERFACE_NAME;

	private static ServerEnv instance = new ServerEnv();

	private String[] serverRoles;
	private String serverId;
	private BootstrapProps props;

	public static ServerEnv getInstance() {
		return instance;
	}

	private ServerEnv() {
		props = BootstrapProps.getInstance();

		String myserverroles = props.getProperty(BootstrapProps.SERVER_ROLES);
		if (myserverroles != null) {
			serverRoles = myserverroles.trim().split("\\s*,\\s*");
		}

		String id = props.getProperty(BootstrapProps.SERVER_ID);
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
		String defHostName = props.getProperty(BootstrapProps.SERVER_NAME);
		if (defHostName != null) {
			return new String[]{defHostName};
		} else {
			Set<String> list = new LinkedHashSet<>();
			String networkInterfaceName = props.getProperty(BootstrapProps.NETWORK_INTERFACE_NAME);
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

			if (list.isEmpty()) {
				// FIX JDK21 NetworkInterface から情報を取得できない場合は、ローカルホストを設定する。
				try {
					addHostNameAndAddress(list, InetAddress.getLocalHost());
					addHostNameAndAddress(list, InetAddress.getLoopbackAddress());
				} catch (UnknownHostException e) {
					throw new ServiceConfigrationException("Unable to retrieve localhost. ", e);
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
