/*
 * Copyright (C) 2026 DENTSU SOKEN INC. All Rights Reserved.
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

/*
 * 検索結果一覧(jqGrid)の「検索結果をまとめる」(セル結合)
 *
 * 一般検索画面(SearchResultSection)、トップ画面のパーツ(EntityListParts)、全文検索画面の
 * 各検索結果一覧で共通利用する。
 * 各 JSP の colModel の cellattr から SearchResultGrouping.cellAttr() を呼び出す。
 *
 * - 同一(oid, version)の行で、かつ判定対象の列の値が連続して同じ場合に、セルを結合(rowspan)する。
 * - ネストした参照プロパティの列(例: "mref2.mref3.name")は、親(祖先)の参照プロパティの列も判定対象に含める。
 *   (親が異なる行同士を、子の値が同じというだけで結合しないようにするため)
 */
// 再読み込みされても再宣言エラーにならないよう window のプロパティとして公開する
window.SearchResultGrouping = (function() {

	/**
	 * 列名から親プロパティのパスを取得する。
	 * 例:
	 *  "mref2.mref3.name" → "mref2.mref3"
	 *  "name" → ""
	 */
	const getParentPath = (name) => {
		const idx = name.lastIndexOf(".");
		return idx < 0 ? "" : name.substring(0, idx);
	};

	/**
	 * path が parentPath と同じ、またはその祖先にあたる親パスか判定する。
	 * 例:
	 *  parentPath = "mref2.mref3" の場合
	 *  "mref2.mref3" → true
	 *  "mref2" → true
	 *  "mref4" → false
	 */
	const isSameOrAncestorPath = (path, parentPath) =>
		path !== "" && (path === parentPath || parentPath.startsWith(path + "."));

	/**
	 * 結合判定の対象とする列名の一覧を取得する。
	 * ネストした参照プロパティの列は、親(祖先)の参照プロパティの列も判定対象に含める。
	 * 例(colNames = ["name", "mref2.name", "mref2.mref3.name", "mref4.name"]):
	 *  "name" → ["name"]
	 *  "mref2.mref3.name" → ["mref2.name", "mref2.mref3.name"]
	 */
	const getGroupingKeyCols = (colName, colNames) => {
		const parentPath = getParentPath(colName);
		return parentPath === ""
			? [colName]
			: colNames.filter((name) => isSameOrAncestorPath(getParentPath(name), parentPath));
	};

	/**
	 * jqGrid の cellattr 用の処理。セル結合の属性文字列を返す。
	 *
	 * @param grid jqGrid(_data に検索結果の行データを保持していること)
	 * @param rowId 行ID(jqGrid の cellattr の引数)
	 * @param colModel 列定義(jqGrid の cellattr の引数)
	 * @param rdata 行のフォーマット済みデータ(jqGrid の cellattr の引数)
	 * @returns cellattr の戻り値(属性文字列。属性を設定しない場合は null)
	 */
	const cellAttr = (grid, rowId, colModel, rdata) => {
		const rowIndex = parseInt(rowId) - 1;
		const data = grid.getGridParam("_data");
		const row = data[rowIndex];
		const colNames = grid.getGridParam("colModel").map((model) => model.name);
		const colName = colModel.name;
		const keyCols = getGroupingKeyCols(colName, colNames);

		// 2つの行が結合対象(同一)か判定する
		const isSame = (row1, row2) => row1.orgOid == row2.orgOid
			&& row1.orgVersion == row2.orgVersion
			&& keyCols.every((col) => row1[col] == row2[col]);

		// 前の行と値が同じ場合(ネストした参照プロパティは親の値も同じ場合)は非表示にする
		// タイトルを指定しないと(""含む)CELL値が設定されるので指定
		if (rowIndex > 0 && isSame(row, data[rowIndex - 1])) {
			return " title=\"&nbsp;\" style=\"display:none;\" ";
		}

		// この行から何行分rowspanを設定するか計算
		const rest = data.slice(rowIndex);
		const diffIndex = rest.findIndex((nextRow) => !isSame(row, nextRow));
		const count = diffIndex < 0 ? rest.length : diffIndex;

		if (count > 1) {
			// タイトルを指定しないと(""含む)CELL値が設定されるので、空の場合は &nbsp; を指定
			const label = $.jgrid.stripHtml(rdata[colName]).replace(/'/g, "&apos;") || "&nbsp;";
			return " title=\"" + label + "\" style=\"vertical-align: center !important;\" rowspan=\"" + count + "\"";
		}
		return null;
	};

	return { cellAttr };
})();
