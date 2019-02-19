/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

$(function(){

	//jquery-ui-timepicker-addonのカスタマイズ
	//Datepicker形式の場合に「今日」ボタンをクリックした時にInputに値を設定する
	//Datetimepicker形式の場合に「現時刻」ボタンをクリックした時にInputに値が設定されるので動作の整合性を合わせる

	//datepickerの_gotoTodayを取得
	$.datepicker._datepicker_base_gotoToday = $.datepicker._base_gotoToday;
	$.datepicker._gotoToday = function (id) {
		var inst = this._getInst($(id)[0]);

		//		this._base_gotoToday(id);
		this._datepicker_base_gotoToday(id);

		var tp_inst = this._get(inst, 'timepicker');
//		if (!tp_inst) {
//		  return;
//		}

		var now = new Date();
//		var tzoffset = $.timepicker.timezoneOffsetNumber(tp_inst.timezone);
//		now.setMinutes(now.getMinutes() + now.getTimezoneOffset() + parseInt(tzoffset, 10));
		//tp_instがある場合、timezone処理
		if (tp_inst) {
			var tzoffset = $.timepicker.timezoneOffsetNumber(tp_inst.timezone);
			now.setMinutes(now.getMinutes() + now.getTimezoneOffset() + parseInt(tzoffset, 10));
		}

		this._setTime(inst, now);
		this._setDate(inst, now);

		//tp_instがある場合、handler処理
//		tp_inst._onSelectHandler();
		if (tp_inst) {
			tp_inst._onSelectHandler();
		}
	};
});
