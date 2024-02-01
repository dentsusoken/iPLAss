/*
 * Copyright (C) 2023 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.auth.log;

import net.logstash.logback.argument.StructuredArgument;
import net.logstash.logback.argument.StructuredArguments;

/**
 * 認証ログに関する固定値定義
 *
 * @author SEKIGUCHI Naoya
 */
public class AuthLogConstants {
	/**
	 * プライベートコンストラクタ
	 */
	private AuthLogConstants() {
	}

	/**
	 * ログキー
	 */
	public static class LogKey {
		/** ID */
		public static final String ID = "id";
		/** 操作 */
		public static final String OPERATION = "operation";
		/** 状態 */
		public static final String STATE = "state";
		/** 例外メッセージ */
		public static final String EXCEPTION_MESSAGE = "exceptionMessage";

		/**
		 * プライベートコンストラクタ
		 */
		private LogKey() {
		}
	}

	/**
	 * 認証操作 列挙値
	 */
	public static enum Operation {
		/** ログイン */
		LOGIN("login"),
		/** パスワード更新 */
		UPDATE_PASSWORD("updatePass"),
		/** パスワードリセット */
		RESET_PASSSROD("resetPass");

		/** 操作文字列 */
		private String value;
		/** ログ出力用引数 */
		private StructuredArgument arg;

		/**
		 * プライベートコンストラクタ
		 *
		 * @param value 操作文字列
		 */
		private Operation(String value) {
			this.value = value;
			this.arg = StructuredArguments.value(LogKey.OPERATION, value);
		}

		/**
		 * 操作文字列を取得する
		 * @return 操作文字列
		 */
		public String getValue() {
			return value;
		}

		/**
		 * ログ出力用の引数を取得する
		 * @return ログ出力用の引数を取得する
		 */
		public StructuredArgument getArg() {
			return arg;
		}
	}

	/**
	 * 認証状態 列挙値
	 */
	public static enum State {
		/** 成功 */
		SUCCESS("success"),
		/** 失敗 */
		FAIL("fail"),
		/** ロック */
		LOCKED("locked"),
		/** パスワード期限切れ */
		PASSWORD_EXPIRED("passExpired");

		/** 状態文字列 */
		private String value;
		/** ログ出力用引数 */
		private StructuredArgument arg;

		/**
		 * プライベートコンストラクタ
		 * @param value 状態文字列
		 */
		private State(String value) {
			this.value = value;
			this.arg = StructuredArguments.value(LogKey.STATE, value);
		}

		/**
		 * 状態文字列を取得する
		 * @return 状態文字列
		 */
		public String getValue() {
			return value;
		}

		/**
		 * ログ出力用の引数を取得する
		 * @return ログ出力用の引数を取得する
		 */
		public StructuredArgument getArg() {
			return arg;
		}
	}
}
