/* 
 * Copyright 2022 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 */

package org.iplass.mtp.impl.entity.auditlog;

public abstract class LogMaskHandler {

	public static final String MASK_CHAR = "*";

	public abstract String maskingProperty(String value);
}
