# 音声制御コマンド

ゲーム内での音響効果を制御するコマンドです。

## 🔊 `/singlesound` - 単発音声再生

特定のタイミングで一度だけ音声を再生します。

### 基本構文
```yaml
/singlesound <音声名> [音量] [ピッチ]
```

### パラメータ
- **音声名**: Minecraftの音声識別子
- **音量**: 0.0〜1.0（省略可、デフォルト: 1.0）
- **ピッチ**: 0.5〜2.0（省略可、デフォルト: 1.0）

## 🎵 基本的な使用例

### シンプルな音声再生

```yaml
sound_examples:
  - /singlesound minecraft:entity.player.levelup
  - レベルアップ音を再生しました
  - /singlesound minecraft:block.note.bell
  - ベルの音を再生
  - /singlesound minecraft:entity.experience_orb.pickup
  - 経験値獲得音
```

### 音量とピッチの調整

```yaml
volume_examples:
  - /singlesound minecraft:block.note.bell 0.5
  - 小さめの音量
  - /singlesound minecraft:block.note.bell 1.0
  - 通常の音量
  - /singlesound minecraft:entity.lightning_bolt.thunder 0.3
  - 雷音（小音量）

pitch_examples:
  - /singlesound minecraft:block.note.bass 1 0.5
  - 低いピッチ
  - /singlesound minecraft:block.note.bass 1 1.0
  - 通常のピッチ
  - /singlesound minecraft:block.note.bass 1 2.0
  - 高いピッチ
```

## 🎼 よく使用される音声

### システム音

```yaml
system_sounds:
  - /singlesound minecraft:entity.player.levelup 1 1
  - 成功・達成
  - /singlesound minecraft:entity.experience_orb.pickup 1 1.2
  - アイテム獲得
  - /singlesound minecraft:block.note.bell 1 1.5
  - 通知・お知らせ
  - /singlesound minecraft:entity.villager.yes 1 1
  - 肯定・承認
  - /singlesound minecraft:entity.villager.no 1 1
  - 否定・拒否
```

### 楽器音

```yaml
instrument_sounds:
  - /singlesound minecraft:block.note.bass 1 1
  - ベース音（低音）
  - /singlesound minecraft:block.note.bell 1 1
  - ベル音（清澄）
  - /singlesound minecraft:block.note.flute 1 1
  - フルート音（軽やか）
  - /singlesound minecraft:block.note.guitar 1 1
  - ギター音
  - /singlesound minecraft:block.note.xylophone 1 1
  - 木琴音（明るい）
  - /singlesound minecraft:block.note.piano 1 1
  - ピアノ音
```

### 環境音・効果音

```yaml
environment_sounds:
  - /singlesound minecraft:block.chest.open 1 1
  - 宝箱を開く
  - /singlesound minecraft:block.door.wood.open 1 1
  - ドアを開く
  - /singlesound minecraft:entity.lightning_bolt.thunder 0.5 1
  - 雷音
  - /singlesound minecraft:block.fire.extinguish 1 1
  - 火が消える
  - /singlesound minecraft:entity.generic.explode 0.3 1
  - 爆発音（小）
```

## 🎯 実用的なシステム例

### 1. 感情表現システム

```yaml
emotion_sounds:
  - どんな気持ちですか？
  - /? 嬉しい 悲しい 怒っている 驚いている
  - /?嬉しい /jump happy_emotion
  - /?悲しい /jump sad_emotion
  - /?怒っている /jump angry_emotion
  - /?驚いている /jump surprised_emotion

happy_emotion:
  - /singlesound minecraft:entity.player.levelup 1 1.2
  - /color \a
  - 嬉しいですね！
  - /color \r

sad_emotion:
  - /singlesound minecraft:entity.villager.hurt 1 0.8
  - /color \b
  - 悲しい気持ち、わかります...
  - /color \r

angry_emotion:
  - /singlesound minecraft:entity.lightning_bolt.thunder 0.3 1.5
  - /color \c
  - 怒りを感じていますね
  - /color \r

surprised_emotion:
  - /singlesound minecraft:entity.cat.ambient 1 2.0
  - /color \e
  - 驚きましたか！
  - /color \r
```

### 2. ショップシステム

```yaml
shop_interaction:
  - /singlesound minecraft:block.note.bell 1 1.5
  - いらっしゃいませ！
  - /? 購入する 売却する 帰る
  - /?購入する /jump shop_buy
  - /?売却する /jump shop_sell
  - /?帰る /jump shop_farewell

shop_buy:
  - /singlesound minecraft:entity.experience_orb.pickup 1 1
  - 何を購入されますか？
  - /? 武器(100G) 薬(50G) 戻る
  - /?武器(100G) /jump buy_weapon
  - /?薬(50G) /jump buy_potion
  - /?戻る /jump shop_interaction

buy_weapon:
  - /if money >= 100 /jump purchase_success
  - /singlesound minecraft:entity.villager.no 1 1
  - /color \c
  - お金が足りません
  - /color \r

purchase_success:
  - /singlesound minecraft:entity.player.levelup 1 1
  - /score money - 100
  - /color \a
  - 購入ありがとうございます！
  - /color \r

shop_farewell:
  - /singlesound minecraft:entity.villager.yes 1 1
  - またお越しください！
```

### 3. 戦闘システム

```yaml
battle_system:
  - /singlesound minecraft:entity.lightning_bolt.thunder 0.4 1
  - 敵が現れた！
  - /? 攻撃 魔法 逃げる
  - /?攻撃 /jump physical_attack
  - /?魔法 /jump magic_attack
  - /?逃げる /jump try_escape

physical_attack:
  - /singlesound minecraft:entity.player.attack.sweep 1 1
  - 攻撃！
  - /score hit_chance random 100
  - /if hit_chance < 80 /jump attack_hit
  - /jump attack_miss

attack_hit:
  - /singlesound minecraft:entity.experience_orb.pickup 1 0.8
  - ヒット！敵にダメージを与えた
  - /if enemy_hp <= 0 /jump victory

attack_miss:
  - /singlesound minecraft:entity.villager.no 1 1.2
  - 攻撃が外れた！

magic_attack:
  - /singlesound minecraft:block.note.bell 1 2.0
  - 魔法を詠唱中...
  - /wait 40
  - /singlesound minecraft:entity.lightning_bolt.thunder 0.6 1.5
  - 魔法攻撃！

victory:
  - /singlesound minecraft:entity.player.levelup 1 1
  - /color \6\l
  - 勝利！
  - /color \r
  - 経験値とゴールドを獲得しました

try_escape:
  - /singlesound minecraft:entity.horse.gallop 1 1.5
  - 逃走を試みた...
  - /score escape_chance random 100
  - /if escape_chance < 70 /jump escape_success
  - /jump escape_fail

escape_success:
  - /singlesound minecraft:entity.experience_orb.pickup 1 1.2
  - 逃走成功！

escape_fail:
  - /singlesound minecraft:entity.villager.hurt 1 1
  - 逃げられなかった...
```

### 4. クエストシステム

```yaml
quest_start:
  - /singlesound minecraft:block.note.bass 1 1
  - 村人: 冒険者よ、頼みがある
  - /? 詳しく聞く 断る
  - /?詳しく聞く /jump quest_explanation
  - /?断る /jump quest_decline

quest_explanation:
  - /singlesound minecraft:block.note.flute 1 1
  - 森に魔物が現れて困っている
  - 退治してもらえないか？
  - /? 受ける 断る
  - /?受ける /jump quest_accept
  - /?断る /jump quest_decline

quest_accept:
  - /singlesound minecraft:entity.player.levelup 1 1
  - /score quest_active 1
  - /color \a
  - ありがとう！頼んだぞ
  - /color \r

quest_decline:
  - /singlesound minecraft:entity.villager.hurt 1 0.8
  - /color \c
  - そうか...残念だ
  - /color \r

quest_complete:
  - /singlesound minecraft:entity.player.levelup 1 1.2
  - /color \6\l
  - クエスト完了！
  - /color \r
  - /score money + 500
  - 報酬を受け取りました
```

### 5. ミニゲーム

```yaml
slot_machine:
  - /singlesound minecraft:block.note.xylophone 1 1
  - スロットマシンです
  - /? プレイする(10G) 帰る
  - /?プレイする(10G) /jump slot_play
  - /?帰る また来てください

slot_play:
  - /if money >= 10 /jump slot_start
  - /singlesound minecraft:entity.villager.no 1 1
  - お金が足りません

slot_start:
  - /score money - 10
  - /singlesound minecraft:block.note.xylophone 1 1.5
  - スロット開始！
  - /jump slot_spin

slot_spin:
  - /score slot1 random 3
  - /score slot2 random 3
  - /score slot3 random 3
  - /add slot1
  - /add slot2
  - /add slot3
  - /singlesound minecraft:block.note.xylophone 1 1.8
  - \\slot1\\ \\slot2\\ \\slot3\\
  - /if slot1 = slot2 & slot2 = slot3 /jump jackpot
  - 残念！また挑戦してください

jackpot:
  - /singlesound minecraft:entity.player.levelup 1 1
  - /color \6\l
  - ジャックポット！
  - /color \r
  - /score money + 100
  - 100ゴールドを獲得！
```

## 🎨 演出テクニック

### メロディーの作成

```yaml
simple_melody:
  - /singlesound minecraft:block.note.piano 1 0.5
  - /wait 10
  - /singlesound minecraft:block.note.piano 1 0.6
  - /wait 10
  - /singlesound minecraft:block.note.piano 1 0.8
  - /wait 10
  - /singlesound minecraft:block.note.piano 1 1.0
  - /wait 20
  - 美しいメロディーが響いた

victory_fanfare:
  - /singlesound minecraft:block.note.bell 1 1.0
  - /wait 5
  - /singlesound minecraft:block.note.bell 1 1.2
  - /wait 5
  - /singlesound minecraft:block.note.bell 1 1.5
  - /wait 10
  - /singlesound minecraft:entity.player.levelup 1 1
  - ファンファーレ！
```

### 雰囲気作り

```yaml
mysterious_atmosphere:
  - /singlesound minecraft:entity.lightning_bolt.thunder 0.2 0.5
  - 不穏な空気が漂っている...
  - /wait 60
  - /singlesound minecraft:block.note.bass 1 0.6
  - 何かが近づいてくる...

peaceful_scene:
  - /singlesound minecraft:block.note.flute 1 1.2
  - 穏やかな風が吹いている
  - /wait 40
  - /singlesound minecraft:entity.experience_orb.pickup 0.5 1.5
  - 鳥のさえずりが聞こえる

dramatic_moment:
  - /singlesound minecraft:entity.lightning_bolt.thunder 0.8 1
  - /wait 20
  - /singlesound minecraft:block.note.bass 1 0.5
  - 運命の瞬間が訪れた...
```

## ⚠️ 注意事項とベストプラクティス

### 音量の配慮

```yaml
# ✅ 適切な音量
good_volume:
  - /singlesound minecraft:entity.lightning_bolt.thunder 0.3 1
  # 大きな音は小さめに

# ❌ 音量が大きすぎる
bad_volume:
  - /singlesound minecraft:entity.lightning_bolt.thunder 1.0 1
  # プレイヤーがびっくりする可能性
```

### タイミングの調整

```yaml
# ✅ 適切なタイミング
good_timing:
  - 重要な発表があります
  - /singlesound minecraft:block.note.bell 1 1.5
  - 音と内容が合っている

# ❌ タイミングが悪い
bad_timing:
  - /singlesound minecraft:entity.player.levelup 1 1
  - 悲しいお知らせです
  # 音と内容が合わない
```

### 連続音声の制御

```yaml
# ✅ 適度な間隔
controlled_sequence:
  - /singlesound minecraft:block.note.bell 1 1
  - /wait 20
  - /singlesound minecraft:block.note.bell 1 1.2
  - /wait 20
  - /singlesound minecraft:block.note.bell 1 1.5

# ❌ 音が重なる
overlapping_sounds:
  - /singlesound minecraft:block.note.bell 1 1
  - /singlesound minecraft:block.note.bell 1 1.2
  - /singlesound minecraft:block.note.bell 1 1.5
  # 同時再生で音が混ざる
```

## 💡 高度なテクニック

### 変数による動的音声

```yaml
dynamic_sound:
  - /if mood = 1 /singlesound minecraft:block.note.flute 1 1.5
  - /if mood = 2 /singlesound minecraft:block.note.bass 1 0.8
  - /if mood = 3 /singlesound minecraft:entity.lightning_bolt.thunder 0.3 1
  - 気分に応じた音楽

health_based_sound:
  - /if hp > 50 /singlesound minecraft:entity.player.levelup 1 1
  - /if hp <= 50 & hp > 20 /singlesound minecraft:block.note.bass 1 1
  - /if hp <= 20 /singlesound minecraft:entity.villager.hurt 1 0.8
  - 体力に応じた効果音
```

### 音声のレイヤリング

```yaml
complex_audio:
  - /singlesound minecraft:block.note.bass 1 0.5
  - ベース音を流す
  - /wait 10
  - /singlesound minecraft:block.note.bell 0.7 1.2
  - メロディーを重ねる
  - /wait 10
  - /singlesound minecraft:entity.experience_orb.pickup 0.5 1.8
  - 装飾音を追加
```

## 🔗 関連ドキュメント

- **[基本設定](basic-settings.md)** - soundコマンドとの違い
- **[ゲーム制御](game-control.md)** - waitコマンドとの組み合わせ
- **[実用例 - ミニゲーム](../examples/mini-games.md)** - 音声を活用したゲーム
- **[リファレンス - 音声一覧](../reference/sound-list.md)** - 使用可能な音声リスト