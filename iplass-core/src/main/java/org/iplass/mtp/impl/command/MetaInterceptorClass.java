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

import org.iplass.mtp.definition.Definition;
import org.iplass.mtp.impl.definition.DefinableMetaData;
import org.iplass.mtp.impl.metadata.BaseRootMetaData;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.metadata.MetaDataRuntime;
import org.iplass.mtp.impl.metadata.RootMetaData;

@SuppressWarnings("rawtypes")
public class MetaInterceptorClass extends BaseRootMetaData implements DefinableMetaData {

	//TODO 実装！！

	private static final long serialVersionUID = 4358990108260809791L;

	@Override
	public MetaDataRuntime createRuntime(MetaDataConfig metaDataConfig) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RootMetaData copy() {
		// TODO Auto-generated method stub
		return null;
	}

	public abstract class InterceptorClassRuntime implements MetaDataRuntime {
		public abstract Object newInterceptor();
		public abstract MetaInterceptorClass getMetaData();
	}

	@Override
	public void applyConfig(Definition definition) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public Definition currentConfig() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
