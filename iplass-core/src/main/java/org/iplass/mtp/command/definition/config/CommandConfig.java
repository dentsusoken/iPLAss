/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 *
 * Unless you have purchased a commercial license,
 * the following license terms apply:
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.iplass.mtp.command.definition.config;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.transaction.Propagation;
import org.iplass.mtp.transaction.RollbackException;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@XmlSeeAlso (value = {
		CompositeCommandConfig.class,
		SingleCommandConfig.class
})
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS)
public abstract class CommandConfig implements Serializable {
	//Commandの初期化、設定を行うための定義

	private static final long serialVersionUID = -2728709200780228430L;

	/** 初期化スクリプト */
	private String initScript;

	private Propagation transactionPropagation = Propagation.REQUIRED;
	private boolean rollbackWhenException = true;
	private boolean throwExceptionIfSetRollbackOnly;

	public boolean isRollbackWhenException() {
		return rollbackWhenException;
	}

	/**
	 * Commandより例外がスローされた場合、トランザクションをロールバックするか否かの設定です。
	 * 未指定（デフォルト）の場合はtrueです。
	 */
	public void setRollbackWhenException(boolean rollbackWhenException) {
		this.rollbackWhenException = rollbackWhenException;
	}

	public boolean isThrowExceptionIfSetRollbackOnly() {
		return throwExceptionIfSetRollbackOnly;
	}

	/**
	 * トランザクションが本Command処理用に新規作成された際、
	 * 処理中にsetRoobackOnlyされた場合、
	 * かつ明示的に例外がスローされなかった場合、{@link RollbackException}をスローするか否かの設定です。
	 * 未指定（デフォルト）の場合はfalseです。
	 */
	public void setThrowExceptionIfSetRollbackOnly(boolean throwExceptionIfSetRollbackOnly) {
		this.throwExceptionIfSetRollbackOnly = throwExceptionIfSetRollbackOnly;
	}

	@XmlElement(namespace="http://mtp.iplass.org/xml/definition/command")
	public Propagation getTransactionPropagation() {
		return transactionPropagation;
	}

	/**
	 * トランザクションのPropagationを指定します。デフォルト値はREQUIREDです。
	 *
	 * @param transactionPropagation
	 */
	public void setTransactionPropagation(Propagation transactionPropagation) {
		this.transactionPropagation = transactionPropagation;
	}

	/**
	 * コマンド初期化スクリプトを返します。
	 *
	 * @return コマンド初期化スクリプト
	 */
	public String getInitializeScript() {
		return initScript;
	}

	/**
	 * コマンド初期化スクリプトを設定します。
	 *
	 * @param initScript コマンド初期化スクリプト
	 */
	public void setInitializeScript(String initScript) {
		this.initScript = initScript;
	}

	public abstract CommandConfig copy();

	protected void fillTo(CommandConfig config) {
		config.initScript = initScript;
		config.transactionPropagation = transactionPropagation;
		config.rollbackWhenException = rollbackWhenException;
		config.throwExceptionIfSetRollbackOnly = throwExceptionIfSetRollbackOnly;
	}

}
