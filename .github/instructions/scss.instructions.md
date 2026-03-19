---
applyTo: "**/*.scss"
excludeAgent: "coding-agent"
---

# SCSS ガイドライン (iPLAss)

## ファイル構成

- CSSファイルを直接修正しない。`src/main/sass` 配下のSCSSを修正し `buildSass` でビルドしたCSSをコミットする
- パーシャルファイルは `_` プレフィックスで命名する
- テーマごとの `module.scss` エントリポイントからインポートする
- 機能単位でディレクトリを分割する
- EE固有スタイルはEE側に置く — CE側 `cemodules` をインポートした上でEE分を追加する
- 新規SCSSでは `@use`/`@forward` を使用する — GEM既存コードは `@import` との整合性を優先してよい

## テーマ構造

- 4テーマ（basedesign, flat, horizontal, vertical）の構造を維持する
- 共通スタイルは basedesign に置き各テーマから参照する
- テーマ固有の変数（URL・色等）は `vars/` に分離する

## セレクタ再利用

- ネストは最大4階層
- 共有スタイルにはプレースホルダセレクタ（`%extend_xxx`）を使う
- 繰り返しパターンには `@mixin` を定義する
- 繰り返す値は変数化する — 変数名は用途または近似色名で命名
- ベンダープレフィックスはAutoprefixerに委ねる
- `!important` 使用時はコメントで理由を記載する
- ランタイムで変更する値（テーマ切替等）にはCSS Custom Propertiesを使用する
