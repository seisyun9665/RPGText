# 条件分岐コマンド

変数や値を比較して条件に応じた処理を実行するコマンドです。

## 🔀 `/if` - 条件分岐

指定した条件が真の場合にのみ、後続のコマンドまたはメッセージを実行します。

### 基本構文
```yaml
/if <変数名|値> <比較演算子> <変数名|値> <実行内容>
```

### 比較演算子

| 演算子 | 意味 | 例 |
|-------|------|-----|
| `=` | 等しい | `/if level = 10` |
| `>` | より大きい | `/if money > 100` |
| `<` | より小さい | `/if hp < 50` |

## 📝 基本的な使用例

### 数値比較

```yaml
level_check:
  - /if level = 1 初心者ですね
  - /if level > 10 中級者レベルです
  - /if level > 50 上級者ですね！
  - /if level > 99 /color \6\l
  - /if level > 99 最高レベル達成！
  - /if level > 99 /color \r

money_check:
  - /if money = 0 お金がありません
  - /if money < 100 お金が少ないようですね
  - /if money > 1000 裕福ですね！
  - /if money > 10000 大富豪です！
```

### 変数同士の比較

```yaml
stats_comparison:
  - /if strength > defense 攻撃特化タイプですね
  - /if defense > strength 防御特化タイプですね
  - /if strength = defense バランス型ですね

hp_status:
  - /if hp = max_hp 体力は満タンです
  - /if hp < max_hp 体力が減っています
  - /score hp_percent hp
  - /score hp_percent * 100
  - /score hp_percent / max_hp
  - /if hp_percent < 25 /color \c
  - /if hp_percent < 25 危険！体力が25%以下です
  - /if hp_percent < 25 /color \r
```

## 🔗 複数条件の指定

### `&`演算子による複数条件

```yaml
/if <条件1> & <条件2> & <条件3> <実行内容>
```

### AND条件の例

```yaml
special_event:
  - /if level > 20 & money > 500 & quest_complete = 1 特別イベント発生！
  - /if level > 20 & money > 500 & quest_complete = 0 クエストを先にクリアしてください

shop_purchase:
  - /if money >= 100 & inventory_space > 0 購入可能です
  - /if money < 100 & inventory_space > 0 お金が足りません
  - /if money >= 100 & inventory_space = 0 インベントリが満杯です
  - /if money < 100 & inventory_space = 0 お金もスペースも足りません

dungeon_entry:
  - /if level >= 15 & key_count > 0 & party_size >= 2 ダンジョンに入場できます
  - /if level < 15 レベル15以上必要です
  - /if key_count = 0 鍵が必要です
  - /if party_size < 2 パーティーは2人以上必要です
```

## 🎯 実用的なシステム例

### 1. RPGステータスチェック

```yaml
status_check:
  - === ステータスチェック ===
  - レベル: \\level\\
  - /if level < 10 /color \c
  - /if level < 10 まだ初心者レベルです
  - /if level >= 10 & level < 30 /color \e  
  - /if level >= 10 & level < 30 中級者レベルです
  - /if level >= 30 & level < 60 /color \a
  - /if level >= 30 & level < 60 上級者レベルです
  - /if level >= 60 /color \6\l
  - /if level >= 60 エキスパートレベルです！
  - /color \r

health_warning:
  - 現在HP: \\hp\\/\\max_hp\\
  - /if hp < 20 /color \c\l
  - /if hp < 20 ⚠️ 体力が危険な状態です！
  - /if hp < 20 /color \r
  - /if hp >= 20 & hp < 50 /color \e
  - /if hp >= 20 & hp < 50 注意：体力が半分以下です
  - /if hp >= 20 & hp < 50 /color \r
  - /if hp >= 50 体力は十分です
```

### 2. ショップ購入システム

```yaml
weapon_shop:
  - 武器屋へようこそ！
  - 所持金: \\money\\ゴールド
  - /? 短剣(100G) 剣(300G) 聖剣(1000G) 帰る
  - /?短剣(100G) /jump buy_dagger
  - /?剣(300G) /jump buy_sword
  - /?聖剣(1000G) /jump buy_holy_sword
  - /?帰る またお越しください

buy_dagger:
  - /if money >= 100 & weapon_equipped = 0 /jump purchase_dagger
  - /if money < 100 お金が足りません
  - /if weapon_equipped > 0 既に武器を装備しています

purchase_dagger:
  - /score money - 100
  - /score weapon_equipped 1
  - /score attack_power + 10
  - 短剣を購入しました！
  - 攻撃力が10上昇しました

buy_holy_sword:
  - /if money >= 1000 & level >= 50 & holy_quest = 1 /jump purchase_holy_sword
  - /if money < 1000 お金が足りません
  - /if level < 50 レベル50以上必要です
  - /if holy_quest = 0 聖なるクエストをクリアする必要があります

purchase_holy_sword:
  - /score money - 1000
  - /score weapon_equipped 3
  - /score attack_power + 100
  - /color \6\l
  - 聖剣を購入しました！
  - /color \r
  - 攻撃力が大幅に上昇しました！
```

### 3. クエスト進行管理

```yaml
quest_npc:
  - こんにちは、冒険者よ
  - /if goblin_quest = 0 /jump new_quest
  - /if goblin_quest = 1 & goblin_defeated < 5 /jump quest_progress
  - /if goblin_quest = 1 & goblin_defeated >= 5 /jump quest_complete
  - /if goblin_quest = 2 ありがとう、また頼むかもしれない

new_quest:
  - ゴブリンが村を襲っている
  - 5匹倒してもらえないか？
  - /? 受ける 断る
  - /?受ける /jump accept_quest
  - /?断る また今度お願いします

accept_quest:
  - /score goblin_quest 1
  - /score goblin_defeated 0
  - ありがとう！よろしく頼む
  - 進行状況: \\goblin_defeated\\/5

quest_progress:
  - 進行状況: \\goblin_defeated\\/5
  - /if goblin_defeated = 0 まだ1匹も倒していませんね
  - /if goblin_defeated >= 1 & goblin_defeated < 3 順調ですね
  - /if goblin_defeated >= 3 & goblin_defeated < 5 もう少しです！

quest_complete:
  - /score goblin_quest 2
  - /score money + 500
  - /score exp + 200
  - ありがとう！これは報酬だ
  - 500ゴールドと200経験値を獲得しました
```

### 4. 天候・時間システム

```yaml
weather_check:
  - /score weather random 4
  - /if weather = 0 今日は晴れです
  - /if weather = 1 今日は曇りです
  - /if weather = 2 今日は雨です
  - /if weather = 3 今日は雪です
  - /if weather = 2 & umbrella = 0 傘がないので濡れてしまいます
  - /if weather = 2 & umbrella = 1 傘があるので大丈夫です

time_of_day:
  - /score time % 24
  - /if time >= 6 & time < 12 おはようございます
  - /if time >= 12 & time < 18 こんにちは
  - /if time >= 18 & time < 22 こんばんは
  - /if time >= 22 | time < 6 夜遅いですね

shop_hours:
  - /if time >= 9 & time < 17 /jump shop_open
  - /if time < 9 | time >= 17 /jump shop_closed

shop_open:
  - いらっしゃいませ！
  - 営業時間: 9:00-17:00

shop_closed:
  - 申し訳ございません
  - 営業時間外です（9:00-17:00）
```

### 5. 戦闘システム

```yaml
battle_turn:
  - /? 攻撃 防御 アイテム 逃げる
  - /?攻撃 /jump player_attack
  - /?防御 /jump player_defend
  - /?アイテム /jump use_item
  - /?逃げる /jump try_escape

player_attack:
  - /score hit_chance random 100
  - /if agility > enemy_agility /score hit_chance + 20
  - /if hit_chance < 85 /jump attack_hit
  - /jump attack_miss

attack_hit:
  - /score damage random 20
  - /score damage + attack_power
  - /if critical_chance > 90 /jump critical_hit
  - \\damage\\ダメージを与えた！
  - /score enemy_hp - damage
  - /if enemy_hp <= 0 /jump victory

critical_hit:
  - /score damage * 2
  - /color \c\l
  - クリティカルヒット！\\damage\\ダメージ！
  - /color \r
  - /score enemy_hp - damage
  - /if enemy_hp <= 0 /jump victory

victory:
  - /color \a\l
  - 勝利しました！
  - /color \r
  - /score exp + 100
  - /score money + 200
  - 経験値100、200ゴールドを獲得
```

## 🚫 制限事項とワークアラウンド

### 入れ子条件の制限

```yaml
# ❌ 動作しない（入れ子条件）
- /if level > 10 /if money > 100 特別な処理

# ✅ 解決方法1: &演算子を使用
- /if level > 10 & money > 100 特別な処理

# ✅ 解決方法2: jumpを使用
- /if level > 10 /jump money_check

money_check:
  - /if money > 100 特別な処理
  - /if money <= 100 お金が足りません
```

### OR条件の実装

```yaml
# OR条件は直接サポートされていないので、個別にチェック
or_condition:
  - /if weapon = 1 /jump weapon_equipped
  - /if armor = 1 /jump armor_equipped
  - /if accessory = 1 /jump accessory_equipped
  - 何も装備していません

weapon_equipped:
  - 武器を装備しています
  - /jump equipment_end

armor_equipped:
  - 防具を装備しています  
  - /jump equipment_end

accessory_equipped:
  - アクセサリを装備しています

equipment_end:
  - 装備チェック完了
```

## ⚠️ 注意事項とベストプラクティス

### 条件の組み立て

```yaml
# ✅ 明確な条件
- /if level >= 10 レベル10以上です
- /if money > 0 お金を持っています
- /if hp = max_hp 体力満タンです

# ❌ 紛らわしい条件
- /if level = >10  # 構文エラー
- /if money >> 100  # 間違った演算子
```

### パフォーマンスの考慮

```yaml
# ✅ 効率的（頻度の高い条件を先に）
efficient_check:
  - /if common_condition = 1 /jump common_path
  - /if rare_condition = 1 /jump rare_path

# ❌ 非効率（複雑な条件を最初に）
inefficient_check:
  - /if complex_condition & another_condition & third_condition /jump complex_path
  - /if simple_condition = 1 /jump simple_path
```

### エラーハンドリング

```yaml
safe_division:
  - /if divisor > 0 /score result value
  - /if divisor > 0 /score result / divisor
  - /if divisor = 0 ゼロで割ることはできません

range_check:
  - /if input >= 1 & input <= 100 /jump valid_input
  - 1から100の間で入力してください

valid_input:
  - 有効な値です: \\input\\
```

## 💡 高度なテクニック

### 状態機械の実装

```yaml
state_machine:
  - /if game_state = 0 /jump state_menu
  - /if game_state = 1 /jump state_playing
  - /if game_state = 2 /jump state_paused
  - /if game_state = 3 /jump state_game_over

state_menu:
  - メニュー画面
  - /? 開始 設定 終了
  - /?開始 /score game_state 1
  - /?設定 /jump settings
  - /?終了 /jump exit

state_playing:
  - ゲーム中
  - /if player_hp <= 0 /score game_state 3
  - /if pause_requested = 1 /score game_state 2
```

### フラグ管理システム

```yaml
flag_manager:
  - /if tutorial_complete = 0 /jump tutorial
  - /if first_boss_defeated = 0 /jump first_area
  - /if all_crystals_collected = 1 /jump final_area
  - /jump main_game

achievement_check:
  - /if level >= 50 & boss_defeated >= 10 & secret_found = 1 /jump unlock_achievement
  - /if level >= 100 /jump master_achievement
```

## 🔗 関連ドキュメント

- **[変数・スコア管理](variables.md)** - 条件で使用する変数の管理
- **[ナビゲーション](navigation.md)** - 条件による移動制御
- **[選択肢・インタラクション](interaction.md)** - 選択と条件の組み合わせ
- **[実用例 - RPGシステム](../examples/rpg-system.md)** - 複雑な条件分岐の例