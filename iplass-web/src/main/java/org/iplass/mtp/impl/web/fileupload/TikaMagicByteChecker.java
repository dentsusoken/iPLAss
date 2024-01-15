/*
 * Copyright (C) 2024 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.web.fileupload;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.iplass.mtp.impl.util.CoreResourceBundleUtil;
import org.iplass.mtp.impl.web.fileupload.FileUploadTikaAdapter.TikaMimeType;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Apache Tika を利用したマジックバイトチェック処理
 *
 * <p>
 * 引数で取得した contentType (MimeType) を利用して、ファイルのマジックバイトをチェックする。
 * オプションでファイル拡張子もチェックする。
 * </p>
 *
 * <p>
 * 本チェック機能を利用する場合は、MimeType検出とMagicByteチェック処理を Tika を利用することを推奨する。
 * MimeType検出機能で tika を利用する為には WebFrontendService の uploadFileTypeDetector を TikaFileTypeDetector に変更する。
 * </p>
 *
 * @see {@link org.iplass.mtp.impl.web.WebFrontendService}
 * @see {@link org.iplass.mtp.impl.web.fileupload.TikaFileTypeDetector}
 * @author SEKIGUCHI Naoya
 */
public class TikaMagicByteChecker implements MagicByteChecker {
	/** ロガー */
	private static Logger LOG = LoggerFactory.getLogger(TikaMagicByteChecker.class);

	/** FileUploadTikaAdapter */
	private FileUploadTikaAdapter tikaAdapter;
	/** 拡張子チェックの実施設定（true の場合チェックを実施する） */
	private boolean isCheckExtension = true;
	/** マジックバイトの読み込みサイズ */
	private int readMagicLength = 64 * 1024;

	/** MimeTypeが取得できない場合に例外をスローするか */
	private boolean isThrowExceptionIfMimeTypeIsNull = false;
	/** チェック対象ファイルを読み取ることができない場合に例外をスローするか */
	private boolean isThrowExceptionIfFileCannotRead = false;

	@Override
	public void checkMagicByte(File tempFile, String contentType, String fileName) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Check magic bytes. Args is tempFile = {}, contentType = {}, fileName = {}.", tempFile.getAbsolutePath(), contentType, fileName);
		}

		// MimeType に対するファイルルール定義を取得
		TikaMimeType mimeType = tikaAdapter.getMimeType(contentType);

		if (mimeType == null) {
			// mimeType が存在しない
			LOG.warn("Undefined MimeType. contentType = {}, filename = {}.", contentType, fileName);

			if (isThrowExceptionIfMimeTypeIsNull) {
				throw new MagicByteCheckApplicationException(getCheckExceptionMessage());
			}

			return;
		}

		// 拡張子チェック
		if (isCheckExtension) {
			checkExtension(mimeType, fileName);
		}

		// マジックバイトチェック
		checkMagic(mimeType, fileName, tempFile);
	}

	/**
	 * FileUploadTikaAdapter を設定する
	 *
	 * <p>
	 * 設定必須のプロパティ。
	 * </p>
	 *
	 * @param tikaAdapter FileUploadTikaAdapter
	 */
	public void setFileUploadTikaAdapter(FileUploadTikaAdapter tikaAdapter) {
		this.tikaAdapter = tikaAdapter;
	}

	/**
	 * 拡張子チェックの実施設定を設定する
	 *
	 * <p>
	 * true の場合、拡張子チェックを実施する。
	 * </p>
	 *
	 * @param isCheckExtension
	 */
	public void setCheckExtension(boolean isCheckExtension) {
		this.isCheckExtension = isCheckExtension;
	}

	/**
	 * マジックバイトの読み込みサイズを設定する
	 *
	 * <p>
	 * MimeType 定義の全体を通した最小のマジックバイトサイズを設定する。チェックの為にはある程度大きなサイズが必要となる。
	 * </p>
	 *
	 * <p>
	 * デフォルト値は 65536 ( = 64 * 1024 ) 。
	 * Tika のマジックバイト最小値を利用。( {@link org.apache.tika.mime.MimeTypes#getMinLength()} )
	 * </p>
	 *
	 * @param readMagicLength マジックバイトの読み込みサイズ
	 */
	public void setReadMagicLength(int readMagicLength) {
		this.readMagicLength = readMagicLength;
	}

	/**
	 * MimeTypeが取得できない場合に例外をスローするか
	 *
	 * <p>
	 * 設定値が true の場合、MimeTypeが取得できない場合に例外をスローする。
	 * デフォルト値は false です。
	 * </p>
	 *
	 * @param isThrowExceptionIfMimeTypeIsNull MimeTypeが取得できない場合に例外をスローするか
	 */
	public void setThrowExceptionIfMimeTypeIsNull(boolean isThrowExceptionIfMimeTypeIsNull) {
		this.isThrowExceptionIfMimeTypeIsNull = isThrowExceptionIfMimeTypeIsNull;
	}

	/**
	 * チェック対象ファイルを読み取ることができない場合に例外をスローするか
	 *
	 * <p>
	 * 設定値が true の場合、チェック対象ファイルを読み取ることができない場合に例外をスローする。
	 * デフォルト値は false です。
	 * </p>
	 *
	 * @param isThrowExceptionIfFileCannotRead チェック対象ファイルを読み取ることができない場合に例外をスローするか
	 */
	public void setThrowExceptionIfFileCannotRead(boolean isThrowExceptionIfFileCannotRead) {
		this.isThrowExceptionIfFileCannotRead = isThrowExceptionIfFileCannotRead;
	}

	/**
	 * 拡張子チェックを実施する
	 *
	 * @param mimeType MimeType
	 * @param fileName ファイル名
	 */
	private void checkExtension(TikaMimeType mimeType, String fileName) {
		// ファイル名から拡張子を取得
		String extension = getExtension(fileName);
		// 拡張子チェック
		ExtensionCheckStrategy strategy = ExtensionCheckStrategy.getStrategy(mimeType, extension);
		LOG.debug("ExtensionCheckStrategy = {}.", strategy);
		strategy.performCheck(mimeType, extension, fileName, this);
	}

	/**
	 * マジックバイトをチェックする
	 * @param mimeType MimeType
	 * @param filename ファイル名
	 * @param file チェック対象ファイル
	 */
	private void checkMagic(TikaMimeType mimeType, String filename, File file) {
		if (!mimeType.hasMagic()) {
			LOG.debug("No magic byte is defined for the MimeType. ( MimeType = {} )", mimeType.getName());
			return;
		}

		try {
			byte[] magic = readMagic(file);
			if (!mimeType.matchesMagic(magic)) {
				// magic byte が一致しない
				LOG.error("Magic bytes did not match. ( filename = {}, MimeType = {} )", filename, mimeType.getName());
				// ファイル名の拡張子が存在しなければ、チェックエラー
				throw new MagicByteCheckApplicationException(getCheckExceptionMessage());

			}
		} catch (IOException e) {
			// DefaultMagicByteChecker では、正常終了している。。。
			// ファイル入出力例外。ファイルが存在しない場合
			LOG.warn("Cannot read file. ( error message = {}, filename = {}, File = {} )", e.getMessage(), filename, file.getAbsolutePath());

			if (isThrowExceptionIfFileCannotRead) {
				throw new MagicByteCheckApplicationException(getCheckExceptionMessage(), e);
			}
		}
	}

	/**
	 * ファイルからマジックバイトを読み取る
	 * @param file 読み取り対象ファイル
	 * @return マジックバイト
	 * @throws FileNotFoundException ファイルが存在しない場合の例外
	 * @throws IOException 入出力例外
	 */
	private byte[] readMagic(File file) throws FileNotFoundException, IOException {
		try (InputStream input = new BufferedInputStream(new FileInputStream(file))) {
			byte[] magic = new byte[readMagicLength];
			int read = input.read(magic);

			if (-1 == read) {
				return new byte[0];
			}

			if (read != magic.length) {
				byte[] readMagic = new byte[read];
				System.arraycopy(magic, 0, readMagic, 0, read);
				magic = readMagic;
			}

			return magic;
		}
	}

	/**
	 * ファイルから拡張子を取得する
	 * @param filename ファイル名
	 * @return ファイルに設定されている拡張子
	 */
	private String getExtension(String filename) {
		int extPos = filename.lastIndexOf('.');
		if (0 > extPos) {
			return null;
		}

		return filename.substring(extPos);
	}

	/**
	 * チェック例外メッセージを取得する
	 * @return チェック例外メッセージ
	 */
	private String getCheckExceptionMessage() {
		return CoreResourceBundleUtil.resourceString("impl.web.fileupload.UploadFileHandleImpl.invalidFileMsg");
	}

	/**
	 * ファイル拡張子チェック処理
	 *
	 * <p>
	 * チェックパターンは以下の４種類。MimeType は必ず設定されていることを前提とする。
	 *
	 * <ul>
	 * <li>MimeType拡張子定義 = 空、 ファイル拡張子 = 無し</li>
	 * <li>MimeType拡張子定義 = 空、 ファイル拡張子 = 有り</li>
	 * <li>MimeType拡張子定義 = 有り、 ファイル拡張子 = 無し</li>
	 * <li>MimeType拡張子定義 = 有り、 ファイル拡張子 = 有り</li>
	 * </ul>
	 * </p>
	 */
	private static enum ExtensionCheckStrategy {
		/** MimeType拡張子定義 = 空、 ファイル拡張子 = 無し */
		EMPTY_MIMETYPE_EXTENSION_AND_NO_FILE_EXTENSION {
			@Override
			public void performCheck(TikaMimeType mimeType, String extension, String fileName, TikaMagicByteChecker checker) {
				// チェック無し
			}
		},
		/** MimeType拡張子定義 = 空、 ファイル拡張子 = 有り */
		EMPTY_MIMETYPE_EXTENSION_AND_FILE_EXTENSION_EXIST {
			@Override
			public void performCheck(TikaMimeType mimeType, String extension, String fileName, TikaMagicByteChecker checker) {
				// ログ出力のみ
				LOG.info(
						"The check was skipped because the extension to be checked does not exist in the MimeType. filename = {}, MimeType = {}.",
						fileName, mimeType.getName());
			}
		},
		/** MimeType拡張子定義 = 有り、 ファイル拡張子 = 無し */
		MIMETYPE_EXTENSION_EXIST_AND_NO_FILE_EXTENSION {
			@Override
			public void performCheck(TikaMimeType mimeType, String extension, String fileName, TikaMagicByteChecker checker) {
				// 警告ログ出力のみ
				LOG.warn(
						"File inspection cannot be performed because the file name does not have an extension. filename = {}, MimeType = {}.",
						fileName, mimeType.getName());

			}
		},
		/** MimeType拡張子定義 = 有り、 ファイル拡張子 = 有り */
		MIMETYPE_EXTENSION_EXIST_AND_FILE_EXTENSION_EXIST {
			@Override
			public void performCheck(TikaMimeType mimeType, String extension, String fileName, TikaMagicByteChecker checker) {
				// mimeType で管理している拡張子に、ファイル名の拡張子が存在するか確認
				if (!mimeType.getExtensions().contains(extension)) {
					LOG.error("Does not match the extension defined for the MimeType. filename = {}, extension = {}, MimeType = {}, defined extension settings = {}",
							fileName, extension, mimeType.getName(), mimeType.getExtensions());
					// ファイル名の拡張子が存在しなければ、チェックエラー
					throw new MagicByteCheckApplicationException(checker.getCheckExceptionMessage());

				}
			}
		};

		/**
		 * チェックを実施する
		 * @param mimeType MimeType
		 * @param extension ファイル拡張子
		 * @param fileName ファイル名
		 * @param checker TikaMagicByteChecker インスタンス
		 */
		abstract void performCheck(TikaMimeType mimeType, String extension, String fileName, TikaMagicByteChecker checker);

		/**
		 * チェックパターンに適合した拡張子チェック処理を取得する
		 * @param mimeType MimeType
		 * @param extension ファイル拡張子
		 * @return 拡張子チェック処理
		 */
		public static ExtensionCheckStrategy getStrategy(TikaMimeType mimeType, String extension) {
			return mimeType.getExtensions().isEmpty()
					// MimeType拡張子定義 = 空
					? StringUtil.isEmpty(extension)
							// ファイル拡張子 = 無し
							? EMPTY_MIMETYPE_EXTENSION_AND_NO_FILE_EXTENSION
							// ファイル拡張子 = 有り
							: EMPTY_MIMETYPE_EXTENSION_AND_FILE_EXTENSION_EXIST
					// MimeType拡張子定義 = 有り
					: StringUtil.isEmpty(extension)
							// ファイル拡張子 = 無し
							? MIMETYPE_EXTENSION_EXIST_AND_NO_FILE_EXTENSION
							// ファイル拡張子 = 有り
							: MIMETYPE_EXTENSION_EXIST_AND_FILE_EXTENSION_EXIST;
		}
	}

}
