# 基本構造

RPGTextのスクリプトファイルの基本的な構造と書き方を説明します。

## ファイル形式

RPGTextは**YMLファイル**を使用してスクリプトを記述します。

### ファイルの保存場所

```
plugins/RPGText/messages/
├── your_story.yml
├── chapter1/
│   └── dialogue.yml
└── tutorial.yml
```

## 基本的な構造

### セクションの概念

YMLファイルは**セクション**という単位で構成されます：

```yaml
section_name:
  - メッセージ1
  - メッセージ2
  - /command example
  - メッセージ3

another_section:
  - 別のセクションのメッセージ
```

### セクション名のルール

- 英数字とアンダースコア（`_`）を使用
- 日本語は避ける（エラーの原因となる可能性）
- わかりやすい名前を付ける

```yaml
# ✅ 良い例
intro:
village_elder:
shop_conversation:

# ❌ 避ける例  
セクション1:
"test section":
```

## メッセージの書き方

### 基本的なメッセージ

```yaml
simple_talk:
  - こんにちは！
  - 今日はいい天気ですね。
  - また会いましょう。
```

### コマンドの挿入

メッセージの間にコマンドを挿入できます：

```yaml
with_commands:
  - こんにちは！
  - /wait 40
  - （少し間を置いて）
  - /sound block.note.bell 1 1
  - いい天気ですね。
```

## ファイル共通設定

ファイルの最初に設定を記述すると、そのファイル全体に適用されます：

```yaml
# ファイル共通設定
sound: block.note.bass 1 2
speed: 20
color: \r

# セクション定義
intro:
  - ゲームを開始します
  - よろしくお願いします

dialogue:
  - こんにちは
  - 調子はどうですか？
```

### 設定可能な項目

- `sound: <音声名> <音量> <ピッチ>` - デフォルト音声
- `speed: <数値>` - 文字表示速度（1秒間の文字数）
- `color: <カラーコード>` - デフォルト文字色

## 実際の例

### hello.yml
```yaml
# 挨拶スクリプト
sound: block.note.bell 1 1.5
speed: 25
color: \2

greeting:
  - /color \6\l
  - こんにちは！
  - /color \r
  - RPGTextの世界へようこそ。
  - このプラグインで素敵な物語を作りましょう。

farewell:
  - それでは、また会いましょう！
  - /singlesound minecraft:entity.player.levelup
  - さようなら！
```

## 次のステップ

基本構造を理解したら、[初回スクリプト作成](first-script.md)に進んで、実際に動くスクリプトを作成してみましょう。

## 関連ドキュメント

- **[コマンドリファレンス - 基本設定](../commands/basic-settings.md)** - sound, speed, colorの詳細
- **[コマンドリファレンス - ナビゲーション](../commands/navigation.md)** - jumpコマンドでセクション間移動
- **[リファレンス - カラーコード](../reference/color-codes.md)** - 使用可能な色の一覧