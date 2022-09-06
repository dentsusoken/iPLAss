/* 
 * Copyright 2022 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 */

package org.iplass.mtp.impl.entity.auditlog;

public class LogAllMaskHandler extends LogMaskHandler {

	@Override
	public String maskingProperty(String definitionName, String keyName, String value) {
		if (isTargetProperty(definitionName, keyName)) {
			// 全ての文字をマスクする
			return value.replaceAll(".+?", MASK_CHAR);
		}
		return value;
	}

}
