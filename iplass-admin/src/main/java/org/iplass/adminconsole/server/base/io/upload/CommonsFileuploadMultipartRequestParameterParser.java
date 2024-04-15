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
package org.iplass.adminconsole.server.base.io.upload;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.fileupload2.core.DiskFileItemFactory;
import org.apache.commons.fileupload2.core.FileItem;
import org.apache.commons.fileupload2.core.FileItemFactory;
import org.apache.commons.fileupload2.core.FileUploadException;
import org.apache.commons.fileupload2.jakarta.servlet6.JakartaServletFileUpload;

import jakarta.servlet.http.HttpServletRequest;


/**
 * Comons Fileupload 用マルチパートリクエストパラメータ解析機能
 *
 * <p>
 * デフォルト設定として、
 * パラメータ解析機能：{@link org.apache.commons.fileupload.servlet.ServletFileUpload}、
 * FileItemFactory：{@link org.apache.commons.fileupload.disk.DiskFileItemFactory}
 * を利用する。
 * </p>
 *
 * <p>
 * 各インスタンスに対してパラメータ設定が必要であれば、適に設定する。
 * </p>
 *
 * @author SEKIGUCHI Naoya
 */
public class CommonsFileuploadMultipartRequestParameterParser implements MultipartRequestParameterParser {
	/** 最大サイズまで許容する */
	public static final long MAXIMUM = -1;

	/** 最大リクエストサイズデフォルト値 */
	private static final long SIZE_MAX_DEFAULT = 2_097_152L;
	/** 最大ファイルサイズデフォルト値 */
	private static final long FILE_SIZE_MAX_DEFAULT = SIZE_MAX_DEFAULT;
	/** 最大パラメータ数デフォルト値 */
	private static final long FILE_COUNT_MAX = 10_000L;

	/** 最大リクエストサイズ */
	private long sizeMax = SIZE_MAX_DEFAULT;
	/** 最大ファイルサイズ */
	private long fileSizeMax = FILE_SIZE_MAX_DEFAULT;
	/** 最大パラメータ数 */
	private long fileCountMax = FILE_COUNT_MAX;

	/** ファイルサイズ閾値 */
	private int sizeThreshold = DiskFileItemFactory.DEFAULT_THRESHOLD;
	/** 一時ファイルディレクトリ */
	private File temporaryDirectory = null;
	/** デフォルト文字コード */
	private String defaultCharset = null;

	/**
	 * 最大リクエストサイズを設定する
	 *
	 * <p>{@value #MAXIMUM}を設定すると最大サイズまで許容します</p>
	 *
	 * @param sizeMax 最大リクエストサイズ
	 */
	public void setSizeMax(long sizeMax) {
		this.sizeMax = sizeMax;
	}

	/**
	 * 最大ファイルサイズを設定する
	 *
	 * <p>{@value #MAXIMUM}を設定すると最大サイズまで許容します</p>
	 *
	 * @param fileSizeMax 最大ファイルサイズ
	 */
	public void setFileSizeMax(long fileSizeMax) {
		this.fileSizeMax = fileSizeMax;
	}

	/**
	 * 最大パラメータ数を設定する
	 *
	 * <p>{@value #MAXIMUM}を設定すると最大サイズまで許容します</p>
	 *
	 * @param fileCountMax 最大パラメータ数
	 */
	public void setFileCountMax(long fileCountMax) {
		this.fileCountMax = fileCountMax;
	}

	/**
	 * ファイルサイズ閾値を設定する
	 *
	 * <p>
	 * 本パラメータに設定した閾値でメモリ管理かファイル管理かを判定する。
	 * <ul>
	 * <li>閾値以下：パラメータ値はメモリ管理</li>
	 * <li>閾値よりも大きい：パラメータ値はファイル管理</li>
	 * </ul>
	 * </p>
	 *
	 * @param sizeThreshold ファイルサイズ閾値
	 */
	public void setSizeThreshold(int sizeThreshold) {
		this.sizeThreshold = sizeThreshold;
	}

	/**
	 * 一時ディレクトリを設定する
	 *
	 * <p>
	 * ファイルサイズ閾値を超えたパラメータ情報を、ディスク上で管理する為の一時ディレクトリ。
	 * </p>
	 *
	 * @param temporaryDirectory 一時ディレクトリ
	 */
	public void setTemporaryDirectory(File temporaryDirectory) {
		this.temporaryDirectory = temporaryDirectory;
	}

	/**
	 * デフォルト文字コードを設定する
	 * @param defaultCharset デフォルト文字コード
	 */
	public void setDefaultCharset(Charset defaultCharset) {
		this.defaultCharset = defaultCharset.name();
	}

	@Override
	public List<MultipartRequestParameter> parse(HttpServletRequest req) {
		try {
			// NOTE ServletFileUpload はリクエスト単位で作成する
			// cf. https://commons.apache.org/proper/commons-fileupload/using
			JakartaServletFileUpload<?, ?> servletFileUpload = getServletFileUpload(getFileItemFactory());
			@SuppressWarnings({ "rawtypes", "unchecked" })
			List<FileItem<?>> fileItemList = (List) servletFileUpload.parseRequest(req);
			return convert(fileItemList);

		} catch (FileUploadException e) {
			throw new MultipartRequestParameterParseRuntimeException(e);
		}
	}

	/**
	 * ServletFileUpload インスタンスを取得する
	 * @param fileItemFactory FileItemFactoryインスタンス
	 * @return ServletFileUpload インスタンス
	 */
	protected JakartaServletFileUpload<?, ?> getServletFileUpload(FileItemFactory<?> fileItemFactory) {
		JakartaServletFileUpload<?, ?> servletFileUpload = new JakartaServletFileUpload<>(fileItemFactory);
		servletFileUpload.setSizeMax(sizeMax);
		servletFileUpload.setFileSizeMax(fileSizeMax);
		servletFileUpload.setFileCountMax(fileCountMax);
		return servletFileUpload;
	}

	/**
	 * FileItemFactory インスタンスを取得する
	 * @return FileItemFactory インスタンス
	 */
	protected FileItemFactory<?> getFileItemFactory() {
		// FIXME 一時的な対応
		DiskFileItemFactory.Builder builder = DiskFileItemFactory.builder();
		builder.setBufferSize(sizeThreshold);
		if (null != temporaryDirectory) {
			builder.setPath(temporaryDirectory.toPath());
		}
		if (null != defaultCharset) {
			builder.setCharset(defaultCharset);
		}
		return builder.get();
	}

	/**
	 * MultipartRequestParameter リストに変換する
	 * @param fileItemList CommonsFileupload FileItem リスト
	 * @return MultipartRequestParameter リスト
	 */
	protected List<MultipartRequestParameter> convert(List<FileItem<?>> fileItemList) {
		return fileItemList.stream().map(i -> new CommonsFileuploadMultipartRequestParameter(i)).collect(Collectors.toCollection(ArrayList::new));
	}

	// FIXME 構成が変わっているので対応方法を検討する必要がある
	//	/**
	//	 * <p>FileItem生成用クラス</p>
	//	 *
	//	 * <p>
	//	 * 複数の同一パラメータの場合に、パラメータ名末尾にパラメータ番号を付与する。
	//	 * </p>
	//	 *
	//	 * <p>
	//	 * 機能再作成前に実装していた FileItemFactory を移植
	//	 * </p>
	//	 */
	//	public static class AdminFileItemFactory extends DiskFileItemFactory {
	//		public static final String MULTI_SUFFIX = "[]";
	//		private HashMap<String, Integer> map = new HashMap<String, Integer>();
	//
	//		@Override
	//		public FileItem createItem(String fieldName, String contentType, boolean isFormField, String fileName) {
	//			Integer cont = map.get(fieldName) != null ? (map.get(fieldName) + 1) : 0;
	//			map.put(fieldName, cont);
	//
	//			// 複数の場合のみ、後ろにCountを付加
	//			if (cont > 0 || fieldName.contains(MULTI_SUFFIX)) {
	//				fieldName = fieldName.replace(MULTI_SUFFIX, "") + "-" + cont;
	//			}
	//
	//			return super.createItem(fieldName, contentType, isFormField, fileName);
	//		}
	//	}

}
