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

////////////////////////////////////////////////////////
// モダンデザイン用のJavascript
////////////////////////////////////////////////////////

// デザイン固有のfunction、pluginの定義、puluginの実行等。

var $navSplit = $([]); // 他スキンのウィジェットエリア展開(split-block)未使用のため、jQueryオブジェクトを空生成だけしておく

/**
 * イベント実行
 */
$(function() {
	/** 全体 **/
	$("#content").addClass("fixHeight"); //高さ揃えのクラス追加
	$("#snav, #content-inner").addClass("fixHeightChild"); //高さ揃えのクラス追加

	$("#pagetop").pageTop(); //ページトップ
	$(".rollover").rollOverSet(); //ロールオーバー

	$(".modal-btn, .modal-lnk").modalWindow();

	//jQuery UI Dialogがドラッグでおかしな動きをするので、デフォルト値を変更
	if ($("#dialog_parent").length > 0) {
		$.ui.dialog.prototype.options.appendTo = "#dialog_parent";
	}

	/** ヘッダ **/
	$(".selectbox").jQselectable(); //ヘッダーセレクトプルダウン
	$("li#account-01").accountInfo(); //アカウント情報プルダウン
	$("li#setting").accountInfo(); //アカウント情報プルダウン(新規作成)

	/** メニュー **/
	$("#nav.sub-popup .subMenuRoot").subMenu(); //グローバルポップアップメニュー
	$("#snav").tabContent({
		clickFunc: function($menu) {
			//メニュー/ウィジェットの状態保持
			if ($menu.hasClass("menu-shortcut")) {
				setCookie("currentMenuTab", "menu-shortcut", 0);
			} else {
				setCookie("currentMenuTab", "menu-shortcut", -1);
			}
		}
	}); //サイドナビタブ切り替え

	//cookieから展開状態復元
	if (getCookie("currentMenuId")) {
		$("#" + es(getCookie("currentMenuId"))).parents(".menu-node:has(.nav-detail)").addClass("sub-open").children("ul").show();

		$("#" + es(getCookie("currentMenuId"))).addClass("selected");
		$("#" + es(getCookie("currentMenuId"))).parents(".root-menu-node").addClass("selected");
	}
	if (getCookie("currentMenuTab")) {
		$("." + getCookie("currentMenuTab")).trigger("click");
	}

	/** パーツ、ウィジェット **/
	//ツリービュー
	$(".treeViewList").treeViewList();
	$(".treeViewGrid").treeViewGrid();

	$("div.entity-list-widget").nameList();

	/** 汎用画面 **/
	//共通
	$(".tp").toolTip();//ツールチップ
	$(".tp02").toolTip({
		offleft: 75,
		range: 77
	});

	$(".commaField").commaField();
	datepicker(".datepicker");
	datetimepicker(".datetimepicker");
	timepicker(".timepicker");

	//バイナリのプレビュー表示
	if ($.fn.mediaelementplayer) {
		$("audio, video").mediaelementplayer({
			success: function(player, node) {
				player.addEventListener("loadeddata", function() {
					$(".fixHeight").fixHeight();
				});
			}
		});
	}

	//検索
	$(".tab-wrap").switchCondition({duration: 300});
	$("table.multi-col").multiColumnTable();
	$(".data-deep-search .add").on("click", function() { //検索項目追加
		addDetailCondition();
		$(".fixHeight").fixHeight();
	});
	$(".data-deep-search").on("click", ".delete", function() { //検索項目削除
		deleteDetailCondition(this);
		$(".fixHeight").fixHeight();
	});

	//詳細
	$(".sechead").sectoinToggle(); //セクション開閉
	$(".nav-section li").pageSecton(); //ページ内スクロール

	$(".massReference").massReferenceTable();
	$(".refCombo").refCombo();
	$(".refComboController").refComboController();
	$(".recursiveTreeTrigger").refRecursiveTree();
	$(".refLinkSelect").refLinkSelect();
	$(".refLinkRadio").refLinkRadio();

	if (typeof CKEDITOR !== "undefined") {
		CKEDITOR.on("instanceReady", function(ev) {
			$(".fixHeight").fixHeight();
		}); //CKEditor読み込み完了時に高さ調整
	}

	if ($.fn.fileupload) {
		$("input.upload-button").each(function() { //画像アップロード
			var token = $(this).attr("data-token");
			uploadFile(this, token);
		});
		$("a.binaryDelete").on("click", function() { //画像アップロード削除
			var fileId = $(this).attr("data-fileId");
			$(this).parent().remove();
			$("#" + es(fileId)).show();
			$(".fixHeight").fixHeight();
		});
	}

	//ブラウザリサイズ時の高さ調整
	$(window).on("resize", function() {
		$(".fixHeight").fixHeight();
	});

	//---------------------------------
	/* for Flat design */
	//---------------------------------
	if (!$("body").is("#login")) {
		// header
		var $headerContainer = $("#header-container"),
			$header = $headerContainer.find("h1");
		$("<div/>", {
			class: "cms-title"
		}).prependTo($headerContainer);
		$header.prepend("<i class='fas fa-bars'></i>");
		if ($header.is(":has(img)")) {
			var style = "<style>"
			var $tenantImage = $header.find(".tenant-image");
			if ($tenantImage.length > 0) {
				style += ".cms-title, body#verify2nd #header-container .cms-title {";
				style += "background-image: url(" + $tenantImage.attr("src") + ") !important;";
				style += "}";
			}
			var $tenantMiniImage = $header.find(".tenant-mini-image");
			if ($tenantMiniImage.length > 0) {
				style += ".nav-closed .cms-title {";
				style += "background-image: url(" + $tenantMiniImage.attr("src") + ") !important;";
				style += "}";
			}
			style += "</style>";
			$("head").append(style);
		}

		$("#account-01").appendTo("#user-nav");
		var $search = $("#search"),
			$searchPanel = $search.find(".fullsearch");
		$search.append("<i/>");
		$search.find(".search-text input[type=text]").attr("placeholder", "Search...");
		$search.find("i").on("click", function() {
			if ($searchPanel.is(".open")) {
				$searchPanel.removeClass("open");
			} else {
				$searchPanel.addClass("open");
			}
		}).on("mouseover", function() {
			$search.addClass("hovered");
		}).on("mouseout", function() {
			$search.removeClass("hovered");
		});
		$searchPanel.on("mouseover", function() {
			$search.addClass("hovered");
		}).on("mouseout", function() {
			$search.removeClass("hovered");
		});
		$("body, .select, .subMenuRoot, .menu-node, .hed-pull").on("click", function() {
			if ($searchPanel.is(".open") && !$search.is(".hovered")) {
				$searchPanel.removeClass("open");
			}
		});
		// footer
		$("#footer").appendTo("#content");

		// menu
		var navClosed = getCookie("nav-closed");
		if (navClosed && navClosed === "true") {
			// 初期状態
			$("html").addClass("nav-closed");
			$("#nav").find(".hover").removeClass("hover");
			if ($("#nav-menu .menu-shortcut").is(".current")) {
				$("#nav-menu .menu-list").trigger("click");
			}
		}
		$header.on("click", "svg", function() {
			if ($("html").is(".nav-closed")) {
				$("html").removeClass("nav-closed");
				$("#nav").find(".selected").addClass("hover");
				setCookie("nav-closed", null, 0);
			} else {
				$("html").addClass("nav-closed");
				$("#nav").find(".hover").removeClass("hover");
				if ($("#nav-menu .menu-shortcut").is(".current")) {
					$("#nav-menu .menu-list").trigger("click");
				}
				setCookie("nav-closed", "true", 0);
			}
		});
		$("#nav-menu").on("click", function() {
			if ($("html").is(".nav-closed")) {
				$("html").removeClass("nav-closed");
				setCookie("nav-closed", null, 0);
			}
		});
		$("#nav").find(".menu-node").on("click", function() {
			var $this = $(this),
				speed = 300;
			if (!$("html").is(".nav-closed")) {
				if (!$this.is(".sub-open")) {
					$this.addClass("sub-open").find(".nav-detail").stop().slideDown(speed);
					$this.siblings().removeClass("sub-open").find(".nav-detail").stop().slideUp(speed);
				} else {
					$this.removeClass("sub-open").find(".nav-detail").stop().slideUp(speed);
				}
			}
		}).each(function() {
			$(this).children("ul").attr("data-category", $(this).children("p").children("a").text());
		});

		// entityview
		// view/detail
		var $detailForm = $("#detailForm");
		$detailForm.find(".nav-section").after("<div class='formArchive'/>");
		var $formArchive = $(".formArchive");
		$detailForm.children("div:not(.lyt-edit-01,.lyt-edit-02)").each(function() {//$detailForm.children("div:not(.operation-bar)").each(function() {
			var $this = $(this);
//			if (!$this.attr("class") || $this.attr("class") === "") {
				$this.appendTo($formArchive);
//			}
		});

		// 検索画面
		$(".data-search").find("tr").last().find("th").remove().end().find("td").attr("colspan", $(".data-search").find("tr:first").children().length);
		$(".data-deep-search").find("tr").last().find("th").remove().end().find("td").attr("colspan", "5");

		// 検索実行時に条件を閉じる
		$(".tab-wrap").on("iplassAfterSetSearchTab", function() {
			if ($("[name='searchCond']").val() !== "" || $("[name='executeSearch']").val() === "t") {
				// 表示時に検索が走る場合は検索条件縮小、アニメーションは使わないので直接非表示に
				$(".tabList-search-01").addClass("contract");
				$(".tab-menu .current").addClass("contractMenu");
				$(".box-search-01").hide();
			}
		});
		$(".result-block").on("iplassAfterSearch", function(event, src) {
			if (src === "button") {
				$(".tab-menu .current").trigger("click");
			}
		});

		if ($("body.modal-body").length > 0 && $(".result-block + .btn").length > 0) {
			$(".result-block form").after($(".result-block + .btn"));
		}
	} else {
		//ログイン
		$("#header-container").find("#header").prependTo("#main");
		$(".login-logo").prependTo("#header");
		$("table").find("input").each(function() {
			var $this = $(this);
			if ($this.is("[type=checkbox]")) {
				$this.closest("tr").addClass("remember-row").find("th").remove();
			} else {
				$(this).attr("placeholder", $(this).closest("tr").find("th").text());
			}
		});
	}

	////////////////////////
	// アイコン画像をfont-awsomeに切り替え
	////////////////////////

	// メニューのアイコンをラベルの前に
	if ($(".navicon,.listicon").length > 0) {
		$(".navicon,.listicon").each(function() {
			var $this = this;
			var $parent = $(this).parent();
			$parent.before($this);
		});
	}

	// ダイアログのボタン
	if ($("#modal-dialog-root").length > 0) {
		var maximize = $("#modal-maximize-root");
		maximize.attr("title", maximize.text()).text("").append("<i class='far fa-window-maximize fa-2x'></i>");

		var restore = $("#modal-restore-root");
		restore.attr("title", restore.text()).text("").append("<i class='far fa-window-restore fa-2x'></i>");

		var close = $("#modal-close-root");
		close.attr("title", close.text()).text("").append("<i class='far fa-window-close fa-2x'></i>");
	}
	// function.jsのcreateModalFunctionを上書き
	scriptContext["createModalFunction"] = function(modal, name, callback) {
		var $dialogs = $(modal, document);
		$dialog = $("<div class='modal-dialog' />").attr({id: "modal-dialog-" + name}).appendTo($dialogs);
		$("<div class='modal-wrap' />").appendTo($dialog);
		var $under = $("<div class='modal-inner sub-modal-inner' />").appendTo($dialog);
		var $title = $("<h2 class='hgroup-01' />").appendTo($under);
		$("<span />").attr({id: "modal-title-" + name}).appendTo($title);
		$("<p class='modal-maximize sub-modal-maximize' />").attr("title", scriptContext.locale.maximizeLink).appendTo($under)
			.append("<i class='far fa-window-maximize fa-2x'></i>");
		$("<p class='modal-restore sub-modal-restore' />").attr("title", scriptContext.locale.restoreLink).appendTo($under)
			.append("<i class='far fa-window-restore fa-2x'></i>");
		$("<p class='modal-close sub-modal-close' />").attr("title", scriptContext.locale.closeLink).appendTo($under)
			.append("<i class='far fa-window-close fa-2x'></i>");
		var ifrm = "<iframe src=\"about:blank\" height=\"686\" width=\"100%\" frameborder=\"0\" name=\"" + name + "\"/>";
		var $frame = $(ifrm).appendTo($under);

		callback.call(this);
	};

	//Checkbox and Radio button
	$("input[type=radio],input[type=checkbox]").each(createPseudoObject);
	$("input[type=radio],input[type=checkbox]").parent("label").on("click", function(e) {
		var $pseudo = $(this).children(".pseudo-radio,.pseudo-checkbox");
		if ($pseudo.is(":visible")) {
			// ラベル部分クリックでイベントが2重発生しないよう抑制
			$pseudo.trigger("click");
			e.preventDefault();
		}
	});

	$(".pseudo-radio").on("click", onClickPseudoRadio);
	$(".pseudo-checkbox").on("click", onClickPseudoCheckbox);

	$("#content").show();
	$(".fixHeight").fixHeight(); //高さ揃え実行

	//ネストテーブルで行追加時にラジオのイベントが反映されないので行追加をオーバーライド
	if (typeof addNestRow !== "undefined") {
		var _addNestRow = addNestRow;

		addNestRow = function(rowId, countId, multiplicy, insertTop, rootDefName, viewName, orgPropName, callback) {
			var $row = _addNestRow(rowId, countId, multiplicy, insertTop, rootDefName, viewName, orgPropName, callback);

			$("input[type=radio],input[type=checkbox]", $row).on("click", function(e) {
				e.stopPropagation();
				$(this).prev().trigger("click");
			});
			$("input[type=radio],input[type=checkbox]", $row).parent("label").on("click", function(e) {
				var $pseudo = $(this).children(".pseudo-radio,.pseudo-checkbox");
				if ($pseudo.is(":visible")) {
					// ラベル部分クリックでイベントが2重発生しないよう抑制
					$pseudo.trigger("click");
					e.preventDefault();
				}
			});

			$(".pseudo-radio", $row).on("click", onClickPseudoRadio);
			$(".pseudo-checkbox", $row).on("click", onClickPseudoCheckbox);
		}
	}
});

/**
 * 偽オブジェクト生成
 * @returns
 */
function createPseudoObject() {
	var $this = $(this), isChecked = "";

	//jqgridの選択用checkboxは除外
	if ($this.hasClass("cbox")) {
		return;
	}

	if ($this.prop("checked")) {
		isChecked = "checked";
	}
	var $pseudo = $("<span class='pseudo-" + $this.attr("type") + " " + isChecked + "'/>").insertBefore(this);
	$pseudo.parent().addClass("pseudo-parent");
	if ($this.prop("disabled")) {
		$pseudo.parent().addClass("disabled");
	}
	if ($this.attr("type") === "checkbox") {
		$this.on("iplassCheckboxPropChange", function() {
			if ($this.is(":checked")) {
				$pseudo.addClass("checked");
			} else {
				$pseudo.removeClass("checked");
			}
		});
	}

	//ダミーのイベントを発生させる
	$this.on("click", function(e) {
		e.stopPropagation();
		$(this).prev().trigger("click");
	});
}

/**
 * 偽チェックボックスクリックイベント
 * @returns
 */
function onClickPseudoCheckbox() {
	var $this = $(this);
	if ($this.next().is(":disabled")) {
		return false;
	}
	if ($this.is(".checked")) {
		$this.removeClass("checked").next("input").prop("checked", false).trigger("change");
	} else {
		$this.addClass("checked").next("input").prop("checked", true).trigger("change");
	}
	return false;
}

/**
 * 偽ラジオクリックイベント
 * @returns
 */
function onClickPseudoRadio() {
	var $this = $(this),
		$radio = $this.next("input"),
		$pseudoSiblings = $("input[type=radio][name='" + $radio.attr("name") + "']").prev();
	if ($this.next().is(":disabled")) {
		return false;
	}
	if (!$this.is(".checked")) {
		$pseudoSiblings.removeClass("checked");
		$this.addClass("checked").next("input").prop("checked", true).trigger("change");
	}
	return false;
}

/**
 * 検索画面の全削除時のチェック解除
 */
function clearAllDelete() {
	$("#cb_searchResult").prop("checked", false).parent().find(".pseudo-checkbox").removeClass("checked");
}

/**
 * ヘッダーセレクトプルダウン
 *
 * [オプション]
 * pulldown:	プルダウンクラス
 * select:	セレクトクラス
 * selected:	プルダウン内の選択している箇所
 * f:  クリック時のクラス
 *
 */
(function($) {
	$.fn.jQselectable = function(options) {
		var defaults = {
			pulldown: ".pulldown",
			select: "a.select",
			selected: "a.selected",
			f: "a.select_focus"
		};
		var options = $.extend(defaults, options);
		return this.each(function() {
			var $self = $(this),
				$select = $(options.select, $self),
				pulldown = $(options.pulldown, $self),
				data = $("input:hidden", $self),
				select_value = $("span", $select),
				text;
			$select.click(function(e) {
				if ($(this).hasClass("select_focus")) {
					$(this).removeClass("select_focus");
					$(options.pulldown).hide();
				} else {
					pulldown.show().css("z-index", 50);
					$(this).addClass("select_focus");
				}
				e.stopPropagation();
				return false;
			});
			$("a", pulldown).click(function() {
				text = $(this).text();
				select_value.text(text);
				$(options.selected, pulldown).removeClass("selected");
				$(options.f).removeClass("select_focus");
				$(this).addClass("selected");
				pulldown.hide();
				$(".select").show();
				return false;
			});
			$("body, .hed-pull, a").not("a.select").click(function() {
				$(options.f).removeClass("select_focus");
				$(options.pulldown).hide();
				$(".select").show();
			});
		});
	};
})(jQuery);
(function($) {
	$.fn.calendarAddList = function(options) {
		var defaults = {};
		var options = $.extend(defaults, options);
		if (!this) return false;
		return this.each(function() {
			var $this = $(this);
			$this.click(function(event) {
				if ($this.next().children("ul").children().length == 1) {
					$this.next().children("ul").children("li").children("a").click();
				} else {
					event.stopPropagation();
					var hasClass = $this.hasClass("open");
					$(".add.open").each(function() {
						$(this).removeClass("open");
						$(this).next().removeClass("open");
					});
					if (hasClass) {
						$this.removeClass("open");
						$this.next().removeClass("open");
					} else {
						$this.addClass("open");
						$this.next().addClass("open");
					}
				}
			});
			$("body").click(function(e) {
				$this.removeClass("open");
				$this.next().removeClass("open");
			});
		});
	};
})(jQuery);
/**
 * 多段メニュー
 */
(function($) {
	$.fn.subMenu = function(option) {
		var defaults = {};
		var options = $.extend(defaults, option);
		if (!this) return false;
		return this.each(function() {
			var $this = $(this);
			//子要素をメニュー化
//			$this.click(function(event) {
//				event.stopPropagation();
//				if ($this.hasClass("open")) {
//					$this.removeClass("open");
//				} else {
//					$(".subMenuRoot.open").removeClass("open"); //他のメニュー閉じる
//					$this.addClass("open");
//					$("#snav").css({
//						zIndex: 41
//					});
//				}
//			});
//			$("body, .select, .hed-pull").click(function(e) {
//				$(".subMenuRoot.open").removeClass("open"); //メニュー閉じる
//				$("#snav").css({
//					zIndex: 2
//				});
//			});
//			$(".change-area", $this).hover(function() {
//				$(this).children("ul").css({
//					left: $(this).parent().width()
//				});
//			}, function() {
//				$(this).children("ul").css({
//					left: 0
//				});
//			});
		});
	};
})(jQuery);
