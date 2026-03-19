---
applyTo: "**/*.jsp"
excludeAgent: "coding-agent"
---

# JSP ガイドライン (iPLAss)

## エスケープ

- `pageEncoding="utf-8"` と `trimDirectiveWhitespaces="true"` を指定する
- 動的な値の出力にはエスケープを適用する: HTML→`m:esc`(EL関数)/`<c:out>`(JSTLタグ), JS→`m:escJs`(EL関数), XML→`m:escXml`(EL関数)。スクリプトレット内では `StringUtil.escapeHtml()` / `StringUtil.escapeJavaScript()` を使用。定数やプリミティブ型はエスケープ不要
- HTML属性値への出力は `<c:out>` で囲む
- エスケープ省略時は理由をコメントで明記する

## CSRFトークン

- 状態変更を伴うフォーム送信・AJAXには `${m:fixToken()}`（CSRFトークン出力EL関数）でトークンを含める
- JavaScript経由の送信では `_t` パラメータにトークン値を設定する

## JSTL / EL

- 条件分岐・ループは JSTL（`<c:if>`, `<c:forEach>`, `<c:choose>`）やEL式を優先する
- 本プロジェクト提供のカスタムタグ（`<m:auth>`権限チェック, `<m:bind>`データバインド, `<m:errors>`エラー表示, `<m:renderContent>`コンテンツ描画）およびEL関数（`m:rs()`メッセージ取得, `m:tcPath()`テナントパス, `m:token()`トークン）を優先使用する
- 表示文字列は `${m:rs("mtp-gem-messages", "key")}` で解決する（i18nリソースバンドル参照）。パラメータ付きは `${m:rsp(...)}`
- 既存EL関数やユーティリティを活用する
- `<%! %>` 宣言タグのロジックはJavaクラスに抽出する

## request / session 属性

- 属性名は `Constants.*` 定数を使用する
