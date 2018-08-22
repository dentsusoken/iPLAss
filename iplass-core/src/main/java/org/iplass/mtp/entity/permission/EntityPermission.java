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

package org.iplass.mtp.entity.permission;

import java.util.function.Supplier;

import org.iplass.mtp.auth.Permission;
import org.iplass.mtp.impl.entity.auth.EntityQueryAuthContextHolder;

/**
 * Entityの権限定義です。
 * Entity定義名×Action（登録、参照、更新、削除）単位で権限を表現します。
 * 
 * @author K.Higuchi
 *
 */
public class EntityPermission extends Permission {

	public enum Action {
		REFERENCE, CREATE, UPDATE, DELETE;
	}

	private final String definitionName;
	private final Action action;
	
	/**
	 * <p>
	 * action内のEntityManager経由の検索処理(search、searchEntity、count)では、
	 * 引数のpermissionAction権限で検索を実行します
	 * （ただしProperty権限(項目レベルの参照可否)についてはREFERENCE権限を利用）。
	 * </p>
	 * <p>
	 * 例えば、permissionActionに {@link EntityPermission.Action.DELETE} を
	 * 指定することで削除可能なEntityの結果だけを取得することが可能です。
	 * </p>
	 *
	 * @param <T> actionの実行結果の型
	 * @param permissionAction 検索時に対象にするEntity権限
	 * @param action 実行するAction
	 * @return actionの実行結果
	 */
	public static <T> T doQueryAs(EntityPermission.Action permissionAction, Supplier<T> action) {
		return doQueryAs(permissionAction, null, action);
	}
	
	/**
	 * <p>
	 * action内のEntityManager経由の検索処理(search、searchEntity、count)にて、
	 * 引数のwithoutConditionReferenceNameで指定されている参照先に関しては、Entity権限における限定条件を適用せずに検索を実行します。
	 * </p>
	 * <p>
	 * たとえば、from句で指定されるEntity権限で絞り込まれればセキュリティ条件を充足するような場合、withoutConditionReferenceNameを指定することにより、
	 * 参照先を結合する際の冗長な限定条件の付与を行わなくてすみます。
	 * </p>
	 * <p>
	 * withoutConditionReferenceNameに"this"を指定した場合は、from句に指定されているメインEntityの限定条件を適用しない形になります。
	 * </p>
	 * 
	 * @param withoutConditionReferenceName
	 * @param action
	 * @return
	 */
	public static <T> T doQueryAs(String[] withoutConditionReferenceName, Supplier<T> action) {
		return doQueryAs(null, withoutConditionReferenceName, action);
	}

	/**
	 * <p>
	 * action内のEntityManager経由の検索処理(search、searchEntity、count)にて、
	 * permissionAction、withoutConditionReferenceNameを指定して検索します。
	 * </p>
	 * <p>permissionAction、withoutConditionReferenceNameの説明は、
	 * それぞれ、{@link #doQueryAs(Action, Supplier)}、{@link #doQueryAs(String[], Supplier)}を参照ください。
	 * </p>
	 * 
	 * @param permissionAction
	 * @param withoutConditionReferenceName
	 * @param action
	 * @return
	 */
	public static <T> T doQueryAs(EntityPermission.Action permissionAction, String[] withoutConditionReferenceName, Supplier<T> action) {
		EntityQueryAuthContextHolder.set(permissionAction, withoutConditionReferenceName);
		try {
			return action.get();
		} finally {
			EntityQueryAuthContextHolder.clear();
		}
		
	}
	
	public EntityPermission(String definitionName, Action action) {
		this.definitionName = definitionName;
		this.action = action;
	}

	public final String getDefinitionName() {
		return definitionName;
	}

	public final Action getAction() {
		return action;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result
				+ ((definitionName == null) ? 0 : definitionName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EntityPermission other = (EntityPermission) obj;
		if (action != other.action)
			return false;
		if (definitionName == null) {
			if (other.definitionName != null)
				return false;
		} else if (!definitionName.equals(other.definitionName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EntityPermission [definitionName=" + definitionName + ", action=" + action + "]";
	}
}
