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

package org.iplass.mtp.impl.lob;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.iplass.mtp.entity.EntityConcurrentUpdateException;
import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.impl.lob.lobstore.LobData;
import org.iplass.mtp.impl.lob.lobstore.LobStore;
import org.iplass.mtp.impl.lob.lobstore.LobValidator;
import org.iplass.mtp.impl.lob.lobstore.rdb.RdbLobStore;
import org.iplass.mtp.impl.util.CoreResourceBundleUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Lobは、Copy on Write方式で実装。
 * 実際のLobDataの保存先は切り替え可能。
 *
 * @author K.Higuchi
 *
 */
public class Lob {

	public static final String STATE_VALID = "V";
	public static final String STATE_TEMP = "T";

	static final long IS_NEW = -1;
	static final long IS_NOT_INIT = -2;


	private int tenantId;
	private long lobId;
	private String name;
	private String type;

	private String definitionId;
	private String propertyId;
	private String oid;
	private Long version;
	private String sessionId;
	private String status;
	private long lobDataId;

	private long prevLobDataId = IS_NOT_INIT;
	private LobData lobData;

	private LobStore lobStore;

	private LobDao dao;
	private boolean updateSize;
	 
	public Lob(int tenantId, long lobId, String name, String type,
			String definitionId, String propertyId, String oid, Long version, String sessionId, String status, long lobDataId, LobStore lobStore, LobDao dao, boolean updateSize) {
		this.tenantId = tenantId;
		this.lobId = lobId;
		this.name = name;
		this.type = type;
		this.definitionId = definitionId;
		this.propertyId = propertyId;
		this.oid = oid;
		this.version = version;
		this.sessionId = sessionId;
		this.status = status;
		this.lobDataId = lobDataId;
		this.lobStore = lobStore;
		this.dao = dao;
		if (lobStore instanceof RdbLobStore) {
			//RdbLobStore側の処理で一括で保存する
			this.updateSize = false;
		} else {
			this.updateSize = updateSize;
		}
	}

	public boolean isUpdateSize() {
		return updateSize;
	}

	public LobDao getDao() {
		return dao;
	}

	public LobStore getLobStore() {
		return lobStore;
	}

	public LobData getLobData() {
		if (lobData == null && lobDataId > 0) {
			lobData = lobStore.load(tenantId, lobDataId);
		}
		return lobData;
	}

	public InputStream getBinaryInputStream() {
		if (getLobData() != null) {
			return getLobData().getBinaryInputStream();
		} else {
			return null;
		}
	}

	public OutputStream getBinaryOutputStream() {
		allocateLobData(null);
		
		OutputStream os = null;
		LobValidator lv = lobStore.getLobValidator();
		if (lv == null) {
			os = lobData.getBinaryOutputStream();
		} else {
			os = new LobValidatedOutputStream(lobData.getBinaryOutputStream(), this, lv);
		}
		
		if (updateSize) {
			os = new SizeUpdateOutputStream(os, tenantId, dao);
		}
		return os;
	}
	
	private void allocateLobData(Long size) {
		if (lobDataId == IS_NEW) {
			//新規にLobDataをロケート
			prevLobDataId = lobDataId;
			lobDataId = lobId;
			if (!dao.updateLobDataId(tenantId, lobId, prevLobDataId, lobDataId)) {
				throw new EntityConcurrentUpdateException(resourceString("impl.lob.Lob.cantUpdate"));
			}
			//参照カウント用のレコード追加
			dao.initLobData(tenantId, lobDataId, size);

			lobData = lobStore.create(tenantId, lobDataId);
		} else if (prevLobDataId == IS_NOT_INIT) {
			//更新用に新しいLobDataをロケート
			prevLobDataId = lobDataId;
			lobDataId = dao.nextLobDataId(tenantId);
			if (!dao.updateLobDataId(tenantId, lobId, prevLobDataId, lobDataId)) {
				throw new EntityConcurrentUpdateException(resourceString("impl.lob.Lob.cantUpdate"));
			}

			//すげ変える前のLobDataの参照カウントの更新
			dao.refCountUp(tenantId, prevLobDataId, -1);

			//新しい参照カウント用のレコード追加
			dao.initLobData(tenantId, lobDataId, size);

			lobData = lobStore.create(tenantId, lobDataId);
		}
	}
	
	public void transferFrom(File file) throws IOException {
		Long size = null;
		if (updateSize) {
			size  = file.length();
		}
		allocateLobData(size);
		
		LobValidator lv = lobStore.getLobValidator();
		if (lv == null) {
			lobData.transferFrom(file);
		} else {
			byte[] buf = new byte[8192];
			int count;
			try (InputStream is = new FileInputStream(file);
					OutputStream los = lobData.getBinaryOutputStream();
					OutputStream os = new LobValidatedOutputStream(los, this, lv)) {
				while ((count = is.read(buf)) != -1) {
					os.write(buf, 0, count);
				}
				os.flush();
			}
		}
	}
	
	public long getLobId() {
		return lobId;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public byte[] getByte() {
		InputStream is = getBinaryInputStream();
		if (is == null) {
			return null;
		}
		byte[] buf = new byte[8192];
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		int count;
		try {
			while ((count = is.read(buf)) != -1) {
				os.write(buf, 0, count);
			}
			os.flush();
			return os.toByteArray();
		} catch (IOException e) {
			throw new EntityRuntimeException(e);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				logFailResourceCleaning(e);
			}
		}
	}

	public void setByte(byte[] binary) {

		OutputStream os = getBinaryOutputStream();
		byte[] buf = new byte[8192];
		ByteArrayInputStream is = new ByteArrayInputStream(binary);
		int count;
		try {
			while ((count = is.read(buf)) != -1) {
				os.write(buf, 0, count);
			}
			os.flush();
		} catch (IOException e) {
			throw new EntityRuntimeException(e);
		} finally {
			try {
				os.close();
			} catch (IOException e) {
				logFailResourceCleaning(e);
			}
		}
	}

	private void logFailResourceCleaning(Exception e) {
		Logger logger = LoggerFactory.getLogger(this.getClass());
		logger.error("Fail to close BinaryData's Stream. Check whether resource is leak or not.", e);
	}

	public long getSize() {
		if (getLobData() != null) {
			return getLobData().getSize();
		} else {
			return 0;
		}
	}

	public String getDefinitionId() {
		return definitionId;
	}


	public String getOid() {
		return oid;
	}

	public Long getVersion() {
		return version;
	}


	public String getPropertyId() {
		return propertyId;
	}


	public String getSessionId() {
		return sessionId;
	}


	public String getStatus() {
		return status;
	}

	public int getTenantId() {
		return tenantId;
	}

	public long getLobDataId() {
		return lobDataId;
	}

	public long getPrevLobDataId() {
		return prevLobDataId;
	}

	private static String resourceString(String key, Object... arguments) {
		return CoreResourceBundleUtil.resourceString(key, arguments);
	}
}
