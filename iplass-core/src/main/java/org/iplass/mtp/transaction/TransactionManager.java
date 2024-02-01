/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

import java.util.function.Function;

import org.iplass.mtp.Manager;

/**
 * TransactionManagerのインタフェース。
 * 
 * @author K.Higuchi
 *
 */
public interface TransactionManager extends Manager {
	
	/**
	 * 新規にトランザクションを開始する。
	 * 
	 * @return
	 */
	public Transaction newTransaction();
	
	/**
	 * 新規に読み込み専用とマークしたトランザクションを開始する。
	 * 
	 * @param readOnly
	 * @return
	 */
	public Transaction newTransaction(boolean readOnly);
	
	/**
	 * トランザクションをサスペンドします。
	 * サスペンドされた状態（Transacton.status=SUSPENDED）であることを示すTransactionインスタンスが返却されます。
	 * 
	 * @return サスペンド状態のTransaction
	 */
	public Transaction suspend();
	
	/**
	 * サスペンドされたトランザクションを復帰します。
	 * 
	 * @param t suspend()の際取得したTransactionインスタンス
	 */
	public void resume(Transaction t);
	
	/**
	 * 現在のトランザクションを取得します。
	 * トランザクションが開始されていない場合、{@link TransactionStatus#NONE}のTransactionが返却されます。
	 * サスペンドされている場合は{@link TransactionStatus#SUSPENDED}のTransactionが返却されます。
	 * 
	 * @return
	 */
	public Transaction currentTransaction();
	
	/**
	 * 指定のfunctionをoptionに従った形でトランザクション処理します。<br>
	 * 
	 * @param <R> functionのリターン値の型
	 * @param option トランザクション制御オプション
	 * @param function トランザクション処理
	 * @return functionで返却されるインスタンス
	 */
	public default <R> R doTransaction(TransactionOption option, Function<Transaction, R> function) {
		
		Propagation propagation = option.getPropagation();
		
		Transaction t = currentTransaction();
		boolean isSuccess = false;
		boolean isCreate = false;
		Throwable th = null;
		try {
			switch (propagation) {
			case SUPPORTS:
				break;
			case NOT_SUPPORTED:
				if (t.getStatus() != TransactionStatus.NONE && t.getStatus() != TransactionStatus.SUSPENDED) {
					t = suspend();
					isCreate = true;
				}
				break;
			case REQUIRED:
				if (t.getStatus() == TransactionStatus.NONE || t.getStatus() == TransactionStatus.SUSPENDED
						|| (t.getStatus() == TransactionStatus.ACTIVE && t.isReadOnly() && !option.isReadOnly())) {
					t = newTransaction(option.isReadOnly());
					isCreate = true;
				}
				break;
			case REQUIRES_NEW:
				t = newTransaction(option.isReadOnly());
				isCreate = true;
				break;
			default:
				break;
			}
			
			R ret = function.apply(t);
			isSuccess = true;
			return ret;
		} catch (RuntimeException | Error e) {
			th = e;
			throw e;
		} finally {
			if (isCreate) {
				switch (t.getStatus()) {
				case SUSPENDED:
					resume(t);
					break;
				case ACTIVE:
					if (t.isReadOnly()) {
						if (Holder.logger.isDebugEnabled()) {
							Holder.logger.debug("rollback readOnly transaction:" + t);
						}
						t.rollback();
					} else if (isSuccess) {
						if (t.isRollbackOnly()) {
							if (Holder.logger.isDebugEnabled()) {
								Holder.logger.debug("rollback transaction because set rollbackOnly=true:" + t);
							}
							t.rollback();
							if (option.isThrowExceptionIfSetRollbackOnly()) {
								throw new RollbackException("setRolbackOnly called");
							}
						} else {
							t.commit();
						}
					} else {
						if (option.isRollbackWhenException()) {
							try {
								if (Holder.logger.isDebugEnabled()) {
									Holder.logger.debug("rollback transaction cause " + th + ":" + t, th);
								}
								t.rollback();
							} catch (RuntimeException e) {
								th.addSuppressed(e);
							}
						} else {
							if (t.isRollbackOnly()) {
								if (Holder.logger.isDebugEnabled()) {
									Holder.logger.debug("rollbackWhenException=false, but rollback transaction because set rollbackOnly=true. exception:" + th + ":" + t, th);
								}
								try {
									t.rollback();
								} catch (RuntimeException e) {
									th.addSuppressed(e);
								}
							} else {
								if (Holder.logger.isDebugEnabled()) {
									Holder.logger.debug("rollbackWhenException=false, so commit transaction on exception:" + th + ":" + t, th);
								}
								try {
									t.commit();
								} catch (RuntimeException e) {
									th.addSuppressed(e);
								}
							}
						}
					}
					break;
				case COMMITTED:
				case ROLLEDBACK:
					//トランザクション処理中に明示的にcommit/rollbackした。
					if (Holder.logger.isDebugEnabled()) {
						Holder.logger.debug("In transaction function, explicitly committed/rolledback, so no handle transaction in doTransactiion():" + t);
					}
					break;
				case NONE:
					//このパターンはありえない
					break;
				default:
					break;
				}
			}
		}
	}
	
}
