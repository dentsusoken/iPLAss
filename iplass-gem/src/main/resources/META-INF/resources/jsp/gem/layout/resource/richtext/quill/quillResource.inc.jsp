<%--
 Copyright (C) 2024 DENTSU SOKEN INC. All Rights Reserved.
 
 Unless you have purchased a commercial license,
 the following license terms apply:
 
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as
 published by the Free Software Foundation, either version 3 of the
 License, or (at your option) any later version.
 
 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.
 
 You should have received a copy of the GNU Affero General Public License
 along with this program. If not, see <https://www.gnu.org/licenses/>.
 --%>

<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" %>
<script type="text/javascript" src="${staticContentPath}/webjars/quill/2.0.2/dist/quill.js?cv=${apiVersion}"></script>
<link rel="stylesheet" href="${staticContentPath}/webjars/quill/2.0.2/dist/quill.snow.css?cv=${apiVersion}"/>

<script>
//元に戻す、やりなおすアイコンの登録
const icons = Quill.import("ui/icons");
icons["undo"] = `<svg viewbox="0 0 18 18">
  <polygon class="ql-fill ql-stroke" points="6 10 4 12 2 10 6 10"></polygon>
  <path class="ql-stroke" d="M8.09,13.91A4.6,4.6,0,0,0,9,14,5,5,0,1,0,4,9"></path>
  </svg>`;
icons["redo"] = `<svg viewbox="0 0 18 18">
  <polygon class="ql-fill ql-stroke" points="12 10 14 12 16 10 12 10"></polygon>
  <path class="ql-stroke" d="M9.91,13.91A4.6,4.6,0,0,1,9,14a5,5,0,1,1,5-5"></path>
  </svg>`;

// フォントの登録
//const Font = Quill.import("formats/font");
const FontAttributor = Quill.import('attributors/style/font');
const fontFamilies = ["Sans Serif", "yu-gothic", "yu-mincho", "meiryo", 'system-ui', 'sans-serif', 'arial', 'serif', 'monospace', 'Courier', 'cursive', 'fantasy'];
FontAttributor.whitelist = fontFamilies;
Quill.register(FontAttributor, true);

// スタイルのインライン出力
const AlignStyle = Quill.import('attributors/style/align');
const BackgroundStyle = Quill.import('attributors/style/background');
const DirectionStyle = Quill.import('attributors/style/direction');
const ColorStyle = Quill.import('attributors/style/color');
const SizeStyle = Quill.import('attributors/style/size');
const FontStyle = Quill.import('attributors/style/font');
Quill.register(AlignStyle, true);
Quill.register(BackgroundStyle, true);
Quill.register(DirectionStyle, true);
Quill.register(ColorStyle, true);
Quill.register(SizeStyle, true);
Quill.register(FontStyle, true);

//デフォルトオプションの設定
if (!scriptContext.gem.richtext) {
	scriptContext.gem.richtext = {};
	scriptContext.gem.richtext.quill = {};
} else {
	if (!scriptContext.gem.richtext.quill) {
		scriptContext.gem.richtext.quill = {};
	}
}
//デフォルトのツールバーオプション
scriptContext.gem.richtext.quill.toolbarOptions = {
	container: [
		[{ "font": fontFamilies }],
		[{ "header": [1, 2, 3, 4, 5, 6, false] }],
		["bold", "italic", "underline", "strike"],
		[{ "script": "sub"}, { "script": "super" }, "formula"],
		[{ "color": [] }, { "background": [] }],
		[{ "list": "ordered"}, { "list": "bullet" }, { "list": "check" }],
		["blockquote", "code-block"],
		[{ "indent": "-1"}, { "indent": "+1" }],
		[{ "align": [] }, { "direction": "rtl" }],
		["link"],
		["undo", "redo"],
		["clean"],
	],
	handlers: {
		undo: function(value) {
			this.quill.history.undo();
		},
		redo: function(value) {
			this.quill.history.redo();
		}
	},
}
</script>

<style>
ql-font-yu-gothic {
  font-family: '游ゴシック Medium', 'Yu Gothic Medium', '游ゴシック体', YuGothic, sans-serif;
}
.ql-snow .ql-picker.ql-font [data-value='yu-gothic'].ql-picker-item::before,
.ql-snow .ql-picker.ql-font [data-value='yu-gothic'].ql-picker-label::before {
  content: '游ゴシック';
  font-family: '游ゴシック Medium', 'Yu Gothic Medium', '游ゴシック体', YuGothic, sans-serif;
}

.ql-font-yu-mincho {
  font-family: 'Yu Mincho', YuMincho, HG明朝B, 'MS Mincho', serif;
}
.ql-snow .ql-picker.ql-font [data-value='yu-mincho'].ql-picker-item::before,
.ql-snow .ql-picker.ql-font [data-value='yu-mincho'].ql-picker-label::before {
  content: '游明朝';
  font-family: 'Yu Mincho', YuMincho, HG明朝B, 'MS Mincho', serif;
}

.ql-font-meiryo {
  font-family: 'Meiryo';
}
.ql-snow .ql-picker.ql-font [data-value='meiryo'].ql-picker-item::before,
.ql-snow .ql-picker.ql-font [data-value='meiryo'].ql-picker-label::before {
  content: 'メイリオ';
  font-family: 'Meiryo';
}

.ql-font-system-ui {
  font-family: 'system-ui';
}
.ql-snow .ql-picker.ql-font [data-value='system-ui'].ql-picker-item::before,
.ql-snow .ql-picker.ql-font [data-value='system-ui'].ql-picker-label::before {
  content: 'system-ui';
  font-family: 'system-ui';
}

.ql-font-sans-serif {
  font-family: 'system-ui';
}
.ql-snow .ql-picker.ql-font [data-value='sans-serif'].ql-picker-item::before,
.ql-snow .ql-picker.ql-font [data-value='sans-serif'].ql-picker-label::before {
  content: 'sans-serif';
  font-family: 'sans-serif';
}

.ql-font-arial {
  font-family: 'arial';
}
.ql-snow .ql-picker.ql-font [data-value='arial'].ql-picker-item::before,
.ql-snow .ql-picker.ql-font [data-value='arial'].ql-picker-label::before {
  content: 'arial';
  font-family: 'arial';
}

.ql-font-serif {
  font-family: 'serif';
}
.ql-snow .ql-picker.ql-font [data-value='serif'].ql-picker-item::before,
.ql-snow .ql-picker.ql-font [data-value='serif'].ql-picker-label::before {
  content: 'serif';
  font-family: 'serif';
}

.ql-font-monospace {
  font-family: 'monospace';
}
.ql-snow .ql-picker.ql-font [data-value='monospace'].ql-picker-item::before,
.ql-snow .ql-picker.ql-font [data-value='monospace'].ql-picker-label::before {
  content: 'monospace';
  font-family: 'monospace';
}

.ql-font-Courier {
  font-family: 'Courier';
}
.ql-snow .ql-picker.ql-font [data-value='Courier'].ql-picker-item::before,
.ql-snow .ql-picker.ql-font [data-value='Courier'].ql-picker-label::before {
  content: 'Courier';
  font-family: 'Courier';
}

.ql-font-cursive {
  font-family: 'cursive';
}
.ql-snow .ql-picker.ql-font [data-value='cursive'].ql-picker-item::before,
.ql-snow .ql-picker.ql-font [data-value='cursive'].ql-picker-label::before {
  content: 'cursive';
  font-family: 'cursive';
}

.ql-font-fantasy {
  font-family: 'fantasy';
}
.ql-snow .ql-picker.ql-font [data-value='fantasy'].ql-picker-item::before,
.ql-snow .ql-picker.ql-font [data-value='fantasy'].ql-picker-label::before {
  content: 'fantasy';
  font-family: 'fantasy';
}

</style>