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
package org.iplass.mtp.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * テスト実施時のトランザクションをロールバックするように指定します。
 * クラス、メソッドに指定可能です。
 * </p>
 * <h3>注意</h3>
 * <p>
 * Rollbackを指定した場合、
 * EntityにAutoNumber型（飛び番を許容する）が定義されていて、
 * そのEntityを複数件登録しようとした場合、重複エラーが発生する可能性があります。
 * これは、AutoNumber型（飛び番を許容する）を設定された場合は、
 * 採番を別トランザクションで起動しますが、その採番値のインクリメント自体がロールバックされてしまい、
 * 再度採番された場合に同一値が採番されてしまう為です。<br>
 * この場合は、Rollbackの使用はできません。<br>
 * 同様に、テストコード中に別トランザクションを起動し、その別トランザクション結果によって、
 * 後続処理を行うようなテストコードでもRollbackの使用はできません。
 * </p>
 * 
 * @author K.Higuchi
 *
 */
@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE, ElementType.METHOD})
public @interface Rollback {
}
