# Game Control Commands

Commands for controlling game state and player interactions.

## 🎮 `/command` - Execute Minecraft Commands

Execute standard Minecraft commands from within RPGText scripts.

### Basic Syntax

```yaml
/command <minecraft_command>
```

### Common Usage Examples

```yaml
give_items:
  - /command give %player% minecraft:diamond 5
  - Gave you 5 diamonds!
  - /command give %player% minecraft:iron_sword 1
  - Iron sword granted!

teleport_player:
  - /command tp %player% 100 64 200
  - Teleported to coordinates (100, 64, 200)
  - /command tp %player% ~ ~10 ~
  - Lifted 10 blocks up!

effects_and_titles:
  - /command title %player% title {"text":"Welcome!","color":"gold"}
  - /command effect give %player% minecraft:speed 30 1
  - Speed boost applied for 30 seconds!
```

## 🚫 `/freeze` - Player Movement Control

Control whether the player can move during dialogue.

### Basic Syntax

```yaml
/freeze <true|false>
```

### Usage Examples

```yaml
important_scene:
  - /freeze true
  - Listen carefully to this important announcement...
  - This information is crucial for your quest.
  - /freeze false
  - You may now move freely again

cutscene_example:
  - /freeze true
  - /command title %player% title {"text":"Chapter 1","color":"gold"}
  - /wait 60
  - /command title %player% subtitle {"text":"The Adventure Begins"}
  - /wait 40
  - /freeze false
```

## ⏰ `/wait` - Time Delays

Pause script execution for a specified duration.

### Basic Syntax

```yaml
/wait <ticks>
```

**Note**: 20 ticks = 1 second

### Usage Examples

```yaml
dramatic_pause:
  - Something important is about to happen...
  - /wait 60
  - ...
  - /wait 40
  - The treasure chest opens!

countdown_example:
  - Countdown starting!
  - /wait 20
  - 3...
  - /wait 20
  - 2...
  - /wait 20
  - 1...
  - /wait 20
  - Go!
```

## ⚡ `/auto` - Auto-Progression Control

Control whether the script automatically advances or waits for player input.

### Basic Syntax

```yaml
/auto <true|false>
```

### Usage Examples

```yaml
fast_paced_scene:
  - /auto true
  - The battle begins!
  - Enemies approach from all sides!
  - You must act quickly!
  - /auto false
  - What will you do?
  - /? Attack Defend Run

reading_scene:
  - /auto false
  - Here is an important scroll.
  - Take your time to read it carefully.
  - /auto true
  - Continuing with the story...
```

## ⏭️ `/skip` - Skip Control

Control whether players can skip through text during this section.

### Basic Syntax

```yaml
/skip <true|false>
```

### Usage Examples

```yaml
important_information:
  - /skip false
  - ⚠️ IMPORTANT: This information cannot be skipped
  - Read carefully as this affects your quest
  - /skip true
  - Normal dialogue resumes here

optional_lore:
  - /skip true
  - This is additional background information
  - You can skip through this if you want
  - But it provides interesting context about the world
```

## 🎯 Practical System Examples

### 1. Cinematic Cutscene

```yaml
opening_cutscene:
  - /freeze true
  - /skip false
  - /auto true
  - /command title %player% title {"text":"Long ago...","color":"dark_purple"}
  - /wait 60
  - /command title %player% subtitle {"text":"In a land far away"}
  - /wait 80
  - /command title %player% title {"text":""}
  - /wait 40
  - A great evil awakened from its slumber...
  - /wait 60
  - And darkness began to spread across the realm.
  - /wait 80
  - /command effect give %player% minecraft:blindness 3 1
  - /wait 60
  - But hope was not lost...
  - /wait 60
  - For a hero would soon emerge.
  - /wait 40
  - /command title %player% title {"text":"Your adventure begins!","color":"gold"}
  - /wait 60
  - /freeze false
  - /skip true
  - /auto false
```

### 2. Timed Challenge System

```yaml
timed_puzzle:
  - /freeze true
  - /auto true
  - You have 10 seconds to solve this puzzle!
  - /command title %player% title {"text":"10","color":"red"}
  - /wait 20
  - /command title %player% title {"text":"9","color":"red"}
  - /wait 20
  - /command title %player% title {"text":"8","color":"red"}
  - /wait 20
  - /command title %player% title {"text":"7","color":"red"}
  - /wait 20
  - /command title %player% title {"text":"6","color":"yellow"}
  - /wait 20
  - /command title %player% title {"text":"5","color":"yellow"}
  - /wait 20
  - /command title %player% title {"text":"4","color":"yellow"}
  - /wait 20
  - /command title %player% title {"text":"3","color":"gold"}
  - /wait 20
  - /command title %player% title {"text":"2","color":"gold"}
  - /wait 20
  - /command title %player% title {"text":"1","color":"gold"}
  - /wait 20
  - /command title %player% title {"text":"Time's up!","color":"dark_red"}
  - /freeze false
  - /auto false
  - /jump puzzle_failed
```

### 3. Interactive Tutorial

```yaml
tutorial_movement:
  - Welcome to the tutorial!
  - /freeze false
  - Try moving around with WASD keys
  - /wait 100
  - /freeze true
  - Good! Now let's learn about jumping
  - /freeze false
  - Press SPACE to jump
  - /wait 80
  - /freeze true
  - Excellent! Tutorial complete

tutorial_reading:
  - /skip false
  - This tutorial cannot be skipped
  - Please read each instruction carefully
  - /auto false
  - Click to continue when ready
  - /skip true
  - /auto true
  - Tutorial completed!
```

### 4. Atmospheric Scene Builder

```yaml
spooky_atmosphere:
  - /auto true
  - /freeze true
  - You enter the abandoned mansion...
  - /command effect give %player% minecraft:slowness 10 1
  - /command effect give %player% minecraft:night_vision 30 1
  - /wait 40
  - The floorboards creak beneath your feet...
  - /command playsound minecraft:block.wood.break ambient %player%
  - /wait 60
  - A cold wind blows through broken windows...
  - /wait 40
  - /command particle minecraft:dust_color_transition 0.3 0.3 0.3 1 0.1 0.1 0.1 ~ ~1 ~ 2 2 2 0 50
  - /wait 80
  - Suddenly, you hear a noise upstairs...
  - /command playsound minecraft:entity.zombie.ambient ambient %player% ~ ~ ~ 0.3 0.5
  - /wait 60
  - /freeze false
  - /auto false
  - What do you do?
  - /? Investigate Go_upstairs Leave_immediately
```

### 5. Reward Ceremony

```yaml
victory_ceremony:
  - /freeze true
  - /auto true
  - /skip false
  - Congratulations, hero!
  - /command title %player% title {"text":"VICTORY!","color":"gold","bold":true}
  - /command effect give %player% minecraft:levitation 3 1
  - /command playsound minecraft:entity.player.levelup ambient %player% ~ ~ ~ 1 1
  - /wait 60
  - /command title %player% subtitle {"text":"Quest Complete"}
  - /command particle minecraft:firework ~ ~2 ~ 3 3 3 0.1 100
  - /wait 80
  - Your heroic deeds will be remembered forever!
  - /command give %player% minecraft:diamond 10
  - /command give %player% minecraft:golden_apple 3
  - /wait 60
  - /command title %player% title {"text":"Rewards Granted!","color":"yellow"}
  - /wait 60
  - /freeze false
  - /skip true
  - /auto false
```

## 💡 Advanced Techniques

### State Management

```yaml
game_state_control:
  - /score game_phase 1
  - /freeze true
  - Entering Phase 1: Preparation
  - /command effect give %player% minecraft:resistance 60 2
  - /wait 60
  - /score game_phase 2
  - Phase 2: Action begins!
  - /freeze false

checkpoint_system:
  - /command spawnpoint %player%
  - Checkpoint saved!
  - /score last_checkpoint current_location
  - /auto false
  - Continue when ready...
```

### Dynamic Environment Control

```yaml
weather_effects:
  - /command weather rain
  - Storm clouds gather overhead...
  - /wait 40
  - /command effect give %player% minecraft:slow_falling 30 1
  - Lightning crackles in the distance!
  - /wait 60
  - /command weather clear
  - The storm passes...

time_manipulation:
  - /command time set day
  - Dawn breaks over the horizon...
  - /wait 80
  - /command time add 6000
  - Time passes quickly...
  - /wait 40
  - /command time set night
  - Night falls once again...
```

### Performance Optimization

```yaml
optimized_cutscene:
  - /auto true
  - /skip true
  - Beginning cutscene...
  - /if seen_cutscene = 1 /jump skip_cutscene
  - /skip false
  - /freeze true
  - [Full cutscene content here]
  - /score seen_cutscene 1
  - /jump cutscene_end

skip_cutscene:
  - Skipping previously viewed cutscene...
  - /jump cutscene_end

cutscene_end:
  - /freeze false
  - /skip true
  - /auto false
```

## ⚠️ Important Notes

### Performance Considerations

1. **Wait Times**: Avoid excessive /wait commands in rapid succession
2. **Command Frequency**: Limit frequent Minecraft command execution
3. **Effect Stacking**: Be careful not to apply too many effects simultaneously

### Player Experience

1. **Freeze Usage**: Don't freeze players for extended periods
2. **Skip Control**: Allow skipping for repeated content
3. **Auto Balance**: Mix auto and manual progression appropriately

### Common Minecraft Commands

| Command Type | Example                                 | Purpose       |
| ------------ | --------------------------------------- | ------------- |
| **Items**    | `give %player% diamond 5`               | Give items    |
| **Effects**  | `effect give %player% speed 30 1`       | Apply effects |
| **Teleport** | `tp %player% 100 64 200`                | Move player   |
| **Title**    | `title %player% title {"text":"Hello"}` | Display text  |
| **Sound**    | `playsound note.bell ambient %player%`  | Play sounds   |

## 🔗 Related Documentation

- **[Basic Settings](basic-settings.md)** - Sound and speed control
- **[Audio](audio.md)** - Detailed sound management
- **[Variables](variables.md)** - State tracking
- **[Conditions](conditions.md)** - Dynamic control flow
- **[Practical Examples](../examples/README.md)** - Complex game control examples
