# Conditional Logic Commands

Commands for comparing variables or values and executing processing based on conditions.

## 🔀 `/if` - Conditional Branching

Executes subsequent commands or messages only when the specified condition is true.

### Basic Syntax

```yaml
/if <variable_name|value> <comparison_operator> <variable_name|value> <execution_content>
```

### Comparison Operators

| Operator | Meaning      | Example           |
| -------- | ------------ | ----------------- |
| `=`      | Equals       | `/if level = 10`  |
| `>`      | Greater than | `/if money > 100` |
| `<`      | Less than    | `/if hp < 50`     |

## 📝 Basic Usage Examples

### Numerical Comparisons

```yaml
level_check:
  - /if level = 1 You're a beginner
  - /if level > 10 You're at intermediate level
  - /if level > 50 You're advanced!
  - /if level > 99 /color \6\l
  - /if level > 99 Maximum level achieved!
  - /if level > 99 /color \r

money_check:
  - /if money = 0 You have no money
  - /if money < 100 You're a bit short on cash
  - /if money > 1000 You're wealthy!
  - /if money > 10000 You're a millionaire!
```

### Variable Comparisons

```yaml
stats_comparison:
  - /if strength > defense You're an attack-focused type
  - /if defense > strength You're a defense-focused type
  - /if strength = defense You're a balanced type

hp_status:
  - /if hp = max_hp Your health is at maximum
  - /if hp < max_hp Your health is reduced
  - /score hp_percent hp
  - /score hp_percent * 100
  - /score hp_percent / max_hp
  - /if hp_percent < 25 /color \c
  - /if hp_percent < 25 Danger! Health below 25%
  - /if hp_percent < 25 /color \r
```

## 🔗 Multiple Conditions

### Multiple Conditions with `&` Operator

```yaml
/if <condition1> & <condition2> & <condition3> <execution_content>
```

### AND Condition Examples

```yaml
special_event:
  - /if level > 20 & money > 500 & quest_complete = 1 Special event triggered!
  - /if level > 20 & money > 500 & quest_complete = 0 Please complete the quest first

shop_purchase:
  - /if money >= 100 & inventory_space > 0 Purchase available
  - /if money < 100 & inventory_space > 0 Not enough money
  - /if money >= 100 & inventory_space = 0 Inventory full
  - /if money < 100 & inventory_space = 0 Not enough money and space

dungeon_entry:
  - /if level >= 15 & key_count > 0 & party_size >= 2 You can enter the dungeon
  - /if level < 15 Level 15 or higher required
  - /if key_count = 0 Key required
  - /if party_size < 2 Party of 2 or more required
```

## 🎯 Practical System Examples

### 1. RPG Status Check

```yaml
status_check:
  - === Status Check ===
  - Level: \\level\\
  - /if level < 10 /color \c
  - /if level < 10 Still at beginner level
  - /if level >= 10 & level < 30 /color \e
  - /if level >= 10 & level < 30 Intermediate level
  - /if level >= 30 & level < 60 /color \a
  - /if level >= 30 & level < 60 Advanced level
  - /if level >= 60 /color \6\l
  - /if level >= 60 Expert level!
  - /color \r

health_warning:
  - Current HP: \\hp\\/\\max_hp\\
  - /if hp < 20 /color \c\l
  - /if hp < 20 ⚠️ Health critically low!
  - /if hp < 20 /color \r
  - /if hp >= 20 & hp < 50 /color \e
  - /if hp >= 20 & hp < 50 Warning: Health below half
  - /if hp >= 20 & hp < 50 /color \r
  - /if hp >= 50 Health is sufficient
```

### 2. Shop Purchase System

```yaml
weapon_shop:
  - Welcome to the weapon shop!
  - Money: \\money\\ Gold
  - /? Dagger(100G) Sword(300G) Holy_Sword(1000G) Leave
  - /?Dagger(100G) /jump buy_dagger
  - /?Sword(300G) /jump buy_sword
  - /?Holy_Sword(1000G) /jump buy_holy_sword
  - /?Leave Please come again

buy_dagger:
  - /if money >= 100 & weapon_equipped = 0 /jump purchase_dagger
  - /if money < 100 Not enough money
  - /if weapon_equipped > 0 Already have a weapon equipped

purchase_dagger:
  - /score money - 100
  - /score weapon_equipped 1
  - /score attack_power + 10
  - Dagger purchased!
  - Attack power increased by 10

buy_holy_sword:
  - /if money >= 1000 & level >= 50 & holy_quest = 1 /jump purchase_holy_sword
  - /if money < 1000 Not enough money
  - /if level < 50 Level 50 or higher required
  - /if holy_quest = 0 Must complete the holy quest first

purchase_holy_sword:
  - /score money - 1000
  - /score weapon_equipped 3
  - /score attack_power + 100
  - /color \6\l
  - Holy Sword acquired!
  - /color \r
  - The sword radiates divine power!
```

### 3. Quest System

```yaml
quest_npc:
  - /if quest_status = 0 /jump new_quest
  - /if quest_status = 1 /jump quest_in_progress
  - /if quest_status = 2 /jump quest_complete
  - /jump quest_finished

new_quest:
  - I have a task for you, adventurer.
  - /if level >= 10 & reputation >= 5 /jump offer_quest
  - /if level < 10 You need more experience first
  - /if reputation < 5 I don't trust you enough yet

offer_quest:
  - Will you help me retrieve the stolen artifact?
  - /? Accept Decline Ask_Details
  - /?Accept /jump accept_quest
  - /?Decline /jump decline_quest
  - /?Ask_Details /jump quest_details

accept_quest:
  - /score quest_status 1
  - /score quest_timer 72
  - Thank you! You have 3 days to complete this
  - The artifact is hidden in the eastern caves

quest_in_progress:
  - /if quest_timer <= 0 /jump quest_failed
  - /if artifact_found = 1 /jump return_artifact
  - Time remaining: \\quest_timer\\ hours
  - Have you found the artifact yet?

quest_failed:
  - /score quest_status 3
  - /score reputation - 5
  - You took too long... The quest has failed
  - Your reputation has decreased

return_artifact:
  - /score quest_status 2
  - /score reputation + 10
  - /score money + 500
  - Excellent work! Here's your reward
  - +500 Gold, +10 Reputation
```

### 4. Dynamic Dialogue System

```yaml
npc_mood_system:
  - /score npc_mood random 3
  - /if npc_mood = 0 /jump grumpy_dialogue
  - /if npc_mood = 1 /jump normal_dialogue
  - /if npc_mood = 2 /jump cheerful_dialogue

relationship_check:
  - /if friendship < 10 /jump stranger_dialogue
  - /if friendship >= 10 & friendship < 30 /jump acquaintance_dialogue
  - /if friendship >= 30 & friendship < 60 /jump friend_dialogue
  - /if friendship >= 60 /jump best_friend_dialogue

grumpy_dialogue:
  - /color \c
  - What do you want? I'm busy.
  - /color \r
  - /if friendship > 20 Though... I suppose you're alright

cheerful_dialogue:
  - /color \a
  - Hello there! Beautiful day, isn't it?
  - /color \r
  - /if friendship > 40 It's always great to see you!

time_based_greetings:
  - /score current_hour time
  - /score current_hour % 24
  - /if current_hour >= 6 & current_hour < 12 Good morning!
  - /if current_hour >= 12 & current_hour < 18 Good afternoon!
  - /if current_hour >= 18 & current_hour < 22 Good evening!
  - /if current_hour >= 22 /jump late_night_dialogue
  - /if current_hour < 6 /jump late_night_dialogue

late_night_dialogue:
  - /color \d
  - You're up late... Is everything alright?
  - /color \r
```

### 5. Combat Advantage System

```yaml
combat_advantages:
  - Analyzing battle conditions...
  - /if player_level > enemy_level /jump level_advantage
  - /if weapon_type = 1 & enemy_armor_type = 1 /jump weapon_advantage
  - /if environment = "forest" & player_class = "ranger" /jump terrain_advantage
  - /jump balanced_combat

level_advantage:
  - /score damage_bonus 20
  - Your experience gives you an edge!

weapon_advantage:
  - /score damage_bonus + 15
  - Your weapon is effective against this armor!

terrain_advantage:
  - /score accuracy_bonus 25
  - /score damage_bonus + 10
  - You know this terrain well!

pre_combat_buffs:
  - /if has_blessing = 1 /score damage_bonus + 10
  - /if has_blessing = 1 Divine blessing active!
  - /if weather = "rain" & spell_element = "lightning" /score magic_power + 30
  - /if weather = "rain" & spell_element = "lightning" Lightning spells empowered by storm!
```

## 💡 Advanced Conditional Patterns

### Nested Logic Simulation

```yaml
# Since nesting isn't supported, use jump chains
complex_check_start:
  - /if condition1 = 1 /jump check_condition2
  - /jump condition1_failed

check_condition2:
  - /if condition2 = 1 /jump check_condition3
  - /jump condition2_failed

check_condition3:
  - /if condition3 = 1 /jump all_conditions_met
  - /jump condition3_failed

all_conditions_met:
  - All conditions satisfied!

condition1_failed:
  - Condition 1 not met
  - /jump check_end
```

### State Machine Pattern

```yaml
game_state_handler:
  - /if game_state = 0 /jump menu_state
  - /if game_state = 1 /jump playing_state
  - /if game_state = 2 /jump paused_state
  - /if game_state = 3 /jump game_over_state
  - /jump error_state

menu_state:
  - Main Menu
  - /? Start Options Quit
  - /?Start /score game_state 1
  - /?Start /jump playing_state
```

## ⚠️ Important Limitations

### What Cannot Be Done

1. **Nested Conditions**: Cannot embed `/if` commands within other `/if` commands
2. **OR Logic**: No direct OR operator (use multiple separate conditions)
3. **Complex Expressions**: Cannot use parentheses for grouping

### Workarounds

```yaml
# Instead of: /if (a = 1 OR b = 1) AND c = 1
# Use separate checks:
or_logic_simulation:
  - /if a = 1 & c = 1 /jump condition_met
  - /if b = 1 & c = 1 /jump condition_met
  - /jump condition_not_met
# Instead of nested /if commands
# Use jump chains as shown above
```

## 🔗 Related Documentation

- **[Variables](variables.md)** - Managing values for conditions
- **[Navigation](navigation.md)** - Using /jump with conditions
- **[Items](items.md)** - Item-based conditional logic
- **[Game Control](game-control.md)** - State management
- **[Practical Examples](../examples/README.md)** - Complex conditional systems
