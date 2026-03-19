---
applyTo: "**/*.vue"
excludeAgent: "coding-agent"
---

# Vue ガイドライン (iPLAss)

## コンポーネント

- `<script setup lang="ts">` を使用する
- ファイル名は `M` プレフィックス（内部共通は `MI`）を付ける
- 1ファイル1コンポーネント。`index.ts` でバレルエクスポートする
- `v-html` はサーバー生成の信頼済みHTMLにのみ使用する
- カスタムのインタラクティブ要素にはARIA属性とキーボードナビゲーションの実装を推奨する

## API通信

- `useWebApiAdapter` の `get<T>()` / `post<T>()` を使用する
- レスポンスは `WebApiResponse<T>` で型付けする
- キャッシュ可能なリクエストには `cacheable: true` を指定する

## Composable

- `use[Feature]` 命名。コンポーネント固有は同ディレクトリに co-locate、共有ロジックは `composables/` に配置

## 状態管理

- Pinia ストアは `stores/` に配置し、グローバル状態のみ格納する
- コンポーネントローカルな状態は composable か `ref()` で管理する
- 深い階層間のデータ共有は ProvideKey パターン（`InjectionKey<T>` + `useXxxProvide` / `useXxxInject` ペア）で行う

## Props / Emits

- `defineProps<T>()` + `withDefaults()` で型定義する
- `defineEmits<{ (e: 'name', value: T): void }>()` で型付き定義する
- 双方向バインディングには `defineModel<T>()` を使用する

## Vuetify

- ラップ時は Props / Emits を透過させる
- Vuetifyコンポーネントは公開APIのみ利用する（`.v-*` 内部クラスでなく）
- スタイル設定は `mdc_variables.scss` の `@forward` で行う。テンプレート内の直接上書きは最終手段

## i18n

- 表示文字列は `useI18n()` の `t()` で解決する
