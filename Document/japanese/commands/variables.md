# 変数・スコア管理コマンド

ゲームの進行状況や数値を管理するコマンドです。

## 📊 `/score` - 変数の設定・計算

変数の設定や計算を行う多機能コマンドです。

### 基本構文
```yaml
/score <変数名> <値>
```

### 計算構文
```yaml
/score <変数名> <演算子> <値または変数名>
```

## 📝 基本的な変数設定

### 数値の設定

```yaml
basic_setup:
  - /score health 100
  - /score money 50
  - /score level 1
  - /score exp 0
  - ステータスを初期化しました
```

### 変数の表示

```yaml
show_status:
  - === ステータス ===
  - レベル: \\level\\
  - 体力: \\health\\
  - 所持金: \\money\\ゴールド
  - 経験値: \\exp\\
```

## 🧮 計算機能

### 加算（+）

```yaml
gain_experience:
  - /score exp + 50
  - 経験値を50獲得しました
  - 現在の経験値: \\exp\\

add_money:
  - /score money + 100
  - 100ゴールドを獲得！
  - 所持金: \\money\\ゴールド
```

### 減算（-）

```yaml
spend_money:
  - /score money - 20
  - 20ゴールドを支払いました
  - 残金: \\money\\ゴールド

take_damage:
  - /score health - 30
  - 30ダメージを受けました！
  - 残り体力: \\health\\
```

### 乗算（*）

```yaml
level_up_bonus:
  - /score exp * 2
  - 経験値が2倍になりました！
  - 経験値: \\exp\\

critical_damage:
  - /score damage * 3
  - クリティカルヒット！ダメージ3倍！
```

### 除算（/）

```yaml
halve_damage:
  - /score incoming_damage / 2
  - シールドで半分のダメージに軽減！

split_reward:
  - /score party_gold / 4
  - 報酬を4人で分けました
```

### 剰余（%）

```yaml
random_effect:
  - /score turn_count % 3
  - /if turn_count = 0 特別な効果が発動！

cycle_system:
  - /score day_cycle % 7
  - /if day_cycle = 0 今日は日曜日です
```

### ランダム値（random）

```yaml
dice_roll:
  - /score dice random 6
  - /add dice
  - サイコロの結果: \\dice\\

treasure_rarity:
  - /score rarity random 100
  - /if rarity < 10 /jump legendary_treasure
  - /if rarity < 30 /jump rare_treasure
  - /jump common_treasure

battle_accuracy:
  - /score hit_chance random 100
  - /if hit_chance < 85 攻撃が命中！
  - /if hit_chance >= 85 攻撃が外れました
```

## 📈 `/add` - 変数の増加

変数を1増加させる簡単なコマンドです。

### 構文
```yaml
/add <変数名>
```

### 使用例

```yaml
counter_system:
  - /add visit_count
  - 訪問回数: \\visit_count\\回目

experience_gain:
  - /add level
  - レベルアップ！現在レベル: \\level\\

turn_based_game:
  - /add turn
  - ターン \\turn\\
  - /if turn > 10 /jump game_end
```

## 🎯 実用的なシステム例

### 1. RPGステータス管理

```yaml
player_setup:
  - /score hp 100
  - /score max_hp 100
  - /score mp 50
  - /score max_mp 50
  - /score level 1
  - /score exp 0
  - /score gold 100
  - キャラクターを作成しました！

level_up_check:
  - /if exp >= 100 /jump level_up_process
  - あと\\needed_exp\\経験値でレベルアップ

level_up_process:
  - /add level
  - /score exp - 100
  - /score max_hp + 20
  - /score max_mp + 10
  - /score hp 100  # 全回復
  - /score mp 50   # 全回復
  - /color \6\l
  - レベルアップ！レベル\\level\\になりました！
  - /color \r
```

### 2. ショップシステム

```yaml
shop_system:
  - いらっしゃいませ！
  - 所持金: \\gold\\ゴールド
  - /? 回復薬(50G) 魔法薬(100G) 剣(500G) 帰る
  - /?回復薬(50G) /jump buy_heal_potion
  - /?魔法薬(100G) /jump buy_mana_potion
  - /?剣(500G) /jump buy_sword
  - /?帰る またのお越しを！

buy_heal_potion:
  - /if gold >= 50 /jump purchase_heal
  - お金が足りません！

purchase_heal:
  - /score gold - 50
  - /add heal_potions
  - 回復薬を購入しました！
  - 所持数: \\heal_potions\\個
  - 残金: \\gold\\ゴールド
```

### 3. クエスト進行管理

```yaml
quest_tracker:
  - /score slime_defeated + 1
  - スライムを倒しました！(\\slime_defeated\\/10)
  - /if slime_defeated >= 10 /jump quest_complete

quest_complete:
  - /score quest_slime_hunt 1  # 完了フラグ
  - /score gold + 200
  - /score exp + 150
  - クエスト完了！報酬を獲得しました
```

### 4. タイマーシステム

```yaml
countdown_start:
  - /score timer 10
  - 10秒カウントダウン開始！
  - /jump countdown_loop

countdown_loop:
  - 残り\\timer\\秒
  - /score timer - 1
  - /wait 20  # 1秒待機
  - /if timer > 0 /jump countdown_loop
  - /jump time_up

time_up:
  - 時間切れ！
```

### 5. 好感度システム

```yaml
npc_interaction:
  - こんにちは、%player%さん
  - /? 贈り物をする 話しかける 去る
  - /?贈り物をする /jump give_gift
  - /?話しかける /jump normal_talk
  - /?去る さようなら

give_gift:
  - /if gold >= 100 /jump gift_success
  - 贈り物するお金がありません

gift_success:
  - /score gold - 100
  - /score npc_affection + 10
  - ありがとうございます！
  - 好感度: \\npc_affection\\
  - /if npc_affection >= 50 /jump special_event
```

## 🎲 ランダム要素の活用

### ガチャシステム

```yaml
gacha_pull:
  - /score rarity random 100
  - /if rarity < 5 /jump ssr_item
  - /if rarity < 20 /jump sr_item
  - /if rarity < 50 /jump r_item
  - /jump n_item

ssr_item:
  - /color \6\l
  - ★★★ SSRアイテム獲得！
  - /color \r
  - /add ssr_count

sr_item:
  - /color \d\l  
  - ★★ SRアイテム獲得！
  - /color \r
  - /add sr_count
```

### 戦闘システム

```yaml
player_attack:
  - /score damage random 20
  - /score damage + 10  # 基礎攻撃力10
  - /score critical random 100
  - /if critical < 20 /jump critical_hit
  - \\damage\\ダメージを与えた！

critical_hit:
  - /score damage * 2
  - /color \c\l
  - クリティカルヒット！\\damage\\ダメージ！
  - /color \r
```

## 🗃️ 変数の種類と用途

### ゲーム進行フラグ

```yaml
# ストーリー進行
/score chapter_1_complete 1
/score met_npc_alice 1
/score castle_visited 1

# 使用例
story_check:
  - /if chapter_1_complete = 1 第1章はクリア済みです
  - /if met_npc_alice = 0 アリスに会ったことがありません
```

### プレイヤーステータス

```yaml
# 基本ステータス
/score level 1
/score hp 100
/score mp 50
/score strength 10
/score defense 8
/score agility 12
```

### アイテム所持数

```yaml
# アイテム管理
/score heal_potions 5
/score magic_keys 3
/score rare_gems 1

item_check:
  - 回復薬: \\heal_potions\\個
  - 魔法の鍵: \\magic_keys\\個
  - 希少な宝石: \\rare_gems\\個
```

### ゲーム内通貨

```yaml
# 通貨システム
/score gold 1000
/score crystals 50
/score tokens 10

currency_display:
  - ゴールド: \\gold\\G
  - クリスタル: \\crystals\\個
  - トークン: \\tokens\\枚
```

## ⚠️ 注意事項とベストプラクティス

### 変数名の命名規則

```yaml
# ✅ 推奨命名
health:
max_health:
player_level:
quest_completed:
npc_affection:

# ❌ 避けるべき命名  
h:              # 短すぎる
playerLevel:    # キャメルケースより_推奨
変数1:          # 日本語
"my var":       # スペース
```

### 初期化の重要性

```yaml
# ✅ ゲーム開始時の初期化
game_start:
  - /score player_level 1
  - /score health 100
  - /score money 0
  - /score quest_count 0

# ❌ 初期化忘れ（予期しない値になる可能性）
```

### オーバーフローの注意

```yaml
# 大きな数値の扱い
safety_check:
  - /if money > 999999 /score money 999999
  - /if level > 100 /score level 100
```

## 💡 高度なテクニック

### 計算式の組み合わせ

```yaml
complex_calculation:
  - /score base_damage 50
  - /score strength_bonus 10
  - /score weapon_bonus 20
  - /score total_damage + strength_bonus
  - /score total_damage + weapon_bonus
  - 総ダメージ: \\total_damage\\
```

### 変数間の操作

```yaml
transfer_system:
  - /score temp_value \\player1_money\\
  - /score player1_money - temp_value
  - /score player2_money + temp_value
  - プレイヤー1からプレイヤー2に送金完了
```

### パーセンテージ計算

```yaml
percentage_system:
  - /score current_hp 75
  - /score max_hp 100
  - /score hp_percent * 100
  - /score hp_percent / max_hp
  - 体力: \\hp_percent\\%
```

## 🔗 関連ドキュメント

- **[条件分岐](conditions.md)** - 変数を使った条件判定
- **[選択肢・インタラクション](interaction.md)** - 選択による変数変更
- **[実用例 - RPGシステム](../examples/rpg-system.md)** - 複雑な変数管理例
- **[リファレンス - 特殊変数](../reference/special-variables.md)** - プレイヤー情報変数