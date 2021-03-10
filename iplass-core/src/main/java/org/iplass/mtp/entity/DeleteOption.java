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

package org.iplass.mtp.entity;

/**
 * 削除時のオプション。
 * 
 * @author K.Higuchi
 *
 */
public class DeleteOption {
	
	/** 更新時タイムスタンプチェックを行うかどうか */
	private boolean checkTimestamp;
	
	/** ごみ箱に入れず、物理削除するかどうか（デフォルトtrue） */
	private boolean purge = true;
	
	/**　ユーザによるロックのチェックを行うかどうか。デフォルトtrue */
	private boolean checkLockedByUser = true;
	
	private boolean notifyListeners = true;
	
	private DeleteTargetVersion targetVersion = DeleteTargetVersion.ALL;
	
	public DeleteOption() {
	}
	
	/**
	 * コンストラクタ。
	 * 
	 * @param checkTimestamp タイムスタンプチェック行う場合trueを指定
	 */
	public DeleteOption(boolean checkTimestamp) {
		this.checkTimestamp = checkTimestamp;
	}

	/**
	 * コンストラクタ。
	 * 
	 * @param checkTimestamp タイムスタンプチェック行う場合trueを指定
	 * @param targetVersion バージョン管理している場合に対象バージョンを指定
	 */
	public DeleteOption(boolean checkTimestamp, DeleteTargetVersion targetVersion) {
		this.checkTimestamp = checkTimestamp;
		this.targetVersion = targetVersion;
	}

	/**
	 * Entityがバージョン管理されている場合に、削除対象とする対象バージョン。
	 * @return
	 */
	public DeleteTargetVersion getTargetVersion() {
		return targetVersion;
	}

	/**
	 * Entityがバージョン管理されている場合に、削除対象とする対象バージョンを指定。
	 * デフォルトはALL。
	 * 
	 * @param targetVersion
	 */
	public void setTargetVersion(DeleteTargetVersion targetVersion) {
		this.targetVersion = targetVersion;
	}

	/**
	 * ごみ箱に入れず、物理削除するかどうか。
	 * trueの場合、物理削除。
	 * 
	 * @return
	 */
	public boolean isPurge() {
		return purge;
	}

	/**
	 * ごみ箱に入れず、物理削除するかどうかを設定。
	 * trueの場合、物理削除。
	 * 
	 * @param purge
	 */
	public void setPurge(boolean purge) {
		this.purge = purge;
	}

	/**
	 * タイムスタンプのチェックを行うかどうかを設定。
	 * trueの場合、チェックを行う。
	 * 
	 * @param checkTimestamp
	 */
	public void setCheckTimestamp(boolean checkTimestamp) {
		this.checkTimestamp = checkTimestamp;
	}

	/**
	 * タイムスタンプチェックを行うかどうか。
	 * 
	 * @return trueの場合、タイムスタンプチェックありを示す
	 */
	public boolean isCheckTimestamp() {
		return checkTimestamp;
	}

	/**
	 * {@link EntityManager#lockByUser(String, String)}により、ユーザによってロックされている場合、
	 * 更新エラー(EntityLockedByUserException)とするかどうか。
	 * 
	 * @return trueの場合、別ユーザによりロックされている場合は更新エラーとする。
	 */
	public boolean isCheckLockedByUser() {
		return checkLockedByUser;
	}

	/**
	 * {@link EntityManager#lockByUser(String, String)}により、ユーザによってロックされている場合、
	 * 更新エラー(EntityLockedByUserException)とするかどうかをセット。
	 * デフォルトtrue。
	 * 例えば、バックエンドのクリーンナッププログラムにてすでに無用なデータを削除するような場合、
	 * 当該オプションをfalseに指定して削除することにより、
	 * バックエンドのプログラムはユーザのロック状態によらず、データを削除することが可能となる。
	 * 
	 * @param checkLockedByUser
	 */
	public void setCheckLockedByUser(boolean checkLockedByUser) {
		this.checkLockedByUser = checkLockedByUser;
	}
	
	/**
	 * 削除時、 {@link EntityEventListener}に通知するか否か
	 * 
	 * @return
	 */
	public boolean isNotifyListeners() {
		return notifyListeners;
	}

	/**
	 * 削除時、 {@link EntityEventListener}に通知するか否かをセット
	 * デフォルトtrue
	 * 
	 * @param notifyListeners
	 */
	public void setNotifyListeners(boolean notifyListeners) {
		this.notifyListeners = notifyListeners;
	}
	
	
	/**
	 * ユーザにより、当該Entityがロックされているか否かを確認せず削除処理する。
	 * @return
	 */
	public DeleteOption noCheckLockedByUser() {
		this.checkLockedByUser = false;
		return this;
	}
	
	/**
	 * 削除時、パージしないように設定
	 * @return
	 */
	public DeleteOption noPurge() {
		this.purge = false;
		return this;
	}
	
	/**
	 * 削除時に{@link EntityEventListener}に通知しないように設定。
	 * @return
	 */
	public DeleteOption unnotifyListeners() {
		this.notifyListeners = false;
		return this;
	}
	
	@Override
	public String toString() {
		return "DeleteOption [checkTimestamp=" + checkTimestamp + ", targetVersion=" + targetVersion + ", purge="
				+ purge + ", checkLockedByUser=" + checkLockedByUser
				+ ", notifyListeners=" + notifyListeners + "]";
	}

}
