/*
 * Copyright (C) 2015 DENTSU SOKEN INC. All Rights Reserved.
 */
package org.iplass.mtp.impl.definition.validation;

import java.util.regex.Pattern;

/**
 * メタデータ定義名チェックパターンEnum
 */
public enum DefinitionNameCheckPattern {

	/** 名前の正規表現(パスにピリオドを利用) */
	PATH_PERIOD("^[0-9a-zA-Z_][0-9a-zA-Z_-]*(\\.[0-9a-zA-Z_-]+)*$", "impl.definition.DefinitionNameCheckPattern.regExp.invalidPathPeriod"),
	/** 名前の正規表現(パスにスラッシュを利用) */
	PATH_SLASH("^[0-9a-zA-Z_][0-9a-zA-Z_-]*(/[0-9a-zA-Z_-]+)*$", "impl.definition.DefinitionNameCheckPattern.regExp.invalidPathSlash"),
	/** 名前の正規表現(パスにスラッシュを利用、名前にピリオド含む) */
	PATH_SLASH_NAME_PERIOD("^[0-9a-zA-Z_][0-9a-zA-Z_-]*(/[0-9a-zA-Z_-]+)*(\\.[0-9a-zA-Z_-]+)*$",
			"impl.definition.DefinitionNameCheckPattern.regExp.invalidPathSlashNamePeriod"),
	/** Entity名の正規表現(英数、アンダスコア、パスにピリオド、先頭の数字不可、マイナス不可) */
	ENTITY_NAME("^[a-zA-Z_][0-9a-zA-Z_]*(\\.[a-zA-Z_][0-9a-zA-Z_]*)*$", "impl.definition.DefinitionNameCheckPattern.regExp.invalidPathPeriod");

	private String messageKey;
	private Pattern definitionNamePattern;

	private DefinitionNameCheckPattern(String regularExpression, String messageKey) {
		this.messageKey = messageKey;
		this.definitionNamePattern = Pattern.compile(regularExpression);
	}

	/**
	 * 定義名の正規表現パターンを返却
	 * 
	 * @return 定義名の正規表現パターン
	 */
	public Pattern getDefinitionNamePattern() {
		return this.definitionNamePattern;
	}

	/**
	 * メッセージキーを返却
	 * 
	 * @return メッセージキー
	 */
	public String getMessageKey() {
		return this.messageKey;
	}
}
