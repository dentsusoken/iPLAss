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

package org.iplass.mtp.impl.lob.lobstore.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.iplass.mtp.SystemException;
import org.iplass.mtp.impl.lob.lobstore.LobData;
import org.iplass.mtp.impl.lob.lobstore.LobStoreRuntimeException;
import org.iplass.mtp.impl.lob.sql.LobStoreUpdateSql;
import org.iplass.mtp.impl.rdb.SqlExecuter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.transaction.TransactionService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.transaction.TransactionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileLobData implements LobData {

	private static final Logger logger = LoggerFactory.getLogger(FileLobData.class);

	private boolean overwriteFile;
	private String rootDir;
	private int tenantId;
	private long lobDataId;

	private File blobFile;

	private boolean manageLobSizeOnRdb;
	private RdbAdapter rdb;

	public FileLobData(int tenantId, long lobDataId, String rootDir, boolean overwriteFile, boolean manageLobSizeOnRdb, RdbAdapter rdb) {
		this.tenantId = tenantId;
		this.lobDataId = lobDataId;
		this.rootDir = rootDir;
		this.overwriteFile = overwriteFile;
		this.manageLobSizeOnRdb = manageLobSizeOnRdb;
		this.rdb = rdb;
	}

	@Override
	public long getLobDataId() {
		return lobDataId;
	}

	@Override
	public long getSize() {
		if (blobFile == null) {
			blobFile = toFile(lobDataId);
		}
		return blobFile.length();
	}

	@Override
	public InputStream getBinaryInputStream() {
		return new FileBinaryDataInputStream();
	}

	@Override
	public OutputStream getBinaryOutputStream() {
		return new FileBinaryDataOutputStream();
	}

	public String getFilePath() {
		if (blobFile == null) {
			blobFile = toFile(lobDataId);
		}
		return blobFile.getAbsolutePath();
	}

	@Override
	public boolean exists() {
		if (blobFile == null) {
			blobFile = toFile(lobDataId);
		}
		return blobFile.exists();
	}

	File toFile(Long id) {
		String idStr = id.toString();
		//newIdを1文字単位で逆順にディレクトリに
		StringBuilder sb = new StringBuilder();
		sb.append(rootDir);
		sb.append(File.separator);
		sb.append(tenantId);
		for (int i = idStr.length() - 1; i >= 0; i--) {
			sb.append(File.separator);
			sb.append(idStr.charAt(i));
		}
		sb.append(".dat");
		return new File(sb.toString());
	}

	private File createNewFile(Long newId) {

		File newFile = toFile(newId);
		File dir = newFile.getParentFile();
		if (!dir.mkdirs()) {
			//他のスレッドにより作られたりした場合
			if (!dir.isDirectory()) {
				throw new SystemException("can not create dir:" + dir.getAbsolutePath());
			}
		}
		try {
			if (newFile.createNewFile()) {
				return newFile;
			} else {
				if (overwriteFile && newFile.exists()) {
					logger.warn("Lob file already exists. But overwrite File mode is ture, so continue process... file: " + newFile.toString());
					return newFile;
				} else {
					throw new SystemException("can not create file:" + newFile.getAbsolutePath());
				}
			}
		} catch (IOException e) {
			throw new SystemException("can not create file:" + newFile.getAbsolutePath() + ", cause " + e, e);
		}
	}

	private class FileBinaryDataOutputStream extends OutputStream {
		private FileOutputStream wrapped;

		FileBinaryDataOutputStream() {
			if (blobFile == null) {
				blobFile = createNewFile(lobDataId);
			}
			try {
				wrapped = new FileOutputStream(blobFile);
			} catch (FileNotFoundException e) {
				throw new SystemException("can not open FileOutputStream:" + blobFile.getAbsolutePath(), e);
			}
			Transaction t = ServiceRegistry.getRegistry().getService(TransactionService.class).getTransacitonManager().currentTransaction();
			if (t != null && t.getStatus() == TransactionStatus.ACTIVE) {
				t.afterRollback(() -> {
						if (!blobFile.delete()) {
							logger.warn("maybe can not delete file:" + blobFile.getAbsolutePath());
						}
				});
			}
		}

		@Override
		public void close() throws IOException {
			if (wrapped != null) {
				try {
					wrapped.flush();
				} finally {
					wrapped.close();

					try {
						//サイズを更新
						storeBlobSize();
					} finally {
						wrapped = null;
					}
				}
			}
		}

		@Override
		public void flush() throws IOException {
			if (wrapped != null) {
				wrapped.flush();
			}
		}

		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			//SRB対応-チェック済み
			wrapped.write(b, off, len);
		}

		@Override
		public void write(byte[] b) throws IOException {
			//SRB対応-チェック済み
			wrapped.write(b);
		}

		@Override
		public void write(int b) throws IOException {
			wrapped.write(b);
		}

		private boolean storeBlobSize() {
			//LobサイズをRdb上で管理しない場合は抜ける
			if (!manageLobSizeOnRdb) {
				return true;
			}

			SqlExecuter<Boolean> exec = new SqlExecuter<Boolean>() {

				@Override
				public Boolean logic() throws SQLException {

					LobStoreUpdateSql sql = rdb.getUpdateSqlCreator(LobStoreUpdateSql.class);
					PreparedStatement stmt = getPreparedStatement(sql.toPrepareSqlForLobSizeUpdate(rdb));
					stmt.setLong(1, getSize());
					stmt.setInt(2, tenantId);
					stmt.setLong(3, lobDataId);
					if (stmt.executeUpdate() != 1) {
						//プログラムから更新の場合、OBJ_BLOBでロックしてるので、発生しえないはず。なのでシステム例外。
						throw new LobStoreRuntimeException("Concurrent Update Occured.");
					}
					return Boolean.TRUE;
				}

			};
			return exec.execute(rdb, true);
		}
	}

	private class FileBinaryDataInputStream extends InputStream {
		private FileInputStream wrapped;

		FileBinaryDataInputStream() {
			if (blobFile == null) {
				blobFile = toFile(lobDataId);
			}

			if (blobFile != null) {
				try {
					wrapped = new FileInputStream(blobFile);
				} catch (FileNotFoundException e) {
					logger.warn("can not find File of lobDataId:" + lobDataId + "(tenant=" + tenantId + "), so return empty data.");
				}
			}
		}

		@Override
		public int available() throws IOException {
			if (wrapped == null) {
				return 0;
			}
			return wrapped.available();
		}

		@Override
		public void close() throws IOException {
			if (wrapped != null) {
				wrapped.close();
			}
		}

		@Override
		public synchronized void mark(int readlimit) {
			if (wrapped != null) {
				wrapped.mark(readlimit);
			}
		}

		@Override
		public boolean markSupported() {
			if (wrapped == null) {
				return false;
			}
			return wrapped.markSupported();
		}

		@Override
		public int read(byte[] b, int off, int len) throws IOException {
			if (wrapped == null) {
				return -1;
			}
			return wrapped.read(b, off, len);
		}

		@Override
		public int read(byte[] b) throws IOException {
			if (wrapped == null) {
				return -1;
			}
			return wrapped.read(b);
		}

		@Override
		public synchronized void reset() throws IOException {
			if (wrapped != null) {
				wrapped.reset();
			}
		}

		@Override
		public long skip(long n) throws IOException {
			if (wrapped == null) {
				return 0;
			}
			return wrapped.skip(n);
		}

		@Override
		public int read() throws IOException {
			if (wrapped == null) {
				return -1;
			}
			return wrapped.read();
		}
	}

}
