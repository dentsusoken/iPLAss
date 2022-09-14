/* 
 * Copyright 2022 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 */

package org.iplass.mtp.impl.entity.auditlog;

public class MaskTarget {

	private String entity;
	private String property;
	private LogMaskHandler maskHandler;

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public LogMaskHandler getMaskHandler() {
		return maskHandler;
	}

	public void setMaskHandler(LogMaskHandler maskHandler) {
		this.maskHandler = maskHandler;
	}
}
