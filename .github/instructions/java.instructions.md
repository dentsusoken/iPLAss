---
applyTo: "**/*.java"
excludeAgent: "coding-agent"
---

# Java ガイドライン (iPLAss)

## Public API

- `org.iplass.mtp.*` のシグネチャでは公開型のみ使用する（`impl.*` を含めない）
- service-config.xmlのプロパティおよびMetaData属性は後方互換性を維持

## MetaData / Runtime パターン

本プロジェクトでは設定（MetaData）と実行ロジック（Runtime）を分離する:
- MetaData（`extends BaseMetaData`）: getter/setter のみ持つ設定Bean。ロジックを含めない
- Runtime（MetaDataの内部クラス, `extends BaseMetaDataRuntime`）: ロジックの実装先。init()で高コスト処理をキャッシュし、init()後のフィールドは不変
- Runtimeは複数スレッドから共有される。リクエスト固有の可変データはメンバ変数に保持せずメソッドのローカル変数で扱う
- MetaDataとユーザー向けDefinition間の変換はapplyConfig() / currentConfig()で実装
- Runtimeの階層はMetaDataの階層と対応させる
- 不変データキャリア（DTO等）にはrecordを検討。MetaDataはJavaBeanを維持

## その他

- Serviceクラス: グローバル設定 + Runtimeのライフサイクル管理のみ担当する
- 例外はApplicationException（ユーザーに通知するエラー）/ SystemException（内部エラー）を使い分ける
- 致命的エラーにはロガーカテゴリ `mtp.fatal` をERRORレベルで使用
- debugログは文字列連結でなくプレースホルダ `{}` を使用
- AutoCloseableリソースにはtry-with-resourcesを使用
- `instanceof` チェック後のキャストには pattern matching (`instanceof Type t`) を使用

## Null安全

- メソッドの戻り値で「値がない」を表す場合は `Optional` の使用を検討する（戻り値専用）
- コレクションの戻り値は空コレクション（`List.of()` 等）を返す
- 文字列比較はリテラル側から呼ぶ: `"literal".equals(variable)`

## JavaDoc 国際化

- Public APIにはGroovyTemplateによる日英バイリンガルJavaDocを付与:
  `<% if (doclang == "ja") out.print '...' %>` / `<% if (doclang == "en") out.print '...' %>`
- エスケープ: `<`=`\&lt;` `>`=`\&gt;` `$`=`\&#36;` `\`=`\&#92;`（HTML/GroovyTemplateタグ除く）
