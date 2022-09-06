/* 
 * Copyright 2022 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 */

package org.iplass.mtp.impl.entity.auditlog;

import java.util.List;

public class MaskTargetStore {

	private String maskEntity;
	private List<String> maskProperties;

	public String getMaskEntity() {
		return maskEntity;
	}

	public void setMaskEntity(String maskEntity) {
		this.maskEntity = maskEntity;
	}

	public List<String> getMaskProperties() {
		return maskProperties;
	}

	public void setMaskProperties(List<String> maskProperties) {
		this.maskProperties = maskProperties;
	}

}
