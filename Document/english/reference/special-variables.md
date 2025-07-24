# Special Variables

Detailed specifications for special variables available in RPGText.

## 📊 Player Information Variables

In RPGText, you can dynamically display player information using the `%variable_name%` format.

### 🏷️ Basic Information

#### `%player%` - Player Name

```yaml
examples:
  - Hello, %player%!
  - %player%'s adventure begins
  - Welcome, %player%
```

**Output Examples:**

- `Hello, Steve!`
- `Steve's adventure begins`
- `Welcome, Steve`

**Notes:**

- Alphanumeric player names are recommended in international environments
- Names with spaces or special characters are also displayable

#### `%player\r%` - Player Name (with color reset)

```yaml
colored_name:
  - /color \6
  - Welcome, %player\r%!
  - /color \a
  - %player\r%'s story
```

**Usage:**

- When you want only the player name in normal color within colored text
- Combining with text decorations

### 📈 Status Information

#### `%level%` - Player Level

```yaml
level_display:
  - Current level: %level%
  - You're a level %level% adventurer
  - /if level > 10 Level %level%! You're an expert
```

**Value Range:** 0 ~ 2,147,483,647 (Minecraft specification)

**Usage Example:**

```yaml
level_based_greeting:
  - /if level = 1 Beginner %player%
  - /if level >= 10 & level < 30 Intermediate %player%
  - /if level >= 30 Veteran %player%
```

#### `%hp%` - Current Health

```yaml
health_display:
  - Current health: %hp%
  - HP has decreased to %hp%
  - Are you okay with %hp% health?
```

**Value Range:** 0 ~ Maximum health value
**Display Format:** Integer (decimal places truncated)

**Practical Example:**

```yaml
health_check:
  - Current HP: %hp%
  - /if hp < 5 /color \c
  - /if hp < 5 ⚠️ Health critically low!
  - /if hp < 5 /color \r
  - /if hp >= 10 Your health is sufficient
```

#### `%food%` - Food Level

```yaml
hunger_display:
  - Food level: %food%
  - Hunger status: %food%/20
  - /if food < 10 You seem hungry
```

**Value Range:** 0 ~ 20
**Meaning:**

- 20: Full
- 18-19: Nearly full
- 6-17: Normal
- 1-5: Hungry
- 0: Starving

#### `%gamemode%` - Game Mode

```yaml
gamemode_display:
  - Current game mode: %gamemode%
  - You're in %gamemode% mode
  - /if gamemode = creative Creative mode detected
```

**Possible Values:**

- `survival` - Survival mode
- `creative` - Creative mode
- `adventure` - Adventure mode
- `spectator` - Spectator mode

#### `%world%` - World Name

```yaml
world_display:
  - Current world: %world%
  - Welcome to %world%
  - You are in the %world% dimension
```

**Usage Examples:**

```yaml
world_specific_message:
  - /if world = world Hello, surface dweller!
  - /if world = world_nether Welcome to the Nether!
  - /if world = world_the_end The End awaits you...
```

### 🎮 Advanced Player Information

#### `%x%`, `%y%`, `%z%` - Player Coordinates

```yaml
location_display:
  - Your position: %x%, %y%, %z%
  - X: %x%  Y: %y%  Z: %z%
  - Coordinates saved: (%x%, %y%, %z%)
```

**Value Range:** -30,000,000 ~ 30,000,000 (Minecraft world border)
**Display Format:** Integer (block coordinates)

#### `%ping%` - Connection Latency

```yaml
connection_display:
  - Your ping: %ping%ms
  - Connection quality: %ping%ms
  - /if ping > 200 High latency detected
```

**Usage for Server Administration:**

```yaml
lag_check:
  - /if ping < 50 Excellent connection (%ping%ms)
  - /if ping >= 50 & ping < 150 Good connection (%ping%ms)
  - /if ping >= 150 Poor connection (%ping%ms)
```

## 💰 Custom Variables

### Variable Display Format

Custom variables created with `/score` are displayed using `\\variable_name\\`.

```yaml
custom_variable_examples:
  - Money: \\money\\ Gold
  - Quest progress: \\quest_progress\\/10
  - Player level: \\player_level\\
  - Items collected: \\items_found\\
```

### Combining Built-in and Custom Variables

```yaml
comprehensive_status:
  - === Player Status ===
  - Name: %player%
  - Level: %level% (Custom: \\custom_level\\)
  - Health: %hp%/\\max_hp\\
  - Money: \\money\\ Gold
  - Position: %x%, %y%, %z%
  - World: %world%
  - Game Mode: %gamemode%
```

## 🎯 Practical Applications

### 1. Dynamic Greetings

```yaml
dynamic_greeting:
  - /if gamemode = creative Hello, Creator %player%!
  - /if gamemode = survival Survivor %player%, be careful!
  - /if gamemode = adventure Adventurer %player%, ready for quests?
  - /if level < 10 Welcome, newcomer %player%!
  - /if level >= 50 Greetings, master %player%!
```

### 2. Health Monitoring System

```yaml
health_monitor:
  - Health Status Check
  - Player: %player%
  - Current HP: %hp%
  - /if hp > 80 /color \a
  - /if hp > 80 Excellent health!
  - /if hp >= 40 & hp <= 80 /color \e
  - /if hp >= 40 & hp <= 80 Moderate health
  - /if hp < 40 /color \c
  - /if hp < 40 Low health - seek healing!
  - /color \r
```

### 3. Location-Based Messages

```yaml
location_messages:
  - Current location: (%x%, %y%, %z%)
  - /if y > 100 You're high up in the sky!
  - /if y < 20 You're deep underground
  - /if x > 1000 You're far from spawn
  - /if world = world_nether Beware of the lava!
```

### 4. Player Statistics Display

```yaml
stats_panel:
  - === %player%'s Statistics ===
  - Level: %level%
  - Health: %hp%/20
  - Food: %food%/20
  - Location: %world% (%x%, %y%, %z%)
  - Connection: %ping%ms
  - \\
  - Custom Stats:
  - Quests completed: \\quests_done\\
  - Total playtime: \\hours_played\\ hours
  - Money earned: \\total_money\\ Gold
```

### 5. Conditional Content Based on Player State

```yaml
state_based_content:
  - Analyzing player state...
  - /if hp < 20 & food < 10 You need rest and food!
  - /if level > 30 & gamemode = survival Elite survivor detected
  - /if world = world_the_end & hp > 80 Ready for the Ender Dragon!
  - /if ping > 300 Connection issues detected for %player%
```

## ⚠️ Important Notes

### Performance Considerations

1. **Update Frequency**: Variables are updated in real-time but may have slight delays
2. **Server Load**: Frequent variable checks can impact server performance
3. **Precision**: Coordinate and health values are truncated to integers

### Compatibility Notes

1. **Minecraft Versions**: Variable availability may vary between versions
2. **Server Plugins**: Some plugins may modify variable behavior
3. **World Types**: Certain variables may behave differently in modded worlds

### Best Practices

1. **Error Handling**: Always account for extreme values (0 health, negative coordinates)
2. **Fallback Values**: Provide alternatives when variables might be unavailable
3. **Validation**: Check variable ranges before using in calculations

## 🔗 Related Documentation

- **[Variables & Scoring](../commands/variables.md)** - Custom variable management
- **[Conditions](../commands/conditions.md)** - Using variables in conditional logic
- **[Color Codes](color-codes.md)** - Formatting variable display
- **[Best Practices](best-practices.md)** - Optimal variable usage patterns
