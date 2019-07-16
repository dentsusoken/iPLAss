/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

(function($){

	function Calendar() {
		this.defaultLangOption = {
			monthNames : ["January","February","March","April","May","June","July","August","September","October","November","December"],
			monthNamesShort: ["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"],
			dayNames:["Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"],
			dayNamesShort:["Sun","Mon","Tue","Wed","Thu","Fri","Sat"],
			dayNamesWidget:["Su","Mo","Tu","We","Th","Fr","Sa"],
			titleFormatMonth:"MMMM YYYY",
			titleFormatWeek:"D MMM YYYY",
			titleFormatDay:"ddd, D MMM YYYY",
			titleFormatWiget:"MM YYYY",
			columnFormatMonth:"ddd",
			columnFormatWeek:"ddd M/D",
			columnFormatDay:"dddd M/D",
			thisMonth:"This Month",
			thisWeek:"This Week",
			today:"today",
			buttonTextMonth:"Month",
			buttonTextWeek:"Week",
			buttonTextDay:"Day"
		};
	}

	$.extend(Calendar.prototype, {
		setLangOption : function(langoption) {
			$.extend(this.defaultLangOption , langoption || {});
		}
	});

	$.fn.calendarView = function(option) {

		var $this = $(this);

		var calendarInfo = {
				calendarName : $this.attr("calendarName"),
				defaultDispType : $this.attr("defaultDispType"),
				isTop : $this.attr("isTop") === "true",
				imagePath : $this.attr("imagePath"),
				addAction: $this.attr("addAction"),
				filterAction: $this.attr("filterAction"),
				defaultDate : $this.attr("defaultDate")
		};
		var calendarTypeArray = {
				month : "Month",
				agendaWeek : "Week",
				agendaDay : "Day"
		};
		var options = $.extend(calendarInfo, option);
		if (!this) return false;

		return this.each(function() {
			init(options);
		});
		var calendarOption;

		function init(options) {

			$this.on("mousemove", function(){
				// ドラッグでセルの複数指定を防ぐ為の処理
				return false;
			});

			// カレンダーオプション設定
			calendarOption = {
					header: {
						left: "title,prev,next today",
						right: !options.isTop ? "agendaDay,agendaWeek,month" : ""
					},
					defaultView: options.defaultDispType,
					monthNames: $.calendar.defaultLangOption.monthNames,
					monthNamesShort: $.calendar.defaultLangOption.monthNamesShort,
					dayNames: $.calendar.defaultLangOption.dayNames,
					dayNamesShort: $.calendar.defaultLangOption.dayNamesShort,
					fixedWeekCount: false,
					selectable: true,
					views: {
						month: {
							titleFormat: $.calendar.defaultLangOption.titleFormatMonth,
							columnFormat: $.calendar.defaultLangOption.columnFormatMonth
						},
						week: {
							titleFormat: $.calendar.defaultLangOption.titleFormatWeek,
							columnFormat: $.calendar.defaultLangOption.columnFormatWeek
						},
						day: {
							titleFormat: $.calendar.defaultLangOption.titleFormatDay,
							columnFormat: $.calendar.defaultLangOption.columnFormatDay
						}
					},
					buttonText: {
						month: $.calendar.defaultLangOption.buttonTextMonth,
						week:$.calendar.defaultLangOption.buttonTextWeek,
						day: $.calendar.defaultLangOption.buttonTextDay
					},
					allDayText: "",
					timeFormat: "H:mm",
					viewRender: function (view, element) {
						if (view.name != calendarOption.displayView) {
							$this.fullCalendar("destroy");
							resetFilterConditionMassageArea();
							calendarOption.defaultView = view.name;
							calendarOption.displayView = view.name;
							calendarOption.monthNames = $.calendar.defaultLangOption.monthNames;
							var height = null;
							switch (view.name) {
								case "month":
									calendarOption.buttonText.today = $.calendar.defaultLangOption.thisMonth;
									calendarOption.aspectRatio = 2.5;
									calendarOption.height = 600;
									break;
								case "agendaWeek":
									calendarOption.buttonText.today = $.calendar.defaultLangOption.thisWeek;
									calendarOption.slotDuration = "12:00:00";
									calendarOption.slotLabelFormat = "A";
									calendarOption.defaultTimedEventDuration = "01:00:00";
									calendarOption.contentHeight = 485; // 微妙に調整しないと下に変なエリアが出来たりスクロールしたり・・・
									delete calendarOption.aspectRatio;
									delete calendarOption.height;
									break;
					    		case "agendaDay":
									calendarOption.buttonText.today = $.calendar.defaultLangOption.today;
									calendarOption.slotDuration = "00:30:00";
									calendarOption.slotLabelFormat = "HH:mm";
									calendarOption.defaultTimedEventDuration = "00:30:00";
									calendarOption.aspectRatio = 1.35;
									calendarOption.height = 650;
					    			break;
					    		default : throw new IllegalStateException();
							}

							$this.fullCalendar(calendarOption);

						} else {
							calendarOption.startDate = view.start;
							calendarOption.endDate = view.end;

							loadCalendar(view.name, view.start, view.end);

							addList(view.name, view.start, view.end);

							calendarOption.defaultDate = view.start;
							calendarOption.selectDate = null;
						}
						if(!options.isTop) {
							addDateSpecifiedButton();
							addFilterButton();
						}

						$(".fixHeight").fixHeight();
					},
					eventRender: function (event, element, view) {

				    	if (event.time != null) {
				    		element.find(".fc-event-time").text(event.time);
				    	}
						element.click(function(){clickCalendarEvent(view.name, view.start, view.end, event.calendarEntityData);}).modalWindow();
					},
					dayClick: function (date, jsEvent, view) {

						if(options.isTop) {
							return;
						}

						// 時間を設定
						calendarOption.defaultDate = moment(date);

						// 予定追加エリアが表示されている時は閉じる
						var $span = $("span.add.open");
						if ($span.length> 0) {
							$span.removeClass("open");
							$span.next().removeClass("open");
						}

						var selectArea = $("td.fc-widget-content.fc-cell-overlay");
						if (selectArea) {
							selectArea.removeClass("fc-cell-overlay");
						}

						setSelectDate(view.name, date);
					}
				}

			// カレンダー表示

			if (options.defaultDate != "null") {
				calendarOption.defaultDate = moment(options.defaultDate);
			}

			$this.fullCalendar(calendarOption);
		}

		/**
		 * カレンダー情報取得
		 *
		 */
		function loadCalendar(calendarType, startDate, endDate) {
			getCalendarData("gem/calendar/getCalendar", calendarType, startDate.format("YYYYMMDD"), endDate.format("YYYYMMDD"), applyData);
		}

		/**
		 * カレンダー情報取得Ajax処理
		 */
		function getCalendarData(webapi, calendarType, fromDate, toDate, func) {

			var formList = document.scriptContext["filterCondition"];
			if (!formList) {
				formList = {};
			}

			formList.calendarName = options.calendarName;
			formList.from = fromDate;
			formList.to = toDate;
			formList.calendarType = calendarTypeArray[calendarType];

			postAsync(webapi, JSON.stringify(formList), function(results) {
				// カレンダーデータの削除
				$this.fullCalendar("removeEvents");
				var calendarData = results.calendarData;
				if (calendarData == null || calendarData.length == 0) {
					return;
				}

				if (func && $.isFunction(func)) func.call(this, calendarData);
			});
		}

		/**
		 * カレンダー情報取得Ajaxコールバック処理
		 */
		function applyData(calendarDataList) {
			$this.fullCalendar("addEventSource", calendarDataList );
		}

		/**
		 * カレンダー情報追加ボタン追加処理
		 *
		 */
		function addList(calendarType, startDate, endDate) {

			switch (calendarType) {

			case "month":
				addListMonth(calendarType, startDate, endDate);
				break;
			case "agendaWeek":
				addListAgendaWeek(calendarType, startDate, endDate);
				break;
			case "agendaDay":
				addListAgendaDay(calendarType, startDate, endDate);
				break;
			default : throw new IllegalStateException();

			}


			/**
			 * 月表示追加ボタン,日付リンク設定
			 */
			function addListMonth(calendarType, startDate, endDate) {
				var dayNumberList = $this.find(".fc-day-number");

				dayNumberList.each(function() {
					var $dayNumber = $(this);
				    var $td = $dayNumber.parent();
					if (!$td.is(".fc-other-month")) {
					    var dataDate = $td.attr("data-date");
					    var dataDateObj = moment(dataDate, "YYYY-MM-DD");

					    // 日付リンク設定
					    if (!options.isTop) {
					    	addLink($dayNumber, dataDateObj);
					    }

					    // 追加ボタン設定
					    $td.attr("id", "fc_div_" + dataDate)
						addButton($dayNumber, "", calendarType, startDate, endDate, dataDateObj);
					}
				});
			}

			/**
			 * 週表示設定
			 */
			function addListAgendaWeek(calendarType, startDate, endDate) {
				var dayHeaderList = $this.find(".fc-day-header");

				dayHeaderList.each(function(i, elem) {
					var $dayHeader = $(elem);
					var targetDay = moment(new Date(startDate.toDate().getTime())).add(i, "days");

				    // 日付リンク設定
					if (!options.isTop) {
						addLink($dayHeader , targetDay);
					}

				    // 追加ボタン設定
					addButton($dayHeader, "week", calendarType, startDate, endDate, targetDay);
				});
			}

			/**
			 * 日表示設定
			 */
			function addListAgendaDay(calendarType, startDate, endDate) {
				var $dayHeader = $this.find(".fc-day-header");
				if ($dayHeader.length != 0) {
					addButton($dayHeader, "day", calendarType, startDate, endDate, startDate);
				}
			}

			/**
			 * 日付リンク設定
			 */
			function addLink(element, targetDay) {
			    var dayText = element.text();
			    element.text("");
			    var $span = $("<span/>").addClass("calendar-day").text(dayText).appendTo(element);

			    $span.on("click", function() {
					$this.fullCalendar("destroy");

					calendarOption.defaultDate = targetDay;
					calendarOption.defaultView = "agendaDay";

					$this.fullCalendar(calendarOption);

					$(".fixHeight").fixHeight();
			    });

			}

			/**
			 * 追加ボタン設定
			 */
			function addButton(element, ulClassName, calendarType, startDate, endDate, now) {
				var calDefs = scriptContext.calendarDefs[options.calendarName];

				var add = $("<span />").appendTo(element).addClass("add");
				var link = $("<a />").appendTo(add).attr("href", "javascript:void(0)");
				var img = $("<img />").addClass("rollover").attr("src", options.imagePath + "/btn-add-01.png").appendTo(link);
				img.rollOverSet();

				var div = $("<div />").addClass("addList").appendTo(element);
				var ul =  $("<ul />").addClass(ulClassName).appendTo(div);
				var li
				$.each(calDefs, function(){
					li = $("<li />").appendTo(ul);
					var data = this;
					$("<a />").appendTo(li).attr("href", "javascript:void(0)").text(data.dispName).on("click", function() {
						addCalendar(data.defName, data.addAction, data.viewName, startDate, endDate, now, calendarType);
					}).modalWindow();
				});

				add.calendarAddList();
			}
		}

		/**
		 * カレンダー情報詳細表示設定
		 */
		function clickCalendarEvent(calendarType, startDate, endDate, data) {
			document.scriptContext["editReferenceCallback"] = function(entity) {
				closeModalDialog();
				loadCalendar(calendarType, startDate, endDate)
			};

			var viewAction = "gem/generic/detail/ref/view";
			if (data.viewAction && data.viewAction != "") viewAction = data.viewAction;
			if (data.viewName && data.viewName != "") viewAction = viewAction + "/" + data.viewName;
			var viewAction = contextPath + "/" + viewAction + "/" + data.defName + "/" + data.oid;

			var isSubModal = $("body.modal-body").length != 0;
			var target = getModalTarget(isSubModal);
			var form = $("<form />").attr({method:"POST", action:viewAction, target:target}).appendTo("body");
			$("<input />").attr({type:"hidden", name:"version", value:data.version}).appendTo(form);
			$("<input />").attr({type:"hidden", name:"refEdit", value:true}).appendTo(form);
			if (isSubModal) $("<input />").attr({type:"hidden", name:"modalTarget", value:target}).appendTo(form);
			form.submit();
			form.remove();
		}

		/**
		 * カレンダー情報新規登録画面設定
		 */
		function addCalendar(defName, action, viewName, startDate, endDate, now, calendarType) {
			document.scriptContext["editReferenceCallback"] = function(entity) {
				closeModalDialog();
				loadCalendar(calendarType, startDate, endDate);
			};

			var addAction = calendarInfo.addAction
			if (action && action != "") addAction = action;
			if (viewName && viewName != "") addAction = addAction + "/" + viewName;
			addAction = contextPath + "/" + addAction;

			var isSubModal = $("body.modal-body").length != 0;
			var target = getModalTarget(isSubModal);
			var form = $("<form />").attr({method:"POST", action:addAction, target:target}).appendTo("body");
			$("<input />").attr({type:"hidden", name:"defName", value:defName}).appendTo(form);
			$("<input />").attr({type:"hidden", name:"calendarName", value:options.calendarName}).appendTo(form);
			$("<input />").attr({type:"hidden", name:"date", value:now.format("YYYYMMDD")}).appendTo(form);
			$("<input />").attr({type:"hidden", name:"refEdit", value:true}).appendTo(form);
			if (isSubModal) $("<input />").attr({type:"hidden", name:"modalTarget", value:target}).appendTo(form);
			form.submit();
			form.remove();
		}

		/**
		 * 日付指定ボタン追加
		 * 不正な値が入っている時は現在日時で検索する（エラーにはならない）
		 */
		function addDateSpecifiedButton() {
			if($this.find(".targetDate").length == 0) {
				var $headerLeftBlock = $("<div />").addClass("targetDateBlock").appendTo($(".fc-left"));
				var $dateField = $("<input />").attr({type:"text", value:calendarOption.dateField}).addClass("datepicker-form-size-02 inpbr targetDate").appendTo($headerLeftBlock);
				$dateField.applyDatepicker({showErrorMessage:false,
										   onChangeActual:function(){
											   $(this).val(dateUtil.formatInputDate(new Date()));
											   $(this).change();
										   }});

				$dateField.on("change", function () {
					var value = $dateField.val();

					if (typeof value !== "undefined" && value != null && value != "") {
						loadDateSpecify($(this).val());
					}
				});

				$dateField.on("keypress", function (e) {
				    if ((e.which && e.which == 13) || (e.keyCode && e.keyCode == 13)) {

				    	try {
					    	validateDate($(this).val(), dateUtil.getInputDateFormat(), null);
					    	loadDateSpecify($(this).val());
						} catch (e) {
							$dateField.val(dateUtil.formatInputDate(new Date()));
							$dateField.change();
						}
				    }
				});

			}

			function loadDateSpecify(d) {
				var date = dateUtil.formatInputDate(d);

				$this.fullCalendar("destroy");

				calendarOption.defaultDate = moment(date);
				calendarOption.dateField = d;

				$this.fullCalendar(calendarOption);

				$(".fixHeight").fixHeight();
			}
		}


		/**
		 * 条件指定ボタン追加
		 */
		function addFilterButton() {
			if($this.find(".filterButtonBlock").length == 0) {
				var $filterButtonBlock = $("<div />").addClass("filterButtonBlock").appendTo($(".fc-left"));

				var button = $("<button />").addClass("fc-button fc-state-default fc-corner-left fc-corner-right calendarFilter").text(scriptContext.gem.locale.calendar.FILTER_APPOINT_BUTTON).appendTo($filterButtonBlock);
				var buttonDel = $("<button />").addClass("fc-button fc-state-default fc-corner-left fc-corner-right fc-state-disabled").text(scriptContext.gem.locale.calendar.FILTER_RELEASE_BUTTON).appendTo($filterButtonBlock);

				if ($("#filterConditionMassage").length == 0) {
					createFilterConditionMassageArea(calendarOption.filterConditionMassage, buttonDel);
				}

				button.click(function () {
					document.scriptContext["editReferenceCallback"] = function(formList, filterMessage) {
						closeModalDialog();
						document.scriptContext["filterCondition"] = {};
						var $filterConditionMassage = $("#filterConditionMassage");
						if ($filterConditionMassage.length == 0) {
							$filterConditionMassage = $("<div/>").attr("id", "filterConditionMassage");
							createFilterConditionMassageArea($filterConditionMassage, buttonDel);
						}

						var notEmptyFlg = false;
						for (var j in formList) {
							notEmptyFlg = true;
							break;
						}
						if(notEmptyFlg) {
							$filterConditionMassage.children().remove();
							$.each(filterMessage,function(){
								$("<div/>").text(this).appendTo($filterConditionMassage);
							});
							document.scriptContext["filterCondition"] = formList;
							// フィルター条件取得
							calendarOption.filterConditionMassage = $filterConditionMassage;
						} else {
							buttonDel.click();
						}

						loadCalendar(calendarOption.defaultView, calendarOption.startDate, calendarOption.endDate);
					};

					var addAction = contextPath + "/" + calendarInfo.filterAction;

					var isSubModal = $("body.modal-body").length != 0;
					var target = getModalTarget(isSubModal);
					$("#modal-title").text(scriptContext.gem.locale.calendar.FILTER_APPOINT_BUTTON);
					var form = $("<form />").attr({method:"POST", action:addAction, target:target}).appendTo("body");
					var filterCondition = $("#calendarFilterCondition"); // TODO これ見つからない、必要？
					if (filterCondition.length > 0) {
						var filterTitle = filterCondition.attr("title");
						if (filterTitle && filterTitle != "")$("<input />").attr({type:"hidden", name:"title", value:filterTitle}).appendTo(form);
						var filterDescription = filterCondition.attr("description");
						if (filterDescription && filterDescription != "")$("<input />").attr({type:"hidden", name:"description", value:filterDescription}).appendTo(form);
						var fDefName = filterCondition.attr("fDefName");
						if (fDefName && fDefName != "")$("<input />").attr({type:"hidden", name:"fDefName", value:fDefName}).appendTo(form);
						var filterName = filterCondition.attr("filterName");
						if (filterName && filterName != "")$("<input />").attr({type:"hidden", name:"filterName", value:filterName}).appendTo(form);
					}
					if (isSubModal) $("<input />").attr({type:"hidden", name:"modalTarget", value:target}).appendTo(form);
					$("<input />").attr({type:"hidden", name:"calendarName", value:options.calendarName}).appendTo(form);
					form.submit();
					form.remove();
				}).modalWindow();


				buttonDel.click(function () {
					if(!$(this).hasClass("fc-state-disabled")) {
						$(this).addClass("fc-state-disabled");
						resetFilterConditionMassageArea();
						document.scriptContext["filterCondition"] ={};
						calendarOption.filterConditionMassage = null;
						loadCalendar(calendarOption.defaultView, calendarOption.startDate, calendarOption.endDate);
					}
				});
			}
		}

		function createFilterConditionMassageArea($filterConditionMassage, buttonDel) {

			if ($filterConditionMassage == null || $filterConditionMassage.length == 0) {
				return;
			}

			var $filterConditionBlock = $("<div />").addClass("filterConditionBlock");
			$(".fc-clear").after($filterConditionBlock);

			var $sectionHead = $("<div/>").addClass("hgroup-03 sechead").appendTo($filterConditionBlock);
			$("<span/>").text(scriptContext.gem.locale.calendar.FILTER_COND_EXIST).appendTo($("<h3/>").appendTo($sectionHead));
			$sectionHead.after($filterConditionMassage).sectoinToggle().click();

			buttonDel.removeClass("fc-state-disabled");
		}

		function resetFilterConditionMassageArea() {
			$(".filterConditionBlock").remove();
		}

		function setSelectDate(calendarType, selectDate) {
			var target
			switch (calendarType) {
				case "month":
					target= $("td.fc-day-top[data-date=\"" + selectDate.format("YYYY-MM-DD")  + "\"]");
					break;
				case "agendaWeek":
					target = $("td.fc-col" + selectDate.get("day") + ".fc-widget-content");
					break;
				case "agendaDay":
					break;
				default : throw new IllegalStateException();
			}

			if (target) {
				//target.addClass("fc-highlight");
			}
		}
	};

	$.fn.calendarWidgetView = function(option) {

		var $this = $(this);

		var calendarInfo = {
				calendarName : $this.attr("calendarName"),
				displayName : $this.attr("displayName"),
				action : $this.attr("action")
		};

		init();

		function init() {
			var $table = $("#calendarWidgetTable_" + es(calendarInfo.calendarName));

			var datestr = getCookie(calendarInfo.calendarName);
			var sysDate;
			if (datestr != "") {
				sysDate = moment(datestr);
			} else {
				sysDate = moment(new Date());
			}
			buildMonth(sysDate.get("year"), sysDate.get("month"), $table);
			$table.find(".prev").on("click", function() {prevNextMonth(-1)});
			$table.find(".next").on("click", function() {prevNextMonth(1)});
			buildWeekTitle($table);

			/**
			 * 月テーブルの構築
			 */
			function buildMonth(year, month, table) {
				//最終週を算出して月初から構築
				var m = moment().year(year).month(month).endOf("month");
				var lastWeek = weekOfMonth(m);

				var formatDate = m.format("YYYYMMDD");
				$this.children(".title").text(calendarInfo.displayName);
				table.find(".month-title").text(buildMonthTitle(m));
				table.attr("date", formatDate);
				setCookie(calendarInfo.calendarName, m.format("YYYYMMDD"), 0);
				for (var numberOfWeek = 0; numberOfWeek < lastWeek; numberOfWeek++) {
					buildWeekOfMonth(year, month, numberOfWeek, table);
				}
				loadWidetData(formatDate);
			}

			function weekOfMonth(m) {
				var startDay = new Date(m.get("year"), m.get("month"), 1).getDay();
				return Math.floor((m.get("date") + startDay - 1) / 7) + 1;
			}

			/**
			 * 月タイトルの作成
			 */
			function buildMonthTitle(m) {
				var str;
				str = $.calendar.defaultLangOption.titleFormatWiget;
				str = str.replace("YYYY", m.get("year"));
				str = str.replace("MM", $.calendar.defaultLangOption.monthNames[m.get("month")]);

				return str;
			}

			/**
			 * 週タイトルの作成
			 */
			function buildWeekTitle(table) {
				table.children("thead").each(function(){
					var tr = $("<tr />").appendTo(this);
					$.each($.calendar.defaultLangOption.dayNamesWidget, function(){
						$("<th />").text(this).appendTo(tr);
					});
				});
			}

			/**
			 * 日付の作成
			 */
			function buildWeekOfMonth(year, month, week, table) {
				var current = moment(sysdate, "YYYYMMDDHHmmss");

				var m = moment().year(year).month(month).date(1);
				if (week > 0) m.add(7 * week, "days");
				m.add(- m.get("day"), "days"); // 日曜の日付をセット

				table.children("tbody").each(function(){
					var tr = $("<tr />").appendTo(this).addClass("week");
					for (var i = 0; i < 7; i++) {
						var ymd = m.format("YYYYMMDD");
						var id = calendarInfo.calendarName + "_" + ymd;
						var $td = $("<td />").appendTo(tr);

						// 当日か
						if (current.isSame(m)) {
							$td.addClass("today");
						}

						//当月外か
						if (month != m.get("month")) {
							$td.addClass("other");
						} else {
							$("<span />").attr("id", id).appendTo($td).addClass("date").text(m.get("date"));
						}

						m.add(1, "days");
					}
				});
			}

			/**
			 * 次月、前月移動処理
			 */
			function prevNextMonth(addMonth) {
				var m = moment($table.attr("date"), "YYYYMMDD").add(addMonth, "month");
				setCookie(calendarInfo.calendarName, m.format("YYYYMMDD"), 0);
				$table.children("tbody").remove();
				$("<tbody />").appendTo($table);
				buildMonth(m.get("year"), m.get("month"), $table);
			}

			/**
			 * ウィジェットデータ取得処理
			 */
			function loadWidetData(targetDate) {
				getCalendarWidget("gem/calendar/getCalendarWidget", targetDate, applyWidgetData);
			}

			/**
			 * ウィジェットデータ取得Ajax処理
			 */
			function getCalendarWidget(webapi, targetDate, func) {
				var params = "{";
				params += "\"calendarName\":\"" + calendarInfo.calendarName + "\"";
				params += ",\"targetDate\":\"" + targetDate + "\"";
				params += "}";
				postAsync(webapi, params, function(results) {
					var calendarWidgetList = results.calendarWidgetList;
					if (calendarWidgetList == null || calendarWidgetList.length == 0) {
						return;
					}

					if (func && $.isFunction(func)) func.call(this, calendarWidgetList);
				});
			}

			/**
			 * ウィジェットデータ取得callback処理
			 */
			function applyWidgetData(calendarWidgetList) {
				$.each(calendarWidgetList, function(){
					var $this = this;
					var $obj = $("#" + es(calendarInfo.calendarName) + "_" + this);
					$obj.addClass("active");
					$obj.on("click", function (){dayClick($this)});
				});
			}

			/**
			 * 日付押下処理
			 */
			function dayClick(date) {
				clearMenuState();
				var params = {calendarName : calendarInfo.calendarName,
							targetDate : date,
							calendarType :"agendaDay"
							};
				submitForm(calendarInfo.action, params);
			}

		}
	};

	$.fn.calendarFilterView = function(calendarName) {

		var $this = $(this);
		if (calendarName) {
			build();
		}

		/**
		 * エンティティごとのフィルター項目行を生成する
		 */
		function build() {

			getCalendarFilterData("gem/calendar/ref/getCalendarFilter", createFilterTable);

			/**
			 * カレンダーフィルター情報取得Ajax処理
			 */
			function getCalendarFilterData(webapi, func) {
				var params = "{";
				params += "\"calendarName\":\"" + calendarName + "\"";
				params += "}";
				postAsync(webapi, params, function(results) {
					// カレンダーフィルターの生成
					var calendarFilterData = results.calendarFilterData;
					if (calendarFilterData == null || calendarFilterData.length == 0) {
						return;
					}

					if (func && $.isFunction(func)) func.call(this, calendarFilterData);
				});
			}

			function createFilterTable(calendarFilterData) {
				$.each(calendarFilterData, function () {
					var $data = this;

					// タイトル
					$("<h3/>").addClass("calendarConditonTitle").text(getMultilingualString($data.entityDefinition.displayName, $data.entityDefinition.localizedDisplayNameList)).appendTo($this);
					// tabMenu作成
					var tabWrap = $("<div/>").addClass("tab-wrap").appendTo($this);
					var tabListSearch = $("<div/>").addClass("tabList tabList-search-01").appendTo(tabWrap);
					var tabMenu = $("<ul/>").addClass("tab-menu").appendTo(tabListSearch);
					var boxSearch = $("<div/>").addClass("box-search-01").appendTo(tabWrap);

					// 通常フィルター設定
					$("<a/>").attr("href", "#").text(scriptContext.gem.locale.calendar.FILTER).appendTo(detailTab = $("<li/>").addClass("detail").appendTo(tabMenu));
					createDeepFilter(boxSearch, $data);

					// 定型フィルター設定
					var fixTab;
					if ($data.entityFilterIteList && $data.entityFilterIteList.length > 0) {
						$("<a/>").attr("href", "#").text(scriptContext.gem.locale.calendar.ROU_FILTER).appendTo($("<li/>").addClass("fixed").appendTo(tabMenu));
						createFixFilter(boxSearch, $data);
					}

					// 初期のフィルターのタブを設定する
					var filterCondition = parent.document.scriptContext["filterCondition"];
					var cunt = 0;
					if (filterCondition) {
						var entityFilter = filterCondition[$data.entityDefinition.name];
						if (entityFilter && entityFilter.fixFilter) {
							cunt = 1;
						}
					}

					tabWrap.switchCondition({cunt:cunt});

					var $without = $("<input/>").attr({defName : $data.entityDefinition.name, type : "checkbox"}).addClass("without-entity");
					$("<label />").text(messageFormat(scriptContext.gem.locale.calendar.WITHOUT_ENTITY, $data.entityDefinition.displayName)).append($without).appendTo($("<div />").addClass("excludeCondition").appendTo(tabWrap));
				});

				// 検索ボタン追加
				$("<input />").attr({id:"calendar_search_button", type:"button", value:scriptContext.gem.locale.calendar.search}).addClass("gr-btn").appendTo($this);

				// 初期値設定
				initFilterCondition();
			}

			/**
			 * 通常フィルターの作成
			 */
			function createDeepFilter(boxSearch, calendarFilterData) {
				var deepSaerch = $("<div/>").addClass("data-deep-search tab-panel").appendTo(boxSearch);
				var $form = $("<form/>").attr({defName:calendarFilterData.entityDefinition.name,
											   displayName:getMultilingualString(calendarFilterData.entityDefinition.displayName, calendarFilterData.entityDefinition.localizedDisplayNameList)})
											   .appendTo(deepSaerch);
				var $table = $("<table/>").addClass("tbl-search-01 calendar-filter").appendTo($form);

				// ヘッダー作成
				var $tr = $("<tr/>").appendTo($("<thead/>").appendTo($table));
				$("<th/>").addClass("col1").appendTo($tr);
				$("<th/>").addClass("col2").text(scriptContext.gem.locale.calendar.SEARCH_ITEM).appendTo($tr);
				$("<th/>").addClass("col3").text(scriptContext.gem.locale.calendar.COND).appendTo($tr);
				$("<th/>").addClass("col4").text(scriptContext.gem.locale.calendar.KEYWORD).appendTo($tr);
				$("<p/>").addClass("btn-toggle-01 tp02 add").attr("title", scriptContext.gem.locale.calendar.ADD_ITEM_BOTTOM).text("＋").appendTo($("<th/>").addClass("col5").appendTo($tr));

				// ダミーフィルター作成
				var $tr = $("<tr/>").addClass("dummy-filter-condition display-none").appendTo($("<tbody/>").appendTo($table));
				$("<span/>").addClass("filter-index").appendTo($("<td/>").appendTo($tr));
				var propertySelect = $("<select/>").addClass("form-size inpbr filter-property").appendTo($("<td/>").appendTo($tr));
				var conditionSelect = $("<select/>").addClass("form-size inpbr filter-condition").appendTo($("<td/>").appendTo($tr));
				var $from = $("<span/>").addClass("data-range-from").appendTo($("<td/>").addClass("filter-input-field").appendTo($tr));
				$("<input/>").attr("type", "text").addClass("inpbr").appendTo($from);
				$("<p/>").addClass("btn-toggle-01 delete").text("－").appendTo($("<td/>").appendTo($tr));

				// プロパティリストの作成
				createPropertyList(propertySelect, calendarFilterData.calendarFilterPropertyItemList, calendarFilterData.entityDefinition.name);

				// 条件リストの作成
				createConditionList(conditionSelect);

				// イベント設定
				deepFilterFunc($table);
			}

			function createPropertyList(propertySelect, propertylist, defName) {
				var pleaseSelectLabel = "";
				if (scriptContext.locale.showPulldownPleaseSelectLabel === true) {
					pleaseSelectLabel = scriptContext.gem.locale.common.pleaseSelect;
				}
				$("<option/>").attr({value : "", type : ""}).text(pleaseSelectLabel).appendTo(propertySelect);
				$.each(propertylist, function () {
					var propertyName = this.propertyName;
					var propertyType = this.propertyType;
					if (!propertyType || propertyType == "BINARY" || propertyType == "LONGTEXT") {
						return true;
					}

					$("<option/>").attr({value : propertyName, type : this.propertyType}).text($(this).attr("displayName")).appendTo(propertySelect);

					var editors = scriptContext["calendar_filter_editors"];
					if (!editors || editors == null) {
						editors = new Array();
						scriptContext["calendar_filter_editors"] = editors;
					}
					editors[defName + "_" + propertyName] = this.propertyEditor;
				});
			}

			function createConditionList(conditionSelect, type) {
				var dispLike = type ? dispLikeOption(type) : false;
				var dispIn = type ? dispInOption(type) : false;
				var dispCompare = type ? dispCompareOption(type) : false;
				$("<option />").attr({value:"EQ"}).text(scriptContext.locale.EQ).appendTo(conditionSelect);
				$("<option />").attr({value:"NE"}).text(scriptContext.locale.NE).appendTo(conditionSelect);
				if (dispLike) {
					$("<option />").attr({value:"SW"}).text(scriptContext.locale.SW).appendTo(conditionSelect);
					$("<option />").attr({value:"LW"}).text(scriptContext.locale.LW).appendTo(conditionSelect);
					$("<option />").attr({value:"IC"}).text(scriptContext.locale.IC).appendTo(conditionSelect);
					$("<option />").attr({value:"NIC"}).text(scriptContext.locale.NIC).appendTo(conditionSelect);
				}
				if (dispIn)$("<option />").attr({value:"IN"}).text(scriptContext.locale.IN).appendTo(conditionSelect);
				if (dispCompare) {
					$("<option />").attr({value:"LT"}).text(scriptContext.locale.LT).appendTo(conditionSelect);
					$("<option />").attr({value:"GT"}).text(scriptContext.locale.GT).appendTo(conditionSelect);
					$("<option />").attr({value:"LE"}).text(scriptContext.locale.LE).appendTo(conditionSelect);
					$("<option />").attr({value:"GE"}).text(scriptContext.locale.GE).appendTo(conditionSelect);
					$("<option />").attr({value:"RG"}).text(scriptContext.locale.RG).appendTo(conditionSelect);
				}
				$("<option />").attr({value:"NNL"}).text(scriptContext.locale.NNL).appendTo(conditionSelect);
				$("<option />").attr({value:"NL"}).text(scriptContext.locale.NL).appendTo(conditionSelect);
			}

			/**
			 * 定型フィルターの作成
			 */
			function createFixFilter(boxSearch, calendarFilterData) {
				var deepSaerch = $("<div/>").addClass("data-fixed-search tab-panel").appendTo(boxSearch);
				var $form = $("<form/>").attr({name:"fixedForm",
											   defName:calendarFilterData.entityDefinition.name,
											   displayName:getMultilingualString(calendarFilterData.entityDefinition.displayName, calendarFilterData.entityDefinition.localizedDisplayNameList)})
											   .appendTo(deepSaerch);
				var $table = $("<table/>").addClass("tbl-search-01 calendar-filter").appendTo($form);
				var $ul = $("<ul/>").addClass("list-radio-01").appendTo($("<td/>").appendTo($("<tr/>").appendTo($table)));
				$.each(calendarFilterData.entityFilterIteList, function(){
					var $li = $("<li/>").appendTo($ul);
					var $label = $("<label />").attr("for", "filter_" + $(this).attr("name")).appendTo($li);
					$("<input />").attr({type:"radio", value:$(this).attr("name"), name:calendarFilterData.entityDefinition.name + "_fixFilter", displayName:$(this).attr("displayName")}).appendTo($label);
					$label.append($(this).attr("displayName"));
				});
			}

			/**
			 * フィルター条件があるときの初期設定
			 */
			function initFilterCondition() {
				var formList = parent.document.scriptContext["filterCondition"];
				if (!formList || formList.length == 0) {
					return;
				}

				$("form").each(function(){
					var $form = $(this);
					var defName = $form.attr("defName");
					var formData = formList[defName];

					if (formData) {
						if (formData.withoutEntity){
							$(".without-entity[defname=\"" + defName + "\"]").prop("checked", true);
							return true;
						}


						if (!$form.attr("name") && formData.valueList && formData.valueList.length > 0) {
							$.each(formData.valueList, function(s){
								var $data = $(this);
								var filterLine;
								if(s == 0) {
									filterLine = $(".filter-line", $form);
								} else {
									 $(".add", $form).click();
									 filterLine = $(".filter-line:last", $form);
								}

								var filterProperty = $(".filter-property", filterLine);
								var filterCondition = $(".filter-condition", filterLine);
								var filterInputField = $(".filter-input-field", filterLine);
								var type = $data.attr("property").type;


								filterProperty.val($data.attr("property").value);
								filterProperty.change();
								filterCondition.val($data.attr("condition").value);
								filterCondition.change();

								if ($data.attr("condition").value == "NL"|| $data.attr("condition").value == "NNL") {
									return true;
								}

								if (type == "SELECT" || type == "BOOLEAN") {
									var fromList = $(".data-range-from input", filterLine);
									var keywordValue = $data.attr("keyword")["data-range-from"];
									var kvalueList = keywordValue.split(",");
									$.each(kvalueList, function(){
										var val = this;
										fromList.each(function(){
											if (val == $(this).val()) {
												$(this).prop("checked", true);
												return true;
											}
										});
									})
								} else {
									var from = $(".data-range-from input", filterLine);
									var to = $(".data-range-to input", filterLine);
									if (type == "DATE" || type == "DATETIME" || type == "TIME") {
										from.val($data.attr("keyword")["data-range-from-display"]);
										from.attr("data-prevalue", $data.attr("keyword")["data-range-from"]);
										if ($data.attr("condition").value == "RG") {
											to.val($data.attr("keyword")["data-range-to-display"]);
											to.attr("data-prevalue", $data.attr("keyword")["data-range-to"]);
										}
									} else {
										from.val($data.attr("keyword")["data-range-from"]);
										if ($data.attr("condition").value == "RG") {
											to.val($data.attr("keyword")["data-range-to"]);
										}
									}


								}

							});
						} else {
							var fixFilterList = $("input[type='radio']", $form);
							var fixFilter = formData.fixFilter;
							fixFilterList.each(function(){
								if ($(this).val() == fixFilter) {
									$(this).prop("checked", true);
								}
							});
						}
					}

				});
			}

			/**
			 * 通常フィルターのイベント設定
			 */
			function deepFilterFunc($table) {

				var addBtn = $(".add", $table);
				var deleteBtn = $(".delete", $table);
				var dummy = $(".dummy-filter-condition", $table);
				var $to = $("<span/>").addClass("data-range-to");


				addBtn.on("click", function(event, filterLine) {
					// ダミー行をコピーし、項番を付ける
					filterLine = dummy.clone(true);
					filterLine.insertBefore(dummy).removeClass("dummy-filter-condition display-none");
					filterLine.insertBefore(dummy).addClass("filter-line");
					$(".filter-index", filterLine).text($(".filter-line", $table).length);

					// プロパティ変更アクション追加
					propertyChangeFunc(filterLine);

					conditionChangeFunc(filterLine);

					return filterLine;
				});

				deleteBtn.on("click", function() {
					// 該当行を削除し、項番をつけなおす
					var filterLineCount = $(".filter-line", $table).length;

					if (filterLineCount != 1) {
						var filterLine = $(this).parents("tr");
						filterLine.remove();

						$(".filter-index", $table).each(function(i) {
							$(this).text(i + 1);
						});
					}
				});
				addBtn.click();
			}

			/**
			 * フィルタープロパティ、条件変更時処理
			 * （入力値はリセットする）
			 */
			function propertyChangeFunc(filterLine) {
				var defName = filterLine.parents("form").attr("defName");
				var filterProperty = $(".filter-property", filterLine);
				var filterCondition = $(".filter-condition", filterLine);
				var filterInputField = $(".filter-input-field", filterLine);

				filterProperty.on("change", function () {
					var selectValue = $(this).val();
					var selectType = $("option:selected", $(this)).attr("type")
					// 条件の初期化
					filterCondition.children().remove();
					// 条件の生成
					filterCondition.attr({property : selectValue, type : selectType});
					createConditionList(filterCondition, selectType);
					filterCondition.val("EQ");
					createInputField(filterInputField, selectValue, selectType, "EQ");
				});
			}

			function conditionChangeFunc(filterLine) {
				var filterProperty = $(".filter-property", filterLine);
				var filterCondition = $(".filter-condition", filterLine);
				var filterInputField = $(".filter-input-field", filterLine);

				filterCondition.on("change", function () {

					var selectValue = $(this).val();
					var propertyType = $(this).attr("type");

					if (propertyType == "SELECT") {
						var selectType = $(".data-range-from", filterInputField).attr("selectType");
						if ((selectValue == "IN" && selectType == "radio")
							|| (selectValue != "IN" && selectType == "checkbox")) {
							var defName = filterLine.parents("form").attr("defName");
							var propertyName = filterCondition.attr("property");
							var editor = scriptContext["calendar_filter_editors"][defName + "_" + propertyName];

							filterInputField.children().remove();
							createSelectField(filterInputField, editor, filterProperty.val(), selectValue);
						}
					} else {
						var $from = $(".data-range-from", filterInputField);
						var $to = $(".data-range-to", filterInputField);
						if (selectValue == "NNL" || selectValue == "NL") {
							if (!$from.hasClass("display-none")) {
								$from.addClass("display-none");
							}
							if (!$to.hasClass("display-none")) {
								$to.addClass("display-none");
							}
						} else {
							$from.removeClass("display-none");
							if (selectValue == "RG" && $to.length > 0) {
								$to.removeClass("display-none");
							} else {
								if (!$to.hasClass("display-none")) {
									$to.addClass("display-none");
								}
							}
						}
					}
				});
			}

			/**
			 * 入力値リセット処理
			 */
			function createInputField(filterInputField, propertyName, type, condition) {
				filterInputField.children().remove();
				if (!propertyName || !type) {
					createStringFiled(filterInputField)
				} else {

					var defName = filterInputField.parents("form").attr("defName");
					var editor = scriptContext["calendar_filter_editors"][defName + "_" + propertyName];
					switch(type) {

						// 文字列入力フィールド
						case "AUTONUMBER":
						case "STRING":
						case "REFERENCE":
						case "EXPRESSION":
							createStringFiled(filterInputField);
							break;

						// 数値入力フィールド
						case "INTEGER":
						case "FLOAT":
						case "DECIMAL":
							createInterFiled(filterInputField);
							break;

						// 日付入力フィールド
						case "DATE":
							createDateField(filterInputField);
							break;

						// 日時入力フィールド
						case "DATETIME":
						case "TIME":
							createTimestampField(filterInputField, editor, type);
							break;

						// 真偽値入力フィールド
						case "BOOLEAN":
							createBooleanFiled(filterInputField, editor, propertyName);
							break;

						// 選択入力フィールド
						case "SELECT":
							createSelectField(filterInputField, editor, propertyName, condition);
							break;
						default : throw new IllegalStateException();
					}
				}
			}

			/**
			 * 文字列の入力フィールドを作成します。
			 */
			function createStringFiled(filterInputField) {
				var $from = $("<span/>").addClass("data-range-from").appendTo(filterInputField);
				$("<input/>").attr("type", "text").addClass("inpbr").appendTo($from);
				var $to = $("<span/>").addClass("data-range-to display-none").text(scriptContext.locale.fromTo).appendTo(filterInputField);
				$("<input/>").attr("type", "text").addClass("inpbr").appendTo($to);
			}

			/**
			 * 数値の入力フィールドを作成します。
			 */
			function createInterFiled(filterInputField) {
				var $from = $("<span/>").addClass("data-range-from").appendTo(filterInputField);
				var $inputForm = $("<input/>").attr("type", "text").addClass("inpbr").appendTo($from);
				$inputForm.on("blur", function(){
					numcheck(this);
				});
				var $to = $("<span/>").addClass("data-range-to display-none").text(scriptContext.locale.fromTo).appendTo(filterInputField);
				var $inputTo = $("<input/>").attr("type", "text").addClass("inpbr").appendTo($to);
				$inputTo.on("blur", function(){
					numcheck(this);
				});
			}

			/**
			 * 日付のフィールド作成
			 */
			function createDateField(filterInputField) {

				var $from = $("<span/>").addClass("data-range-from").appendTo(filterInputField);
				var $inputFrom = $("<input/>").attr("type", "text").addClass("datepicker-form-size-02 inpbr").appendTo($from);
				$inputFrom.applyDatepicker();
				var $to = $("<span/>").addClass("data-range-to display-none").text(scriptContext.locale.fromTo).appendTo(filterInputField);
				var $inputTo = $("<input/>").attr("type", "text").addClass("datepicker-form-size-02 inpbr").appendTo($to);
				$inputTo.applyDatepicker();
			}

			/**
			 * 日時のフィールド作成
			 */
			function createTimestampField(filterInputField, editor, type) {
				var dispRange = editor.dispRange
				if (dispRange == "NONE" && type == "DATETIME") {
					 createDateField(filterInputField);
					 return;
				}


				//時間のフォーマットと最大文字長
				var fixedHourFlg = false;
				var fixedMinFlg = false;
				var fixedSecFlg = false;
				if (dispRange == "SEC" || !dispRange) {
					tmFormat = "HH:mm:ss";
				} else if (dispRange == "MIN") {
					tmFormat = "HH:mm";
					fixedSecFlg = true;
				} else if (dispRange == "HOUR"){
					tmFormat = "HH";
					fixedMinFlg = true;
					fixedSecFlg = true;
				} else {
					tmFormat = "";
				}
				//分の刻み幅
				var stepMin = 1;
				if (editor.interval == "_1MIN") {
					stepMin = 1;
				} else if (editor.interval == "_5MIN") {
					stepMin = 5;
				} else if (editor.interval == "_10MIN") {
					stepMin = 10;
				} else if (editor.interval == "_15MIN") {
					stepMin = 15;
				} else if (editor.interval == "_30MIN") {
					stepMin = 30;
				}

				if (type == "DATETIME") {
					var $from = $("<span/>").addClass("data-range-from").appendTo(filterInputField);
					var $inputFrom = $("<input/>").attr("type", "text").addClass("datetimepicker-form-size-02 inpbr").appendTo($from);

					var $to = $("<span/>").addClass("data-range-to display-none").text(scriptContext.locale.fromTo).appendTo(filterInputField);
					var $inputTo = $("<input/>").attr("type", "text").addClass("datetimepicker-form-size-02 inpbr").appendTo($to);

					$inputFrom.applyDatetimepicker({
						timeFormat: tmFormat == "" ? "HH:mm:ss" : tmFormat,
						stepMinute: stepMin,
						fixedHour: fixedHourFlg ? "00" : "",
						fixedMin: fixedMinFlg ? "00" : "",
						fixedSec: fixedSecFlg ? "00" : "",
						fixedMSec: "000"
					});

					$inputTo.applyDatetimepicker({
						timeFormat: tmFormat == "" ? "HH:mm:ss" : tmFormat,
						stepMinute: stepMin,
						fixedHour: fixedHourFlg ? "23" : "",
						fixedMin: fixedMinFlg ? "59" : "",
						fixedSec: fixedSecFlg ? "59" : "",
						fixedMSec: "999"
					});
				} else {
					var $from = $("<span/>").addClass("data-range-from").appendTo(filterInputField);
					var $inputFrom = $("<input/>").attr("type", "text").addClass("inpbr").appendTo($from);

					var $to = $("<span/>").addClass("data-range-to display-none").text(scriptContext.locale.fromTo).appendTo(filterInputField);
					var $inputTo = $("<input/>").attr("type", "text").addClass("inpbr").appendTo($to);

					$inputFrom.applyTimepicker({
						timeFormat: tmFormat == "" ? "HH" : tmFormat,
						stepMinute: stepMin,
						fixedHour: fixedHourFlg ? "00" : "",
						fixedMin: fixedMinFlg ? "00" : "",
						fixedSec: fixedSecFlg ? "00" : "",
						fixedMSec: "000"
					});

					$inputTo.applyTimepicker({
						timeFormat: tmFormat == "" ? "HH" : tmFormat,
						stepMinute: stepMin,
						fixedHour: fixedHourFlg ? "23" : "",
						fixedMin: fixedMinFlg ? "59" : "",
						fixedSec: fixedSecFlg ? "59" : "",
						fixedMSec: "999"
					});
				}
			}

			/**
			 * 真偽値の入力フィールドを作成します。
			 */
			function createBooleanFiled(filterInputField, editor, propertyName) {
				var $from = $("<span/>").addClass("data-range-from").appendTo(filterInputField);

				var $ul = $("<ul />").addClass("list-radio-01").appendTo($from);
				var $li1 = $("<li />").appendTo($ul);
				var $label1 = $("<label />").appendTo($li1);
				var $radioTrue = $("<input />").attr({type:"radio", name:propertyName, value:"true", displayName:getMultilingualString(editor.trueLabel, editor.localizedTrueLabelList)}).appendTo($label1);
				$label1.append(getMultilingualString(editor.trueLabel, editor.localizedTrueLabelList));

				var $li2 = $("<li />").appendTo($ul);
				var $label2 = $("<label />").appendTo($li2);
				var $radioFalse = $("<input />").attr({type:"radio", name:propertyName, value:"false", displayName:getMultilingualString(editor.falseLabel, editor.localizedFalseLabelList)}).appendTo($label2);
				$label2.append(getMultilingualString(editor.falseLabel, editor.localizedFalseLabelList));
			}

			/**
			 * 選択フィールド作成
			 */
			function createSelectField(filterInputField, editor, propertyName, condition) {

				var selectType;
				if (condition != "IN") {
					selectType = "radio";
				} else {
					selectType = "checkbox"
				}

				var $from = $("<span/>").addClass("data-range-from").appendTo(filterInputField);
				$from.attr("selectType", selectType);
				var $ul = $("<ul />").addClass("list-radio-01").appendTo($from);
				if (typeof editor.values !== "undefined" && editor.values != null && editor.values.length != 0) {
					for (var i = 0; i < editor.values.length; i++) {
						var ev = editor.values[i];	//EditorValue
						var $li = $("<li />").addClass("blc-range-form").appendTo($ul);
						var $label = $("<label />").appendTo($li);
						$("<input />").attr({type:selectType, name:propertyName, value:ev.value, displayName:getMultilingualString(ev.label, ev.localizedDisplayLabelList)}).appendTo($label);
						$label.append(getMultilingualString(ev.label, ev.localizedDisplayLabelList));
					}
				}
			}


			function dispLikeOption(type) {
				if (!type) return false;
				else if (type == "BOOLEAN") return false;
				else if (type == "DATE") return false;
				else if (type == "DATETIME") return false;
				else if (type == "TIME") return false;
				else if (type == "SELECT") return false;
				return true;
			}

			function dispInOption(type) {
				if (!type) return false;
				else if (type == "BOOLEAN") return false;
				else if (type == "DATE") return false;
				else if (type == "DATETIME") return false;
				else if (type == "TIME") return false;
				return true;
			}

			function dispCompareOption(type) {
				if (!type) return false;
				else if (type == "BOOLEAN") return false;
				else if (type == "SELECT") return false;
				return true;
			}

		}

	};


	$.calendar = new Calendar();

})(jQuery);
