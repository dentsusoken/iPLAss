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

package org.iplass.mtp.entity.definition;

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.entity.definition.listeners.JavaClassEventListenerDefinition;
import org.iplass.mtp.entity.definition.listeners.ScriptingEventListenerDefinition;
import org.iplass.mtp.entity.definition.listeners.SendNotificationEventListenerDefinition;

import com.fasterxml.jackson.annotation.JsonTypeInfo;


/**
 * <p>
 * Entity登録、更新、削除、検索、検証時に
 * 前処理、後処理を付け加えるためのEventListenerの定義。
 * </p>
 * <h5>※注意</h5>
 * <p>
 * EntityManager#updateAll()、updateAll()では、リスナーは呼び出されない。<br>
 * また、onLoadイベントは、search()では呼び出されない（Entityの形で返却されるわけではないため）。
 * </p>
 *
 * @author K.Higuchi
 *
 */
@XmlSeeAlso (value = {
		JavaClassEventListenerDefinition.class,
		ScriptingEventListenerDefinition.class,
		SendNotificationEventListenerDefinition.class
})
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS)
public abstract class EventListenerDefinition implements Serializable {
	private static final long serialVersionUID = -643271790982890174L;

	private boolean withoutMappedByReference;

	public boolean isWithoutMappedByReference() {
		return withoutMappedByReference;
	}

	/**
	 * beforeUpdate,afterUpdateの際、
	 * EntityEventContextに、"beforeUpdateEntity"の名前でセットされる更新前のEntityデータを取得する際、
	 * 被参照のReferencePropertyの値も取得するかどうかを設定。
	 *
	 * @param withoutMappedByReference
	 */
	public void setWithoutMappedByReference(boolean withoutMappedByReference) {
		this.withoutMappedByReference = withoutMappedByReference;
	}



}
