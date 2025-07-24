# 特殊変数

RPGTextで使用できる特殊変数の詳細仕様です。

## 📊 プレイヤー情報変数

RPGTextでは`%変数名%`の形式でプレイヤーの情報を動的に表示できます。

### 🏷️ 基本情報

#### `%player%` - プレイヤー名
```yaml
examples:
  - こんにちは、%player%さん！
  - %player%の冒険が始まります
  - %player%様、いらっしゃいませ
```

**出力例:**
- `こんにちは、Steve さん！`
- `Steve の冒険が始まります`
- `Steve 様、いらっしゃいませ`

**注意事項:**
- 日本語環境では英数字のプレイヤー名が推奨
- スペースや特殊文字を含む名前も表示可能

#### `%player\r%` - プレイヤー名（色リセット付き）
```yaml
colored_name:
  - /color \6
  - ようこそ、%player\r%さん！
  - /color \a
  - %player\r%の物語
```

**用途:**
- 色付きテキスト内でプレイヤー名だけ通常色にしたい場合
- テキスト装飾との組み合わせ

### 📈 ステータス情報

#### `%level%` - プレイヤーレベル
```yaml
level_display:
  - 現在のレベル: %level%
  - レベル%level%の冒険者ですね
  - /if level > 10 レベル%level%！上級者ですね
```

**値の範囲:** 0 〜 2,147,483,647（Minecraftの仕様）

**活用例:**
```yaml
level_based_greeting:
  - /if level = 1 初心者の%player%さん
  - /if level >= 10 & level < 30 中級者の%player%さん
  - /if level >= 30 熟練者の%player%さん
```

#### `%hp%` - 現在体力
```yaml
health_display:
  - 現在の体力: %hp%
  - HPが%hp%まで減っています
  - 体力%hp%で大丈夫ですか？
```

**値の範囲:** 0 〜 最大体力値
**表示形式:** 整数（小数点以下切り捨て）

**実用例:**
```yaml
health_check:
  - 現在HP: %hp%
  - /if hp < 5 /color \c
  - /if hp < 5 ⚠️ 体力が危険な状態です！
  - /if hp < 5 /color \r
  - /if hp >= 10 体力は十分ですね
```

#### `%food%` - 満腹度
```yaml
hunger_display:
  - 満腹度: %food%
  - お腹の減り具合: %food%/20
  - /if food < 10 お腹が空いていますね
```

**値の範囲:** 0 〜 20
**意味:**
- 20: 満腹
- 18-19: ほぼ満腹
- 6-17: 普通
- 1-5: 空腹
- 0: 餓死寸前

#### `%gamemode%` - ゲームモード
```yaml
gamemode_display:
  - 現在のモード: %gamemode%
  - %gamemode%モードでプレイ中
```

**表示値:**
- `SURVIVAL` - サバイバルモード
- `CREATIVE` - クリエイティブモード
- `ADVENTURE` - アドベンチャーモード
- `SPECTATOR` - スペクテイターモード

**活用例:**
```yaml
mode_specific_message:
  - ゲームモード: %gamemode%
  - /if gamemode = CREATIVE クリエイティブモードですね
  - /if gamemode = SURVIVAL サバイバルで頑張って！
```

### 🎯 カスタム変数

#### `\\変数名\\` - スコア変数表示
```yaml
custom_variables:
  - 所持金: \\money\\ゴールド
  - 経験値: \\exp\\
  - レベル: \\player_level\\
  - 好感度: \\friendship\\
```

**使用方法:**
1. `/score`コマンドで変数を設定
2. `\\変数名\\`で表示

```yaml
variable_example:
  - /score money 1000
  - /score exp 250
  - 所持金: \\money\\G, 経験値: \\exp\\
```

**出力:** `所持金: 1000G, 経験値: 250`

## 💡 実用的な組み合わせ

### ステータス表示システム
```yaml
full_status:
  - === %player%のステータス ===
  - レベル: %level%
  - 体力: %hp%
  - 満腹度: %food%/20
  - ゲームモード: %gamemode%
  - 所持金: \\money\\ゴールド
  - 経験値: \\exp\\
```

### 個人化されたメッセージ
```yaml
personalized_greeting:
  - /color \6\l
  - %player%さん、おかえりなさい！
  - /color \r
  - レベル%level%での冒険はいかがですか？
  - 体力%hp%、満腹度%food%ですね。
  - /if hp < 10 少し休憩されてはいかがでしょう？
  - /if food < 10 何か食べ物はいかがですか？
```

### 条件付き表示
```yaml
conditional_status:
  - プレイヤー: %player%
  - /if level >= 50 【上級者】レベル%level%
  - /if level < 50 レベル%level%
  - /if hp <= 5 【危険】体力%hp%
  - /if hp > 5 体力%hp%
  - モード: %gamemode%
```

### ゲーム進行との連携
```yaml
story_integration:
  - %player%は洞窟の奥へ進んだ
  - 現在レベル%level%の%player%にとって
  - この洞窟は危険かもしれない...
  - /if level >= 20 しかし、経験豊富な%player%なら大丈夫だろう
  - /if level < 20 注意深く進む必要がある
```

## ⚠️ 注意事項とベストプラクティス

### 変数名の制限
```yaml
# ✅ 正しい使用
- %player%
- %level%
- \\money\\
- \\quest_status\\

# ❌ 間違った使用
- %プレイヤー%        # 日本語変数名は不可
- %player name%      # スペースは不可
- \\money G\\        # スペースは不可
- %level%\\exp\\     # 混在はできない
```

### パフォーマンスの考慮
```yaml
# ✅ 効率的
efficient_display:
  - ステータス: %player% Lv.%level% HP:%hp%

# ❌ 非効率的
inefficient_display:
  - プレイヤー名: %player%
  - プレイヤー名: %player%  # 同じ変数の重複表示
  - プレイヤー名: %player%
```

### 更新タイミング
- **リアルタイム更新:** `%hp%`, `%food%`は常に最新値
- **セッション固定:** `%player%`, `%gamemode%`はセッション開始時の値
- **変動:** `%level%`は経験値変動時に更新

### エラーハンドリング
```yaml
safe_variable_use:
  - /if hp > 0 現在HP: %hp%
  - /if level >= 1 レベル: %level%
  - /if money >= 0 所持金: \\money\\G
  - /if money < 0 所持金情報なし
```

## 🔗 関連ドキュメント

- **[変数・スコア管理](../commands/variables.md)** - カスタム変数の設定
- **[条件分岐](../commands/conditions.md)** - 変数を使った条件判定
- **[基本設定](../commands/basic-settings.md)** - 色コードとの組み合わせ
- **[実用例 - 基本的な会話](../examples/basic-conversation.md)** - 特殊変数の活用例

## 💡 応用テクニック

### 動的な難易度調整
```yaml
dynamic_difficulty:
  - /if level < 10 初心者向けクエスト
  - /if level >= 10 & level < 30 中級者向けクエスト
  - /if level >= 30 上級者向けクエスト
  - %player%さん（Lv.%level%）にぴったりですね
```

### 体力に応じた演出
```yaml
health_based_narrative:
  - /if hp > 15 %player%は元気に歩いている
  - /if hp <= 15 & hp > 5 %player%は少し疲れている
  - /if hp <= 5 %player%はふらつきながら歩いている
```

### カスタム変数との統合
```yaml
integrated_display:
  - === %player%の冒険記録 ===
  - 基本情報: Lv.%level% HP:%hp%
  - 進行状況: \\chapter\\章 \\quest_complete\\個完了
  - 戦績: 勝利\\wins\\回 敗北\\losses\\回
  - 財産: \\money\\G \\items_collected\\種類収集
```

これらの特殊変数を効果的に活用することで、プレイヤーに個人化された体験を提供できます！