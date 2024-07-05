/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.entity;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.Manager;
import org.iplass.mtp.SystemException;
import org.iplass.mtp.entity.bulkupdate.BulkUpdatable;
import org.iplass.mtp.entity.bulkupdate.BulkUpdateEntity;
import org.iplass.mtp.entity.fulltextsearch.FulltextSearchOption;
import org.iplass.mtp.entity.query.Query;



/**
 * <p>
 * Entityを管理するクラスのインタフェースです。
 * EntityManagerを利用し、定義されているEntityのデータのCRUD操作が可能です。
 * </p>
 * <p>
 * EntityManager操作中に、例外が発生した場合のトランザクションに関する挙動は次の形となります。
 * <ul>
 * <li>{@link SystemException}およびRuntime例外、エラー:トランザクションは自動的にsetRollbackOnlyに設定される</li>
 * <li>{@link ApplicationException}:参照メソッド（load,searchなど）では、トランザクションはsetRollbackOnlyに設定されない。更新系メソッドでは自動的にsetRollbackOnlyに設定される</li>
 * </ul>
 * </p>
 *
 * @author K.Higuchi
 *
 */
public interface EntityManager extends Manager {

	/**
	 * 指定のEntityの検証を行います。
	 * 検証の前に、プロパティの値は正規化されます。
	 *
	 * @param entity 検証対象のEntity
	 * @return 検証結果
	 */
	public ValidateResult validate(Entity entity);

	/**
	 * 指定のEntityの指定のプロパティの検証を行います。
	 * 検証の前に、プロパティの値は正規化されます。
	 *
	 * @param entity 検証対象のEntity
	 * @param properties 検証対象のプロパティ名のリスト
	 * @return 検証結果
	 */
	public ValidateResult validate(Entity entity, List<String> properties);

	/**
	 * 指定のEntityのプロパティの値を正規化します。
	 * 
	 * @param entity 正規化対象のEntity
	 */
	public void normalize(Entity entity);
	
	/**
	 * 指定のEntityの指定のプロパティの値を正規化します。
	 * 
	 * @param entity  正規化対象のEntity
	 * @param properties 正規化対象のプロパティ名のリスト
	 */
	public void normalize(Entity entity, List<String> properties);

	/**
	 * 指定の条件で検索します。
	 *
	 * @param query 検索条件
	 * @return 検索条件に一致したデータのリスト
	 */
	public <T extends Entity> SearchResult<T> searchEntity(Query query);

	/**
	 * 指定の条件で検索します。
	 *
	 * @param query 検索条件
	 * @param option 検索処理のオプション指定
	 * @return
	 */
	public <T extends Entity> SearchResult<T> searchEntity(Query query, SearchOption option);

	/**
	 * 指定の条件で検索します。
	 * データ件数が多い場合、取得したデータを一括でロードせずに、1件ずつロードします。
	 * ロードしたデータは、callbackに渡されます。
	 *
	 * @param query 検索条件
	 * @param callback 順次読み込んだデータを受けるコールバック処理
	 */
	public <T extends Entity> void searchEntity(Query query, Predicate<T> callback);

	/**
	 * 指定の条件で検索します。
	 * データ件数が多い場合、取得したデータを一括でロードせずに、1件ずつロードします。
	 * ロードしたデータは、callbackに渡されます。<br>
	 * optionでは、{@link SearchOption#unnotifyListeners()}の設定のみ有効です。その他の設定は本メソッド呼び出しでは無視されます。
	 *
	 * @param query 検索条件
	 * @param option 検索処理のオプション指定
	 * @param callback 順次読み込んだデータを受けるコールバック処理
	 */
	public <T extends Entity> void searchEntity(Query query, SearchOption option, Predicate<T> callback);

	/**
	 * Entityの形ではない、汎用的な検索（複数Entityをまたいだ集計クエリー等）を行うための検索メソッドです。
	 *
	 * @param query 検索条件
	 * @return queryに指定されたselect句の項目を保持するObject[]
	 */
	public SearchResult<Object[]> search(Query query);

	/**
	 * 指定の条件で検索します。
	 * Entityの形ではない、汎用的な検索（複数Entityをまたいだ集計クエリー等）を行うための検索メソッドです。
	 *
	 * @param query 検索条件
	 * @param option 検索処理のオプション指定
	 * @return
	 */
	public SearchResult<Object[]> search(Query query, SearchOption option);

	/**
	 * Entityの形ではない、汎用的な検索（複数Entityをまたいだ集計クエリー等）を行うためのメソッドです。
	 * データ件数が多い場合に利用可能で、取得したデータを一括でロードせずに1件ずつロードするためのメソッドです。
	 * ロードしたデータは、callbackに渡されます。
	 *
	 * @param query 検索条件
	 * @param callback 順次読み込んだデータを受けるコールバック処理
	 */
	public void search(Query query, Predicate<Object[]> callback);

	/**
	 * Entityの形ではない、汎用的な検索（複数Entityをまたいだ集計クエリー等）を行うためのメソッドです。
	 * データ件数が多い場合に利用可能で、取得したデータを一括でロードせずに1件ずつロードするためのメソッドです。
	 * ロードしたデータは、callbackに渡されます。<br>
	 * optionでは、{@link SearchOption#unnotifyListeners()}の設定のみ有効です。その他の設定は本メソッド呼び出しでは無視されます。
	 *
	 * @param query 検索条件
	 * @param option 検索処理のオプション指定
	 * @param callback 順次読み込んだデータを受けるコールバック処理
	 */
	public void search(Query query, SearchOption option, Predicate<Object[]> callback);

	/**
	 * 検索件数を取得します。
	 *
	 * @param cond 検索条件
	 * @return 実検索件数
	 */
	public int count(Query query);

	/**
	 * 指定のoid（Entityオブジェクトを一意に特定するID）で特定されるデータを取得します。
	 * 取得するプロパティは、Entityに定義されているもの全てを取得します。
	 * ReferencePropertyが定義されている場合は、
	 * 当該プロパティにはoidとnameが格納されたEntityのインスタンスが格納されます。
	 * ReferencePropertyが指定されている場合で、そのプロパティが複数可の場合は、
	 * 当該プロパティはEntity[]の形で取得されます。
	 *
	 * @param oid オブジェクトID
	 * @param definitionName Entity定義名
	 * @return oidで指定されるデータ
	 */
	public Entity load(String oid, String definitionName);

	/**
	 * 指定のoid（Entityオブジェクトを一意に特定するID）、versionで特定されるデータを取得します。
	 * 取得するプロパティは、Entityに定義されているもの全てを取得します。
	 * ReferencePropertyが定義されている場合は、
	 * 当該プロパティにはoidとnameが格納されたEntityのインスタンスが格納されます。
	 * ReferencePropertyが指定されている場合で、そのプロパティが複数可の場合は、
	 * 当該プロパティはEntity[]の形で取得されます。
	 *
	 * @param oid オブジェクトID
	 * @param version バージョン番号
	 * @param definitionName Entity定義名
	 * @return oid,versionで指定されるデータ
	 */
	public Entity load(String oid, Long version, String definitionName);
	//TODO -1は最大のバージョン番号を表すみたいなものは必要か

	/**
	 * 指定のoid（Entityオブジェクトを一意に特定するID）で特定されるデータを取得します。
	 * 取得するプロパティは、optionにて指定されたものです。
	 *
	 * @param oid オブジェクトID
	 * @param definitionName Entity定義名
	 * @param option 読込オプション、読み込むプロパティを指定可能
	 * @return oidで指定されるデータ
	 */
	public Entity load(String oid, String definitionName, LoadOption option);

	/**
	 * 指定のoid（Entityオブジェクトを一意に特定するID）、versionで特定されるデータを取得します。
	 * 取得するプロパティは、optionにて指定されたものです。
	 *
	 * @param oid オブジェクトID
	 * @param version バージョン番号
	 * @param definitionName Entity定義名
	 * @param option 読込オプション、読み込むプロパティを指定可能
	 * @return oid,versionで指定されるデータ
	 */
	public Entity load(String oid, Long version, String definitionName, LoadOption option);

	/**
	 * 指定のoid（Entityオブジェクトを一意に特定するID）で一意に特定されるデータを取得し、更新ロックします。
	 * ロックは、トランザクション終了まで保持されます。
	 *
	 * @param oid オブジェクトID
	 * @param definitionName Entity定義名
	 * @return oidで指定されるデータ
	 */
	public Entity loadAndLock(String oid, String definitionName);//バージョン管理の場合はすべてロック

	/**
	 * 指定のoid（Entityオブジェクトを一意に特定するID）で一意に特定されるデータを取得し、更新ロックします。
	 * ロックは、トランザクション終了まで保持されます。
	 * 取得するプロパティは、optionにて指定されたものです。
	 *
	 * @param oid オブジェクトID
	 * @param definitionName Entity定義名
	 * @param option 読込オプション、読み込むプロパティを指定可能
	 * @return oidで指定されるデータ
	 */
	public Entity loadAndLock(String oid, String definitionName, LoadOption option);

	/**
	 * 指定のEntityキー情報で特定されるデータを取得します。
	 * 取得するプロパティは、Entityに定義されているもの全てを取得します。
	 * ReferencePropertyが定義されている場合は、
	 * 当該プロパティにはoidとnameが格納されたEntityのインスタンスが格納されます。
	 * ReferencePropertyが指定されている場合で、そのプロパティが複数可の場合は、
	 * 当該プロパティはEntity[]の形で取得されます。
	 *
	 * @param keys Entityキー情報
	 * @param definitionName Entity定義名
	 * @return キー情報で指定されるデータ
	 */
	public List<Entity> batchLoad(List<EntityKey> keys, String definitionName);

	/**
	 * 指定のEntityキー情報で特定されるデータを取得します。
	 * 取得するプロパティは、optionにて指定されたものです。
	 *
	 * @param keys Entityキー情報
	 * @param definitionName Entity定義名
	 * @param option 読込オプション、読み込むプロパティを指定可能
	 * @return キー情報で指定されるデータ
	 */
	public List<Entity> batchLoad(List<EntityKey> keys, String definitionName, LoadOption option);

	/**
	 * 指定の引数のentityを追加します。
	 * entityにoid（オブジェクトID）が設定されていても必ず新しいIDで採番されます。
	 *
	 * @param entity 追加対象のデータ
	 * @return オブジェクトID
	 */
	public String insert(Entity entity);

	/**
	 * 指定の引数のentityを追加します。
	 *
	 * @param entity 追加対象のデータ
	 * @param 追加時の追加方法に関する指定
	 *
	 * @return オブジェクトID
	 */
	public String insert(Entity entity, InsertOption option);

	/**
	 * 引数のentityを更新します。
	 *
	 * @param entity 更新対象のデータ
	 * @param option 更新時の更新方法に関する指定
	 * @throws EntityConcurrentUpdateException 更新対象のデータが存在しない場合、同時更新が発生した場合
	 */
	public void update(Entity entity, UpdateOption option);

	/**
	 * 引数のentityに設定されたoid（オブジェクトID）で一意に特定されるデータを削除します。
	 *
	 * @param entity 削除対象のデータ
	 * @param option 削除時に削除方法に関する指定
	 * @throws EntityConcurrentUpdateException 削除対象のデータが存在しない場合、同時更新が発生した場合
	 */
	public void delete(Entity entity, DeleteOption option);
	//論理削除。バージョン含め全て削除
	//別テーブルに保管（のほうが都合がよい。同一テーブルだと、oidの重複、UniqueKeyの重複が発生する）

	/**
	 * 引数のcondに一致するデータを設定された値にて一括更新します。
	 * タイムスタンプチェックなどは行いません。
	 * 別のデータ（親オブジェクトなど）で一貫性を保障した上での、ステータス情報の一括更新等の用途を想定しています。
	 *
	 * ReferencePropertyの更新は不可です。
	 * updateAllでは、Entityの更新イベントは発生しません。
	 *
	 * @param cond 更新対象のデータを指定する条件
	 * @return 更新件数
	 */
	public int updateAll(UpdateCondition cond);

	/**
	 * 引数のcondに一致するデータを一括で削除します。
	 * タイムスタンプチェックなどは行いません。
	 * 別のデータ（親オブジェクトなど）で一貫性を保障した上での、一括削除操作を想定しています。
	 * deleteAllでは、Entityの削除イベントは発生しません。
	 *
	 * @param cond 削除対象のデータを指定する条件
	 * @return 削除件数
	 */
	public int deleteAll(DeleteCondition cond);

	/**
	 * bulkUpdatableで指定される一連のEntityを一括で更新（Insert/Update/Delete）します。
	 * 更新処理の際は、EntityEventListenerの呼び出しや、Validation、タイムスタンプチェック、CascadeDelete処理などは実行されません。
	 * 外部のマスタデータの取り込み、初期データImportなどの用途での利用を想定しています。
	 * bulkUpdatableのclose()は処理後（もしくは例外発生後）、当メソッド内で呼び出されます。<br>
	 * bulkUpdate()を呼び出す実行ユーザーは当該Entityに対して登録、更新、削除権限を範囲条件なしに保有している必要があります。<br>
	 * 更新、削除時には対象とするEntityのoidを指定してください。また、バージョン管理が有効の場合はversionの値も明示的に指定する必要があります。<br>
	 *
	 * <b>注意</b><br>
	 * Binary型、LongText型、AutoNumber型を利用している場合、bulkUpdate利用によっても高速化は望めない場合があります。<br>
	 * また、{@link BulkUpdateEntity.UpdateMethod#MERGE}を利用している場合、bulkUpdate利用によっても高速化は望めない場合があります。
	 *
	 * @param bulkUpdatable
	 */
	public void bulkUpdate(BulkUpdatable bulkUpdatable);

	/**
	 * 引数で指定されるBinaryReferenceを取得します。
	 *
	 * @param lobId
	 * @return BinaryReference
	 */
	public BinaryReference loadBinaryReference(long lobId);

	/**
	 * BinaryReferenceを新規に作成します。
	 * 引数のisのデータを同時にバイナリデータとして書き込みます。<br>
	 * isがnullの場合は、バイナリデータが空の状態で新規に作成されます。
	 * 書き込みは別途getOutputStream(BinaryReference)メソッドを介して、行うことが可能です。<br>
	 * 作成されたBinaryReferenceはまだEntityに紐付いていない状態です。
	 * 引数のisはBinaryReference作成後、当メソッド内でcloseします。
	 *
	 * @param name
	 * @param type
	 * @param is
	 * @return
	 */
	public BinaryReference createBinaryReference(String name, String type, InputStream is);

	/**
	 * BinaryReferenceを新規に作成します。
	 * 引数のfileのデータを同時にバイナリデータとして書き込みます。<br>
	 * 作成されたBinaryReferenceはまだEntityに紐付いていない状態です。<br>
	 * nameが未指定の場合は、fileの名前がnameに指定されます。
	 * typeが未指定の場合は、fileの拡張子からmimetypeが解決されます。
	 *
	 * @param file
	 * @param name
	 * @param type
	 * @return
	 */
	public BinaryReference createBinaryReference(File file, String name, String type);

	/**
	 * 引数で指定されたBinaryReferenceのバイナリデータを取得するためのInputStreamを取得します。
	 *
	 * @param binaryReference
	 * @return
	 */
	public InputStream getInputStream(BinaryReference binaryReference);

	/**
	 * 引数で指定されたBinaryReferenceのバイナリデータを書き込むためのOutputStreamを取得します。
	 * OutputStramは利用終了したら必ずclose()してください。
	 *
	 * @param binaryReference
	 * @return
	 */
	public OutputStream getOutputStream(BinaryReference binaryReference);

//全てのユーザーのロックができてしまってよいか（インタフェースとしてユーザーIDを開放すべきか）
//⇒apiレベルでは自身のユーザーIDでロック＆開放。管理者は誰のIDのものでも開放できる
	/**
	 * 現在、当該メソッドを呼び出しているユーザーにて、 指定のEntityをデータをロックします。
	 * ユーザーによるロックはunlockByUser()メソッドにて明示的に開放しない限り、
	 * トランザクションを終了してもロックは保持されます。
	 *
	 * @param oid オブジェクトID
	 * @param definitionName Entity定義名
	 * @return ロックできた場合true
	 */
	public boolean lockByUser(String oid, String definitionName);

	/**
	 * ユーザーにてロックされているEntityのデータのロックを解除します。
	 *
	 *
	 * @param oid オブジェクトID
	 * @param definitionName Entity定義名
	 * @return
	 */
	public boolean unlockByUser(String oid, String definitionName);


	/**
	 * ごみ箱から、削除します。
	 * recycleBinIdは、ごみ箱内のデータを一意に特定するidです（oidは重複する可能性があるため）。<br>
	 * purge()を呼び出す実行ユーザーは当該Entityに対して削除権限を範囲条件なしに保有している必要があります。<br>
	 *
	 * @param recycleBinId ごみ箱内のデータを一意に特定するid
	 * @param definitionName Entity定義名
	 */
	public void purge(long recycleBinId, String definitionName);

	/**
	 * ごみ箱から復活します。<br>
	 * restore()を呼び出す実行ユーザーは当該Entityに対して削除権限を範囲条件なしに保有しているか、
	 * もしくは自身がごみ箱に格納したEntityである必要があります。<br>
	 *
	 * @param recycleBinId ごみ箱内のデータを一意に特定するid
	 * @param definitionName Entity定義名
	 */
	public Entity restore(long recycleBinId, String definitionName);

	/**
	 * ごみ箱の中身のリストを取得します。
	 * 取得可能なものは、recycleBinId,oid,nameのみです。
	 * recycleBinIdは、ごみ箱内のデータを一意に特定するid。（oidは重複する可能性があるため）
	 *
	 * @param definitionName Entity定義名
	 * @param callback
	 */
	public void getRecycleBin(String definitionName, Predicate<Entity> callback);

	/**
	 * ごみ箱の中身から、指定のrecycleBinIdで特定される1件を取得します。
	 * @param recycleBinId
	 * @param definitionName
	 * @return
	 */
	public Entity getRecycleBin(long recycleBinId, String definitionName);

	/**
	 * 現在の時間を取得します。
	 * 取得される値は、実時間ではなく、あらかじめ設定されたシステム時間である場合もあります。
	 *
	 * @return 現在時間（もしくは事前に設定されたプレビュー時間）
	 */
	public Timestamp getCurrentTimestamp();

	/**
	 * 指定のoid（オブジェクトID）で一意に特定されるデータを取得し、参照先を含めたコピーを行います。
	 * 親子関係の参照先のエンティティはoidが新たに採番され、
	 * 通常の参照の場合、被参照ならコピー対象外になり、被参照でなければそのまま参照されます。
	 * また制約として、変更不可の項目についてはコピー後に変更はできなくなり、
	 * 文字列以外のプロパティでユニーク指定されている場合はコピー自体ができません。
	 *
	 * @param oid オブジェクトID
	 * @param definitionName Entity定義名
	 * @return コピーされたデータ
	 */
	public Entity deepCopy(String oid, String definitionName);

	/**
	 * 指定のoid（オブジェクトID）で一意に特定されるデータを取得し、参照先を含めたコピーを行います。
	 * 親子関係の参照先のエンティティはoidが新たに採番され、
	 * 通常の参照の場合、被参照ならコピー対象外になり、被参照でなければそのまま参照されます。
	 * また制約として、変更不可の項目についてはコピー後に変更はできなくなり、
	 * 文字列以外のプロパティでユニーク指定されている場合はコピー自体ができません。
	 *
	 * @param oid オブジェクトID
	 * @param definitionName Entity定義名
	 * @param option コピー時のオプション
	 * @return コピーされたデータ
	 */
	public Entity deepCopy(String oid, String definitionName, DeepCopyOption option);

	/**
	 * 指定のワードで全文検索します。
	 * defNameが未指定の場合は利用テナントの全エンティティに対して全文検索を実施します。
	 *
	 * @param definitionName Entity定義名
	 * @param keyword 全文検索用キーワード
	 * @return 検索キーワードを含むエンティティデータのリスト
	 */
	public <T extends Entity> SearchResult<T> fulltextSearchEntity(String definitionName, String keyword);

	/**
	 * 指定のワードで全文検索し、oidのリストを取得します。
	 * definitionName、keywordは必須。未指定の場合は空のリストを返却します。
	 *
	 * @param definitionName Entity定義名
	 * @param keyword 全文検索用キーワード
	 * @return 検索キーワードを含むエンティティデータのoidのリスト
	 */
	public List<String> fulltextSearchOidList(String definitionName, String keyword);

	/**
	 * 指定のワードで全文検索し、対象Entity毎のoidリストのMapを取得します。
	 * definitionNames、keywordは必須。未指定の場合は空のMapを返却します。
	 *
	 * @param definitionNames Entity定義名のリスト
	 * @param keyword 全文検索用キーワード
	 * @return 検索キーワードを含むエンティティデータのoidリストのMap
	 */
	public Map<String, List<String>> fulltextSearchOidList(List<String> definitionNames, String keyword);

	/**
	 * 指定のワードで全文検索し、指定プロパティのみを取得します。
	 * entityPropertiesが未指定の場合は利用テナントの全エンティティに対して全文検索を実施します。
	 *
	 * @param entityProperties Entity定義毎に取得するプロパティ
	 * @param keyword 全文検索用キーワード
	 * @return 検索キーワードを含むエンティティデータのリスト
	 */
	public <T extends Entity> SearchResult<T> fulltextSearchEntity(Map<String, List<String>> entityProperties, String keyword);

	/**
	 * クエリ実行結果に対して、指定のワードで全文検索します。
	 *<pre><b>メモリを大量消費する恐れがありますので、絞り込む条件とリミット条件を指定した上でご利用してください。</b></pre>
	 *
	 * @param Query クエリ、Entity.OIDを検索項目として設定する必要が有ります。 <pre>Entity.OIDを指定しないと、空のリストが返されます。</pre>
	 * @param keyword 全文検索用キーワード
	 * @return SearchOption 検索時のオプション、countTotal=trueの場合総件数を積み上げる
	 */
	public <T extends Entity> SearchResult<T> fulltextSearchEntity(Query query, String keyword, SearchOption option);

	/**
	 * 指定のワードで全文検索し、指定プロパティのみを取得します。
	 * FulltextSearchOptionのconditionsが未指定の場合は利用テナントの全エンティティに対して全文検索を実施します。
	 *
	 * @param keyword 全文検索用キーワード
	 * @param option 全文検索時のオプション
	 * @return 検索キーワードを含むエンティティデータのリスト
	 */
	public <T extends Entity> SearchResult<T> fulltextSearchEntity(String keyword, FulltextSearchOption option);

}
