/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.script;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;

/**
 * GroovyObjectのデシリアライズを初回アクセスまで遅延するラッパークラス。
 * HttpSessionに格納する場合、格納されたObjectのデシリアライズ処理にGroovyClassLoaderをはさみこめないので。
 * 
 * @author K.Higuchi
 *
 */
public class GroovyObjectSerializeWrapper implements Serializable {
	private static final long serialVersionUID = 5102391278876534117L;

	private static Logger logger = LoggerFactory.getLogger(GroovyObjectSerializeWrapper.class);
	
	private byte[] byteData;
	private transient volatile GroovyObject object;
	
	public GroovyObjectSerializeWrapper(GroovyObject object) {
		this.object = object;
	}
	
	public GroovyObject getObject() {
		
		//まずは、volatile変数のみでチェック
		GroovyObject obj = object;
		if (obj != null) {
			//classloaderチェック。リロードされた場合、classloaderが異なるため、castができない
			GroovyScriptEngine gse = (GroovyScriptEngine) ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();
			if (((GroovyClassLoader.InnerLoader) obj.getClass().getClassLoader()).getParent() != gse.getSharedClassLoader()) {
				synchronized (this) {
					//double check
					if (object != null && ((GroovyClassLoader.InnerLoader) object.getClass().getClassLoader()).getParent() != gse.getSharedClassLoader()) {
						byte[] copyByte = serializeObject(obj);
						try {
							obj = deserializeObject(copyByte);
						} catch (Exception e) {
							logger.error("cant deserialize GroovyObject... so return null.", e);
							obj = null;
						}
						object = obj;
					}
				}
			}
			return obj;
		}
		
		synchronized (this) {
			if (byteData != null) {
				try {
					object = deserializeObject(byteData);
				} catch (Exception e) {
					logger.error("cant deserialize GroovyObject... so return null.", e);
				}
				byteData = null;
			}
		}
		return object;
	}

	private GroovyObject deserializeObject(byte[] dat) throws IOException, ClassNotFoundException {
		try (ByteArrayInputStream byteIn = new ByteArrayInputStream(dat);
				ObjectInputStream in = new GroovyObjectInputStream(byteIn)) {
			return (GroovyObject) in.readObject();
		}
	}
	
	private byte[] serializeObject(Object obj) {
		try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(byteOut)) {
			oos.writeObject(object);
			return byteOut.toByteArray();
		} catch (IOException e) {
			//多分発生し得ない
			throw new RuntimeException(e);
		}
	}
	
	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		byteData = serializeObject(object);
		out.defaultWriteObject();
	}
	
}
