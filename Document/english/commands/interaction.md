# Choice & Interaction Commands

Commands for displaying choices and handling player interactions.

## 🎯 `/? <choices>` - Display Choices

Display choices to the player and wait for selection.

### Syntax

```yaml
/? <choice1> <choice2> <choice3> ...
```

### Basic Usage Examples

```yaml
simple_choice:
  - Which will you choose?
  - /? Yes No

multiple_choice:
  - What's your favorite color?
  - /? Red Blue Green Yellow Purple

many_options:
  - Choose a number
  - /? 1 2 3 4 5 6 7 8 9 10
```

### Choice Operation Methods

- **Sneak (Shift)**: Change selection
- **F Key**: Change selection (default setting)
- **Left Click**: Confirm selection

## 🔀 `/?<choice_name>` - Branch Processing

Specify commands or text to execute based on the selected choice.

### Syntax

```yaml
/?<choice_name> <execution_content>
```

### Basic Branching

```yaml
basic_branch:
  - How are you feeling today?
  - /? Good Bad Normal
  - /?Good That's wonderful!
  - /?Bad Are you alright?
  - /?Normal I see

conversation_end:
  - See you again
```

### Command Execution

```yaml
action_choice:
  - What would you like to do?
  - /? Rest Shopping Adventure Exit
  - /?Rest /jump rest_area
  - /?Shopping /jump shop
  - /?Adventure /jump adventure_start
  - /?Exit /jump game_end
```

## 🎮 Practical Examples

### 1. Simple Conversation System

```yaml
greeting:
  - /color \2
  - Hello, %player%!
  - /color \r
  - What brings you here today?
  - /? Just saying hello I have business Just passing by
  - /?Just saying hello /jump friendly_chat
  - /?I have business /jump business_talk
  - /?Just passing by /jump casual_talk

friendly_chat:
  - /color \a
  - How nice! You look well.
  - /color \r
  - /jump farewell

business_talk:
  - /color \6
  - Business, I see. How can I help you?
  - /color \r
  - /? Quest Item Trade Information
  - /?Quest /jump quest_menu
  - /?Item Trade /jump shop_menu
  - /?Information /jump info_menu

casual_talk:
  - Yes, it's beautiful weather today.
  - /jump farewell

farewell:
  - See you again!
```

### 2. Shop System

```yaml
shop_entrance:
  - /color \6\l
  - Welcome!
  - /color \r
  - What are you looking for?
  - Money: \\money\\ Gold
  - /? Weapon(100G) Armor(80G) Potion(20G) Leave
  - /?Weapon(100G) /jump buy_weapon
  - /?Armor(80G) /jump buy_armor
  - /?Potion(20G) /jump buy_potion
  - /?Leave /jump shop_exit

buy_weapon:
  - /if money >= 100 /jump weapon_purchase
  - /color \c
  - Not enough money!
  - /color \r
  - /jump shop_entrance

weapon_purchase:
  - /score money - 100
  - /command give %player% minecraft:iron_sword
  - /color \a
  - Sword purchased!
  - /color \r
  - Remaining: \\money\\ Gold
  - /jump shop_entrance

shop_exit:
  - Thank you for your visit!
```

### 3. Quest System

```yaml
quest_giver:
  - Adventurer, I have a request...
  - /? Tell me more Maybe later
  - /?Tell me more /jump quest_explanation
  - /?Maybe later /jump quest_decline

quest_explanation:
  - There's a dangerous monster in the nearby cave.
  - Could you defeat it for us?
  - Reward: 500 Gold
  - /? Accept Decline Ask for details
  - /?Accept /jump quest_accept
  - /?Decline /jump quest_decline
  - /?Ask for details /jump quest_details

quest_accept:
  - /score active_quest_monster 1
  - /color \a
  - Thank you! Please be careful.
  - /color \r
  - The cave is to the north of the village.
  - /jump quest_info

quest_decline:
  - I understand. If you change your mind, please come back.

quest_details:
  - The monster is a giant spider.
  - It's been blocking the trade route.
  - Many travelers are in trouble.
  - /jump quest_explanation
```

### 4. Character Status Menu

```yaml
status_menu:
  - Player Status
  - Name: %player%
  - Level: %level%
  - Health: %hp%/%max_hp%
  - Money: \\money\\ Gold
  - /? Check Inventory Skill Tree Statistics Back
  - /?Check Inventory /jump inventory_menu
  - /?Skill Tree /jump skill_menu
  - /?Statistics /jump stats_menu
  - /?Back /jump main_menu

inventory_menu:
  - Inventory Contents
  - /has DIAMOND 1 none Diamonds: \\diamond_count\\
  - /has EMERALD 1 none Emeralds: \\emerald_count\\
  - /? Use Item Drop Item Back
  - /?Use Item /jump use_item_menu
  - /?Drop Item /jump drop_item_menu
  - /?Back /jump status_menu
```

## 🔥 Advanced Techniques

### Dynamic Choice Generation

```yaml
dynamic_shop:
  - /if has_weapon = 0 /jump weapon_shop
  - /if has_armor = 0 /jump armor_shop
  - /jump complete_shop

weapon_shop:
  - Weapon Shop
  - /? Buy Sword(50G) Buy Bow(40G) Leave
  - /?Buy Sword(50G) /jump buy_sword
  - /?Buy Bow(40G) /jump buy_bow
  - /?Leave /jump main_menu

complete_shop:
  - Premium Shop
  - /? Enchanted Sword(200G) Magic Armor(150G) Leave
  - /?Enchanted Sword(200G) /jump buy_enchanted_sword
  - /?Magic Armor(150G) /jump buy_magic_armor
  - /?Leave /jump main_menu
```

### Nested Conversations

```yaml
complex_dialogue:
  - Villager: The situation is complicated...
  - /? What happened? How can I help? I'll leave
  - /?What happened? /jump story_explanation
  - /?How can I help? /jump help_options
  - /?I'll leave /jump dialogue_end

story_explanation:
  - Long story short, monsters invaded our village.
  - Our guards were overwhelmed.
  - /? That's terrible What about the guards? I'll help
  - /?That's terrible /jump sympathy_response
  - /?What about the guards? /jump guard_status
  - /?I'll help /jump help_options

help_options:
  - There are several ways you could help:
  - /? Fight monsters Gather supplies Escort villagers
  - /?Fight monsters /jump combat_quest
  - /?Gather supplies /jump supply_quest
  - /?Escort villagers /jump escort_quest
```

## ⚠️ Important Notes

### Choice Naming Rules

1. **No Spaces in Choice Names**: Use underscores or CamelCase
2. **Case Sensitive**: Ensure exact matching between choice and branch
3. **Special Characters**: Avoid special characters in choice names

```yaml
# ✅ Correct examples
- /? good_choice bad_choice neutral
- /?good_choice Great selection!

# ❌ Avoid these
- /? "good choice" "bad choice" # Spaces cause issues
- /?Good_Choice # Case mismatch
```

### Performance Tips

1. **Limit Choices**: Keep choices to 8 or fewer for better UX
2. **Clear Labels**: Use descriptive choice text
3. **Consistent Formatting**: Maintain uniform choice presentation

## 🔗 Related Documentation

- **[Navigation](navigation.md)** - Using /jump with choices
- **[Conditions](conditions.md)** - Conditional choice display
- **[Variables](variables.md)** - Choice-based variable modification
- **[Practical Examples](../examples/README.md)** - Complex interaction examples
