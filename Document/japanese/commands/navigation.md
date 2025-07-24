# ナビゲーションコマンド

セクション間やファイル間の移動を行うコマンドです。

## 🧭 `/jump` - セクション移動

指定したセクションまたは他のファイルのセクションに移動します。

### 構文
```yaml
/jump <移動先>
```

### 移動先の指定方法

#### 1. 同一ファイル内のセクション
```yaml
/jump section_name
```

#### 2. 他のファイルのセクション
```yaml
/jump filename.yml/section_name
```

#### 3. 階層構造のファイル
```yaml
/jump folder/filename.yml/section_name
```

## 📁 基本的な使用例

### 同一ファイル内移動

```yaml
# story.yml
intro:
  - ゲームを開始します
  - /jump chapter1

chapter1:
  - 第1章: 冒険の始まり
  - 準備はできましたか？
  - /? はい いいえ
  - /?はい /jump start_adventure
  - /?いいえ /jump preparation

start_adventure:
  - 冒険を開始しましょう！

preparation:
  - もう少し準備しましょう
  - /jump chapter1
```

### 他のファイルへの移動

```yaml
# main.yml
menu:
  - メインメニュー
  - /? ストーリー ショップ 終了
  - /?ストーリー /jump story/chapter1.yml/intro
  - /?ショップ /jump shop.yml/main_shop
  - /?終了 /jump ending

# story/chapter1.yml
intro:
  - 第1章が始まります
  - /jump main.yml/menu

# shop.yml  
main_shop:
  - ショップへようこそ
  - /jump main.yml/menu
```

## 🏗️ 階層構造での使用

### ディレクトリ構造例
```
messages/
├── main.yml
├── story/
│   ├── chapter1.yml
│   ├── chapter2.yml
│   └── ending.yml
├── characters/
│   ├── npc1.yml
│   └── npc2.yml
└── systems/
    ├── shop.yml
    └── battle.yml
```

### 移動例

```yaml
# main.yml
main_menu:
  - /? 物語を始める NPCと話す ショップ
  - /?物語を始める /jump story/chapter1.yml/intro
  - /?NPCと話す /jump characters/npc1.yml/greeting
  - /?ショップ /jump systems/shop.yml/main

# story/chapter1.yml
intro:
  - 第1章: 新たな出発
  - /jump story/chapter2.yml/intro

boss_fight:
  - ボス戦！
  - /jump systems/battle.yml/boss_battle

# characters/npc1.yml
greeting:
  - こんにちは、冒険者よ
  - /jump story/chapter1.yml/npc_encounter
```

## 🔄 条件付き移動

### 変数による分岐

```yaml
check_progress:
  - /if chapter_completed = 1 /jump next_chapter
  - /if level > 10 /jump advanced_path
  - /jump normal_path

next_chapter:
  - 次の章に進みます
  - /jump chapter2.yml/intro

advanced_path:
  - 上級者向けルート
  - /jump advanced/special.yml/start

normal_path:
  - 通常ルート
  - /jump story/normal.yml/start
```

### 選択肢による複数分岐

```yaml
story_branch:
  - どの道を選びますか？
  - /? 森の道 山の道 海の道
  - /?森の道 /jump routes/forest.yml/entrance
  - /?山の道 /jump routes/mountain.yml/entrance  
  - /?海の道 /jump routes/sea.yml/entrance
```

## 🎯 実用的なパターン

### 1. チャプター選択システム

```yaml
# main.yml
chapter_select:
  - チャプター選択
  - /? 第1章 第2章 第3章 戻る
  - /?第1章 /jump story/chapter1.yml/start
  - /?第2章 /if chapter1_clear = 1 /jump story/chapter2.yml/start
  - /?第2章 まだ第1章をクリアしていません
  - /?第3章 /if chapter2_clear = 1 /jump story/chapter3.yml/start
  - /?第3章 まだ第2章をクリアしていません
  - /?戻る /jump main_menu
```

### 2. エリア移動システム

```yaml
# world_map.yml
town_square:
  - 町の中央広場
  - /? 武器屋 道具屋 宿屋 城 外に出る
  - /?武器屋 /jump shops/weapon.yml/entrance
  - /?道具屋 /jump shops/item.yml/entrance
  - /?宿屋 /jump facilities/inn.yml/entrance
  - /?城 /jump areas/castle.yml/gate
  - /?外に出る /jump areas/field.yml/town_exit
```

### 3. 対話システム

```yaml
# character_talk.yml
npc_conversation:
  - こんにちは、%player%さん
  - /? 話をする クエストを受ける 去る
  - /?話をする /jump conversation_topics
  - /?クエストを受ける /jump quest/available.yml/npc_quests
  - /?去る また会いましょう

conversation_topics:
  - 何について聞きますか？
  - /? 町のこと 最近の出来事 戻る
  - /?町のこと /jump town_info
  - /?最近の出来事 /jump recent_events
  - /?戻る /jump npc_conversation
```

## ⚠️ 注意事項とベストプラクティス

### ファイルパスの指定

```yaml
# ✅ 正しい例
/jump story/chapter1.yml/intro
/jump ../main.yml/menu
/jump systems/shop.yml/main

# ❌ 間違った例
/jump story\chapter1.yml\intro  # Windows形式は使用不可
/jump "story/chapter 1.yml"/intro  # スペースを含むファイル名は避ける
```

### セクション名の規則

```yaml
# ✅ 推奨
section_name:
intro:
chapter1_start:
boss_battle_phase1:

# ❌ 避けるべき
セクション1:  # 日本語
"section name":  # スペース
section-name:  # ハイフンは可能だが非推奨
```

### 循環参照の回避

```yaml
# ❌ 無限ループになる可能性
section_a:
  - /jump section_b

section_b:
  - /jump section_a

# ✅ 終了条件を設ける
section_a:
  - /if loop_count > 3 /jump end_section
  - /add loop_count
  - /jump section_b

section_b:
  - /jump section_a

end_section:
  - ループを終了します
```

### パフォーマンスの考慮

```yaml
# ✅ 効率的
main_hub:
  - /? A B C D
  - /?A /jump section_a
  - /?B /jump section_b
  - /?C /jump section_c
  - /?D /jump section_d

# ❌ 非効率的（深いネスト）
chain1:
  - /jump chain2
chain2:
  - /jump chain3
chain3:
  - /jump chain4
# ...続く
```

## 💡 高度なテクニック

### 動的なジャンプ

```yaml
# 変数による動的移動先決定
dynamic_jump:
  - /if area = 1 /jump area1/main.yml/entrance
  - /if area = 2 /jump area2/main.yml/entrance
  - /if area = 3 /jump area3/main.yml/entrance
  - /jump default_area.yml/entrance
```

### 履歴管理

```yaml
# 前のセクションを記録
visit_shop:
  - /score previous_section 1  # 1=町, 2=城, etc
  - /jump shop.yml/main

# ショップから戻る
shop_exit:
  - /if previous_section = 1 /jump town.yml/square
  - /if previous_section = 2 /jump castle.yml/entrance
  - /jump default_area.yml/main
```

## 🔗 関連ドキュメント

- **[条件分岐](conditions.md)** - if文と組み合わせた移動
- **[変数・スコア管理](variables.md)** - 進行状況の管理
- **[選択肢・インタラクション](interaction.md)** - 選択肢からの移動
- **[実用例 - クエストシステム](../examples/quest-system.md)** - 複雑な移動の例