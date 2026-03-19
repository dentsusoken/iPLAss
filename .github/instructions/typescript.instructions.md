---
applyTo: "**/*.ts"
excludeAgent: "coding-agent"
---

# TypeScript ガイドライン (iPLAss)

## 型定義

- 不明な型には `unknown` を使用する（`any` でなく）
- オブジェクト型は `interface` を使う — `type` は union/ユーティリティ型のみ
- 列挙には文字列リテラルのユニオン型か `as const` を使用する（`enum` でなく）
- 外部APIレスポンスには必ず型を定義する
- 型定義は `types/` ディレクトリに機能単位で配置する
- 型の検証には `satisfies` を優先する（`as` よりも型安全）
- union型の分岐では `never` を使った網羅性チェック（exhaustive check）を実装する

## import

- 型のみのインポートには `import type` を使用する
- パスエイリアス `@/` を使用する
