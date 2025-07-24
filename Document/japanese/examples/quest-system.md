# クエストシステム

RPGTextで実装する包括的なクエスト受注・進行管理システムの構築方法を解説します。

## 🗺️ クエストシステムの基本構造

### システム概要

```yaml
# quest_system.yml - メインハブ
quest_hub:
  - /color \6\l
  - === 冒険者ギルド ===
  - /color \r
  - ようこそ、%player%さん
  - 現在のレベル: %level%
  - /? クエスト受注 進行確認 報酬受取 帰る
  - /?クエスト受注 /jump quest_board
  - /?進行確認 /jump quest_progress
  - /?報酬受取 /jump quest_rewards
  - /?帰る また来てくださいね

# クエストの初期化（初回実行時）
quest_init:
  - /score quest_001_status 0  # 0:未受注 1:進行中 2:完了 3:報酬受取済
  - /score quest_002_status 0
  - /score quest_003_status 0
  - /score quest_001_progress 0  # 進行度
  - /score quest_002_progress 0
  - /score quest_003_progress 0
  - ギルドシステムを初期化しました
  - /jump quest_hub
```

## 📋 クエスト受注システム

### クエストボード

```yaml
quest_board:
  - /color \b\l
  - === 依頼掲示板 ===
  - /color \r
  - 利用可能なクエスト:
  - /jump check_available_quests

check_available_quests:
  - /score available_count 0
  - /if quest_001_status = 0 /add available_count
  - /if quest_002_status = 0 /add available_count
  - /if quest_003_status = 0 /add available_count
  - /if available_count = 0 /jump no_quests_available
  - /jump show_quest_list

no_quests_available:
  - /color \8
  - 現在受注可能なクエストはありません
  - /color \r
  - /? 戻る
  - /?戻る /jump quest_hub

show_quest_list:
  - /if quest_001_status = 0 /jump show_quest_001
  - /jump check_quest_002

show_quest_001:
  - /color \2
  - [1] スライム退治
  - /color \r
  - 　報酬: 100ゴールド、経験値50
  - 　難易度: ★☆☆
  - 　内容: スライムを5匹倒してください
  - /jump check_quest_002

check_quest_002:
  - /if quest_002_status = 0 /jump show_quest_002
  - /jump check_quest_003

show_quest_002:
  - /color \3
  - [2] アイテム収集
  - /color \r
  - 　報酬: 200ゴールド、特別アイテム
  - 　難易度: ★★☆
  - 　内容: 鉄鉱石を10個集めてください
  - /jump check_quest_003

check_quest_003:
  - /if quest_003_status = 0 /jump show_quest_003
  - /jump quest_selection

show_quest_003:
  - /color \4
  - [3] ボス討伐
  - /color \r
  - 　報酬: 500ゴールド、レア装備
  - 　難易度: ★★★
  - 　内容: エンダードラゴンを討伐してください
  - /jump quest_selection

quest_selection:
  - 
  - どのクエストを受注しますか？
  - /score quest_choices 0
  - /if quest_001_status = 0 /add quest_choices
  - /if quest_002_status = 0 /add quest_choices
  - /if quest_003_status = 0 /add quest_choices
  - /if quest_choices = 1 /jump single_quest_choice
  - /if quest_choices = 2 /jump dual_quest_choice
  - /jump triple_quest_choice

single_quest_choice:
  - /if quest_001_status = 0 /? スライム退治を受注 戻る
  - /if quest_002_status = 0 /? アイテム収集を受注 戻る
  - /if quest_003_status = 0 /? ボス討伐を受注 戻る
  - /jump process_single_choice

dual_quest_choice:
  - /? スライム退治 アイテム収集 戻る
  - /?スライム退治 /jump accept_quest_001
  - /?アイテム収集 /jump accept_quest_002
  - /?戻る /jump quest_hub

triple_quest_choice:
  - /? スライム退治 アイテム収集 ボス討伐 戻る
  - /?スライム退治 /jump accept_quest_001
  - /?アイテム収集 /jump accept_quest_002
  - /?ボス討伐 /jump accept_quest_003
  - /?戻る /jump quest_hub

# 各クエスト受注処理
accept_quest_001:
  - /score quest_001_status 1
  - /score quest_001_progress 0
  - /color \a
  - 「スライム退治」を受注しました！
  - /color \r
  - 頑張って5匹のスライムを倒してください
  - /singlesound minecraft:entity.experience_orb.pickup 1 1
  - /jump quest_hub

accept_quest_002:
  - /score quest_002_status 1
  - /score quest_002_progress 0
  - /color \a
  - 「アイテム収集」を受注しました！
  - /color \r
  - 鉄鉱石を10個集めてきてください
  - /singlesound minecraft:entity.experience_orb.pickup 1 1
  - /jump quest_hub

accept_quest_003:
  - /if level < 30 /jump quest_003_level_check
  - /score quest_003_status 1
  - /score quest_003_progress 0
  - /color \a
  - 「ボス討伐」を受注しました！
  - /color \r
  - エンダードラゴン討伐に向かってください！
  - /singlesound minecraft:entity.experience_orb.pickup 1 1
  - /jump quest_hub

quest_003_level_check:
  - /color \c
  - このクエストはレベル30以上が必要です
  - /color \r
  - 現在のレベル: %level%
  - /jump quest_board
```

## 📈 進行管理システム

### 進行状況確認

```yaml
quest_progress:
  - /color \b\l
  - === クエスト進行状況 ===
  - /color \r
  - /jump check_active_quests

check_active_quests:
  - /score active_count 0
  - /if quest_001_status = 1 /add active_count
  - /if quest_002_status = 1 /add active_count
  - /if quest_003_status = 1 /add active_count
  - /if active_count = 0 /jump no_active_quests
  - /jump show_active_quests

no_active_quests:
  - /color \8
  - 現在進行中のクエストはありません
  - /color \r
  - /? 戻る
  - /?戻る /jump quest_hub

show_active_quests:
  - /if quest_001_status = 1 /jump show_progress_001
  - /jump check_progress_002

show_progress_001:
  - /color \2
  - [進行中] スライム退治
  - /color \r
  - 　進行度: \\quest_001_progress\\/5
  - 　残り: \\quest_001_remaining\\匹
  - /score quest_001_remaining 5
  - /score quest_001_remaining - \\quest_001_progress\\
  - /if quest_001_progress >= 5 /color \a
  - /if quest_001_progress >= 5 完了！報酬を受け取れます
  - /if quest_001_progress < 5 /color \e
  - /if quest_001_progress < 5 まだ\\quest_001_remaining\\匹必要です
  - /color \r
  - /jump check_progress_002

check_progress_002:
  - /if quest_002_status = 1 /jump show_progress_002
  - /jump check_progress_003

show_progress_002:
  - /color \3
  - [進行中] アイテム収集
  - /color \r
  - 　進行度: \\quest_002_progress\\/10
  - /score quest_002_remaining 10
  - /score quest_002_remaining - \\quest_002_progress\\
  - /if quest_002_progress >= 10 /color \a
  - /if quest_002_progress >= 10 完了！報酬を受け取れます
  - /if quest_002_progress < 10 /color \e
  - /if quest_002_progress < 10 まだ\\quest_002_remaining\\個必要です
  - /color \r
  - /jump check_progress_003

check_progress_003:
  - /if quest_003_status = 1 /jump show_progress_003
  - /jump progress_actions

show_progress_003:
  - /color \4
  - [進行中] ボス討伐
  - /color \r
  - /if quest_003_progress = 0 　状況: 未討伐
  - /if quest_003_progress >= 1 /color \a
  - /if quest_003_progress >= 1 　状況: 討伐完了！
  - /color \r
  - /jump progress_actions

progress_actions:
  - 
  - /? 進行更新 戻る
  - /?進行更新 /jump update_progress
  - /?戻る /jump quest_hub

# 進行度更新（手動またはイベント連動）
update_progress:
  - どのクエストの進行を更新しますか？
  - /if quest_001_status = 1 /? スライム退治 
  - /if quest_002_status = 1 /? アイテム収集
  - /if quest_003_status = 1 /? ボス討伐
  - /? 戻る
  - /if quest_001_status = 1 /?スライム退治 /jump update_quest_001
  - /if quest_002_status = 1 /?アイテム収集 /jump update_quest_002
  - /if quest_003_status = 1 /?ボス討伐 /jump update_quest_003
  - /?戻る /jump quest_progress

update_quest_001:
  - /add quest_001_progress
  - スライムを1匹倒しました！
  - 現在の進行度: \\quest_001_progress\\/5
  - /if quest_001_progress >= 5 /jump complete_quest_001
  - /singlesound minecraft:entity.experience_orb.pickup 1 1
  - /jump quest_progress

complete_quest_001:
  - /score quest_001_status 2
  - /color \a\l
  - クエスト「スライム退治」完了！
  - /color \r
  - ギルドに報告してください
  - /singlesound minecraft:entity.player.levelup 1 1
  - /jump quest_progress

update_quest_002:
  - アイテムをいくつ入手しましたか？
  - /? 1個 3個 5個 戻る
  - /?1個 /jump add_items_1
  - /?3個 /jump add_items_3
  - /?5個 /jump add_items_5
  - /?戻る /jump quest_progress

add_items_1:
  - /score quest_002_progress + 1
  - /jump check_quest_002_complete

add_items_3:
  - /score quest_002_progress + 3
  - /jump check_quest_002_complete

add_items_5:
  - /score quest_002_progress + 5
  - /jump check_quest_002_complete

check_quest_002_complete:
  - 鉄鉱石を入手しました！
  - 現在の進行度: \\quest_002_progress\\/10
  - /if quest_002_progress >= 10 /jump complete_quest_002
  - /singlesound minecraft:entity.experience_orb.pickup 1 1
  - /jump quest_progress

complete_quest_002:
  - /score quest_002_status 2
  - /color \a\l
  - クエスト「アイテム収集」完了！
  - /color \r
  - ギルドに報告してください
  - /singlesound minecraft:entity.player.levelup 1 1
  - /jump quest_progress

update_quest_003:
  - エンダードラゴンを討伐しましたか？
  - /? はい いいえ
  - /?はい /jump complete_quest_003
  - /?いいえ /jump quest_progress

complete_quest_003:
  - /score quest_003_progress 1
  - /score quest_003_status 2
  - /color \a\l
  - クエスト「ボス討伐」完了！
  - /color \r
  - 素晴らしい功績です！ギルドに報告してください
  - /singlesound minecraft:entity.player.levelup 1 2
  - /jump quest_progress
```

## 🎁 報酬システム

### 報酬受取処理

```yaml
quest_rewards:
  - /color \6\l
  - === 報酬受取 ===
  - /color \r
  - /jump check_completed_quests

check_completed_quests:
  - /score completed_count 0
  - /if quest_001_status = 2 /add completed_count
  - /if quest_002_status = 2 /add completed_count
  - /if quest_003_status = 2 /add completed_count
  - /if completed_count = 0 /jump no_completed_quests
  - /jump show_completed_quests

no_completed_quests:
  - /color \8
  - 受取可能な報酬はありません
  - /color \r
  - /? 戻る
  - /?戻る /jump quest_hub

show_completed_quests:
  - 受取可能な報酬:
  - /if quest_001_status = 2 /jump show_reward_001
  - /jump check_reward_002

show_reward_001:
  - /color \2
  - [完了] スライム退治
  - /color \r
  - 　報酬: 100ゴールド + 経験値50
  - /jump check_reward_002

check_reward_002:
  - /if quest_002_status = 2 /jump show_reward_002
  - /jump check_reward_003

show_reward_002:
  - /color \3
  - [完了] アイテム収集
  - /color \r
  - 　報酬: 200ゴールド + 特別アイテム
  - /jump check_reward_003

check_reward_003:
  - /if quest_003_status = 2 /jump show_reward_003
  - /jump reward_selection

show_reward_003:
  - /color \4
  - [完了] ボス討伐
  - /color \r
  - 　報酬: 500ゴールド + レア装備
  - /jump reward_selection

reward_selection:
  - 
  - どの報酬を受け取りますか？
  - /if quest_001_status = 2 /? スライム退治の報酬
  - /if quest_002_status = 2 /? アイテム収集の報酬
  - /if quest_003_status = 2 /? ボス討伐の報酬
  - /? 戻る
  - /if quest_001_status = 2 /?スライム退治の報酬 /jump receive_reward_001
  - /if quest_002_status = 2 /?アイテム収集の報酬 /jump receive_reward_002
  - /if quest_003_status = 2 /?ボス討伐の報酬 /jump receive_reward_003
  - /?戻る /jump quest_hub

receive_reward_001:
  - /score quest_001_status 3
  - /score money + 100
  - /score exp + 50
  - /color \a\l
  - 報酬を受け取りました！
  - /color \r
  - + 100ゴールド
  - + 経験値50
  - /command give %player% minecraft:gold_ingot 5
  - /singlesound minecraft:entity.player.levelup 1 1.5
  - /jump quest_rewards

receive_reward_002:
  - /score quest_002_status 3
  - /score money + 200
  - /color \a\l
  - 報酬を受け取りました！
  - /color \r
  - + 200ゴールド
  - + 特別アイテム
  - /command give %player% minecraft:diamond_sword
  - /command give %player% minecraft:gold_ingot 10
  - /singlesound minecraft:entity.player.levelup 1 1.5
  - /jump quest_rewards

receive_reward_003:
  - /score quest_003_status 3
  - /score money + 500
  - /score legendary_items + 1
  - /color \a\l
  - 報酬を受け取りました！
  - /color \r
  - + 500ゴールド
  - + レア装備
  - /command give %player% minecraft:netherite_sword
  - /command give %player% minecraft:dragon_head
  - /command give %player% minecraft:gold_ingot 25
  - /singlesound minecraft:entity.player.levelup 1 2
  - /jump quest_rewards
```

## 🔄 高度なクエストシステム

### 連鎖クエスト

```yaml
# 段階的なクエストライン
story_quest_line:
  - /if main_story_progress = 0 /jump story_chapter_1
  - /if main_story_progress = 1 /jump story_chapter_2
  - /if main_story_progress = 2 /jump story_chapter_3
  - /jump story_complete

story_chapter_1:
  - /color \5\l
  - === 第1章: 冒険の始まり ===
  - /color \r
  - 村を脅かすゴブリンを倒してください
  - /? 受注する 後で
  - /?受注する /jump accept_story_001
  - /?後で /jump quest_hub

accept_story_001:
  - /score story_quest_001_status 1
  - /score main_story_progress 1
  - ストーリークエスト「第1章」を受注
  - ゴブリンを10匹倒してください
  - /jump quest_hub

# 時限クエスト
timed_quest:
  - /score current_time 0  # 現在時刻の取得（仮想）
  - /score quest_deadline 3600  # 1時間後
  - /if current_time > quest_deadline /jump quest_expired
  - 残り時間: \\remaining_time\\秒
  - /jump normal_quest_flow

quest_expired:
  - /color \c
  - クエストの制限時間が過ぎました
  - /color \r
  - クエストは自動的にキャンセルされます
  - /score time_quest_status 0
  - /jump quest_hub

# 条件付きクエスト
conditional_quest:
  - /if level < 20 /jump level_requirement_not_met
  - /if completed_quests < 5 /jump experience_requirement_not_met
  - /has DIAMOND_SWORD 1 none /jump item_requirement_not_met
  - 全ての条件を満たしています
  - /jump show_special_quest

level_requirement_not_met:
  - レベル20以上が必要です（現在: %level%）
  - /jump quest_hub

experience_requirement_not_met:
  - 5個以上のクエスト完了が必要です
  - 現在の完了数: \\completed_quests\\
  - /jump quest_hub

item_requirement_not_met:
  - ダイヤモンドの剣が必要です
  - /jump quest_hub
```

### デイリー・ウィークリークエスト

```yaml
daily_quest_system:
  - /score current_day 0  # 現在の日を取得
  - /if current_day > last_daily_reset /jump reset_daily_quests
  - /jump show_daily_quests

reset_daily_quests:
  - /score last_daily_reset \\current_day\\
  - /score daily_quest_001_status 0
  - /score daily_quest_002_status 0
  - /score daily_quest_003_status 0
  - デイリークエストがリセットされました！
  - /jump show_daily_quests

show_daily_quests:
  - /color \e\l
  - === デイリークエスト ===
  - /color \r
  - 毎日リセットされる特別なクエストです
  - 
  - /if daily_quest_001_status = 0 [利用可能] モンスター狩り
  - /if daily_quest_001_status = 1 [進行中] モンスター狩り
  - /if daily_quest_001_status = 2 [完了] モンスター狩り
  - 
  - /if daily_quest_002_status = 0 [利用可能] 採掘作業
  - /if daily_quest_002_status = 1 [進行中] 採掘作業
  - /if daily_quest_002_status = 2 [完了] 採掘作業
  - 
  - /if daily_quest_003_status = 0 [利用可能] 取引任務
  - /if daily_quest_003_status = 1 [進行中] 取引任務
  - /if daily_quest_003_status = 2 [完了] 取引任務

weekly_quest_system:
  - /score current_week 0  # 現在の週を取得
  - /if current_week > last_weekly_reset /jump reset_weekly_quests
  - /jump show_weekly_quests

reset_weekly_quests:
  - /score last_weekly_reset \\current_week\\
  - /score weekly_quest_status 0
  - ウィークリークエストがリセットされました！
  - /jump show_weekly_quests

show_weekly_quests:
  - /color \d\l
  - === ウィークリーチャレンジ ===
  - /color \r
  - 高難易度の週間クエストです
  - 報酬も豪華になっています！
```

## 📊 統計・実績システム

### クエスト統計

```yaml
quest_statistics:
  - /color \b\l
  - === クエスト統計 ===
  - /color \r
  - 総受注回数: \\total_accepted\\
  - 総完了回数: \\total_completed\\
  - 成功率: \\success_rate\\%
  - 獲得ゴールド: \\total_gold_earned\\G
  - 最高難易度クリア: \\max_difficulty_cleared\\
  - 
  - === カテゴリ別統計 ===
  - 討伐系: \\combat_quests_completed\\完了
  - 収集系: \\gathering_quests_completed\\完了
  - 探索系: \\exploration_quests_completed\\完了
  - 配達系: \\delivery_quests_completed\\完了

# 実績システム
achievement_system:
  - /if total_completed >= 10 /jump unlock_veteran_badge
  - /if combat_quests_completed >= 20 /jump unlock_warrior_title
  - /if total_gold_earned >= 10000 /jump unlock_wealthy_title
  - /jump check_other_achievements

unlock_veteran_badge:
  - /if veteran_badge_unlocked = 0 /jump new_veteran_badge
  - /jump check_warrior_title

new_veteran_badge:
  - /score veteran_badge_unlocked 1
  - /color \6\l
  - 🏆 実績解除！「ベテラン冒険者」🏆
  - /color \r
  - 10個のクエストを完了しました！
  - /singlesound minecraft:entity.player.levelup 1 2
  - /jump check_warrior_title

unlock_warrior_title:
  - /if warrior_title_unlocked = 0 /jump new_warrior_title
  - /jump check_wealthy_title

new_warrior_title:
  - /score warrior_title_unlocked 1
  - /color \c\l
  - ⚔️ 称号獲得！「戦士」⚔️
  - /color \r
  - 20個の討伐クエストを完了しました！
  - /singlesound minecraft:entity.player.levelup 1 2
  - /jump check_wealthy_title
```

## 🎮 自動化・連動機能

### イベント連動システム

```yaml
# Minecraftイベントとの連動
event_integration:
  - /command testfor @p[scores={killCount=1..}]
  - /if kill_detected = 1 /jump process_monster_kill
  - /command testfor @p[nbt={Inventory:[{id:"minecraft:iron_ore"}]}]
  - /if item_detected = 1 /jump process_item_collection

process_monster_kill:
  - /score kill_detected 0
  - /if quest_001_status = 1 /add quest_001_progress
  - /if quest_001_progress >= 5 /jump auto_complete_001
  - モンスターを倒しました！(\\quest_001_progress\\/5)
  - /singlesound minecraft:entity.experience_orb.pickup 1 1

auto_complete_001:
  - /score quest_001_status 2
  - /color \a\l
  - クエスト自動完了！「スライム退治」
  - /color \r
  - ギルドに報告してください
  - /singlesound minecraft:entity.player.levelup 1 1

# 時間管理システム
time_management:
  - /score game_day 0  # ゲーム内日数
  - /add game_day
  - /if game_day % 7 = 0 /jump weekly_reset
  - /jump daily_update

weekly_reset:
  - ウィークリークエストをリセット
  - /score weekly_quest_status 0
  - /jump daily_update

daily_update:
  - デイリークエストを更新
  - /jump check_quest_deadlines

check_quest_deadlines:
  - /if timed_quest_deadline < game_day /jump expire_timed_quest
  - /jump normal_flow
```

## 🛠️ カスタマイズ・拡張

### 新しいクエストタイプ

```yaml
# 護衛クエスト
escort_quest:
  - NPCを安全に目的地まで送り届けてください
  - /score escort_health 100
  - /if escort_health <= 0 /jump escort_failed
  - /jump escort_checkpoint_1

escort_checkpoint_1:
  - チェックポイント1に到達
  - /score escort_progress + 25
  - 残り: \\escort_remaining\\%
  - /jump escort_checkpoint_2

# パズルクエスト  
puzzle_quest:
  - 古代の謎を解いてください
  - 手がかり1: 太陽が昇る方向
  - 手がかり2: 4つの要素
  - /? 東 火 神秘の言葉
  - /?東 正解です！次の謎へ
  - /?火 間違いです
  - /?神秘の言葉 /jump secret_answer

# 建築クエスト
building_quest:
  - 指定された建造物を作成してください
  - 要求: 木材100個、石材50個
  - /has WOOD 100 none /jump insufficient_wood
  - /has STONE 50 none /jump insufficient_stone
  - /jump building_approved

# レースクエスト
race_quest:
  - 制限時間内にゴールに到達してください
  - /score race_timer 300  # 5分
  - /jump race_start

race_timer_update:
  - /score race_timer - 1
  - /if race_timer <= 0 /jump race_timeout
  - /if player_at_goal = 1 /jump race_success
  - 残り時間: \\race_timer\\秒
  - /jump race_timer_update
```

### UI改善

```yaml
# ページネーション機能
quest_list_page_1:
  - /color \b\l
  - === クエスト一覧 (1/3) ===
  - /color \r
  - [1] 初心者の試練
  - [2] スライム退治
  - [3] 宝探し
  - [4] 薬草採集
  - [5] 配達任務
  - /? 次のページ 前のページ 選択
  - /?次のページ /jump quest_list_page_2
  - /?前のページ /jump quest_list_page_3
  - /?選択 /jump quest_selection_page_1

# フィルター機能
quest_filter:
  - クエストを絞り込みますか？
  - /? 全て表示 討伐系のみ 収集系のみ 難易度別
  - /?全て表示 /jump show_all_quests
  - /?討伐系のみ /jump show_combat_quests
  - /?収集系のみ /jump show_gathering_quests
  - /?難易度別 /jump show_by_difficulty

# 検索機能
quest_search:
  - どのような条件で検索しますか？
  - /? 報酬金額 難易度 カテゴリ キーワード
  - /?報酬金額 /jump search_by_reward
  - /?難易度 /jump search_by_difficulty
  - /?カテゴリ /jump search_by_category
  - /?キーワード /jump search_by_keyword
```

## 🔗 関連ドキュメント

- **[変数・スコア管理](../commands/variables.md)** - クエスト状態の管理
- **[条件分岐](../commands/conditions.md)** - クエスト条件の判定
- **[ゲーム制御](../commands/game-control.md)** - Minecraftとの連動
- **[ベストプラクティス](../reference/best-practices.md)** - 効率的な実装方法

クエストシステムは RPG の核となる要素です。プレイヤーのモチベーション維持と継続的な楽しみを提供できるよう、バランスの取れた設計を心がけましょう！