# デバッグ方法

RPGTextスクリプトの問題を効率的に特定・解決するためのデバッグテクニックです。

## 🔍 基本的なデバッグアプローチ

### 1. 問題の分離

**大きな問題を小さく分解**
```yaml
# ❌ 大きすぎて問題箇所の特定が困難
complex_system:
  - /score money + 100
  - /if money > 500 /jump rich_player
  - /? 購入する アップグレード 戻る
  - /?購入する /jump purchase_flow
  - /?アップグレード /has DIAMOND 1 none /jump upgrade_available
  # 多くの機能が混在

# ✅ 機能を分離してテスト
test_money:
  - /score money + 100
  - 所持金: \\money\\

test_conditions:
  - /if money > 500 お金持ちです
  - /if money <= 500 まだまだです

test_choices:
  - /? 選択肢1 選択肢2
  - /?選択肢1 1が選ばれました
  - /?選択肢2 2が選ばれました
```

### 2. バイナリサーチ法

**問題箇所を半分ずつ絞り込む**
```yaml
# 長いスクリプトで問題が発生している場合
long_script:
  - 行1    # この辺りで問題？
  - 行2
  - 行3
  - 行4
  - 行5    # それともこの辺り？
  - 行6
  - 行7
  - 行8
  - 行9
  - 行10   # もしくはこの辺り？

# 手順1: 中間をコメントアウト
long_script:
  - 行1
  - 行2
  - 行3
  - 行4
  - 行5
  # - 行6    # コメントアウト
  # - 行7
  # - 行8
  # - 行9
  # - 行10

# 動作確認 → 前半に問題があれば更に半分に分割
```

## 🛠️ デバッグツール

### デバッグ用セクション

```yaml
# debug.yml - デバッグ専用ファイル
debug_variables:
  - === 変数デバッグ ===
  - プレイヤー: %player%
  - レベル: %level%
  - HP: %hp%
  - 所持金: \\money\\
  - クエスト状態: \\quest_status\\
  - ループカウント: \\loop_count\\
  - === デバッグ終了 ===

debug_flow:
  - デバッグポイント1通過
  - /jump debug_variables

debug_conditions:
  - 条件テスト開始
  - /if money > 100 金額チェック: OK
  - /if money <= 100 金額チェック: NG
  - /if level >= 10 レベルチェック: OK
  - /if level < 10 レベルチェック: NG
```

### ログ出力システム

```yaml
# ログ機能の実装
log_system:
  - /score debug_step + 1
  - [LOG] ステップ\\debug_step\\: セクション開始
  - /jump actual_process

log_entry:
  - /add debug_counter
  - [DEBUG \\debug_counter\\] 処理実行中...

log_variable:
  - [VAR] money=\\money\\, level=\\level\\, hp=\\hp\\
```

### トレース機能

```yaml
# 実行経路の追跡
trace_start:
  - === トレース開始 ===
  - /score trace_level 0
  - /jump main_process

trace_enter:
  - /add trace_level
  - [ENTER \\trace_level\\] セクション: main_process

trace_exit:
  - [EXIT \\trace_level\\] セクション: main_process
  - /score trace_level - 1
```

## 🔍 具体的なデバッグ手法

### 変数状態の確認

```yaml
# 変数の値を段階的に確認
variable_debug:
  - 初期値確認
  - money: \\money\\, exp: \\exp\\
  - /score money + 50
  - 加算後確認
  - money: \\money\\, exp: \\exp\\
  - /score exp * 2
  - 乗算後確認
  - money: \\money\\, exp: \\exp\\
```

### 条件分岐のテスト

```yaml
# 条件の詳細確認
condition_debug:
  - 条件テスト: money > 100
  - 現在のmoney: \\money\\
  - /if money > 100 条件TRUE: 処理実行
  - /if money <= 100 条件FALSE: 処理スキップ
  - /score test_result 0
  - /if money > 100 /score test_result 1
  - 結果: \\test_result\\ (1=TRUE, 0=FALSE)
```

### 選択肢の確認

```yaml
# 選択肢の動作確認
choice_debug:
  - 選択肢テスト開始
  - /? デバッグA デバッグB デバッグC
  - /?デバッグA [選択] Aが選ばれました
  - /?デバッグB [選択] Bが選ばれました
  - /?デバッグC [選択] Cが選ばれました
  - /jump choice_debug_end

choice_debug_end:
  - 選択肢テスト完了
```

### ファイル間移動の確認

```yaml
# file1.yml
jump_test_source:
  - ファイル1からファイル2へジャンプ
  - /jump file2.yml/jump_test_target

# file2.yml
jump_test_target:
  - ファイル2に到着しました
  - /jump file1.yml/jump_test_return

# file1.yml
jump_test_return:
  - ファイル1に戻りました
```

## 🎯 問題パターン別デバッグ

### パターン1: スクリプトが途中で止まる

**デバッグ手順:**
```yaml
# 1. チェックポイントを設置
checkpoint_debug:
  - チェックポイント1
  - /singlesound minecraft:block.note.bell 1 1
  - /jump suspected_problem_area

suspected_problem_area:
  - チェックポイント2
  - /singlesound minecraft:block.note.bell 1 1.5
  - # 問題のありそうなコード
  - /jump after_problem

after_problem:
  - チェックポイント3
  - /singlesound minecraft:block.note.bell 1 2
```

**分析:**
- どのチェックポイントまで到達するかで問題箇所を特定
- 音声で進行状況を確認

### パターン2: 変数が期待値にならない

**デバッグ手順:**
```yaml
variable_tracking:
  - 変数追跡開始
  - 初期値: \\target_var\\
  - /score target_var + 10
  - +10後: \\target_var\\
  - /score target_var * 2
  - *2後: \\target_var\\
  - /if target_var > 50 期待値に到達
  - /if target_var <= 50 期待値未到達: \\target_var\\
```

### パターン3: 条件分岐が正しく動作しない

**デバッグ手順:**
```yaml
condition_analysis:
  - 条件分析開始
  - 変数A: \\var_a\\
  - 変数B: \\var_b\\
  - /if var_a > var_b A > B です
  - /if var_a = var_b A = B です
  - /if var_a < var_b A < B です
  - /score comparison_result 0
  - /if var_a > var_b /score comparison_result 1
  - /if var_a = var_b /score comparison_result 2
  - /if var_a < var_b /score comparison_result 3
  - 比較結果: \\comparison_result\\ (1:大, 2:同, 3:小)
```

### パターン4: ファイル間移動でエラー

**デバッグ手順:**
```yaml
# 1. ファイルの存在確認
file_test:
  - 現在のファイル: main.yml
  - /jump test_target.yml/test_section

# test_target.yml
test_section:
  - test_target.ymlに到達
  - /jump main.yml/file_test_return

# main.yml
file_test_return:
  - main.ymlに戻りました
```

## 📊 デバッグ情報の収集

### システム情報の取得

```yaml
system_info:
  - === システム情報 ===
  - プレイヤー: %player%
  - レベル: %level%
  - 体力: %hp%
  - 満腹度: %food%
  - ゲームモード: %gamemode%
  - === カスタム変数 ===
  - 所持金: \\money\\
  - 経験値: \\exp\\
  - クエスト進行: \\quest_progress\\
```

### 実行環境の確認

```yaml
environment_check:
  - 環境チェック開始
  - /singlesound minecraft:entity.experience_orb.pickup 1 1
  - 音声: OK
  - /color \a
  - 色表示: OK
  - /color \r
  - /score env_test 123
  - 変数操作: \\env_test\\
  - /command title %player% subtitle {"text":"コマンド実行: OK"}
  - /wait 40
  - 環境チェック完了
```

## 🔧 高度なデバッグ技法

### 状態保存・復元

```yaml
# 状態保存
save_state:
  - /score saved_money \\money\\
  - /score saved_level \\level\\
  - /score saved_exp \\exp\\
  - 状態を保存しました

# 問題のあるコードをテスト
test_problematic_code:
  - 問題のあるコードを実行...
  # 問題のあるコード

# 状態復元
restore_state:
  - /score money \\saved_money\\
  - /score level \\saved_level\\
  - /score exp \\saved_exp\\
  - 状態を復元しました
```

### パフォーマンス測定

```yaml
performance_test:
  - パフォーマンステスト開始
  - /score start_time 0
  - /singlesound minecraft:block.note.bell 1 1
  - 開始時刻記録
  # テスト対象のコード
  - /singlesound minecraft:block.note.bell 1 2
  - 終了時刻記録
  - パフォーマンステスト完了
```

### メモリ使用量の監視

```yaml
memory_monitor:
  - === メモリ監視 ===
  - アクティブ変数数の確認
  - money: \\money\\
  - exp: \\exp\\
  - quest_vars: \\quest_1\\, \\quest_2\\, \\quest_3\\
  - temp_vars: \\temp_1\\, \\temp_2\\
  - 合計変数数: 7個
```

## 📋 デバッグチェックリスト

### 実行前チェック
- [ ] ファイルが正しい場所にある
- [ ] セクション名が正しい
- [ ] プレイヤー名が正しい
- [ ] 権限が設定されている

### 構文チェック
- [ ] YAMLフォーマットが正しい
- [ ] インデントが統一されている
- [ ] 特殊文字が適切にエスケープされている
- [ ] コマンド名にタイプミスがない

### ロジックチェック
- [ ] 変数の初期化が行われている
- [ ] 条件分岐が適切に設定されている
- [ ] 無限ループの可能性がない
- [ ] 全ての選択肢に対応がある

### パフォーマンスチェック
- [ ] 待機時間が適切
- [ ] 不要な処理がない
- [ ] 変数の使用量が適正
- [ ] ファイルサイズが適切

## 🔗 デバッグツールの活用

### 外部ツール

1. **YAMLバリデーター**
   - オンラインのYAML検証ツール
   - 構文エラーの事前チェック

2. **テキストエディタ**
   - シンタックスハイライト機能
   - インデント表示機能

3. **ログ解析ツール**
   - サーバーログの効率的な確認
   - エラーパターンの検索

### プラグイン連携

```yaml
# 他のプラグインとの連携確認
plugin_compatibility:
  - RPGText動作確認
  - /singlesound minecraft:entity.experience_orb.pickup 1 1
  - 音声プラグインとの競合チェック
  - /command title %player% title {"text":"Title Test"}
  - 他のメッセージプラグインとの競合チェック
```

## 💡 デバッグのベストプラクティス

### 効率的なデバッグ

1. **仮説を立てる** - 何が原因かを推測
2. **最小テストケース** - 問題を再現する最小構成
3. **段階的修正** - 一度に一つずつ修正
4. **記録を残す** - 試したことと結果を記録

### 予防的デバッグ

1. **コードレビュー** - 実装前にロジックを確認
2. **単体テスト** - 個別機能の動作確認
3. **統合テスト** - 全体の動作確認
4. **ストレステスト** - 負荷時の動作確認

デバッグは根気強さと論理的思考が重要です。一つずつ丁寧に確認していけば、必ず原因を特定できます！

## 🔗 関連ドキュメント

- **[よくあるエラー](common-errors.md)** - 典型的な問題と解決策
- **[制限事項](../reference/limitations.md)** - システムの制限を理解
- **[ベストプラクティス](../reference/best-practices.md)** - 問題を避ける実装方法