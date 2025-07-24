# アイテム管理コマンド

プレイヤーのインベントリ内のアイテムをチェックしたり、削除したりするコマンドです。

## 🎒 `/has` - アイテム所持チェック

プレイヤーが指定したアイテムを所持している場合にのみ、後続のコマンドまたはメッセージを実行します。

### 基本構文
```yaml
/has <アイテム種類> <数量> <アイテム名> <実行内容>
```

### パラメータ
- **アイテム種類**: Minecraftのアイテム識別子（大文字）
- **数量**: 必要な個数
- **アイテム名**: カスタム名（名前なしの場合は`none`）
- **実行内容**: 所持している場合に実行するコマンドまたはメッセージ

## 📦 基本的な使用例

### シンプルなアイテムチェック

```yaml
diamond_check:
  - /has DIAMOND 1 none ダイヤモンドを持っていますね！
  - /has EMERALD 3 none エメラルドを3個以上持っています
  - /has GOLD_INGOT 5 none 金インゴットが5個あります

food_check:
  - /has BREAD 1 none パンを持っています
  - /has APPLE 1 none リンゴがありますね
  - /has COOKED_BEEF 2 none 焼いた牛肉が2個以上あります
```

### 複数アイテムの確認

```yaml
inventory_scan:
  - インベントリをチェックしています...
  - /has IRON_SWORD 1 none 鉄の剣: ✓
  - /has IRON_HELMET 1 none 鉄のヘルメット: ✓
  - /has SHIELD 1 none 盾: ✓
  - /has BOW 1 none 弓: ✓
```

## 🗑️ `/removeItem` - アイテム削除

プレイヤーのインベントリから指定したアイテムを削除します。

### 基本構文
```yaml
/removeItem <アイテム種類> <数量> <アイテム名>
```

### 基本的な使用例

```yaml
consume_items:
  - /removeItem BREAD 1 none
  - パンを1個消費しました
  - /removeItem EMERALD 5 none
  - エメラルドを5個支払いました
  - /removeItem DIAMOND 1 none
  - ダイヤモンドを1個使用しました
```

## 🎯 実用的なシステム例

### 1. 料理システム

```yaml
cooking_menu:
  - 何を作りますか？
  - /? パン ケーキ ステーキ 戻る
  - /?パン /jump make_bread
  - /?ケーキ /jump make_cake  
  - /?ステーキ /jump make_steak
  - /?戻る /jump main_menu

make_bread:
  - /has WHEAT 3 none /jump craft_bread
  - 小麦が3個必要です

craft_bread:
  - /removeItem WHEAT 3 none
  - /command give %player% minecraft:bread 2
  - 小麦3個からパン2個を作りました！

make_cake:
  - /has WHEAT 3 none /has SUGAR 2 none /has EGG 1 none /jump craft_cake
  - 材料が足りません
  - 必要: 小麦3個、砂糖2個、卵1個

craft_cake:
  - /removeItem WHEAT 3 none
  - /removeItem SUGAR 2 none
  - /removeItem EGG 1 none
  - /command give %player% minecraft:cake 1
  - ケーキを作りました！

make_steak:
  - /has RAW_BEEF 1 none /has COAL 1 none /jump craft_steak
  - 生の牛肉1個と石炭1個が必要です

craft_steak:
  - /removeItem RAW_BEEF 1 none
  - /removeItem COAL 1 none
  - /command give %player% minecraft:cooked_beef 1
  - ステーキを焼きました！
```

### 2. 商店システム

```yaml
item_shop:
  - アイテム交換所へようこそ！
  - /? 武器を交換 防具を交換 材料を売る 帰る
  - /?武器を交換 /jump weapon_exchange
  - /?防具を交換 /jump armor_exchange
  - /?材料を売る /jump sell_materials
  - /?帰る また来てください

weapon_exchange:
  - 何と交換しますか？
  - /? 鉄の剣(鉄5個) ダイヤの剣(ダイヤ3個) 弓(糸3個+棒3個) 戻る
  - /?鉄の剣(鉄5個) /jump trade_iron_sword
  - /?ダイヤの剣(ダイヤ3個) /jump trade_diamond_sword
  - /?弓(糸3個+棒3個) /jump trade_bow
  - /?戻る /jump item_shop

trade_iron_sword:
  - /has IRON_INGOT 5 none /jump exchange_iron_sword
  - 鉄インゴットが5個必要です

exchange_iron_sword:
  - /removeItem IRON_INGOT 5 none
  - /command give %player% minecraft:iron_sword 1
  - 鉄の剣と交換しました！

trade_diamond_sword:
  - /has DIAMOND 3 none /jump exchange_diamond_sword
  - ダイヤモンドが3個必要です

exchange_diamond_sword:
  - /removeItem DIAMOND 3 none
  - /command give %player% minecraft:diamond_sword 1
  - /color \6\l
  - ダイヤの剣と交換しました！
  - /color \r

trade_bow:
  - /has STRING 3 none /has STICK 3 none /jump exchange_bow
  - 糸3個と棒3個が必要です

exchange_bow:
  - /removeItem STRING 3 none
  - /removeItem STICK 3 none
  - /command give %player% minecraft:bow 1
  - 弓と交換しました！
```

### 3. クエストアイテムシステム

```yaml
quest_delivery:
  - 依頼の品は持参されましたか？
  - /? アイテムを渡す 確認する 帰る
  - /?アイテムを渡す /jump check_quest_items
  - /?確認する /jump show_requirements
  - /?帰る また後でお越しください

show_requirements:
  - 必要なアイテム：
  - • 赤い花 x5
  - • 青い羊毛 x3  
  - • 金インゴット x2
  - /jump quest_delivery

check_quest_items:
  - /has RED_TULIP 5 none /has BLUE_WOOL 3 none /has GOLD_INGOT 2 none /jump complete_delivery
  - アイテムが足りません
  - /jump show_requirements

complete_delivery:
  - /removeItem RED_TULIP 5 none
  - /removeItem BLUE_WOOL 3 none
  - /removeItem GOLD_INGOT 2 none
  - /score quest_delivery_complete 1
  - /score money + 500
  - /score exp + 200
  - /color \a\l
  - クエスト完了！ありがとうございます！
  - /color \r
  - 報酬: 500ゴールド、200経験値
```

### 4. 入場料・アクセス制御

```yaml
dungeon_entrance:
  - 古代のダンジョンの入り口です
  - /has ANCIENT_KEY 1 none /jump enter_dungeon
  - 入場には古代の鍵が必要です

enter_dungeon:
  - 古代の鍵を使用しますか？
  - /? 使用する やめる
  - /?使用する /jump use_key
  - /?やめる また今度にしましょう

use_key:
  - /removeItem ANCIENT_KEY 1 none
  - /color \d\l
  - 古代の鍵が光り、扉が開きました...
  - /color \r
  - /jump dungeon_interior

vip_area:
  - VIPエリアへようこそ
  - /has GOLD_PASS 1 none /jump vip_access
  - ゴールドパス会員限定エリアです

vip_access:
  - /color \6\l
  - ゴールドパス会員様、いらっしゃいませ！
  - /color \r
  - 特別なサービスをご利用いただけます
```

### 5. 回復・消費システム

```yaml
rest_area:
  - 休憩エリアです
  - /? 体力回復 魔力回復 状態異常治療 帰る
  - /?体力回復 /jump heal_hp
  - /?魔力回復 /jump heal_mp
  - /?状態異常治療 /jump cure_status
  - /?帰る /jump exit_rest

heal_hp:
  - /has HEALING_POTION 1 none /jump use_heal_potion
  - 回復ポーションが必要です

use_heal_potion:
  - /removeItem HEALING_POTION 1 none
  - /score hp + 50
  - /if hp > max_hp /score hp max_hp
  - /color \a
  - 体力が50回復しました！
  - /color \r
  - 現在HP: \\hp\\/\\max_hp\\

heal_mp:
  - /has MANA_POTION 1 none /jump use_mana_potion
  - マナポーションが必要です

use_mana_potion:
  - /removeItem MANA_POTION 1 none
  - /score mp + 30
  - /if mp > max_mp /score mp max_mp
  - /color \b
  - 魔力が30回復しました！
  - /color \r
  - 現在MP: \\mp\\/\\max_mp\\

cure_status:
  - /has ANTIDOTE 1 none /jump use_antidote
  - 解毒剤が必要です

use_antidote:
  - /removeItem ANTIDOTE 1 none
  - /score poison_status 0
  - /score paralysis_status 0
  - /color \a
  - 状態異常が治りました！
  - /color \r
```

## 🏷️ カスタム名付きアイテム

### カスタム名の指定

```yaml
# 名前付きアイテムのチェック
special_sword_check:
  - /has IRON_SWORD 1 "勇者の剣" 勇者の剣を持っていますね！
  - /has DIAMOND 1 "純粋なダイヤ" 純粋なダイヤを発見！

magic_items:
  - /has STICK 1 "魔法の杖" 魔法の杖を所持しています
  - /has BOOK 1 "古代の書物" 古代の書物を持参されましたね

# カスタム名付きアイテムの削除
use_special_item:
  - /removeItem GOLDEN_APPLE 1 "生命の果実"
  - 生命の果実を使用しました！
  - /score hp + 100
  - /score max_hp + 10
```

## ⚠️ 注意事項とベストプラクティス

### アイテム種類の指定

```yaml
# ✅ 正しいアイテム識別子（大文字）
- /has DIAMOND 1 none
- /has IRON_SWORD 1 none
- /has COOKED_BEEF 1 none

# ❌ 間違った識別子
- /has diamond 1 none      # 小文字
- /has iron_sword 1 none   # 小文字
- /has beef 1 none         # 正式名称ではない
```

### 数量の指定

```yaml
# 指定数量以上を所持していればチェック成功
- /has EMERALD 5 none 5個以上持っています
- /has BREAD 1 none 1個以上あります

# 正確な数量をチェックしたい場合は変数を使用
exact_count_check:
  - /score emerald_count 0
  - /has EMERALD 1 none /add emerald_count
  - /has EMERALD 2 none /add emerald_count  
  - /has EMERALD 3 none /add emerald_count
  # ... 以下同様に数える
```

### エラーハンドリング

```yaml
safe_removal:
  - /has DIAMOND 1 none /jump remove_diamond
  - ダイヤモンドを持っていません

remove_diamond:
  - /removeItem DIAMOND 1 none
  - ダイヤモンドを使用しました

trade_safety:
  - /has EMERALD 10 none /has GOLD_INGOT 5 none /jump proceed_trade
  - 交換に必要なアイテムが足りません
  - 必要: エメラルド10個、金インゴット5個

proceed_trade:
  - /removeItem EMERALD 10 none
  - /removeItem GOLD_INGOT 5 none
  - /command give %player% minecraft:diamond 1
  - 交換完了！ダイヤモンドを獲得しました
```

## 💡 高度なテクニック

### アイテム変数との連携

```yaml
item_counter:
  - /score item_count 0
  - /has DIAMOND 1 none /add item_count
  - /has EMERALD 1 none /add item_count
  - /has GOLD_INGOT 1 none /add item_count
  - 貴重なアイテム数: \\item_count\\個

trading_system:
  - /score trade_value 0
  - /has DIAMOND 1 none /score trade_value + 100
  - /has EMERALD 1 none /score trade_value + 50
  - /has GOLD_INGOT 1 none /score trade_value + 30
  - 総取引価値: \\trade_value\\ゴールド相当
```

### 複合的なアイテム要求

```yaml
complex_recipe:
  - 特別な装備を作るには：
  - /has DIAMOND 3 none /has NETHERITE_INGOT 1 none /has ENCHANTED_BOOK 1 none /jump craft_legendary
  - 材料が不足しています

craft_legendary:
  - /removeItem DIAMOND 3 none
  - /removeItem NETHERITE_INGOT 1 none
  - /removeItem ENCHANTED_BOOK 1 none
  - /command give %player% minecraft:netherite_sword 1
  - /command enchant %player% minecraft:sharpness 5
  - /color \6\l
  - 伝説の剣を作成しました！
  - /color \r
```

## 🔗 関連ドキュメント

- **[ゲーム制御](game-control.md)** - commandでアイテム付与
- **[条件分岐](conditions.md)** - アイテム所持による分岐
- **[変数・スコア管理](variables.md)** - アイテム数の記録
- **[実用例 - ショップシステム](../examples/shop-system.md)** - アイテム売買の例