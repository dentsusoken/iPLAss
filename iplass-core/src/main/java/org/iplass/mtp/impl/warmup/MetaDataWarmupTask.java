/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.warmup;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * メタデータウォームアップタスク
 * <p>
 * テナント単位に事前にメタデータを読み込みキャッシュしておくタスクです。
 * 設定されたメタデータパスに応じてメタデータを読み込みます。
 * </p>
 *
 * <h3>メタデータパス設定ルール</h3>
 * <ul>
 * <li>メタデータの最上位パスは指定必須。（例：エンティティであれば <code>/entity/</code>）</li>
 * <li>前方一致検索する場合は、末尾にアスタリスク（&#42;）を設定する。（例： <code>/entity/mtp/&#42;</code>）</li>
 * <li>完全一致検索する場合は、完全なメタデータパスを設定する。（例： <code>/entity/mtp/auth/User</code>）</li>
 * </ul>
 *
 * @author SEKIGUCHI Naoya
 */
public class MetaDataWarmupTask implements WarmupTask {
	/** メタデータルートパス指定ありパターン */
	private static final Pattern METADATA_ROOT_PATH_PATTERN = Pattern.compile("^\\/[a-zA-Z0-9]+\\/.*");

	/** ロガー */
	private Logger logger = LoggerFactory.getLogger(MetaDataWarmupTask.class);

	// setter によって設定される。init で処理される。
	/** メタデータパスリスト */
	private List<String> metadataPathList;

	// init で初期化される。warmup で利用される。
	/** 前方一致用 メタデータパスリスト */
	private List<String> metadataPathPrefixList = new ArrayList<>();
	/** 完全一致用 メタデータパスリスト */
	private List<String> metadataPathExactMatchList = new ArrayList<>();

	@Override
	public void init() {
		if (null == metadataPathList || metadataPathList.isEmpty()) {
			logger.warn("metadataPathList is empty.");
			return;
		}

		for (String metadataPath : metadataPathList) {
			if (!checkPath(metadataPath)) {
				logger.warn("Incorrect metadata path \"{}\". Please check the service-config setting.", metadataPath);
				continue;
			}

			if (metadataPath.endsWith("*")) {
				metadataPathPrefixList.add(metadataPath.substring(0, metadataPath.length() - 1));
			} else {
				metadataPathExactMatchList.add(metadataPath);
			}
		}
	}

	@Override
	public void warmup(WarmupContext context) {
		MetaDataContext metadataContext = MetaDataContext.getContext();

		for (String metadataPathPrefix : metadataPathPrefixList) {
			loadMetadataPrefix(metadataContext, metadataPathPrefix);
		}

		loadMetadataList(metadataContext, metadataPathExactMatchList);
	}

	/**
	 * メタデータパスリストを取得します。
	 * @return メタデータパスリスト
	 */
	public List<String> getMetadataPathList() {
		return metadataPathList;
	}

	/**
	 * メタデータパスリストを設定します。
	 * @param metadataPathList メタデータパスリスト
	 */
	public void setMetadataPathList(List<String> metadataPathList) {
		this.metadataPathList = metadataPathList;
	}

	/**
	 * 設定されたメタデータパスをチェックする
	 *
	 * @param metadataPath メタデータパス
	 * @return 正しいメタデータパスの場合 true が返却される
	 */
	private boolean checkPath(String metadataPath) {
		if (StringUtil.isBlank(metadataPath)) {
			// null もしくは空文字
			return false;
		}

		// メタデータルートパスが指定されているか確認
		Matcher matcher = METADATA_ROOT_PATH_PATTERN.matcher(metadataPath);
		return matcher.matches();
	}

	/**
	 * 前方一致するメタデータを読み込む
	 * @param context メタデータコンテキスト
	 * @param metadataPathPrefix メタデータパス
	 */
	private void loadMetadataPrefix(MetaDataContext context, String metadataPathPrefix) {
		List<String> searchMetadataPathList = context.pathList(metadataPathPrefix);
		if (null == searchMetadataPathList || searchMetadataPathList.isEmpty()) {
			logger.warn("No metadata matching \"{}\" exists.", metadataPathPrefix);
			return;
		}

		loadMetadataList(context, searchMetadataPathList);
	}

	/**
	 * メタデータを読み込む
	 * @param context メタデータコンテキスト
	 * @param metadataPathList メタデータパスリスト
	 */
	private void loadMetadataList(MetaDataContext context, List<String> metadataPathList) {
		for (String metadataPath : metadataPathList) {
			logger.debug("Load metadata: {}", metadataPath);
			context.getMetaDataEntry(metadataPath);
		}
	}
}
