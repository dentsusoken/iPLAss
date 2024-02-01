/*
 * Copyright (C) 2015 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.tools.data.permissionexplorer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.tools.dto.permissionexplorer.PermissionInfo;
import org.iplass.adminconsole.shared.tools.dto.permissionexplorer.PermissionSearchResult;
import org.iplass.adminconsole.shared.tools.dto.permissionexplorer.RolePermissionInfo;
import org.iplass.mtp.entity.Entity;

import com.google.gwt.core.client.JavaScriptObject;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public abstract class PermissionListGridDS extends PermissionGridDS {

	protected PermissionListGridDS(LinkedHashMap<String, Entity> roleMap) {
		super(roleMap);
	}

	@Override
	public String getColRoleCode(int col) {
		int roleIndex = getColRoleCodeIndex(col);
		if (roleIndex < 0) {
			return null;
		}
		return getRoleCodeList().get(roleIndex);
	}

	@Override
	public int getColRoleCodeIndex(int col) {
		//行番号列と名前列を考慮
		if (col < 2 || (getRoleCodeList().size() <= (col - 2))) {
			return -1;
		}
		return col - 2;
	}

	/**
	 * <p>ListGridFieldを生成します。</p>
	 *
	 * @return ListGridField
	 */
	public ListGridField[] getListGridField() {

		List<ListGridField> fields = new ArrayList<ListGridField>();

		//ListGridではDisplayNameは未利用
		ListGridField displayNameField = new ListGridField("displayName", "Display Name");
		displayNameField.setHidden(true);
		fields.add(displayNameField);
		ListGridField definitionNameField = new ListGridField("definitionName", "Name");
		//definitionNameField.setHidden(true);
		definitionNameField.setWidth(200);
		definitionNameField.setFrozen(true);
		fields.add(definitionNameField);

		int i = 0;
		List<ListGridField> summaryFieldList = new ArrayList<ListGridField>();
		List<ListGridField> codeFieldList = new ArrayList<ListGridField>();
		List<ListGridField> dataFieldList = new ArrayList<ListGridField>();
		for (String roleCode : getRoleCodeList()) {
			//タイトルはロールコード
			ListGridField roleSummaryField = new ListGridField("roleSummary_" + i, roleCode);
			roleSummaryField.setWidth(110);
			summaryFieldList.add(roleSummaryField);

			ListGridField roleCodeField = new ListGridField("roleCode_" + i, null);
			roleCodeField.setHidden(true);
			codeFieldList.add(roleCodeField);

			ListGridField roleDataField = new ListGridField("roleData_" + i, null);
			roleDataField.setHidden(true);
			dataFieldList.add(roleDataField);
			i++;
		}
		fields.addAll(summaryFieldList);
		fields.addAll(codeFieldList);
		fields.addAll(dataFieldList);

		return fields.toArray(new ListGridField[]{});
	}

	/**
	 * <p>画面に表示されているGrid情報から全PermissionInfoを生成します。</p>
	 *
	 * @param records Gridレコード
	 * @return 全PermissionInfo情報
	 */
	public PermissionSearchResult getEditPermissionData(ListGridRecord[] records) {

		List<PermissionInfo> permissionList = new ArrayList<PermissionInfo>();

		if (!getRoleCodeList().isEmpty() && records != null) {
			for (ListGridRecord lgrecord : records) {
				PermissionListGridRecord record = (PermissionListGridRecord)lgrecord;

				PermissionInfo permissionInfo = new PermissionInfo();
				permissionInfo.setDefinitionName(record.getDefinitionName());
				permissionInfo.setDisplayName(record.getDisplayName());

				List<RolePermissionInfo> rolePermissionList = new ArrayList<RolePermissionInfo>();
				for (int i = 0; i < getRoleCodeList().size(); i++) {
					if (record.getPermission(i) != null) {
						for (Entity permission : record.getPermission(i)) {
							RolePermissionInfo rolePermissionInfo = new RolePermissionInfo();
							rolePermissionInfo.setPermission(permission);
							rolePermissionInfo.setStatus(record.getPermissionStatus(i));	//StatusはRoleレベルで同じ
							rolePermissionList.add(rolePermissionInfo);
						}
					}
				}

				if (!rolePermissionList.isEmpty()) {
					permissionInfo.setRolePermissionList(rolePermissionList);
				}

				permissionList.add(permissionInfo);
			}
		}

		PermissionSearchResult result = new PermissionSearchResult();
		result.setPermissionList(permissionList);
		return result;
	}

	/**
	 * <p>画面に表示されているGrid情報から更新対象PermissionInfoを生成します。</p>
	 *
	 * @param records Gridレコード
	 * @return 更新対象PermissionInfo情報
	 */
	public List<PermissionInfo> getUpdatePermissionInfoList(ListGridRecord[] records) {

		List<PermissionInfo> permissionList = new ArrayList<PermissionInfo>();

		if (!getRoleCodeList().isEmpty() && records != null) {
			for (ListGridRecord lgrecord : records) {
				PermissionListGridRecord record = (PermissionListGridRecord)lgrecord;

				List<RolePermissionInfo> rolePermissionList = new ArrayList<RolePermissionInfo>();
				for (int i = 0; i < getRoleCodeList().size(); i++) {
					//Statusが設定されている場合のみ対象(追加、編集、削除されているデータ)
					if (record.getPermissionStatus(i) != null && !record.getPermissionStatus(i).isEmpty()) {
						if (record.getPermission(i) != null) {
							for (Entity permission : record.getPermission(i)) {
								RolePermissionInfo rolePermissionInfo = new RolePermissionInfo();
								rolePermissionInfo.setPermission(permission);
								rolePermissionInfo.setStatus(record.getPermissionStatus(i));	//StatusはRoleレベルで同じ
								rolePermissionList.add(rolePermissionInfo);
							}
						}
					}
				}

				if (!rolePermissionList.isEmpty()) {
					//編集対象のデータがある場合のみPermissionInfoを対象にする
					PermissionInfo permissionInfo = new PermissionInfo();
					permissionInfo.setDefinitionName(record.getDefinitionName());
					permissionInfo.setDisplayName(record.getDisplayName());
					permissionInfo.setRolePermissionList(rolePermissionList);
					permissionList.add(permissionInfo);
				}
			}
		}
		return permissionList;
	}

	/**
	 * <p>検索結果をResponseにセットします。</p>
	 *
	 * @param requestId リクエストID
	 * @param request リクエスト
	 * @param response レスポンス
	 * @param result 検索結果
	 */
	protected void setResponsePermissinData(String requestId, DSRequest request, DSResponse response, PermissionSearchResult result) {
		List<PermissionListGridRecord> records = createRecordList(result.getPermissionList());
		response.setData(records.toArray(new PermissionListGridRecord[]{}));
		response.setTotalRows(records.size());
		response.setStartRow(0);
		processResponse(requestId, response);
	}

	/**
	 * <p>Permissionを編集値に変更します。</p>
	 *
	 * @param record 対象レコード
	 * @param roleIndex ロールIndex
	 * @param permissionArray 変更値
	 */
	public void updatePermission(PermissionListGridRecord record, int roleIndex, Entity[] permissionArray) {

		Entity permission = permissionArray[0];

		if (SmartGWTUtil.isEmpty(permission.getOid())) {
			//新規(新規登録で複数状態はないので、1件目のOIDが空の場合のみ新規になる)
			record.setPermissionStatus(roleIndex, RolePermissionInfo.INSERT);
		} else {
			//更新
			record.setPermissionStatus(roleIndex, RolePermissionInfo.UPDATE);
		}

		record.setPermission(roleIndex, permissionArray);
		record.setPermissionSummary(roleIndex, getPermissionEditingSummaryText(permission));
	}

	/**
	 * <p>Permissionを全て削除対象にします。</p>
	 *
	 * @param record 対象レコード
	 */
	public void deleteAllPermission(PermissionListGridRecord record) {
		if (getRoleCodeList() != null) {
			for (int i = 0; i < getRoleCodeList().size(); i++) {
				deletePermission(record, i);
			}
		}
	}

	/**
	 * <p>Permissionを削除対象にします。</p>
	 *
	 * @param record 対象レコード
	 * @param roleIndex ロールIndex
	 */
	public void deletePermission(PermissionListGridRecord record, int roleIndex) {
		if (record.getPermission(roleIndex) != null) {
			String status = record.getPermissionStatus(roleIndex);
			if (status != null && status.equals(RolePermissionInfo.INSERT)) {
				//追加されているものは直接消す
				record.setPermission(roleIndex, null);
				record.setPermissionStatus(roleIndex, "");
				record.setPermissionSummary(roleIndex, "");
			} else {
				record.setPermissionStatus(roleIndex, RolePermissionInfo.DELETE);
				record.setPermissionSummary(roleIndex, getPermissionDeleteSummaryText());
			}
		}
	}

	/**
	 * <p>Permissionに対して全て削除オペレーションが可能かを返します。</p>
	 *
	 * @param record 対象レコード
	 * @return true:可能
	 */
	public boolean canDeleteAllPermission(PermissionListGridRecord record) {
		if (getRoleCodeList() == null) {
			return false;
		}
		for (int i = 0; i < getRoleCodeList().size(); i++) {
			if (canDeletePermission(record, i)) {
				//1つでも削除可能であれば可能
				return true;
			}
		}
		return false;
	}

	/**
	 * <p>Permissionに対して削除オペレーションが可能かを返します。</p>
	 *
	 * @param record 対象レコード
	 * @param roleIndex ロールIndex
	 * @return true:可能
	 */
	public boolean canDeletePermission(PermissionListGridRecord record, int roleIndex) {
		Entity[] permission = record.getPermission(roleIndex);
		if (permission != null) {
			String status = record.getPermissionStatus(roleIndex);
			//編集されてないか削除以外は削除可能
			if (status == null || !status.equals(RolePermissionInfo.DELETE)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * <p>Permissionの状態が削除かを返します。</p>
	 *
	 * @param record 対象レコード
	 * @param roleIndex ロールIndex
	 * @return true:削除対象
	 */
	public boolean isDeletingPermission(PermissionListGridRecord record, int roleIndex) {
		return RolePermissionInfo.DELETE.equals(record.getPermissionStatus(roleIndex));
	}

	/**
	 * <p>Permissionの状態が変更あり(新規も含む)かを返します。</p>
	 *
	 * @param record 対象レコード
	 * @param roleIndex ロールIndex
	 * @return true:変更あり
	 */
	public boolean isEditingPermission(PermissionListGridRecord record, int roleIndex) {
		return record.getPermissionStatus(roleIndex) != null;
	}

	/**
	 * <p>Permissionの状態が設定済み(新規も含む)かを返します。</p>
	 *
	 * @param record 対象レコード
	 * @param roleIndex ロールIndex
	 * @return true:設定済み
	 */
	public boolean isConfiguredPermission(PermissionListGridRecord record, int roleIndex) {
		return record.getPermission(roleIndex) != null;
	}

	/**
	 * 検索結果をもとにListGridRecordを生成します。
	 *
	 * @param permissionInfoList 検索結果
	 * @return レコード
	 */
	private List<PermissionListGridRecord> createRecordList(List<PermissionInfo> permissionInfoList) {
		List<PermissionListGridRecord> list = new ArrayList<PermissionListGridRecord>();

		if (permissionInfoList != null) {
			for (PermissionInfo permissionInfo : permissionInfoList) {
				PermissionListGridRecord record = new PermissionListGridRecord(getRoleCodeList(), permissionInfo);
				list.add(record);
			}
		}
		return list;
	}

	public class PermissionListGridRecord extends ListGridRecord {

		private List<String> roleCodeList;

		public PermissionListGridRecord(){
		}

		public PermissionListGridRecord(List<String> roleCodeList, PermissionInfo info) {
			this.roleCodeList = roleCodeList;

			setDefinitionName(info.getDefinitionName());

			if (SmartGWTUtil.isEmpty(info.getDisplayName())) {
				setDisplayName(info.getDefinitionName());
			} else {
				setDisplayName(info.getDisplayName());
			}

			if (info.getRolePermissionList() != null) {
				for (RolePermissionInfo permissionInfo : info.getRolePermissionList()) {
					Entity permission = permissionInfo.getPermission();
					String roleCode = permission.getValue("role.code");
					addPermission(roleCode, permission);
					setPermissionStatus(roleCode, permissionInfo.getStatus());
					setPermissionSummary(roleCode, getPermissionConfiguredSummaryText(permission));
				}
			}
		}

		public void setDefinitionName(String value) {
			setAttribute("definitionName", value);
		}

		public String getDefinitionName() {
			return getAttribute("definitionName");
		}

		public void setDisplayName(String value) {
			setAttribute("displayName", value);
		}

		public String getDisplayName() {
			return getAttribute("displayName");
		}

		public void setPermission(String roleCode, Entity[] value) {
			setPermission(getRoleIndex(roleCode), value);
		}

		public void setPermission(int roleIndex, Entity[] value) {
			if (roleIndex >= 0) {
				setAttribute("roleData_" + roleIndex, value);
			}
		}

		public Entity[] getPermission(String roleCode) {
			return getPermission(getRoleIndex(roleCode));
		}

		public Entity[] getPermission(int roleIndex) {
			if (roleIndex < 0) {
				return null;
			}
			Object jsObject = getAttributeAsObject("roleData_" + roleIndex);
			if (jsObject != null) {
				Object[] objArray = (Object[]) JSOHelper.convertToJava((JavaScriptObject)jsObject, true);
				Entity[] entityArray = new Entity[objArray.length];
				int cnt = 0;
				for (Object obj : objArray) {
					entityArray[cnt]  = (Entity) obj;
					cnt ++;
				}
				return entityArray;
			} else {
				return null;
			}
		}

		public void addPermission(String roleCode, Entity value) {
			Entity[] current = getPermission(roleCode);
			if (current == null) {
				setPermission(roleCode, new Entity[]{value});
			} else {
				Entity[] newArray = new Entity[current.length + 1];
				for (int i = 0; i < current.length; i++) {
					newArray[i] = current[i];
				}
				newArray[current.length] = value;
				setPermission(roleCode, newArray);
			}
		}

		public void setPermissionSummary(String roleCode, String value) {
			setPermissionSummary(getRoleIndex(roleCode), value);
		}

		public void setPermissionSummary(int roleIndex, String value) {
			if (roleIndex >= 0) {
				setAttribute("roleSummary_" + roleIndex, value);
			}
		}

		public String getPermissionSummary(String roleCode) {
			return getPermissionSummary(getRoleIndex(roleCode));
		}

		public String getPermissionSummary(int roleIndex) {
			if (roleIndex < 0) {
				return null;
			}
			return getAttribute("roleSummary_" + roleIndex);
		}

		private void setPermissionStatus(String roleCode, String value) {
			setPermissionStatus(getRoleIndex(roleCode), value);
		}

		private void setPermissionStatus(int roleIndex, String value) {
			if (roleIndex >= 0) {
				setAttribute("status_" + roleIndex, value);
			}
		}

		private String getPermissionStatus(int roleIndex) {
			if (roleIndex < 0) {
				return null;
			}
			return getAttribute("status_" + roleIndex);
		}

		private int getRoleIndex(String roleCode) {
			if (roleCodeList == null) {
				return -1;
			}
			return roleCodeList.indexOf(roleCode);
		}

	}

}
