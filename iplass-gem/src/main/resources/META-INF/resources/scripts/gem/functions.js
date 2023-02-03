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

// デザイン共通のfunction、pluginの定義。puluginの実行はdesign.jsで。

/*
 * toggleFunction
 * pageTop
 * rollOverSet
 * accountInfo
 * tabContent
 * sectoinToggle
 * pageSecton
 * allInputCheck
 * toolTip
 * modalDialog
 * modalWindow
 * subModalWindow
 * commaField
 * togglableRadio
 * switchCondition
 * massReferenceTable
 * pager
 * nameList
 * refCombo
 * refComboController
 * refLinkSelect
 * refLinkRadio
 * refUnique
 * multiColumnTable
 * applyDatepicker
 * applyTimepicker
 * applyDatetimepicker
 * treeViewList
 * treeViewGrid
 */

$(function(){
	//IE8でtrimが実装されていないので追加
	if (typeof String.prototype.trim !== 'function') {
		String.prototype.trim = function() {
			return this.replace(/^\s+|\s+$/g, '');
		}
	}
	//IE8でindexOfが実装されてないので追加
	if (typeof Array.prototype.indexOf !== 'function') {
		Array.prototype.indexOf = function(o) {
			for ( var i in this) {
				if (this[i] == o) {
					return i;
				}
			}
			return -1;
		}
	}
	//IEでstartsWith、endsWithが実装されてないので追加
	if (typeof String.prototype.startsWith !== 'function') {
		String.prototype.startsWith = function(searchString, position) {
			position = position || 0;
			return this.lastIndexOf(searchString, position) === position;
		}
	}
	if (typeof String.prototype.endsWith !== 'function') {
		String.prototype.endsWith = function(searchString, position) {
			position = position || this.length;
			position = position - searchString.length;
			return position >= 0 && this.lastIndexOf(searchString) === position;
		}
	}

	/* ダイアログ */
	$("body.modal-body").modalDialog();
	scriptContext["createModalFunction"] = function(modal, name, callback) {
		var $dialogs = $(modal, document);
		$dialog = $("<div class='modal-dialog' />").attr({id: "modal-dialog-" + name}).appendTo($dialogs);
		$("<div class='modal-wrap' />").appendTo($dialog);
		var $under = $("<div class='modal-inner sub-modal-inner' />").appendTo($dialog);
		var $title = $("<h2 class='hgroup-01' />").appendTo($under);
		$("<span />").attr({id: "modal-title-" + name}).appendTo($title);
		$("<p class='modal-maximize sub-modal-maximize' />").text(scriptContext.gem.locale.modal.maximizeLink).appendTo($under);
		$("<p class='modal-restore sub-modal-restore' />").text(scriptContext.gem.locale.modal.restoreLink).appendTo($under);
		$("<p class='modal-close sub-modal-close' />").text(scriptContext.gem.locale.modal.closeLink).appendTo($under);
		var ifrm = "<iframe src=\"about:blank\" height=\"686\" width=\"100%\" frameborder=\"0\" name=\"" + name + "\"/>";
		var $frame = $(ifrm).appendTo($under);

		callback.call(this);
	};
	scriptContext["closeModalFunction"] = closeModalDialog;
	if ($("body.modal-body").length == 0) {
		scriptContext.getWindow = function() {return window;};
		scriptContext.overlayManager = {
			overlays:new Array(),
			zindex:52,
			addOverlay:function(overlay){
				for (var i = 0; i < this.overlays.length; i++) {
					$(this.overlays[i]).removeClass("modal-overlay");
				}
				$(overlay).css({zIndex:this.nextZindex()}).addClass("modal-overlay").attr("overlay-id", this.zindex);
				this.overlays.push(overlay);

				//スクロール無効化
				if (this.overlays.length == 1) {
					$("body").css('overflow','hidden');
				}
			},
			removeOverlay:function($overlay){
				for (var i = this.overlays.length - 1; i >= 0; i--) {
					if (this.overlays[i] == $overlay
							|| $(this.overlays[i]).attr("overlay-id") == $overlay.attr("overlay-id")) {
						$overlay.css({zIndex:0}).removeClass("modal-overlay");
						if (i > 0) {
							$(this.overlays[i - 1]).addClass("modal-overlay");
						}
						this.overlays.splice(i, 1);
						this.zindex -= 2;

						//スクロール有効化
						if (this.overlays.length == 0) {
							$("body").css('overflow','auto');
						}
						return;
					}
				}
			},
			nextZindex:function() {return this.zindex++;}
		};
	}

	/* jQueryUI ダイアログ設定 */
	$(document).on("dialogopen", ".mtp-jq-dialog", function() {
		var $html = $("html");
		var $document = $(document);
		var $window = $(window);
		var $overlay =$(".ui-widget-overlay.ui-front");
		var $under = $(".ui-dialog.ui-corner-all.ui-widget.ui-widget-content.ui-front.ui-dialog-buttons.ui-draggable");
		//ダイアログ表示前、スクロールバーを非表示にする。
		$html.css('overflow','hidden');
		resizeHandler();
		$window.on("resize", resizeHandler);

		function resizeHandler(e){
			$overlay.width(0);
			$document.scrollLeft(0);
			var height = Math.max($window.height() , $document.height());
			var width = Math.min($window.width() , $document.width());
			$overlay.css({
				height : height,
				width : width,
				top: $document.scrollTop(),
				left: 0,
				position:"absolute"
			});
			setModalWindowToCenter();
		}

		function setModalWindowToCenter(){
			var width = Math.min($window.width() , $document.width());
			var height = Math.min($window.height() , $document.height());
			var top = height / 2 > 120 ? height / 2 - 120 : 10;
			var left = width / 2 > 240 ? width / 2 - 240 : 10;
			$under.css({
				top: top + $document.scrollTop(),
				left: left,
				marginLeft: 30
			});
		}
	});
	$(document).on("dialogclose", ".mtp-jq-dialog", function() {
		$("html").css('overflow','');
	});
});

/**
 * jQuery1.9で廃止されたtoggleの代替処理
 * jQuety.toggleとの差別化のため関数名を変える
 * ⇒元ソースはjquery-migrate-1.2.1.jsから
 */
(function($) {
	var oldToggle = $.fn.toggle;
	$.fn.toggleFunction = function(fn, fn2) {
		// Don't mess with animation or css toggles
		if (!$.isFunction(fn) || !$.isFunction(fn2)) {
			return oldToggle.apply(this, arguments);
		}

		// Save reference to arguments for access in closure
		var args = arguments,
			guid = fn.guid || $.guid++,
			i = 0,
			toggler = function(event) {
				// Figure out which function to execute
				var lastToggle = ($._data(this, "lastToggle" + fn.guid) || 0) % i;
				$._data(this, "lastToggle" + fn.guid, lastToggle + 1);

				// Make sure that clicks stop
				event.preventDefault();

				// and execute the function
				return args[lastToggle].apply(this, arguments) || false;
			};

		// link all the functions, so any of them can unbind this click handler
		toggler.guid = guid;
		while (i < args.length) {
			args[i++].guid = guid;
		}

		return this.on("click", toggler);
	};
})(jQuery);

/**
 * ページトップ
 */
(function($) {
	$.fn.pageTop = function(){
		var $this = this;
		$this.hide();
		$(window).on("scroll", function(){
			if($(this).scrollTop() > 10){
				$this.fadeIn();
			}else{
				$this.fadeOut();
			}
		});

		jQuery.easing.quart = function (x, t, b, c, d) {
			return -c * ((t = t / d - 1) * t * t * t - 1) + b;
		};

		$this.children().on("click", function(){
			$('body,html').animate({
				scrollTop : 0
			}, 300, 'quart');
			return false;
		});
		return this;
	};
})(jQuery);


/**
 * ロールオーバー
 */
(function($) {
	$.fn.rollOverSet = function(){
		this.on("mouseenter", function(){
			var path = $(this).attr("src");
			path = path.replace(/(?:_o)?(\.gif|\.png|\.jpg)/,"_o$1");
			$(this).attr("src",path);
		})
		.on("mouseleave", function(){
			var path = $(this).attr("src");
			path = path.replace(/(?:_o(\.gif|\.png|\.jpg))/,"$1");
			$(this).attr("src",path);
		});
		return this;
	};
})(jQuery);

/**
 * ヘッダーアカウント情報
 *
 * [オプション]
 * change: 第2階層を持っているメニュー
 *
 */
(function($) {
	$.fn.accountInfo = function(options){
		var defaults = {
			change : '.change-area'
		};
		var options = $.extend(defaults, options);
		if (!this) return false;

		return this.each(function() {
			var $this = $(this), //第1階層のTOP
				tw = $this.width();

			//第1階層がClickされたときの設定
			$this.on("click", function(event){
				event.stopPropagation(); //bodyのClickイベントを発生させない

				//第1階層の表示制御
				if($(this).hasClass("open")){
					$this.removeClass("open");
				}else{
					$(this).prevAll().removeClass("open");
					$(this).nextAll().removeClass("open");

					//第2階層の表示位置調整
					adjustBottomMenuPosition($this);

					$this.addClass("open");
				}
			});

			//Bodyがクリックされたらメニューを閉じる
			$('body, .select, .subMenuRoot, .menu-node').on("click", function(e){
				$('li.hed-pull').removeClass("open"); //第1階層を非表示
			});

			//第2階層の表示制御
			$(options.change, $this).each(function(){
				var $item = $(this);
				$item.on("mouseenter", function(){
					//第3階層の表示位置調整
					adjustSideMenuPosition($item);
					$item.addClass("hover");
				}).on("mouseleave", function(){
					$item.removeClass("hover");
				});
			});

			//第2階層の表示位置制御
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
					//my: "left top-1", //topのborderを消すため上にあげる
					my: "left top+11", //第2階層は全文検索Inputのためtop指定しているため調整
					at: "left bottom",
					of: $topItem,
					collision: "flip none",
					using: function(position, feedback ) {
						if (position.left < 0) {
							//左側に表示ができない場合右側に表示させるが、
							//一部右側が少しはみ出る現象が発生するため、rightを0に設定して対応
							$(this).css("right", 0);
							//文言が長い場合にはみ出る現象が発生する為、leftをautoに設定して対応
							$(this).css("left", "auto");
						} else {
							//左側に表示可能なためright属性を削除
							if (!$(this).attr("data-short-width")) {
								//サブメニュの幅が小さくてrightを0にした場合以外の場合、rightをクリア、leftを0に設定。
								$(this).css("right", "");
								$(this).css("left", 0);
							}
						}
						$(this).css("top", position.top);
					}
				});
			}

			//第3階層の表示位置制御
			function adjustSideMenuPosition($hasItem) {

				//横はrightに合わせ(leftをrightに合わせる)、表示できない場合はleftに合わせる
				//縦はtop(border分-1)固定
				$hasItem.children("ul").position({
					my: "left top-1", //border分下にずれるので上にあげる
					at: "right top",
					of: $hasItem,
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
 * タブ切り替え
 *
 * [オプション]
 * menu:	タブメニュークラス
 * panel:  切り替えコンテンツクラス
 * cunt:	初回表示panel
 *
 */
(function($){
	$.fn.tabContent = function(options){
		var defaults = {
			menu : '.tab-menu',
	 		panel : '.tab-panel',
			cunt : 0,
			clickFunc: null
		};
		var options = $.extend(defaults, options);
		if (!this) return false;
		return this.each(function() {
			var $this = $(this),
				$menu = $this.find(options.menu).children(),
				$panel = $this.find(options.panel);
			$menu.eq(options.cunt).addClass('current');
			$panel.not(':eq('+options.cunt+')').hide();
			$menu.on("click", function() {
				$(this).siblings().removeClass('current');
				$(this).addClass('current');
				var clickIndex = $menu.index(this);
				$panel.hide().eq(clickIndex).show();
				$(".fixHeight").fixHeight();
				if (options.clickFunc && $.isFunction(options.clickFunc))
					options.clickFunc.call(this, $(this));
				return false;
			});
		});
	};
})(jQuery);

/**
 * セクション開閉
 *
 * [オプション]
 * allOpen:	全て開くクラス
 * allClose:	全て閉じるクラス
 * closeClass:	閉じている時の判別クラス
 *
 */
(function($){
	$.fn.sectoinToggle = function(options){
		var defaults = {
			allOpen : '.all-open',
			allClose : '.all-close',
			closeClass : 'disclosure-close'
		};
		var options = $.extend(defaults, options);
		if (!this) return false;
		return this.each(function(){
			var $allOpen = $(options.allOpen),
				$allClose = $(options.allClose),
				$this = $(this);
			$this.on("click", function(){
				if($this.hasClass(options.closeClass)){
					$this.removeClass(options.closeClass).next().show();
					$(".fixHeight").fixHeight();
				}else{
					$this.addClass(options.closeClass).next().hide();
					$(".fixHeight").fixHeight();
				}
			});
			$allOpen.on("click", function(){
				if($this.hasClass(options.closeClass)){
					//$this.removeClass(options.closeClass).next().show();
					$this.click();
				}
				$(".fixHeight").fixHeight();
				return false;
			});
			$allClose.on("click", function(){
				//if($this.not('hasClass(options.closeClass)')){
				if(!$this.hasClass(options.closeClass)){
					//$this.addClass(options.closeClass).next().hide();
					$this.click();
				}
				$(".fixHeight").fixHeight();
				return false;
			});
		});
	};
})(jQuery);

/**
 * セクションスクロール
 */
(function($) {
	$.fn.pageSecton = function(){
		var $this = this,
			id,
			sectionId,
			sectionHeader,
			sectionHeaderBlock,
			hash;
		jQuery.easing.quart = function (x, t, b, c, d) {
			return -c * ((t = t / d - 1) * t * t * t - 1) + b;
		};

		$this.children().on("click", function(){
			id = $(this).attr('href');
			sectionId = $('#main-inner').find($(id));
			sectionHeader = sectionId.find("h3");
			sectionHeaderBlock = sectionHeader.parent();
			hash = sectionId.offset();
			if(sectionHeaderBlock.hasClass('disclosure-close')){
				sectionHeader.click();
			}
			$("html,body").animate({
			  scrollTop: hash.top,
			  scrollLeft: hash.left
			}, 300, 'quart');
			return false;
		});
		return this;
	};
})(jQuery);


/**
 * チェックボックス全選択
 */
(function($) {
$.fn.allInputCheck = function(){
	return this.each(function(){
		var $this = $(this),
			checkInp = $('.selectCheck input'),
			$tr = $('.selectCheck tr');
		$this.on("click", function(){
			if(this.checked){
				checkInp.prop('checked', true)
				.closest('tr').addClass('selected');
			}else{
				checkInp.prop('checked', false)
				.closest('tr').removeClass('selected');
			}
		});
		$tr.on("click", function(){
			$this = $(this);
			if($this.hasClass('selected')){
				$this.removeClass('selected')
				.find('input').prop('checked', false);
			}else{
				$this.addClass('selected')
				.find('input').prop('checked', true);
			}
		});
	});
};
})(jQuery);


/**
 * ツールチップ
 *
 * [オプション]
 * offleft:   offsetの微調整
 * range:  下矢印の位置
 *
 */
(function($){
	$.fn.toolTip = function(options){
		var defaults = {
			offleft : 4,
			range : 6
		};
		var options = $.extend(defaults, options);
		if (!this) return false;
		return this.each(function() {
			var $toolwrap = $('.tooltip-wrap');
			var $tooltip = $('.tooltip');
			var $tooltxt = $('.tooltxt');
			var $toolicon = $('.tool-icon');
			var $this = $(this);
			var range = $(options.range);
			var $title = $this.attr("title");

			$this.on("mouseenter", function() {
				$this.attr("title","");
				var offset = $this.offset();
				$toolwrap.show();
				$toolicon.css({
					left :  (options.range)
				});
				$tooltxt.append($title);
				$toolwrap.css({
					top : offset.top - $tooltxt.height() - 23,
					left : offset.left - (options.offleft)
				});
			}).on("mouseleave", function() {
				$toolwrap.hide();
				$tooltxt.text('');	//title属性が表示するのを防ぐ
				$this.attr("title",$title);
			});
 		});
	};
})(jQuery);

/**
 * モーダルダイアログ初期化
 */
(function($){
	$.fn.modalDialog = function(option){
		var defaults = {
			dialogs : '.modal-dialogs',
			controls : '.modal-body .modal-btn, .modal-body .modal-lnk'
		};
		var options = $.extend(defaults, option);
		if (!this) return false;
		return this.each(function() {
			var rootWindow = null;
			if (!parent.document.rootWindow) {
				rootWindow = parent.document;
			} else {
				rootWindow = parent.document.rootWindow
			}
			document.rootWindow = rootWindow;
			var name = uniqueId();
			document.targetName = name;
			var windowManager = null;
			if (!rootWindow.scriptContext["windowManager"]) {
				windowManager = {};
				rootWindow.scriptContext["windowManager"] = windowManager;
			} else {
				windowManager = rootWindow.scriptContext["windowManager"];
			}
			windowManager[name] = document;

			//↓IE7ではIFrame内から親にアクセスできないので、
			//　ロード時にscriptContextに登録し、親側の処理を呼び出すよう変更
			//ルートウィンドウにダイアログの素を作成
//			var $dialogs = $(options.dialogs, document.rootWindow);
//			$dialog = $("<div class='modal-dialog' />").attr({id: "modal-dialog-" + name}).appendTo($dialogs);
//			$("<div class='modal-wrap sub-modal-wrap' />").appendTo($dialog);
//			var $under = $("<div class='modal-inner sub-modal-inner' />").appendTo($dialog);
//			var $title = $("<h2 class='hgroup-01' />").appendTo($under);
//			$("<span />").attr({id: "modal-title-" + name}).appendTo($title);
//			$("<p class='modal-close sub-modal-close' />").text("閉じる").appendTo($under);
//			var $frame = $("<iframe />").attr({name:name, src:"about:blank", height:"686", width:"100%", frameborder:"0"}).appendTo($under);
//			$(options.btn).subModalWindow();	//モーダルウィンドウ
//			$(options.lnk).subModalWindow();

			rootWindow.scriptContext["createModalFunction"].call(this, options.dialogs, name, function(){
				$(options.controls).subModalWindow();	//モーダルウィンドウ
			});
		});
	};
})(jQuery);

/**
 * モーダルウィンドウ
 *
 * [オプション]
 * overlay:	バックグランドコンテンツ
 * under:  モーダルコンテンツ
 *
 */
(function($){
	var $trigger = null;
	$.fn.modalWindow = function(option){
		var defaults = {
			overlay : '#modal-dialog-root .modal-wrap',
			under : '#modal-dialog-root .modal-inner',
			dialogHeight: 735,
			dialogWidth: 750,
			resizable: true
		};
		var options = $.extend(defaults, option);
		if (!this) return false;
		if ($("body.modal-body").length != 0) return false;
		return this.each(function(){
			var $this = $(this);
			var $document = $(document);
			var $window = $(window);
			var $overlay = $(options.overlay, document);
			var $under = $(options.under, document);
			var $frame = $("iframe", $under);

			$this.attr("targetName", $frame.attr("name"));
			var fade = {
				show : function() {
					$under.height(options.dialogHeight);
					$overlay.fadeIn(options.speed);
					$under.fadeIn(options.speed);
					scriptContext.overlayManager.addOverlay($overlay);
					$under.css({zIndex:scriptContext.overlayManager.nextZindex()});
				},
				hide : function() {
					$overlay.fadeOut(options.speed);
					$under.fadeOut(options.speed);
					$("iframe", $under).attr("src", "about:blank");
					$("#modal-title").text("");
					$(".modal-restore", $under).click();
					scriptContext.overlayManager.removeOverlay($overlay);
				}
			};
			$this.on("click", function(){
				//ダイアログを起動したものをトリガーとして保持しておき、
				//maximize,restoreから呼び出せるようにする。
				$trigger = $this;

				$under.removeClass("unresizable");
				if (options.resizable == false) {
					$under.addClass("unresizable");
				}

				fade.show();
				resizeHandler();
				$window.on("resize", resizeHandler);
			});
			//maximize,restoreで起動したtriggerにあわせてサイズ調整できるようfunction紐づけ
			$this.setModalWindowToCenter = setModalWindowToCenter;

			//メインのレイアウトから呼ばれるダイアログは共通のため、初期化は1回だけ
			if (!$under.attr("initialized")) {
				$under.on("click", ".modal-close", function(){
					//GemConfigServiceで編集画面でキャンセル時に確認ダイアログを表示する設定になってる場合確認を行う
					//⇒ダイアログ内画面のfunction「onDialogClose」で確認ダイアログをコントロール
					//  アプリ側は確認が必要な画面に同名のfunctionを入れておけば、ここからその関数が呼び出される
					if (typeof $frame.get(0).contentWindow.onDialogClose === "function" && !$frame.get(0).contentWindow.onDialogClose()) {
						return;
					}
					fade.hide();
					$frame.parents(".modal-dialog").trigger(new $.Event('closeModalDialog', {}));
				});
				$under.on("click", ".modal-maximize", function() {
					$under.addClass("fullWindow");
					//$thisと違って共通部分なので、複数のダイアログがいると最初の初期化時のものが実行されてしまう
					//⇒dialogHeigth:550を指定しても、先に730で指定してると730になってしまう
//					setModalWindowToCenter();
					//ダイアログを呼び出したトリガーに紐づくfunctionを呼び出し、
					//別のトリガーで指定したサイズにならないようにする
					$trigger.setModalWindowToCenter();
				});
				$under.on("click", ".modal-restore", function() {
					$under.removeClass("fullWindow");
//					setModalWindowToCenter();
					$trigger.setModalWindowToCenter();
				});
				$overlay.on("click", function(){
					//GemConfigServiceで編集画面でキャンセル時に確認ダイアログを表示する設定になってる場合確認を行う
					//⇒ダイアログ内画面のfunction「onDialogClose」で確認ダイアログをコントロール
					//  アプリ側は確認が必要な画面に同名のfunctionを入れておけば、ここからその関数が呼び出される
					if (typeof $frame.get(0).contentWindow.onDialogClose === "function" && !$frame.get(0).contentWindow.onDialogClose()) {
						return;
					}
					fade.hide();
					$frame.parents(".modal-dialog").trigger(new $.Event('closeModalDialog', {}));
				});
				$under.attr("initialized", true);
			}

			function resizeHandler(e){
				$overlay.width(0);
				var width = $window.width() > $document.width() ? $window.width() : $document.width();
				$overlay.css({
					height : $document.height(),
//					width : $window.width(),
					width : width,
					top: 0,
					left: 0,
					position:"absolute"
				});
				setModalWindowToCenter()
			}

			function setModalWindowToCenter(){
				if ($under.hasClass("fullWindow")) {
					var dialogHeight = $window.height() - 40;
					//frameはheader分減らす
					var frameHeight = dialogHeight - 49;

					$under.css({
						height : dialogHeight,
						width : $window.width() - 30,
						top: $document.scrollTop(),
						left: 0,
						marginLeft: 0
					});
					$frame.height(frameHeight);
				} else {
					var windowHeight = $window.height();
					var windowWidth = $window.width();

					var dialogHeight = options.dialogHeight;
					//windowの高さより大きい場合はwindowの高さに設定
					if (dialogHeight > (windowHeight -80)) {
						dialogHeight = windowHeight -80;
					}
					//最小高さを200
					if (dialogHeight < 200) {
						dialogHeight = 200;
					}
					//frameはheader分減らす
					var frameHeight = dialogHeight - 49;

					$under.css({
						height: dialogHeight,
						width: options.dialogWidth,
						top: $document.scrollTop() + 20,
						left: "auto",
						marginLeft:(windowWidth - options.dialogWidth - 30)/2
					});
					$frame.height(frameHeight);
				}
			}
		});
	};
})(jQuery);

/**
 * サブモーダルウィンドウ
 *
 * [オプション]
 * overlay:	バックグランドコンテンツ
 * under:  モーダルコンテンツ
 *
 */
(function($){
	$.fn.subModalWindow = function(option){
		var defaults = {
			overlay : 'body.modal-body .modal-wrap',
			under : 'body.modal-body .modal-inner',
			dialogHeight: 735,
			dialogWidth: 750,
			resizable: true
		};

		var rootWindow = document.rootWindow;
		var targetName = document.targetName;
		var $document = $(document);
		var $window = $(window);
		var $frame = $("iframe[name='" + targetName + "']", rootWindow);
		var $under = $frame.parent();
		var $overlay = $under.prev();
		var $dialog = $under.parent();

		var options = $.extend(defaults, option);
		if (!this) return false;
		var fade = {
			show : function() {
				$under.height(options.dialogHeight);
				$overlay.fadeIn(options.speed);
				$under.fadeIn(options.speed);
				rootWindow.scriptContext.overlayManager.addOverlay($overlay);
				$under.css({zIndex:rootWindow.scriptContext.overlayManager.nextZindex()});
			},
			hide : function() {
//				$overlay.fadeOut(options.speed);
//				$under.fadeOut(options.speed);
				$overlay.fadeOut(0);
				$under.fadeOut(0);
				$("iframe", $under).attr("src", "about:blank");
				$(".modal-restore", $under).click();
				rootWindow.scriptContext.overlayManager.removeOverlay($overlay);
				$under.css({zIndex:0});
			}
		};

		if (!$under.attr("initialized")) {
			$under.on("click", ".modal-close.sub-modal-close", function(){
				//GemConfigServiceで編集画面でキャンセル時に確認ダイアログを表示する設定になってる場合確認を行う
				//⇒ダイアログ内画面のfunction「onDialogClose」で確認ダイアログをコントロール
				//  アプリ側は確認が必要な画面に同名のfunctionを入れておけば、ここからその関数が呼び出される
				if (typeof $frame.get(0).contentWindow.onDialogClose === "function" && !$frame.get(0).contentWindow.onDialogClose()) {
					return;
				}
				fade.hide();
				$frame.parents(".modal-dialog").trigger(new $.Event('closeModalDialog', {}));
			});
			$under.on("click", ".modal-maximize.sub-modal-maximize", function(){
				var pw = $(rootWindow).width();
				$under.addClass("fullWindow");
				setModalWindowToCenter(pw);
			});
			$under.on("click", ".modal-restore.sub-modal-restore", function(){
				$under.removeClass("fullWindow");
				setModalWindowToCenter();
			});
			$overlay.on("click", function(){
				//GemConfigServiceで編集画面でキャンセル時に確認ダイアログを表示する設定になってる場合確認を行う
				//⇒ダイアログ内画面のfunction「onDialogClose」で確認ダイアログをコントロール
				//  アプリ側は確認が必要な画面に同名のfunctionを入れておけば、ここからその関数が呼び出される
				if (typeof $frame.get(0).contentWindow.onDialogClose === "function" && !$frame.get(0).contentWindow.onDialogClose()) {
					return;
				}
				fade.hide();
				$frame.parents(".modal-dialog").trigger(new $.Event('closeModalDialog', {}));
			});
			$under.attr("initialized", true);
		}

		return this.each(function(){
			var $this = $(this);

			$this.on("click", function(){
				$under.removeClass("unresizable");
				if (options.resizable == false) {
					$under.addClass("unresizable");
				}

				fade.show();
				resizeHandler();
				$window.on("resize", resizeHandler);
			});
		});

		function resizeHandler(){
			$overlay.width(0);
			$overlay.css({
				height : $(rootWindow).height(),//$document.height(),
				width : $(rootWindow).width(),//$window.width()
				top: 0,
				left: 0,
				position:"absolute"
			});
			setModalWindowToCenter()
		}

		function setModalWindowToCenter(pw){
			if ($under.hasClass("fullWindow")) {
				var pwd = rootWindow.scriptContext.getWindow();
				var dialogHeight = $(pwd).height() - 40;
				//frameはheader分減らす
				var frameHeight = dialogHeight - 49;

				$under.css({
					height : dialogHeight,
					width : $(pwd).width() - 30,
					top: $(pwd).scrollTop(),
					left: 0,
					marginLeft: 0
				});
				$frame.height(frameHeight);
			} else {
				var pwd = rootWindow.scriptContext.getWindow();
				var windowHeight = $(pwd).height();
				var windowWidth = pw != null ? pw : $(pwd).width();

				var dialogHeight = options.dialogHeight;
				//windowの高さより大きい場合はwindowの高さに設定
				if (dialogHeight > (windowHeight -80)) {
					dialogHeight = windowHeight -80;
				}
				//最小高さを200
				if (dialogHeight < 200) {
					dialogHeight = 200;
				}
				//frameはheader分減らす
				var frameHeight = dialogHeight - 49;

				$under.css({
					height: dialogHeight,
					width: options.dialogWidth,
					top: $(pwd).scrollTop() + 20,
					left:"auto",
					marginLeft:(windowWidth - options.dialogWidth - 30)/2
				});
				$frame.height(frameHeight);
			}
		}
	};
})(jQuery);

/**
 * 入力値(数値)に対してカンマ編集されたテキストをマスク表示する。
 * カンマ編集された値にフォーカスが当たったタイミングで元のテキストを表示する。
 */
(function($) {
	$.fn.commaField = function(){
		if (!this) return false;

		this.each(function(){
			var $this = $(this);
			var $parent = $this.closest(".property-data");

			//あらかじめ設定されてるイベント消しとく
			$this.removeAttr("onblur").off("blur");
			$this.on("blur", function () {
				var tb = this;
				var val = $(tb).val().trim();	//空白だけの場合にNaNになるので除去
				if (val.length == 0) {
					//未入力時はアラート出さない
					$(tb).val("");
					$(tb).removeClass("validate-error");
					if ($(".validate-error", $parent).length === 0) {
						$(".format-error", $parent).remove();
					}
					return true;
				}
				val = replaceAll(val, ",", "");
				if(isNaN(val)) {
					$(tb).addClass("validate-error");
					if ($(".format-error", $parent).length === 0) {
						var $p = $("<p />").addClass("error format-error").appendTo($parent);
						$("<span />").addClass("error").text(scriptContext.gem.locale.common.numcheckMsg).appendTo($p);
					}
					return true;
				}

				$(tb).removeClass("validate-error");
				if ($(".validate-error", $parent).length === 0) {
					$(".format-error", $parent).remove();
				}

				if (val != null && val != "") {
					////前後の0を消すためparse
					val = parseFloat(val);
				}
				//空白除去、parse結果の値を反映
				$(tb).val(val);

				var result = insertComma(val, separator);
				// スタイルをコピー
				var className = $(tb).attr("class");
				// 入力フォームを非表示に設定
				$(tb).hide();
				if ($(".dummyField", $(tb).parent()).length == 0) {
					//Chromeでブラウザの外にフォーカス当てると、戻ってきたときに挙動がおかしくなるので
					//表示用のフィールドがない場合だけ追加
					var $dummy = $("<input type='text' />").addClass(className).addClass("dummyField").val(result);
					$(tb).parent().prepend($dummy);

					$dummy.on("focus", function () {
						var df = this;
						//Chromeだとフォーカスイベントとキャレットが移動するタイミングが違うため
						//setTimeoutで0ミリ秒後に選択
						setTimeout(function() {
							let fillSelected = isFillSelected(df);
							let currentPosition = getPosition(df, separator);

							//IEの場合に画面がちらつくので先にダミーを消す
							$(df).remove();

							$(tb).show();

							if (fillSelected) {
								//全選択
								tb.select();
							} else {
								//カーソル位置指定
								setPosition(tb, currentPosition);
							}

							$(tb).trigger("focus");
						}, 0);
					});
				}
			});
		});
		//return this.blur();
		return this.trigger("blur");
	};

	/**
	 * カンマ編集.
	 * @param str 対象数値
	 * @return カンマ編集後の数値
	 */
	function insertComma(str, separator) {
		var reg = new RegExp(separator, "g");
		var num = new String(str).replace(reg, "");
		while (num != (num = num.replace(/^(-?\d+)(\d{3})/, "$1" + separator + "$2")));
		return num;
	}

	/**
	 * カンマフィールドが全選択されているかを取得
	 * @param item 表示用のテキスト
	 * @return 全選択状態
	 */
	function isFillSelected(item) {
		//selectionStartとselectionEndが利用可能な場合のみチェック
		if ("selectionStart" in item && "selectionEnd" in item) {
			let selectionText = item.value.substring(item.selectionStart, item.selectionEnd);
			return selectionText == item.value;
		}
		return false;
	}

	/**
	 * キャレットの位置を取得
	 * @param item 表示用のテキスト
	 * @return 表示用のテキストのキャレットの位置
	 */
	function getPosition(item, separator) {
		var currentPosition = 0;
		if (document.selection) {
			//旧IE
			var sel = document.selection.createRange();
			sel.moveStart("character", - item.value.length)
			currentPosition = sel.text.length;
		} else if ("selectionStart" in item) {
			currentPosition = item.selectionStart;
			if (item.value) {
				if ("selectionDirection" in item) {
					//IE右寄せ暫定対応（Chrome、Firefoxはforwardが返ってくる前提）
					if ($(item).css("text-align") == "right") {
						//IEで右寄せの場合、selectionStartが逆順で、かつ安定しないので先頭固定
						currentPosition = 0;
					}
				}
			}
		}
		if (item.value) {
			var str = item.value.substr(0, currentPosition);
			var startPosition = 0;
			var count = 0;
			while (str.indexOf(separator) != -1) {
				startPosition = str.indexOf(separator);
				str = str.substr(startPosition + 1);
				count++;
			}
			currentPosition -= count;//カンマの分だけマイナス
		}
		return currentPosition;
	}

	/**
	 * キャレットの位置を設定
	 * @param 編集用のテキスト
	 * @param キャレットの位置
	 */
	function setPosition(item, pos) {
		if ("setSelectionRange" in item) {
			item.focus();
			item.setSelectionRange(pos, pos);
		} else if (item.createTextRange) {
			//旧FireFox、Chrome
			var range = item.createTextRange();
			range.collapse(true);
			range.moveEnd("character", pos);
			range.moveStart("character", pos);
			range.select();
		}
	}
})(jQuery);

/**
 * 編集画面のラジオボタンの選択解除を可能にする。
 */
(function($) {
	var _radioValues = [];
	$.fn.togglableRadio = function(){
		if (!this) return false;

		this.each(function(){
			var $this = $(this);

			$this.on("click", function() {
				var _this = $(this);
				var _name = _this.attr('name');
				var _val  = _this.val();
				if (_radioValues[_name] === '' || _radioValues[_name] === null || _radioValues[_name] === undefined) {
					_radioValues[_name] = _val;
				} else {
					if (_radioValues[_name] == _val) {
						_this.prop('checked', false);
						_radioValues[_name] = '';
					} else {
						_radioValues[_name] = _val;
					}
				}
			});
			//初期値を保持
			if ($this.prop("checked")) {
				var _name = $this.attr('name');
				_radioValues[_name] = $this.val();
			}
		});
	};
})(jQuery);

/**
 * 検索条件開閉
 */
(function($){
	$.fn.switchCondition = function(option) {
		var defaults = {
				menu : '.tab-menu',
		 		panel : '.tab-panel',
				tabList : '.tabList-search-01',
				tabBox : '.box-search-01',
				cunt : 0
		};
		var options = $.extend(defaults, option);
		if (!this) return false;
		return this.each(function() {
			var $this = $(this);
			var $menu = $this.find(options.menu).children();
			var $panel = $this.find(options.panel);
			var $tabList = $this.find(options.tabList);
			var $tabBox = $this.find(options.tabBox);

			var _switch = {
				show: function() {
					$tabList.removeClass("contract");
					if (options.duration) {
						$tabBox.slideDown(options.duration, function() {
							$(".fixHeight").fixHeight();
						});
					} else {
						$tabBox.show();
					}
				},
				hide: function() {
					$tabList.addClass("contract");
					if (options.duration) {
						$tabBox.slideUp(options.duration, function() {
							$(".fixHeight").fixHeight();
						});
					} else {
						$tabBox.hide();
					}
				}
			};

			$menu.eq(options.cunt).addClass('current');
			$panel.not(':eq('+options.cunt+')').hide();
			$menu.on("click", function() {
				if ($(this).hasClass("current")) {
					if ($(this).hasClass("contractMenu")) {
						$(this).removeClass("contractMenu");
						_switch.show();
					} else {
						$(this).addClass("contractMenu");
						_switch.hide();
					}
				} else {
					$(this).siblings().removeClass('current').removeClass("contractMenu");
					_switch.show();
					$(this).addClass('current');
					var clickIndex = $menu.index(this);
					$panel.hide().eq(clickIndex).show();
				}
				$(".fixHeight").fixHeight();
				return false;
			});
			$tabList.on("click", function() {
				if ($(this).hasClass("contract")) {
					$menu.filter(".current").removeClass("contractMenu");
					_switch.show();
				} else {
					$menu.filter(".current").addClass("contractMenu");
					_switch.hide();
				}
				$(".fixHeight").fixHeight();
			});
		});
	};
})(jQuery);

/**
 * 大量データ用参照セクション
 */
(function($){
	$.fn.massReferenceTable = function(option){
		var defaults = {
				table: "table.massReferenceTable",
				pager: ".result-nav",
				btns: "div.mr-btn"
		};
		var options = $.extend(defaults, option);
		if (!this) return false;
		return this.each(function() {
			var $this = $(this);
			init($this, options);

			$this.getMassReference();
		});

		function init($v, options) {
			var $btns = $(options.btns, $v);
			var $pager = null;
			var isSubModal = $("body.modal-body").length != 0;
			$.extend($v, {
				oid: $v.attr("data-oid"),
				version: $v.attr("data-version"),
				defName: $v.attr("data-defName"),
				propName: $v.attr("data-propName"),
				viewName: $v.attr("data-viewName"),
				offset: $v.attr("data-offset") - 0,
				limit: $v.attr("data-limit") - 0,
				sortKey: $v.attr("data-sortKey"),
				sortType: $v.attr("data-sortType"),
				orgOutputType: $v.attr("data-orgOutputType"),
				editable: $v.attr("data-outputType") == "Edit",
				webapiName: $v.attr("data-webapiName"),
				removeWebapiName: $v.attr("data-removeWebapiName"),
				viewAction: $v.attr("data-viewAction"),
				viewActionCtrl: $v.attr("data-viewActionCtrl"),
				detailAction: $v.attr("data-detailAction"),
				detailActionCtrl: $v.attr("data-detailActionCtrl"),
				targetDefName: $v.attr("data-targetDefName"),
				mappedBy: $v.attr("data-mappedBy"),
				changeEditLinkToViewLink: $v.attr("data-changeEditLinkToViewLink") == "true",
				creatable: $v.attr("data-creatable") == "true",
				updatable: $v.attr("data-updatable") == "true",
				deletable: $v.attr("data-deletable") == "true",
				showPaging: $v.attr("data-showPaging") == "true",
				showPageJump: $v.attr("data-showPageJump") == "true",
				showPageLink: $v.attr("data-showPageLink") == "true",
				showCount: $v.attr("data-showCount") == "true",
				showSearchBtn: $v.attr("data-showSearchBtn") == "true",
				elementId: $v.attr("data-elementId"),
				tokenValue: $v.attr("data-tokenValue"),
				purge: $v.attr("data-purge"),
				entityOid: $v.attr("data-entityOid"),
				entityVersion: $v.attr("data-entityVersion"),
				setTableId: function(table) {
					$(table).each(function() {
						var index = 0, prefix = "massReferenceTable_";
						while (true) {
							if ($("#" + prefix + es($v.propName) + index).length == 0) {
								$(this).attr("id", prefix + $v.propName + index);
								break;
							} else { index++; }
						}
					});
				},
				clear: function() {
					if ($v.grid) {
						$v.grid.clearGridData(true);
					}
				},
				getMassReference: function() {
					$v.clear();

					var $table = $(options.table, $v);

					if ($table.length == 0) return this;

					if (!$table.build) {
						$table.build = function(dispInfo, count, list) {
							var colNames = new Array();
							var colModel = new Array();
							colNames.push("oid");
							colModel.push({name:"orgOid", index:"orgOid", sortable:false, hidden:true, formatter:oidCellFormatter});
							colNames.push("version");
							colModel.push({name:"orgVersion", index:"orgVersion", sortable:false, hidden:true});
							colNames.push("");
							colModel.push({name:'_mtpDetailLink', index:'_mtpDetailLink', sortable:false, align:'center', width:60, classes:"detail-links"});
							for (var i = 0; i < dispInfo.length; i++) {
								colNames.push("<p class='title'>" + dispInfo[i].displayName + "</p>");
								var cm = {name:dispInfo[i].name, index:dispInfo[i].name};
								if (dispInfo[i].width > 0) cm.width = dispInfo[i].width;
								if (dispInfo[i].hide === true) cm.hidden = true;
								colModel.push(cm);
							}

							$v.setTableId(this);

							var $self = $(this);
							var grid = $self.jqGrid({
								datatype: "local",
								autoencode: false,
								height: "auto",
								colNames: colNames,
								colModel: colModel,
								headertitles: true,
								multiselect: $v.editable && $v.deletable,
								caption: "MassReferenceTable",
								viewrecords: true,
								altRows: true,
								altclass:'myAltRowClass',
								onSortCol: function(index, iCol, sortorder) {
									$v.sortKey = index;
									$v.sortType = sortorder.toUpperCase();
									$v.getMassReference();
									$("tr.ui-jqgrid-labels th:eq(" + iCol + ") .ui-jqgrid-sortable", $v).removeClass('asc desc').addClass(sortorder.toLowerCase());
									return "stop";
								}
							});

							return grid;
						};
					}

					if ($table.length > 0) {

						//参照プロパティ取得
						getMassReferenceData($v.webapiName, $v.oid, $v.defName, $v.propName, $v.viewName, $v.offset, $v.sortKey, $v.sortType, $v.showCount, $v.orgOutputType, $v.elementId, $v.entityOid, $v.entityVersion, function(dispInfo, count, list) {
							//テーブル作成
							if (!$v.grid) {
								$v.grid = $table.build(dispInfo, count, list);
							}

							//リンク生成
							var $detailLink = $("<p/>");
							var $viewLink = $("<a/>").attr("href","javascript:void(0)").appendTo($detailLink);
							if ($v.editable && $v.updatable && !$v.changeEditLinkToViewLink) {
								$viewLink.addClass("lnk-mr-01").text(scriptContext.gem.locale.reference.edit);
							} else {
								$viewLink.addClass("lnk-mr-02").text(scriptContext.gem.locale.reference.detail);
							}

							//データセット
							$(list).each(function(index) {
								$viewLink.attr({"data-oid":this.orgOid, "data-version":this.orgVersion});

								this["id"] = this.orgOid + "_" + this.orgVersion;
								this["_mtpDetailLink"] = $detailLink.html();
								$v.grid.addRowData(index + 1, this);
							});

							//ダイアログのトリガー設定
							var isSubModal = $("body.modal-body").length != 0;
							if (isSubModal) {
								$(".modal-lnk", $table).subModalWindow();
							} else {
								$(".modal-lnk", $table).modalWindow();
							}

							//リンクのイベント設定
							if (isSubModal) {
								$(".lnk-mr-01", $v).on("click", function(e) {
									if(e.ctrlKey) {
										editReferenceCtrl(this);
										e.stopImmediatePropagation();
									} else {
										editReference(this);
									}
								}).subModalWindow();
								$(".lnk-mr-02", $v).on("click", function(e) {
									if(e.ctrlKey) {
										viewReferenceCtrl(this);
										e.stopImmediatePropagation();
									} else {
										viewReference(this);
									}
								}).subModalWindow();
							} else {
								$(".lnk-mr-01", $v).on("click", function(e) {
									if(e.ctrlKey) {
										editReferenceCtrl(this);
										e.stopImmediatePropagation();
									} else {
										editReference(this);
									}
								}).modalWindow();
								$(".lnk-mr-02", $v).on("click", function(e) {
									if(e.ctrlKey) {
										viewReferenceCtrl(this);
										e.stopImmediatePropagation();
									} else {
										viewReference(this);
									}
								}).modalWindow();
							}

							//ページング処理
							if ($pager) {
								$pager.setPage($v.offset, list.length, count);
							}
						});
					}
				}
			});

			//ページングリンクのイベント処理
			//「ページングを非表示」「検索アイコンを常に表示」両方チェック⇒検索アイコンは表示
			if (($v.showPaging || $v.showSearchBtn) && $v.limit != null && $v.limit != "") {
				var $pager = $(options.pager, $v).pager({
					limit: $v.limit,
					showPageLink: $v.showPaging && $v.showCount ? $v.showPageLink : false,
					showPageJump: $v.showPaging && $v.showCount ? $v.showPageJump : false,
					showPrev: $v.showPaging,
					showNext: $v.showPaging,
					showSearchBtn: $v.showSearchBtn,
					showItemCount: $v.showPaging && $v.showCount,
					previewFunc: function(){
						$v.offset -= $v.limit;
						$v.getMassReference();
					},
					nextFunc: function() {
						$v.offset += $v.limit;
						$v.getMassReference();
					},
					searchFunc: function(currentPage) {
						$v.offset = currentPage * $v.limit;
						$v.getMassReference();
					}
				});
			}

			if ($v.editable) {
				$btns.show();
				//追加ボタンのイベント処理
				if ($v.creatable) {
					var $add = $(".btn-mr-01", $btns).on("click", function() {

						var target = getModalTarget(isSubModal);

						//追加(=編集)ダイアログでの保存時は、追加したデータの詳細ダイアログを表示
						document.scriptContext["editReferenceCallback"] = function(entity) {
							$v.getMassReference();

							var viewAction = $v.viewAction + "/" + encodeURIComponent(entity.oid);
							var $form = $("<form />").attr({method:"POST", action:contextPath + "/" + viewAction, target:target}).appendTo("body");
							$("<input />").attr({type:"hidden", name:"oid", value:entity.oid}).appendTo($form);
							$("<input />").attr({type:"hidden", name:"version", value:entity.version}).appendTo($form);
							$("<input />").attr({type:"hidden", name:"refEdit", value:$v.updatable}).appendTo($form);
							if (isSubModal) $("<input />").attr({type:"hidden", name:"modalTarget", value:target}).appendTo($form);
							$form.submit();
							$form.remove();
						}

						var mappedByKey = $v.oid + "_" + $v.version;
						var $form = $("<form />").attr({method:"POST", action:contextPath + "/" + $v.detailAction, target:target}).appendTo("body");
						$("<input />").attr({type:"hidden", name:"updateByParam", value:true}).appendTo($form);
						$("<input />").attr({type:"hidden", name:$v.mappedBy, value:mappedByKey}).appendTo($form);
						if (isSubModal) $("<input />").attr({type:"hidden", name:"modalTarget", value:target}).appendTo($form);
						$form.submit();
						$form.remove();
					});
					if (isSubModal) {
						$add.subModalWindow();
					} else {
						$add.modalWindow();
					}
				}

				//削除ボタンのイベント設定
				if ($v.deletable) {
					$(".btn-mr-02", $btns).on("click", function() {
						var ids = $v.grid.getGridParam("selarrrow");
						if(ids.length <= 0) {
							return;
						}
						var key = [];
						for (var i = 0; i < ids.length; ++i) {
							var id = ids[i];
							var row = $v.grid.getRowData(id);
							key.push(row.orgOid + "_" + row.orgVersion);
						}
						removeMappedByReference($v.removeWebapiName, $v.oid, $v.defName, $v.viewName, $v.propName, key, $v.tokenValue, $v.purge, function(errors, _t) {

							$v.tokenValue = _t;

							if (errors == null || erros.length == 0) {
								$v.getMassReference();
							} else {
								alert(errors);
							}
						});
					});
				}
			}

			//詳細ダイアログ表示
			function viewReference(src) {

				var target = getModalTarget(isSubModal);

				//詳細ダイアログ⇒編集ダイアログでの保存時は、再度詳細ダイアログを表示
				document.scriptContext["editReferenceCallback"] = function(entity) {
					$v.getMassReference();

					var viewAction = $v.viewAction + "/" + encodeURIComponent(entity.oid);
					var $form = $("<form />").attr({method:"POST", action:contextPath + "/" + viewAction, target:target}).appendTo("body");
//					$("<input />").attr({type:"hidden", name:"defName", value:$v.targetDefName}).appendTo($form);
//					$("<input />").attr({type:"hidden", name:"oid", value:entity.oid}).appendTo($form);
					$("<input />").attr({type:"hidden", name:"version", value:entity.version}).appendTo($form);
					$("<input />").attr({type:"hidden", name:"refEdit", value:$v.updatable}).appendTo($form);
					if (isSubModal) $("<input />").attr({type:"hidden", name:"modalTarget", value:target}).appendTo($form);
					$form.submit();
					$form.remove();
				}

				var viewAction = $v.viewAction + "/" + encodeURIComponent($(src).attr("data-oid"));
				var $form = $("<form />").attr({method:"POST", action:contextPath + "/" + viewAction, target:target}).appendTo("body");
//				$("<input />").attr({type:"hidden", name:"defName", value:$v.targetDefName}).appendTo($form);
//				$("<input />").attr({type:"hidden", name:"oid", value:$(src).attr("oid")}).appendTo($form);
				$("<input />").attr({type:"hidden", name:"version", value:$(src).attr("data-version")}).appendTo($form);
				$("<input />").attr({type:"hidden", name:"refEdit", value:$v.updatable}).appendTo($form);
				if (isSubModal) $("<input />").attr({type:"hidden", name:"modalTarget", value:target}).appendTo($form);
				$form.submit();
				$form.remove();
			}

			//編集ダイアログ表示⇒編集ダイアログでの保存時は、再度詳細ダイアログを表示
			function editReference(src, action) {
				//編集ダイアログでの保存時は、ダイアログ閉じる
				document.scriptContext["editReferenceCallback"] = function(entity) {
					$v.getMassReference();

					var viewAction = $v.viewAction + "/" + encodeURIComponent(entity.oid);
					var $form = $("<form />").attr({method:"POST", action:contextPath + "/" + viewAction, target:target}).appendTo("body");
//					$("<input />").attr({type:"hidden", name:"defName", value:$v.targetDefName}).appendTo($form);
					$("<input />").attr({type:"hidden", name:"oid", value:entity.oid}).appendTo($form);
					$("<input />").attr({type:"hidden", name:"version", value:entity.version}).appendTo($form);
					$("<input />").attr({type:"hidden", name:"refEdit", value:$v.updatable}).appendTo($form);
					if (isSubModal) $("<input />").attr({type:"hidden", name:"modalTarget", value:target}).appendTo($form);
					$form.submit();
					$form.remove();
				}

				var target = getModalTarget(isSubModal);
				var $form = $("<form />").attr({method:"POST", action:contextPath + "/" + $v.detailAction, target:target}).appendTo("body");
//				$("<input />").attr({type:"hidden", name:"defName", value:$v.targetDefName}).appendTo($form);
				$("<input />").attr({type:"hidden", name:"oid", value:$(src).attr("data-oid")}).appendTo($form);
				$("<input />").attr({type:"hidden", name:"version", value:$(src).attr("data-version")}).appendTo($form);
				$("<input />").attr({type:"hidden", name:"refEdit", value:$v.updatable}).appendTo($form);
				if (isSubModal) $("<input />").attr({type:"hidden", name:"modalTarget", value:target}).appendTo($form);
				$form.submit();
				$form.remove();
			}

			//詳細を別タブ表示
			function viewReferenceCtrl(src) {
				var viewActionCtrl = $v.viewActionCtrl + "/" + encodeURIComponent($(src).attr("data-oid"));
				var $form = $("<form />").attr({method:"POST", action:contextPath + "/" + viewActionCtrl, target:"_blank"}).appendTo("body");
				$("<input />").attr({type:"hidden", name:"version", value:$(src).attr("data-version")}).appendTo($form);
				$("<input />").attr({type:"hidden", name:"refEdit", value:$v.updatable}).appendTo($form);
				$form.submit();
				$form.remove();
			}

			//編集を別タブ表示
			function editReferenceCtrl(src) {
				var $form = $("<form />").attr({method:"POST", action:contextPath + "/" + $v.detailActionCtrl, target:"_blank"}).appendTo("body");
				$("<input />").attr({type:"hidden", name:"oid", value:$(src).attr("data-oid")}).appendTo($form);
				$("<input />").attr({type:"hidden", name:"version", value:$(src).attr("data-version")}).appendTo($form);
				$("<input />").attr({type:"hidden", name:"refEdit", value:$v.updatable}).appendTo($form);
				$form.submit();
				$form.remove();
			}

		};
	};
})(jQuery);

/**
 * ページャー
 */
(function($) {
	$.fn.pager = function(option) {
		var defaults = {
				limit: 10,
				showPageLink: true,
				pageLinksNum: 10,
				showPageJump: true,
				showItemCount: true,
				showNoPage: true,
				showCurrentPage: false,
				showPrev: true,
				showNext: true,
				showSearchBtn: false,
				previewFunc: null,
				nextFunc: null,
				searchFunc: null,
				pagingInputErrorFunc: null,
				previousLabel: scriptContext.gem.locale.pager.previous,
				nextLabel: scriptContext.gem.locale.pager.next
		};
		var options = $.extend(defaults, option);
		if (!this) return false;
		var ret = this.each(function() {
			var $this = $(this);
			init($this, options);

			//jQueryオブジェクトのメソッドをjavascriptオブジェクトで利用する為、内部メソッドをバインド
			var methods = ["setPage", "lock", "unlock"];
			for (var i = 0; i < methods.length; i++) {
				_bindMethod($this, this, methods[i]);
			}
			function _bindMethod(obj,to,method){
				to[method]=function(){
					return obj[method].apply(obj,arguments);
				};
			}
		});

		ret.setPage = function(offset, length, count) {
			$(this).each(function() {
				this.setPage(offset, length, count);
			});
		};

		ret.lock = function() {
			$(this).each(function() {
				this.lock(this);
			});
		};

		ret.unlock = function() {
			$(this).each(function() {
				this.unlock(this);
			});
		};

		return ret;

		function init($v, options) {
			var limit = options.limit;
			if (limit == 0) limit = 10;
			var pageLinksNum = options.pageLinksNum;
			if (pageLinksNum == 0) pageLinksNum = 10;

			var $ul = $("<ul />").css("white-space", "nowrap").appendTo($v);

			//前へ
			if (options.showPrev) {
				var $preview = $("<li />").addClass("preview").appendTo($ul);
				createLink($preview, options.previousLabel);
			}

			//次へ
			if (options.showNext) {
				var $next = $("<li />").addClass("next").appendTo($ul);
				createLink($next, options.nextLabel);
			}

			//入力エリアと検索ボタン
			var $current = null;
			if (options.showPageJump) {
				var $quickJump = $("<li />").addClass("quick-jump").appendTo($ul);
				$current = $("<input />").addClass("current-page").attr({type:"text", maxlength:7, size:2}).appendTo($quickJump);
				$("<span />").text(" / ").appendTo($quickJump);
				var $max = $("<span />").addClass("last-page").appendTo($quickJump);
				$("<span />").text(scriptContext.gem.locale.pager.page).appendTo($quickJump);
			}

			if (options.showSearchBtn || options.showPageJump) {
				var $btns = $("<li />").addClass("search").appendTo($ul);
				var $searchBtn = $("<span />").addClass("ui-icon ui-icon-search").appendTo($btns);
				if ($current == null) {
					//showPageJumpがfalseの場合
					$current = $("<input type='hidden' />").addClass("current-page").appendTo($btns);
				}
			}

			var $currentLabel = null;
			if (options.showCurrentPage) {
				var $currentPage = $("<li />").addClass("mr05").appendTo($ul);
				$currentLabel = $("<span />").addClass("mr05").appendTo($currentPage);
				$("<span />").text(scriptContext.gem.locale.pager.page).appendTo($currentPage);
			}

			//ページリンク
			if (options.showPageLink) {
				var $pageLinks = $("<li />").addClass("page-links").appendTo($ul);
				var $linkList = $("<ul />").appendTo($pageLinks);
				$pageLinks.createLinks = function(current, max) {
					$linkList.children().remove();
					var precount, nextcount;
					if (pageLinksNum % 2 == 1) {
						//奇数
						precount = (pageLinksNum - 1) / 2;
						nextcount = (pageLinksNum - 1) / 2;
					} else {
						//偶数
						precount = pageLinksNum / 2;
						nextcount = pageLinksNum / 2 - 1;
					}

					//リンクの最初
					var start = current - precount < 0 ? 0 : current - precount;
					//リンクの最後
					var end = current + nextcount >= max ? max : current + nextcount + 1;
					if (max > pageLinksNum) {
						if (current + nextcount < pageLinksNum) {
							end = pageLinksNum;
						}
						if (current - precount > max - pageLinksNum) {
							start = max - pageLinksNum;
						}
					} else {
						start = 0;
						end = max;
					}

					if (max > 0) {
						for (var i = start; i < current; i++) {
							addPagenationLink(i);
						}
						$("<li />").addClass("current").text(current + 1).appendTo($linkList);
					} else {
						$("<li />").addClass("current").text("").appendTo($linkList);
					}

					var next = current + 1 >= max ? max : current + 1;
					for (var i = next; i < end; i++) {
						addPagenationLink(i);
					}

					if (start > 1) {
						$("<li />").text("...").prependTo($linkList);
					}
					if (start > 0) {
						var $link = $("<li />").prependTo($linkList);
						$("<a />").attr({href:"javascript:void(0)", offset:0}).text(1).appendTo($link);
					}

					if (end < max - 1) {
						$("<li />").text("...").appendTo($linkList);
					}
					if (end < max) {
						var $link = $("<li />").appendTo($linkList);
						$("<a />").attr({href:"javascript:void(0)", offset:max - 1}).text(max).appendTo($link);
					}

					$("a", $linkList).on("click", function() {
						if ($v.lockStatus === true) {
							return false;
						}

						var offset = $(this).attr("offset");
						if ($v.searchFunc && $.isFunction($v.searchFunc)) {
							$v.searchFunc.call(this, offset);
						}
					});

					function addPagenationLink(offset) {
						var $link = $("<li />").appendTo($linkList);
						$("<a />").attr({href:"javascript:void(0)", offset:offset}).text(offset + 1).appendTo($link);
					}
				}
			}

			//件数
			if (options.showItemCount) {
				var $resultNum = $("<li />").addClass("result-num").appendTo($ul);
				var $range = $("<span />").addClass("range").appendTo($resultNum);
				var $count = $("<span />").addClass("count").text(0).appendTo($resultNum);
				$("<span />").text(scriptContext.gem.locale.pager.count).appendTo($resultNum);
			}

			$.extend($v, {
				previewFunc: options.previewFunc,
				nextFunc: options.nextFunc,
				searchFunc: options.searchFunc,
				pagingInputErrorFunc: options.pagingInputErrorFunc,
				lockStatus: false,
				lock: function() {
					this.lockStatus = true;
				},
				unlock: function() {
					this.lockStatus = false;
				},
				setPage: function(offset, length, count) {
					//件数
					var tail = offset + limit;
					var hasPreview;
					var hasNext;
					var notCount = typeof count === "undefined" || count == null;
					if (notCount) {
						hasPreview = offset > 0;
						hasNext = limit == length;
					} else {
						hasPreview = offset > 0 && count > 0;
						hasNext= limit < (count - offset);
						if (tail > count) { tail = count; }
					}

					if (!options.showNoPage && !hasPreview && !hasNext) {
						$v.hide();
						return;
					}

					if (hasPreview) {
						$(".clickable", $preview).show();
						$(".unclickable", $preview).hide();
					} else {
						$(".clickable", $preview).hide();
						$(".unclickable", $preview).show();
					}
					if (hasNext) {
						$(".clickable", $next).show();
						$(".unclickable", $next).hide();
					} else {
						$(".clickable", $next).hide();
						$(".unclickable", $next).show();
					}

					//ページ数
					var currentPage = (offset / limit) + 1;
					if (!notCount) {
						var pages = count % limit;
						var maxPage = (count - pages) / limit;
						if (pages > 0) maxPage++;

						if (options.showPageLink) {
							$pageLinks.createLinks(currentPage - 1, maxPage);
						}

						if (count > 0) {
							if (options.showItemCount) {
								if (offset + 1 > tail) {
									$range.css("display","").text("0" + scriptContext.gem.locale.pager.count + " / ");
								} else {
									$range.css("display","").text((offset + 1) + " - " + tail + scriptContext.gem.locale.pager.count + " / ");
								}
							}
							if (options.showPageJump) {
								$quickJump.show();
								$searchBtn.show();
								$max.text(maxPage);
								$v.maxPage = maxPage;
								$current.attr("beforeValue", currentPage);
							}
							if (options.showSearchBtn || options.showPageJump) {
								$btns.show();
							}
							if (options.showPageLink) {
								$pageLinks.show();
							}
						} else {
							if (options.showItemCount) {
								$range.css("display","none").text("");
							}
							if (options.showPageJump) {
								$quickJump.hide();
							}
							if (options.showSearchBtn || options.showPageJump) {
								$searchBtn.hide();
								$btns.hide();
							}
							if (options.showPageLink) {
								$pageLinks.hide();
							}
						}
						if (options.showItemCount) {
							$count.text(count);
						}
					}
					if (options.showSearchBtn || options.showPageJump) {
						$current.val(currentPage);
					}
					if (options.showCurrentPage) {
						$currentLabel.text(currentPage);
					}
				}
			});

			$("a", $preview).on("click", function() {
				if ($v.lockStatus === true) {
					return false;
				}

				if ($v.previewFunc && $.isFunction($v.previewFunc)) {
					$v.previewFunc.call(this, limit);
				}
			});
			$("a", $next).on("click", function() {
				if ($v.lockStatus === true) {
					return false;
				}

				if ($v.nextFunc && $.isFunction($v.nextFunc)) {
					$v.nextFunc.call(this, limit);
				}
			});

			if (options.showPageJump) {
				$current.css("ime-mode", "disabled").on("change", function() {
					var v = Number($(this).val());
					if (!isValidNumber(v)) {
						if ($v.pagingInputErrorFunc && $.isFunction($v.pagingInputErrorFunc)) {
							$v.pagingInputErrorFunc(this);
						} else {
							alert(scriptContext.gem.locale.common.numcheckMsg);
						}
						if ($(this).attr("beforeValue")) {
							$(this).val($(this).attr("beforeValue"));
						} else {
							$(this).val("");
						}
					}
				}).on("keypress", function(event) {
					if ($v.lockStatus === true) {
						return false;
					}

					if( event.which === 13 ){
						$searchBtn.click();
					}
				});
			}

			if (options.showSearchBtn || options.showPageJump) {
				$searchBtn.on("mouseenter", function() {
					$(this).addClass("hover");
				}).on("mouseleave", function() {
					$(this).removeClass("hover");
				}).on("click", function() {
					if ($v.lockStatus === true) {
						return false;
					}

					var currentPage = $current.val();
					//notCount=trueかつshowSearchBtn=trueの場合、maxPageが設定されてません。
					if (!$v.maxPage || $v.maxPage && currentPage > 0 && currentPage <= $v.maxPage) {
						if ($v.searchFunc && $.isFunction($v.searchFunc)) {
							$v.searchFunc.call(this, currentPage - 1);
						}
					}
				});
			}

			function createLink($parent, label) {
				var $clickable = $("<span />").addClass("clickable").appendTo($parent);
				var $link = $("<a />").attr("href", "javascript:void(0)").text(label).appendTo($clickable);
				var $unclickable = $("<span />").addClass("unclickable").text(label).appendTo($parent);
			}

			function isValidNumber(value) {
				if (isNaN(value)) {
					// 数字変換不可
					return false;
				}
				
				if (!$v.maxPage) {
					// maxPage未設定
					return false;
				}
				
				var num = value - 0;
				if (num < 1) {
					// 0以下値入力
					return false;
				}
				if (num > $v.maxPage) {
					// maxPageを超える
					return false;
				}
				
				return true;
			}
		}
	};
})(jQuery);

/**
 * 検索結果一覧ウィジェット
 */
(function($){
	$.fn.nameList = function(option){
		var defaults = {
			linkList:"ul.list-entity-name",
			all:"li.list-view a"
		};
		var options = $.extend(defaults, options);
		if (!this) return false;
		return this.each(function(){
			var $this = $(this);
			var defName = $this.attr("data-defName");
			var viewName = $this.attr("data-viewName");
			var filterName = $this.attr("data-filterName");
			var prevLabel = $this.attr("data-prevLabel");
			var nextLabel = $this.attr("data-nextLabel");
			var limit = $this.attr("data-limit") - 0;
			var offset = 0;

			var $linkList = $(options.linkList, $this);
			var $all = $(options.all, $this);

			//ページング
			var $pager = $(".result-nav", $this).pager({
				limit: limit,
				showPageLink: false,
				showPageJump: false,
				showItemCount: false,
				previewFunc: function(){
					offset -= limit;
					search();
				},
				nextFunc: function() {
					offset += limit;
					search();
				},
				previousLabel: prevLabel,
				nextLabel: nextLabel
			});

			//一覧へのリンク
			$all.on("click", function() {
				clearMenuState();

				var searchViewAction = $(this).attr("data-searchViewAction");
				var params = {searchCond:"&searchType=normal"};
				submitForm(contextPath + "/" + searchViewAction , params);
			});

			//検索
			var webapiName = $linkList.attr("data-webapiName");
			var viewAction = $linkList.attr("data-viewAction");

			search();

			function search() {
				searchNameList(webapiName, defName, viewName, filterName, offset, function(count, list) {
					$pager.setPage(offset, list.length, count);

					//一覧にリンク再作成
					$linkList.children().remove();
					$(list).each(function() {
						var name = this.name;
						var oid = this.oid;
						var version = this.version;
						var $li = $("<li />").appendTo($linkList);
						$("<a />").text(name).appendTo($li).attr({"href":"javascript:void(0)"}).on("click", function(e) {
							clearMenuState();
							if (e.ctrlKey) {
								showDetail(viewAction, oid, version, false, "_blank", {});
							} else {
								showDetail(viewAction, oid, version, false, null, {});
							}
							return false;
						});
					});
				});
			}
		});
	};
})(jQuery);

/**
 * 参照コンボ
 */
(function($){
	$.fn.refCombo = function(option) {
		var defaults = {
			reset:false,
			oid:""
		};
		var options = $.extend(defaults, option);
		if (!this) return false;

		return this.each(function(){
			var $this = $(this);

			var props = new Array();

			if (!options.reset) {
				init($this, options, props);
			} else {
				reset($this, options, props);
			}
		});

		/*
		 * 初期化処理
		 */
		function init($v, options, props) {
			setup($v);

			var pleaseSelectLabel = "";
			if (scriptContext.gem.showPulldownPleaseSelectLabel === true) {
				pleaseSelectLabel = scriptContext.gem.locale.common.pleaseSelect;
			}
			$("<option />").attr({value:""}).text(pleaseSelectLabel).appendTo($v);

			getPropertyEditor($v.getEditorWebapiName, $v.defName, $v.viewName, $v.propName, $v.viewType, $v.entityOid, $v.entityVersion, function(editor) {
				var setting = editor.referenceComboSetting;

				//連動コンボの設定がない場合は、連動できないので表示しない
				if (!setting || !setting.propertyName || setting.propertyName == "") return;

				//最後にコンボを選択するためのコールバック関数
				var func = null;
				if ($v.oid != "") {
					func = function() { $v.val($v.oid); }
				}
				//親コンボ生成
				initParent($v, $v.propName, $v.oid, setting, props, false, func);

				if ($v.oid == "") {
					if ($v.upperOid == "") {
						$v.siblings("select:first").change();
					} else {
						$v.siblings("select[name='" + $v.upperName + "']").change();
					}
				}
			});

			if ($v.searchType == "ALERT") {
				//common.js
				addNormalValidator(function() {
					if ($v.val() == "" && $v.siblings("select").children(":selected[value!='']").length > 0) {
						alert(scriptContext.gem.locale.reference.specifyCondition);
						$v.focus();
						return false;
					}
					return true;
				});
			}
		}

		/**
		 * リセット処理
		 */
		function reset($v, options, props) {

			$v.attr("data-oid", options.oid); //optionで指定されたOIDをセット
			$v.attr("data-upperName", "");    //詳細画面から戻ってきた場合に設定されている可能性があるのでクリア
			$v.attr("data-upperOid", "");     //詳細画面から戻ってきた場合に設定されている可能性があるのでクリア

			setup($v);

			//全要素の初期化(先頭は要素を消す必要はないが、initParentで再生成されるのでクリア)
			$v.parent().children("select").val("");
			//$v.parent().children("select:not(:first)").each(function(){
			$v.parent().children("select").each(function(){
				$(this).children("option[value!='']").remove();
			});

			getPropertyEditor($v.getEditorWebapiName, $v.defName, $v.viewName, $v.propName, $v.viewType, $v.entityOid, $v.entityVersion, function(editor) {
				var setting = editor.referenceComboSetting;

				//連動コンボの設定がない場合は、連動できないので表示しない
				if (!setting || !setting.propertyName || setting.propertyName == "") return;

				//最後にコンボを選択するためのコールバック関数
				var func = null;
				if ($v.oid != "") {
					func = function() { $v.val($v.oid); }
				}

				//親コンボリセット
				initParent($v, $v.propName, $v.oid, setting, props, true, func);

				if ($v.oid == "") {
					/*
					if ($v.upperOid == "") {
						$v.siblings("select:first").change();
					} else {
						$v.siblings("select[name='" + $v.upperName + "']").change();
					}
					*/
					//リセット時はupperは未指定状態のため$v.oidが空なら先頭のchange()を実行
					$v.siblings("select:first").change();
				}
			});
		}

		function setup($v) {
			//一括設定の場合、option指定できないのでattributeから取得
			$.extend($v, {
				propName:$v.attr("data-propName"),
				defName:$v.attr("data-defName"),
				viewName:$v.attr("data-viewName"),
				webapiName:$v.attr("data-webapiName"),
				getEditorWebapiName:$v.attr("data-getEditorWebapiName"),
				searchParentWebapiName:$v.attr("data-searchParentWebapiName"),
				viewType:$v.attr("data-viewType"),
				oid:$v.attr("data-oid"),
				prefix:$v.attr("data-prefix"),
				searchType:$v.attr("data-searchType"),
				upperName:$v.attr("data-upperName"),
				upperOid:$v.attr("data-upperOid"),
				customStyle:$v.attr("data-customStyle"),
				entityOid:$v.attr("data-entityOid"),
				entityVersion:$v.attr("data-entityVersion")
			});
		}

		/*
		 * コンボ毎の初期化処理
		 * データの読み込み順(詳細編集や検索画面の復元時)
		 * ------------------------------------------------------------------------------------------
		 * 1.最下層の一個上のEntity取得(最下層のOIDが条件)
		 * 2.その上の階層のEntity取得(1のOIDが条件)
		 *   ・・・
		 * 3.一番上の階層のEntity取得(2のOIDが条件)
		 * ------------------------------------------------------------------------------------------
		 * 4.一番上の選択データ取得(条件なし)、3で取得した自身の階層を選択状態にする
		 * 5.その下の階層の選択データ取得(4のOIDが条件)、1～2で取得した自身の階層を選択状態にする
		 *   ・・・
		 * 6.最下層の選択データ取得(5のOIDが条件)、最下層を選択状態にする
		 * ------------------------------------------------------------------------------------------
		 */
		function initParent($v, name, childOid, setting, props, reset, topCallback) {
			var parentName = name + "." + setting.propertyName;
			props.push(parentName);
			if (childOid == "") {
				var parentOid = "";
				var func = null;
				if (parentName == $v.upperName && $v.upperOid != "") {
					parentOid = $v.upperOid;
					func = function($parent) {
						$parent.val(parentOid);
					}
				}
				if (setting.parent && setting.parent.propertyName && setting.parent.propertyName != "") {
					//更に上位がいれば先に生成
					initParent($v, parentName, parentOid, setting.parent, props, reset, null);
				}

				createNode($v, parentName, props, reset, func);
			} else {
				//子から親を検索
				searchParent($v.searchParentWebapiName, $v.defName, $v.viewName, $v.propName, $v.viewType, parentName, childOid, $v.entityOid, $v.entityVersion, function(parentEntity) {
					var parentOid = getOid(parentEntity);
					if (setting.parent && setting.parent.propertyName && setting.parent.propertyName != "") {
						//更に上位がいれば先に生成
						initParent($v, parentName, parentOid, setting.parent, props, reset, null);
					}

					var func = null;
					if (parentOid != "") {
						//階層を選択済みにする場合は階層データを読み込ませた後に選択対象指定
						func = function($parent) {
							$parent.val(parentOid);

							//最下層の一個上の場合は最下層を読み込んで選択状態を設定する
							if (topCallback && $.isFunction(topCallback)) {
								$parent.change();
								topCallback.call(this);
							}
						}
					}
					createNode($v, parentName, props, reset, func);
				});
			}
		}

		/*
		 * OID取得
		 */
		function getOid(entity) {
			if (typeof entity === "undefined" || entity == null) return "";
			var oid = entity.oid;
			if (typeof oid === "undefined" || oid == null) return "";
			return oid;
		}

		/*
		 * 親階層生成
		 */
		function createNode($v, name, props, reset, func) {
			var $parent = null;
			if (!reset) {
				var pleaseSelectLabel = "";
				if (scriptContext.gem.showPulldownPleaseSelectLabel === true) {
					pleaseSelectLabel = scriptContext.gem.locale.common.pleaseSelect;
				}
				$parent = $("<select />").attr("name", name).attr("data-norewrite", true).addClass("form-size-02 inpbr");
				if ($v.customStyle) {
					$parent.attr("style", $v.customStyle);
				}
				$("<option />").attr({value:""}).text(pleaseSelectLabel).appendTo($parent);
				$parent.val("");
				$v.before($parent);
				$v.before(" &gt; ");

				$parent.change(function() {
					//自分の階層以下を初期化
					$parent.nextAll("select").each(function() {
						$(this).val("");
						$(this).children("option[value!='']").remove();
					});

					loadReferenceData($v, props);
				});
			} else {
				$parent = $v.parent().children("select[name='" + name + "']");
			}

			if (func && $.isFunction(func)) {
				//親が先に読み込まれてるはず、この階層のデータを読み込む
				loadReferenceData($v, props, function(){func.call(this, $parent);});
			}
		}

		/*
		 * 参照データ読み込み
		 */
		function loadReferenceData($v, props, func) {
			var params = new Array();
			for (var i = 0; i < props.length; i++) {
				var val = $("select[name='" + props[i] + "']", $v.parent()).val();
				if (!val) val = "";
				params.push({key:props[i], value:val});
			}
			if (typeof $v.entityOid !== "undefined" && typeof $v.entityVersion !== "undefined") {
				params.push({key: "entityOid", value: $v.entityOid});
				params.push({key: "entityVersion", value: $v.entityVersion});
			}

			refComboChange($v.webapiName, $v.defName, $v.viewName, $v.propName, params, $v.viewType, function(selName, entities) {
				if (entities == null || entities.length == 0) return;

				if ($v.propName == selName) {
					selName = $v.prefix + selName;
				}

				var $select = $("select[name='" + selName + "']", $v.parent());
				$select.children("option[value!='']").remove();

				for (var i = 0; i < entities.length; i++) {
					var entity = entities[i];
					$select.append("<option value='" + entity.oid + "'>" + entity.name + "</option>");
				}

				if (func && $.isFunction(func)) func.call(this);
			});
		}
	};
})(jQuery);

/**
 * 参照コンボのコントローラー
 * 多重度1以外の際のノードをコントロール
 */
(function($){
	$.fn.refComboController = function(option) {
		var defaults = {
		};
		var options = $.extend(defaults, options);
		if (!this) return false;

		return this.each(function(){
			var $this = $(this);
			init($this, options);
		});

		function init($v, options) {
			$.extend($v, {
				ulId:$v.attr("data-ulId"),
				dummyId:$v.attr("data-dummyId"),
				propName:$v.attr("data-propName"),
				multiplicity:$v.attr("data-multiplicity") - 0
			});

			$v.on("click", function() {
				if (canAddItem($v.ulId, $v.multiplicity + 1)) {
					var $src = $("#" + $v.dummyId)
					var $copy = clone($src, null);
					var $sel = $("select", $copy).attr("name", $v.propName);
					$sel.refCombo();
					$(":button", $copy).on("click", function() { $copy.remove()});
				}
			});
		}
	};
})(jQuery);

/**
 * 再帰構造ツリー
 */
(function($){
	$.fn.refRecursiveTree = function(option) {
		var defaults = {
		};
		var options = $.extend(defaults, options);
		if (!this) return false;

		return this.each(function(){
			var $this = $(this);
			init($this, options);
		});

		function init($v, options) {
			$.extend($v, {
				defName        :$v.attr("data-defName"),//本体の定義名
				viewType       :$v.attr("data-viewType"),//detail or search
				viewName       :$v.attr("data-viewName"),//View名
				propName       :$v.attr("data-propName"),//プロパティ名
				prefix         :$v.attr("data-prefix"),//プロパティ名のプレフィックス
				multiplicity   :$v.attr("data-multiplicity") - 0,//多重度
				linkPropName   :$v.attr("data-linkPropName"),//連動プロパティ
				upperType      :$v.attr("data-upperType"),//連動元の表示形式
				webapiName     :$v.attr("data-webapiName"),//データ検索用のWebAPI
				container      :$v.attr("data-container"),//検索結果の格納先ノード
				title          :$v.attr("data-title"),//ダイアログのタイトル
				deletable      :$v.attr("data-deletable"),//削除可否
				customStyle    :$v.attr("data-customStyle"),//リンクのカスタムスタイル
				viewAction     :$v.attr("data-viewAction"),//リンクの表示用アクション
				refDefName     :$v.attr("data-refDefName"),//参照先の定義名
				refEdit        :$v.attr("data-refEdit") == "true",//ダイアログの編集可否
				updateRefAction:$v.attr("data-updateRefAction"),//表示画面での更新アクション
				reloadUrl      :$v.attr("data-reloadUrl"),//更新時のリロード用URL
				selCallbackKey :$v.attr("data-selCallbackKey"),//選択コールバック
				delCallbackKey :$v.attr("data-delCallbackKey"),//削除コールバック
				refSectionIndex:$v.attr("data-refSectionIndex"),//参照セクションインデックス
				entityOid      :$v.attr("data-entityOid"),
				entityVersion  :$v.attr("data-entityVersion")
			});

			var $dialog = createDialog($v);
			$v.on("click", function() {
				if ($v.linkPropName && $v.linkPropName != "") {
					if (!getLinkValue($v)) {
						alert(messageFormat(scriptContext.gem.locale.reference.pleaseSelectAny, $v.linkPropName));
						return;
					}
				}

				$dialog.dialog("open")
			});

			if ($v.linkPropName && $v.linkPropName != "") {
				//リンクプロパティ変更時は選択内容初期化
				$v.closest("form").change(function(e) {
					var $target = $(e.target);
					if ($target.attr("name") === $v.prefix + $v.linkPropName) {
						clear($v);

						if ($v._tree) $v._tree.tree('reload');
					}
				});
			}
		}

		function createDialog($v) {
			var id = uniqueId();
			var $div = $("<div />").addClass("recursiveTree").attr({id:"container_" + id, title:$v.title}).css("display", "none");
			$v.after($div);
			$("<span />").addClass("treeTitle").text(messageFormat(scriptContext.gem.locale.reference.pleaseSelectAny, $v.title)).appendTo($div);
			var $tree = $("<div />").addClass("treeContents").attr("id", "tree_" + id).appendTo($div);

			var $dialog = $div.dialog({
				resizable: false,
				autoOpen: false,
				height: 360,
				width: 440,
				modal: true,
				resizable: true,
				buttons: [{
					text: "OK",
					click: function() {
						//選択してるノードをリンク表示
						var $container = $("#" + es($v.container));//ul

						//一旦削除
						$container.children("li").remove();

						var nodes = $tree.tree('getSelectedNodes');
						for (var i = 0; i < nodes.length; i++) {
							(function () {
								var id = "li_" + $v.propName + i;
								var $li = $("<li />").addClass("list-add").attr("id", id).appendTo($container);
								var oid = nodes[i].oid;
								var version = nodes[i].version;
								var linkId = $v.prefix + $v.propName + "_" + oid;
								var $link = $("<a href='javascript:void(0)' />").addClass("modal-lnk").attr({"id":linkId, "data-linkId":linkId, "style":$v.customStyle}).text(nodes[i].name).on("click", function() {
									showReference($v.viewAction, $v.refDefName, oid, version, linkId, $v.refEdit, null, $v.defName, $v.viewName, $v.propName, $v.viewType, $v.refSectionIndex, $v.entityOid, $v.entityVersion);
								}).appendTo($li);
								if ($("body.modal-body").length != 0) {
									$link.subModalWindow();
								} else {
									$link.modalWindow();
								}
								if ($v.deletable) {
									var $delBtn = $("<input type='button' />").addClass("gr-btn-02 del-btn").val(scriptContext.gem.locale.reference.deleteLabel).on("click", function() {$li.remove();}).appendTo($li);
									if ($v.delCallbackKey) {
										var delCallback = scriptContext[$v.delCallbackKey];
										if (delCallback && $.isFunction(delCallback)) {
											$delBtn.on("click", function() { delCallback.call(this, $li.attr("id"));});
										}
									}
								}

								$("<input type='hidden' />").attr("name", $v.prefix + $v.propName).val(oid + "_" + version).appendTo($li);

								if ($v.selCallbackKey) {
									var selCallback = scriptContext[$v.selCallbackKey];
									if (selCallback && $.isFunction(selCallback)) {
										selCallback.call(this, $li.attr("id"));
									}
								}
							})();
						}

						if ($v.updateRefAction && $v.reloadUrl) {
							//詳細表示はリロード
							var $form = $("#detailForm");
							$("<input type='hidden' name='updatePropertyName' />").val($v.propName).appendTo($form);
							$("<input type='hidden' name='reloadUrl' />").val($v.reloadUrl).appendTo($form);
							$form.attr("action", $v.updateRefAction).submit();
						} else {
							$dialog.dialog("close");
						}
					}
				},{
					text: scriptContext.gem.locale.common.cancel,
					click: function() {
						$(this).dialog("close");
					}
				}],
				close: function() {
				}
			});
			$dialog.dialog({open: function() {
				//初期化
				$tree.tree({
					dataUrl : function(node) {
						var url_info = {
							url : contextPath + "/api/" + $v.webapiName,
							method : "POST"
						};

						var data = "defName=" + $v.defName;
						data += "&viewType=" + $v.viewType;
						data += "&viewName=" + $v.viewName;
						data += "&propName=" + formatPropNameChainString($v.prefix + $v.propName);
						if (typeof $v.entityOid !== "undefined" && typeof $v.entityVersion !== "undefined") {
							data += "&entityOid=" + $v.entityOid;
							data += "&entityVersion=" + $v.entityVersion;
						}
						if (node) {
							//子階層
							data += "&oid=" + node.oid;
						} else {
							//親階層
							var linkValue = getLinkValue($v);
							if (linkValue) {
								data += "&linkValue=" + linkValue;
							}
						}
						url_info["data"] = data;
						return url_info;
					}
				});
				$tree.off('tree.click');
				$tree.on('tree.click', function(e) {
					if ($v.multiplicity == 1) {
						//多重度1の場合は複数選択不可
						return;
					}

					// Disable single selection
					e.preventDefault();

					var selected_node = e.node;

					if (selected_node.id == undefined) {
						//console.log('The multiple selection functions require that nodes have an id');
						return;
					}

					if ($tree.tree('isNodeSelected', selected_node)) {
						$tree.tree('removeFromSelection',selected_node);
					} else {
						var nodes = $tree.tree('getSelectedNodes');
						//多重度以上に選択させない
						if ($v.multiplicity == -1 || nodes.length < $v.multiplicity) {
							$tree.tree('addToSelection', selected_node);
						}
					}
				});
				$v._tree = $tree;
			}});
			$dialog.on("dialogopen", function(e) {
				adjustDialogLayer($(".ui-widget-overlay"));
			});

			return $dialog;
		}

		function formatPropNameChainString(str) {
			//プロパティ名だけを.で結合した形にフォーマット
			if (str.indexOf("sc_") > -1) {
				return formatPropNameChainString(str.replace("sc_", ""));
			}

			if (str.indexOf("[") == -1 && str.indexOf("]") == -1) return str;
			return formatPropNameChainString(str.substr(0, str.indexOf("[")) + str.substr(str.indexOf("]") + 1));
		}

		function clear($v) {
			var $container = $("#" + es($v.container));
			$("li", $container).remove();
		}

		function getLinkValue($v) {
			var $linkProp = null;
			if ($v.upperType == "select") {
				$linkProp = $("select[name='" + es($v.prefix + $v.linkPropName) + "']");
			} else if ($v.upperType == "radio") {
				$linkProp = $("input[type='radio'][name='" + es($v.prefix + $v.linkPropName) + "']:checked");
			}

			if (!$linkProp) {
				return null;
			}
			return $linkProp.val();
		}
	};
})(jQuery);

/**
 * 連動プロパティ(Select型)
 */
(function($){
	$.fn.refLinkSelect = function(option) {
		var defaults = {
		};
		var options = $.extend(defaults, options);
		if (!this) return false;

		return this.each(function(){
			var $this = $(this);

			init($this, options);
		});

		/*
		 * 初期化処理
		 */
		function init($v, options) {
			$.extend($v, {
				defName:$v.attr("data-defName"),
				viewType:$v.attr("data-viewType"),
				viewName:$v.attr("data-viewName"),
				propName:$v.attr("data-propName"),
				linkName:$v.attr("data-linkName"),
				prefix:$v.attr("data-prefix"),
				getItemWebapiName:$v.attr("data-getItemWebapiName"),
				upperType:$v.attr("data-upperType"),
				entityOid:$v.attr("data-entityOid"),
				entityVersion:$v.attr("data-entityVersion")
			});

			//リンク元のアイテムを取得
			if ($v.upperType == "select") {
				var $select = $("select[name='" + $v.prefix + $v.linkName + "']");
				if (!$select) {
					return;
				}
				//リンク元にイベント設定
				$select.change(function() {
					//自身(連動先)値を初期化
					$v.val("").change();	//子供がいる可能性があるのでchangeイベント発火
					$v.children("option[value!='']").remove();

					//アイテムの取得
					getItemData($v, $select);
				});
			} else if ($v.upperType == "radio") {
				var $ul = $("ul[data-itemName='" + $v.prefix + $v.linkName + "']");
				if (!$ul) {
					return;
				}
				//リンク元にイベント設定(radioの場合、連動により消える可能性があるのでonで設定)
				$ul.on("change", "input:radio", function(){
					$v.val("").change();	//子供がいる可能性があるのでchangeイベント発火
					$v.children("option[value!='']").remove();

					//アイテムの取得
					getItemData($v, $(this));
				});
			}

		}

		/*
		 * 参照データ読み込み
		 */
		function getItemData($v, $link) {
			var linkValue = null;
			if ($v.upperType == "select") {
				linkValue = $link.val();
			} else if ($v.upperType == "radio") {
				linkValue = $("input[name='" + $v.prefix + $v.linkName + "']:radio:checked").val();
			}

			if (linkValue && linkValue != "") {
				//アイテムの検索(webapi)
				getLinkItems($v.getItemWebapiName, $v.defName, $v.viewType, $v.viewName, $v.propName, linkValue, $v.entityOid, $v.entityVersion, function(entities) {
					if (entities == null || entities.length == 0) return;

					for (var i = 0; i < entities.length; i++) {
						var entity = entities[i];
						if ($v.viewType == "detail") {
							//oid_version
							$v.append("<option value='" + entity.oid + "_" + entity.version + "'>" + entity.name + "</option>");
						} else if ($v.viewType == "search") {
							//oidのみ
							$v.append("<option value='" + entity.oid + "'>" + entity.name + "</option>");
						}
					}
				});
			}
		}
	};
})(jQuery);

/**
 * 連動プロパティ(Radio型)
 */
(function($){
	$.fn.refLinkRadio = function(option) {
		var defaults = {
		};
		var options = $.extend(defaults, options);
		if (!this) return false;

		return this.each(function(){
			var $this = $(this);

			init($this, options);
		});

		/*
		 * 初期化処理
		 */
		function init($v, options) {
			$.extend($v, {
				defName:$v.attr("data-defName"),
				viewType:$v.attr("data-viewType"),
				viewName:$v.attr("data-viewName"),
				propName:$v.attr("data-propName"),
				linkName:$v.attr("data-linkName"),
				prefix:$v.attr("data-prefix"),
				getItemWebapiName:$v.attr("data-getItemWebapiName"),
				upperType:$v.attr("data-upperType"),
				customStyle:$v.attr("data-customStyle"),
				entityOid:$v.attr("data-entityOid"),
				entityVersion:$v.attr("data-entityVersion")
			});

			//リンク元のアイテムを取得
			if ($v.upperType == "select") {
				var $select = $("select[name='" + $v.prefix + $v.linkName + "']");
				if (!$select) {
					return;
				}
				//リンク元にイベント設定
				$select.change(function() {
					//自身(連動先)値を初期化
					$("input[type='radio']", $v).prop("checked", false).change();

					$v.children("li").remove();

					//アイテムの取得
					getItemData($v, $select);
				});
			} else if ($v.upperType == "radio") {
				var $ul = $("ul[data-itemName='" + $v.prefix + $v.linkName + "']");
				if (!$ul) {
					return;
				}
				//リンク元にイベント設定(radioの場合、連動により消える可能性があるのでonで設定)
				$ul.on("change", "input:radio", function(){
					//自身(連動先)値を初期化
					$("input[type='radio']", $v).prop("checked", false).change();

					$v.children("li").remove();

					//アイテムの取得
					getItemData($v, $(this));
				});

			}
		}

		/*
		 * 参照データ読み込み
		 */
		function getItemData($v, $link) {

			var linkValue = null;
			if ($v.upperType == "select") {
				linkValue = $link.val();
			} else if ($v.upperType == "radio") {
				linkValue = $("input[name='" + $v.prefix + $v.linkName + "']:radio:checked").val();
			}

			if (linkValue && linkValue != "") {
				//アイテムの検索(webapi)
				getLinkItems($v.getItemWebapiName, $v.defName, $v.viewType, $v.viewName, $v.propName, linkValue, $v.entityOid, $v.entityVersion, function(entities) {
					if (entities == null || entities.length == 0) return;

					for (var i = 0; i < entities.length; i++) {
						var entity = entities[i];

						var $li = $("<li />")
						var $label = $("<label />").appendTo($li);
						if ($v.customStyle) {
							$label.attr("style", $v.customStyle);
						}
						var $radio = $("<input />").attr({
							type: "radio",
							value: entity.oid + "_" + entity.version ,
							name: $v.propName
						}).appendTo($label);
						$("<span />").text(entity.name).appendTo($label);

						$v.append($li);
					}
				});
			}
		}
	};
})(jQuery);

/**
 * 参照プロパティ(ユニークキー)
 */
(function($){
	$.fn.refUnique = function(option) {
		var defaults = {
		};
		var options = $.extend(defaults, option);
		if (!this) return false;

		return this.each(function(options) {
			var $this = $(this);
			init($this, options);
		});

		function init($v, options) {
			var params = {
				defName:					$v.attr("data-defName"),
				viewType:					$v.attr("data-viewType"),
				viewName:					$v.attr("data-viewName"),
				propName:					$v.attr("data-propName"),
				webapiName: 				$v.attr("data-webapiName"),
				selectAction:				$v.attr("data-selectAction"),
				viewAction:					$v.attr("data-viewAction"),
				addAction:					$v.attr("data-addAction"),
				selectUrlParam:				$v.attr("data-selectUrlParam"),
				insertUrlParam:				$v.attr("data-insertUrlParam"),
				refDefName:					$v.attr("data-refDefName"),
				refViewName:				$v.attr("data-refViewName"),
				refEdit:					$v.attr("data-refEdit") == "true",
				refSectionIndex:			$v.attr("data-refSectionIndex"),//参照セクションインデックス
				specVersionKey:				$v.attr("data-specVersionKey"),
				permitConditionSelectAll:	$v.attr("data-permitConditionSelectAll"),
				permitVersionedSelect:		$v.attr("data-permitVersionedSelect"),
				multiplicity:				$v.attr("data-multiplicity"),
				selUniqueRefCallback:		$v.attr("data-selUniqueRefCallback"),
				insUniqueRefCallback:		$v.attr("data-insUniqueRefCallback"),
				entityOid:					$v.attr("data-entityOid"),
				entityVersion:				$v.attr("data-entityVersion")
			};
			$.extend($v, params);

			var $txt = $("input[type='text'].inpbr", $v);
			var $selBtn = $(":button.sel-btn", $v);
			var $insBtn = $(":button.ins-btn", $v);
			var $link = $("a.modal-lnk", $v);
			var $hidden = $(":hidden[name='" + $v.propName + "']", $v);

			if ($("body.modal-body").length != 0) {
				$selBtn.subModalWindow();
			} else {
				$selBtn.modalWindow();
			}

			for (key in params) {
				$selBtn.attr("data-" + key, params[key]);
			}
			$selBtn.on("click", function() {
				//選択コールバック
				var selRefCallback = scriptContext[$v.selUniqueRefCallback];
				searchUniqueReference($v.attr("id"), $v.selectAction, $v.viewAction, $v.refDefName, $v.propName, $v.selectUrlParam, $v.refEdit, selRefCallback, this, $v.refViewName, $v.permitConditionSelectAll, $v.permitVersionedSelect, $v.defName, $v.viewName, $v.viewType, $v.refSectionIndex, $v.entityOid, $v.entityVersion);
			});

			if ($("body.modal-body").length != 0) {
				$insBtn.subModalWindow();
			} else {
				$insBtn.modalWindow();
			}

			for (key in params) {
				$insBtn.attr("data-" + key, params[key]);
			}
			$insBtn.on("click", function() {
				//新規コールバック
				var insRefCallback = scriptContext[$v.insUniqueRefCallback];
				insertUniqueReference($v.attr("id"), $v.addAction, $v.viewAction, $v.refDefName, $v.propName, $v.multiplicity, $v.insertUrlParam, $v.defName, $v.viewName, $v.refEdit, insRefCallback, this, $v.viewType, $v.refSectionIndex);
			});

			$hidden.on("change", function() {
				$link.show();
			});

			$txt.on("change", function() {
				$link.attr({"id":"", "data-linkId":""}).text("").hide();
				$hidden.val("");

				//ユニークキーフィールドがクリアされた場合は、検索に行かずに、参照リンクの文字とoid、version情報をクリアする
				if ($txt.val() == "") return;

				var duplicate = false;
				$v.parent("ul").find(".unique-key:not(:hidden)").children("input[type='text']").each(function() {
					//重複チェック （自分を除く）
					if ($(this).val() == $txt.val() && !$txt.is(this)) {
						duplicate = true;
						return;
					}
				});

				if (duplicate) {
					alert(scriptContext.gem.locale.reference.duplicateData);
					return;
				}

				var _propName = $v.propName.replace(/^sc_/, "").replace(/\[\w+\]/g, "");
				var uniqueValue = $txt.val().length == 0 ? null : $txt.val();
				getUniqueItem($v.webapiName, $v.defName, $v.viewName, $v.viewType, _propName, uniqueValue, $v.entityOid, $v.entityVersion, function(entity) {
					var entityList = new Array();
					if(entity && !$.isEmptyObject(entity)) {
						var linkId = $v.propName + "_" + entity.oid;
						var label = entity.name;
						var key = entity.oid + "_" + entity.version;
						var func = function() {
							showReference($v.viewAction, $v.refDefName, entity.oid, entity.version, linkId, $v.refEdit, null, $v.defName, $v.viewName, $v.propName, $v.viewType, $v.refSectionIndex, $v.entityOid, $v.entityVersion);
						};

						$link.attr({"id":linkId, "data-linkId":linkId}).text(label).show();
						$link.removeAttr("onclick").off("click", func);
						$link.on("click", func);
						$hidden.val(key);

						entityList.push(entity);

						//選択コールバック
						var selRefCallback = scriptContext[$v.selUniqueRefCallback];
						if (selRefCallback && $.isFunction(selRefCallback)) {
							if (typeof button === "undefined" || button == null) {
								selRefCallback.call(this, entityList, null, $v.propName);
							} else {
								//引数で渡されたトリガーとなるボタンをthisとして渡す
								selRefCallback.call(button, entityList, null, $v.propName);
							}
						}
					} else {
						alert(scriptContext.gem.locale.reference.noResult);
					}
				});

			});
		}
	};
})(jQuery);

/**
 * テーブルカラム数変更
 */
(function($){
	$.fn.multiColumnTable =function(option) {
		var defaults = {
			colNum:2,
			exclude:".submit-area,.version-area",
			deleteTarget:":has(div.deleteRow)",
			cellClass:""
		}
		var options = $.extend(defaults, option);
		if (!this) return false;

		var ret = this.each(function() {
			var $this = $(this);

			if ($this.attr("data-colNum")) {
				options.colNum = $this.attr("data-colNum") - 0;
			}
			if ($this.attr("data-cell-class")) {
				options.cellClass = $this.attr("data-cell-class");
			}

			//対象の行取得
			var $rows = null;
			if (options.exclude) {
				//除外指定あり
				$rows = $("tr:not(" + options.exclude + ")", $this);
			} else {
				$rows = $("tr", $this);
			}

			var $current = null;
			var reUse = false;
			var colIndex = 0;
			$rows.each(function() {
				var $row = $(this);
				if (options.deleteTarget && $row.is(options.deleteTarget)) {
					//対象外の行は捨てる
					$row.remove();

					return true;
				}

				if (colIndex == 0) {
					//複数列用の行入れる
					if ($current == null) {
						//最初の１行目
						$current = $("<tr />").addClass("col" + options.colNum).prependTo($this);
					} else {
						//２行目以降
						if (reUse == false) {
							var $tmp = $("<tr />").addClass("col" + options.colNum).insertAfter($current);
							$current = $tmp;
						} else {
							reUse = false;
						}
					}
				}

				//1列行から複数列行に中身コピー
				$current.append($row.children());

				//1列行削除
				$row.remove();

				if (++colIndex == options.colNum) {
					colIndex = 0;
					if ($("td", $current).children().length == 0) {
						//行内のtdに何も入ってない場合はセル消して再利用
						$("th,td", $current).remove();
						reUse = true;
					}
				}
			});

			// 指定列数に足りない場合は空をうめる
			// ReferenceSectionの対応、SearchConditionSectionはBlankが設定される
			if (colIndex !== 0) {
				for (var i = colIndex; i < options.colNum; i++) {
					$current.append($("<th/>").addClass(options.cellClass));
					$current.append($("<td/>").addClass(options.cellClass));
				}
			}

			//除外行は先頭以外をcolspanでくっつける(折り返さないように)
			var $excludes = $(options.exclude, $this);
			var colspan = options.colNum * 2 - 1;
			$excludes.each(function() {
				var $row = $(this);
				var $td = $("td", $row);
				$td.attr("colspan", colspan);
			});
		});
	};
})(jQuery);

////////////////////////////////////////////////////////
// 日付・時間用のJavascript
////////////////////////////////////////////////////////

/**
 * Datepickerの一括適用
 *
 * 1.4.5からは以下の方式を推奨。
 *
 * <pre>
 * $target.applyDatepicker({
 *     onChangeActual: function() {
 *         dateToHidden($(this), $hidden);
 *     }
 * });
 * </pre>
 */
function datepicker(selector) {

	$(selector).each(function() {
		$(this).applyDatepicker();
	});
}

/**
 * Datepicker適用(1.4.5～)
 */
(function($){
	$.fn.applyDatepicker =function(option) {
		var defaults = {
			dateFormat: dateUtil.getDatepickerDateFormat(),
			inputDateFormat: dateUtil.getInputDateFormat(),
			buttonText: scriptContext.gem.locale.date.datepickerBtn,
			buttonImage: null,
			showErrorMessage: true,
			validErrMsg: null,
			onChangeActual: function(){},
			dayLabelSelector: ".dp-weekday-label"
		}
		var options = $.extend(defaults, option);
		if (!this) return false;

		if (options.buttonImage == null) {
			var skinName = scriptContext.skinName;
			if (typeof skinName === "undefined" || skinName == null || skinName == "") {
				skinName = "vertical";
			}
			options.buttonImage = contentPath + "/images/gem/skin/" + skinName + "/icon-calender-01.png";
		}

		this.each(function() {
			var $this = $(this);

			if (!$this.hasClass("datepicker")) {
				$this.addClass("datepicker");
			}
			var formatLength = options.dateFormat.length;
			var formatClass = "datepicker-form-size-01";
			if (formatLength > 6) {//datepickerの年はyyなのでmin6-max8
				formatClass = "datepicker-form-size-02";
			}
			if (!$this.hasClass(formatClass)) {
				$this.addClass(formatClass);
			}
			$this.attr("maxLength", formatLength + 2);

			$this.datepicker({
				showOn: 'both',
				buttonImage: options.buttonImage,
				buttonText: options.buttonText,
				buttonImageOnly: true,
				showMonthAfterYear: true,
				showButtonPanel:$this.attr("data-showButtonPanel") == "true",//今日ボタン表示
				dateFormat: options.dateFormat,
				beforeShow: function() {
					//初回時のためここでセット(beforeShow->focus)
					$this.attr("data-prevalue", convertFromLocaleDateString($this.val()));

					//年月移動時のblurを抑制するためマーク
					$this.attr("data-dispPicker", true);
				},
				onClose: function() {
					$this.removeAttr("data-dispPicker");

					//カレンダー選択時はblurが走らないため明示的にChange呼び出し
					fireOnChange(this);
				}
			}).on("blur", function() {
				//未入力からのEnter時や直接入力エラー時にカレンダーが消えた状態でフォーカスされることがあるため
				//onCloseだけではなくblurでも制御する

				//年月移動時に呼び出されるのでpickerが表示されているかをチェック
				if (isUnDispPicker(this)) {
					//入力値の検証はfireOnChangeで実施(1.4.5～)
					fireOnChange(this);
				}

			}).on("focus", function() {
				//focus時の値を設定
				//初回時の対応のためbeforeShowでセットするようにしたので不要だが念のため
				//年月移動時に呼び出されるのでpickerが表示されているかをチェック
				if (isUnDispPicker(this)) {
					//pickerが表示されていない場合のみセット
					$this.attr("data-prevalue", convertFromLocaleDateString($this.val()));
				}
			});
			if ($this.attr("data-showWeekday") === "true") {
				$this.after($("<span  />").addClass("dp-weekday-label"));
			}
		});

		function isUnDispPicker(input) {
			var $input = $(input);
			var dispPicker = $input.attr("data-dispPicker");
			return (typeof dispPicker === "undefined" || dispPicker == null);
		}

		function fireOnChange(input) {
			var $input = $(input);
			var $parent = $input.closest(".property-data"); // 詳細編集、検索画面のみ対応

			//検証
			try {
				//common.js
				validateDate($input.val(), options.inputDateFormat, options.validErrMsg);

				//正常値
				if ($input.attr("data-notFillTime") !== "true") {
					fillTime(input);
				}

				$input.removeClass("validate-error");
				if ($(".validate-error", $parent).length === 0) {
					$(".format-error", $parent).remove();
				}
			} catch (e) {
				var suppressAlert = $input.attr("data-suppress-alert")
				if (suppressAlert) {
					$input.addClass("validate-error");
					//アラートの代わりにエラーメッセージ
					if ($(".format-error", $parent).length === 0) {
						var $p = $("<p />").addClass("error format-error").appendTo($parent);
						$("<span />").addClass("error").text(e).appendTo($p);
					}
				} else {
					//値をクリア(メッセージ表示する前にクリアする)
					//直接入力時にフォーカスロストすると2度呼ばれる(onCloseとblur)ためalert前にクリアする
					//(最初のイベントで値がクリアされて次のチェックでは引っかからない)
					$input.val("");

					if (options.showErrorMessage == true) {
						alert(e);
					}
				}
			}

			//focus時の値と比較して変更があるかをチェック
			var realChange = false;
			var prev = $input.attr("data-prevalue");
			var cur = convertFromLocaleDateString($input.val());
			if (typeof prev !== "undefined") {
				if (prev != cur) {
					realChange = true;
				}
			} else {
				//不正な状態なので念のため呼び出し
				realChange = true;
			}

			if (realChange == true) {
				//現状の動作を変えないため、change呼び出しを残す
				$input.change();

				if (options.onChangeActual && $.isFunction(options.onChangeActual)) {
					options.onChangeActual.call($input);
				}

				//Enterでダイアログが開いたままblurが走って消えてしまうので消さずに、最新の値をセット
				//$dp.removeAttr("data-prevalue");
				$input.attr("data-prevalue", cur);
			}

			//Enterでダイアログが開いたままblurが走って消えてしまうので消さない
			//$dp.removeAttr("data-prevalue");

			if ($input.next().is(".dp-weekday-label") && $input.val() != null && $input.val() != "") {
				$input.next().text(dateUtil.getWeekday($input.val(), options.inputDateFormat));
			} else {
				$input.next().text("");
			}
		}

		function fillTime(input) {
			//DateTimeEditorで日付選択時に時分秒のSelectを利用する場合

			var $input = $(input);
			if ($input.val() == "") {
				//空の場合はセットしない
				return;
			}

			//Selectにデフォルト値を設定
			$input.siblings("label").children("select").each(function() {
				var val = $(this).val();
				if (val == null || val =="  ") {
					var defaultValue = $(this).attr("data-defaultValue");
					$(this).val(defaultValue);
				}
			});
		}
	};
})(jQuery);

/**
 * Timepickerの一括適用
 *
 * 1.4.5からは以下の方式を推奨。
 *
 * <pre>
 * $target.applyTimepicker({
 *     onChangeActual: function() {
 *         timePickerToHidden($(this), $hidden);
 *     }
 * });
 * </pre>
 */
function timepicker(selector) {

	$(selector).each(function() {
		$(this).applyTimepicker();
	});
}

/**
 * Timepicker適用(1.4.5～)
 */
(function($){
	$.fn.applyTimepicker =function(option) {
		var defaults = {
			timeFormat: null,
			stepMinute: null,
			fixedMin: null,
			fixedSec: null,
			fixedMSec: "000",
			buttonText: scriptContext.gem.locale.date.timepickerBtn,
			buttonImage: null,
			showErrorMessage: true,
			validErrMsg: null,
			onChangeActual: function(){}
		}
		var options = $.extend(defaults, option);
		if (!this) return false;

		if (options.buttonImage == null) {
			var skinName = scriptContext.skinName;
			if (typeof skinName === "undefined" || skinName == null || skinName == "") {
				skinName = "vertical";
			}
			options.buttonImage = contentPath + "/images/gem/skin/" + skinName + "/icon-clock.png";
		}

		this.each(function() {
			var $this = $(this);

			//attributeで定義されている場合はoptionより優先(旧形式)
			var timeFormat = $this.attr("data-timeformat") || options.timeFormat;
			var stepMinute = $this.attr("data-stepmin") || options.stepMinute;

			if (typeof timeFormat === "undefined" || timeFormat == "") {
				throw "timeFormat is required.";
			}
			if (typeof stepMinute === "undefined" || stepMinute == "") {
				throw "stepMinute is required.";
			}

			$this.timepicker({
				showOn: 'both',
				buttonImage: options.buttonImage,
				buttonText: options.buttonText,
				buttonImageOnly: true,
				timeFormat : timeFormat,
				stepMinute: Number(stepMinute),	//数値に変換
				beforeShow: function(ip, dp, tp) {
					//初回時のためここでセット(beforeShow->focus)
					$this.attr("data-prevalue", convertFromTimeString(this));

					//blurを抑制するためマーク(他のpickerに合わせる)
					$this.attr("data-dispPicker", true);

					if (ip.value == "") {
						tp.hour = 0;
						tp.minute = 0;
						tp.second = 0;
						tp.millisec = 0;
					}
				},
				onClose: function(val, dp, tp) {
					$this.removeAttr("data-dispPicker");

					//カレンダー選択時はblurが走らないため明示的にChange呼び出し
					fireOnChange(this);
				}
			}).on("blur", function() {
				//未入力からのEnter時や直接入力エラー時にカレンダーが消えた状態でフォーカスされることがあるため
				//onCloseだけではなくblurでも制御する

				//pickerが表示されているかをチェック(他のpickerに合わせる)
				if (isUnDispPicker(this)) {
					//pickerが表示されていない場合のみ変更処理
					fireOnChange(this);
				}

			}).on("focus", function() {
				//focus時の値を設定
				//初回時の対応のためbeforeShowでセットするようにしたので不要だが念のため
				//pickerが表示されているかをチェック(他のpickerに合わせる)
				if (isUnDispPicker(this)) {
					//pickerが表示されていない場合のみセット
					$this.attr("data-prevalue", convertFromTimeString(this));
				}
			});
		});

		function isUnDispPicker(input) {
			var $input = $(input);
			var dispPicker = $input.attr("data-dispPicker");
			return (typeof dispPicker === "undefined" || dispPicker == null);
		}

		function convertFromTimeString(input) {
			var $input = $(input);

			var val = $input.val();
			if (val == "") return "";

			var fixedMin = $input.attr("data-fixedMin") || options.fixedMin || "";
			var fixedSec = $input.attr("data-fixedSec") || options.fixedSec || "";
			var fixedMSec = $input.attr("data-fixedMSec") || options.fixedMSec || "";

			return val.split(":").join("") + fixedMin + fixedSec + fixedMSec;
		}

		function fireOnChange(input) {
			var $input = $(input);
			var $parent = $input.closest(".property-data"); // 詳細編集、検索画面のみ対応

			//検証
			try {
				var timeFormat = $input.attr("data-timeformat") || options.timeFormat;
				var fixedMin = $input.attr("data-fixedMin") || options.fixedMin || "";
				var fixedSec = $input.attr("data-fixedSec") || options.fixedSec || "";
				//common.js
				validateTimePicker($input.val(), timeFormat, fixedMin, fixedSec, options.validErrMsg);

				$input.removeClass("validate-error");
				if ($(".validate-error", $parent).length === 0) {
					$(".format-error", $parent).remove();
				}
			} catch (e) {
				var suppressAlert = $input.attr("data-suppress-alert")
				if (suppressAlert) {
					$input.addClass("validate-error");
					//アラートの代わりにエラーメッセージ
					if ($(".format-error", $parent).length === 0) {
						var $p = $("<p />").addClass("error format-error").appendTo($parent);
						$("<span />").addClass("error").text(e).appendTo($p);
					}
				} else {
					//値をクリア(メッセージ表示する前にクリアする)
					//直接入力時にフォーカスロストすると2度呼ばれる(onCloseとblur)ためalert前にクリアする
					//(最初のイベントで値がクリアされて次のチェックでは引っかからない)
					$input.val("");

					if (options.showErrorMessage == true) {
						alert(e);
					}
				}
			}

			//focus時の値と比較して変更があるかをチェック
			var realChange = false;
			var prev = $input.attr("data-prevalue");
			var cur = convertFromTimeString(input);
			if (typeof prev !== "undefined") {
				if (prev != cur) {
					realChange = true;
				}
			} else {
				//不正な状態なので念のため呼び出し
				realChange = true;
			}

			if (realChange == true) {
				//onChangeでハンドリングしている場合の動作に影響を与えないため、change呼び出しを残す
				$input.change();

				if (options.onChangeActual && $.isFunction(options.onChangeActual)) {
					options.onChangeActual.call($input);
				}

				//Enterでダイアログが開いたままblurが走って消えてしまうので消さずに、最新の値をセット
				//$input.removeAttr("data-prevalue");
				$input.attr("data-prevalue", cur);
			}
		}
	};
})(jQuery);

/**
 * Datetimepickerの一括適用
 *
 * 1.4.5からは以下の方式を推奨。
 *
 * <pre>
 * $target.applyDatetimepicker({
 *     onChangeActual: function() {
 *         timestampPickerToHidden($(this), $hidden);
 *     }
 * });
 * </pre>
 */
function datetimepicker(selector) {

	$(selector).each(function() {
		$(this).applyDatetimepicker();
	});
}


/**
 * Datetimepicker適用(1.4.5～)
 */
(function($){
	$.fn.applyDatetimepicker =function(option) {
		var defaults = {
			dateFormat: dateUtil.getDatepickerDateFormat(),
			inputDateFormat: dateUtil.getInputDateFormat(),
			timeFormat: null,
			stepMinute: null,
			fixedMin: null,
			fixedSec: null,
			fixedMSec: "000",	//現状未使用
			buttonText: scriptContext.gem.locale.date.datepickerBtn,
			buttonImage: null,
			showErrorMessage: true,
			validErrMsg: null,
			onChangeActual: function(){},
			dayLabelSelector: ".dp-weekday-label"
		}
		var options = $.extend(defaults, option);
		if (!this) return false;

		if (options.buttonImage == null) {
			var skinName = scriptContext.skinName;
			if (typeof skinName === "undefined" || skinName == null || skinName == "") {
				skinName = "vertical";
			}
			options.buttonImage = contentPath + "/images/gem/skin/" + skinName + "/icon-calender-01.png";
		}

		this.each(function() {
			var $this = $(this);

			//attributeで定義されている場合はoptionより優先(旧形式)
			var timeFormat = $this.attr("data-timeformat") || options.timeFormat;
			var stepMinute = $this.attr("data-stepmin") || options.stepMinute;

			if (typeof timeFormat === "undefined" || timeFormat == "") {
				throw "timeFormat is required.";
			}
			if (typeof stepMinute === "undefined" || stepMinute == "") {
				throw "stepMinute is required.";
			}

			if (!$this.hasClass("datetimepicker")) {
				$this.addClass("datetimepicker");
			}
			var formatLength = options.dateFormat.length + timeFormat.length + 1;
			var formatClass = "datetimepicker-form-size-01";
			if (formatLength > 15) {//datepickerの年はyyなのでmin15-max17
				formatClass = "datetimepicker-form-size-02";
			}
			if (!$this.hasClass(formatClass)) {
				$this.addClass(formatClass);
			}
			$this.attr("maxLength", formatLength + 2);

			$this.datetimepicker({
				showOn: 'both',
				buttonImage: options.buttonImage,
				buttonText: options.buttonText,
				buttonImageOnly: true,
				separator: " ",
				dateFormat: options.dateFormat,
				timeFormat : timeFormat,
				stepMinute: Number(stepMinute),	//数値に変換
				beforeShow: function(ip, dp, tp) {
					//初回時のためここでセット(beforeShow->focus)
					$this.attr("data-prevalue", convertFromLocaleDatetimeString($this.val()));

					//年月移動時のblurを抑制するためマーク
					$this.attr("data-dispPicker", true);

					if (ip.value == "") {
						tp.hour = 0;
						tp.minute = 0;
						tp.second = 0;
						tp.millisec = 0;
					}
				},
				onClose: function(val, dp, tp) {
					$this.removeAttr("data-dispPicker");

					//カレンダー選択時はblurが走らないため明示的にChange呼び出し
					fireOnChange(this);
				}
			}).on("blur", function() {
				//未入力からのEnter時や直接入力エラー時にカレンダーが消えた状態でフォーカスされることがあるため
				//onCloseだけではなくblurでも制御する

				//年月移動時に呼び出されるのでpickerが表示されているかをチェック
				if (isUnDispPicker(this)) {
					//pickerが表示されていない場合のみ変更処理
					fireOnChange(this);
				}

			}).on("focus", function() {
				//focus時の値を設定
				//初回時の対応のためbeforeShowでセットするようにしたので不要だが念のため
				//年月移動時に呼び出されるのでpickerが表示されているかをチェック
				if (isUnDispPicker(this)) {
					//pickerが表示されていない場合のみセット
					$this.attr("data-prevalue", convertFromLocaleDatetimeString($this.val()));
				}
			});
			if ($this.attr("data-showWeekday") === "true") {
				$this.after($("<span  />").addClass("dp-weekday-label"));
			}
		});

		function isUnDispPicker(input) {
			var $input = $(input);
			var dispPicker = $input.attr("data-dispPicker");
			return (typeof dispPicker === "undefined" || dispPicker == null);
		}

		function fireOnChange(input) {
			var $input = $(input);
			var $parent = $input.closest(".property-data"); // 詳細編集、検索画面のみ対応

			//検証
			try {
				var timeFormat = $input.attr("data-timeformat") || options.timeFormat;
				var fixedMin = $input.attr("data-fixedMin") || options.fixedMin || "";
				var fixedSec = $input.attr("data-fixedSec") || options.fixedSec || "";
				//common.js
				validateTimestampPicker($input.val(), options.inputDateFormat, timeFormat, fixedMin, fixedSec, options.validErrMsg);

				$input.removeClass("validate-error");
				if ($(".validate-error", $parent).length === 0) {
					$(".format-error", $parent).remove();
				}
			} catch (e) {
				var suppressAlert = $input.attr("data-suppress-alert")
				if (suppressAlert) {
					$input.addClass("validate-error");
					//アラートの代わりにエラーメッセージ
					if ($(".format-error", $parent).length === 0) {
						var $p = $("<p />").addClass("error format-error").appendTo($parent);
						$("<span />").addClass("error").text(e).appendTo($p);
					}
				} else {
					//値をクリア(メッセージ表示する前にクリアする)
					//直接入力時にフォーカスロストすると2度呼ばれる(onCloseとblur)ためalert前にクリアする
					//(最初のイベントで値がクリアされて次のチェックでは引っかからない)
					$input.val("");

					if (options.showErrorMessage == true) {
						alert(e);
					}
				}
			}

			//focus時の値と比較して変更があるかをチェック
			var realChange = false;
			var prev = $input.attr("data-prevalue");
			var cur = convertFromLocaleDatetimeString($input.val());
			if (typeof prev !== "undefined") {
				if (prev != cur) {
					realChange = true;
				}
			} else {
				//不正な状態なので念のため呼び出し
				realChange = true;
			}

			if (realChange == true) {
				//onChangeでハンドリングしている場合の動作に影響を与えないため、change呼び出しを残す
				$input.change();

				if (options.onChangeActual && $.isFunction(options.onChangeActual)) {
					options.onChangeActual.call($input);
				}

				//Enterでダイアログが開いたままblurが走って消えてしまうので消さずに、最新の値をセット
				//$input.removeAttr("data-prevalue");
				$input.attr("data-prevalue", cur);
			}

			if ($input.next().is(".dp-weekday-label") && $input.val() != null && $input.val() != "") {
				$input.next().text(dateUtil.getWeekday($input.val(), options.inputDateFormat));
			} else {
				$input.next().text("");
			}
		}
	};
})(jQuery);

/**
 * ツリービューリスト
 */
(function($) {
	$.fn.treeViewList = function(options){
		var defaults = {
			cookiePath:"treeViewListNode"
		};
		var option = $.extend(defaults, options);
		if (!this) return false;
		return this.each(function() {
			var $this = $(this);
			init($this, option);
		});

		function init($v, option) {
			$.extend($v,{
				defName: $v.attr("data-defName")
				,viewAction: $v.attr("data-viewAction")
				,webapiName: $v.attr("data-webapiName")
			});
			$v.tree({
				selectable : false,
				dataUrl : function(node) {
					var url_info = {
						url : contextPath + "/api/" + $v.webapiName,
						method : "POST"
					};
					var data = "defName=" + $v.defName;
					if (node) {
						//子階層
						data += "&path=" + node.path;
						data += "&type=" + node.type;
						if (node.type == "entitydefinition") {
							if (typeof node.parent.oid !== "undefined" && node.parent.oid != null) {
								data += "&oid=" + escapeJsonParamValue(node.parent.oid);
							} else {
								data += "&oid=root";
							}
						} else if (node.type == "index") {
							if (typeof node.oid !== "undefined" && node.oid != null) {
								data += "&oid=" + escapeJsonParamValue(node.oid);
							}
							if (typeof node.offset !== "undefined" && node.offset != null) {
								data += "&offset=" + node.offset;
							}
						} else if (node.type == "entity") {
							if (typeof node.oid !== "undefined" && node.oid != null) {
								data += "&oid=" + escapeJsonParamValue(node.oid);
							}
						}
					}
					url_info["data"] = data;
					return url_info;
				},
				onCreateLi: function(node, $li) {
					$li.attr("id", node.id);
					var $span = $("span.jqtree-title", $li);
					if (node.style) {
						$span.addClass(node.style);
					}
					if (node.icon) {
						$("<img />").attr("src", getResourceContentPath(node.icon)).prependTo($span);
					}
					if (node.type != "entity") {
						var $div = $("div.jqtree-element", $li);
						$div.addClass("folderNode");
					}

					//クッキー内に保存したパスを元に、展開状態を復元
					var path = getSessionStorage(option.cookiePath);
					if (path) {
						var pathList = path.split("/");
						if (pathList.length > 0 && pathList[0] == node.id) {
							if (pathList.length > 1) {
								pathList.shift();
								setSessionStorage(option.cookiePath, pathList.join("/"));
								//同一階層が読み込み終わったらノード展開
								$v.one("tree.refresh", function() {
									$("#" + es(node.id), $v).children('.jqtree-element').find('a.jqtree-toggler').click();
									$(".fixHeight").fixHeight();
								});
							} else {
								deleteSessionStorage(option.cookiePath);
							}
						}
					}
				}
			});

			//クリック処理
			$v.on("tree.click", function(e) {
				var node = e.node;
				if (node.type == "entity") {
					// メニューの状態を初期化
					clearMenuState();

					//現在展開中の階層のパスをクッキーに保存
					setSessionStorage(option.cookiePath, getNodePath(node));


					//Entityの内容を表示
					var action = node.action ? node.action : $v.viewAction;
					if (node.viewName) {
						action = action + "/" + node.viewName;
					}
					action += "/" + node.defName;
					showDetail(action, node.oid, 0, false, null, {});
				} else {
					if (node.is_open) {
						$v.tree('closeNode', node);
					} else {
						$v.tree('openNode', node);
					}
				}
			});

			//ツリー開閉時の高さ再調整
			$v.on("tree.open", function(e) {
				$(".fixHeight").fixHeight();
			});
			$v.on("tree.close", function(e) {
				$(".fixHeight").fixHeight();
			});

			function getNodePath(node) {
				if (node.parent && node.parent.id) {
					return getNodePath(node.parent) + "/" + node.id;
				} else {
					return node.id;
				}
			}
		}
	};
})(jQuery);

/**
 * ツリービューグリッド
 */
(function($) {
	$.fn.treeViewGrid = function(options){
		var defaults = {
			cookiePath:"treeViewListNode"
		};
		var option = $.extend(defaults, options);
		if (!this) return false;
		return this.each(function() {
			var $this = $(this);
			init($this, option);
		});

		function init($v, option) {
			$.extend($v, {
				defName: $v.attr("data-defName"),
				getDefinitionWebapi: $v.attr("data-getDefinitionWebapi"),
				getDataWebapiName: $v.attr("data-getDataWebapiName"),
				viewAction: $v.attr("data-viewAction")
			});

			//定義取得
			getTreeViewDefinition($v.getDefinitionWebapi, $v.defName, function(definition) {
				//ツリーグリッド生成
				var colModels = new Array();

				//key
				colModels.push({name:"id", index:"id", hidden: true, key:true, label:"id", sortable:false });

				//name
				colModels.push({name:"name", index:"name", width: 260, label:"<p class='title'>" + scriptContext.gem.locale.common.name + "</p>", sortable:false, formatter:function(cellValue, opt, rowObject) {
					var dispValue = $.jgrid.htmlEncode(cellValue);
					if (rowObject.type == "E") {
						var defName = rowObject.defName;
						var oid = $.jgrid.htmlEncode(rowObject.oid);
						var action = rowObject.action;
						var viewName = rowObject.viewName;

						var linkHtml = "<a href='javascript:void(0)' data-defName='" + defName + "' data-oid='" + oid + "' data-action='" + action + "' data-viewName='" + viewName + "' class='treeGridEntityLink'>" + dispValue + "</a>";
						return linkHtml;
					}
					return dispValue;
				}});

				//entityのリンク用
				colModels.push({name:"type", index:"type", hidden: true, label:"type", sortable:false});
				colModels.push({name:"defName", index:"defName", hidden: true, label:"defName", sortable:false});
				colModels.push({name:"oid", index:"oid", hidden: true, label:"oid", sortable:false, formatter:oidCellFormatter});
				colModels.push({name:"action", index:"action", hidden: true, label:"action", sortable:false});
				colModels.push({name:"viewName", index:"viewName", hidden: true, label:"viewName", sortable:false});

				//properties
				$(definition.colModel).each(function(index) {
					var displayName = "<p class='title'>" + getMultilingualStringWithView(this.displayLabel, this.localizedDisplayLabelList) + "</p>";

					var name = this.name.split(".").join("_");
					var colModel = {name:name, index:name, align:"center", label:displayName, sortable:false, formatter:function(cellValue, opt, rowObject) {
						//プロパティ値はstringValuesから取得する(プロパティの先頭から順番にセットされて返される)
						var strValues = rowObject.stringValues;
						var dispValue = "";
						if (strValues && $.isArray(strValues)
								&& strValues.length >= index && strValues[index] != null) {
							dispValue = $.jgrid.htmlEncode(strValues[index]);
						}
						return dispValue;
					}};
					if (this.align) colModel.align = this.align;
					if (this.width) colModel.width = this.width;
					colModels.push(colModel);
				});

				var $table = $("<table />").prependTo($v);
				var $treeGrid = $table.jqGrid({
					url:contextPath + "/api/" + $v.getDataWebapiName,
					postData:{defName:$v.defName},
					autoencode: true,
					treeGrid:true,
					datatype: "json",
					treeGridModel:"adjacency",
					ExpandColumn:"name",
					ExpandColClick:false,
					mtype: "POST",
					colModel: colModels,
					headertitles: true,
					caption: "TreeViewGrid",
					height: 230,
					viewrecords: true,
					sortable:false,
					sortname:"name",
					sortorder: "asc",
					altclass:"myAltRowClass",
					loadui:"disable",
					loadComplete: function(data) {
						$(".treeGridEntityLink", $v).each(function() {
							var $link = $(this);
							$link.removeClass("treeGridEntityLink");
							$link.on("click", function() {
								var defName = $link.attr("data-defName");
								var oid = $link.attr("data-oid");
								var action = $link.attr("data-action");
								var viewName = $link.attr("data-viewName");
								var viewAction = $v.viewAction;
								if (typeof action !== "undefined" && action != null && action != "" && action != "null") {
									viewAction = action;
								}
								if (typeof viewName !== "undefined" && viewName != null && viewName != "" && viewName != "null") {
									viewAction = viewAction + "/" + viewName;
								}
								viewAction += "/" + defName;
								showDetail(viewAction, oid, 0, false, null, {});

							});
						});
//						$("#load_", $v).hide();//複数treeGridがあるとIDが被るので個別に消す
						$(".fixHeight").fixHeight();
					},
					treeReader: {
						level_field: "level",
						parent_id_field: "parent",
						leaf_field: "leaf",
						expanded_field: "expanded"
					},
					onSelectRow: function(rowid, e) {
					}
				});
			});

		}
	};
})(jQuery);

/**
 * tableStripeクラスを定義しているtableにストライプ（oddスタイル）を付ける
 * ただし、下記条件に一致するtrにのみとする
 * ・hiddenになっていないtable
 * ・trの中にtdが含まれている行
 */
(function($) {
	$.fn.tableStripe = function(options){
		var defaults = {
		};
		var option = $.extend(defaults, options);
		if (!this) return false;

		return this.each(function() {
			var $this = $(this);
			$("tr:not(:hidden):has(td)", $this).addClass("stripeRow");
		});
	};
})(jQuery);
