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

package org.iplass.mtp.command;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Map;

import org.iplass.mtp.impl.util.ConvertUtil;
import org.iplass.mtp.impl.util.CoreResourceBundleUtil;

/**
 * Commandへのリクエストを表すクラスです。
 * （ServletにおけるServletRequest相当のクラスです）
 *
 * @author K.Higuchi
 *
 */
public interface RequestContext {

	// paramとattribteの位置づけの再検討（paramはあくまで、getAttribute("param").get(name)へのショートカットとするとか、、）

	/**
	 * クライアントからの呼び出しパラメータを取得。 （HttpServletRequestのgetParameter相当）
	 *
	 * @param name
	 * @return
	 */
	public String getParam(String name);

	/**
	 * クライアントからの呼び出しパラメータ（複数） を取得します。 （HttpServletRequestのgetParameterValues相当）
	 *
	 *
	 * @param name
	 * @return
	 */
	public String[] getParams(String name);

	/**
	 * クライアントからの呼び出しパラメータを指定の型として取得します。
	 * typeにはプリミティブ側のラッパークラス、String、BigDecimal、SelectValue、
	 * java.sql.Date、Timestamp、Timeを指定可能です。
	 *
	 * @param name
	 * @param type
	 * @return
	 */
	public default <T> T getParam(String name, Class<T> type) {
		return getParam(name, type, null);
	}

	/**
	 * クライアントからの呼び出しパラメータを指定の型として取得します。
	 * typeにはプリミティブ側のラッパークラス、String、BigDecimal、SelectValue、
	 * java.sql.Date、Timestamp、Timeを指定可能です。
	 * 値が指定されていなかった場合は、defaultValueの値が返却されます。
	 *
	 * @param name
	 * @param type
	 * @param defaultValue
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public default <T> T getParam(String name, Class<T> type, T defaultValue) {
		T ret;
		if (type == UploadFileHandle.class) {
			ret = (T) getParamAsFile(name);
		} else {
			try {
				ret = ConvertUtil.convert(type, this.getParam(name));
			} catch (RuntimeException e) {
				throw new IllegalParameterException(resourceString("command.RequestContextWrapper.canNotConv", name), e);
			}
		}
		if (ret == null) {
			return defaultValue;
		} else {
			return ret;
		}
	}
	
	/**
	 * クライアントからの呼び出しパラメータを指定の型の配列として取得します。
	 * typeにはプリミティブ側のラッパークラス、String、BigDecimal、SelectValue、
	 * java.sql.Date、Timestamp、Timeを指定可能です。
	 *
	 * @param name
	 * @param type
	 * @return
	 */
	public default <T> T[] getParams(String name, Class<T> type) {
		return getParams(name, type, null);
	}

	/**
	 * クライアントからの呼び出しパラメータを指定の型の配列として取得します。
	 * typeにはプリミティブ側のラッパークラス、String、BigDecimal、SelectValue、
	 * java.sql.Date、Timestamp、Timeを指定可能です。
	 * 値が指定されていなかった場合は、defaultValuesの値が返却されます。
	 *
	 * @param name
	 * @param type
	 * @param defaultValues
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public default <T> T[] getParams(String name, Class<T> type, T[] defaultValues) {
		T[] ret;
		if (type == UploadFileHandle.class) {
			ret = (T[]) getParamsAsFile(name);
		} else {
			String[] vals = this.getParams(name);
			if (vals == null) {
				ret = null;
			} else if (type == String.class) {
				ret = (T[]) vals;
			} else {
				try {
					ret = (T[]) Array.newInstance(type, vals.length);
					for (int i = 0; i < vals.length; i++) {
						ret[i] = ConvertUtil.convert(type, vals[i]);
					}
				} catch (RuntimeException e) {
					throw new IllegalParameterException(resourceString("command.RequestContextWrapper.canNotConv", name), e);
				}
			}
		}
		
		if (ret == null) {
			return defaultValues;
		} else {
			return ret;
		}
	}
	
	/**
	 * クライアントからの呼び出しパラメータをBoolean型として取得します。
	 * Boolean.valueOf(String)で変換します。
	 * @param name
	 * @return
	 */
	public default Boolean getParamAsBoolean(String name) {
		return getParam(name, Boolean.class);
	}

	/**
	 * クライアントからの呼び出しパラメータをBoolean型配列として取得します。
	 * Boolean.valueOf(String)で変換します。
	 *
	 * @param name
	 * @return
	 */
	public default Boolean[] getParamsAsBoolean(String name) {
		return getParams(name, Boolean.class);
	}

	/**
	 * クライアントからの呼び出しパラメータをLong型として取得します。
	 * Long.valueOf(String)で変換します。
	 * @param name
	 * @return
	 */
	public default Long getParamAsLong(String name) {
		return getParam(name, Long.class);
	}

	/**
	 * クライアントからの呼び出しパラメータをLong型配列として取得します。
	 * Long.valueOf(String)で変換します。
	 * @param name
	 * @return
	 */
	public default Long[] getParamsAsLong(String name) {
		return getParams(name, Long.class);
	}

	/**
	 * クライアントからの呼び出しパラメータをInteger型として取得します。
	 * Integer.valueOf(String)で変換します。
	 * @param name
	 * @return
	 */
	public default Integer getParamAsInt(String name) {
		return getParam(name, Integer.class);
	}

	/**
	 * クライアントからの呼び出しパラメータをInteger型配列として取得します。
	 * Integer.valueOf(String)で変換します。
	 * @param name
	 * @return
	 */
	public default Integer[] getParamsAsInt(String name) {
		return getParams(name, Integer.class);
	}

	/**
	 * クライアントからの呼び出しパラメータをDouble型として取得します。
	 * Double.valueOf(String)で変換します。
	 * @param name
	 * @return
	 */
	public default Double getParamAsDouble(String name) {
		return getParam(name, Double.class);
	}

	/**
	 * クライアントからの呼び出しパラメータをDouble型配列として取得します。
	 * Double.valueOf(String)で変換します。
	 * @param name
	 * @return
	 */
	public default Double[] getParamsAsDouble(String name) {
		return getParams(name, Double.class);
	}

	/**
	 * クライアントからの呼び出しパラメータをjava.sql.Date型として取得します。
	 * 値の文字列のフォーマットはformatで指定します。
	 *
	 * @param name
	 * @param format
	 * @return
	 */
	public default Date getParamAsDate(String name, String format) {
		try {
			return ConvertUtil.convertToDate(Date.class, getParam(name), format, false);
		} catch (RuntimeException e) {
			throw new IllegalParameterException(resourceString("command.RequestContextWrapper.canNotConv", name), e);
		}
	}

	/**
	 * クライアントからの呼び出しパラメータをjava.sql.Date型配列として取得します。
	 * 値の文字列のフォーマットはformatで指定します。
	 *
	 * @param name
	 * @param format
	 * @return
	 */
	public default Date[] getParamsAsDate(String name, String format) {
		Object[] vals = getParams(name);
		if (vals == null) {
			return null;
		}
		if (vals.getClass().getComponentType() == Date.class) {
			return (Date[]) vals;
		}
		try {
			Date[] ret = new Date[vals.length];
			for (int i = 0; i < vals.length; i++) {
				ret[i] = ConvertUtil.convertToDate(Date.class, vals[i], format, false);
			}
			return ret;
		} catch (RuntimeException e) {
			throw new IllegalParameterException(resourceString("command.RequestContextWrapper.canNotConv", name), e);
		}
	}

	/**
	 * クライアントからの呼び出しパラメータをTimestamp型として取得します。
	 * 値の文字列のフォーマットはformatで指定します。
	 *
	 * @param name
	 * @param format
	 * @return
	 */
	public default Timestamp getParamAsTimestamp(String name, String format) {
		try {
			return ConvertUtil.convertToDate(Timestamp.class, getParam(name), format, true);
		} catch (RuntimeException e) {
			throw new IllegalParameterException(resourceString("command.RequestContextWrapper.canNotConv", name), e);
		}
	}

	/**
	 * クライアントからの呼び出しパラメータをTimestamp型配列として取得します。
	 * 値の文字列のフォーマットはformatで指定します。
	 *
	 * @param name
	 * @param format
	 * @return
	 */
	public default Timestamp[] getParamsAsTimestamp(String name, String format) {
		Object[] vals = getParams(name);
		if (vals == null) {
			return null;
		}
		if (vals.getClass().getComponentType() == Timestamp.class) {
			return (Timestamp[]) vals;
		}
		try {
			Timestamp[] ret = new Timestamp[vals.length];
			for (int i = 0; i < vals.length; i++) {
				ret[i] = ConvertUtil.convertToDate(Timestamp.class, vals[i], format, true);
			}
			return ret;
		} catch (RuntimeException e) {
			throw new IllegalParameterException(resourceString("command.RequestContextWrapper.canNotConv", name), e);
		}
	}

	/**
	 * クライアントからの呼び出しパラメータをTime型として取得します。
	 * 値の文字列のフォーマットはformatで指定します。
	 *
	 * @param name
	 * @param format
	 * @return
	 */
	public default Time getParamAsTime(String name, String format) {
		try {
			return ConvertUtil.convertToDate(Time.class, getParam(name), format, false);
		} catch (RuntimeException e) {
			throw new IllegalParameterException(resourceString("command.RequestContextWrapper.canNotConv", name), e);
		}
	}

	/**
	 * クライアントからの呼び出しパラメータをTime型配列として取得します。
	 * 値の文字列のフォーマットはformatで指定します。
	 *
	 * @param name
	 * @param format
	 * @return
	 */
	public default Time[] getParamsAsTime(String name, String format) {
		Object[] vals = getParams(name);
		if (vals == null) {
			return null;
		}
		if (vals.getClass().getComponentType() == Time.class) {
			return (Time[]) vals;
		}
		try {
			Time[] ret = new Time[vals.length];
			for (int i = 0; i < vals.length; i++) {
				ret[i] = ConvertUtil.convertToDate(Time.class, vals[i], format, false);
			}
			return ret;
		} catch (RuntimeException e) {
			throw new IllegalParameterException(resourceString("command.RequestContextWrapper.canNotConv", name), e);
		}
	}

	/**
	 * クライアントからの呼び出しパラメータをBigDecimal型として取得します。
	 * new BigDecimal(String)で変換します。
	 *
	 * @param name
	 * @return
	 */
	public default BigDecimal getParamAsBigDecimal(String name) {
		return getParam(name, BigDecimal.class);
	}

	/**
	 * クライアントからの呼び出しパラメータをBigDecimal型配列として取得します。
	 * new BigDecimal(String)で変換します。
	 *
	 * @param name
	 * @return
	 */
	public default BigDecimal[] getParamsAsBigDecimal(String name) {
		return getParams(name, BigDecimal.class);
	}

	/**
	 * ファイルを添付してPOST（multipart/form-data）された場合、アップロードされたファイルを取得可能です。
	 *
	 * @param name
	 * @return
	 */
	public UploadFileHandle getParamAsFile(String name);

	/**
	 * ファイルを添付してPOST（multipart/form-data）された場合、アップロードされたファイルを取得可能です。
	 * 同一名称で複数件のファイルがアップされた場合、こちらを利用可能です。
	 *
	 * @param name
	 * @return
	 */
	public UploadFileHandle[] getParamsAsFile(String name);

	/**
	 * パラメータをMap形式で取得します。
	 *
	 * @return
	 */
	public Map<String, Object> getParamMap();

	/**
	 * パラメータ名の一覧のIteratorを取得します。
	 *
	 * @return
	 */
	public Iterator<String> getParamNames();

	/**
	 * サーバサイドでリクエストスコープで保持している属性を取得します。 （HttpServletRequestのgetAttribute相当）
	 *
	 * @param name
	 * @return
	 */
	public Object getAttribute(String name);

	/**
	 * サーバサイドでリクエストスコープで属性を保持します。 （HttpServletRequestのsetAttribute相当）
	 *
	 * @param name
	 * @param value
	 */
	public void setAttribute(String name, Object value);

	/**
	 * 指定のnameの属性を削除します。 setAttribute(name, null)と同義
	 *
	 * @param name
	 */
	public void removeAttribute(String name);

	/**
	 * 属性の名前の一覧をIteratorで取得します。
	 *
	 * @return
	 */
	public Iterator<String> getAttributeNames();

	/**
	 * クライアントのセッションオブジェクトを取得します。 セッションオブジェクトが存在しなかったら新規に作成して返します。
	 * {@link #getSession(boolean) getSession(true)}呼び出しと同等。
	 *
	 * @return
	 */
	public SessionContext getSession();

	/**
	 * クライアントのセッションオブジェクトを取得します。
	 *
	 * @param create
	 *            trueの場合、セッションオブジェクトが存在しなかったら新規に作成して返す
	 * @return
	 */
	public SessionContext getSession(boolean create);

	static String resourceString(String key, Object... arguments) {
		return CoreResourceBundleUtil.resourceString(key, arguments);
	}
}
