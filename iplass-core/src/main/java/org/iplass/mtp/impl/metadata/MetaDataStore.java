/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.metadata;

import java.util.List;

import org.iplass.mtp.spi.ServiceInitListener;


public interface MetaDataStore extends ServiceInitListener<MetaDataRepository> {

	//TODO インタフェースは再検討
	//TODO バージョニング
	//TODO ロック（ステータスの変更）

	//TODO XMLTypeの使用

	public MetaDataEntry loadById(int tenantId, String id);

	public MetaDataEntry loadById(int tenantId, String id, int version);

	public List<MetaDataEntryInfo> definitionList(int tenantId, String prefixPath) throws MetaDataRuntimeException;
	
	public List<MetaDataEntryInfo> definitionList(int tenantId, String prefixPath, boolean withInvalid) throws MetaDataRuntimeException;

	public MetaDataEntry load(int tenantId, String path) throws MetaDataRuntimeException;

	public MetaDataEntry load(int tenantId, String path, int version) throws MetaDataRuntimeException;

	public void store(int tenantId, MetaDataEntry metaDataEntry) throws MetaDataRuntimeException;

	public void update(int tenantId, MetaDataEntry metaDataEntry) throws MetaDataRuntimeException;

	public void remove(int tenantId, String path) throws MetaDataRuntimeException;

	public void purgeById(int tenantId, String id) throws MetaDataRuntimeException;

	public void updateConfigById(int tenantId, String id, MetaDataConfig config);

	public List<MetaDataEntryInfo> getHistoryById(int tenantId, String id);
	
	public List<Integer> getTenantIdsOf(String id);

}
