# Item Management Commands

Commands for checking and removing items from player inventories.

## 🎒 `/has` - Item Possession Check

Executes subsequent commands or messages only when the player possesses the specified item.

### Basic Syntax

```yaml
/has <item_type> <quantity> <item_name> <execution_content>
```

### Parameters

- **item_type**: Minecraft item identifier (uppercase)
- **quantity**: Required quantity
- **item_name**: Custom name (use `none` for unnamed items)
- **execution_content**: Commands or message to execute when item is possessed

## 📦 Basic Usage Examples

### Simple Item Checks

```yaml
diamond_check:
  - /has DIAMOND 1 none You have a diamond!
  - /has EMERALD 3 none You have 3 or more emeralds
  - /has GOLD_INGOT 5 none You have 5 gold ingots

food_check:
  - /has BREAD 1 none You have bread
  - /has APPLE 1 none You have an apple
  - /has COOKED_BEEF 2 none You have 2 or more cooked beef
```

### Multiple Item Verification

```yaml
inventory_scan:
  - Checking inventory...
  - /has IRON_SWORD 1 none Iron Sword: ✓
  - /has IRON_HELMET 1 none Iron Helmet: ✓
  - /has SHIELD 1 none Shield: ✓
  - /has BOW 1 none Bow: ✓
```

## 🗑️ `/removeItem` - Item Removal

Removes specified items from the player's inventory.

### Basic Syntax

```yaml
/removeItem <item_type> <quantity> <item_name>
```

### Basic Usage Examples

```yaml
consume_items:
  - /removeItem BREAD 1 none
  - Consumed 1 bread
  - /removeItem EMERALD 5 none
  - Paid 5 emeralds
  - /removeItem DIAMOND 1 none
  - Used 1 diamond
```

## 🎯 Practical System Examples

### 1. Cooking System

```yaml
cooking_menu:
  - What would you like to make?
  - /? Bread Cake Steak Back
  - /?Bread /jump make_bread
  - /?Cake /jump make_cake
  - /?Steak /jump make_steak
  - /?Back /jump main_menu

make_bread:
  - /has WHEAT 3 none /jump craft_bread
  - You need 3 wheat

craft_bread:
  - /removeItem WHEAT 3 none
  - /command give %player% minecraft:bread 2
  - Made 2 bread from 3 wheat!

make_cake:
  - /has WHEAT 3 none /has SUGAR 2 none /has EGG 1 none /jump craft_cake
  - Missing ingredients
  - Required: 3 wheat, 2 sugar, 1 egg

craft_cake:
  - /removeItem WHEAT 3 none
  - /removeItem SUGAR 2 none
  - /removeItem EGG 1 none
  - /command give %player% minecraft:cake 1
  - Cake crafted!

make_steak:
  - /has RAW_BEEF 1 none /has COAL 1 none /jump craft_steak
  - Need 1 raw beef and 1 coal

craft_steak:
  - /removeItem RAW_BEEF 1 none
  - /removeItem COAL 1 none
  - /command give %player% minecraft:cooked_beef 1
  - Steak cooked!
```

### 2. Trading Post System

```yaml
item_shop:
  - Welcome to the Trading Post!
  - /? Trade Weapons Trade Armor Sell Materials Leave
  - /?Trade Weapons /jump weapon_exchange
  - /?Trade Armor /jump armor_exchange
  - /?Sell Materials /jump sell_materials
  - /?Leave Come back anytime

weapon_exchange:
  - What would you like to trade for?
  - /? Iron Sword(5 Iron) Diamond Sword(3 Diamonds) Bow(3 String + 3 Sticks) Back
  - /?Iron Sword(5 Iron) /jump trade_iron_sword
  - /?Diamond Sword(3 Diamonds) /jump trade_diamond_sword
  - /?Bow(3 String + 3 Sticks) /jump trade_bow
  - /?Back /jump item_shop

trade_iron_sword:
  - /has IRON_INGOT 5 none /jump exchange_iron_sword
  - You need 5 iron ingots

exchange_iron_sword:
  - /removeItem IRON_INGOT 5 none
  - /command give %player% minecraft:iron_sword 1
  - Traded for an iron sword!

trade_diamond_sword:
  - /has DIAMOND 3 none /jump exchange_diamond_sword
  - You need 3 diamonds

exchange_diamond_sword:
  - /removeItem DIAMOND 3 none
  - /command give %player% minecraft:diamond_sword 1
  - /color \6\l
  - Traded for a diamond sword!
  - /color \r

trade_bow:
  - /has STRING 3 none /has STICK 3 none /jump exchange_bow
  - You need 3 string and 3 sticks

exchange_bow:
  - /removeItem STRING 3 none
  - /removeItem STICK 3 none
  - /command give %player% minecraft:bow 1
  - Crafted a bow!

sell_materials:
  - What would you like to sell?
  - /? Cobblestone(1G each) Iron_Ore(5G each) Gold_Ore(10G each) Back
  - /?Cobblestone(1G each) /jump sell_cobblestone
  - /?Iron_Ore(5G each) /jump sell_iron_ore
  - /?Gold_Ore(10G each) /jump sell_gold_ore
  - /?Back /jump item_shop

sell_cobblestone:
  - /has COBBLESTONE 1 none /jump process_cobblestone_sale
  - You don't have any cobblestone

process_cobblestone_sale:
  - /removeItem COBBLESTONE 1 none
  - /score money + 1
  - Sold cobblestone for 1 gold!
  - /jump sell_materials

sell_iron_ore:
  - /has IRON_ORE 1 none /jump process_iron_sale
  - You don't have any iron ore

process_iron_sale:
  - /removeItem IRON_ORE 1 none
  - /score money + 5
  - Sold iron ore for 5 gold!
  - /jump sell_materials
```

### 3. Quest Item System

```yaml
quest_item_check:
  - Let me see what you've brought...
  - /has DIAMOND 1 none /jump check_emerald
  - You're missing the rare diamond

check_emerald:
  - /has EMERALD 3 none /jump check_golden_apple
  - You need 3 emeralds, not less

check_golden_apple:
  - /has GOLDEN_APPLE 1 none /jump all_items_present
  - Where is the golden apple?

all_items_present:
  - Perfect! You have everything I need
  - /removeItem DIAMOND 1 none
  - /removeItem EMERALD 3 none
  - /removeItem GOLDEN_APPLE 1 none
  - /score quest_complete 1
  - /score money + 1000
  - /command give %player% minecraft:enchanted_book 1
  - Quest completed! Here's your reward
```

### 4. Upgrade System

```yaml
weapon_upgrade:
  - Weapon Upgrade Service
  - Current weapon level: \\weapon_level\\
  - /? Upgrade_to_+1 Upgrade_to_+2 Upgrade_to_+3 Leave
  - /?Upgrade_to_+1 /jump upgrade_level_1
  - /?Upgrade_to_+2 /jump upgrade_level_2
  - /?Upgrade_to_+3 /jump upgrade_level_3
  - /?Leave /jump main_menu

upgrade_level_1:
  - /if weapon_level >= 1 Already at this level or higher
  - /if weapon_level >= 1 /jump weapon_upgrade
  - /has IRON_INGOT 3 none /jump process_upgrade_1
  - Need 3 iron ingots for +1 upgrade

process_upgrade_1:
  - /removeItem IRON_INGOT 3 none
  - /score weapon_level 1
  - /score attack_power + 5
  - Weapon upgraded to +1!
  - Attack power increased by 5
  - /jump weapon_upgrade

upgrade_level_2:
  - /if weapon_level < 1 Must upgrade to +1 first
  - /if weapon_level < 1 /jump weapon_upgrade
  - /if weapon_level >= 2 Already at this level or higher
  - /if weapon_level >= 2 /jump weapon_upgrade
  - /has DIAMOND 2 none /has GOLD_INGOT 5 none /jump process_upgrade_2
  - Need 2 diamonds and 5 gold ingots for +2 upgrade

process_upgrade_2:
  - /removeItem DIAMOND 2 none
  - /removeItem GOLD_INGOT 5 none
  - /score weapon_level 2
  - /score attack_power + 10
  - /color \6
  - Weapon upgraded to +2!
  - /color \r
  - Attack power increased by 10
  - /jump weapon_upgrade

upgrade_level_3:
  - /if weapon_level < 2 Must upgrade to +2 first
  - /if weapon_level < 2 /jump weapon_upgrade
  - /if weapon_level >= 3 Already at maximum level
  - /if weapon_level >= 3 /jump weapon_upgrade
  - /has NETHERITE_INGOT 1 none /has DIAMOND 5 none /jump process_upgrade_3
  - Need 1 netherite ingot and 5 diamonds for +3 upgrade

process_upgrade_3:
  - /removeItem NETHERITE_INGOT 1 none
  - /removeItem DIAMOND 5 none
  - /score weapon_level 3
  - /score attack_power + 20
  - /color \6\l
  - Weapon upgraded to +3! Maximum level reached!
  - /color \r
  - Attack power increased by 20
  - /singlesound minecraft:entity.player.levelup
```

### 5. Alchemy System

```yaml
alchemy_lab:
  - Welcome to the Alchemy Lab
  - /? Healing_Potion Strength_Potion Speed_Potion Check_Recipes Leave
  - /?Healing_Potion /jump brew_healing
  - /?Strength_Potion /jump brew_strength
  - /?Speed_Potion /jump brew_speed
  - /?Check_Recipes /jump recipe_book
  - /?Leave /jump exit_lab

brew_healing:
  - Healing Potion Recipe:
  - 2 Red Mushrooms + 1 Golden Apple + 1 Glass Bottle
  - /has RED_MUSHROOM 2 none /has GOLDEN_APPLE 1 none /has GLASS_BOTTLE 1 none /jump craft_healing
  - Missing ingredients for healing potion

craft_healing:
  - /removeItem RED_MUSHROOM 2 none
  - /removeItem GOLDEN_APPLE 1 none
  - /removeItem GLASS_BOTTLE 1 none
  - /command give %player% minecraft:potion 1
  - /color \a
  - Healing Potion brewed successfully!
  - /color \r
  - /jump alchemy_lab

brew_strength:
  - Strength Potion Recipe:
  - 3 Blaze Powder + 1 Spider Eye + 1 Glass Bottle
  - /has BLAZE_POWDER 3 none /has SPIDER_EYE 1 none /has GLASS_BOTTLE 1 none /jump craft_strength
  - Missing ingredients for strength potion

craft_strength:
  - /removeItem BLAZE_POWDER 3 none
  - /removeItem SPIDER_EYE 1 none
  - /removeItem GLASS_BOTTLE 1 none
  - /command give %player% minecraft:potion 1
  - /color \c
  - Strength Potion brewed successfully!
  - /color \r
  - /jump alchemy_lab

recipe_book:
  - === Alchemy Recipes ===
  - Healing Potion: 2 Red Mushroom + 1 Golden Apple + 1 Glass Bottle
  - Strength Potion: 3 Blaze Powder + 1 Spider Eye + 1 Glass Bottle
  - Speed Potion: 2 Sugar + 1 Rabbit's Foot + 1 Glass Bottle
  - /jump alchemy_lab
```

## 💡 Advanced Techniques

### Inventory Management

```yaml
inventory_check:
  - Checking inventory space...
  - /score inventory_count 0
  - /has DIRT 1 none /add inventory_count
  - /has STONE 1 none /add inventory_count
  - /has IRON_INGOT 1 none /add inventory_count
  - /if inventory_count > 25 /jump inventory_full
  - /jump inventory_ok

inventory_full:
  - Your inventory is nearly full!
  - Consider selling some items

bulk_operations:
  - Processing bulk sale...
  - /has COBBLESTONE 64 none /jump sell_stack_cobblestone
  - /has COBBLESTONE 32 none /jump sell_half_stack_cobblestone
  - /has COBBLESTONE 1 none /jump sell_individual_cobblestone
```

### Conditional Crafting

```yaml
smart_crafting:
  - /has IRON_INGOT 9 none /jump can_craft_block
  - /has IRON_INGOT 3 none /jump can_craft_bucket
  - /has IRON_INGOT 1 none /jump can_craft_nuggets
  - No iron available for crafting

can_craft_block:
  - You can craft an Iron Block (9 ingots)
  - /? Craft_Block Check_Other_Options
  - /?Craft_Block /jump craft_iron_block
  - /?Check_Other_Options /jump can_craft_bucket

craft_iron_block:
  - /removeItem IRON_INGOT 9 none
  - /command give %player% minecraft:iron_block 1
  - Iron Block crafted!
```

## ⚠️ Important Notes

### Item Type Guidelines

1. **Case Sensitivity**: Use UPPERCASE for item types (`DIAMOND`, not `diamond`)
2. **Exact Names**: Must match Minecraft's internal item names exactly
3. **Custom Names**: Use `none` for items without custom names

### Common Item Types

| Category      | Item Types                                       |
| ------------- | ------------------------------------------------ |
| **Ores**      | `DIAMOND`, `EMERALD`, `IRON_INGOT`, `GOLD_INGOT` |
| **Food**      | `BREAD`, `APPLE`, `COOKED_BEEF`, `GOLDEN_APPLE`  |
| **Tools**     | `IRON_SWORD`, `DIAMOND_PICKAXE`, `BOW`           |
| **Materials** | `STONE`, `COBBLESTONE`, `WHEAT`, `STRING`        |

### Error Prevention

```yaml
# ✅ Correct usage
- /has DIAMOND 1 none Message
- /removeItem BREAD 1 none

# ❌ Common mistakes
- /has diamond 1 none Message # Wrong case
- /has DIAMOND none 1 Message # Wrong parameter order
```

## 🔗 Related Documentation

- **[Variables](variables.md)** - Tracking item counts with variables
- **[Conditions](conditions.md)** - Complex item-based logic
- **[Game Control](game-control.md)** - Giving items with /command
- **[Practical Examples](../examples/README.md)** - Advanced item systems
