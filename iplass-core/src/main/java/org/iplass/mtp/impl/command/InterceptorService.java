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

package org.iplass.mtp.impl.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.command.interceptor.CommandInterceptor;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;

public class InterceptorService implements Service {

	//FIXME 暫定的な実装。共通のnewInstanceしたままのInterceptorのみ。要：メタデータ化、きめ細やかなコンフィグレーションの実装
	//TODO メタデータ、アノテーションによる、Interceptorの定義

	private Map<String, CommandInterceptor[]>  interceptorsMap;
	
	public CommandInterceptor[] getInterceptors(String interceptorSetName) {
		return interceptorsMap.get(interceptorSetName);
	}

	@Override
	public void destroy() {

	}

	@Override
	public void init(Config config) {
		interceptorsMap = new HashMap<>();
		
		for (String name: config.getNames()) {
			@SuppressWarnings("unchecked")
			List<Object> vals = (List<Object>) config.getBeans(name);
			if (vals.size() > 0 && vals.get(0) instanceof CommandInterceptor) {
				CommandInterceptor[] set = vals.toArray(new CommandInterceptor[vals.size()]);
				interceptorsMap.put(name, set);
			}
		}
	}

}
