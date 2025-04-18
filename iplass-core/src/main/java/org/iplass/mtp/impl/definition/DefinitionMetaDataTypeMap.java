/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.definition;

import java.util.Objects;
import java.util.Optional;

import org.iplass.mtp.definition.Definition;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.impl.definition.validation.DefaultDefinitionNameCheckValidator;
import org.iplass.mtp.impl.definition.validation.DefinitionNameCheckValidator;
import org.iplass.mtp.impl.metadata.RootMetaData;
import org.iplass.mtp.impl.util.CoreResourceBundleUtil;
import org.iplass.mtp.util.StringUtil;

public abstract class DefinitionMetaDataTypeMap<D extends Definition, M extends RootMetaData> {
	protected String pathPrefix;
	protected Class<M> metaType;
	protected Class<D> defType;
	private DefinitionNameCheckValidator definitionNameCheckValidator;
	//	boolean replaceDot;

	protected DefinitionMetaDataTypeMap(String pathPrefix, Class<M> metaType, Class<D> defType) {
		this.pathPrefix = pathPrefix;
		this.metaType = metaType;
		this.defType = defType;
		this.definitionNameCheckValidator = this.createDefinitionNameCheckValidator();
		//		this.replaceDot = replaceDot;
	}

	@SuppressWarnings("unchecked")
	public D toDefinition(M metaData) {
		if (metaData instanceof DefinableMetaData<?>) {
			return ((DefinableMetaData<D>) metaData).currentConfig();
		}
		return null;
	};

	public abstract TypedDefinitionManager<D> typedDefinitionManager();

	public String toPath(String defName) {
		return pathPrefix + defName;
	}

	public String toDefName(String path) {
		return path.substring(pathPrefix.length());
	}

	public String typeName() {
		return defType.getSimpleName();
	}

	/**
	 * メタデータ定義名チェックValidator生成
	 * TODO コンパイルエラー回避のため一旦abstractメソッドにしない
	 * 
	 * @return メタデータ定義名チェックValidator
	 */
	protected DefinitionNameCheckValidator createDefinitionNameCheckValidator() {
		return new DefaultDefinitionNameCheckValidator();
	}

	/**
	 * メタデータ定義名のValidationチェック
	 * 
	 * <p>
	 * 以下のチェックをする
	 * <ul>
	 * <li>メタデータのパスがメタデータ定義のパスに一致するかどうか</li>
	 * <li>メタデータ定義名に指定できない文字列が含まれていないかどうか</li>
	 * </ul>
	 * </p>
	 * 
	 * @param defName メタデータ定義名
	 * @return エラーメッセージ（チェックエラーの場合）
	 */
	public Optional<String> validateDefinitionName(String path, String defName) {
		// パスチェック
		if (StringUtil.isNotEmpty(path) && !path.startsWith(this.pathPrefix)) {
			return Optional.of(CoreResourceBundleUtil.resourceString("impl.definition.DefinitionNameCheckPattern.regExp.invalidPath"));
		}

		if (Objects.isNull(this.definitionNameCheckValidator) || StringUtil.isEmpty(defName)) {
			return Optional.empty();
		}

		// 定義名チェック
		return this.definitionNameCheckValidator.validate(defName);
	}
}
