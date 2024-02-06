/*
 * Copyright (C) 2015 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.entity.bulkupdate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * ラムダ式を利用してBulkUpdatableを構築するためのクラス。
 * 以下のような使い方。
 * 
 * <pre>
 * EntityManager em = ...
 * 
 * List<String> oids = new ArrayList<>();
 * long[] counter = {0};
 * em.bulkUpdate(BulkUpdatable.as("TargetEntity").onNext(() -> {
 *     //onNextで次の1件のBulkUpdateEntityを返却するように実装
 *     if (counter[0] >= 100) {
 *       //処理対象が終了した場合は、nullを返却
 *       return null;
 *     }
 *     GenericEntity ret = new GenericEntity("TargetEntity");
 *     ret.setName("hoge");
 *     counter[0]++;
 *     return new BulkUpdateEntity(UpdateMethod.INSERT, ret);
 *   }).onUpdated(bue -> {
 *     //onUpdatedは、更新処理後のコールバック処理を記述可能（オプション）
 *     oids.add(bue.getEntity().getOid());
 *   }).onClose(() -> {
 *     //onCloseは、BulkUpdatableのクローズ処理を記述可能（オプション）
 *     doSomething...
 *   }));
 * 
 * </pre>
 * 
 * @author K.Higuchi
 *
 */
public class FunctionalEntityStream implements BulkUpdatable {
	
	private String definitionName;
	private List<String> updateProperties;
	private boolean enableAuditPropertySpecification;
	private Supplier<BulkUpdateEntity> onNext;
	private Runnable onClose;
	private Consumer<BulkUpdateEntity> onUpdated;
	
	/**
	 * BulkUpdateEntityを取得するロジックを記述。
	 * 処理対象のBulkUpdateEntityが既にない場合はnullを返却するように実装。
	 * 
	 * @param onNext
	 * @return
	 */
	public FunctionalEntityStream onNext(Supplier<BulkUpdateEntity> onNext) {
		this.onNext = onNext;
		return this;
	}
	
	/**
	 * Entityの定義名を指定
	 * 
	 * @param definitionName
	 * @return
	 */
	public FunctionalEntityStream definitionName(String definitionName) {
		this.definitionName = definitionName;
		return this;
	}
	
	/**
	 * 更新時の更新対象プロパティを指定
	 * 
	 * @param propName
	 * @return
	 */
	public FunctionalEntityStream updateProperties(String... propName) {
		if (propName != null) {
			if (updateProperties == null) {
				updateProperties = new ArrayList<>();
			}
			for (String pn: propName) {
				if (!updateProperties.contains(pn)) {
					updateProperties.add(pn);
				}
			}
		}
		return this;
	}

	/**
	 * バルク更新（INSERT時）の際、EntityにcreateBy,createDate,updateBy,updateDateの値を
	 * 指定してその値のまま登録するように指定します。
	 * このフラグを利用する場合、
	 * 当該処理を呼び出すユーザーがadmin権限を保持している必要があります。
	 * 
	 * @return
	 */
	public FunctionalEntityStream auditPropertySpecified() {
		this.enableAuditPropertySpecification = true;
		return this;
	}

	/**
	 * BulkUpdatableのクローズ処理を記述。
	 * 
	 * @param onClose
	 * @return
	 */
	public FunctionalEntityStream onClose(Runnable onClose) {
		this.onClose = onClose;
		return this;
	}
	
	/**
	 * 更新処理成功後のコールバック処理を記述可能。
	 * 
	 * @param onUpdated
	 * @return
	 */
	public FunctionalEntityStream onUpdated(Consumer<BulkUpdateEntity> onUpdated) {
		this.onUpdated = onUpdated;
		return this;
	}
	
	

	@Override
	public Iterator<BulkUpdateEntity> iterator() {
		if (onNext == null) {
			throw new IllegalStateException("onNext function not specified");
		}
		
		return new Iterator<BulkUpdateEntity>() {
			
			private boolean peeked = false;
			private BulkUpdateEntity next;

			@Override
			public boolean hasNext() {
				peek();
				if (next == null) {
					return false;
				} else {
					return true;
				}
			}
			
			private void peek() {
				if (!peeked) {
					next = onNext.get();
					peeked = true;
				}
			}

			@Override
			public BulkUpdateEntity next() {
				peek();
				if (next == null) {
					throw new NoSuchElementException();
				} else {
					peeked = false;
					BulkUpdateEntity ret = next;
					next = null;
					return ret;
				}
			}
		};
	}

	@Override
	public String getDefinitionName() {
		return definitionName;
	}

	@Override
	public boolean isEnableAuditPropertySpecification() {
		return enableAuditPropertySpecification;
	}

	@Override
	public void updated(BulkUpdateEntity updatedEntity) {
		if (onUpdated != null) {
			onUpdated.accept(updatedEntity);
		}
	}

	@Override
	public void close() {
		if (onClose != null) {
			onClose.run();
		}
	}

	@Override
	public List<String> getUpdateProperties() {
		return updateProperties;
	}
	
	

}
