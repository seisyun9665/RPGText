# 初回スクリプト作成

実際にRPGTextスクリプトを作成して、動作を確認してみましょう。

## 🎯 作成するもの

簡単な挨拶と選択肢のあるインタラクティブなスクリプトを作成します。

## 📝 ステップ1: ファイル作成

`plugins/RPGText/messages/`フォルダに`my_first_script.yml`を作成します。

```yaml
# 私の最初のRPGTextスクリプト
sound: block.note.bell 1 1.5
speed: 30
color: \r

# メインの挨拶セクション
greeting:
  - /color \6\l
  - こんにちは！
  - /color \r
  - 私はRPGTextの案内人です。
  - あなたのお名前は%player%さんですね。
  - /wait 40
  - 今日の調子はいかがですか？
  - /? とても良い まあまあ あまり良くない
  - /?とても良い /jump good_response
  - /?まあまあ /jump okay_response  
  - /?あまり良くない /jump bad_response

# 良い調子の場合
good_response:
  - /color \a
  - それは素晴らしいですね！
  - /color \r
  - そのまま良い一日をお過ごしください。
  - /singlesound minecraft:entity.player.levelup
  - /jump farewell

# まあまあの場合
okay_response:
  - /color \e
  - そうですか。
  - /color \r
  - 何か楽しいことが見つかると良いですね。
  - /jump farewell

# 調子が悪い場合
bad_response:
  - /color \c
  - それは大変ですね...
  - /color \r
  - 無理せず、ゆっくり休んでくださいね。
  - 応援しています！
  - /singlesound minecraft:block.note.bass 1 0.5
  - /jump farewell

# お別れのセクション
farewell:
  - また会いましょう、%player%さん！
  - /wait 20
  - /color \6
  - RPGTextをお楽しみください！
  - /color \r
```

## 🎮 ステップ2: スクリプトの実行

ゲーム内で以下のコマンドを実行してスクリプトをテストします：

```
/rpgtext config <プレイヤー名> my_first_script.yml/greeting
```

例：
```
/rpgtext config Steve my_first_script.yml/greeting
```

## 📖 スクリプトの解説

### 使用している機能

1. **ファイル共通設定**
   ```yaml
   sound: block.note.bell 1 1.5
   speed: 30
   color: \r
   ```

2. **カラーコード**
   - `\6\l` - 金色＋太字
   - `\a` - 濃い緑
   - `\e` - 黄色
   - `\c` - 濃い赤
   - `\r` - リセット

3. **特殊変数**
   - `%player%` - プレイヤー名を表示

4. **待機**
   ```yaml
   - /wait 40
   ```
   40ティック（2秒）の待機

5. **選択肢**
   ```yaml
   - /? とても良い まあまあ あまり良くない
   ```

6. **分岐処理**
   ```yaml
   - /?とても良い /jump good_response
   ```

7. **音声再生**
   ```yaml
   - /singlesound minecraft:entity.player.levelup
   ```

## 🔄 ステップ3: カスタマイズしてみよう

### 選択肢を増やす

```yaml
- /? とても良い まあまあ あまり良くない 秘密
- /?秘密 /jump secret_response

secret_response:
  - /color \d
  - 秘密ですか...？
  - /color \r
  - 興味深いですね。
  - /jump farewell
```

### 音声を変更

```yaml
sound: block.note.flute 1 2
```

### 表示速度を調整

```yaml
speed: 50  # より速く
# または
speed: 15  # よりゆっくり
```

## ⚡ ステップ4: よくある問題と解決法

### 問題1: スクリプトが動かない

**確認事項：**
- ファイルが正しい場所にあるか
- YML形式が正しいか（インデントなど）
- セクション名が正しいか

### 問題2: 文字化けする

**解決法：**
- ファイルをUTF-8で保存
- カラーコードを確認

### 問題3: 音が出ない

**解決法：**
- 音声名を確認
- クライアントの音量設定を確認

## 🚀 次のステップ

基本的なスクリプトが作成できたら、以下のドキュメントで更に高度な機能を学習しましょう：

- **[コマンドリファレンス](../commands/README.md)** - 全コマンドの詳細説明
- **[実用例](../examples/README.md)** - より複雑なサンプル
- **[変数とスコア](../commands/variables.md)** - ゲーム進行の管理
- **[条件分岐](../commands/conditions.md)** - 複雑な分岐処理

## 💡 学習のヒント

1. **小さく始めて徐々に拡張** - 最初は簡単なものから
2. **他の例を参考にする** - `tutorial.yml`や実用例を見て学習
3. **実際に試す** - 頭で理解するより手を動かす
4. **エラーログを確認** - 問題が起きたときはサーバーログを見る

頑張って素敵な物語を作ってください！