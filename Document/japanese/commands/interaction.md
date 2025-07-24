# 選択肢・インタラクションコマンド

プレイヤーとの対話や選択肢を表示するコマンドです。

## 🎯 `/? <選択肢>` - 選択肢表示

プレイヤーに選択肢を表示し、選択を待ちます。

### 構文
```yaml
/? <選択肢1> <選択肢2> <選択肢3> ...
```

### 基本的な使用例

```yaml
simple_choice:
  - どちらを選びますか？
  - /? はい いいえ

multiple_choice:
  - 好きな色は？
  - /? 赤 青 緑 黄 紫

many_options:
  - 数字を選んでください
  - /? 1 2 3 4 5 6 7 8 9 10
```

### 選択肢の操作方法

- **スニーク（Shift）**: 選択肢を変更
- **Fキー**: 選択肢を変更（デフォルト設定）
- **左クリック**: 選択決定

## 🔀 `/?<選択肢名>` - 分岐処理

選択された選択肢に基づいて実行するコマンドや文章を指定します。

### 構文
```yaml
/?<選択肢名> <実行内容>
```

### 基本的な分岐

```yaml
basic_branch:
  - 今日の調子は？
  - /? 良い 悪い 普通
  - /?良い それは素晴らしい！
  - /?悪い 大丈夫ですか？
  - /?普通 そうですか

conversation_end:
  - また会いましょう
```

### コマンド実行

```yaml
action_choice:
  - 何をしますか？
  - /? 休憩 買い物 冒険 終了
  - /?休憩 /jump rest_area
  - /?買い物 /jump shop
  - /?冒険 /jump adventure_start
  - /?終了 /jump game_end
```

## 🎮 実用的な例

### 1. 簡単な会話システム

```yaml
greeting:
  - /color \2
  - こんにちは、%player%さん！
  - /color \r
  - 今日はどうされましたか？
  - /? 挨拶しに来ました 用事があります 通りがかりです
  - /?挨拶しに来ました /jump friendly_chat
  - /?用事があります /jump business_talk
  - /?通りがかりです /jump casual_talk

friendly_chat:
  - /color \a
  - そうですか！お元気そうで何よりです。
  - /color \r
  - /jump farewell

business_talk:
  - /color \6
  - 用事ですね。何をお手伝いできますか？
  - /color \r
  - /? クエスト アイテム売買 情報収集
  - /?クエスト /jump quest_menu
  - /?アイテム売買 /jump shop_menu
  - /?情報収集 /jump info_menu

casual_talk:
  - そうですね、良い天気ですね。
  - /jump farewell

farewell:
  - また会いましょう！
```

### 2. ショップシステム

```yaml
shop_entrance:
  - /color \6\l
  - いらっしゃいませ！
  - /color \r
  - 何をお探しですか？
  - 所持金: \\money\\ゴールド
  - /? 武器(100G) 防具(80G) 回復薬(20G) 帰る
  - /?武器(100G) /jump buy_weapon
  - /?防具(80G) /jump buy_armor
  - /?回復薬(20G) /jump buy_potion
  - /?帰る /jump shop_exit

buy_weapon:
  - /if money >= 100 /jump weapon_purchase
  - /color \c
  - お金が足りません！
  - /color \r
  - /jump shop_entrance

weapon_purchase:
  - /score money - 100
  - /command give %player% minecraft:iron_sword
  - /color \a
  - 剣を購入しました！
  - /color \r
  - 残金: \\money\\ゴールド
  - /jump shop_entrance

shop_exit:
  - ありがとうございました！
```

### 3. クエスト受注システム

```yaml
quest_giver:
  - 冒険者よ、頼み事があるのだが...
  - /? 詳しく聞く 後にする
  - /?詳しく聞く /jump quest_explanation
  - /?後にする また今度お話しましょう

quest_explanation:
  - 森に魔物が現れて困っている
  - 退治してもらえないだろうか？
  - 報酬は100ゴールドだ
  - /? 受ける 断る 条件を聞く
  - /?受ける /jump accept_quest
  - /?断る /jump decline_quest
  - /?条件を聞く /jump quest_conditions

accept_quest:
  - /score quest_forest_monster 1
  - /color \a
  - ありがとう！よろしく頼む！
  - /color \r
  - クエスト「森の魔物退治」を受注しました

decline_quest:
  - そうか...仕方ない
  - また気が向いたら声をかけてくれ

quest_conditions:
  - 魔物は森の奥にいる
  - レベル10以上推奨だ
  - /? やはり受ける 断る
  - /?やはり受ける /jump accept_quest
  - /?断る /jump decline_quest
```

### 4. 難易度選択

```yaml
difficulty_select:
  - ゲームの難易度を選択してください
  - /? 簡単 普通 難しい エキスパート
  - /?簡単 /jump set_easy
  - /?普通 /jump set_normal
  - /?難しい /jump set_hard
  - /?エキスパート /jump set_expert

set_easy:
  - /score difficulty 1
  - /color \a
  - 簡単モードに設定しました
  - /color \r
  - 敵の攻撃力が50%減少します
  - /jump game_start

set_normal:
  - /score difficulty 2
  - 普通モードに設定しました
  - バランスの取れた難易度です
  - /jump game_start

set_hard:
  - /score difficulty 3
  - /color \c
  - 難しいモードに設定しました
  - /color \r
  - 敵の攻撃力が150%になります
  - /jump game_start

set_expert:
  - /score difficulty 4
  - /color \4\l
  - エキスパートモードに設定しました！
  - /color \r
  - 敵の攻撃力が200%、制限時間も追加されます
  - /jump game_start
```

## 🎨 選択肢の演出

### カテゴリ分け

```yaml
main_menu:
  - /color \6\l
  - *** メインメニュー ***
  - /color \r
  - /? [ゲーム]開始 [ゲーム]続行 [設定]音量 [設定]操作 [その他]クレジット 終了
  - /?[ゲーム]開始 /jump new_game
  - /?[ゲーム]続行 /jump load_game
  - /?[設定]音量 /jump volume_settings
  - /?[設定]操作 /jump control_settings
  - /?[その他]クレジット /jump credits
  - /?終了 /jump exit_game
```

### 感情表現

```yaml
emotion_choice:
  - どんな気持ちですか？
  - /? 😊嬉しい 😢悲しい 😠怒っている 😴眠い 😕困っている
  - /?😊嬉しい /jump happy_response
  - /?😢悲しい /jump sad_response
  - /?😠怒っている /jump angry_response
  - /?😴眠い /jump sleepy_response
  - /?😕困っている /jump confused_response
```

### 数値選択

```yaml
bet_system:
  - 賭け金を選んでください
  - 所持金: \\money\\ゴールド
  - /? 10G 50G 100G 全部賭ける やめる
  - /?10G /jump bet_10
  - /?50G /if money >= 50 /jump bet_50
  - /?50G お金が足りません
  - /?100G /if money >= 100 /jump bet_100
  - /?100G お金が足りません
  - /?全部賭ける /jump bet_all
  - /?やめる 賢明な判断です
```

## ⚠️ 注意事項とベストプラクティス

### 選択肢の制限

```yaml
# ✅ 適切な選択肢数（2-8個）
good_choice:
  - /? はい いいえ

better_choice:
  - /? 武器 防具 道具 魔法 戻る

# ❌ 多すぎる選択肢（読みにくい）
bad_choice:
  - /? 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15
```

### 選択肢名の注意

```yaml
# ✅ 分かりやすい選択肢名
clear_options:
  - /? 攻撃する 逃げる アイテムを使う
  - /?攻撃する 攻撃します！
  - /?逃げる 逃走しました
  - /?アイテムを使う /jump item_menu

# ❌ 紛らわしい選択肢名
confusing_options:
  - /? はい いいえ はい(確認) 
  - /?はい /jump yes1
  - /?いいえ /jump no
  - /?はい(確認) /jump yes2  # 同じ名前で処理が異なる
```

### 未定義分岐の対策

```yaml
safe_choice:
  - /? A B C
  - /?A Aを選びました
  - /?B Bを選びました
  - /?C Cを選びました
  # 全ての選択肢に対応する処理を必ず記述
```

## 💡 高度なテクニック

### 条件付き選択肢

```yaml
conditional_options:
  - 何をしますか？
  - /? 基本行動 特別行動
  - /?基本行動 /jump basic_actions
  - /?特別行動 /if level >= 10 /jump special_actions
  - /?特別行動 レベルが足りません

special_actions:
  - 特別な行動を選択できます
  - /? 必殺技 魔法 秘密の力
  - /?必殺技 /jump ultimate_attack
  - /?魔法 /jump magic_menu
  - /?秘密の力 /jump secret_power
```

### 動的選択肢

```yaml
dynamic_shop:
  - /if has_sword = 0 /jump sword_available
  - /if has_shield = 0 /jump shield_available
  - /jump basic_shop

sword_available:
  - 何を購入しますか？
  - /? 剣(100G) 盾(80G) 薬(20G) 戻る
  - /?剣(100G) /jump buy_sword
  # 他の処理...

shield_available:
  - 何を購入しますか？
  - /? 盾(80G) 薬(20G) 戻る
  - /?盾(80G) /jump buy_shield
  # 他の処理...

basic_shop:
  - 何を購入しますか？
  - /? 薬(20G) 戻る
  - /?薬(20G) /jump buy_potion
  - /?戻る /jump shop_exit
```

## 🔗 関連ドキュメント

- **[条件分岐](conditions.md)** - より複雑な分岐処理
- **[変数・スコア管理](variables.md)** - 選択による状態変更
- **[ナビゲーション](navigation.md)** - 選択肢からの移動
- **[実用例 - ショップシステム](../examples/shop-system.md)** - 実装例