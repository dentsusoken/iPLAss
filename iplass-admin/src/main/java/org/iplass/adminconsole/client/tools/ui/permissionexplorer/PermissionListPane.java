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

package org.iplass.adminconsole.client.tools.ui.permissionexplorer;


import java.util.LinkedHashMap;
import java.util.List;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.tools.dto.permissionexplorer.PermissionSearchResult;
import org.iplass.adminconsole.shared.tools.rpc.permissionexplorer.PermissionExplorerServiceAsync;
import org.iplass.adminconsole.shared.tools.rpc.permissionexplorer.PermissionExplorerServiceFactory;
import org.iplass.mtp.entity.Entity;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * PermissionListパネル
 */
public abstract class PermissionListPane extends VLayout {

	public static final String REFRESH_ICON = "[SKIN]/actions/refresh.png";
	public static final String PROGRESS_ICON = "[SKINIMG]/shared/progressCursorTracker.gif";
	public static final String EXPAND_ICON = "[SKIN]/actions/sort_descending.png";
	public static final String CONTRACT_ICON = "[SKIN]/actions/sort_ascending.png";

	private Label countLabel;
	private ToolStripButton expandAllButton;
	private ToolStripButton contractAllButton;

	private PermissionListMainPane mainPane;

	/** ロールデータ(code, Entity) */
	private LinkedHashMap<String, Entity> roleMap;

	private boolean editing = false;
	private boolean proccesing = false;

	/** 編集中の権限情報(TreeGrid用) */
	private PermissionSearchResult editingData;

	protected PermissionExplorerServiceAsync service;

	/**
	 * 一覧リフレッシュ処理(リフレッシュボタン、キャンセルボタン)
	 */
	protected abstract void doRefreshGrid();

	/**
	 * 保存ボタン処理
	 */
	protected abstract void update();

	/**
	 * ロールデータ変更反映処理
	 */
	protected abstract void doRefreshRoleList();

	/**
	 * コンストラクタ
	 */
	public PermissionListPane(PermissionListMainPane mainPane, List<Entity> roleList) {
		this.mainPane = mainPane;

		//ロールのリストをMapに変換
		convertRoleMap(roleList);

		service = PermissionExplorerServiceFactory.get();

		//レイアウト設定
		setWidth100();
		setHeight100();

		HLayout header = new HLayout();
		header.setWidth100();
		header.setHeight(30);
		header.setMargin(6);
		header.setMembersMargin(5);
		header.setAlign(Alignment.LEFT);

		IButton save = new IButton("Save");
		save.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {

				SC.ask(AdminClientMessageUtil.getString("ui_tools_permissionexplorer_PermissionMainPane_updateConf"), new BooleanCallback() {

					@Override
					public void execute(Boolean value) {
						if (value) {
							update();
						}
					}
				});
			}
		});
		IButton cancel = new IButton("Cancel");
		cancel.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				refreshGridConfirm();
			}
		});
		header.setMembers(save, cancel);

		final ToolStrip toolStrip = new ToolStrip();
		toolStrip.setWidth100();
		toolStrip.setMembersMargin(5);
		toolStrip.setAlign(VerticalAlignment.BOTTOM);

		toolStrip.addFill();

		countLabel = new Label();
		countLabel.setWrap(false);
		countLabel.setAutoWidth();
		setRecordCount(0);
		toolStrip.addMember(countLabel);

		expandAllButton = new ToolStripButton();
		expandAllButton.setIcon(EXPAND_ICON);
		expandAllButton.setTitle("Expand");
		expandAllButton.setVisible(false);	//Tree用なのでデフォルト非表示
		expandAllButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				doGridExpandAll();
			}
		});
		toolStrip.addButton(expandAllButton);

		contractAllButton = new ToolStripButton();
		contractAllButton.setIcon(CONTRACT_ICON);
		contractAllButton.setTitle("Collapse");
		contractAllButton.setVisible(false);	//Tree用なのでデフォルト非表示
		contractAllButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				doGridContractAll();
			}
		});
		toolStrip.addButton(contractAllButton);

		final ToolStripButton refreshButton = new ToolStripButton();
		refreshButton.setIcon(REFRESH_ICON);
		refreshButton.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_tools_permissionexplorer_PermissionListPane_refreshList")));
		refreshButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				refreshGridConfirm();
			}
		});
		toolStrip.addButton(refreshButton);

		setMembers(header, toolStrip);
	}

	/**
	 * <p>一覧のTree展開ボタンを表示します。</p>
	 */
	final protected void setShowExpandAllButton() {
		expandAllButton.setVisible(true);
	}

	/**
	 * <p>一覧のTree展開処理を実装してください。</p>
	 */
	protected void doGridExpandAll() {
	}

	/**
	 * <p>一覧のTree縮小ボタンを表示します。</p>
	 */
	final protected void setShowContractAllButton() {
		contractAllButton.setVisible(true);
	}

	/**
	 * <p>一覧のTree縮小処理を実装してください。</p>
	 */
	protected void doGridContractAll() {
	}

	/**
	 * <p>一覧の件数を非表示に設定します</p>
	 */
	final protected void setHiddenCountLabel() {
		countLabel.setVisible(false);
	}

	/**
	 * <p>一覧の件数を設定します。</p>
	 *
	 * @param count 件数
	 */
	final protected void setRecordCount(long count) {
		countLabel.setContents("Total Count：" + count);
	}

	/**
	 * <p>一覧を再表示します。</p>
	 *
	 * <p>タブをロード状態にした後に、{@link PermissionListPane#doRefreshGrid()}を呼び出します。
	 * 個別の検索・再表示処理は、{@link PermissionListPane#doRefreshGrid()}で実装してください。</p>
	 */
	final protected void refreshGrid() {
		setProgressStart();
		doRefreshGrid();
	}

	/**
	 * ロールMap(code, Entity)を返します。
	 *
	 * @return ロールMap(code, Entity)
	 */
	final protected LinkedHashMap<String, Entity> getRoleMap() {
		return roleMap;
	}

	/**
	 * ロールEntityを返します。
	 *
	 * @param roleCode ロールコード
	 *
	 * @return ロールEntity
	 */
	final protected Entity getRoleEntity(String roleCode) {
		return roleMap.get(roleCode);
	}

	/**
	 * <p>編集状態を設定します。</p>
	 *
	 * <p>編集中の場合、タブのタイトルを変更します。</p>
	 *
	 * @param editing 編集状態
	 */
	final protected void setEditState(boolean editing) {
		this.editing = editing;
		if (!proccesing) {
			//処理中以外の場合はタイトルをステータスに合わせて変更
			mainPane.getTab().setTitle(getStateTabTitle());
		}
	}

	/**
	 * <p>タブをロード状態にします。</p>
	 */
	final protected void setProgressStart() {
		proccesing = true;
		mainPane.getTab().setTitle("<span>" + Canvas.imgHTML(PROGRESS_ICON) + "&nbsp;Loading...</span>");
	}

	/**
	 * <p>タブのロード状態を解除します。</p>
	 */
	final protected void setProgressFinish() {
		proccesing = false;
		mainPane.getTab().setTitle(getStateTabTitle());
	}

	/**
	 * <p>編集中かを返します。</p>
	 *
	 * <p>編集中の状態を設定するには、{@link #setEditState(boolean)}を利用してください。</p>
	 *
	 * @return true:編集中
	 */
	final public boolean isEditing() {
		return editing;
	}

	final protected PermissionSearchResult getEditingData() {
		return editingData;
	}

	final protected void setEditingData(PermissionSearchResult editingData) {
		this.editingData = editingData;
	}

	/**
	 * <p>ロールデータ反映処理</p>
	 *
	 * <p>MainPaneでロール変更通知を受け取った際に呼び出されます。
	 * 個別の反映処理は、{@link PermissionListPane#refreshRoleList()}で実装してください。</p>
	 *
	 * @param roleList
	 */
	final public void changeRoleList(List<Entity> roleList) {
		convertRoleMap(roleList);

		doRefreshRoleList();
	}

	private String getStateTabTitle() {
		if (editing) {
			return "* " + mainPane.getTabTitle();
		} else {
			return mainPane.getTabTitle();
		}
	}

	/**
	 * <p>ロールリストのMap変換処理</p>
	 *
	 * <p>ロールコードを利用する箇所が多いので、毎回LoopしないようにKEYとして保持する。</p>
	 *
	 * @param roleList ロールリスト
	 */
	private void convertRoleMap(List<Entity> roleList) {
		roleMap = new LinkedHashMap<String, Entity>();
		if (roleList != null) {
			for (Entity role: roleList) {
				roleMap.put((String)role.getValue("code"), role);
			}
		}
	}

	/**
	 * キャンセルボタン、リフレッシュボタンによる明示的な一覧リセット処理
	 */
	private void refreshGridConfirm() {
		if (isEditing()) {
			//編集中の場合は確認メッセージを表示
			SC.ask(AdminClientMessageUtil.getString("ui_tools_permissionexplorer_PermissionListPane_refreshConf"), new BooleanCallback() {
				@Override
				public void execute(Boolean value) {
					if (value) {
						refreshGrid();
					}
				}
			});
		} else {
			refreshGrid();
		}
	}

}
