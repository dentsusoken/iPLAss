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

package org.iplass.mtp.impl.webapi;

import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.webapi.definition.WebApiTokenCheck;

public class MetaWebApiTokenCheck implements MetaData {

	private static final long serialVersionUID = -6169961467571684440L;

	private boolean consume;
	private boolean exceptionRollback;
	private boolean useFixedToken;
	
	public MetaWebApiTokenCheck() {
	}
	

	public MetaWebApiTokenCheck(boolean consume, boolean exceptionRollback,
			boolean useFixedToken) {
		this.consume = consume;
		this.exceptionRollback = exceptionRollback;
		this.useFixedToken = useFixedToken;
	}
	
	public boolean isUseFixedToken() {
		return useFixedToken;
	}

	public void setUseFixedToken(boolean useFixedToken) {
		this.useFixedToken = useFixedToken;
	}

	public boolean isConsume() {
		return consume;
	}

	public void setConsume(boolean consume) {
		this.consume = consume;
	}

	public boolean isExceptionRollback() {
		return exceptionRollback;
	}

	public void setExceptionRollback(boolean exceptionRollback) {
		this.exceptionRollback = exceptionRollback;
	}

	//Definition → Meta
	public void applyConfig(WebApiTokenCheck definition) {
		consume = definition.isConsume();
		exceptionRollback = definition.isExceptionRollback();
		useFixedToken = definition.isUseFixedToken();
	}

	//Meta → Definition
	public WebApiTokenCheck currentConfig() {
		WebApiTokenCheck definition = new WebApiTokenCheck();
		definition.setConsume(consume);
		definition.setExceptionRollback(exceptionRollback);
		definition.setUseFixedToken(useFixedToken);
		return definition;
	}

	@Override
	public MetaWebApiTokenCheck copy() {
		return ObjectUtil.deepCopy(this);
	}

}
