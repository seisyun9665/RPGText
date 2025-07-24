# Variables & Scoring Commands

Commands for managing game progression and numerical values.

## 📊 `/score` - Variable Setting & Calculation

A multi-functional command for setting and calculating variables.

### Basic Syntax

```yaml
/score <variable_name> <value>
```

### Calculation Syntax

```yaml
/score <variable_name> <operator> <value_or_variable>
```

## 📝 Basic Variable Setting

### Setting Numbers

```yaml
basic_setup:
  - /score health 100
  - /score money 50
  - /score level 1
  - /score exp 0
  - Status initialized
```

### Displaying Variables

```yaml
show_status:
  - === Status ===
  - Level: \\level\\
  - Health: \\health\\
  - Money: \\money\\ Gold
  - Experience: \\exp\\
```

## 🧮 Calculation Functions

### Addition (+)

```yaml
gain_experience:
  - /score exp + 50
  - Gained 50 experience points
  - Current experience: \\exp\\

add_money:
  - /score money + 100
  - Earned 100 gold!
  - Money: \\money\\ Gold
```

### Subtraction (-)

```yaml
spend_money:
  - /score money - 20
  - Paid 20 gold
  - Remaining: \\money\\ Gold

take_damage:
  - /score health - 30
  - Took 30 damage!
  - Remaining health: \\health\\
```

### Multiplication (\*)

```yaml
level_up_bonus:
  - /score exp * 2
  - Experience doubled!
  - Experience: \\exp\\

critical_damage:
  - /score damage * 3
  - Critical hit! Triple damage!
```

### Division (/)

```yaml
halve_damage:
  - /score incoming_damage / 2
  - Shield reduced damage by half!

split_reward:
  - /score party_gold / 4
  - Reward split among 4 party members
```

### Modulo (%)

```yaml
random_effect:
  - /score turn_count % 3
  - /if turn_count = 0 Special effect activated!

cycle_system:
  - /score day_cycle % 7
  - /if day_cycle = 0 Today is Sunday
```

### Random Values (random)

```yaml
dice_roll:
  - /score dice random 6
  - /add dice
  - Dice result: \\dice\\

treasure_rarity:
  - /score rarity random 100
  - /if rarity < 10 /jump legendary_treasure
  - /if rarity < 30 /jump rare_treasure
  - /jump common_treasure

battle_accuracy:
  - /score hit_chance random 100
  - /if hit_chance < 85 Attack hit!
  - /if hit_chance >= 85 Attack missed
```

## 📈 `/add` - Variable Increment

A simple command that increases a variable by 1.

### Syntax

```yaml
/add <variable_name>
```

### Usage Examples

```yaml
counter_system:
  - /add visit_count
  - Visit count: \\visit_count\\ times

experience_gain:
  - /add level
  - Level up! Current level: \\level\\

turn_based_game:
  - /add turn_number
  - Turn \\turn_number\\
  - Player's turn

quest_progress:
  - /add quest_progress
  - Quest progress: \\quest_progress\\/10
```

## 🎮 Practical System Examples

### 1. RPG Character Stats

```yaml
character_creation:
  - Creating your character...
  - /score strength 10
  - /score defense 8
  - /score agility 12
  - /score intelligence 15
  - /score luck 5
  - Character stats set!

stat_display:
  - === Character Stats ===
  - Strength: \\strength\\
  - Defense: \\defense\\
  - Agility: \\agility\\
  - Intelligence: \\intelligence\\
  - Luck: \\luck\\
  - Total Power:
  - /score total_power strength
  - /score total_power + defense
  - /score total_power + agility
  - /score total_power + intelligence
  - /score total_power + luck
  - \\total_power\\
```

### 2. Economy System

```yaml
bank_deposit:
  - Welcome to the bank!
  - Current balance: \\bank_balance\\ Gold
  - Cash on hand: \\money\\ Gold
  - /? Deposit Withdraw Check_Balance Leave
  - /?Deposit /jump deposit_money
  - /?Withdraw /jump withdraw_money
  - /?Check_Balance /jump check_balance
  - /?Leave Have a good day!

deposit_money:
  - How much would you like to deposit?
  - /? 100 500 1000 All Cancel
  - /?100 /jump deposit_100
  - /?500 /jump deposit_500
  - /?1000 /jump deposit_1000
  - /?All /jump deposit_all
  - /?Cancel /jump bank_deposit

deposit_100:
  - /if money >= 100 /jump process_deposit_100
  - You don't have enough money to deposit

process_deposit_100:
  - /score money - 100
  - /score bank_balance + 100
  - Deposited 100 gold
  - New balance: \\bank_balance\\ Gold
  - /jump bank_deposit
```

### 3. Combat System

```yaml
battle_start:
  - Battle begins!
  - /score player_hp 100
  - /score enemy_hp 80
  - /score player_mp 50
  - /score turn 1

battle_turn:
  - Turn \\turn\\
  - Your HP: \\player_hp\\/100
  - Enemy HP: \\enemy_hp\\/80
  - Your MP: \\player_mp\\/50
  - /? Attack Magic Defend Run
  - /?Attack /jump player_attack
  - /?Magic /jump player_magic
  - /?Defend /jump player_defend
  - /?Run /jump attempt_flee

player_attack:
  - /score damage random 20
  - /score damage + 10
  - You attack for \\damage\\ damage!
  - /score enemy_hp - damage
  - /if enemy_hp <= 0 /jump victory
  - /jump enemy_turn

player_magic:
  - /if player_mp < 10 Not enough MP!
  - /if player_mp < 10 /jump battle_turn
  - /score player_mp - 10
  - /score magic_damage random 15
  - /score magic_damage + 20
  - Magic attack for \\magic_damage\\ damage!
  - /score enemy_hp - magic_damage
  - /if enemy_hp <= 0 /jump victory
  - /jump enemy_turn

enemy_turn:
  - /add turn
  - /score enemy_damage random 12
  - /score enemy_damage + 8
  - Enemy attacks for \\enemy_damage\\ damage!
  - /score player_hp - enemy_damage
  - /if player_hp <= 0 /jump defeat
  - /jump battle_turn

victory:
  - Victory!
  - /score exp_gained random 50
  - /score exp_gained + 25
  - /score gold_gained random 30
  - /score gold_gained + 20
  - Gained \\exp_gained\\ experience!
  - Earned \\gold_gained\\ gold!
  - /score exp + exp_gained
  - /score money + gold_gained
```

### 4. Skill Point System

```yaml
level_up_system:
  - /score exp_needed level
  - /score exp_needed * 100
  - /if exp >= exp_needed /jump level_up_process
  - Experience needed for next level:
  - /score exp_remaining exp_needed
  - /score exp_remaining - exp
  - \\exp_remaining\\

level_up_process:
  - /add level
  - /score exp - exp_needed
  - /score skill_points + 3
  - Level up! You are now level \\level\\!
  - Gained 3 skill points!
  - Available skill points: \\skill_points\\
  - /jump allocate_skills

allocate_skills:
  - /if skill_points = 0 No skill points to allocate
  - /if skill_points = 0 /jump level_up_complete
  - Skill points remaining: \\skill_points\\
  - /? Increase_Strength Increase_Defense Increase_Magic Save_Points
  - /?Increase_Strength /jump boost_strength
  - /?Increase_Defense /jump boost_defense
  - /?Increase_Magic /jump boost_magic
  - /?Save_Points /jump level_up_complete

boost_strength:
  - /score skill_points - 1
  - /score strength + 2
  - Strength increased! New strength: \\strength\\
  - /jump allocate_skills
```

## 💡 Advanced Techniques

### Variable-Based Storytelling

```yaml
story_branching:
  - /score karma_alignment good_deeds
  - /score karma_alignment - evil_deeds
  - /if karma_alignment > 10 /jump hero_path
  - /if karma_alignment < -10 /jump villain_path
  - /jump neutral_path

reputation_system:
  - /score total_reputation village_rep
  - /score total_reputation + city_rep
  - /score total_reputation + guild_rep
  - /if total_reputation > 50 /jump famous_hero
  - /if total_reputation < -20 /jump notorious_villain
```

### Dynamic Difficulty

```yaml
adjust_difficulty:
  - /score difficulty_level deaths
  - /score difficulty_level + failures
  - /score difficulty_level - victories
  - /if difficulty_level > 10 /jump increase_difficulty
  - /if difficulty_level < -5 /jump decrease_difficulty

increase_difficulty:
  - /score enemy_hp_multiplier 150
  - /score enemy_damage_multiplier 120
  - Difficulty increased due to performance

decrease_difficulty:
  - /score enemy_hp_multiplier 80
  - /score enemy_damage_multiplier 90
  - Difficulty decreased to help progression
```

## ⚠️ Important Notes

### Variable Naming Best Practices

1. **Use Descriptive Names**: `player_health` instead of `hp`
2. **Consistent Naming**: Use underscores or camelCase consistently
3. **Avoid Conflicts**: Don't use Minecraft reserved words

### Performance Considerations

1. **Initialize Variables**: Set default values at the start
2. **Clean Up**: Reset temporary variables when done
3. **Limit Calculations**: Avoid excessive mathematical operations

## 🔗 Related Documentation

- **[Conditions](conditions.md)** - Using variables in conditional logic
- **[Items](items.md)** - Combining with item-based rewards
- **[Game Control](game-control.md)** - Managing game state
- **[Practical Examples](../examples/README.md)** - Complex variable usage examples
