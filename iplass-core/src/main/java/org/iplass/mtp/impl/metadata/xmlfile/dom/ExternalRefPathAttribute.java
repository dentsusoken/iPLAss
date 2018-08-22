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
package org.iplass.mtp.impl.metadata.xmlfile.dom;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ExternalRefPathAttribute {
	
	/**
	 * 対象のタグのパス.
	 * <p>"metaDataEntry/metaData/message/content"のようにルートタグから指定する. 
	 * @return
	 */
	String path();
	
	/**
	 * 外部参照ファイルの拡張子を取得.
	 * @return
	 */
	FileExtention fileExtension() default FileExtention.TMPL;
	
	/**
	 * 当該ノードのテンプレートの種別.
	 * @return
	 */
	TemplateType templateType() default TemplateType.NULL;
	
	/**
	 * 外部参照ファイルにBase64デコード/エンコードして保存するタグか.
	 * @return
	 */
	boolean base64Tag() default false;
	
	/**
	 * 対象のタグに対応するロケールタグの名前
	 * @return
	 */
	String localeTagName() default "localeName";
	
	/**
	 * 外部参照ファイル名のシーケンス番号有無
	 * @return
	 */
	boolean useFileSequence() default false;
	
	/**
	 * 外部参照ファイルのGroovy専用ソースフォルダ使用有無.
	 * @return
	 */
	boolean useGroovyDir() default false;
		
	/**
	 * 外部参照ファイルの拡張子.
	 */
	public enum FileExtention {
		GROOVY(".groovy"), TMPL(".gtmpl"), GIF(".gif"), 
		BMP(".bmp"), JPEG(".jpg"), PNG(".png"), BIN(".bin"), XML(".xml"), HTML(".html"), JAVASCRIPT(".js");

		private final String ext;
		
		private FileExtention(String ext) {
			this.ext = ext;
		}
		
		public String getExt() {
			return ext;
		}
	}
	
	/**
	 * テンプレートの種別. 
	 * <p>メールテンプレートとプレーンテキスト版とHTML版など外部参照ファイルの名前を一意にするための文字列.
	 */
	public enum TemplateType {
		NULL(null), HTML("html"), CSS("css");

		private final String type;

		private TemplateType(String type) {
			this.type = type;
		}

		public String getType() {
			return type;
		}
	}
}
