/*
 * Copyright (C) 2018 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.base.ui.widget;

import org.iplass.adminconsole.client.base.util.SmartGWTUtil;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;

/**
 * Windowのベースクラス。
 */
public abstract class AbstractWindow extends Window {

	/** destroy時にAnimationを実行するか */
	private boolean destroyWithAnimation = false;

	public AbstractWindow() {

		//Closeボタン押下時にdestroyを実行
		super.addCloseClickHandler(new CloseClickHandler() {
			@Override
			public void onCloseClick(CloseClickEvent event) {
				destroy();
			}
		});
	}

	/**
	 * このクラスにて、CloseClickHandlerを設定しているため、独自に設定しても有効にしません。
	 * destroy処理をカスタマイズしたい場合は、 {@link #onPreDestroy()} をオーバーライドしてください。
	 */
	@Override
	public final HandlerRegistration addCloseClickHandler(com.smartgwt.client.widgets.events.CloseClickHandler handler) {
		//CloseClickHandlerの追加を防ぐ
		GWT.log("unsupported operation.");
		return null;
	}

	/**
	 * destroy処理。
	 * destroy前に {@link #onPreDestroy()} を実行します。
	 */
	@Override
	public final void destroy() {
		if (!onPreDestroy()) {
			return;
		}

		if (destroyWithAnimation) {
			SmartGWTUtil.closeAnimationScreen(this, new AnimationCloseCallback() {

				@Override
				public void beforeAnimate() {
					AbstractWindow.super.destroy();
				}

				@Override
				public void afterAnimate() {
				}
			});
		} else {
			super.destroy();
		}

	}

	/**
	 * destroy時にアニメーションを実行するかを返します。
	 *
	 * @return true : アニメーションを実行
	 */
	public boolean isDestroyWithAnimation() {
		return destroyWithAnimation;
	}

	/**
	 * destroy時にアニメーションを実行するかを設定します。
	 *
	 * @param destroyWithAnimation true : アニメーションを実行
	 */
	public void setDestroyWithAnimation(boolean destroyWithAnimation) {
		this.destroyWithAnimation = destroyWithAnimation;
	}

	/**
	 * destroy時のカスタム処理が必要な場合は、オーバーライドして実装してください。
	 * falseを返すと、destroyを実行しません。
	 *
	 * @return true : destroyを実行
	 */
	protected boolean onPreDestroy() {
		return true;
	}

}
