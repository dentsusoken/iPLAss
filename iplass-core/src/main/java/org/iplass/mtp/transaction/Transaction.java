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

package org.iplass.mtp.transaction;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * <p>
 * トランザクションを表すインタフェースです。<br>
 * トランザクションを制御するためのメソッドを持っています。
 * </p>
 * 
 * <h5>トランザクション制御コード例</h5>
 * ※現在起動中のトランザクションと別に新規にトランザクション処理を起動する場合
 * <pre>
 * 
 * EntityManager em = ...
 * Entity e = ...
 * 
 * //新規にトランザクションを起動
 * Transaction.requiresNew(t -> {
 *   
 *   em.insert(e);
 *   
 *   //コミット成功時に実行する処理を追加
 *   t.afterCommit(() -> {
 *     System.out.println("committed");
 *   });
 *   
 *   :
 *   :
 *   
 * });
 * </pre>
 * 
 * 
 * @author K.Higuchi
 *
 */
public interface Transaction {
	
	/**
	 * トランザクションがない状態を表すTransactionのインスタンスです。
	 * トランザクションがまだ開始されていない状態、もしくはトランザクションがサスペンドされている場合に当インスタンスが返却されます。<br>
	 * {@link #getStatus()}は常に{@link TransactionStatus#NONE}を返します。<br>
	 * {@link #setAttribute(Object, Object)}などのその他のトランザクション制御メソッドはなにも処理しません。
	 */
	public static final Transaction NO_TRANSACTION = new NoTransaction();
	
	/**
	 * 現在の実行コンテキストに関連しているトランザクションのインスタンスを取得します。
	 * もし、トランザクションが開始されていない場合は、{@link #NO_TRANSACTION}が返却されます。
	 * 
	 * @return
	 */
	public static Transaction getCurrent() {
		Transaction t = Holder.tm.currentTransaction();
		if (t == null) {
			return NO_TRANSACTION;
		} else {
			return t;
		}
	}
	
	/**
	 * トランザクションをコミットします。
	 * 
	 */
	public void commit();
	
	/**
	 * トランザクションをロールバックします。
	 * 
	 */
	public void rollback();
	
	/**
	 * このトランザクションをロールバックするものとしてマークします。
	 * setRollbackOnlyされたトランザクションは最終的にはロールバックされます。
	 * 
	 */
	public void setRollbackOnly();
	
	/**
	 * このトランザクションがsetRollbackOnlyされているかどうかを取得します。
	 * 
	 * @return setRollbackOnlyの場合true
	 */
	public boolean isRollbackOnly();
	
	/**
	 * このトランザクションがReadOnlyとしてマークされているかどうかを取得します。
	 * 
	 * @return
	 */
	public boolean isReadOnly();
	
	/**
	 * トランザクションの状態を取得します。
	 * 
	 * @return
	 */
	public TransactionStatus getStatus();
	
	/**
	 * このトランザクションのコンテキストに特定の属性を紐づけます。
	 * 
	 * @param key 属性のキー
	 * @param value 属性の値
	 */
	public void setAttribute(Object key, Object value);
	
	/**
	 * このトランザクションコンテキストに紐づけられている特定の属性を取得します。
	 * 同一トランザクション内でsetAttribute()したものがあれば、その値が取得できます。
	 * 
	 * @param key 属性のキー
	 * @return 属性の値
	 */
	public Object getAttribute(Object key);
	
	/**
	 * このトランザクションコンテキストに紐づけられている特定の属性を削除します。
	 * 
	 * @param key 属性のキー
	 * @return 削除された値
	 */
	public Object removeAttribute(Object key);
	
	/**
	 * このトランザクションのcommit、rollbackを監視するリスナーを登録します。
	 * 
	 * @param listener
	 */
	public void addTransactionListener(TransactionListener listener);
	
	
	/**
	 * このトランザクションがcommitされた場合、funcを実行します。
	 * funcの引数にはこのトランザクションが渡されます。
	 * @param func
	 */
	public default void afterCommit(Runnable func) {
		addTransactionListener(new TransactionListener() {
			@Override
			public void afterCommit(Transaction t) {
				func.run();
			}
		});
	}
	
	/**
	 * このトランザクションがrollbackされた場合、funcを実行します。
	 * funcの引数にはこのトランザクションが渡されます。
	 * @param func
	 */
	public default void afterRollback(Runnable func) {
		addTransactionListener(new TransactionListener() {
			@Override
			public void afterRollback(Transaction t) {
				func.run();
			}
		});
	}

	/**
	 * トランザクションが開始していない場合はトランザクションを起動し、funcを実行します。
	 * トランザクション制御方法は、{@link TransactionOption}のデフォルトが適用されます。
	 * 
	 * @param func
	 */
	public static void required(Consumer<Transaction> func) {
		with(new TransactionOption(), func);
	}
	
	/**
	 * トランザクションが開始していない場合はトランザクションを起動し、funcを実行します。
	 * トランザクション制御方法は、{@link TransactionOption}のデフォルトが適用されます。
	 * 
	 * @param func
	 * @return funcが返却する値
	 */
	public static <R> R required(Function<Transaction, R> func) {
		return with(new TransactionOption(), func);
	}
	
	/**
	 * 新規にトランザクションを起動し、funcを実行します。
	 * トランザクション制御方法は、{@link TransactionOption}のデフォルトが適用されます。
	 * 
	 * @param func
	 */
	public static void requiresNew(Consumer<Transaction> func) {
		with(new TransactionOption(Propagation.REQUIRES_NEW), func);
	}
	
	/**
	 * 新規にトランザクションを起動し、funcを実行します。
	 * トランザクション制御方法は、{@link TransactionOption}のデフォルトが適用されます。
	 * 
	 * @param func
	 * @return funcが返却する値
	 */
	public static <R> R requiresNew(Function<Transaction, R> func) {
		return with(new TransactionOption(Propagation.REQUIRES_NEW), func);
	}
	
	/**
	 * トランザクションをreadOnlyモードで起動します。
	 * ただし、すでにトランザクションが開始されている場合、
	 * 新規にトランザクションは開始せず既存のトランザクションをそのまま利用します。
	 * これは、<br>
	 * TransactionOption.propagation=REQUIRED<br>
	 * TransactionOption.readOnly=true<br>
	 * でトランザクションを起動することを示します。
	 * 
	 * @param func
	 */
	public static void readOnly(Consumer<Transaction> func) {
		with(new TransactionOption().readOnly(), func);
	}
	
	/**
	 * トランザクションをreadOnlyモードで起動します。
	 * ただし、すでにトランザクションが開始されている場合、
	 * 新規にトランザクションは開始せず既存のトランザクションをそのまま利用します。
	 * これは、<br>
	 * TransactionOption.propagation=REQUIRED<br>
	 * TransactionOption.readOnly=true<br>
	 * でトランザクションを起動することを示します。
	 * 
	 * @param func
	 * @return funcが返却する値
	 */
	public static <R> R readOnly(Function<Transaction, R> func) {
		return with(new TransactionOption().readOnly(), func);
	}
	
	/**
	 * 指定のpropagationで、トランザクションを起動しfuncを実行します。
	 * 
	 * @param propagation
	 * @param func
	 */
	public static void with(Propagation propagation, Consumer<Transaction> func) {
		with(new TransactionOption(propagation), func);
	}
	
	/**
	 * 指定のpropagationで、トランザクションを起動しfuncを実行します。
	 * 
	 * @param propagation
	 * @param func
	 * @return funcが返却する値
	 */
	public static <R> R with(Propagation propagation, Function<Transaction, R> func) {
		return with(new TransactionOption(propagation), func);
	}
	
	/**
	 * 指定のoptionで、トランザクションを起動しfuncを実行します。
	 * 
	 * @param option
	 * @param func
	 */
	public static void with(TransactionOption option, Consumer<Transaction> func) {
		Holder.tm.doTransaction(option, new Function<Transaction, Void>() {
			@Override
			public Void apply(Transaction t) {
				func.accept(t);
				return null;
			}
		});
	}
	
	/**
	 * 指定のoptionで、トランザクションを起動しfuncを実行します。
	 * 
	 * @param option
	 * @param func
	 * @return funcが返却する値
	 */
	public static <R> R with(TransactionOption option, Function<Transaction, R> func) {
		return Holder.tm.doTransaction(option, func);
	}

}
