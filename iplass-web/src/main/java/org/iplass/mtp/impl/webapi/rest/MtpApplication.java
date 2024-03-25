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

package org.iplass.mtp.impl.webapi.rest;

import java.util.HashSet;
import java.util.Set;

import jakarta.ws.rs.core.Application;

import org.iplass.mtp.impl.webapi.jackson.ObjectMapperProvider;
import org.iplass.mtp.impl.webapi.jaxb.JAXBContextProvider;

/**
 * JAX-RS Application定義。
 *
 * @author K.Higuchi
 *
 */
public class MtpApplication extends Application {
	@Override
	public Set<Class<?>> getClasses() {
		//TODO 設定ファイル化
		Set<Class<?>> s = new HashSet<Class<?>>();
		s.add(RestCommandInvoker.class);
		s.add(MtpExceptionMapper.class);
		s.add(MtpContainerRequestFilter.class);

		s.add(ObjectMapperProvider.class);
		s.add(JAXBContextProvider.class);
		return s;
	}

}
