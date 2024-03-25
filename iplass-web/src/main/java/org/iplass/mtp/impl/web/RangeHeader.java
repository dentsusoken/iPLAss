/*
 * Copyright (C) 2016 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.io.input.BoundedInputStream;

public class RangeHeader {
	static final long UNSPEC = -1;
	static final long NAN = -2;
	
	
	public static RangeHeader getRangeHeader(WebRequestStack req, long contentSize) {
		String rangeValue = req.getRequest().getHeader("Range");
		if (rangeValue != null) {
			return new RangeHeader(rangeValue, contentSize);
		} else {
			return null;
		}
	}
	
	public static long writeResponseHeader(WebRequestStack req, RangeHeader range, long contentSize) {
		req.getResponse().setHeader("Accept-Ranges", "bytes");
		if (range != null) {
			if (range.valid) {
				req.getResponse().setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
				req.getResponse().setHeader("Content-Range", "bytes " + range.start + "-" + range.end + "/" + range.totalSize);
				return range.end - range.start + 1;
			} else {
//				//FIXME エラーでよいか？？マルチパート対応してない
//				requestContext.getResponse().setStatus(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
//				return;
			}
		}
		return contentSize;
	}
	
	public static void writeResponseBody(InputStream is, OutputStream os, RangeHeader range) throws IOException {
		//Range指定の場合は、部分的な出力に
		if (range != null && range.valid) {
			is = new BoundedInputStream(is, range.end + 1);
			is.skip(range.start);
		}

		byte[] buf = new byte[8192 * 2];//TODO oracleのBLOBの場合、blobのフェッチサイズに合わせたほうがよい？
//				byte[] buf = new byte[8132 * 10];

		int count;
		while ((count = is.read(buf)) != -1) {
			//SRB対応-チェック済み
			os.write(buf, 0, count);
		}
		os.flush();
	}
	
	
	public long start;
	public long end;
	public long totalSize;
	public boolean valid;
	
	public RangeHeader(String value, long totalSize) {
		this.totalSize = totalSize;
		if (value != null) {
			if (value.startsWith("bytes=")) {
				//FIXME マルチパートは未対応
				int splitIndex = value.indexOf('-');
				if (splitIndex >= 0) {
					start = toLong(value.substring(6, splitIndex));
					end = toLong(value.substring(splitIndex + 1));
				}
			}
		}
		
		if (start == NAN || end == NAN) {
			valid = false;
		} else {
			if (start == UNSPEC) {
				start = totalSize - end;
				end = totalSize - 1;
			} else if (end == UNSPEC || end > totalSize - 1) {
				end = totalSize - 1;
			}
			if (start > end) {
				valid = false;
			} else {
				valid = true;
			}
		}
	}
	
	private long toLong(String val) {
		if (val == null || val.length() == 0) {
			return UNSPEC;
		} else {
			try {
				return Long.parseLong(val);
			} catch (NumberFormatException e) {
				return NAN;
			}
		}
	}
}