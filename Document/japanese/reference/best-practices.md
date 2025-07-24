# ベストプラクティス

RPGTextで効果的で保守しやすいスクリプトを作成するための推奨事項です。

## 🏗️ 設計原則

### KISS原則（Keep It Simple, Stupid）

**シンプルさを重視**
```yaml
# ✅ シンプルで理解しやすい
simple_approach:
  - こんにちは！
  - /? 挨拶する 去る
  - /?挨拶する こんにちは！
  - /?去る さようなら

# ❌ 不必要に複雑
overly_complex:
  - /if time >= 6 & time < 12 おはようございます
  - /if time >= 12 & time < 18 こんにちは
  - /if time >= 18 こんばんは
  - /? 詳細な挨拶をする 簡単な挨拶をする カスタム挨拶をする 去る
  # 選択肢が多すぎて混乱
```

### DRY原則（Don't Repeat Yourself）

**重複を避ける**
```yaml
# ✅ ファイル共通設定を活用
# ファイル先頭に設定
sound: block.note.bell 1 1.2
speed: 25
color: \r

section1:
  - メッセージ1
  - メッセージ2

section2:
  - メッセージ3
  - メッセージ4

# ❌ 毎回同じ設定を繰り返す
section1:
  - /sound block.note.bell 1 1.2
  - /speed 25
  - /color \r
  - メッセージ1

section2:
  - /sound block.note.bell 1 1.2  # 重複
  - /speed 25                     # 重複
  - /color \r                     # 重複
  - メッセージ3
```

### SRP原則（Single Responsibility Principle）

**一つのセクションは一つの役割**
```yaml
# ✅ 役割が明確なセクション
shop_greeting:
  - ショップへようこそ！
  - 何をお探しですか？

process_purchase:
  - 購入処理を行います
  - /score money - 100

show_inventory:
  - 現在の所持品：
  - 剣、盾、ポーション

# ❌ 複数の役割を持つセクション
mixed_responsibilities:
  - ショップへようこそ！        # 挨拶
  - /score money - 100          # 購入処理
  - 現在の所持品：剣            # インベントリ表示
  - 天気がいいですね            # 雑談
  # 役割が混在している
```

## 📝 コード構成

### 命名規則

**一貫した命名**
```yaml
# ✅ 推奨命名規則
section_names:
  shop_entrance:           # 動詞_名詞
  process_payment:         # 動詞_名詞
  show_status:            # 動詞_名詞
  quest_delivery_complete: # 名詞_名詞_形容詞

variable_names:
  player_level:           # 名詞_名詞
  money_amount:           # 名詞_名詞
  quest_status:           # 名詞_名詞
  is_member:              # is_形容詞

# ❌ 避けるべき命名
bad_naming:
  s1:                     # 短すぎる
  very_long_section_name_that_explains_everything:  # 長すぎる
  section_１:             # 日本語数字
  "section name":         # スペース
```

### ファイル構造

**論理的なファイル分割**
```yaml
# ✅ 推奨ファイル構造
messages/
├── main/
│   ├── hub.yml           # メインハブ
│   └── menu.yml          # メニューシステム
├── shops/
│   ├── weapon_shop.yml   # 武器屋
│   ├── item_shop.yml     # アイテム屋
│   └── magic_shop.yml    # 魔法屋
├── quests/
│   ├── tutorial.yml      # チュートリアル
│   ├── main_story.yml    # メインストーリー
│   └── side_quests.yml   # サイドクエスト
└── systems/
    ├── battle.yml        # 戦闘システム
    └── character.yml     # キャラクター管理

# ❌ 悪い構造
bad_structure/
├── everything.yml        # 全てを一つのファイルに
├── random_stuff.yml      # 関連性のない内容
└── temp_file.yml         # 一時的なファイルが残っている
```

### セクション構成

**読みやすいセクション配置**
```yaml
# ✅ 推奨構成（上から下へ論理的に配置）

# 1. メインフロー
shop_entrance:
  - ショップへようこそ
  - /jump shop_menu

shop_menu:
  - 何をご希望ですか？
  - /? 購入 売却 帰る
  - /?購入 /jump purchase_menu
  - /?売却 /jump sell_menu
  - /?帰る /jump shop_farewell

# 2. サブフロー
purchase_menu:
  - 購入メニュー
  # ...

sell_menu:
  - 売却メニュー
  # ...

# 3. 終了・ユーティリティ
shop_farewell:
  - ありがとうございました
```

## 🎯 ユーザー体験

### レスポンシブデザイン

**プレイヤーの行動を予測**
```yaml
# ✅ ユーザーフレンドリー
user_friendly:
  - 購入しますか？
  - /? 購入する 詳細を見る キャンセル
  - /?購入する /jump confirm_purchase
  - /?詳細を見る /jump item_details
  - /?キャンセル /jump shop_menu

confirm_purchase:
  - 100ゴールドで購入します
  - よろしいですか？
  - /? はい いいえ
  - /?はい /jump execute_purchase
  - /?いいえ /jump purchase_menu

# ❌ ユーザビリティが悪い
poor_usability:
  - 購入しますか？
  - /? はい
  - /?はい /jump execute_purchase
  # キャンセルや詳細確認の選択肢がない
```

### エラーハンドリング

**想定外の状況への対応**
```yaml
# ✅ 適切なエラーハンドリング
robust_error_handling:
  - 武器を購入しますか？
  - /? 購入する やめる
  - /?購入する /jump check_money

check_money:
  - /if money >= 100 /jump check_inventory
  - /color \c
  - 所持金が足りません（必要: 100G）
  - /color \r
  - 現在の所持金: \\money\\G
  - /jump earn_money_tips

check_inventory:
  - /has WOODEN_SWORD 1 none /jump inventory_full_warning
  - /jump execute_purchase

inventory_full_warning:
  - 既に武器を持っています
  - 売却してから購入してください
  - /jump weapon_sell_options

earn_money_tips:
  - === お金の稼ぎ方 ===
  - • クエストをクリアする
  - • アイテムを売却する
  - • ミニゲームで勝利する
  - /jump shop_menu

# ❌ エラーハンドリングなし
no_error_handling:
  - 武器を購入しますか？
  - /? 購入する
  - /?購入する /score money - 100
  # お金が足りない場合やインベントリが満杯の場合を考慮していない
```

### フィードバック

**プレイヤーへの適切な情報提供**
```yaml
# ✅ 豊富なフィードバック
good_feedback:
  - 購入処理中...
  - /wait 20
  - /score money - 100
  - /command give %player% minecraft:iron_sword
  - /singlesound minecraft:entity.experience_orb.pickup 1 1
  - /color \a\l
  - ✓ 鉄の剣を購入しました！
  - /color \r
  - 残金: \\money\\ゴールド
  - 攻撃力が6上昇します

# ❌ フィードバックが少ない
poor_feedback:
  - /score money - 100
  - /command give %player% minecraft:iron_sword
  - 購入完了
  # 何が起こったか分からない
```

## 🔧 パフォーマンス最適化

### 効率的な処理

**不要な処理を避ける**
```yaml
# ✅ 効率的
efficient_processing:
  - /if money >= 100 /jump can_afford
  - お金が足りません
  - /jump shop_menu

can_afford:
  - /if inventory_space > 0 /jump proceed_purchase
  - インベントリが満杯です
  - /jump shop_menu

proceed_purchase:
  - 購入を実行します
  # 必要な条件をクリアしてから処理

# ❌ 非効率
inefficient_processing:
  - /score money - 100              # 先に減算
  - /if money < 0 /score money + 100  # 条件不一致で元に戻す
  - /if money < 0 お金が足りません
  # 無駄な計算が多い
```

### メモリ効率

**変数の適切な管理**
```yaml
# ✅ 効率的な変数使用
efficient_variables:
  - /score temp_calc 0              # 一時変数を初期化
  - /score temp_calc player_attack
  - /score temp_calc + weapon_bonus
  - /score total_damage temp_calc
  - /score temp_calc 0              # 使用後はクリア

# ❌ 変数の無駄遣い
variable_waste:
  - /score calculation_result_for_damage_with_bonus_attack_power_final 0
  # 変数名が長すぎる
  - /score temp1 0
  - /score temp2 0
  - /score temp3 0
  # 大量の一時変数を使用
```

## 🎨 コードスタイル

### インデントと整理

**読みやすいレイアウト**
```yaml
# ✅ 整理された構造
organized_structure:
  # === メインメニュー ===
  main_menu:
    - /color \6\l
    - *** ゲームメニュー ***
    - /color \r
    - /? ゲーム開始 設定 終了
    - /?ゲーム開始 /jump start_game
    - /?設定 /jump settings_menu
    - /?終了 /jump exit_game

  # === ゲーム開始 ===
  start_game:
    - ゲームを開始します
    - /jump character_creation

  # === 設定メニュー ===
  settings_menu:
    - /color \b\l
    - --- 設定 ---
    - /color \r
    - /? 音量設定 表示設定 戻る
    - /?音量設定 /jump volume_settings
    - /?表示設定 /jump display_settings
    - /?戻る /jump main_menu

# ❌ 整理されていない
disorganized_structure:
  main_menu:
  - /color \6\l
  - *** ゲームメニュー ***
  - /color \r
  - /? ゲーム開始 設定 終了
  - /?ゲーム開始 /jump start_game
  volume_settings:
  - 音量設定
  start_game:
  - ゲームを開始します
  # セクションの順序がバラバラ、コメントなし
```

### コメント活用

**適切なドキュメント化**
```yaml
# ✅ 適切なコメント
well_commented:
  # プレイヤーの初回訪問をチェック
  shop_entrance:
    - /if first_visit = 0 /jump first_time_greeting
    - /jump regular_greeting

  # 初回来店時の特別な挨拶
  first_time_greeting:
    - /score first_visit 1          # 訪問フラグを設定
    - 初めてのご来店ですね！
    - /jump shop_tutorial

  # 常連客への挨拶
  regular_greeting:
    - いつもありがとうございます！
    - /jump shop_menu

# ❌ コメントなし/過剰
poor_comments:
  shop_entrance:                    # コメントなし
    - /if first_visit = 0 /jump first_time_greeting
    - /jump regular_greeting
    # ここで挨拶する        # 当たり前すぎるコメント
    # この行は挨拶です      # 無意味なコメント
    # TODO: 後で修正       # 未完成のまま
```

## 🔄 メンテナンス

### バージョン管理

**変更履歴の管理**
```yaml
# ファイル先頭にバージョン情報を記載
# shop_system.yml
# Version: 1.2.0
# Last Updated: 2024-01-15
# Changes: 
#   - 会員システム追加
#   - 価格調整
#   - バグ修正（在庫チェック）

# === ショップシステム ===
shop_entrance:
  # ...
```

### テスト可能性

**デバッグしやすい構造**
```yaml
# ✅ テストしやすい設計
testable_design:
  # デバッグ用セクション
  debug_shop:
    - === デバッグ情報 ===
    - プレイヤー: %player%
    - 所持金: \\money\\
    - 会員ランク: \\member_rank\\
    - 購入回数: \\purchase_count\\
    - /jump shop_entrance

  # テスト用の初期化
  test_setup:
    - /score money 1000
    - /score member_rank 1
    - /score purchase_count 0
    - テスト環境を初期化しました
    - /jump debug_shop

# ❌ テストしにくい
hard_to_test:
  complex_process:
    - /score money - 100
    - /score purchase_count + 1
    - /if member_rank = 1 & purchase_count >= 5 /score member_rank 2
    - /if member_rank = 2 & purchase_count >= 15 /score member_rank 3
    # 一つのセクションで複数の処理、テストが困難
```

## 📋 チェックリスト

### 開発時チェック

**コード品質確認**
- [ ] セクション名が分かりやすいか
- [ ] 変数名が一貫しているか
- [ ] エラーハンドリングが適切か
- [ ] 不要な重複がないか
- [ ] コメントが適切に書かれているか

### リリース前チェック

**品質保証**
- [ ] 全ての分岐をテストしたか
- [ ] エラー状況での動作を確認したか
- [ ] パフォーマンスに問題ないか
- [ ] プレイヤー体験が良好か
- [ ] ドキュメントが更新されているか

### メンテナンス時チェック

**継続的改善**
- [ ] 新機能が既存機能と矛盾しないか
- [ ] 変更による影響範囲を把握しているか
- [ ] バックアップを取っているか
- [ ] 変更履歴を記録しているか

## 🔗 関連ドキュメント

- **[制限事項](limitations.md)** - 避けるべき実装方法
- **[トラブルシューティング](../troubleshooting/README.md)** - 問題解決方法
- **[実用例](../examples/README.md)** - ベストプラクティスの実装例

これらのベストプラクティスに従うことで、保守しやすく、拡張しやすく、プレイヤーにとって使いやすいRPGTextスクリプトを作成できます！