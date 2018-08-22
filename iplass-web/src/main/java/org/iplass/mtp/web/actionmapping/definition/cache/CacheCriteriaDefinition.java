/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.web.actionmapping.definition.cache;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * サーバ側でコンテンツのキャッシュを行う場合の定義です。
 * このキャッシュ基準定義に一致した場合、実際にはActionの処理が実行されず、
 * キャッシュされたコンテンツ（HTML、バイナリデータなど）がクライアントへ返却されます。
 * 
 * @author K.Higuchi
 *
 */
@XmlSeeAlso (value = {
		JavaClassCacheCriteriaDefinition.class,
		ParameterMatchCacheCriteriaDefinition.class,
		ScriptingCacheCriteriaDefinition.class
})
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS)
public abstract class CacheCriteriaDefinition implements Serializable {

	private static final long serialVersionUID = -7269996485444294477L;

	/** キャッシュしてよいCommandのステータス。未指定、*指定の場合は、なんでもキャッシュ。 */
	private List<String> cachableCommandResultStatus;//*指定可能。未指定の場合は*と同義。

	/** このキャッシュに関連するEntityの定義。当該Entityが更新されたら、キャッシュが無効化される。 */
	private List<CacheRelatedEntityDefinition> relatedEntity;

	/** キャッシュの有効期間（ms） */
	private Integer timeToLive;

	public List<String> getCachableCommandResultStatus() {
		return cachableCommandResultStatus;
	}

	public void setCachableCommandResultStatus(List<String> cachableCommandResultStatus) {
		this.cachableCommandResultStatus = cachableCommandResultStatus;
	}

	public List<CacheRelatedEntityDefinition> getRelatedEntity() {
		return relatedEntity;
	}

	public void setRelatedEntity(List<CacheRelatedEntityDefinition> relatedEntity) {
		this.relatedEntity = relatedEntity;
	}

	public Integer getTimeToLive() {
		return timeToLive;
	}

	public void setTimeToLive(Integer timeToLive) {
		this.timeToLive = timeToLive;
	}

	/**
	 * 各Resultのサマリー情報を返します。
	 *
	 * @return Resultのサマリー情報
	 */
	public abstract String summaryInfo();
}
