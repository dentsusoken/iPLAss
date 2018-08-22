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

package org.iplass.mtp.entity.query.hint;

public abstract class HintVisitorSupport implements HintVisitor {
	
	@Override
	public boolean visit(HintComment hintComment) {
		return true;
	}
	@Override
	public boolean visit(IndexHint indexHint) {
		return true;
	}
	@Override
	public boolean visit(NoIndexHint noIndexHint) {
		return true;
	}
	@Override
	public boolean visit(NativeHint nativeHint) {
		return true;
	}
	@Override
	public boolean visit(BindHint bindHint) {
		return true;
	}
	@Override
	public boolean visit(NoBindHint noBindHint) {
		return true;
	}
	@Override
	public boolean visit(CacheHint cacheHint) {
		return true;
	}
	@Override
	public boolean visit(FetchSizeHint fetchSizeHint) {
		return true;
	}
	@Override
	public boolean visit(TimeoutHint timeoutHint) {
		return true;
	}
}
