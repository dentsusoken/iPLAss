/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.command.async;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.LoadOption;
import org.iplass.mtp.entity.UpdateOption;

/**
 * 非同期実行Commandの処理結果を指定のEntityの指定のプロパティに書き出すResultHandler。
 * 
 * 例外が発生した場合は、"FAIL"と書き込む。
 * 合わせて、propertyNameForExceptionNameが設定されている場合は、
 * そのプロパティに例外クラス名を書き込む。
 * propertyNameForExceptionMessageが設定されている場合は、
 * そのプロパティに例外クラスメッセージを書き込む。
 * 
 * exceptionIfNoEntity=trueが設定されている場合、
 * 書き込み対象のEntityが存在しなかった場合、例外をスローする。デフォルトfalse。
 * 
 * @author K.Higuchi
 *
 */
public class WriteToEntityHandler implements ResultHandler {
	private static final long serialVersionUID = -3139078172022183983L;
	
	public static final String FAIL_STATUS = "FAIL";
	
	private String entityDefinitionName;
	private String propertyName;
	private String propertyNameForExceptionName;
	private String propertyNameForExceptionMessage;

	private String oid;
	
	private boolean exceptionIfNoEntity = false;
	
	public WriteToEntityHandler() {
	}
	
	public WriteToEntityHandler(String entityDefinitionName,
			String propertyName, String oid, boolean exceptionIfNoEntity) {
		this.entityDefinitionName = entityDefinitionName;
		this.propertyName = propertyName;
		this.oid = oid;
		this.exceptionIfNoEntity = exceptionIfNoEntity;
	}
	
	public WriteToEntityHandler(String entityDefinitionName,
			String propertyName, String propertyNameForExceptionName, String propertyNameForExceptionMessage, String oid, boolean exceptionIfNoEntity) {
		this.entityDefinitionName = entityDefinitionName;
		this.propertyName = propertyName;
		this.propertyNameForExceptionName = propertyNameForExceptionName;
		this.propertyNameForExceptionMessage = propertyNameForExceptionMessage;
		this.oid = oid;
		this.exceptionIfNoEntity = exceptionIfNoEntity;
	}

	public String getEntityDefinitionName() {
		return entityDefinitionName;
	}

	public void setEntityDefinitionName(String entityDefinitionName) {
		this.entityDefinitionName = entityDefinitionName;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getPropertyNameForExceptionName() {
		return propertyNameForExceptionName;
	}

	public void setPropertyNameForExceptionName(String propertyNameForExceptionName) {
		this.propertyNameForExceptionName = propertyNameForExceptionName;
	}

	public String getPropertyNameForExceptionMessage() {
		return propertyNameForExceptionMessage;
	}

	public void setPropertyNameForExceptionMessage(
			String propertyNameForExceptionMessage) {
		this.propertyNameForExceptionMessage = propertyNameForExceptionMessage;
	}
	public boolean isExceptionIfNoEntity() {
		return exceptionIfNoEntity;
	}

	public void setExceptionIfNoEntity(boolean exceptionIfNoEntity) {
		this.exceptionIfNoEntity = exceptionIfNoEntity;
	}

	@Override
	public void handle(String commandResult) {
		EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);
		Entity e = em.load(oid, entityDefinitionName, new LoadOption(false, false));
		if (e == null && exceptionIfNoEntity) {
			throw new EntityRuntimeException("entity[definitionName=" + entityDefinitionName + ", oid=" + oid + "] not found.");
		}
		e.setValue(propertyName, commandResult);
		UpdateOption opt = new UpdateOption(true);
		opt.setUpdateProperties(propertyName);
		em.update(e, opt);
	}

	@Override
	public void handle(Throwable exception) {
		EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);
		Entity e = em.load(oid, entityDefinitionName, new LoadOption(false, false));
		if (e == null && exceptionIfNoEntity) {
			throw new EntityRuntimeException("entity[definitionName=" + entityDefinitionName + ", oid=" + oid + "] not found.");
		}
		
		List<String> updateProperties = new ArrayList<>();
		e.setValue(propertyName, FAIL_STATUS);
		updateProperties.add(propertyName);
		if (propertyNameForExceptionName != null) {
			e.setValue(propertyNameForExceptionName, exception.getClass().getName());
			updateProperties.add(propertyNameForExceptionName);
		}
		if (propertyNameForExceptionMessage != null) {
			e.setValue(propertyNameForExceptionMessage, exception.getMessage());
			updateProperties.add(propertyNameForExceptionMessage);
		}
		UpdateOption opt = new UpdateOption(true);
		opt.setUpdateProperties(updateProperties);
		em.update(e, opt);
	}

}
