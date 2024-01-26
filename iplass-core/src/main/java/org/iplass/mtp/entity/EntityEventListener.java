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

package org.iplass.mtp.entity;

/**
 * <p>
 * Entityの登録、更新、削除、ロード、検索、検証時になんらかの処理を実行したい場合に利用します。
 * 当該インタフェースを実装したクラスをEventListenerとして、
 * EntityDefinitionに定義することにより、各イベント発生時にメソッドが呼び出されます。
 * </p>
 * 
 * <h5>※注意</h5>
 * <p>
 * EntityManager#updateAll()、deleteAll()、bulkUpdate()などの一括更新処理では、リスナーは呼び出されません。<br>
 * また、onLoadイベントは、load()もしくは、searchEntity()で呼び出され、search()では呼び出されません（Entityの形で返却されるわけではないため）。
 * </p>
 * <p>
 * また、update/delete時など更新前のEntityが存在する状態で、各通知メソッドが呼び出される際のentityインスタンスは、EntityManagerから各更新メソッド呼び出し時のEntityインスタンスが
 * そのまま引き渡されたものです。そのため、oid以外のプロパティ項目を保持していない場合もあります。
 * </p>
 * 
 * @see org.iplass.mtp.entity.definition.listeners.JavaClassEventListenerDefinition
 * 
 * @author K.Higuchi
 *
 */
public interface EntityEventListener {
	
	//TODO 複数件一括更新時に複数のEntityを一括で渡す対応。<-難しい。。。メモリ消費が大きくなる可能性。。
	
	/**
	 * 削除処理後に呼び出されます。
	 * 
	 * @param entity
	 * @param context
	 */
	public default void afterDelete(Entity entity, EntityEventContext context) {
	}

	/**
	 * 追加処理後に呼び出されます。
	 * 
	 * @param entity
	 * @param context
	 */
	public default void afterInsert(Entity entity, EntityEventContext context) {
	}

	/**
	 * 更新処理後に呼び出されます。
	 * 
	 * @param entity
	 * @param context
	 */
	public default void afterUpdate(Entity entity, EntityEventContext context) {
	}

	
	/**
	 * 削除処理前に呼び出されます。
	 * リターン値がfalseの場合、後続処理（実際の削除処理）を実行しません（例外は発生しません。処理が成功したかのように動作します）。
	 * 
	 * @param entity
	 * @param context
	 * @return
	 */
	public default boolean beforeDelete(Entity entity, EntityEventContext context) {
		return true;
	}

	/**
	 * 追加処理前に呼び出されます。
	 * リターン値がfalseの場合、後続処理（実際の追加処理）を実行しません（例外は発生しません。処理が成功したかのように動作します）。
	 * 
	 * @param entity
	 * @param context
	 * @return
	 */
	public default boolean beforeInsert(Entity entity, EntityEventContext context) {
		return true;
	}

	/**
	 * 更新処理前に呼び出されます。
	 * リターン値がfalseの場合、後続処理（実際の更新処理）を実行しません（例外は発生しません。処理が成功したかのように動作します）。
	 * 
	 * @param entity
	 * @param context
	 * @return
	 */
	public default boolean beforeUpdate(Entity entity, EntityEventContext context) {
		return true;
	}
	
	/**
	 * 検証処理前に呼び出されます。
	 * 
	 * @param entity
	 * @param context
	 */
	public default void beforeValidate(Entity entity, EntityEventContext context) {
	}

	/**
	 * load、searchEntityでEntityの形で検索した際に呼び出されます。
	 * 1Entity単位の呼び出しなので、利用する場合、大量検索されてもよいように十分考慮する必要があります。
	 * 
	 * @param entity
	 */
	public default void onLoad(Entity entity) {
	}
	
	/**
	 * ごみ箱から復旧した際に呼び出されます。
	 * 
	 * @param entity
	 */
	public default void afterRestore(Entity entity) {
	}
	
	/**
	 * 削除処理時に、物理削除された場合、
	 * もしくはごみ箱から完全に物理削除された際に呼び出されます。
	 * 
	 * @param entity
	 */
	public default void afterPurge(Entity entity) {
	}

}
