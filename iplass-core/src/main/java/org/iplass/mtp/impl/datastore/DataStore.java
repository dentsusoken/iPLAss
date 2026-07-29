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

package org.iplass.mtp.impl.datastore;

import org.iplass.mtp.impl.datastore.strategy.ApplyMetaDataStrategy;
import org.iplass.mtp.impl.datastore.strategy.EntityStoreStrategy;
import org.iplass.mtp.impl.entity.MetaStoreMapping;
import org.iplass.mtp.spi.ServiceInitListener;

public abstract class DataStore implements ServiceInitListener<StoreService> {

	public abstract ApplyMetaDataStrategy getApplyMetaDataStrategy();

	public abstract EntityStoreStrategy getEntityStoreStrategy();

	public abstract MetaEntityStore newEntityStoreInstance();

	public abstract Class<? extends MetaEntityStore> getEntityStoreType();

	public abstract int stringPropertyStoreMaxLength(MetaStoreMapping metaStoreMapping);

	/**
	 * SELECT DISTINCT時に、ORDER BYで指定した式（列そのものでなく式自体）がSELECT句に
	 * 含まれていないとエラーとなるDataStoreを利用している場合に<code>true</code>を返却。
	 */
	public abstract boolean isRequireOrderByExpressionInSelectForDistinct();
}
