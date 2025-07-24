# 制限事項

RPGTextの制限事項と回避方法をまとめています。

## ⚠️ 構文・コマンドの制限

### 入れ子条件の制限

**制限内容:** 条件コマンド（`/if`, `/has`）の入れ子はサポートされていません。

```yaml
# ❌ 動作しない例
nested_conditions:
  - /if level > 10 /has DIAMOND 1 none 特別な処理
  - /if money > 100 /if weapon_equipped = 1 攻撃力アップ
  - /has SWORD 1 none /has SHIELD 1 none 完全装備
```

**回避方法1:** `&`演算子を使用
```yaml
# ✅ 解決策
and_conditions:
  - /if level > 10 & diamond_count >= 1 特別な処理
  - /if money > 100 & weapon_equipped = 1 攻撃力アップ
```

**回避方法2:** `/jump`で分割
```yaml
# ✅ 解決策
split_conditions:
  - /if level > 10 /jump check_diamond
  
check_diamond:
  - /has DIAMOND 1 none 特別な処理
  - 条件を満たしていません

conditional_attack:
  - /if money > 100 /jump check_weapon
  - お金が足りません

check_weapon:
  - /if weapon_equipped = 1 攻撃力アップ
  - 武器を装備してください
```

### OR条件の制限

**制限内容:** OR（または）条件は直接サポートされていません。

```yaml
# ❌ 直接のOR条件はできない
# /if weapon = 1 OR armor = 1 装備している
```

**回避方法:** 個別チェック
```yaml
# ✅ 解決策
or_simulation:
  - /if weapon = 1 /jump equipped
  - /if armor = 1 /jump equipped
  - /if accessory = 1 /jump equipped
  - 何も装備していません
  - /jump end_check

equipped:
  - 何かを装備していますね

end_check:
  - 装備チェック完了
```

### 複数条件コマンドの制限

**制限内容:** 一つのコマンド内で複数の異なる条件系コマンドは使用できません。

```yaml
# ❌ 動作しない例
multiple_command_conditions:
  - /if level > 5 /has SWORD 1 none /command give %player% diamond
```

**回避方法:** 段階的チェック
```yaml
# ✅ 解決策
staged_checks:
  - /if level > 5 /jump sword_check
  - レベルが足りません

sword_check:
  - /has SWORD 1 none /jump give_reward
  - 剣が必要です

give_reward:
  - /command give %player% diamond
  - ダイヤモンドを獲得！
```

## 📝 テキスト・表示の制限

### 文字数制限

**制限内容:** 一行のメッセージには文字数制限があります。

- **推奨:** 50文字以内
- **最大:** 約100文字（環境による）
- **超過時:** 表示が崩れる可能性

```yaml
# ❌ 長すぎる文章
too_long:
  - これは非常に長いメッセージでプレイヤーにとって読みにくくなってしまう可能性があり画面からはみ出してしまうかもしれません

# ✅ 適切な長さ
appropriate_length:
  - これは適切な長さのメッセージです
  - 長い内容は複数行に分けましょう
  - 読みやすさを重視しています
```

### 特殊文字の制限

**制限内容:** 一部の特殊文字は正しく表示されない場合があります。

```yaml
# ⚠️ 注意が必要な文字
special_chars:
  - 絵文字: 😀（環境により表示されない）
  - 記号: ★☆♦♠（一部制限）
  - 特殊: ""''（引用符は問題ない場合が多い）

# ✅ 安全な代替案
safe_alternatives:
  - 絵文字の代替: :) ^_^ (^^)
  - 記号の代替: * + - = 
  - 装飾: /color \6\l で色付け
```

### 改行・フォーマットの制限

**制限内容:** 手動改行や複雑なフォーマットは制限されています。

```yaml
# ❌ 手動改行はできない
manual_breaks:
  - 一行目\n二行目  # \nは機能しない

# ✅ リスト形式で分割
proper_formatting:
  - 一行目
  - 二行目
  - 三行目
```

## 🔢 変数・データの制限

### 変数名の制限

**制限内容:** 変数名には命名規則があります。

```yaml
# ❌ 使用できない変数名
invalid_names:
  - /score プレイヤー名 10        # 日本語
  - /score player name 10        # スペース
  - /score player-level 10       # ハイフン
  - /score 123variable 10        # 数字開始

# ✅ 正しい変数名
valid_names:
  - /score player_name 10        # アンダースコア
  - /score playerLevel 10        # キャメルケース
  - /score hp 10                 # 短縮形
  - /score quest_001 10          # 数字は途中・末尾OK
```

### 数値の制限

**制限内容:** 扱える数値には範囲制限があります。

- **整数範囲:** -2,147,483,648 〜 2,147,483,647
- **小数点:** サポートされていません

```yaml
# ❌ 制限を超える値
out_of_range:
  - /score big_number 999999999999999999  # 大きすぎる
  - /score decimal 10.5                   # 小数点

# ✅ 適切な値
appropriate_values:
  - /score money 1000000                  # 範囲内の整数
  - /score percentage 85                  # パーセントは整数で
```

### 変数スコープの制限

**制限内容:** 変数はプレイヤー個別で、ファイル間で共有されます。

```yaml
# 注意：変数はプレイヤー毎に管理される
variable_scope:
  - /score money 100  # プレイヤーAのmoney
  # プレイヤーBのmoneyは別の値

# 注意：ファイル間で変数は共有される
# fileA.yml
- /score global_var 10

# fileB.yml  
- global_varは10が設定済み: \\global_var\\
```

## 🎮 ゲーム機能の制限

### Minecraftコマンドの制限

**制限内容:** 一部のMinecraftコマンドは実行できません。

```yaml
# ❌ 危険・制限されるコマンド
restricted_commands:
  - /command stop                    # サーバー停止
  - /command op %player%             # 権限付与
  - /command ban %player%            # プレイヤーBAN
  - /command /say                    # 二重スラッシュ

# ✅ 安全なコマンド
safe_commands:
  - /command give %player% diamond   # アイテム付与
  - /command tp %player% 100 64 200  # テレポート
  - /command title %player% title {"text":"Hello"}  # タイトル表示
```

### プレイヤーアクションの制限

**制限内容:** プレイヤーの行動を完全に制御することはできません。

```yaml
# 制限があること
player_limitations:
  - /freeze true    # 移動制限（一時的）
  # ただし、ログアウトやコマンド入力は制限できない
  
  - /skip false     # スキップ制限
  # ただし、強制終了などは防げない
```

## 📱 環境・互換性の制限

### プラットフォーム制限

**制限内容:** 一部機能は特定環境でのみ動作します。

- **Java版:** 全機能利用可能
- **統合版:** 一部のコマンドや表示に制限
- **サーバー:** 管理者設定により機能制限の可能性

### パフォーマンス制限

**制限内容:** 大量のコマンドや複雑な処理は負荷がかかります。

```yaml
# ❌ パフォーマンスに問題
performance_issues:
  - /wait 1    # 短すぎる待機時間
  - /wait 1
  - /wait 1    # 大量の短時間待機
  # ... 100回繰り返し

# ✅ 効率的な実装
efficient_implementation:
  - /wait 100  # まとめて待機
  - 必要な処理のみ実行
```

## 🔧 回避方法・代替案

### 複雑な条件の実装

```yaml
# 複雑な条件をシンプルに分解
complex_condition_workaround:
  - /if condition1 = 1 /jump check_condition2
  - 条件1が満たされていません
  - /jump end

check_condition2:
  - /if condition2 = 1 /jump check_condition3
  - 条件2が満たされていません
  - /jump end

check_condition3:
  - /if condition3 = 1 /jump all_conditions_met
  - 条件3が満たされていません
  - /jump end

all_conditions_met:
  - 全ての条件が満たされました！

end:
  - 処理終了
```

### 大きな数値の扱い

```yaml
# 大きな数値は分割して管理
large_number_handling:
  - /score gold_thousands 1000      # 千の位
  - /score gold_ones 500            # 一の位
  - 所持金: \\gold_thousands\\,\\gold_ones\\ゴールド
  # 結果: 所持金: 1000,500ゴールド
```

### OR条件の実装

```yaml
# フラグを使ったOR条件
or_condition_implementation:
  - /score condition_met 0
  - /if weapon = 1 /score condition_met 1
  - /if armor = 1 /score condition_met 1
  - /if accessory = 1 /score condition_met 1
  - /if condition_met = 1 何かを装備しています
  - /if condition_met = 0 何も装備していません
```

## 📋 制限チェックリスト

開発時に確認すべき制限事項：

### コマンド関連
- [ ] 入れ子条件を使用していないか
- [ ] 複数の条件コマンドを組み合わせていないか
- [ ] 危険なMinecraftコマンドを使用していないか

### テキスト関連
- [ ] 一行のメッセージが長すぎないか
- [ ] 特殊文字が正しく表示されるか
- [ ] 色コードのリセットを忘れていないか

### 変数関連
- [ ] 変数名が命名規則に従っているか
- [ ] 数値が範囲内に収まっているか
- [ ] 変数の初期化を行っているか

### パフォーマンス関連
- [ ] 不要な短時間待機を避けているか
- [ ] 複雑すぎる処理になっていないか
- [ ] プレイヤーの体験を阻害していないか

## 🔗 関連ドキュメント

- **[ベストプラクティス](best-practices.md)** - 制限を踏まえた推奨実装
- **[トラブルシューティング](../troubleshooting/README.md)** - 問題解決の手順
- **[コマンドリファレンス](../commands/README.md)** - 各コマンドの詳細仕様

これらの制限事項を理解することで、安定して動作するRPGTextスクリプトを作成できます！