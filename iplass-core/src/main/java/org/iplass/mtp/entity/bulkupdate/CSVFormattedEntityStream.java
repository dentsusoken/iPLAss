/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.entity.bulkupdate;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.bulkupdate.BulkUpdateEntity.UpdateMethod;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.impl.entity.csv.EntityCsvReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CSV形式で記述されたStreamをソースとして実行するBulkUpdatable。<br>
 * CSVデータストリームはヘッダー行と、各Entityを1行のCSVデータで表現する複数行のデータとして構成されている前提。
 *
 * CSVデータストリームの例
 * <pre>
 * _useCtrl,oid,name,propA,propB[0],propB[1],propC,refX,...
 * I,,"hoge,hoge",15,a,b,"2014-12-11 13:24:00.000","100234.0,100235.0",...
 * U,2012,"fuga",15,a,b,"2014-12-12 10:00:00.000","100531.0",...
 * :
 * :
 * </pre>
 *
 * <h5>_useCtrl列</h5>
 * <p>
 * _useCtrl列は、制御フラグ列。
 * </p>
 * <table border="1" cellspacing="0">
 * <tr><th>フラグ</th><th>説明</th></tr>
 * <tr><td>I</td><td>追加（{@link UpdateMethod#INSERT}）</td></tr>
 * <tr><td>U</td><td>更新（{@link UpdateMethod#UPDATE}）</td></tr>
 * <tr><td>D</td><td>削除（{@link UpdateMethod#DELETE}）</td></tr>
 * <tr><td>M</td><td>マージ（{@link UpdateMethod#MERGE}）</td></tr>
 * </table>
 * <p>
 * ※_useCtrl列が存在しない場合は、oidがある行は、MERGE、oidがない行はINSERTと判断され処理される。<br>
 * </p>
 * <h5>多重度が複数のプロパティ（参照型除く）</h5>
 * <p>
 * 多重度複数の場合は、プロパティ名の後に配列添え字[n]を指定する。<br>
 * Select型は、value（コード）値を指定。<br>
 * 日時型は、yyyy-MM-dd HH:mm:ss.SSS形式。<br>
 * 日付型は、yyyy-MM-dd形式。<br>
 * 時間型は、HH:mm:ss形式。<br>
 *
 *
 * ※現状、BinaryReference未対応
 * </p>
 * <h5>参照型のプロパティ</h5>
 * <p>
 * [oid].[version]の形式で1参照を表す。
 * 複数Entityへの参照を示す場合は、カンマで区切る（CSVの要素となるので、全体を"で囲う）。<br>
 * 例：<br>
 * 1Entityへの参照の表現：1234.0<br>
 * 複数Entityへの参照の表現："1234.0,1235.0,1236.0"<br>
 * </p>
 *
 * @author K.Higuchi
 *
 */
public class CSVFormattedEntityStream implements BulkUpdatable {
	private static final Logger logger = LoggerFactory.getLogger(CSVFormattedEntityStream.class);

	/**
	 * 制御フラグのヘッダー項目名
	 */
	public static final String CTRL_CODE_KEY = EntityCsvReader.CTRL_CODE_KEY;

	/**
	 * 制御フラグ、追加を示す値。
	 */
	public static final String CTRL_INSERT = EntityCsvReader.CTRL_INSERT;
	/**
	 * 制御フラグ、更新を示す値。
	 */
	public static final String CTRL_UPDATE = EntityCsvReader.CTRL_UPDATE;
	/**
	 * 制御フラグ、削除を示す値。
	 */
	public static final String CTRL_DELETE = EntityCsvReader.CTRL_DELETE;
	/**
	 * 制御フラグ、マージ(追加更新)を示す値。
	 */
	public static final String CTRL_MERGE = EntityCsvReader.CTRL_MERGE;;


	private String definitionName;
	private List<String> updateProperties;
	private boolean enableAuditPropertySpecification;
	private Reader reader;
	private EntityCsvReader esReader;

	/**
	 * 文字コードUTF-8でCSVFormattedEntityStreamを構築。
	 *
	 * @param definitionName
	 * @param inputStream
	 */
	public CSVFormattedEntityStream(String definitionName, InputStream inputStream) {
		this.definitionName = definitionName;
		try {
			this.reader = new InputStreamReader(inputStream, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new EntityRuntimeException(e);
		}
	}

	/**
	 * 指定の文字コードで、CSVFormattedEntityStreamを構築。
	 *
	 * @param definitionName
	 * @param inputStream
	 * @param charset
	 */
	public CSVFormattedEntityStream(String definitionName, InputStream inputStream, String charset) {
		this.definitionName = definitionName;
		try {
			this.reader = new InputStreamReader(inputStream, charset);
		} catch (UnsupportedEncodingException e) {
			throw new EntityRuntimeException(e);
		}
	}

	/**
	 * 指定のReaderで、CSVFormattedEntityStreamを構築。
	 * @param definitionName
	 * @param reader
	 */
	public CSVFormattedEntityStream(String definitionName, Reader reader) {
		this.definitionName = definitionName;
		this.reader = reader;
	}

	/**
	 * 文字コードUTF-8、更新時の更新対象updatePropertiesでCSVFormattedEntityStreamを構築。
	 *
	 * @param definitionName
	 * @param updateProperties
	 * @param inputStream
	 */
	public CSVFormattedEntityStream(String definitionName, List<String> updateProperties, InputStream inputStream) {
		this.definitionName = definitionName;
		this.updateProperties = updateProperties;
		try {
			this.reader = new InputStreamReader(inputStream, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new EntityRuntimeException(e);
		}
	}

	public CSVFormattedEntityStream(String definitionName, List<String> updateProperties, InputStream inputStream, String charset) {
		this.definitionName = definitionName;
		this.updateProperties = updateProperties;
		try {
			this.reader = new InputStreamReader(inputStream, charset);
		} catch (UnsupportedEncodingException e) {
			throw new EntityRuntimeException(e);
		}
	}

	public CSVFormattedEntityStream(String definitionName, List<String> updateProperties, Reader reader) {
		this.definitionName = definitionName;
		this.updateProperties = updateProperties;
		this.reader = reader;
	}

	@SuppressWarnings("resource")
	@Override
	public Iterator<BulkUpdateEntity> iterator() {
		if (esReader != null) {
			throw new EntityRuntimeException("concurrent iterate not supported on CSVFormattedEntityStream.");
		}

		EntityDefinitionManager edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
		EntityDefinition def = edm.get(definitionName);
		if (def == null) {
			throw new EntityRuntimeException(definitionName + " definition not found.");
		}
		esReader = new EntityCsvReader(def, reader).withReferenceVersion(true);
		return new It(esReader.iterator());
	}

	@Override
	public String getDefinitionName() {
		return definitionName;
	}

	@Override
	public void close() {
		if (esReader != null) {
			try {
				esReader.close();
			} catch (Exception e) {
				logger.error("Fail to close CSVFormattedEntityStream Resource. Check whether resource is leak or not.", e);
			}
			esReader = null;
			reader = null;
		}
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				logger.error("Fail to close CSVFormattedEntityStream Resource. Check whether resource is leak or not.", e);
			}
			reader = null;
		}
	}

	@Override
	public List<String> getUpdateProperties() {
		return updateProperties;
	}

	@Override
	public boolean isEnableAuditPropertySpecification() {
		return enableAuditPropertySpecification;
	}

	public void setEnableAuditPropertySpecification(boolean enableAuditPropertySpecification) {
		this.enableAuditPropertySpecification = enableAuditPropertySpecification;
	}


	private class It implements Iterator<BulkUpdateEntity> {

		Iterator<Entity> internal;

		private It(Iterator<Entity> internal) {
			this.internal = internal;
		}

		@Override
		public boolean hasNext() {
			return internal.hasNext();
		}

		@Override
		public BulkUpdateEntity next() {
			Entity e = internal.next();

			String ctrl = e.getValue(CTRL_CODE_KEY);
			if (ctrl == null) {
				if (e.getValue(Entity.OID) == null) {
					ctrl = CTRL_INSERT;
				} else {
					ctrl = CTRL_MERGE;
				}
			}

			switch (ctrl) {
			case CTRL_DELETE:
				return new BulkUpdateEntity(UpdateMethod.DELETE, e);
			case CTRL_INSERT:
				return new BulkUpdateEntity(UpdateMethod.INSERT, e);
			case CTRL_MERGE:
				return new BulkUpdateEntity(UpdateMethod.MERGE, e);
			case CTRL_UPDATE:
				return new BulkUpdateEntity(UpdateMethod.UPDATE, e);
			default:
				throw new IllegalArgumentException("unsupprot controll flag");
			}
		}
	}
}
