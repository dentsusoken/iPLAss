/*
 * Copyright (C) 2016 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
	$.calendar.setLangOption({monthNames: ["มกราคม","กุมภาพันธ์","มีนาคม","เมษายน","พฤษภาคม","มิถุนายน","กรกฎาคม","สิงหาคม","กันยายน","ตุลาคม","พฤศจิกายน","ธันวาคม"],
		monthNamesShort: ["ม.ค.","ก.พ.","มี.ค.","เม.ย.","พ.ค.","มิ.ย.","ก.ค.","ส.ค.","ก.ย.","ต.ค.","พ.ย.","ธ.ค."],
		dayNames: ["อาทิตย์","จันทร์","อังคาร","พุธ","พฤหัสบดี","ศุกร์","เสาร์"],
		dayNamesShort: ["อา.","จ.","อ.","พ.","พฤ.","ศ.","ส."],
		dayNamesWidget:["อา.","จ.","อ.","พ.","พฤ.","ศ.","ส."],
		titleFormatMonth:"MMMM YYYY",
		titleFormatWeek:"D MMM YYYY",
		titleFormatDay:"ddd, D MMM YYYY",
		titleFormatWiget:"MM YYYY",
		columnFormatMonth:"ddd",
		columnFormatWeek:"ddd M/D",
		columnFormatDay:"dddd M/D",
		thisMonth:"เดือนนี้",
		thisWeek:"ในสัปดาห์นี้",
		today:"ในวันนี้",
		buttonTextMonth:"เดือน",
		buttonTextWeek:"สัปดาห์",
		buttonTextDay:"วัน"
	});
});

