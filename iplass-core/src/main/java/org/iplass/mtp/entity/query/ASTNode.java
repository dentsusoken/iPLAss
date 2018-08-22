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

package org.iplass.mtp.entity.query;

import java.io.Serializable;

public interface ASTNode extends Serializable {
	
	//TODO toString()をオーバーライドでなく、appendString(StringBuilder sb)をオーバーライドさせる
	
	//TODO ASTTransformer、Visitorはapiとして公開しない形の方がよい
	
	public String toString();
	public int hashCode();
	public boolean equals(Object obj);
	
	public ASTNode accept(ASTTransformer transformer);
	
	public default ASTNode copy() {
		return this.accept(ASTTransformerSupport.copier);
	}
	
//	public void appendTo(StringBuilder sb);
	
}
