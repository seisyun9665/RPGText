# よくあるエラー

RPGTextでよく遭遇するエラーとその解決方法です。

## 🚨 実行エラー

### エラー: "ファイルが見つかりません"

**症状:**
```
/rpgtext config Player story.yml/intro
Error: File not found: story.yml
```

**原因:**
- ファイルが正しい場所に配置されていない
- ファイル名が間違っている
- ファイル拡張子が間違っている

**解決方法:**
```yaml
# 1. ファイルの場所を確認
plugins/RPGText/messages/story.yml

# 2. ファイル名・拡張子を確認
story.yml (正しい)
story.yaml (間違い)
story.txt (間違い)

# 3. パスの確認
/rpgtext config Player story.yml/intro (正しい)
/rpgtext config Player story/intro (間違い - .yml忘れ)
```

### エラー: "セクションが見つかりません"

**症状:**
```
/rpgtext config Player story.yml/opening
Error: Section 'opening' not found in story.yml
```

**原因:**
- セクション名が間違っている
- インデントが間違っている
- セクションが定義されていない

**解決方法:**
```yaml
# ✅ 正しいセクション定義
story.yml:
  intro:  # セクション名（インデントなし）
    - メッセージ1  # メッセージ（2スペースインデント）
    - メッセージ2

  opening:  # 別のセクション
    - オープニング

# ❌ 間違った例
story.yml:
    intro:  # インデントが間違い
  - メッセージ1  # インデントが間違い
```

### エラー: "プレイヤーが見つかりません"

**症状:**
```
/rpgtext config UnknownPlayer story.yml/intro
Error: Player 'UnknownPlayer' not found
```

**原因:**
- プレイヤー名が間違っている
- プレイヤーがオフライン
- 大文字小文字の違い

**解決方法:**
```yaml
# 1. 正確なプレイヤー名を確認
/list  # オンラインプレイヤー一覧表示

# 2. 大文字小文字を正確に
Steve (正しい)
steve (間違いの可能性)

# 3. プレイヤーがオンラインか確認
```

## 📝 構文エラー

### エラー: "YAML構文エラー"

**症状:**
```
Error: Invalid YAML syntax in story.yml
```

**原因:**
- インデントが間違っている
- 特殊文字の使用
- クォートの問題

**解決方法:**
```yaml
# ✅ 正しいYAML構文
section_name:
  - "メッセージに特殊文字: を含む場合"
  - メッセージ2
  - /command example

# ❌ 間違った例
section_name:
- メッセージ1  # インデント不足
  - メッセージ2  # インデント不一致
  - メッセージに:特殊文字  # クォートなしで:使用
```

### エラー: "無効なコマンド"

**症状:**
```
Error: Invalid command '/invalidcommand'
```

**原因:**
- 存在しないコマンドを使用
- コマンド名のタイプミス
- パラメータの不足

**解決方法:**
```yaml
# ✅ 正しいコマンド
- /sound block.note.bell 1 1
- /speed 30
- /color \a
- /jump section_name

# ❌ 間違った例
- /sond block.note.bell 1 1  # タイプミス
- /speed  # パラメータ不足
- /colour \a  # 間違ったスペル
- /goto section_name  # 存在しないコマンド
```

### エラー: "パラメータが不正"

**症状:**
```
Error: Invalid parameter for /sound command
```

**原因:**
- 音声名が間違っている
- 数値の範囲外
- パラメータの型が違う

**解決方法:**
```yaml
# ✅ 正しいパラメータ
- /sound minecraft:block.note.bell 1.0 1.0
- /speed 30
- /wait 20

# ❌ 間違った例
- /sound invalid_sound 1 1  # 存在しない音声
- /speed -5  # 負の値
- /wait 1.5  # 小数点（整数のみ）
```

## 🔄 動作エラー

### エラー: "無限ループ"

**症状:**
- スクリプトが停止しない
- サーバーが重くなる
- プレイヤーが操作できない

**原因:**
- セクション間の循環参照
- 終了条件のない繰り返し

**解決方法:**
```yaml
# ❌ 無限ループの例
section_a:
  - /jump section_b

section_b:
  - /jump section_a  # 永続ループ

# ✅ 正しい実装
section_a:
  - /add loop_count
  - /if loop_count > 10 /jump end_loop
  - /jump section_b

section_b:
  - /jump section_a

end_loop:
  - ループを終了します
```

### エラー: "変数が更新されない"

**症状:**
- `/score`コマンドが効果がない
- 変数の値が変わらない

**原因:**
- 変数名の間違い
- 計算コマンドの構文エラー
- スコープの問題

**解決方法:**
```yaml
# ✅ 正しい変数操作
- /score money 100      # 設定
- /score money + 50     # 加算
- /add money           # 1増加
- 所持金: \\money\\      # 表示

# ❌ 間違った例
- /score money = 100    # =は不要
- /score money +50     # スペース必要
- /add money 1         # addに数値は不要
- 所持金: %money%      # 間違った表示形式
```

### エラー: "選択肢が機能しない"

**症状:**
- `/？`で選択肢が表示されない
- `/?選択肢`で分岐しない

**原因:**
- 全角文字の使用
- 選択肢名の不一致
- 構文の間違い

**解決方法:**
```yaml
# ✅ 正しい選択肢
- /? はい いいえ         # 半角?
- /?はい 良い選択です      # 選択肢名完全一致
- /?いいえ 残念です

# ❌ 間違った例
- /？ はい いいえ        # 全角？
- /?yes 良い選択です     # 選択肢名不一致（はい≠yes）
- /? はい、いいえ        # カンマは選択肢の一部になる
```

## 🎨 表示エラー

### エラー: "文字化け"

**症状:**
- 日本語が正しく表示されない
- 文字が□や?で表示される

**原因:**
- ファイルエンコーディングの問題
- クライアント設定の問題

**解決方法:**
```yaml
# 1. ファイル保存時にUTF-8を指定
# 2. BOM（Byte Order Mark）なしで保存
# 3. エディタの設定確認

# テスト用メッセージ
test_japanese:
  - 日本語テストメッセージ
  - ひらがな、カタカナ、漢字
  - 特殊文字: ♦♠♣♥
```

### エラー: "色が適用されない"

**症状:**
- `/color`コマンドが効果がない
- 文字色が変わらない

**原因:**
- カラーコードの間違い
- リセットし忘れ
- クライアント設定

**解決方法:**
```yaml
# ✅ 正しい色コード
- /color \a        # 明るい緑
- このテキストは緑色です
- /color \r        # リセット
- 通常の色に戻りました

# ❌ 間違った例
- /color a         # \が抜けている
- /color &a        # Minecraftの&記法は使用不可
- /color green     # 英語名は使用不可
```

### エラー: "音が再生されない"

**症状:**
- `/sound`や`/singlesound`で音が出ない

**原因:**
- 音声名の間違い
- クライアントの音量設定
- サーバー設定

**解決方法:**
```yaml
# ✅ 確実に動く音声
- /singlesound minecraft:entity.experience_orb.pickup 1 1
- /sound minecraft:block.note.bell 1 1

# ❌ 間違いやすい例
- /sound experience_orb.pickup 1 1  # minecraftプレフィックス不足
- /sound block.note.bell 0 1       # 音量0（聞こえない）

# デバッグ用
sound_test:
  - 音声テスト開始
  - /singlesound minecraft:entity.player.levelup 1 1
  - レベルアップ音が聞こえましたか？
```

## 🔧 設定エラー

### エラー: "権限がありません"

**症状:**
```
Error: You don't have permission to use this command
```

**原因:**
- プラグイン権限の設定不足
- サーバー管理者権限が必要

**解決方法:**
```yaml
# 1. 権限の確認・設定（サーバー管理者が実行）
/lp user <プレイヤー名> permission set rpgtext.use true
/lp user <プレイヤー名> permission set rpgtext.admin true

# 2. 権限プラグインの設定確認
# permissions.yml または LuckPerms等の設定

# 3. OPプレイヤーでテスト
/op <プレイヤー名>
```

### エラー: "設定ファイルエラー"

**症状:**
```
Error: Could not load config.yml
```

**原因:**
- config.ymlの構文エラー
- ファイルが破損している

**解決方法:**
```yaml
# 1. バックアップから復元
plugins/RPGText/config.yml.bak → config.yml

# 2. デフォルト設定を再生成
# config.ymlを削除してサーバー再起動

# 3. 手動修正（基本的な設定例）
# config.yml
plugin:
  enabled: true
  language: ja
  auto_save: true
```

## 🔍 デバッグのコツ

### 段階的テスト

```yaml
# 1. 最小構成でテスト
minimal_test:
  - テスト成功

# 2. 基本機能追加
basic_test:
  - /color \a
  - 緑色のテスト
  - /color \r

# 3. 変数テスト
variable_test:
  - /score test_var 123
  - 変数テスト: \\test_var\\

# 4. 選択肢テスト
choice_test:
  - /? テスト1 テスト2
  - /?テスト1 選択1が選ばれました
  - /?テスト2 選択2が選ばれました
```

### ログ確認方法

1. **サーバーコンソールの確認**
   - リアルタイムでエラーメッセージを確認
   - 警告やデバッグ情報もチェック

2. **ログファイルの確認**
   ```
   logs/latest.log
   logs/yyyy-mm-dd-1.log.gz
   ```

3. **プラグイン固有ログ**
   ```
   plugins/RPGText/logs/
   ```

## 📞 サポート情報の収集

エラー報告時に必要な情報：

### 基本情報
- **サーバーバージョン**: /version の結果
- **RPGTextバージョン**: プラグイン情報
- **Java版/統合版**: プラットフォーム情報

### エラー情報
- **エラーメッセージ**: 完全なメッセージ
- **実行コマンド**: 使用したコマンド
- **ファイル内容**: 問題のあるセクション

### 環境情報
- **他のプラグイン**: 併用しているプラグイン
- **権限設定**: 権限プラグインの設定
- **サーバー設定**: server.properties の関連設定

## 🔗 関連ドキュメント

- **[デバッグ方法](debugging-tips.md)** - より詳細なデバッグ手順
- **[制限事項](../reference/limitations.md)** - できないことの確認
- **[ベストプラクティス](../reference/best-practices.md)** - エラーを避ける方法

## 💡 予防のポイント

エラーを減らすための心がけ：

1. **小さく始める** - 複雑なものをいきなり作らない
2. **こまめにテスト** - 機能追加の度にテスト
3. **バックアップ** - 動作するバージョンを保存
4. **ドキュメント参照** - 分からないことは調べる
5. **コミュニティ活用** - 他のユーザーの事例を参考に

これらのエラーパターンを覚えておくと、問題が発生した時に素早く解決できるようになります！