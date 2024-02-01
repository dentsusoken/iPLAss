/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.iplass.mtp.entity.BinaryReference;

/**
 * RequestContextのラッパー。 Command内から、別Commandを呼び出す場合や、
 * Templateから、別templateをincludeする場合に、 パラメータを上書きしたい場合に利用します。
 *
 */
public class RequestContextWrapper implements RequestContext {
	
	private static final Object TOMB = new Object();
	
	/**
	 * ラップするRequestContextの扱い方のモードをあらわします。
	 * 非同期処理に渡すRequestContextを生成する場合は、COPYモードを利用してください。
	 * 
	 */
	public enum Mode {
		/** attributeMapはラップされるコンテキストと共有されます 。 */
		SHARED,
		/** attributeMapはインスタンス生成時点のスナップショット（シャローコピー）となります。  */
		COPY,
	}

	private RequestContext context;
	private SessionContext session;
	private Map<String, Object> paramMap;
	private Map<String, Object> attribteMap;
	private Mode mode;

	/**
	 * 指定のcontextをラップするRequestContextWrapperを生成します。
	 * ラップされるRequestContextの扱い方は、modeで指定します。
	 * 
	 * @param context
	 * @param mode
	 */
	public RequestContextWrapper(RequestContext context, Mode mode) {
		this.mode = mode;
		paramMap = new HashMap<String, Object>();
		switch (mode) {
		case SHARED:
			if (context == null) {
				throw new IllegalArgumentException("if mode is SHARED, specify non null RequestContext");
			}
			this.context = context;
			break;
		case COPY:
			attribteMap = new HashMap<String, Object>();
			paramMap.putAll(context.getParamMap());
			for (Iterator<String> names = context.getAttributeNames(); names.hasNext();) {
				String name = names.next();
				attribteMap.put(name, context.getAttribute(name));
			}
			session = context.getSession(false);
			break;
		default:
			break;
		}
		
	}

	/**
	 * 指定のcontextをラップするRequestContextWrapperを生成します。
	 * modeはCOPYとして動作します。
	 * 
	 * @param context
	 */
	public RequestContextWrapper(RequestContext context) {
		this(context, Mode.COPY);
	}
	
	/**
	 * ラップされるContextの扱い方に関するモードを取得します。
	 * 
	 * @return
	 */
	public Mode getMode() {
		return mode;
	}

	/**
	 * ラップしているRequestContextを取得します。
	 * 
	 * @return
	 */
	public RequestContext getWrapped() {
		return context;
	}

	/**
	 * 指定のパラメータの値をセットします。
	 * セットされた値は、当RequestContextWrapperのインスタンス内に保持され、
	 * ラップしているRequestContextには影響しません。
	 *
	 * @param key
	 * @param value
	 */
	public void setParam(String key, String value) {
		if (value == null) {
			if (mode == Mode.SHARED) {
				paramMap.put(key, TOMB);
			} else {
				paramMap.remove(key);
			}
		} else {
			paramMap.put(key, new String[] { value });
		}
	}

	/**
	 * 指定のパラメータの値（複数）をセットします。
	 * セットされた値は、当RequestContextWrapperのインスタンス内に保持され、
	 * ラップしているRequestContextには影響しません。
	 *
	 * @param key
	 * @param value
	 */
	public void setParams(String key, String[] value) {
		if (value == null) {
			if (mode == Mode.SHARED) {
				paramMap.put(key, TOMB);
			} else {
				paramMap.remove(key);
			}
		} else {
			paramMap.put(key, value);
		}
	}

	@Override
	public String getParam(String name) {
		Object val = paramMap.get(name);
		if (val == null) {
			if (mode == Mode.COPY) {
				return null;
			} else {
				return context.getParam(name);
			}
		} else if (val == TOMB) {
			return null;
		} else if (val instanceof String[]) {
			return ((String[]) val)[0];
		} else {
			return val.toString();
		}
	}

	@Override
	public String[] getParams(String name) {
		Object val = paramMap.get(name);
		if (val == null) {
			if (mode == Mode.COPY) {
				return null;
			} else {
				return context.getParams(name);
			}
		} else if (val == TOMB) {
			return null;
		} else {
			return (String[]) val;
		}
	}

	@Override
	public UploadFileHandle getParamAsFile(String name) {
		Object val = paramMap.get(name);
		if (val == null) {
			if (mode == Mode.COPY) {
				return null;
			} else {
				return context.getParamAsFile(name);
			}
		} else if (val == TOMB) {
			return null;
		} else if (val instanceof UploadFileHandle[]) {
			return ((UploadFileHandle[]) val)[0];
		} else {
			return (UploadFileHandle) val;
		}
	}

	@Override
	public UploadFileHandle[] getParamsAsFile(String name) {
		Object val = paramMap.get(name);
		if (val == null) {
			if (mode == Mode.COPY) {
				return null;
			} else {
				return context.getParamsAsFile(name);
			}
		} else if (val == TOMB) {
			return null;
		} else {
			return (UploadFileHandle[]) val;
		}
	}

	@Override
	public Map<String, Object> getParamMap() {
		if (paramMap.size() > 0) {
			if (mode == Mode.COPY) {
				return paramMap;
			} else {
				HashMap<String, Object> ret = new HashMap<>(context.getParamMap());
				for (Map.Entry<String, Object> e : paramMap.entrySet()) {
					if (e.getValue() == TOMB) {
						ret.remove(e.getKey());
					} else {
						ret.put(e.getKey(), e.getValue());
					}
				}
				return ret;
			}
		} else {
			if (mode == Mode.COPY) {
				return Collections.emptyMap();
			} else {
				return context.getParamMap();
			}
		}
	}

	@Override
	public Iterator<String> getParamNames() {
		if (paramMap.size() > 0) {
			if (mode == Mode.COPY) {
				return paramMap.keySet().iterator();
			} else {
				HashSet<String> ret = new HashSet<>();
				for (Iterator<String> it = context.getParamNames(); it.hasNext();) {
					ret.add(it.next());
				}
				for (Map.Entry<String, Object> e : paramMap.entrySet()) {
					if (e.getValue() == TOMB) {
						ret.remove(e.getKey());
					} else {
						ret.add(e.getKey());
					}
				}
				return ret.iterator();
			}
		} else {
			if (mode == Mode.COPY) {
				return Collections.<String>emptySet().iterator();
			} else {
				return context.getParamNames();
			}
		}
	}

	@Override
	public Object getAttribute(String name) {
		if (mode == Mode.COPY) {
			return attribteMap.get(name);
		} else {
			return context.getAttribute(name);
		}

	}

	@Override
	public void setAttribute(String name, Object value) {
		if (mode == Mode.COPY) {
			if (value == null) {
				attribteMap.remove(name);
			} else {
				attribteMap.put(name, value);
			}
		} else {
			context.setAttribute(name, value);
		}
	}

	@Override
	public void removeAttribute(String name) {
		if (mode == Mode.COPY) {
			attribteMap.remove(name);
		} else {
			context.removeAttribute(name);
		}
	}

	@Override
	public Iterator<String> getAttributeNames() {
		if (mode == Mode.SHARED) {
			return context.getAttributeNames();
		} else {
			return attribteMap.keySet().iterator();
		}
	}

	@Override
	public SessionContext getSession() {
		return getSession(true);
	}

	@Override
	public SessionContext getSession(boolean create) {
		if (mode == Mode.SHARED) {
			return context.getSession(create);
		} else {
			if (context != null) {
				return context.getSession(create);
			} else {
				//temporary session
				if (create && session == null) {
					session = new SessionContext() {
						private Map<String, Object> map = new HashMap<>();
						@Override
						public void setAttribute(String name, Object value) {
							map.put(name, value);
						}
						@Override
						public void removeAttribute(String name) {
							map.remove(name);
						}
						@Override
						public Iterator<String> getAttributeNames() {
							return map.keySet().iterator();
						}
						
						@Override
						public Object getAttribute(String name) {
							return map.get(name);
						}
						@Override
						public BinaryReference loadFromTemporary(long lobId) {
							return null;
						}
					};
				}
				return session;
			}
		}
	}

}
