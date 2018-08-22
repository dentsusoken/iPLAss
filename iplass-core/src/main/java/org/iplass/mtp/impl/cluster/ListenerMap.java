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

package org.iplass.mtp.impl.cluster;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ListenerMap {
	
	private final ConcurrentHashMap<String, List<ClusterEventListener>> listenerMap;
	
	public ListenerMap() {
		listenerMap = new ConcurrentHashMap<String, List<ClusterEventListener>>(16, 0.75f, 32);
	}
	
	public void init() {
		listenerMap.clear();
	}
	
	public List<ClusterEventListener> getListener(String eventName) {
		List<ClusterEventListener> l = listenerMap.get(eventName);
		if (l == null) {
			return Collections.emptyList();
		} else {
			return l;
		}
	}
	
	public void addListener(String eventName, ClusterEventListener listener) {
		//CopyOnWrite
		List<ClusterEventListener> currentList = listenerMap.get(eventName);
		if (currentList == null) {
			List<ClusterEventListener> newList = new CopyOnWriteArrayList<ClusterEventListener>(new ClusterEventListener[]{listener});
			currentList = listenerMap.putIfAbsent(eventName, newList);
			if (currentList == null) {
				return;
			}
		}
		currentList.add(listener);
	}

	public void removeListener(String eventName, ClusterEventListener listener) {
		List<ClusterEventListener> currentList = listenerMap.get(eventName);
		if (currentList != null) {
			currentList.remove(listener);
		}
	}
	

}
