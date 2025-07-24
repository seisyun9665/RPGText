# 基本的な会話システム

RPGTextの基本機能を使ったシンプルな会話システムの実装例です。

## 📋 概要

このサンプルでは以下の機能を実装します：

- NPCとの基本的な対話
- プレイヤーの選択による分岐
- 好感度システムの基礎
- 色分けによる視認性向上
- 音声効果による演出

## 💻 完全なコード

### friendly_npc.yml

```yaml
# 友好的なNPCとの会話システム
# ファイル共通設定
sound: block.note.bell 1 1.2
speed: 25
color: \r

# メイン会話
greeting:
  - /color \2\l
  - こんにちは、%player%さん！
  - /color \r
  - /singlesound minecraft:entity.villager.yes 1 1
  - 今日はいい天気ですね。
  - どちらから来られたのですか？
  - /? 近くの村から 遠い街から 旅の途中です
  - /?近くの村から /jump nearby_village
  - /?遠い街から /jump distant_city
  - /?旅の途中です /jump traveling

# 近くの村出身の場合
nearby_village:
  - /color \a
  - おお、ご近所さんですね！
  - /color \r
  - /score friendship + 5
  - あの村の人たちは皆親切で...
  - /singlesound minecraft:entity.experience_orb.pickup 1 1.2
  - いつも助けてもらっています。
  - /jump ask_about_help

# 遠い街出身の場合
distant_city:
  - /color \6
  - 遠いところからお疲れ様です！
  - /color \r
  - /score friendship + 3
  - 長旅は大変だったでしょう。
  - この辺りで何かお困りのことはありませんか？
  - /jump ask_about_help

# 旅人の場合
traveling:
  - /color \d
  - 旅人さんですか！
  - /color \r
  - /score friendship + 2
  - この土地には珍しい場所や
  - 美味しい食べ物がたくさんありますよ。
  - /jump ask_about_help

# 手伝いの申し出
ask_about_help:
  - /wait 20
  - 何かお手伝いできることはありますか？
  - /? 道を教えて 情報が欲しい お礼をしたい 特になし
  - /?道を教えて /jump give_directions
  - /?情報が欲しい /jump share_information
  - /?お礼をしたい /jump receive_gift
  - /?特になし /jump polite_farewell

# 道案内
give_directions:
  - /color \b
  - どちらへ向かわれますか？
  - /color \r
  - /? 町の中心部 宿屋 武器屋 城
  - /?町の中心部 /jump directions_center
  - /?宿屋 /jump directions_inn
  - /?武器屋 /jump directions_weapon
  - /?城 /jump directions_castle

directions_center:
  - /singlesound minecraft:block.note.flute 1 1
  - この道をまっすぐ行って、
  - 大きな噴水が見えたらそこが中心部です。
  - 迷子になったら誰かに聞いてくださいね。
  - /jump helpful_farewell

directions_inn:
  - /singlesound minecraft:block.note.flute 1 1
  - 宿屋でしたら、あの赤い屋根の建物です。
  - 主人のおばさんはとても親切ですよ。
  - 美味しい料理も食べられます！
  - /jump helpful_farewell

directions_weapon:
  - /singlesound minecraft:block.note.flute 1 1
  - 武器屋は町の東側にあります。
  - 鍛冶の音が聞こえてくるのですぐ分かりますよ。
  - 良い品が揃っています。
  - /jump helpful_farewell

directions_castle:
  - /singlesound minecraft:block.note.bass 1 0.8
  - /color \e
  - 城ですか...
  - /color \r
  - 最近は警備が厳しくて、
  - 一般の人は入れないかもしれません。
  - でも、丘の上なのですぐ見つかります。
  - /jump helpful_farewell

# 情報共有
share_information:
  - /color \d
  - どんな情報をお探しですか？
  - /color \r
  - /? この町について 最近の出来事 周辺の危険
  - /?この町について /jump town_info
  - /?最近の出来事 /jump recent_events
  - /?周辺の危険 /jump danger_info

town_info:
  - /singlesound minecraft:block.note.piano 1 1
  - この町は商業が盛んで、
  - 色々な場所から商人がやってきます。
  - 人々は親切で、困った人を見つけると
  - すぐに助けてくれる温かい町です。
  - /score friendship + 2
  - /jump information_farewell

recent_events:
  - /singlesound minecraft:block.note.xylophone 1 1
  - 最近、町の東の森で
  - 珍しい花が咲いているのが発見されました。
  - とても美しい青い花で、
  - 見る人を魅了するそうです。
  - /score friendship + 2
  - /jump information_farewell

danger_info:
  - /singlesound minecraft:entity.lightning_bolt.thunder 0.3 1
  - /color \c
  - 夜の森には近づかない方がいいでしょう。
  - /color \r
  - 最近、野生の動物が増えているようで...
  - でも、昼間なら大丈夫ですよ。
  - /score friendship + 3
  - /jump information_farewell

# お礼を受ける
receive_gift:
  - /color \6
  - お気遣いありがとうございます！
  - /color \r
  - /? 金貨を渡す アイテムを渡す 感謝の言葉
  - /?金貨を渡す /jump give_money
  - /?アイテムを渡す /jump give_item
  - /?感謝の言葉 /jump give_thanks

give_money:
  - /if money >= 10 /jump money_gift
  - お気持ちだけで十分ですよ！

money_gift:
  - /score money - 10
  - /score friendship + 10
  - /singlesound minecraft:entity.player.levelup 1 1
  - /color \a\l
  - ありがとうございます！
  - /color \r
  - お心遣いが嬉しいです。
  - /jump grateful_farewell

give_item:
  - /has BREAD 1 none /jump bread_gift
  - /has APPLE 1 none /jump apple_gift
  - /has EMERALD 1 none /jump emerald_gift
  - お気持ちだけで十分ですよ！

bread_gift:
  - /removeItem BREAD 1 none
  - /score friendship + 8
  - /singlesound minecraft:entity.experience_orb.pickup 1 1.2
  - パンをいただけるのですか！
  - とても助かります、ありがとう！
  - /jump grateful_farewell

apple_gift:
  - /removeItem APPLE 1 none
  - /score friendship + 6
  - /singlesound minecraft:entity.experience_orb.pickup 1 1.2
  - 新鮮なリンゴですね！
  - 子供たちが喜びます。
  - /jump grateful_farewell

emerald_gift:
  - /removeItem EMERALD 1 none
  - /score friendship + 15
  - /singlesound minecraft:entity.player.levelup 1 1.2
  - /color \a\l
  - エメラルド！！こんな貴重なものを...
  - /color \r
  - 本当にありがとうございます！
  - /jump grateful_farewell

give_thanks:
  - /score friendship + 5
  - /singlesound minecraft:entity.villager.yes 1 1
  - /color \a
  - お気持ちだけで十分です。
  - /color \r
  - 温かい言葉をありがとう！
  - /jump grateful_farewell

# 各種お別れ
polite_farewell:
  - /singlesound minecraft:entity.villager.yes 1 1
  - そうですか。
  - また何かありましたら声をかけてくださいね。
  - 良い一日を！

helpful_farewell:
  - /score friendship + 3
  - /singlesound minecraft:entity.experience_orb.pickup 1 1
  - /color \a
  - お役に立てて良かったです！
  - /color \r
  - また何かありましたらどうぞ。

information_farewell:
  - /singlesound minecraft:entity.villager.yes 1 1
  - 他にも何か知りたいことがあれば
  - いつでも聞いてくださいね。
  - /jump check_friendship

grateful_farewell:
  - /color \6\l
  - %player%さんのような親切な方に出会えて
  - /color \r
  - 本当に幸せです！
  - /jump check_friendship

# 好感度チェック
check_friendship:
  - /if friendship >= 20 /jump best_friend
  - /if friendship >= 10 /jump good_friend
  - /jump normal_friend

best_friend:
  - /color \6\l
  - あなたは私の親友です！
  - /color \r
  - 何か困ったことがあれば
  - いつでも相談してください。
  - また会える日を楽しみにしています！

good_friend:
  - /color \a
  - あなたとお話しできて楽しかったです。
  - /color \r
  - またお会いできることを願っています！

normal_friend:
  - それでは、また会いましょう！
  - %player%さんの旅が素晴らしいものになりますように。
```

## 📖 解説

### 1. ファイル共通設定
```yaml
sound: block.note.bell 1 1.2
speed: 25
color: \r
```
- 全体で統一感のある音声と表示速度を設定
- 基本色をリセット（\r）に設定

### 2. 分岐システム
- プレイヤーの出身地により初期好感度が変化
- 各選択肢に対応する適切な反応を用意
- 自然な会話の流れを作成

### 3. 好感度システム
```yaml
/score friendship + 5
```
- プレイヤーの行動により好感度が増減
- 最終的な好感度により別れの言葉が変化

### 4. アイテムシステム
```yaml
/has BREAD 1 none /jump bread_gift
/removeItem BREAD 1 none
```
- プレイヤーが持っているアイテムをチェック
- アイテムを渡すことで好感度アップ

### 5. 演出効果
- 色分けにより重要な情報を強調
- 適切な音声効果で感情を表現
- 待機時間で自然な間を作成

## 🎨 カスタマイズ案

### 難易度調整
```yaml
# 好感度の獲得量を調整
/score friendship + 3  # 少なめ
/score friendship + 10 # 多め
```

### 新しい話題の追加
```yaml
extended_topics:
  - /? 天気について 家族について 趣味について
  - /?天気について /jump weather_talk
  - /?家族について /jump family_talk
  - /?趣味について /jump hobby_talk
```

### 時間帯による変化
```yaml
time_greeting:
  - /if time >= 6 & time < 12 おはようございます！
  - /if time >= 12 & time < 18 こんにちは！
  - /if time >= 18 こんばんは！
```

### クエスト要素の追加
```yaml
quest_offer:
  - /if friendship >= 15 /jump special_quest
  - またお話ししましょう

special_quest:
  - 実は、お願いがあるのですが...
  - /? 詳しく聞く 後で聞く
  - /?詳しく聞く /jump quest_explanation
```

## 🔗 使用している機能

- **[基本設定](../commands/basic-settings.md)** - sound, speed, color
- **[選択肢・インタラクション](../commands/interaction.md)** - ?, /?
- **[変数・スコア管理](../commands/variables.md)** - score, friendship管理
- **[条件分岐](../commands/conditions.md)** - if文による分岐
- **[アイテム管理](../commands/items.md)** - has, removeItem
- **[音声制御](../commands/audio.md)** - singlesound
- **[ナビゲーション](../commands/navigation.md)** - jump

## 💡 学習ポイント

1. **自然な会話の流れ** - 選択肢から適切な反応への分岐
2. **状態管理** - 好感度という数値による関係性の表現
3. **演出の重要性** - 色と音声で感情を表現
4. **プレイヤーの行動の意味** - 選択や行動が結果に影響

このサンプルを基に、より複雑なNPCシステムや対話システムを作成してみてください！