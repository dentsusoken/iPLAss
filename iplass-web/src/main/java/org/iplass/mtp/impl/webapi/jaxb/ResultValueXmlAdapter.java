/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.webapi.jaxb;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.iplass.mtp.impl.xml.jaxb.DateXmlAdapter;

public class ResultValueXmlAdapter extends XmlAdapter<Object, Object> {
	
	private DateXmlAdapter dateAdapter = new DateXmlAdapter();
	
	@Override
	public Object marshal(Object v) throws Exception {
		if (v instanceof List<?>) {
			List<?> vList = (List<?>) v;
			List<Object> bList = new ArrayList<>(vList.size());
			for (Object lv: vList) {
				bList.add(marshal(lv));
			}
			return new JaxbListValue(bList);
		}
		
		return dateAdapter.marshal(v);
	}

	@Override
	public Object unmarshal(Object b) throws Exception {
		if (b instanceof JaxbListValue) {
			List<?> bList = (List<?>) b;
			List<Object> vList = new ArrayList<>(bList.size());
			for (Object lv: bList) {
				vList.add(unmarshal(lv));
			}
			
			return vList;
		}
		
		return dateAdapter.unmarshal(b);
	}

}
