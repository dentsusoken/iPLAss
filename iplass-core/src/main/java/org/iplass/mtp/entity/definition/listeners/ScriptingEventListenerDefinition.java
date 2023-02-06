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

package org.iplass.mtp.entity.definition.listeners;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.definition.EventListenerDefinition;


/**
 * <p>
 * GroovyScriptによるEventListenerの定義。
 * </p>
 *
 * <p>
 * Groovyでは２つの記述方法がある。
 * </p>
 * <ul>
 * <li>Javaと同様に、EntityEventListenerをimplementsしたクラスを記述。※クラス名は一意である必要がある。</li>
 * <li>Script形式で記述。</li>
 * </ul>
 * <p>
 * Script形式での記述の場合、entityの変数名でEntityのインスタンスが、
 * eventの変数名でEventTypeがあらかじめバインドされている。
 * </p>
 * <p>
 * バインドされている変数
 * <ul>
 * <li>entity : 対象のEntity</li>
 * <li>event : {@link EventType}</li>
 * <li>context : {@link org.iplass.mtp.entity.EntityEventContext}のインスタンス</li>
 * <li>user : 実行するユーザーの情報</li>
 * <li>date : 現在日時のjava.util.Dateのインスタンス</li>
 * </ul>
 * </p>
 * <h5>Script形式での記述例：</h5>
 * <code><pre>
 * if (event == EventType.BEFORE_INSERT) {
 *     entity.status = "new";
 *     entity.amount = 500L;
 *     entity.orderDate = date;
 * } else {
 *     entity.status = "";
 * }
 * </pre></code>
 * 
 *
 *
 * @author K.Higuchi
 *
 */
public class ScriptingEventListenerDefinition extends EventListenerDefinition {
	private static final long serialVersionUID = 5712119979506470199L;

	private String script;
	private List<EventType> listenEvent;

	public List<EventType> getListenEvent() {
		return listenEvent;
	}

	public void setListenEvent(List<EventType> listenEvent) {
		this.listenEvent = listenEvent;
	}

	public void addListenEvent(EventType eventType) {
		if (listenEvent == null) {
			listenEvent = new ArrayList<EventType>();
		}
		listenEvent.add(eventType);
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

//	@Override
//	public ScriptingEventListenerDefinition copy() {
//		ScriptingEventListenerDefinition copy = new ScriptingEventListenerDefinition();
//		copy.script = script;
//		if (listenEvent != null) {
//			copy.listenEvent = new ArrayList<EventType>();
//			copy.listenEvent.addAll(listenEvent);
//		}
//		return copy;
//	}

}
