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
 * 検索結果一覧(jqGrid)のカラム固定
 *
 * - SearchResultSection の「固定指定を許可」が ON の画面でのみ JSP から初期化される(OFF の場合はピンも固定も動作しない)。
 * - システム列(選択欄・詳細リンク等)を除くすべてのユーザー列にピンを表示する。
 * - 固定が有効になるのは 1 列(基準列)のみで、基準列とその左側の列をすべて固定する。
 *   初期表示の基準列は「列を固定」指定された列(colModel に frozen:true で出力された列)のうち最も右側の列とする。
 *   指定された列がない場合は固定なしで表示する。
 * - ピン操作: 基準列以外のピンを押すとその列が基準列になる(他のピンは OFF)。基準列のピンを押すと固定を全解除する。
 * - ピンの支援技術への通知: 操作ラベル(freezeLabel/unfreezeLabel。{0}=表示列名)と aria-pressed(基準列の ON/OFF)を
 *   基準列の切替(ピン押下・初期化・SessionStorage 復元のすべて)に同期する。属性は固定表頭の clone 側にも複製される。
 * - 全列が表示領域に収まる場合はピンを無効化する(aria-disabled + tabindex=-1 で Tab 到達・キー操作を不可能にし、
 *   無効化された瞬間にフォーカス中のピンからはフォーカスを外す。見た目は CSS クラスで制御)。
 * - 基準列(解除状態を含む)は SessionStorage に保存し、同一キーの画面を再表示した際に復元する(開発者の初期固定指定より優先)。
 *   保存された列が定義から消えた場合は固定なしとする。
 *
 */
(function($) {

	// 固定範囲の算出対象外とするシステム列(選択欄・詳細リンク等)。colModel で frozen:true が指定され常に固定される
	// cb は multiselect(true) 時に jqGrid が自動追加するチェックボックス列
	const SYSTEM_COLUMNS = new Set(["cb", "orgOid", "orgVersion", "orgTimestamp", "selOid", "_mtpDetailLink"]);

	// window resize 連続発火時の過剰実行を抑止するための待機時間(ms)
	const RESIZE_DEBOUNCE_MILLIS = 200;

	const DATA_KEY = "mtpFrozenColumns";

	// 固定解除状態を表す SessionStorage の保存値(プロパティ名として使用できない文字列)
	const UNFROZEN_VALUE = ":none";

	class FrozenColumns {

		/**
		 * @param $grid jqGrid 生成済みの table 要素
		 * @param options
		 *   storageKey: 固定状態を SessionStorage に保存するキー
		 *   containerSelector: ピン操作可否の判定基準となるコンテナ(gbox の祖先要素)のセレクタ。未指定時は gbox の親要素
		 *   freezeLabel: 非基準列ピンの操作ラベル({0}=表示列名)。JSP 経由で mtp-gem-messages の文言が渡される
		 *   unfreezeLabel: 基準列ピンの操作ラベル({0}=表示列名)
		 */
		constructor($grid, options) {
			this.$grid = $grid;
			this.storageKey = options.storageKey;
			this.containerSelector = options.containerSelector;
			this.freezeLabel = options.freezeLabel || "Freeze {0}";
			this.unfreezeLabel = options.unfreezeLabel || "Unfreeze {0}";
			this.resizeTimerId = null;

			const colModel = $grid.jqGrid("getGridParam", "colModel");
			// ユーザー列(システム列以外)の列名を colModel の並び順で保持する
			this.userColumns = colModel
					.filter(col => !SYSTEM_COLUMNS.has(col.name))
					.map(col => col.name);
			// ピンの操作ラベルに埋め込む表示列名。colModel の label は HTML 断片のためテキストを抽出する(空の場合は列名で代用)
			this.displayNames = new Map(colModel.map(col => [col.name, this.toPlainText(col.label) || col.name]));
			// 「列を固定」指定列(frozen:true)の列名。colModel の frozen は固定適用時に書き換わるため初期化時に確定する
			this.initiallyFrozenColumns = colModel
					.filter(col => col.frozen === true && !SYSTEM_COLUMNS.has(col.name))
					.map(col => col.name);
			// 基準列(固定が有効な列)。解除中は null
			this.baseColumnName = this.loadBaseColumnName();

			$(window).on("resize", () => {
				if (this.resizeTimerId != null) {
					clearTimeout(this.resizeTimerId);
				}
				this.resizeTimerId = setTimeout(() => {
					this.resizeTimerId = null;
					this.updatePinDisabled();
				}, RESIZE_DEBOUNCE_MILLIS);
			});
		}

		/**
		 * 行データの描画後に固定を再適用する。
		 * addRowData は固定列の clone 再生成のトリガにならないため、行を再描画するたびに呼び出す。
		 * clone 生成時にイベントハンドラも複製されるため、行内リンク等のイベント設定後に呼び出すこと。
		 */
		refresh() {
			if (this.userColumns.length === 0) return;
			this.apply();
			this.updatePinDisabled();
		}

		$gbox() {
			return this.$grid.closest(".ui-jqgrid");
		}

		// 固定する列数(ユーザー列の先頭から基準列まで)。解除中は 0
		frozenColumnCount() {
			return this.baseColumnName != null ? this.userColumns.indexOf(this.baseColumnName) + 1 : 0;
		}

		apply() {
			const $gbox = this.$gbox();
			const frozenColumnCount = this.frozenColumnCount();
			const colModel = this.$grid.jqGrid("getGridParam", "colModel");
			for (const col of colModel) {
				const pos = this.userColumns.indexOf(col.name) + 1;
				if (pos === 0) continue;
				// 基準列より左の列は「列を固定」指定されていなくても固定し、固定領域を連続させる
				const shouldFreeze = pos <= frozenColumnCount;
				if (col.frozen !== shouldFreeze) {
					this.$grid.jqGrid("setColProp", col.name, { frozen: shouldFreeze });
				}
			}
			// 固定済みの場合は破棄してから再適用する(適用済みの setFrozenColumns は no-op で clone が再構築されないため)
			if (this.$grid.jqGrid("getGridParam", "frozenColumns") === true) {
				this.$grid.jqGrid("destroyFrozenColumns");
				// 適用中マーカー除去(frozen-ever は行高恒定マーカーのため解除後も残す)
				$gbox.removeClass("frozen-columns");
			}
			// ピンは setFrozenColumns で clone 側にも複製されるため、適用前に主表頭へ反映する
			this.refreshPins();
			if (frozenColumnCount === 0) return;

			this.$grid.jqGrid("setFrozenColumns");
			// frozen-columns=適用中マーカー。frozen-ever=行高恒定マーカー(一度適用した grid は解除後も同一行高)
			$gbox.addClass("frozen-columns").addClass("frozen-ever");
			// fhDiv(固定表頭)の寸法同期: 列幅合計を明示する。
			// flat skin の module.css(`width:auto !important`)が inline width を打ち負かすため
			// min-width で指定し、幅は hidden 列を除いた実効幅として内部 table の実測幅を採用する
			const $frozenHeader = $gbox.find(".frozen-div");
			if ($frozenHeader.length > 0) {
				const $frozenHeaderTable = $frozenHeader.children("table.ui-jqgrid-htable");
				const frozenHeaderWidth = $frozenHeaderTable.length > 0 ? $frozenHeaderTable[0].offsetWidth : 0;
				if (frozenHeaderWidth > 0) $frozenHeader.css("min-width", frozenHeaderWidth + "px");
				// 主表頭は fhDiv も .ui-jqgrid-hdiv を持つため :not(.frozen-div) で除外して取得
				const mainHeaderHeight = $gbox.find(".ui-jqgrid-hdiv:not(.frozen-div)").height();
				if ($frozenHeader.height() !== mainHeaderHeight) {
					$frozenHeader.height(mainHeaderHeight);
				}
			}
			// 両表の行高を明示同期する(固定側/主表側の描画差による累積ずれ対策)。
			// 主表側セレクタは .frozen-bdiv が ui-jqgrid-bdiv を兼任するため :not で固定側を除外
			const $frozenRows = $gbox.find(".frozen-bdiv tr.jqgrow");
			$gbox.find(".ui-jqgrid-bdiv:not(.frozen-bdiv) tr.jqgrow").each(function(i) {
				if ($frozenRows.eq(i).length > 0) $frozenRows.eq(i).height($(this).height());
			});
		}

		// ピン操作: 基準列のピンなら固定を全解除し、それ以外なら押した列を基準列に切り替える
		select(colName) {
			this.baseColumnName = this.baseColumnName === colName ? null : colName;
			this.apply();
			this.updatePinDisabled();
			this.saveBaseColumnName();
		}

		// 全ユーザー列の主表頭にピンを設置し、基準列のピンのみ ON にする
		refreshPins() {
			const colModel = this.$grid.jqGrid("getGridParam", "colModel");
			// 主表頭のみ対象: fhDiv も .ui-jqgrid-hdiv class を持つため .frozen-div を除外
			const $headers = this.$gbox().find(".ui-jqgrid-hdiv tr.ui-jqgrid-labels th").filter(function() {
				return $(this).closest(".frozen-div").length === 0;
			});
			for (const colName of this.userColumns) {
				const $th = $headers.eq(colModel.findIndex(col => col.name === colName));
				if ($th.length === 0) continue;

				let $pin = $th.find(".mtp-col-pin");
				if ($pin.length === 0) {
					$pin = this.createPin(colName);
					// title(p.title) と同一行・sort ボタン(s-ico)の前に挿入
					const $headerContent = $th.children("div").first();
					const $sortIcon = $headerContent.children("span.s-ico");
					if ($sortIcon.length > 0) {
						$pin.insertBefore($sortIcon);
					} else {
						$headerContent.append($pin);
					}
				}
				// 基準列はON(常時表示)、それ以外はOFF(hover 時のみ表示)
				$pin.toggleClass("mtp-pin-on", colName === this.baseColumnName);
				// 操作ラベル(固定/解除)と aria-pressed を基準列の ON/OFF に同期する(生成直後・切替直後の両方で通る)
				this.updatePinA11y($pin, colName);
			}
		}

		createPin(colName) {
			const $pin = $("<a/>").attr({
				href: "javascript:void(0)",
				"class": "mtp-col-pin"
			}).append($("<i/>").addClass("fas fa-thumbtack"));
			// th の jqGrid クリック処理が伝播を断つため document 委譲でなく直接結合する(clone 側へも handler が複製される)
			$pin.on("click", e => {
				e.preventDefault();
				e.stopPropagation();
				// 無効化(全列が表示領域に収まる)状態のピンは操作しない。currentTarget で本体/clone の実際に押された側を判定する
				if ($(e.currentTarget).hasClass("mtp-pin-disabled")) return;
				this.select(colName);
			});
			return $pin;
		}

		// ピンの支援技術向け属性を更新する。ラベルは基準列かどうかで「固定する/固定を解除する」に切り替わる
		updatePinA11y($pin, colName) {
			const on = colName === this.baseColumnName;
			const label = (on ? this.unfreezeLabel : this.freezeLabel)
					.replace("{0}", this.displayNames.get(colName) || colName);
			$pin.attr({
				"aria-label": label,
				"aria-pressed": on ? "true" : "false"
			});
		}

		// colModel の label(HTML 断片)から表示列名としてのテキストを抽出する
		toPlainText(html) {
			return html ? $("<div/>").html(html).text().trim() : "";
		}

		// 全列が表示領域に収まる(横スクロール無し)場合はピンを操作不可とする
		updatePinDisabled() {
			if (this.userColumns.length === 0) return;
			const $gbox = this.$gbox();
			if ($gbox.length === 0) return;
			let totalColumnWidth = 0;
			for (const col of this.$grid.jqGrid("getGridParam", "colModel")) {
				if (col.hidden !== true) {
					totalColumnWidth += (col.width ? +col.width : 0);
				}
			}
			// 判定基準はコンテナ幅: 固定なしの場合 grid は列幅合計まで自動拡張し
			// bdiv 自体が広がるため、bdiv 幅では横スクロール有無を判定できない
			const $container = this.containerSelector ? $gbox.closest(this.containerSelector) : $gbox.parent();
			const availableWidth = $container.width() || $gbox.parent().width() || 0;
			// +1 は幅の小数丸めによる 1px 未満の誤差を許容するためのマージン
			const disabled = totalColumnWidth <= availableWidth + 1;
			// 主表頭と固定表頭(clone)の両方のピンに反映する
			const $pins = $gbox.find(".mtp-col-pin").toggleClass("mtp-pin-disabled", disabled);
			if (disabled) {
				// Tab 到達・キー操作を不可能にする(CSS の pointer-events はマウス操作のみで、フォーカス可否は tabindex で制御する)
				$pins.attr("aria-disabled", "true").attr("tabindex", "-1");
				// リサイズで無効化された瞬間にフォーカス中のピンがあればフォーカスを外す
				if ($pins.is(document.activeElement)) {
					document.activeElement.blur();
				}
			} else {
				$pins.removeAttr("aria-disabled").removeAttr("tabindex");
			}
		}

		// 基準列の初期値を決定する。
		// SessionStorage に保存された基準列(ユーザー操作)を優先し、保存されていない場合は「列を固定」指定列の最も右側の列とする。
		// 保存された列が現在のユーザー列に存在しない(定義から消えた)場合と指定列がない場合は固定なし(null)とする。
		loadBaseColumnName() {
			const stored = getSessionStorage(this.storageKey);
			if (stored === UNFROZEN_VALUE) return null;
			if (stored != null) return this.userColumns.includes(stored) ? stored : null;
			return this.initiallyFrozenColumns.length > 0
					? this.initiallyFrozenColumns[this.initiallyFrozenColumns.length - 1] : null;
		}

		saveBaseColumnName() {
			setSessionStorage(this.storageKey, this.baseColumnName != null ? this.baseColumnName : UNFROZEN_VALUE);
		}
	}

	/**
	 * カラム固定を初期化し、制御オブジェクトを返す(jQuery チェーンは継続しない)。
	 * 同一 grid に対して再度呼び出した場合は初期化済みの制御オブジェクトを返す。
	 */
	$.fn.frozenColumns = function(options) {
		const $grid = this.first();
		let frozenColumns = $grid.data(DATA_KEY);
		if (!frozenColumns) {
			frozenColumns = new FrozenColumns($grid, options || {});
			$grid.data(DATA_KEY, frozenColumns);
		}
		return frozenColumns;
	};

})(jQuery);
