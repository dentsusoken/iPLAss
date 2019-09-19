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
// 垂直メニューデザイン用のJavascript
////////////////////////////////////////////////////////

// デザイン固有のfunction、pluginの定義、puluginの実行等。

var $navSplit;//サイドエリア開閉のイベントを紐づけるための変数(もう少しうまい方法はないのか・・・)

/**
 * イベント実行
 */
$(function(){
	/** 全体 **/
	$("#content").addClass("fixHeight");	//高さ揃えのクラス追加
	$("#snav, #content-inner, #split-block").addClass("fixHeightChild");	//高さ揃えのクラス追加

	$("#pagetop").pageTop();	//ページトップ
	$(".rollover").rollOverSet();	//ロールオーバー

	$(".modal-btn, .modal-lnk").modalWindow();

	//jQuery UI Dialogがドラッグでおかしな動きをするので、デフォルト値を変更
	if ($("#dialog_parent").length > 0) {
		$.ui.dialog.prototype.options.appendTo = "#dialog_parent";
	}

	/** ヘッダ **/
	$(".selectbox").jQselectable();	//ヘッダーセレクトプルダウン
	$("li#account-01").accountInfo();	//アカウント情報プルダウン
	$("li#setting").accountInfo();	//アカウント情報プルダウン(新規作成)
	$navSplit = $("#split-block").navSplit();	//サイドエリア開閉

	/** メニュー **/
	$("#nav.sub-popup .nav-wrap > li").children(".nav-detail").navToggle();	//グローバルメニュー(サイドナビ開閉)
	$("#nav.sub-popup .subMenuRoot").subMenu();	//グローバルポップアップメニュー
	$("#snav").tabContent({
		clickFunc:function($menu) {
			//メニュー/ウィジェットの状態保持
			if ($menu.hasClass("menu-shortcut")) {
				setCookie("currentMenuTab", "menu-shortcut", 0);
			} else {
				setCookie("currentMenuTab", "menu-shortcut", -1);
			}
		}
	});	//サイドナビタブ切り替え

	//cookieから展開状態復元
	$("#nav .nav-wrap > li:has('.nav-detail')").each(function() {
		var id = $(this).attr("id");
		if (getCookie(id)) {
			$("p>a", this).trigger("click");
		}
	});
	if (getCookie("currentMenuId")) {
		$("#" + es(getCookie("currentMenuId"))).addClass("selected");
		$("#" + es(getCookie("currentMenuId"))).parents(".menu-node").addClass("selected");
	}
	if (getCookie("currentMenuTab")) {
		$("." + getCookie("currentMenuTab")).trigger("click");
	}
	if (getCookie("navSplit")) {
		$("#split-btn").trigger("click");
	}

	/** パーツ、ウィジェット **/
	//ツリービュー
	$(".treeViewList").treeViewList();
	$(".treeViewGrid").treeViewGrid();

	$("div.entity-list-widget").nameList();

	/** 汎用画面 **/
	//共通
	$(".tp").toolTip();	//ツールチップ
	$(".tp02").toolTip({
		offleft : 75,
		range : 77
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
	$(".tab-wrap").switchCondition();
	$("table.multi-col").multiColumnTable();
	$(".data-deep-search .add").on("click", function(){ //検索項目追加
		addDetailCondition();
		$(".fixHeight").fixHeight();
	});
	$(".data-deep-search").on("click", ".delete", function(){	//検索項目削除
		deleteDetailCondition(this);
		$(".fixHeight").fixHeight();
	});

	//詳細
	$(".sechead").sectoinToggle();	//セクション開閉
	$(".nav-section li").pageSecton();	//ページ内スクロール

	$(".massReference").massReferenceTable();
	$(".refCombo").refCombo();
	$(".refComboController").refComboController();
	$(".recursiveTreeTrigger").refRecursiveTree();
	$(".refLinkSelect").refLinkSelect();
	$(".refLinkRadio").refLinkRadio();
	$(".refUnique").refUnique();

	if (typeof CKEDITOR !== "undefined") {
		CKEDITOR.on("instanceReady", function(ev) {
			$(".fixHeight").fixHeight();
		});//CKEditor読み込み完了時に高さ調整
	}

	if ($.fn.fileupload) {
		$("input.upload-button").each(function() {//画像アップロード
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

	//編集画面のラジオボタンの選択解除
	$("input.radio-togglable").togglableRadio();

	//ゴミ箱
	$(".allInput").allInputCheck();

	//ブラウザリサイズ時の高さ調整
	$(window).on("resize", function() {
		$(".fixHeight").fixHeight();
	});

	$("#content").show();

	//以下は画面表示後でないと動かない処理
	$(".fixHeight").fixHeight();	//高さ揃え実行
	$(".tableStripe").tableStripe();
});

/**
 * ナビトグル
 */
(function($) {
	$.fn.navToggle = function(){
		return $(this).each(function(){
			var $this = $(this);
			$this.prev().toggleFunction(function(){
				$this.parent().addClass("hover");
				$(".fixHeight").fixHeight();

				var id = $this.parent().attr("id");
				setCookie(id, true, 0);
			},
			function () {
				$this.parent().removeClass("hover");
				$(".fixHeight").fixHeight();

				var id = $this.parent().attr("id");
				setCookie(id, false, -1);
			});
		});
	};
})(jQuery);

/**
 * サイドエリア開閉
 *
 * [オプション]
 * content:	ホバーのクラス追加する属性
 *
 */
(function($){
	$.fn.navSplit = function(options){
		var defaults = {
			content : "#content",
			footer : "#footer"
		};
		var options = $.extend(defaults, options);
		return $(this).each(function(){
			var $this = $(this),
				$splitbtn = $this.children(),
				$footer = $(options.footer),
				//$offtop = $splitbtn.offset().top,
				$offtop = $footer.offset().top,
				$content = $(options.content),
				$contenth;

			$this.on("mouseenter", function(e){
				$content.addClass("hover");
				$contenth = $this.height();
				if($contenth - 25 < e.pageY - $offtop){
					$splitbtn.css({"bottom": 0, "top": "auto"});
				}else if(e.pageY - $offtop < 40){
					$splitbtn.css({top: 0});
				}else{
					$splitbtn.css({top: e.pageY - $offtop - 40});
				}

			}).on("mouseleave", function(){
				$content.removeClass("hover");
			}).toggleFunction(function(){
				$content.addClass("cotent-col-01");
				$(".fixHeight").fixHeight();

				setCookie("navSplit", true, 0);

				$this.trigger("navSplitToggle", {toggle:"on"});
			},
			function () {
				$content.removeClass("cotent-col-01");
				$(".fixHeight").fixHeight();

				setCookie("navSplit", false, -1);

				$this.trigger("navSplitToggle", {toggle:"off"});
			});
		});
	};
})(jQuery);

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
		var defaults = {
		};
		var options = $.extend(defaults, option);
		if (!this) return false;

		return this.each(function() {
			var $this = $(this);
			//子要素をメニュー化
			$this.click(function(event) {
				event.stopPropagation();
				if ($this.hasClass("open")) {
					$this.removeClass("open");
				} else {
					$(".subMenuRoot.open").removeClass("open");//他のメニュー閉じる
					$this.addClass("open");
					$("#snav").css({zIndex:41});
				}
			});

			$("body, .select, .hed-pull").click(function(e){
				$(".subMenuRoot.open").removeClass("open");//メニュー閉じる
				$("#snav").css({zIndex:2});
			});

			$(".change-area", $this).hover(function() {
				$(this).children("ul").css({left:$(this).parent().width()});
			}, function() {
				$(this).children("ul").css({left:0});
			});
		});
	};
})(jQuery);

/**
 * 検索画面の全削除時のチェック解除
 */
function clearAllDelete() {
	$("#cb_searchResult").prop("checked", false);
}
