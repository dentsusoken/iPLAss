/* 
 * Copyright 2022 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 */

package org.iplass.mtp.impl.entity.auditlog;

import java.util.List;

public abstract class LogMaskHandler {

	public static final String MASK_CHAR = "*";
	public static final String ALL_ENTITY = "*";

	protected List<MaskTargetStore> maskTargetStore;

	public List<MaskTargetStore> getMaskTargetStore() {
		return maskTargetStore;
	}

	public void setMaskTargetStore(List<MaskTargetStore> maskTargetStore) {
		this.maskTargetStore = maskTargetStore;
	}

	public abstract String maskingProperty(String definitionName, String keyName, String value);

	public boolean isTargetProperty(String definitionName, String keyName) {
		for (MaskTargetStore maskTarget : maskTargetStore) {
			if (maskTarget.getMaskEntity().equals(ALL_ENTITY) || definitionName.equals(maskTarget.getMaskEntity())) {
				for (String maskProperty : maskTarget.getMaskProperties()) {
					if (keyName.equals(maskProperty)) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
