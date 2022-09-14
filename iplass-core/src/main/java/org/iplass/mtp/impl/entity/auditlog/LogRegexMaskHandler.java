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
	public String maskingProperty(String value) {
		// 指定された正規表現でマスクする
		return value.replaceAll(maskRegex, MASK_CHAR);
	}
}
