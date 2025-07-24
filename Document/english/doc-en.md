# RPGText Syntax Documentation

RPGText is a Minecraft server plugin that provides a sequential text display system similar to RPG games. You can write stories using special syntax in YML files and control player interactions and in-game events.

## Basic Structure

### File Format

- YML file format
- Section-based management
- List format description

```yaml
section_name:
  - Message1
  - /command example
  - Message2
```

### File Global Settings

Set at the beginning of the file to apply to all sections within that file.

```yaml
# Sound settings
sound: block.note.bass 1 2
# Display speed
speed: 20
# Text color
color: \r
```

## Command List

### 1. Basic Settings

#### `/sound <sound_name> <volume> <pitch>`

Set sound for message display

```yaml
- /sound block.note.flute 1 1.75
- /sound block.note.bass 1 2
```

#### `/speed <speed>`

Set number of characters displayed per second (default: 30)

```yaml
- /speed 3 # 3 chars/sec
- /speed 50 # 50 chars/sec
```

#### `/color <color_code>`

Set basic message color

```yaml
- /color \6\l # Gold + Bold
- /color \r # Reset to default color
```

### 2. Navigation

#### `/jump <filename>/<section_name>`

Move to other sections or files

```yaml
- /jump Tutorial.yml/users2
- /jump users2 # Within same file
- /jump chapter1/title.yml/talk1 # Hierarchical structure
```

### 3. Choices and Interaction

#### `/? <choice1> <choice2> ...`

Display choices to player

```yaml
- /? option1 option2 option3
- /? yes no
- /? 1 2 3 4 5
```

#### `/?<choice_name> <execution_content>`

Branch processing based on choices

```yaml
- /? yes no
- /?yes Good choice!
- /?no That's unfortunate...
```

### 4. Variables & Score Management

#### `/score <variable_name> <value>`

Set variable

```yaml
- /score test 1
- /score money 100
```

#### `/score <variable_name> <operator> <value_or_variable>`

Variable calculation

```yaml
- /score money + 50 # money += 50
- /score hp - damage # hp -= damage
- /score level * 2 # level *= 2
- /score remainder % 4 # remainder %= 4
- /score dice random 6 # Random value 0-5
```

#### `/add <variable_name>`

Increment variable by 1

```yaml
- /add counter
- /add visit_count
```

#### `\\variable_name\\`

Display variable value

```yaml
- Your money is \\money\\ gold
- Level: \\level\\
```

### 5. Conditional Branching

#### `/if <variable|value> <comparison_operator> <variable|value> <execution_content>`

Conditional processing

```yaml
- /if level > 10 Your level is 10 or higher!
- /if money = 0 You have no money
- /if hp < max_hp Your health is reduced
```

Comparison operators: `=`, `>`, `<`

#### Multiple Conditions

Connect multiple conditions with `&`

```yaml
- /if level > 5 & money >= 100 You can purchase special items
```

### 6. Item Management

#### `/has <item_type> <quantity> <name> <execution_content>`

Check item possession

```yaml
- /has DIAMOND 1 none You have a diamond
- /has EMERALD 5 none You have 5 or more emeralds
```

#### `/removeItem <item_type> <quantity> <name>`

Remove items

```yaml
- /removeItem EMERALD 1 none
- /removeItem DIAMOND 2 none
```

### 7. Game Control

#### `/command <minecraft_command>`

Execute Minecraft commands

```yaml
- /command give %player% minecraft:diamond
- /command title @a title {"text":"Hello"}
- /command tp %player% 100 64 200
```

#### `/freeze <true|false>`

Player movement restriction

```yaml
- /freeze true # Disable movement
- /freeze false # Enable movement
```

#### `/wait <ticks>`

Wait for specified time (20 ticks = 1 second)

```yaml
- /wait 20 # Wait 1 second
- /wait 60 # Wait 3 seconds
```

#### `/auto <true|false>`

Auto-progression setting

```yaml
- /auto true # Auto proceed to next
- /auto false # Wait for click
```

#### `/skip <true|false>`

Skippability setting

```yaml
- /skip true # Skippable
- /skip false # Not skippable
```

### 8. Sound Control

#### `/singlesound <sound_name> [volume] [pitch]`

Play single sound

```yaml
- /singlesound minecraft:entity.player.levelup
- /singlesound minecraft:block.note.bell 1 1.5
```

### 9. Special Variables

#### Player Information

- `%player%` - Player name
- `%level%` - Player level
- `%hp%` - Health
- `%food%` - Food level
- `%gamemode%` - Game mode

#### Color Codes

- `\a` - Dark green
- `\b` - Aqua
- `\c` - Dark red
- `\d` - Light purple
- `\e` - Yellow
- `\1` - Dark blue
- `\2` - Green
- `\3` - Dark aqua
- `\4` - Dark red
- `\l` - Bold
- `\r` - Reset

## Practical Examples

### Basic Conversation

```yaml
simple_talk:
  - /sound block.note.bell 1 1.5
  - /color \c
  - Hello!
  - How are you doing?
  - /? Good Bad
  - /?Good That's great!
  - /?Bad Are you okay? Don't push yourself too hard.
  - Have a good day!
```

### Slot Game

```yaml
slot_game:
  - Slot game start!
  - /score slot1 0
  - /score slot2 0
  - /score slot3 0
  - /jump slot_countdown

slot_countdown:
  - /command title %player% title {"text":"3"}
  - /wait 20
  - /command title %player% title {"text":"2"}
  - /wait 20
  - /command title %player% title {"text":"1"}
  - /wait 20
  - /jump slot_spin

slot_spin:
  - /score slot1 random 3
  - /score slot2 random 3
  - /score slot3 random 3
  - /add slot1
  - /add slot2
  - /add slot3
  - Result: \\slot1\\ \\slot2\\ \\slot3\\
  - /if slot1 = slot2 & slot2 = slot3 /jump jackpot
  - Too bad! Try again.

jackpot:
  - /singlesound minecraft:entity.player.levelup
  - /command give %player% diamond
  - Jackpot! You got a diamond!
```

### Shop System

```yaml
shop:
  - Welcome!
  - Money: \\money\\ Gold
  - /? Buy_Sword(50G) Buy_Potion(20G) Leave
  - /?Buy_Sword(50G) /jump buy_sword
  - /?Buy_Potion(20G) /jump buy_potion
  - /?Leave Thank you for visiting!

buy_sword:
  - /if money >= 50 /jump sword_purchase
  - Not enough money!

sword_purchase:
  - /score money - 50
  - /command give %player% minecraft:iron_sword
  - Sword purchased!
  - Remaining: \\money\\ Gold
```

## Notes

### Limitations

- Nested condition commands are not supported

  ```yaml
  # ❌ This doesn't work
  - /if test = 1 /has DIAMOND 1 none Message
  - /if test < 3 /if test2 = 1 Message
  ```

- Use `&` or `/jump` for multiple conditions
  ```yaml
  # ✅ Correct examples
  - /if test = 1 & money > 100 Condition met!
  # or
  - /if test = 1 /jump check_diamond
  ```

### Best Practices

1. Use meaningful variable names
2. Use clear section names
3. Split complex conditions into multiple sections
4. Create atmosphere with appropriate sounds and timing
5. Use `/auto false` when waiting for player input

## In-Game Usage

### Sending Messages to Players

```
/rpgtext config <player_name> <file_path>/<section_name>
```

### NPC Conversation Setup

```
/rpgtext character <entity_name> <file_path>/<section_name>
```

Using this syntax, you can easily implement various RPG elements such as dialogue systems, quests, shops, and mini-games.
