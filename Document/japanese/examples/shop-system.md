# ショップシステム

RPGTextを使った本格的なアイテム売買システムの実装例です。

## 📋 概要

このサンプルでは以下の機能を実装します：

- アイテムの購入・売却システム
- 所持金・インベントリ管理
- カテゴリ別商品表示
- 価格交渉システム
- 在庫管理
- 会員ランクシステム

## 💻 完全なコード

### shop_system.yml

```yaml
# 総合ショップシステム
# ファイル共通設定
sound: block.note.bell 1 1
speed: 30
color: \r

# ショップ入場
shop_entrance:
  - /singlesound minecraft:block.chest.open 1 1
  - /color \6\l
  - *** 冒険者の店 ***
  - /color \r
  - いらっしゃいませ、%player%さん！
  - /wait 20
  - 所持金: \\money\\ゴールド
  - /if member_rank > 0 /jump member_greeting
  - /jump first_time_greeting

# 初回来店
first_time_greeting:
  - 初めてのご来店ですね。
  - 当店では冒険に必要な様々なアイテムを
  - 取り扱っております。
  - /score member_rank 1
  - /jump main_menu

# 会員向け挨拶
member_greeting:
  - /if member_rank = 1 いつもありがとうございます！
  - /if member_rank = 2 /color \a
  - /if member_rank = 2 シルバー会員様、いらっしゃいませ！
  - /if member_rank = 2 /color \r
  - /if member_rank >= 3 /color \6\l
  - /if member_rank >= 3 ゴールド会員様、お待ちしておりました！
  - /if member_rank >= 3 /color \r
  - /jump main_menu

# メインメニュー
main_menu:
  - 何をご希望でしょうか？
  - /? 商品を購入 アイテム売却 会員特典 店を出る
  - /?商品を購入 /jump purchase_menu
  - /?アイテム売却 /jump sell_menu
  - /?会員特典 /jump member_benefits
  - /?店を出る /jump shop_farewell

# 購入メニュー
purchase_menu:
  - /singlesound minecraft:entity.experience_orb.pickup 1 1
  - どちらのカテゴリをご覧になりますか？
  - /? 武器・防具 回復アイテム 道具類 特別商品 戻る
  - /?武器・防具 /jump weapons_armor
  - /?回復アイテム /jump healing_items
  - /?道具類 /jump tools_items
  - /?特別商品 /jump special_items
  - /?戻る /jump main_menu

# 武器・防具
weapons_armor:
  - /color \c\l
  - === 武器・防具 ===
  - /color \r
  - /? 木の剣(50G) 鉄の剣(200G) ダイヤの剣(1000G) 革の帽子(30G) 戻る
  - /?木の剣(50G) /jump buy_wood_sword
  - /?鉄の剣(200G) /jump buy_iron_sword
  - /?ダイヤの剣(1000G) /jump buy_diamond_sword
  - /?革の帽子(30G) /jump buy_leather_cap
  - /?戻る /jump purchase_menu

buy_wood_sword:
  - /if money >= 50 /jump confirm_wood_sword
  - /singlesound minecraft:entity.villager.no 1 1
  - /color \c
  - 申し訳ございません、所持金が足りません。
  - /color \r
  - /jump weapons_armor

confirm_wood_sword:
  - 木の剣（攻撃力+3）を50ゴールドで購入しますか？
  - /? 購入する やめる
  - /?購入する /jump purchase_wood_sword
  - /?やめる /jump weapons_armor

purchase_wood_sword:
  - /score money - 50
  - /command give %player% minecraft:wooden_sword 1
  - /score purchase_count + 1
  - /singlesound minecraft:entity.player.levelup 1 1
  - /color \a
  - 木の剣をご購入いただき、ありがとうございます！
  - /color \r
  - 残金: \\money\\ゴールド
  - /jump check_rank_up

buy_iron_sword:
  - /if money >= 200 /jump confirm_iron_sword
  - /singlesound minecraft:entity.villager.no 1 1
  - /color \c
  - 申し訳ございません、所持金が足りません。
  - /color \r
  - /jump weapons_armor

confirm_iron_sword:
  - 鉄の剣（攻撃力+6）を購入しますか？
  - /if member_rank >= 2 180ゴールド（会員割引）
  - /if member_rank < 2 200ゴールド
  - /? 購入する やめる
  - /?購入する /jump purchase_iron_sword
  - /?やめる /jump weapons_armor

purchase_iron_sword:
  - /if member_rank >= 2 /score money - 180
  - /if member_rank < 2 /score money - 200
  - /command give %player% minecraft:iron_sword 1
  - /score purchase_count + 1
  - /singlesound minecraft:entity.player.levelup 1 1
  - /color \a
  - 鉄の剣をご購入いただき、ありがとうございます！
  - /color \r
  - /if member_rank >= 2 会員割引が適用されました
  - 残金: \\money\\ゴールド
  - /jump check_rank_up

buy_diamond_sword:
  - /if money >= 1000 /jump confirm_diamond_sword
  - /singlesound minecraft:entity.villager.no 1 1
  - /color \c
  - 申し訳ございません、所持金が足りません。
  - /color \r
  - こちらは高級品でございます...
  - /jump weapons_armor

confirm_diamond_sword:
  - /color \6\l
  - ダイヤの剣（攻撃力+12）
  - /color \r
  - /if member_rank >= 3 800ゴールド（ゴールド会員特価）
  - /if member_rank = 2 900ゴールド（シルバー会員価格）
  - /if member_rank < 2 1000ゴールド
  - /? 購入する やめる
  - /?購入する /jump purchase_diamond_sword
  - /?やめる /jump weapons_armor

purchase_diamond_sword:
  - /if member_rank >= 3 /score money - 800
  - /if member_rank = 2 /score money - 900
  - /if member_rank < 2 /score money - 1000
  - /command give %player% minecraft:diamond_sword 1
  - /score purchase_count + 1
  - /singlesound minecraft:entity.player.levelup 1 1.2
  - /color \6\l
  - 最高級のダイヤの剣です！
  - /color \r
  - /if member_rank >= 2 特別価格でご提供させていただきました
  - 残金: \\money\\ゴールド
  - /jump check_rank_up

buy_leather_cap:
  - /if money >= 30 /jump confirm_leather_cap
  - /singlesound minecraft:entity.villager.no 1 1
  - /color \c
  - 申し訳ございません、所持金が足りません。
  - /color \r
  - /jump weapons_armor

confirm_leather_cap:
  - 革の帽子（防御力+1）を30ゴールドで購入しますか？
  - /? 購入する やめる
  - /?購入する /jump purchase_leather_cap
  - /?やめる /jump weapons_armor

purchase_leather_cap:
  - /score money - 30
  - /command give %player% minecraft:leather_helmet 1
  - /score purchase_count + 1
  - /singlesound minecraft:entity.experience_orb.pickup 1 1
  - 革の帽子をご購入いただき、ありがとうございます！
  - 残金: \\money\\ゴールド
  - /jump check_rank_up

# 回復アイテム
healing_items:
  - /color \a\l
  - === 回復アイテム ===
  - /color \r
  - /? 回復薬(20G) 高級回復薬(50G) パン(5G) 戻る
  - /?回復薬(20G) /jump buy_heal_potion
  - /?高級回復薬(50G) /jump buy_strong_heal
  - /?パン(5G) /jump buy_bread
  - /?戻る /jump purchase_menu

buy_heal_potion:
  - /if money >= 20 /jump confirm_heal_potion
  - /singlesound minecraft:entity.villager.no 1 1
  - /color \c
  - 申し訳ございません、所持金が足りません。
  - /color \r
  - /jump healing_items

confirm_heal_potion:
  - 回復薬（体力30回復）を20ゴールドで購入しますか？
  - /? 購入する 5個まとめ買い(90G) やめる
  - /?購入する /jump purchase_single_heal
  - /?5個まとめ買い(90G) /jump purchase_bulk_heal
  - /?やめる /jump healing_items

purchase_single_heal:
  - /score money - 20
  - /command give %player% minecraft:potion{Potion:"minecraft:healing"} 1
  - /score purchase_count + 1
  - /singlesound minecraft:entity.experience_orb.pickup 1 1
  - 回復薬をご購入いただき、ありがとうございます！
  - 残金: \\money\\ゴールド
  - /jump check_rank_up

purchase_bulk_heal:
  - /if money >= 90 /jump bulk_heal_success
  - /singlesound minecraft:entity.villager.no 1 1
  - /color \c
  - まとめ買いには90ゴールド必要です。
  - /color \r
  - /jump healing_items

bulk_heal_success:
  - /score money - 90
  - /command give %player% minecraft:potion{Potion:"minecraft:healing"} 5
  - /score purchase_count + 5
  - /singlesound minecraft:entity.player.levelup 1 1
  - /color \a
  - 回復薬5個セット！お得にご購入いただけました。
  - /color \r
  - 残金: \\money\\ゴールド
  - /jump check_rank_up

# 売却メニュー
sell_menu:
  - /singlesound minecraft:block.chest.close 1 1
  - /color \e\l
  - === アイテム買取 ===
  - /color \r
  - どちらをお売りになりますか？
  - /? 武器類 貴重品 素材 戻る
  - /?武器類 /jump sell_weapons
  - /?貴重品 /jump sell_valuables
  - /?素材 /jump sell_materials
  - /?戻る /jump main_menu

sell_weapons:
  - 買取価格（通常価格の70%）
  - /? 木の剣(35G) 鉄の剣(140G) ダイヤの剣(700G) 戻る
  - /?木の剣(35G) /jump sell_wood_sword
  - /?鉄の剣(140G) /jump sell_iron_sword
  - /?ダイヤの剣(700G) /jump sell_diamond_sword
  - /?戻る /jump sell_menu

sell_wood_sword:
  - /has WOODEN_SWORD 1 none /jump confirm_sell_wood_sword
  - 木の剣をお持ちでないようです。

confirm_sell_wood_sword:
  - 木の剣を35ゴールドで買い取らせていただきます。
  - /? 売却する やめる
  - /?売却する /jump execute_sell_wood_sword
  - /?やめる /jump sell_weapons

execute_sell_wood_sword:
  - /removeItem WOODEN_SWORD 1 none
  - /score money + 35
  - /singlesound minecraft:entity.experience_orb.pickup 1 1.2
  - /color \a
  - 買い取らせていただきました！
  - /color \r
  - 所持金: \\money\\ゴールド
  - /jump sell_weapons

sell_valuables:
  - 貴重品の買取価格
  - /? ダイヤモンド(100G) エメラルド(80G) 金インゴット(50G) 戻る
  - /?ダイヤモンド(100G) /jump sell_diamond
  - /?エメラルド(80G) /jump sell_emerald
  - /?金インゴット(50G) /jump sell_gold
  - /?戻る /jump sell_menu

sell_diamond:
  - /has DIAMOND 1 none /jump confirm_sell_diamond
  - ダイヤモンドをお持ちでないようです。

confirm_sell_diamond:
  - ダイヤモンドを何個売却されますか？
  - 所持数をチェック中...
  - /? 1個(100G) 5個(500G) 全部 やめる
  - /?1個(100G) /jump sell_diamond_1
  - /?5個(500G) /jump sell_diamond_5
  - /?全部 /jump sell_diamond_all
  - /?やめる /jump sell_valuables

sell_diamond_1:
  - /removeItem DIAMOND 1 none
  - /score money + 100
  - /singlesound minecraft:entity.experience_orb.pickup 1 1.5
  - ダイヤモンド1個を買い取らせていただきました！
  - 所持金: \\money\\ゴールド
  - /jump sell_valuables

# 会員特典
member_benefits:
  - /color \6\l
  - === 会員特典 ===
  - /color \r
  - 現在のランク: 
  - /if member_rank = 1 一般会員
  - /if member_rank = 2 /color \a
  - /if member_rank = 2 シルバー会員
  - /if member_rank = 2 /color \r
  - /if member_rank >= 3 /color \6
  - /if member_rank >= 3 ゴールド会員
  - /if member_rank >= 3 /color \r
  - 購入回数: \\purchase_count\\回
  - /jump show_benefits

show_benefits:
  - /if member_rank = 1 次回ランクアップまで: あと\\rank_up_needed\\回購入
  - /if member_rank = 2 【特典】商品10%割引
  - /if member_rank >= 3 【特典】商品20%割引＋特別商品購入可能
  - /? 特典商品 戻る
  - /?特典商品 /jump special_items
  - /?戻る /jump main_menu

# 特別商品
special_items:
  - /if member_rank >= 3 /jump gold_special_items
  - /color \c
  - ゴールド会員様限定商品です。
  - /color \r
  - ランクアップしてからお越しください。
  - /jump main_menu

gold_special_items:
  - /color \6\l
  - === ゴールド会員限定 ===
  - /color \r
  - /? 魔法の剣(2000G) 不死のポーション(1500G) 戻る
  - /?魔法の剣(2000G) /jump buy_magic_sword
  - /?不死のポーション(1500G) /jump buy_immortal_potion
  - /?戻る /jump main_menu

# ランクアップチェック
check_rank_up:
  - /if member_rank = 1 & purchase_count >= 5 /jump rank_up_silver
  - /if member_rank = 2 & purchase_count >= 15 /jump rank_up_gold
  - /jump continue_shopping

rank_up_silver:
  - /score member_rank 2
  - /singlesound minecraft:entity.player.levelup 1 1.5
  - /color \a\l
  - おめでとうございます！
  - シルバー会員にランクアップしました！
  - /color \r
  - 今後は商品を10%割引でご購入いただけます。
  - /jump continue_shopping

rank_up_gold:
  - /score member_rank 3
  - /singlesound minecraft:entity.player.levelup 1 2
  - /color \6\l
  - おめでとうございます！
  - ゴールド会員にランクアップしました！
  - /color \r
  - 20%割引＋限定商品をご購入いただけます！
  - /jump continue_shopping

continue_shopping:
  - /? 他の商品を見る メインメニュー 店を出る
  - /?他の商品を見る /jump purchase_menu
  - /?メインメニュー /jump main_menu
  - /?店を出る /jump shop_farewell

# お別れ
shop_farewell:
  - /singlesound minecraft:entity.villager.yes 1 1
  - /color \6
  - ありがとうございました！
  - /color \r
  - /if member_rank >= 2 また特別価格でお待ちしております
  - /if member_rank < 2 またのお越しをお待ちしております
  - 良い冒険を！
```

## 📖 解説

### 1. 会員ランクシステム
```yaml
/if member_rank = 1  # 一般会員
/if member_rank = 2  # シルバー会員（10%割引）
/if member_rank >= 3 # ゴールド会員（20%割引＋限定商品）
```

### 2. 動的価格システム
```yaml
/if member_rank >= 2 /score money - 180  # 会員価格
/if member_rank < 2 /score money - 200   # 通常価格
```

### 3. 在庫管理
```yaml
/has DIAMOND 1 none /jump confirm_sell_diamond
```
プレイヤーが実際にアイテムを持っているかチェック

### 4. まとめ買いシステム
```yaml
/?5個まとめ買い(90G) /jump purchase_bulk_heal
```
単品購入より安い価格設定

### 5. 進行状況管理
```yaml
/score purchase_count + 1
```
購入回数でランクアップを管理

## 🎨 カスタマイズ案

### 季節限定商品
```yaml
seasonal_items:
  - /if season = spring /jump spring_items
  - /if season = summer /jump summer_items
  - 季節限定商品はございません
```

### 在庫数限定
```yaml
limited_stock:
  - /if rare_item_stock > 0 /jump rare_item_available
  - /color \c
  - 申し訳ございません、売り切れです
  - /color \r
```

### 価格交渉システム
```yaml
price_negotiation:
  - /? 定価で購入 値引き交渉
  - /?値引き交渉 /jump negotiate_price

negotiate_price:
  - /score negotiation_skill random 100
  - /if negotiation_skill > 70 /jump negotiation_success
  - 申し訳ございませんが、その価格では...
```

### レベル制限
```yaml
level_requirement:
  - /if level >= 10 /jump high_level_items
  - このアイテムはレベル10以上の方限定です
```

## 🔗 使用している機能

- **[変数・スコア管理](../commands/variables.md)** - money, member_rank, purchase_count
- **[条件分岐](../commands/conditions.md)** - 複雑な価格・在庫判定
- **[アイテム管理](../commands/items.md)** - 売買システム
- **[ゲーム制御](../commands/game-control.md)** - Minecraftコマンドでアイテム付与
- **[選択肢・インタラクション](../commands/interaction.md)** - メニューシステム
- **[音声制御](../commands/audio.md)** - 取引時の効果音

## 💡 学習ポイント

1. **複雑な条件分岐** - 会員ランクによる価格変動
2. **状態の永続化** - プレイヤー毎の会員情報保存
3. **エラーハンドリング** - 所持金・アイテム不足への対応
4. **ユーザー体験** - 直感的なメニュー構造
5. **ゲームバランス** - 適切な価格設定とインセンティブ

このシステムを基に、より複雑な経済システムや特殊な商品を扱うショップを作成してみてください！