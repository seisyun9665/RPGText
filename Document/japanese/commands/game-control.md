# ゲーム制御コマンド

ゲームの流れや環境を制御する高度なコマンドです。

## 🎮 `/command` - Minecraftコマンド実行

Minecraftの標準コマンドを実行します。

### 基本構文
```yaml
/command <Minecraftコマンド>
```

### 基本的な使用例

```yaml
# アイテム付与
item_rewards:
  - /command give %player% minecraft:diamond 5
  - ダイヤモンドを5個獲得しました！
  - /command give %player% minecraft:iron_sword 1
  - 鉄の剣を獲得しました！

# テレポート
teleport_examples:
  - /command tp %player% 100 64 200
  - 指定座標にテレポートしました
  - /command tp %player% @a[name="Target"]
  - 他のプレイヤーの元へテレポート

# タイトル表示
title_display:
  - /command title %player% title {"text":"ミッション完了!","color":"gold","bold":true}
  - /command title %player% subtitle {"text":"報酬を受け取りました","color":"green"}
  - /command title %player% times 10 70 20
```

## 🧊 `/freeze` - 移動制限

プレイヤーの移動を制御します。

### 基本構文
```yaml
/freeze <true|false>
```

### 使用例

```yaml
# 重要な場面での移動制限
dramatic_scene:
  - /freeze true
  - /color \d\l
  - 突然、時が止まったかのような感覚に襲われた...
  - /color \r
  - /wait 60
  - 何かが起こりそうだ...
  - /wait 40
  - /freeze false
  - 体が動くようになった

# 会話中の制限
important_conversation:
  - /freeze true
  - 重要な話があります
  - 最後まで聞いてください
  - /wait 20
  - この情報は極秘です...
  - /freeze false
  - お疲れ様でした

# 戦闘準備
battle_preparation:
  - /freeze true
  - 敵が現れました！
  - /wait 40
  - 戦闘準備をしてください
  - /freeze false
  - 戦闘開始！
```

## ⏰ `/wait` - 待機

指定した時間だけ処理を停止します。

### 基本構文
```yaml
/wait <ティック数>
```

### 時間の目安
- 20ティック = 1秒
- 40ティック = 2秒
- 60ティック = 3秒

### 演出での使用例

```yaml
# タイミングを作る
dramatic_reveal:
  - 古い扉がゆっくりと...
  - /wait 40
  - きしむ音を立てながら...
  - /wait 60
  - 開いていく...
  - /wait 40
  - /color \6\l
  - 光が差し込んできた！
  - /color \r

# カウントダウン
countdown:
  - ゲーム開始まで...
  - /wait 20
  - 3
  - /wait 20
  - 2
  - /wait 20
  - 1
  - /wait 20
  - /color \a\l
  - スタート！
  - /color \r

# 思考時間
thinking_pause:
  - 村人は考え込んでいる...
  - /wait 80
  - うーん...
  - /wait 60
  - そうだ！良いアイデアがある！
```

## 🔄 `/auto` - 自動進行設定

メッセージ表示完了後の自動進行を制御します。

### 基本構文
```yaml
/auto <true|false>
```

### 使用例

```yaml
# 自動進行で連続表示
auto_sequence:
  - /auto true
  - 物語が始まります...
  - 昔々、ある所に...
  - 勇敢な冒険者がいました...
  - その名は%player%...
  - /auto false
  - （クリックして続行）

# 緊急場面での自動進行
emergency_scene:
  - /auto true
  - /freeze true
  - 危険！
  - 爆発まで...
  - 3秒！
  - 2秒！
  - 1秒！
  - /auto false
  - /freeze false
  - 間一髪でした！

# 会話での使い分け
natural_conversation:
  - こんにちは、%player%さん
  - /auto true
  - 今日はいい天気ですね
  - そうそう、お話があるんです
  - /auto false
  - 詳しく聞きますか？
  - /? はい いいえ
```

## ⏭️ `/skip` - スキップ設定

プレイヤーのメッセージスキップ可能性を制御します。

### 基本構文
```yaml
/skip <true|false>
```

### 使用例

```yaml
# 重要な情報はスキップ不可
important_info:
  - /skip false
  - /color \c\l
  - 【重要】
  - /color \r
  - この情報は必ず読んでください
  - ゲーム進行に必要な内容です
  - /skip true
  - 通常のメッセージに戻ります

# チュートリアル
tutorial_section:
  - /skip false
  - チュートリアル開始
  - 基本操作を覚えましょう
  - まず移動方法から...
  - /skip true
  - 理解できましたか？

# ムービーシーン
cutscene:
  - /skip false
  - /auto true
  - === カットシーン ===
  - 重要な場面が展開されます...
  - （中略）
  - === カットシーン終了 ===
  - /auto false
  - /skip true
```

## 🎯 実用的なシステム例

### 1. ミニゲーム制御

```yaml
minigame_start:
  - ミニゲームを開始します！
  - /freeze true
  - /auto true
  - ルール説明
  - 制限時間は30秒
  - 準備はいいですか？
  - /auto false
  - /freeze false
  - /? 開始する 説明を聞く
  - /?開始する /jump game_begin
  - /?説明を聞く /jump game_rules

game_begin:
  - /freeze true
  - /auto true
  - /skip false
  - カウントダウン開始！
  - 3
  - /wait 20
  - 2
  - /wait 20  
  - 1
  - /wait 20
  - スタート！
  - /skip true
  - /auto false
  - /freeze false
  - ゲーム開始！
```

### 2. 演出システム

```yaml
boss_entrance:
  - /freeze true
  - /skip false
  - /auto true
  - 地面が揺れ始めた...
  - /wait 40
  - /command playsound minecraft:entity.enderdragon.growl master @a
  - 何かが近づいてくる...
  - /wait 60
  - /color \c\l
  - ボスが現れた！
  - /color \r
  - /command title %player% title {"text":"ボス戦","color":"red","bold":true}
  - /wait 40
  - /auto false
  - /skip true
  - /freeze false
  - 戦闘開始！

victory_celebration:
  - /freeze true
  - /auto true
  - /command give %player% minecraft:diamond 10
  - /command title %player% title {"text":"勝利！","color":"gold","bold":true}
  - /command playsound minecraft:entity.player.levelup master %player%
  - おめでとうございます！
  - ダイヤモンド10個を獲得！
  - /wait 60
  - /auto false
  - /freeze false
```

### 3. 謎解きシステム

```yaml
puzzle_presentation:
  - /freeze true
  - /skip false
  - 古代の謎解きが現れました
  - この扉を開くには...
  - 正しい順番でスイッチを押す必要があります
  - ヒント: 太陽の動きを思い出せ
  - /skip true
  - /freeze false
  - 挑戦しますか？
  - /? 挑戦する ヒントをもらう 後で来る
  - /?挑戦する /jump puzzle_start
  - /?ヒントをもらう /jump puzzle_hint
  - /?後で来る またいつでもどうぞ

puzzle_solving:
  - /freeze true
  - スイッチ1を押しました
  - /wait 20
  - /command playsound minecraft:block.stone_button.click_on master %player%
  - 何かが動く音がした...
  - /freeze false
```

### 4. ストーリーテリング

```yaml
story_chapter:
  - /auto true
  - /skip false
  - === 第1章: 始まりの村 ===
  - /wait 40
  - 小さな村に住む%player%は...
  - ある日、不思議な手紙を受け取った
  - それは遠い国からの冒険の誘いだった...
  - /wait 60
  - 決断の時が来た
  - /auto false
  - /skip true
  - 冒険に出ますか？
  - /? 出発する 村に残る
  - /?出発する /jump adventure_path
  - /?村に残る /jump peaceful_path

dialogue_scene:
  - /freeze true
  - 村長: %player%よ、聞いてくれ
  - /wait 30
  - 村長: 大変なことが起こったのじゃ
  - /wait 30
  - あなた: 何があったのですか？
  - /wait 30
  - 村長: 魔物が森に現れたのじゃ...
  - /freeze false
  - 詳しく聞きますか？
```

### 5. システムメッセージ

```yaml
save_system:
  - /auto true
  - /skip false
  - /command save-all
  - ゲームデータを保存中...
  - /wait 40
  - 保存完了！
  - /auto false
  - /skip true
  - 続行できます

loading_screen:
  - /freeze true
  - /auto true
  - /skip false
  - エリアを読み込んでいます...
  - /wait 60
  - 少々お待ちください...
  - /wait 40
  - 読み込み完了！
  - /auto false
  - /skip true
  - /freeze false
  - 新しいエリアへようこそ！
```

## ⚠️ 注意事項とベストプラクティス

### プレイヤー体験の配慮

```yaml
# ✅ 適切な制御
user_friendly:
  - /freeze true
  - 重要な場面です
  - /wait 40  # 短時間の演出
  - /freeze false

# ❌ 長時間の制限（プレイヤーが退屈）
bad_control:
  - /freeze true
  - /wait 300  # 15秒は長すぎる
  - /freeze false
```

### コマンドの安全性

```yaml
# ✅ 安全なコマンド
safe_commands:
  - /command give %player% minecraft:diamond 1
  - /command tp %player% 100 64 200
  - /command title %player% title {"text":"Hello"}

# ❌ 危険なコマンド（避けるべき）
dangerous_commands:
  - /command op %player%  # 管理者権限付与
  - /command stop         # サーバー停止
  - /command ban %player% # プレイヤーBANDer
```

### パフォーマンスの考慮

```yaml
# ✅ 効率的
efficient_timing:
  - /wait 20   # 適度な間
  - メッセージ
  - /wait 40   # 演出

# ❌ 非効率的
inefficient_timing:
  - /wait 5    # 短すぎて意味がない
  - /wait 5
  - /wait 5
  - /wait 5    # 同じ効果なら/wait 20で十分
```

## 💡 高度なテクニック

### 条件付き制御

```yaml
dynamic_control:
  - /if difficulty = 1 /skip true
  - /if difficulty > 1 /skip false
  - 難易度に応じた制御
  - /if first_time = 1 /auto false
  - /if first_time = 0 /auto true
```

### 状況に応じた演出

```yaml
adaptive_presentation:
  - /if time_of_day = night /jump night_scene
  - /if weather = rain /jump rain_scene
  - /jump normal_scene

night_scene:
  - /freeze true
  - 夜の演出...
  - /command time set night
  - /freeze false

rain_scene:
  - /freeze true
  - 雨の演出...
  - /command weather rain
  - /freeze false
```

## 🔗 関連ドキュメント

- **[基本設定](basic-settings.md)** - wait時間と音声・速度の関係
- **[条件分岐](conditions.md)** - 条件による制御の分岐
- **[音声制御](audio.md)** - 演出での音声制御
- **[実用例 - ミニゲーム](../examples/mini-games.md)** - ゲーム制御の実装例