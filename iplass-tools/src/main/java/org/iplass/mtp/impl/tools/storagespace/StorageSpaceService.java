/*
 * Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.tools.storagespace;

import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.spi.Service;

public interface StorageSpaceService extends Service {

	/**
	 * StorageSpaceを移行します。
	 * <p>
	 * EntityのStorageSpaceを変更とデータの移行を行います。<br/>
	 * 移行元のデータは削除されません。移行元データの削除には{@link #cleanup(int, String, MetaEntity)}を使用してください。
	 * </p>
	 *
	 * @param storageSpaceName 移行先のStorageSpace名
	 * @param entityDefinition 移行するEntity定義
	 */
	void migrate(String storageSpaceName, EntityDefinition entityDefinition);

	/**
	 * StorageSpaceをクリーンアップします。
	 * <p>
	 * StorageSpaceから指定されたEntityのデータをすべて削除します。
	 * </p>
	 *
	 * @param tenantId テナントID
	 * @param storageSpaceName StorageSpace名
	 * @param metaEntity Entityメタデータ
	 */
	void cleanup(int tenantId, String storageSpaceName, MetaEntity metaEntity);

}
