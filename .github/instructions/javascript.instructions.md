---
applyTo: "**/*.js"
excludeAgent: "coding-agent"
---

# JavaScript ガイドライン (iPLAss)

## XSS対策

- jQueryセレクタには `es()`（特殊文字エスケープ関数）でエスケープした値を渡す
- JSONパラメータには `escapeJsonParamValue()`（引用符エスケープ関数）を使用する
- テキスト挿入は `.text()` を使用する（`.html()` / `innerHTML` でなく）
- 動的プロパティアクセスにはブラケット記法を使用する（`eval()` / `new Function()` でなく）

## WebAPI

- API呼び出しは本プロジェクトのラッパー関数 `webapi()` / `webapiJson()` / `postAsync()` 経由で行う
- URLは `contextPath` ベースで組み立てる
- 状態変更リクエストにはCSRFトークン（`_t`）を含める
- レスポンスの `status` を必ず検査し、`"SUCCESS"` 以外はエラー処理する

## scriptContext

- サーバー→クライアントの値受け渡しはグローバルオブジェクト `scriptContext` 経由。JS側で構造を変更しない
- `scriptContext` への初期値設定はJSP側（`coreResource.inc.jsp` 等）で行う

## モジュール構造

- IIFEでスコープを閉じ、グローバル名前空間を汚染しない
- 公開APIは名前空間オブジェクトか `$.fn` 経由で公開する

## jQuery

- jQueryオブジェクトには `$` プレフィックスを付ける
- プラグイン定義はIIFE + `$.fn` パターンに従う

## 国際化

- ユーザー向けメッセージは `scriptContext.gem.locale`（i18nメッセージ格納オブジェクト）経由で取得する
- エンティティの多言語プロパティ表示には `getMultilingualString()` を使用する

## エラー処理

- API通信エラーには `alertErrMessage()` を使用し、ページ遷移中（`isPageBeingRefreshed`）は抑制する

## コードスタイル

本モジュールはjQuery + IIFEベースの環境である:
- 新規コードでは `const`/`let` を使用する。既存コードの `var` も修正時に置き換えてよい
- モジュール構造（IIFE + 名前空間）は維持する — ES Modulesへの移行は行わない
- API呼び出しパターン（コールバック方式）は既存ラッパー関数に合わせる
