# RPGText 概要・クイックリファレンス

RPGText は、Minecraft サーバー用プラグインで、RPG ゲームのような順次テキスト表示システムを提供します。YML ファイルに特殊な記法でストーリーを記述し、プレイヤーとの対話やゲーム内でのイベント制御を行えます。

## 🚀 クイックスタート

### 1. 最初のスクリプト作成

```yaml
# my_first.yml
greeting:
  - こんにちは、%player%さん！
  - RPGTextへようこそ
  - /? 続ける 終了
  - /?続ける 素晴らしい選択です！
  - /?終了 また会いましょう
```

### 2. 実行方法

```
/rpgtext config <プレイヤー名> my_first.yml/greeting
```

## 📚 ドキュメント構造

### 🌟 初心者向け

- **[入門ガイド](getting-started/README.md)** - 基本的な使い方を段階的に学習
  - [基本構造](getting-started/basic-structure.md) - YML ファイルの書き方
  - [初回スクリプト作成](getting-started/first-script.md) - 実際に動くスクリプトを作成

### 📖 リファレンス

- **[コマンドリファレンス](commands/README.md)** - 全コマンドの詳細説明
  - [基本設定](commands/basic-settings.md) - sound, speed, color
  - [ナビゲーション](commands/navigation.md) - jump
  - [選択肢・インタラクション](commands/interaction.md) - ?, /?
  - [変数・スコア管理](commands/variables.md) - score, add
  - [条件分岐](commands/conditions.md) - if 文
  - [アイテム管理](commands/items.md) - has, removeItem
  - [ゲーム制御](commands/game-control.md) - command, freeze, wait
  - [音声制御](commands/audio.md) - singlesound

### 🎯 実用例

- **[実用例・サンプル](examples/README.md)** - コピー&ペースト可能な実装例
  - [基本的な会話](examples/basic-conversation.md) - NPC 対話システム
  - [ショップシステム](examples/shop-system.md) - アイテム売買システム

### 🔧 技術情報

- **[リファレンス資料](reference/README.md)** - 技術的な詳細情報
  - [特殊変数](reference/special-variables.md) - %player%, %level% など
  - [カラーコード](reference/color-codes.md) - 色と装飾の全一覧
  - [制限事項](reference/limitations.md) - できないこと・注意点
  - [ベストプラクティス](reference/best-practices.md) - 推奨される実装方法

### 🆘 トラブルシューティング

- **[問題解決](troubleshooting/README.md)** - エラーや問題の解決方法
  - [よくあるエラー](troubleshooting/common-errors.md) - 典型的な問題と解決策
  - [デバッグ方法](troubleshooting/debugging-tips.md) - 効率的な問題解決手順

## ⚡ クイックリファレンス

### よく使用するコマンド

| コマンド                        | 用途           | 例                               |
| ------------------------------- | -------------- | -------------------------------- |
| `/sound <音声> <音量> <ピッチ>` | 音声設定       | `/sound block.note.bell 1 1.5`   |
| `/speed <文字数>`               | 表示速度       | `/speed 30`                      |
| `/color <コード>`               | 文字色         | `/color \6\l` (金色・太字)       |
| `/jump <移動先>`                | セクション移動 | `/jump section_name`             |
| `/? <選択肢>`                   | 選択肢表示     | `/? はい いいえ`                 |
| `/?<選択肢> <処理>`             | 分岐処理       | `/?はい 良い選択です`            |
| `/score <変数> <値>`            | 変数設定       | `/score money 100`               |
| `/if <条件> <処理>`             | 条件分岐       | `/if money > 50 購入可能`        |
| `/wait <ティック>`              | 待機           | `/wait 40` (2 秒)                |
| `/command <コマンド>`           | MC コマンド    | `/command give %player% diamond` |

### 特殊変数

| 変数         | 説明             | 例                       |
| ------------ | ---------------- | ------------------------ |
| `%player%`   | プレイヤー名     | `こんにちは%player%さん` |
| `%level%`    | プレイヤーレベル | `レベル: %level%`        |
| `%hp%`       | 体力             | `体力: %hp%`             |
| `%food%`     | 満腹度           | `満腹度: %food%`         |
| `\\変数名\\` | カスタム変数     | `所持金: \\money\\G`     |

### カラーコード

| コード | 色/効果    | 用途               |
| ------ | ---------- | ------------------ |
| `\r`   | リセット   | 色のリセット       |
| `\6\l` | 金色・太字 | 重要な情報         |
| `\a`   | 明るい緑   | 成功メッセージ     |
| `\c`   | 赤         | 警告・エラー       |
| `\b`   | 水色       | システムメッセージ |
| `\d`   | 明るい紫   | 魔法・神秘         |
| `\e`   | 黄色       | 注意・警告         |

## 🎮 基本的な使用例

### シンプルな会話

```yaml
simple_talk:
  - /color \2
  - こんにちは、%player%さん！
  - /color \r
  - 今日はいい天気ですね
  - /? 同感です そうでもない
  - /?同感です そうですね！
  - /?そうでもない そうですか...
```

### ショップ例

```yaml
shop:
  - いらっしゃいませ！
  - 所持金: \\money\\ゴールド
  - /? 剣を買う(100G) 帰る
  - /?剣を買う(100G) /jump buy_sword
  - /?帰る また来てください

buy_sword:
  - /if money >= 100 /jump purchase
  - お金が足りません

purchase:
  - /score money - 100
  - /command give %player% minecraft:iron_sword
  - 剣を購入しました！
```

### 変数システム

```yaml
status_check:
  - /score hp 100
  - /score money 50
  - === ステータス ===
  - 体力: \\hp\\
  - 所持金: \\money\\G
  - /if hp < 20 体力が危険です！
  - /if money > 100 お金持ちですね
```

## 🎯 学習パス

### 初心者

1. [基本構造](getting-started/basic-structure.md)で YML の書き方を学習
2. [初回スクリプト作成](getting-started/first-script.md)で実際に作成
3. [基本設定](commands/basic-settings.md)で音声・色・速度を理解

### 中級者

1. [変数・スコア管理](commands/variables.md)でゲーム状態の管理を学習
2. [条件分岐](commands/conditions.md)で複雑な処理を実装
3. [実用例](examples/README.md)で実際のシステムを参考

### 上級者

1. [制限事項](reference/limitations.md)でシステムの限界を理解
2. [ベストプラクティス](reference/best-practices.md)で効率的な実装方法を習得
3. [デバッグ方法](troubleshooting/debugging-tips.md)で問題解決スキルを向上

## 🔗 外部リンク

- **公式フォーラム** - 最新情報と質問
- **GitHub リポジトリ** - ソースコードと Issue 報告
- **コミュニティ Discord** - リアルタイムでの質問・相談

## 💡 はじめに読むべきページ

RPGText が初めての方は、以下の順序で読み進めることをお勧めします：

1. **[入門ガイド](getting-started/README.md)** - 全体的な概念を理解
2. **[基本構造](getting-started/basic-structure.md)** - ファイルの書き方を学習
3. **[初回スクリプト作成](getting-started/first-script.md)** - 実際に作って動かす
4. **[基本的な会話](examples/basic-conversation.md)** - より実用的な例を参考
5. **[コマンドリファレンス](commands/README.md)** - 必要に応じて詳細を確認

問題が発生した場合は、[よくあるエラー](troubleshooting/common-errors.md)をまず確認してください。

RPGText で素晴らしい物語とゲーム体験を作成してください！
