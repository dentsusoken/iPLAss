/*
 * サンプル: CSVアップロード時にカスタムReference項目を設定するInterrupter
 *
 * AdminConsoleのUtilityClassに登録して使用します。
 * 検索画面の検索条件セクション > CSVアップロードInterrupterクラス名 に
 * このクラスの完全修飾名を設定してください。
 *
 * UtilityClassとして登録する場合はGroovyスクリプトになりますが、
 * このファイルはJavaでの参考実装です。
 */
package org.iplass.mtp.gem.sample;

import java.util.List;

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.view.generic.SearchFormCsvUploadInterrupter;
import org.iplass.mtp.web.template.TemplateUtil;

/**
 * カスタムCSVアップロードInterrupter サンプル実装
 *
 * <p>
 * カスタムJSP (customFileUpload.jsp) から送信されたReference項目の
 * 選択結果を、CSVの各行のEntityにセットします。
 * </p>
 *
 * <h3>AdminConsole UtilityClass登録時のGroovyコード例:</h3>
 * 
 * <pre>
 * import org.iplass.mtp.command.RequestContext
 * import org.iplass.mtp.entity.Entity
 * import org.iplass.mtp.entity.GenericEntity
 * import org.iplass.mtp.entity.definition.EntityDefinition
 * import org.iplass.mtp.entity.definition.PropertyDefinition
 * import org.iplass.mtp.view.generic.SearchFormCsvUploadInterrupter
 * import org.iplass.mtp.web.template.TemplateUtil
 *
 * class SampleCsvUploadInterrupter implements SearchFormCsvUploadInterrupter {
 *
 *     void dataMapping(int row, Entity entity, EntityDefinition definition, CsvRegistrationType registrationType) {
 *         RequestContext request = TemplateUtil.getRequestContext()
 *         String refOid = request.getParam("customRefOid")
 *         String refDefName = request.getParam("customRefDefName")
 *
 *         if (refOid != null &amp;&amp; refDefName != null) {
 *             Entity refEntity = new GenericEntity(refDefName)
 *             refEntity.setOid(refOid)
 *             // ★ "yourReferencePropertyName" を実際のReference項目名に置き換えてください
 *             entity.setValue("yourReferencePropertyName", refEntity)
 *         }
 *     }
 * }
 * </pre>
 */
public class SampleCsvUploadInterrupter implements SearchFormCsvUploadInterrupter {

    /**
     * CSVデータから登録用のデータをマッピングする際に呼ばれます。
     * カスタムJSPのhiddenフィールドから送信されたReference OIDを取得し、
     * CSV各行のEntityに参照先をセットします。
     *
     * @param row              行番号
     * @param entity           CSVから読み取ったEntity
     * @param definition       Entity定義
     * @param registrationType 登録処理の種類 (INSERT/UPDATE/DELETE)
     */
    @Override
    public void dataMapping(int row, Entity entity, EntityDefinition definition,
            CsvRegistrationType registrationType) {

        RequestContext request = TemplateUtil.getRequestContext();

        // カスタムJSPから送信されたhiddenパラメータを取得
        String refOid = request.getParam("customRefOid");
        String refVersion = request.getParam("customRefVersion");
        String refDefName = request.getParam("customRefDefName");

		// 何らかの処理
        System.out.println("Row " + row + ": Received refOid=" + refOid
                + ", refVersion=" + refVersion + ", refDefName=" + refDefName);
    }

    /**
     * 出力するサンプルCSVデータを返します。
     * nullを返すとプロパティ型に応じたランダムな値が出力されます。
     */
    @Override
    public List<Entity> sampleCsvData(EntityDefinition definition, List<PropertyDefinition> properties) {
        return null;
    }
}
