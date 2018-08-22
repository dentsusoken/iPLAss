/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.gem.command;

import org.iplass.gem.command.auth.LoginCommand;
import org.iplass.gem.command.auth.LogoutCommand;
import org.iplass.gem.command.auth.ReAuthCommand;
import org.iplass.gem.command.auth.ResetPasswordCommand;
import org.iplass.gem.command.auth.UpdateExpirePasswordCommand;
import org.iplass.gem.command.auth.UpdatePasswordCommand;
import org.iplass.gem.command.auth.UpdateUserCommand;
import org.iplass.gem.command.binary.DownloadCommand;
import org.iplass.gem.command.binary.UploadCommand;
import org.iplass.gem.command.calendar.AddCalendarCommand;
import org.iplass.gem.command.calendar.CalendarCommand;
import org.iplass.gem.command.calendar.GetCalendarCommand;
import org.iplass.gem.command.calendar.GetCalendarWidgetCommand;
import org.iplass.gem.command.calendar.ref.CalendarFilterCommand;
import org.iplass.gem.command.calendar.ref.GetCalendarFilterCommand;
import org.iplass.gem.command.fulltext.FullTextSearchCommand;
import org.iplass.gem.command.fulltext.FullTextSearchViewCommand;
import org.iplass.gem.command.generic.common.GetAutocompletionValueCommand;
import org.iplass.gem.command.generic.common.GetEntityNameCommand;
import org.iplass.gem.command.generic.common.GetEntityNameListCommand;
import org.iplass.gem.command.generic.delete.DeleteAllCommand;
import org.iplass.gem.command.generic.delete.DeleteCommand;
import org.iplass.gem.command.generic.delete.DeleteListCommand;
import org.iplass.gem.command.generic.delete.GetRecycleBinCommand;
import org.iplass.gem.command.generic.delete.PurgeCommand;
import org.iplass.gem.command.generic.delete.RestoreCommand;
import org.iplass.gem.command.generic.delete.TrashCommand;
import org.iplass.gem.command.generic.detail.DetailViewCommand;
import org.iplass.gem.command.generic.detail.GetMassReferencesCommand;
import org.iplass.gem.command.generic.detail.GetNestTableDataCommand;
import org.iplass.gem.command.generic.detail.GetVersionCommand;
import org.iplass.gem.command.generic.detail.InsertCommand;
import org.iplass.gem.command.generic.detail.LockCommand;
import org.iplass.gem.command.generic.detail.UnlockCommand;
import org.iplass.gem.command.generic.detail.UpdateCommand;
import org.iplass.gem.command.generic.detail.UpdateMappedbyReferenceCommand;
import org.iplass.gem.command.generic.detail.UpdateReferencePropertyCommand;
import org.iplass.gem.command.generic.detail.UpdateTableOrderCommand;
import org.iplass.gem.command.generic.refcombo.GetEditorCommand;
import org.iplass.gem.command.generic.refcombo.ReferenceComboCommand;
import org.iplass.gem.command.generic.refcombo.SearchParentCommand;
import org.iplass.gem.command.generic.reflink.GetReferenceLinkItemCommand;
import org.iplass.gem.command.generic.reftree.SearchTreeDataCommand;
import org.iplass.gem.command.generic.search.CountCommand;
import org.iplass.gem.command.generic.search.CsvDownloadCommand;
import org.iplass.gem.command.generic.search.DetailSearchCommand;
import org.iplass.gem.command.generic.search.FixedSearchCommand;
import org.iplass.gem.command.generic.search.NormalSearchCommand;
import org.iplass.gem.command.generic.search.SearchCommand;
import org.iplass.gem.command.generic.search.SearchListCommand;
import org.iplass.gem.command.generic.search.SearchNameListCommand;
import org.iplass.gem.command.generic.search.SearchSelectListCommand;
import org.iplass.gem.command.generic.search.SearchValidateCommand;
import org.iplass.gem.command.generic.search.SearchViewCommand;
import org.iplass.gem.command.generic.upload.CsvSampleDownloadCommand;
import org.iplass.gem.command.generic.upload.CsvUploadCommand;
import org.iplass.gem.command.generic.upload.CsvUploadIndexCommand;
import org.iplass.gem.command.generic.upload.CsvUploadStatusCommand;
import org.iplass.gem.command.information.InformationListCommand;
import org.iplass.gem.command.information.InformationViewCommand;
import org.iplass.gem.command.language.SelectLanguageCommand;
import org.iplass.gem.command.preview.GetPreviewDateTimeCommand;
import org.iplass.gem.command.preview.SetPreviewDateTimeCommand;
import org.iplass.gem.command.treeview.GetTreeViewDefinitionCommand;
import org.iplass.gem.command.treeview.GetTreeViewGridDataCommand;
import org.iplass.gem.command.treeview.GetTreeViewListDataCommand;
import org.iplass.mtp.command.annotation.MetaDataSeeAlso;

@MetaDataSeeAlso({
	MenuCommand.class,
	AboutCommand.class,
	ChangeRoleCommand.class,
	ConsumeTokenCommand.class,
	//集計
	//バイナリ
	DownloadCommand.class,
	UploadCommand.class,
	//カレンダー
	CalendarCommand.class,
	CalendarFilterCommand.class,
	AddCalendarCommand.class,
	GetCalendarCommand.class,
	GetCalendarWidgetCommand.class,
	GetCalendarFilterCommand.class,
	//汎用削除
	DeleteAllCommand.class,
	DeleteCommand.class,
	DeleteListCommand.class,
	GetRecycleBinCommand.class,
	PurgeCommand.class,
	RestoreCommand.class,
	TrashCommand.class,
	//汎用詳細
	DetailViewCommand.class,
	GetMassReferencesCommand.class,
	GetNestTableDataCommand.class,
	GetEntityNameCommand.class,
	GetEntityNameListCommand.class,
	GetVersionCommand.class,
	InsertCommand.class,
	LockCommand.class,
	UnlockCommand.class,
	UpdateCommand.class,
	UpdateMappedbyReferenceCommand.class,
	UpdateReferencePropertyCommand.class,
	UpdateTableOrderCommand.class,
	//自動補完
	GetAutocompletionValueCommand.class,
	//参照コンボ
	GetEditorCommand.class,
	ReferenceComboCommand.class,
	SearchParentCommand.class,
	//再帰ツリー
	SearchTreeDataCommand.class,
	//連動プロパティ
	GetReferenceLinkItemCommand.class,
	//お知らせ
	InformationListCommand.class,
	InformationViewCommand.class,
	/* 汎用検索系 */
	SearchViewCommand.class,
	SearchCommand.class,
	SearchValidateCommand.class,
	CountCommand.class,
	NormalSearchCommand.class,
	DetailSearchCommand.class,
	FixedSearchCommand.class,
	SearchListCommand.class,
	SearchNameListCommand.class,
	SearchSelectListCommand.class,
	CsvDownloadCommand.class,
	CsvUploadIndexCommand.class,
	CsvUploadCommand.class,
	CsvUploadStatusCommand.class,
	CsvSampleDownloadCommand.class,
	//ツリー
	GetTreeViewDefinitionCommand.class,
	GetTreeViewGridDataCommand.class,
	GetTreeViewListDataCommand.class,
	//汎用WebAPI
//	CreateEntityCommand.class,
//	DeleteEntityCommand.class,
//	LoadEntityCommand.class,
//	QueryEntityCommand.class,
//	UpdateEntityCommand.class,
//	org.iplass.mtp.impl.webapi.command.CSVUploadCommand.class,
	SelectLanguageCommand.class,
	//認証
	LoginCommand.class,
	LogoutCommand.class,
	UpdatePasswordCommand.class,
	UpdateExpirePasswordCommand.class,
	ResetPasswordCommand.class,
	UpdateUserCommand.class,
	ReAuthCommand.class,
	//プレビュー日付
	GetPreviewDateTimeCommand.class,
	SetPreviewDateTimeCommand.class,
	FullTextSearchViewCommand.class,
	FullTextSearchCommand.class
})
public class MetaDataRefs {
}
