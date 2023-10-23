/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.gem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.poi.util.StringUtil;
import org.iplass.gem.AutoGenerateSetting.DisplayPosition;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;

/**
 * gem固有の設定など
 * @author lis3wg
 */
public class GemConfigService implements Service {

	/** リクエストのパラメータを基に参照データをロードする際、参照プロパティも合わせてロードするか */
	private boolean loadWithReference;

	/** 詳細表示画面で数値プロパティの値をカンマでフォーマットするか */
	private boolean formatNumberWithComma;

	/** CSVダウンロード件数上限 */
	private int csvDownloadMaxCount;

	/** Upload形式のCSVダウンロード時の一括ロード件数 */
	private int uploadableCsvDownloadLoadSize;

	/** CSVダウンロード選択可能文字コード */
	private List<String> csvDownloadCharacterCode;

	/** CSVダウンロード常時ダブルクォート出力 */
	private boolean csvDownloadQuoteAll;

	/** Upload形式のCSVダウンロード参照項目バージョン出力 */
	private boolean csvDownloadReferenceVersion;

	/** Upload形式のCSVダウンロード時に被参照プロパティも出力する */
	private boolean uploadableCsvDownloadWithMappedByReference;

	/** CSVアップロードコミット単位 */
	private int csvUploadCommitCount;

	/** CSVアップロード非同期 */
	private boolean csvUploadAsync;

	/** CSVアップロードステータスポーリングのインターバル */
	private int csvUploadStatusPollingInterval;

	/** CSVダウンロード時User名取得のための検索内部キャッシュサイズ */
	private int searchResultCacheLimit;

	/** 編集画面で保存時に確認ダイアログを表示するか */
	private boolean confirmEditSave;

	/** 編集画面でキャンセル時に確認ダイアログを表示するか */
	private boolean confirmEditCancel;

	/** 編集画面でキャンセル時にTopViewに戻るか */
	@Deprecated
	private boolean topViewEditCancelBackToTop;

	/** 検索画面でリセットボタンを表示するか */
	private boolean showSeachCondResetButton;

	/** 検索画面で表示する検索結果の件数*/
	private int searchResultDispRowCount;

	/** 検索処理のインターバル */
	private int searchInterval;

	/** CSVダウンロードのインターバル */
	private int csvDownloadInterval;

	/** 汎用検索のCSVダウンロードでフッターを出力するか */
	private boolean csvDownloadWithFooter;

	/** 汎用検索のCSVダウンロードのフッター文言 */
	private String csvDownloadFooter;

	/** 検索処理で表示ラベルとして扱うプロパティを検索条件に利用するか */
	private boolean useDisplayLabelItemInSearch;

	/** CSVダウンロード処理で表示ラベルとして扱うプロパティを出力するか */
	private boolean useDisplayLabelItemInCsvDownload;

	/** プルダウンの「選択してください」を表示するか */
	private boolean showPulldownPleaseSelectLabel;

	/** DatePickerのデフォルトオプション */
	private String datePickerDefaultOption;

	/** ゴミ箱の表示件数上限 */
	private int recycleBinMaxCount;

	/** 一括削除のコミット件数 */
	private int deleteAllCommandBatchSize;

	/** 一括更新のコミット件数 */
	private int bulkUpdateAllCommandBatchSize;

	/** エンティティをコピーする際にLobデータをシャッローコピーするか */
	private boolean shallowCopyLobData;

	/** Binaryダウンロード時のログ出力設定 */
	private List<BinaryDownloadLoggingTargetProperty> binaryDownloadLoggingTargetProperty;

	/** エンティティのバイナリプロパティにアップロード受け入れ可能な MIME Types  正規表現パターン */
	private Pattern binaryUploadAcceptMimeTypesPattern;

	/** カラー */
	private List<ImageColorSetting> imageColors;

	/** スキン */
	private List<Skin> skins;

	/** テーマ */
	private List<Theme> themes;

	/** EntityViewHelper */
	private EntityViewHelper entityViewHelper;

	/** 自動生成設定 */
	private AutoGenerateSetting autoGenerateSetting;

	/** GemAuth Gem許可ロール */
	private List<String> permitRolesToGem;

	/** GemAuth EntityViewが未定義の場合の許可ロール */
	private List<String> permitRolesToNoView;

	@Override
	public void init(Config config) {
		binaryDownloadLoggingTargetProperty = config.getValues("binaryDownloadLoggingTargetProperty", BinaryDownloadLoggingTargetProperty.class);
		String binaryUploadAcceptMimeTypesPattern = config.getValue("binaryUploadAcceptMimeTypesPattern");
		this.binaryUploadAcceptMimeTypesPattern = StringUtil.isNotBlank(binaryUploadAcceptMimeTypesPattern)
				? Pattern.compile(binaryUploadAcceptMimeTypesPattern)
				: null;

		imageColors = config.getValues("imageColors", ImageColorSetting.class);
		loadWithReference = Boolean.valueOf(config.getValue("loadWithReference"));
		formatNumberWithComma = Boolean.valueOf(config.getValue("formatNumberWithComma"));
		csvDownloadMaxCount = config.getValue("csvDownloadMaxCount", Integer.class, 65535);
		uploadableCsvDownloadLoadSize = config.getValue("uploadableCsvDownloadLoadSize", Integer.class, 1);
		csvDownloadCharacterCode = config.getValues("csvDownloadCharacterCode");
		if (csvDownloadCharacterCode == null) {
			csvDownloadCharacterCode = new ArrayList<>();
			csvDownloadCharacterCode.add("UTF-8");
		}

		csvDownloadQuoteAll = config.getValue("csvDownloadQuoteAll", Boolean.class, true);

		csvDownloadReferenceVersion = config.getValue("csvDownloadReferenceVersion", Boolean.class, true);

		uploadableCsvDownloadWithMappedByReference = config.getValue("uploadableCsvDownloadWithMappedByReference", Boolean.class, false);

		csvUploadCommitCount = config.getValue("csvUploadCommitCount", Integer.class, 1000);

		csvUploadAsync = Boolean.valueOf(config.getValue("csvUploadAsync"));

		csvUploadStatusPollingInterval = config.getValue("csvUploadStatusPollingInterval", Integer.class, 10000);

		searchResultCacheLimit = config.getValue("searchResultCacheLimit", Integer.class, 300);

		confirmEditSave = Boolean.valueOf(config.getValue("confirmEditSave"));

		confirmEditCancel = Boolean.valueOf(config.getValue("confirmEditCancel"));

		topViewEditCancelBackToTop = config.getValue("topViewEditCancelBackToTop", Boolean.class, false);

		showSeachCondResetButton = Boolean.valueOf(config.getValue("showSeachCondResetButton"));

		searchResultDispRowCount = config.getValue("searchResultDispRowCount", Integer.class, 10);

		searchInterval = config.getValue("searchInterval", Integer.class, 1000);

		csvDownloadInterval = config.getValue("csvDownloadInterval", Integer.class, 1000);

		csvDownloadWithFooter = config.getValue("csvDownloadWithFooter", Boolean.class, false);
		csvDownloadFooter = config.getValue("csvDownloadFooter");
		if (csvDownloadFooter == null) {
			csvDownloadFooter = "";
		}

		useDisplayLabelItemInSearch = config.getValue("useDisplayLabelItemInSearch", Boolean.class, false);

		useDisplayLabelItemInCsvDownload = config.getValue("useDisplayLabelItemInCsvDownload", Boolean.class, false);

		showPulldownPleaseSelectLabel = config.getValue("showPulldownPleaseSelectLabel", Boolean.class, true);

		datePickerDefaultOption = config.getValue("datePickerDefaultOption", String.class, null);

		recycleBinMaxCount = config.getValue("recycleBinMaxCount", Integer.class, 100);

		deleteAllCommandBatchSize = config.getValue("deleteAllCommandBatchSize", Integer.class, 100);

		bulkUpdateAllCommandBatchSize = config.getValue("bulkUpdateAllCommandBatchSize", Integer.class, 100);

		shallowCopyLobData = config.getValue("shallowCopyLobData", Boolean.class, false);

		skins = config.getValues("skins", Skin.class);
		themes = config.getValues("themes", Theme.class);

		entityViewHelper = config.getValue("entityViewHelper", EntityViewHelper.class);

		autoGenerateSetting = new AutoGenerateSetting();
		autoGenerateSetting.setShowSystemProperty(config.getValue("autoGenerateShowSystemProperty", Boolean.class, false));
		if (autoGenerateSetting.isShowSystemProperty()) {
			if (config.getValue("autoGenerateSystemProperties") != null) {
				Set<String> supports = new HashSet<>(Arrays.asList(
						Entity.OID,
						Entity.VERSION,
						Entity.CREATE_BY,
						Entity.CREATE_DATE,
						Entity.UPDATE_BY,
						Entity.UPDATE_DATE,
						Entity.LOCKED_BY
						));
				String systemPropertiesStr = config.getValue("autoGenerateSystemProperties");
				String[] inputArray = systemPropertiesStr.split(",");
				String[] validArray = Arrays.stream(inputArray)
						.filter(property -> supports.contains(property.trim()))
						.map(String::trim)
						.toArray(String[]::new);
				autoGenerateSetting.setSystemProperties(validArray);
			}
			if (config.getValue("autoGenerateSystemPropertyDisplayPosition") != null) {
				String displayPosition = config.getValue("autoGenerateSystemPropertyDisplayPosition");
				autoGenerateSetting.setSystemPropertyDisplayPosition(DisplayPosition.valueOf(displayPosition));
			}
			autoGenerateSetting.setExcludeOidWhenCustomOid(config.getValue("autoGenerateExcludeOidWhenCustomOid", Boolean.class, true));
			autoGenerateSetting.setUseUserPropertyEditor(config.getValue("autoGenerateUseUserPropertyEditor", Boolean.class, true));
		}

		permitRolesToGem = config.getValues("permitRolesToGem");
		permitRolesToNoView = config.getValues("permitRolesToNoView");
	}

	@Override
	public void destroy() {
	}

	/**
	 * リクエストのパラメータを基に参照データをロードする際、参照プロパティも合わせてロードするかを取得します。
	 * @return リクエストのパラメータを基に参照データをロードする際、参照プロパティも合わせてロードするか
	 */
	public boolean isLoadWithReference() {
		return loadWithReference;
	}

	/**
	 * 詳細表示画面で数値プロパティの値をカンマでフォーマットするかを取得します。
	 * @return 詳細表示画面で数値プロパティの値をカンマでフォーマットするか
	 */
	public boolean isFormatNumberWithComma() {
		return formatNumberWithComma;
	}

	/**
	 * CSVダウンロード件数の上限値を取得します。
	 * @return CSVダウンロード件数上限
	 */
	public int getCsvDownloadMaxCount() {
		return csvDownloadMaxCount;
	}

	/**
	 * Upload形式のCSVダウンロード時の一括ロード件数を取得します。
	 * @return Upload形式のCSVダウンロード時の一括ロード件数
	 */
	public int getUploadableCsvDownloadLoadSize() {
		return uploadableCsvDownloadLoadSize;
	}

	/**
	 * CSVダウンロード時に選択可能な文字コードリストを取得します。
	 * @return CSVダウンロード時に選択可能な文字コードリスト
	 */
	public List<String> getCsvDownloadCharacterCode() {
		return csvDownloadCharacterCode;
	}

	/**
	 * CSVダウンロード時に常時ダブルクォートを出力するかを取得します。
	 * @return CSVダウンロード時に常時ダブルクォートを出力するか
	 */
	public boolean isCsvDownloadQuoteAll() {
		return csvDownloadQuoteAll;
	}

	/**
	 * CSVダウンロード参照項目バージョン出力を取得します。
	 * @return CSVダウンロード参照項目バージョン出力
	 */
	public boolean isCsvDownloadReferenceVersion() {
		return csvDownloadReferenceVersion;
	}

	/**
	 * Upload形式のCSVダウンロード時に被参照プロパティも出力を取得します。
	 * @return Upload形式のCSVダウンロード時に被参照プロパティも出力
	 */
	public boolean isUploadableCsvDownloadWithMappedByReference() {
		return uploadableCsvDownloadWithMappedByReference;
	}

	/**
	 * CSVアップロードコミット単位を取得します。
	 * @return CSVアップロードコミット単位
	 */
	public int getCsvUploadCommitCount() {
		return csvUploadCommitCount;
	}

	/**
	 * CSVアップロードを非同期で行うかを取得します。
	 * @return CSVアップロードを非同期で行うか
	 */
	public boolean isCsvUploadAsync() {
		return csvUploadAsync;
	}

	/**
	 * CSVアップロードステータスポーリングのインターバルを取得します。
	 * @return CSVアップロードステータスポーリングのインターバル
	 */
	public int getCsvUploadStatusPollingInterval() {
		return csvUploadStatusPollingInterval;
	}

	/**
	 * CSVダウンロード時User名取得のための検索内部キャッシュサイズを取得します。
	 * @return CSVダウンロード時User名取得のための検索内部キャッシュサイズ
	 */
	public int getSearchResultCacheLimit() {
		return searchResultCacheLimit;
	}

	/**
	 * 編集画面で保存時に確認ダイアログを表示するかを取得します。
	 * @return 編集画面で保存時に確認ダイアログを表示するか
	 */
	public boolean isConfirmEditSave() {
		return confirmEditSave;
	}

	/**
	 * 編集画面でキャンセル時に確認ダイアログを表示するかを取得します。
	 * @return 編集画面でキャンセル時に確認ダイアログを表示するか
	 */
	public boolean isConfirmEditCancel() {
		return confirmEditCancel;
	}

	/**
	 * 詳細画面から編集画面に遷移した際にキャンセル時にTopViewに戻るかを取得します。
	 * @return 編集画面でキャンセル時にTopViewに戻るか
	 * @deprecated 3.0.20までの互換設定です。今後は詳細画面に遷移する動作に統一する予定です。
	 */
	@Deprecated
	public boolean isTopViewEditCancelBackToTop() {
		return topViewEditCancelBackToTop;
	}

	/**
	 * 検索画面でリセットボタンを表示するかを取得します。
	 * @return 検索画面でリセットボタンを表示するか
	 */
	public boolean isShowSeachCondResetButton() {
		return showSeachCondResetButton;
	}

	/**
	 * 検索画面で表示する検索結果の件数を取得します。
	 * @return 検索画面で表示する検索結果の件数
	 */
	public int getSearchResultDispRowCount() {
		return searchResultDispRowCount;
	}

	/**
	 * 検索処理のインターバルを取得します。
	 * @return 検索処理のインターバル
	 */
	public int getSearchInterval() {
		return searchInterval;
	}

	/**
	 * CSVダウンロードのインターバルを取得します。
	 * @return CSVダウンロードのインターバル
	 */
	public int getCsvDownloadInterval() {
		return csvDownloadInterval;
	}

	/**
	 * 汎用検索のCSVダウンロードでフッターを出力するかを取得します。
	 * @return 汎用検索のCSVダウンロードでフッターを出力するか
	 */
	public boolean isCsvDownloadWithFooter() {
		return csvDownloadWithFooter;
	}

	/**
	 * 汎用検索のCSVダウンロードのフッター文言を取得します。
	 * @return 汎用検索のCSVダウンロードのフッター文言
	 */
	public String getCsvDownloadFooter() {
		return csvDownloadFooter;
	}

	/**
	 * 検索処理で表示ラベルとして扱うプロパティを検索条件に利用するかを取得します。
	 * @return 検索処理で表示ラベルとして扱うプロパティを検索条件に利用するか
	 */
	public boolean isUseDisplayLabelItemInSearch() {
		return useDisplayLabelItemInSearch;
	}

	/**
	 * CSVダウンロード処理で表示ラベルとして扱うプロパティを出力するかを取得します。
	 * @return CSVダウンロード処理で表示ラベルとして扱うプロパティを出力するか
	 */
	public boolean isUseDisplayLabelItemInCsvDownload() {
		return useDisplayLabelItemInCsvDownload;
	}

	/**
	 * プルダウンの「選択してください」を表示するかを取得します。
	 * @return プルダウンの「選択してください」を表示するか
	 */
	public boolean isShowPulldownPleaseSelectLabel() {
		return showPulldownPleaseSelectLabel;
	}

	/**
	 * DatePickerのデフォルトオプションを返します。
	 *
	 * @return DatePickerのデフォルトオプション
	 */
	public String getDatePickerDefaultOption() {
		return datePickerDefaultOption;
	}

	public List<BinaryDownloadLoggingTargetProperty> getBinaryDownloadLoggingTargetProperty() {
		return binaryDownloadLoggingTargetProperty;
	}

	/**
	 * エンティティのバイナリプロパティにアップロード受け入れ可能なMIME Types 正規表現パターンを取得します。
	 * @return エンティティのバイナリプロパティにアップロード可能なMIME Types 正規表現パターン
	 */
	public Pattern getBinaryUploadAcceptMimeTypesPattern() {
		return binaryUploadAcceptMimeTypesPattern;
	}

	public List<ImageColorSetting> getImageColors() {
		return imageColors;
	}

	public List<String> getImageColorNames() {
		if (imageColors != null && !imageColors.isEmpty()) {
			List<String> imageColorNames = imageColors.stream().map(s -> s.getColorName()).collect(Collectors.toList());
			return imageColorNames;
		} else {
			return Collections.emptyList();
		}
	}

	public List<String> getCssPathList(String skinName) {
		if (imageColors != null && !imageColors.isEmpty()) {
			List<String> imageColorNames = imageColors.stream()
					.flatMap(s -> s.getCssSettings().stream()
							.filter(c -> skinName.equals(c.getSkinName()))
							.map(c -> c.getCssPath()))
					.collect(Collectors.toList());
			return imageColorNames;
		} else {
			return Collections.emptyList();
		}
	}

	public List<Skin> getSkins() {
		return skins;
	}

	public List<Theme> getThemes() {
		return themes;
	}

	/**
	 * EntityViewHelperを取得します。
	 * @return
	 */
	public EntityViewHelper getEntityViewHelper() {
		return entityViewHelper;
	}

	/**
	 * ゴミ箱の表示件数上限値を取得します。
	 * @return ゴミ箱の表示件数上限値
	 */
	public int getRecycleBinMaxCount() {
		return recycleBinMaxCount;
	}

	/**
	 * 一括削除のコミット件数を取得します。
	 * @return 一括削除のコミット件数
	 */
	public int getDeleteAllCommandBatchSize() {
		return deleteAllCommandBatchSize;
	}

	/**
	 * 一括更新のコミット件数を取得します。
	 * @return 一括更新のコミット件数
	 */
	public int getBulkUpdateAllCommandBatchSize() {
		return bulkUpdateAllCommandBatchSize;
	}

	/**
	 * エンティティをコピーする際にLobデータをシャッローコピーするかを取得します。
	 * @return エンティティをコピーする際にLobデータをシャッローコピーするか
	 */
	public boolean isShallowCopyLobData() {
		return shallowCopyLobData;
	}

	/**
	 * 自動生成設定を取得します。
	 * @return 自動生成設定
	 */
	public AutoGenerateSetting getAutoGenerateSetting() {
		return autoGenerateSetting;
	}

	/**
	 * GemAuth Gem許可ロールを取得します。
	 * @return Gem許可ロール
	 */
	public List<String> getPermitRolesToGem() {
		return permitRolesToGem;
	}

	/**
	 * GemAuth EntityViewが未定義の場合の許可ロールを取得します。
	 * @return EntityViewが未定義の場合の許可ロール
	 */
	public List<String> getPermitRolesToNoView() {
		return permitRolesToNoView;
	}
}
