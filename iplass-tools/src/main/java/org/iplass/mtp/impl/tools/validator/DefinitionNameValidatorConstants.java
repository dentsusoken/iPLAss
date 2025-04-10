/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
 */

package org.iplass.mtp.impl.tools.validator;

/**
 * メタデータ定義名チェックValidator用定数定義クラス
 */
public interface DefinitionNameValidatorConstants {
	/** 名前の正規表現(パスにスラッシュを利用) */
	public static final String NAME_REG_EXP_PATH_SLASH = "^[0-9a-zA-Z_][0-9a-zA-Z_-]*(/[0-9a-zA-Z_-]+)*$";
	/** 名前の正規表現(パスにスラッシュを利用、名前にピリオド含む) */
	public static final String NAME_REG_EXP_PATH_SLASH_NAME_PERIOD = "^[0-9a-zA-Z_][0-9a-zA-Z_-]*(/[0-9a-zA-Z_-]+)*(\\.[0-9a-zA-Z_-]+)*$";
	/** 名前の正規表現(パスにピリオドを利用) */
	public static final String NAME_REG_EXP_PATH_PERIOD = "^[0-9a-zA-Z_][0-9a-zA-Z_-]*(\\.[0-9a-zA-Z_-]+)*$";

	/** Entity名の正規表現(英数、アンダスコア、パスにピリオド、先頭の数字不可、マイナス不可) */
	public static final String ENTITY_NAME_REG_EXP_PATH_PERIOD = "^[a-zA-Z_][0-9a-zA-Z_]*(\\.[a-zA-Z_][0-9a-zA-Z_]*)*$";
	/** Entityプロパティ名の正規表現(英数、アンダスコア、先頭の数字不可、マイナス不可) */
	public static final String ENTITY_PROPERTY_NAME_REG_EXP_PATH_PERIOD = "^[a-zA-Z_][0-9a-zA-Z_]*$";
}
