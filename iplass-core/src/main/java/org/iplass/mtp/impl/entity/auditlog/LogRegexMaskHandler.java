/* 
 * Copyright 2022 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 */

package org.iplass.mtp.impl.entity.auditlog;

public class LogRegexMaskHandler extends LogMaskHandler {

	private String maskRegex;

	public String getMaskRegex() {
		return maskRegex;
	}

	public void setMaskRegex(String maskRegex) {
		this.maskRegex = maskRegex;
	}

	@Override
	public String maskingProperty(String definitionName, String keyName, String value) {
		if (isTargetProperty(definitionName, keyName)) {
			// 指定された正規表現でマスクする
			return value.replaceAll(maskRegex, MASK_CHAR);
		}
		return value;
	}
}
