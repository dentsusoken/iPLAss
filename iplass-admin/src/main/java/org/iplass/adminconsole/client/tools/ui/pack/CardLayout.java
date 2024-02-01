/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.tools.ui.pack;

import java.util.LinkedHashMap;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;

public class CardLayout extends Canvas {

	private LinkedHashMap<Object, Canvas> cards = null;
	private Object currentCardKey = null;

	public CardLayout() {
		cards = new LinkedHashMap<Object, Canvas>();

		setWidth100();
		setHeight100();
		setMargin(0);
		setPadding(0);

		setOverflow(Overflow.AUTO);	//これを指定しないと余計な余白が表示される

		//TODO 最大化して戻った場合にスクロールが表示されてしまうが、再描画しても消えない
//		addResizedHandler(new ResizedHandler() {
//
//			@Override
//			public void onResized(ResizedEvent event) {
//				if (getCurrentCard() != null) {
//					getCurrentCard().redraw();
//				}
//			}
//		});

	}

	public void addCard(Object key, Canvas card) {

		if(key == null) {
			throw new IllegalArgumentException("key must not be NULL");
		}

		if(card == null) {
			throw new IllegalArgumentException("card must not be NULL");
		}

		card.setWidth100();
		card.setHeight100();
		card.setPageLeft(0);
		card.setPageTop(0);

//java.lang.IllegalStateException: Cannot change configuration property 'overflow' to visible after the component has been created.
//		card.setOverflow(Overflow.VISIBLE);

		this.addChild(card);
		cards.put(key, card);

		if(currentCardKey == null) {
			currentCardKey = key;
		} else {
			card.hide();
		}
	}

	public Canvas getCard(Object key) {
		return cards.get(key);
	}

	public void showFirst() {
		if (cards.size() > 0) {
			showCard(cards.keySet().iterator().next());
		} else {
			throw new IllegalStateException("card not exist");
		}
	}

	public Canvas getFirstCardCanvas() {
		if (cards.size() > 0) {
			return getCard(cards.keySet().iterator().next());
		} else {
			throw new IllegalStateException("card not exist");
		}
	}

	public Object getFirstCardKey() {
		if (cards.size() > 0) {
			return cards.keySet().iterator().next();
		} else {
			throw new IllegalStateException("card not exist");
		}
	}

	public void showCard(Object key) {

		if(key == null) {
			throw new IllegalArgumentException("key must not be NULL");
		}

		for (Object _key : cards.keySet()) {
			Canvas c = cards.get(_key);
			if (key.equals(_key)) {
				c.show();
				currentCardKey = key;
			} else {
				c.hide();
			}
		}
	}

	/**
	 * @return the cards
	 */
	public LinkedHashMap<Object, Canvas> getCards() {
		return cards;
	}

	/**
	 * @return the currentCard
	 */
	public Canvas getCurrentCard() {
		return  (currentCardKey==null) ? null : cards.get(currentCardKey);
	}

	/**
	 * @return the currentCard key
	 */
	public Object getCurrentCardKey() {
		return currentCardKey;
	}
}
