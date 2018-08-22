/*
 * Copyright (C) 2016 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.lob.lobstore.multi;

import java.util.List;

import org.iplass.mtp.impl.lob.LobStoreService;
import org.iplass.mtp.impl.lob.lobstore.LobData;
import org.iplass.mtp.impl.lob.lobstore.LobStore;
import org.iplass.mtp.impl.lob.lobstore.LobValidator;
import org.iplass.mtp.spi.Config;

/**
 * 複数のLobStoreを定義可能なLobStore。
 * 追加は常に先頭のlobStoreに行う。
 * 
 * @author K.Higuchi
 *
 */
public class AddableMultiLobStore implements LobStore {
	
	private List<LobStore> lobStore;
	private LobValidator lobValidator;

	public List<LobStore> getLobStore() {
		return lobStore;
	}

	public void setLobStore(List<LobStore> lobStore) {
		this.lobStore = lobStore;
	}

	@Override
	public void inited(LobStoreService service, Config config) {
		if (lobStore != null) {
			for (LobStore ls: lobStore) {
				ls.inited(service, config);
			}
		}
	}

	@Override
	public void destroyed() {
		if (lobStore != null) {
			for (LobStore ls: lobStore) {
				ls.destroyed();
			}
		}
	}

	@Override
	public LobData create(int tenantId, long lobDataId) {
		return lobStore.get(0).create(tenantId, lobDataId);
	}

	@Override
	public LobData load(int tenantId, long lobDataId) {
		LobData ld = null;
		if (lobStore != null) {
			for (int i = 0; i < lobStore.size(); i++) {
				ld = lobStore.get(i).load(tenantId, lobDataId);
				if (i == lobStore.size() - 1) {
					//last one
					return ld;
				}
				if (ld != null && ld.exists()) {
					return ld;
				}
			}
		}
		
		return ld;
	}

	@Override
	public void remove(int tenantId, long lobDataId) {
		if (lobStore != null) {
			for (LobStore ls: lobStore) {
				ls.remove(tenantId, lobDataId);
			}
		}
	}

	@Override
	public LobValidator getLobValidator() {
		return lobValidator;
	}

	public void setLobValidator(LobValidator lobValidator) {
		this.lobValidator = lobValidator;
	}
	
}
