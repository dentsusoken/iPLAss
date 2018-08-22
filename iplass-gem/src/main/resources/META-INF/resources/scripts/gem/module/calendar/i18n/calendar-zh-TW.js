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

$(function(){
	$.calendar.setLangOption({monthNames: ["01月","02月","03月","04月","05月","06月","07月","08月","09月","10月","11月","12月"],
		monthNamesShort: ["01","02","03","04","05","06","07","08","09","10","11","12"],
		dayNames: ["星期日","星期一","星期二","星期三","星期四","星期五","星期六"],
		dayNamesShort: ["週日","週一","週二","週三","週四","週五","週六"],
		dayNamesWidget:["日","一","二","三","四","五","六"],
		titleFormatMonth: "YYYY年M月",
		titleFormatWeek: "YYYY年M月D日",
		titleFormatDay: "YYYY年M月D日 ddd",
		titleFormatWiget:"YYYY年MM",
		columnFormatMonth: "ddd",
		columnFormatWeek: "M/D ddd",
		columnFormatDay: "M/D dddd",
		thisMonth:"本月",
		thisWeek:"本週",
		today:"今天",
		buttonTextMonth:"月",
		buttonTextWeek:"週",
		buttonTextDay:"天"
	});
});
