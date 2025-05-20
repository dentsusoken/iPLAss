/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
 */

package org.iplass.mtp.impl.entity;

import org.iplass.mtp.impl.definition.DefinitionNameChecker;

/**
 * Entity定義名Checkerクラス
 * 
 * TODO EntityViewなどでも利用する
 */
public class EntityDefinitionNameChecker extends DefinitionNameChecker {

	public EntityDefinitionNameChecker() {
		super("^[a-zA-Z_][0-9a-zA-Z_]*(\\.[a-zA-Z_][0-9a-zA-Z_]*)*$", "impl.entity.EntityDefinitionNameChecker.invalidPattern");
	}
}
