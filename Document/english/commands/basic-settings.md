# Basic Settings Commands

Commands for configuring the basic display settings of RPGText.

## 🔊 `/sound` - Audio Settings

Sets the sound played when messages are displayed.

### Syntax

```yaml
/sound <sound_name> <volume> <pitch>
```

### Parameters

- **sound_name**: Minecraft sound identifier
- **volume**: 0.0 ~ 1.0 (default: 1.0)
- **pitch**: 0.5 ~ 2.0 (default: 1.0)

### Usage Examples

```yaml
# Basic usage
- /sound block.note.bell 1 1

# Adjust volume and pitch
- /sound block.note.flute 0.8 1.5

# Low pitch
- /sound block.note.bass 1 0.7
```

### Commonly Used Sounds

| Sound Name                     | Purpose             |
| ------------------------------ | ------------------- |
| `block.note.bell`              | Normal conversation |
| `block.note.flute`             | Light scenes        |
| `block.note.bass`              | Important scenes    |
| `block.note.xylophone`         | Action              |
| `entity.experience_orb.pickup` | Success             |

### File-wide Settings

```yaml
# Write at the beginning of file
sound: block.note.bell 1 1.2

section1:
  - This section uses the above sound
  - /sound block.note.flute 1 1.5
  - From this line, flute sound is used

section2:
  - This section also starts with bell sound
```

## ⚡ `/speed` - Display Speed Settings

Sets the number of characters displayed per second.

### Syntax

```yaml
/speed <character_count>
```

### Parameters

- **character_count**: Characters displayed per second (default: 30)

### Usage Examples

```yaml
# Slow display (3 chars/sec)
- /speed 3
- This text displays slowly

# Normal speed
- /speed 30
- Back to normal speed

# Fast display (50 chars/sec)
- /speed 50
- Displays quickly
```

### Recommended Values

| Speed      | Purpose        | Chars/sec |
| ---------- | -------------- | --------- |
| Ultra fast | Instant read   | 60+       |
| Fast       | Readable       | 40-50     |
| Standard   | Balanced       | 25-35     |
| Slow       | Dramatic       | 15-20     |
| Ultra slow | Special effect | 5-10      |

### File-wide Settings

```yaml
# 25 chars/sec for entire file
speed: 25

dramatic_scene:
  - /speed 10
  - Important...
  - announcement...
  - coming...
  - /speed 25
  - Back to normal speed
```

## 🎨 `/color` - Text Color Settings

Sets the basic color for messages.

### Syntax

```yaml
/color <color_code>
```

### Parameters

- **color_code**: Symbols specifying color or effects

### Basic Color Codes

| Code | Color        | Example     |
| ---- | ------------ | ----------- |
| `\a` | Dark green   | `/color \a` |
| `\b` | Aqua         | `/color \b` |
| `\c` | Dark red     | `/color \c` |
| `\d` | Light purple | `/color \d` |
| `\e` | Yellow       | `/color \e` |
| `\1` | Dark blue    | `/color \1` |
| `\2` | Green        | `/color \2` |
| `\3` | Dark aqua    | `/color \3` |
| `\4` | Dark red     | `/color \4` |
| `\6` | Gold         | `/color \6` |

### Decoration Effects

| Code | Effect        | Example     |
| ---- | ------------- | ----------- |
| `\l` | Bold          | `/color \l` |
| `\s` | Strikethrough | `/color \s` |
| `\h` | Underline     | `/color \h` |
| `\r` | Reset         | `/color \r` |

### Usage Examples

```yaml
# Basic color change
- /color \6
- This text is gold
- /color \r
- Back to normal color

# Multiple effect combination
- /color \6\l
- Gold and bold text
- /color \r

# Conversation color coding
- /color \2
- Player: Hello!
- /color \3
- NPC: Welcome
- /color \r
```

### Color Usage by Scene

| Scene           | Recommended | Code   |
| --------------- | ----------- | ------ |
| Normal dialogue | Default     | `\r`   |
| Important info  | Gold + Bold | `\6\l` |
| Warning         | Red         | `\c`   |
| Success         | Green       | `\a`   |
| System          | Aqua        | `\b`   |
| Mysterious      | Purple      | `\d`   |

### File-wide Settings

```yaml
# Set default color to reset
color: \r

special_announcement:
  - /color \6\l
  - 【Important Notice】
  - /color \r
  - Normal message

warning_section:
  - /color \c
  - ⚠️ Warning
  - /color \r
  - Content...
```

## 💡 Practical Combinations

### Dramatic Expression

```yaml
dramatic_entrance:
  - /speed 10
  - /color \d
  - A mysterious figure...
  - /wait 60
  - appears!
  - /speed 30
  - /color \r
  - /sound entity.lightning_bolt.thunder 0.5 1.5
```

### Conversation System

```yaml
conversation_start:
  - /sound block.note.bell 1 1.5
  - /speed 25
  - /color \2
  - %player%: Hello
  - /color \3
  - Shopkeeper: Welcome
  - /color \r
```

### System Messages

```yaml
system_message:
  - /color \b
  - /speed 40
  - [System] Game saved
  - /color \r
  - /speed 30
```

## ⚠️ Important Notes

1. **Color Reset**: Properly reset with `\r` for long texts
2. **File Setting Inheritance**: Settings changed within sections don't carry over to next sections
3. **Speed Consideration**: Adjust according to player reading speed

## 🔗 Related Documentation

- **[Reference - Color Codes](../reference/color-codes.md)** - Complete color code list
- **[Audio Control](audio.md)** - More detailed audio control
- **[Game Control](game-control.md)** - wait, auto, skip commands
