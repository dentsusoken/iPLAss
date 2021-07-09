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
// 水平メニューデザイン用のJavascript
////////////////////////////////////////////////////////

// デザイン固有のfunction、pluginの定義、puluginの実行等。

var $navSplit;//サイドエリア開閉のイベントを紐づけるための変数(もう少しうまい方法はないのか・・・)

/**
 * イベント実行
 */
$(function(){
	/** 全体 **/
	$("#content").addClass("fixHeight");	//高さ揃えのクラス追加
	$("#widget, #content-inner, #split-block").addClass("fixHeightChild");	//高さ揃えのクラス追加

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
	$("#nav.sub-popup ul.nav-wrap > li").navToggle();	//グローバルメニュー(ポップアップ形式)
	$("#nav.sub-popup li.subMenuRoot").subMenu();	//グローバルポップアップメニュー
	$("#nav.sub-droplist ul.nav-wrap > li").navDroplist();	//グローバルメニュー（ドロップリスト形式）

	//cookieから展開状態復元
	var currentMenuId = getSessionStorage("currentMenuId");
	if (currentMenuId) {
		$("#" + es(currentMenuId)).addClass("selected");
		$("#" + es(currentMenuId)).parents(".menu-node").addClass("selected");
	}
	if (getSessionStorage("navSplit")) {
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
 * ナビ トグル
 */
(function($) {
	$.fn.navToggle = function(option){
		var defaults = {
		};
		var options = $.extend(defaults, option);
		if (!this) return false;

		var $items = $(this);	//第1階層全アイテム

		return this.each(function(){
			var $this = $(this);	//第1階層

			var $nav = $this.parent("ul.nav-wrap");	//TOP

			//第1階層がHoverされたときの設定
			$this.on("mouseenter", setHover)
				.on("mouseleave", removeHover)
				.on("click", function(){
					var lockId = $nav.attr("lockId");
					var id = $(this).attr("id");
					if (lockId == id) {
						$nav.removeAttr("lockId");
					} else {
						$nav.attr("lockId", id);
						setHover();
					}
				});

			//第2階層がHoverされた時の設定
			$this.children("ul").children("li").each(function(){
				var $item = $(this);
				$item.on("mouseenter", function(){
					$item.addClass("hover");
				}).on("mouseleave", function(){
					$item.removeClass("hover");
				});
			});

			function setHover() {
				var lockId = $nav.attr("lockId");
				if (!lockId) {
					//ロック対象が未指定の場合は、対象のアイテムを表示

					//全第2階層を非表示にする
					$items.removeClass("hover");
					$items.children("ul").hide();

					//対象の第2階層を表示
					$this.addClass("hover");
					$this.children("ul").show();
				} else {
					//ロック対象が指定されている場合は、対象のアイテムを表示
					$items.each(function() {
						var $item = $(this);
						if (lockId == $item.attr("id")) {
							//対象なので表示
							$item.addClass("hover");
							$item.children("ul").show();
						} else {
							//対象外は非表示
							$item.removeClass("hover");
							$item.children("ul").hide();
						}
					});
				}
			}
			function removeHover() {
				var lockId = $nav.attr("lockId");
				if (!lockId) {
					$this.removeClass("hover");
					$this.children("ul").hide();
				}
			}
		});
	};
})(jQuery);

/**
 * ナビ ドロップリスト
 */
(function($) {
	$.fn.navDroplist = function(option){
		var defaults = {
		};
		var options = $.extend(defaults, option);
		if (!this) return false;

		var $items = $(this);	//第1階層全アイテム
		var $nav = $items.parent("ul.nav-wrap");	//TOP

		//Bodyがクリックされたらメニューを閉じる
		$("body, .hed-pull, .subMenuRoot").on("click", function(e){
			$nav.removeAttr("lockId");
			$(".hover", $nav).removeClass("hover");
		});

		return this.each(function() {
			var $this = $(this);	//第1階層(li)

			//第1階層がHoverされたときの設定
			$this.on("click", function(event){
				event.stopPropagation();	//BodyのClickイベントを発生させない
				var lockId = $nav.attr("lockId");
				var id = $(this).attr("id");
				if (lockId == id) {
					$nav.removeAttr("lockId");
					removeTopHover();
				} else {
					$nav.attr("lockId", id);
					setTopHover();
				}
			});

			//第2階層以降がHoverされたときの設定
			$("ul li", $this).each(function(){
				var $item = $(this);	//第2階層以下のアイテム
				$item.on("mouseenter", function(){
					//下位メニューの表示位置調整
					adjustSideMenuPosition($item);
					$item.addClass("hover");
				}).on("mouseleave", function(){
					$item.removeClass("hover");
				});
			});

			function setTopHover() {
				var lockId = $nav.attr("lockId");
				if (!lockId) {
					//ロック対象が未指定の場合は、対象のアイテムを表示

					//全第2階層を非表示にする
					$items.removeClass("hover");

					//第2階層の表示位置調整
					adjustBottomMenuPosition($this);

					//第1階層にhoverを付加し、第2階層を表示
					$this.addClass("hover");
				} else {
					//ロック対象が指定されている場合は、対象のアイテムを表示
					$items.each(function() {
						var $item = $(this);
						if (lockId == $item.attr("id")) {
							//対象なので表示

							//第2階層の表示位置調整
							adjustBottomMenuPosition($this);

							$item.addClass("hover");
						} else {
							//対象外は非表示
							$item.removeClass("hover");
						}
					});
				}
			}

			function removeTopHover() {
				var lockId = $nav.attr("lockId");
				if (!lockId) {
					$this.removeClass("hover");
				}
			}

			function adjustBottomMenuPosition($topItem) {

				var $subMenu = $topItem.children("ul");

				//Topメニュー幅とサブメニュー幅の調整(第2階層は最小でTopメニューと同じ幅にする)
				if (!$subMenu.attr("data-fix-width")) {
					//Topのpに対してBorder指定しているので、
					//pのpaddingを含めたclientWidthと比較して、
					//pより小さい場合はサブメニューの幅を合わせる
					var topWidth = $topItem.children("p").prop("clientWidth") - 0;	//paddingを含めたwidthを取得
					var menuWidth = $subMenu.width() - 0;
					if (topWidth > menuWidth) {
						isShortSubMenu = true;
						//$subMenu.width(topWidth); //widthで指定すると、pと同じサイズを指定していても一部ずれる現象が発生するためrightを0に設定して対応
						$subMenu.css("right", 0);
						$subMenu.attr("data-short-width", true); //position設定時のためにマーク
					}
					$subMenu.attr("data-fix-width", true);
				}

				//横はleftに揃え、表示できない場合はrightを揃える
				//縦はbottom固定
				$subMenu.position({
					my: "left top-1", //topのborderを消すため上にあげる
					at: "left bottom",
					of: $topItem,
					collision: "flip none",
					using: function(position, feedback ) {
						if (position.left < 0) {
							//左側に表示ができない場合右側に表示させるが、
							//一部右側が少しはみ出る現象が発生するため、rightを0に設定して対応
							$(this).css("right", 0);
						} else {
							//左側に表示可能なためright属性を削除
							if (!$(this).attr("data-short-width")) {
								//サブメニュの幅が小さくてrightを0にした場合以外の場合、rightをクリア
								$(this).css("right", "");
							}
						}
						$(this).css("left", position.left);
						$(this).css("top", position.top);
					}
				});
			}

			function adjustSideMenuPosition($menuItem) {

				//横はrightに合わせ(leftをrightに合わせる)、表示できない場合はleftに合わせる
				//縦はtop(border分-1)固定
				$menuItem.children("ul").position({
					my: "left top-1", //border分下にずれるので上にあげる
					at: "right top",
					of: $menuItem,
					collision: "flip none",
					using: function(position, feedback ) {
						$(this).css("left", position.left);
						$(this).css("top", position.top);
					}
				});
			}
		});
	};
})(jQuery);

/**
 * サイドエリア開閉
 *
 * [オプション]
 * content:   ホバーのクラス追加する属性
 *
 */
(function($){
	$.fn.navSplit = function(options){
		var defaults = {
			content : "#content",
			widget : "#widget",
			footer : "#footer"
		};
		var options = $.extend(defaults, options);
		var $widget = $(options.widget);
		if ($widget.length == 0) {
			$(options.content).addClass("cotent-col-01");
			$(this).hide();
			return $(this);
		}
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

				setSessionStorage("navSplit", "true");

				$this.trigger("navSplitToggle", {toggle:"on"});
			},
			function () {
				$content.removeClass("cotent-col-01");
				$(".fixHeight").fixHeight();

				deleteSessionStorage("navSplit");

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
				value,
				text;
			pulldown.hide().children(":last-child").addClass("last-child");
			$select.click(function(e) {
				if($(this).hasClass("select_focus")) {
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
				value = $(this).attr("href").replace("#",""),
				text = $(this).text();
				select_value.text(text);
				data.val(value);
				$(options.selected, pulldown).removeClass("selected");
				$(options.f).removeClass("select_focus");
				$(this).addClass("selected");
				pulldown.hide();
				return false;
			});
			$("body, .hed-pull, a, .menu-node").not("a.select").click(function() {
				$(options.f).removeClass("select_focus");
				$(options.pulldown).hide();
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
				}
			});

			$("body, .select, .hed-pull").click(function(e){
				$(".subMenuRoot.open").removeClass("open");//メニュー閉じる
			});

			$(".change-area", $this).hover(function() {
				$(this).children("ul").css({left:$(this).parent().width()});
			}, function() {
				//IEにてhoverが外れた場合に子メニューが一瞬右に被って表示されるので指定しない
				//change-areaがhoverされていない場合、css側でdisplayをnoneにしているので消える
				//$(this).children("ul").css({left:0});
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
