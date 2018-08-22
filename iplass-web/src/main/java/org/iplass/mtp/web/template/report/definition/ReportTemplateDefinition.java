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

package org.iplass.mtp.web.template.report.definition;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.web.template.definition.TemplateDefinition;

/**
 * <p>帳票出力用テンプレートファイルのTemplate定義。</p>
 *
 * <h5>※注意</h5>
 * <p>
 * バイナリデータは、メモリ内にロード＆キャッシュされるため、
 * 大きなサイズのバイナリデータは当Templateとして利用しないこと。
 * </p>
 * <p>
 * また、ReportTemplateDefinitionは、layout機能の利用、別テンプレート内からのincludeはできない。
 * </p>
 *
 * @author lis71n
 *
 */

public class ReportTemplateDefinition extends TemplateDefinition {

	private static final long serialVersionUID = -7473797665141741546L;

	private ReportType reportType;

	private String fileName;

	private byte[] binary;

	/** 多言語設定情報 */
	private List<LocalizedReportDefinition> localizedReportList;

	/**
	 * reportTypeを取得します。
	 * @return reportType
	 */
	public ReportType getReportType() {
	    return reportType;
	}

	/**
	 * reportTypeを設定します。
	 * @param reportType reportType
	 */
	public void setReportType(ReportType reportType) {
	    this.reportType = reportType;
	}

	public String getFileName() {
	    return fileName;
	}

	public void setFileName(String fileName) {
	    this.fileName = fileName;
	}

	public byte[] getBinary() {
		return binary;
	}

	public void setBinary(byte[] binary) {
		this.binary = binary;
	}

	/**
	 * 多言語設定情報を取得します。
	 * @return リスト
	 */
	public List<LocalizedReportDefinition> getLocalizedReportList() {
		return localizedReportList;
	}

	/**
	 * 多言語設定情報を設定します。
	 * @param リスト
	 */
	public void setLocalizedReportList(List<LocalizedReportDefinition> localizedReportList) {
		this.localizedReportList = localizedReportList;
	}

	/**
	 * 多言語設定情報を追加します。
	 * @param 多言語設定情報
	 */
	public void addLocalizedReport(LocalizedReportDefinition localizedReport) {
		if (localizedReportList == null) {
			localizedReportList = new ArrayList<LocalizedReportDefinition>();
		}

		localizedReportList.add(localizedReport);
	}

}
