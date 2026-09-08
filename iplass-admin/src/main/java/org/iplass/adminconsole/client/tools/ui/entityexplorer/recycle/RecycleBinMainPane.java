package org.iplass.adminconsole.client.tools.ui.entityexplorer.recycle;

import java.sql.Timestamp;

import org.iplass.adminconsole.client.tools.ui.entityexplorer.EntityExplorerMainPane;

import com.smartgwt.client.widgets.layout.VLayout;

/**
 * EntityExplorerのRecycleBinにおける一覧画面とEntity別詳細画面を管理するメインパネルです。
 * <p>
 * RecycleBinタブの選択時に一覧画面を遅延生成し、Entity選択時に詳細画面を生成します。
 * 一覧画面へ戻る際、または別Entityを選択する際には既存の詳細画面を破棄します。
 */
public class RecycleBinMainPane extends VLayout {

	private final EntityExplorerMainPane owner;
	private RecycleBinListPane recycleBinListPane;
	private RecycleBinDataListPane recycleBinDataListPane;

	public RecycleBinMainPane(EntityExplorerMainPane owner) {
		this.owner = owner;

		// レイアウト設定
		setWidth100();

		// 初期状態では一覧を生成せずに、Tabが選択されたタイミングで生成する
	}

	public void selectedPane() {

		if (recycleBinListPane == null) {
			// 初期表示にゴミ箱一覧を生成
			recycleBinListPane = new RecycleBinListPane(this);
			addMember(recycleBinListPane);
		}
	}

	public void showDataListPane(String entityName, Timestamp purgeTargetDate) {
		if (recycleBinDataListPane != null) {
			recycleBinDataListPane.hide();
			recycleBinDataListPane.destroy();
		}

		recycleBinDataListPane = new RecycleBinDataListPane(this, entityName, purgeTargetDate);
		addMember(recycleBinDataListPane);
		recycleBinDataListPane.show();
		recycleBinListPane.hide();
		owner.setWorkspaceTabName(entityName);
	}

	public void backRecycleBinListPane() {
		recycleBinListPane.show();
		if (recycleBinDataListPane != null) {
			recycleBinDataListPane.hide();
			recycleBinDataListPane.destroy();
			recycleBinDataListPane = null;
		}
		owner.setWorkspaceTabName(null);
	}
}
