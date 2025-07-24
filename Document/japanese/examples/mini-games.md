# ミニゲーム

RPGTextで実装できる様々なミニゲームの作り方を解説します。

## 🎲 基本的なミニゲーム

### じゃんけんゲーム

```yaml
janken_game:
  - /color \6\l
  - === じゃんけん勝負！ ===
  - /color \r
  - 参加費: 20ゴールド
  - 勝利: 50ゴールド獲得
  - あいこ: 参加費返却
  - 敗北: 参加費没収
  - /? プレイする 帰る
  - /?プレイする /jump janken_start
  - /?帰る また来てください

janken_start:
  - /if money >= 20 /jump janken_play
  - お金が足りません
  - /jump janken_game

janken_play:
  - /score money - 20
  - 何を出しますか？
  - /? グー チョキ パー
  - /?グー /jump player_rock
  - /?チョキ /jump player_scissors
  - /?パー /jump player_paper

player_rock:
  - /score player_choice 1
  - あなた: グー
  - /jump cpu_choice

player_scissors:
  - /score player_choice 2
  - あなた: チョキ
  - /jump cpu_choice

player_paper:
  - /score player_choice 3
  - あなた: パー
  - /jump cpu_choice

cpu_choice:
  - /score cpu_choice 1
  - /add cpu_choice
  - /add cpu_choice
  - /if cpu_choice > 3 /score cpu_choice - 3
  - /wait 40
  - 相手の選択...
  - /wait 30
  - /if cpu_choice = 1 相手: グー
  - /if cpu_choice = 2 相手: チョキ
  - /if cpu_choice = 3 相手: パー
  - /jump janken_result

janken_result:
  - /wait 30
  - /if player_choice = cpu_choice /jump janken_draw
  - /if player_choice = 1 & cpu_choice = 2 /jump janken_win
  - /if player_choice = 2 & cpu_choice = 3 /jump janken_win
  - /if player_choice = 3 & cpu_choice = 1 /jump janken_win
  - /jump janken_lose

janken_win:
  - /color \a\l
  - あなたの勝ち！
  - /color \r
  - /score money + 50
  - 50ゴールド獲得！
  - /singlesound minecraft:entity.player.levelup 1 1.5
  - /jump janken_continue

janken_draw:
  - /color \e
  - あいこです
  - /color \r
  - /score money + 20
  - 参加費を返却します
  - /singlesound minecraft:block.note.bell 1 1
  - /jump janken_continue

janken_lose:
  - /color \c
  - あなたの負けです
  - /color \r
  - /singlesound minecraft:entity.villager.no 1 1
  - /jump janken_continue

janken_continue:
  - 現在の所持金: \\money\\ゴールド
  - /? もう一度 帰る
  - /?もう一度 /jump janken_start
  - /?帰る ありがとうございました
```

### 数当てゲーム

```yaml
number_guess:
  - /color \b\l
  - === 数当てゲーム ===
  - /color \r
  - 1〜10の数字を当ててください
  - 参加費: 30ゴールド
  - 正解: 100ゴールド
  - /? プレイする 帰る
  - /?プレイする /jump guess_start
  - /?帰る また来てください

guess_start:
  - /if money >= 30 /jump guess_setup
  - お金が足りません
  - /jump number_guess

guess_setup:
  - /score money - 30
  - /score answer 1
  - /add answer
  - /add answer
  - /add answer
  - /if answer > 10 /score answer - 10
  - 数字を選択してください（1〜10）
  - /jump guess_choice

guess_choice:
  - /? 1 2 3 4 5
  - /?1 /jump check_answer_1
  - /?2 /jump check_answer_2
  - /?3 /jump check_answer_3
  - /?4 /jump check_answer_4
  - /?5 /jump check_answer_5

check_answer_1:
  - /score guess 1
  - /jump check_result

check_answer_2:
  - /score guess 2
  - /jump check_result

check_result:
  - あなたの予想: \\guess\\
  - 正解: \\answer\\
  - /if guess = answer /jump guess_correct
  - /jump guess_wrong

guess_correct:
  - /color \a\l
  - 正解です！
  - /color \r
  - /score money + 100
  - 100ゴールド獲得！
  - /singlesound minecraft:entity.player.levelup 1 2
  - /jump guess_continue

guess_wrong:
  - /color \c
  - 残念、不正解です
  - /color \r
  - /jump guess_continue

guess_continue:
  - /? もう一度 帰る
  - /?もう一度 /jump guess_start
  - /?帰る ありがとうございました
```

## 🏁 競技系ミニゲーム

### レースゲーム

```yaml
race_game:
  - /color \e\l
  - === スピードレース ===
  - /color \r
  - ゴールまで最短で到達せよ！
  - 参加費: 50ゴールド
  - /? スタート 帰る
  - /?スタート /jump race_start
  - /?帰る また来てください

race_start:
  - /if money >= 50 /jump race_begin
  - お金が足りません
  - /jump race_game

race_begin:
  - /score money - 50
  - /score position 0
  - /score moves 0
  - レース開始！
  - ゴールは10マス先です
  - /jump race_move

race_move:
  - 現在位置: \\position\\/10
  - どう移動しますか？
  - /? 慎重に進む 普通に進む 全力疾走
  - /?慎重に進む /jump move_careful
  - /?普通に進む /jump move_normal
  - /?全力疾走 /jump move_fast

move_careful:
  - /score position + 1
  - /add moves
  - 慎重に1マス進みました
  - /jump check_race_result

move_normal:
  - /score temp_move 1
  - /add temp_move
  - /score position + \\temp_move\\
  - /add moves
  - \\temp_move\\マス進みました
  - /jump check_race_result

move_fast:
  - /score temp_move 2
  - /add temp_move
  - /score position + \\temp_move\\
  - /add moves
  - 勢いよく\\temp_move\\マス進みました！
  - /jump check_race_result

check_race_result:
  - /if position >= 10 /jump race_goal
  - /if moves >= 15 /jump race_timeout
  - /jump race_move

race_goal:
  - /color \a\l
  - ゴール到達！
  - /color \r
  - /if moves <= 8 /jump race_excellent
  - /if moves <= 12 /jump race_good
  - /jump race_normal

race_excellent:
  - 素晴らしいタイム！
  - /score money + 150
  - 150ゴールド獲得！
  - /jump race_end

race_good:
  - 良いタイムです
  - /score money + 100
  - 100ゴールド獲得！
  - /jump race_end

race_normal:
  - /score money + 75
  - 75ゴールド獲得
  - /jump race_end

race_timeout:
  - 制限に達しました
  - 参加費没収...
  - /jump race_end

race_end:
  - /? もう一度 帰る
  - /?もう一度 /jump race_start
  - /?帰る お疲れ様でした
```

## 🧩 パズル系ミニゲーム

### 記憶ゲーム

```yaml
memory_game:
  - /color \d\l
  - === 記憶力テスト ===
  - /color \r
  - 表示される順番を覚えてください
  - 参加費: 40ゴールド
  - /? チャレンジ 帰る
  - /?チャレンジ /jump memory_start
  - /?帰る また来てください

memory_start:
  - /if money >= 40 /jump memory_begin
  - お金が足りません
  - /jump memory_game

memory_begin:
  - /score money - 40
  - パターンを表示します...
  - /wait 40
  - /jump show_pattern

show_pattern:
  - /score pattern_1 1
  - /score pattern_2 2
  - /score pattern_3 3
  - /add pattern_1
  - /add pattern_2
  - /add pattern_3
  - /color \a
  - 1番目: \\pattern_1\\
  - /color \r
  - /wait 60
  - /color \b
  - 2番目: \\pattern_2\\
  - /color \r
  - /wait 60
  - /color \c
  - 3番目: \\pattern_3\\
  - /color \r
  - /wait 60
  - パターン終了
  - /jump memory_test

memory_test:
  - 順番を答えてください
  - 1番目は何でしたか？
  - /? 1 2 3 4
  - /?1 /jump check_first_1
  - /?2 /jump check_first_2
  - /?3 /jump check_first_3
  - /?4 /jump check_first_4

check_first_1:
  - /score answer_1 1
  - /jump ask_second

check_first_2:
  - /score answer_1 2
  - /jump ask_second

ask_second:
  - 2番目は何でしたか？
  - /? 1 2 3 4
  - /?1 /jump check_second_1
  - /?2 /jump check_second_2

check_memory_result:
  - /if answer_1 = pattern_1 & answer_2 = pattern_2 & answer_3 = pattern_3 /jump memory_perfect
  - /if answer_1 = pattern_1 & answer_2 = pattern_2 /jump memory_partial
  - /jump memory_wrong

memory_perfect:
  - /color \a\l
  - 完璧です！
  - /color \r
  - /score money + 120
  - 120ゴールド獲得！
  - /jump memory_end

memory_partial:
  - 惜しい！半分正解
  - /score money + 60
  - 60ゴールド獲得
  - /jump memory_end

memory_wrong:
  - 残念、不正解です
  - /jump memory_end

memory_end:
  - /? もう一度 帰る
  - /?もう一度 /jump memory_start
  - /?帰る ありがとうございました
```

## 🎰 カジノ系ミニゲーム

### ブラックジャック（簡易版）

```yaml
blackjack:
  - /color \0\l
  - === ブラックジャック ===
  - /color \r
  - 21に近づけろ！
  - 参加費: 100ゴールド
  - /? プレイ 帰る
  - /?プレイ /jump bj_start
  - /?帰る また来てください

bj_start:
  - /if money >= 100 /jump bj_begin
  - お金が足りません
  - /jump blackjack

bj_begin:
  - /score money - 100
  - /score player_total 0
  - /score dealer_total 0
  - カードを配ります...
  - /jump deal_initial_cards

deal_initial_cards:
  - /score card1 1
  - /score card2 1
  - /add card1
  - /add card1
  - /add card2
  - /add card2
  - /if card1 > 10 /score card1 10
  - /if card2 > 10 /score card2 10
  - /score player_total \\card1\\ + \\card2\\
  - あなたのカード: \\card1\\, \\card2\\ (合計: \\player_total\\)
  - /jump player_turn

player_turn:
  - /if player_total >= 21 /jump check_player_bust
  - もう1枚引きますか？
  - /? ヒット スタンド
  - /?ヒット /jump player_hit
  - /?スタンド /jump dealer_turn

player_hit:
  - /score new_card 1
  - /add new_card
  - /add new_card
  - /if new_card > 10 /score new_card 10
  - /score player_total + \\new_card\\
  - 引いたカード: \\new_card\\ (合計: \\player_total\\)
  - /jump player_turn

check_player_bust:
  - /if player_total > 21 /jump player_bust
  - /jump dealer_turn

player_bust:
  - /color \c
  - バースト！あなたの負けです
  - /color \r
  - /jump bj_end

dealer_turn:
  - ディーラーのターン...
  - /score dealer_card1 1
  - /score dealer_card2 1
  - /add dealer_card1
  - /add dealer_card2
  - /if dealer_card1 > 10 /score dealer_card1 10
  - /if dealer_card2 > 10 /score dealer_card2 10
  - /score dealer_total \\dealer_card1\\ + \\dealer_card2\\
  - ディーラー: \\dealer_card1\\, \\dealer_card2\\ (合計: \\dealer_total\\)
  - /jump dealer_decision

dealer_decision:
  - /if dealer_total >= 17 /jump compare_hands
  - /if dealer_total < 17 /jump dealer_hit
  - /jump compare_hands

dealer_hit:
  - /score dealer_new 1
  - /add dealer_new
  - /if dealer_new > 10 /score dealer_new 10
  - /score dealer_total + \\dealer_new\\
  - ディーラーが引いたカード: \\dealer_new\\
  - /jump dealer_decision

compare_hands:
  - /if dealer_total > 21 /jump dealer_bust
  - /if player_total > dealer_total /jump player_wins
  - /if player_total = dealer_total /jump push
  - /jump dealer_wins

dealer_bust:
  - ディーラーバースト！あなたの勝ちです
  - /score money + 200
  - 200ゴールド獲得！
  - /jump bj_end

player_wins:
  - あなたの勝ちです！
  - /score money + 200
  - 200ゴールド獲得！
  - /jump bj_end

push:
  - 引き分けです
  - /score money + 100
  - 参加費を返却
  - /jump bj_end

dealer_wins:
  - ディーラーの勝ちです
  - /jump bj_end

bj_end:
  - /? もう一度 帰る
  - /?もう一度 /jump bj_start
  - /?帰る ありがとうございました
```

## 🎯 スキル系ミニゲーム

### タイミングゲーム

```yaml
timing_game:
  - /color \e\l
  - === タイミングチャレンジ ===
  - /color \r
  - 完璧なタイミングを狙え！
  - 参加費: 30ゴールド
  - /? スタート 帰る
  - /?スタート /jump timing_start
  - /?帰る また来てください

timing_start:
  - /if money >= 30 /jump timing_begin
  - お金が足りません
  - /jump timing_game

timing_begin:
  - /score money - 30
  - /score timing_counter 0
  - 準備...
  - /wait 60
  - /jump timing_sequence

timing_sequence:
  - /add timing_counter
  - /if timing_counter = 5 ●○○○○○○○○○
  - /if timing_counter = 10 ○●○○○○○○○○
  - /if timing_counter = 15 ○○●○○○○○○○
  - /if timing_counter = 20 ○○○●○○○○○○
  - /if timing_counter = 25 ○○○○●○○○○○
  - /wait 10
  - /if timing_counter >= 50 /jump timing_failed
  - /? ストップ！
  - /?ストップ！ /jump check_timing
  - /jump timing_sequence

check_timing:
  - /if timing_counter >= 23 & timing_counter <= 27 /jump timing_perfect
  - /if timing_counter >= 20 & timing_counter <= 30 /jump timing_good
  - /jump timing_miss

timing_perfect:
  - /color \a\l
  - パーフェクト！
  - /color \r
  - /score money + 100
  - 100ゴールド獲得！
  - /jump timing_end

timing_good:
  - グッド！
  - /score money + 60
  - 60ゴールド獲得
  - /jump timing_end

timing_miss:
  - ミス...
  - /jump timing_end

timing_failed:
  - タイムアップ
  - /jump timing_end

timing_end:
  - /? もう一度 帰る
  - /?もう一度 /jump timing_start
  - /?帰る お疲れ様でした
```

## 🎪 応用・拡張

### トーナメント機能

```yaml
tournament_system:
  - /color \6\l
  - === ミニゲーム大会 ===
  - /color \r
  - 参加費: 200ゴールド
  - 優勝賞金: 1000ゴールド
  - /? 参加する 観戦する 帰る
  - /?参加する /jump tournament_join
  - /?観戦する /jump tournament_watch
  - /?帰る また来てください

tournament_bracket:
  - 1回戦: じゃんけん
  - 2回戦: 数当て
  - 決勝戦: ブラックジャック
  - /jump tournament_round_1
```

### スコアランキング

```yaml
ranking_system:
  - /color \d\l
  - === ハイスコア ===
  - /color \r
  - 1位: \\rank_1_name\\ - \\rank_1_score\\点
  - 2位: \\rank_2_name\\ - \\rank_2_score\\点
  - 3位: \\rank_3_name\\ - \\rank_3_score\\点
  - 
  - あなたの最高記録: \\personal_best\\点
  - /if personal_best > rank_3_score ランクイン可能です！
```

## 🔗 関連ドキュメント

- **[変数・スコア管理](../commands/variables.md)** - ゲーム状態の管理
- **[条件分岐](../commands/conditions.md)** - ゲームロジックの実装
- **[音声制御](../commands/audio.md)** - 効果音とBGM
- **[スロットゲーム](slot-game.md)** - より複雑なゲーム例

これらのミニゲームを組み合わせることで、プレイヤーが長時間楽しめるエンターテイメント空間を作ることができます！