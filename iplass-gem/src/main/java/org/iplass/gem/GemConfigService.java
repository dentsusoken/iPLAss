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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

	/** CSVダウンロード選択可能文字コード */
	private List<String> csvDownloadCharacterCode;

	/** CSVダウンロード常時ダブルクォート出力 */
	private boolean csvDownloadQuoteAll;

	/** CSVダウンロード参照項目バージョン出力 */
	private boolean csvDownloadReferenceVersion;

	/** CSVアップロードコミット単位 */
	private int csvUploadCommitCount;

	/** CSVアップロード非同期 */
	private boolean csvUploadAsync;

	/** CSVアップロードステータスポーリングのインターバル */
	private int csvUploadStatusPollingInterval;

	/** CSVダウンロード時User名取得のための検索内部キャッシュサイズ */
	private int searchResultCacheLimit;

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

	/** プルダウンの「選択してください」を表示するか */
	private boolean showPulldownPleaseSelectLabel;

	/** ゴミ箱の表示件数上限 */
	private int recycleBinMaxCount;

	/** 一括削除のコミット件数 */
	private int deleteAllCommandBatchSize;

	/** 一括更新のコミット件数 */
	private int bulkUpdateAllCommandBatchSize;

	/** エンティティをコピーする際にLobデータをシャッローコピーするか */
	private boolean shallowCopyLobData;

	private List<BinaryDownloadLoggingTargetProperty> binaryDownloadLoggingTargetProperty;

	private List<ImageColorSetting> imageColors;
	private Map<String, ImageColorSetting> imageColorsMap;

	private List<Skin> skins;

	private List<Theme> themes;

	/** EntityViewHelper */
	private EntityViewHelper entityViewHelper;

	/** 自動生成設定 */
	private AutoGenerateSetting autoGenerateSetting;

	@SuppressWarnings("unchecked")
	@Override
	public void init(Config config) {
		binaryDownloadLoggingTargetProperty = config.getValues("binaryDownloadLoggingTargetProperty", BinaryDownloadLoggingTargetProperty.class);
		imageColors = config.getValues("imageColors", ImageColorSetting.class);
		imageColorsMap = new HashMap<>();
		if (imageColors != null && !imageColors.isEmpty()) {
			for (ImageColorSetting setting : imageColors) {
				imageColorsMap.put(setting.getColorName(), setting);
			}
		}

		loadWithReference = Boolean.valueOf(config.getValue("loadWithReference"));
		formatNumberWithComma = Boolean.valueOf(config.getValue("formatNumberWithComma"));
		String csvdownloadMaxCount = config.getValue("csvDownloadMaxCount");
		if (csvdownloadMaxCount != null) {
			this.csvDownloadMaxCount = Integer.parseInt(csvdownloadMaxCount);
		} else {
			this.csvDownloadMaxCount = 65535;
		}
		csvDownloadCharacterCode = config.getValues("csvDownloadCharacterCode");
		if (csvDownloadCharacterCode == null) {
			csvDownloadCharacterCode = new ArrayList<>();
			csvDownloadCharacterCode.add("UTF-8");
		}

		if (config.getValue("csvDownloadQuoteAll") != null) {
			csvDownloadQuoteAll = Boolean.valueOf(config.getValue("csvDownloadQuoteAll"));
		} else {
			csvDownloadQuoteAll = true;
		}

		String csvdownloadReferenceVersion = config.getValue("csvDownloadReferenceVersion");
		if (csvdownloadReferenceVersion != null) {
			csvDownloadReferenceVersion = Boolean.valueOf(csvdownloadReferenceVersion);
		} else {
			csvDownloadReferenceVersion = true;
		}

		String csvUploadCommitCount = config.getValue("csvUploadCommitCount");
		if (csvUploadCommitCount != null) {
			this.csvUploadCommitCount = Integer.parseInt(csvUploadCommitCount);
		} else {
			this.csvUploadCommitCount = 1000;
		}

		csvUploadAsync = Boolean.valueOf(config.getValue("csvUploadAsync"));

		String csvUploadStatusPollingInterval = config.getValue("csvUploadStatusPollingInterval");
		if (csvUploadStatusPollingInterval != null) {
			this.csvUploadStatusPollingInterval = Integer.parseInt(csvUploadStatusPollingInterval);
		} else {
			this.csvUploadStatusPollingInterval = 10000;
		}

		String searchResultCacheLimit = config.getValue("searchResultCacheLimit");
		if (searchResultCacheLimit != null) {
			this.searchResultCacheLimit = Integer.parseInt(searchResultCacheLimit);
		} else {
			this.searchResultCacheLimit = 300;
		}

		confirmEditCancel = Boolean.valueOf(config.getValue("confirmEditCancel"));

		topViewEditCancelBackToTop = config.getValue("topViewEditCancelBackToTop", Boolean.class, false);

		showSeachCondResetButton = Boolean.valueOf(config.getValue("showSeachCondResetButton"));

		String searchResultDispRowCount = config.getValue("searchResultDispRowCount");
		if (searchResultDispRowCount != null) {
			this.searchResultDispRowCount = Integer.parseInt(searchResultDispRowCount);
		} else {
			this.searchResultDispRowCount = 10;
		}

		String searchInterval = config.getValue("searchInterval");
		if (searchInterval != null) {
			this.searchInterval = Integer.parseInt(searchInterval);
		} else {
			this.searchInterval = 1000;
		}

		String csvDownloadInterval = config.getValue("csvDownloadInterval");
		if (csvDownloadInterval != null) {
			this.csvDownloadInterval = Integer.parseInt(csvDownloadInterval);
		} else {
			this.csvDownloadInterval = 1000;
		}

		if (config.getValue("csvDownloadWithFooter") != null) {
			csvDownloadWithFooter = Boolean.valueOf(config.getValue("csvDownloadWithFooter"));
		} else {
			csvDownloadWithFooter = false;
		}
		csvDownloadFooter = config.getValue("csvDownloadFooter");
		if (csvDownloadFooter == null) {
			csvDownloadFooter = "";
		}

		if (config.getValue("showPulldownPleaseSelectLabel") != null) {
			showPulldownPleaseSelectLabel = Boolean.valueOf(config.getValue("showPulldownPleaseSelectLabel"));
		} else {
			showPulldownPleaseSelectLabel = true;
		}

		if (config.getValue("recycleBinMaxCount") != null) {
			recycleBinMaxCount = Integer.valueOf(config.getValue("recycleBinMaxCount"));
		} else {
			this.recycleBinMaxCount = 100;
		}

		String deleteAllCommandBatchSize = config.getValue("deleteAllCommandBatchSize");
		if (deleteAllCommandBatchSize != null){
			this.deleteAllCommandBatchSize = Integer.parseInt(deleteAllCommandBatchSize);
		} else {
			this.deleteAllCommandBatchSize = 100;
		}

		String bulkUpdateAllCommandBatchSize = config.getValue("bulkUpdateAllCommandBatchSize");
		if (bulkUpdateAllCommandBatchSize != null){
			this.bulkUpdateAllCommandBatchSize = Integer.parseInt(bulkUpdateAllCommandBatchSize);
		} else {
			this.bulkUpdateAllCommandBatchSize = 100;
		}

		if (config.getValue("shallowCopyLobData") != null) {
			this.shallowCopyLobData = Boolean.valueOf(config.getValue("shallowCopyLobData"));
		} else {
			this.shallowCopyLobData = false;
		}

		skins = (List<Skin>) config.getBeans("skins");
		themes = (List<Theme>) config.getBeans("themes");

		entityViewHelper = (EntityViewHelper) config.getBean("entityViewHelper");

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
					.filter(property -> supports.contains(property))
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
	 * プルダウンの「選択してください」を表示するかを取得します。
	 * @return プルダウンの「選択してください」を表示するか
	 */
	public boolean isShowPulldownPleaseSelectLabel() {
		return showPulldownPleaseSelectLabel;
	}

	public List<BinaryDownloadLoggingTargetProperty> getBinaryDownloadLoggingTargetProperty() {
		return binaryDownloadLoggingTargetProperty;
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

}
