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

package org.iplass.mtp.impl.datastore;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.impl.datastore.grdb.MetaGRdbMultiplePropertyStore;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbPropertyStore;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.metadata.MetaData;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(value={MetaGRdbPropertyStore.class, MetaGRdbMultiplePropertyStore.class})
public abstract class MetaPropertyStore implements MetaData {
	private static final long serialVersionUID = 6456276072021992839L;

	public abstract MetaPropertyStore copy();
	
	public abstract PropertyStoreHandler createRuntime(PropertyHandler property, MetaEntity metaEntity);
	
}
