/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.transaction;

/**
 * トランザクション処理のオプションを指定するためのクラスです。<br>
 * 次のオプションを指定可能です。
 * 
 * <table border="1">
 * <tr>
 * <th>項目</th><th>デフォルト値</th><th>説明</th>
 * </tr>
 * <tr>
 * <td>propagation</td><td>REQUIRED</td><td>トランザクション伝搬種別です。<br>{@link Propagation}に定義される種別を指定可能です。</td>
 * </tr>
 * <tr>
 * <td>readOnly</td><td>false</td><td>このトランザクションをreadOnlyとしてマークします。</td>
 * </tr>
 * <tr>
 * <td>rollbackWhenException</td><td>true</td><td>トランザクション処理中に例外が発生した場合、トランザクションをロールバックします。ただし呼び出し元コードとトランザクションを共有する場合（既にトランザクションが開始されている中で、Propagation.REQUIRED、SUPPORTSで呼び出された場合）はトランザクションは呼び出し元で制御され、このブロック内ではロールバックされません。</td>
 * </tr>
 * <tr>
 * <td>throwExceptionIfSetRollbackOnly</td><td>false</td><td>trueに設定された場合、トランザクションが当該トランザクション処理用に新規作成され、かつ処理中にsetRoobackOnlyされた場合、かつ明示的に例外がスローされなかった場合、{@link RollbackException}をスローします。</td>
 * </tr>
 * </table>
 * 
 * @see Transaction
 * @author K.Higuchi
 *
 */
public class TransactionOption {
	private Propagation propagation = Propagation.REQUIRED;
	private boolean readOnly;
	private boolean rollbackWhenException = true;
	private boolean throwExceptionIfSetRollbackOnly;
	
	/**
	 * デフォルト設定のTransactionOptionを生成します。
	 * 
	 */
	public TransactionOption() {
	}
	
	/**
	 * 指定のpropagationでTransactionOptionを生成します。
	 * 
	 * @param propagation
	 */
	public TransactionOption(Propagation propagation) {
		this.propagation = propagation;
	}
	
	/**
	 * readOnly=trueに設定します。
	 * 
	 * @return
	 */
	public TransactionOption readOnly() {
		this.readOnly = true;
		return this;
	}
	
	/**
	 * rollbackWhenException=falseに設定します。
	 * @return
	 */
	public TransactionOption noRollbackWhenException() {
		this.rollbackWhenException = false;
		return this;
	}
	
	/**
	 * throwExceptionIfSetRollbackOnly=trueに設定します。
	 * @return
	 */
	public TransactionOption throwExceptionIfSetRollbackOnly() {
		this.throwExceptionIfSetRollbackOnly = true;
		return this;
	}
	
	public Propagation getPropagation() {
		return propagation;
	}
	public void setPropagation(Propagation propagation) {
		this.propagation = propagation;
	}
	public boolean isReadOnly() {
		return readOnly;
	}
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	public boolean isRollbackWhenException() {
		return rollbackWhenException;
	}
	public void setRollbackWhenException(boolean rollbackWhenException) {
		this.rollbackWhenException = rollbackWhenException;
	}
	public boolean isThrowExceptionIfSetRollbackOnly() {
		return throwExceptionIfSetRollbackOnly;
	}
	public void setThrowExceptionIfSetRollbackOnly(boolean throwExceptionIfSetRollbackOnly) {
		this.throwExceptionIfSetRollbackOnly = throwExceptionIfSetRollbackOnly;
	}
	
}
