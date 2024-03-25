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

package org.iplass.mtp.impl.entity.jaxb;

import java.math.BigDecimal;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.entity.GenericEntity;
import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.impl.xml.jaxb.DateXmlAdapter;
import org.iplass.mtp.impl.xml.jaxb.XmlDate;
import org.iplass.mtp.impl.xml.jaxb.XmlDateTime;
import org.iplass.mtp.impl.xml.jaxb.XmlTime;

/**
 *
 * jaxb用のProperty表現。
 *
 * @author K.Higuchi
 *
 */
@XmlSeeAlso({
	XmlDate.class, XmlDate[].class,
	XmlTime.class, XmlTime[].class,
	XmlDateTime.class, XmlDateTime[].class,
	BinaryReference.class, BinaryReference[].class,
	Boolean[].class,
	BigDecimal[].class,
	Double[].class,
	Long[].class,
	SelectValue.class, SelectValue[].class,
	String[].class,
	GenericEntity[].class
})
@XmlType(name="property")
public class JaxbProperty {

	@XmlAttribute
	String name;

	@XmlElement
	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	Object value;

}
