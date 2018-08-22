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
		dayNames: ["日","月","火","水","木","金","土"],
		dayNamesShort: ["(日)","(月)","(火)","(水)","(木)","(金)","(土)"],
		dayNamesWidget:["日","月","火","水","木","金","土"],
		titleFormatMonth: "YYYY年MMMM",
		titleFormatWeek: "YYYY年MMMMD日 ddd",
		titleFormatDay: "YYYY年MMMMD日",
		titleFormatWiget:"YYYY年MM",
		columnFormatMonth: "dddd",
		columnFormatWeek: "M/D ddd",
		columnFormatDay: "M/D ddd",
		thisMonth:"今月",
		thisWeek:"今週",
		today:"今日",
		buttonTextMonth:"月",
		buttonTextWeek:"週",
		buttonTextDay:"日"
	});
});

