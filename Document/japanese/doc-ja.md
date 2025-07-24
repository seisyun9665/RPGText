# RPGText 記法ドキュメント

RPGText は、Minecraft サーバー用プラグインで、RPG ゲームのような順次テキスト表示システムを提供します。YML ファイルに特殊な記法でストーリーを記述し、プレイヤーとの対話やゲーム内でのイベント制御を行えます。

## 基本構造

### ファイル形式

- YML ファイル形式
- セクション単位での管理
- リスト形式での記述

```yaml
section_name:
  - メッセージ1
  - /command example
  - メッセージ2
```

### ファイル共通設定

ファイルの最初に設定することで、そのファイル内の全セクションに適用されます。

```yaml
# 音声設定
sound: block.note.bass 1 2
# 表示速度
speed: 20
# 文字色
color: \r
```

## コマンド一覧

### 1. 基本設定

#### `/sound <音声名> <音量> <ピッチ>`

メッセージ表示時の音声を設定

```yaml
- /sound block.note.flute 1 1.75
- /sound block.note.bass 1 2
```

#### `/speed <速度>`

1 秒間に表示する文字数を設定（デフォルト: 30）

```yaml
- /speed 3 # 3文字/秒
- /speed 50 # 50文字/秒
```

#### `/color <カラーコード>`

メッセージの基本色を設定

```yaml
- /color \6\l # 金色＋太字
- /color \r # デフォルト色に戻す
```

### 2. ナビゲーション

#### `/jump <ファイル名>/<セクション名>`

他のセクションやファイルに移動

```yaml
- /jump Tutorial.yml/users2
- /jump users2 # 同一ファイル内の場合
- /jump chapter1/title.yml/talk1 # 階層構造
```

### 3. 選択肢とインタラクション

#### `/? <選択肢1> <選択肢2> ...`

プレイヤーに選択肢を表示

```yaml
- /? option1 option2 option3
- /? yes no
- /? 1 2 3 4 5
```

#### `/?<選択肢名> <実行内容>`

選択肢に基づく分岐処理

```yaml
- /? yes no
- /?yes よい選択です！
- /?no 残念ですね...
```

### 4. 変数・スコア管理

#### `/score <変数名> <値>`

変数の設定

```yaml
- /score test 1
- /score money 100
```

#### `/score <変数名> <演算子> <値または変数名>`

変数の計算

```yaml
- /score money + 50 # money += 50
- /score hp - damage # hp -= damage
- /score level * 2 # level *= 2
- /score remainder % 4 # remainder %= 4
- /score dice random 6 # 0-5のランダム値
```

#### `/add <変数名>`

変数を 1 増加

```yaml
- /add counter
- /add visit_count
```

#### `\\変数名\\`

変数値の表示

```yaml
- あなたの所持金は\\money\\ゴールドです
- レベル: \\level\\
```

### 5. 条件分岐

#### `/if <変数名|値> <比較演算子> <変数名|値> <実行内容>`

条件に基づく処理

```yaml
- /if level > 10 レベル10以上ですね！
- /if money = 0 お金がありません
- /if hp < max_hp 体力が減っています
```

比較演算子: `=`, `>`, `<`

#### 複数条件の指定

`&`で複数条件を連結

```yaml
- /if level > 5 & money >= 100 特別なアイテムを購入できます
```

### 6. アイテム管理

#### `/has <アイテム種類> <数量> <名前> <実行内容>`

アイテム所持チェック

```yaml
- /has DIAMOND 1 none ダイヤモンドを持っています
- /has EMERALD 5 none エメラルドを5個以上持っています
```

#### `/removeItem <アイテム種類> <数量> <名前>`

アイテムの削除

```yaml
- /removeItem EMERALD 1 none
- /removeItem DIAMOND 2 none
```

### 7. ゲーム制御

#### `/command <Minecraftコマンド>`

Minecraft のコマンドを実行

```yaml
- /command give %player% minecraft:diamond
- /command title @a title {"text":"Hello"}
- /command tp %player% 100 64 200
```

#### `/freeze <true|false>`

プレイヤーの移動制限

```yaml
- /freeze true # 移動禁止
- /freeze false # 移動許可
```

#### `/wait <ティック数>`

指定時間の待機（20 ティック = 1 秒）

```yaml
- /wait 20 # 1秒待機
- /wait 60 # 3秒待機
```

#### `/auto <true|false>`

自動進行の設定

```yaml
- /auto true # 自動で次に進む
- /auto false # クリック待ち
```

#### `/skip <true|false>`

スキップ可能性の設定

```yaml
- /skip true # スキップ可能
- /skip false # スキップ不可
```

### 8. 音声制御

#### `/singlesound <音声名> [音量] [ピッチ]`

単発音声の再生

```yaml
- /singlesound minecraft:entity.player.levelup
- /singlesound minecraft:block.note.bell 1 1.5
```

### 9. 特殊変数

#### プレイヤー情報

- `%player%` - プレイヤー名
- `%level%` - プレイヤーレベル
- `%hp%` - 体力
- `%food%` - 満腹度
- `%gamemode%` - ゲームモード

#### カラーコード

- `\a` - 濃い緑
- `\b` - 水色
- `\c` - 濃い赤
- `\d` - 紫
- `\e` - 黄色
- `\1` - 濃い青
- `\2` - 緑
- `\3` - 青緑
- `\4` - 濃い赤
- `\l` - 太字
- `\r` - リセット

## 実用例

### 基本的な会話

```yaml
simple_talk:
  - /sound block.note.bell 1 1.5
  - /color \c
  - こんにちは！
  - 調子はどうですか？
  - /? 良い 悪い
  - /?良い それは良かった！
  - /?悪い 大丈夫ですか？無理しないでくださいね。
  - 良い一日を！
```

### スロットゲーム

```yaml
slot_game:
  - スロットゲーム開始！
  - /score slot1 0
  - /score slot2 0
  - /score slot3 0
  - /jump slot_countdown

slot_countdown:
  - /command title %player% title {"text":"3"}
  - /wait 20
  - /command title %player% title {"text":"2"}
  - /wait 20
  - /command title %player% title {"text":"1"}
  - /wait 20
  - /jump slot_spin

slot_spin:
  - /score slot1 random 3
  - /score slot2 random 3
  - /score slot3 random 3
  - /add slot1
  - /add slot2
  - /add slot3
  - 結果: \\slot1\\ \\slot2\\ \\slot3\\
  - /if slot1 = slot2 & slot2 = slot3 /jump jackpot
  - 残念！また挑戦してください。

jackpot:
  - /singlesound minecraft:entity.player.levelup
  - /command give %player% diamond
  - ジャックポット！ダイヤモンドを獲得！
```

### ショップシステム

```yaml
shop:
  - いらっしゃいませ！
  - 所持金: \\money\\ゴールド
  - /? 剣を買う(50G) ポーションを買う(20G) 帰る
  - /?剣を買う(50G) /jump buy_sword
  - /?ポーションを買う(20G) /jump buy_potion
  - /?帰る またお越しください！

buy_sword:
  - /if money >= 50 /jump sword_purchase
  - お金が足りません！

sword_purchase:
  - /score money - 50
  - /command give %player% minecraft:iron_sword
  - 剣を購入しました！
  - 残金: \\money\\ゴールド
```

## 注意事項

### 制限事項

- 複数の条件コマンドの入れ子は不可

  ```yaml
  # ❌ 動作しない例
  - /if test = 1 /has DIAMOND 1 none メッセージ
  - /if test < 3 /if test2 = 1 メッセージ
  ```

- 複数条件は`&`または`/jump`を使用
  ```yaml
  # ✅ 正しい例
  - /if test = 1 & money > 100 条件達成！
  # または
  - /if test = 1 /jump check_diamond
  ```

### ベストプラクティス

1. 変数名は意味のある名前を使用
2. セクション名も分かりやすく命名
3. 複雑な条件は複数のセクションに分割
4. 適切な音声とタイミングで臨場感を演出
5. プレイヤーの入力を待つ場面では`/auto false`を使用

## ゲーム内での使用

### プレイヤーへのメッセージ送信

```
/rpgtext config <プレイヤー名> <ファイルパス>/<セクション名>
```

### NPC との会話設定

```
/rpgtext character <エンティティ名> <ファイルパス>/<セクション名>
```

この記法を使うことで、対話システム、クエスト、ショップ、ミニゲームなど様々な RPG 要素を簡単に実装できます。
