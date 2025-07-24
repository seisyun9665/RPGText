# スロットゲーム

tutorial.ymlのスロット実装を詳しく解説し、さらに高機能なスロットゲームの作り方を説明します。

## 🎰 基本的なスロットゲーム

### tutorial.ymlの実装解説

```yaml
slot:
  - /color \6\l
  - === スロットゲーム ===
  - /color \r
  - 参加費: 10ゴールド
  - 現在の所持金: \\money\\ゴールド
  - /? プレイする 帰る
  - /?プレイする /jump slot_play
  - /?帰る また来てくださいね

slot_play:
  - /if money >= 10 /jump slot_start
  - /color \c
  - お金が足りません！
  - /color \r
  - /jump slot

slot_start:
  - /score money - 10
  - スロット開始！
  - /wait 20
  - /sound minecraft:block.note.bell 1 1
  - リールが回転中...
  - /wait 40
  - /sound minecraft:block.note.bell 1 1.2
  - /wait 40
  - /sound minecraft:block.note.bell 1 1.5
  - /wait 40
  - 結果発表！
  - /jump slot_result

slot_result:
  - /score slot_random 0
  - /add slot_random
  - /add slot_random
  - /add slot_random
  - /if slot_random = 3 /jump slot_jackpot
  - /if slot_random >= 6 /jump slot_win
  - /color \8
  - 残念...何も揃いませんでした
  - /color \r
  - /jump slot_continue

slot_jackpot:
  - /color \6\l
  - 🎉 ジャックポット！ 🎉
  - /color \r
  - /score money + 100
  - 100ゴールド獲得！
  - /singlesound minecraft:entity.player.levelup 1 1
  - /jump slot_continue

slot_win:
  - /color \a
  - おめでとう！当たりです！
  - /color \r
  - /score money + 30
  - 30ゴールド獲得！
  - /singlesound minecraft:entity.experience_orb.pickup 1 1.5
  - /jump slot_continue

slot_continue:
  - 現在の所持金: \\money\\ゴールド
  - /? もう一度 帰る
  - /?もう一度 /jump slot_play
  - /?帰る ありがとうございました！
```

### 実装のポイント解説

#### 1. 参加条件のチェック
```yaml
slot_play:
  - /if money >= 10 /jump slot_start
  - /color \c
  - お金が足りません！
  - /color \r
  - /jump slot
```
**解説:**
- プレイヤーの所持金をチェック
- 不足時は適切なメッセージで案内
- エラー時も元のメニューに戻る

#### 2. ランダム要素の実装
```yaml
slot_result:
  - /score slot_random 0
  - /add slot_random
  - /add slot_random
  - /add slot_random
```
**解説:**
- `/add`コマンドでランダムな値を生成
- 複数回実行することで確率を調整
- 結果によって異なる分岐処理

#### 3. 演出効果
```yaml
slot_start:
  - スロット開始！
  - /wait 20
  - /sound minecraft:block.note.bell 1 1
  - リールが回転中...
  - /wait 40
  - /sound minecraft:block.note.bell 1 1.2
```
**解説:**
- `/wait`で適切な間合いを作成
- 音声のピッチを変化させて演出
- プレイヤーの期待感を高める

## 🎯 高機能スロットゲーム

### 多彩な当たりパターン

```yaml
advanced_slot:
  - /color \6\l
  - === プレミアムスロット ===
  - /color \r
  - 参加費: 50ゴールド
  - 現在の所持金: \\money\\ゴールド
  - /? プレイする ルール確認 帰る
  - /?プレイする /jump advanced_slot_play
  - /?ルール確認 /jump slot_rules
  - /?帰る また来てください

slot_rules:
  - /color \b\l
  - === ルール ===
  - /color \r
  - • 参加費: 50ゴールド
  - • 7-7-7: 1000ゴールド（ジャックポット）
  - • 同じ数字3つ: 200ゴールド
  - • 同じ数字2つ: 100ゴールド
  - • 777以外の連番: 50ゴールド
  - /? 戻る
  - /?戻る /jump advanced_slot

advanced_slot_play:
  - /if money >= 50 /jump advanced_slot_start
  - お金が足りません（必要: 50G）
  - /jump advanced_slot

advanced_slot_start:
  - /score money - 50
  - /color \e
  - スロット開始！
  - /color \r
  - /sound minecraft:block.note.harp 1 0.5
  - /wait 30
  - 1番目のリール...
  - /sound minecraft:block.note.harp 1 1
  - /wait 30
  - 2番目のリール...
  - /sound minecraft:block.note.harp 1 1.5
  - /wait 30
  - 3番目のリール...
  - /sound minecraft:block.note.harp 1 2
  - /wait 30
  - /jump generate_slot_numbers

generate_slot_numbers:
  - /score slot1 1
  - /score slot2 1
  - /score slot3 1
  - /add slot1
  - /add slot1
  - /add slot1
  - /add slot2
  - /add slot2
  - /add slot2
  - /add slot3
  - /add slot3
  - /add slot3
  - /if slot1 > 7 /score slot1 - 7
  - /if slot2 > 7 /score slot2 - 7
  - /if slot3 > 7 /score slot3 - 7
  - /jump slot_result_display

slot_result_display:
  - /color \6\l
  - === 結果 ===
  - /color \r
  - [ \\slot1\\ ] [ \\slot2\\ ] [ \\slot3\\ ]
  - /wait 40
  - /jump slot_check_result

slot_check_result:
  - /score prize 0
  - /if slot1 = 7 & slot2 = 7 & slot3 = 7 /jump jackpot_777
  - /if slot1 = slot2 & slot2 = slot3 /jump three_same
  - /if slot1 = slot2 /jump two_same
  - /if slot2 = slot3 /jump two_same
  - /if slot1 = slot3 /jump two_same
  - /jump check_sequence

check_sequence:
  - /score temp_check 0
  - /if slot1 + 1 = slot2 & slot2 + 1 = slot3 /score temp_check 1
  - /if slot1 - 1 = slot2 & slot2 - 1 = slot3 /score temp_check 1
  - /if temp_check = 1 /jump sequence_win
  - /jump no_win

jackpot_777:
  - /color \6\l
  - 🎉🎉🎉 JACKPOT 🎉🎉🎉
  - /color \r
  - /score money + 1000
  - 1000ゴールド獲得！
  - /singlesound minecraft:entity.player.levelup 1 2
  - /jump slot_result_end

three_same:
  - /color \a\l
  - 🎊 3つ揃い！ 🎊
  - /color \r
  - /score money + 200
  - 200ゴールド獲得！
  - /singlesound minecraft:entity.player.levelup 1 1.5
  - /jump slot_result_end

two_same:
  - /color \a
  - ✨ 2つ揃い！ ✨
  - /color \r
  - /score money + 100
  - 100ゴールド獲得！
  - /singlesound minecraft:entity.experience_orb.pickup 1 1.5
  - /jump slot_result_end

sequence_win:
  - /color \d
  - 🌟 連番！ 🌟
  - /color \r
  - /score money + 50
  - 50ゴールド獲得！
  - /singlesound minecraft:entity.experience_orb.pickup 1 1
  - /jump slot_result_end

no_win:
  - /color \8
  - 残念...何も揃いませんでした
  - /color \r
  - /jump slot_result_end

slot_result_end:
  - 現在の所持金: \\money\\ゴールド
  - /? もう一度 帰る
  - /?もう一度 /jump advanced_slot_play
  - /?帰る ありがとうございました！
```

## 🎲 特殊スロットゲーム

### ボーナスゲーム付きスロット

```yaml
bonus_slot:
  - /color \d\l
  - === ボーナススロット ===
  - /color \r
  - 参加費: 100ゴールド
  - ボーナスチャンス付き！
  - 現在の所持金: \\money\\ゴールド
  - /? プレイする 帰る
  - /?プレイする /jump bonus_slot_play
  - /?帰る また来てください

bonus_slot_play:
  - /if money >= 100 /jump bonus_slot_start
  - お金が足りません（必要: 100G）
  - /jump bonus_slot

bonus_slot_start:
  - /score money - 100
  - /score bonus_chance 0
  - ボーナススロット開始！
  - /jump generate_bonus_numbers

generate_bonus_numbers:
  - /score slot1 1
  - /score slot2 1
  - /score slot3 1
  - /add slot1
  - /add slot1
  - /add slot2
  - /add slot2
  - /add slot3
  - /add slot3
  - /if slot1 > 6 /score slot1 - 6
  - /if slot2 > 6 /score slot2 - 6
  - /if slot3 > 6 /score slot3 - 6
  - /jump bonus_result_display

bonus_result_display:
  - /sound minecraft:block.note.chime 1 1
  - /wait 20
  - 🎰 [ \\slot1\\ ] 
  - /sound minecraft:block.note.chime 1 1.2
  - /wait 20
  - 🎰 [ \\slot1\\ ] [ \\slot2\\ ] 
  - /sound minecraft:block.note.chime 1 1.5
  - /wait 20
  - 🎰 [ \\slot1\\ ] [ \\slot2\\ ] [ \\slot3\\ ]
  - /wait 40
  - /jump bonus_check_result

bonus_check_result:
  - /if slot1 = slot2 & slot2 = slot3 /jump bonus_three_same
  - /if slot1 = 1 & slot2 = 2 & slot3 = 3 /jump bonus_special
  - /if slot1 = slot2 /jump bonus_two_same
  - /if slot2 = slot3 /jump bonus_two_same
  - /if slot1 = slot3 /jump bonus_two_same
  - /jump bonus_check_chance

bonus_check_chance:
  - /add bonus_chance
  - /if bonus_chance >= 3 /jump bonus_game
  - /color \8
  - 残念...でもボーナスチャンス！
  - /color \r
  - チャンス: \\bonus_chance\\/3
  - /? もう一度無料 帰る
  - /?もう一度無料 /jump bonus_slot_start
  - /?帰る また来てください

bonus_three_same:
  - /color \6\l
  - 🎉 3つ揃い！ 🎉
  - /color \r
  - /score money + 500
  - 500ゴールド獲得！
  - /singlesound minecraft:entity.player.levelup 1 2
  - /jump bonus_result_end

bonus_special:
  - /color \5\l
  - ✨ 特別ボーナス！ ✨
  - /color \r
  - /jump bonus_game

bonus_two_same:
  - /color \a
  - 2つ揃い！
  - /color \r
  - /score money + 150
  - 150ゴールド獲得！
  - /singlesound minecraft:entity.experience_orb.pickup 1 1.5
  - /jump bonus_result_end

bonus_game:
  - /color \5\l
  - 🌟 ボーナスゲーム突入！ 🌟
  - /color \r
  - 3つの宝箱から1つ選んでください
  - /? 左の宝箱 真ん中の宝箱 右の宝箱
  - /?左の宝箱 /jump bonus_box_1
  - /?真ん中の宝箱 /jump bonus_box_2
  - /?右の宝箱 /jump bonus_box_3

bonus_box_1:
  - /score bonus_reward 1
  - /add bonus_reward
  - /add bonus_reward
  - /jump bonus_reward_calc

bonus_box_2:
  - /score bonus_reward 2
  - /add bonus_reward
  - /add bonus_reward
  - /jump bonus_reward_calc

bonus_box_3:
  - /score bonus_reward 3
  - /add bonus_reward
  - /add bonus_reward
  - /jump bonus_reward_calc

bonus_reward_calc:
  - 宝箱を開けています...
  - /wait 60
  - /sound minecraft:block.chest.open 1 1
  - /if bonus_reward <= 3 /jump bonus_small
  - /if bonus_reward <= 6 /jump bonus_medium
  - /jump bonus_large

bonus_small:
  - 銅貨が出ました！
  - /score money + 200
  - 200ゴールド獲得！
  - /jump bonus_result_end

bonus_medium:
  - 銀貨が出ました！
  - /score money + 500
  - 500ゴールド獲得！
  - /jump bonus_result_end

bonus_large:
  - /color \6\l
  - 金貨が出ました！
  - /color \r
  - /score money + 1000
  - 1000ゴールド獲得！
  - /singlesound minecraft:entity.player.levelup 1 2
  - /jump bonus_result_end

bonus_result_end:
  - 現在の所持金: \\money\\ゴールド
  - /? もう一度 帰る
  - /?もう一度 /jump bonus_slot_play
  - /?帰る ありがとうございました！
```

## 🎨 カスタマイズ要素

### 確率の調整

```yaml
# 基本的な確率調整
probability_settings:
  # addコマンドの回数で確率を調整
  # 1回: 1-2の範囲（50%ずつ）
  # 2回: 1-3の範囲（33%、33%、33%）
  # 3回: 1-4の範囲（25%ずつ）

# 例：7が出やすいスロット
lucky_seven_slot:
  - /score slot_num 6  # 基準値を6に設定
  - /add slot_num      # 7または8になる
  - /if slot_num > 7 /score slot_num 1  # 8は1に変換
  # 結果：7が50%、1が50%の確率
```

### 視覚効果の向上

```yaml
visual_effects:
  - /color \e
  - ⚡ スロット回転中 ⚡
  - /color \r
  - /sound minecraft:block.note.bell 1 0.5
  - [ ? ] [ ? ] [ ? ]
  - /wait 20
  - /sound minecraft:block.note.bell 1 1
  - [ \\slot1\\ ] [ ? ] [ ? ]
  - /wait 20
  - /sound minecraft:block.note.bell 1 1.5
  - [ \\slot1\\ ] [ \\slot2\\ ] [ ? ]
  - /wait 20
  - /sound minecraft:block.note.bell 1 2
  - [ \\slot1\\ ] [ \\slot2\\ ] [ \\slot3\\ ]
```

### 統計システム

```yaml
statistics_system:
  - /add total_plays
  - /if prize > 0 /add total_wins
  - /score win_rate 0
  - /if total_plays > 0 /score win_rate \\total_wins\\ * 100 / \\total_plays\\
  - 
  - === 統計情報 ===
  - 総プレイ回数: \\total_plays\\
  - 勝利回数: \\total_wins\\
  - 勝率: \\win_rate\\%
```

## 💡 応用アイデア

### 1. テーマ別スロット
```yaml
# ファンタジースロット
fantasy_slot:
  - モンスターの絵柄（ドラゴン、エルフ、オークなど）
  - 属性による特殊効果
  - レア度による配当変更

# スポーツスロット  
sports_slot:
  - スポーツ用品の絵柄
  - 季節イベント連動
  - チーム戦での特別ボーナス
```

### 2. 連動システム
```yaml
# 他のゲームとの連動
linked_systems:
  - スロット勝利でクエストアイテム入手
  - 特定の絵柄でミニゲーム解放
  - 勝利回数による称号システム
```

### 3. ソーシャル要素
```yaml
# プレイヤー間競争
social_features:
  - 日間勝利金額ランキング
  - ジャックポット共有プール
  - 友達紹介ボーナス
```

## 🔧 デバッグ・テスト

### デバッグ用セクション
```yaml
slot_debug:
  - === スロットデバッグ ===
  - /score money 10000  # テスト用資金
  - /score slot1 7
  - /score slot2 7  
  - /score slot3 7
  - 強制ジャックポット: [ \\slot1\\ ] [ \\slot2\\ ] [ \\slot3\\ ]
  - /jump jackpot_777

slot_test_probability:
  - 確率テスト（100回実行）
  - /score test_count 0
  - /score jackpot_count 0
  - /jump probability_test_loop

probability_test_loop:
  - /add test_count
  - /if test_count > 100 /jump probability_results
  - # ここでスロット処理を実行
  - /jump probability_test_loop

probability_results:
  - テスト結果:
  - 総回数: \\test_count\\
  - ジャックポット回数: \\jackpot_count\\
  - ジャックポット確率: \\jackpot_count\\%
```

## 🔗 関連ドキュメント

- **[変数・スコア管理](../commands/variables.md)** - 変数操作の詳細
- **[条件分岐](../commands/conditions.md)** - 条件判定の活用
- **[音声制御](../commands/audio.md)** - 効果音の設定
- **[基本設定](../commands/basic-settings.md)** - 色と演出効果

スロットゲームは確率、演出、報酬のバランスが重要です。プレイヤーが楽しめる適切な設定を心がけましょう！