/*
Copyright (c) 2003-2011, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

CKEDITOR.editorConfig = function( config )
{
	config.language = scriptContext.locale.defaultLocale;
	//config.skin = 'office2003';
	config.uiColor = '';
	CKEDITOR.config.toolbar =
		[
//			['Source','-','Save','NewPage','Preview','-','Templates'],
			['Source','-','Preview','-','ShowBlocks','-','Templates'],
//			['Cut','Copy','Paste','PasteText','PasteFromWord','-','Print','SpellChecker','Scayt'],
			['Cut','Copy','Paste','PasteText','PasteFromWord'],
			['Undo','Redo','-','Find','Replace','-','SelectAll','RemoveFormat'],
//			['Form','Checkbox','Radio','TextField','Textarea','Select','Button','ImageButton','HiddenField'],
//			'/',
			['Table','HorizontalRule'],
			['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
//			['NumberedList','BulletedList','-','Outdent','Indent','Blockquote'],
			['NumberedList','BulletedList'],
//			['Image','Flash','Table','HorizontalRule','Smiley','SpecialChar','PageBreak'],
			'/',
//			['Styles','Format','Font','FontSize'],
			['Bold','Italic','Underline','Strike','-','Subscript','Superscript'],
			['Font','FontSize'],
			['TextColor','BGColor'],
//			['Link','Unlink','Anchor'],
			['Link','Unlink','Anchor']
//			['Smiley','SpecialChar'],
//			['Maximize','ShowBlocks','-','About']
		];
	CKEDITOR.config.font_names='ＭＳ Ｐゴシック;ＭＳ Ｐ明朝;ＭＳ ゴシック;ＭＳ 明朝;Arial/Arial, Helvetica, sans-serif;Comic Sans MS/Comic Sans MS, cursive;Courier New/Courier New, Courier, monospace;Georgia/Georgia, serif;Lucida Sans Unicode/Lucida Sans Unicode, Lucida Grande, sans-serif;Tahoma/Tahoma, Geneva, sans-serif;Times New Roman/Times New Roman, Times, serif;Trebuchet MS/Trebuchet MS, Helvetica, sans-serif;Verdana/Verdana, Geneva, sans-serif';
	CKEDITOR.config.enterMode = CKEDITOR.ENTER_BR;
	CKEDITOR.config.toolbarCanCollapse = false;
	CKEDITOR.config.removePlugins = 'maximize,resize,exportpdf';

	CKEDITOR.dtd['a']['article'] = 1;
	CKEDITOR.dtd['a']['aside'] = 1;
	CKEDITOR.dtd['a']['div'] = 1;
	CKEDITOR.dtd['a']['footer'] = 1;
	CKEDITOR.dtd['a']['h1'] = 1;
	CKEDITOR.dtd['a']['h2'] = 1;
	CKEDITOR.dtd['a']['h3'] = 1;
	CKEDITOR.dtd['a']['h4'] = 1;
	CKEDITOR.dtd['a']['h5'] = 1;
	CKEDITOR.dtd['a']['h6'] = 1;
	CKEDITOR.dtd['a']['header'] = 1;
	CKEDITOR.dtd['a']['hgroup'] = 1;
	CKEDITOR.dtd['a']['nav'] = 1;
	CKEDITOR.dtd['a']['p'] = 1;
	CKEDITOR.dtd['a']['section'] = 1;

};
