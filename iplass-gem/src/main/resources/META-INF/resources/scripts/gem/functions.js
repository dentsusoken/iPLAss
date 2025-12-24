/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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
 * refComboSync
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
 * refSelectFilter
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
		if (!this) return false;

		const defaults = {
			dialogs : '.modal-dialogs',
			controls : '.modal-body .modal-btn, .modal-body .modal-lnk'
		};
		const options = $.extend(defaults, option);

		return this.each(function() {
			let rootDocument = null;
			if (!parent.document.rootDocument) {
				rootDocument = parent.document;
			} else {
				rootDocument = parent.document.rootDocument;
			}
			document.rootDocument = rootDocument;
			// 既存のカスタムScriptの利用を想定し、rootWindowも設定
			// @deprecated use document.rootDocument
			document.rootWindow = rootDocument;

			const name = uniqueId();
			document.targetName = name;

			let windowManager = null;
			if (!rootDocument.scriptContext["windowManager"]) {
				windowManager = {};
				rootDocument.scriptContext["windowManager"] = windowManager;
			} else {
				windowManager = rootDocument.scriptContext["windowManager"];
			}
			windowManager[name] = document;

			rootDocument.scriptContext["createModalFunction"].call(this, options.dialogs, name, function(){
				// サブモーダルウィンドウ化
				const subModals = $.find(options.controls);
				if (subModals.length) {
					$(subModals).subModalWindow();
				}
			});
		});
	};
})(jQuery);

/**
 * モーダルウィンドウ
 *
 * [オプション]
 * overlay: バックグランドコンテンツSelector
 * under: モーダルコンテンツSelector
 * dialogHeight: ダイアログの高さ
 * dialogWidth: ダイアログの幅
 * resizable: 最大化可能か
 */
(function($){
	let $trigger = null;
	$.fn.modalWindow = function(option){
		if (!this) return false;
		// モーダルウィンドウ内では実行しない
		if ($("body.modal-body").length != 0) return false;

		const defaults = {
			overlay : '#modal-dialog-root .modal-wrap',
			under : '#modal-dialog-root .modal-inner',
			dialogHeight: 735,
			dialogWidth: 750,
			resizable: true
		};
		const options = $.extend(defaults, option);

		return this.each(function(){
			const $this = $(this);
			const $document = $(document);
			const $window = $(window);
			const $overlay = $document.find(options.overlay);
			const $under = $document.find(options.under);
			const $frame = $("iframe", $under);

			$this.attr("targetName", $frame.attr("name"));

			const fade = {
				show : function() {
					$under.height(options.dialogHeight);
					$overlay.fadeIn(options.speed);
					$under.fadeIn(options.speed);
					scriptContext.overlayManager.addOverlay($overlay);
					$under.css({zIndex: scriptContext.overlayManager.nextZindex()});
				},
				hide : function() {
					$overlay.fadeOut(options.speed);
					$under.fadeOut(options.speed);
					$frame.attr("src", "about:blank");
					$("#modal-title").text("");
					$(".modal-restore", $under).click();
					scriptContext.overlayManager.removeOverlay($overlay);
				}
			};
			$this.on("click", function(){
				//ダイアログを起動したものをトリガーとして保持しておき、
				//maximize,restore,resizeHandlerから呼び出せるようにする。
				$trigger = $this;

				$under.removeClass("unresizable");
				if (options.resizable == false) {
					$under.addClass("unresizable");
				}

				fade.show();
				resizeHandler();
			});

			//maximize,restore,resizeHandlerで起動したtriggerにあわせてサイズ調整できるようfunction紐づけ
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
					//ダイアログを呼び出したトリガーに紐づくfunctionを呼び出し、
					//別のトリガーで指定したサイズにならないようにする
					$trigger.setModalWindowToCenter();
				});
				$under.on("click", ".modal-restore", function() {
					$under.removeClass("fullWindow");
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

				// ウインドウのリサイズでサイズ、配置調整
				$window.on("resize", resizeHandler);

				$under.attr("initialized", true);
			}

			function resizeHandler(e){
				$overlay.width(0);
				const width = $window.width() > $document.width() ? $window.width() : $document.width();
				$overlay.css({
					height : $document.height(),
//					width : $window.width(),
					width : width,
					top: 0,
					left: 0,
					position:"absolute"
				});

				// トリガーが指定されている場合は、トリガーのオプションで配置調整
				if ($trigger) {
					$trigger.setModalWindowToCenter();
				} else {
					setModalWindowToCenter();
				}
			}

			function setModalWindowToCenter(){
				if ($under.hasClass("fullWindow")) {
					const dialogHeight = $window.height() - 40;

					//frameはheader分減らす
					const frameHeight = dialogHeight - 49;

					$under.css({
						height : dialogHeight,
						width : $window.width() - 30,
						top: $document.scrollTop(),
						left: 0,
						marginLeft: 0
					});
					$frame.height(frameHeight);
				} else {
					const windowHeight = $window.height();
					const windowWidth = $window.width();

					let dialogHeight = options.dialogHeight;
					//windowの高さより大きい場合はwindowの高さに設定
					if (dialogHeight > (windowHeight -80)) {
						dialogHeight = windowHeight -80;
					}
					//最小高さを200
					if (dialogHeight < 200) {
						dialogHeight = 200;
					}

					//frameはheader分減らす
					const frameHeight = dialogHeight - 49;

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
 * dialogHeight: ダイアログの高さ
 * dialogWidth: ダイアログの幅
 * resizable: 最大化可能か
 */
(function($){
	let $trigger = null;
	$.fn.subModalWindow = function(option){
		if (!this) return false;

		const defaults = {
			dialogHeight: 735,
			dialogWidth: 750,
			resizable: true
		};

		const rootDocument = document.rootDocument;
		const targetName = document.targetName;
		const $frame = $("iframe[name='" + targetName + "']", rootDocument);
		const $under = $frame.parent();
		const $overlay = $under.prev();

		const options = $.extend(defaults, option);

		const fade = {
			show : function() {
				$under.height(options.dialogHeight);
				$overlay.fadeIn(options.speed);
				$under.fadeIn(options.speed);
				rootDocument.scriptContext.overlayManager.addOverlay($overlay);
				$under.css({zIndex: rootDocument.scriptContext.overlayManager.nextZindex()});
			},
			hide : function() {
//				$overlay.fadeOut(options.speed);
//				$under.fadeOut(options.speed);
				$overlay.fadeOut(0);
				$under.fadeOut(0);
				$frame.attr("src", "about:blank");
				$(".modal-restore", $under).click();
				rootDocument.scriptContext.overlayManager.removeOverlay($overlay);
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
				$under.addClass("fullWindow");
				$trigger.setModalWindowToCenter();
			});
			$under.on("click", ".modal-restore.sub-modal-restore", function(){
				$under.removeClass("fullWindow");
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

			// ルートウインドウのリサイズでサイズ、配置調整
			const $rootWindow = $(rootDocument.scriptContext.getWindow());
			$rootWindow.on("resize", resizeHandler);

			$under.attr("initialized", true);
		}

		return this.each(function(){
			const $this = $(this);

			$this.on("click", function(){
				// ダイアログを起動したものをトリガーとして保持しておき、
				// maximize,restore,resizeHandlerから呼び出せるようにする。
				$trigger = $this;

				$under.removeClass("unresizable");
				if (options.resizable == false) {
					$under.addClass("unresizable");
				}

				fade.show();
				resizeHandler();
			});

			//maximize,restore,resizeHandlerで、起動したtriggerにあわせてサイズ調整できるようfunction紐づけ
			$this.setModalWindowToCenter = setModalWindowToCenter;
		});

		function resizeHandler(){
			$overlay.width(0);
			$overlay.css({
				height : $(rootDocument).height(),
				width : $(rootDocument).width(),
				top: 0,
				left: 0,
				position:"absolute"
			});

			// トリガーが指定されている場合は、トリガーのオプションで配置調整
			if ($trigger) {
				$trigger.setModalWindowToCenter();
			} else {
				setModalWindowToCenter();
			}
		}

		function setModalWindowToCenter(){
			if ($under.hasClass("fullWindow")) {
				const $rootWindow = $(rootDocument.scriptContext.getWindow());
				const dialogHeight = $rootWindow.height() - 40;

				//frameはheader分減らす
				const frameHeight = dialogHeight - 49;

				$under.css({
					height : dialogHeight,
					width : $rootWindow.width() - 30,
					top: $rootWindow.scrollTop(),
					left: 0,
					marginLeft: 0
				});
				$frame.height(frameHeight);
			} else {
				const $rootWindow = $(rootDocument.scriptContext.getWindow());
				const windowHeight = $rootWindow.height();
				const windowWidth = $rootWindow.width();

				let dialogHeight = options.dialogHeight;
				//windowの高さより大きい場合はwindowの高さに設定
				if (dialogHeight > (windowHeight -80)) {
					dialogHeight = windowHeight -80;
				}
				//最小高さを200
				if (dialogHeight < 200) {
					dialogHeight = 200;
				}

				//frameはheader分減らす
				const frameHeight = dialogHeight - 49;

				$under.css({
					height: dialogHeight,
					width: options.dialogWidth,
					top: $rootWindow.scrollTop() + 20,
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
		} else if ("createTextRange" in item) {
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

							$(".fixHeight").fixHeight();
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
				hasPreviewFunc: function(offset, length, count, limit, notCount) {
					return notCount
						? offset > 0
						: offset > 0 && count > 0
				},
				hasNextFunc: function(offset, length, count, limit, notCount) {
					return notCount
						? hasNext = limit <= length
						: hasNext = limit < (count - offset)
				},
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

					hasPreview = options.hasPreviewFunc(offset, length, count, limit, notCount);
					hasNext = options.hasNextFunc(offset, length, count, limit, notCount);
					if (!notCount && tail > count) { tail = count; }

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
				hasNextFunc: function(offset, length, count, limit, notCount) {
					return length > limit;
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
				searchNameList(webapiName, defName, viewName, filterName, offset, function(list) {
					$pager.setPage(offset, list.length, null);
					// listのサイズを表示件数設定に従い補正
					if (list.length > limit) {
						list = list.slice(0, limit);
					}

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

			getReferenceComboSetting($v.getComboSettingWebapiName, $v.defName, $v.viewName, $v.propName, $v.viewType, $v.entityOid, $v.entityVersion, function(setting) {

				//連動コンボの設定がない場合は、連動できないので表示しない
				if (!setting || !setting.propertyName || setting.propertyName == "") return;

				//最後にコンボを選択するためのコールバック関数
				var func = null;
				if ($v.oid != "") {
					func = function() { $v.val($v.oid); }
				}
				//親コンボ生成
				initParent($v, setting, $v.propName, $v.oid, props, false, func);

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
		 * 検索画面のリセット処理
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

			getReferenceComboSetting($v.getComboSettingWebapiName, $v.defName, $v.viewName, $v.propName, $v.viewType, $v.entityOid, $v.entityVersion, function(setting) {

				//連動コンボの設定がない場合は、連動できないので表示しない
				if (!setting || !setting.propertyName || setting.propertyName == "") return;

				//最後にコンボを選択するためのコールバック関数
				var func = null;
				if ($v.oid != "") {
					func = function() { $v.val($v.oid); }
				}

				//親コンボリセット
				initParent($v, setting, $v.propName, $v.oid, props, true, func);

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
				//EntityView定義名
				defName:$v.attr("data-defName"),
				//EntityView View名
				viewName:$v.attr("data-viewName"),
				//EntityView プロパティ名(NestTableの場合は階層)
				propName:$v.attr("data-propName"),
				//EntityView Layoutタイプ
				viewType:$v.attr("data-viewType"),
				//編集画面 ルートEntityのoid
				entityOid:$v.attr("data-entityOid"),
				//編集画面 ルートEntityのversion
				entityVersion:$v.attr("data-entityVersion"),
				//参照コンボ選択リスト取得WebApi
				webapiName:$v.attr("data-webapiName"),
				//連動コンボ設定取得WebApi
				getComboSettingWebapiName:$v.attr("data-getComboSettingWebapiName"),
				//上位階層選択値取得WebApi
				searchParentWebapiName:$v.attr("data-searchParentWebapiName"),
				//検索画面のsc指定用
				prefix:$v.attr("data-prefix"),
				//検索画面の検索タイプ
				searchType:$v.attr("data-searchType"),
				//選択値
				oid:$v.attr("data-oid"),
				//選択最下層のプロパティパス
				upperName:$v.attr("data-upperName"),
				//選択最下層の選択値
				upperOid:$v.attr("data-upperOid"),
				//カスタムスタイル
				customStyle:$v.attr("data-customStyle")
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
		 * 
		 * @param $v 対象RefComboインスタンス
		 * @param setting 参照コンボ設定
		 * @param currentPath 現在のパス
		 * @param childOid 下層の選択値
		 * @param props コンボデータを検索する際のパラメータ生成用KEY値(階層ごとの名前を保持)
		 * @param reset リセットか
		 * @param callback コンボ生成後のコールバック処理
		 */
		function initParent($v, setting, currentPath, childOid, props, reset, callback) {
			var parentPath = currentPath + "." + setting.propertyName;
			props.push(parentPath);
			if (childOid == "") {
				var parentOid = "";
				var func = null;
				if (parentPath == $v.upperName && $v.upperOid != "") {
					parentOid = $v.upperOid;
					func = function($parent) {
						$parent.val(parentOid);
					}
				}
				if (setting.parent && setting.parent.propertyName && setting.parent.propertyName != "") {
					//更に上位がいれば先に生成
					initParent($v, setting.parent, parentPath, parentOid, props, reset, null);
				}

				createNode($v, parentPath, props, reset, func);
			} else {
				//子から親を検索
				searchParent($v.searchParentWebapiName, $v.defName, $v.viewName, $v.propName, $v.viewType, parentPath, childOid, $v.entityOid, $v.entityVersion, function(parentEntity) {
					var parentOid = getOid(parentEntity);
					if (setting.parent && setting.parent.propertyName && setting.parent.propertyName != "") {
						//更に上位がいれば先に生成
						initParent($v, setting.parent, parentPath, parentOid, props, reset, null);
					}

					var func = null;
					if (parentOid != "") {
						//階層を選択済みにする場合は階層データを読み込ませた後に選択対象指定
						func = function($parent) {
							$parent.val(parentOid);

							//最下層の一個上の場合は最下層を読み込んで選択状態を設定する
							if (callback && $.isFunction(callback)) {
								$parent.change();
								callback.call(this);
							}
						}
					}
					createNode($v, parentPath, props, reset, func);
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
		function createNode($v, name, props, reset, callback) {
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

			if (callback && $.isFunction(callback)) {
				//親が先に読み込まれてるはず、この階層のデータを読み込む
				loadReferenceData($v, props, function(){callback.call(this, $parent);});
			}
		}

		/*
		 * 参照データ読み込み
		 */
		function loadReferenceData($v, props, callback) {
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

				if (callback && $.isFunction(callback)) callback.call(this);
			});
		}
	};

	/**
	 * 参照コンボ(同期)
	 */
	$.fn.refComboSync = function() {
		if (!this) return false;

		return this.each(function(){
			const $this = $(this);
			init($this);
		});

		/*
		 * 初期化処理
		 */
		function init($v) {
			setup($v);
			
			const combos = $v.parent().children("select");
			const size = combos.length;
			const comboNames = [];
			for (let i = 0; i < size - 1; i++) {
				$(combos[i]).on("change", function() {
					//自分の階層以下を初期化
					$(this).nextAll("select").each(function() {
						$(this).val("");
						$(this).children("option[value!='']").remove();
					});
					if ($(this).val()) {
						loadReferenceData($v, comboNames, $(this).next("select"));
					}
				});

				comboNames.push($(combos[i]).attr("name"));
			}
		};

		/**
		 * 設定値取得
		 * 
		 * @param $v 対象Selectオブジェクト
		 */
		function setup($v) {
			//一括設定の場合、option指定できないのでattributeから取得
			$.extend($v, {
				//EntityView定義名
				defName:$v.attr("data-defName"),
				//EntityView View名
				viewName:$v.attr("data-viewName"),
				//EntityView プロパティ名(NestTableの場合は階層)
				propName:$v.attr("data-propName"),
				//EntityView Layoutタイプ
				viewType:$v.attr("data-viewType"),
				//編集画面 ルートEntityのoid
				entityOid:$v.attr("data-entityOid"),
				//編集画面 ルートEntityのversion
				entityVersion:$v.attr("data-entityVersion"),
				//参照コンボ選択リスト取得WebApi
				webapiName:$v.attr("data-webapiName"),
			});
		}

		/*
		 * 参照データ読み込み
		 * 
		 * @param $v 対象Selectオブジェクト
		 * @param comboNames 上位コンボ名
		 * @param $target 変更対象Selectオブジェクト
		 */
		function loadReferenceData($v, comboNames, $target) {
			const params = new Array();
			for (let i = 0; i < comboNames.length; i++) {
				let val = $("select[name='" + comboNames[i] + "']", $v.parent()).val();
				if (!val) val = "";
				params.push({key:comboNames[i], value:val});
			}
			if (typeof $v.entityOid !== "undefined" && typeof $v.entityVersion !== "undefined") {
				params.push({key: "entityOid", value: $v.entityOid});
				params.push({key: "entityVersion", value: $v.entityVersion});
			}

			refComboChange($v.webapiName, $v.defName, $v.viewName, $v.propName, params, $v.viewType, function(selName, entities) {
				if (entities == null || entities.length == 0) return;

				for (let i = 0; i < entities.length; i++) {
					const entity = entities[i];
					const optionData = $("<option/>").val(entity.oid).text(entity.name);
					$target.append(optionData);
				}
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
				radioTogglable:$v.attr("data-radioTogglable"),
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
						if ($v.radioTogglable) {
							$radio.addClass($v.radioTogglable);
						}
						$("<span />").text(entity.name).appendTo($label);

						$v.append($li);
					}

					//デザイン固有の処理があったら呼び出し
					if (applyDesignRefLinkRadio) {
						applyDesignRefLinkRadio($("input[type=radio]:not('.applied-pseudo')", $v));
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
				selectDynamicParamCallback:	$v.attr("data-selectDynamicParamCallback"),
				insertUrlParam:				$v.attr("data-insertUrlParam"),
				insertDynamicParamCallback:	$v.attr("data-insertDynamicParamCallback"),
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
				entityVersion:				$v.attr("data-entityVersion"),
				customStyle:				$v.attr("data-customStyle")
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
				var selDynamicParamCallback = scriptContext[$v.selectDynamicParamCallback];
				searchUniqueReference($v.attr("id"), $v.selectAction, $v.viewAction, $v.refDefName, $v.propName, $v.selectUrlParam, $v.refEdit, selRefCallback, this, $v.refViewName, $v.permitConditionSelectAll, $v.permitVersionedSelect, $v.defName, $v.viewName, $v.viewType, $v.refSectionIndex, $v.entityOid, $v.entityVersion, selDynamicParamCallback, $v.customStyle);
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
				var insDynamicParamCallback = scriptContext[$v.insertDynamicParamCallback];
				insertUniqueReference($v.attr("id"), $v.addAction, $v.viewAction, $v.refDefName, $v.propName, $v.multiplicity, $v.insertUrlParam, $v.defName, $v.viewName, $v.refEdit, insRefCallback, this, $v.viewType, $v.refSectionIndex, insDynamicParamCallback, $v.customStyle);
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
			dayLabelSelector: ".dp-weekday-label",
			pickerClearButtonText: scriptContext.gem.locale.date.pickerClearBtn,
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

		// クリアボタンを追加する
		const appendClearButton = function(input) {
			const $input = $(input);
			const $buttonPane = $input.datepicker('widget').find('.ui-datepicker-buttonpane');
			if ($buttonPane.length > 0) {
				// $buttonPane が存在していたら、クリアボタンを追加する
				$buttonPane.append(
					$('<button type="button" class="ui-datepicker-clear ui-datepicker-current ui-state-default ui-priority-secondary ui-corner-all"></button>')
						.text(options.pickerClearButtonText)
						.on('click', null, $input, function(event) {
							event.data.val('');
						})
				);
			}
		};

		// フォーカスイベントでクリアボタンを追加する
		// TODO ui 1.13 で onUpdateDatepicker を利用したイベント制御に変更した場合は、本メソッドは不要
		const appendClearButtonWithFocusEvent = function(input) {
			const $input = $(input);
			const $buttonPane = $input.datepicker('widget').find('.ui-datepicker-buttonpane');
			// focus イベントは、様々なタイミングで発生する。
			// picker 表示状態で focus イベントが発生し、ボタンが追加されることを抑制する。
			if ($buttonPane.length > 0 && $buttonPane.find('.ui-datepicker-clear').length === 0) {
				// クリアボタンが追加されていなければ、クリアボタンを追加する
				appendClearButton(input);
			}
		};

		// ファンクション実行結果を取得する
		const getFunctionCallResult = function(fnName, parent, input) {
			if (!fnName) {
				// ファンクション名の指定がない場合は終了
				return undefined;
			}
			// '.' で分割する
			const fnNameArr = fnName.split('.');
			// ファンクションとファンクションの親オブジェクトを取得する
			const fnArr = fnNameArr.reduce(function (pv, cv, idx, arr) {
				const curr = pv && pv[0];
				
				if (!curr) {
					return undefined;
				}
				// 最後の位置では以下のようになる
				// curr[cv] = ファンクション
				// curr     = 親オブジェクト
				return [curr[cv], curr];
			}, [parent, null]);
			
			const fn = fnArr && fnArr[0];
			const fnParent = fnArr && fnArr[1]; 
			if (!fn || !fnParent || 'function' !== typeof fn) {
				// 'function' !== typeof fn の場合は、 fn が function ではない
				return null;
			}
			// ファンクション実行
			return fn.call(fnParent, $(input));
		};

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

			// .datepicker selector の data-* 属性の取得
			const minDate = this.dataset.minDateFunction === 'true' ? getFunctionCallResult(this.dataset.minDate, window, this) : this.dataset.minDate;
			const maxDate = this.dataset.maxDateFunction === 'true' ? getFunctionCallResult(this.dataset.maxDate, window, this) : this.dataset.maxDate;
			const extendOptions = {
				minDate: minDate,
				maxDate: maxDate,
			};

			$this.datepicker({
				showOn: 'both',
				buttonImage: options.buttonImage,
				buttonText: options.buttonText,
				buttonImageOnly: true,
				showMonthAfterYear: true,
				showButtonPanel:$this.attr("data-showButtonPanel") == "true",//今日ボタン表示
				dateFormat: options.dateFormat,
				minDate: extendOptions.minDate,
				maxDate: extendOptions.maxDate,
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
				
				// クリアボタンを追加する
				// TODO ui 1.13 になれば、 onUpdateDatepicker が追加されるので、そちらで追加することが正しい
				appendClearButtonWithFocusEvent(this);
			});

			if (this.hasAttribute('readonly')) {
				// readonly テキストフィールドの場合、削除操作のみ受け付ける
				$this.on('keydown', function(keyboardEvent) {
					if (/(Delete)|(Backspace)/i.test(keyboardEvent.code)) {
						// Delete or Backspace キーであれば、入力値を空に設定する。
						this.value = '';
					}
				});
			}

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
			onChangeActual: function(){},
			pickerClearButtonText: scriptContext.gem.locale.date.pickerClearBtn,
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

		// クリアボタンを追加する
		const appendClearButton = function(input) {
			const $input = $(input);
			const $buttonPane = $input.datepicker('widget').find('.ui-datepicker-buttonpane');
			if ($buttonPane.length > 0) {
				// $buttonPane が存在していたら、クリアボタンを追加する
				$buttonPane.append(
					$('<button type="button" class="ui-datepicker-clear ui-datepicker-current ui-state-default ui-priority-secondary ui-corner-all"></button>')
						.text(options.pickerClearButtonText)
						.on('click', null, $input, function(event) {
							event.data.val('');
						})
				);
			}
		};
		
		// フォーカスイベントでクリアボタンを追加する
		// TODO ui 1.13 で onUpdateDatepicker を利用したイベント制御に変更した場合は、本メソッドは不要
		const appendClearButtonWithFocusEvent = function(input) {
			const $input = $(input);
			const $buttonPane = $input.datepicker('widget').find('.ui-datepicker-buttonpane');
			// focus イベントは、様々なタイミングで発生する。
			// picker 表示状態で focus イベントが発生し、ボタンが追加されることを抑制する。
			if ($buttonPane.length > 0 && $buttonPane.find('.ui-datepicker-clear').length === 0) {
				// クリアボタンが追加されていなければ、クリアボタンを追加する
				appendClearButton(input);
			}
		};

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

				// クリアボタンを追加する
				// TODO ui 1.13 になれば、 onUpdateDatepicker が追加されるので、そちらで追加することが正しい
				appendClearButtonWithFocusEvent(this);
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
			dayLabelSelector: ".dp-weekday-label",
			pickerClearButtonText: scriptContext.gem.locale.date.pickerClearBtn,
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

		// クリアボタンを追加する
		const appendClearButton = function(input) {
			const $input = $(input);
			const $buttonPane = $input.datepicker('widget').find('.ui-datepicker-buttonpane');
			if ($buttonPane.length > 0) {
				// $buttonPane が存在していたら、クリアボタンを追加する
				$buttonPane.append(
					$('<button type="button" class="ui-datepicker-clear ui-datepicker-current ui-state-default ui-priority-secondary ui-corner-all"></button>')
						.text(options.pickerClearButtonText)
						.on('click', null, $input, function(event) {
							event.data.val('');
						})
				);
			}
		};

		// フォーカスイベントでクリアボタンを追加する
		// TODO ui 1.13 で onUpdateDatepicker を利用したイベント制御に変更した場合は、本メソッドは不要
		const appendClearButtonWithFocusEvent = function(input) {
			const $input = $(input);
			const $buttonPane = $input.datepicker('widget').find('.ui-datepicker-buttonpane');
			// focus イベントは、様々なタイミングで発生する。
			// picker 表示状態で focus イベントが発生し、ボタンが追加されることを抑制する。
			if ($buttonPane.length > 0 && $buttonPane.find('.ui-datepicker-clear').length === 0) {
				// クリアボタンが追加されていなければ、クリアボタンを追加する
				appendClearButton(input);
			}
		};

		// ファンクション実行結果を取得する
		const getFunctionCallResult = function(fnName, parent, input) {
			if (!fnName) {
				// ファンクション名の指定がない場合は終了
				return undefined;
			}
			// '.' で分割する
			const fnNameArr = fnName.split('.');
			// ファンクションとファンクションの親オブジェクトを取得する
			const fnArr = fnNameArr.reduce(function (pv, cv, idx, arr) {
				const curr = pv && pv[0];
				
				if (!curr) {
					return undefined;
				}
				// 最後の位置では以下のようになる
				// curr[cv] = ファンクション
				// curr     = 親オブジェクト
				return [curr[cv], curr];
			}, [parent, null]);
			
			const fn = fnArr && fnArr[0];
			const fnParent = fnArr && fnArr[1]; 
			if (!fn || !fnParent || 'function' !== typeof fn) {
				// 'function' !== typeof fn の場合は、 fn が function ではない
				return null;
			}
			// ファンクション実行
			return fn.call(fnParent, $(input));
		};

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

			// .datetimepicker selector の data-* 属性の取得
			const minDate = this.dataset.minDateFunction === 'true' ? getFunctionCallResult(this.dataset.minDate, window, this) : this.dataset.minDate;
			const maxDate = this.dataset.maxDateFunction === 'true' ? getFunctionCallResult(this.dataset.maxDate, window, this) : this.dataset.maxDate;
			const extendOptions = {
				minDate: minDate,
				maxDate: maxDate,
			};

			$this.datetimepicker({
				showOn: 'both',
				buttonImage: options.buttonImage,
				buttonText: options.buttonText,
				buttonImageOnly: true,
				separator: " ",
				dateFormat: options.dateFormat,
				timeFormat : timeFormat,
				stepMinute: Number(stepMinute),	//数値に変換
				minDate: extendOptions.minDate,
				maxDate: extendOptions.maxDate,
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

				// クリアボタンを追加する
				// TODO ui 1.13 になれば、 onUpdateDatepicker が追加されるので、そちらで追加することが正しい
				appendClearButtonWithFocusEvent(this);
			});

			if (this.hasAttribute('readonly')) {
				// readonly テキストフィールドの場合、削除操作のみ受け付ける
				$this.on('keydown', function(keyboardEvent) {
					if (/(Delete)|(Backspace)/i.test(keyboardEvent.code)) {
						// Delete or Backspace キーであれば、入力値を空に設定する。
						this.value = '';
					}
				});
			}

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

(function ($) {
  /**
   * 参照選択フィルター
   */
  $.fn.refSelectFilter = function (option) {
    const defaults = {
		i18n: {
			pleaseSelect: scriptContext.gem.locale.common.pleaseSelect,
			noResult: scriptContext.gem.locale.reference.noResult,
			more: "...",
			moreOptionsValue: "__more__"
		},
    };
    const options = $.extend({}, defaults, option);

    return this.each(function () {
		const $container = $(this);
		setup($container);

		const $txt = $container.find("input");
		const $select = $container.find("select");
		const isMultiple = !!$select.prop("multiple");
		const keepSelection = $txt.data("researchPattern");
		const keepSelectionFlag = !(keepSelection && keepSelection === "CLEAR");

		// 状態（インスタンスごと）
		let loading = false;
		let lastKeyword = "";

		// ページング / 集合
		let serverOffset = 0;          // サーバーに渡す offset（元の返却件数を累積）
		let totalRawCount = 0;         // サーバーが返した総件数
		let canLoadMoreResults = false;// さらに検索できるか

		// 選択と結果
		let selectedMap = new Map();   // oid -> entity（挿入順を維持＝選択順／初期順）
		let resultEntities = [];       // 未選択の検索結果（重複除去かつ選択済みを除外）
		let excludeOids = []; 		   // 新しい検索するとき、除外したいOIDのリスト
		
		// 初期選択を selectedMap に取り込む
		updateSelectedMap();
		// 初期選択を 検索値を補完
		autoUpdateInputValue();

		function setup($v) {
			var $input = $v.find("input");
			$input.data({
				defName: $input.attr("data-defName"),
				viewName: $input.attr("data-viewName"),
				propName: $input.attr("data-propName"),
				viewType: $input.attr("data-viewType"),
				entityOid: $input.attr("data-entityOid"),
				entityVersion: $input.attr("data-entityVersion"),
				webapiName: $input.attr("data-webapiName"),
				researchPattern: $input.attr("data-researchPattern")
			});
		}

		/**
		 * 選択状態を同期
		 * @param {jQuery} $select セレクトボックスのjQueryオブジェクト
		 */
		function updateSelectedMap() {
			// 選択肢がない場合、すべて初期値にする
			if ($select.find("option").length === 0) {
				selectedMap = new Map();
				resultEntities = [];
				$txt.val("");
				lastKeyword = "";
				return;
			}

			if ($select.prop("multiple")) {
				handleMultipleSelect();
			} else {
				handleSingleSelect();
			}
		}

		function handleMultipleSelect() {
			selectedMap = new Map();
			resultEntities = [];
			$select.find("option").each(function () {
				const val = $(this).val();
				if (!val || val === options.i18n.moreOptionsValue) return;
				const oid = String(val || "");
				const code = $(this).attr("data-code") || "";
				const name = $(this).text() || "";

				if ($(this).is(":selected")) {
					selectedMap.set(oid, { oid, code, name });
				} else {
					resultEntities.push({ oid, code, name });
				}
			});
		}

		function handleSingleSelect() {
			selectedMap = new Map();
			resultEntities = [];
			const $opt = $select.find("option:selected").first();
			if ($opt.length > 0) {
				const oid = String($opt.val() || "");
				const code = $opt.attr("data-code") || "";
				const name = $opt.text() || "";
				selectedMap.set(oid, { oid, code, name });
				lastKeyword = code;
			}
		}

		/**
		 * 複数選択不可の場合、検索値を補完
		 */
		function autoUpdateInputValue() {
			if (!$select.prop("multiple")) {
				const $opt = $select.find("option:selected").first();
				if ($opt.length > 0) {
					const selectedCode = $opt.attr("data-code") || "";
					if (selectedCode) {
						lastKeyword = selectedCode
						$select.empty().append($opt.prop("selected", true))
						$txt.val(selectedCode);
					}
				}
			}
		}

		/**
		 * エンティティオプションを追加
		 * @param {Object} e - エンティティ
		 * @param {boolean} isSelected - 選択済みかどうか
		 */
		function appendEntityOption(e, isSelected) {
			const name = e.name != null ? String(e.name) : "";
			const code = e.code != null ? String(e.code) : "";
			const oid = e.oid != null ? String(e.oid) : "";
			const $opt = $("<option/>").val(oid).text(name).attr("data-code", code);
			if (isSelected) $opt.prop("selected", true);
			return $opt.get(0);
		}


		/**
		 * 選択済みオプションをレンダリングする。
		 * 単一選択（isMultiple === false）の場合は、selectedList の先頭要素のみを選択状態で追加し、
		 * 2件目以降は未選択のオプションとして追加する。
		 * 複数選択（isMultiple === true）の場合は、selectedList の全要素を選択状態で追加する。
		 * @param {Array} selectedList - 選択済みエンティティのリスト
		 * @param {boolean} isMultiple - 複数選択可能かどうか（単一選択時のみ先頭要素が選択状態になる）
		 */
		function renderSelectedOptions(selectedList, isMultiple, fragment) {
			if (!isMultiple) {
				fragment.appendChild(appendEntityOption(selectedList[0], true));
			} else {
				for (const e of selectedList) {
					fragment.appendChild(appendEntityOption(e, true));
				}
			}
		}
		
		/**
		 * 未選択オプションをレンダリング
		 * @param {Array} resultEntities - 未選択のエンティティ
		 * @param {boolean} isMultiple - 複数選択可能かどうか
		 * @param {DocumentFragment} fragment - 追加先のフラグメント
		 */
		function renderResultOptions(resultEntities, isMultiple, fragment) {
			if (!isMultiple && resultEntities.length > 1) {
				fragment.appendChild($("<option/>").val("").text(options.i18n.pleaseSelect).get(0));
			}

			for (const e of resultEntities) {
				fragment.appendChild(appendEntityOption(e, false));
			}
		}
		
		// レンダリング：'選択済み（先頭、選択順）' + '未選択結果（API順）'。
		function renderOptions() {

			const fragment = document.createDocumentFragment();
			const selectedList = Array.from(selectedMap.values());

			// 選択済選択肢
			if (selectedList.length > 0) {
				renderSelectedOptions(selectedList, isMultiple, fragment)
			}
			
			// 未選択選択肢
			if (resultEntities.length > 0) {
				renderResultOptions(resultEntities, isMultiple, fragment)
			}

			// 「もっと」：さらに読み込み可能な場合のみ表示
			if (canLoadMoreResults) {
				fragment.appendChild($("<option/>").val(options.i18n.moreOptionsValue).text(options.i18n.more).get(0));
			}

			$select.empty().append(fragment);
		}

		/**
		 * ローディング状態を設定
		 * @param {boolean} isLoading ローディング中かどうか
		 */
		function setLoading(isLoading) {
			loading = isLoading;
			$select.prop("disabled", isLoading);
			$txt.prop("disabled", isLoading);
		}


		/**
		 * 検索前の処理
		 * @param {boolean} isNewSearch 新しい検索かどうか
		 */
		function triggerSearch(isNewSearch) {
			if (loading) return;
			setLoading(true)

			const keyword = $txt.val().trim();
			if (keyword && isNewSearch && keyword === lastKeyword) {
				setLoading(false);
				return;
			}

			if (isNewSearch) {
				serverOffset = 0;
				resultEntities = [];
				lastKeyword = keyword;
				if (isMultiple && keepSelectionFlag) {
					excludeOids = selectedMap.size > 0 ? Array.from(selectedMap.keys()).map(String): [];
				} else {
					selectedMap = new Map()
					excludeOids = [];
				}
			}

			loadReferenceData($txt, $select);
		}

		// Enterで即時検索
		$txt.on("keydown", function (e) {
			if (e.key === "Enter") {
				e.preventDefault();
				triggerSearch(true);
			}
		});

		// blurで検索をトリガー
		$txt.on("blur", function () {
			triggerSearch(true);
		});

		// select の変更
		$select.on("change", function () {
			const val = $select.val();
			const selectedValues = Array.isArray(val) ? val : (val ? [val] : []);

			// 「もっと」をトリガー
			if (selectedValues.includes(options.i18n.moreOptionsValue)) {
				// 「もっと」の選択状態を解除
				$select.find("option[value='" + options.i18n.moreOptionsValue + "']").remove();
				// さらに読み込む（再描画を許可、選択済みは先頭のまま）
				triggerSearch(false);
				return;
			}

			// 選択状態を同期
			updateSelectedMap();
			autoUpdateInputValue();
		});

		function loadReferenceData($v, $target) {
			const keyword = $v.val().trim();
			const defName = $v.data("defName");
			const viewName = $v.data("viewName");
			const propName = $v.data("propName");
			const viewType = $v.data("viewType");
			const entityOid = $v.data("entityOid");
			const entityVersion = $v.data("entityVersion");
			const webapiName = $v.data("webapiName");

			const params = [];
			if (entityOid && entityVersion) {
				params.push({ key: "entityOid", value: entityOid });
				params.push({ key: "entityVersion", value: entityVersion });
			}
			if (excludeOids && excludeOids.length > 0) {
				params.push({ key: "excludeOid", value: excludeOids.join(",") }); 
			}
			params.push({ key: "keyword", value: keyword });
			params.push({ key: "offset", value: serverOffset }); 
            
			getSelectFilterItem(webapiName, defName, viewName, propName, params, viewType, function (entities, count) {
				try {
					const list = Array.isArray(entities) ? entities : [];
					// サーバーの元の戻り件数を加算して offset を進める（フロントでの重複除去による再取得を防ぐ）
					serverOffset += list.length;

					// 総数
					if (typeof count === "number" && !isNaN(count)) {
						totalRawCount = count;
					} else if (typeof count === "string" && !isNaN(Number(count))) {
						totalRawCount = Number(count);
					}

					// マージ：選択済みおよび既存を除外（oidで重複除去）
					const existOids = new Set(resultEntities.map(x => String(x.oid)));
					const selectedOids = new Set(Array.from(selectedMap.keys()).map(String));

					const filteredEntities = [];
					for (const e of entities) {
						const oid = e && e.oid != null ? String(e.oid) : "";
						if (!oid) continue;
						if (isMultiple && selectedOids.has(oid)) continue; // 選択済みは未選結果に入れない
						if (existOids.has(oid)) continue;                 // 未選結果を重複排除
						const name = e.name != null ? String(e.name) : "";
						const code = e.code != null ? String(e.code) : "";
						filteredEntities.push({ oid, name, code });
						existOids.add(oid);
					}
					resultEntities.push(...filteredEntities);

					// 未選択結果があり、さらに読み込みが可能な場合のみ「もっと」を表示
					canLoadMoreResults = (serverOffset < totalRawCount);
					renderOptions();
					autoUpdateInputValue();
				} finally {
					setLoading(false);
				}
			});
		}

		// 破棄
		$container.data("destroyRefSelectFilter", function () {
			$txt.off("keydown blur");
			$select.off("change");
		});
    });
  };
})(jQuery);