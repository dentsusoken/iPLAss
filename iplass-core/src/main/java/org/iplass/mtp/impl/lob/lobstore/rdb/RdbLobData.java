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

package org.iplass.mtp.impl.lob.lobstore.rdb;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.iplass.mtp.impl.lob.lobstore.LobData;
import org.iplass.mtp.impl.lob.lobstore.LobStoreRuntimeException;
import org.iplass.mtp.impl.lob.sql.LobStoreSearchSql;
import org.iplass.mtp.impl.lob.sql.LobStoreUpdateSql;
import org.iplass.mtp.impl.rdb.SqlExecuter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RdbLobData implements LobData {

	private static Logger logger = LoggerFactory.getLogger(RdbLobData.class);

	private int tenantId;
	private long lobDataId;

	private boolean manageLobSizeOnRdb;
	private RdbAdapter rdb;

	private long sizeCache = -1;


	public RdbLobData(int tenantId, long lobDataId, boolean manageLobSizeOnRdb, RdbAdapter rdb) {
		this.tenantId = tenantId;
		this.lobDataId = lobDataId;
		this.manageLobSizeOnRdb = manageLobSizeOnRdb;
		this.rdb = rdb;
	}


	@Override
	public long getLobDataId() {
		return lobDataId;
	}

	@Override
	public long getSize() {
		if (sizeCache == -1) {
			SqlExecuter<Long> exec = new SqlExecuter<Long>() {
				@Override
				public Long logic() throws SQLException {
					LobStoreSearchSql sqlCreator = rdb.getQuerySqlCreator(LobStoreSearchSql.class);
					String sql = sqlCreator.toSql(tenantId, lobDataId, rdb);

					ResultSet rs = getStatement().executeQuery(sql);
					try {
						if (rdb.isSupportBlobType()) {
							Blob blob = sqlCreator.getBlob(rs, rdb);
							if (blob == null) {
								return 0L;
							} else {
								return blob.length();
							}
						} else {
							try (InputStream is = sqlCreator.getBinaryStream(rs, rdb)) {
								if (is == null) {
									return 0L;
								} else {
									try {
										return (long) is.available();
									} catch (IOException e) {
										throw new LobStoreRuntimeException(e);
									}
								}
							} catch (IOException e) {
								throw new LobStoreRuntimeException(e);
							}
						}
					} finally {
						rs.close();
					}
				}
			};
			sizeCache = exec.execute(rdb, true);
		}

		return sizeCache;
	}

	@Override
	public boolean exists() {
		if (sizeCache == -1) {
			getSize();
		}
		return sizeCache > 0L;
	}

	@Override
	public InputStream getBinaryInputStream() {
		return new LobInputStream();
	}

	@Override
	public OutputStream getBinaryOutputStream() {
		return new LobOutputStream();
	}

	@Override
	public void transferFrom(File file) throws IOException {
		byte[] buf = new byte[8192];
		int count;
		try (InputStream is = new FileInputStream(file);
				OutputStream os = getBinaryOutputStream()) {
			while ((count = is.read(buf)) != -1) {
				os.write(buf, 0, count);
			}
			os.flush();
		}
	}

	private class LobInputStream extends InputStream {

		private SqlExecuter<InputStream> exec;
		private ResultSet rs;
		private Blob blob;
		private InputStream wrapped;

		private boolean closed;

		LobInputStream() {
			this.exec = new SqlExecuter<InputStream>() {

				@Override
				public InputStream logic() throws SQLException {

					LobStoreSearchSql sql = rdb.getQuerySqlCreator(LobStoreSearchSql.class);
					rs = getStatement().executeQuery(sql.toSql(tenantId, lobDataId, rdb));

					if (rdb.isSupportBlobType()) {
						blob = sql.getBlob(rs, rdb);
						if (blob == null) {
							sizeCache = 0;
							return null;
						}

						//SRB(Unreleased Resource: Streams)対象外
						//呼び出し側で利用するので、InputStreamは開いたまま
						InputStream is = blob.getBinaryStream();
						sizeCache = blob.length();
						return is;
					} else {
						//SRB(Unreleased Resource: Streams)対象外
						//呼び出し側で利用するので、InputStreamは開いたまま
						InputStream is = sql.getBinaryStream(rs, rdb);
						if (is == null) {
							sizeCache = 0;
							return null;
						}

						try {
							// PostgreSQLのJDBCではResultSet#getBinaryStreamで返すInputStreamがByteArrayInputStreamのため
							// サイズの取得にInputStream#availableを使用しても問題はなし
							// https://docs.oracle.com/javase/jp/8/docs/api/java/io/ByteArrayInputStream.html#available--
							sizeCache = is.available();
						} catch (IOException e) {
							//例外スローする場合は閉じる
							try {
								is.close();
							} catch (IOException e1) {
								e.addSuppressed(e1);
							}
							throw new LobStoreRuntimeException(e);
						}
						return is;
					}
				}
			};
			this.wrapped = exec.execute(rdb, false);
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
			if (!closed) {
				try {
					try {
						if (wrapped != null) {
							wrapped.close();
						}
						if (blob != null) {
							blob.free();
						}

					} finally {
						if (rs != null) {
							rs.close();
						}
					}
				} catch (SQLException se) {
					throw new IOException(se);
				} finally {
					if (exec != null) {
						exec.close();
					}
				}
				closed = true;
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

	private class LobOutputStream extends OutputStream {

		private LobOutputStreamSqlExecuter exec;

		LobOutputStream() {
			exec = new LobOutputStreamSqlExecuter();
			exec.doInit = true;
			if (!exec.execute(rdb, false)) {
				//プログラムから更新の場合、OBJ_BLOBでロックしてるので、発生しえないはず。なのでシステム例外。
				throw new LobStoreRuntimeException("Concurrent Update Occured.");
			}
		}

		@Override
		public void close() throws IOException {
			try {
				if (exec != null && exec.os != null) {
					exec.os.flush();
					exec.os.close();
				}
			} finally {

				if (exec != null) {
					exec.doInit = false;
					exec.execute(rdb, true);
				}
				exec = null;
				sizeCache = -1;
			}
		}

		@Override
		public void flush() throws IOException {
			if (exec.os != null) {
				exec.os.flush();
			}
		}

		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			if (exec == null) {
				throw new IOException("already closed.");
			}
			//SRB対応-チェック済み
			exec.os.write(b, off, len);
		}

		@Override
		public void write(byte[] b) throws IOException {
			if (exec == null) {
				throw new IOException("already closed.");
			}
			//SRB対応-チェック済み
			exec.os.write(b);
		}

		@Override
		public void write(int b) throws IOException {
			if (exec == null) {
				throw new IOException("already closed.");
			}
			exec.os.write(b);
		}
	}

	private class LobOutputStreamSqlExecuter extends SqlExecuter<Boolean> {

		boolean doInit;
		Blob blob;
		OutputStream os;

		LobOutputStreamSqlExecuter() {
			doInit = true;
		}

		@Override
		public Boolean logic() throws SQLException {
			if (doInit) {
				return initBlob();
			} else {
				return storeBlob();
			}
		}

		private Boolean initBlob() throws SQLException {
			if (rdb.isSupportBlobType()) {
				blob = getConnection().createBlob();
				os = blob.setBinaryStream(1L);
			} else {
				os = new ByteArrayOutputStream();
			}
			return Boolean.TRUE;
		}


		private Boolean storeBlob() throws SQLException {

			InputStream is = null;
			try {
				LobStoreUpdateSql sql = rdb.getUpdateSqlCreator(LobStoreUpdateSql.class);
				PreparedStatement stmt = getPreparedStatement(sql.toPrepareSqlForLobUpdate(manageLobSizeOnRdb, rdb));
				int i = 1;
				if (rdb.isSupportBlobType()) {
					stmt.setBlob(i++, blob);
					if (manageLobSizeOnRdb) {
						stmt.setLong(i++, blob.length());
					}
				} else {
					byte[] lobBytes = ((ByteArrayOutputStream) os).toByteArray();
					stmt.setBytes(i++, lobBytes);
					if (manageLobSizeOnRdb) {
						stmt.setLong(i++, lobBytes.length);
					}
				}
				stmt.setInt(i++, tenantId);
				stmt.setLong(i++, lobDataId);
				if (stmt.executeUpdate() != 1) {
					//プログラムから更新の場合、OBJ_BLOBでロックしてるので、発生しえないはず。なのでシステム例外。
					throw new LobStoreRuntimeException("Concurrent Update Occured.");
				}
				if (blob != null) {
					blob.free();
				}
				return Boolean.TRUE;
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						logger.warn("can not close inputstream resource:" + is, e);
					}
				}
			}
		}
	}

}
