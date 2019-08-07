/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.gem.command;

public class Constants {

	public static final String ID = "id";

	public static final String MODAL_TARGET = "modalTarget";

	public static final String MENU_TREE = "menuTree";
	public static final String ROLE = "role";
	public static final String ROLE_NAME = "roleName";
	public static final String DEF_NAME = "defName";
	public static final String ENTITY_DEF_NAME = "entityDefName";
	public static final String ROOT_DEF_NAME = "rootDefName";
	public static final String NAME = "name";
	public static final String PROP_NAME = "propName";
	public static final String VIEW_NAME = "viewName";
	public static final String FILTER_NAME = "filterName";
	public static final String OID = "oid";
	public static final String RBID = "rbid";
	public static final String VERSION = "version";
	public static final String TIMESTAMP = "timestamp";
	public static final String NEWVERSION = "newversion";
	public static final String MESSAGE = "message";
	public static final String ERROR_PROP = "errorProperty";
	public static final String DATA = "data";
	public static final String REF_EDIT = "refEdit";
	public static final String COPY = "copy";
	public static final String DIALOG_MODE = "isDialog";

	public static final String SEARCH_TYPE = "searchType";
	public static final String SELECT_TYPE = "selectType";
	public static final String SEARCH_LIMIT = "limit";
	public static final String SEARCH_OFFSET = "offset";
	public static final String SEARCH_SORTKEY = "sortKey";
	public static final String SEARCH_SORTTYPE = "sortType";
	public static final String SEARCH_COND = "searchCond";
	public static final String SEARCH_COND_PREFIX = "sc_";
	public static final String DEFAULT_SEARCH_COND = "defaultSearchCond";
	public static final String SEARCH_SPEC_VERSION = "specVersion";
	public static final String TOPVIEW_LIST_OFFSET = "topViewListOffset";

	public static final String EXEC_TYPE = "execType";
	public static final String EXEC_TYPE_INSERT = "insert";
	public static final String EXEC_TYPE_UPDATE = "update";

	public static final String OUTPUT_TYPE = "outputType";
	public static final String VIEW_TYPE = "viewType";
	public static final String BACK_PATH = "backPath";
	public static final String FROM_VIEW = "fromView";

	public static final String VIEW_TYPE_DETAIL = "detail";
	public static final String VIEW_TYPE_SEARCH = "search";
	public static final String VIEW_TYPE_SEARCH_RESULT = "searchResult";
	public static final String VIEW_TYPE_BULK = "bulk";
	public static final String VIEW_TYPE_MULTI_BULK = "multiBulk";

	public static final String FORM_SCRIPT_KEY = "formScriptKey";
	public static final String SECTION_SCRIPT_KEY = "sectionScriptKey";

	public static final String REF_LINK_VALUE = "linkValue";
	public static final String REF_UNIQUE_VALUE = "uniqueValue";

	public static final String ENTITY_DATA = "value";
	public static final String ENTITY_DEFINITION = "entityDefinition";
	public static final String NAV_SECTION = "navSection";
	public static final String ELEMENT = "element";
	public static final String COL_NUM = "colNum";
	public static final String IS_VIRTUAL = "isVirtual";
	public static final String OUTPUT_HIDDEN = "outputHidden";

	// 検索条件のパラメータ
	public static final String SEARCH_TYPE_NORMAL = "normal";
	public static final String SEARCH_TYPE_DETAIL = "detail";
	public static final String SEARCH_TYPE_FIXED = "fixed";
	public static final String SEARCH_TYPE_FILTER = "filter";

	public static final String DETAIL_COND_EXPR = "dtlCndExpr";
	public static final String DETAIL_COND_PROP_NM = "dtlCndPropNm_";
	public static final String DETAIL_COND_PREDICATE = "dtlCndPrdct_";
	public static final String DETAIL_COND_VALUE = "dtlCndVl_";
	public static final String DETAIL_COND_COUNT = "dtlCndCount";
	public static final String DETAIL_COND_FILTER_EXPRESSION = "dtlCndFilterExpression";

	public static final String BULK_UPDATE_SELECT_TYPE = "selectAllType";
	public static final String BULK_UPDATE_SELECT_ALL_PAGE = "selectAllPage";
	public static final String BULK_UPDATE_USE_BULK_VIEW = "useBulkView";
	//一括更新のプロパティ名
	public static final String BULK_UPDATE_PROP_NM = "bulkUpdatePropNm";
	//一括更新されたプロパティ名
	public static final String BULK_UPDATED_PROP_NM = "bulkUpdatedPropNm";
	public static final String BULK_UPDATED_PROP_VALUE = "bulkUpdatedPropVl";

	/** CSVダウンロード時の文字コード */
	public static final String CSV_CHARACTER_CODE = "characterCode";
	public static final String CSV_FILE_DOWNLOAD_TOKEN = "fileDownloadToken";
	public static final String CSV_IS_OUTPUT_CODE_VALUE = "isOutputCodeValue";

	public static final String AND = "And";
	public static final String OR = "Or";
	public static final String NOT = "Not";
	public static final String EXPRESSION = "Expression";

	public static final String EQUALS = "Equals";
	public static final String NOTEQUALS = "Not" + EQUALS;
	public static final String GREATER = "Greater";
	public static final String GREATEREQUALS = GREATER + EQUALS;
	public static final String LESSER = "Lesser";
	public static final String LESSEREQUALS = LESSER + EQUALS;
	public static final String FRONTMATCH = "FrontMatch";
	public static final String BACKWARDMATCH = "BackwardMatch";
	public static final String INCLUDE = "Include";
	public static final String NOTINCLUDE = "NotInclude";
	public static final String NOTNULL = "NotNull";
	public static final String NULL = "Null";
	public static final String IN = "In";
	public static final String RANGE = "Range";

	//Editor用パラメータ
	public static final String EDITOR_STYLE = "style";
	public static final String EDITOR_DISPLAY_LABEL = "displayLabel";
	public static final String EDITOR_REQUIRED = "required";
	public static final String EDITOR_EDITOR = "editor";
	public static final String EDITOR_PROPERTY_DEFINITION = "propertyDefinition";
	public static final String EDITOR_PROP_VALUE = "propValue";
	public static final String EDITOR_DEFAULT_VALUE = "propDefaultValue";
	public static final String EDITOR_PARENT_ENTITY = "parentEntity";

	public static final String EDITOR_SELECT_VALUE_LIST = "selectValueList";
	public static final String EDITOR_LOCAL_VALUE_LIST = "localValueList";

	public static final String EDITOR_PICKER_PROP_NAME = "_propName";
	public static final String EDITOR_PICKER_PROP_VALUE = "_propValue";
	public static final String EDITOR_PICKER_DEFAULT_VALUE = "_defaultValue";
	public static final String EDITOR_PICKER_DEFAULT_HOUR = "defaultHour";
	public static final String EDITOR_PICKER_DEFAULT_MIN = "defaultMin";
	public static final String EDITOR_PICKER_DEFAULT_SEC = "defaultSec";
	public static final String EDITOR_PICKER_DEFAULT_MSEC = "defaultMsec";

	public static final String EDITOR_REF_RELOAD_URL = "reloadUrl";
	public static final String EDITOR_REF_MAPPEDBY = "mappedBy";
	public static final String EDITOR_REF_ENTITY_VALUE_MAP = "entityValueMap";
	public static final String EDITOR_REF_NEST = "nest";
	public static final String EDITOR_REF_NEST_DUMMY_ROW = "nestDummyRow";
	public static final String EDITOR_REF_NEST_PROPERTY = "nestProperty";
	public static final String EDITOR_REF_NEST_PROPERTY_ITEM = "nestPropertyItem";
	public static final String EDITOR_REF_NEST_PROPERTY_PREFIX = "nest_";
	public static final String EDITOR_REF_NEST_PROP_NAME = "nestPropName";
	public static final String EDITOR_REF_NEST_EDITOR = "nestEditor";
	public static final String EDITOR_REF_NEST_STYLE = "nestStyle";
	public static final String EDITOR_REF_NEST_VALUE = "nestValue";
	public static final String EDITOR_REF_NEST_REQUIRED = "nestRequired";
	public static final String EDITOR_REF_NEST_DISPLAY_LABEL = "nestDisplayLabel";
	public static final String EDITOR_REF_SHOW_PROPERTY = "showProperty";

	public static final String EDITOR_JOIN_NEST_PREFIX = "nestPrefix";

	/** デフォルトのView検索画面を表示する際に、同時に条件なし検索を実行する場合にParameterに設定するValue値 */
	public static final String EXECUTE_SEARCH_VALUE = "t";

	/** デフォルトのView検索画面を表示する際に、同時に条件なし検索を実行する場合のParameterKey値 */
	public static final String EXECUTE_SEARCH = "es";

	/** UserPropertyEditorでoidからUserEntityを取得するマップをリクエストから取得するためのkey */
	public static final String USER_INFO_MAP = "userInfoMap";

	// ライブラリ多重読み込み防止のキー
	public static final String UPLOAD_LIB_LOADED = "upload_lib_loaded";
	public static final String RICHTEXT_LIB_LOADED = "richtext_lib_loaded";

	// 検索画面用
	public static final String PROPERTY_ITEM = "propertyItem";
	public static final String COND_PROP_NAME = "condPropName";

	// 選択画面用
	public static final String ROOT_NAME = "rootName";
	public static final String SELECT_MULTI = "multiplicity";
	public static final String SHOW_DETERMINE_BUTTON = "showDetermineButton";
	public static final String PERMIT_CONDITION_SELECT_ALL = "permitConditionSelectAll";

	//参照先エンティティ登録用
	public static final String PARENT_DEFNAME = "parentDefName";
	public static final String PARENT_VIEWNAME = "parentViewName";
	public static final String PARENT_PROPNAME = "parentPropName";

	//自動補完
	public static final String AUTOCOMPLETION_SETTING = "autocompletionSetting";
	public static final String AUTOCOMPLETION_DEF_NAME = "autocompletionDefName";
	public static final String AUTOCOMPLETION_VIEW_NAME = "autocompletionViewName";
	public static final String AUTOCOMPLETION_VIEW_TYPE = "autocompletionViewType";
	public static final String AUTOCOMPLETION_PROP_NAME = "autocompletionPropName";
	public static final String AUTOCOMPLETION_SCRIPT_PATH = "autocompletionScriptPath";
	public static final String AUTOCOMPLETION_EDITOR = "autocompletionEditor";
	public static final String AUTOCOMPLETION_MULTIPLICTTY = "autocompletionMultiplicity";
	public static final String AUTOCOMPLETION_REF_NEST_PROP_NAME = "autocompletionNestPropName";
	public static final String AUTOCOMPLETION_REF_SECTION_INDEX = "autocompletionReferenceSectionIndex";

	// お知らせ用
	public static final String DATA_ENTITY = "entity";
	public static final String INFO_SETTING = org.iplass.mtp.impl.view.top.parts.Constants.INFO_SETTING;
	public static final String INFO_TITLE = "infoTitle";
	public static final String INFO_TIMERANGE = "infoTimeRange";
	public static final String INFO_PASSWORD_WARNING = "infoPasswordWarning";
	public static final String INFO_PASSWORD_REMAINING_DAYS = "infoPasswordRemainingDays";

	// カレンダー用
	public static final String CALENDAR_NAME = org.iplass.mtp.impl.view.top.parts.Constants.CALENDAR_NAME;
	public static final String CALENDAR_TYPE = "calendarType";
	public static final String CALENDAR_DATE = "date";
	public static final String CALENDAR_FROM = "from";
	public static final String CALENDAR_TO = "to";
	public static final String CALENDAR_TARGET_DATE = "targetDate";
	public static final String CALENDAR_VALUE_LIST = "valueList";
	public static final String CALENDAR_WITHOUT_ENTITY = "withoutEntity";
	public static final String CALENDAR_FIX_FILTER = "fixFilter";
	public static final String CALENDAR_PROPERTY = "property";
	public static final String CALENDAR_CONDITION = "condition";
	public static final String CALENDAR_KEYWORD = "keyword";
	public static final String CALENDAR_VALUE = "value";
	public static final String CALENDAR_PROPERTY_TYPE = "type";
	public static final String CALENDAR_DATA_RANGE_FROM = "data-range-from";
	public static final String CALENDAR_DATA_RANGE_TO = "data-range-to";

	public static final String CALENDAR_LIB_LOADED = "calendar_lib_loaded";

	// ツリー用
	public static final String TREE_PATH = "path";

	// ロック処理
	public static final String LOCK_RESULT = "lockResult";

	//ユーザ情報変更
	public static final String UPDATE_USER_INFO = "updateUserInfo";

	// コマンド内でEntityに設定しているフラグ等
	public static final String REF_INDEX = "##refIndex";
	public static final String REF_RELOAD = "##refReload";
	public static final String DELETE_OID_FLAG = "##deleteOidFlag";
	public static final String REF_TABLE_ORDER_INDEX = "##refTableOrderIndex";

	//リクエストのattributeに設定している項目
	public static final String VALID_ERROR_LIST = "validErrorList";

	// コマンド実行結果
	public static final String CMD_EXEC_SUCCESS = "SUCCESS";
	public static final String CMD_EXEC_SUCCESS_DETAIL_VIEW = "SUCCESS_DETAIL_VIEW";
	public static final String CMD_EXEC_SUCCESS_BACK_PATH = "SUCCESS_BACK_PATH";
	public static final String CMD_EXEC_SUCCESS_ASYNC = "SUCCESS_ASYNC";
	public static final String CMD_EXEC_ERROR = "ERROR";
	public static final String CMD_EXEC_ERROR_VIEW = "ERROR_VIEW";
	public static final String CMD_EXEC_ERROR_NODATA = "ERROR_NODATA";
	public static final String CMD_EXEC_ERROR_LOCK = "ERROR_LOCK";
	public static final String CMD_EXEC_ERROR_TOKEN = "ERROR_TOKEN";
	public static final String CMD_EXEC_ERROR_PERMIT = "ERROR_PERMIT";
	public static final String CMD_EXEC_ERROR_PARAMETER = "ERROR_PARAMETER";
	public static final String CMD_EXEC_ERROR_VALIDATE = "ERROR_VALIDATE";
	public static final String CMD_EXEC_ERROR_SEARCH = "ERROR_SEARCH";
	public static final String CMD_EXEC_FAILURE = "FAILURE";

	// 処理結果
	public static final String CMD_RSLT_STREAM_FILENAME = "fileName";
	public static final String CMD_RSLT_STREAM_CONTENT_TYPE = "contentType";
	public static final String CMD_RSLT_STREAM = "streamData";

	public static final String CMD_RSLT_JSP_ERROR = "/jsp/gem/generic/error.jsp";
	public static final String CMD_RSLT_JSP_SYSTEM_ERROR = "/jsp/gem/error/Error.jsp";

	public static final String CMD_RSLT_JSP_CALENDAR = "/jsp/gem/calendar/calendarView.jsp";
	public static final String CMD_RSLT_JSP_CALENDAR_FILTER = "/jsp/gem/calendar/ref/calendarFilter.jsp";
	public static final String CMD_RSLT_JSP_EDIT = "/jsp/gem/generic/detail/edit.jsp";
	public static final String CMD_RSLT_JSP_VIEW = "/jsp/gem/generic/detail/view.jsp";
	public static final String CMD_RSLT_JSP_SEARCH = "/jsp/gem/generic/search/search.jsp";
	public static final String CMD_RSLT_JSP_PURGE = "/jsp/gem/generic/delete/purge.jsp";
	public static final String CMD_RSLT_JSP_REF_EDIT = "/jsp/gem/generic/detail/ref/edit.jsp";
	public static final String CMD_RSLT_JSP_REF_VIEW = "/jsp/gem/generic/detail/ref/view.jsp";
	public static final String CMD_RSLT_JSP_COMPLETED = "/jsp/gem/generic/detail/ref/completed.jsp";
	public static final String CMD_RSLT_JSP_REF_SEARCH = "/jsp/gem/generic/search/select.jsp";
	public static final String CMD_RSLT_JSP_INFO_LIST = "/jsp/gem/information/list.jsp";
	public static final String CMD_RSLT_JSP_INFO_VIEW = "/jsp/gem/information/view.jsp";
	public static final String CMD_RSLT_JSP_INDEX = "/jsp/gem/layout/index.jsp";
	public static final String CMD_RSLT_JSP_DEFAULT = "/jsp/gem/layout/layout.jsp";
	public static final String CMD_RSLT_JSP_DIALOG = "/jsp/gem/layout/dialog.jsp";
	public static final String CMD_RSLT_JSP_FULLTEXT_SEARCH = "/jsp/gem/fulltext/search.jsp";
	public static final String CMD_RSLT_JSP_BACK_PATH = "/jsp/gem/generic/backPath.jsp";
	public static final String CMD_RSLT_JSP_CSV_UPLOAD_RESULT = "/jsp/gem/generic/upload/csvUploadResult.jsp";
	public static final String CMD_RSLT_JSP_CSV_UPLOAD = "/jsp/gem/generic/upload/csvUpload.jsp";
	public static final String CMD_RSLT_JSP_LOGIN = "/jsp/gem/auth/Login.jsp";
	public static final String CMD_RSLT_JSP_REAUTH = "/jsp/gem/auth/ReAuth.jsp";
	public static final String CMD_RSLT_JSP_UPDATE_PASSWORD = "/jsp/gem/auth/Password.jsp";
	public static final String CMD_RSLT_JSP_RESET_SPECIFIC_PASSWORD = "/jsp/gem/auth/specificPassword.jsp";
	public static final String CMD_RSLT_JSP_PASSWORD_EXPIRE = "/jsp/gem/auth/Expire.jsp";
	public static final String CMD_RSLT_JSP_VERIFY2ND = "/jsp/gem/auth/Verify2nd.jsp";
	public static final String CMD_RSLT_JSP_BULK_EDIT="/jsp/gem/generic/bulk/bulkEdit.jsp";
	public static final String CMD_RSLT_JSP_BULK_MULTI_EDIT="/jsp/gem/generic/bulk/edit.jsp";
	public static final String CMD_RSLT_JSP_APP_MAINTENANCE="/jsp/gem/auth/application.jsp";
	public static final String CMD_RSLT_HTML_PDFVIEWER_PATH = "/jsp/gem/binary/pdfviewer.jsp";

	public static final String TEMPLATE_ERROR = "gem/generic/error";
	public static final String TEMPLATE_COMMON_ERROR = "gem/generic/common/error";
	public static final String TEMPLATE_SYSTEM_ERROR = "gem/error/system";
	public static final String TEMPLATE_PERMISSION_ERROR = "gem/auth/PermissionError";

	public static final String TEMPLATE_LOGIN = "gem/auth/Login";
	public static final String TEMPLATE_PASSWORD_EXPIRE = "gem/auth/Expire";
	public static final String TEMPLATE_VERIFY2ND = "gem/auth/Verify2nd";
	public static final String TEMPLATE_UPDATE_PASSWORD = "gem/auth/Password";
	public static final String TEMPLATE_REAUTH = "gem/auth/ReAuth";

	public static final String TEMPLATE_EDIT = "gem/generic/detail/edit";
	public static final String TEMPLATE_REF_EDIT = "gem/generic/detail/ref/edit";
	public static final String TEMPLATE_BULK_EDIT="gem/generic/bulk/bulkEdit";
	public static final String TEMPLATE_BULK_MULTI_EDIT="gem/generic/bulk/edit";

	// LayoutAction
	public static final String LAYOUT_NORMAL_ACTION = "gem/layout/defaultLayout";
	public static final String LAYOUT_POPOUT_ACTION = "gem/layout/popupLayout";

}
