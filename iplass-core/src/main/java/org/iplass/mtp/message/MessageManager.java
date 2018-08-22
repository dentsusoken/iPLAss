/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.message;

import java.util.List;

import org.iplass.mtp.definition.DefinitionModifyResult;
import org.iplass.mtp.definition.TypedDefinitionManager;

/**
* メッセージマネージャ
*
* @author 藤田　義弘
*
*/
public interface MessageManager extends TypedDefinitionManager<MessageCategory> {

	/**
	 * メッセージカテゴリを新規に作成する
	 * @param messageCategory 作成するメッセージカテゴリ
	 * @deprecated {@link #create(MessageCategory)} を使用してください。
	 */
	@Deprecated
	public DefinitionModifyResult createMessage(MessageCategory messageCategory);

	/**
	 * メッセージカテゴリを更新する。
	 * @param messageCategory 更新するメッセージカテゴリ
	 * @deprecated {@link #update(MessageCategory)} を使用してください。
	 */
	@Deprecated
	public DefinitionModifyResult updateMessage(MessageCategory messageCategory);

	/**
	 * メッセージカテゴリを削除する。
	 * @param name 削除するメッセージカテゴリ
	 * @deprecated {@link #remove(String)} を使用してください。
	 */
	@Deprecated
	public DefinitionModifyResult deleteMessage(String name);

	// FIXME RootMetaDataではないが、Item用は使われてないので消しても平気？アプリ側では使ってないよね？
	/**
	 * 指定されたメッセージカテゴリにメッセージを作成する
	 * @param category 作成するメッセージを含むメッセージカテゴリ名
	 * @param messageItem 作成するメッセージオブジェクト
	 */
	public DefinitionModifyResult createMessageItem(String category, MessageItem messageItem);

	/**
	 * 指定されたメッセージカテゴリのメッセージを更新する。
	 * @param category 更新するメッセージを含むメッセージカテゴリ名
	 * @param messageItem 更新するメッセージオブジェクト
	 */
	public DefinitionModifyResult updateMessageItem(String category, MessageItem messageItem);

	/**
	 * 指定されたメッセージカテゴリのメッセージを削除する。
	 * @param category 削除するメッセージを含むメッセージカテゴリ名
	 * @param messageItem 削除するメッセージオブジェクト
	 */
	public DefinitionModifyResult deleteMessageItem(String category, String messageId);

	/**
	 * 指定のメッセージカテゴリを取得する
	 * @return MessageCategory メッセージカテゴリ
	 * @deprecated {@link #get(String)} を使用してください。
	 */
	@Deprecated
	public MessageCategory getMessageCategoryByName(String name);

	/**
	 * 指定されたカテゴリのメッセージID一覧を取得する
	 * @return List<String>
	 */
	public List<String> getMessageIdList(String category);

	/**
	 * メッセージカテゴリ内のメッセージを取得する
	 * @return List<String>
	 */
	public MessageItem getMessageItem(String category , String messageId);

}
